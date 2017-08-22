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
  val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
    .withBootstrapServers("localhost:9092")
    .withGroupId("group1")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
 
    val kafkaSource = Consumer.atMostOnceSource(consumerSettings, Subscriptions.topics("test"))
                .map(x=> 
                  {
                    println("Received message: " + x.value())
                    x.value()
                   })
  }
  
