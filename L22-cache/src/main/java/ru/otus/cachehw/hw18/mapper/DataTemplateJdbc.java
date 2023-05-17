package ru.otus.cachehw.hw18.mapper;

import ru.otus.cachehw.hw18.core.repository.executor.DbExecutor;
import ru.otus.cachehw.hw18.core.repository.DataTemplate;
import ru.otus.cachehw.hw18.core.repository.DataTemplateException;
import ru.otus.cachehw.hw18.crm.model.Id;
import ru.otus.cachehw.hw18.crm.model.Name;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private DbExecutor dbExecutor;
    private EntitySQLMetaData entitySQLMetaData;
    private Constructor<T> constructor;
    private Class<T> clazz;
    private String className;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, Constructor<T> constructor, Class<T> clazz, String className) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.constructor = constructor;
        this.clazz = clazz;
        this.className = className;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        try {
            return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
                try {
                    if (rs.next()) {
                        Object[] params = calculateParamsForNewInstance(rs);
                        var newInstance = (T) constructor.newInstance(params);
                        return newInstance;
                    }
                    return null;
                } catch (Exception e) {
                    throw new DataTemplateException(e);
                }
            });
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var clientList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    Object[] params = calculateParamsForNewInstance(rs);
                    var newInstance = (T) constructor.newInstance(params);
                    clientList.add(newInstance);
                }
                return clientList;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        String queryInsert = entitySQLMetaData.getInsertSql();
        List<Object> params = calculateParamsForInsertQuery(queryInsert, object);

        try {
            return dbExecutor.executeStatement(connection, queryInsert, params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        String queryUpdate = entitySQLMetaData.getUpdateSql();
        List<Object> params = calculateParamsForUpdateQuery(queryUpdate, object);

        try {
            dbExecutor.executeStatement(connection, queryUpdate, params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private Object[] calculateParamsForNewInstance(ResultSet rs) {
        try {
            Field[] conFields = getConstructorFields();
            Object[] params = new Object[2];

            params[0] = rs.getObject(conFields[0].getName());
            params[1] = rs.getObject(conFields[1].getName());

            return params;
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private Field[] getConstructorFields() {
        Field[] declaredFields = clazz.getDeclaredFields();
        Field[] constructorFields = new Field[2];

        for (Field field: declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                constructorFields[0] = field;
            } else if (field.isAnnotationPresent(Name.class)) {
                constructorFields[1] = field;
            }
        }

        return constructorFields;
    }

    private List<Object> calculateParamsForInsertQuery(String query, T object) {
        // Формируем массив имён полей объекта.
        int begin = query.indexOf(className + "(");
        int end = query.indexOf(")", begin + 1);

        String partQuery = query.substring(begin + className.length() + 1, end);
        String[] fieldNamesDirty = partQuery.split(",");
        String[] fieldNames = new String[fieldNamesDirty.length];
        int lengthFN = fieldNames.length;

        for (int i=0; i < lengthFN; i++) {
            fieldNames[i] = fieldNamesDirty[i].trim();
        }


        // Формируем массив значений полей объекта.
        List<Object> values = new ArrayList<>();
        String getterName;
        Method getter;
        Object currValue;

        for (int i=0; i < lengthFN; i++) {
            getterName = "get" + fieldNames[i].substring(0, 1).toUpperCase() + fieldNames[i].substring(1);

            try {
                getter = clazz.getDeclaredMethod(getterName);
                currValue = getter.invoke(object);
                values.add(currValue);
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }

        return values;
    }

    private List<Object> calculateParamsForUpdateQuery(String query, T object) {
        // Формируем массив имён полей объекта.
        int begin = query.indexOf("set");
        String queryPart = query.substring(begin + 1);
        String[] fieldNamesDirty = queryPart.split("\\?");
        String[] fieldNames = new String[fieldNamesDirty.length - 1];
        int lengthFN = fieldNames.length;

        char blankSign = ' ';
        char equalSign = '=';
        char commaSign = ',';
        char emptySign = Character.MIN_VALUE;
        CharSequence whereSeq = "where".subSequence(0, "where".length());
        CharSequence emptySeq = "".subSequence(0, 1);

        String currString;
        for (int i = 0; i < lengthFN; i++) {
            currString = fieldNamesDirty[i].replace(blankSign, emptySign);
            currString = currString.replace(equalSign, emptySign);
            currString = currString.replace(commaSign, emptySign);
            currString = currString.replace(whereSeq, emptySeq);
            fieldNames[i] = currString;
        }


        // Формируем массив значений полей объекта.
        List<Object> values = new ArrayList<>();
        String getterName;
        Method getter;
        Object currValue;

        for (int i=0; i < lengthFN; i++) {
            getterName = "get" + fieldNames[i].substring(0, 1).toUpperCase() + fieldNames[i].substring(1);

            try {
                getter = clazz.getDeclaredMethod(getterName);
                currValue = getter.invoke(object);
                values.add(currValue);
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }

        return values;
    }
}