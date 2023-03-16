import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "dev.vozdyx"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
    maven {url = uri("https://m2.dv8tion.net/releases")}
}

val implementation by configurations

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.ini4j:ini4j:0.5.4")
    implementation("com.sedmelluq:lavaplayer:1.3.77")
    implementation("net.dv8tion:JDA:5.0.0-alpha.21") {
        exclude(module = "opus-java")
    }
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
        archiveFileName.set("radiobot.jar")
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}



tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("MainKt")
}