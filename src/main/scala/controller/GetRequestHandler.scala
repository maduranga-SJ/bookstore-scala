package controller

import com.sun.net.httpserver.HttpExchange
import dao.Library
import scala.util.matching.Regex

object GetRequestHandler {

  private val GET_BOOK_SUCCESS_MESSAGE = "Successfully Retrieved the Book"
  private val SEARCH_BOOK_SUCCESS_MESSAGE = "Successfully Retrieved the Matching Books"
  private val GET_BOOK_LIST_SUCCESS_MESSAGE = "Successfully Retrieved All Books"
  private val INVALID_URL_MESSAGE = "Invalid URL"

  private val ISBNPattern: Regex = "(/books/book/)(.*)".r
  private val TitleAuthorPattern: Regex = "(/books[?]q=)(.*)".r
  private val ListPattern: Regex = "(/books/)".r

  def apply(exchange: HttpExchange): Unit = {
    val requestURI = exchange.getRequestURI.toString
    requestURI match {
      case ISBNPattern(_, param) => ResponseHandler(exchange, GetBookResponse(GET_BOOK_SUCCESS_MESSAGE, Library.getBook(param.trim.toLowerCase).orNull))
      case TitleAuthorPattern(_,param) => ResponseHandler(exchange, GetBookListResponse(SEARCH_BOOK_SUCCESS_MESSAGE, Library.searchBook(param.trim.toLowerCase)))
      case ListPattern(_) => ResponseHandler(exchange, GetBookListResponse(GET_BOOK_LIST_SUCCESS_MESSAGE, Library.getAllBooks))
      case _ => ResponseHandler(exchange, InvalidRequestMethodResponse(INVALID_URL_MESSAGE))
    }
  }
}