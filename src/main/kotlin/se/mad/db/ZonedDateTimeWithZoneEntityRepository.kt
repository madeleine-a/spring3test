package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.ZonedDateTime

interface ZonedDateTimeWithZoneEntityRepository : JpaRepository<ZonedDateTimeWithZoneEntity, Long> {
    fun findFirstByDateValue(dateValue: ZonedDateTime): ZonedDateTimeWithZoneEntity?
    fun deleteAllByDateValueBefore(dateValue: ZonedDateTime): Long

    fun findAllByDateValueBetween(from: ZonedDateTime, to: ZonedDateTime): List<ZonedDateTimeWithZoneEntity>

}