# Use Node.js image for building the React app
FROM node:20.12.2 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json (or package*.json) to the container
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the React app files
COPY . .

# Build the React app for production
RUN npm run build

# Use nginx image to serve the React app
FROM nginx:alpine

# Copy the build output from the build stage to the nginx directory
COPY --from=build /app/build /usr/share/nginx/html

# Expose the nginx port
EXPOSE 443

# Start nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]
