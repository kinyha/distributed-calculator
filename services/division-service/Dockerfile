FROM rust:1.82 AS builder

WORKDIR /usr/src/app

# Copy manifests first
COPY Cargo.toml Cargo.lock ./

# Create dummy source to cache dependencies
RUN mkdir src && echo "fn main() {}" > src/main.rs && \
    cargo build --release && \
    rm -rf src target/release/deps/division_service*

# Copy real source
COPY src src/

# Build the real binary
RUN cargo build --release

FROM debian:bookworm-slim

RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /usr/src/app/target/release/division-service .

EXPOSE 8084

CMD ["./division-service"]