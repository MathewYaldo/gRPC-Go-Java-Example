package main

import (
	"context"
	"gRPCGo/grpc"
	"log"
	"sync"
	"time"
)

func main() {
	message := "hello world"
	connection := grpc.GetGRPCConnection()
	client := grpc.NewHashClient(connection)

	now := time.Now()
	var wg sync.WaitGroup
	for i := 1; i <= 100_000; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			data, err := client.GetHash(context.Background(), &grpc.HashRequest{Msg: message})

			if err != nil {
				log.Println(err)
			}

			if len(data.Hash) != 64 {
				log.Println("Invalid hash")
			}
		}()
	}

	wg.Wait()
	log.Println("Time elapsed: ", time.Since(now))

	//for z := 0; z < 10; z++ {
	//	now := time.Now()
	//
	//	client := grpc.NewHashClient(connection)
	//	data, err := client.GetHash(context.Background(), &grpc.HashRequest{Msg: message})
	//
	//	if err == nil {
	//		log.Println(data.Hash)
	//	} else {
	//		log.Println(err)
	//	}
	//	log.Println(time.Since(now))
	//}
}
