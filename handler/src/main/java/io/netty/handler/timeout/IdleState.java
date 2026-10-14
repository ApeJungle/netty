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
package io.netty.handler.timeout;

import io.netty.channel.Channel;


/**
 * {@link Channel} 的空闲状态枚举
 */
public enum IdleState {
    /**
     * 接收空闲
     */
    READER_IDLE,
    /**
     * 发送空闲
     */
    WRITER_IDLE,
    /**
     * 没有接收和发送
     */
    ALL_IDLE
}
