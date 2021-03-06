package client.utility;

import Lab5.common.interactions.User;
import client.App;
import Lab5.common.data.*;
import Lab5.common.exceptions.CommandUsageException;
import Lab5.common.exceptions.IncorrectInputInScriptException;
import Lab5.common.exceptions.ScriptRecursionException;
import Lab5.common.interactions.Request;
import Lab5.common.interactions.GroupRaw;
import Lab5.common.interactions.ResponseCode;
import Lab5.common.utility.Outputer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class UserHandler {
    private final int maxRewriteAttempts =1;
    private Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();

    public UserHandler(Scanner userScanner){
        this.userScanner = userScanner;
    }

    /**
     * Receives user input
     *
     * @param serverResponseCode
     * @param user
     * @return
     */
    public Request handle(ResponseCode serverResponseCode, User user){
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try{
            do{
                try{
                    if(fileMode() && (serverResponseCode == ResponseCode.ERROR || serverResponseCode == ResponseCode.SERVER_EXIT))
                        throw new IncorrectInputInScriptException();
                    while (fileMode() && !userScanner.hasNextLine()){
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        scriptStack.pop();
                    }
                    if(fileMode()){
                        userInput = userScanner.nextLine();
                        if(!userInput.isEmpty()){
                            Outputer.print(App.PS1);
                            Outputer.println(userInput);
                        }
                    } else {
                        Outputer.print(App.PS1);
                        userInput = userScanner.nextLine();
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    Outputer.println();
                    Outputer.printerror("?????????????????? ???????????? ?????? ?????????? ??????????????!");
                    userCommand = new String[]{"",""};
                    rewriteAttempts++;
                    if(rewriteAttempts >= maxRewriteAttempts){
                        Outputer.printerror("?????????????????? ???????????????????? ?????????????? ??????????!");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0],userCommand[1]);
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try{
                if(fileMode() && (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR))
                    throw new IncorrectInputInScriptException();
                switch(processingCode){
                    case OBJECT:
                        GroupRaw groupAddRaw = generateGroupAdd();
                        return new Request(userCommand[0], userCommand[1],groupAddRaw,user);
                    case UPDATE_OBJECT:
                        GroupRaw groupUpdateRaw = generateGroupUpdate();
                        return new Request(userCommand[0],userCommand[1],groupUpdateRaw, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if(!scriptFile.exists()) throw new FileNotFoundException();
                        if(!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        Outputer.println("???????????????? ????????????: '" + scriptFile.getName() + "'...");
                        break;
                }
            }  catch (FileNotFoundException e){
                Outputer.printerror("???????? ???? ???????????????? ???? ????????????");
            }   catch (ScriptRecursionException e){
                Outputer.printerror("?????????????? ???? ?????????? ???????????????????? ????????????????????!");
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException e){
            Outputer.printerror("?????????????????? ?????????????? ????????????????!");
            while  (!scannerStack.isEmpty()){
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scannerStack.clear();
            return new Request(user);
        }
        return new Request(userCommand[0],userCommand[1],null,user);
    }

    private ProcessingCode processCommand(String command, String commandArgument){
        try{
            switch (command){
                case "":
                    return ProcessingCode.ERROR;
                case "help":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "info":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "show":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "update":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                case "remove_by_id":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID>");
                    break;
                case "clear":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "execute_script":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                case "exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add_if_min":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "remove_greater":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "history":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "remove_by_semester":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "sum_of_transferred_students":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "filter_by_form_of_education_type":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<form_of_education_type>");
                    break;
                case "server_exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                default:
                    Outputer.println("?????????????? '" + command + "' ???? ??????????????. ???????????????? 'help' ?????? ??????????????.");
                    return ProcessingCode.ERROR;
            }

        }catch (CommandUsageException e){
            if(e.getMessage() != null) command+=" " + e.getMessage();
            Outputer.println("??????????????????????????: '" + command + "'");
            return  ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }
    private GroupRaw generateGroupAdd() throws IncorrectInputInScriptException{
        GroupAsker groupAsker = new GroupAsker(userScanner);
        if(fileMode()) groupAsker.setFileMode();
        return  new GroupRaw(
                        groupAsker.askName(),
                        groupAsker.askCoordinates(),
                        groupAsker.askStundentsCount(),
                        groupAsker.askFormOfEducation(),
                        groupAsker.askTransferredStundentsCount(),
                        groupAsker.askSemester(),
                        groupAsker.askGroupAdmin()
                );
    }
    private GroupRaw generateGroupUpdate() throws IncorrectInputInScriptException{
        GroupAsker groupAsker = new GroupAsker(userScanner);
        if(fileMode()) groupAsker.setFileMode();
        String name = groupAsker.askQuestion("???????????? ???????????????? ?????? ????????????? ")?
                groupAsker.askName() : null;
        Coordinates coordinates = groupAsker.askQuestion("???????????? ???????????????? ???????????????????? ??????????????? ")?
                groupAsker.askCoordinates() : null;
        Long studentsCount = groupAsker.askQuestion("???????????? ???????????????? ?????????? ???????????????????") ?
                groupAsker.askStundentsCount() : -1;
        int transferredStudents = groupAsker.askQuestion("???????????? ???????????????? ?????????? ???????????????????????? ???????????????????")?
                groupAsker.askTransferredStundentsCount() : -1;
        Semester semester = groupAsker.askQuestion("???????????? ???????????????? ?????????? ????????????????? ")?
                groupAsker.askSemester():null;
        FormOfEducation formOfEducation = groupAsker.askQuestion("???????????? ???????????????? ?????????? ?????????????????")?
                groupAsker.askFormOfEducation():null;
        Person groupAdmin = groupAsker.askQuestion("???????????? ???????????????? ?????????? ????????????")?
                groupAsker.askGroupAdmin():null;
        return new GroupRaw(
                        name,
                        coordinates,
                        studentsCount,
                formOfEducation,
                transferredStudents,
                semester,
                groupAdmin
                );
    }
    private boolean fileMode(){
        return !scannerStack.isEmpty();
    }

}
