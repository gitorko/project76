# Project 76

Spring Boot - Vault & Property Refresh

[https://gitorko.github.io/stock-exchange/](https://gitorko.github.io/stock-exchange/)

### Version

Check version

```bash
$java --version
openjdk 17.0.3 2022-04-19 LTS

$vault --version
Vault v1.5.0 ('340cc2fa263f6cbd2861b41518da8a62c153e2e7+CHANGES')
```

### Postgres DB

```
docker run -p 5432:5432 --name pg-container -e POSTGRES_PASSWORD=password -d postgres:9.6.10
docker ps
docker exec -it pg-container psql -U postgres -W postgres
CREATE USER test WITH PASSWORD 'test@123';
CREATE DATABASE "test-db" WITH OWNER "test" ENCODING UTF8 TEMPLATE template0;
grant all PRIVILEGES ON DATABASE "test-db" to test;

docker stop pg-container
docker start pg-container
```

## Vault

To install vault on mac run the command, for other OS download and install vault.

```bash
brew install vault
```

Start the dev server

```bash
vault server -dev -log-level=INFO -dev-root-token-id=00000000-0000-0000-0000-000000000000
```

Once vault is up, insert some values

```bash
export VAULT_ADDR=http://localhost:8200
export VAULT_SKIP_VERIFY=true
export VAULT_TOKEN=00000000-0000-0000-0000-000000000000
vault kv put secret/myapp/dev username=test password=test@123 dbname=test-db myKey=foobar featureFlag=true
vault kv put secret/myapp/prod username=test password=test@123 dbname=test-db myKey=fooprod featureFlag=true
```

You can login to vault UI with token '00000000-0000-0000-0000-000000000000'

Vault UI: [http://127.0.0.1:8200/](http://127.0.0.1:8200/)

To update property value

```bash
vault kv patch secret/myapp/dev featureFlag=true
vault kv patch secret/myapp/dev featureFlag=false
```

### Dev

To run the code.

```bash
./gradlew clean build
./gradlew bootRun --args='--spring.profiles.active=dev'
./gradlew bootRun --args='--spring.profiles.active=prod'
```
