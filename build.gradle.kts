plugins {
    kotlin("multiplatform") version "2.0.21"
    id("org.jetbrains.compose") version "1.7.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    kotlin("plugin.compose") version "2.0.21"
}

group = "at.hillstromeeg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.software/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
            testTask {
                useKarma {
                    useFirefox()
                }
            }
        }
        binaries.executable()
    }

    afterEvaluate {
        tasks.named("kotlinStoreYarnLock") {
            enabled = false
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.html.core)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.kotest:kotest-assertions-core:5.8.0")
                implementation("io.kotest:kotest-framework-engine:5.8.0")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation("io.kotest:kotest-framework-engine:5.8.0")
            }
        }
    }
}

