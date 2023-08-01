package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.*

interface InstantWithZoneEntityRepository: JpaRepository<InstantWithZoneEntity, Long> {
    fun findFirstByDateValue(dateValue: Instant): InstantWithZoneEntity?

    fun deleteAllByDateValueBefore(dateValue: Instant): Long
}