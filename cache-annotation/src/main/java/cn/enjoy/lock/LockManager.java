package cn.enjoy.lock;

public interface LockManager {
    Lock getLock(String name);

    void putLock(String name,Lock lock);
}
