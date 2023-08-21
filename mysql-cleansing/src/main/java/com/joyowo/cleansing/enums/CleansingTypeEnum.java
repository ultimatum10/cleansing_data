package com.joyowo.cleansing.enums;

import java.util.Arrays;
import com.joyowo.cleansing.service.builder.AnnotationDBProcessorBuilder;
import com.joyowo.cleansing.service.builder.DatabaseDBProcessorBuilder;
import com.joyowo.cleansing.service.builder.MixDBProcessorBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CleansingTypeEnum {

    ANNOTATION("annotation", "数据库注释方式", AnnotationDBProcessorBuilder.class),

    DATA("data", "数据库表配置方式", DatabaseDBProcessorBuilder.class),

//    MIX("mix", "数据库配置和注释方式", MixDBProcessorBuilder.class),

    ;

    private final String code;

    private final String title;

    private final Class builder;

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