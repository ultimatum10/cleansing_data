package com.joyowo.cleansing.service.handler;


import java.util.List;

/**
 * 额外数据库操作集合
 *
 * 适用场景：当你开启了备份功能：
 * 1.需要备份老数据，即cleansing.config.bakData.enable=true时
 * 2.需要源数据备份时，即cleansing.config.bakSource.enable=true时
 *
 * 如果没有上述功能，不需要具体实现下列功能，直接返回null即可
 */
public interface ExtDatabaseHandler {

    /**
     * 创建表
     *
     * 仅在开启备份时需要实现
     * @param sql 建表sql
     */
    void createTable(String sql);

    /**
     * 删除表数据
     *
     * 仅在开启备份时需要实现
     * @param name 删除数据的表的信息
     */
    void dropTable(String name);

    /**
     * 插入数据
     *
     * 仅在开启备份时需要实现
     * @param tableName 插入表名
     * @param values 插入值
     */
    void batchInsert(String tableName, List<String> values);

}
