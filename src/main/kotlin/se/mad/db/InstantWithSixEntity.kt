package se.mad.db

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant


@Entity
data class InstantWithSixEntity(val dateValue: Instant) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null // Actually NOT NULL in DB

    @CreationTimestamp
    var created: Instant = Instant.now()

    @UpdateTimestamp
    var updated: Instant = Instant.now()

    @Version
    var version: Long = 0

}