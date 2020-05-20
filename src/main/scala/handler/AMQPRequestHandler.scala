package handler

import db.Library
import io.circe.generic.semiauto.deriveDecoder
import io.circe.syntax._
import io.circe.{Decoder, parser}
import model.Book

object AMQPRequestHandler {
  private val ADD_BOOK_SUCCESS_MESSAGE = "Successfully Added The Book"

  // Add new book for AMQP Request
  def addBook(jsonStr: String): String = {
    println(jsonStr)
    implicit val bookDecoder: Decoder[Book] = deriveDecoder[Book]
    val decodeResult = parser.decode[Book](jsonStr)
    decodeResult match {
      case Right(book) => AddBookResponse(ADD_BOOK_SUCCESS_MESSAGE, Library.addBook(book.isbn, book).getOrElse(Book("", "", ""))).asJson.noSpaces
      case Left(error) => InvalidRequestMethodResponse(error.getMessage).asJson.noSpaces
    }
  }

  //handle all the requests for the AMQP server using the request type
  def requestHandlerAMQP(jsonStr: String, requestType: String): String = {
    val GET_BOOK_SUCCESS_MESSAGE = "Matching Book"
    val SEARCH_BOOK_SUCCESS_MESSAGE = "Matching Books"
    val GET_BOOK_LIST_SUCCESS_MESSAGE = "All Books"
    val INVALID_URL_MESSAGE = "Invalid URL"
    requestType match {
      case "GET_BY_ID" => GetBookResponse(GET_BOOK_SUCCESS_MESSAGE, Library.getBook(jsonStr.trim.toLowerCase).getOrElse(Book("", "", ""))).asJson.noSpaces
      case "SEARCH" => GetBookListResponse(SEARCH_BOOK_SUCCESS_MESSAGE, Library.searchBook(jsonStr.trim.toLowerCase)).asJson.noSpaces
      case "GET_ALL" => GetBookListResponse(GET_BOOK_LIST_SUCCESS_MESSAGE, Library.getAllBooks).asJson.noSpaces
      case "ADD_BOOK" => addBook(jsonStr)
      case _ => InvalidRequestMethodResponse(INVALID_URL_MESSAGE).asJson.noSpaces
    }
  }
}

