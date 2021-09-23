package server.utility;
import Lab5.common.interactions.User;
import server.commands.Command;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import Lab5.common.exceptions.HistoryIsEmptyException;
import Lab5.common.utility.Outputer;
import server.commands.RegisterCommand;

/**
 * Operates the command
 */
public class CommandManager {
    private final int COMMAND_HISTORY_SIZE = 8;

    private String[] commandHistory = new String[COMMAND_HISTORY_SIZE];
    private List<Command> commands = new ArrayList<>();
    private Command helpCommand;
    private Command infoCommand;
    private Command showCommand;
    private Command addCommand;
    private Command updateCommand;
    private Command removeByIdCommand;
    private Command clearCommand;
    private Command exitCommand;
    private Command executeScriptCommand;
    private Command addIfMinCommand;
    private Command removeGreaterCommand;
    private Command historyCommand;
    private Command sumOfTransferredStudents;
    private Command filterByFormOfEducationCommand;
    private Command printFieldDescendingTransferredStudents;
    private Command removeAnyBySemesterEnum;
    private Command serverExitCommand;
    private Command registerCommand;
    private Command loginCommand;

    private ReentrantLock locker = new ReentrantLock();

    public CommandManager(Command helpCommand, Command infoCommand, Command showCommand, Command addCommand, Command updateCommand,
                          Command removeByIdCommand, Command clearCommand, Command exitCommand, Command executeScriptCommand,
                          Command addIfMinCommand, Command removeGreaterCommand, Command historyCommand, Command sumOfTransferredStudentsCommand,
                          Command filterByFormOfEducationCommand, Command printFieldDescendingTransferredStudents,
                          Command removeAnyBySemesterEnum, Command serverExitCommand, Command registerCommand,Command loginCommand) {
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.showCommand = showCommand;
        this.addCommand = addCommand;
        this.updateCommand = updateCommand;
        this.removeByIdCommand = removeByIdCommand;
        this.clearCommand = clearCommand;
        this.exitCommand = exitCommand;
        this.executeScriptCommand = executeScriptCommand;
        this.addIfMinCommand = addIfMinCommand;
        this.removeGreaterCommand = removeGreaterCommand;
        this.historyCommand = historyCommand;
        this.sumOfTransferredStudents = sumOfTransferredStudentsCommand;
        this.filterByFormOfEducationCommand = filterByFormOfEducationCommand;
        this.printFieldDescendingTransferredStudents = printFieldDescendingTransferredStudents;
        this.removeAnyBySemesterEnum = removeAnyBySemesterEnum;
        this.serverExitCommand = serverExitCommand;
        this.registerCommand = registerCommand;
        this.loginCommand = loginCommand;

        commands.add(helpCommand);
        commands.add(infoCommand);
        commands.add(showCommand);
        commands.add(addCommand);
        commands.add(updateCommand);
        commands.add(removeByIdCommand);
        commands.add(clearCommand);
        commands.add(exitCommand);
        commands.add(executeScriptCommand);
        commands.add(addIfMinCommand);
        commands.add(removeGreaterCommand);
        commands.add(historyCommand);
        commands.add(sumOfTransferredStudentsCommand);
        commands.add(filterByFormOfEducationCommand);
        commands.add(printFieldDescendingTransferredStudents);
        commands.add(removeAnyBySemesterEnum);
        commands.add(serverExitCommand);
        commands.add(registerCommand);
        commands.add(loginCommand);

    }

    /**
     * @return The command history.
     */
    public String[] getCommandHistory() {
        return commandHistory;
    }

    /**
     * @return List of manager's commands.
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Adds command to command history.
     * @param commandToStore Command to add.
     */
    public void addToHistory(String commandToStore, User user) throws NullPointerException {
        locker.lock();
        try{
            for (Command command : commands) {
                if (command.getName().split(" ")[0].equals(commandToStore)) {
                    for (int i = COMMAND_HISTORY_SIZE-1; i>0; i--) {
                        commandHistory[i] = commandHistory[i-1];
                    }
                    commandHistory[0] = commandToStore;
                }
            }
        } finally {
            locker.unlock();
        }

    }

