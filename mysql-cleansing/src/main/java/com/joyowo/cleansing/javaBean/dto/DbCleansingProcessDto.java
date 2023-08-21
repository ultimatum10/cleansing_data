package com.joyowo.cleansing.javaBean.dto;

import java.util.List;
import com.joyowo.cleansing.javaBean.queryBean.InformationSchemaDto;
import lombok.Data;

/**
 * @author lyn
 * @date 2023/7/31
 */
@Data
public class DbCleansingProcessDto {

    /**
     * 数据库表字段信息
     */
    private List<InformationSchemaDto> informationSchemaDtos;

}
