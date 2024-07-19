rootProject.name = "recipe-book"

System.setProperty("sonar.gradle.skipCompile", "true")

include(":recipe-book-server:recipe-book-server-data")
include(":recipe-book-server:recipe-book-server-rest")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs"){
            version("jwt", "0.11.5")
            version("jooq", "3.19.10")
            version("spring-boot", "3.3.1")

            plugin("jooq", "org.jooq.jooq-codegen-gradle").versionRef("jooq")
            plugin("lombok", "io.freefair.lombok").version("8.6")
            plugin("spring-boot", "org.springframework.boot").versionRef("spring-boot")
            plugin("openapi-generator", "org.openapi.generator").version("7.7.0")
            plugin("openapi-spec", "org.springdoc.openapi-gradle-plugin").version("1.9.0")
            plugin("sonarcloud", "org.sonarqube").version("4.4.1.3373")

            library("h2", "com.h2database", "h2").version("2.2.224")
            library("jooq", "org.jooq", "jooq").versionRef("jooq")
            library("greenmail", "com.icegreen", "greenmail").version("2.0.1")
            library("jooq-meta", "org.jooq", "jooq-meta-extensions").versionRef("jooq")
            library("spring-boot-web", "org.springframework.boot", "spring-boot-starter-web").versionRef("spring-boot")
            library("spring-boot-mail", "org.springframework.boot", "spring-boot-starter-mail").versionRef("spring-boot")
            library("spring-boot-validation", "org.springframework.boot", "spring-boot-starter-validation").versionRef("spring-boot")
            library("spring-boot-security", "org.springframework.boot", "spring-boot-starter-security").versionRef("spring-boot")
            library("spring-boot-test", "org.springframework.boot", "spring-boot-starter-test").versionRef("spring-boot")
            library("spring-boot-devtools", "org.springframework.boot", "spring-boot-devtools").versionRef("spring-boot")
            library("mysql","com.mysql","mysql-connector-j").version("9.0.0")
            library("jwt-api","io.jsonwebtoken","jjwt-api").versionRef("jwt")
            library("jwt-impl","io.jsonwebtoken","jjwt-impl").versionRef("jwt")
            library("jwt-jackson","io.jsonwebtoken","jjwt-jackson").versionRef("jwt")
            library("jetbrains-annotations","org.jetbrains","annotations").version("24.0.0")
            library("springdoc.openapi", "org.springdoc", "springdoc-openapi-starter-webmvc-ui").version("2.5.0")
        }
    }
}