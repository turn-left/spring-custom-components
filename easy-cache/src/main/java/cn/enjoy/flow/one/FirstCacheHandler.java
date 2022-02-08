package cn.enjoy.flow.one;

import cn.enjoy.cache.LocalCacheService;
import cn.enjoy.flow.BeanPostProcessor;
import cn.enjoy.flow.FirstBeanPostProcessor;
import cn.enjoy.flow.HandlerData;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Classname FirstCacheHandler
 * @Description TODO
 * @Author Jack
 * Date 2020/11/23 11:11
 * Version 1.0
 */
public class FirstCacheHandler implements HandlerData, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private LocalCacheService localCacheService;

    private HandlerData secondHandlerData;

    public void setSecondHandlerData(HandlerData secondHandlerData) {
        this.secondHandlerData = secondHandlerData;
    }

    @Override
    public <T, K> T handlerResult(K k) {
        Object rs = localCacheService.get(k);
        if (StringUtils.isEmpty(rs)) {
            Object o = secondHandlerData.handlerResult(k);
            if (!StringUtils.isEmpty(o)) {
                localCacheService.put(k, o);
            }
            return (T) o;
        }
        //提供对二级缓存数据修改的扩展，装饰器
        Map<String, BeanPostProcessor> beansOfType = applicationContext.getBeansOfType(BeanPostProcessor.class);
        for (Map.Entry<String, BeanPostProcessor> s : beansOfType.entrySet()) {
            if (s.getValue() instanceof FirstBeanPostProcessor) {
                rs = s.getValue().handlerResult(rs);
            }
        }
        return (T) rs;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
