package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface InstantWithSixEntityRepository: JpaRepository<InstantWithSixEntity, Long> {
    fun findFirstByDateValue(dateValue: Instant): InstantWithSixEntity?

    fun deleteAllByDateValueBefore(dateValue: Instant): Long

    fun findAllByDateValueBetween(from: Instant, to: Instant): List<InstantWithSixEntity>
}