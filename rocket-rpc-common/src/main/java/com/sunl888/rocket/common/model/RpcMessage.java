package com.sunl888.rocket.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcMessage<T> implements Serializable {
    /**
     * 请求头
     */
    private Header header;

    /**
     * body
     */
    private T body;

    /**
     * 请求头
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Header {
        /**
         * 魔数
         */
        private byte magic;

        /**
         * 版本
         */
        private byte version;

        /**
         * 序列化器
         */
        private byte serialize;

        /**
         * 消息类型
         */
        private byte type;

        /**
         * 消息状态
         */
        private byte status;

        /**
         * 请求id
         */
        private long requestId;

        /**
         * 压缩格式
         */
        private byte compress;

        /**
         * 消息体长度
         */
        private long length;
    }
}
