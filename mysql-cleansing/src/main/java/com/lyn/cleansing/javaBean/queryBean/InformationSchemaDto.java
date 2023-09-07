package com.lyn.cleansing.javaBean.queryBean;


import lombok.Data;

/**
 * @author Deolin 2020-07-11
 */
@Data
public class InformationSchemaDto {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段数据类型（例：varchar）
     */
    private String dataType;

    /**
     * 字段类型具体类型（例：varchar(255)）
     */
    private String columnType;

    /**
     * 字段注释
     */
    private String columnComment;

    /**
     * 字段列键
     * PRI：表示该列是表的主键（Primary Key）。
     * UNI：表示该列是一个唯一键（Unique Key）。
     * MUL：表示该列允许有多个相同的值，但不是主键或唯一键。
     */
    private String columnKey;

    /**
     * 字段长度
     */
    private Long characterMaximumLength;

    /**
     * 是否允许为空 值为YES NO
     */
    private String isNullable; // YES NO

    /**
     * 默认值
     */
    private String columnDefault;

}