import com.vysapp.GreeterGrpc
import com.vysapp.HelloRequest
import io.grpc.ManagedChannelBuilder

fun main(args: Array<String>) {
    val channel = ManagedChannelBuilder.forAddress("localhost", 15001)
        .usePlaintext()
        .build()

    val stub = GreeterGrpc.newBlockingStub(channel)

    val response = stub.sayHello(HelloRequest.newBuilder().setName("VYS").build())

    println(response.message)
}
