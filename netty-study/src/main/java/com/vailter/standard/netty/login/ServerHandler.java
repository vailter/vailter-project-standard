package com.vailter.standard.netty.login;

import com.vailter.standard.netty.protocol.LoginRequestPacket;
import com.vailter.standard.netty.protocol.LoginResponsePacket;
import com.vailter.standard.netty.protocol.MessageRequestPacket;
import com.vailter.standard.netty.protocol.MessageResponsePacket;
import com.vailter.standard.netty.protocol.Packet;
import com.vailter.standard.netty.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodeC.getInstance().decode(requestByteBuf);

        // 判断是否是登录请求数据包
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

            boolean loginSuccessful = false;
            // 登录校验
            if (valid(loginRequestPacket)) {
                loginSuccessful = true;
                // 校验成功
                System.out.println(new Date() + "客户端[" + loginRequestPacket.getUsername() + "]登录校验成功");
            } else {
                // 校验失败
                System.out.println(new Date() + "客户端[" + loginRequestPacket.getUsername() + "]登录校验失败");
            }

            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            loginResponsePacket.setSuccess(loginSuccessful);
            if (!loginSuccessful) {
                loginResponsePacket.setReason("账号密码校验失败");
            }
            // 编码
            ByteBuf responseByteBuf = PacketCodeC.getInstance().encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        } else if (packet instanceof MessageRequestPacket) {
            // 处理消息
            MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);
            String message = messageRequestPacket.getMessage();
            System.out.println(new Date() + ": 收到客户端消息: " + message);

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();

            // 价值一个亿的AI核心算法😂
            String responseMsg = StringUtils.replace(message, "?", "!");
            responseMsg = StringUtils.replace(responseMsg, "？", "！");
            responseMsg = StringUtils.replace(responseMsg, "吗", "");
            messageResponsePacket.setMessage(responseMsg);
            ByteBuf responseByteBuf = PacketCodeC.getInstance().encode(ctx.alloc(), messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
