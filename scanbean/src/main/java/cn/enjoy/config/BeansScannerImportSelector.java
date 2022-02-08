package cn.enjoy.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Classname BeansScannerImportSelector
 * @Description TODO
 * @Author Jack
 * Date 2021/1/10 20:35
 * Version 1.0
 */
public class BeansScannerImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{BeansScannerBeanPro.class.getName()};
    }
}
