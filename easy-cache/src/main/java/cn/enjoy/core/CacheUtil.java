package cn.enjoy.core;

import cn.enjoy.flow.one.FirstCacheHandler;
import cn.enjoy.flow.three.ThirdCacheHandler;
import cn.enjoy.flow.two.SecondCacheHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @Classname CacheUtil
 * @Description TODO
 * @Author Jack
 * Date 2020/11/19 14:29
 * Version 1.0
 */
public class CacheUtil<T,K> {

    @Autowired
    private FirstCacheHandler firstCacheHandler;

    @Autowired
    private SecondCacheHandler secondCacheHandler;

    @Autowired
    private ThirdCacheHandler thirdCacheHandler;

    @PostConstruct
    public void init() {
        firstCacheHandler.setSecondHandlerData(secondCacheHandler);
        secondCacheHandler.setThirdHandlerData(thirdCacheHandler);
    }

    public T getCache(K k) {
        return firstCacheHandler.handlerResult(k);
    }
}
