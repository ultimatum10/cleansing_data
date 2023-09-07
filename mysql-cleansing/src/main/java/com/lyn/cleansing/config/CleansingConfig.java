package com.lyn.cleansing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.lyn.cleansing.enums.CleansingTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 洗数据配置
 *
 * @author lyn
 * @date 2023/7/27
 */
@Configuration
@Data
@Slf4j
public class CleansingConfig {

    /**
     * 洗数据方式
     * 数据库注释 annotation
     * 数据库配置 data
     * 混合模式 mix 当cleansingType=mix时，cleansingName和conditionName必须配置
     */
    @Value("${cleansing.config.cleansingType}")
    private String cleansingType;

    /**
     * 数据源清洗字段名
     */
    @Value("${cleansing.config.cleansingName:}")
    private String cleansingName;

    /**
     * 数据源清洗条件字段名
     */
    @Value("${cleansing.config.conditionName:}")
    private String conditionName;


    public CleansingTypeEnum getCleansingTypeEnum() {
        CleansingTypeEnum cleansingTypeEnum = CleansingTypeEnum.of(cleansingType);
        return cleansingTypeEnum;
    }


    /**
     * 打印配置
     */
    public void printConfig() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n清洗操作配置：").append("\n").append("\t操作类型：").append(cleansingType == null ? "" : cleansingType)
                .append("\n");
        log.info(sb.toString());
    }

}
