pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        // (可选) 你可以添加此 URL 以使用我们的公共存储库
        // 当 Sonatype-OSS 发生故障无法发布依赖时，此存储库作为备选进行添加
        // 详情请前往：https://github.com/HighCapable/maven-repository
        // 中国大陆用户请将下方的 "raw.githubusercontent.com" 修改为 "raw.gitmirror.com"
        maven("https://raw.githubusercontent.com/HighCapable/maven-repository/main/repository/releases")
    }
}

rootProject.name = "氢竹笔记"
include(":app")