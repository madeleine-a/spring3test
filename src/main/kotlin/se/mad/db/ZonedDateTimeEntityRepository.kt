package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OffsetDateTimeEntityRepository : JpaRepository<OffsetDateTimeEntity, UUID> {
}