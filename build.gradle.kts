
plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
    id("com.google.cloud.tools.jib") version "3.4.0"
    id("xyz.theevilroot.gradle-env-plugin") version "1.1"
}

group = "lol.malinovskaya"
version = "1.0.0"

gradleEnv {
    enableSystemEnvironment()
    propertiesFile("local.properties")
}

jib {
    containerizingMode = "packaged"
    container {
        args = listOf()
        workingDirectory = "/app"
    }
    from {
        image = "amazoncorretto:17"
    }
    to {
        image = "ghcr.io/theevilroot/shop-server"
        tags = mutableSetOf("$version")
        auth {
            username = gradleEnv.queryKey("DOCKER_USER")
            password = gradleEnv.queryKey("DOCKER_PASSWORD")
        }
    }
}


application {
    mainClass = "lol.malinovskaya.ApplicationKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("org.jetbrains.exposed:exposed-core:0.49.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.49.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.49.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.49.0")
    implementation("com.ucasoft.ktor:ktor-simple-cache:0.53.4")
    implementation("com.ucasoft.ktor:ktor-simple-redis-cache:0.53.4")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-call-id")
    implementation("io.ktor:ktor-server-swagger")
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-host-common")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-request-validation")
    implementation("io.ktor:ktor-server-sessions")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.kodein.di:kodein-di:7.25.0")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.1.10")
}
