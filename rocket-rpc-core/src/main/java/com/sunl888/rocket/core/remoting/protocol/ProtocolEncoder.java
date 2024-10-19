package com.sunl888.rocket.core.remoting.protocol;


import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.common.config.RocketConfig;
import com.sunl888.rocket.common.model.RpcMessage;
import com.sunl888.rocket.compressor.Compressor;
import com.sunl888.rocket.compressor.factory.CompressorFactory;
import com.sunl888.rocket.serialization.Serializer;
import com.sunl888.rocket.serialization.factory.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtocolEncoder extends MessageToByteEncoder<RpcMessage<?>> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage<?> rpcMessage, ByteBuf byteBuf)
            throws Exception {
        if (rpcMessage == null || rpcMessage.getHeader() == null) {
            return;
        }
        // 写入基础信息
        byteBuf.writeByte(rpcMessage.getHeader().getMagic());
        byteBuf.writeByte(rpcMessage.getHeader().getVersion());
        byteBuf.writeByte(rpcMessage.getHeader().getSerialize());
        byteBuf.writeByte(rpcMessage.getHeader().getType());
        byteBuf.writeByte(rpcMessage.getHeader().getStatus());
        byteBuf.writeLong(rpcMessage.getHeader().getRequestId());
        byteBuf.writeByte(rpcMessage.getHeader().getCompress());

        // 序列化消息体
        Serializer serializer = SerializerFactory.getInstance(rpcMessage.getHeader().getSerialize());
        byte[] bodyBytes = serializer.serialize(rpcMessage.getBody());
        if (log.isDebugEnabled()) {
            log.debug("序列化后的消息长度: {}", bodyBytes.length);
        }
        // 压缩消息体
        RocketConfig rocketConfig = ConfigManager.getInstance().getRocketConfig();
        Compressor compressor = CompressorFactory.getInstance(rocketConfig.getCompressor());
        byte[] compressedBody = compressor.compress(bodyBytes);
        if (log.isDebugEnabled()) {
            log.debug("压缩后的消息长度: {}", compressedBody.length);
        }
        // 写入消息长度
        byteBuf.writeInt(compressedBody.length);
        // 写入消息体
        byteBuf.writeBytes(compressedBody);
    }
}
