# commons

This contains Java and Scala utility classes used across a number of projects

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
3. Select the staged repo and click the close button above it. This will go through the activity at the bottom. Check that everything passed. If it did it will appear in Maven central after some time. Be patient.
