package se.mad.db

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*


@Entity
data class ZonedDateTimeWithZoneEntity(val dateValue: ZonedDateTime) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null // Actually NOT NULL in DB
    
    @CreationTimestamp
    var created: ZonedDateTime = ZonedDateTime.now()

    @UpdateTimestamp
    var updated: ZonedDateTime = ZonedDateTime.now()

    @Version
    var version: Long = 0
}