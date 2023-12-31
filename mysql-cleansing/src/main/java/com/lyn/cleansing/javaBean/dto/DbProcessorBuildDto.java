package com.lyn.cleansing.javaBean.dto;

import java.util.List;
import java.util.Map;
import com.lyn.cleansing.javaBean.queryBean.InformationSchemaDto;
import com.lyn.cleansing.service.processor.BaseCleansingProcessor;
import lombok.Data;

/**
 * @author lyn
 * @date 2023/8/7
 */
@Data
public class DbProcessorBuildDto {

    /**
     * 数据库表信息
     */
    private Map<String, List<InformationSchemaDto>> tableMap;

    /**
     * 执行器集合
     */
    private List<BaseCleansingProcessor> processorList;

}
