rootProject.name = "advent-of-code"

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
    }
}

include(":current")
include(":utils")
