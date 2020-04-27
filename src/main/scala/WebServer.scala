import com.sun.net.httpserver._
import java.net._
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import scala.io.Source
import spray.json._
import java.nio.charset.StandardCharsets


object WebServer extends App {


  private val HEADER_CONTENT_TYPE = "Content-Type"
  private val CHARSET = StandardCharsets.UTF_8
  private val STATUS_OK = 200

  val port:Int = 8001
  val hostname:String = "localhost"
  val server:HttpServer = HttpServer.create(new InetSocketAddress(hostname, port), 2)
  val threadPoolExecutor = Executors.newFixedThreadPool(10).asInstanceOf[ThreadPoolExecutor]

  server.createContext("/", new HttpHandler {


    def handle(exchange: HttpExchange): Unit ={
      var requestParamValue:String = null//String variable that holds the response body
      val headers: Headers = exchange.getResponseHeaders//header object to change the response type to JSON


      //call the relevant function depending on the type of the HTTP request
      if("GET".equals(exchange.getRequestMethod)) {

        //for GET requests call handleGetRequest()
        requestParamValue = handleGetRequest(exchange)
      } else if("POST".equals(exchange.getRequestMethod)) {

        //for POST requests call handlePostRequest()
        requestParamValue = handlePostRequest(exchange)
      }

      //Set the header configurations of the responses
      headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET))
      val rawResponseBody = requestParamValue.getBytes(CHARSET)
      exchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length)
      exchange.getResponseBody.write(rawResponseBody)
      handleResponse(exchange,requestParamValue)

    }

    def handleGetRequest(exchange: HttpExchange): String ={

      val rest_path = exchange.getRequestURI.toString

      // Get Book By ISBN
      if (rest_path.contains("/books/book/")){
        val isbn = rest_path.split("book/")(1)
        val book = Library.getBook(isbn).getOrElse("""{"message":"No Book Found"}""").toString

        return book//jsonString
      }
      // Get Book List
      if (rest_path.contains("/books/")){
        val book = Library.getAllBooks.toString
        return s"""{ "books":[${book.drop(5).dropRight(1)}] }"""
      }
      // Get Book By Search With Title and Author
      if (rest_path.contains("/books?q=")){
        val search_item = rest_path.split("=")(1).trim.toLowerCase
        val book = Library.searchBook(search_item).toString

        return s"""{ "books":[${book.drop(5).dropRight(1)}] }"""
      }

      """{"message":"Invalid URL"}"""
    }
    // POST Book to the in memory-database
    def handlePostRequest(exchange: HttpExchange): String ={

      if(exchange.getRequestURI.toString.contains("/books/book")){
        val str = Source.fromInputStream(exchange.getRequestBody).mkString.parseJson.asJsObject.getFields("isbn", "title", "author")
        str match{
          case Seq(JsString(isbn), JsString(title), JsString(author)) =>  Library.addBook(isbn, Book(isbn.trim,title.trim.toLowerCase,author.trim.toLowerCase))
          case _ =>
            """{"message": "Invalid JSON format","body":{
              |"isbn": <isbn>,
              |"title": <title>,
              |"author": <author>}}""".stripMargin
        }
      } else {
        """{"message":"Unknown URL"}"""
      }

    }

    def handleResponse(exchange: HttpExchange, requestParamValue: String): Unit = {
      val outputStream = exchange.getResponseBody

      exchange.sendResponseHeaders(200, 0)
      outputStream.write(requestParamValue.getBytes())
      outputStream.flush()
      outputStream.close()
    }
  })
  server.setExecutor(threadPoolExecutor)
  server.start()
}



