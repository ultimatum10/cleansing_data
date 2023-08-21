package com.joyowo.cleansing.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import com.joyowo.cleansing.exception.ObjectUtilException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lyn
 * @date 2023/8/8
 */
@Slf4j
public class ObjectUtils {

    /**
     * 对象copy
     */
    public static <T> T copy(Object source, Class<T> target) {
        try {
            if (null != source) {
                T t = target.newInstance();
                BeanUtils.copyProperties(source, t);
                return t;
            }
        } catch (Exception e) {
            log.error("source={} target={}", source, target, e);
            throw new ObjectUtilException(e);
        }
        return null;
    }

    /**
     * 对象copy
     */
    public static <S, T> List<T> copy(List<S> list, Class<T> target) {
        List<T> returnList = new ArrayList<>();
        try {
            if (!CollectionUtils.isEmpty(list)) {
                for (Object source : list) {
                    if (null != source) {
                        T t = target.newInstance();
                        BeanUtils.copyProperties(source, t);
                        returnList.add(t);
                    }
                }
            }
            return returnList;
        } catch (Exception e) {
            log.error("list={} target={}", list, target, e);
            throw new ObjectUtilException(e);
        }
    }

}
