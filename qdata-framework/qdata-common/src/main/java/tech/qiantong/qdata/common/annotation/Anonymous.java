package tech.qiantong.qdata.common.annotation;

import java.lang.annotation.*;

/**
 * 匿名访问不鉴权注解
 *
 * @author qdata
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Anonymous
{
}
