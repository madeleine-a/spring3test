package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

interface OffsetDateTimeEntityRepository : JpaRepository<OffsetDateTimeEntity, Long> {
    fun findFirstByDateValue(dateValue: OffsetDateTime): OffsetDateTimeEntity?

    fun deleteAllByDateValueBefore(dateValue: OffsetDateTime): Long
}