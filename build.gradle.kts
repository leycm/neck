plugins {
    java
    `maven-publish`

}

group = "org.leycm.neck"
description = "Neck utils"
version = "1.1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.springframework:spring-context:6.1.9")
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("org.jetbrains:annotations:24.0.1")

    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])

        groupId = group.toString()
        artifactId = "neck"
        version = rootProject.version as String?
    }
}
