# Clase 10 - Store Distribuido

## Hazelcast

### Tecnologías utilizadas
- JDK 25
- Maven 3
- Hazelcast 3.8.6

**Documentación de Hazelcast:** https://docs.hazelcast.org/docs/3.8.6/manual/html-single/index.html

### Build

```shell
./mvnw clean package
```

### Estructura del proyecto

#### api

- Vacío

#### server

- `Server.java`: Main

#### client

- `Client.java`: Main

### Ejecución Server

Después de haber realizado el build

#### Shell

```shell
cd server/target 
```
```shell
tar -xzf hz-store-server-2026.1Q-bin.tar.gz
```
```shell
cd hz-store-server-2026.1Q 
```
```shell
chmod u+x run-server.sh 
```
```shell
sh run-server.sh 
```

### Ejecución Client

#### Shell

```shell
cd client/target 
```
```shell
tar -xzf hz-store-client-2026.1Q-bin.tar.gz 
```
```shell
cd hz-store-client-2026.1Q 
```
```shell
chmod u+x run-client.sh 
```
```shell
sh run-client.sh 
```
