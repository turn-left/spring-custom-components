package cn.enjoy.cache;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname DefaultCacheManager
 * @Description TODO
 * @Author Jack
 * Date 2020/11/30 15:14
 * Version 1.0
 */
public class DefaultCacheManager implements CacheManager, InitializingBean, ApplicationContextAware {
    
    private ApplicationContext applicationContext;

    private ConcurrentHashMap<String, Cache> caches = new ConcurrentHashMap();

    @Override
    public Cache getCache(String cacheName) {
        return caches.get(cacheName);
    }

    @Override
    public void putCache(String cacheName, Cache cache) {
        caches.put(cacheName,cache);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Cache> cacheBeans = applicationContext.getBeansOfType(Cache.class);
        for (String s : cacheBeans.keySet()) {
            caches.put(cacheBeans.get(s).getName(),cacheBeans.get(s));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
