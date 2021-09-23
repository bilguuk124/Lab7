package Lab5.common.interactions;

import java.io.Serializable;


/**
 * Class for get request value.
 */
public class Request implements Serializable {
    private String commandName;
    private String commandStringArgument;
    private Serializable commandObjectArgument;
    private User user;

    public Request(String commandName, String commandStringArgument, Serializable commandObjectArgument, User user) {
        this.commandName = commandName;
        this.commandStringArgument = commandStringArgument;
        this.commandObjectArgument = commandObjectArgument;
        this.user = user;
    }

    public Request(String commandName, String commandStringArgument, User user) {
        this.commandName = commandName;
        this.commandStringArgument = commandStringArgument;
        this.commandObjectArgument = null;
        this.user = user;
    }
    public Request(String commandName, String commandStringArgument){
        this.commandName = commandName;
        this.commandStringArgument = commandStringArgument;
        this.commandObjectArgument = null;
        this.user = null;
    }

    public Request(User user) {
        this("", "",null, user);
    }

    /**
     * @return Command name.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return Command string argument.
     */
    public String getCommandStringArgument() {
        return commandStringArgument;
    }

    /**
     * @return Command object argument.
     */
    public Object getCommandObjectArgument() {
        return commandObjectArgument;
    }

    public User getUser(){
        return user;
    }
    /**
     * @return Is this request empty.
     */
    public boolean isEmpty() {
        return commandName.isEmpty() && commandStringArgument.isEmpty() && commandObjectArgument == null;
    }

    @Override
    public String toString() {
        return "Request[" + commandName + ", " + commandStringArgument + ", " + commandObjectArgument + "]";
    }
}
