package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface InstantEntityRepository: JpaRepository<InstantEntity, Long> {
    fun findFirstByDateValue(dateValue: Instant): InstantEntity?

    fun deleteAllByDateValueBefore(dateValue: Instant): Long

    fun findAllByDateValueBetween(from: Instant, to: Instant): List<InstantEntity>
}