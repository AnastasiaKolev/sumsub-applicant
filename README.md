# The Project Purpose

This application is a tool for creating an Applicant within a SumSub system.

## Project details:

The application is used for the following actions:
1. Create an applicant within a SumSub system
2. Upload the document for the applicant
3. Start the applicant verification by setting Pending status
4. Get document recognition details after a wait for its creation

## Implementation details:

Implementation is designed for 2 various HTTP clients: Jersey 2 (Jax-RS) and OpenFeign.

### Build

Set properties in file named `gradle.properties` that can be found by path `src/main/resources`

    ./gradlew clean build

### Run

    ./gradlew run