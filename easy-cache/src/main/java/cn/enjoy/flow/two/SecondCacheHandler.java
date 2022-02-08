package cn.enjoy.flow.two;

import cn.enjoy.distributedCache.CacheService;
import cn.enjoy.flow.BeanPostProcessor;
import cn.enjoy.flow.HandlerData;
import cn.enjoy.flow.SecondBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Classname SecondCacheHandler
 * @Description TODO
 * @Author Jack
 * Date 2020/11/23 11:46
 * Version 1.0
 */
public class SecondCacheHandler implements HandlerData, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private CacheService cacheService;

    private HandlerData thirdHandlerData;

    public void setThirdHandlerData(HandlerData thirdHandlerData) {
        this.thirdHandlerData = thirdHandlerData;
    }

    @Override
    public <T, K> T handlerResult(K k) {
        Object rs = cacheService.get(k);
        if(StringUtils.isEmpty(rs)) {
            Object o = thirdHandlerData.handlerResult(k);
            if(!StringUtils.isEmpty(o)) {
                cacheService.put(k,o);
            }
            return (T) o;
        }
        //提供对二级缓存数据修改的扩展
        Map<String, BeanPostProcessor> beansOfType = applicationContext.getBeansOfType(BeanPostProcessor.class);
        for (Map.Entry<String, BeanPostProcessor> s : beansOfType.entrySet()) {
            if(s.getValue() instanceof SecondBeanPostProcessor) {
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
