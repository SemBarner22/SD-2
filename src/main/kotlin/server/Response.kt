package server

import rx.Observable
import io.netty.handler.codec.http.HttpResponseStatus

data class Response(val status: HttpResponseStatus, val message: Observable<String>)