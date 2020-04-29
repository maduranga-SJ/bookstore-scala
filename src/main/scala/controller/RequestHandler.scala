package controller

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

class RequestHandler extends HttpHandler{

  private val METHOD_GET = "GET"
  private val METHOD_POST = "POST"

  override def handle(exchange: HttpExchange): Unit = {
    exchange.getRequestMethod.toUpperCase match {
      case METHOD_GET => GetRequestHandler(exchange)
      case METHOD_POST => PostRequestHandler(exchange)
      case _ => ResponseHandler(exchange, InvalidRequestMethodResponse("Invalid Request Method"))
    }
  }
}
