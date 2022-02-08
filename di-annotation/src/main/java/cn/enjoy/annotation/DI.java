package cn.enjoy.annotation;

import java.lang.annotation.*;

@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DI {
    String value() default "all";

    String[] serviceIds() default "";

    boolean required() default true;
}
