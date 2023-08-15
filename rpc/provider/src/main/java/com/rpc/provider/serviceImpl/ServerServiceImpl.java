package com.rpc.provider.serviceImpl;

import com.rpc.api.ServerService;
import com.rpc.server.annotation.RpcService;

/**
 * @author zhaoyubo
 * @title ServerServiceImpl
 * @description 服务端接口实现类
 * @create 2023/8/14 16:25
 **/
@RpcService(interfaceType = ServerService.class, version = "1.0")
public class ServerServiceImpl implements ServerService {
    @Override
    public String helloRPC(String var1) {
        return var1+"-->端口：9991";
    }
}
