import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    id("jacoco")
    alias(libs.plugins.lombok)
    alias(libs.plugins.sonarcloud)
    alias(libs.plugins.spring.boot)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    runtimeOnly(libs.mysql)

    implementation(libs.jooq)
    implementation(libs.jwt.api)
    implementation(libs.jwt.impl)
    implementation(libs.jwt.jackson)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.security)
    implementation(libs.spring.boot.mail)
    implementation(libs.jetbrains.annotations)
    implementation(libs.spring.boot.validation)
    implementation(project(":recipe-book-server:recipe-book-server-data"))

    testRuntimeOnly(libs.h2)

    testImplementation(libs.greenmail)
    testImplementation(libs.spring.boot.test)
}

tasks {
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)

        testLogging {
            events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
    jacocoTestReport {
        reports {
            xml.required = true
        }
    }
    named("sonar") {
        dependsOn(compileJava)
    }
}

sonar {
    properties {
        property("sonar.projectKey", "net.internalerror:recipe-book-server-rest")
        property("sonar.organization", "philipp-gatzka")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}