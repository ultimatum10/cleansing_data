package com.joyowo.cleansing.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.joyowo.cleansing.constant.LogicConstant;
import com.joyowo.cleansing.enums.ProcessorTypeEnum;
import com.joyowo.cleansing.service.processor.BaseCleansingProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * 排序工具类
 *
 * @author lyn
 * @date 2023/8/7
 */
@Slf4j
public class ProcessorUtils {


    /**
     * 执行器排序
     * 排序逻辑：
     * 1.序号orderNo优先，降序处理
     * 2.在1优先的情况下，数据库配置优先
     */
    public static void processorSort(List<BaseCleansingProcessor> processorList) {
        Comparator<BaseCleansingProcessor> customComparator = (processor1, processor2) -> {
            // First, compare by orderNo in descending order
            Integer orderNo2 =
                    processor2.getOrderNo() == null ? LogicConstant.DEFAULT_ORDER_NO : processor2.getOrderNo();
            Integer orderNo1 =
                    processor1.getOrderNo() == null ? LogicConstant.DEFAULT_ORDER_NO : processor1.getOrderNo();
            int orderNoComparison = orderNo2.compareTo(orderNo1);
            if (orderNoComparison != 0) {
                return orderNoComparison;
            }

            // If orderNo is the same, compare by ProcessorTypeEnum
            ProcessorTypeEnum type1 = processor1.getType();
            ProcessorTypeEnum type2 = processor2.getType();
            int typeComparison = type1.compareTo(type2);
            return typeComparison;

        };
        Collections.sort(processorList, customComparator);
    }

    /**
     * 校验字段注解格式
     */
    public static boolean validColumnComment(String columnComment, String regex) {
        //匹配注释格式
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(columnComment);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

}
