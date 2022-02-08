package cn.enjoy.flow;

public interface HandlerData {
    <T,K> T handlerResult(K k);
}
