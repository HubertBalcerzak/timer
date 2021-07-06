package me.hubertus248.timer.security

import com.auth0.jwk.GuavaCachedJwkProvider
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.auth.*
import io.ktor.request.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL
import java.security.interfaces.RSAPublicKey


private val logger: Logger = LoggerFactory.getLogger("me.hubertus248.timer.security.keycloak")

class KeycloakAuthenticationProvider internal constructor(private val config: Configuration) :
    AuthenticationProvider(config) {

    private val certsUrl = "${config.keycloakUrl}/auth/realms/${config.realm}/protocol/openid-connect/certs"

    private val jwkProvider: JwkProvider = GuavaCachedJwkProvider(JwkProviderBuilder(URL(certsUrl)).build())

    private val issuer = "${config.keycloakUrl}/auth/realms/${config.realm}"

    internal fun intercept() {
        pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
            val jwt = context.call.request.authorization()?.removePrefix("Bearer ")

            val cause = if (jwt == null) {
                AuthenticationFailedCause.NoCredentials
            } else {
                try {
                    val decoded = JWT.decode(jwt)
                    val jwk = jwkProvider[decoded.keyId]
                    val algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
                    val verifier = JWT.require(algorithm)
                        .ignoreIssuedAt()//todo make configurable
                        .withIssuer(issuer)
                        .build()

                    verifier.verify(jwt)
                    context.principal(createPrincipal(decoded))
                    config.onAuthSuccess(context)
                    null
                } catch (e: JWTVerificationException) {
                    logger.debug("Failed to process keycloak authentication", e)
                    AuthenticationFailedCause.InvalidCredentials
                } catch (e: Exception) {
                    logger.debug("Failed to process keycloak authentication", e)
                    AuthenticationFailedCause.Error("Failed to verify access token due to $e")
                }

            }
            if (cause != null) {
                context.error("keycloak", cause)
                context.challenge("keycloakChallenge", cause) { challenge ->
                    config.onAuthFailure(context, cause)
                    challenge.complete()
                }
            }


        }
    }


    private fun createPrincipal(decodedJWT: DecodedJWT): KeycloakPrincipal {
        val realmRoles: List<String> =
            decodedJWT.getClaim("realm_access").asMap()?.get("roles") as List<String>? ?: emptyList()
        val resourceRoles = decodedJWT.getClaim("resource_access").asMap()?.entries
            ?.filter { entry -> config.getRolesFromResources.contains(entry.key) }
            ?.flatMap { entry -> (entry.value as Map<String, Any>)["roles"] as List<String> } ?: emptyList()

        return KeycloakPrincipal(
            decodedJWT.getClaim("sub").asString(),
            decodedJWT.getClaim("preferred_username").asString(),
            (realmRoles + resourceRoles).map { Role(it) },
            decodedJWT.getClaim("email_verified").asBoolean(),
            decodedJWT.getClaim("aud")?.asList(String::class.java) ?: emptyList(),
            decodedJWT
        )
    }

    class Configuration(name: String?) : AuthenticationProvider.Configuration(name) {

        lateinit var keycloakUrl: String

        lateinit var realm: String

        var getRolesFromResources: List<String> = listOf()

        var onAuthSuccess: (AuthenticationContext) -> Unit = {}

        lateinit var onAuthFailure: suspend (AuthenticationContext, AuthenticationFailedCause) -> Unit

        fun build() = KeycloakAuthenticationProvider(this)
    }
}

fun Authentication.Configuration.keycloak(
    name: String?,
    configure: KeycloakAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = KeycloakAuthenticationProvider.Configuration(name).apply(configure).build()
    provider.intercept()
    register(provider)
}
