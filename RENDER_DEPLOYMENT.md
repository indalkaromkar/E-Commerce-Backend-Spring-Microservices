# Render Docker Deployment Guide

Deploy each microservice individually on Render using Docker.

## Services to Deploy

### 1. Eureka Server
- **Repository**: Connect your GitHub repo
- **Environment**: Docker
- **Dockerfile Path**: `EurekaServerService/Dockerfile`
- **Port**: 9099

### 2. Config Server
- **Repository**: Connect your GitHub repo
- **Environment**: Docker
- **Dockerfile Path**: `CloudConfig/Dockerfile`
- **Port**: 9296
- **Environment Variables**:
  - `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://eureka-server.onrender.com/eureka`

### 3. User Service
- **Repository**: Connect your GitHub repo
- **Environment**: Docker
- **Dockerfile Path**: `UserService/Dockerfile`
- **Port**: 9050
- **Environment Variables**:
  - `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://eureka-server.onrender.com/eureka`
  - `SPRING_CLOUD_CONFIG_URI=https://config-server.onrender.com`

### 4. Product Service
- **Repository**: Connect your GitHub repo
- **Environment**: Docker
- **Dockerfile Path**: `ProductService/Dockerfile`
- **Port**: 9051
- **Environment Variables**:
  - `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://eureka-server.onrender.com/eureka`
  - `SPRING_CLOUD_CONFIG_URI=https://config-server.onrender.com`

### 5. Order Service
- **Repository**: Connect your GitHub repo
- **Environment**: Docker
- **Dockerfile Path**: `OrderService/Dockerfile`
- **Port**: 9052
- **Environment Variables**:
  - `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://eureka-server.onrender.com/eureka`
  - `SPRING_CLOUD_CONFIG_URI=https://config-server.onrender.com`

### 6. Payment Service
- **Repository**: Connect your GitHub repo
- **Environment**: Docker
- **Dockerfile Path**: `PaymentService/Dockerfile`
- **Port**: 9053
- **Environment Variables**:
  - `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://eureka-server.onrender.com/eureka`
  - `SPRING_CLOUD_CONFIG_URI=https://config-server.onrender.com`

### 7. API Gateway
- **Repository**: Connect your GitHub repo
- **Environment**: Docker
- **Dockerfile Path**: `ApiGateway/Dockerfile`
- **Port**: 9191
- **Environment Variables**:
  - `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://eureka-server.onrender.com/eureka`
  - `SPRING_CLOUD_CONFIG_URI=https://config-server.onrender.com`

## Deployment Order
1. Deploy Eureka Server first
2. Deploy Config Server
3. Deploy all other services (User, Product, Order, Payment)
4. Deploy API Gateway last