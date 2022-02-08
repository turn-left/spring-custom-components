package cn.enjoy.cache.redis;

import cn.enjoy.cache.Cache;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;

/**
 * @Classname RedisCache
 * @Description TODO
 * @Author Jack
 * Date 2020/11/30 14:48
 * Version 1.0
 */
@Getter
@Setter
public class RedisCache implements Cache {

    private RedisApi redisApi;

    private static String name = "redis";

    public static String getCacheName() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T> T get(Object key) {
        return (T) redisApi.get(JSONObject.toJSONString(key));
    }

    @Override
    public void put(Object key, Object value) {
        redisApi.set(JSONObject.toJSONString(key), JSONObject.toJSONString(value));
    }

    @Override
    public void put(Object key, Object value, int expire) {
        final String keyf = key.toString();
        byte[] keyb = keyf.getBytes();
        byte[] valueb = SerializationUtils.serialize((Serializable) value);
        redisApi.set(keyb, valueb, expire);
    }

    @Override
    public void evict(Object key) {
        final String keyf = key.toString();
        byte[] keyb = keyf.getBytes();
        redisApi.del(keyb);
    }
}
