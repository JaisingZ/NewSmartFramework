package org.smart4j.framework.core;

import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Jaising
 * @Description: 加载Controller
 * @Date: Created in 3/29/2020 9:35 AM
 */
public final class ControllerHelper {

    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        for (Class<?> controllerClass : controllerClassSet) {
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {
                // 判断当前方法是否带有Action注解
                if (method.isAnnotationPresent(Action.class)) {
                    Action action = method.getAnnotation(Action.class);
                    String mapping = action.value();
                    // 验证URL规则
                    if (mapping.matches("\\w+:/\\w*")) {
                        String[] mapArray = mapping.split(":");
                        if (mapArray.length == 2) {
                            String requestMethod = mapArray[0];
                            String requestPath = mapArray[1];
                            Request request = new Request(requestMethod, requestPath);
                            Handler handler = new Handler(controllerClass, method);
                            // 初始化ACTION_MAP
                            ACTION_MAP.put(request, handler);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取Handler
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
