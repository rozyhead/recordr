package jp.co.sunarch.apps.recordr.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.util.StdConverter

@JsonSerialize(converter = UserIdToStringConverter::class)
@JsonDeserialize(converter = StringToUserIdConverter::class)
data class UserId(val value: String)

private class UserIdToStringConverter : StdConverter<UserId, String>() {
  override fun convert(value: UserId): String = value.value
}

private class StringToUserIdConverter : StdConverter<String, UserId>() {
  override fun convert(value: String): UserId = UserId(value)
}
