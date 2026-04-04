loom {
    accessWidenerPath = file("src/main/resources/elevatormod.classtweaker")
}

architectury {
    common((property("enabled_platforms") as String).split(','))
}

dependencies {
    // We depend on Fabric Loader here to use the Fabric @Environment annotations,
    // which get remapped to the correct annotations on each platform.
    // Do NOT use other classes from Fabric Loader.
    implementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")

    api("fuzs.forgeconfigapiport:forgeconfigapiport-common-neoforgeapi:${property("forge_config_api_port_version")}")
}
