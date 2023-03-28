package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        //return null;

        try {
            Map<String, Double> result = new TreeMap<>();
            String elementName;
            Double elementValue, resultValue, newValue;
            for (var element : data) {
                elementName = element.getName();
                elementValue = element.getValue();
                resultValue = result.get(elementName);
                newValue = resultValue != null ? resultValue + elementValue : elementValue;

                result.put(elementName, newValue);
            }

            return result;
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}