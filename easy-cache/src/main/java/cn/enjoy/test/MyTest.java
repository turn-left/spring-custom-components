package cn.enjoy.test;

import cn.enjoy.EasyCacheApplication;
import cn.enjoy.cache.LocalCacheService;
import cn.enjoy.core.CacheUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Duration;

/**
 * @Classname MyTest
 * @Description TODO
 * @Author Jack
 * Date 2020/11/23 15:12
 * Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EasyCacheApplication.class)
public class MyTest {

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private LocalCacheService localCacheService;

    @Test
    public void test1() {
        cacheUtil.getCache("12");
    }

    @Test
    public void test2() {
        Cache<Object, Object> build = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofDays(10000L))
                .maximumSize(10000)
                .build();
        build.put("name".getBytes(),"jack");
        System.out.println(build.getIfPresent("name".getBytes()));

    }
}
