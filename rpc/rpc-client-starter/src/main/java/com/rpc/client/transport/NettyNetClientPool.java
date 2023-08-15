package com.rpc.client.transport;

import com.rpc.core.exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Classname NettyNetClientPool
 * @Description Netty pool
 * @Date 2023/5/24 16:46
 * @Created by wangchangjiu
 */
public class NettyNetClientPool {

    // volatile用来确保将变量的更新操作通知到其他线程。
    private volatile static NettyNetClientPool nettyNetClientPool;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * key为目标主机的InetSocketAddress对象，value为目标主机对应的连接池
     * InetSocketAddress可以为ip+port,也可以为hostname+port
     * FixedChannelPool:ChannelPool，可以强制保持一个最大的连接并发
     */

    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    private final Bootstrap bootstrap = new Bootstrap();

    private volatile static Map<InetSocketAddress, FixedChannelPool> pools = new HashMap<>();

    private NettyNetClientPool() {
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(1024 * 2, 1024 * 10, 1024 * 1024));
    }

    public static NettyNetClientPool getInstance() {
        if (nettyNetClientPool == null) {
            // 同步操作，即加锁
            synchronized (NettyNetClientPool.class) {
                // 为了避免多次初始化，此处又重新做了一次null值判断
                if (nettyNetClientPool == null) {
                    nettyNetClientPool = new NettyNetClientPool();
                }
            }
        }
        return nettyNetClientPool;
    }


    /**
     * 从缓存中获取
     *
     * @param address
     * @return
     */
    private FixedChannelPool getFixedChannelPool(InetSocketAddress address) {
        FixedChannelPool fixedChannelPool = pools.get(address);
        if (fixedChannelPool == null) {
            synchronized (address) {
                fixedChannelPool = pools.get(address);
                if (fixedChannelPool == null) {
                    fixedChannelPool = new FixedChannelPool(bootstrap.remoteAddress(address), new NettyChannelPoolHandler(), 2, 2);
                    pools.put(address, fixedChannelPool);
                }
            }
        }
        return fixedChannelPool;
    }

    /**
     *  功能描述：
     *  从pool中取出channel
     * @param requestId
     * @param hostname
     * @param port
     * @param retry
     * @return
     */
    public Channel getChannel(String requestId, String hostname, int port, int retry) {
        InetSocketAddress address = new InetSocketAddress(hostname, port);
        Channel channel;
        try {
            // 根据address获取对应的连接池
            FixedChannelPool pool = getFixedChannelPool(address);
            // 从连接池获取连接
            Future<Channel> future = pool.acquire();
            channel = future.get();

            AttributeKey<String> REQUEST_ID = AttributeKey.valueOf("requestId");
            channel.attr(REQUEST_ID).set(requestId);
        } catch (ExecutionException e) {
            //如果是因为服务端挂掉，连接失败而获取不到channel，重试
            logger.info(e.getMessage());
            // 每个池子尝试获取2次
            if (retry > 0) {
                return getChannel(requestId, hostname, port, retry - 1);
            } else {
                logger.error("Peer Server address {} Error", address);
                throw new RpcException("Server Error");
            }
        } catch (Exception e) {
            logger.error("Peer Server address {} Error", address);
            throw new RpcException("Server Error");
        }
        return channel;
    }

    /**
     * 回收channel进池，需要保证随机值和getChannel获取到的随机值是同一个，才能从同一个pool中释放资源
     *
     * @param ch
     */
    public void release(Channel ch, String hostname, int port) {
        InetSocketAddress address = new InetSocketAddress(hostname, port);
        ch.flush();
        pools.get(address).release(ch);
    }

    /**
     * 获取线程池的hash值
     */
    public int getPoolHash(Channel ch) {
        InetSocketAddress address = (InetSocketAddress) ch.remoteAddress();
        return System.identityHashCode(pools.get(address));
    }

}
