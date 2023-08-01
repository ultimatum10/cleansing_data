package com.lyn.cleansing.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CleansingTypeEnum {

    ANNOTATION("annotation", "数据库注释方式"),

    DATA("data", "数据库表配置方式"),


    ;

    private final String code;

    private final String title;


    /**
     * 获取code对应的枚举
     */
    public static CleansingTypeEnum of(String code) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getCode().equals(code)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "["+getCode()+"-"+getTitle()+"]";
    }

}