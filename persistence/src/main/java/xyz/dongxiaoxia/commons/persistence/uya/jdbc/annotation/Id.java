package xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation;

import java.lang.annotation.*;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 17:29
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
    boolean updatable() default false;
    boolean insertable() default false;
}
