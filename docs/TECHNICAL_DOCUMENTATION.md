# Technical Documentation - eCommerce Microservices

## Architecture Overview

### System Architecture
This eCommerce platform follows a microservices architecture pattern with the following core components:

- **API Gateway**: Single entry point for all client requests
- **Service Discovery**: Eureka server for service registration and discovery
- **Configuration Management**: Centralized configuration server
- **Distributed Tracing**: Zipkin for request tracing across services
- **Database**: MySQL for persistent data storage

### Microservices Components

| Service | Port | Purpose | Database |
|---------|------|---------|----------|
| EurekaServerService | 9099 | Service Discovery | N/A |
| ApiGateway | 8080 | API Gateway & Routing | N/A |
| CloudConfig | 9296 | Configuration Server | N/A |
| UserService | 9050 | User Management | MySQL |
| ProductService | 9051 | Product Catalog | MySQL |
| OrderService | 9052 | Order Processing | MySQL |
| PaymentService | 9053 | Payment Processing | MySQL |
| ShippingService | 9054 | Shipping Management | MySQL |
| FavouriteService | 9055 | User Favorites | MySQL |
| ProxyClient | 9056 | External API Proxy | N/A |

## Technology Stack

### Core Technologies
- **Java 17**: Programming language
- **Spring Boot 3.2.4**: Application framework
- **Spring Cloud 2023.0.0**: Microservices framework
- **Maven**: Build tool and dependency management

### Key Dependencies
- **Spring Cloud Gateway**: API Gateway implementation
- **Spring Cloud Netflix Eureka**: Service discovery
- **Spring Cloud Config**: Configuration management
- **Spring Security**: Authentication and authorization
- **JWT**: Token-based authentication
- **Spring Data JPA**: Data persistence
- **MySQL**: Relational database
- **Flyway**: Database migration
- **Zipkin**: Distributed tracing
- **Spring Boot Actuator**: Monitoring and health checks
- **SpringDoc OpenAPI**: API documentation
- **Lombok**: Code generation

## Prerequisites

### System Requirements
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

### Development Tools
- IDE (IntelliJ IDEA, Eclipse, VS Code)
- Postman or similar API testing tool
- MySQL Workbench or similar database client

## Setup Instructions

### 1. Database Setup
```sql
-- Create databases for each service
CREATE DATABASE ecommerce_userservice;
CREATE DATABASE ecommerce_productservice;
CREATE DATABASE ecommerce_orderservice;
CREATE DATABASE ecommerce_paymentservice;
CREATE DATABASE ecommerce_shippingservice;
CREATE DATABASE ecommerce_favouriteservice;
```

### 2. Service Startup Order
Start services in the following order to ensure proper dependency resolution:

1. **EurekaServerService** (Port: 9099)
2. **CloudConfig** (Port: 9296)
3. **ApiGateway** (Port: 8080)
4. **Core Services** (UserService, ProductService, etc.)

### 3. Build and Run
```bash
# Clone repository
git clone <repository-url>
cd E-Commerce-Backend-Spring-Microservices

# Build all services
mvn clean install

# Start Eureka Server
cd EurekaServerService
mvn spring-boot:run

# Start other services (in separate terminals)
cd ../UserService
mvn spring-boot:run
```

## Configuration Management

### Environment-Specific Configuration
- **Development**: Local MySQL, embedded config
- **Production**: External MySQL, centralized config server

### Key Configuration Files
- `application.properties`: Service-specific configuration
- `bootstrap.properties`: Bootstrap configuration for config server
- `application-{profile}.properties`: Environment-specific overrides

### Security Configuration
- JWT secret key configuration
- Database credentials
- Email service configuration
- CORS settings

## API Documentation

### Swagger/OpenAPI
Each service exposes API documentation at:
```
http://localhost:{port}/swagger-ui.html
```

### Authentication
- JWT-based authentication
- Token expiration: 1 hour (configurable)
- Refresh token mechanism

## Monitoring and Observability

### Health Checks
All services expose health endpoints:
```
GET /actuator/health
GET /actuator/info
```

### Distributed Tracing
- **Zipkin Server**: http://localhost:9411
- Trace sampling: 100% (configurable)
- Request correlation across services

### Logging
- Structured logging with correlation IDs
- Log levels configurable per package
- Centralized log aggregation ready

## Database Design

### User Service Schema
- `users`: User profile information
- `credentials`: Authentication data
- `addresses`: User addresses
- `verification_tokens`: Email verification

### Product Service Schema
- `products`: Product catalog
- `categories`: Product categories
- `inventory`: Stock management

### Order Service Schema
- `orders`: Order information
- `order_items`: Order line items
- `order_status`: Order lifecycle

## Security Implementation

### Authentication Flow
1. User login with credentials
2. JWT token generation
3. Token validation on subsequent requests
4. Token refresh mechanism

### Authorization
- Role-based access control (RBAC)
- Method-level security annotations
- Resource-level permissions

### Data Protection
- Password encryption (BCrypt)
- Sensitive data masking in logs
- SQL injection prevention

## Development Guidelines

### Code Standards
- Follow Spring Boot best practices
- Use Lombok for boilerplate reduction
- Implement proper exception handling
- Write comprehensive unit tests

### API Design
- RESTful API principles
- Consistent response formats
- Proper HTTP status codes
- API versioning strategy

### Database Guidelines
- Use JPA entities with proper relationships
- Implement database migrations with Flyway
- Follow naming conventions
- Optimize queries for performance

## Deployment

### Docker Support
Each service includes Dockerfile for containerization:
```dockerfile
FROM openjdk:17-jre-slim
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Kubernetes Deployment
- Service discovery via Kubernetes DNS
- ConfigMaps for configuration
- Secrets for sensitive data
- Health checks and readiness probes

### CI/CD Pipeline
- Automated testing on commit
- Docker image building
- Deployment to staging/production
- Database migration automation

## Troubleshooting

### Common Issues
1. **Service Registration Failures**
   - Check Eureka server connectivity
   - Verify service configuration

2. **Database Connection Issues**
   - Validate database credentials
   - Check network connectivity
   - Verify database schema exists

3. **JWT Token Issues**
   - Check token expiration
   - Validate JWT secret configuration
   - Verify token format

### Debugging Tools
- Spring Boot Actuator endpoints
- Zipkin trace analysis
- Application logs
- Database query logs

## Performance Optimization

### Caching Strategy
- Redis for session management
- Application-level caching
- Database query optimization

### Load Balancing
- Client-side load balancing with Ribbon
- API Gateway routing
- Database connection pooling

### Monitoring Metrics
- Response times
- Error rates
- Resource utilization
- Database performance

## Future Enhancements

### Planned Features
- Event-driven architecture with Kafka
- CQRS pattern implementation
- Circuit breaker pattern
- API rate limiting
- Advanced monitoring with Prometheus/Grafana

### Scalability Considerations
- Horizontal scaling support
- Database sharding strategy
- Microservice decomposition
- Event sourcing implementation