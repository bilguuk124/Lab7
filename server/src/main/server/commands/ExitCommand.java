package server.commands;

import Lab5.common.interactions.User;
import server.utility.*;
import Lab5.common.exceptions.WrongAmountOfElementsException;


/**
 * Command 'exit'. Checks for wrong arguments then do nothing.
 */
public class ExitCommand extends AbstractCommand {

    public ExitCommand() {
        super("exit", "","завершить программу (без сохранения в файл)");
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object commandObjectArgument, User user) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: " + getName() + "'");
        }
        return false;
    }
}
