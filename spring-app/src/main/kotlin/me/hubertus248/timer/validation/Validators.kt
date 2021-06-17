package me.hubertus248.timer.validation

import io.konform.validation.Constraint
import io.konform.validation.ValidationBuilder


fun ValidationBuilder<String>.isNotBlank(): Constraint<String> {
    return addConstraint(
        "Must not be blank",
    ) { it.isNotBlank() }
}
