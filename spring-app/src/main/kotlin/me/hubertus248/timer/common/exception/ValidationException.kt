package me.hubertus248.timer.common.exception

import io.ktor.http.*

class ValidationException(val errors: String) : ApplicationException(HttpStatusCode.BadRequest, "Bad request")
