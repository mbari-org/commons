# commons

JVM utility libraries from the [Monterey Bay Aquarium Research Institute](https://www.mbari.org). Two artifacts are published to Maven Central:

- **`jcommons`** — Java 21 utilities (JPMS module `org.mbari.jcommons`)
- **`scommons_3`** — Scala 3 utilities, depends on `jcommons`

[![Javadoc](https://javadoc.io/badge2/org.mbari.commons/jcommons/javadoc.svg)](https://javadoc.io/doc/org.mbari.commons/jcommons/latest/org.mbari.jcommons/module-summary.html)

## Using in your project

Replace `VERSION` with the latest release (current: `0.0.8`).

### Gradle (Kotlin DSL)

```kotlin
// Java only
implementation("org.mbari.commons:jcommons:VERSION")

// Scala 3
implementation("org.mbari.commons:scommons_3:VERSION")
```

### Gradle (Groovy DSL)

```groovy
// Java only
implementation 'org.mbari.commons:jcommons:VERSION'

// Scala 3
implementation 'org.mbari.commons:scommons_3:VERSION'
```

### Maven

```xml
<!-- Java only -->
<dependency>
    <groupId>org.mbari.commons</groupId>
    <artifactId>jcommons</artifactId>
    <version>VERSION</version>
</dependency>

<!-- Scala 3 -->
<dependency>
    <groupId>org.mbari.commons</groupId>
    <artifactId>scommons_3</artifactId>
    <version>VERSION</version>
</dependency>
```

## What's included

### jcommons (`org.mbari.jcommons`)

| Package | Contents |
|---------|----------|
| `ocean` | Seawater properties, light attenuation, wave equations, chlorophyll |
| `math` | Matrix operations (`Matlib`), statistics (`Statlib`), double/number utilities |
| `util` | Fluent `Logging` (wraps `System.Logger`), `StringUtil`, `IOUtil`, `ReflectUtil`, `StreamUtil`, `AutoCloseableLock`, `ActiveAppBeacon`/`Pinger`, `Tuple2`/`Tuple3` |
| `awt` | Image and AWT utilities |

### scommons_3 (`org.mbari.scommons`)

| Package | Contents |
|---------|----------|
| `ocean` | Scala versions of seawater, light, waves, chlorophyll, atmosphere |
| `math` | Statistics, KDE, FFT (`JTransforms`), trigonometry, probabilities, matrix ops, array extensions |
| `geometry` | `Point2D`/`3D`/`4D`, `Polygon2D`, `Triangle3D`, `Envelope` |
| `gis` | Grid types (ASC read/write), rugosity calculator, space-time zones |
| `canadiangrid` | Camera pixel-to-grid math |
| `util` | `ISO8601`, `SeqUtil`, `Zip`, email (via commons-email), reflection |
| `etc/jdk` | Scala-idiomatic wrappers: `Loggers`, `Futures`, `Strings`, `Uris`, `Instants`, `Paths`, `Loops`, `Colors`, `Images` |
| `etc/sdk` | Functional helpers: `Eithers` (traverse, `Optional` → `Either`), `IO`, `Iterables` |
| `etc/spire` | Complex number support via Typelevel Spire |
| `etc/xml` | XML converters |
| `etc/csv` | CSV transformers |

## Developers

### Build and test

```bash
./gradlew build
./gradlew test

# Single subproject
./gradlew :jcommons:test
./gradlew :scommons:test

# Format Scala code
./gradlew :scommons:scalafmt
```

### Deployment to Maven Central

```bash
# Publish to Sonatype staging (requires GPG key and OSSRH credentials in ~/.gradle/gradle.properties)
./gradlew uploadToSonatype
```

Then log in to <https://s01.oss.sonatype.org/>, select the staged repository under **Staging Repositories**, click **Close**, verify all checks passed, then click **Release**.
