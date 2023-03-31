package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessorExceptionAtEvenSecondTest {
    @Test
    @DisplayName("Тестируем процессор, выбрасывающий исключение в чётную секунду")
    void Test() {
        var processors = List.of((Processor) new ProcessorExceptionAtEvenSecond(()->{return 2000L;}));
        var historyListener = new HistoryListener();
        var complexProcessor = new ComplexProcessor(processors, ex -> {historyListener.addToHistory(new Message.Builder(1L).field1(ex.getMessage()).build());});
        complexProcessor.addListener(historyListener);

        var message = new Message.Builder(2L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field4("field4")
                .field5("field5")
                .field6("field6")
                .field7("field7")
                .field8("field8")
                .field9("field9")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(new ObjectForMessage())
                .build();

        var result = complexProcessor.handle(message);
        complexProcessor.removeListener(historyListener);

        var msgOpt = historyListener.findMessageById(1L);
        assertThat(msgOpt.isPresent()).isTrue();
        assertThat(msgOpt.get().getField1().equals("Ошибка в чётную секунду!!!")).isTrue();
    }
}
