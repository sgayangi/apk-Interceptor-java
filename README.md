# WSO2 APK Interceptor Configuration

This repository contains a gateway-level interceptor that can be applied in WSO2 API Gateway (APK). This interceptor adds specific headers to requests and responses, allowing for flexible API management.

## Overview

The interceptor performs the following actions:
- Adds the header `x-user: admin` before forwarding the request to the backend.
- Adds the header `x-user: Interceptor-Service` and replaces the `Content-Type` header with `application/json`.
- Replaces the response body with `{ "Status": "Interceptor Response" }` before sending the response to the user.

## Steps to Apply the Interceptor

1. **Clone the Repository:**
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Apply the Interceptor:**
   Navigate to the `Interceptor-Files` directory and apply the interceptor using:
   ```bash
   kubectl apply -f .
   ```

3. **Configure the Interceptor in `api-policy.yaml`:**
   In the `api-policy.yaml` file, configure the interceptor as follows:
   ```yaml
   apiVersion: dp.wso2.com/v1alpha2
   kind: APIPolicy
   metadata:
     name: sample-api-policy
   spec:
     override:
       requestInterceptors:
       - name: req-interceptor-service
       responseInterceptors:
       - name: res-interceptor-service
     targetRef:
       group: dp.wso2.com
       kind: Gateway
       name: wso2-apk-default
   ```
   This configuration ensures that the interceptor is applied to all APIs deployed in the gateway.

4. **Testing the Interceptor Service:**
   You can use any API to test the interceptor, as it is a global interceptor. Alternatively, use the sample API provided in this documentation.

5. **Build the Backend Service:**
   Run the following script to build the backend image:
   ```bash
   sh ./build-legacy-backend-service.sh
   ```

6. **Deploy the Sample API:**
   Navigate to the `API-Files` directory and deploy the sample API:
   ```bash
   kubectl apply -f .
   ```

7. **Invoke the API:**
   Test the interceptor by invoking the API using:
   ```bash
   curl --location 'https://default.gw.wso2.com:9095/interceptor/1.0.0/books' --header 'Host: default.gw.wso2.com' --data '{"SamplePayload":"WSO2 APK"}' -k
   ```

## Developing the Interceptor Service

In this example, the interceptor service is generated using the following OpenAPI definition:

```yaml
openapi: 3.0.0
servers:
  - url: "{interceptor_service_url}/api/v1"
    variables:
      interceptor_service_url:
        default: https://interceptor-service:8443
        description: interceptor_host assigned by the service provider
info:
  title: Choreo-Connect Interceptor Service
  description: Interceptor Service
  version: v1
  contact:
    name: WSO2
    url: http://wso2.com/products/api-manager/
  license:
    name: Apache 2.0
    url: http://www.apache.org/licenses/LICENSE-2.0.html
tags:
  - name: interceptor
    description: Interceptor
paths:
  "/handle-request":
    post:
      tags:
        - Request
      summary: Handle Request
      operationId: handleRequest
      requestBody:
        description: Content of the request
        content:
          application/json: {}
      responses:
        200:
          description: Successful operation
          content:
            application/json: {}
  "/handle-response":
    post:
      tags:
        - Response
      summary: Handle Response
      operationId: handleResponse
      requestBody:
        description: Content of the request
        content:
          application/json: {}
      responses:
        200:
          description: Successful operation
          content:
            application/json: {}
```

Save this definition as `interceptor-service-open-api.yaml` and run the following command to generate the interceptor service source code:

```bash
swagger-codegen generate -i ./interceptor-service-open-api.yaml -l spring -o ./interceptors-java
```

After generating the code, modify the logic in `HandleRequestApiController.java` and `HandleResponseApiController.java` as necessary. 

### Build the JAR File
Build the JAR file using Maven (assuming you are using IntelliJ IDEA):
```bash
mvn clean install
```

### Build the Docker Image
Add the generated `swagger-spring-1.0.0.jar` file to the `interceptors-java-jar` folder, and run the following script to build the Docker image of the interceptor service:
```bash
sh ./build-interceptor-service.sh
```

### Apply the Interceptor
Finally, navigate back to the `Interceptor-Files` and apply the interceptor again:
```bash
kubectl apply -f .
```
