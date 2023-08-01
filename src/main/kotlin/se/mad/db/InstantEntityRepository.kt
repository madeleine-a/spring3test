package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.*

interface InstantEntityRepository: JpaRepository<InstantEntity, Long> {
    fun findFirstByDateValue(dateValue: Instant): InstantEntity?

    fun deleteAllByDateValueBefore(dateValue: Instant): Long
}