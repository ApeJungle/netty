/*
 * Copyright 2016 The Netty Project
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

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.FutureListener;

import java.net.ConnectException;
import java.net.SocketAddress;

/**
 * 用于触发和传播出站操作（发出数据）
 * 出站主动发起操作，控制权在用户手中，所以是以操作执行的方式。
 */
public interface ChannelOutboundInvoker {

    /**
     * 绑定 Socket 地址并监听
     */
    default ChannelFuture bind(SocketAddress localAddress) {
        return bind(localAddress, newPromise());
    }

    /**
     * 建立连接
     */
    default ChannelFuture connect(SocketAddress remoteAddress) {
        return connect(remoteAddress, newPromise());
    }

    /**
     * 建立连接
     */
    default ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return connect(remoteAddress, localAddress, newPromise());
    }

    /**
     * 断开连接
     */
    default ChannelFuture disconnect() {
        return disconnect(newPromise());
    }

    /**
     * 关闭 Channel
     */
    default ChannelFuture close() {
        return close(newPromise());
    }

    /**
     * 将 Channel 从其当前绑定的 EventLoop上解绑。
     */
    default ChannelFuture deregister() {
        return deregister(newPromise());
    }

    /**
     * 绑定 Socket 地址并监听
     */
    ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise);

    /**
     * 建立连接
     */
    ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise);

    /**
     * 建立连接
     */
    ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise);

    /**
     * 断开连接
     */
    ChannelFuture disconnect(ChannelPromise promise);

    /**
     * 关闭 Channel
     */
    ChannelFuture close(ChannelPromise promise);

    /**
     * 将 Channel 从其当前绑定的 EventLoop上解绑。
     */
    ChannelFuture deregister(ChannelPromise promise);

    /**
     * Netty 默认开启了 AUTO_READ（自动读取）。如果将 AUTO_READ 关闭，Netty 将不会主动从底层 Socket 接收数据；此时必须显式调用 read()，
     * 底层才会触发下一次 Socket 读操作并向 Pipeline 触发 channelRead 事件（用于流量控制/背压）。
     */
    ChannelOutboundInvoker read();

    /**
     * 将要发送的数据对象 msg 写入到 Netty 的内部出站缓冲区（ChannelOutboundBuffer）中。
     * write 不会触发底层的实际网络发送！ 数据只是进入了内存缓冲区，直到调用 flush() 时才会刷入 Socket 发送出去。
     */
    default ChannelFuture write(Object msg) {
        return write(msg, newPromise());
    }

    /**
     * 将要发送的数据对象 msg 写入到 Netty 的内部出站缓冲区（ChannelOutboundBuffer）中。
     * write 不会触发底层的实际网络发送！ 数据只是进入了内存缓冲区，直到调用 flush() 时才会刷入 Socket 发送出去。
     */
    ChannelFuture write(Object msg, ChannelPromise promise);

    /**
     * 将之前通过 write 刷入出站缓冲区（ChannelOutboundBuffer）中的所有挂起数据，一次性刷新并写入底层 Socket 传输通道。
     */
    ChannelOutboundInvoker flush();

    /**
     * Shortcut for call {@link #write(Object, ChannelPromise)} and {@link #flush()}.
     */
    ChannelFuture writeAndFlush(Object msg, ChannelPromise promise);

    /**
     * Shortcut for call {@link #write(Object)} and {@link #flush()}.
     */
    default ChannelFuture writeAndFlush(Object msg) {
        return writeAndFlush(msg, newPromise());
    }

    /**
     * Return a new {@link ChannelPromise}.
     */
    ChannelPromise newPromise();

    /**
     * Return an new {@link ChannelProgressivePromise}
     */
    ChannelProgressivePromise newProgressivePromise();

    /**
     * Create a new {@link ChannelFuture} which is marked as succeeded already. So {@link ChannelFuture#isSuccess()}
     * will return {@code true}. All {@link FutureListener} added to it will be notified directly. Also
     * every call of blocking methods will just return without blocking.
     */
    ChannelFuture newSucceededFuture();

    /**
     * Create a new {@link ChannelFuture} which is marked as failed already. So {@link ChannelFuture#isSuccess()}
     * will return {@code false}. All {@link FutureListener} added to it will be notified directly. Also
     * every call of blocking methods will just return without blocking.
     */
    ChannelFuture newFailedFuture(Throwable cause);

    /**
     * Return a special ChannelPromise which can be reused for different operations.
     * <p>
     * It's only supported to use
     * it for {@link ChannelOutboundInvoker#write(Object, ChannelPromise)}.
     * </p>
     * <p>
     * Be aware that the returned {@link ChannelPromise} will not support most operations and should only be used
     * if you want to save an object allocation for every write operation. You will not be able to detect if the
     * operation  was complete, only if it failed as the implementation will call
     * {@link ChannelPipeline#fireExceptionCaught(Throwable)} in this case.
     * </p>
     * <strong>Be aware this is an expert feature and should be used with care!</strong>
     */
    ChannelPromise voidPromise();
}
