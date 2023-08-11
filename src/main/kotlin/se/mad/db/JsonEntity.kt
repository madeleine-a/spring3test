package se.mad.db

import com.fasterxml.jackson.annotation.JsonCreator
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Version
import org.hibernate.annotations.*
import org.hibernate.type.SqlTypes
import org.hibernate.type.descriptor.jdbc.JsonAsStringJdbcType
import org.springframework.stereotype.Component
import java.sql.SQLType
import java.time.Instant
import java.util.*


@Entity
data class JsonEntity (
    @JdbcType(CustomJsonAsStringJdbcType::class)
    var trapp: Trapp, // VARCHAR2

    @JdbcType(CustomJsonAsStringJdbcType::class)
    var trappLista: TrappLista, // CLOB

    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    var dateValue: Instant,

    var vanligString: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
}


data class Trapp(val antalSteg: Int, val farg: String){
    @JsonCreator constructor() : this(0, "") //... inte viktigt för testet..
}

data class TrappLista(val lista: List<Trapp>? = null){
    @JsonCreator constructor() : this(null) //... inte viktigt för testet..
}


class CustomJsonAsStringJdbcType: JsonAsStringJdbcType(SqlTypes.VARCHAR, null)