package server.commands;

import Lab5.common.exceptions.WrongAmountOfElementsException;
import Lab5.common.interactions.User;
import Lab5.common.utility.Outputer;
import server.utility.ResponseOutputer;

/**
 * Command 'history'. It's here just for logical structure.
 */
public class HistoryCommand extends AbstractCommand {

    public HistoryCommand() {
        super("history", "","вывести историю использованных команд");
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
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        }
        return false;
    }
}
