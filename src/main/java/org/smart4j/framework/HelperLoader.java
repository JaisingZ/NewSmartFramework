package org.smart4j.framework;


import org.smart4j.framework.core.BeanHelper;
import org.smart4j.framework.core.ClassHelper;
import org.smart4j.framework.core.ControllerHelper;
import org.smart4j.framework.core.IOCHelper;
import org.smart4j.framework.util.ClassLoadUtils;

/**
 * @Author: Jaising
 * @Description: 统一加载Helper类
 * @Date: Created in 3/29/2020 9:51 AM
 */
public final class HelperLoader {

    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IOCHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassLoadUtils.loadClass(cls.getName());
        }
    }
}
