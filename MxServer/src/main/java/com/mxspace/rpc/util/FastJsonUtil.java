package com.mxspace.rpc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * fastJson 全局配置
 */
public class FastJsonUtil {

    static {
        ParserConfig globalInstance = ParserConfig.getGlobalInstance();
        globalInstance.setAutoTypeSupport(true);
        globalInstance.addAccept(MxRpcHeartBeat.class.getTypeName());
        globalInstance.addAccept(MxRpcRequest.class.getTypeName());
        globalInstance.addAccept(MxRpcResponse.class.getTypeName());
        globalInstance.addAccept(MxRpcLogin.class.getTypeName());
        globalInstance.addAccept(MxRpcHandleObj.class.getTypeName());
    }


    public static String toJSONString(Object object){
        return JSON.toJSONString(object, SerializerFeature.WriteClassName);
    }

    public static Object parse(String json){
        return JSON.parse(json);
    }
}
