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

/**
 * 用于触发和传播入站事件（接收数据）
 * 入站被动发生的客观事件，控制权不在用户手中，所以是以事件触发的方式。
 */
public interface ChannelInboundInvoker {

    /**
     * Channel 注册到 EventLoop 的事件。
     */
    ChannelInboundInvoker fireChannelRegistered();

    /**
     * Channel 从 EventLoop 中注销的事件。
     */
    ChannelInboundInvoker fireChannelUnregistered();

    /**
     * Channel 激活（连接建立）事件
     */
    ChannelInboundInvoker fireChannelActive();

    /**
     * Channel 失活（连接关闭）事件
     */
    ChannelInboundInvoker fireChannelInactive();

    /**
     * Channel 异常事件
     */
    ChannelInboundInvoker fireExceptionCaught(Throwable cause);

    /**
     * 触发自定义用户事件（如心跳超时）
     */
    ChannelInboundInvoker fireUserEventTriggered(Object event);

    /**
     * Channel 读取消息事件
     */
    ChannelInboundInvoker fireChannelRead(Object msg);

    /**
     * Triggers an {@link ChannelInboundHandler#channelReadComplete(ChannelHandlerContext)}
     * event to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * TODO ??
     */
    ChannelInboundInvoker fireChannelReadComplete();

    /**
     * Triggers an {@link ChannelInboundHandler#channelWritabilityChanged(ChannelHandlerContext)}
     * event to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * TODO ??
     */
    ChannelInboundInvoker fireChannelWritabilityChanged();
}
