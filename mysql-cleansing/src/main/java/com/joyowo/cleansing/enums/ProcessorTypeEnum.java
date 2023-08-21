package com.joyowo.cleansing.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcessorTypeEnum {

    /**
     * sql语句直接处理，（业务无关）
     */
    DB("DB处理"),

    /**
     * java代码逻辑处理，（业务相关）
     */
    LOGIC("代码逻辑处理"),

    ;

    private final String title;

}