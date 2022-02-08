package cn.enjoy.aop;

import cn.enjoy.annotation.EasyCache;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.reflect.Method;

/**
 * @Classname EasyCachePointcut
 * @Description TODO
 * @Author Jack
 * Date 2020/11/23 16:57
 * Version 1.0
 */
public class EasyCachePointcut extends StaticMethodMatcherPointcut {
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(
                method, EasyCache.class, false, false);
        return attributes != null;
    }
}
