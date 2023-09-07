package com.lyn.cleansing.constant;

/**
 * @author lyn
 * @date 2023/7/26
 */
public class SqlConstant {

    /**
     * 清洗数据sql
     */
    public static final String UPDATE_SQL = "update %s set %s where %s;";

    /**
     * 参数sql
     */
    public static final String PARAMS_SQL = " %s = %s ";

    /**
     * 参数sql
     */
    public static final String BAK_SOURCE_DEFAULT_TABLE_NAME = "cleansing_source_bak";

    /**
     * 创建默认入参数据备份表
     */
    public static final String BAK_SOURCE_DEFAULT_CREATE_TABLE_SQL =
            "CREATE TABLE `" + BAK_SOURCE_DEFAULT_TABLE_NAME + "` (%s) "
                    + "ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='默认清洗数据入参备份表';";

    /**
     * 创建默认入参数据备份表字段属性
     */
    public static final String BAK_SOURCE_DEFAULT_PARAM_SQL = "  `column_%d` varchar(256) default NULL COMMENT '字段%d'";

}
