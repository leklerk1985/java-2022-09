package ru.otus.processor;

public class TimeMillis implements TimeMillisProvider {
    @Override
    public long getTimeMillis() {
        return System.currentTimeMillis();
    }
}
