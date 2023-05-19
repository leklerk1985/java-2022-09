package ru.otus.cachehw.hw18.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.hw18.core.repository.DataTemplate;
import ru.otus.cachehw.hw18.core.repository.DataTemplateException;
import ru.otus.cachehw.hw18.core.sessionmanager.TransactionRunner;
import ru.otus.cachehw.hw18.crm.model.Client;
import ru.otus.cachehw.hw18.crm.model.Id;
import ru.otus.cachehw.mycache.MyCache;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final MyCache<String, Client> cache = new MyCache<>();

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                saveToCache(createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = getFromCache(id);
        if (client != null)
            return Optional.of(client);

        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    private void saveToCache(Client client) {
        Class<Client> clazz = Client.class;
        Field idField = null;
        Field[] allFields = clazz.getDeclaredFields();
        String idValue;
        String getterName;
        Method getter;

        for (Field field: allFields) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
                break;
            }
        }
        if (idField == null) throw new DataTemplateException(new RuntimeException("Поле Id для класса Client не найдено!"));

        try {
            getterName = "get" + idField.getName().substring(0, 1).toUpperCase() + idField.getName().substring(1);
            getter = clazz.getDeclaredMethod(getterName);
            idValue = getter.invoke(client).toString().intern();
            cache.put(idValue, client);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private Client getFromCache(long id) {
        return cache.get(String.valueOf(id));
    }
}