package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

interface LocalDateTimeEntityRepository: JpaRepository<LocalDateTimeEntity, Long> {
    fun findFirstByDateValue(dateValue: LocalDateTime): LocalDateTimeEntity?

    fun deleteAllByDateValueBefore(dateValue: LocalDateTime): Long
}