package server.commands;

import Lab5.common.exceptions.*;
import Lab5.common.interactions.User;
import Lab5.common.utility.*;

import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'sum_of_transferred_students'. Prints the sum of transfer students of all groups.
 */
public class SumOfTransferredStudents extends AbstractCommand {
    private CollectionManager collectionManager;

    public SumOfTransferredStudents(CollectionManager collectionManager) {
        super("sum_of_transferred_students", "","вывести сумму переведённых студентов для всех элементов коллекции");
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
            double sumOfTransferStudents = collectionManager.getSumOfTransferStudents();
            if (sumOfTransferStudents == 0) throw new CollectionIsEmptyException();
            ResponseOutputer.appendln("Сумма переведённых студентов: " + sumOfTransferStudents);
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendln("Коллекция пуста!");
        }
        return false;
    }
}
