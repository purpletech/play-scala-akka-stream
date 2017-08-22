package controllers

import javax.inject.{Inject, Singleton}

import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.mvc._

@Singleton
class ScalaEventSourceController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with KafkaSource {

  def index() = Action {
    Ok(views.html.teamnames())
  }

  def teamMemberNames() = Action {
    Ok.chunked(kafkaSource.via(EventSource.flow)).as(ContentTypes.EVENT_STREAM)
  }
}