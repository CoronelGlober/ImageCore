import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    `maven-publish`
    id("org.jetbrains.kotlin.native.cocoapods")
}


group = "imagecore"
version = "0.0.3"

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }
    ios {
    }
    sourceSets {
        val commonMain by getting
        val androidMain by getting
        val iosMain by getting
    }

    cocoapods {
        summary = "Image core frame for iOS"
        homepage = "https://www.github.com/CoronelGlober/ImageCore"
    }
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionName("0.0.2")
        versionCode(12)
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
            kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)

val fis = FileInputStream("github.properties")
val githubProperties = Properties()
githubProperties.load(fis)

publishing {
    publications {
        create<MavenPublication>("release") {
            // Applies the component for the release build variant.
//            from(components["release"])
            groupId = "images"
            artifactId = "core"
            version = "0.0.3"
            artifact("$buildDir/outputs/aar/imagecore-release.aar") {
                builtBy(tasks.getByName("assemble"))
            }
        }
    }

    repositories {
        maven {
            name = "github"
            url = uri("https://maven.pkg.github.com/CoronelGlober/ImageCore")
            credentials {
                username = githubProperties.getProperty("user")
                password = githubProperties.getProperty("key")
            }
        }
    }
}