# Deployment Guide

## Local Development Setup

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Git

### Quick Start
```bash
# 1. Clone repository
git clone <repository-url>
cd E-Commerce-Backend-Spring-Microservices

# 2. Setup databases
mysql -u root -p < scripts/setup-databases.sql

# 3. Start services in order
./scripts/start-services.sh
```

## Docker Deployment

### Build Docker Images
```bash
# Build all services
docker-compose build

# Or build individual service
cd UserService
docker build -t ecommerce/user-service:latest .
```

### Docker Compose
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: ecommerce
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  eureka-server:
    build: ./EurekaServerService
    ports:
      - "9099:9099"
    environment:
      - SPRING_PROFILES_ACTIVE=docker

  config-server:
    build: ./CloudConfig
    ports:
      - "9296:9296"
    depends_on:
      - eureka-server
    environment:
      - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:9099/eureka

  api-gateway:
    build: ./ApiGateway
    ports:
      - "8080:8080"
    depends_on:
      - eureka-server
      - config-server
    environment:
      - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:9099/eureka

  user-service:
    build: ./UserService
    ports:
      - "9050:9050"
    depends_on:
      - mysql
      - eureka-server
      - config-server
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/ecommerce_userservice
      - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:9099/eureka

volumes:
  mysql_data:
```

### Run with Docker Compose
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## Kubernetes Deployment

### Prerequisites
- Kubernetes cluster (minikube, EKS, GKE, AKS)
- kubectl configured
- Docker images pushed to registry

### Namespace Setup
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: ecommerce
```

### ConfigMap
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: ecommerce-config
  namespace: ecommerce
data:
  mysql.host: "mysql-service"
  eureka.url: "http://eureka-service:9099/eureka"
  zipkin.url: "http://zipkin-service:9411"
```

### MySQL Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  namespace: ecommerce
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: mysql
        image: mysql:8.0
        env:
        - name: MYSQL_ROOT_PASSWORD
          value: "root"
        ports:
        - containerPort: 3306
        volumeMounts:
        - name: mysql-storage
          mountPath: /var/lib/mysql
      volumes:
      - name: mysql-storage
        persistentVolumeClaim:
          claimName: mysql-pvc
---
apiVersion: v1
kind: Service
metadata:
  name: mysql-service
  namespace: ecommerce
spec:
  selector:
    app: mysql
  ports:
  - port: 3306
    targetPort: 3306
```

### Eureka Server Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: eureka-server
  namespace: ecommerce
spec:
  replicas: 1
  selector:
    matchLabels:
      app: eureka-server
  template:
    metadata:
      labels:
        app: eureka-server
    spec:
      containers:
      - name: eureka-server
        image: ecommerce/eureka-server:latest
        ports:
        - containerPort: 9099
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "kubernetes"
---
apiVersion: v1
kind: Service
metadata:
  name: eureka-service
  namespace: ecommerce
spec:
  selector:
    app: eureka-server
  ports:
  - port: 9099
    targetPort: 9099
```

### User Service Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
  namespace: ecommerce
spec:
  replicas: 2
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
      - name: user-service
        image: ecommerce/user-service:latest
        ports:
        - containerPort: 9050
        env:
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:mysql://mysql-service:3306/ecommerce_userservice"
        - name: EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE
          value: "http://eureka-service:9099/eureka"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 9050
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 9050
          initialDelaySeconds: 30
          periodSeconds: 10
---
apiVersion: v1
kind: Service
metadata:
  name: user-service
  namespace: ecommerce
spec:
  selector:
    app: user-service
  ports:
  - port: 9050
    targetPort: 9050
```

### Deploy to Kubernetes
```bash
# Apply all configurations
kubectl apply -f k8s/

# Check deployment status
kubectl get pods -n ecommerce

# View logs
kubectl logs -f deployment/user-service -n ecommerce

# Port forward for testing
kubectl port-forward service/api-gateway 8080:8080 -n ecommerce
```

## AWS EKS Deployment

### EKS Cluster Setup
```bash
# Install eksctl
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin

# Create EKS cluster
eksctl create cluster \
  --name ecommerce-cluster \
  --region us-west-2 \
  --nodegroup-name standard-workers \
  --node-type t3.medium \
  --nodes 3 \
  --nodes-min 1 \
  --nodes-max 4
```

### RDS Setup
```bash
# Create RDS MySQL instance
aws rds create-db-instance \
  --db-instance-identifier ecommerce-db \
  --db-instance-class db.t3.micro \
  --engine mysql \
  --master-username admin \
  --master-user-password password123 \
  --allocated-storage 20 \
  --vpc-security-group-ids sg-xxxxxxxxx
```

### Application Load Balancer
```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ecommerce-ingress
  namespace: ecommerce
  annotations:
    kubernetes.io/ingress.class: alb
    alb.ingress.kubernetes.io/scheme: internet-facing
    alb.ingress.kubernetes.io/target-type: ip
spec:
  rules:
  - host: api.ecommerce.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: api-gateway
            port:
              number: 8080
```

## Production Considerations

### Environment Variables
```bash
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://prod-db:3306/ecommerce
SPRING_DATASOURCE_USERNAME=${DB_USERNAME}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}

# Security
JWT_SECRET=${JWT_SECRET}
ENCRYPTION_KEY=${ENCRYPTION_KEY}

# External Services
MAIL_HOST=${MAIL_HOST}
MAIL_USERNAME=${MAIL_USERNAME}
MAIL_PASSWORD=${MAIL_PASSWORD}

# Monitoring
ZIPKIN_BASE_URL=${ZIPKIN_URL}
PROMETHEUS_ENDPOINT=${PROMETHEUS_URL}
```

### Security Hardening
- Use secrets management (AWS Secrets Manager, Kubernetes Secrets)
- Enable HTTPS/TLS
- Configure network policies
- Implement pod security policies
- Regular security scanning

### Monitoring Setup
```yaml
# Prometheus ConfigMap
apiVersion: v1
kind: ConfigMap
metadata:
  name: prometheus-config
data:
  prometheus.yml: |
    global:
      scrape_interval: 15s
    scrape_configs:
    - job_name: 'spring-boot'
      metrics_path: '/actuator/prometheus'
      static_configs:
      - targets: ['user-service:9050', 'product-service:9051']
```

### Backup Strategy
- Database automated backups
- Configuration backups
- Application state snapshots
- Disaster recovery procedures

### Scaling Configuration
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: user-service-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: user-service
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

## CI/CD Pipeline

### GitHub Actions
```yaml
name: Deploy to EKS
on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    
    - name: Configure AWS credentials
      uses: aws-actions/configure-aws-credentials@v1
      with:
        aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
        aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        aws-region: us-west-2
    
    - name: Build and push Docker images
      run: |
        docker build -t $ECR_REGISTRY/user-service:$GITHUB_SHA ./UserService
        docker push $ECR_REGISTRY/user-service:$GITHUB_SHA
    
    - name: Deploy to EKS
      run: |
        aws eks update-kubeconfig --name ecommerce-cluster
        kubectl set image deployment/user-service user-service=$ECR_REGISTRY/user-service:$GITHUB_SHA
```

### Health Checks
- Liveness probes for container health
- Readiness probes for traffic routing
- Custom health indicators
- Circuit breaker patterns

### Rollback Strategy
```bash
# Rollback deployment
kubectl rollout undo deployment/user-service

# Check rollout status
kubectl rollout status deployment/user-service

# View rollout history
kubectl rollout history deployment/user-service
```