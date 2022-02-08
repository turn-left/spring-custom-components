package cn.enjoy.pojo;

import lombok.Data;

/**
 * @Classname Lock
 * @Description TODO
 * @Author Jack
 * Date 2020/11/30 16:14
 * Version 1.0
 */
@Data
public class Lock {
    private String lock;

    private int expire;

    private String key;
}
