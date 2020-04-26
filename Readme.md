# Book-Store

#####Bookstore Web Application - Using Scala

### Requirements
Scala 2.11 

JDK 1.8



### Built With

[SBT](https://www.scala-sbt.org/) - Scala Built Tool

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

```
GET /books?q=<<search team>>
```

### Author

[Maduranga Jayasooriya](https://www.linkedin.com/in/madurangajayasooriya/)

