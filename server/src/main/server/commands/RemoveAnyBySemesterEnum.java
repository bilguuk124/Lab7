package server.commands;

import Lab5.common.data.*;
import Lab5.common.exceptions.*;
import Lab5.common.interactions.User;
import Lab5.common.utility.Outputer;

import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

import java.util.Locale;

/**
 * Command 'remove_any_by_semester'. Removes one group if it's semester is equal to user input.
 */
public class RemoveAnyBySemesterEnum extends AbstractCommand{
    private CollectionManager collectionManager;

    public RemoveAnyBySemesterEnum(CollectionManager collectionManager) {
        super("remove_by_semester", "","удалить из коллекции один элемент, значение поля semesterEnum которого эквивалентно заданному");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object commandObjectArgument, User user) {
        try {
            if (argument.isEmpty()) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            String semester = argument.trim().toUpperCase(Locale.ROOT);
            if(!collectionManager.semesterContains(semester)) throw new SemesterNotFoundException();
            StudyGroup groupToRemove = collectionManager.getBySemester(semester);
            if (groupToRemove == null) throw new GroupNotFoundException();
            collectionManager.removeAnyBySemesterEnum(groupToRemove);
            ResponseOutputer.appendln("Успешно удален");
            return true;
        } catch (SemesterNotFoundException e) {
            ResponseOutputer.appenderror("Такого Семестра нет");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: " + getName() + "<семестр>");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (GroupNotFoundException exception) {
            ResponseOutputer.appenderror("Группы с таким семестром в коллекции нет!");
        }
        return false;
    }
}
