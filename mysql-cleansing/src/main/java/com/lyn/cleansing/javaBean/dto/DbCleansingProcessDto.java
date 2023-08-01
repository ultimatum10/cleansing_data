package com.lyn.cleansing.javaBean.dto;

import java.util.List;
import com.lyn.cleansing.javaBean.queryBean.InformationSchemaDto;
import lombok.Data;

/**
 * @author ultimatum10
 * @date 2023/7/31
 */
@Data
public class DbCleansingProcessDto {

    /**
     * 数据库表字段信息
     */
    private List<InformationSchemaDto> informationSchemaDtos;

}
