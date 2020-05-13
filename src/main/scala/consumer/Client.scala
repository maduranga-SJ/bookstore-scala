package consumer

import java.util.UUID
import java.util.concurrent.{ArrayBlockingQueue, BlockingQueue}
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client._

class ResponseCallback(val corrId: String) extends DeliverCallback {
  val response: BlockingQueue[String] = new ArrayBlockingQueue[String](1)

  override def handle(consumerTag: String, message: Delivery): Unit = {
    if (message.getProperties.getCorrelationId.equals(corrId)) {
      response.offer(new String(message.getBody, "UTF-8"))
    }
  }

  def take(): String = {
    response.take();
  }
}

class Client(host: String) {

  val factory = new ConnectionFactory()
  factory.setHost(host)

  val connection: Connection = factory.newConnection()
  val channel: Channel = connection.createChannel()
  val requestQueueName: String = "rpc_queue"
  val replyQueueName: String = channel.queueDeclare().getQueue

  def call(message: String,method:String): String = {
    val corrId = UUID.randomUUID().toString
    val props = new BasicProperties.Builder().correlationId(corrId).contentType(method.toUpperCase())
      .replyTo(replyQueueName)
      .build()
    channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"))

    val responseCallback = new ResponseCallback(corrId)
    val cancel = new CancelCallback {
      override def handle(consumerTag: String): Unit = {}
    }
    channel.basicConsume(replyQueueName, true, responseCallback, cancel)

    responseCallback.take()
  }

  def close() {
    connection.close()
  }
}
object Client {

  def main(argv: Array[String]) {//argv(0) = host , argv(1) = request body , argv(2) = request type (GET /POST)
    var Library: Client = null
    var response: String = null
    try {
      val host = if (argv.isEmpty) "localhost" else argv(0)

      Library = new Client(host)
      println(" Sending Request ...")
      response = Library.call(argv(2),argv(1))
      println(" Response :  '" + response + "'")
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (Library != null) {
        try {
          Library.close()
        } catch {
          case ignore: Exception =>
        }
      }
    }
  }
}
