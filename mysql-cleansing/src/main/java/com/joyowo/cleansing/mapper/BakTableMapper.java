package com.joyowo.cleansing.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 清洗数据mapper
 *
 * @Author: lyn
 * @Date: 2023/7/31
 */

@Mapper
public interface BakTableMapper {


    /**
     * 创建默认源数据备份表
     */
    int createDefaultSourceBakTable(@Param("sql") String sql);

    /**
     * 批量插入数据
     *
     * @param tableName 表名
     * @param values 数值
     */
    int batchInsert(@Param("tableName") String tableName, @Param("values") List<String> values);

    void dropTable(@Param("tableName") String tableName);

}
