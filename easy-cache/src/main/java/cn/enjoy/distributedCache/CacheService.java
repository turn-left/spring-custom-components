package cn.enjoy.distributedCache;

/**
 * @Classname CacheService
 * @Description TODO
 * @Author Jack
 * Date 2020/11/23 13:45
 * Version 1.0
 */
public interface CacheService {
    <T,K> T get(K k);

    <K,V> V put(K k,V v);
}
