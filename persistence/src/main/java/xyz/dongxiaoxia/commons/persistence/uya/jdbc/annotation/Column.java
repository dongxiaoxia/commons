package xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation;

import java.lang.annotation.*;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 17:33
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    String name() default "fieldName";

    String setFuncName() default "setField";

    String getFuncName() default "getField";

    boolean defaultDBValue() default false;
}
