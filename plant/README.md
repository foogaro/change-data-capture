# coffee-orders

Microservice project to place orders and get updates using Server-Sent Events.

# Build and Run

See the `buildAndRun.sh` script


You can check docker logs using `logs.sh` when you run locally.


## Endpoints

Details on curl and other aspects are covered under `ch2`'s [README](../README.md)
- When running using the provided script the Docker container will use a port binding of 8030 to container's 8080 port.
- This makes the service available under localhost:8030


### All orders
http://localhost:8030/coffee-orders/resources/orders

### A particular order
http://localhost:8030/coffee-orders/resources/orders/1

### The Server-Sent event stream
http://localhost:8030/coffee-orders/resources/order-events