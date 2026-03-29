plugins {
    id("dev.architectury.loom-no-remap") version "1.14-SNAPSHOT" apply false
    id("architectury-plugin") version "3.5-SNAPSHOT"
    id("com.gradleup.shadow") version "9.4.1" apply false
}

architectury {
    minecraft = property("minecraft_version") as String
}

allprojects {
    group = property("maven_group") as String
    version = property("mod_version") as String
}

subprojects {
    apply(plugin = "dev.architectury.loom-no-remap")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    configure<BasePluginExtension> {
        archivesName = "${property("archives_name")}-${project.name}"
    }

    repositories {
        // You should only use this when depending on other mods because
        // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.

        exclusiveContent {
            forRepository {
                maven {
                    name = "Modrinth"
                    url = uri("https://api.modrinth.com/maven")
                }
            }
            filter {
                includeGroup("maven.modrinth")
            }
        }

        maven {
            name = "Fuzs Mod Resources"
            url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
        }
    }

    dependencies {
        "minecraft"("net.minecraft:minecraft:${property("minecraft_version")}")
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release = 25
    }

    if (project.name != "common") {
        apply(plugin = "com.gradleup.shadow")

        val common by configurations.creating { isCanBeResolved = true; isCanBeConsumed = false }
        val shadowBundle by configurations.creating { isCanBeResolved = true; isCanBeConsumed = false }

        configurations {
            getByName("compileClasspath").extendsFrom(common)
            getByName("runtimeClasspath").extendsFrom(common)
        }

        tasks.named<Jar>("jar") {
            archiveClassifier = "raw"
        }

        tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
            from(zipTree(tasks.named<Jar>("jar").get().archiveFile))
            configurations = listOf(shadowBundle)
            archiveClassifier = ""

            val mainOutput = project.the<SourceSetContainer>()["main"].output
            exclude { element ->
                mainOutput.classesDirs.any { element.file.startsWith(it) } ||
                        element.file.startsWith(mainOutput.resourcesDir!!)
            }
        }
    }

    // Configure Maven publishing.
//    publishing {
//        publications {
//            mavenJava(MavenPublication) {
//                artifactId = base.archivesName.get()
//                from components . java
//            }
//        }
//
//        // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
//        repositories {
//            // Add repositories to publish to here.
//            // Notice: This block does NOT have the same function as the block in the top level.
//            // The repositories here will be used for publishing your artifact, not for
//            // retrieving dependencies.
//        }
//    }
}
