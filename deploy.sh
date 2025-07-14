#!/bin/bash

echo "Starting deployment to Render..."

# Make sure all Maven wrapper scripts are executable
find . -name "mvnw" -exec chmod +x {} \;

echo "Deployment configuration ready!"
echo "Next steps:"
echo "1. Push your code to GitHub"
echo "2. Connect your GitHub repo to Render"
echo "3. Use the render.yaml file for Blueprint deployment"
echo "4. Set up PostgreSQL database on Render"
echo "5. Configure environment variables"