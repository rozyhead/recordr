package jp.co.sunarch.apps.recordr.service

import jp.co.sunarch.apps.recordr.model.*
import jp.co.sunarch.apps.recordr.repository.WorkRecordRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = NONE)
@Transactional
class WorkRecordServiceTest {

  @Autowired
  lateinit var sut: WorkRecordService

  @Autowired
  lateinit var workRecordRepository: WorkRecordRepository

  @Before
  fun setUp() {
    workRecordRepository.save(WorkRecord(
        UserId("user"),
        WorkDate.of(2017, 9, 30),
        null,
        null,
        null,
        null
    ))
    workRecordRepository.save(WorkRecord(
        UserId("user"),
        WorkDate.of(2017, 10, 1),
        null,
        null,
        null,
        null
    ))
    workRecordRepository.save(WorkRecord(
        UserId("user2"),
        WorkDate.of(2017, 10, 1),
        null,
        null,
        null,
        null
    ))
    workRecordRepository.save(WorkRecord(
        UserId("user"),
        WorkDate.of(2017, 10, 31),
        WorkTime.of(9, 0),
        WorkTime.of(18, 0),
        WorkHours.ofHours(1),
        "sample"
    ))
    workRecordRepository.save(WorkRecord(
        UserId("user"),
        WorkDate.of(2017, 11, 1),
        WorkTime.of(9, 0),
        WorkTime.of(18, 0),
        WorkHours.ofHours(1),
        "sample"
    ))
  }

  @Test
  fun test_findByYearMonth() {
    val records = sut.findByYearMonth(UserId("user"), 2017, 10)

    assertThat(records).containsExactly(
        WorkRecord(
            UserId("user"),
            WorkDate.of(2017, 10, 1),
            null,
            null,
            null,
            null
        ),
        WorkRecord(
            UserId("user"),
            WorkDate.of(2017, 10, 31),
            WorkTime.of(9, 0),
            WorkTime.of(18, 0),
            WorkHours.ofHours(1),
            "sample"
        )
    ).hasSize(2)
  }

  @Test
  fun test_findByDate() {
    val record = sut.findByDate(UserId("user"), WorkDate.of(2017, 10, 1))

    assertThat(record).isEqualTo(WorkRecord(
        UserId("user"),
        WorkDate.of(2017, 10, 1),
        null,
        null,
        null,
        null
    ))
  }

  @Test
  fun test_findByDate_notFound() {
    val record = sut.findByDate(UserId("user"), WorkDate.of(2017, 10, 2))

    assertThat(record).isNull()
  }

  @Test
  fun test_save_insert() {
    val record = WorkRecord(
        UserId("user"),
        WorkDate.of(2017, 10, 2),
        WorkTime.of(10, 10),
        WorkTime.of(20, 20),
        null,
        null
    )

    sut.save(record)

    val saved = workRecordRepository.findByUserIdAndDate(UserId("user"), record.date)
    assertThat(saved).isEqualTo(record)
  }

  @Test
  fun test_save_update() {
    val record = WorkRecord(
        UserId("user"),
        WorkDate.of(2017, 10, 1),
        WorkTime.of(10, 10),
        WorkTime.of(20, 20),
        null,
        null
    )

    sut.save(record)

    val saved = workRecordRepository.findByUserIdAndDate(UserId("user"), record.date)
    assertThat(saved).isEqualTo(record)
  }

  @Test
  fun test_deleteByDate() {
    sut.deleteByDate(UserId("user"), WorkDate.of(2017, 10, 1))

    val deleted = workRecordRepository.findByUserIdAndDate(UserId("user"), WorkDate.of(2017, 10, 1))
    assertThat(deleted).isNull()
  }

  @Test
  fun test_deleteByDate_notFound() {
    sut.deleteByDate(UserId("user"), WorkDate.of(2017, 10, 2))
  }
}