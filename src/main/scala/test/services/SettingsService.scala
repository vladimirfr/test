package test.services

import com.redis.RedisClient
import test.helpers.AppConfig

object SettingsService {
  var client: RedisClient = new RedisClient(AppConfig.redisHost, AppConfig.redisPort, AppConfig.redisDB)

  def default(): Unit = {
    client.set(1, "click");
    client.set(2, "purchase");
  }

  def read(configId: Int): Option[String] = {
    client.get(configId)
  }
}
