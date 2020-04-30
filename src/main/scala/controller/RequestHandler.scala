package controller

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

class RequestHandler extends HttpHandler {

  private val METHOD_GET = "GET"
  private val METHOD_POST = "POST"
  private val INVALID_ERROR = "Invalid Request Method"

  override def handle(exchange: HttpExchange): Unit = {
    exchange.getRequestMethod.toUpperCase match {

      //Identify the GET and POST requests separately and call suitable Request Handler objects
      case METHOD_GET => GetRequestHandler(exchange)
      case METHOD_POST => PostRequestHandler(exchange)
      case _ => ResponseHandler(exchange, InvalidRequestMethodResponse(INVALID_ERROR))
    }
  }
}