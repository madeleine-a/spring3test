package se.mad.db

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Version
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.*


@Entity
data class InstantEntity(val date_value: Instant) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private val id: UUID? = null // Actually NOT NULL in DB

    @CreationTimestamp
    var created: Instant = Instant.now()

    @UpdateTimestamp
    var updated: Instant = Instant.now()

    @Version
    var version: Long = 0

}