package com.vailter.standard.netty.protocol;

import com.vailter.standard.netty.JSONSerializer;
import com.vailter.standard.netty.Serializer;
import io.netty.buffer.ByteBuf;

public class PacketCodeCTest {
    public static void main(String[] args) {
        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserId(123);
        loginRequestPacket.setUsername("zhangsan");
        loginRequestPacket.setPassword("password");

        ByteBuf byteBuf = PacketCodeC.getInstance().encode(loginRequestPacket);
        Packet decodedPacket = PacketCodeC.getInstance().decode(byteBuf);

        byte[] serialize = serializer.serialize(loginRequestPacket);

        byte[] serialize1 = serializer.serialize(decodedPacket);

        System.out.println(serialize == serialize1);

        //Asserts.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }
}
