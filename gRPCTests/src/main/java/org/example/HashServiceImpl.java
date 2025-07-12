package org.example;

import io.grpc.Status;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HashServiceImpl extends HashGrpc.HashImplBase {

    // Inject this once, not per-request
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);


    @Override
    public void getHash(HashRequest request, io.grpc.stub.StreamObserver<HashResponse> responseObserver) {
        executor.submit(() -> {
            long startTime = System.nanoTime();
            try {
                String text = request.getMsg();
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedHash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
                String hash = HexFormat.of().formatHex(encodedHash);

                HashResponse response = HashResponse.newBuilder().setHash(hash).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } catch (Exception e) {
                responseObserver.onError(Status.INTERNAL.withDescription("Hashing error").withCause(e).asRuntimeException());
            } finally {
                long stopTime = System.nanoTime();
                // Use structured logging (optional) instead of println
                // logger.debug("Hashing took {} ns", stopTime - startTime);
            }
        });
    }
}
