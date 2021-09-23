package Lab5.common.exceptions;

public class CommandUsageException extends Exception{
    public CommandUsageException(){
        super();
    }
    public CommandUsageException(String message){
        super(message);
    }
}
