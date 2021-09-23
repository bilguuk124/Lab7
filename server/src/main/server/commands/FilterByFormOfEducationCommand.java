package server.commands;

import Lab5.common.data.*;
import Lab5.common.exceptions.CollectionIsEmptyException;
import Lab5.common.exceptions.WrongAmountOfElementsException;
import Lab5.common.interactions.User;
import Lab5.common.utility.Outputer;

import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'filter_by_education_form'. Filters the collection by form of education type.
 */
public class FilterByFormOfEducationCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public FilterByFormOfEducationCommand(CollectionManager collectionManager) {
        super("filter_by_education_form ", "<form>","вывести элементы, значение поля educationForm которых равно заданному");
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
            FormOfEducation formOfEducation = FormOfEducation.valueOf(argument.toUpperCase());
            String filteredInfo = collectionManager.FormOfEducationFilteredInfo(formOfEducation);
            if (!filteredInfo.isEmpty()) {
                ResponseOutputer.appendln(filteredInfo);
                return true;
            } else ResponseOutputer.appendln("В коллекции нет групп с выбранным формой обучения!");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (IllegalArgumentException exception) {
            ResponseOutputer.appenderror("Форм обучения нет в списке!");
            ResponseOutputer.appendln("Список форм обучения - " + Semester.nameList());
        }
        return false;
    }
}