    /**
     * Prints that command is not found.
     * @param command Command, which is not found.
     * @return Command exit status.
     */
    public boolean noSuchCommand(String command) {
        ResponseOutputer.appendln("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
        return false;
    }

    /**
     * Prints info about the all commands.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean help(String argument, Object commandObjectArgument, User user) {
        if (helpCommand.execute(argument,commandObjectArgument, user)) {
            for (Command command : commands) {
                ResponseOutputer.appendtable(command.getName(), command.getDescription());
            }
            return true;
        } else return false;
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean info(String argument, Object commandObjectArgument, User user) {
        return infoCommand.execute(argument, commandObjectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean show(String argument, Object commandObjectArgument, User user) {
        return showCommand.execute(argument, commandObjectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean add(String argument, Object commandObjectArgument, User user) {
        return addCommand.execute(argument, commandObjectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean update(String argument, Object commandObjectArgument, User user) {
        return updateCommand.execute(argument, commandObjectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean removeById(String argument, Object commandObjectArgument, User user ) {
        return removeByIdCommand.execute(argument, commandObjectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeAnyBySemesterEnum(String argument, Object commandObjectArgument, User user){
        return removeAnyBySemesterEnum.execute(argument, commandObjectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean clear(String argument, Object commandObjectArgument, User user) {
        return clearCommand.execute(argument, commandObjectArgument, user );
    }


    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean exit(String argument, Object commandObjectArgument, User user) {
        return exitCommand.execute(argument, commandObjectArgument, user);
    }




    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean executeScript(String argument, Object commandObjectArgument, User user) {
        return executeScriptCommand.execute(argument, commandObjectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean addIfMin(String argument, Object commandObjectArgument, User user) {
        return addIfMinCommand.execute(argument, commandObjectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean removeGreater(String argument, Object commandObjectArgument, User user) {
        return removeGreaterCommand.execute(argument, commandObjectArgument, user);
    }

    /**
     * Prints the history of used commands.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean history(String argument, Object commandObjectArgument, User user) {
        if (historyCommand.execute(argument, commandObjectArgument, user)) {
            try {
                if (commandHistory.length == 0) throw new HistoryIsEmptyException();

                ResponseOutputer.appendln("Последние использованные команды:");
                for (int i=0; i<commandHistory.length; i++) {
                    if (commandHistory[i] != null) ResponseOutputer.appendln(" " + commandHistory[i]);
                }
                return true;
            } catch (HistoryIsEmptyException exception) {
                ResponseOutputer.appendln("Ни одной команды еще не было использовано!");
            }
        }
        return false;
    }


    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean sumOfTransferredStudents(String argument, Object commandObjectArgument, User user) {
        locker.lock();
        try {
            return sumOfTransferredStudents.execute(argument, commandObjectArgument, user);
        } finally {
            locker.unlock();
        }
    }
    public boolean printFieldDescendingTransferredStudents(String argument, Object commandObjectArgument, User user){
        locker.lock();
        try {
            return printFieldDescendingTransferredStudents.execute(argument, commandObjectArgument, user);
        } finally {
            locker.unlock();
        }
    }


    /**
     * Executes needed command.
     * @param argument Its argument.
     * @param commandObjectArgument
     * @return Command exit status.
     */
    public boolean filterByFormOfEducation(String argument, Object commandObjectArgument, User user) {
        locker.lock();
        try {
            return filterByFormOfEducationCommand.execute(argument, commandObjectArgument, user);
        } finally{
            locker.unlock();
        }
    }

    public boolean register(String argument, Object objectArgument,User user){
        return registerCommand.execute(argument, objectArgument, user);
    }
    public boolean login(String argument, Object objectArgument, User user){
        return loginCommand.execute(argument,objectArgument,user);
    }

    public boolean serverExit(String stringArgument, Object objectArgument, User user) {return serverExitCommand.execute(stringArgument,objectArgument, user);}

    @Override
    public String toString() {
        return "CommandManager (вспомогательный класс для работы с командами)";
    }
}

