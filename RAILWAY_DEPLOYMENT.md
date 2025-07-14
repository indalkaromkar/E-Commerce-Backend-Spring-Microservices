# Railway Deployment Guide

## Quick Setup

1. **Push to GitHub** - Ensure your code is in a GitHub repository
2. **Connect to Railway**:
   - Go to [railway.app](https://railway.app)
   - Sign up with GitHub
   - Click "Deploy from GitHub repo"
   - Select your repository

3. **Railway Auto-Detection**:
   - Railway will detect `docker-compose.yml`
   - All services will be deployed automatically
   - Each service gets its own URL

## Environment Variables

Railway automatically sets:
- `SPRING_PROFILES_ACTIVE=railway`
- `PORT` for each service

## Access Your Services

After deployment, Railway provides URLs for each service:
- API Gateway: `https://[project-name]-api-gateway.up.railway.app`
- User Service: `https://[project-name]-user-service.up.railway.app`
- Product Service: `https://[project-name]-product-service.up.railway.app`
- Order Service: `https://[project-name]-order-service.up.railway.app`
- Payment Service: `https://[project-name]-payment-service.up.railway.app`

## Free Tier Limits

- $5 credit per month
- Sleeps after 30 minutes of inactivity
- 500MB RAM per service
- 1GB disk space

## Commands

```bash
# Install Railway CLI (optional)
npm install -g @railway/cli

# Login
railway login

# Deploy
railway up
```