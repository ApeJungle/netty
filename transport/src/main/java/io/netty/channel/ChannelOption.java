/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.AbstractConstant;
import io.netty.util.ConstantPool;
import io.netty.util.internal.ObjectUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * A {@link ChannelOption} allows to configure a {@link ChannelConfig} in a type-safe
 * way. Which {@link ChannelOption} is supported depends on the actual implementation
 * of {@link ChannelConfig} and may depend on the nature of the transport it belongs
 * to.
 *
 * @param <T>   the type of the value which is valid for the {@link ChannelOption}
 */
public class ChannelOption<T> extends AbstractConstant<ChannelOption<T>> {

    private static final ConstantPool<ChannelOption<Object>> pool = new ConstantPool<ChannelOption<Object>>() {
        @Override
        protected ChannelOption<Object> newConstant(int id, String name) {
            return new ChannelOption<Object>(id, name);
        }
    };

    /**
     * Returns the {@link ChannelOption} of the specified name.
     */
    @SuppressWarnings("unchecked")
    public static <T> ChannelOption<T> valueOf(String name) {
        return (ChannelOption<T>) pool.valueOf(name);
    }

    /**
     * Shortcut of {@link #valueOf(String) valueOf(firstNameComponent.getName() + "#" + secondNameComponent)}.
     */
    @SuppressWarnings("unchecked")
    public static <T> ChannelOption<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return (ChannelOption<T>) pool.valueOf(firstNameComponent, secondNameComponent);
    }

    /**
     * Returns {@code true} if a {@link ChannelOption} exists for the given {@code name}.
     */
    public static boolean exists(String name) {
        return pool.exists(name);
    }

    /**
     * Creates a new {@link ChannelOption} for the given {@code name} or fail with an
     * {@link IllegalArgumentException} if a {@link ChannelOption} for the given {@code name} exists.
     *
     * @deprecated use {@link #valueOf(String)}.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static <T> ChannelOption<T> newInstance(String name) {
        return (ChannelOption<T>) pool.newInstance(name);
    }

    // 指定 ByteBuf 分配器，默认 PooledByteBufAllocator
    public static final ChannelOption<ByteBufAllocator> ALLOCATOR = valueOf("ALLOCATOR");

    // 控制每次读操作分配多大的 ByteBuf
    public static final ChannelOption<RecvByteBufAllocator> RECVBUF_ALLOCATOR = valueOf("RECVBUF_ALLOCATOR");

    /**
     * @deprecated use {@link #RECVBUF_ALLOCATOR}.
     */
    @Deprecated
    public static final ChannelOption<RecvByteBufAllocator> RCVBUF_ALLOCATOR = RECVBUF_ALLOCATOR;

    // 估算消息大小，用于写缓冲区水位计算
    public static final ChannelOption<MessageSizeEstimator> MESSAGE_SIZE_ESTIMATOR = valueOf("MESSAGE_SIZE_ESTIMATOR");

    // 连接超时时间（毫秒）。客户端 connect() 时生效，超时会失败
    public static final ChannelOption<Integer> CONNECT_TIMEOUT_MILLIS = valueOf("CONNECT_TIMEOUT_MILLIS");
    /**
     * @deprecated Use {@link MaxMessagesRecvByteBufAllocator}
     * and {@link MaxMessagesRecvByteBufAllocator#maxMessagesPerRead(int)}.
     */
    // 一次读循环最多读多少条消息
    @Deprecated
    public static final ChannelOption<Integer> MAX_MESSAGES_PER_READ = valueOf("MAX_MESSAGES_PER_READ");
    // 一次写循环最多写多少条消息
    public static final ChannelOption<Integer> MAX_MESSAGES_PER_WRITE = valueOf("MAX_MESSAGES_PER_WRITE");
    // 写操作自旋次数。写不完会重新注册写事件。默认通常 16。
    public static final ChannelOption<Integer> WRITE_SPIN_COUNT = valueOf("WRITE_SPIN_COUNT");
    /**
     * @deprecated Use {@link #WRITE_BUFFER_WATER_MARK}
     */
    @Deprecated
    public static final ChannelOption<Integer> WRITE_BUFFER_HIGH_WATER_MARK = valueOf("WRITE_BUFFER_HIGH_WATER_MARK");
    /**
     * @deprecated Use {@link #WRITE_BUFFER_WATER_MARK}
     */
    @Deprecated
    public static final ChannelOption<Integer> WRITE_BUFFER_LOW_WATER_MARK = valueOf("WRITE_BUFFER_LOW_WATER_MARK");
    // 写缓冲区高低水位。超过高水位 → channel.isWritable() = false；降到低水位 → 重新变为 true。用于背压控制。
    public static final ChannelOption<WriteBufferWaterMark> WRITE_BUFFER_WATER_MARK =
            valueOf("WRITE_BUFFER_WATER_MARK");
    // 是否允许半关闭（TCP FIN 只关闭写方向，仍可读）
    public static final ChannelOption<Boolean> ALLOW_HALF_CLOSURE = valueOf("ALLOW_HALF_CLOSURE");
    // 是否自动读。true（默认）时有数据自动触发读；false 时需手动调用 channel.read()（流量控制常用）。
    public static final ChannelOption<Boolean> AUTO_READ = valueOf("AUTO_READ");

    // 写失败时是否自动关闭 Channel。默认 true。
    public static final ChannelOption<Boolean> AUTO_CLOSE = valueOf("AUTO_CLOSE");

    // 是否允许发送广播包（UDP 常用）
    public static final ChannelOption<Boolean> SO_BROADCAST = valueOf("SO_BROADCAST");
    // 开启 TCP 保活探测。系统默认探测间隔通常很长（如 Linux 2 小时），实时性差，生产中更推荐应用层心跳。
    public static final ChannelOption<Boolean> SO_KEEPALIVE = valueOf("SO_KEEPALIVE");
    // 发送缓冲区大小（字节）
    public static final ChannelOption<Integer> SO_SNDBUF = valueOf("SO_SNDBUF");
    // 接收缓冲区大小（字节）
    public static final ChannelOption<Integer> SO_RCVBUF = valueOf("SO_RCVBUF");
    // 允许地址重用。服务端重启时常用，避免 “Address already in use”。
    public static final ChannelOption<Boolean> SO_REUSEADDR = valueOf("SO_REUSEADDR");
    public static final ChannelOption<Integer> SO_LINGER = valueOf("SO_LINGER");
    // 仅服务端。已完成三次握手但还未被 accept 的连接队列长度。高并发时需调大（如 1024、2048）。
    public static final ChannelOption<Integer> SO_BACKLOG = valueOf("SO_BACKLOG");
    // Socket 超时时间
    public static final ChannelOption<Integer> SO_TIMEOUT = valueOf("SO_TIMEOUT");

    // IP Type of Service / DSCP 值，用于 QoS。
    public static final ChannelOption<Integer> IP_TOS = valueOf("IP_TOS");

    public static final ChannelOption<InetAddress> IP_MULTICAST_ADDR = valueOf("IP_MULTICAST_ADDR");
    // 多播出口网卡
    public static final ChannelOption<NetworkInterface> IP_MULTICAST_IF = valueOf("IP_MULTICAST_IF");
    // 多播 TTL
    public static final ChannelOption<Integer> IP_MULTICAST_TTL = valueOf("IP_MULTICAST_TTL");
    // 是否禁用多播回环
    public static final ChannelOption<Boolean> IP_MULTICAST_LOOP_DISABLED = valueOf("IP_MULTICAST_LOOP_DISABLED");

    // 是否禁用 Nagle 算法。true = 立即发送小包（低延迟，推荐大多数实时场景）；false = 合并小包（提高吞吐量但增加延迟）。Netty 默认通常为 true。
    public static final ChannelOption<Boolean> TCP_NODELAY = valueOf("TCP_NODELAY");
    /**
     * 客户端 是否启用 TCP Fast Open（在 SYN 包中带数据）。
     */
    public static final ChannelOption<Boolean> TCP_FASTOPEN_CONNECT = valueOf("TCP_FASTOPEN_CONNECT");

    /**
     * 服务端 TCP Fast Open 队列长度
     */
    public static final ChannelOption<Integer> TCP_FASTOPEN = valueOf(ChannelOption.class, "TCP_FASTOPEN");

    @Deprecated
    public static final ChannelOption<Boolean> DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION =
            valueOf("DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION");

    public static final ChannelOption<Boolean> SINGLE_EVENTEXECUTOR_PER_GROUP =
            valueOf("SINGLE_EVENTEXECUTOR_PER_GROUP");

    /**
     * Creates a new {@link ChannelOption} with the specified unique {@code name}.
     */
    private ChannelOption(int id, String name) {
        super(id, name);
    }

    @Deprecated
    protected ChannelOption(String name) {
        this(pool.nextId(), name);
    }

    /**
     * Validate the value which is set for the {@link ChannelOption}. Sub-classes
     * may override this for special checks.
     */
    public void validate(T value) {
        ObjectUtil.checkNotNull(value, "value");
    }
}
