package jp.co.sunarch.apps.recordr.controller

import jp.co.sunarch.apps.recordr.model.*
import jp.co.sunarch.apps.recordr.service.WorkRecordService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@RunWith(SpringRunner::class)
@WebMvcTest(WorkRecordController::class)
class WorkRecordControllerTest {

  @Autowired
  lateinit var mvc: MockMvc

  @MockBean
  lateinit var workRecordService: WorkRecordService

  @Test
  @WithMockUser
  fun test_findByYearMonth() {
    given(this.workRecordService.findByYearMonth(UserId("user"), 2017, 10))
        .willReturn(listOf(
            WorkRecord(
                UserId("user"),
                WorkDate.of(2017, 10, 10),
                WorkTime.of(10, 0),
                WorkTime.of(19, 0),
                WorkHours.ofHours(1),
                null),
            WorkRecord(
                UserId("user"),
                WorkDate.of(2017, 10, 11),
                WorkTime.of(11, 0),
                WorkTime.of(20, 0),
                WorkHours.ofHours(1),
                "sample")
        ))

    this.mvc.perform(
        get("/api/v1/me/records")
            .param("year", "2017")
            .param("month", "10")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk)
        .andExpect(content().json("""
          |[
          |  {
          |    "userId": "user",
          |    "date": "2017-10-10",
          |    "startTime": "10:00",
          |    "endTime": "19:00",
          |    "restHours": 1,
          |    "notes": null
          |  },
          |  {
          |    "userId": "user",
          |    "date": "2017-10-11",
          |    "startTime": "11:00",
          |    "endTime": "20:00",
          |    "restHours": 1,
          |    "notes": "sample"
          |  }
          |]
        """.trimMargin()))
  }

  @Test
  fun test_findByYearMonth_noAuth() {
    this.mvc.perform(
        get("/api/v1/me/records")
            .param("year", "2017")
            .param("month", "10")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isUnauthorized)
  }

  @Test
  @WithMockUser
  fun test_findByDate() {
    given(this.workRecordService.findByDate(UserId("user"), WorkDate.of(2017, 10, 10)))
        .willReturn(
            WorkRecord(
                UserId("user"),
                WorkDate.of(2017, 10, 10),
                WorkTime.of(10, 0),
                WorkTime.of(19, 0),
                WorkHours.ofHours(1),
                null)
        )

    this.mvc.perform(
        get("/api/v1/me/records/2017-10-10")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk)
        .andExpect(content().json("""
          |{
          |  "userId": "user",
          |  "date": "2017-10-10",
          |  "startTime": "10:00",
          |  "endTime": "19:00",
          |  "restHours": 1,
          |  "notes": null
          |}
        """.trimMargin()))
  }

  @Test
  @WithMockUser
  fun test_findByDate_notFound() {
    given(this.workRecordService.findByDate(UserId("user"), WorkDate.of(2017, 10, 10)))
        .willReturn(null)

    this.mvc.perform(
        get("/api/v1/me/records/2017-10-10")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isNotFound)
  }

  @Test
  fun test_findByDate_noAuth() {
    this.mvc.perform(
        get("/api/v1/me/records/2017-10-10")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isUnauthorized)
  }

  @Test
  @WithMockUser
  fun test_save_post() {
    given(this.workRecordService.findByDate(UserId("user"), WorkDate.of(2017, 10, 10)))
        .willReturn(
            WorkRecord(
                UserId("user"),
                WorkDate.of(2017, 10, 10),
                WorkTime.of(10, 0),
                WorkTime.of(19, 0),
                WorkHours.ofHours(1),
                null)
        )

    this.mvc.perform(
        post("/api/v1/me/records/2017-10-10")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              |{
              |  "startTime": "10:00",
              |  "endTime": "19:00",
              |  "restHours": 1,
              |  "notes": null
              |}
            """.trimMargin())
    ).andExpect(status().isOk)
        .andExpect(content().json("""
          |{
          |  "userId": "user",
          |  "date": "2017-10-10",
          |  "startTime": "10:00",
          |  "endTime": "19:00",
          |  "restHours": 1,
          |  "notes": null
          |}
        """.trimMargin()))

    then(this.workRecordService)
        .should(times(1))
        .save(WorkRecord(
            UserId("user"),
            WorkDate.of(2017, 10, 10),
            WorkTime.of(10, 0),
            WorkTime.of(19, 0),
            WorkHours.ofHours(1),
            null
        ))
  }

  @Test
  @WithMockUser
  fun test_save_put() {
    given(this.workRecordService.findByDate(UserId("user"), WorkDate.of(2017, 10, 10)))
        .willReturn(
            WorkRecord(
                UserId("user"),
                WorkDate.of(2017, 10, 10),
                WorkTime.of(10, 0),
                WorkTime.of(19, 0),
                WorkHours.ofHours(1),
                null)
        )

    this.mvc.perform(
        put("/api/v1/me/records/2017-10-10")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              |{
              |  "startTime": "10:00",
              |  "endTime": "19:00",
              |  "restHours": 1,
              |  "notes": null
              |}
            """.trimMargin())
    ).andExpect(status().isOk)
        .andExpect(content().json("""
          |{
          |  "userId": "user",
          |  "date": "2017-10-10",
          |  "startTime": "10:00",
          |  "endTime": "19:00",
          |  "restHours": 1,
          |  "notes": null
          |}
        """.trimMargin()))

    then(this.workRecordService)
        .should(times(1))
        .save(WorkRecord(
            UserId("user"),
            WorkDate.of(2017, 10, 10),
            WorkTime.of(10, 0),
            WorkTime.of(19, 0),
            WorkHours.ofHours(1),
            null
        ))
  }

  @Test
  fun test_save_noAuth() {
    this.mvc.perform(
        post("/api/v1/me/records/2017-10-10")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              |{
              |  "startTime": "10:00",
              |  "endTime": "19:00",
              |  "restHours": 1,
              |  "notes": null
              |}
            """.trimMargin())
    ).andExpect(status().isUnauthorized)
  }

  @Test
  @WithMockUser
  fun test_deleteByDate() {
    this.mvc.perform(
        delete("/api/v1/me/records/2017-10-10")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk)

    then(this.workRecordService)
        .should(times(1))
        .deleteByDate(UserId("user"), WorkDate.of(2017, 10, 10))
  }

  @Test
  fun test_deletedByDate_noAuth() {
    this.mvc.perform(
        delete("/api/v1/me/records/2017-10-10")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isUnauthorized)
  }

}