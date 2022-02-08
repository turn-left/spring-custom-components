package cn.enjoy.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @Classname GuavaCacheServiceImpl
 * @Description TODO
 * @Author Jack
 * Date 2020/11/19 14:48
 * Version 1.0
 */
public class GuavaCacheServiceImpl<K,V> implements LocalCacheService, InitializingBean, ApplicationContextAware {

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
        cache = CacheBuilder.newBuilder()
                //设置cache的初始大小为10，要合理设置该值
                .initialCapacity(Integer.valueOf(environment.getProperty("spring.guavaCache.initialCapacity")))
                //设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
                .concurrencyLevel(Integer.valueOf(environment.getProperty("spring.guavaCache.concurrencyLevel")))
                //设置cache中的数据在写入之后的存活时间为10秒
                .expireAfterWrite(Integer.valueOf(environment.getProperty("spring.guavaCache.expireAfterWrite")), TimeUnit.SECONDS)
                //构建cache实例
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
