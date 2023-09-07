package com.lyn.cleansing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.lyn.cleansing.enums.CleansingDbTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库配置
 * cleansing.db.type!=null的时候，数据库信息必须配置
 *
 * @author lyn
 * @date 2023/8/7
 */
@Configuration
@Data
@Slf4j
public class CleansingDbConfig {

    /**
     * 数据库配置
     */
    @Value("${cleansing.db.type}")
    private String cleansingDbType;

    /**
     * 数据库连接
     */
    @Value("${cleansing.db.url:}")
    private String url;

    /**
     * 数据库用户名
     */
    @Value("${cleansing.db.userName:}")
    private String userName;

    /**
     * 数据库密码
     */
    @Value("${cleansing.db.password:}")
    private String password;

    /**
     * 指定schema
     */
    @Value("${cleansing.db.schema:}")
    private String schema;

    /**
     * 驱动
     */
    @Value("${cleansing.db.driver:}")
    private String driver;

    public CleansingDbTypeEnum getCleansingDbTypeEnum() {
        CleansingDbTypeEnum dbTypeEnum = CleansingDbTypeEnum.of(cleansingDbType);
        return dbTypeEnum;
    }

    /**
     * 打印数据库配置
     */
    public void printConfig() {
        StringBuffer sb = new StringBuffer();
        CleansingDbTypeEnum cleansingDbTypeEnum = getCleansingDbTypeEnum();
        sb.append("\n清洗数据库配置：").append("\n").append("\t数据库操作类型：").append(cleansingDbType == null ? "" : cleansingDbType)
                .append("\n");
        if (CleansingDbTypeEnum.NONE.equals(cleansingDbTypeEnum)) {
            sb.append("\thost地址url：").append(cleansingDbTypeEnum.getCode()).append("\n").append("\t用户名userName：")
                    .append(userName).append("\n").append("\t密码password：").append(password).append("\n")
                    .append("\t库名schema：").append(schema).append("\n").append("\t驱动driver：").append(driver)
                    .append("\n");
        }
        log.info(sb.toString());
    }

}
