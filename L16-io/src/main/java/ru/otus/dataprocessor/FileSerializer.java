package ru.otus.dataprocessor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import com.google.gson.Gson;

public class FileSerializer implements Serializer {
    private String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл

        try {
            var gson = new Gson();
            String jsonText = gson.toJson(data);
            writeJsonFile(jsonText);
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }

    private void writeJsonFile(String jsonText) throws IOException {
        try (var bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(jsonText);
        }
    }
}