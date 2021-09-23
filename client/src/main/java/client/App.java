package client;

import Lab5.common.utility.Outputer;
import client.utility.AuthHandler;
import client.utility.UserHandler;

import java.util.Scanner;

public class App{
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    private  static final  int RECONNECTION_TIMEOUT = 5 * 1000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;
    private static int port = 1821;

    public static void main(String[] args){
        Outputer.println("Клиент запущен");
        Scanner userScanner = new Scanner(System.in);
        UserHandler userHandler = new UserHandler(userScanner);
        AuthHandler authHandler = new AuthHandler(userScanner);
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler, authHandler);
        client.run();
        userScanner.close();
    }




}
