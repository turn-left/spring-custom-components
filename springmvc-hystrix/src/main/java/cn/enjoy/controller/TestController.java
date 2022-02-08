package cn.enjoy.controller;

import cn.enjoy.annotation.MappingAndCircuit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Classname TestController
 * @Description TODO
 * @Author Jack
 * Date 2020/12/5 15:09
 * Version 1.0
 */
@Controller
@MappingAndCircuit("/test")
public class TestController {

    @MappingAndCircuit(value = "test1",fallbackMethod = "test1fallback")
    public @ResponseBody
    String test1() {
        if(true) throw new RuntimeException("s");
        return "test1";
    }

    @MappingAndCircuit("test2")
    public @ResponseBody String test2() {
        return "test2";
    }

    public String test1fallback() {
        System.out.println("test1fallback");
        return "test1fallback";
    }
}
