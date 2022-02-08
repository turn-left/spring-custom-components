package cn.enjoy.autoConfiguration;

import cn.enjoy.cache.LocalCacheService;
import cn.enjoy.config.ConfigBeans;
import cn.enjoy.distributedCache.CacheService;
import cn.enjoy.flow.BeanPostProcessor;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 兼容spring工程方式
 * @Classname AutoConfigurationImportSelector
 * @Description TODO
 * @Author Jack
 * Date 2020/11/19 15:52
 * Version 1.0
 */
public class AutoConfigurationImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> localCacheclassNames = SpringFactoriesLoader.loadFactoryNames(LocalCacheService.class, ClassUtils.getDefaultClassLoader());
        List<String> beanPostProcessorclassNames = SpringFactoriesLoader.loadFactoryNames(BeanPostProcessor.class, ClassUtils.getDefaultClassLoader());
        List<String> cacheServiceclassNames = SpringFactoriesLoader.loadFactoryNames(CacheService.class, ClassUtils.getDefaultClassLoader());
        localCacheclassNames.addAll(beanPostProcessorclassNames);
        localCacheclassNames.addAll(cacheServiceclassNames);
        localCacheclassNames.add(ConfigBeans.class.getName());
        return StringUtils.toStringArray(localCacheclassNames);
    }
}
