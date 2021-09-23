package server.commands;

import Lab5.common.interactions.User;

/**
 * Interface for all commands.
 */
public interface Command {
    String getDescription();
    String getName();
    boolean execute(String argument, Object commandObjectArgument, User user);
}
