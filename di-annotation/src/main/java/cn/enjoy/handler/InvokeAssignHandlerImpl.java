package cn.enjoy.handler;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Classname InvokeHandlerImpl
 * @Description TODO
 * @Author Jack
 * Date 2020/12/4 15:59
 * Version 1.0
 */
public class InvokeAssignHandlerImpl implements InvokeHandler, ApplicationContextAware {
    
    private ApplicationContext applicationContext;
    
    @Override
    public boolean support(String type) {
        return "assign".equalsIgnoreCase(type);
    }

    @Override
    public Object invoke(Object bean, Method method, Object[] args, Field field,String[] serviceIds) {
        for (String serviceId : serviceIds) {
            try {
                method.invoke(applicationContext.getBean(serviceId),args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
