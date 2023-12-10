import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

val sessionId = run {
    val file = file("session_id.txt")
    if (file.exists()) file.readLines()[0]
    else ""
}

subprojects {
    apply {
        plugin("java-library")
        plugin("org.jetbrains.kotlin.jvm")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(kotlin("test"))
    }

    val jvmTarget: String by project

    the<JavaPluginExtension>().apply {
        sourceCompatibility = JavaVersion.valueOf("VERSION_$jvmTarget")
    }

    tasks.withType<JavaExec> {
        jvmArgs("-Xmx16g")
        systemProperty("aoc.session.id", sessionId)
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = jvmTarget
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        systemProperty("aoc.session.id", sessionId)
    }

}
