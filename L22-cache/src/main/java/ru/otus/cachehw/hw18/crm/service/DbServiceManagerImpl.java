package ru.otus.cachehw.hw18.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.hw18.core.repository.DataTemplate;
import ru.otus.cachehw.hw18.core.repository.DataTemplateException;
import ru.otus.cachehw.hw18.core.sessionmanager.TransactionRunner;
import ru.otus.cachehw.hw18.crm.model.Client;
import ru.otus.cachehw.hw18.crm.model.Id;
import ru.otus.cachehw.hw18.crm.model.Manager;
import ru.otus.cachehw.mycache.MyCache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);
    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final MyCache<String, Manager> cache = new MyCache<>();

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                saveToCache(createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        Manager manager = getFromCache(no);
        if (manager != null)
            return Optional.of(manager);

        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }

    private void saveToCache(Manager manager) {
        Class<Manager> clazz = Manager.class;
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
        if (idField == null) throw new DataTemplateException(new RuntimeException("Поле Id для класса Manager не найдено!"));

        try {
            getterName = "get" + idField.getName().substring(0, 1).toUpperCase() + idField.getName().substring(1);
            getter = clazz.getDeclaredMethod(getterName);
            idValue = getter.invoke(manager).toString().intern();
            cache.put(idValue, manager);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private Manager getFromCache(long id) {
        return cache.get(String.valueOf(id));
    }
}