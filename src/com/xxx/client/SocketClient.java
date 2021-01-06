package com.xxx.client;

import com.xxx.utils.Constants;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Socket 客户端
 */
public class SocketClient {

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        socket = new Socket("localhost",Constants.port);
        while(true) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
            //这里要转成字节发送
            out.write(str.getBytes());
        }
    }
}
