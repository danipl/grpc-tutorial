package com.vysapp

import com.vysapp.GreeterGrpcKt
import com.vysapp.model.proto.test.Test
import io.grpc.ServerBuilder

class GreaterService : GreeterGrpcKt.GreeterCoroutineImplBase() {
    override suspend fun sayHello(request: Test): Test {
        return Test.newBuilder().setMsg("Hello, ${request.msg}").build()
    }
}

fun main(args: Array<String>) {

    val greaterService = GreaterService();

    val server = ServerBuilder.forPort(15001).addService(greaterService).build()

    Runtime.getRuntime().addShutdownHook(Thread {
        server.shutdown()
        server.awaitTermination()
    })

    server.start()
    server.awaitTermination()
}