package model
//Format parameters of a book object
object CleanBook {

  def cleanBook(book: Book): Book = {
    val isbn = cleanIsbn(book.isbn)
    val title = book.title.trim.toLowerCase
    val author = book.author.trim.toLowerCase
    Book(isbn, title, author)
  }

  def cleanIsbn(isbn: String): String = isbn.replace("-", "")
}