package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime

interface OffsetDateTimeWithZoneEntityRepository : JpaRepository<OffsetDateTimeWithZoneEntity, Long> {
    fun findFirstByDateValue(dateValue: OffsetDateTime): OffsetDateTimeWithZoneEntity?

    fun deleteAllByDateValueBefore(dateValue: OffsetDateTime): Long

    fun findAllByDateValueBetween(from: OffsetDateTime, to: OffsetDateTime): List<OffsetDateTimeWithZoneEntity>
}