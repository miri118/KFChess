package interfaces;

import impl.command.*;;

public interface IObserver {
    public void notify(Command cmd);
}
