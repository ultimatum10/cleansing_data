package com.lyn.cleansing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import com.lyn.cleansing.constant.SqlConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lyn
 * @date 2023/8/10
 */
@Configuration
@Data
@Slf4j
public class BakConfig {

    /**
     * 是否启用老数据入库备份
     *
     * 开启该功能，执行器中BaseCleansingProcessor.getBakTableNames()返回的表名会自动备份
     * 备份表名为原表名后缀增加_bak
     *
     * 值：true/false ，默认false不入库备份
     */
    @Value("${cleansing.config.bakData.enable:false}")
    private Boolean enableData;

    /**
     * 是否启用db执行器数据库表备份
     *
     * 当且仅当cleansing.config.bakData.enable=true时生效
     * 值：true/false ，默认true备份
     */
    @Value("${cleansing.config.bakData.enableDBProcessor:true}")
    private Boolean enableDBProcessorData;

    /**
     * 是否启用源数据入库备份
     *
     * 值：true/false ，默认false不入库备份
     */
    @Value("${cleansing.config.bakSource.enable:false}")
    private Boolean enableBakSource;

    /**
     * 备份入参表名
     *
     * 当配置cleansing.config.bakSource.enable=true时，支持自定义备份入参数据表名
     * 如果没配置，则会默认备份表default_cleansing_table_bak，列名默认为column1，column2...3....
     * 自定义的表名需要和入参List<Map<String,String>> 中每个map.entrySet的size长度相等。否则无法入库
     */
    @Value("${cleansing.config.bakSource.tableName:}")
    private String bakSourceTableName;

    /**
     * 打印备份配置
     */
    public void printConfig() {
        StringBuffer sb = new StringBuffer();

        sb.append("\n备份配置：").append("\n").append("\t是否开启执行器备份：").append(enableData == null ? "" : enableData)
                .append("\n");
        if (enableData != null && enableData) {
            sb.append("\t是否开启默认db执行器备份：").append(enableDBProcessorData).append("\n");
        }
        sb.append("\t是否开启入参源数据备份：").append(enableBakSource == null ? "" : enableBakSource).append("\n");
        if (enableBakSource != null && enableBakSource) {
            if (StringUtils.isEmpty(bakSourceTableName)) {
                sb.append("\t备份入参数据表名：").append(SqlConstant.BAK_SOURCE_DEFAULT_TABLE_NAME).append("\n");
            } else {
                sb.append("\t备份入参数据表名：").append(bakSourceTableName).append("\n");
            }
        }
        log.info(sb.toString());
    }

}
