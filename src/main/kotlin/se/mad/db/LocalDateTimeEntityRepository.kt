package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface LocalDateTimeEntityRepository: JpaRepository<LocalDateTimeEntity, Long> {
    fun findFirstByDateValue(dateValue: LocalDateTime): LocalDateTimeEntity?

    fun deleteAllByDateValueBefore(dateValue: LocalDateTime): Long

    fun findAllByDateValueBetween(from: LocalDateTime, to: LocalDateTime): List<LocalDateTimeEntity>
}