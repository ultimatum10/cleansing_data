//package com.joyowo.cleansing.demo;
//
//import java.util.List;
//import java.util.Map;
//import org.springframework.stereotype.Service;
//import ProcessorTypeEnum;
//import BaseCleansingProcessor;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 范例
// * 【继承说明】：请确保添加@Component或者@Service，并且确保spring容器管理到该bean
// *
// * @author lyn
// * @date 2023/8/1
// */
//@Service
//@Slf4j
//public class DemoHandler implements BaseCleansingProcessor {
//
////    @Autowired
////    private TestcleansingMapper testcleansingMapper;
//
//
//    /**
//     * 操作顺序，默认100。降序处理。
//     */
//    @Override
//    public Integer getOrderNo() {
//        return 123;
//    }
//
//    /**
//     * 类型
//     * 重写该方法，请返回ProcessorTypeEnum.LOGIC
//     */
//    @Override
//    public ProcessorTypeEnum getType() {
//        return ProcessorTypeEnum.LOGIC;
//    }
//
//    /**
//     * 备份表表名集合
//     * 若有需要备份的表，请返回需要备份的原表名
//     */
//    @Override
//    public List<String> getBakTableNames() {
//        return null;
//    }
//
//    /**
//     * 处理数据逻辑
//     * 该方法外层使用@Transactional注解应用事务，保证所有processor执行的事务一致性
//     *
//     * .如果希望单独控制事务，请设置事务传播子事务级别
//     * ... @Transactional(propagation = Propagation.NESTED)
//     * ... 同时在当前子类上增加@Service注解，并确保spring容器能管理到该bean
//     */
//    @Override
//    public void process(List<Map<String, String>> list) {
////        TestcleansingEntity entity = new TestcleansingEntity();
////        entity.setPerson("mike");
////        entity.setAge(1);
////        entity.setNum(1);
////        entity.setId(123456L);
////        log.info("这里是基础数据::{}", JsonUtils.toJson(list));
////
////        testcleansingMapper.batchInsertSourceBak(entity);
//    }
//
//}
