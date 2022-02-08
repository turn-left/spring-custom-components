package cn.enjoy.aop;

import cn.enjoy.annotation.DS;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

/**
 * @Classname AnnotationMatchingPointcut
 * @Description TODO
 * @Author Jack
 * Date 2020/11/24 14:47
 * Version 1.0
 */
public class AnnotationMatchingPointcut implements Pointcut {

    @Override
    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
                if(AnnotatedElementUtils.hasAnnotation(targetClass,DS.class)
                        || AnnotatedElementUtils.hasAnnotation(specificMethod,DS.class)) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean isRuntime() {
                return false;
            }

            @Override
            public boolean matches(Method method, Class<?> targetClass, Object... args) {
                throw new UnsupportedOperationException("Illegal MethodMatcher usage");
            }
        };
    }
}
