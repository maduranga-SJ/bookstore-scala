package handler

import com.sun.net.httpserver.HttpExchange

import scala.util.matching.Regex
import db.Library
import handler.PostRequestHandler.ADD_BOOK_SUCCESS_MESSAGE
import io.circe.generic.semiauto.deriveDecoder
import io.circe.{Decoder, parser}
import io.circe
import model.Book


object GetRequestHandler {
  //Response messages
  private val GET_BOOK_SUCCESS_MESSAGE = "Matching Book"
  private val SEARCH_BOOK_SUCCESS_MESSAGE = "Matching Books"
  private val GET_BOOK_LIST_SUCCESS_MESSAGE = "All Books"
  private val INVALID_URL_MESSAGE = "Invalid URL"

  private val ISBNPattern: Regex = "(/books/book/)(.*)".r
  private val TitleAuthorPattern: Regex = "(/books[?]q=)(.*)".r
  private val ListPattern: Regex = "(/books/)".r

  //Call the suitable Library method for different GET requests
  def apply(exchange: HttpExchange): Unit = {
    val requestURI = exchange.getRequestURI.toString
    requestURI match {
      case ISBNPattern(_, param) => ResponseHandler(exchange, GetBookResponse(GET_BOOK_SUCCESS_MESSAGE, Library.getBook(param.trim.toLowerCase).getOrElse(Book("", "", ""))))
      case TitleAuthorPattern(_, param) => ResponseHandler(exchange, GetBookListResponse(SEARCH_BOOK_SUCCESS_MESSAGE, Library.searchBook(param.trim.toLowerCase)))
      case ListPattern(_) => ResponseHandler(exchange, GetBookListResponse(GET_BOOK_LIST_SUCCESS_MESSAGE, Library.getAllBooks))
      case _ => ResponseHandler(exchange, InvalidRequestMethodResponse(INVALID_URL_MESSAGE))
    }
  }






}