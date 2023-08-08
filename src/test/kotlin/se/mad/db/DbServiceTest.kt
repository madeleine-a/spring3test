package se.mad.db

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.Commit
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


@DataJpaTest(properties = ["spring.flyway.enabled = false"])
@Import(DbService::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class DbServiceTest {
    @Autowired
    private lateinit var instantEntityRepository: InstantEntityRepository

    @Autowired
    private lateinit var instantWithSixEntityRepository: InstantWithSixEntityRepository

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
        dbService.deleteThemAll()
    }

    @Test
    fun saveAndFindLocalDateTime() {
        val dateValue = LocalDateTime.now()
        val saved = localDateTimeEntityRepository.save(LocalDateTimeEntity(dateValue))
        val found = localDateTimeEntityRepository.findFirstByDateValue(dateValue)
        assertEquals(saved, found)
    }

    /**
     * Not working because oracle holds 9 numbers (nano) but the micro is rounded before it is saved to database.     *
     * This behavior isn't new for spring 3.
     * So this time in service 2023-08-02T04:02:28.957488Z will become 2023-08-02 04:02:28.957488000
     * And this time 2023-08-02T04:10:31.781810400Z will become 2023-08-02 04:10:31.781810000
     * And this time 2023-08-02T04:12:00.987974800Z will become 2023-08-02 04:12:00.987975000
     */
    @Test
    fun saveAndFindInstant() {
        instantEntityRepository.deleteAllInBatch()
        val dateValue = Instant.now()
        val saved = instantEntityRepository.save(InstantEntity(dateValue))
        val found = instantEntityRepository.findFirstByDateValue(dateValue)
        val all = instantEntityRepository.findAll()
        println(TimeZone.getDefault())
        println(dateValue)
        println(saved)
        println(all)
        println(found)
        assertEquals(saved,all.first{it.dateValue == dateValue})
        assertEquals(saved, found)
    }

    /**
     * This will work.
     * dateValue will be something like: 2023-08-02T05:05:34.758656Z and saved value will be 2023-08-02 07:05:34.758656000
     * Comparing these to will give a result.
     */
    @Test
    fun saveAndFindTruncatedInstant() {
        val dateValue = Instant.now().truncatedTo(ChronoUnit.MICROS)
        println(dateValue)
        val saved = instantEntityRepository.save(InstantEntity(dateValue))
        em.clear()
        val found = instantEntityRepository.findFirstByDateValue(dateValue)
        val all = instantEntityRepository.findAll()
        assertEquals(saved,all.first{it.dateValue == dateValue})
        assertEquals(saved, found)
    }

    /**
     * Doesn't matter if you set the datatype to timestamp(6) on the database, same behavior.
     * Not working because oracle holds 9 numbers (nano) but the micro is rounded before it is saved to database.
     * This behavior isn't new for spring 3.
     * So this time in service 2023-08-02T04:02:28.957488Z will become 2023-08-02 04:02:28.957488000
     * And this time 2023-08-02T04:10:31.781810400Z will become 2023-08-02 04:10:31.781810000
     * And this time 2023-08-02T04:12:00.987974800Z will become 2023-08-02 04:12:00.987975000
     */
    @Test
    fun saveAndFindInstantWithSix() {
        val dateValue = Instant.now()
        val saved = instantWithSixEntityRepository.save(InstantWithSixEntity(dateValue))
        em.clear()
        val found = instantWithSixEntityRepository.findFirstByDateValue(dateValue)
        val all = instantWithSixEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }



    @Test
    fun saveAndFindInstantWithZone() {
        val dateValue = Instant.now()
        val saved = instantWithZoneEntityRepository.save(InstantWithZoneEntity(dateValue))
        em.clear()
        val found = instantWithZoneEntityRepository.findFirstByDateValue(dateValue)
        val all = instantWithZoneEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindOffsetDateTime() {
        val dateValue = OffsetDateTime.now()
        val saved = offsetDateTimeEntityRepository.save(OffsetDateTimeEntity(dateValue))
        em.clear()
        val found = offsetDateTimeEntityRepository.findFirstByDateValue(dateValue)

        val all = offsetDateTimeEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindOffsetDateTimeWithZone() {
        val dateValue = OffsetDateTime.now()
        val saved = offsetDateTimeWithZoneEntityRepository.save(OffsetDateTimeWithZoneEntity(dateValue))
        em.clear()
        val found = offsetDateTimeWithZoneEntityRepository.findFirstByDateValue(dateValue)

        val all = offsetDateTimeWithZoneEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindZonedDateTime() {
        val dateValue = ZonedDateTime.now()
        val saved = zonedDateTimeEntityRepository.save(ZonedDateTimeEntity(dateValue))
        em.clear()
        val found = zonedDateTimeEntityRepository.findFirstByDateValue(dateValue)

        val all = zonedDateTimeEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindZonedDateTimeWithZone() {
        val dateValue = ZonedDateTime.now()
        val saved = zonedDateTimeWithZoneEntityRepository.save(ZonedDateTimeWithZoneEntity(dateValue))
        em.clear()
        val found = zonedDateTimeWithZoneEntityRepository.findFirstByDateValue(dateValue)

        val all = zonedDateTimeWithZoneEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindLocalDateTimeDbRounded() {
        val dateValue = LocalDateTime.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS)
        val saved = localDateTimeEntityRepository.save(LocalDateTimeEntity(dateValue))
        em.clear()
        val found = localDateTimeEntityRepository.findFirstByDateValue(dateValue)
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindInstantDbRounded() {
        instantEntityRepository.deleteAllInBatch()
        val dateValue = Instant.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS)
        val saved = instantEntityRepository.save(InstantEntity(dateValue))
        em.clear()
        val found = instantEntityRepository.findFirstByDateValue(dateValue)
        val all = instantEntityRepository.findAll()
        assertEquals(saved,all.first{it.dateValue == dateValue})
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindInstantWithSixDbRounded() {
        val dateValue = Instant.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS)
        val saved = instantWithSixEntityRepository.save(InstantWithSixEntity(dateValue))
        em.clear()
        val found = instantWithSixEntityRepository.findFirstByDateValue(dateValue)
        val all = instantWithSixEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }



    @Test
    fun saveAndFindInstantWithZoneDbRounded() {
        val dateValue = Instant.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS)
        val saved = instantWithZoneEntityRepository.save(InstantWithZoneEntity(dateValue))
        em.clear()
        val found = instantWithZoneEntityRepository.findFirstByDateValue(dateValue)
        val all = instantWithZoneEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindOffsetDateTimeDbRounded() {
        val dateValue = OffsetDateTime.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS)
        val saved = offsetDateTimeEntityRepository.save(OffsetDateTimeEntity(dateValue))
        em.clear()
        val found = offsetDateTimeEntityRepository.findFirstByDateValue(dateValue)

        val all = offsetDateTimeEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindOffsetDateTimeWithZoneDbRounded() {
        val dateValue = OffsetDateTime.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS)
        val saved = offsetDateTimeWithZoneEntityRepository.save(OffsetDateTimeWithZoneEntity(dateValue))
        em.clear()
        val found = offsetDateTimeWithZoneEntityRepository.findFirstByDateValue(dateValue)

        val all = offsetDateTimeWithZoneEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindZonedDateTimeDbRounded() {
        val dateValue = ZonedDateTime.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS)
        val saved = zonedDateTimeEntityRepository.save(ZonedDateTimeEntity(dateValue))
        em.clear()
        val found = zonedDateTimeEntityRepository.findFirstByDateValue(dateValue)

        val all = zonedDateTimeEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun saveAndFindZonedDateTimeWithZoneDbRounded() {
        val dateValue = ZonedDateTime.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS)
        val saved = zonedDateTimeWithZoneEntityRepository.save(ZonedDateTimeWithZoneEntity(dateValue))
        em.clear()
        val found = zonedDateTimeWithZoneEntityRepository.findFirstByDateValue(dateValue)

        val all = zonedDateTimeWithZoneEntityRepository.findAll()
        assertEquals(saved,all.first())
        assertEquals(saved, found)
    }

    @Test
    fun deleteLocalDateTimeEntityByDateValueDbRounded() {
        val dateValue = LocalDateTime.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS)
        val dateValueOld = LocalDateTime.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS).minus(10, ChronoUnit.MINUTES)

        val list = mutableListOf<LocalDateTimeEntity>()
        repeat(10) { _ -> list.add(LocalDateTimeEntity(dateValue)) }
        repeat(10) { _ -> list.add(LocalDateTimeEntity(dateValueOld)) }
        localDateTimeEntityRepository.saveAllAndFlush(list)
        em.clear()
        val result =
            localDateTimeEntityRepository.deleteAllByDateValueBefore(LocalDateTime.now().plusNanos(500).truncatedTo(ChronoUnit.MICROS).minus(5, ChronoUnit.MINUTES))
        assertEquals(10, result)
    }

    @Test
    fun deleteLocalDateTimeEntityByDateValue() {
        val dateValue = LocalDateTime.now()
        val dateValueOld = LocalDateTime.now().minus(10, ChronoUnit.MINUTES)

        val list = mutableListOf<LocalDateTimeEntity>()
        repeat(10) { _ -> list.add(LocalDateTimeEntity(dateValue)) }
        repeat(10) { _ -> list.add(LocalDateTimeEntity(dateValueOld)) }
        localDateTimeEntityRepository.saveAllAndFlush(list)
        em.clear()
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
        em.clear()
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
        em.clear()
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
        em.clear()
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
        em.clear()
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
        em.clear()
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
        em.clear()
        val result = zonedDateTimeWithZoneEntityRepository.deleteAllByDateValueBefore(
            ZonedDateTime.now().minus(5, ChronoUnit.MINUTES)
        )
        assertEquals(10, result)
    }


    @Test
    fun findLocalDateTimeByValueBetween() {
        val dateValue = LocalDateTime.now()
        val list = mutableListOf<LocalDateTimeEntity>()
        (0 until 10).forEach { list.add(LocalDateTimeEntity(dateValue.minus((it*10).toLong(), ChronoUnit.MINUTES))) }
        // 6 entities within the last hour and 4 more that are one hour or more old.
        localDateTimeEntityRepository.saveAll(list)
        em.clear()
        val all = localDateTimeEntityRepository.findAll()
        val from = LocalDateTime.now().minus(2, ChronoUnit.HOURS)
        val to = LocalDateTime.now().minus(1, ChronoUnit.HOURS)
        val found = localDateTimeEntityRepository.findAllByDateValueBetween(from, to)
        assertEquals(4, found.size)
    }
    
    @Test
    fun findInstantByValueBetween() {
        val dateValue = Instant.now()
        val list = mutableListOf<InstantEntity>()
        (0 until 10).forEach { list.add(InstantEntity(dateValue.minus((it*10).toLong(), ChronoUnit.MINUTES))) }
        // 6 entities within the last hour and 4 more that are one hour or more old.
        instantEntityRepository.saveAll(list)
        em.clear()
        val all = instantEntityRepository.findAll()
        val from = Instant.now().minus(2, ChronoUnit.HOURS)
        val to = Instant.now().minus(1, ChronoUnit.HOURS)
        val found = instantEntityRepository.findAllByDateValueBetween(from, to)
        assertEquals(4, found.size)
    }

    @Test
    fun findInstantByValueBetweenV2() {
        val timeZone = TimeZone.getDefault()
        println(timeZone)

        val dateValue = Instant.now().minus(70, ChronoUnit.MINUTES)
        instantEntityRepository.saveAndFlush(InstantEntity(dateValue))
        em.clear()
        val from = Instant.now().minus(2, ChronoUnit.HOURS)
        val to = Instant.now().minus(1, ChronoUnit.HOURS)
        val found = instantEntityRepository.findAllByDateValueBetween(from, to)

        val fromOff = Instant.now().minus(4, ChronoUnit.HOURS)
        val toOff = Instant.now().minus(2, ChronoUnit.HOURS)
        val foundOffTime = instantEntityRepository.findAllByDateValueBetween(fromOff, toOff)
        assertEquals(0, foundOffTime.size)
        assertEquals(1, found.size)
    }

    @Test
    fun findInstantByValueBetweenV3() {
        val dateValue = Instant.now()
        instantEntityRepository.saveAndFlush(InstantEntity(dateValue))
        em.clear()
        val from = Instant.now().minus(1, ChronoUnit.HOURS)
        val to = Instant.now().plus(1, ChronoUnit.HOURS)
        val found = instantEntityRepository.findAllByDateValueBetween(from, to)
        assertEquals(1, found.size)
    }

    @Test
    fun findInstantByValueBetweenSistaSommartiden() {
        val str = "2023-10-29T02:28:00.000000+02:00"
        val dateValue = Instant.parse(str)
        instantEntityRepository.saveAndFlush(InstantEntity(dateValue))
        em.clear()
        val from = Instant.now().minus(1, ChronoUnit.HOURS)
        val to = Instant.now().plus(1, ChronoUnit.HOURS)
        val found = instantEntityRepository.findAllByDateValueBetween(
            dateValue.minus(1, ChronoUnit.HOURS),
            dateValue.plus(1, ChronoUnit.HOURS)
        )
        val all = instantEntityRepository.findAll()
        assertEquals(1, found.size)
    }

    @Test
    fun findInstantByValueBetweenVintertid() {
        val str = "2023-10-29T02:28:00.000000+01:00"
        val dateValue = Instant.parse(str)
        instantEntityRepository.saveAndFlush(InstantEntity(dateValue))
        em.clear()
        val from = Instant.now().minus(1, ChronoUnit.HOURS)
        val to = Instant.now().plus(1, ChronoUnit.HOURS)
        val found = instantEntityRepository.findAllByDateValueBetween(
            dateValue.minus(1, ChronoUnit.HOURS),
            dateValue.plus(1, ChronoUnit.HOURS)
        )
        val all = instantEntityRepository.findAll()
        assertEquals(1, found.size)
    }

    @Test
    fun findInstantByValueBetweenSommartidV2() {
        val str = "2023-10-29T00:28:00.000000Z"
        val str2 = "2023-10-29T01:28:00.000000Z"
        val dateValue = Instant.parse(str)
        val dateValue2 = Instant.parse(str2)
        instantEntityRepository.save(InstantEntity(dateValue))
        instantEntityRepository.saveAndFlush(InstantEntity(dateValue2))
        em.clear()
        val found = instantEntityRepository.findAllByDateValueBetween(
            dateValue.minus(1, ChronoUnit.HOURS),
            dateValue.plus(1, ChronoUnit.HOURS)
        )
        val all = instantEntityRepository.findAll()
        println(all[0].dateValue)
        println(all[1].dateValue)
        assertNotEquals(all[0].dateValue, all[1].dateValue)

    }

    @Test
    fun findInstantByValueBetweenSommartidV3() {
        val str = "2023-10-29T02:28:00.000000+01:00"
        val str2 = "2023-10-29T02:28:00.000000+02:00"
        val dateValue = Instant.parse(str)
        val dateValue2 = Instant.parse(str2)
        instantEntityRepository.save(InstantEntity(dateValue))
        instantEntityRepository.saveAndFlush(InstantEntity(dateValue2))
        em.clear()
        val found = instantEntityRepository.findAllByDateValueBetween(
            dateValue.minus(1, ChronoUnit.HOURS),
            dateValue.plus(1, ChronoUnit.HOURS)
        )
        val all = instantEntityRepository.findAll()
        assertNotEquals(all[0].dateValue, all[1].dateValue)
    }


    @Test
    fun findInstantWithZoneByValueBetween() {
        val dateValue = Instant.now()
        val list = mutableListOf<InstantWithZoneEntity>()
        (0 until 10).forEach { list.add(InstantWithZoneEntity(dateValue.minus((it*10).toLong(), ChronoUnit.MINUTES))) }
        // 6 entities within the last hour and 4 more that are one hour or more old.
        instantWithZoneEntityRepository.saveAll(list)
        em.clear()
        val all = instantWithZoneEntityRepository.findAll()
        val from = Instant.now().minus(2, ChronoUnit.HOURS)
        val to = Instant.now().minus(1, ChronoUnit.HOURS)
        val found = instantWithZoneEntityRepository.findAllByDateValueBetween(from, to)
        assertEquals(4, found.size)
    }

    @Test
    fun findInstantWithSixByValueBetween() {
        val dateValue = Instant.now()
        val list = mutableListOf<InstantWithSixEntity>()
        (0 until 10).forEach { list.add(InstantWithSixEntity(dateValue.minus((it*10).toLong(), ChronoUnit.MINUTES))) }
        // 6 entities within the last hour and 4 more that are one hour or more old.
        instantWithSixEntityRepository.saveAll(list)
        em.clear()
        val all = instantWithSixEntityRepository.findAll()
        val from = Instant.now().minus(2, ChronoUnit.HOURS)
        val to = Instant.now().minus(1, ChronoUnit.HOURS)
        val found = instantWithSixEntityRepository.findAllByDateValueBetween(from, to)
        assertEquals(4, found.size)
    }

    @Test
    fun findOffsetDateTimeByValueBetween() {
        val dateValue = OffsetDateTime.now()
        val list = mutableListOf<OffsetDateTimeEntity>()
        (0 until 10).forEach { list.add(OffsetDateTimeEntity(dateValue.minus((it*10).toLong(), ChronoUnit.MINUTES))) }
        // 6 entities within the last hour and 4 more that are one hour or more old.
        offsetDateTimeEntityRepository.saveAll(list)

        em.clear()
        val all = offsetDateTimeEntityRepository.findAll()
        val from = OffsetDateTime.now().minus(2, ChronoUnit.HOURS)
        val to = OffsetDateTime.now().minus(1, ChronoUnit.HOURS)
        val found = offsetDateTimeEntityRepository.findAllByDateValueBetween(from, to)
        assertEquals(4, found.size)
    }

    @Test
    fun findOffsetDateTimeWithZoneByValueBetween() {
        val dateValue = OffsetDateTime.now()
        val list = mutableListOf<OffsetDateTimeWithZoneEntity>()
        (0 until 10).forEach { list.add(OffsetDateTimeWithZoneEntity(dateValue.minus((it*10).toLong(), ChronoUnit.MINUTES))) }
        // 6 entities within the last hour and 4 more that are one hour or more old.
        offsetDateTimeWithZoneEntityRepository.saveAll(list)
        em.clear()
        val all = offsetDateTimeWithZoneEntityRepository.findAll()
        val from = OffsetDateTime.now().minus(2, ChronoUnit.HOURS)
        val to = OffsetDateTime.now().minus(1, ChronoUnit.HOURS)
        val found = offsetDateTimeWithZoneEntityRepository.findAllByDateValueBetween(from, to)
        assertEquals(4, found.size)
    }


    @Test
    fun findZonedDateTimeByValueBetween() {
        val dateValue = ZonedDateTime.now()
        val list = mutableListOf<ZonedDateTimeEntity>()
        (0 until 10).forEach { list.add(ZonedDateTimeEntity(dateValue.minus((it*10).toLong(), ChronoUnit.MINUTES))) }
        // 6 entities within the last hour and 4 more that are one hour or more old.
        zonedDateTimeEntityRepository.saveAll(list)
        em.clear()
        val all = zonedDateTimeEntityRepository.findAll()
        val from = ZonedDateTime.now().minus(2, ChronoUnit.HOURS)
        val to = ZonedDateTime.now().minus(1, ChronoUnit.HOURS)
        val found = zonedDateTimeEntityRepository.findAllByDateValueBetween(from, to)
        assertEquals(4, found.size)
    }

    @Test
    fun findZonedDateTimeWithZoneByValueBetween() {
        val dateValue = ZonedDateTime.now()
        val list = mutableListOf<ZonedDateTimeWithZoneEntity>()
        (0 until 10).forEach { list.add(ZonedDateTimeWithZoneEntity(dateValue.minus((it*10).toLong(), ChronoUnit.MINUTES))) }
        // 6 entities within the last hour and 4 more that are one hour or more old.
        zonedDateTimeWithZoneEntityRepository.saveAll(list)
        em.clear()
        val all = zonedDateTimeWithZoneEntityRepository.findAll()
        val from = ZonedDateTime.now().minus(2, ChronoUnit.HOURS)
        val to = ZonedDateTime.now().minus(1, ChronoUnit.HOURS)
        val found = zonedDateTimeWithZoneEntityRepository.findAllByDateValueBetween(from, to)
        assertEquals(4, found.size)
    }

}