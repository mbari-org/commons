plugins {
    id("com.gradleup.nmcp.aggregation")
}

nmcpAggregation {
    centralPortal {
        username = providers.gradleProperty("mavenCentralUsername")
        password = providers.gradleProperty("mavenCentralPassword")
        // USER_MANAGED: review and release manually at central.sonatype.com/publishing/deployments
        // AUTOMATIC: release immediately after validation passes
        publishingType = "USER_MANAGED"
    }
}

dependencies {
    nmcpAggregation(project(":jcommons"))
    nmcpAggregation(project(":scommons"))
}
