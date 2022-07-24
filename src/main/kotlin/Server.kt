import com.vysapp.GreeterGrpcKt
import com.vysapp.HelloReply
import com.vysapp.HelloRequest
import io.grpc.ServerBuilder

class GreaterService : GreeterGrpcKt.GreeterCoroutineImplBase() {
    override suspend fun sayHello(request: HelloRequest): HelloReply {
        return HelloReply.newBuilder()
            .setMessage("Hello, ${request.name}")
            .build()
    }
}

fun main(args: Array<String>) {

    val greaterService = GreaterService();

    val server = ServerBuilder
        .forPort(15001)
        .addService(greaterService)
        .build()

    Runtime.getRuntime().addShutdownHook(Thread {
        server.shutdown()
        server.awaitTermination()
    })

    server.start()
    server.awaitTermination()
}