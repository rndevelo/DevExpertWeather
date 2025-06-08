package com.rndeveloper.myapplication.data.server

import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException

class MockWebServerRule : TestWatcher() {

    lateinit var server: MockWebServer
        private set

    override fun starting(description: Description?) {
        super.starting(description)
        server = MockWebServer()
        server.start(8080)
        TestUrlProvider.mockUrl = server.url("/").toString() // actualiza la URL compartida
    }

    override fun finished(description: Description?) {
        super.finished(description)
        try {
            server.shutdown()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

object TestUrlProvider {
    var mockUrl: String = "http://localhost:8080/" // valor por defecto
}