# Use Node.js image as base image
FROM node:20.12.2

# Set the working directory inside the container
WORKDIR /app

# Install build tools for bcrypt (GCC, make, etc.)
RUN apt-get update && apt-get install -y build-essential python3

RUN npm cache clean --force
RUN rm -rf node_modules

# Copy package.json and package-lock.json (or package*.json) to the container
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Expose the backend port (3001, as per your setup)
EXPOSE 3001

# Start the app
CMD ["npm", "run", "build-start"]
