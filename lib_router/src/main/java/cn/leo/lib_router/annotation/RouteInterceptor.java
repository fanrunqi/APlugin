package cn.leo.lib_router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/20 14:52
 * description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RouteInterceptor {
    /**
     * page path.
     */
    String[] value();
}
