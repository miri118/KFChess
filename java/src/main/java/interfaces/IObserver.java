package interfaces;

import impl.model.command.*;;

public interface IObserver {
    public void notify(Command cmd);
}
