package db

import model.Book
import scala.collection.mutable

object Library {

  // Create a HashMap object to act as the in-memory database
  val bookList: mutable.HashMap[String, Book] = mutable.HashMap.empty


  //addBook function insert a new Book Object to the Hashmap
  def addBook(isbn: String, book: Book): Option[Book] = {
    bookList.put(isbn, book.lowerBook(book))
    getBook(isbn)
  }

  //getBook function returns all the book objects
  def getBook(isbn: String): Option[Book] = bookList.get(isbn)

  //getBook function returns all the book objects
  def getAllBooks: List[Book] = bookList.values.toList

  //searchBook function returns all the book objects which contains the search query in author or book title
  def searchBook(query_param: String): List[Book] = {

    val searchListTitle = bookList.values.filter(_.title.contains(query_param)).toList
    val searchListAuthor = bookList.values.filter(_.author.contains(query_param)).toList

    if (searchListTitle.nonEmpty && searchListAuthor.nonEmpty) {
      searchListTitle ++ searchListAuthor
    } else if (searchListTitle.isEmpty && searchListAuthor.nonEmpty) {
      searchListAuthor
    } else {
      searchListTitle
    }

  }
}

