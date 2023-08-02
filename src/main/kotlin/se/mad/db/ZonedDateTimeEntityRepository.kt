package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.ZonedDateTime

interface ZonedDateTimeEntityRepository : JpaRepository<ZonedDateTimeEntity, Long> {
    fun findFirstByDateValue(dateValue: ZonedDateTime): ZonedDateTimeEntity?

    fun deleteAllByDateValueBefore(dateValue: ZonedDateTime): Long

    fun findAllByDateValueBetween(from: ZonedDateTime, to: ZonedDateTime): List<ZonedDateTimeEntity>
}