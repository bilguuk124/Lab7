package server.utility;

import Lab5.common.interactions.Request;
import Lab5.common.interactions.Response;
import Lab5.common.interactions.ResponseCode;
import Lab5.common.interactions.User;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.RecursiveTask;


public class RequestHandler extends RecursiveTask<Response> {
    private CommandManager commandManager;
    private Request request;

    public RequestHandler(Request request ,CommandManager commandManager){
        this.request = request;
        this.commandManager = commandManager;
    }

    @Override
    public Response compute(){
        User hashUser;
        if(request.getUser() == null ){
            hashUser = null;
        }else{
            hashUser = new User(
                    request.getUser().getUsername(), DataHasher.hash(request.getUser().getPassword()));
            commandManager.addToHistory(request.getCommandName(), request.getUser());
        }
        ResponseCode responseCode = executeCommand(request.getCommandName(),request.getCommandStringArgument(),
                request.getCommandObjectArgument(), hashUser);
        return new Response(responseCode, ResponseOutputer.getAndClear());
    }
    private synchronized ResponseCode executeCommand(String command, String commandStringArgument, Object commandObjectArgument, User user){
        switch(command){
            case "":
                break;
            case "help":
                if(!commandManager.help(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "info":
                if(!commandManager.info(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "show":
                if(!commandManager.show(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "add":
                if(!commandManager.add(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "update":
                if(!commandManager.update(commandStringArgument,commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_any_by_semester_enum":
                if(!commandManager.removeAnyBySemesterEnum(commandStringArgument,commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if(!commandManager.removeById(commandStringArgument,commandObjectArgument, user))
                    return  ResponseCode.ERROR;
                break;
            case "clear":
                if(!commandManager.clear(commandStringArgument,commandObjectArgument, user))
                    return  ResponseCode.ERROR;
                break;

            case "execute_script":
                if(!commandManager.executeScript(commandStringArgument,commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "add_if_min":
                if(!commandManager.addIfMin(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_greater":
                if(!commandManager.removeGreater(commandStringArgument,commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "history":
                if(!commandManager.history(commandStringArgument,commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "sum_of_transferred_students":
                if(!commandManager.sumOfTransferredStudents(commandStringArgument,commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "filter_by_form_of_education":
                if(!commandManager.filterByFormOfEducation(commandStringArgument,commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "server_exit":
                if(!commandManager.serverExit(commandStringArgument,commandObjectArgument, user))
                    return ResponseCode.ERROR;
                return ResponseCode.SERVER_EXIT;
            case "register":
                if(!commandManager.register(commandStringArgument,commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "login":
                if(!commandManager.login(commandStringArgument,commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            default:
                ResponseOutputer.appendln("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                return ResponseCode.ERROR;
        } return ResponseCode.OK;
    }

}
