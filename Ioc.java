package homework;

import java.lang.reflect.*;

public class Ioc {
    private Ioc() {
    }
    static <T> T createMyClass(String className, String interfaceName) throws Exception {
        Class clazzCls = Class.forName(className);
        Class clazzInf = Class.forName(interfaceName);
        Constructor constrCls = clazzCls.getConstructor();

        InvocationHandler handler = new DemoInvocationHandler(constrCls.newInstance());
        return (T) Proxy.newProxyInstance(clazzInf.getClassLoader(),
                new Class<?>[]{clazzInf}, handler);
    }

    static class DemoInvocationHandler<T> implements InvocationHandler {
        private final T myClass;
        private final Class myClazz;

        DemoInvocationHandler(T myClass) {
            this.myClass = myClass;
            this.myClazz = myClass.getClass();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method correspondingMethod = getCorrespondingMethodInClass(method);
            if (correspondingMethod.isAnnotationPresent(Log.class)) {
                String logMessage = makeLogMessage(args);
                System.out.println(logMessage);
            }
            return method.invoke(myClass, args);
        }

        private Method getCorrespondingMethodInClass(Method method) throws NoSuchMethodException {
            return myClazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
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
