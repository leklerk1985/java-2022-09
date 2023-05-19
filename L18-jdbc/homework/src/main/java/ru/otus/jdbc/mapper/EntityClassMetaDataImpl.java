package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplateException;
import ru.otus.crm.model.ForJDBC;
import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private Class<T> clazz;
    private String className;
    private Constructor<T> constructor;
    private Field idField;
    private List<Field> allFields;
    private List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        setAllFields();
        this.constructor = getConstructor();
    }

    @Override
    public String getName() {
        if (className == null) {
            String tooLongName = clazz.getName();
            int lastIndexDot = tooLongName.lastIndexOf(".");
            className = tooLongName.substring(lastIndexDot + 1).toLowerCase();
        }

        return className;
    }

    @Override
    public Constructor<T> getConstructor() {
        if (constructor == null) {
            try {
                Constructor[] constructors = clazz.getDeclaredConstructors();
                for (var currConstructor: constructors) {
                    if (currConstructor.isAnnotationPresent(ForJDBC.class)) {
                        constructor = currConstructor;
                        break;
                    }
                }
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }

        return constructor;
    }

    @Override
    public Field getIdField() {
        if (idField == null) {
            var allFields = getAllFields();
            for (Field field: allFields) {
                if (field.isAnnotationPresent(Id.class)) {
                    idField = field;
                    break;
                }
            }

            if (idField == null) throw new DataTemplateException(new RuntimeException("Поле Id для класса " + clazz.getName() + " не найдено!"));
        }

        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (fieldsWithoutId == null) {
            fieldsWithoutId = new ArrayList<>();

            var allFields = getAllFields();
            for (Field field: allFields) {
                if (!field.isAnnotationPresent(Id.class)) {
                    fieldsWithoutId.add(field);
                }
            }
        }

        return fieldsWithoutId;
    }

    private void setAllFields() {
        if (this.allFields != null && this.allFields.size() != 0) return;

        Field[] allFields = clazz.getDeclaredFields();
        Field[] sortedAllFields = new Field[allFields.length];
        Field idField = null;
        int currIndex = 0;

        for (Field field: allFields) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            } else {
                sortedAllFields[currIndex] = field;
                currIndex++;
            }
        }
        if (idField == null) throw new DataTemplateException(new RuntimeException("Поле Id для класса " + clazz.getName() + " не найдено!"));

        Arrays.sort(sortedAllFields, new FieldComparator());
        sortedAllFields[currIndex] = idField;

        this.allFields = new ArrayList<>(Arrays.asList(sortedAllFields));
    }

    class FieldComparator implements Comparator<Field> {
        @Override
        public int compare(Field field1, Field field2) {
            return (field1 != null && field2 != null) ? field1.getName().compareTo(field2.getName()) : 0;
        }
    }
}
