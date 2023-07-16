import generated.NumberServer;
import generated.NumberServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import service.RemoteNumberServiceImpl;
import generated.NumberClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class HWClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int BEGIN_SEQ = 0;
    private static final int END_SEQ = 30;
    private static final int BEGIN_PRINT = 0;
    private static final int END_PRINT = 50;
    private static final AtomicInteger lastServerNumber = new AtomicInteger(0);
    private static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var stub = NumberServiceGrpc.newBlockingStub(channel);
        var iteratorSeq = stub.getNumberSequence(NumberClient.newBuilder().setBeginSeq(BEGIN_SEQ).setEndSeq(END_SEQ).build());
        var threadSeq = new Thread(() -> fillQueueSeq(iteratorSeq));
        threadSeq.start();

        latch.countDown();

        int currentValue = 0;
        int currentServerNumber;
        for (int i = BEGIN_PRINT; i <= END_PRINT; i++) {
            Thread.sleep(1000);

            currentServerNumber = lastServerNumber.getAndSet(0);
            currentValue = currentValue + 1 + currentServerNumber;
            if (currentServerNumber != 0) {
                System.out.println("lastServerNumber: " + currentServerNumber);
            }

            System.out.println("currentValue: " + currentValue);
        }
    }

    private static void fillQueueSeq(Iterator<NumberServer> iterator) {
        try {
            latch.await();
            while (iterator.hasNext()) {
                lastServerNumber.set(iterator.next().getValueSeq());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
