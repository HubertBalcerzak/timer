package me.hubertus248.timer.common.pagination

import io.konform.validation.Validation
import io.konform.validation.jsonschema.maximum
import io.konform.validation.jsonschema.minimum
import io.ktor.application.*
import kotlinx.serialization.Serializable
import me.hubertus248.timer.validation.validateAndThrowOnFailure

@Serializable
data class Pageable(val page: Int, val pageSize: Int) {
    init {
        Validation<Pageable> {
            Pageable::page {
                minimum(1)
            }
            Pageable::pageSize{
                minimum(1)
                maximum(100)
            }
        }.validateAndThrowOnFailure(this)
    }

    companion object {
        fun receive(call: ApplicationCall): Pageable {
            return Pageable(
                call.parameters["page"]?.toIntOrNull() ?: 1,
                call.parameters["pageSize"]?.toIntOrNull() ?: 10
            )
        }
    }
}
