package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime

interface OffsetDateTimeEntityRepository : JpaRepository<OffsetDateTimeEntity, Long> {
    fun findFirstByDateValue(dateValue: OffsetDateTime): OffsetDateTimeEntity?

    fun deleteAllByDateValueBefore(dateValue: OffsetDateTime): Long

    fun findAllByDateValueBetween(from: OffsetDateTime, to: OffsetDateTime): List<OffsetDateTimeEntity>
}