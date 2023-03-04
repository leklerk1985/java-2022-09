package homework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestLogging implements TestLoggingInterface {

    public static TestLoggingInterface createInstanceUsingProxy() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(TestLogging.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;

        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
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
            return myClass.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        }

        private String makeLogMessage(Object[] args) {
            String msg = "executed method: calculation; ";
            for (int i=0; i<args.length; i++) {
                msg += "param" + (i+1) + ": " + args[i] + ", ";
            }
            return msg.substring(0, msg.length()-2);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }

    @Log @Override
    public void calculation(int param1) {
        System.out.println("calculation с одним параметром\n");
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.println("calculation с двумя параметрами\n");
    }

    @Override
    public void calculation(int param1, int param2, int param3) {
        System.out.println("calculation с тремя параметрами\n");
    }

    @Override
    public String toString() {
        return "TestLogging{}";
    }
}
