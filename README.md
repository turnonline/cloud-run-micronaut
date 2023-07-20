# Cloud Run service skeleton based on the Micronaut framework

This combination of technologies allows developers to create scalable and efficient microservices or serverless
applications on the Cloud Run platform.

## Packaging types of the Micronaut Maven Plugin

* jar (default): produces a runnable fat JAR.
* native-image: generates a GraalVM native image.
* docker: builds a Docker image with the application artifacts (compiled classes, resources, dependencies, etc).
* docker-native: builds a Docker image with a GraalVM native image inside.

In order to successfully build a docker image, the docker daemon must be running.

### Local docker build

```bash
mvn clean package -Dpackaging=docker
```

once successfully finished, you can find the final image in Docker daemon tagged as
**europe-west1-docker.pkg.dev/[gcp_project_id]/docker-images/cloud-run-micronaut:1.0-SNAPSHOT**

### Manual build and remote deployment of the docker image

Before attempting to push the image, make sure you run GraalVM JDK. The easiest way to install GraalVM on Linux or Mac
is to use [SDKMan.io](https://sdkman.io/).

*Java 17*, check the latest version at [GraalVM downloads](https://www.graalvm.org/downloads/)

```bash
sdk install java 17.0.7-graal
```

Command to push the Docker image of the application to the Artifact Registry:

```bash
mvn clean deploy -Dpackaging=docker
```

the final image will be pushed at:

```
europe-west1-docker.pkg.dev/[gcp_project_id]/docker-images
```

Before the push, make sure the authentication is correctly configured
[gcloud credential helper (recommended)](https://cloud.google.com/artifact-registry/docs/docker/authentication#gcloud-helper)

