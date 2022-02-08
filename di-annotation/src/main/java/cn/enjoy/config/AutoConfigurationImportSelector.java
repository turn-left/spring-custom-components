package cn.enjoy.config;

import cn.enjoy.handler.InvokeHandler;
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
        List<String> invokeHandlerclassNames = SpringFactoriesLoader.loadFactoryNames(InvokeHandler.class, ClassUtils.getDefaultClassLoader());
        return StringUtils.toStringArray(invokeHandlerclassNames);
    }
}
