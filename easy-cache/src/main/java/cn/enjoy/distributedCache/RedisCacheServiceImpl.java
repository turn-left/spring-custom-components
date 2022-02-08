package cn.enjoy.distributedCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;

/**
 * @Classname RedisCacheServiceImpl
 * @Description TODO
 * @Author Jack
 * Date 2020/11/23 13:45
 * Version 1.0
 */
public class RedisCacheServiceImpl implements CacheService {

    @Autowired
    private RedisApi redisApi;

    @Override
    public <T, K> T get(K k) {
        final String keyf = k.toString();
        byte[] key = keyf.getBytes();
        byte[] ifPresent = redisApi.get(key);
        return (T) SerializationUtils.deserialize(ifPresent);
    }

    @Override
    public <K, V> V put(K k, V v) {
        final String keyf = k.toString();
        byte[] keyb = keyf.getBytes();
        byte[] valueb = SerializationUtils.serialize((Serializable) v);
        redisApi.set(keyb, valueb);
        return v;
    }
}
