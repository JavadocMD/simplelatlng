# Development notes

## Local testing

I have a bash script for switching java versions. To make sure I'm on Java 8 for example,

```bash
jvm -u 8
source jvm --home
```

Then running

```bash
mvn clean test jacoco:report
```

will test and create a coverage report at `target/site/jacoco/index.html`

## Release procedure

1. Increment version identifier in `pom.xml` and commit.
1. Tag the commit with the version number; e.g.: `git tag v1.4.0`
1. When pushed, GitHub Action "Create Release" will create a draft Release.
1. GitHub action "Publish to the Maven Central Repository" was intended to respond to that and execute the build and deploy. However that doesn't work because the GitHub Release is created as draft. Instead, just run it manually. (_I imagine there's a way to automate this to be much smoother, but I'm an Actions noob so it will wait for now._)
1. Log in to [Maven Central Repository](https://oss.sonatype.org).
1. Find the project in Staging Repositories.
1. Close the repo, wait a few, then release it.
1. (_More manual steps in need of automation..._) Download the artifacts (jar, source, and javadoc) and upload them to the GitHub Release created earlier. Add a changelog for the release. Publish it.
