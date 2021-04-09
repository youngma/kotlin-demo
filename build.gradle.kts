import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.spring") version "1.4.31"
    kotlin("plugin.jpa") version "1.4.31"
    kotlin("kapt") version "1.4.10"
}

group = "com.kt.v1"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation( "org.javassist", "javassist", "3.27.0-GA")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.querydsl:querydsl-jpa:4.2.1")
    implementation("org.mapstruct:mapstruct:1.3.0.Final")

    implementation("org.springframework.boot:spring-boot-starter-security")

    // https://mvnrepository.com/artifact/commons-codec/commons-codec
    implementation("commons-codec",  "commons-codec", "1.15")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    kapt("com.querydsl:querydsl-apt:4.2.2:jpa")
    kaptTest("com.querydsl:querydsl-apt:4.2.2:jpa")

    kapt("org.mapstruct:mapstruct-processor:1.3.0.Final")
    kaptTest("org.mapstruct:mapstruct-processor:1.3.0.Final")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("mysql:mysql-connector-java")

    compileOnly(group = "io.jsonwebtoken", name = "jjwt-api", version = "0.11.2")
    runtimeOnly(group = "io.jsonwebtoken", name = "jjwt-impl", version = "0.11.2")
    runtimeOnly(group = "io.jsonwebtoken", name = "jjwt-jackson", version = "0.11.2")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir("$buildDir/generated/sources/kapt/main")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
