package server.commands;

import Lab5.common.exceptions.WrongAmountOfElementsException;
import Lab5.common.exceptions.UserIsNotFoundException;
import Lab5.common.exceptions.DatabaseHandlingException;
import Lab5.common.interactions.User;
import server.utility.DatabaseUserManager;
import server.utility.ResponseOutputer;


public class LoginCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;

    public LoginCommand(DatabaseUserManager databaseUserManager){
        super("login", "", "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user){
        try{
            if(!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if(databaseUserManager.checkUserByUsernameAndPassword(user)) ResponseOutputer.appendln("Пользователь " +
                    user.getUsername() + " авторизован.");
            else throw new UserIsNotFoundException();
            return true;
        } catch (WrongAmountOfElementsException e){
            ResponseOutputer.appendln("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        } catch (UserIsNotFoundException exception) {
            ResponseOutputer.appenderror("Неправильные имя пользователя или пароль!");
        } return true;
    }
}
