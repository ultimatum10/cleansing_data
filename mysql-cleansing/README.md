# 清洗工具

- 作者： lyn

Version：1.0.1

### 目录

- [1.说明](#1.说明)
- [2.适用范围](#2.适用范围)
- [3.使用方式](#3.使用方式)
- [4.风险](#4.风险)
- [5.改进方向](#5.改进方向)
- [6.配置范例](#5.配置范例)

- - -

## 1.说明
1. 由于日常需要进行数据清洗，且清洗操作和业务关系不大，常见的手动编写sql可能因为纰漏疏忽导致脏数据。将此工作自动化处理，降低工作失误概率。
2. 支持自定义业务清洗和清洗顺序排序，方便一些复杂场景的支持。
3. 清洗数据常常需要备份，将备份功能默认进操作过程中
4. 会保存入参源数据，方便查找核对（通常是一个excel或者类似的表格结构数据）

- - -

## 2.适用范围

- 仅支持mysql数据库
- 使用mybatis框架(暂时，后续可能增加更多)
- 入参数据为表格形式（下文简称**【入参】**），入参中的List<Map<String,String>>，list每个元素即为每行数据，map的key为字段名，value为值
- 支持数据库配置更新字段和条件，也支持自定义逻辑更新数据
- 支持清洗数据逻辑排序
- 支持入参源数据备份

- - -

## 3.使用方式

1. application.yml配置文件中增加cleansing相关配置(或者在其他等同功能配置文件中添加)

2. 配置mybatis扫描mapper的xml地址能扫描依赖jar包下的地址 mapper-locations: classpath*:mapper/*.xml

3. （可选）如果cleansingType配置为（data）时
    - 新增数据库表cleansing_table_config，建库脚本详见：src/main/resources/bak-sql/配置建表（可选）.sql
    - 配置cleansing_table_config表

5. （可选）如果cleansingType配置为（annotation或者mix）时
    - 需要对应的数据库增加字段注释配置，格式为(注意大小写)：
        - 注释格式：<#CL>$cl:【入参修改字段名称】（必填）,$cdt:[(【条件字段名称（必填）】,【入参条件字段名称（必填）】)
          ,(【条件字段名称2（可选）】,【入参条件字段名称2（可选）】),(更多条件...)],$order:【操作顺序（可选）】</#CL>
        - 格式标签说明：
            - <#CL></#CL>: 功能标签起始标记
            - $cl:         入参数据list中对应的修改字段中文名称，必填。
            - $cdt:        清洗数据条件内容，至少要填写一组，支持多组，多个条件拼接为and方式。最多支持3组。
            - $order:      执行排序，选填，若未填写，默认优先度为100
        - 范例
            - 例1： <#CL>$cl:新当前部门id,$cdt:[(project_leader_id,员工id)],$order:100</#CL>
            - 例2： <#CL>$cl:新当前部门id,$cdt:[(project_leader_id,员工id),(org_id,老当前部门id)],$order:100</#CL>
            - 例3： <#CL>$cl:新当前部门id,$cdt:[(project_leader_id,员工id),(org_id,老当前部门id)]</#CL>

6. 程序主类中@ComponentScan确保扫描到com.joyowo.*下的包

7. （可选）如果有自定义的逻辑处理，请实现BaseCleansingProcessor接口。详情请参照DemoHandler.java范例模板

8. 自动注入CleansingService类，并调用cleansingMysqlData方法

- - -

## 4.风险

1. 暂不支持数据库配置和入参之间的类型校验，可能导致执行失败，请谨慎配置
2. 本功能中部分sql使用的是占位拼接的方式，可能导致sql注入问题，后续考虑改进，**请自行保证调用方的安全**
3. 暂时未做数据库权限校验，可能导致建表删表失败
4. 数据库清洗存在循环依赖的问题，本版本只能通过指定执行优先级orderNo，来避免数据被清洗后，后续不能使用的问题
- - -

## 5.改进方向

1. 支出数据库字段类型校验
2. 支持洗数据前备份功能
3. 对数据库清洗配置进行循环依赖检测，对互相依赖的sql提供默认优先度执行逻辑

- - -

## 6.nacos配置范例
    #cleansing配置
        cleansing:
            config:
                #洗数据类型（data、annotation，mix）
                cleansingType: data
                #当配置cleansingType为mix时配置
                #数据源清洗数据字段名称
                cleansingName: 新当前部门id
                #数据源条件数据字段名称
                conditionName: 员工id
                #是否开启入参备份
                bakSource:
                  #是否开启入参备份 true开启，false未开启，不配置默认false不开启
                  enable: true
                  自定义备份表名（请注意表字段数需和入参列数相同！！！）
                  tableName:
        db:
            #数据库操作框架 目前支持mybatis和none，如果是none，需要配置数据库链接信息
            type: mybatis
            #数据库主机地址，如果type: mybatis则不需要配置，下同
            url: jdbc:mysql://172.16.16.30:3310?useSSL=false
            #数据库用户名
            userName: JYW_dev
            #数据库密码
            password: Mx%+AfDb2
            #数据库处理库名
            schema: smarthr-bill
            #java链接数据库驱动
            driver: com.mysql.jdbc.Driver

- - -