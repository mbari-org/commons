# commons

This contains Java and Scala utility classes used across a number of projects.

[javadoc](https://javadoc.io/doc/org.mbari.commons/jcommons/latest/org.mbari.jcommons/module-summary.html)

## Developers

### Deployment to Maven Central

#### Build and push to staging repo

```bash
# build with Java 17
gradlew clean build package --info
```

#### Publish

1. Login to <https://s01.oss.sonatype.org/>
2. Click on [Staging Repositories](https://s01.oss.sonatype.org/#stagingRepositories) on the left.
3. Select the staged repo and click the `Close` button above it. This will go through the activity at the bottom. Verify that all checks passed.
4. Select the staged repo and click the `Release` button above it. Be patient, it will appear in Maven central after some time
