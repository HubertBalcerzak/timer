package me.hubertus248.timer.common.exception

import io.ktor.http.*

class BadRequestException : ApplicationException(HttpStatusCode.BadRequest, "Bad request")
