package com.joyowo.cleansing.enums;

import java.util.Arrays;
import com.joyowo.cleansing.service.handler.JoddDatabaseHandler;
import com.joyowo.cleansing.service.handler.MybatisDatabaseHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  数据库操作类型
 *
 */
@Getter
@AllArgsConstructor
public enum CleansingDbTypeEnum {

    NONE("none", "无自带数据库", JoddDatabaseHandler.class),

    MYBATIS("mybatis", "mybatis框架", MybatisDatabaseHandler.class),


    ;

    private final String code;

    private final String title;

    private final Class clazz;

    /**
     * 获取code对应的枚举
     */
    public static CleansingDbTypeEnum of(String code) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getCode().equals(code)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "["+getCode()+"-"+getTitle()+"]";
    }

}