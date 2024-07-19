package com.mxspace.rpc.data;

import com.mxspace.rpc.util.MxRpcRequest;
import io.netty.util.internal.StringUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务对象
 */
@Data
public class MxRpcServiceObj {

    /**
     * 提供的服务名称
     */
    private String serviceName;

    /**
     * 访问策略 轮询 IP_HASH 权重
     */
    private Integer visitStrategy = 0;

    /**
     * 服务提供者数量
     */
    private List<MxRpcProviderObj> providerObjList = new ArrayList<>();

    /**
     * 下次使用
     */
    private Integer nextSendIndex = 0;

    /**
     * 权重索引值
     */
    private Integer weightIndex = 0;

    /**
     * 添加服务提供者
     * @param mxRpcProviderObj
     */
    public void addProvider(MxRpcProviderObj mxRpcProviderObj){
        providerObjList.add(mxRpcProviderObj);
    }

    /**
     * 移除服务提供者
     * @param mxRpcProviderObj
     */
    public void removeProvider(MxRpcProviderObj mxRpcProviderObj){
        providerObjList.remove(mxRpcProviderObj);
    }


    /**
     * 访问
     */
    public String visit(MxRpcRequest rpcRequest){
        if (providerObjList.isEmpty()){
            return null;
        }
        MxRpcProviderObj mxRpcProviderObj = getNextProvider(rpcRequest);
        if (mxRpcProviderObj == null){
            return null;
        }
        boolean result = mxRpcProviderObj.sendRequest(rpcRequest);
        return result?mxRpcProviderObj.getCtxId():null;
    }

    /**
     * 获取下个服务提供者
     * @param rpcRequest
     * @return
     */
    private MxRpcProviderObj getNextProvider(MxRpcRequest rpcRequest) {
        String serviceVersion = rpcRequest.getServiceVersion();
        List<MxRpcProviderObj> providerObjList = this.providerObjList;
        if (!StringUtil.isNullOrEmpty(serviceVersion)){
            providerObjList = providerObjList.stream()
                    .filter(item -> item.getServiceVersion().equalsIgnoreCase(rpcRequest.getServiceVersion()))
                    .collect(Collectors.toList());
        }
        MxRpcProviderObj mxRpcProviderObj = null;
        switch (visitStrategy){
            case 0:
                if (providerObjList.isEmpty()){
                    break;
                }
                //轮询
                if (nextSendIndex >= providerObjList.size()){
                    nextSendIndex = 0;
                }
                mxRpcProviderObj = providerObjList.get(nextSendIndex);
                nextSendIndex++;
                break;
            case 1:
                //HASH
                String requestId = rpcRequest.getRequestId();
                int index = requestId.hashCode() % providerObjList.size();
                if (index >= providerObjList.size()){
                    index = 0;
                }
                mxRpcProviderObj = providerObjList.get(index);
                break;
            case 2:
                //权重
                int sum = 0;
                for (MxRpcProviderObj rpcProviderObj : providerObjList) {
                    sum += rpcProviderObj.getWeight();
                    if (weightIndex <= sum){
                        mxRpcProviderObj = rpcProviderObj;
                        weightIndex++;
                        break;
                    }
                }
                if (mxRpcProviderObj == null){
                    mxRpcProviderObj = providerObjList.get(0);
                    weightIndex = 0;
                }
                break;
        }
        return mxRpcProviderObj;
    }

}
