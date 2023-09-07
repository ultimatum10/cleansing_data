package com.joyowo.cleansing.service.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.joyowo.cleansing.exception.CleansingException;
import com.joyowo.cleansing.javaBean.dto.MybatisDatabaseHandlerDto;
import com.joyowo.cleansing.javaBean.entity.CleansingTableConfigEntity;
import com.joyowo.cleansing.javaBean.queryBean.InformationSchemaDto;
import com.joyowo.cleansing.mapper.BakTableMapper;
import com.joyowo.cleansing.mapper.CleansingTableConfigMapper;
import com.joyowo.cleansing.mapper.CleansingTableMapper;
import com.joyowo.cleansing.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author linkaidi
 * @date 2023/8/7
 */
@Component
@Qualifier("myBatisDatabaseHandler")
@Slf4j
public class MybatisDatabaseHandler implements DatabaseHandler<MybatisDatabaseHandlerDto> {

    @Autowired
    private CleansingTableConfigMapper cleansingTableConfigMapper;

    @Autowired
    private CleansingTableMapper cleansingTableMapper;

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private BakTableMapper bakTableMapper;

    /**
     * 获取表字段信息
     *
     * @return 返回表及字段信息
     */
    @Override
    public Map<String, List<InformationSchemaDto>> getTableColumnMap() throws CleansingException {

        // 获取数据库名
        String databaseName = cleansingTableConfigMapper.getDbName();
        System.out.println("当前连接的数据库名为：" + databaseName);
        List<InformationSchemaDto> informationSchemaDtos = cleansingTableConfigMapper.queryTableInfo(databaseName);
        Map<String, List<InformationSchemaDto>> tableInfos = informationSchemaDtos.stream()
                .collect(Collectors.groupingBy(o -> o.getTableName()));

        if (tableInfos == null || tableInfos.size() == 0) {
            log.error("该数据库无表信息，请检查后再试。异常库名：{}", databaseName);
            throw new CleansingException("数据库无表信息");
        }
        return tableInfos;
    }

    /**
     * 查询清洗数据配置表所有数据
     */
    @Override
    public List<CleansingTableConfigEntity> getTableCleansingConfigAll() {
        List<CleansingTableConfigEntity> tableConfigDtoList = cleansingTableConfigMapper.queryAllConfig();
        return tableConfigDtoList;
    }

    /**
     * 处理清洗数据库数据
     */
    @Override
    public void doCleansingSql(MybatisDatabaseHandlerDto dto) {
        Long time = new Date().getTime();
        List<String> sqlList = dto.getSqlList();
        log.info("执行sql::{}\n{}", time, JsonUtils.toJson(sqlList));
        cleansingTableMapper.cleansingTable(sqlList);
        log.info("执行结束，使用时间:{}ms", new Date().getTime() - time);
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
        bakTableMapper.createDefaultSourceBakTable(sql);
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
        bakTableMapper.dropTable(name);
    }

    /**
     * 插入源数据备份
     *
     * 仅在开启备份时需要实现
     *
     * @param tableName 表名
     * @param values 数值
     */
    public void batchInsert(String tableName, List<String> values) {
        bakTableMapper.batchInsert(tableName, values);
    }

}
