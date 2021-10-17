package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerController {
    private ServerSocket server;
    private ArrayList<ThreadOfServer> sockets = new ArrayList<>();
    private static int port = 4006;

    ServerController() {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sockets.add(new ThreadOfServer(server));
    }

    public void checkSocketListThread(){
        Runnable task = () -> {
            checkSocketList();
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void checkSocketList() {
        while (true) {
            for (int i = sockets.size()-1; i > 0; i--) {
                if (!sockets.get(i).getIsConnected()) {
                    sockets.get(i).closeSocket();
                    sockets.remove(i);
                }
            }
            if(sockets.get(0).getIsConnected()){
                sockets.add(0, new ThreadOfServer(server));
            }
            //System.out.println(sockets);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPort(int port) {
        ServerController.port = port;
    }

    public void shutdownServer(){
        for(ThreadOfServer socketThread : sockets){
            socketThread.closeSocket();

        }
        sockets.clear();
    }
}
