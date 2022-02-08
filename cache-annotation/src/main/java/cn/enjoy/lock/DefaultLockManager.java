package cn.enjoy.lock;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname DefaultLockManager
 * @Description TODO
 * @Author Jack
 * Date 2020/11/30 16:25
 * Version 1.0
 */
public class DefaultLockManager implements LockManager, InitializingBean, ApplicationContextAware {
    
    private ApplicationContext applicationContext;

    private ConcurrentHashMap<String,Lock> locks = new ConcurrentHashMap<>();
    
    @Override
    public Lock getLock(String name) {
        return locks.get(name);
    }

    @Override
    public void putLock(String name, Lock lock) {
        locks.put(name,lock);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Lock> lockbeans = applicationContext.getBeansOfType(Lock.class);
        for (Map.Entry<String, Lock> entry : lockbeans.entrySet()) {
            locks.put(entry.getValue().getName(),entry.getValue());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
