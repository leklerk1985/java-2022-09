package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {
    private final TreeMap<Long, Message> history = new TreeMap<>();

    @Override
    public void onUpdated(Message msg) {
        //throw new UnsupportedOperationException();

        // Взято из метода onUpdated класса ListenerPrinterConsole.
        var logString = String.format("oldMsg:%s", msg);
        System.out.println(logString);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        //throw new UnsupportedOperationException();
        return Optional.ofNullable(history.get(id));
    }

    public void addToHistory(Message msg) {
        history.put(msg.getId(), new Message(msg));
    }

    public long calculateUniqueId() {
        long lastKey = !history.isEmpty() ? history.lastKey() : 0;
        return lastKey + 1;
    }

    public void printHistory() {
        for (var entry: history.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue() + "\n");
        }
    }
}