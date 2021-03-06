package com.safframework.androidserver.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.safframework.server.converter.gson.GsonConverter
import com.safframework.server.core.AndroidServer
import com.safframework.server.core.http.Response

/**
 *
 * @FileName:
 *          com.safframework.androidserver.service.HttpService
 * @author: Tony Shen
 * @date: 2020-03-27 10:31
 * @version: V1.0 <描述当前版本功能>
 */
class HttpService : Service() {

    private lateinit var androidServer: AndroidServer

    override fun onCreate() {
        super.onCreate()
        startServer()
    }

    // 启动 Http 服务端
    private fun startServer() {

        androidServer = AndroidServer.Builder().converter(GsonConverter()).build()

        androidServer
            .get("/hello")  { _, response: Response ->
                response.setBodyText("hello world")
            }
            .get("/sayHi/{name}") { request,response: Response ->
                val name = request.param("name")
                response.setBodyText("hi $name!")
            }
            .post("/uploadLog") { request,response: Response ->
                val requestBody = request.content()
                response.setBodyText(requestBody)
            }
            .start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        androidServer.close()
        super.onDestroy()
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}
