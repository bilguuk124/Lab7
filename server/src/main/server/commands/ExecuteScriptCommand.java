package server.commands;

import Lab5.common.exceptions.WrongAmountOfElementsException;
import Lab5.common.interactions.User;
import Lab5.common.utility.Outputer;
import server.utility.*;

/**
 * Command 'execute_script'. Executes scripts from a file. Actually only checks argument and prints messages.
 */
public class ExecuteScriptCommand extends AbstractCommand {
    public ExecuteScriptCommand() {
        super("execute_script ", "<file_name>","исполнить скрипт из указанного файла");
    }

    /**
     * Executes the command, but partially.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object commandObjectArgument, User user) {
        try {
            if (argument.isEmpty() || commandObjectArgument != null) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование:  " + getName() + "'");
        }
        return false;
    }
}
