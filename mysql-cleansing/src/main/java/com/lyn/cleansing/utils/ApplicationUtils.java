package com.lyn.cleansing.utils;

import org.springframework.context.ApplicationContext;

/**
 * @author linkaidi
 * @date 2023/7/31
 */
public class ApplicationUtils {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ApplicationUtils.applicationContext;
    }

}
