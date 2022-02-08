package cn.enjoy.aop;


import cn.enjoy.cache.Cache;
import cn.enjoy.cache.CacheManager;
import cn.enjoy.cache.redis.RedisCache;
import cn.enjoy.lock.Lock;
import cn.enjoy.lock.LockManager;
import cn.enjoy.pojo.EasyCacheBean;
import cn.enjoy.spel.ElParser;
import cn.enjoy.typeHandler.TypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Classname CacheAdvice
 * @Description TODO
 * @Author Jack
 * Date 2020/11/28 14:48
 * Version 1.0
 */
@Slf4j
public class CacheAdvice implements MethodInterceptor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private LockManager lockManager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String keyValue = getKeyValue(invocation);
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        EasyCacheBean easyCacheBean = CachePointCut.getCacheBean(invocation.getMethod(),targetClass);
        String[] cacheNames = easyCacheBean.getCacheNames();

        Object cacheResult = getCache(easyCacheBean,keyValue);
        if (!StringUtils.isEmpty(cacheResult)) {
            if(log.isInfoEnabled()) {
                log.info("easyCache : query from redis!");
            }
            return handlerType(invocation,cacheResult);
        }
        Lock lock = null;
        try {
            //缓存为空
            lock = lockManager.getLock(easyCacheBean.getLock().getLock());
            //获取锁的进行后续操作..自旋等待
            lock.lock(easyCacheBean.getLock().getExpire(), TimeUnit.SECONDS, easyCacheBean.getLock().getKey());
            cacheResult = getCache(easyCacheBean,keyValue);
            if (!StringUtils.isEmpty(cacheResult)) {
                return cacheResult;
            }
            //调用被代理方法
            Object result = invocation.proceed();
            if(!StringUtils.isEmpty(result)) {
                if(log.isInfoEnabled()) {
                    log.info("easyCache : query from db!");
                }
                putCache(easyCacheBean,keyValue,result);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    public String getKeyValue(MethodInvocation invocation) {
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        EasyCacheBean easyCacheBean = CachePointCut.getCacheBean(invocation.getMethod(),targetClass);
        //获取到参数名称
        String[] parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(easyCacheBean.getMethod());
        return ElParser.getKey(easyCacheBean.getKey(), parameterNames, invocation.getArguments());
    }

    private Object getCache(EasyCacheBean easyCacheBean,String keyValue) {
        String[] cacheNames = easyCacheBean.getCacheNames();
        if(StringUtils.isEmpty(cacheNames) || cacheNames.length == 0) {
            Cache cache = cacheManager.getCache(RedisCache.getCacheName());
            Object o = cache.get(keyValue);
            if (!StringUtils.isEmpty(o)) {
                return o;
            }
        }

        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            Object o = cache.get(easyCacheBean.getKey());
            if (!StringUtils.isEmpty(o)) {
                return o;
            }
        }
        return null;
    }
    
    private void putCache(EasyCacheBean easyCacheBean,String keyValue,Object result) {
        String[] cacheNames = easyCacheBean.getCacheNames();
        if(StringUtils.isEmpty(cacheNames) || cacheNames.length == 0) {
            Cache cache = cacheManager.getCache(RedisCache.getCacheName());
            cache.put(keyValue,result);
        }
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (!StringUtils.isEmpty(result)) {
                cache.put(keyValue, result);
            }
        }
    }

    private Object handlerType(MethodInvocation invocation,Object result) {
        Map<String, TypeHandler> types = applicationContext.getBeansOfType(TypeHandler.class);
        for (Map.Entry<String, TypeHandler> entry : types.entrySet()) {
            //根据调用的方法的返回值类型，找到该类型的处理类
            if(entry.getValue().support(invocation.getMethod().getGenericReturnType())) {
                return entry.getValue().handler(result,invocation.getMethod().getGenericReturnType());
            }
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
