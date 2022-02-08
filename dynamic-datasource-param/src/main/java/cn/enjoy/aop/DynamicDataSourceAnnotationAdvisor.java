package cn.enjoy.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

/**
 * @Classname DynamicDataSourceAnnotationAdvisor
 * @Description TODO
 * @Author Jack
 * Date 2020/11/24 14:40
 * Version 1.0
 */
public class DynamicDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor {

    private Advice advice;

    private Pointcut pointcut;

    public DynamicDataSourceAnnotationAdvisor() {
        advice = new DynamicDataSourceAnnotationInterceptor();
        pointcut = new AnnotationMatchingPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }
}
