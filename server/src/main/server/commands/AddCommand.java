package server.commands;

import Lab5.common.exceptions.DatabaseHandlingException;
import Lab5.common.interactions.User;
import server.utility.DatabaseCollectionManager;
import server.utility.*;

import Lab5.common.exceptions.WrongAmountOfElementsException;
import Lab5.common.interactions.GroupRaw;

import javax.xml.crypto.Data;


/**
 * Command 'add'. Adds a new element to collection.
 */
public class AddCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add ", "{element}","добавить новый элемент в коллекцию");
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
            if (!argument.isEmpty() || commandObjectArgument == null ) throw new WrongAmountOfElementsException();
            GroupRaw groupRaw = (GroupRaw) commandObjectArgument;
            collectionManager.addToCollection(databaseCollectionManager.insertGroup(groupRaw,user));
            ResponseOutputer.appendln("Группа успешно добавлен!");
            return true;
        } catch (WrongAmountOfElementsException e ) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException e){
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException e){
            ResponseOutputer.appendln("Произошла ошибка при обращении к базе данных!");
            e.printStackTrace();
        }
        return false;
    }
}
