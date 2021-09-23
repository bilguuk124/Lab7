package server.commands;

import Lab5.common.exceptions.WrongAmountOfElementsException;
import server.utility.ResponseOutputer;
import Lab5.common.interactions.User;

/**
 * Command 'help'. It's here just for logical structure.
 */
public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help","", "вывести справку по доступным командам");
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object commandObjectArgument, User user) {
        try {
            if (!argument.isEmpty() || commandObjectArgument != null) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование:  '" + getName() + "'");
        }
        return false;
    }
}
