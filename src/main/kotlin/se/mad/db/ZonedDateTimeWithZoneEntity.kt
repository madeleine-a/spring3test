package se.mad.db

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*


@Entity
data class ZonedDateTimeEntity(val date_value: ZonedDateTime) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private val id: UUID? = null // Actually NOT NULL in DB
    
    @CreationTimestamp
    var created: ZonedDateTime = ZonedDateTime.now()

    @UpdateTimestamp
    var updated: ZonedDateTime = ZonedDateTime.now()

    @Version
    var version: Long = 0
}