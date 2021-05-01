package test.repository

import com.sksamuel.elastic4s.ElasticDsl.{indexInto, search}
import com.sksamuel.elastic4s.fields.{ElasticField, IntegerField, TextField}
import com.sksamuel.elastic4s.requests.indexes.IndexRequest
import com.sksamuel.elastic4s.requests.searches.{SearchRequest, SearchResponse}
import com.sksamuel.elastic4s.{RequestSuccess, Response}
import spray.json._
import test.models.product.Products.ProductItemJsonProtocol._
import test.models.product.Products._
import test.services.ElasticSearchService

import scala.io.Source

object ProductRepository {
  protected val index: String = "products"

  protected val fields: Seq[ElasticField] = Seq(
    TextField("item_id"),
    TextField("name"),
    TextField("locale"),
    IntegerField("click"),
    IntegerField("purchase")
  )

  def getIndexName(): String = this.index

  /**
   * Fill documents from source
   *
   * @param path
   */
  def insert(path: String = "https://insider-sample-data.s3-eu-west-1.amazonaws.com/scala-api-design/sample.json"): Unit = {
    ElasticSearchService.upsertIndex(this.index, this.fields)

    val json: String = if (path.startsWith("http")) {
      Source.fromURL(path).getLines().toList.mkString
    } else {
      Source.fromFile(path).getLines().toList.mkString
    }

    val products: List[ProductItem] = json.parseJson.convertTo[List[ProductItem]]

    val requests: Seq[IndexRequest] = products.map((product: ProductItem) =>
      indexInto(index).doc(product)
    )

    ElasticSearchService.bulkInsert(requests)
  }

  /**
   * Find products
   *
   * @param sortField
   * @param limit
   * @param start
   * @return
   */
  def find(sortField: String, limit: Int = 10000, start: Int = 0): Products = {
    val query: SearchRequest = search(this.index).sortByFieldAsc(sortField).start(start).limit(limit)
    val response: Response[SearchResponse] = ElasticSearchService.searchQuery(query)

    response match {
      case results: RequestSuccess[SearchResponse] =>
        Products(results.result.to[ProductItem], results.result.totalHits)
    }
  }
}
