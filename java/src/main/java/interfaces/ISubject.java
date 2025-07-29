package interfaces;

import impl.model.command.*;;

public interface ISubject {
    void AddObserver(IObserver observer);

    void RemoveObserver(IObserver observer);

    void NotifyAll(Command cmd);
}
