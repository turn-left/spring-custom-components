package cn.enjoy.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @Classname CacheAdvisor
 * @Description TODO
 * @Author Jack
 * Date 2020/11/28 14:42
 * Version 1.0
 */
public class CacheAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private Pointcut pointcut = new CachePointCut();

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
}
