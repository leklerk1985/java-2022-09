package ru.otus.processor;

import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;

public class ProcessorExceptionAtEvenSecond implements Processor {
    @Override
    public Message process(Message message) {
        long currentTimeSeconds = (long) Math.floor(System.currentTimeMillis() / 1000);
        boolean secondIsEven = (currentTimeSeconds % 2) == 0;
        if (secondIsEven)
            throw new RuntimeException("Ошибка в чётную секунду!!!");

        return message;
    }
}
