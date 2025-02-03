import java.io.FileInputStream
import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

val properties = Properties()
try {
    properties.load(FileInputStream(file(rootProject.projectDir.toPath().resolve("gpr.properties"))))
} catch (e: Exception) {
    logger.error("Properties not found! Checked under: ${rootProject.projectDir.toPath().resolve("gpr.properties")}")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WellMate Kotlin API Client"
include(":api-client")
 