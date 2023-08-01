package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.*

interface ZonedDateTimeEntityRepository : JpaRepository<ZonedDateTimeEntity, Long> {
    fun findFirstByDateValue(dateValue: ZonedDateTime): ZonedDateTimeEntity?

    fun deleteAllByDateValueBefore(dateValue: ZonedDateTime): Long
}