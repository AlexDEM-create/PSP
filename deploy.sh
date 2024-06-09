#!/bin/bash

# Define variables
PROJECT_DIR="/home/admin/tech/flacko-pay"
LOCAL_REGISTRY="localhost:5000"
MICROSERVICES=(
  "user"
  "trader-team"
  "terminal"
  "payment"
  "payment-verification"
  "payment-method"
  "merchant"
  "balance"
  "appeal"
  "currency"
  "receipt-data-extractor"
)

# Function to update code from GitHub
update_code() {
    cd "$PROJECT_DIR" || exit 1
    git pull
}

# Function to start local Docker registry
start_local_registry() {
    docker run -d -p 5000:5000 --name registry registry:2 || true
}

# Function to build Docker images
build_images() {
    for service in "${MICROSERVICES[@]}"; do
        service_dir="$PROJECT_DIR/$service/webapp"
        docker build --no-cache -f "$service_dir/Dockerfile" -t "$service:latest" .
        docker tag "$service:latest" "$LOCAL_REGISTRY/$service:latest"
        docker push "$LOCAL_REGISTRY/$service:latest"
    done
}

# Function to run Flyway migrations
run_flyway_migration() {
    # Assuming you have a Flyway Docker image and your migration scripts are mounted as volumes
    docker run --rm \
        -v "$PROJECT_DIR/migration:/flyway/sql" \
        -e FLYWAY_URL=jdbc:postgresql://185.191.127.194:5432/fp \
        -e FLYWAY_USER=postgres \
        -e FLYWAY_PASSWORD=GFmyjF3eN6d5 \
        flyway/flyway:latest migrate
}

# Function to deploy microservices to Kubernetes
deploy_to_kubernetes() {
    for service in "${MICROSERVICES[@]}"; do
        sudo kubectl apply -f "$PROJECT_DIR/$service/webapp/deployment.yaml" --validate=false
        sudo kubectl apply -f "$PROJECT_DIR/$service/webapp/service.yaml" --validate=false
    done

    # Apply all YAML files in the infra folder
    sudo kubectl apply -f "$PROJECT_DIR/infra" --recursive --validate=false
}

# Main function
main() {
    update_code
    start_local_registry
    build_images
    run_flyway_migration
    deploy_to_kubernetes
}

# Execute main function
main
