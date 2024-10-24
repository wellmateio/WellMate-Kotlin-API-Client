import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest
import org.apache.tools.ant.taskdefs.condition.Os
import java.io.ByteArrayOutputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)

    alias(libs.plugins.venniktech)
}

val versions = Properties()
file("version.properties").inputStream().use { stream ->
    versions.load(stream)
}

group = "io.wellmate"
version = versions.getProperty("version")

kotlin {
    withSourcesJar(publish = false)

    jvmToolchain(17)
    androidTarget {
        publishLibraryVariants("release")

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlin.test)
            implementation(libs.junit)
        }
    }
}

android {
    namespace = group.toString()
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }
}


// Add boot for ios device - patching missing certificates that cause test failure
val deviceName = project.findProperty("iosDevice") as? String ?: "iPhone 16"

tasks.register<Exec>("bootIOSSimulator") {
    isIgnoreExitValue = true
    val errorBuffer = ByteArrayOutputStream()
    errorOutput = ByteArrayOutputStream()
    commandLine("xcrun", "simctl", "boot", deviceName)

    doLast {
        val result = executionResult.get()
        if (result.exitValue != 148 && result.exitValue != 149) { // ignoring device already booted errors
            println(errorBuffer.toString())
            result.assertNormalExitValue()
        }
    }
}

tasks.withType<KotlinNativeSimulatorTest>().configureEach {
    if (Os.isFamily(Os.FAMILY_MAC)) {
        dependsOn("bootIOSSimulator")
        standalone.set(false)
        device.set(deviceName)
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/wellmateio/WellMate-Kotlin-API-Client")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GPR_TOKEN")
            }
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.wellmate",
        artifactId = "api.client",
        version = versions.getProperty("version")
    )

    pom {
        name.set("KMP Library containing WellMate API Client")
        description.set("This library can be utilized by various KMP targets to connect with WellMate API")
        inceptionYear.set("2024")
        url.set("https://github.com/wellmateio/WellMate-Kotlin-API-Client")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("rgryta")
                name.set("Radosław Gryta")
                email.set("radek.gryta@gmail.com")
                url.set("https://github.com/rgryta/")
            }
        }

        scm {
            url.set("https://github.com/wellmateio/WellMate-Kotlin-API-Client")
            connection.set("scm:git:git://github.com/wellmateio/WellMate-Kotlin-API-Client.git")
            developerConnection.set("scm:git:ssh://git@github.com/wellmateio/WellMate-Kotlin-API-Client.git")
        }
    }
}