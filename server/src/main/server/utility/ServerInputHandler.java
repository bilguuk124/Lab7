package server.utility;

import server.App;

import java.util.Scanner;

public class ServerInputHandler extends Thread{

    private CollectionManager collectionManager;

    public void run(){
        Scanner scanner = new Scanner(System.in);
        do{
            if(!scanner.hasNext()) continue;
            String input = scanner.nextLine();
            if(input.equals("exit")){
                App.logger.info("Exiting...");
                System.exit(0);
            }
            else{
                App.logger.error("Неверная команда!");
            }
        }while (scanner.hasNext());
    }
}
