package cn.enjoy.test;

import cn.enjoy.beans.User;
import cn.enjoy.mapper.UserMapper;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Classname MyTest
 * @Description TODO
 * @Author Jack
 * Date 2020/11/17 16:19
 * Version 1.0
 */
public class MyTest {

    @Test
    public void test1() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("cn.enjoy");
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        User user = new User();
        user.setPassword("sdfasf");
        user.setUsername("aaaaaaa");
        userMapper.insert(user);
    }
}
