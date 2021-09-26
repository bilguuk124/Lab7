package client;

import Lab5.common.exceptions.NotInDeclaredLimitsException;
import Lab5.common.exceptions.WrongAmountOfElementsException;
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
    private static int port;

    public static void main(String[] args){
        if (!intitialize(args)) return;

        Outputer.println("Клиент запущен");
        Scanner userScanner = new Scanner(System.in);
        UserHandler userHandler = new UserHandler(userScanner);
        AuthHandler authHandler = new AuthHandler(userScanner);
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler, authHandler);
        client.run();
        userScanner.close();
    }

    private static boolean intitialize(String[] args){
        try{
            if(args.length != 1) throw new WrongAmountOfElementsException();
            port = Integer.parseInt(args[0]);
            if (port<0) throw new NotInDeclaredLimitsException();
            return true;
        }catch (WrongAmountOfElementsException e){
            String jarName = new java.io.File(App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Использование: 'java -jar client.jar <port>'");
        } catch ( NotInDeclaredLimitsException e){
            Outputer.printerror("Порт не может быть отрицательным!");
        } catch (NumberFormatException e){
            Outputer.printerror("Порт должен быть числом !");
        }
        Outputer.printerror("Ошибка инициализации клиента !");
        return false;
    }




}
