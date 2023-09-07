-- 如果你需要使用数据库配置清洗数据，请新建下表并增加配置
CREATE TABLE `cleansing_table_config` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `tb_name` char(64) COLLATE utf8_bin NOT NULL COMMENT '表名',
    `cleansing_column` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '清洗字段',
    `data_cleansing_name` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '清洗赋值数据字段名称',
    `condition_column_1` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '条件字段1',
    `data_condition_name_1` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '清洗赋值条件字段名称1',
    `condition_column_2` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '条件字段2',
    `data_condition_name_2` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '清洗赋值条件字段名称2',
    `condition_column_3` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '条件字段3',
    `data_condition_name_3` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '清洗赋值条件字段名称3',
    `order_no` int(4) DEFAULT NULL COMMENT '操作排序',
    `delete_flag` tinyint(1) DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='清洗数据表配置';

-- 【说明】
    -- id：主键，请保持唯一
    -- tb_name：表名，你需要清洗数据的表名
    -- cleansing_column：清洗数据的字段名
    -- data_cleansing_name：清洗入参数据的字段名，入参数据中对应名字的值会洗到cleansing_column对应的字段中
    -- condition_column_1：查询条件字段名，只有符合条件的数据，会清洗cleansing_column配置的字段数据
    -- data_condition_name_1：清洗入参条件的字段名，入参条件中对应名字的值会赋值到condition_column对应的字段中
    -- condition_column_2：同上，选填，and方式
    -- data_condition_name_2：同上，选填
    -- condition_column_3：同上，选填
    -- data_condition_name_3：同上，选填
    -- order_no：清洗顺序（0-9999），未配置则按照id顺序清洗数据,未配置时默认优先度为100
    -- delete_flag: 删除标识 0未删除 1已删除