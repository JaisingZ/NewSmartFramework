package org.smart4j.framework;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.core.BeanHelper;
import org.smart4j.framework.core.ControllerHelper;
import org.smart4j.framework.util.CodecUtils;
import org.smart4j.framework.util.JsonUtils;
import org.smart4j.framework.util.ReflectionUtils;
import org.smart4j.framework.util.StreamUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jaising
 * @Description: 请求转发器
 * @Date: Created in 3/29/2020 10:18 AM
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    /**
     * 初始化
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        HelperLoader.init();
    }

    /**
     * 处理请求转发
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取请求方法和请求路径
        String requestMethod = request.getMethod().toLowerCase();
        String requestPath = request.getPathInfo();
        // 获取Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (null == handler) {
            return;
        }
        // 获取Controller类及其Bean实例
        Class<?> controllerClass = handler.getControllerClass();
        Object controllerBean = BeanHelper.getBean(controllerClass);
        // 创建请求参数对象
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            paramMap.put(paramName, paramValue);
        }
        String body = CodecUtils.decodeURL(StreamUtils.getString(request.getInputStream()));
        if (StringUtils.isNotEmpty(body)) {
            String[] params = body.split("&");
            for (String param : params) {
                String[] kvArray = param.split("=");
                if (ArrayUtils.isNotEmpty(kvArray) && kvArray.length == 2) {
                    String paramName = kvArray[0];
                    String paramValue = kvArray[1];
                    paramMap.put(paramName, paramValue);
                }
            }
        }
        // 调用Action方法
        Param param = new Param(paramMap);
        Method method = handler.getActionMethod();
        Object result = ReflectionUtils.invokeMethod(controllerBean, method, param);
        // 写回浏览器
        if (result instanceof Data) {
            Data data = (Data) result;
            Object model = data.getModel();
            if (model != null) {
                response.setContentType("application/json");
                request.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                String json = JsonUtils.toJson(model);
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }
    }
}
