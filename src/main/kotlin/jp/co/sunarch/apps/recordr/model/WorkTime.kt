package jp.co.sunarch.apps.recordr.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.util.StdConverter
import java.time.Duration
import java.time.LocalTime
import java.util.Comparator.comparing

private val TEXT_REGEX = """(?<hour>\d+):(?<minute>\d+)""".toRegex()

@JsonSerialize(converter = WorkTimeToStringConverter::class)
@JsonDeserialize(converter = StringToWorkTimeConverter::class)
data class WorkTime(val hour: Int, val minute: Int) : Comparable<WorkTime> {

  fun durationTo(other: WorkTime): Duration {
    require(this <= other, { "The other must be >= this" })

    return Duration.ofHours((other.hour - hour).toLong())
        .plusMinutes((other.minute - minute).toLong())
  }

  override fun toString(): String {
    return String.format("%02d:%02d", hour, minute)
  }

  override fun compareTo(other: WorkTime): Int = comparator.compare(this, other)

  companion object {

    fun of(hour: Int, minute: Int): WorkTime {
      require(hour >= 0, { "The hour must be >= 0" })
      require(minute >= 0, { "The minute must be >= 0" })
      return WorkTime(hour + minute / 60, minute % 60)
    }

    fun parse(text: String): WorkTime {
      val result = TEXT_REGEX.find(text)?: throw IllegalArgumentException()
      val hour = result.groups["hour"]!!.value.toInt()
      val minute = result.groups["minute"]!!.value.toInt()
      return WorkTime(hour, minute)
    }

    val comparator: Comparator<WorkTime> = comparing(WorkTime::hour)
        .thenComparing(comparing(WorkTime::minute))

  }
}

private class WorkTimeToStringConverter : StdConverter<WorkTime, String>() {
  override fun convert(value: WorkTime): String = value.toString()
}

private class StringToWorkTimeConverter : StdConverter<String, WorkTime>() {
  override fun convert(value: String): WorkTime = WorkTime.parse(value)
}