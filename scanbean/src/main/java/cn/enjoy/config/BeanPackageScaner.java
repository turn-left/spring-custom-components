package cn.enjoy.config;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * @Classname BeanPackageScaner
 * @Description TODO
 * @Author Jack
 * Date 2020/11/16 14:41
 * Version 1.0
 */
public class BeanPackageScaner extends ClassPathBeanDefinitionScanner {

    public BeanPackageScaner(BeanDefinitionRegistry registry) {
        super(registry,false);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        return beanDefinitions;
    }
}
