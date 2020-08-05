package com.vailter.standard.netty.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty Server
 * <p>
 * boss 对应 IOServer.java 中的接受新连接线程，主要负责创建新连接
 * worker 对应 IOServer.java 中的负责读取数据的线程，主要用于读取数据以及业务逻辑处理
 */
public class NettyServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                // 指定在服务端启动过程中的一些逻辑
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) {
                        System.out.println("服务端启动中...");
                    }
                })
                // 用于指定处理新连接数据的读写处理逻辑
                //.childHandler(new ChannelInitializer<NioSocketChannel>() {
                //    @Override
                //    protected void initChannel(NioSocketChannel ch) {
                //        //ch.pipeline().addLast(new StringDecoder());
                //        ch.pipeline().addLast(new FirstServerHandler());
                //        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                //            @Override
                //            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                //                System.out.println(msg);
                //            }
                //        });
                //    }
                //})
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // 指定连接数据读写逻辑
                        ch.pipeline().addLast(new FirstServerHandler());
                    }
                });
        bind(serverBootstrap, 8000);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
