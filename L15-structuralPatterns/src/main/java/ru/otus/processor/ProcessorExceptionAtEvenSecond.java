package ru.otus.processor;

import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;

import java.util.function.Supplier;

public class ProcessorExceptionAtEvenSecond implements Processor {
    private final TimeMillisProvider timeMillis;

    public ProcessorExceptionAtEvenSecond(TimeMillisProvider timeMillis) {
        this.timeMillis = timeMillis;
    }

    @Override
    public Message process(Message message) {
        long currentTimeSeconds = (long) Math.floor(timeMillis.getTimeMillis() / 1000);
        boolean secondIsEven = (currentTimeSeconds % 2) == 0;
        if (secondIsEven)
            throw new RuntimeException("Ошибка в чётную секунду!!!");

        return message;
    }
}
