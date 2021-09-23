package server.commands;

import Lab5.common.exceptions.WrongAmountOfElementsException;
import Lab5.common.interactions.User;
import server.utility.ResponseOutputer;

public class ServerExitCommand extends AbstractCommand{
    public ServerExitCommand(){
        super("server_exit","","завершить работу сервера");
    }
    @Override
    public boolean execute(String argument, Object commandObjectArgument, User user) {
        try{
            if(!argument.isEmpty() || commandObjectArgument != null) throw new WrongAmountOfElementsException();
            ResponseOutputer.appendln("Работа сервера успешно завершена!");
            return(true);
        }catch (WrongAmountOfElementsException e){
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }

        return false;
    }
}
