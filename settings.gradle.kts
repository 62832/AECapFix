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

            val minecraftVersion = "1.18.2"
            version("minecraft", minecraftVersion)

            val forgeVersion = "40.1.60"
            version("loader", forgeVersion.substringBefore('.'))
            library("forge", "net.minecraftforge", "forge").version("$minecraftVersion-$forgeVersion")
            library("mixin", "org.spongepowered", "mixin").version("0.8.5")

            version("ae2", "11.7.4")
            library("ae2", "appeng", "appliedenergistics2-forge").versionRef("ae2")

            version("powah", "3.0.8")
            library("powah", "curse.maven", "powah-rearchitected-633483").version("4525198")
            library("architectury", "dev.architectury", "architectury-forge").version("4.11.93")
            library("cloth", "me.shedaniel.cloth", "cloth-config-forge").version("6.5.102")

            library("lazierae2", "curse.maven", "lazierae2-489843").version("4145521")
            library("jei", "mezz.jei", "jei-$minecraftVersion-forge").version("10.2.1.283")
            library("jade", "curse.maven", "jade-324717").version("4575623")
        }
    }
}
