package com.joyowo.cleansing.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.joyowo.cleansing.config.BakConfig;
import com.joyowo.cleansing.constant.LogicConstant;
import com.joyowo.cleansing.constant.SqlConstant;
import com.joyowo.cleansing.exception.CleansingException;
import com.joyowo.cleansing.javaBean.queryBean.InformationSchemaDto;
import com.joyowo.cleansing.other.DbHandlerThreadLocal;
import lombok.extern.slf4j.Slf4j;

/**
 * 备份数据逻辑
 *
 * @author lyn
 * @date 2023/8/7
 */
@Slf4j
@Service
public class BakDataLogic {

    @Autowired
    private BakConfig bakConfig;

    /**
     * 备份源数据
     */
    public void backSourceData(List<Map<String, String>> list) {
        Boolean enableBakSource = bakConfig.getEnableBakSource();
        if (enableBakSource) {
            List<InformationSchemaDto> informationSchemaDtos = null;
            Map<String, List<InformationSchemaDto>> tableMap = null;
            //获取数据库表字段信息
            String bakSourceTableName = bakConfig.getBakSourceTableName();
            boolean useDefaultBak;
            if (StringUtils.isEmpty(bakSourceTableName)) {
                log.info("无默认配置备份源数据表，使用默认备份入参表...");
                useDefaultBak = true;
            } else {
                tableMap = DbHandlerThreadLocal.getDbHandler().getTableColumnMap();
                informationSchemaDtos = tableMap.get(bakSourceTableName);
                if (CollectionUtils.isEmpty(informationSchemaDtos)) {
                    log.info("无默认配置备份源数据表，使用默认备份入参表...");
                    useDefaultBak = true;
                } else {
                    int mapSize = list.get(0).size();
                    int bakTableColumnSize = informationSchemaDtos.size();
                    if (mapSize != bakTableColumnSize) {
                        throw new CleansingException("备份表和入参长度不一致，请检查备份入参表结构");
                    }
                    useDefaultBak = false;
                }
            }

            String bakTableName;
            if (useDefaultBak) {
                bakTableName = SqlConstant.BAK_SOURCE_DEFAULT_TABLE_NAME;
                //先删除表（可能存在的）
                DbHandlerThreadLocal.getDbHandler().dropTable(bakTableName);
                //创建默认源数据备份表
                createDefaultBakTable(list);
            } else {
                bakTableName = bakSourceTableName;
            }

            List<String> values = new ArrayList<>();
            for (Map<String, String> map : list) {
                String valueStr = map.values().stream()
                        //如果值为null，则设置字符串为null
                        .map(o -> o == null ? "\"null\"" : "\""+o+"\"")
                        //拼接成逗号隔开的结构
                        .collect(Collectors.joining(LogicConstant.SPLIT_NODE));
                values.add(valueStr);
            }
            //批量插入数据
            DbHandlerThreadLocal.getDbHandler().batchInsert(bakTableName, values);
            log.info("备份源数据结束...");
        } else {
            log.info("无需备份源数据");
        }
    }

    /**
     * 创建默认的源数据备份表
     */
    private void createDefaultBakTable(List<Map<String, String>> list) {
        //使用默认备份入参表结构
        //建表
        //只要一行，取列数
        int mapSize = list.get(0).size();
        List<String> columnList = new ArrayList<>();
        for (int i = 0; i < mapSize; i++) {
            String sqlParam = String.format(SqlConstant.BAK_SOURCE_DEFAULT_PARAM_SQL, i, i);
            columnList.add(sqlParam);
        }
        String columnStr = columnList.stream().collect(Collectors.joining(LogicConstant.SPLIT_NODE));
        String createTableSql = String.format(SqlConstant.BAK_SOURCE_DEFAULT_CREATE_TABLE_SQL, columnStr);
        DbHandlerThreadLocal.getDbHandler().createTable(createTableSql);
    }

}
