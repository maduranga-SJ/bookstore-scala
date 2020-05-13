# Book-Store

Bookstore Web Application - Using Scala

### Requirements
Scala 2.11 

JDK 1.8

### Built With

[SBT](https://www.scala-sbt.org/) - Scala Built Tool

### Instructions

- Open the files in **IntelliJ** 
- Create a sbt **run** task 
- Run the app using the created task

### Sample Requests 

#### Add Book

```
POST /books/book
```

```
{
  "isbn" : "978-1-56619-909-5",
  "author" : "Alex Goot",
  "title" : "Musical Life"
}
```
#### Return all the books

```
GET /books/
```
#### Return a book by ISBN

```
GET /books/book/<<isbn>>
```
#### Search books
use a part of the auther or a part of the book name to search.
```
GET /books?q=<<search team>>
```

### Sample Requests for AMQP requests

### Instructions

- Run AMQPServer.scala located in server package 
- Run Cilent.scala located in consumer package
- The request parameters should be given as commandline parameters
    * Option 1 
        * Run the application in Terminal by giving command line arguments at the end of the file name.
    * Option 2
        * Edit Configurations of Client by giving arguments as "program arguments".

order of commandline arguments

[host] [request type] [request body] 

example
```
localhost SEARCH life
```
#### Add Book

```
localhost ADD_BOOK {\"isbn\":\"978-1-56619-909-519\",\"title\":\"Musical_Life\",\"author\":\"jack_sparrow\"}
```


#### Return a book by ISBN

```
localhost GET_BY_ID 978-1-56619-909-519
```
#### Return all the books

```
localhost GET_ALL
```
#### Search books
use a part of the auther or a part of the book name to search.
```
localhost SEARCH life
```

### Author

[Maduranga Jayasooriya](https://www.linkedin.com/in/madurangajayasooriya/)

