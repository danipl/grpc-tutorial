import com.google.protobuf.gradle.*;
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.19")
    }
}

plugins {
    java
    idea
    kotlin("jvm") version "1.7.10"
    id("com.google.protobuf") version "0.8.19"
    application
}

group = "org.example"
version = "1.0.0-SNAPSHOT"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        google()
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}

dependencies {
    //api(kotlin("stdlib-jdk8"))

    //api("io.grpc:protoc-gen-grpc-java:1.48.0")
    //api("io.grpc:protoc-gen-grpc-kotlin:1.3.0")

    api("io.grpc:grpc-protobuf:1.47.0")
    api("io.grpc:grpc-kotlin-stub:1.3.0")
    api("io.grpc:grpc-stub:1.47.0")
    implementation("io.grpc:grpc-netty-shaded:1.48.0")

    api("com.google.protobuf:protobuf-gradle-plugin:0.8.19")
    api("com.google.protobuf:protobuf-java-util:3.21.3")
    api("com.google.protobuf:protobuf-kotlin:3.21.3")

    //api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

application {
    mainClass.set("MainKt")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.3"
    }
    generatedFilesBaseDir = "$projectDir/src"
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.48.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.0:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}
