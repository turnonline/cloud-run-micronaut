# Cloud Run service skeleton based on the Micronaut framework

This combination of technologies allows developers to create scalable and efficient microservices or serverless
applications on the Cloud Run platform.

Successful build is configured to be pushed to docker repository:

```
europe-west1-docker.pkg.dev/[gcp_project_id]/docker-images
```

In order to manually build and deploy docker image to Artifact Registry use:

```
mvn compile jib:build
```

If you have Docker installed, you can build to your local Docker installation,
so you can inspect or run the image locally:

```
mvn compile jib:dockerBuild
```