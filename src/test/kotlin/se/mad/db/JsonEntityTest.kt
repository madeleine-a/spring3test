package se.mad.db

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.annotation.Commit
import java.time.Instant

@DataJpaTest(properties = ["spring.flyway.enabled = false"])
@Import(CustomJsonAsStringJdbcType::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class JsonEntityTest {
    @Autowired
    private lateinit var jsonEntityRepository: JsonEntityRepository

    @Autowired
    private lateinit var jsonHolderRepository: JsonHolderRepository

    @Autowired
    private lateinit var em: TestEntityManager

    val objectMapper = jacksonObjectMapper()
    @Test
    @Commit
    fun saveJson() {
        jsonEntityRepository.deleteAllInBatch()
        em.flush()
        val list = mutableListOf<Trapp>()
        (0 until 1000).forEach{i -> list.add(Trapp(i, "En konstig farg"))}
        val tl = TrappLista(list)
        val jsonEntity = JsonEntity(Trapp(12, "Mörk grå"),tl , Instant.now(),"Bara lite strunttext")

        jsonEntityRepository.save(jsonEntity)
        em.flush()
        em.clear()

        val findAll = jsonEntityRepository.findAll()
        println(findAll)

    }

    @Test
    @Commit
    fun saveJsonEmbedded(){
        val holder = JsonHolder(EmbeddableAggregate(1, "Hejhopp"))
        jsonHolderRepository.saveAndFlush(holder)


        em.clear()
        val findAll = jsonHolderRepository.findAll()
        println(findAll)


    }

    @Test
    fun saveEmptyShouldFail(){
        // Save empty aggregate, should fail, due to constraints on json data
        assertThrows<DataIntegrityViolationException> {  jsonHolderRepository.saveAndFlush(JsonHolder(EmbeddableAggregate(null, null))) }

        //Save without aggregate should do the same
        assertThrows<DataIntegrityViolationException> {  jsonHolderRepository.saveAndFlush(JsonHolder(null)) }
    }
}