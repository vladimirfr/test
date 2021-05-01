package test.controller

import java.security.InvalidParameterException

import test.models.product.Products.Products
import test.repository.ProductRepository
import test.services.SettingsService

object ProductController {
  def listAction(configId: Option[Int], size: Option[Int], page: Option[Int]): Products = {
    val field = SettingsService.read(configId.get)
    if (field.isEmpty) {
      throw new InvalidParameterException("Configuration not found")
    }

    val limit = size.getOrElse(10000)
    val start = (page.getOrElse(1) - 1) * limit

    ProductRepository.find(field.get, limit, start)
  }
}
