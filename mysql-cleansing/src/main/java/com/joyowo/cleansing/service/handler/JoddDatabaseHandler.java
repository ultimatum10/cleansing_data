package com.joyowo.cleansing.service.handler;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.joyowo.cleansing.exception.CleansingException;
import com.joyowo.cleansing.javaBean.dto.DbOomDatabaseHandlerDto;
import com.joyowo.cleansing.javaBean.entity.CleansingTableConfigEntity;
import com.joyowo.cleansing.javaBean.queryBean.InformationSchemaDto;

/**
 * @author lyn
 * @date 2023/8/7
 */
@Component
@Qualifier("joddDatabaseHandler")
public class JoddDatabaseHandler implements DatabaseHandler<DbOomDatabaseHandlerDto> {

    /**
     * 获取表字段信息
     *
     * @return 返回表及字段信息
     */
    @Override
    public Map<String, List<InformationSchemaDto>> getTableColumnMap() throws CleansingException {
        return null;
    }

    /**
     * 查询清洗数据配置表所有数据
     */
    @Override
    public List<CleansingTableConfigEntity> getTableCleansingConfigAll() {
        return null;
    }

    /**
     * 处理清洗数据库数据
     */
    @Override
    public void doCleansingSql(DbOomDatabaseHandlerDto dto) {

    }

    /**
     * 创建表
     *
     * 仅在开启备份时需要实现
     *
     * @param sql 建表sql
     */
    @Override
    public void createTable(String sql) {

    }

    /**
     * 删除表数据
     *
     * 仅在开启备份时需要实现
     *
     * @param name 删除数据的表的信息
     */
    @Override
    public void dropTable(String name) {

    }

    /**
     * 插入数据
     *
     * 仅在开启备份时需要实现
     */
    @Override
    public void batchInsert(String tableName, List<String> values) {

    }

}
