package jp.co.sunarch.apps.recordr

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
class RecordrApplication {

  @Bean
  fun kotlinModule() = KotlinModule()

  @Bean
  fun javaTimeModule() = JavaTimeModule()

  @Bean
  fun transactionManager(dataSource: DataSource) = SpringTransactionManager(dataSource)

}

fun main(args: Array<String>) {
    SpringApplication.run(RecordrApplication::class.java, *args)
}
