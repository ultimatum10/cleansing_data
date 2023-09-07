# 清洗工具

- 作者： ultimatum lyn
- 项目地址：<https://github.com/ultimatum10/cleansing_data>

Version：1.0.1

### 目录

- [1.0.0](#1.0.0)
- [1.0.1](#1.0.1)

- - -

## 1.0.0
- 仅支持mysql数据库
- 使用mybatis框架(暂时，后续可能增加更多)
- 入参数据为表格形式（下文简称**【入参】**），入参中的List<Map<String,String>>，list每个元素即为每行数据，map的key为字段名，value为值
- 支持数据库配置更新字段和条件，也支持自定义逻辑更新数据
- 支持清洗数据逻辑排序
- 支持入参源数据备份
- 支持data和annotation两种配置模式
- - -

## 1.0.1
- 现在会校验数据库执行器之间的依赖先后关系，产生循环或者冲突时，报错。同优先度的执行器，如果有依赖先后关系，会自动排序
- 增加data方式下的cleansing_table_config表的delete_flag字段
- 整理代码，并完善部分说明
- - -