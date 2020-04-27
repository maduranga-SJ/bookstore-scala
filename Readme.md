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

### Author

[Maduranga Jayasooriya](https://www.linkedin.com/in/madurangajayasooriya/)

