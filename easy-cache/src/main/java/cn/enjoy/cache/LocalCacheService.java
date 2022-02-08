package cn.enjoy.cache;

/**
 * @Classname LocalCacheService
 * @Description TODO
 * @Author Jack
 * Date 2020/11/19 14:37
 * Version 1.0
 */
public interface LocalCacheService {

    <T,K> T get(K k);

    <K,V> V put(K k,V v);
}
