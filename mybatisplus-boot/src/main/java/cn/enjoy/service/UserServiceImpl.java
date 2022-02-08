package cn.enjoy.service;

import cn.enjoy.beans.User;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Author Jack
 * Date 2020/11/17 16:29
 * Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Transactional
    @DS("master")
    @Override
    public int insertUser(User user) {
        return 0;
    }
}
