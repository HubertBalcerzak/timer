package me.hubertus248.timer.common.exception

import io.ktor.http.*

open class ApplicationException(val statusCode: HttpStatusCode, val errorMessage: String) : RuntimeException()
