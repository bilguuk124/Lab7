package server;

import Lab5.common.exceptions.NotInDeclaredLimitsException;
import Lab5.common.exceptions.WrongAmountOfElementsException;
import Lab5.common.utility.Outputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.commands.*;
import server.utility.*;


public class App {

    private static final int MAX_CLIENTS = 1000;
    public static int port;
    public static Logger logger = LoggerFactory.getLogger("server");
    private static String databaseUsername = "s289142";
    private static String databaseHost;
    private static String databasePassword;
    private static String databaseAddress;

    public static void main(String[] args) {
        if (!initialize(args)) return;

        DatabaseHandler databaseHandler = new DatabaseHandler(databaseAddress,databaseUsername,databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandler);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseHandler,databaseUserManager);
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new AddCommand(collectionManager,databaseCollectionManager),
                new UpdateCommand(collectionManager,databaseCollectionManager),
                new RemoveByIdCommand(collectionManager),
                new ClearCommand(collectionManager,databaseCollectionManager),
                new ExitCommand(),
                new ExecuteScriptCommand(),
                new AddIfMinCommand(collectionManager,databaseCollectionManager),
                new RemoveGreaterCommand(collectionManager),
                new HistoryCommand(),
                new SumOfTransferredStudents(collectionManager),
                new FilterByFormOfEducationCommand(collectionManager),
                new PrintFieldDescendingTransferredStudents(collectionManager),
                new RemoveAnyBySemesterEnum(collectionManager),
                new ServerExitCommand(),
                new RegisterCommand(databaseUserManager),
                new LoginCommand(databaseUserManager)
        );
        Server server = new Server(port,commandManager);
        server.run();
        databaseHandler.closeConnection();
    }
    private static boolean initialize(String[] args){
        try{
            if (args.length != 3) throw new WrongAmountOfElementsException();
            port = Integer.parseInt(args[0]);
            if (port<0) throw new NotInDeclaredLimitsException();
            databaseHost = args[1];
            databasePassword = args[2];
            databaseAddress = "jdbc:postgresql://" + databaseHost + ":5432/studs";
            return true;
        } catch (WrongAmountOfElementsException e) {
            String jarName = new java.io.File(App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Использование: 'java -jar" + " <port> <db_port> <db_password>'");
        } catch (NumberFormatException e){
            Outputer.printerror("Порт должен быть представлен числом!");
            logger.error("Порт должен быть представлен числом!");
        }
        catch (NotInDeclaredLimitsException e) {
            Outputer.printerror("Порт не может быть отрицательным!");
            logger.error("Порт не может быть отрицательным!");
        }
        logger.error("Ошибка инициализации порта запуска!");
        return false;
    }


}
