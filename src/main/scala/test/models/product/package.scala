package test.models.product

import com.sksamuel.elastic4s.{Hit, HitReader, Indexable}
import spray.json.{DefaultJsonProtocol, RootJsonFormat, _}

import scala.util.Try

package object Products {

  case class ProductItem(
                          item_id: String,
                          name: String,
                          locale: String,
                          click: Int,
                          purchase: Int
                        )

  case class Products(
                       products: Seq[ProductItem],
                       total: Long,
                     )

  object ProductItemJsonProtocol extends DefaultJsonProtocol {
    implicit val productItemFormat: RootJsonFormat[ProductItem] = jsonFormat5(ProductItem)
  }

  object ProductsJsonProtocol extends DefaultJsonProtocol {
    implicit val productItemFormat = jsonFormat5(ProductItem)
    implicit val productsFormat: RootJsonFormat[Products] = jsonFormat2(Products)
  }

  implicit object ProductItemHitReader extends HitReader[ProductItem] {
    override def read(hit: Hit): Try[ProductItem] = {
      val source = hit.sourceAsMap
      Try(
        ProductItem(
          source("item_id").toString,
          source("name").toString,
          source("locale").toString,
          source("click").toString.toInt,
          source("purchase").toString.toInt
        )
      )
    }
  }

  implicit object ProductItemIndexable extends Indexable[ProductItem] {

    import ProductItemJsonProtocol._

    override def json(data: ProductItem): String = {
      data.toJson.toString
    }
  }

}
