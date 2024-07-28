plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.13.3"
}

group = "com.szjlc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
//    localPath.set("D:\\ideaC")
    localPath.set("/Applications/IntelliJ IDEA CE.app/Contents")
//    plugins = ['java']
//    version.set("2023.1.3")
//    type.set("IC") // Target IDE Platform
//
//    plugins.set(listOf(/* Plugin Dependencies */))
}

dependencies {

    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("org.slf4j:slf4j-simple:2.0.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")
    implementation("com.squareup.okhttp3:okhttp-sse:3.14.9")
    implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.knuddels:jtokkit:0.2.0")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("com.auth0:java-jwt:4.2.2")
    implementation("com.google.guava:guava:32.1.2-jre")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.java-websocket:Java-WebSocket:1.5.4")
    implementation("com.squareup.okio:okio:2.10.0")
    implementation("cn.hutool:hutool-all:5.8.16")
    testImplementation("junit:junit:4.13.2")
    implementation("com.alibaba:fastjson:2.0.41")

}

tasks.withType<JavaCompile> {
    options.encoding = "utf-8"
}
tasks.withType<JavaExec> {
    systemProperty("file.encoding", "utf-8")
}

tasks {
    // Set the JVM compatibility versions
//    withType<JavaCompile> {
//        sourceCompatibility = "17"
//        targetCompatibility = "17"
//    }
//    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//        kotlinOptions.jvmTarget = "17"
//    }
    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("242.*")
    }

//    signPlugin {
//        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
//        privateKey.set(System.getenv("PRIVATE_KEY"))
//        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
//    }

//    publishPlugin {
//        token.set(System.getenv("PUBLISH_TOKEN"))
//    }
}
