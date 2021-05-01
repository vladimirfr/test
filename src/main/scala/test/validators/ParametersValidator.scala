package test.validators

import java.security.InvalidParameterException

object ParametersValidator {
  def validate(configId: Option[Int], size: Option[Int], page: Option[Int]): Unit = {
    val id = configId.getOrElse(0)
    if (id > 2 || id < 1) {
      throw new InvalidParameterException("'configId' id invalid")
    }

    val limit = size.getOrElse(-1)
    if (limit > 100000 || limit < 1) {
      throw new InvalidParameterException("'size' should be between 1 and 10000")
    }

    if (page.getOrElse(1) < 1) {
      throw new InvalidParameterException("'page' should be greater then 1")
    }
  }
}
