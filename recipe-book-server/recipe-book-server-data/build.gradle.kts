import org.jooq.meta.jaxb.MatcherTransformType

plugins {
    id("java-library")
    alias(libs.plugins.jooq)
}

dependencies {
    implementation(libs.jooq)
    jooqCodegen(libs.jooq.meta)
}

java {
    sourceSets["main"].java {
        srcDir("build/generated-sources/jooq")
    }
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

jooq {
    configuration {
        generator {
            database {
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                properties {
                    property {
                        key = "scripts"
                        value = "src/main/resources/tables"
                    }
                    property {
                        key = "sort"
                        value = "semantic"
                    }
                }
            }
            strategy {
                matchers {
                    tables {
                        table {
                            tableClass {
                                transform = MatcherTransformType.PASCAL
                                expression = "$0_Table"
                            }
                            recordClass {
                                transform = MatcherTransformType.PASCAL
                                expression = "$0"
                            }
                        }
                    }
                }
            }
            target {
                packageName = "net.internalerror"
            }
        }
    }
}



tasks {
    build {
        dependsOn("createSchemaFile")
    }
    compileJava {
        dependsOn(jooqCodegen)
    }
}

tasks.register("createSchemaFile") {
    val schemaFile = file("src/main/resources/0_schema.sql")
    val tablesDir = file("src/main/resources/tables")
    val outputFile = file("${layout.buildDirectory.asFile.get()}/generatedSchema.sql")

    doLast {
        outputFile.parentFile.mkdirs()
        outputFile.writeText(schemaFile.readText())
        tablesDir.listFiles()?.sorted()?.forEach { tableFile ->
            if (tableFile.isFile) {
                outputFile.appendText("\n\n")
                outputFile.appendText(tableFile.readText())
            }
        }
    }
}
