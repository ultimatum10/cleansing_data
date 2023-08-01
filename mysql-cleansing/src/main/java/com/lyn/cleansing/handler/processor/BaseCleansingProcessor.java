package com.lyn.cleansing.handler.processor;

import java.util.List;
import java.util.Map;
import com.lyn.cleansing.enums.ProcessorTypeEnum;

/**
 * 清洗数据基础接口
 *
 * 【实现说明】：请确保添加@Component或者@Service，并且确保spring容器管理到该bean
 *
 * @author ultimatum10
 * @date 2023/7/28
 */
public interface BaseCleansingProcessor {


    /**
     * 操作顺序，默认100。降序处理。
     */
    Integer getOrderNo();

    /**
     * 类型
     * 重写该方法，请返回ProcessorTypeEnum.LOGIC
     */
    ProcessorTypeEnum getType();

    /**
     * 备份表表名集合
     * 若有需要备份的表，请返回需要备份的原表名
     */
    List<String> getBakTableNames();

    /**
     * 处理数据逻辑
     * 该方法外层使用@Transactional注解应用事务，保证所有processor执行的事务一致性
     *
     * .如果希望有子事务控制，请设置事务传播级别如下
     * ... @Transactional(propagation = Propagation.NESTED)
     * ... 同时在当前子类上增加@Service注解，并确保spring容器能管理到该bean
     */
    void process(List<Map<String, String>> list) throws Exception;

}
