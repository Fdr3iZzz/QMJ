plugins {
    kotlin("jvm") version "1.9.21"
}

group = "de.Franz3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation ("com.github.Fdr3iZzz:Octane:0c407936a9")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation ("com.rometools:rome:2.1.0")
    implementation("io.ktor:ktor-server-core-jvm:2.3.7")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.7")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.7")
    implementation("io.ktor:ktor-server-default-headers-jvm:2.3.7")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}