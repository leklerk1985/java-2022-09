package ru.otus.dataprocessor;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import ru.otus.model.Measurement;

import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private String fileName;
    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        //return null;

        try (var jsonReader = Json.createReader(ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName))) {
            JsonStructure jsonFromTheFile = jsonReader.read();
            JsonArray jsonArray = jsonFromTheFile.asJsonArray();
            List<Measurement> listMsr = new ArrayList<>();
            Measurement newMsr;
            JsonObject currObj;
            String currName;
            double currValue;

            for (int i = 0; i < jsonArray.size(); i++) {
                currObj = jsonArray.getJsonObject(i);
                currName = currObj.getString("name");
                currValue = currObj.getJsonNumber("value").doubleValue();

                newMsr = new Measurement(currName, currValue);
                listMsr.add(newMsr);
            }

            return listMsr;
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}