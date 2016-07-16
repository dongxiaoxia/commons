package xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation;

import java.lang.annotation.*;

/**
 * Created by dongxiaoxia on 2016/7/11.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcedureName {
    String delete();

    String insert();

    String update();

    String updateByID();

    String load();

    String locaByPage();
}
