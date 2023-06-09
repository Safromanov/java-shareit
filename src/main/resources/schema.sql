CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255),
    email   VARCHAR(255) UNIQUE,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS items (
   item_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(255),
   description VARCHAR(255),
   available BOOLEAN NOT NULL,
   owner_id BIGINT REFERENCES users(user_id),
   CONSTRAINT pk_items PRIMARY KEY (item_id)
);

CREATE TABLE IF NOT EXISTS comments (
   comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   commentator_id BIGINT REFERENCES users(user_id),
   text VARCHAR(255),
   item_id BIGINT REFERENCES items (item_id),
   create_time TIMESTAMP,
   CONSTRAINT pk_comments PRIMARY KEY (comment_id)
);

CREATE TABLE IF NOT EXISTS items_comments (
   item_id BIGINT NOT NULL  REFERENCES items(item_id),
   comment_id BIGINT NOT NULL REFERENCES comments(comment_id)
);

CREATE TABLE IF NOT EXISTS booking
(
    booking_id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start          TIMESTAMP WITHOUT TIME ZONE,
    end_booking    TIMESTAMP WITHOUT TIME ZONE,
    item_id        BIGINT REFERENCES items (item_id) ON DELETE CASCADE,
    booker_user_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    status         VARCHAR(255),
    CONSTRAINT pk_booking PRIMARY KEY (booking_id)
);




