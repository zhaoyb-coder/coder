package com.rpc.client.transport;

import com.rpc.client.cache.LocalRpcResponseCache;
import com.rpc.core.common.RpcRequest;
import com.rpc.core.common.RpcResponse;
import com.rpc.core.protocol.MessageProtocol;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Classname NettyNetClientTransport
 * @Description
 * @Date 2021/7/7 14:19
 * @Created by wangchangjiu
 */
@Slf4j
public class NettyNetClientTransport implements NetClientTransport {

    private NettyNetClientPool nettyNetClientPool;

    public NettyNetClientTransport() {
        nettyNetClientPool = NettyNetClientPool.getInstance();
    }

    @Override
    public MessageProtocol<RpcResponse> sendRequest(RequestMetadata metadata) throws Exception {
        MessageProtocol<RpcRequest> protocol = metadata.getProtocol();
        RpcFuture<MessageProtocol<RpcResponse>> future = new RpcFuture<>();
        LocalRpcResponseCache.add(protocol.getHeader().getRequestId(), future);

        // 连接池获取 通道 复用通道
        Channel channel = nettyNetClientPool.getChannel(protocol.getHeader().getRequestId(), metadata.getAddress(), metadata.getPort(), 3);
        channel.writeAndFlush(protocol);

        nettyNetClientPool.release(channel, metadata.getAddress(), metadata.getPort());
        return metadata.getTimeout() != null ? future.get(metadata.getTimeout(), TimeUnit.MILLISECONDS) : future.get();
    }
}
