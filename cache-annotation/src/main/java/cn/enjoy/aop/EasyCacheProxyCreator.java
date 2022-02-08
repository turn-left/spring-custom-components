package cn.enjoy.aop;

import cn.enjoy.annotation.EasyCache;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Classname EasyCacheProxyCreator
 * @Description TODO
 * @Author Jack
 * Date 2020/12/1 17:45
 * Version 1.0
 */
public class EasyCacheProxyCreator extends AbstractAdvisorAutoProxyCreator {

    private PointcutAdvisor pointcutAdvisor;

    public PointcutAdvisor getPointcutAdvisor() {
        return pointcutAdvisor;
    }

    public void setPointcutAdvisor(PointcutAdvisor pointcutAdvisor) {
        this.pointcutAdvisor = pointcutAdvisor;
    }

    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {

        Class<?> targetClass = null;
        try {
            targetClass = findTargetClass(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //不关心
        if (!existsAnnotation(new Class[]{targetClass})) {
            return bean;
        }

        if (!AopUtils.isAopProxy(bean)) {
            bean = super.wrapIfNecessary(bean, beanName, cacheKey);
        }
        return bean;
    }

    public static Class<?> findTargetClass(Object proxy) throws Exception {
        if (AopUtils.isAopProxy(proxy)) {
            AdvisedSupport advised = getAdvisedSupport(proxy);
            Object target = advised.getTargetSource().getTarget();
            return findTargetClass(target);
        } else {
            return proxy == null ? null : proxy.getClass();
        }
    }

    public static AdvisedSupport getAdvisedSupport(Object proxy) throws Exception {
        Field h;
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            h = proxy.getClass().getSuperclass().getDeclaredField("h");
        } else {
            h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        }
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return (AdvisedSupport) advised.get(dynamicAdvisedInterceptor);
    }

    private boolean existsAnnotation(Class<?>[] classes) {
        if (classes != null && classes.length > 0) {
            for (Class clazz : classes) {
                if (clazz == null) {
                    continue;
                }
                EasyCache classAnno = (EasyCache) clazz.getAnnotation(EasyCache.class);
                if (classAnno != null) {
                    return true;
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    EasyCache easyCache = method.getAnnotation(EasyCache.class);
                    if (easyCache != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource) {
        return super.getAdvicesAndAdvisorsForBean(beanClass,beanName,targetSource);
//        return new Object[]{getPointcutAdvisor()};
    }
}
