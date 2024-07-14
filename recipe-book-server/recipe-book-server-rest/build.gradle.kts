import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    id("jacoco")
    alias(libs.plugins.lombok)
    alias(libs.plugins.openapi.generator)
    alias(libs.plugins.openapi.spec)
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
    implementation(libs.springdoc.openapi)
    implementation(libs.jwt.jackson)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.security)
    implementation(libs.jetbrains.annotations)
    implementation(libs.spring.boot.validation)
    implementation(project(":recipe-book-server:recipe-book-server-data"))

    testRuntimeOnly(libs.h2)

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
    compileJava {
        mustRunAfter(":recipe-book-server:recipe-book-server-data:jar")
    }
    compileTestJava {
        mustRunAfter(generateOpenApiDocs)
    }
    named("openApiGenerate") {
        dependsOn(generateOpenApiDocs)
    }
    openApiGenerate {
        generatorName = "java"
        generateApiTests = false
        generateModelTests = false
        inputSpec = "${layout.buildDirectory.asFile.get()}/openapi.json"
        packageName = "net.internalerror"
        apiPackage = "net.internalerror.api"
        modelPackage = "net.internalerror.model"
        outputDir = project(":recipe-book-server:recipe-book-server-adapter:").layout.projectDirectory.asFile.absolutePath
        invokerPackage = "net.internalerror.invoker"
        version = "${project.version}"
        groupId = "${project.group}"
        id = "recipe-book-server-adapter"
    }
}