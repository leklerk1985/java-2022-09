package ru.otus.processor;

import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;

import java.util.function.Supplier;

public class ProcessorExceptionAtEvenSecond implements Processor {
    private final Supplier<Long> millis;

    ProcessorExceptionAtEvenSecond() {
        this.millis = System::currentTimeMillis;
    }

    ProcessorExceptionAtEvenSecond(Supplier<Long> millis) {
        this.millis = millis;
    }
    @Override
    public Message process(Message message) {
        long currentTimeSeconds = (long) Math.floor(millis.get() / 1000);
        boolean secondIsEven = (currentTimeSeconds % 2) == 0;
        if (secondIsEven)
            throw new RuntimeException("Ошибка в чётную секунду!!!");

        return message;
    }
}
