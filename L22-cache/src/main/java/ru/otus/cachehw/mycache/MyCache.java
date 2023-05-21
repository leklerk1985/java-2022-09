package ru.otus.cachehw.mycache;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> data = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

//Надо реализовать эти методы

    @Override
    public void put(K key, V value) {
        data.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        data.remove(key);
        notifyListeners(key, null, "remove");
    }

    @Override
    public V get(K key) {
        V value = data.get(key);
        notifyListeners(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        for (var listener: listeners) {
            listener.notify(key, value, action);
        }
    }
}