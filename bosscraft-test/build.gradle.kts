group = "com.bosspvp"
version = rootProject.version

dependencies {
    compileOnly(project(":bosscraft-api"))
    compileOnly(project(":bosscraft-core"))
}
tasks.processResources {

    filesMatching(listOf("**plugin.yml", "**paper-plugin.yml")) {
        expand("projectVersion" to project.version)
    }
}