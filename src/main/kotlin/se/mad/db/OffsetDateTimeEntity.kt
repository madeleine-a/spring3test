package se.mad.db

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*


@Entity
data class LocalDateTimeEntity(val date_value: LocalDateTime) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private val id: UUID? = null // Actually NOT NULL in DB
    
    @CreationTimestamp
    var created: LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    var updated: LocalDateTime = LocalDateTime.now()

}