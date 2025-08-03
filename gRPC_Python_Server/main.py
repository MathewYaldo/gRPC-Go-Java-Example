# Copyright 2015 gRPC authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""The Python implementation of the gRPC route guide server."""

import logging
import hashlib
import asyncio


import grpc
import hash_pb2
import hash_pb2_grpc

class RouteGuideServicer(hash_pb2_grpc.HashServicer):
    """Provides async methods that implement functionality of route guide server."""

    def __init__(self):
        print("Initializing RouteGuideServicer...")

    async def GetHash(self, request, context):
        hash_result = hashlib.sha256(request.msg.encode()).hexdigest()
        return hash_pb2.HashResponse(hash=hash_result)

async def serve():
    server = grpc.aio.server()
    hash_pb2_grpc.add_HashServicer_to_server(RouteGuideServicer(), server)
    server.add_insecure_port("[::]:50051")
    await server.start()
    print("Async gRPC server started on [::]:50051")
    await server.wait_for_termination()

if __name__ == "__main__":
    logging.basicConfig()
    asyncio.run(serve())
