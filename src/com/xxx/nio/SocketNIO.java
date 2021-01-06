package com.xxx.nio;

import com.xxx.utils.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置accept和read为非阻塞(NIO:nonblocking io)
 */
public class SocketNIO {

    private ServerSocketChannel channel;

    private List<SocketChannel> channels;

    private ByteBuffer buf = ByteBuffer.allocateDirect(1024);

    public void init() throws IOException {
        channel = ServerSocketChannel.open();
        //设置accept为非阻塞
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(Constants.port));
        channels = new ArrayList<>();
        while (true) {
            accept();
            handle();
        }
    }

    private void accept() throws IOException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SocketChannel clientChannel = channel.accept();
        if (clientChannel == null) {
            System.out.println("null...");
            return;
        }
        System.out.println("Client " + clientChannel.socket().getPort() + " has connected");
        //设置read为非阻塞
        clientChannel.configureBlocking(false);
        channels.add(clientChannel);
    }

    private void handle() throws IOException {
        for (SocketChannel channel : channels) {
            int ret = channel.read(buf);//这里的read会使用系统调用
            if(ret > 0) {
                buf.flip();
                byte[] res = new byte[buf.limit()];
                buf.get(res);
                System.out.println("Receive content:" + new String(res));
                buf.clear();
            } else if(ret == 0) {
                continue;
            } else {
                channel.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        SocketNIO server = new SocketNIO();
        server.init();
    }
}
