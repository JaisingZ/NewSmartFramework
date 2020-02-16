package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author: Jaising
 * @Description: 类加载工具类
 * @Date: Created in 2/16/2020 3:37 PM
 */
public final class ClassLoadUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoadUtils.class);

    private static final String PROTOCOL_FILE = "file";
    private static final String PROTOCOL_JAR = "jar";

    /**
     * 获取当前线程中的类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     * 不执行类的静态代码块，为提高加载类的性能，默认为false
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, false);
    }

    /**
     * 加载类
     * @param isInitialized 是否执行类的静态代码块，为提高加载类的性能，默认为false
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        try {
            return Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("加载类失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定包名下所有类
     * @param packageName   包名
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet();
        try {
            // 将包名转换为文件路径
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replaceAll(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (null == url) {
                    continue;
                }
                String protocol = url.getProtocol();
                if (StringUtils.equals(protocol, PROTOCOL_FILE)) {
                    String packagePath = url.getPath().replaceAll("%20", " ");
                    addClass(classSet, packagePath, packageName);
                } else if (StringUtils.equals(protocol, PROTOCOL_JAR)) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (null == jarURLConnection) {
                        continue;
                    }
                    JarFile jarFile = jarURLConnection.getJarFile();
                    if (null == jarFile) {
                        continue;
                    }
                    Enumeration<JarEntry> jarEntries = jarFile.entries();
                    while (jarEntries.hasMoreElements()) {
                        JarEntry jarEntry = jarEntries.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (jarEntryName.endsWith(".class")) {
                            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                            doAddClass(classSet, className);
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("获取类集合失败", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    /**
     * 递归将所有class文件加载并加入集合中
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        for (File file : files != null ? files : new File[0]) {
            String fileName = file.getName();
            // 文件
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            }
            // 文件夹
            else {
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName)) {
                    subPackageName = packageName + "/" + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    /**
     * 加载类并加入集合中
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className);
        classSet.add(cls);
    }
}
