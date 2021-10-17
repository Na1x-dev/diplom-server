package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Menu {
    int key = 0;
    Scanner scanner = new Scanner(System.in);
    int buffPort;

    Menu() {
        Scanner scanner = new Scanner(System.in);

        //ServerController serverController = new ServerController();
        this.buffPort = useLastPort();

        System.out.println("Выберите пункт меню: ");
        System.out.println("1. Использовать порт " + buffPort);
        System.out.println("2. Ввести новый порт");
        key = scanner.nextInt();
        switch (key) {
            case 1:
                useLastPort();
                break;
            case 2:
                enterNewPort();
                ServerController.setPort(buffPort);
                break;
        }
        ServerController serverController = new ServerController();
        serverController.checkSocketListThread();
        System.out.println("Чтобы выключить сервер, введите 1");
//        int exit;
//        do {
//            exit = scanner.nextInt();
//            if(exit==1){
//                serverController.shutdownServer();
//            }
//        }while (exit!=1);
    }

    public void enterNewPort() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите новый порт: ");
        this.buffPort = scanner.nextInt();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("port.txt");
            fileOutputStream.write(String.valueOf(buffPort).getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer useLastPort() {
        String stringPort = "";
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("port.txt");
            int i;
            while ((i = fileInputStream.read()) != -1) {
                stringPort += (char) i;
            }
            ServerController.setPort(Integer.valueOf(stringPort));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.valueOf(stringPort);
    }

}
