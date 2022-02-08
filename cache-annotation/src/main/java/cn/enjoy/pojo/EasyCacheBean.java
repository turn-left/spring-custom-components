package cn.enjoy.pojo;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @Classname EasyCacheBean
 * @Description TODO
 * @Author Jack
 * Date 2020/11/28 16:49
 * Version 1.0
 */
@Data
public class EasyCacheBean {

    private String[] cacheNames;

    private int expire;

    private String key;

    private Method method;

    private Lock lock;
}
