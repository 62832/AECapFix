plugins {
    eclipse
    idea
    alias(libs.plugins.forge)
    alias(libs.plugins.mixin)
    alias(libs.plugins.spotless)
}

val modId = "aecapfix"

version = (System.getenv("CAPFIX_VERSION") ?: "v0.0").substring(1).substringBefore('-')
group = "gripe.90"
base.archivesName.set(modId)

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

minecraft {
    mappings("official", libs.versions.minecraft.get())

    copyIdeResources.set(true)

    runs {
        configureEach {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "info")
            property("appeng.tests", "true")

            mods {
                create(modId) {
                    source(sourceSets.main.get())
                }
            }
        }

        create("client")

        create("server") {
            args("--nogui")
        }
    }
}

repositories {
    maven {
        name = "ModMaven (K4U-NL)"
        url = uri("https://modmaven.dev/")
        content {
            includeGroup("appeng")
            includeGroup("mezz.jei")
        }
    }

    maven {
        name = "CurseMaven"
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }

    maven {
        name = "Architectury"
        url = uri("https://maven.architectury.dev/")
        content {
            includeGroup("dev.architectury")
            includeGroup("me.shedaniel.cloth")
        }
    }
}

dependencies {
    minecraft(libs.forge)
    annotationProcessor(variantOf(libs.mixin) { classifier("processor") })

    implementation(fg.deobf(libs.ae2.get()))

    implementation(fg.deobf(libs.powah.get()))
    runtimeOnly(fg.deobf(libs.architectury.get()))
    runtimeOnly(fg.deobf(libs.cloth.get()))

    runtimeOnly(fg.deobf(libs.lazierae2.get()))
    runtimeOnly(fg.deobf(libs.jei.get()))
    runtimeOnly(fg.deobf(libs.jade.get()))
}

mixin {
    add(sourceSets.main.get(), "$modId.refmap.json")
    config("$modId.mixins.json")
}

tasks {
    register("releaseInfo") {
        doLast {
            val output = System.getenv("GITHUB_OUTPUT")

            if (!output.isNullOrEmpty()) {
                val outputFile = File(output)
                outputFile.appendText("MOD_VERSION=$version\n")
                outputFile.appendText("MINECRAFT_VERSION=${libs.versions.minecraft.get()}\n")
            }
        }
    }

    processResources {
        val replaceProperties = mapOf(
            "version" to project.version,
            "fmlVersion" to "[${libs.versions.loader.get()},)",
            "ae2Version" to "(,${libs.versions.ae2.get().substringBefore('.').toInt() + 1})",
            "powahVersion" to "(,${libs.versions.powah.get().substringBefore('.').toInt() + 1})"
        )

        inputs.properties(replaceProperties)

        filesMatching("META-INF/mods.toml") {
            expand(replaceProperties)
        }
    }

    jar {
        finalizedBy("reobfJar")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

spotless {
    kotlinGradle {
        target("*.kts")
        diktat()
    }

    java {
        target("src/**/java/**/*.java")
        palantirJavaFormat()
        endWithNewline()
        indentWithSpaces(4)
        removeUnusedImports()
        toggleOffOn()
        trimTrailingWhitespace()

        // courtesy of diffplug/spotless#240
        // https://github.com/diffplug/spotless/issues/240#issuecomment-385206606
        custom("noWildcardImports") {
            if (it.contains("*;\n")) {
                throw Error("No wildcard imports allowed")
            }

            it
        }

        bumpThisNumberIfACustomStepChanges(1)
    }

    json {
        target("src/*/resources/**/*.json")
        targetExclude("src/generated/resources/**")
        prettier().config(mapOf("parser" to "json"))
    }
}
