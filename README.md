# DLQ Event Handler

## Postgres

docker run --name my-postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres

docker exec -it my-postgres psql -U postgres
CREATE DATABASE mydatabase;
List databases -> \l

CREATE TABLE failed_events (
	id serial8 NOT NULL,
	payload text NOT NULL,
	headers text NULL,
	error text NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	CONSTRAINT failed_events_pkey PRIMARY KEY (id)
);

## Liquibase

mvn clean install liquibase:generateChangeLog -DskipTests=true
mvn clean install liquibase:diff -DskipTests=true