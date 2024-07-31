package com.aat.application.core.event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class EventBus {
    private static EventBus instance;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Queue<Consumer<Object>> listeners = new ConcurrentLinkedQueue<>();

    public static synchronized EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public void post(Object event) {
        executor.execute(() -> listeners.forEach(listener -> listener.accept(event)));
    }

    public void register(Consumer<Object> listener) {
        listeners.add(listener);
    }

    public void unregister(Consumer<Object> listener) {
        listeners.remove(listener);
    }
}