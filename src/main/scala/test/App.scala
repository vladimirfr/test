package test

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import test.helpers.{AppConfig, InitHelper}
import test.routes.ProductsRouter

import scala.io.StdIn

object App {
  val interface: String = AppConfig.appInterface
  val port: Int = AppConfig.appPort

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "test")
    implicit val executionContext = system.executionContext

    //Prefill values
    InitHelper.init()

    val bindingFuture = Http().newServerAt(this.interface, this.port).bind(ProductsRouter.route)
    println(s"""Sever running at ${this.interface}:${this.port}...""")

    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
