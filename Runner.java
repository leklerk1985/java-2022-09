package homework;

public class Runner {
    public static void main(String[] args) {
        TestLogging.createInstanceUsingProxy().calculation(100);
        TestLogging.createInstanceUsingProxy().calculation(100, 200);
        TestLogging.createInstanceUsingProxy().calculation(100, 200, 300);
    }
}
