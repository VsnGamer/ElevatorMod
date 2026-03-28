plugins {
    id("com.gradleup.shadow")
}

loom {
    accessWidenerPath = project(":common").loom.accessWidenerPath
}

architectury {
    platformSetupLoomIde()
    fabric()
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
    getByName("developmentFabric").extendsFrom(common)
}


dependencies {
    api("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    api("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

    common(project(path = ":common")) { isTransitive = false }
    shadowBundle(project(path = ":common", configuration = "transformProductionFabric"))

    api("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:${property("forge_config_api_port_version")}")
    include("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:${property("forge_config_api_port_version")}")

//    modLocalRuntime "maven.modrinth:modmenu:12.0.0-beta.1"
//    modLocalRuntime "maven.modrinth:sodium:mc1.21.2-0.6.0-beta.3-fabric"
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }

    // TODO: Dev workaround - copy classTweaker from common so Fabric Loader can find it at runtime.
    //  This may not be needed in a future Architectury release.
    from(project(":common").loom.accessWidenerPath)
}

tasks.jar {
    archiveClassifier = "raw"
}

tasks.shadowJar {
    dependsOn(tasks.jar)
    from(zipTree(tasks.jar.get().archiveFile))
    configurations = listOf(shadowBundle)
    archiveClassifier = null

    val mainOutput = sourceSets.main.get().output
    exclude { element ->
        mainOutput.classesDirs.any { element.file.startsWith(it) } ||
                element.file.startsWith(mainOutput.resourcesDir!!)
    }
}

//remapJar {
//    injectAccessWidener = true
//    input.set shadowJar.archiveFile
//}
