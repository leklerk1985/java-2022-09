package service;

import generated.NumberClient;
import generated.NumberServiceGrpc;
import generated.NumberServer;
import io.grpc.stub.StreamObserver;

public class RemoteNumberServiceImpl extends NumberServiceGrpc.NumberServiceImplBase  {

    private final NumberService numberService;

    public RemoteNumberServiceImpl(NumberService numberService) {
        this.numberService = numberService;
    }

    @Override
    public void getNumberSequence(NumberClient request, StreamObserver<NumberServer> responseObserver) {
        int beginSeq = request.getBeginSeq();
        int endSeq = request.getEndSeq();
        var sequence = numberService.getNumberSequence(beginSeq, endSeq);

        sequence.forEach(number -> sleepAndOnNext(responseObserver, number));
        responseObserver.onCompleted();
    }

    private void sleepAndOnNext(StreamObserver<NumberServer> responseObserver, int number) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        responseObserver.onNext(number2NumberServer(number));
    }

    private NumberServer number2NumberServer(int number) {
        return NumberServer.newBuilder().setValueSeq(number).build();
    }
}
