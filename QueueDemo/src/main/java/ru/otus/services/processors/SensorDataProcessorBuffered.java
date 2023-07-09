package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final List<SensorData> bufferedData = new CopyOnWriteArrayList<>();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public synchronized void process(SensorData data) {
        bufferedData.add(data);
        if (bufferedData.size() >= bufferSize) {
            flush();
        }
    }

    public synchronized void flush() {
        if (bufferedData.size() == 0) {
            return;
        }

        sortBufferedData();
        if (bufferedData.size() <= bufferSize) {
            flushBufferedData();
        } else {
            flushPartialData();
        }
    }

    private void sortBufferedData() {
        var dataAsMap = new TreeMap<LocalDateTime, SensorData>();
        for (var data : bufferedData) {
            dataAsMap.put(data.getMeasurementTime(), data);
        }

        bufferedData.clear();
        bufferedData.addAll(dataAsMap.values());
    }

    private void flushBufferedData() {
        var copyBufferedData = new ArrayList<>(bufferedData);

        try {
            writer.writeBufferedData(copyBufferedData);
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
            return;
        }

        bufferedData.clear();
    }

    private void flushPartialData() {
        var partialData = generatePartialDataFromBufferedData();

        try {
            writer.writeBufferedData(partialData);
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
            return;
        }

        deletePartialDataFromBufferedData(partialData);
    }

    private List<SensorData> generatePartialDataFromBufferedData() {
        return bufferedData.subList(0, bufferSize);
    }

    private void deletePartialDataFromBufferedData(List<SensorData> partialData) {
        for (var data : partialData) {
            var idx = bufferedData.indexOf(data);
            if (idx != -1) {
                bufferedData.remove(idx);
            }
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}