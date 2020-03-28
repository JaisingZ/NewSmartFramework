package org.smart4j.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Jaising
 * @Description: Bean容器
 * @Date: Created in 3/28/2020 9:48 PM
 */
public class BeanHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanHelper.class);

    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getClassSet();
        for (Class<?> cls : beanClassSet) {
            BEAN_MAP.put(cls, ReflectionUtils.newInstance(cls));
        }
    }

    /**
     * 获取bean映射
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取bean实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can't get bean by class: " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }
}
