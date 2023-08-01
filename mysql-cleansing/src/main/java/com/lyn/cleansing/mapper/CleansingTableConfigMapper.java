package com.lyn.cleansing.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.lyn.cleansing.javaBean.entity.CleansingTableConfigEntity;
import com.lyn.cleansing.javaBean.queryBean.InformationSchemaDto;

/**
 * 查询清洗配置
 *
 * @Author: linkaidi
 * @Date: 2023/7/31
 */
@Mapper
public interface CleansingTableConfigMapper {

    /**
     * 查询所有清洗表数据配置
     */
    List<CleansingTableConfigEntity> queryAll();

    /**
     * 查询指定表设置信息
     */
    List<InformationSchemaDto> queryTableInfo(@Param("tableSchema") String schema);

}
