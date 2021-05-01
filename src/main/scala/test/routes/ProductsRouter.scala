package test.routes

import java.security.InvalidParameterException

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{_symbol2NR, complete, get, parameters, path}
import spray.json._
import test.controller.ProductController
import test.models.product.Products.Products
import test.models.product.Products.ProductsJsonProtocol._
import test.validators.ParametersValidator

object ProductsRouter {
  val route =
    path("products") {
      get {
        parameters(Symbol("configId").as[Int].?, Symbol("size").as[Int].?, Symbol("page").as[Int].?) {
          (configId: Option[Int], size: Option[Int], page: Option[Int]) =>
            try {
              ParametersValidator.validate(configId, size, page)

              val result: Products = ProductController.listAction(configId, size, page)
              complete(HttpEntity(ContentTypes.`application/json`, result.toJson.toString))
            } catch {
              case e: InvalidParameterException => {
                val message = e.getMessage;
                complete(400 -> HttpEntity(ContentTypes.`application/json`, s"""{"message":"$message"}"""))
              }
              case _: Throwable => {
                val message = "Something went wrong. Please contact technical support.";
                complete(500 -> HttpEntity(ContentTypes.`application/json`, s"""{"message":"$message"}"""))
              }
            }
        }
      }
    }
}
