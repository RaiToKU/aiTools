## IntelliJ IDEA plugin AI开发助手

#### 环境要求
IntelliJ IDEA: 2022.1 及以上版本
Java: 17 或更高版本  
IntelliJ Platform SDK: 用于插件开发的IDEA SDK环境  
Gradle： 8.1.1或以上

#### 功能说明

##### 1.测试用例助手


##### 2.代码审核助手
（1）代码质量评估  
（2）代码推荐与优化

##### 3.开发助手
（1）代码片段生成（无法生成正片逻辑，但是通常需要生成一些请求的，或者流处理等的代码片段，不需要通过查询baidu等方式，可以直接生成，并生成调试代码，提供给开发者使用和调试）  
（2）正则生成（根据描述，生成对应的正则表达式）  
（3）需求分析（结合上下文，分析需求，生成需要对应的注意点，但是考虑到业务复杂的情况，该功能作为保留）  
（4）SQL（慢sql等分析，给出优化建议；初步设计sql，给出初版给开发者参考；）  
（5）


#### Q & A

##### 1.部分版本不支持插件开发，需要在plugin市场查找plugin安装（注：但似乎已经下载，需要去baidu找）

##### 2.gradle配置 build.gradle.kt
```
plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.13.3"//根据实际情况对应当前版本
}

group = "com.szjlc"
version = "1.0-SNAPSHOT"
//设置私仓 但是我设置不成功，有兴趣自己尝试，目前外网开发
repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
   //使用本地的idea，不然要下载一个idea 很麻烦，而且网络问题很难下载成功
    localPath.set("D:\\ideaC")
}
//编码
tasks.withType<JavaCompile> {
    options.encoding = "utf-8"
}
tasks.withType<JavaExec> {
    systemProperty("file.encoding", "utf-8")
}

tasks {

    //插件支持的版本
    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("242.*")
    }

```

##### 3.gradle的idea配置
windows下：
setting -> Build,Execution,Deployment -> Build Tools -> Gradle  
设置Gradle user home 相当于设置maven的仓库地址  
Distribution 设置成Local installation 再gradle的地址  
Gradle JVM 建议设置成JBR  
  


##### 4.尽量使用jetbrains runtime 的open jdk 不然会有一些奇怪的问题
下载地址：https://github.com/JetBrains/JetBrainsRuntime/releases/tag/jbr-release-21.0.3b509.4
按自己想要的版本，目前我使用17  
  

##### 5.项目问题
建议新建出来带了kotlin都可以删掉，包括src里面的


