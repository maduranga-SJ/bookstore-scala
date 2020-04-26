case class Book(isbn: String, title: String, author: String){
  override def toString: String = s"""{"isbn":"${isbn}", "title":"${title}", "author":"${author}"}"""
}

