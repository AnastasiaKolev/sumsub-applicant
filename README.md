# The Project Purpose

This application is a tool for creating an Applicant within a SumSub system.

## Project details:

The application is used for the following actions:
1. Create an applicant within a SumSub system
2. Upload the document for the applicant
3. Start the applicant verification by setting Pending status
4. Get document recognition details after a wait for its creation

## Implementation details:

Implementation is designed for 2 various HTTP clients: Jersey (Jax-RS) and OpenFeign.

### Build

Set properties in file named `gradle.properties` that can be found by path `src/main/resources`

    ./gradlew clean build

### Run

    ./gradlew run

### Jersey vs. OpenFeign

_Jersey_

    Pros:
        - "JSR standard can be run without servlet container"
        - "Production-ready implementation designed for REST applications only"
        - Jersey2 is easily converted to Jersey3, just need to replace the javax.ws.rs to jakarta
        - Interceptors and Filters are easy to use 

    Cons:
        - Extra code for simple tasks
        - The need to explicitly process each query param for each specific request
        - Entity is hard to conver to byte array 
        - Need to use Interceptor or Filter in order to handle extra work with request/response

---
_OpenFeign_

    Pros:
        - Easy to implement
        - "A higher level of abstraction over REST-based service calls"
        - "A declarative way to build HTTP clients"
        - "URLs are not hardcoded"
        - Query params are flexible to use by including into path variable
        - Source code is written in Kotlin and therefore smoothly compatable with Gradle

    Cons:
        - "it does not support caching based on Cache Control headers" stackoverflow
