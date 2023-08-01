package se.mad.db

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime
import java.util.*


@Entity
data class OffsetDateWithZoneTimeEntity(val date_value: OffsetDateTime) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private val id: UUID? = null // Actually NOT NULL in DB
    
    @CreationTimestamp
    var created: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    var updated: OffsetDateTime = OffsetDateTime.now()

    @Version
    var version: Long = 0

}