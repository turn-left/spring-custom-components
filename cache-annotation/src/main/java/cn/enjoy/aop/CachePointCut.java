package cn.enjoy.aop;

import cn.enjoy.annotation.EasyCache;
import cn.enjoy.pojo.EasyCacheBean;
import cn.enjoy.pojo.Lock;
import lombok.Getter;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname CachePointCut
 * @Description TODO
 * @Author Jack
 * Date 2020/11/28 14:45
 * Version 1.0
 */
public class CachePointCut implements Pointcut, MethodMatcher {

    @Getter
    private static final Map<Object, EasyCacheBean> attributeCache = new ConcurrentHashMap<>(1024);

    @Override
    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Object cacheKey = getCacheKey(method, targetClass);
        EasyCacheBean s = attributeCache.get(cacheKey);
        if (s != null) {
            return true;
        }

        //拿原始方法对象，这个方法上才有注解
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        if (AnnotatedElementUtils.hasAnnotation(targetClass, EasyCache.class)
                || AnnotatedElementUtils.hasAnnotation(specificMethod, EasyCache.class)) {
//            System.out.println("method--" + method.hashCode());
//            System.out.println("specificMethod--" + specificMethod.hashCode());
            EasyCache annotation = AnnotationUtils.getAnnotation(specificMethod, EasyCache.class);
            EasyCacheBean easyCacheBean = new EasyCacheBean();
            easyCacheBean.setCacheNames(annotation.cacheNames());
            easyCacheBean.setExpire(annotation.expire());
            easyCacheBean.setKey(annotation.key());
            easyCacheBean.setMethod(specificMethod);
            Lock lockbean = new Lock();
            cn.enjoy.annotation.Lock lock = annotation.lock();
            lockbean.setLock(lock.lock());
            lockbean.setExpire(lock.expire());
            if (StringUtils.isEmpty(lock.key())) {
                lockbean.setKey("lock" + UUID.randomUUID());
            } else {
                lockbean.setKey(lock.key());
            }
            easyCacheBean.setLock(lockbean);
            attributeCache.put(cacheKey, easyCacheBean);
            return true;
        }
/*        Method[] methods = targetClass.getMethods();
        for (Method method1 : methods) {
            if (AnnotatedElementUtils.hasAnnotation(method1, EasyCache.class) && method.getName().equals(method1.getName())) {
                EasyCache annotation = AnnotationUtils.getAnnotation(method1, EasyCache.class);
                EasyCacheBean easyCacheBean = new EasyCacheBean();
                easyCacheBean.setCacheNames(annotation.cacheNames());
                easyCacheBean.setExpire(annotation.expire());
                easyCacheBean.setKey(annotation.key());
                easyCacheBean.setMethod(method1);
                Lock lockbean = new Lock();
                cn.enjoy.annotation.Lock lock = annotation.lock();
                lockbean.setLock(lock.lock());
                lockbean.setExpire(lock.expire());
                if (StringUtils.isEmpty(lock.key())) {
                    lockbean.setKey("lock" + UUID.randomUUID());
                } else {
                    lockbean.setKey(lock.key());
                }
                easyCacheBean.setLock(lockbean);
                attributeCache.put(cacheKey, easyCacheBean);
                return true;
            }
        }*/

/*        if(AnnotatedElementUtils.hasAnnotation(targetClass,EasyCache.class)
                || AnnotatedElementUtils.hasAnnotation(method,EasyCache.class)) {
            EasyCache annotation = AnnotationUtils.getAnnotation(method, EasyCache.class);
            EasyCacheBean easyCacheBean = new EasyCacheBean();
            easyCacheBean.setCacheNames(annotation.cacheNames());
            easyCacheBean.setExpire(annotation.expire());
            easyCacheBean.setKey(annotation.key());
            easyCacheBean.setMethod(method);
            Lock lockbean = new Lock();
            cn.enjoy.annotation.Lock lock = annotation.lock();
            lockbean.setLock(lock.lock());
            lockbean.setExpire(lock.expire());
            if(StringUtils.isEmpty(lock.key())) {
                lockbean.setKey("lock" + UUID.randomUUID());
            } else {
                lockbean.setKey(lock.key());
            }
            easyCacheBean.setLock(lockbean);
            attributeCache.put(targetClass.getName() + ":" + method.getName(),easyCacheBean);
            return true;
        }*/
        return false;
    }

    @Override
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        return false;
    }

    protected static Object getCacheKey(Method method, @Nullable Class<?> targetClass) {
        return new MethodClassKey(method, targetClass);
    }

    public static EasyCacheBean getCacheBean(Method method, @Nullable Class<?> targetClass) {
        return attributeCache.get(getCacheKey(method, targetClass));
    }
}
