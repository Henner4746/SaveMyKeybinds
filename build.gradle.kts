plugins {
	id("net.fabricmc.fabric-loom")
	id("me.modmuss50.mod-publish-plugin")
}

fun prop(name: String): String = findProperty(name)?.toString() ?: throw IllegalArgumentException("Missing property: $name")

val minecraft = stonecutter.current.version
val modVersion = prop("mod_version")

base { archivesName.set(prop("archives_base_name")) }
group = prop("maven_group")
version = "$modVersion+mc$minecraft"

repositories {
	mavenCentral()
	exclusiveContent {
		forRepository {
			maven {
				name = "Modrinth"
				url = uri("https://api.modrinth.com/maven")
			}
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraft")
	implementation("net.fabricmc:fabric-loader:${prop("loader_version")}")

	// Fabric API is not an end user dep -- it's required by Controlling hence why it's marked as runtimeOnly
	runtimeOnly("net.fabricmc.fabric-api:fabric-api:${prop("fabric_version")}")

	// -PwithoutControlling starts the game without Controlling, to test the vanilla keybinds screen
	val controllingScope = if (hasProperty("withoutControlling")) "compileOnly" else "implementation"
	add(controllingScope, "maven.modrinth:controlling:${prop("controlling_version")}")
	add(controllingScope, "maven.modrinth:searchables:${prop("searchables_version")}")
	include(implementation("com.moulberry:mixinconstraints:${prop("mcon_version")}")!!)
}

tasks.processResources {
	val properties = mapOf("version" to project.version, "support_range" to prop("support_range"))
	inputs.properties(properties)

	filesMatching("fabric.mod.json") {
		expand(properties)
	}
}

java {
	withSourcesJar()
	toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.withType<JavaCompile>().configureEach {
	options.release.set(25)
	options.encoding = "UTF-8"
}

tasks.named<Jar>("jar") {
	from(rootDir.resolve("LICENSE")) {
		rename { "${it}_${project.base.archivesName.get()}" }
	}
}

loom {
	// Not runConfigs.all: the game test run config has no environment yet when it gets added
	runConfigs.named("client") {
		ideConfigGenerated(true)
		runDir("../../run")
	}
	runConfigs.named("server") {
		ideConfigGenerated(false)
		runDir("../../run")
	}
}

// Scripted in-game tests: ./gradlew :26.3:runClientGameTest (add -PwithoutControlling for the vanilla screen)
fabricApi {
	configureTests {
		createSourceSet = true
		modId = "savemykeybinds_test"
		enableGameTests = false
		enableClientGameTests = true
		eula = true
	}
}

dependencies {
	"gametestImplementation"("net.fabricmc.fabric-api:fabric-api:${prop("fabric_version")}")
}

publishMods {
	val modrinthToken = findProperty("modrinthToken")
	dryRun = modrinthToken == null

	file = tasks.jar.flatMap { it.archiveFile }
	additionalFiles.from(tasks.named<Jar>("sourcesJar").flatMap { it.archiveFile })

	displayName = "${prop("mod_name")} $modVersion for Fabric $minecraft"
	version = "$modVersion+mc$minecraft-fabric"
	changelog = rootProject.file("CHANGELOG.md").readText()
	type = STABLE
	modLoaders.add("fabric")

	modrinth {
		projectId = prop("publish_modrinth")
		accessToken = modrinthToken.toString()
		minecraftVersions.add(minecraft)
		optional("fabric-api")
		optional("controlling")
	}
}
