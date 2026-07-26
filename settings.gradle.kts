import javax.xml.parsers.DocumentBuilderFactory
import java.io.File

/*
 * Publishing to Maven Central goes through the Central Portal (central.sonatype.com)
 * via the nmcp plugin. The Portal user token is reused from the existing Maven
 * ~/.m2/settings.xml <server id="central"> entry so there is a single source of
 * truth for the credential. Gradle properties (centralUsername / centralPassword)
 * or environment variables (CENTRAL_USERNAME / CENTRAL_PASSWORD) override it, e.g.
 * in CI where ~/.m2/settings.xml is not present.
 */
plugins {
    id("com.gradleup.nmcp.settings") version "1.6.1"
}

/** Reads username/password for a <server> id from ~/.m2/settings.xml, or null. */
fun mavenServerCredentials(serverId: String): Pair<String, String>? {
    val settingsFile = File(System.getProperty("user.home"), ".m2/settings.xml")
    if (!settingsFile.exists()) return null
    return runCatching {
        val doc = DocumentBuilderFactory.newInstance()
            .apply { isNamespaceAware = false }
            .newDocumentBuilder()
            .parse(settingsFile)
        val servers = doc.getElementsByTagName("server")
        for (i in 0 until servers.length) {
            val children = servers.item(i).childNodes
            var id: String? = null
            var user: String? = null
            var pass: String? = null
            for (j in 0 until children.length) {
                val node = children.item(j)
                when (node.nodeName) {
                    "id" -> id = node.textContent?.trim()
                    "username" -> user = node.textContent?.trim()
                    "password" -> pass = node.textContent?.trim()
                }
            }
            if (id == serverId && !user.isNullOrEmpty() && !pass.isNullOrEmpty()) {
                return@runCatching user to pass
            }
        }
        null
    }.getOrNull()
}

val central = mavenServerCredentials("central")
val centralUser = providers.gradleProperty("centralUsername").orNull
    ?: System.getenv("CENTRAL_USERNAME")
    ?: central?.first
val centralPass = providers.gradleProperty("centralPassword").orNull
    ?: System.getenv("CENTRAL_PASSWORD")
    ?: central?.second

nmcpSettings {
    centralPortal {
        centralUser?.let { username.set(it) }
        centralPass?.let { password.set(it) }
        // Upload only; release manually from https://central.sonatype.com/ (like the
        // old "Close" + "Release" flow). Use "AUTOMATIC" to release on validation.
        publishingType.set("USER_MANAGED")
    }
}

// Provides a repository for the root project (which has no build.gradle.kts) so
// nmcp can resolve its helper artifacts. Subprojects keep their own repositories
// (declared in the convention plugin) under the default PREFER_PROJECT mode.
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "commons"
include(":jcommons")
include(":scommons")
