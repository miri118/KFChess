package impl.model.command;

import java.util.ArrayList;
import java.util.List;

import interfaces.command.CommandListenerInterface;

public class CommandDispatcher{

    private final List<CommandListenerInterface> listeners = new ArrayList<>();
    
    public void register(CommandListenerInterface listener) {
        listeners.add(listener);
    }

    public void dispatch(Command cmd) {
        for (CommandListenerInterface listener : listeners) {
            if (listener.supports(cmd)) {
                listener.handle(cmd);
                return;
            }
        }
        throw new IllegalStateException("No listener found for command: " + cmd.getType());
    }
}