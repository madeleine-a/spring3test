package se.mad.db

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*


@Entity
data class LocalDateTimeEntity(val dateValue: LocalDateTime) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null // Actually NOT NULL in DB
    
    @CreationTimestamp
    var created: LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    var updated: LocalDateTime = LocalDateTime.now()

    @Version
    var version: Long = 0

}