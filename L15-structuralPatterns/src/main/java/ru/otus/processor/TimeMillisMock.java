package ru.otus.processor;

import java.util.ArrayList;
import java.util.List;

public class TimeMillisMock implements TimeMillisProvider {
    List<String> errors = new ArrayList<>();

    @Override
    public long getTimeMillis() {
        return 2000L;
    }

    public void writeError(String error) {
        errors.add(error);
    }

    public boolean verify() {
        return errors.size() == 1 && errors.get(0).equals("Ошибка в чётную секунду!!!");
    }
}
