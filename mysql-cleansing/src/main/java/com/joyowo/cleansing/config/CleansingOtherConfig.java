package com.joyowo.cleansing.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.joyowo.cleansing.utils.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lyn
 * @date 2023/8/8
 */
@Configuration
@MapperScan("com.joyowo.cleansing.mapper")
@Slf4j
public class CleansingOtherConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    void setApplication() {
        log.info("--------------------init cleansing-data project--------------------");
        ApplicationUtils.setApplicationContext(applicationContext);
    }

}
