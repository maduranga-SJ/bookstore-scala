name := "bookstore-scala-final-github"

version := "0.1"

scalaVersion := "2.11.0"

val circeVersion = "0.11.1"
libraryDependencies ++= Seq(
  "io.circe"  %% "circe-core"     % circeVersion,
  "io.circe"  %% "circe-generic"  % circeVersion,
  "io.circe"  %% "circe-parser"   % circeVersion,
"io.spray" %%  "spray-json" % "1.3.5",
  "com.rabbitmq" % "amqp-client" % "5.7.3",
  "org.slf4j" % "slf4j-simple" % "1.7.12"
)
mainClass in (Compile, run) := Some("server.WebServer")