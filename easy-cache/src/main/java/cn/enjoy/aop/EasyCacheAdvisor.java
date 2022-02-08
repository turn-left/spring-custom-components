package cn.enjoy.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

/**
 * @Classname EasyCacheAdvisor
 * @Description TODO
 * @Author Jack
 * Date 2020/11/23 16:54
 * Version 1.0
 */
public class EasyCacheAdvisor extends AbstractPointcutAdvisor {

    private Advice advice;

    private Pointcut pointcut;

    public EasyCacheAdvisor() {
        pointcut = new EasyCachePointcut();
        advice = new EasyCacheAdvice();
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
