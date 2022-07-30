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
    // https://github.com/google/protobuf-gradle-plugin/releases
    id("com.google.protobuf") version "0.8.19"
    application
}

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
    api("io.grpc:grpc-protobuf:${project.ext["grpc-protobuf.version"]}")
    api("io.grpc:grpc-stub:${project.ext["grpc-protobuf.version"]}")
    api("io.grpc:grpc-netty-shaded:${project.ext["grpc-protobuf.version"]}")
    api("io.grpc:grpc-kotlin-stub:${project.ext["grpc-kotlin-stub.version"]}")

    api("com.google.protobuf:protobuf-gradle-plugin:${project.ext["protobuf.gradle.plugin.version"]}")
    api("com.google.protobuf:protobuf-java-util:${project.ext["protobuf-java-util.version"]}")
    api("com.google.protobuf:protobuf-kotlin:${project.ext["protobuf-kotlin.version"]}")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.ext["kotlinx-coroutines-core.version"]}")

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

sourceSets {
    val main by getting { }
    main.java.srcDirs("build/generated/source/proto/main/grpc")
    main.java.srcDirs("build/generated/source/proto/main/grpckt")
    main.java.srcDirs("build/generated/source/proto/main/java")
    main.java.srcDirs("build/generated/source/proto/main/kotlin")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.3"
    }
    //generatedFilesBaseDir = "$projectDir/src"
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
