# Cloud Run service skeleton based on the Micronaut framework

This combination of technologies enables developers to create scalable and efficient microservices or serverless
applications that run on Cloud Run. With an option to build GraalVM native image that can be deployed using Cloud Build.

## Packaging types of the Micronaut Maven Plugin

* jar (default): produces a runnable fat JAR.
* native-image: generates a GraalVM native image.
* docker: builds a Docker image with the application artifacts (compiled classes, resources, dependencies, etc).
* docker-native: builds a Docker image with a GraalVM native image inside.

In order to successfully build a docker image, the docker daemon must be running.

# Default JAR packaging

### Build to local Docker daemon

```bash
mvn clean package -Dpackaging=docker
```

It's an equivalent of executing the jib::dockerBuild goal.

Once successfully finished, you can find the final image in Docker daemon tagged as
**europe-west1-docker.pkg.dev/gcp_project_id/docker-images/cloud-run-micronaut:short_sha**

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
europe-west1-docker.pkg.dev/gcp_project_id/docker-images
```

Before the push, make sure the authentication is correctly configured
[gcloud credential helper (recommended)](https://cloud.google.com/artifact-registry/docs/docker/authentication#gcloud-helper)

# GraalVM support

There is a dedicated Cloud Build configuration for GraalVM native image build. To use
it, [build-graalvm.yaml](build-graalvm.yaml)
file must be utilized.

**Note about the workaround:** *docker push* is used instead of *mvn deploy* as the micronaut-maven-plugin to upload
docker image to the Artifact Registry supports only *auth.username/auth.password* instead of preferred gcloud credential
helper,
see [Choosing an authentication method](https://cloud.google.com/artifact-registry/docs/docker/authentication#methods)

### Performance boost seen directly on GCP

```stacktrace
INFO io.micronaut.runtime.Micronaut - Startup completed in 183ms. Server Running: http://localhost:8080
GET 200 648 B 57 ms Safari 16.5.2 https://cloud-run-micronaut-nj5jdl6g3q-ew.a.run.app/       #first attempt
GET 200 648 B 2 ms Safari 16.5.2 https://cloud-run-micronaut-nj5jdl6g3q-ew.a.run.app/
```

compared to the Default JAR packaging

```stacktrace
INFO io.micronaut.runtime.Micronaut - Startup completed in 3573ms. Server Running: http://localhost:8080
GET 200 648 B 1.387 s Safari 16.5.2 https://cloud-run-micronaut-nj5jdl6g3q-ew.a.run.app/     #first attempt
GET 200 648 B 5 ms Safari 16.5.2 https://cloud-run-micronaut-nj5jdl6g3q-ew.a.run.app/
```

### Check Native Image Support for Google Cloud Libraries

[Supported APIs](https://github.com/googleapis/google-cloud-java#supported-apis)

# Cloud Build testing

```bash
gcloud builds submit --substitutions=REPO_NAME="cloud-run-micronaut",SHORT_SHA="1.0.0" --config build-graalvm.yaml .
```

To speed up the build time, you can configure a more powerful machine type (attach it to the end of the Cloud Build
YAML).
The first 120 build-minutes per day are free (applicable only to the default one).

```yaml
options:
  machineType: 'E2_HIGHCPU_8'
```

# Additional useful links

* [Micronaut GCP](https://micronaut-projects.github.io/micronaut-gcp/latest/)
* [Micronaut Maven Plugin](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/)
* [Deploy GraalVM native executable to Google Cloud Run](https://guides.micronaut.io/latest/micronaut-graalvm-native-image-google-cloud-platform-cloud-run-maven-java.html)
* [Maven plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html)
* [Jib - Containerize your Maven project](https://github.com/GoogleContainerTools/jib/blob/master/jib-maven-plugin/README.md)
* [Cloud Run for App Engine customers](https://cloud.google.com/appengine/docs/standard/cloud-run-for-gae-customers)