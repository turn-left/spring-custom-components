package cn.enjoy.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.time.Duration;

/**
 * @Classname CaffeineCacheServiceImpl
 * @Description TODO
 * @Author Jack
 * Date 2020/11/19 14:49
 * Version 1.0
 */
public class CaffeineCacheServiceImpl implements LocalCacheService, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    Cache<byte[], byte[]> cache = null;

    @Override
    public <T, K> T get(K k) {
        final String keyf = k.toString();
        byte[] key = keyf.getBytes();
        byte[] ifPresent = cache.getIfPresent(key);
        return (T) SerializationUtils.deserialize(ifPresent);
    }

    @Override
    public <K, V> V put(K k, V v) {
        final String keyf = k.toString();
        byte[] keyb = keyf.getBytes();
        byte[] valueb = SerializationUtils.serialize((Serializable) v);
        cache.put(keyb, valueb);
        return v;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Environment environment = applicationContext.getEnvironment();
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(Long.valueOf(environment.getProperty("spring.caffeineCache.expireAfterWrite"))))
                .maximumSize(Long.valueOf(environment.getProperty("spring.caffeineCache.maximumSize")))
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
