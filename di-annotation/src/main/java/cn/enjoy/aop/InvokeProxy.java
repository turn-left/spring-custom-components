package cn.enjoy.aop;

import cn.enjoy.annotation.DI;
import cn.enjoy.handler.InvokeHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @Classname InvokeProxy
 * @Description TODO
 * @Author Jack
 * Date 2020/12/4 15:13
 * Version 1.0
 */
public class InvokeProxy {
    public static Object newProxy(Field field, ApplicationContext context) {
        return Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(),new Class[]{field.getType()},new InvokeAdvice(field,context));
    }

    private static class InvokeAdvice implements InvocationHandler {

        private Field field;
        
        private ApplicationContext context;

        public InvokeAdvice(Field field,ApplicationContext context) {
            this.field = field;
            this.context = context;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Map<String, InvokeHandler> beansOfType = context.getBeansOfType(InvokeHandler.class);
            AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(field, DI.class);
            String value = attributes.getString("value");
            String[] serviceIds = attributes.getStringArray("serviceIds");
            for (Map.Entry<String, InvokeHandler> entry : beansOfType.entrySet()) {
                if(entry.getValue().support(value)) {
                    entry.getValue().invoke(proxy,method,args,field,serviceIds);
                }
            }
            return null;
        }
    }
}
