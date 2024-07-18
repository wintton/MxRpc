package com.mxspace.rpc.annotation;

import com.mxspace.rpc.component.MxRpcServer;
import com.mxspace.rpc.enums.ProviderVisitStrategyEnums;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(MxRpcServer.class)
public @interface EnableMxRpcServer {
    ProviderVisitStrategyEnums visitStrategy() default ProviderVisitStrategyEnums.NEXT;
}
