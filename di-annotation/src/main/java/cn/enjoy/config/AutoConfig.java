package cn.enjoy.config;

import cn.enjoy.beanPostProcessor.DIAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @Classname AutoConfig
 * @Description TODO
 * @Author Jack
 * Date 2020/12/3 17:24
 * Version 1.0
 */
@Import(AutoConfigurationImportSelector.class)
public class AutoConfig {

    @Bean
    public DIAnnotationBeanPostProcessor diAnnotationBeanPostProcessor() {
        return new DIAnnotationBeanPostProcessor();
    }
}
