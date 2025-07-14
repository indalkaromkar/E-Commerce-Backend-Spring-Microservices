# OpenAPI Integration Guide

## Overview
OpenAPI (Swagger) integration has been implemented across all microservices using SpringDoc OpenAPI 3.

## Implementation Details

### Dependencies Added
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

### Services with OpenAPI Integration

| Service | Port | Swagger UI URL | API Docs URL |
|---------|------|----------------|--------------|
| UserService | 9050 | http://localhost:9050/swagger-ui.html | http://localhost:9050/v3/api-docs |
| ProductService | 9051 | http://localhost:9051/swagger-ui.html | http://localhost:9051/v3/api-docs |
| OrderService | 9052 | http://localhost:9052/swagger-ui.html | http://localhost:9052/v3/api-docs |
| PaymentService | 9053 | http://localhost:9053/swagger-ui.html | http://localhost:9053/v3/api-docs |
| ShippingService | 9054 | http://localhost:9054/swagger-ui.html | http://localhost:9054/v3/api-docs |
| FavouriteService | 9055 | http://localhost:9055/swagger-ui.html | http://localhost:9055/v3/api-docs |
| ProxyClient | 9056 | http://localhost:9056/swagger-ui.html | http://localhost:9056/v3/api-docs |

### Configuration Features
- JWT Bearer token authentication
- Consistent API documentation structure
- Service-specific titles and descriptions
- Security scheme configuration

### Usage
1. Start the desired microservice
2. Navigate to the Swagger UI URL
3. Use the "Authorize" button to add JWT token
4. Test API endpoints directly from the interface

### API Gateway Integration
Access all service APIs through the API Gateway:
- Base URL: http://localhost:8080
- Route format: `/service-name/**`

### Security Configuration
All protected endpoints require JWT token:
```
Authorization: Bearer <your-jwt-token>
```

### Customization
Each service's OpenAPI configuration can be customized in:
```
src/main/java/{package}/config/OpenApiConfig.java
```