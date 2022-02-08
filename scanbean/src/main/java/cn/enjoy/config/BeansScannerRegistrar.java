package cn.enjoy.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname BeansScannerRegistrar
 * @Description TODO
 * @Author Jack
 * Date 2020/11/16 14:43
 * Version 1.0
 */
public class BeansScannerRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean acceptAllBeans = true;

        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(BeansScaner.class.getName()));
        BeanPackageScaner scaner = new BeanPackageScaner(registry);

        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            acceptAllBeans = false;
            scaner.addIncludeFilter(new AnnotationTypeFilter(annotationClass));
        }

        List<String> basePackages = new ArrayList<String>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        if(acceptAllBeans) {
            scaner.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
                return true;
            });
        }

        scaner.doScan(StringUtils.toStringArray(basePackages));
    }
}
