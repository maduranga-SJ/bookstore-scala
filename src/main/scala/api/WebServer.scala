package api

import java.net.InetSocketAddress
import java.nio.charset.{Charset, StandardCharsets}
import java.util.concurrent.{Executors, ThreadPoolExecutor}

import com.sun.net.httpserver.HttpServer

import controller.RequestHandler

object WebServer extends App {

  private val PORT:Int = 8001
  private val BACKLOG:Int = 2
  private val HOSTNAME:String = "localhost"

  private val server:HttpServer = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG)
  private val threadPoolExecutor = Executors.newFixedThreadPool(10).asInstanceOf[ThreadPoolExecutor]

  server.createContext("/", new RequestHandler)
  server.setExecutor(threadPoolExecutor)
  server.start()

}
