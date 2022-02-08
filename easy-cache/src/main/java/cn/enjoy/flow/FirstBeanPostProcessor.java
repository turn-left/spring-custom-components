package cn.enjoy.flow;

public interface FirstBeanPostProcessor extends BeanPostProcessor {
    int getOrder();
}
