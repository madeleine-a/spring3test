package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LocalDateTimeEntityRepository: JpaRepository<LocalDateTimeEntity, UUID> {
}