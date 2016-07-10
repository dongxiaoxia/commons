package xyz.dongxiaoxia.commons.persistence.uya.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标识Mybatis的DAO，方便{@link org.mybatis.spring.mapper.MapperScannerConfigurer}的扫描
 * Created by chenzhihua on 2016/7/6.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface MyBatisDao {

    String value() default "";
}
