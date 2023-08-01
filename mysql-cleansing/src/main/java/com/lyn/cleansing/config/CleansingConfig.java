package com.lyn.cleansing.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.lyn.cleansing.enums.CleansingTypeEnum;
import com.lyn.cleansing.utils.ApplicationUtils;
import lombok.Data;

/**
 * 洗数据配置
 *
 * @author linkaidi
 * @date 2023/7/27
 */
@Configuration
@Data
@MapperScan("com.lyn.cleansing.mapper")
public class CleansingConfig {

    /**
     *  洗数据方式
     *  数据库注释 annotation
     *  数据库配置 data
     */
    @Value("${cleansing.config.cleansingType}")
    private String cleansingType;

    @Autowired
    private ApplicationContext applicationContext;

    public CleansingTypeEnum getCleansingTypeEnum(){
        CleansingTypeEnum cleansingTypeEnum = CleansingTypeEnum.of(cleansingType);
        return cleansingTypeEnum;
    }

    @Bean
    void setApplication(){
        ApplicationUtils.setApplicationContext(applicationContext);
    }
}
