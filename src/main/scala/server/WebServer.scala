package server

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.util.concurrent.{Executors, ThreadPoolExecutor}

import handler.RequestHandler

object WebServer extends App{
//Configure and start the server
  private val PORT: Int = 8000
  private val BACKLOG: Int = 2
  private val HOSTNAME: String = "localhost"

  private val server: HttpServer = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG)
  private val threadPoolExecutor = Executors.newFixedThreadPool(10).asInstanceOf[ThreadPoolExecutor]


    server.createContext("/", new RequestHandler)
    server.setExecutor(threadPoolExecutor)
    server.start()



}
