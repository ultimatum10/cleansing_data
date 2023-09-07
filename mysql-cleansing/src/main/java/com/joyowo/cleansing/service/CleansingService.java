package com.joyowo.cleansing.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.joyowo.cleansing.config.BakConfig;
import com.joyowo.cleansing.config.CleansingConfig;
import com.joyowo.cleansing.config.CleansingDbConfig;
import com.joyowo.cleansing.enums.CleansingDbTypeEnum;
import com.joyowo.cleansing.logic.BakDataLogic;
import com.joyowo.cleansing.logic.CleansingLogic;
import com.joyowo.cleansing.other.DbHandlerThreadLocal;
import com.joyowo.cleansing.service.handler.DatabaseHandler;
import com.joyowo.cleansing.utils.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 普通处理器
 * 通过数据库配置处理
 *
 * @author linkaidi
 * @date 2023/7/26
 */
@Service
@Slf4j
public class CleansingService {

    @Autowired
    private CleansingLogic cleansingLogic;

    @Autowired
    private CleansingDbConfig cleansingDbConfig;

    @Autowired
    private CleansingConfig cleansingConfig;

    @Autowired
    private BakConfig bakConfig;

    @Autowired
    private BakDataLogic bakDataLogic;

    /**
     * 清理数据
     *
     * @param list 读取需要修改的数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void cleansingMysqlData(List<Map<String, String>> list) throws Exception {
        try {
            if (CollectionUtils.isEmpty(list)) {
                throw new Exception("入参数据为空，请检查...");
            }
            //配置校验信息打印
            printAndValidCleansingConfig();

            //初始化数据库操作handler
            initDbHandler();

            //测试数据库权限，需要删表建表，增删改查数据库权限
            validDBPermission();

            //开始清洗数据
            cleansingLogic.cleansingData(list);

            //保存入参源数据信息
            bakDataLogic.backSourceData(list);

            log.info("处理数据成功");
        } catch (Exception e) {
            throw e;
        } finally {
            //清除数据库操作handler
            DbHandlerThreadLocal.removeDbHandler();
        }
    }

    /**
     * 校验数据库权限
     *
     * 增删改查权限
     */
    private void validDBPermission() {
    }

    /**
     * 打印清洗数据配置
     */
    private void printAndValidCleansingConfig() {
        //打印配置
        cleansingDbConfig.printConfig();
        cleansingConfig.printConfig();
        bakConfig.printConfig();

        //校验cleansing相关配置

    }

    /**
     * 初始化数据库操作handler
     */
    private void initDbHandler() {
        CleansingDbTypeEnum dbTypeEnum = cleansingDbConfig.getCleansingDbTypeEnum();
        DatabaseHandler dbHandler = (DatabaseHandler) ApplicationUtils.getApplicationContext()
                .getBean(dbTypeEnum.getClazz());
        DbHandlerThreadLocal.setDbHandler(dbHandler);
    }

}
