# Render Individual Service Deployment

## Quick Setup Steps

1. **Create New Web Service** on Render dashboard
2. **Connect GitHub repo**
3. **Configure each service** with settings below

## Service Configurations

### 1. Eureka Server
- **Root Directory**: `EurekaServerService`
- **Environment Variables**:
  ```
  SPRING_PROFILES_ACTIVE=docker
  PORT=9099
  ```

### 2. Config Server
- **Root Directory**: `CloudConfig`
- **Environment Variables**:
  ```
  SPRING_PROFILES_ACTIVE=docker
  EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://[eureka-service-name].onrender.com/eureka
  PORT=9296
  ```

### 3. User Service
- **Root Directory**: `UserService`
- **Environment Variables**:
  ```
  SPRING_PROFILES_ACTIVE=docker
  EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://[eureka-service-name].onrender.com/eureka
  SPRING_CLOUD_CONFIG_URI=https://[config-service-name].onrender.com
  PORT=9050
  ```

### 4. Product Service
- **Root Directory**: `ProductService`
- **Environment Variables**:
  ```
  SPRING_PROFILES_ACTIVE=docker
  EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://[eureka-service-name].onrender.com/eureka
  SPRING_CLOUD_CONFIG_URI=https://[config-service-name].onrender.com
  PORT=9051
  ```

### 5. Order Service
- **Root Directory**: `OrderService`
- **Environment Variables**:
  ```
  SPRING_PROFILES_ACTIVE=docker
  EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://[eureka-service-name].onrender.com/eureka
  SPRING_CLOUD_CONFIG_URI=https://[config-service-name].onrender.com
  PORT=9052
  ```

### 6. Payment Service
- **Root Directory**: `PaymentService`
- **Environment Variables**:
  ```
  SPRING_PROFILES_ACTIVE=docker
  EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://[eureka-service-name].onrender.com/eureka
  SPRING_CLOUD_CONFIG_URI=https://[config-service-name].onrender.com
  PORT=9053
  ```

### 7. API Gateway
- **Root Directory**: `ApiGateway`
- **Environment Variables**:
  ```
  SPRING_PROFILES_ACTIVE=docker
  EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://[eureka-service-name].onrender.com/eureka
  SPRING_CLOUD_CONFIG_URI=https://[config-service-name].onrender.com
  PORT=9191
  ```

## Deployment Order
1. Eureka Server
2. Config Server
3. All other services
4. API Gateway

**Note**: Replace `[service-name]` with actual Render service URLs