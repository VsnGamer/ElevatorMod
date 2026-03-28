loom {
    accessWidenerPath = project(":common").loom.accessWidenerPath
}

architectury {
    platformSetupLoomIde()
    fabric()
}

configurations {
    named("developmentFabric") { extendsFrom(configurations.getByName("common")) }
}


dependencies {
    api("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
    api("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

    "common"(project(path = ":common")) { isTransitive = false }
    "shadowBundle"(project(path = ":common", configuration = "transformProductionFabric"))

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
