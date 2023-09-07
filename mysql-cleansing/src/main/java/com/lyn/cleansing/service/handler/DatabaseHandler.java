package com.lyn.cleansing.service.handler;


import java.util.List;
import java.util.Map;
import com.lyn.cleansing.exception.CleansingException;
import com.lyn.cleansing.javaBean.entity.CleansingTableConfigEntity;
import com.lyn.cleansing.javaBean.queryBean.InformationSchemaDto;

/**
 * 数据库操作集合
 */
public interface DatabaseHandler<T> extends  ExtDatabaseHandler{

    /**
     * 获取表字段信息
     *
     * @return 返回表及字段信息
     */
    Map<String, List<InformationSchemaDto>> getTableColumnMap() throws CleansingException;

    /**
     * 查询清洗数据配置表所有数据
     */
    List<CleansingTableConfigEntity> getTableCleansingConfigAll();

    /**
     * 处理清洗数据库数据
     */
    void doCleansingSql(T t);

}
