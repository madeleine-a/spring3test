package se.mad.db

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes


@Entity(name = "JsonHolder")
data class JsonHolder(
    // Has to be a blob when dealing with oracle and blob is not the best choice majbeee
    /*
    LOB ("AGGREGATE") STORE AS SECUREFILE (
        TABLESPACE "RCS_DATA" ENABLE STORAGE IN ROW CHUNK 8192
    )
     */
    @JdbcTypeCode(SqlTypes.JSON)
    private val aggregate: EmbeddableAggregate? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
}

@Embeddable
data class EmbeddableAggregate(
    val attribute1: Long? = null,
    val attribute2: String? = null
)