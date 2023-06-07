package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> configClass) {
        Method[] componentMethods = getComponentMethods(configClass);

        checkConfigClass(componentMethods, configClass);
        processConfig(componentMethods, configClass);
    }

    public AppComponentsContainerImpl(Class<?>... configClasses) {
        var allComponentMethods = getAllComponentMethods(configClasses);

        checkConfigClasses(allComponentMethods, configClasses);

        Arrays.sort(configClasses, new ComparatorConfigClassByOrder());
        for (var configClass: configClasses) {
            processConfig(allComponentMethods.get(configClass), configClass);
        }
    }

    private void processConfig(Method[] componentMethods, Class<?> configClass) {
        Object configObject = null;

        try {
            configObject = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        for (var method: componentMethods) {
            createComponent(method, configObject);
        }
    }

    private Map<Class<?>, Method[]> getAllComponentMethods(Class<?>... configClasses) {
        Map<Class<?>, Method[]> allComponentMethods = new HashMap<>();
        Method[] componentMethods;

        for (var configClass: configClasses) {
            componentMethods = getComponentMethods(configClass);
            allComponentMethods.put(configClass, componentMethods);
        }

        return allComponentMethods;
    }

    private void checkConfigClass(Method[] componentMethods, Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }

        if (inConfigClassExistComponentsWithSameName(componentMethods)) {
            throw new IllegalStateException("In config class exist components with the same name!");
        }
    }

    private void checkConfigClasses(Map<Class<?>, Method[]> allComponentMethods, Class<?>... configClasses) {
        for (var configClass: configClasses) {
            if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
            }
        }

        if (inConfigClassesExistComponentsWithSameName(allComponentMethods)) {
            throw new IllegalStateException("In config class exist components with the same name!");
        }
    }

    private boolean inConfigClassesExistComponentsWithSameName(Map<Class<?>, Method[]> allComponentMethods) {
        List<String> componentNames = new ArrayList<>();
        String componentName;

        for (var methodsArray: allComponentMethods.values()) {
            for (var method: methodsArray) {
                componentName = method.getAnnotation(AppComponent.class).name();
                if (!componentNames.contains(componentName)) {
                    componentNames.add(componentName);
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean inConfigClassExistComponentsWithSameName(Method[] componentMethods) {
        List<String> componentNames = new ArrayList<>();
        String componentName;

        for (var method: componentMethods) {
            componentName = method.getAnnotation(AppComponent.class).name();
            if (!componentNames.contains(componentName)) {
                componentNames.add(componentName);
            } else {
                return true;
            }
        }

        return false;
    }

    private void createComponent(Method method, Object configObject) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] paramValues = new Object[paramTypes.length];
        Object paramComponent;
        Object newComponent;
        String componentName;
        int currIdx = 0;

        for (var paramType: paramTypes) {
            paramComponent = getAppComponent(paramType);
            paramValues[currIdx] = paramComponent;
            currIdx++;
        }

        try {
            newComponent = method.invoke(configObject, paramValues);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        componentName = method.getAnnotation(AppComponent.class).name();
        appComponentsByName.put(componentName, newComponent);
        appComponents.add(newComponent);
    }

    private Method[] getComponentMethods(Class<?> configClass) {
        Method[] declaredMethods;
        Method[] componentMethods;
        List<Method> componentMethodsList = new ArrayList<>();

        declaredMethods = configClass.getDeclaredMethods();
        for (var method: declaredMethods) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                componentMethodsList.add(method);
            }
        }

        componentMethods = componentMethodsList.toArray(new Method[0]);
        Arrays.sort(componentMethods, new ComparatorMethodByOrder());

        return componentMethods;
    }

    static class ComparatorMethodByOrder implements Comparator<Method> {
        @Override
        public int compare(Method methodFirst, Method methodSecond) {
            int orderFirst = methodFirst.getAnnotation(AppComponent.class).order();
            int orderSecond = methodSecond.getAnnotation(AppComponent.class).order();

            return Integer.compare(orderFirst, orderSecond);
        }
    }

    static class ComparatorConfigClassByOrder implements Comparator<Class<?>> {
        @Override
        public int compare(Class<?> configClassFirst, Class<?> configClassSecond) {
            int orderFirst = configClassFirst.getAnnotation(AppComponentsContainerConfig.class).order();
            int orderSecond = configClassSecond.getAnnotation(AppComponentsContainerConfig.class).order();

            return Integer.compare(orderFirst, orderSecond);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<C> components = new ArrayList<>();
        for (var component: appComponents) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                components.add((C) component);
            }
        }

        if (components.size() == 0) {
            throw new IllegalStateException("The necessary component doesn't exist in the context!");
        } else if (components.size() >= 2) {
            throw new IllegalStateException("In context exist more than one necessary component!");
        }

        return components.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        C component = (C) appComponentsByName.get(componentName);
        if (component == null) {
            throw new IllegalStateException("The necessary component doesn't exist in the context!");
        }

        return component;
    }
}