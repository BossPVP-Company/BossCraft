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

        //PAPI
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

        //crunch
        maven("https://redempt.dev")

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


        //redis
        implementation("redis.clients:jedis:2.7.2")
        implementation("org.redisson:redisson:3.20.1")
        implementation("org.msgpack:jackson-dataformat-msgpack:0.9.3")
        //gson
        implementation("com.google.code.gson:gson:2.10.1")
        //appache
        implementation("org.apache.commons:commons-pool2:2.6.0")

        //okaeri config
        implementation("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.0-beta.5")
        implementation("eu.okaeri:okaeri-configs-validator-okaeri:5.0.0-beta.5")
        implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.0-beta.5")

        //kyori
        implementation("net.kyori:adventure-api:4.13.1")

        //crunch
        implementation("com.github.Redempt:Crunch:1.1.3")

        //caffeine
        implementation("com.github.ben-manes.caffeine:caffeine:3.1.6")

        //PAPI
        compileOnly("me.clip:placeholderapi:2.11.2")

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