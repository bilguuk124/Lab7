package server.commands;

import Lab5.common.exceptions.DatabaseHandlingException;
import Lab5.common.interactions.User;
import server.utility.*;
import java.time.Instant;

import Lab5.common.data.*;
import Lab5.common.exceptions.WrongAmountOfElementsException;
import Lab5.common.interactions.GroupRaw;

/**
 * Command 'add_if_min'. Adds a new element to collection if it's less than the minimal one.
 */
public class AddIfMinCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;


    public AddIfMinCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add_if_min ", "{element}","добавить новый элемент, если его значение меньше, чем у наименьшего");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object commandObjectArgument, User user) {
        try {
            if (!argument.isEmpty() || commandObjectArgument == null) throw new WrongAmountOfElementsException();
            GroupRaw groupRaw = (GroupRaw) commandObjectArgument;
            StudyGroup groupToAdd = databaseCollectionManager.insertGroup(groupRaw,user);
            if (collectionManager.collectionSize() == 0 || groupToAdd.compareTo(collectionManager.getFirst()) < 0) {
                collectionManager.addToCollection(groupToAdd);
                ResponseOutputer.appendln("Группа успешно добавлен!");
                return true;
            } else ResponseOutputer.appenderror("Значение группы больше, чем значение наименьшего из групп!");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appenderror("Использование: " + getName() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException e) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных.");
        }
        return false;
    }
}
