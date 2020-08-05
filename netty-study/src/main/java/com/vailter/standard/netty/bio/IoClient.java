package com.vailter.standard.netty.bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class IoClient {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                for (int i = 0; i < 3; i++) {
                    try {
                        // 每隔 2 秒，我们向服务端写一个带有时间戳的 "hello world"。
                        socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                        TimeUnit.SECONDS.sleep(2);
                    } catch (Exception e) {
                    }
                }
                //while (true) {
                //
                //}
            } catch (IOException e) {
            }
        }).start();
    }
}
