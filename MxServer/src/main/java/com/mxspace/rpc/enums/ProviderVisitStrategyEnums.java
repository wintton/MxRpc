package com.mxspace.rpc.enums;

/**
 * 访问策略枚举对象
 */
public enum ProviderVisitStrategyEnums {

    NEXT(0,"轮询"),
    HASH(1,"HASH"),
    WEIGHT(2,"权重"),
    ;

    /**
     * 编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;

    ProviderVisitStrategyEnums(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
