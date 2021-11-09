plugins {
    java
}

group = "org.mail"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.flywaydb.enterprise:flyway-core:8.0.1")
    implementation("org.postgresql:postgresql:42.3.1")
    implementation("org.jetbrains:annotations:20.1.0")
    implementation("org.projectlombok:lombok:1.18.22")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}