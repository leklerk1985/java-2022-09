package ru.otus.cachehw.mycache;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    Map<K, V> data = new WeakHashMap<>();
    List<HwListener<K, V>> listeners = new ArrayList<>();

//Надо реализовать эти методы

    @Override
    public void put(K key, V value) {
        data.put(key, value);
    }

    @Override
    public void remove(K key) {
        data.remove(key);
    }

    @Override
    public V get(K key) {
        return data.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}