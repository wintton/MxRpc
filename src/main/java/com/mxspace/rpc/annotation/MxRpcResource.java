package com.mxspace.rpc.annotation;


import com.mxspace.rpc.component.MxRpcClient;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({TYPE, FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(MxRpcClient.class)
public @interface MxRpcResource {
    String name() default "";
    String version() default "";
}
