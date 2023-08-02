package se.mad.db

import org.springframework.stereotype.Service
import se.mad.logger
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime

@Service
class DbService(
    private val instantEntityRepository: InstantEntityRepository,
    private val instantWithSixEntityRepository: InstantWithSixEntityRepository,
    private val instantWithZoneEntityRepository: InstantWithZoneEntityRepository,
    private val localDateTimeEntityRepository: LocalDateTimeEntityRepository,
    private val offsetDateTimeEntityRepository: OffsetDateTimeEntityRepository,
    private val offsetDateTimeWithZoneEntityRepository: OffsetDateTimeWithZoneEntityRepository,
    private val zonedDateTimeEntityRepository: ZonedDateTimeEntityRepository,
    private val zonedDateTimeWithZoneEntityRepository: ZonedDateTimeWithZoneEntityRepository

) {
    companion object {
        val logger = logger()
    }

    fun deleteThemAll() {
        localDateTimeEntityRepository.deleteAllInBatch()
        instantEntityRepository.deleteAllInBatch()
        instantWithSixEntityRepository.deleteAllInBatch()
        offsetDateTimeEntityRepository.deleteAllInBatch()
        zonedDateTimeEntityRepository.deleteAllInBatch()
        instantWithZoneEntityRepository.deleteAllInBatch()
        offsetDateTimeWithZoneEntityRepository.deleteAllInBatch()
        zonedDateTimeWithZoneEntityRepository.deleteAllInBatch()
    }

    fun saveOneOfEach() {
        val i = Instant.now()
        val l = LocalDateTime.now()
        val o = OffsetDateTime.now()
        val z = ZonedDateTime.now()
        logger.info("Instant: $i, LocalDateTime: $l, OffsetDateTime: $o, ZonedDateTime: $z")

        localDateTimeEntityRepository.save(LocalDateTimeEntity(l))
        instantEntityRepository.save(InstantEntity(i))
        instantWithSixEntityRepository.save(InstantWithSixEntity(i))
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
        map["InstantWithSiz"] = instantWithSixEntityRepository.findAll()
        map["OffsetDateTime"] = offsetDateTimeEntityRepository.findAll()
        map["ZonedDateTime"] = zonedDateTimeEntityRepository.findAll()
        map["InstantWithZone"] = instantWithZoneEntityRepository.findAll()
        map["OffsetDateTimeWithZone"] = offsetDateTimeWithZoneEntityRepository.findAll()
        map["ZonedDateTimeWithZone"] = zonedDateTimeWithZoneEntityRepository.findAll()
        return map
    }


}