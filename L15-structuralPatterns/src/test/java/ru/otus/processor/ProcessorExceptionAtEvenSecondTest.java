package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessorExceptionAtEvenSecondTest {
    @Test
    @DisplayName("Тестируем процессор, выбрасывающий исключение в чётную секунду")
    void Test() {
        // Время выполнения программы не превышает 100 миллисекунд, поэтому для того, чтобы выполнение процессора пришлось на чётную секунду,
        // достаточно дождаться начала чётной секунды.
        while (System.currentTimeMillis() % 2000 != 0) {
        }

        var processors = List.of((Processor) new ProcessorExceptionAtEvenSecond());
        var historyListener = new HistoryListener();
        var complexProcessor = new ComplexProcessor(
                processors,
                ex -> {historyListener.addToHistory(new Message.Builder(1L).build());});

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

        assertThat(historyListener.findMessageById(1L).isPresent() == true).isTrue();
    }
}
