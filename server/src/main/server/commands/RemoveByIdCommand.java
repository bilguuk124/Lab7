package server.commands;

import Lab5.common.data.StudyGroup;
import Lab5.common.exceptions.*;
import Lab5.common.interactions.User;
import Lab5.common.utility.Outputer;

import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'remove_by_id'. Removes the element by its ID.
 */
public class RemoveByIdCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "<ID>", "удалить элемент из коллекции по ID");
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
            int id = Integer.valueOf(argument);
            StudyGroup groupToRemove = collectionManager.getById(id);
            if (groupToRemove == null) throw new GroupNotFoundException();
            collectionManager.removeToCollection(groupToRemove);
            ResponseOutputer.appendln("Группа успешно удален!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID должен быть представлен числом!");
        } catch (GroupNotFoundException exception) {
            ResponseOutputer.appenderror("Группы с таким ID в коллекции нет!");
        }
        return false;
    }
}
