package client.utility;

import client.App;
import Lab5.common.exceptions.MustBeNotEmptyException;
import Lab5.common.exceptions.NotInDeclaredLimitsException;
import Lab5.common.utility.Outputer;
import com.sun.tools.internal.ws.wsdl.document.Output;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class AuthAsker {
    private Scanner userScanner;

    public AuthAsker(Scanner userScanner){
        this.userScanner = userScanner;
    }


    public String askLogin() {
        String login;
        while (true) {
            try {
                Outputer.println("Введите логин");
                Outputer.println(App.PS2);
                login = userScanner.nextLine().trim();
                if (login.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Данного логина не существует!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Имя не может быть пустым");
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return login;
    }

    public String askPassword(){
        String password;
        while(true){
            try{
                Outputer.println("Введите пароль: ");
                Outputer.println(App.PS2);
                password = userScanner.nextLine().trim();
                break;
            } catch (NoSuchElementException exception){
                Outputer.printerror("Неверный логин или пароль");
            } catch (IllegalStateException exception){
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return password;
    }

    public boolean askQuestion(String question){
        String finalQuestion = question + "(+/-): ";
        String answer;
        while(true){
            try{
                Outputer.println(finalQuestion);
                Outputer.println(App.PS2);
                answer = userScanner.nextLine().trim();
                if(!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            }catch (NotInDeclaredLimitsException e){
                Outputer.printerror("Ответ должен быть '+' или '-' !");
            }catch (NoSuchElementException e){
                Outputer.printerror("Ответ не распознан!");
            }catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        } return answer.equals("+");
    }
}
