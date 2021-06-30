package me.hubertus248.timer.task.dto

import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import kotlinx.serialization.Serializable
import me.hubertus248.timer.validation.isNotBlank
import me.hubertus248.timer.validation.validateAndThrowOnFailure

data class CreateTaskDTO(val name: String) {
    init {
        Validation<CreateTaskDTO> {
            CreateTaskDTO::name {
                isNotBlank()
                maxLength(128)
            }
        }.validateAndThrowOnFailure(this)
    }
}
