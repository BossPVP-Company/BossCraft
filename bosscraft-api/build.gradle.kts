group = "com.bosspvp"
version = rootProject.version

repositories {
    maven ("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven ("https://oss.sonatype.org/content/repositories/snapshots")
    maven ("https://oss.sonatype.org/content/repositories/central")

    //minecraft
    maven ("https://libraries.minecraft.net/")

    maven ("https://repo.papermc.io/repository/maven-public/")
}


dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("com.mojang:authlib:1.5.21")
}