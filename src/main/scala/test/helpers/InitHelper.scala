package test.helpers

import test.repository.ProductRepository
import test.services.{ElasticSearchService, SettingsService}

object InitHelper {
  /**
   * Prefill values
   */
  def init(): Unit = {
    //Fill values
    //ElasticSearchService.removeIndex(ProductRepository.getIndexName)
    if (!ElasticSearchService.isIndexExist(ProductRepository.getIndexName)) {
      ProductRepository.insert()
      SettingsService.default()
    }
  }
}
