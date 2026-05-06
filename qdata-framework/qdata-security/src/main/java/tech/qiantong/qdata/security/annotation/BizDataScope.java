package tech.qiantong.qdata.security.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizDataScope {

    String code();

    String userField() default "";

    String deptField() default "";

    String tenantField() default "";
}
