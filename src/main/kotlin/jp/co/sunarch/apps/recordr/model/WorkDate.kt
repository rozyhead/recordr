package jp.co.sunarch.apps.recordr.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@JsonSerialize(using = WorkDate.JsonSerializer::class)
@JsonDeserialize(using = WorkDate.JsonDeserializer::class)
data class WorkDate(private val value: LocalDate) {

  override fun toString(): String {
    return FORMATTER.format(value)
  }

  companion object {
    private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun of(year: Int, month: Int, dayOfMonth: Int): WorkDate = WorkDate(LocalDate.of(year, month, dayOfMonth))

    fun parse(text: CharSequence): WorkDate = WorkDate(LocalDate.parse(text, FORMATTER))
  }

  object JsonSerializer : StdSerializer<WorkDate>(WorkDate::class.java) {
    override fun serialize(value: WorkDate, gen: JsonGenerator, provider: SerializerProvider) {
      value.let { gen.writeString(it.toString()) }
    }
  }

  object JsonDeserializer : StdDeserializer<WorkDate>(WorkDate::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): WorkDate {
      return p.readValueAs(String::class.java)
          .let { WorkDate(LocalDate.parse(it, FORMATTER)) }
    }
  }

}