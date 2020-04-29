package model

import io.circe.{Encoder, Json}

// The type Book definition
case class Book(isbn: String, title: String, author: String)

object Book {
  implicit val encoderBook = new Encoder[Book]() {
    final def apply(b: Book): Json = Json.obj(
      ("isbn", Json.fromString(b.isbn)),
      ("title", Json.fromString(b.title)),
      ("author", Json.fromString(b.author))
    )
  }
}