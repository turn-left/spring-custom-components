package cn.enjoy.config;

import cn.enjoy.cache.CaffeineCacheServiceImpl;
import cn.enjoy.cache.GuavaCacheServiceImpl;
import cn.enjoy.core.CacheUtil;
import cn.enjoy.distributedCache.RedisApi;
import cn.enjoy.distributedCache.RedisCacheServiceImpl;
import cn.enjoy.flow.one.FirstCacheHandler;
import cn.enjoy.flow.three.ThirdCacheHandler;
import cn.enjoy.flow.two.SecondCacheHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @Classname ConfigBeans
 * @Description TODO
 * @Author Jack
 * Date 2020/11/23 15:56
 * Version 1.0
 */
public class ConfigBeans {

    @Bean
    @ConditionalOnProperty(name = "spring.easycache.local.type", havingValue = "caffeine",
            matchIfMissing = true)
    public CaffeineCacheServiceImpl caffeineCacheService() {
        return new CaffeineCacheServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.easycache.local.type", havingValue = "guava",
            matchIfMissing = false)
    public GuavaCacheServiceImpl guavaCacheService() {
        return new GuavaCacheServiceImpl();
    }

    @Bean
    public RedisApi redisApi() {
        return new RedisApi();
    }

    @Bean
    public CacheUtil cacheUtil() {
        return new CacheUtil();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.easycache.distributed.type", havingValue = "redis",
            matchIfMissing = true)
    public RedisCacheServiceImpl redisCacheService() {
        return new RedisCacheServiceImpl();
    }

    @Bean
    public FirstCacheHandler firstCacheHandler() {
        return new FirstCacheHandler();
    }

    @Bean
    public SecondCacheHandler secondCacheHandler() {
        return new SecondCacheHandler();
    }

    @Bean
    public ThirdCacheHandler thirdCacheHandler() {
        return new ThirdCacheHandler();
    }
}
