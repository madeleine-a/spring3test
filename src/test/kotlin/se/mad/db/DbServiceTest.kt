package se.mad.db

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.Commit
import java.time.*
import java.time.temporal.ChronoUnit


@DataJpaTest(properties = ["spring.flyway.enabled = false"])
@Import(DbService::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class DbServiceTest() {
    @Autowired
    private lateinit var instantEntityRepository: InstantEntityRepository

    @Autowired
    private lateinit var instantWithZoneEntityRepository: InstantWithZoneEntityRepository

    @Autowired
    private lateinit var localDateTimeEntityRepository: LocalDateTimeEntityRepository

    @Autowired
    private lateinit var offsetDateTimeEntityRepository: OffsetDateTimeEntityRepository

    @Autowired
    private lateinit var offsetDateTimeWithZoneEntityRepository: OffsetDateTimeWithZoneEntityRepository

    @Autowired
    private lateinit var zonedDateTimeEntityRepository: ZonedDateTimeEntityRepository

    @Autowired
    private lateinit var zonedDateTimeWithZoneEntityRepository: ZonedDateTimeWithZoneEntityRepository

    @Autowired
    private lateinit var em: TestEntityManager

    @Autowired
    lateinit var dbService: DbService

    // Run this to look at the data in the database
    //@Test
    @Commit
    fun saveOneOfEach() {
        // What timezone do we have
        println("Default TimeZone: ${ZoneId.systemDefault()}")

        dbService.saveOneOfEach()
        em.flush()
        val result = dbService.getThemAll()
        result.forEach { (k, l) ->
            println("Type: $k: $l")
        }
        //dbService.deleteThemAll()
    }

    @Test
    fun saveAndFindLocalDateTime() {
        val dateValue = LocalDateTime.now()
        val saved = dbService.save(dateValue)
        val found = dbService.findLocalDateTimeByDateValue(dateValue)
        assertEquals(saved, found)
    }

    @Test
    @Commit
    fun saveAndFindInstant() {
        instantEntityRepository.deleteAllInBatch()
        val dateValue = Instant.now()
        println(dateValue)
        println(dateValue.truncatedTo(ChronoUnit.MICROS))
 // 2023-08-01 12:32:57.271285000

        val saved = dbService.save(dateValue)
        val found = dbService.findInstantByDateValue(dateValue.truncatedTo(ChronoUnit.MICROS))
        val all = instantEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindInstantWithZone() {
        val dateValue = Instant.now()
        val saved = dbService.saveWithZone(dateValue)
        val found = dbService.findInstantWithZoneByDateValue(dateValue)
        val all = instantWithZoneEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindOffsetDateTime() {
        val dateValue = OffsetDateTime.now()
        val saved = dbService.save(dateValue)
        val found = dbService.findOffsetDateTimeByDateValue(dateValue)
        val all = offsetDateTimeEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindOffsetDateTimeWithZone() {
        val dateValue = OffsetDateTime.now()
        val saved = dbService.saveWithZone(dateValue)
        val found = dbService.findOffsetDateTimeWithZoneByDateValue(dateValue)
        val all = offsetDateTimeWithZoneEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindZonedDateTime() {
        val dateValue = ZonedDateTime.now()
        val saved = dbService.save(dateValue)
        val found = dbService.findZonedDateTimeByDateValue(dateValue)
        val all = zonedDateTimeEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindZonedDateTimeWithZone() {
        val dateValue = ZonedDateTime.now()
        val saved = dbService.saveWithZone(dateValue)
        val found = dbService.findZonedDateTimeWithZoneByDateValue(dateValue)
        val all = zonedDateTimeWithZoneEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun deleteLocalDateTimeEntityByDateValue() {
        val dateValue = LocalDateTime.now()
        val dateValueOld = LocalDateTime.now().minus(10, ChronoUnit.MINUTES)

        val list = mutableListOf<LocalDateTimeEntity>()
        repeat(10) { _ -> list.add(LocalDateTimeEntity(dateValue)) }
        repeat(10) { _ -> list.add(LocalDateTimeEntity(dateValueOld)) }
        localDateTimeEntityRepository.saveAllAndFlush(list)
        val result =
            localDateTimeEntityRepository.deleteAllByDateValueBefore(LocalDateTime.now().minus(5, ChronoUnit.MINUTES))
        assertEquals(10, result)
    }

    @Test
    fun deleteInstantEntityByDateValue() {
        val dateValue = Instant.now()
        val dateValueOld = Instant.now().minus(10, ChronoUnit.MINUTES)

        val list = mutableListOf<InstantEntity>()
        repeat(10) { _ -> list.add(InstantEntity(dateValue)) }
        repeat(10) { _ -> list.add(InstantEntity(dateValueOld)) }
        instantEntityRepository.saveAllAndFlush(list)
        val result = instantEntityRepository.deleteAllByDateValueBefore(Instant.now().minus(5, ChronoUnit.MINUTES))
        assertEquals(10, result)
    }

    @Test
    fun deleteInstantWithZoneEntityByDateValue() {
        val dateValue = Instant.now()
        val dateValueOld = Instant.now().minus(10, ChronoUnit.MINUTES)

        val list = mutableListOf<InstantWithZoneEntity>()
        repeat(10) { _ -> list.add(InstantWithZoneEntity(dateValue)) }
        repeat(10) { _ -> list.add(InstantWithZoneEntity(dateValueOld)) }
        instantWithZoneEntityRepository.saveAllAndFlush(list)
        val result =
            instantWithZoneEntityRepository.deleteAllByDateValueBefore(Instant.now().minus(5, ChronoUnit.MINUTES))
        assertEquals(10, result)
    }

    @Test
    fun deleteOffsetDateTimeEntityByDateValue() {
        val dateValue = OffsetDateTime.now()
        val dateValueOld = OffsetDateTime.now().minus(10, ChronoUnit.MINUTES)

        val list = mutableListOf<OffsetDateTimeEntity>()
        repeat(10) { _ -> list.add(OffsetDateTimeEntity(dateValue)) }
        repeat(10) { _ -> list.add(OffsetDateTimeEntity(dateValueOld)) }

        offsetDateTimeEntityRepository.saveAllAndFlush(list)
        val result =
            offsetDateTimeEntityRepository.deleteAllByDateValueBefore(OffsetDateTime.now().minus(5, ChronoUnit.MINUTES))
        assertEquals(10, result)
    }

    @Test
    fun deleteOffsetDateTimeWithZoneEntityByDateValue() {
        val dateValue = OffsetDateTime.now()
        val dateValueOld = OffsetDateTime.now().minus(10, ChronoUnit.MINUTES)

        val list = mutableListOf<OffsetDateTimeWithZoneEntity>()
        repeat(10) { _ -> list.add(OffsetDateTimeWithZoneEntity(dateValue)) }
        repeat(10) { _ -> list.add(OffsetDateTimeWithZoneEntity(dateValueOld)) }
        offsetDateTimeWithZoneEntityRepository.saveAllAndFlush(list)
        val result = offsetDateTimeWithZoneEntityRepository.deleteAllByDateValueBefore(
            OffsetDateTime.now().minus(5, ChronoUnit.MINUTES)
        )
        assertEquals(10, result)
    }

    @Test
    fun deleteZonedDateTimeEntityByDateValue() {
        val dateValue = ZonedDateTime.now()
        val dateValueOld = ZonedDateTime.now().minus(10, ChronoUnit.MINUTES)

        val list = mutableListOf<ZonedDateTimeEntity>()
        repeat(10) { _ -> list.add(ZonedDateTimeEntity(dateValue)) }
        repeat(10) { _ -> list.add(ZonedDateTimeEntity(dateValueOld)) }

        zonedDateTimeEntityRepository.saveAllAndFlush(list)
        val result =
            zonedDateTimeEntityRepository.deleteAllByDateValueBefore(ZonedDateTime.now().minus(5, ChronoUnit.MINUTES))
        assertEquals(10, result)
    }

    @Test
    fun deleteZonedDateTimeWithZoneEntityByDateValue() {
        val dateValue = ZonedDateTime.now()
        val dateValueOld = ZonedDateTime.now().minus(10, ChronoUnit.MINUTES)

        val list = mutableListOf<ZonedDateTimeWithZoneEntity>()
        repeat(10) { _ -> list.add(ZonedDateTimeWithZoneEntity(dateValue)) }
        repeat(10) { _ -> list.add(ZonedDateTimeWithZoneEntity(dateValueOld)) }
        zonedDateTimeWithZoneEntityRepository.saveAllAndFlush(list)
        val result = zonedDateTimeWithZoneEntityRepository.deleteAllByDateValueBefore(
            ZonedDateTime.now().minus(5, ChronoUnit.MINUTES)
        )
        assertEquals(10, result)
    }
}