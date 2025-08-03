package org.example;

import io.grpc.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
            try {
                String text = request.getMsg();
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedHash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
                String hash = HexFormat.of().formatHex(encodedHash);

                hitAPI();

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

    public void hitAPI() {
            String url = "http://104.236.69.23:8080"; // Replace with your API endpoint

            try {
                // Create URL and open connection
                URL obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "JavaHttpClient/1.0");

                int responseCode = connection.getResponseCode();
                //System.out.println("Response Code: " + responseCode);

                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {

                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // Print result
                    //System.out.println("Response: " + response.toString());
                }

                // Close connection
                connection.disconnect();

                if(responseCode != 200) {
                    System.out.println("HTTP ERROR: " + responseCode);
                }

            } catch (Exception e) {
                System.err.println("Error during HTTP request: " + e.getMessage());
                e.printStackTrace();
            }
        }
}

