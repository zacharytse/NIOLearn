package com.xxx.nio;

import com.xxx.utils.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Selector版本的nio(new io)
 */
public class SelectorNIO {
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

    public void init() throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(Constants.port));
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            doAction();
        }
    }

    private void doAction() throws IOException {
        int readyNum = selector.select();
        if (readyNum == 0) {
            return;
        }
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iter = selectionKeys.iterator();
        while (iter.hasNext()) {
            SelectionKey key = iter.next();
            if (key.isAcceptable()) {
                accept(key);
            } else if (key.isReadable()) {
                read(key);
            }
            iter.remove();
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        buffer.clear();
        int ret = channel.read(buffer);
        if (ret > 0) {
            buffer.flip();
            byte[] content = new byte[buffer.limit()];
            buffer.get(content);
            System.out.println("Receive content:" + new String(content));
        } else if (ret == 0) {
            return;
        } else {
            channel.close();
            key.cancel();
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = channel.accept();
        clientChannel.configureBlocking(false);
        System.out.println("Client " + clientChannel.socket().getPort() + " has connected...");
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws IOException {
        SelectorNIO server = new SelectorNIO();
        server.init();
    }
}
