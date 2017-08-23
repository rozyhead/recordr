package jp.co.sunarch.apps.recordr.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class WorkHoursTest {

  @Test
  fun test_toString_0000() {
    val hours = WorkHours.ofHours(0)
    assertThat(hours.toString()).isEqualTo("0.00")
  }

  @Test
  fun test_toString_0100() {
    val hours = WorkHours.ofHours(1)
    assertThat(hours.toString()).isEqualTo("1.00")
  }

  @Test
  fun test_toString_0115() {
    val hours = WorkHours.of(1, 15)
    assertThat(hours.toString()).isEqualTo("1.25")
  }

  @Test
  fun test_toString_0116() {
    val hours = WorkHours.of(1, 16)
    assertThat(hours.toString()).isEqualTo("1.27")
  }

  @Test
  fun test_between_0900_to_1800() {
    val hours = WorkHours.between(
        WorkTime.of(9, 0),
        WorkTime.of(18, 0))

    assertThat(hours.toString()).isEqualTo("9.00")
  }

  @Test
  fun test_between_0900_to_1830() {
    val hours = WorkHours.between(
        WorkTime.of(9, 0),
        WorkTime.of(18, 30))

    assertThat(hours.toString()).isEqualTo("9.50")
  }

  @Test
  fun test_between_0900_to_1845() {
    val hours = WorkHours.between(
        WorkTime.of(9, 0),
        WorkTime.of(18, 45))

    assertThat(hours.toString()).isEqualTo("9.75")
  }

  @Test
  fun test_between_0900_to_2500() {
    val hours = WorkHours.between(
        WorkTime.of(9, 0),
        WorkTime.of(25, 0))

    assertThat(hours.toString()).isEqualTo("16.00")
  }

  @Test
  fun test_between_0900_to_0900() {
    val hours = WorkHours.between(
        WorkTime.of(9, 0),
        WorkTime.of(9, 0))

    assertThat(hours.toString()).isEqualTo("0.00")
  }

  @Test
  fun test_between_0900_to_0859() {
    assertThatThrownBy {
      WorkHours.between(
          WorkTime.of(9, 0),
          WorkTime.of(8, 59))
    }.isInstanceOf(IllegalArgumentException::class.java)
  }

}
