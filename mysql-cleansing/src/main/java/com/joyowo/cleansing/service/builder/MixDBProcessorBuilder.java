package com.joyowo.cleansing.service.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.joyowo.cleansing.config.CleansingConfig;
import com.joyowo.cleansing.constant.LogicConstant;
import com.joyowo.cleansing.exception.CleansingException;
import com.joyowo.cleansing.javaBean.dto.CleansingConfigDto;
import com.joyowo.cleansing.javaBean.dto.DbProcessorBuildDto;
import com.joyowo.cleansing.javaBean.queryBean.InformationSchemaDto;
import com.joyowo.cleansing.utils.JsonUtils;
import com.joyowo.cleansing.utils.ProcessorUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 混合配置执行器build类
 *
 * @author lyn
 * @date 2023/8/7
 */
@Component
@Slf4j
public class MixDBProcessorBuilder extends BaseDBProcessorBuilder {

    @Autowired
    private CleansingConfig cleansingConfig;

    /**
     * 构建数据库清洗处理器
     *
     * @param buildDto 构建执行器参数
     */
    @Override
    public void buildDbProcessor(DbProcessorBuildDto buildDto) throws CleansingException {
        log.info("使用数据库和注释混合配置，创建执行器");

        List<CleansingConfigDto> configDtoList = new ArrayList<>();
        Map<String, List<InformationSchemaDto>> tableMap = buildDto.getTableMap();
        //处理所有的表信息
        for (List<InformationSchemaDto> values : tableMap.values()) {
            List<CleansingConfigDto> list = values.stream()
                    .filter(o-> ProcessorUtils.validColumnComment(o.getColumnComment(), LogicConstant.MIX_REGEX))
                    .map(o->buildCleansingTableConfigByAnnotation(o)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(list)) {
                configDtoList.addAll(list);
            }
        }
        buildProcessorByEntity(buildDto, configDtoList);
    }

    /**
     * 根据注解信息，构建清洗配置实体
     */
    private CleansingConfigDto buildCleansingTableConfigByAnnotation(InformationSchemaDto schemaDto) throws CleansingException {
        //转化表注释为洗数据实体
        CleansingConfigDto configDto = new CleansingConfigDto();
        String cleansingName = cleansingConfig.getCleansingName();
        String conditionName = cleansingConfig.getConditionName();
        if(StringUtils.isEmpty(cleansingName)||StringUtils.isEmpty(conditionName)){
            throw new CleansingException("mix混合清洗方式，未配置数据源字段名");
        }

        String columnComment = schemaDto.getColumnComment();
        int beginIndex = columnComment.indexOf(LogicConstant.ANNOTATION_MARK[0]);
        if (beginIndex > -1) {
            columnComment = columnComment.substring(beginIndex + 4, columnComment.length() - 1);
            int endIndex = columnComment.indexOf(LogicConstant.ANNOTATION_MARK[1]);
            if (endIndex > -1) {
                columnComment = columnComment.substring(0, endIndex - 1);
                String[] split = columnComment.split(LogicConstant.SPLIT_NODE);
                if (split.length == 1 || split.length == 2) {
                    configDto.setTbName(schemaDto.getTableName());
                    configDto.setCleansingColumn(schemaDto.getColumnName());
                    configDto.setDataCleansingName(cleansingName);
                    configDto.setConditionColumn1(split[0]);
                    configDto.setDataConditionName1(conditionName);
                    if (split.length == 2) {
                        configDto.setOrderNo(Integer.valueOf(split[1]));
                    }
                } else {
                    log.error("数据异常！！！注释内容切割长度异常!!!注解内容：{}", JsonUtils.toJson(schemaDto));
                }
            } else {
                log.error("数据异常！！！没有匹配到#cl()格式!!!注解内容：{}", JsonUtils.toJson(schemaDto));
            }
        } else {
            log.error("数据异常！！！没有匹配到#cl()格式!!!注解内容：{}", JsonUtils.toJson(schemaDto));
        }
        return configDto;
    }

}
