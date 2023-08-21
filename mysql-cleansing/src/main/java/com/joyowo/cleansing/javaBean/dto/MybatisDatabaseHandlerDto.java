package com.joyowo.cleansing.javaBean.dto;

import java.util.List;
import lombok.Data;

/**
 * @author lyn
 * @date 2023/8/7
 */
@Data
public class MybatisDatabaseHandlerDto extends DatabaseHandlerDto{

    /**
     * 数据库操作语句集合
     */
    private List<String> sqlList;

}
