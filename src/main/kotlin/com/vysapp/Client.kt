package com.vysapp

import com.vysapp.GreeterGrpc
import com.vysapp.model.proto.test.Test
import io.grpc.ManagedChannelBuilder

fun main(args: Array<String>) {
    val channel = ManagedChannelBuilder.forAddress("localhost", 15001)
        .usePlaintext()
        .build()

    val stub = GreeterGrpc.newBlockingStub(channel)

    val response = stub.sayHello(Test.newBuilder().setMsg("VYS").build())

    println(response.msg)
}
