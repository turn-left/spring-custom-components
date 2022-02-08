package cn.enjoy.cache.redis;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class RedisApi implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private JedisPool pool;

    private Properties prop = null;

    @PostConstruct
    public void init() {
        Environment environment = applicationContext.getEnvironment();
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(environment.getProperty("spring.easycache.redis.max_total")));
        config.setMaxIdle(Integer.valueOf(environment.getProperty("spring.easycache.redis.max_idle")));

        config.setMaxWaitMillis(Integer.valueOf(environment.getProperty("spring.easycache.redis.max_wait_millis")));

        config.setTestOnBorrow(Boolean.valueOf(environment.getProperty("spring.easycache.redis.test_on_borrow")));
        config.setTestOnReturn(Boolean.valueOf(environment.getProperty("spring.easycache.redis.test_on_return")));
        config.setTestWhileIdle(Boolean.valueOf(environment.getProperty("spring.easycache.redis.test_while_idle")));
        pool = new JedisPool(config, environment.getProperty("spring.easycache.redis.redis_ip"),
                Integer.valueOf(environment.getProperty("spring.easycache.redis.redis_port")), 100000, String.valueOf(environment.getProperty("spring.easycache.redis.password")));
    }

    public JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Integer.valueOf(prop.getProperty("spring.easycache.redis.max_total")));
            config.setMaxIdle(Integer.valueOf(prop.getProperty("spring.easycache.redis.max_idle")));

            config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("spring.easycache.redis.max_wait_millis")));

            config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("spring.easycache.redis.test_on_borrow")));
            config.setTestOnReturn(Boolean.valueOf(prop.getProperty("spring.easycache.redis.test_on_return")));
            config.setTestWhileIdle(Boolean.valueOf(prop.getProperty("spring.easycache.redis.test_while_idle")));
            pool = new JedisPool(config, prop.getProperty("spring.easycache.redis.redis_ip"),
                    Integer.valueOf(prop.getProperty("spring.easycache.redis.redis_port")), 100000, String.valueOf(prop.getProperty("spring.easycache.redis.password")));
        }

        return pool;
    }

    public void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }

    public Long hdel(String key, String key1) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hdel(key, key1);
        } catch (Exception e) {

        } finally {
            returnResource(pool, jedis);
        }
        return null;
    }

    /**
     * 鑾峰彇鏁版嵁
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {

        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public byte[] get(byte[] key) {
        Jedis jedis = null;
        byte[] value = new byte[0];
        try {
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {

        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    /**
     * set鏁版�?
     */
    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {

            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String set(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {

            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String set(byte[] key, byte[] value,int expire) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setex(key,expire, value);
        } catch (Exception e) {

            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String set(String key, String value, int expire) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.expire(key, expire);
            return jedis.set(key, value);
        } catch (Exception e) {

            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public Long del(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(key);
        } catch (Exception e) {
            return null;
        } finally {
            returnResource(pool, jedis);
        }
    }

    public Long del(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(key);
        } catch (Exception e) {
            return null;
        } finally {
            returnResource(pool, jedis);
        }
    }

    public Long lpush(String key, String... strings) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, strings);
        } catch (Exception e) {

            return 0L;
        } finally {
            returnResource(pool, jedis);
        }
    }

    public List<String> lrange(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, 0, -1);
        } catch (Exception e) {
            return null;
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String hmset(String key, Map map) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmset(key, map);
        } catch (Exception e) {

            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public List<String> hmget(String key, String... strings) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            return jedis.hmget(key, strings);
        } catch (Exception e) {

        } finally {
            returnResource(pool, jedis);
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
