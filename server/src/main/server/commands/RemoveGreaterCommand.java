package server.commands;

import Lab5.common.interactions.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;
import java.time.Instant;
import java.util.Date;

import Lab5.common.data.StudyGroup;
import Lab5.common.exceptions.CollectionIsEmptyException;
import Lab5.common.exceptions.GroupNotFoundException;
import Lab5.common.exceptions.WrongAmountOfElementsException;
import Lab5.common.interactions.GroupRaw;




public class RemoveGreaterCommand extends AbstractCommand {
    private CollectionManager collectionManager;


    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater ","{element}", "удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;

    }


    @Override
    public boolean execute(String argument, Object commandObjectArgument, User user) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
                GroupRaw groupRaw = (GroupRaw) commandObjectArgument;
                StudyGroup marineToFind = new StudyGroup(
                collectionManager.generateNextId(),
                groupRaw.getName(),
                groupRaw.getCoordinates(),
                Date.from(Instant.now()),
                groupRaw.getStudentsCount(),
                groupRaw.getTransferredStudents(),
                groupRaw.getFormOfEducation(),
                groupRaw.getSemester(),
                groupRaw.getGroupAdmin(),
                        user);
            StudyGroup marineFromCollection = collectionManager.getByValue(marineToFind);
            if (marineFromCollection == null) throw new GroupNotFoundException();
            collectionManager.removeGreater(marineFromCollection);
            ResponseOutputer.appendln("Группа успешно удалены!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование:  " + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (GroupNotFoundException exception) {
            ResponseOutputer.appenderror("Группы с такими характеристиками в коллекции нет!");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        }
        return false;
    }
}
