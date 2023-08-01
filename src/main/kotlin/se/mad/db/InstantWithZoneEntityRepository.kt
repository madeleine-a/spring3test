package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface InstantEntityRepository: JpaRepository<InstantEntity, UUID> {
}