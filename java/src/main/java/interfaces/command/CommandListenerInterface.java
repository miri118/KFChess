package interfaces.command;

import impl.command.Command;

public interface CommandListenerInterface {
    boolean supports(Command cmd);
    void handle(Command cmd);
}
