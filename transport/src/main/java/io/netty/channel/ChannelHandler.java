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

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 处理 I/O 事件或拦截 I/O 操作，并将其转发到其 {@link ChannelPipeline} 中的下一个处理器。
 * <h3>子类型</h3>
 * <p>
 * {@link ChannelHandler} 并没有提供很多方法，但是通常必须实现以下子类型中的一个：
 * <ul>
 * <li>{@link ChannelInboundHandler} 处理入站事件</li>
 * <li>{@link ChannelOutboundHandler} 处理出站操作</li>
 * </ul>
 * </p>
 * <p>
 * 此外，还提供了以下适配器类便于使用：
 * <ul>
 * <li>{@link ChannelInboundHandlerAdapter} 处理入站事件</li>
 * <li>{@link ChannelOutboundHandlerAdapter} 处理出站操作</li>
 * <li>{@link ChannelDuplexHandler} 处理入站和出站事件</li>
 * </ul>
 * </p>
 */
public interface ChannelHandler {

    /**
     * ChannelHandler 添加触发事件
     */
    void handlerAdded(ChannelHandlerContext ctx) throws Exception;

    /**
     * ChannelHandler 移除触发事件
     */
    void handlerRemoved(ChannelHandlerContext ctx) throws Exception;

    /**
     * 不推荐使用，并将其收拢到了 ChannelInboundHandler 接口中。
     *
     * @deprecated if you want to handle this event you should implement {@link ChannelInboundHandler} and
     * implement the method there.
     */
    @Deprecated
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;

    /**
     * 这是一个文档注解，表示该 ChannelHandler 实例是线程安全的，可以被多次添加到不同的 ChannelPipeline 中。
     */
    @Inherited
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Sharable {
        // no value
    }
}
