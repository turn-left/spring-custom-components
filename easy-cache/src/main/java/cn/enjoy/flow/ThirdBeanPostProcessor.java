package cn.enjoy.flow;

public interface ThirdBeanPostProcessor extends BeanPostProcessor {
    <T,K> T handlerResult(K k,T t);
}
