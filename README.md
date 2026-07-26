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

Releases are published through the [Central Portal](https://central.sonatype.com) using the
[nmcp](https://gradleup.com/nmcp/) Gradle plugin, which bundles both `jcommons` and `scommons_3`
into a single, signed deployment.

**One-time setup**

1. **GPG signing** — a GPG key must be configured locally; the build signs artifacts with
   `gpg` (`useGpgCmd()`) and the Portal rejects unsigned uploads.
2. **Central Portal token** — generate a user token at
   <https://central.sonatype.com/account>. The build reads it from your Maven
   `~/.m2/settings.xml`:

   ```xml
   <settings>
     <servers>
       <server>
         <id>central</id>
         <username>TOKEN_USERNAME</username>
         <password>TOKEN_PASSWORD</password>
       </server>
     </servers>
   </settings>
   ```

   Alternatively, set gradle properties `centralUsername` / `centralPassword` in
   `~/.gradle/gradle.properties`, or environment variables `CENTRAL_USERNAME` /
   `CENTRAL_PASSWORD` (handy for CI, where `~/.m2/settings.xml` is absent).

**Releasing**

1. Bump the `version` in
   `buildSrc/src/main/kotlin/org.mbari.commons.java-conventions.gradle.kts`.
2. (Optional) Inspect the exact bundle before uploading:

   ```bash
   ./gradlew nmcpZipAggregation   # -> build/nmcp/zip/aggregation.zip
   ```

3. Upload the deployment to the Portal:

   ```bash
   ./gradlew publishAggregationToCentralPortal
   ```

4. Log in to <https://central.sonatype.com>, open the deployment under **Deployments**,
   confirm validation passed, and click **Publish**.

The deployment is uploaded as `USER_MANAGED`, so it waits in the *Validated* state for you
to publish it manually — you can drop it from the Portal if something looks wrong. To release
automatically once validation passes, change `publishingType` to `"AUTOMATIC"` in
`settings.gradle.kts`.
