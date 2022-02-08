package cn.enjoy.mvc;

import org.springframework.context.annotation.ComponentScan;

//不扫描有@Controller注解的类
@ComponentScan(value = "cn.enjoy")
public class SpringContainer {
}
