package test.helpers

import java.io.{File, IOException}

import com.typesafe.config.{Config, ConfigFactory}

object AppConfig {
  val cfgName: String = "App.conf"

  val cfgFile: File = new File(cfgName)
  val config: Config = if (cfgFile.exists()) {
    ConfigFactory.parseFile(cfgFile)
  } else {
    throw new IOException(s"The configuration file [$cfgName] not found")
  }

  //Server
  val appInterface: String = config.getString("app.interface")
  val appPort: Int = config.getInt("app.port")

  //Redis
  val redisHost: String = config.getString("redis.host")
  val redisPort: Int = config.getInt("redis.port")
  val redisDB: Int = config.getInt("redis.db")

  //Elasticsearch
  val elastic4sScheme: String = config.getString("elastic4s.scheme")
  val elastic4sHost: String = config.getString("elastic4s.host")
  val elastic4sPort: Int = config.getInt("elastic4s.port")
}
