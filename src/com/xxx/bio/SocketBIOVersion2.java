package com.xxx.bio;

import com.xxx.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 这里多开线程实现多客户端连接
 */
public class SocketBIOVersion2 {

    private ServerSocket serverSocket;

    public void init() throws IOException {
        serverSocket = new ServerSocket(Constants.port);
        while (true) {
            Socket client = serverSocket.accept();
            System.out.println("Get connected from client " + client.getPort());
            handler(client);
        }
    }

    private void handler(Socket client) {
        new Thread(new ClientHandler(client)).start();
    }

    private class ClientHandler implements Runnable {

        private Socket client;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            DataInputStream in = null;
            try {
                in = new DataInputStream(client.getInputStream());
                byte[] buf = new byte[1024];
                in.read(buf);
                System.out.println("Get content from client: " + new String(buf));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        SocketBIOVersion2 server = new SocketBIOVersion2();
        server.init();
    }
}
