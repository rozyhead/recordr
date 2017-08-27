package jp.co.sunarch.apps.recordr.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

@JsonSerialize(using = WorkHours.JsonSerializer::class)
@JsonDeserialize(using = WorkHours.JsonDeserializer::class)
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
    private val MINUTES_IN_HOUR = 60
    private val MINUTES_IN_HOUR_DECIMAL = BigDecimal.valueOf(MINUTES_IN_HOUR.toLong())

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

  class JsonSerializer : StdSerializer<WorkHours>(WorkHours::class.java) {
    override fun serialize(value: WorkHours, gen: JsonGenerator, provider: SerializerProvider) {
      value.let { gen.writeNumber(BigDecimal(it.minutes).divide(MINUTES_IN_HOUR_DECIMAL, 2, RoundingMode.HALF_UP)) }
    }
  }

  class JsonDeserializer : StdDeserializer<WorkHours>(WorkHours::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): WorkHours {
      return p.readValueAs(BigDecimal::class.java)
          .let { WorkHours((it * MINUTES_IN_HOUR_DECIMAL).toInt()) }
    }
  }
}

