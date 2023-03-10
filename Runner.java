package homework;

public class Runner {
    public static void main(String[] args) throws Exception {
        TestLogging myClass = Ioc.createMyClass("homework.TestLoggingImpl", "homework.TestLogging");
        myClass.calculation(100);
        myClass.calculation(100, 200);
        myClass.calculation(100, 200, 300);
    }
}
