package cn.enjoy.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.stereotype.Component;

/**
 * @Classname CircuitBreakerAdvisor
 * @Description TODO
 * @Author Jack
 * Date 2020/12/5 17:29
 * Version 1.0
 */
@Component
public class CircuitBreakerAdvisor extends AbstractPointcutAdvisor {

    private Advice advice;

    private Pointcut pointcut;

    public CircuitBreakerAdvisor() {
        this.advice = new CircuitBreakerAdvice();
        this.pointcut = new CircuitBreakerPointcut();
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
