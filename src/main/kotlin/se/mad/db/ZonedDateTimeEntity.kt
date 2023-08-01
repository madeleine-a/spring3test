package se.mad.db

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime
import java.util.*


@Entity
data class OffsetDateTimeEntity(val date_value: OffsetDateTime) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private val id: UUID? = null // Actually NOT NULL in DB
    
    @CreationTimestamp
    var created: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    var updated: OffsetDateTime = OffsetDateTime.now()

}