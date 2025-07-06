package org.example;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

public class HashServiceImpl extends HashGrpc.HashImplBase {

    @Override
    public void getHash(HashRequest request, io.grpc.stub.StreamObserver<HashResponse> responseObserver) {
        long startTime = System.nanoTime();
        System.out.println("Handling hello endpoint: " + request.toString());
        String text = request.getMsg();
        String hash = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            // For Java 17 and later, HexFormat is a convenient way:
            hash = HexFormat.of().formatHex(encodedHash);
        } catch (Exception e) {
            hash = "";
        }

        HashResponse response = HashResponse
                .newBuilder()
                .setHash(hash)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);
    }
}