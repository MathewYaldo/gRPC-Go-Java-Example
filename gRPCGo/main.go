package main

import (
	"context"
	"gRPCGo/grpc"
	"log"
	"time"
)

func main() {
	message := "hello world"
	connection := grpc.GetGRPCConnection()

	for z := 0; z < 10; z++ {
		now := time.Now()

		client := grpc.NewHashClient(connection)
		data, err := client.GetHash(context.Background(), &grpc.HashRequest{Msg: message})

		if err == nil {
			log.Println(data.Hash)
		} else {
			log.Println(err)
		}
		log.Println(time.Since(now))
	}
}
