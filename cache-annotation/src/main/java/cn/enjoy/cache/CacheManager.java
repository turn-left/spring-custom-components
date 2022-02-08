package cn.enjoy.cache;

public interface CacheManager {

    Cache getCache(String cacheName);

    void putCache(String cacheName,Cache cache);
}
