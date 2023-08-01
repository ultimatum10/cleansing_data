#清洗工具
- 作者： ultimatum lyn
- 项目地址：<https://github.com/ultimatum10/cleansing_data>

Version：0.0.1-SNAPSHOT

###目录
- [1.说明](#1.说明)
- [2.适用范围](#2.适用范围)
- [3.使用方式](#3.使用方式)
- [4.风险](#4.风险)
- [5.改进方向](#5.改进方向)

## 1.说明

## 2.适用范围
- 仅支持mysql数据库
- 使用mybatis框架
- 入参数据为表格形式（下文简称入参），入参中的List<Map<String,String>>，list每个元素即为每行数据，map的key为字段名，value为值
- 支持数据库配置更新字段和条件，也支持自定义逻辑更新数据
- 支持清洗数据逻辑排序
## 3.使用方式
1. 配置文件中增加cleansing.config.cleansingType=data(目前仅支持data即数据库配置清洗)
2. 新增数据库表cleansing_table_config，建库脚本详见：src/main/resources/bak-sql/配置建表（可选）.sql
3. 配置cleansing_table_config表
4. 程序主类中@ComponentScan确保扫描到com.lyn.*下的包
5. （可选）如果有自定义的逻辑处理，请实现BaseCleansingProcessor接口
6. 自动注入NormalHandler类，并调用cleansingMysqlData方法
## 4.风险
1. 暂不支持数据库配置和入参之间的类型校验，可能导致执行失败
2. 暂不支持洗数据前，涉及配置相关表的备份功能，可能导致数据丢失
## 5.改进方向
1. 支持数据库字段备注配置洗数据
2. 支出数据库字段类型校验
3. 支持洗数据前备份功能