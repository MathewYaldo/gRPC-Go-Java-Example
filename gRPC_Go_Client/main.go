package main

import (
	"context"
	"gRPCGo/grpc"
	"log"
)

func main() {
	message := "hello world!!"
	connection := grpc.GetGRPCConnection()
	client := grpc.NewHashClient(connection)

	data, err := client.GetHash(context.Background(), &grpc.HashRequest{Msg: message})

	if err == nil {
		log.Println(data.Hash)
	} else {
		log.Println(err)
	}

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

// Define a struct for the JSON response
//type Response struct {
//	Message string `json:"message"`
//	Status  string `json:"status"`
//}
//
//func handler(w http.ResponseWriter, r *http.Request) {
//	// Create the response object
//	resp := Response{
//		Message: "Hello, world!",
//		Status:  "success",
//	}
//
//	// Set headers
//	w.Header().Set("Content-Type", "application/json")
//	w.WriteHeader(http.StatusOK)
//
//	// Encode the response to JSON
//	if err := json.NewEncoder(w).Encode(resp); err != nil {
//		http.Error(w, err.Error(), http.StatusInternalServerError)
//	}
//}
//
//func main() {
//	http.HandleFunc("/", handler)
//
//	log.Println("Starting server on :8080")
//	if err := http.ListenAndServe(":8080", nil); err != nil {
//		log.Fatalf("Server failed to start: %v", err)
//	}
//}
