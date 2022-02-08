package cn.enjoy.aop;

import cn.enjoy.annotation.EasyCache;
import cn.enjoy.core.CacheUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @Classname EasyCacheAdvice
 * @Description TODO
 * @Author Jack
 * Date 2020/11/23 17:14
 * Version 1.0
 */
public class EasyCacheAdvice implements MethodInterceptor {

    @Autowired
    private CacheUtil cacheUtil;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        EasyCache ec = method.getAnnotation(EasyCache.class);
        String key = getKey(ec.key(),method,invocation.getArguments());
        Object cache = cacheUtil.getCache(key);
        if(StringUtils.isEmpty(cache)) {

        }
        return null;
    }

    public String getKey(String key, Method method,Object[] args) {
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        return ElParser.getKey(key,parameterNames,args);
    }
}
