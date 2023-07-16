package generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.30.2)",
    comments = "Source: NumberService.proto")
public final class NumberServiceGrpc {

  private NumberServiceGrpc() {}

  public static final String SERVICE_NAME = "generated.NumberService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<generated.NumberClient,
      generated.NumberServer> getGetNumberSequenceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getNumberSequence",
      requestType = generated.NumberClient.class,
      responseType = generated.NumberServer.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<generated.NumberClient,
      generated.NumberServer> getGetNumberSequenceMethod() {
    io.grpc.MethodDescriptor<generated.NumberClient, generated.NumberServer> getGetNumberSequenceMethod;
    if ((getGetNumberSequenceMethod = NumberServiceGrpc.getGetNumberSequenceMethod) == null) {
      synchronized (NumberServiceGrpc.class) {
        if ((getGetNumberSequenceMethod = NumberServiceGrpc.getGetNumberSequenceMethod) == null) {
          NumberServiceGrpc.getGetNumberSequenceMethod = getGetNumberSequenceMethod =
              io.grpc.MethodDescriptor.<generated.NumberClient, generated.NumberServer>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getNumberSequence"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.NumberClient.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.NumberServer.getDefaultInstance()))
              .setSchemaDescriptor(new NumberServiceMethodDescriptorSupplier("getNumberSequence"))
              .build();
        }
      }
    }
    return getGetNumberSequenceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NumberServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NumberServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NumberServiceStub>() {
        @java.lang.Override
        public NumberServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NumberServiceStub(channel, callOptions);
        }
      };
    return NumberServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NumberServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NumberServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NumberServiceBlockingStub>() {
        @java.lang.Override
        public NumberServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NumberServiceBlockingStub(channel, callOptions);
        }
      };
    return NumberServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NumberServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NumberServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NumberServiceFutureStub>() {
        @java.lang.Override
        public NumberServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NumberServiceFutureStub(channel, callOptions);
        }
      };
    return NumberServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class NumberServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getNumberSequence(generated.NumberClient request,
        io.grpc.stub.StreamObserver<generated.NumberServer> responseObserver) {
      asyncUnimplementedUnaryCall(getGetNumberSequenceMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetNumberSequenceMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                generated.NumberClient,
                generated.NumberServer>(
                  this, METHODID_GET_NUMBER_SEQUENCE)))
          .build();
    }
  }

  /**
   */
  public static final class NumberServiceStub extends io.grpc.stub.AbstractAsyncStub<NumberServiceStub> {
    private NumberServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NumberServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NumberServiceStub(channel, callOptions);
    }

    /**
     */
    public void getNumberSequence(generated.NumberClient request,
        io.grpc.stub.StreamObserver<generated.NumberServer> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetNumberSequenceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NumberServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<NumberServiceBlockingStub> {
    private NumberServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NumberServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NumberServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<generated.NumberServer> getNumberSequence(
        generated.NumberClient request) {
      return blockingServerStreamingCall(
          getChannel(), getGetNumberSequenceMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NumberServiceFutureStub extends io.grpc.stub.AbstractFutureStub<NumberServiceFutureStub> {
    private NumberServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NumberServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NumberServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_GET_NUMBER_SEQUENCE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NumberServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NumberServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_NUMBER_SEQUENCE:
          serviceImpl.getNumberSequence((generated.NumberClient) request,
              (io.grpc.stub.StreamObserver<generated.NumberServer>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class NumberServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NumberServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return generated.NumberServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NumberService");
    }
  }

  private static final class NumberServiceFileDescriptorSupplier
      extends NumberServiceBaseDescriptorSupplier {
    NumberServiceFileDescriptorSupplier() {}
  }

  private static final class NumberServiceMethodDescriptorSupplier
      extends NumberServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NumberServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (NumberServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NumberServiceFileDescriptorSupplier())
              .addMethod(getGetNumberSequenceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
