# Documentation Index

This directory contains comprehensive technical documentation for the eCommerce Microservices project.

## Documentation Structure

### 📋 [Technical Documentation](TECHNICAL_DOCUMENTATION.md)
Complete technical overview including:
- System architecture and components
- Technology stack and dependencies
- Setup instructions and prerequisites
- Configuration management
- Security implementation
- Database design
- Monitoring and observability

### 🔌 [API Documentation](API_DOCUMENTATION.md)
Detailed API reference covering:
- Authentication endpoints
- User management APIs
- Product service APIs
- Order processing APIs
- Payment service APIs
- Shipping service APIs
- Error handling and status codes

### 🚀 [Deployment Guide](DEPLOYMENT_GUIDE.md)
Comprehensive deployment instructions for:
- Local development setup
- Docker containerization
- Kubernetes deployment
- AWS EKS deployment
- Production considerations
- CI/CD pipeline setup

### 💻 [Development Guide](DEVELOPMENT_GUIDE.md)
Development best practices including:
- Environment setup
- Coding standards and conventions
- Testing strategies
- Database management
- Error handling patterns
- Security implementation
- Development workflow

## Quick Start

1. **Setup**: Follow the [Technical Documentation](TECHNICAL_DOCUMENTATION.md#setup-instructions) for initial setup
2. **Development**: Refer to [Development Guide](DEVELOPMENT_GUIDE.md) for coding standards
3. **API Usage**: Check [API Documentation](API_DOCUMENTATION.md) for endpoint details
4. **Deployment**: Use [Deployment Guide](DEPLOYMENT_GUIDE.md) for production deployment

## Additional Resources

- **Swagger UI**: Available at `http://localhost:{port}/swagger-ui.html` for each service
- **Eureka Dashboard**: `http://localhost:9099`
- **Zipkin Tracing**: `http://localhost:9411`
- **Health Checks**: `http://localhost:{port}/actuator/health`

## Contributing

When contributing to this project:
1. Follow the coding standards in [Development Guide](DEVELOPMENT_GUIDE.md)
2. Update relevant documentation
3. Add/update API documentation for new endpoints
4. Include deployment notes for infrastructure changes

## Support

For technical issues or questions:
1. Check the troubleshooting section in [Technical Documentation](TECHNICAL_DOCUMENTATION.md#troubleshooting)
2. Review the development workflow in [Development Guide](DEVELOPMENT_GUIDE.md)
3. Consult the API documentation for endpoint-specific issues