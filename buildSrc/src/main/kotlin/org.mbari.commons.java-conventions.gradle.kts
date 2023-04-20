/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    `java-library`
    `maven-publish`
    signing
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    testImplementation("ch.qos.logback:logback-classic:1.4.6")
    testImplementation("org.slf4j:slf4j-jdk-platform-logging:2.0.7")
}

group = "org.mbari.commons"
version = "0.0.4"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("commons")
                // packaging.set("jar")
                url.set("https://github.com/mbari-org/commons")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("hohonuuli")
                        name.set("Brian Schlining")
                        email.set("brian@mbari.org")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/mbari-org/commons.git")
                    developerConnection.set("scm:git:ssh://github.com:mbari-org/commons.git")
                    url.set("https://github.com/mbari-org/commons/tree/main")
                }
            }
        }
    }
    repositories {
        maven {
            // change URLs to point to your repos, e.g. http://my.org/repo
            val releasesRepoUrl = uri(layout.buildDirectory.dir("repos/releases"))
            val snapshotsRepoUrl = uri(layout.buildDirectory.dir("repos/snapshots"))
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            // val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            // val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            // url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            // credentials { 
            //     username = project.findProperty("osshrUsername") as String? ?: System.getenv("OSSRH_USERNAME")
            //     password = project.findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
            // }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
    useGpgCmd()
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}