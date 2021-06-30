import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
    kotlin("plugin.serialization") version "1.5.10"
    kotlin("kapt") version "1.5.10"
}

group = "me.huber"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

val ktorVersion = "1.5.4"
val exposedVersion = "0.31.1"
val koinVersion = "3.1.0"

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.viartemev:ktor-flyway-feature:1.2.2")
    implementation("io.konform:konform:0.3.0")
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.2")
    implementation("com.github.HubertBalcerzak.konvert:konvert-api:0.1")
    kapt("com.github.HubertBalcerzak.konvert:konvert-processor:0.1")
    runtimeOnly("org.postgresql:postgresql:42.2.20")
    testImplementation(kotlin("test-junit"))
    testImplementation("io.insert-koin:koin-test:$koinVersion")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}
