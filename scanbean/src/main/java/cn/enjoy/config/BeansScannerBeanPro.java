package cn.enjoy.config;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Classname BeansScannerBeanPro
 * @Description TODO
 * @Author Jack
 * Date 2021/1/10 20:36
 * Version 1.0
 */
public class BeansScannerBeanPro implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @SneakyThrows
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        boolean acceptAllBeans = true;

        Properties properties = PropertiesLoaderUtils.loadAllProperties("application.properties");

        BeanPackageScaner scaner = new BeanPackageScaner(registry);

        String property = properties.getProperty("cn.enjoy.scan.packages");
        List<String> basePackages = new ArrayList<>();
        basePackages.add(property);

        if(acceptAllBeans) {
            scaner.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
                return true;
            });
        }

        scaner.doScan(StringUtils.toStringArray(basePackages));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
