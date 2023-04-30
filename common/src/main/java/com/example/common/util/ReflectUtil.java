package com.example.common.util;

import java.io.File;
import java.net.URL;
import java.util.*;



public class ReflectUtil {

    public static String getStackTrace() {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        return stack[stack.length - 1].getClassName();
    }

    public static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        scan(packageName,classes);
        return classes;
    }

    private static void scan(String packageName, List<Class<?>> classes) throws ClassNotFoundException {
        String pathName= packageName.replace('.','/');
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource(pathName);
        File file = new File(url.getFile());
        File[] files = file.listFiles();
        for(File f:files){
            String fileName=f.getName();
            //如果是class文件
            if(fileName.endsWith(".class")){
                //classLoader.loadClass()比Class.forName()快
                Class<?> clazz = classLoader.loadClass(packageName + "." + fileName.substring(0, fileName.length() - 6));
                classes.add(clazz);
            }
            //如果是包
            else{
                scan(packageName+"."+fileName,classes);
            }
        }
    }

}
