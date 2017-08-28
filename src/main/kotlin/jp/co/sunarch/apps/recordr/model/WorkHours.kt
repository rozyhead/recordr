package jp.co.sunarch.apps.recordr.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.util.StdConverter
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

private val MINUTES_IN_HOUR = 60
private val MINUTES_IN_HOUR_DECIMAL = BigDecimal.valueOf(MINUTES_IN_HOUR.toLong())

@JsonSerialize(converter = WorkHoursToBigDecimalConverter::class)
@JsonDeserialize(converter = BigDecimalToWorkHoursConverter::class)
data class WorkHours(val minutes: Int = 0) : Comparable<WorkHours> {

  operator fun plus(other: WorkHours): WorkHours {
    return WorkHours(minutes + other.minutes)
  }

  operator fun minus(other: WorkHours): WorkHours {
    return WorkHours(minutes - other.minutes)
  }

  operator fun times(times: Int): WorkHours {
    return WorkHours(minutes * times)
  }

  operator fun div(div: Int): WorkHours {
    return WorkHours(minutes / div)
  }

  override fun toString(): String {
    val hours = BigDecimal.valueOf(this.minutes.toLong() / MINUTES_IN_HOUR)
    val minutes = BigDecimal.valueOf(this.minutes.toLong() % MINUTES_IN_HOUR)
        .divide(MINUTES_IN_HOUR_DECIMAL, 2, RoundingMode.HALF_UP)

    val format = NumberFormat.getInstance()
    format.maximumFractionDigits = 2
    format.minimumFractionDigits = 2
    return format.format(hours.add(minutes))
  }

  override fun compareTo(other: WorkHours): Int {
    return java.lang.Integer.compare(minutes, other.minutes)
  }

  companion object {
    val ZERO = WorkHours.ofMinutes(0)

    fun of(hours: Int, minutes: Int): WorkHours {
      return WorkHours(hours * MINUTES_IN_HOUR + minutes)
    }

    fun ofHours(hours: Int): WorkHours {
      return WorkHours(hours * MINUTES_IN_HOUR)
    }

    fun ofMinutes(minutes: Int): WorkHours {
      return WorkHours(minutes)
    }

    fun between(start: WorkTime, end: WorkTime): WorkHours {
      require(start <= end, { "The start must be before or equals to end" })

      val duration = start.durationTo(end)
      val minutes = duration.toMinutes()
      return WorkHours(minutes.toInt())
    }
  }
}

private class WorkHoursToBigDecimalConverter : StdConverter<WorkHours, BigDecimal>() {
  override fun convert(value: WorkHours): BigDecimal {
    return BigDecimal(value.minutes).divide(MINUTES_IN_HOUR_DECIMAL, 2, RoundingMode.HALF_UP)
  }
}

private class BigDecimalToWorkHoursConverter : StdConverter<BigDecimal, WorkHours>() {
  override fun convert(value: BigDecimal): WorkHours {
    return WorkHours((value * MINUTES_IN_HOUR_DECIMAL).toInt())
  }
}
