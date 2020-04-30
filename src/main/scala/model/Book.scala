package model

import io.circe.{Encoder, Json}

// The type Book definition
case class Book(isbn: String, title: String, author: String){
  def lowerBook(book: Book): Book = {
    val title = book.title.trim.toLowerCase
    val author = book.author.trim.toLowerCase
    Book(isbn, title, author)
  }

  def isEmpty(): Boolean ={
    if(isbn == ""){
      true
    }else{
      false
    }
  }


}

object Book {
  //Convert Book object to a Json object
  implicit val encoderBook = new Encoder[Book]() {
    final def apply(b: Book): Json = Json.obj(
      ("isbn", Json.fromString(b.isbn)),
      ("title", Json.fromString(b.title)),
      ("author", Json.fromString(b.author))
    )
  }

}