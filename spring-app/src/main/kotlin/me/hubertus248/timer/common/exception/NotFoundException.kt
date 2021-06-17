package me.hubertus248.timer.common.exception

import io.ktor.http.*

class NotFoundException : ApplicationException(HttpStatusCode.NotFound, "Not found")
