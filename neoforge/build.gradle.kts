// TODO: Maintaining AT for now
//loom {
//    accessWidenerPath = project(":common").loom.accessWidenerPath
//}

architectury {
    platformSetupLoomIde()
    neoForge()
}

configurations {
    named("developmentNeoForge") { extendsFrom(configurations.getByName("common")) }
}

repositories {
    maven {
        name = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }
}

dependencies {
    neoForge("net.neoforged:neoforge:${property("neoforge_version")}")

    "common"(project(path = ":common")) { isTransitive = false }
    "shadowBundle"(project(path = ":common", configuration = "transformProductionNeoForge"))
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand("version" to project.version)
    }
}
