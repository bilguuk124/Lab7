package client.utility;

import Lab5.common.exceptions.EndOfLineException;
import client.App;
import Lab5.common.exceptions.MustBeNotEmptyException;
import Lab5.common.exceptions.NotInDeclaredLimitsException;
import Lab5.common.utility.Outputer;
import com.sun.tools.internal.ws.wsdl.document.Output;

import java.io.Console;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AuthAsker {
    private Scanner userScanner;
    private Console console;

    public AuthAsker(Scanner userScanner){
        this.userScanner = userScanner;
    }


    public String askLogin() {
        String login;
        while (true) {
            try {
                Outputer.println("Введите логин");
                Outputer.print(App.PS2);
                if(!userScanner.hasNextLine()) throw new EndOfLineException();
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
            } catch (EndOfLineException e){
                Outputer.printerror("Заказано выход клиента!");
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
                Outputer.print(App.PS2);
                if(!userScanner.hasNextLine()) throw new EndOfLineException();
                password = userScanner.nextLine().trim();
                break;
            } catch (NoSuchElementException exception){
                Outputer.printerror("Неверный логин или пароль");
            } catch (IllegalStateException exception){
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            } catch (EndOfLineException e){
                Outputer.printerror("Заказано выход клиента!");
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
                Outputer.print(App.PS2);
                if(!userScanner.hasNextLine()) throw new EndOfLineException();
                answer = userScanner.nextLine().trim();
                if(!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            }catch (NotInDeclaredLimitsException e){
                Outputer.printerror("Ответ должен быть '+' или '-' !");
            }catch (NoSuchElementException e){
                Outputer.printerror("Ответ не распознан!");
            }catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(404);
            }catch (EndOfLineException e){
                Outputer.printerror("Заказано выход клиента!");
                System.exit(0);
            }
        } return answer.equals("+");
    }
}
