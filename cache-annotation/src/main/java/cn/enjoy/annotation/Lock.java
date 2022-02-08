package cn.enjoy.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

    String lock() default "redis";

    int expire() default 60;

    String key() default "";

}
