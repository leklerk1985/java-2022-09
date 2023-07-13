import io.grpc.ServerBuilder;
import service.NumberServiceImpl;
import service.RemoteNumberServiceImpl;
import java.io.IOException;

public class HWServer {
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var numberService = new NumberServiceImpl();
        var remoteNumberService = new RemoteNumberServiceImpl(numberService);

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteNumberService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
