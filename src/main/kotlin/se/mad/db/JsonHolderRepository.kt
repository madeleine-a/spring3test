package se.mad.db

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface JsonHolderRepository: JpaRepository<JsonHolder, Long> {
}