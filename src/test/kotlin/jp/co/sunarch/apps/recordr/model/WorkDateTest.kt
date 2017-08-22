package jp.co.sunarch.apps.recordr.model

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.*
import org.junit.Test

/**
 * @author takeshi
 */
class WorkDateTest {

  private val objectMapper = ObjectMapper()

  @Test
  fun test_toString() {
    assertThat(WorkDate.of(2017, 1, 1).toString())
        .isEqualTo("2017-01-01")
  }

  @Test
  fun test_toJson() {
    val date = WorkDate.of(2017, 1, 1)
    assertThat(objectMapper.writeValueAsString(date))
        .isEqualTo(""""2017-01-01"""")
  }

  @Test
  fun test_fromJson() {
    assertThat(objectMapper.readValue(""""2017-01-01"""", WorkDate::class.java))
        .isEqualTo(WorkDate.of(2017, 1, 1))
  }

}