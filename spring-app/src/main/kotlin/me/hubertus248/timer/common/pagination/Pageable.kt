package me.hubertus248.timer.common.pagination

import io.konform.validation.Validation
import io.konform.validation.jsonschema.maximum
import io.konform.validation.jsonschema.minimum
import me.hubertus248.timer.validation.validateAndThrowOnFailure

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
}
