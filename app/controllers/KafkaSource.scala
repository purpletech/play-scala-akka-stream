package controllers

import akka.kafka._
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.stream.ActorMaterializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}


trait KafkaSource {
  val system = ActorSystem("example")
  implicit val ec = system.dispatcher
  implicit val m = ActorMaterializer.create(system)

  def kafkaSource = {
    val groupId: String = System.currentTimeMillis().toString
    println("Kafka consumer group id: " + groupId)
    val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers("localhost:9092")
      .withGroupId(groupId)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    Consumer.atMostOnceSource(consumerSettings, Subscriptions.topics("test"))
      .map(x=>
      {
        println("Received message: " + x.value())
        x.value()
      })
    }
  }
