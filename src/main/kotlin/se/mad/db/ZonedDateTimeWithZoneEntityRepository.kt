package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.*

interface ZonedDateTimeWithZoneEntityRepository : JpaRepository<ZonedDateTimeWithZoneEntity, Long> {
    fun findFirstByDateValue(dateValue: ZonedDateTime): ZonedDateTimeWithZoneEntity?
    fun deleteAllByDateValueBefore(dateValue: ZonedDateTime): Long

}