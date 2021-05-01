package test.services

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.fields.ElasticField
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.requests.indexes.IndexRequest
import com.sksamuel.elastic4s.requests.mappings.MappingDefinition
import com.sksamuel.elastic4s.requests.searches.{SearchRequest, SearchResponse}
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties, Response}
import test.helpers.AppConfig

object ElasticSearchService {
  var client: ElasticClient = null
  val connectionString = s"""${AppConfig.elastic4sScheme}://${AppConfig.elastic4sHost}:${AppConfig.elastic4sPort}"""

  /**
   * Create or get client
   *
   * @return
   */
  def getClient(): ElasticClient = {
    if (client == null) {
      this.client = ElasticClient(
        JavaClient(
          ElasticProperties(
            this.connectionString
          )
        )
      )
    }

    this.client
  }

  /**
   * Fully remove index from ElasticSearch
   *
   * @param name
   */
  def removeIndex(name: String): Unit = {
    this.getClient()
      .execute {
        deleteIndex(name)
      }
      .await
  }

  /**
   * @param name
   * @return
   */
  def isIndexExist(name: String): Boolean = {
    this.getClient()
      .execute {
        indexExists(name)
      }
      .await
      .result
      .isExists
  }

  /**
   * Create index
   *
   * @param name
   * @param fields
   */
  def upsertIndex(name: String, fields: Seq[ElasticField]): Unit = {
    if (!this.isIndexExist(name)) {
      this.getClient()
        .execute {
          createIndex(name).mapping(
            fields.foldLeft(emptyMapping)((m: MappingDefinition, n: ElasticField) => properties(n))
          )
        }.await
    }
  }

  /**
   * Insert rows
   *
   * @param requests
   */
  def bulkInsert(requests: Seq[IndexRequest]): Unit = {
    this.getClient().execute {
      bulk(requests)
    }.await
  }

  /**
   *
   * @param request
   * @return
   */
  def searchQuery(request: SearchRequest): Response[SearchResponse] = {
    this.getClient().execute {
      request
    }.await
  }
}
