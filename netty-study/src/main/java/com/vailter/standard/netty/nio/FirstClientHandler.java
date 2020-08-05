package com.vailter.standard.netty.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 1.这个逻辑处理器继承自 ChannelInboundHandlerAdapter，然后覆盖了 channelActive()方法，这个方法会在客户端连接建立成功之后被调用
 * <p>
 * 2.客户端连接建立成功之后，调用到 channelActive() 方法，在这个方法里面，我们编写向服务端写数据的逻辑
 * <p>
 * 3.写数据的逻辑分为两步：首先我们需要获取一个 netty 对二进制数据的抽象 ByteBuf，上面代码中, ctx.alloc() 获取到一个 ByteBuf 的内存管理器，这个 内存管理器的作用就是分配一个 ByteBuf，然后我们把字符串的二进制数据填充到 ByteBuf，这样我们就获取到了 Netty 需要的一个数据格式，最后我们调用 ctx.channel().writeAndFlush() 把数据写到服务端
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + ": 客户端写出数据");

        // 1. 获取数据
        ByteBuf buffer = getByteBuf(ctx);

        // 2. 写数据
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "你好，卡卡罗特!".getBytes(StandardCharsets.UTF_8);

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }
}
