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

public class HWClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static BlockingQueue<Integer> queueSeq = new ArrayBlockingQueue<Integer>(30);
    private static CountDownLatch latch = new CountDownLatch(1);
    private static final int BEGIN_SEQ = 0;
    private static final int END_SEQ = 30;
    private static final int BEGIN_PRINT = 0;
    private static final int END_PRINT = 50;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var stub = NumberServiceGrpc.newBlockingStub(channel);
        var iteratorSeq = stub.getNumberSequence(NumberClient.newBuilder().setBeginSeq(BEGIN_SEQ).setEndSeq(END_SEQ).build());
        var threadSeq = new Thread(()-> fillQueueSeq(iteratorSeq));
        threadSeq.start();

        latch.countDown();

        int currentValue = 0;
        int lastServerNumber;
        for (int i = BEGIN_PRINT; i <= END_PRINT; i++) {
            Thread.sleep(1000);
            lastServerNumber = getLastServerNumber();

            if (lastServerNumber != -1) {
                currentValue = currentValue + 1 + lastServerNumber;
                System.out.println("lastServerNumber: " + lastServerNumber);
            } else {
                currentValue = currentValue + 1;
            }

            System.out.println("currentValue: " + currentValue);
        }
    }

    private static int getLastServerNumber() {
        Collection<Integer> queueElements = new ArrayList<>();
        queueSeq.drainTo(queueElements);
        return queueElements.size() >= 1 ? queueElements.toArray(new Integer[0])[queueElements.size() - 1] : -1;
    }

    private static void fillQueueSeq(Iterator<NumberServer> iterator) {
        try {
            latch.await();
            while (iterator.hasNext()) {
                queueSeq.offer(iterator.next().getValueSeq());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
