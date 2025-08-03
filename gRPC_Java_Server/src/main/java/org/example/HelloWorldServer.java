package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class HelloWorldServer {
    private static final int PORT = 50051;
    private Server server;

    public void start() throws IOException {
        server = ServerBuilder.forPort(PORT)
                .addService(new HashServiceImpl())
                .build()
                .start();
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server == null) {
            return;
        }

        server.awaitTermination();
    }

    public static void begin() {
        try {
            HelloWorldServer server = new HelloWorldServer();
            server.start();
            server.blockUntilShutdown();
        } catch(Exception e) {
            System.out.println("Error starting HelloWorldServer: " + e.getMessage());
        }
    }
}