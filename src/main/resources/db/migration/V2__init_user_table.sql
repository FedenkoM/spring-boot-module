
CREATE TABLE IF NOT EXISTS users (
	id BIGINT NOT NULL,
	email VARCHAR(255) NOT NULL,
	enabled BOOL NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	CONSTRAINT users_email_unique_constraint UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

