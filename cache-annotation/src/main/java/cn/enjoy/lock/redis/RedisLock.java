package cn.enjoy.lock.redis;

import cn.enjoy.lock.Lock;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.concurrent.TimeUnit;

/**
 * @Classname RedisLock
 * @Description TODO
 * @Author Jack
 * Date 2020/11/30 15:51
 * Version 1.0
 */
public class RedisLock implements Lock, InitializingBean {

    private String name = "redis";

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Autowired
    private Environment environment;

    private RedissonClient redissonClient;

    private RLock lock;

    @Override
    public void lock(long leaseTime, TimeUnit unit,String key) {
        lock = redissonClient.getLock(key);
        lock.lock(leaseTime,unit);
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + environment.getProperty("spring.easycache.redis.redis_ip") + ":" + environment.getProperty("spring.easycache.redis.redis_port"));
        config.useSingleServer().setPassword(String.valueOf(environment.getProperty("spring.easycache.redis.password")));
        config.useSingleServer().setConnectionPoolSize(Integer.valueOf(environment.getProperty("spring.easycache.redis.max_total")));
        config.useSingleServer().setConnectionMinimumIdleSize(Integer.valueOf(environment.getProperty("spring.easycache.redis.max_idle")));
        redissonClient = Redisson.create(config);
    }
}
