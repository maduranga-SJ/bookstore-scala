package controller

import io.circe.syntax._
import io.circe.{Encoder, Json}
import model.Book

sealed trait ResponseMessage

//Combine the response message with the response values to create complete responses for each request
case class AddBookResponse(message: String, book: Book) extends ResponseMessage

object AddBookResponse {
  implicit val encoderAddBookResponse: Encoder[AddBookResponse] = new Encoder[AddBookResponse]() {
    final def apply(r: AddBookResponse): Json = Json.obj(
      ("message", Json.fromString(r.message)),
      ("book", r.book.asJson)
    )
  }
}

case class GetBookResponse(message: String, book: Book) extends ResponseMessage

object GetBookResponse {
  implicit val encoderGetBookResponse: Encoder[GetBookResponse] = new Encoder[GetBookResponse]() {

    final def apply(r: GetBookResponse): Json = {
      if(r.book.isEmpty()){  Json.obj(("message", Json.fromString("No match found")))}
      else {Json.obj(("message", Json.fromString(r.message)), ("book", r.book.asJson))}}
  }
}

case class GetBookListResponse(message: String, booklist: List[Book]) extends ResponseMessage

object GetBookListResponse {
  implicit val encoderGetBookListResponse: Encoder[GetBookListResponse] = new Encoder[GetBookListResponse]() {
    final def apply(r: GetBookListResponse): Json ={
      if(r.booklist.isEmpty){   Json.obj(("message", Json.fromString("No match found")))}
      else{Json.obj(("message", Json.fromString(r.message)), ("booklist", r.booklist.asJson))}
    }
  }
}


sealed trait ErrorResponseMessage extends ResponseMessage

case class InvalidRequestMethodResponse(message: String) extends ErrorResponseMessage

object InvalidRequestMethodResponse {
  implicit val encoderInvalidRequestMethodResponse: Encoder[InvalidRequestMethodResponse] = new Encoder[InvalidRequestMethodResponse]() {
    final def apply(m: InvalidRequestMethodResponse): Json = Json.obj(("message", Json.fromString(m.message)))
  }
}