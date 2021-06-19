package me.hubertus248.timer.common.exception

import io.ktor.http.*

class UnauthorizedException : ApplicationException(HttpStatusCode.Unauthorized, "Unauthorized")
