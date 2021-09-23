package server.commands;

import Lab5.common.exceptions.*;
import Lab5.common.interactions.User;
import Lab5.common.utility.Outputer;

import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command print_descending_transferred. Prints values of transferred students in descending order.
 */
public class PrintFieldDescendingTransferredStudents extends AbstractCommand{
    private CollectionManager collectionManager;

    public PrintFieldDescendingTransferredStudents(CollectionManager collectionManager) {
        super("print_descending_transferred", "","вывести значения поля transferredStudents всех элементов в порядке убывания");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object commandObjectArgument, User user) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            collectionManager.print_field_descending_transferred_students();
            if (collectionManager.getSumOfTransferStudents() == 0) throw new CollectionIsEmptyException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        }
        return false;
    }
}
