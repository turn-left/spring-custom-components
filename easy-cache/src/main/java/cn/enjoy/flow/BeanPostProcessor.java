package cn.enjoy.flow;

/**
 * @Classname BeanPostProcessor
 * @Description TODO
 * @Author Jack
 * Date 2020/11/19 18:20
 * Version 1.0
 */
public interface BeanPostProcessor {

    <T,K> T handlerResult(T t);
}
