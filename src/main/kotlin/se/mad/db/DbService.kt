package se.mad.db

import jakarta.persistence.criteria.CriteriaBuilder
import oracle.net.aso.l
import org.springframework.stereotype.Service
import se.mad.logger
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime

@Service
class DbService(
    private val instantEntityRepository: InstantEntityRepository,
    private val instantWithZoneEntityRepository: InstantWithZoneEntityRepository,
    private val localDateTimeEntityRepository: LocalDateTimeEntityRepository,
    private val offsetDateTimeEntityRepository: OffsetDateTimeEntityRepository,
    private val offsetDateTimeWithZoneEntityRepository: OffsetDateTimeWithZoneEntityRepository,
    private val zonedDateTimeEntityRepository: ZonedDateTimeEntityRepository,
    private val zonedDateTimeWithZoneEntityRepository: ZonedDateTimeWithZoneEntityRepository

) {
    companion object{
        val logger = logger()
    }

    fun deleteThemAll(){
        localDateTimeEntityRepository.deleteAllInBatch()
        instantEntityRepository.deleteAllInBatch()
        offsetDateTimeEntityRepository.deleteAllInBatch()
        zonedDateTimeEntityRepository.deleteAllInBatch()
        instantWithZoneEntityRepository.deleteAllInBatch()
        offsetDateTimeWithZoneEntityRepository.deleteAllInBatch()
        zonedDateTimeWithZoneEntityRepository.deleteAllInBatch()
    }

    fun saveOneOfEach(){
        val i = Instant.now()
        val l = LocalDateTime.now()
        val o = OffsetDateTime.now()
        val z = ZonedDateTime.now()
        logger.info("Instant: $i, LocalDateTime: $l, OffsetDateTime: $o, ZonedDateTime: $z")

        localDateTimeEntityRepository.save(LocalDateTimeEntity(l))
        instantEntityRepository.save(InstantEntity(i))
        offsetDateTimeEntityRepository.save(OffsetDateTimeEntity(o))
        zonedDateTimeEntityRepository.save(ZonedDateTimeEntity(z))
        instantWithZoneEntityRepository.save(InstantWithZoneEntity(i))
        offsetDateTimeWithZoneEntityRepository.save(OffsetDateTimeWithZoneEntity(o))
        zonedDateTimeWithZoneEntityRepository.save(ZonedDateTimeWithZoneEntity(z))
    }

    fun getThemAll(): MutableMap<String, List<Any>> {
        val map = mutableMapOf<String, List<Any>>()
        map["LocalDateTime"] = localDateTimeEntityRepository.findAll()
        map["Instant"] = instantEntityRepository.findAll()
        map["OffsetDateTime"] = offsetDateTimeEntityRepository.findAll()
        map["ZonedDateTime"] = zonedDateTimeEntityRepository.findAll()
        map["InstantWithZone"] =        instantWithZoneEntityRepository.findAll()
        map["OffsetDateTimeWithZone"] = offsetDateTimeWithZoneEntityRepository.findAll()
        map["ZonedDateTimeWithZone"] = zonedDateTimeWithZoneEntityRepository.findAll()
        return map
    }

    fun save(dateValue: LocalDateTime) = localDateTimeEntityRepository.save(LocalDateTimeEntity(dateValue))
    fun save(dateValue: Instant) = instantEntityRepository.save(InstantEntity(dateValue))
    fun saveWithZone(dateValue: Instant) = instantWithZoneEntityRepository.save(InstantWithZoneEntity(dateValue))
    fun save(dateValue: OffsetDateTime) = offsetDateTimeEntityRepository.save(OffsetDateTimeEntity(dateValue))
    fun saveWithZone(dateValue: OffsetDateTime) = offsetDateTimeWithZoneEntityRepository.save(OffsetDateTimeWithZoneEntity(dateValue))
    fun save(dateValue: ZonedDateTime) =zonedDateTimeEntityRepository.save(ZonedDateTimeEntity(dateValue))
    fun saveWithZone(dateValue: ZonedDateTime) = zonedDateTimeWithZoneEntityRepository.save(ZonedDateTimeWithZoneEntity(dateValue))

    fun findInstantByDateValue(dateValue: Instant) = instantEntityRepository.findFirstByDateValue(dateValue)
    fun findInstantWithZoneByDateValue(dateValue: Instant) = instantWithZoneEntityRepository.findFirstByDateValue(dateValue)
    fun findLocalDateTimeByDateValue(dateValue: LocalDateTime) = localDateTimeEntityRepository.findFirstByDateValue(dateValue)

    fun findOffsetDateTimeByDateValue(dateValue: OffsetDateTime) = offsetDateTimeEntityRepository.findFirstByDateValue(dateValue)
    fun findOffsetDateTimeWithZoneByDateValue(dateValue: OffsetDateTime) = offsetDateTimeWithZoneEntityRepository.findFirstByDateValue(dateValue)

    fun findZonedDateTimeByDateValue(dateValue: ZonedDateTime) = zonedDateTimeEntityRepository.findFirstByDateValue(dateValue)
    fun findZonedDateTimeWithZoneByDateValue(dateValue: ZonedDateTime) = zonedDateTimeWithZoneEntityRepository.findFirstByDateValue(dateValue)



}