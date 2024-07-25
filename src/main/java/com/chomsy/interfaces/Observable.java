package com.chomsy.interfaces;

import java.util.LinkedList;
import java.util.List;

public abstract class Observable<T> {
    private List<Observer<T>> observers;

    public Observable() {
        observers = new LinkedList<>();
    }

    public void subscribe(Observer<T> newObserver) {
        observers.add(newObserver);
    }

    public void notifyListeners(T input) {
        observers.forEach(observer -> observer.update(input));
    }
}
