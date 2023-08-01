package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

interface OffsetDateTimeWithZoneEntityRepository : JpaRepository<OffsetDateTimeWithZoneEntity, Long> {
    fun findFirstByDateValue(dateValue: OffsetDateTime): OffsetDateTimeWithZoneEntity?

    fun deleteAllByDateValueBefore(dateValue: OffsetDateTime): Long
}