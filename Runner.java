import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Runner {
    public static void main(String[] args) throws Exception {
        runTests("Tester");
    }

    private static void runTests(String className) throws Exception {
        Class<Tester> testerClazz = Tester.class;
        Constructor<Tester> testerConstr = testerClazz.getConstructor();
        Method[] methodsAll = testerClazz.getDeclaredMethods();
        ArrayList<Method> methodsBefore = new ArrayList<>();
        ArrayList<Method> methodsAfter = new ArrayList<>();
        ArrayList<Method> methodsTest = new ArrayList<>();
        HashMap<String, Object> statistics = new HashMap<>();
        statistics.put("total", 0);
        statistics.put("success", 0);
        statistics.put("fail", 0);
        statistics.put("currentFailed", false);

        fillMethodCollections(methodsAll, methodsBefore, methodsAfter, methodsTest);
        invokeMethods(testerConstr, methodsBefore, methodsAfter, methodsTest, statistics);
        showStatistics(statistics);
    }

    private static void fillMethodCollections (Method[] methodsAll,
                                               ArrayList<Method> methodsBefore,
                                               ArrayList<Method> methodsAfter,
                                               ArrayList<Method> methodsTest) {
        for (Method method: methodsAll) {
            if (method.isAnnotationPresent(Before.class)) {
                methodsBefore.add(method);
            }
            if (method.isAnnotationPresent(After.class)) {
                methodsAfter.add(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                methodsTest.add(method);
            }
        }
    }

    private static void invokeMethods(Constructor<Tester> testerConstr,
                                              ArrayList<Method> methodsBefore,
                                              ArrayList<Method> methodsAfter,
                                              ArrayList<Method> methodsTest,
                                              HashMap<String, Object> statistics) throws Exception {
        for (Method methodT: methodsTest) {
            statistics.put("currentFailed", false);
            Tester testerObj = testerConstr.newInstance();


            for (Method methodB: methodsBefore) {
                invokeMethod(methodB, testerObj, statistics, false);
            }
            if (!(boolean) statistics.get("currentFailed")) {
                invokeMethod(methodT, testerObj, statistics, true);
            }
            for (Method methodA: methodsAfter) {
                invokeMethod(methodA, testerObj, statistics, true);
            }


            statistics.put("total", (int) statistics.get("total") + 1);
            if (!(boolean) statistics.get("currentFailed")) {
                statistics.put("success", (int) statistics.get("success") + 1);
            }
        }
    }

    private static void invokeMethod(Method method, Tester testerObj, HashMap<String, Object> statistics, boolean invokeAnyway) {
        if ((boolean) statistics.get("currentFailed") && !invokeAnyway) return;

        try {
            method.invoke(testerObj);
        } catch (Exception e) {
            statistics.put("fail", (int) statistics.get("fail") + 1);
            statistics.put("currentFailed", true);
            System.out.println(e.getCause().getMessage());
        }
    }

    private static void showStatistics(HashMap<String, Object> statistics) {
        System.out.println("---------------------------------\n");
        System.out.println("ВСЕГО: " + (int) statistics.get("total"));
        System.out.println("УСПЕШНО: " + (int) statistics.get("success"));
        System.out.println("ОШИБКИ: " + (int) statistics.get("fail"));
    }
}
