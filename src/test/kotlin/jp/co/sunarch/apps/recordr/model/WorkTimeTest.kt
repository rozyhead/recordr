package jp.co.sunarch.apps.recordr.model

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.SoftAssertions
import org.junit.Test

import java.time.Duration

import org.assertj.core.api.Assertions.*

/**
 * @author takeshi
 */
class WorkTimeTest {

  private val objectMapper = ObjectMapper()

  @Test
  fun test_toString() {
    assertThat(WorkTime.of(10, 0).toString())
        .isEqualTo("10:00")
  }

  @Test
  fun test_toJson() {
    val time = WorkTime.of(10, 0)
    assertThat(objectMapper.writeValueAsString(time))
        .isEqualTo(""""10:00"""")
  }

  @Test
  fun test_fromJson() {
    assertThat(objectMapper.readValue(""""10:00"""", WorkTime::class.java))
        .isEqualTo(WorkTime.of(10, 0))
  }

  @Test
  fun test_of_0900() {
    val (hour, minute) = WorkTime.of(9, 0)

    val softly = SoftAssertions()
    softly.assertThat(hour).isEqualTo(9)
    softly.assertThat(minute).isEqualTo(0)
    softly.assertAll()
  }

  @Test
  fun test_of_0970() {
    val (hour, minute) = WorkTime.of(9, 70)

    val softly = SoftAssertions()
    softly.assertThat(hour).isEqualTo(10)
    softly.assertThat(minute).isEqualTo(10)
    softly.assertAll()
  }

  @Test
  fun test_toString_0900() {
    val time = WorkTime.of(9, 0)
    assertThat(time.toString()).isEqualTo("09:00")
  }

  @Test
  fun test_toString_2400() {
    val time = WorkTime.of(24, 0)
    assertThat(time.toString()).isEqualTo("24:00")
  }

  @Test
  fun test_0900_isBefore_0901() {
    val time = WorkTime.of(9, 0)
    assertThat(time < WorkTime.of(9, 1)).isTrue()
  }

  @Test
  fun test_0901_isNotBefore_0900() {
    val time = WorkTime.of(9, 1)
    assertThat(time < WorkTime.of(9, 0)).isFalse()
  }

  @Test
  fun test_0900_isBeforeOrEquals_0900() {
    val time = WorkTime.of(9, 0)
    assertThat(time <= WorkTime.of(9, 0)).isTrue()
  }

  @Test
  fun test_0900_isNotBeforeOrEquals_0859() {
    val time = WorkTime.of(9, 0)
    assertThat(time <= WorkTime.of(8, 59)).isFalse()
  }

  @Test
  fun test_0900_isNotAfter_0901() {
    val time = WorkTime.of(9, 0)
    assertThat(time > WorkTime.of(9, 1)).isFalse()
  }

  @Test
  fun test_0901_isAfter_0900() {
    val time = WorkTime.of(9, 1)
    assertThat(time > WorkTime.of(9, 0)).isTrue()
  }

  @Test
  fun test_0900_isAfterOrEquals_0900() {
    val time = WorkTime.of(9, 0)
    assertThat(time >= WorkTime.of(9, 0)).isTrue()
  }

  @Test
  fun test_0900_isNotAfterOrEquals_0901() {
    val time = WorkTime.of(9, 0)
    assertThat(time > WorkTime.of(9, 1)).isFalse()
  }

  @Test
  fun test_0900_duration_to_0900() {
    val time = WorkTime.of(9, 0)
    assertThat(time.durationTo(WorkTime.of(9, 0))).isEqualTo(Duration.ofMinutes(0))
  }

  @Test
  fun test_0900_duration_to_0901() {
    val time = WorkTime.of(9, 0)
    assertThat(time.durationTo(WorkTime.of(9, 1))).isEqualTo(Duration.ofMinutes(1))
  }

  @Test
  fun test_0900_duration_to_1000() {
    val time = WorkTime.of(9, 0)
    assertThat(time.durationTo(WorkTime.of(10, 0))).isEqualTo(Duration.ofMinutes(60))
  }

  @Test
  fun test_0930_duration_to_1000() {
    val time = WorkTime.of(9, 30)
    assertThat(time.durationTo(WorkTime.of(10, 0))).isEqualTo(Duration.ofMinutes(30))
  }

  @Test
  fun test_parse_1000() {
    val time = WorkTime.parse("10:00")
    assertThat(time).isEqualTo(WorkTime.of(10, 0))
  }

  @Test
  fun test_parse_1030() {
    val time = WorkTime.parse("10:30")
    assertThat(time).isEqualTo(WorkTime.of(10, 30))
  }
}
