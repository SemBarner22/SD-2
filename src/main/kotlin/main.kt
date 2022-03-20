import server.MongoReactiveDatabase
import server.MongoController
import server.Response
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import org.apache.log4j.BasicConfigurator
import rx.Observable

fun main() {
    val controller = MongoController(MongoReactiveDatabase())
    BasicConfigurator.configure()
    HttpServer
        .newServer(8080)
        .start { req, resp ->
            val uri = req.decodedPath
            val response: Response = if (uri.isNullOrEmpty()) {
                Response(HttpResponseStatus.BAD_REQUEST, Observable.just("Request Error!"))
            } else {
                controller.process(uri.substring(1), req.queryParameters)
            }
            resp.status = response.status
            resp.writeString(response.message)
        }
        .awaitShutdown()
}