package me.hubertus248.timer.common.exception

import io.ktor.http.*

class SystemException : ApplicationException(HttpStatusCode.InternalServerError, "Internal server error")
