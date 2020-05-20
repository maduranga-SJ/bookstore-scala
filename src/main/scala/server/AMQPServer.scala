package server

import java.util.concurrent.CountDownLatch
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client._
import handler.AMQPRequestHandler
import scala.util.{Failure, Success, Try}

class ServerCallback(val ch: Channel, val latch: CountDownLatch) extends DeliverCallback {

  override def handle(consumerTag: String, delivery: Delivery): Unit = {
    var response: String = null
    val replyProps = new BasicProperties.Builder()
      .correlationId(delivery.getProperties.getCorrelationId)
      .build

    val message = new String(delivery.getBody, "UTF-8")
    println(" Recieved : " + message)
    //Call the Request handler and save the returned response
    Try(AMQPRequestHandler.requestHandlerAMQP(message, delivery.getProperties.getContentType)) match {
      case Success(v) => response = "" + v
      case Failure(e) => {
        println(" [.] " + e.toString)
        response = ""
      }
    }
    // send the response to the response queue
    ch.basicPublish("", delivery.getProperties.getReplyTo, replyProps, response.getBytes("UTF-8"))
    ch.basicAck(delivery.getEnvelope.getDeliveryTag, false)
  }
}

object AMQPServer {
  private val RPC_QUEUE_NAME = "rpc_queue"

  def main(argv: Array[String]) {
    var connection: Connection = null
    var channel: Channel = null
    Try(new ConnectionFactory) match {
      case Success(factory) => {
        factory.setHost("localhost")
        connection = factory.newConnection()
        channel = connection.createChannel()
        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null)
        channel.basicQos(1)
        val latch = new CountDownLatch(1)
        val serverCallback = new ServerCallback(channel, latch)
        val cancel = new CancelCallback {
          override def handle(consumerTag: String): Unit = {}
        }
        channel.basicConsume(RPC_QUEUE_NAME, false, serverCallback, cancel)
        println(" Awaiting requests from Consumer ...")
        latch.await()
      }
      case Failure(e) => e.printStackTrace()
        if (connection != null) {
          Try(connection.close()) match {
            case Failure(e) => e.printStackTrace()
          }
        }
    }
  }
}
