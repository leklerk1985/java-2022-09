package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public static ObjectForMessage createCopy(ObjectForMessage obj) {
        var copy = new ObjectForMessage();
        if (obj != null && obj.data != null)
            copy.data = new ArrayList<>(obj.data);

        return copy;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}