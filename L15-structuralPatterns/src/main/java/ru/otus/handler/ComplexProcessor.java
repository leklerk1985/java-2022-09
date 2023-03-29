package ru.otus.handler;

import ru.otus.listener.homework.HistoryListener;
import ru.otus.listener.homework.HistoryReader;
import ru.otus.model.Message;
import ru.otus.listener.Listener;
import ru.otus.processor.Processor;

import java.util.*;
import java.util.function.Consumer;

public class ComplexProcessor implements Handler {

    //private final List<Listener> listeners = new ArrayList<>();
    private final Map<String, List<Listener>> listeners;
    private final List<Processor> processors;
    private final Consumer<Exception> errorHandler;

    public ComplexProcessor(List<Processor> processors, Consumer<Exception> errorHandler) {
        this.processors = processors;
        this.errorHandler = errorHandler;

        listeners = new HashMap<>();
        listeners.put("history", new ArrayList<>());
        listeners.put("others", new ArrayList<>());
    }

    @Override
    public Message handle(Message msg) {
        Message newMsg = msg;
        for (Processor pros : processors) {
            try {
                newMsg = pros.process(newMsg);
            } catch (Exception ex) {
                errorHandler.accept(ex);
            }
        }
        notify(newMsg);
        return newMsg;
    }

    @Override
    public void addListener(Listener listener) {
        //listeners.add(listener);

        if (listener instanceof HistoryListener) {
            if (listeners.get("history").size() == 0)
                listeners.get("history").add(listener);
            else
                throw new RuntimeException("Экземпляр класса истории уже заведён!");
        } else {
            listeners.get("others").add(listener);
        }
    }

    @Override
    public void removeListener(Listener listener) {
        //listeners.remove(listener);
        listeners.get(listener instanceof HistoryListener ? "history" : "others").remove(listener);
    }

    private void notify(Message msg) {
        List<Listener> allListeners = new ArrayList<>();
        allListeners.addAll(listeners.get("history"));
        allListeners.addAll(listeners.get("others"));

        allListeners.forEach(listener -> {
            try {
                listener.onUpdated(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}