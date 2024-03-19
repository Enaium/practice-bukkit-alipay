import cn.enaium.jimmer.gradle.extension.Driver
import cn.enaium.jimmer.gradle.extension.Language

plugins {
    kotlin("jvm") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
    id("cn.enaium.jimmer.gradle") version "0.0.6"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "cn.enaium"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven { url = uri("https://repo.codemc.org/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("dev.jorel:commandapi-bukkit-core:9.3.0")
    implementation("com.alipay.sdk:alipay-easysdk:2.2.3")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.zaxxer:HikariCP:5.1.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("plugin.yml") {
        expand(mapOf("version" to project.version))
    }
}

jimmer {
    language = Language.KOTLIN
    generator {
        jdbc {
            url = "jdbc:postgresql://localhost:5432/postgres"
            username = "postgres"
            password = "postgres"
            driver = Driver.POSTGRESQL
        }
        target {
            packageName = "cn.enaium.model.entity"
            srcDir = "src/main/kotlin"
        }
    }
}