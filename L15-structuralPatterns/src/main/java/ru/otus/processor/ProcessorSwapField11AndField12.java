package ru.otus.processor;

import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;

public class ProcessorSwapField11AndField12 implements Processor {
    @Override
    public Message process(Message message) {
        String originalField11 = message.getField11();
        String originalField12 = message.getField12();
        return message.toBuilder().field11(originalField12).field12(originalField11).build();
    }
}
