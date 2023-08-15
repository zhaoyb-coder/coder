package com.rpc.client.handler;

import com.rpc.client.common.Constant;
import com.rpc.client.transport.NettyNetClientPool;
import com.rpc.client.cache.LocalRpcResponseCache;
import com.rpc.core.common.RpcResponse;
import com.rpc.core.protocol.MessageProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  数据响应处理器
 * @Author: changjiu.wang
 * @Date: 2021/7/25 15:09
 */
@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcResponse>> {

    private volatile static Map<Integer, Set<Channel>> CORE_CHANNELS = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcResponse> rpcResponseMessageProtocol) throws Exception {
        String requestId = rpcResponseMessageProtocol.getHeader().getRequestId();
        // 收到响应 设置响应数据
        LocalRpcResponseCache.fillResponse(requestId, rpcResponseMessageProtocol);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("Client heartbeat monitoring sending... Channel id:{}",ctx.channel().id());
        Channel channel = ctx.channel();
        if (evt instanceof IdleStateEvent){
            // 当客户端开始发送心跳检测时，说明没有业务请求，释放通道数设定的CORE_CONNECTIONS
            if (channel.isActive()){
                // 使用pool的hash作为key，维护CORE_CONNECTIONS个通道数，多余关闭
                int poolHash = NettyNetClientPool.getInstance().getPoolHash(channel);;
                // 获取poolHash对应的连接集合
                Set<Channel> channels = CORE_CHANNELS.get(poolHash);
                channels = channels == null ? new HashSet<>(Constant.CORE_CONNECTIONS) : channels;
                channels.add(channel);
                if (channels.stream().filter(Channel::isActive).count() > Constant.CORE_CONNECTIONS){
                    log.info("Close exist channel beyond CORE_CONNECTIONS: {}",channel.id());
                    channels.remove(channel);
                    channel.close();
                }
                // 将更新后的连接集合到coreChannel中
                CORE_CHANNELS.put(poolHash,channels);
            }
        } else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
