package se.atg.service.rcs.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

/**
 * Extension function to obtain a logger. If used from a companion object, the declaring class will be used for logging.
 */
fun Any.logger(): Logger =
    this::class.run {
        val clazz = if (isCompanion) java.declaringClass else java
        getLogger(clazz)
    }
