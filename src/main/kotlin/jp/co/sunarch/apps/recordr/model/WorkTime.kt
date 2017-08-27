package jp.co.sunarch.apps.recordr.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.Duration
import java.time.LocalTime
import java.util.Comparator.*

@JsonSerialize(using = WorkTime.JsonSerializer::class)
@JsonDeserialize(using = WorkTime.JsonDeserializer::class)
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
      val time = LocalTime.parse(text)
      return WorkTime(time.hour, time.minute)
    }

    val comparator: Comparator<WorkTime> = comparing(WorkTime::hour)
        .thenComparing(comparing(WorkTime::minute))

  }

  class JsonSerializer : StdSerializer<WorkTime>(WorkTime::class.java) {
    override fun serialize(value: WorkTime, gen: JsonGenerator, provider: SerializerProvider) {
      value.let { gen.writeString(it.toString()) }
    }
  }

  class JsonDeserializer : StdDeserializer<WorkTime>(WorkTime::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): WorkTime {
      return p.readValueAs(String::class.java)
          .let { WorkTime.parse(it) }
    }
  }

}