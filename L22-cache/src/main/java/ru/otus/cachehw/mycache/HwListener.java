package ru.otus.cachehw.mycache;


public interface HwListener<K, V> {
    void notify(K key, V value, String action);
}