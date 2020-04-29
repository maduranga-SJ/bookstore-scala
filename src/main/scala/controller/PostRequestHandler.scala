package controller

import com.sun.net.httpserver.HttpExchange
import io.circe.generic.semiauto.deriveDecoder
import io.circe.{Decoder, parser}
import model._
import dao._

import scala.io.Source
import scala.util.matching.Regex

object PostRequestHandler {

  private val ADD_BOOK_SUCCESS_MESSAGE = "Successfully Added The Book"
  private val INVALID_URL_MESSAGE = "Invalid Url to Add the Book"
  private val INVALID_JSON_MESSAGE = """Invalid Json, Please Provide a JSON with {"isbn":<isbn>, "title":<title>, "author":<author>} """

  private val addBookPattern: Regex = "(/books/book.*)".r

  def apply(exchange: HttpExchange): Unit = {
    val requestURI = exchange.getRequestURI.toString
    requestURI match {
      case addBookPattern(_) => addBook(exchange)
      case _ => ResponseHandler(exchange, InvalidRequestMethodResponse(INVALID_URL_MESSAGE))
    }
  }

  def addBook(exchange: HttpExchange): Unit ={
    val jsonStr = Source.fromInputStream(exchange.getRequestBody).mkString
    implicit val bookDecoder: Decoder[Book] = deriveDecoder[Book]
    val decodeResult = parser.decode[Book](jsonStr)
    decodeResult match {
      case Right(book) => ResponseHandler(exchange, AddBookResponse( ADD_BOOK_SUCCESS_MESSAGE, Library.addBook(book.isbn, book).orNull))
      case Left(error) => ResponseHandler(exchange, InvalidRequestMethodResponse(error.getMessage))
    }
  }
}