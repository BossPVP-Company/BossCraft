plugins {
    id ("java")
    id ("java-library")
    id ("com.github.johnrengelman.shadow") version ("8.0.0")
}
dependencies {
    implementation(project("bosscraft-api"))
    implementation(project("bosscraft-core"))
    implementation(project("bosscraft-test"))
}

allprojects{
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        maven ("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven ("https://oss.sonatype.org/content/repositories/snapshots")
        maven ("https://oss.sonatype.org/content/repositories/central")

        //minecraft
        maven ("https://libraries.minecraft.net/")

        //other
        maven ("https://repo.papermc.io/repository/maven-public/")
        maven("https://storehouse.okaeri.eu/repository/maven-public/")

        maven ("https://jitpack.io")
        mavenCentral()


    }
    dependencies{
        compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

        //annotations
        compileOnly("org.jetbrains:annotations:24.0.1")
        compileOnly("org.projectlombok:lombok:1.18.26")
        annotationProcessor("org.projectlombok:lombok:1.18.26")
        annotationProcessor("org.jetbrains:annotations:24.0.1")


        //okaeri config
        implementation("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.0-beta.5")
        implementation("eu.okaeri:okaeri-configs-validator-okaeri:5.0.0-beta.5")
        implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.0-beta.5")

        //kyori
        implementation("net.kyori:adventure-api:4.13.1")

        // Test impl
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    }
    java{
        withSourcesJar()
    }
    tasks {
        compileJava {
            options.encoding = "UTF-8"

            dependsOn(clean)
        }

        build {
            dependsOn(shadowJar)
        }
        test {
            useJUnitPlatform()

            // Show test results.
            testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }
}

group = "com.bosspvp"
version = findProperty("version")!!