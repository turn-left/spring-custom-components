package cn.enjoy.cache;

import cn.enjoy.aop.AopCondition;
import cn.enjoy.aop.CacheAdvice;
import cn.enjoy.aop.CacheAdvisor;
import cn.enjoy.aop.EasyCacheProxyCreator;
import cn.enjoy.cache.redis.RedisApi;
import cn.enjoy.cache.redis.RedisCache;
import cn.enjoy.lock.DefaultLockManager;
import cn.enjoy.lock.redis.RedisLock;
import cn.enjoy.typeHandler.ListTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Classname CacheAutoconfig
 * @Description TODO
 * @Author Jack
 * Date 2020/11/27 14:47
 * Version 1.0
 */
@Configuration
@PropertySource("classpath:application.properties")
public class CacheAutoconfig {

    @Bean
    public DefaultCacheManager defaultCacheManager() {
        return new DefaultCacheManager();
    }

    @Bean
    public RedisCache redisCache(RedisApi redisApi) {
        RedisCache redisCache = new RedisCache();
        redisCache.setRedisApi(redisApi);
        return redisCache;
    }

    @Bean
    public DefaultLockManager defaultLockManager() {
        return new DefaultLockManager();
    }

    @Bean
    public RedisLock redisLock() {
        return new RedisLock();
    }

    @Bean
    public RedisApi redisApi() {
        return new RedisApi();
    }

    @Bean
//    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheAdvisor cacheAdvisor() {
        CacheAdvisor cacheAdvisor = new CacheAdvisor();
        cacheAdvisor.setAdvice(cacheAdvice());
        return cacheAdvisor;
    }

    @Bean
//    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheAdvice cacheAdvice() {
        CacheAdvice interceptor = new CacheAdvice();
        return interceptor;
    }

    @Bean
    public ListTypeHandler listTypeHandler() {
        return new ListTypeHandler();
    }

    @Bean
    @Conditional(value = AopCondition.class)
    public EasyCacheProxyCreator easyCacheProxyCreator() {
        EasyCacheProxyCreator easyCacheProxyCreator = new EasyCacheProxyCreator();
        easyCacheProxyCreator.setPointcutAdvisor(cacheAdvisor());
        return easyCacheProxyCreator;
    }
}
