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
            plugin("spotless", "com.diffplug.spotless").version("6.20.0")

            val minecraftVersion = "1.20.1"
            version("minecraft", minecraftVersion)

            val forgeVersion = "47.1.3"
            version("loader", forgeVersion.substringBefore('.'))
            library("forge", "net.minecraftforge", "forge").version("$minecraftVersion-$forgeVersion")
            library("mixin", "org.spongepowered", "mixin").version("0.8.5")

            version("ae2", "15.0.13")
            library("ae2", "appeng", "appliedenergistics2-forge").versionRef("ae2")

            version("powah", "5.0.2")
            library("powah", "curse.maven", "powah-rearchitected-633483").version("4729364")
            library("architectury", "dev.architectury", "architectury-forge").version("9.1.12")
            library("cloth", "me.shedaniel.cloth", "cloth-config-forge").version("11.1.106")

            library("jei", "mezz.jei", "jei-$minecraftVersion-forge").version("15.2.0.27")
            library("jade", "curse.maven", "jade-324717").version("4768593")
        }
    }
}
