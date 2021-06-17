package me.hubertus248.timer.validation

import io.konform.validation.Invalid
import io.konform.validation.Validation
import me.hubertus248.timer.common.exception.ValidationException

fun <T> Validation<T>.validateAndThrowOnFailure(value: T) {
    val result = validate(value)
    if (result is Invalid<T>) {
        throw ValidationException(result.errors.toString())
    }
}
