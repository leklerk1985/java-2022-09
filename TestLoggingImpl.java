package homework;

public class TestLoggingImpl implements TestLogging {

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
