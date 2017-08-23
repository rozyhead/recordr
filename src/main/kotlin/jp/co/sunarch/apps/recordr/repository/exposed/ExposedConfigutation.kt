package jp.co.sunarch.apps.recordr.repository.exposed

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@ConditionalOnProperty(prefix = "exposed", name = arrayOf("enabled"), matchIfMissing = true)
class ExposedConfigutation {

  @Bean
  fun exposedMigrationInitializer(txManager: PlatformTransactionManager) = ExposedMigrationInitializer(txManager)

}