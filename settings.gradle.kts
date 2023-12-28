pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.minecraftforge.net/") }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("forge", "net.minecraftforge.gradle").version("6.0.+")
            plugin("mixin", "org.spongepowered.mixin").version("0.7.+")
            plugin("spotless", "com.diffplug.spotless").version("6.23.3")

            val minecraftVersion = "1.19.2"
            version("minecraft", minecraftVersion)

            val forgeVersion = "43.2.14"
            version("loader", forgeVersion.substringBefore('.'))
            library("forge", "net.minecraftforge", "forge").version("$minecraftVersion-$forgeVersion")
            library("mixin", "org.spongepowered", "mixin").version("0.8.5")

            version("ae2", "12.9.5")
            library("ae2", "appeng", "appliedenergistics2-forge").versionRef("ae2")

            version("powah", "4.0.11")
            library("powah", "curse.maven", "powah-rearchitected-633483").version("4729361")
            library("architectury", "dev.architectury", "architectury-forge").version("6.2.43")
            library("cloth", "me.shedaniel.cloth", "cloth-config-forge").version("7.0.74")

            library("ic2c", "curse.maven", "ic2-classic-242942").version("4746971")
            library("jei", "mezz.jei", "jei-$minecraftVersion-forge").version("11.4.0.285")
            library("jade", "curse.maven", "jade-324717").version("4433884")
        }
    }
}
