package interfaces;

import impl.command.*;;

public interface ISubject {
    void AddObserver(IObserver observer);

    void RemoveObserver(IObserver observer);

    void NotifyAll(Command cmd);
}
