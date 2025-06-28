plugins {
    java
    alias(libs.plugins.springboot)
}

apply(plugin = "io.spring.dependency-management")

group = "com.xdesign"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

val jacksonVersion = "2.17.0"


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(libs.opencsv)
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

configurations.all {
    exclude(group = "commons-logging", module = "commons-logging")
}

tasks.test {
    useJUnitPlatform()
}
