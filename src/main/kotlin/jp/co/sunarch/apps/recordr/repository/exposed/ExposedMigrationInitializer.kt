package jp.co.sunarch.apps.recordr.repository.exposed

import org.jetbrains.exposed.sql.SchemaUtils
import org.slf4j.LoggerFactory
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import javax.annotation.PostConstruct

class ExposedMigrationInitializer(val txManager: PlatformTransactionManager) {

  private val log = LoggerFactory.getLogger(ExposedMigrationInitializer::class.java)

  @PostConstruct
  fun migrate() {
    log.info("Migrating exposed tables")

    TransactionTemplate(txManager).execute {
      SchemaUtils.create(WorkRecordTable)

      log.info("Migration succeeded")
    }
  }

}

