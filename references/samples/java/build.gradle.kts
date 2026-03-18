plugins {
    id("java")
    id("io.freefair.lombok") version "8.0.1"
}

group = "com.codevasp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20230227")
    implementation("com.goterl:lazysodium-java:5.1.4")
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.20.0"))
    implementation("org.apache.logging.log4j:log4j-api")
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl")
}

tasks.test {
    useJUnitPlatform()
}
