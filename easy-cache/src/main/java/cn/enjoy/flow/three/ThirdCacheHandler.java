package cn.enjoy.flow.three;

import cn.enjoy.flow.BeanPostProcessor;
import cn.enjoy.flow.HandlerData;
import cn.enjoy.flow.ThirdBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @Classname ThirdCacheHandler
 * @Description TODO
 * @Author Jack
 * Date 2020/11/23 14:46
 * Version 1.0
 */
public class ThirdCacheHandler implements HandlerData, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public <T, K> T handlerResult(K k) {
        Object rs = null;
        //提供对三级缓存数据修改的扩展 ..可以是掉接口，可以是数据库，自己写类实现ThirdBeanPostProcessor接口写逻辑就可以了，返回数据
        Map<String, BeanPostProcessor> beansOfType = applicationContext.getBeansOfType(BeanPostProcessor.class);
        for (Map.Entry<String, BeanPostProcessor> s : beansOfType.entrySet()) {
            if(s.getValue() instanceof ThirdBeanPostProcessor) {
                rs = ((ThirdBeanPostProcessor)s.getValue()).handlerResult(k,rs);
            }
        }
        return (T) rs;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
