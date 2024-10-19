package com.sunl888.rocket.core.remoting.protocol;


import com.sunl888.rocket.common.constants.ProtocolConstant;
import com.sunl888.rocket.common.enums.MsgTypeEnum;
import com.sunl888.rocket.common.exception.ProtocolException;
import com.sunl888.rocket.common.model.RpcMessage;
import com.sunl888.rocket.common.model.RpcRequest;
import com.sunl888.rocket.common.model.RpcResponse;
import com.sunl888.rocket.compressor.factory.CompressorFactory;
import com.sunl888.rocket.serialization.Serializer;
import com.sunl888.rocket.serialization.factory.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (log.isDebugEnabled()) {
            log.info("接收到消息. size: [{}]", byteBuf.readableBytes());
        }
        if (byteBuf.readableBytes() < 14) {
            log.info("消息不合法");
            return;
        }
        // 复位
        byteBuf.markReaderIndex();
        // 校验魔数
        byte magic = byteBuf.readByte();
        if (magic != ProtocolConstant.MAGIC) {
            throw new ProtocolException("Magic不合法");
        }
        // 读取版本
        byte version = byteBuf.readByte();
        // 读取序列化方式
        byte serialize = byteBuf.readByte();
        // 读取类型
        byte type = byteBuf.readByte();
        // 读取状态
        byte status = byteBuf.readByte();
        // 读取requestId
        long requestId = byteBuf.readLong();
        // 读取压缩方式
        byte compress = byteBuf.readByte();
        // 读取消息体长度
        int bodyLength = byteBuf.readInt();
        // 如果可读字节数小于bodyLength，说明数据还没到齐，等待下一次读取
        if (byteBuf.readableBytes() < bodyLength) {
            // 重置读指针
            byteBuf.resetReaderIndex();
            return;
        }
        // 读取消息
        byte[] body = new byte[bodyLength];
        byteBuf.readBytes(body);
        // 解压
        byte[] unCompressBody = CompressorFactory.getInstance(compress).decompress(body);
        // 反序列化
        Class<?> clazz = type == MsgTypeEnum.REQUEST.getValue() ? RpcRequest.class : RpcResponse.class;
        Serializer serializer = SerializerFactory.getInstance(serialize);
        Object bodyObj = serializer.deserialize(unCompressBody, clazz);
        // 解析消息
        RpcMessage.Header header = RpcMessage.Header.builder()
                .magic(magic)
                .version(version)
                .serialize(serialize)
                .type(type)
                .status(status)
                .requestId(requestId)
                .compress(compress)
                .length(bodyLength)
                .build();
        RpcMessage<?> rpcMessage = new RpcMessage<>(header, bodyObj);
        list.add(rpcMessage);
    }
}
