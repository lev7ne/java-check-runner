plugins {
    application
    war
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "ru.clevertec.check.CheckRunner"
}

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("org.postgresql:postgresql:42.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.3")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
    // для теста DAO
    testImplementation("com.h2database:h2:2.2.224")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks {
    register<War>("warTask") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName.set("clevertec-check.war")

        manifest {
            attributes["Main-Class"] = "ru.clevertec.check.CheckRunner"
        }

        from(sourceSets.main.get().output)

        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }
}
