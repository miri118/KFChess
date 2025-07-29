package impl.observer;

import java.util.ArrayList;
import java.util.List;

import impl.model.command.*;
import interfaces.IObserver;
import interfaces.ISubject;

public class Subject implements ISubject {

    // List to hold observers
private final List<IObserver> observers = new ArrayList<>();

    @Override
    public void AddObserver(IObserver observer) {
        if (observer != null && !observers.contains(observer)){
            observers.add(observer);
        }
    }

    @Override
    public void RemoveObserver(IObserver observer) {
        if(observer != null && observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    @Override
    public void NotifyAll(Command cmd) {
        for (IObserver observer : observers) {
            observer.notify(cmd);
        }
    }
}
