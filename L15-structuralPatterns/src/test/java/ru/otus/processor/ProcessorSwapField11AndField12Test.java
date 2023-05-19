package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessorSwapField11AndField12Test {
    @Test
    @DisplayName("Тестируем процессор, переставляющий Field11 и Field12")
    void Test() {
        var processors = List.of((Processor) new ProcessorSwapField11AndField12());
        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
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
        complexProcessor.removeListener(listenerPrinter);

        String field11Result = result.getField11();
        String field12Result = result.getField12();
        String field11Message = message.getField11();
        String field12Message = message.getField12();

        assertThat(field11Result.equals(field12Message)).isTrue();
        assertThat(field12Result.equals(field11Message)).isTrue();
    }
}
