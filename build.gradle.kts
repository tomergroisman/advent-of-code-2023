plugins {
    kotlin("jvm") version "1.9.21"
    application
}

group = "com.squid-productions"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("com.github.bmaslakov:kotlin-algorithm-club:master")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}
