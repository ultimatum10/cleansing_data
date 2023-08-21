package com.joyowo.cleansing.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 清洗数据mapper
 *
 * @Author: lyn
 * @Date: 2023/7/31
 */

@Mapper
public interface CleansingTableMapper {

    void cleansingTable(@Param("sqlList") List<String> sqlList);
}
