package xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation;

import java.lang.annotation.*;

/**
 * Created by dongxiaoxia on 2016/7/11.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcedureName {
    public String delete();

    public String insert();

    public String update();

    public String updateByID();

    public String load();

    public String locaByPage();
}
