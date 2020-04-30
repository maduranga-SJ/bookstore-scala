package controller

import java.nio.charset.{Charset, StandardCharsets}
import com.sun.net.httpserver.HttpExchange
import io.circe.syntax._

object ResponseHandler {

  private val HEADER_ALLOW:String = "Allow"
  private val HEADER_CONTENT_TYPE:String = "Content-Type"
  private val CHARSET:Charset = StandardCharsets.UTF_8
  private val STATUS_OK:Int = 200
  private val STATUS_ERROR:Int = 500
  private val STATUS_METHOD_NOT_ALLOWED = 405
  private val NO_RESPONSE_LENGTH = -1

  def apply(exchange: HttpExchange, responseMessage: ResponseMessage): Unit = {

    responseMessage match {
      case InvalidRequestMethodResponse(msg) => sendResponse(exchange, STATUS_ERROR, InvalidRequestMethodResponse(msg).asJson.noSpaces)
      case AddBookResponse(msg,book) => sendResponse(exchange, STATUS_OK, AddBookResponse(msg, book).asJson.noSpaces)
      case GetBookResponse(msg,book) => sendResponse(exchange, STATUS_OK, GetBookResponse(msg, book).asJson.noSpaces)
      case GetBookListResponse(msg,bookList) => sendResponse(exchange, STATUS_OK, GetBookListResponse(msg, bookList).asJson.noSpaces)
    }
  }

  def sendResponse(exchange: HttpExchange, statusCode: Int, responseBody: String ): Unit ={

    val header = exchange.getResponseHeaders
    header.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET))

    val rawResponseBody: Array[Byte] = responseBody.getBytes(CHARSET)
    exchange.sendResponseHeaders(statusCode, rawResponseBody.length)

    val outputStream = exchange.getResponseBody
    outputStream.write(rawResponseBody)
    outputStream.flush()
    outputStream.close()
  }
}