package com.rpc.client.transport;

import com.rpc.core.common.RpcResponse;
import com.rpc.core.protocol.MessageProtocol;

/**
 * @Classname NetClientTransport
 * @Description 网络传输层
 * @Date 2021/7/5 15:54
 * @Created by wangchangjiu
 */
public interface NetClientTransport {

    /**
     *  发送数据
     * @param metadata
     * @return
     * @throws Exception
     */
    MessageProtocol<RpcResponse> sendRequest(RequestMetadata metadata) throws Exception;

}
