package server

import java.util.concurrent.CountDownLatch

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client._
import db.Library
import handler.{AddBookResponse, PostRequestHandler}
import handler.AMQPRequestHandler
import model.Book

class ServerCallback(val ch: Channel, val latch: CountDownLatch) extends DeliverCallback {

  override def handle(consumerTag: String, delivery: Delivery): Unit = {
    var response: String = null
    val replyProps = new BasicProperties.Builder()
      .correlationId(delivery.getProperties.getCorrelationId)
      .build

    try {
      val message = new String(delivery.getBody, "UTF-8")
      println(" Recieved : " + message )

      response =AMQPRequestHandler.requestHandlerAMQP(message,delivery.getProperties.getContentType)

    } catch {
      case e: Exception => {
        println(" [.] " + e.toString)
        response = ""
      }
    } finally {
      ch.basicPublish("", delivery.getProperties.getReplyTo, replyProps, response.getBytes("UTF-8"))
      ch.basicAck(delivery.getEnvelope.getDeliveryTag, false)
      latch.countDown()
    }
  }
}

object AMQPServer {
  private val RPC_QUEUE_NAME = "rpc_queue"

  def main(argv: Array[String]) {
    var connection: Connection = null
    var channel: Channel = null
    try {
      val factory = new ConnectionFactory()
      factory.setHost("localhost")
      connection = factory.newConnection()
      channel = connection.createChannel()
      channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null)
      channel.basicQos(1)
      val latch = new CountDownLatch(10)//TODO reponds for 10 req make it infinite
      val serverCallback = new ServerCallback(channel, latch)


      val cancel = new CancelCallback {
        override def handle(consumerTag: String): Unit = {}
      }
      channel.basicConsume(RPC_QUEUE_NAME, false, serverCallback, cancel)
      println(" Awaiting requests from Consumer ...")
      latch.await()
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (connection != null) {
        try {
          connection.close()
        } catch {
          case ignore: Exception =>
        }
      }
    }
  }
}