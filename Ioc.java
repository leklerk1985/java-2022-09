package homework;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class Ioc {
    private Ioc() {
    }
    static <T> T createMyClass(String className, String interfaceName) throws Exception {
        Class clazzCls = Class.forName(className);
        Class clazzInf = Class.forName(interfaceName);
        Constructor constrCls = clazzCls.getConstructor();

        InvocationHandler handler = new DemoInvocationHandler(constrCls.newInstance(), clazzCls, clazzInf);
        return (T) Proxy.newProxyInstance(clazzInf.getClassLoader(),
                new Class<?>[]{clazzInf}, handler);
    }

    static class DemoInvocationHandler<T> implements InvocationHandler {
        private final T myClass;
        private final Map<Method, Boolean> areAnnotationsPresent = new HashMap<>();

        DemoInvocationHandler(T myClass, Class clazzCls, Class clazzInf) throws NoSuchMethodException {
            this.myClass = myClass;

            // Заполним areAnnotationsPresent.
            Method[] methodsInf = clazzInf.getDeclaredMethods();
            Method correspondingMethod;
            boolean isAnnotationPresent;
            for (var method: methodsInf) {
                correspondingMethod = clazzCls.getDeclaredMethod(method.getName(), method.getParameterTypes());
                isAnnotationPresent = correspondingMethod.isAnnotationPresent(Log.class);
                this.areAnnotationsPresent.put(method, isAnnotationPresent);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (areAnnotationsPresent.get(method)) {
                String logMessage = makeLogMessage(args);
                System.out.println(logMessage);
            }
            return method.invoke(myClass, args);
        }

        private String makeLogMessage(Object[] args) {
            String msg = "executed method: calculation; ";
            for (int i = 0; i < args.length; i++) {
                msg += "param" + (i + 1) + ": " + args[i] + ", ";
            }
            return msg.substring(0, msg.length() - 2);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
