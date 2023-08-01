package com.lyn.cleansing.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.lyn.cleansing.config.CleansingConfig;
import com.lyn.cleansing.constant.LogicConstant;
import com.lyn.cleansing.enums.CleansingTypeEnum;
import com.lyn.cleansing.enums.ProcessorTypeEnum;
import com.lyn.cleansing.handler.processor.BaseCleansingProcessor;
import com.lyn.cleansing.handler.processor.DBCleansingProcessor;
import com.lyn.cleansing.javaBean.dto.DbCleansingProcessDto;
import com.lyn.cleansing.javaBean.entity.CleansingTableConfigEntity;
import com.lyn.cleansing.javaBean.queryBean.InformationSchemaDto;
import com.lyn.cleansing.mapper.CleansingTableConfigMapper;
import com.lyn.cleansing.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 普通处理器
 * 通过数据库配置处理
 *
 * @author ultimatum10
 * @date 2023/7/26
 */
@Service
@Slf4j
public class NormalHandler {

    @Autowired
    private CleansingConfig cleansingConfig;

    @Autowired
    private CleansingTableConfigMapper cleansingTableConfigMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SqlSession sqlSession;

    /**
     * 清理数据
     *
     * @param list 读取需要修改的数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void cleansingMysqlData(List<Map<String, String>> list) throws Exception {
        //配置校验信息打印
        if (CollectionUtils.isEmpty(list)) {
            throw new Exception("入参数据为空，请检查...");
        }

        //测试数据库权限，需要删表建表，增删改查数据库权限

        //获取数据库表字段信息
        Map<String, List<InformationSchemaDto>> tableMap = getTableColumnMap();

        //根据配置走数据库配置或者字段注释
        CleansingTypeEnum cleansingType = cleansingConfig.getCleansingTypeEnum();
        switch (cleansingType) {
            case ANNOTATION:
                //表注释修改数据
                break;
            case DATA:
                log.info("开始数据库配置修改数据");
                cleansingByDbData(list, tableMap);
                break;
            default:
                log.error("清洗数据类型异常,{}", JsonUtils.toJson(cleansingType));
        }

        log.info("处理数据成功");
    }

    /**
     * 根据数据库配置清洗
     *
     * @param list 需要更新进数据库的数据集合
     * @param tableMap 表信息
     */
    private void cleansingByDbData(List<Map<String, String>> list, Map<String, List<InformationSchemaDto>> tableMap)
            throws Exception {

        List<BaseCleansingProcessor> processorList = new ArrayList<>();

        //添加数据库执行器进集合
        addDbProcessor(tableMap, processorList);

        //添加代码执行器进集合
        addLogicProcessor(processorList);

        if (CollectionUtils.isEmpty(processorList)) {
            log.info("没有需要执行的操作，结束");
            return;
        }

        //排序
        processorSort(processorList);

        //备份需要备份数据库表的数据
        List<String> bakTableNames = new ArrayList<>();
        for (BaseCleansingProcessor baseCleansingProcessor : processorList) {
            List<String> bakTable = baseCleansingProcessor.getBakTableNames();
            if (!CollectionUtils.isEmpty(bakTable)) {
                bakTableNames.addAll(bakTable);

            }
        }

        //按顺序操作执行器
        for (BaseCleansingProcessor baseCleansingProcessor : processorList) {
            baseCleansingProcessor.process(list);
        }
    }

    /**
     * 添加逻辑执行器
     */
    private void addLogicProcessor(List<BaseCleansingProcessor> processorList) {

        Map<String, BaseCleansingProcessor> beansOfTypeMap = applicationContext
                .getBeansOfType(BaseCleansingProcessor.class);

        //通过类对象拿到spring容器内的bean
//                BaseCleansingProcessor instance = applicationContext.getBean(clazz);
        if (beansOfTypeMap.size() > 0) {
            Collection<BaseCleansingProcessor> values = beansOfTypeMap.values();
            processorList.addAll(values);
        }
    }

    private void addDbProcessor(Map<String, List<InformationSchemaDto>> tableMap,
            List<BaseCleansingProcessor> processorList) throws Exception {
        //查询配置表
        List<CleansingTableConfigEntity> tableConfigDtoList = cleansingTableConfigMapper.queryAll();

        //数据库配置转化成db执行器
        for (CleansingTableConfigEntity configDto : tableConfigDtoList) {
            //拼接入参
            DbCleansingProcessDto processDto = new DbCleansingProcessDto();
            List<InformationSchemaDto> informationSchemaDtos = tableMap.get(configDto.getTbName());
            if (CollectionUtils.isEmpty(informationSchemaDtos)) {
                throw new Exception("未找到数据库信息");
            }
            processDto.setInformationSchemaDtos(informationSchemaDtos);

            //转化成各个执行器
            DBCleansingProcessor dbCleansingProcessor = new DBCleansingProcessor();
            dbCleansingProcessor.setConfigDto(configDto);
            dbCleansingProcessor.setParam(processDto);
            processorList.add(dbCleansingProcessor);
        }
    }

    /**
     * 执行器排序
     * 排序逻辑：
     * 1.序号orderNo优先，降序处理
     * 2.在1优先的情况下，数据库配置优先
     */
    private void processorSort(List<BaseCleansingProcessor> processorList) {
        Comparator<BaseCleansingProcessor> customComparator = (processor1, processor2) -> {
            // First, compare by orderNo in descending order
            Integer orderNo2 =
                    processor2.getOrderNo() == null ? LogicConstant.DEFAULT_ORDER_NO : processor2.getOrderNo();
            Integer orderNo1 =
                    processor1.getOrderNo() == null ? LogicConstant.DEFAULT_ORDER_NO : processor1.getOrderNo();
            int orderNoComparison = orderNo2.compareTo(orderNo1);
            if (orderNoComparison != 0) {
                return orderNoComparison;
            }

            // If orderNo is the same, compare by ProcessorTypeEnum
            ProcessorTypeEnum type1 = processor1.getType();
            ProcessorTypeEnum type2 = processor2.getType();
            return type1.compareTo(type2);
        };
        Collections.sort(processorList, customComparator);
    }

    /**
     * 获取表字段信息
     *
     * @return 返回表及字段信息
     */
    private Map<String, List<InformationSchemaDto>> getTableColumnMap() throws Exception {

        // 获取数据库名
        String databaseName = sqlSession.getConnection().getCatalog();
        if (StringUtils.isEmpty(databaseName.trim())) {
            throw new Exception("未找到数据库名称...");
        }
        System.out.println("当前连接的数据库名为：" + databaseName);
        List<InformationSchemaDto> informationSchemaDtos = cleansingTableConfigMapper.queryTableInfo(databaseName);
        Map<String, List<InformationSchemaDto>> tableInfos = informationSchemaDtos.stream()
                .collect(Collectors.groupingBy(o -> o.getTableName()));
        return tableInfos;
    }

}
