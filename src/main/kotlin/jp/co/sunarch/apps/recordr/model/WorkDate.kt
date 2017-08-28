package jp.co.sunarch.apps.recordr.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.util.StdConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@JsonSerialize(converter = WorkDateToStringConverter::class)
@JsonDeserialize(converter = StringToWorkDateConverter::class)
data class WorkDate(private val value: LocalDate) {

  override fun toString(): String {
    return FORMATTER.format(value)
  }

  companion object {
    private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun of(year: Int, month: Int, dayOfMonth: Int): WorkDate = WorkDate(LocalDate.of(year, month, dayOfMonth))

    fun parse(text: CharSequence): WorkDate = WorkDate(LocalDate.parse(text, FORMATTER))
  }

}

private class WorkDateToStringConverter : StdConverter<WorkDate, String>() {
  override fun convert(value: WorkDate): String = value.toString()
}

private class StringToWorkDateConverter : StdConverter<String, WorkDate>() {
  override fun convert(value: String): WorkDate = WorkDate.parse(value)
}
