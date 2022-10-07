package com.junseo.postboy.util.web

object WebConfig {

    var PROTOCOL = "http"

    var HOST = "127.0.0.1"

    var PORT = 8080

    fun setUrl(protocol: String, host: String, port: Int) {
        PROTOCOL = protocol
        HOST = host
        PORT = port
    }

    val BASE_URL get() = run { "${PROTOCOL}://${HOST}:${PORT}" }

}