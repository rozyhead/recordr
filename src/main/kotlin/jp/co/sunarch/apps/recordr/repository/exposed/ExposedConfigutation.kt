package jp.co.sunarch.apps.recordr.repository.exposed

import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@ConditionalOnProperty(prefix = "exposed", name = arrayOf("enabled"), matchIfMissing = true)
class ExposedConfigutation {

  @Bean
  fun exposedMigrationInitializer(txManager: PlatformTransactionManager) = ExposedMigrationInitializer(txManager)

  @Bean
  fun transactionManager(dataSource: DataSource) = SpringTransactionManager(dataSource)

  @Bean
  fun exposedWorkRecordRepository() = ExposedWorkRecordRepository()

}