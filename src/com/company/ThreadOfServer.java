package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ThreadOfServer {
    private Socket clientSocket; //сокет для общения
    private boolean isOpen;
    private boolean isConnected;
    private ServerSocket server;
    private BufferedReader in;
    private BufferedWriter out;
    private String nickname;

    ThreadOfServer(ServerSocket server) {
        this.server = server;
        acceptConnectionThread();
    }

// пронумерованный список заданий, вариант 7

    public void reconnect() {
        try {
            clientSocket = server.accept(); // accept() будет ждать пока
            isConnected = true;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnection() {
        isOpen = true;
        isConnected = false;
        reconnect();
        try {
            nickname = String.valueOf(in.readLine());
            System.out.println(nickname + " подключился");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isOpen) {
            try {
                String word = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                System.out.println(word);
                out.write(nickname + ": " + word + "\n");
                out.flush(); // выталкиваем все из буфера
            } catch (SocketException se){
                System.out.println(nickname + " отключился");
                isConnected = false;
                reconnect();
                closeSocket();
            }
            catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public void closeSocket() {
        try {
            isOpen = false;
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnectionThread(){
        Runnable task = () -> {
            acceptConnection();
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public boolean getIsConnected(){
        return isConnected;
    }
}
