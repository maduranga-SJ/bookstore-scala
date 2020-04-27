
// The type Book definition
case class Book(isbn: String, title: String, author: String){

  // Redefine the toString function to return a string in the JSON format
  override def toString: String = s""" {"isbn":"${isbn}", "title":"${title}", "author":"${author}"}"""
}

