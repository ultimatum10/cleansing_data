package com.lyn.cleansing.javaBean.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 清洗数据表配置
 *
 * @author linkaidi
 * @date 2023/7/28
 */
@Data
@EqualsAndHashCode()
public class CleansingTableConfigEntity {

    /**
     * 主键，请保持唯一
     */
    private Long id;

    /**
     * 表名，你需要清洗数据的表名
     */
    private String tbName;

    /**
     * 清洗数据字段的字段名
     */
    private String cleansingColumn;

    /**
     * 清洗入参数据的字段名，入参数据中对应名字的值会洗到cleansing_column对应的字段中
     */
    private String dataCleansingName;

    /**
     * 查询条件字段名，只有符合条件的数据，会清洗cleansing_column配置的字段数据
     */
    private String conditionColumn;

    /**
     * 入参清洗条件数据的字段名，该名称字段数值会放到查询条件conditionColumn中
     */
    private String dataConditionName;

    /**
     * 执行排序
     */
    private Integer orderNo;

}
