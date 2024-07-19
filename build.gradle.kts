plugins {
    alias(libs.plugins.sonarcloud)
}

sonar {
    properties {
        property("sonar.projectKey", "net.internalerror:recipe-book")
        property("sonar.organization", "philipp-gatzka")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

subprojects {

    group = "net.internalerror"
    version = "1.0.0-SNAPSHOT"


    repositories {
        mavenCentral()
    }
}

tasks.named("sonar") {
    dependsOn("compileJava")
}