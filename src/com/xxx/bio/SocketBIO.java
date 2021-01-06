package com.xxx.bio;

import com.xxx.utils.Constants;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO的测试类
 */
public class SocketBIO {

    private ServerSocket serverSocket;

    public void init() throws IOException {
        serverSocket = new ServerSocket(Constants.port);
        while (true) {
            Socket client = serverSocket.accept();
            System.out.println("Get connected from client " + client.getPort());
            handleConnect(client);
        }
    }

    private void handleConnect(Socket client) throws IOException {
        DataInputStream input = null;
        try {
            input = new DataInputStream(client.getInputStream());
            byte[] buf = new byte[1024];
            input.read(buf);
            System.out.println("Get content from client: " + new String(buf));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(client != null) {
                client.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        SocketBIO server = new SocketBIO();
        server.init();

    }
}
