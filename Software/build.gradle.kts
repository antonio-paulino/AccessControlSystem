plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(files("./AccessControlSystem.jar"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    // Specify Java 17 as the default SDK version for Java compilation
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    // Specify Java 17 as the default SDK version for Kotlin compilation
    jvmToolchain(17)
}
