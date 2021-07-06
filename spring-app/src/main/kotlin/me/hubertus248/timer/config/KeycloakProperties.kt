package me.hubertus248.timer.config

class KeycloakProperties(
    val url: String = Config.requireProperty("keycloak.url"),
    val realm: String = Config.requireProperty("keycloak.realm"),
    val secret: String = Config.requireProperty("keycloak.secret")
)