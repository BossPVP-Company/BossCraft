plugins {
    id ("java")
    id ("java-library")
    id ("com.github.johnrengelman.shadow") version ("8.0.0")
}
dependencies {
    implementation(project("bosscraft-api"))
    implementation(project("bosscraft-core"))
}

allprojects{
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        maven ("https://jitpack.io")
        mavenCentral()


    }
    dependencies{

        //annotations
        compileOnly("org.jetbrains:annotations:24.0.1")
        compileOnly("org.projectlombok:lombok:1.18.26")
        annotationProcessor("org.projectlombok:lombok:1.18.26")
        annotationProcessor("org.jetbrains:annotations:24.0.1")


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