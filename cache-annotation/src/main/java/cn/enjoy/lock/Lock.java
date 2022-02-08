package cn.enjoy.lock;

import java.util.concurrent.TimeUnit;

public interface Lock {

    String getName();

    void lock(long leaseTime, TimeUnit unit,String key);

    void unlock();
}
