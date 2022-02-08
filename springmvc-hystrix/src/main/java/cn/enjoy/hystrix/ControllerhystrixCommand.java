package cn.enjoy.hystrix;

import cn.enjoy.annotation.MappingAndCircuit;
import cn.enjoy.aop.CircuitBreakerAdvice;
import com.netflix.hystrix.HystrixCommand;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

/**
 * @Classname ControllerhystrixCommand
 * @Description TODO
 * @Author Jack
 * Date 2020/12/7 20:20
 * Version 1.0
 */
public class ControllerhystrixCommand extends HystrixCommand {

    private MethodInvocation methodInvocation;

    public ControllerhystrixCommand(Setter setter, MethodInvocation methodInvocation) {
//        super(
//                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("hystrixGroupkey"))
//                        .andCommandKey(HystrixCommandKey.Factory.asKey("hystrixCommandKey"))
//                        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
//                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
//                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("jackPoolkey")));
        super(setter);
        this.methodInvocation = methodInvocation;
    }

    @SneakyThrows
    @Override
    protected Object run() throws Exception {
        try {
            return methodInvocation.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }

    @Override
    protected Object getFallback() {
        Class<?> targetClass = null;
        try {
            targetClass = CircuitBreakerAdvice.findTargetClass(methodInvocation.getThis());
            //找到原始method对象
            Method specificMethod = AopUtils.getMostSpecificMethod(methodInvocation.getMethod(), targetClass);
            MappingAndCircuit mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, MappingAndCircuit.class);
            String fallbackMethodStr = mergedAnnotation.fallbackMethod();
            Method method = methodInvocation.getThis().getClass().getMethod(fallbackMethodStr, methodInvocation.getMethod().getParameterTypes());
            return method.invoke(methodInvocation.getThis(), methodInvocation.getArguments());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "getFallback";
    }
}
