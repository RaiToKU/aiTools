plugins {
    id("java")
//    id("org.jetbrains.intellij") version "1.16.0"
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
    // mac 社区版 idea 路径参考：
    localPath.set("/Applications/IntelliJ IDEA CE.app/Contents")
//    windows 直接贴 .exe 所在地址gang
//    localPath.set("D:\\ideaC")


//    plugins = ['java']
//    version.set("2023.1.3")
//    type.set("IC") // Target IDE Platform
//
//    plugins.set(listOf(/* Plugin Dependencies */))
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
