package server.commands;

import Lab5.common.data.StudyGroup;
import Lab5.common.exceptions.DatabaseHandlingException;
import Lab5.common.exceptions.ManualDatabaseEditException;
import Lab5.common.exceptions.PermissionDeniedException;
import Lab5.common.exceptions.WrongAmountOfElementsException;
import Lab5.common.interactions.User;
import Lab5.common.utility.Outputer;

import server.utility.*;


/**
 * Command 'clear'. Clears the collection.
 */
public class ClearCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("clear", "","очистить коллекцию");
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
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            for(StudyGroup studyGroup : collectionManager.getCollection()){
                if (!studyGroup.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkGroupUserId(studyGroup.getId(), user)) throw new ManualDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.clearCollection();
            ResponseOutputer.appendln("Коллекция очищена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        } catch (DatabaseHandlingException e) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
            e.printStackTrace();
        } catch (PermissionDeniedException e) {
            ResponseOutputer.appenderror("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (ManualDatabaseEditException e) {
            ResponseOutputer.appenderror("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        }

        return false;
    }
}
