package org.smart4j.framework.core;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @Author: Jaising
 * @Description: IOC容器
 * @Date: Created in 3/28/2020 10:07 PM
 */
public class IOCHelper {

    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (beanMap != null && beanMap.size() > 0) {
            for (Class<?> beanClass : beanMap.keySet()) {
                Object beanObject = beanMap.get(beanClass);
                // 获取所有Bean Field
                Field[] beanFields = beanClass.getDeclaredFields();
                for (Field beanField : beanFields) {
                    // 判断Bean Field是否含有Inject注解
                    if (beanField.isAnnotationPresent(Inject.class)) {
                        // 在BeanMap中获取Bean Field对应的实例
                        Class<?> beanFieldClass = beanField.getType();
                        Object beanFieldObject = beanMap.get(beanFieldClass);
                        if (beanFieldObject != null) {
                            // 通过反射初始化BeanFiled的值
                            ReflectionUtils.setField(beanObject, beanField, beanFieldObject);
                        }
                    }
                }
            }
        }
    }
}
