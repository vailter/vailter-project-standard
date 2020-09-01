package com.vailter.standard.netty.nio;

import com.vailter.standard.netty.login.ClientHandler;
import com.vailter.standard.netty.login.LoginUtil;
import com.vailter.standard.netty.protocol.MessageRequestPacket;
import com.vailter.standard.netty.protocol.PacketCodeC;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        // ch.pipeline() 返回的是和这条连接相关的逻辑处理链
                        // addLast() 添加一个逻辑处理器，这个逻辑处理器为的就是在客户端建立连接成功之后，向服务端写数据
                        //ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

        //bootstrap.connect("localhost", 8080)
        //        .addListener(future -> {
        //            if (future.isSuccess()) {
        //                System.out.println("连接成功!");
        //            } else {
        //                System.err.println("连接失败!");
        //            }
        //        });
        //Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();
        //Channel channel = bootstrap.connect("127.0.0.1", 8000).sync().channel();
        //channel.writeAndFlush(new Date() + ": hello world!");
        //for (int i = 0; i < 1; i++) {
        //    try {
        //        channel.writeAndFlush(new Date() + ": hello world!");
        //    } catch (Exception e) {
        //        e.printStackTrace();
        //    }
        //
        //    Thread.sleep(2000);
        //}
        //while (true) {
        //
        //}

        connect(bootstrap, "localhost", 8000, MAX_RETRY);
    }

    public static final int MAX_RETRY = 5;

    /**
     * 每隔 1 秒、2 秒、4 秒、8 秒，以 2 的幂次来建立连接，然后到达一定次数之后就放弃连接，
     *
     * @param bootstrap
     * @param host
     * @param port
     */
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
                Channel channel = ((ChannelFuture) future).channel();
                // 连接成功之后，启动控制台线程
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.err.println(new Date() + ": 重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……" + delay);
                // bootstrap.config() 这个方法返回的是 BootstrapConfig，对 Bootstrap 配置参数的抽象
                // bootstrap.config().group() 返回的就是我们在一开始的时候配置的线程模型 workerGroup，调 workerGroup 的 schedule 方法即可实现定时任务逻辑。
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMessage(line);
                    ByteBuf byteBuf = PacketCodeC.getInstance().encode(channel.alloc(), packet);
                    channel.writeAndFlush(byteBuf);
                }
            }
        }).start();
    }
}
