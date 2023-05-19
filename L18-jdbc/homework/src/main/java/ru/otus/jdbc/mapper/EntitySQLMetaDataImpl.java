package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        var name = entityClassMetaData.getName();
        var allFields = entityClassMetaData.getAllFields();
        var query = "select ";

        for (Field field: allFields) {
            query += field.getName() + ", ";
       }
        query = query.substring(0, query.length() - 2);
        query += " from " + name;

       return query;
    }

    @Override
    public String getSelectByIdSql() {
        var fieldId = entityClassMetaData.getIdField();
        var query = getSelectAllSql();
        query += " where " + fieldId.getName() + " = ?";

        return query;
    }

    @Override
    public String getInsertSql() {
        var query = "insert into " + entityClassMetaData.getName();
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var columns = "(";
        var values = "(";

        for (Field field: fieldsWithoutId) {
            columns += field.getName() + ", ";
            values += "?, ";
        }
        columns = columns.substring(0, columns.length() - 2);
        columns += ")";
        values = values.substring(0, values.length() - 2);
        values += ")";
        query += columns + " values " + values;

        return query;
    }

    @Override
    public String getUpdateSql() {
        var fieldId = entityClassMetaData.getIdField();
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var query = "update " + entityClassMetaData.getName() + " set ";

        var columns = "";
        for (Field field: fieldsWithoutId)
            columns += field.getName() + " = ?, ";
        columns = columns.substring(0, columns.length() - 2);

        query += columns;
        query += " where " + fieldId.getName() + " = ?";

        return query;
    }
}
