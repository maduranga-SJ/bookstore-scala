import scala.collection.mutable

object Library {

  val bookList: mutable.HashMap[String,Book] = mutable.HashMap.empty

  def addBook(isbn: String, book: Book): String ={
    bookList.put(isbn, book)
    s"""{"message":"Successfully Added The Book!","book":${ book.toString }}"""
  }
  def getBook(isbn: String): Option[Book] = bookList.get(isbn)
  def getAllBooks: List[Book] = bookList.values.toList
  def searchBook(query_param: String): List[Book] = {

    val searchListTitle = bookList.values.filter(_.title.contains(query_param)).toList
    val searchListAuthor = bookList.values.filter(_.author.contains(query_param)).toList

    if (searchListTitle.nonEmpty && searchListAuthor.nonEmpty){
      return searchListTitle ++ searchListAuthor
    } else if (searchListTitle.isEmpty && searchListAuthor.nonEmpty) {
      return searchListAuthor
    }
    searchListTitle
  }
}
