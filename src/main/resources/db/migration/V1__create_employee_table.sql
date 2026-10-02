CREATE TABLE employees (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    department      VARCHAR(100) NOT NULL,
    salary          DOUBLE PRECISION NOT NULL,
    date_of_joining DATE NOT NULL
);
