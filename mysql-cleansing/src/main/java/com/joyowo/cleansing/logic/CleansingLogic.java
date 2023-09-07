package com.joyowo.cleansing.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.joyowo.cleansing.config.CleansingConfig;
import com.joyowo.cleansing.enums.CleansingTypeEnum;
import com.joyowo.cleansing.enums.ProcessorTypeEnum;
import com.joyowo.cleansing.exception.CleansingException;
import com.joyowo.cleansing.javaBean.dto.DbProcessorBuildDto;
import com.joyowo.cleansing.javaBean.queryBean.InformationSchemaDto;
import com.joyowo.cleansing.other.DbHandlerThreadLocal;
import com.joyowo.cleansing.service.builder.BaseDBProcessorBuilder;
import com.joyowo.cleansing.service.processor.BaseCleansingProcessor;
import com.joyowo.cleansing.utils.ProcessorUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 清洗数据逻辑
 *
 * @author linkaidi
 * @date 2023/8/7
 */
@Slf4j
@Service
public class CleansingLogic {

    @Autowired
    private CleansingConfig cleansingConfig;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 根据数据库配置清洗
     *
     * @param list 需要更新进数据库的数据集合
     */
    public void cleansingData(List<Map<String, String>> list) {

        if (CollectionUtils.isEmpty(list)) {
            log.info("源数据为为空，结束...");
            return;
        }

        //执行器集合
        LinkedList<BaseCleansingProcessor> processorList = new LinkedList<>();

        //添加数据库执行器进集合
        addDbProcessor(processorList);

        //添加代码执行器进集合
        addLogicProcessor(processorList);

        if (!CollectionUtils.isEmpty(processorList)) {
            //排序
            ProcessorUtils.processorSort(processorList);

            //条件循环判定，防止被修改后，条件where语句找不到
            ProcessorUtils.conditionCycleValid(processorList);

            //备份需要备份数据库表的数据
            bakTable(processorList);

            //按顺序操作执行器
            for (BaseCleansingProcessor baseCleansingProcessor : processorList) {
                baseCleansingProcessor.process(list);
            }
        } else {
            log.info("没有需要执行的操作，结束流程...");
        }

    }

    /**
     * 备份需要备份数据库表的数据
     */
    private void bakTable(List<BaseCleansingProcessor> processorList) {
        List<String> bakTableNames = new ArrayList<>();
        for (BaseCleansingProcessor baseCleansingProcessor : processorList) {
            List<String> bakTable = baseCleansingProcessor.getBakTableNames();
            if (!CollectionUtils.isEmpty(bakTable)) {
                bakTableNames.addAll(bakTable);

            }
        }
    }

    /**
     * 添加数据库执行器
     *
     * @param processorList 执行器集合
     */
    private void addDbProcessor(List<BaseCleansingProcessor> processorList) throws CleansingException {
        log.info("开始添加数据库处理器");

        //获取数据库表字段信息
        Map<String, List<InformationSchemaDto>> tableMap = DbHandlerThreadLocal.getDbHandler().getTableColumnMap();

        //根据配置走数据库配置或者字段注释
        CleansingTypeEnum cleansingType = cleansingConfig.getCleansingTypeEnum();
        if (cleansingType == null) {
            throw new CleansingException("添加数据库执行器处理异常");
        }
        BaseDBProcessorBuilder builder = (BaseDBProcessorBuilder) applicationContext
                .getBean(cleansingType.getBuilder());
        DbProcessorBuildDto buildDto = new DbProcessorBuildDto();
        buildDto.setTableMap(tableMap);
        buildDto.setProcessorList(processorList);

        builder.buildDbProcessor(buildDto);
    }


    /**
     * 添加逻辑执行器
     */
    private void addLogicProcessor(List<BaseCleansingProcessor> processorList) {
        log.info("开始添加逻辑处理器");
        Map<String, BaseCleansingProcessor> beansOfTypeMap = applicationContext
                .getBeansOfType(BaseCleansingProcessor.class);

        if (beansOfTypeMap.size() > 0) {
            Collection<BaseCleansingProcessor> values = beansOfTypeMap.values().stream()
                    .filter(o -> ProcessorTypeEnum.LOGIC.equals(o.getType())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(values)) {
                processorList.addAll(values);
                log.info("添加逻辑执行器数量：{}", values.size());
            } else {
                log.info("无自定义逻辑处理执行器");
            }
        } else {
            log.info("无处理执行器");
        }
        log.info("当前执行器数量：{}", processorList.size());
    }

}
