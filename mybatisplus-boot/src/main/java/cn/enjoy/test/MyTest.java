package cn.enjoy.test;

import cn.enjoy.MyBatisPlusApplication;
import cn.enjoy.beans.User;
import cn.enjoy.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Classname MyTest
 * @Description TODO
 * @Author Jack
 * Date 2020/11/17 16:19
 * Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBatisPlusApplication.class)
public class MyTest {

//    @Autowired
//    private UserMapper userMapper;

    @Autowired
    private UserService userService;

//    @Test
//    public void test1() {
//        User user = new User();
//        user.setPassword("sdfasf2");
//        user.setUsername("aaaaaaa2");
//        userMapper.insert(user);
//    }

    @Test
    public void test2() {
        User user = new User();
        user.setPassword("sdfasf1");
        user.setUsername("aaaaaaa1");
        userService.insertUser(user);
    }
}
