package com.vailter.standard.netty.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
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
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
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
                //.childHandler(new ChannelInitializer<NioSocketChannel>() {
                //    @Override
                //    protected void initChannel(NioSocketChannel ch) {
                //        // 指定连接数据读写逻辑
                //        ch.pipeline().addLast(new ServerHandler());
                //    }
                //});
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // inBound，处理读数据的逻辑链
                        ch.pipeline().addLast(new InBoundHandlerA());
                        ch.pipeline().addLast(new InBoundHandlerB());
                        ch.pipeline().addLast(new InBoundHandlerC());

                        // outBound，处理写数据的逻辑链
                        ch.pipeline().addLast(new OutBoundHandlerA());
                        ch.pipeline().addLast(new OutBoundHandlerB());
                        ch.pipeline().addLast(new OutBoundHandlerC());
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

    public static class InBoundHandlerA extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InBoundHandlerA: " + msg);
            super.channelRead(ctx, msg);
        }
    }

    public static class InBoundHandlerB extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InBoundHandlerB: " + msg);
            super.channelRead(ctx, msg);
        }
    }

    public static class InBoundHandlerC extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InBoundHandlerC: " + msg);
            //super.channelRead(ctx, msg);
            ctx.channel().writeAndFlush(msg);
        }
    }

    public static class OutBoundHandlerA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutBoundHandlerA: " + msg);
            super.write(ctx, msg, promise);
        }
    }

    public static class OutBoundHandlerB extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutBoundHandlerB: " + msg);
            super.write(ctx, msg, promise);
        }
    }

    public static class OutBoundHandlerC extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutBoundHandlerC: " + msg);
            super.write(ctx, msg, promise);
        }
    }
}
