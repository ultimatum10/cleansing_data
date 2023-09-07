package com.lyn.cleansing.service.builder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.lyn.cleansing.constant.LogicConstant;
import com.lyn.cleansing.exception.CleansingException;
import com.lyn.cleansing.javaBean.dto.CleansingConfigDto;
import com.lyn.cleansing.javaBean.dto.DbProcessorBuildDto;
import com.lyn.cleansing.javaBean.queryBean.InformationSchemaDto;
import com.lyn.cleansing.utils.JsonUtils;
import com.lyn.cleansing.utils.ProcessorUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 根据数据库字段注释清洗数据
 *
 * 在【需要修改的字段】上增加数据库字段注释，说明如下：
 *
 * 注释格式：#cl([入参修改字段名称（必填）]，[条件字段名称（必填）]，[入参条件字段名称（必填）]，[操作排序（非必填）])
 * 例： #cl(新当前部门id,project_leader_id,员工id,100)
 *
 * @author lyn
 * @date 2023/8/7
 */
@Component
@Slf4j
public class AnnotationDBProcessorBuilder extends BaseDBProcessorBuilder {

    /**
     * 构建数据库清洗处理器
     *
     * @param buildDto 构建执行器参数
     */
    @Override
    public void buildDbProcessor(DbProcessorBuildDto buildDto) throws CleansingException {
        log.info("使用数据库字段注释，构建执行器");

        List<CleansingConfigDto> configDtoList = new ArrayList<>();
        Map<String, List<InformationSchemaDto>> tableMap = buildDto.getTableMap();
        //处理所有的表信息
        for (List<InformationSchemaDto> values : tableMap.values()) {
            List<CleansingConfigDto> list = values.stream()
                    .filter(o -> ProcessorUtils.validColumnComment(o.getColumnComment(), LogicConstant.ANNOTATION_REGEX))
                    .map(this::buildCleansingTableConfigByAnnotation).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(list)) {
                configDtoList.addAll(list);
            }
        }
        buildProcessorByEntity(buildDto, configDtoList);
    }

    /**
     * 根据注解信息，构建清洗配置实体
     */
    private CleansingConfigDto buildCleansingTableConfigByAnnotation(InformationSchemaDto schemaDto) {
        //转化表注释为洗数据实体
        CleansingConfigDto configDto = new CleansingConfigDto();

        String columnComment = schemaDto.getColumnComment();
        int beginIndex = columnComment.indexOf(LogicConstant.ANNOTATION_MARK[0]);
        if (beginIndex > -1) {
            columnComment = columnComment.split(LogicConstant.ANNOTATION_MARK[0])[1];
            int endIndex = columnComment.indexOf(LogicConstant.ANNOTATION_MARK[1]);
            if (endIndex > -1) {
                columnComment = columnComment.split(LogicConstant.ANNOTATION_MARK[1])[0];
                if(StringUtils.isEmpty(columnComment)){
                    log.error("数据异常！！！没有匹配到<#CL></#CL>之间的配置!!!注解内容：{}", JsonUtils.toJson(schemaDto));
                    return new CleansingConfigDto();
                }
                //解析注解
                parseAnnotation(schemaDto, configDto, "<#CL>"+columnComment+"</#CL>");
            } else {
                log.error("数据异常！！！没有匹配到<#CL></#CL>格式!!!注解内容：{}", JsonUtils.toJson(schemaDto));
                return new CleansingConfigDto();
            }
        } else {
            log.error("数据异常！！！没有匹配到<#CL></#CL>格式!!!注解内容：{}", JsonUtils.toJson(schemaDto));
            return new CleansingConfigDto();
        }
        return configDto;
    }

    /**
     * 解析注解
     */
    private void parseAnnotation(InformationSchemaDto schemaDto, CleansingConfigDto configDto, String columnComment) {
        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(LogicConstant.ANNOTATION_PARSE_REGEX);
        // 创建 Matcher 对象
        Matcher matcher = pattern.matcher(columnComment);

        configDto.setTbName(schemaDto.getTableName());
        configDto.setCleansingColumn(schemaDto.getColumnName());
        // 查找匹配项
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                //解析入参清洗字段名称
                configDto.setDataCleansingName(matcher.group(1));
            }
            if (matcher.group(2) != null) {
                //解析条件名称集合
                String conditionStr = matcher.group(2);
                parseCondition(configDto, conditionStr);

            }
            if (matcher.group(3) != null) {
                //解析执行顺序
                configDto.setOrderNo(Integer.valueOf(matcher.group(3)));
            }
        }
        log.info("解析注解后配置数据：{}",JsonUtils.toJson(configDto));
    }

    /**
     * 解析条件字段
     */
    private void parseCondition(CleansingConfigDto configDto, String conditionStr) {
        // 创建 Pattern 对象
        Pattern conditionPattern = Pattern.compile(LogicConstant.ANNOTATION_PARSE_CONDITION_REGEX);
        // 创建 Matcher 对象
        Matcher conditionMatcher = conditionPattern.matcher(conditionStr);
        Map<String, String> keyValueMap = new LinkedHashMap<>();
        while (conditionMatcher.find()) {
            String key = conditionMatcher.group(1);
            String value = conditionMatcher.group(2);
            keyValueMap.put(key, value);
        }

        List<Entry<String, String>> list = new ArrayList(keyValueMap.entrySet());
        configDto.setDataConditionName1(list.get(0).getValue());
        configDto.setConditionColumn1(list.get(0).getKey());
        if(list.size()>1){
            configDto.setDataConditionName2(list.get(1).getValue());
            configDto.setConditionColumn2(list.get(1).getKey());
        }
        if(list.size()>2) {
            configDto.setDataConditionName3(list.get(2).getValue());
            configDto.setConditionColumn3(list.get(2).getKey());
        }
    }

}
