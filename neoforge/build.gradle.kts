plugins {
    id("com.gradleup.shadow")
}

//loom {
//    accessWidenerPath = project(":common").loom.accessWidenerPath
//}

architectury {
    platformSetupLoomIde()
    neoForge()
}

val common by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

val shadowBundle by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

configurations {
    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
    getByName("developmentNeoForge").extendsFrom(common)
}

repositories {
    maven {
        name = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }
}

dependencies {
    neoForge("net.neoforged:neoforge:${property("neoforge_version")}")

    common(project(path = ":common")) { isTransitive = false }
    shadowBundle(project(path = ":common", configuration = "transformProductionNeoForge"))
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand("version" to project.version)
    }
}

tasks.jar {
    archiveClassifier = "raw"
}

tasks.shadowJar {
    dependsOn(tasks.jar)
    //mainSpec.sourcePaths.clear()
    from(zipTree(tasks.jar.get().archiveFile))
    configurations = listOf(shadowBundle)
    archiveClassifier = null
}

//remapJar {
//    atAccessWideners.add "elevatormod.accesswidener"
//    input.set shadowJar.archiveFile
//}
