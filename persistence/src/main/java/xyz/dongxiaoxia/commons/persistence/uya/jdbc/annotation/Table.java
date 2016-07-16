package xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation;

import java.lang.annotation.*;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 17:21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String name() default "className";
}
