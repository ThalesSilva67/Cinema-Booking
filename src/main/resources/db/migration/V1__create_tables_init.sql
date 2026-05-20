CREATE TYPE roles_enum AS ENUM ('ADMINISTRADOR', 'STANDARD');
CREATE TYPE booking_state AS ENUM ('PENDING', 'APPROVED', 'EXPIRED', 'CANCELED');
CREATE TYPE status_enum AS ENUM ('PENDING', 'APPROVED', 'FAILED', 'REFUNDED', 'CANCELED');

CREATE TABLE Users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     roles_enum   NOT NULL
);

CREATE TABLE Room
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(20) NOT NULL,
    total_rows  INT         NOT NULL,
    seat_per_row INT         NOT NULL
);

CREATE TABLE Movie
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    duration    INT          NOT NULL
);


CREATE TABLE Session
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    movie_id  BIGINT         NOT NULL REFERENCES Movie (id),
    room_id   BIGINT         NOT NULL REFERENCES Room (id),
    start_time TIMESTAMP      NOT NULL,
    price     DECIMAL(10, 2) NOT NULL
);


CREATE TABLE Booking
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    seat_label  VARCHAR(20)    NOT NULL,
    user_id    BIGINT         NOT NULL REFERENCES Users (id),
    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at  TIMESTAMP      NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    session_id BIGINT         NOT NULL REFERENCES Session (id),
    state      booking_state  NOT NULL
);

CREATE TABLE Payment
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    booking_id            BIGINT         NOT NULL REFERENCES Booking (id),
    amount                DECIMAL(10, 2) NOT NULL,
    status                status_enum    NOT NULL,
    payment_method         VARCHAR(50)    NOT NULL,
    external_transaction_id VARCHAR(50)    NOT NULL,
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);