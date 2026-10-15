# HA RabbitMQ Cluster & Spring Boot Integration

A capstone project demonstrating a High Availability (HA) RabbitMQ 3-node cluster with classic queue mirroring and Spring Boot multi-module integration.

## Architecture

- **RabbitMQ Cluster (Docker Compose)**:
  - 3 nodes: `rabbitmq1` (5672 / 15672), `rabbitmq2` (5673 / 15673), `rabbitmq3` (5674 / 15674).
  - Common Erlang Cookie: `SECRET_CLUSTER_COOKIE_HA_RABBITMQ`.
  - Classic Peer Discovery: configured via `rabbitmq-cluster/rabbitmq.conf`.
  - HA Policy (`ha-all`): pattern `^ha\.`, configured with `ha-mode: all` and `ha-sync-mode: automatic` (eagerly synchronized across all 3 nodes).
- **Producer (`producer`)**:
  - Spring Boot 3 with Spring AMQP.
  - Automatically declares DirectExchange (`ha.direct.exchange`), durable Queue (`ha.mirrored.queue`), and Binding (`ha.routing.key`).
  - Publishes messages at 10 messages/second (`@Scheduled(fixedRate = 100)`).
  - Configured with cluster failover addresses: `localhost:5672,localhost:5673,localhost:5674`.
- **Listener (`listener`)**:
  - Spring Boot 3 with Spring AMQP.
  - Consumes from `ha.mirrored.queue` using `@RabbitListener`.
  - Logs all incoming messages with payload content and timestamps.
  - Configured with cluster failover addresses: `localhost:5672,localhost:5673,localhost:5674`.

## Running the Project

### 1. Start the RabbitMQ Cluster
```bash
docker compose up -d
```
Verify cluster status:
```bash
docker exec rabbitmq1 rabbitmqctl cluster_status
docker exec rabbitmq1 rabbitmqctl list_policies
```

### 2. Start the Listener
In a terminal:
```bash
./mvnw spring-boot:run -pl listener
```

### 3. Start the Producer
In a second terminal:
```bash
./mvnw spring-boot:run -pl producer
```

### 4. Verify High Availability & Mirroring Failover
While both applications are running, simulate a node failure by stopping `rabbitmq1`:
```bash
docker stop rabbitmq1
```
Observe that:
- RabbitMQ promotes `rabbitmq2` or `rabbitmq3` to the queue master automatically.
- Both Producer and Listener seamlessly reconnect to another node in the cluster without message loss.
- When `rabbitmq1` is restarted (`docker start rabbitmq1`), it automatically rejoins the cluster and resynchronizes the mirrored queue.
