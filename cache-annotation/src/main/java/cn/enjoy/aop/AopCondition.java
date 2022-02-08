package cn.enjoy.aop;

import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Classname AopCondition
 * @Description TODO
 * @Author Jack
 * Date 2020/12/2 15:05
 * Version 1.0
 */
public class AopCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String[] beanNamesForType = context.getBeanFactory().getBeanNamesForType(AbstractAdvisorAutoProxyCreator.class);
        if(beanNamesForType.length == 0) {
            return true;
        }
        return false;
    }
}
