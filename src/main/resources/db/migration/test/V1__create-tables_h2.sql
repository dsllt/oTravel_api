CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    image VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('ADMIN', 'USER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE places (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image_url VARCHAR(255),
    description VARCHAR(255),
    address VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    latitude DOUBLE,
    longitude DOUBLE,
    slug VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    category VARCHAR(255) NOT NULL,
    rating DOUBLE CHECK (rating >= 0 AND rating <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE reviews (
    id UUID PRIMARY KEY,
    description VARCHAR(65535),
    rating DOUBLE,
    place_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    CONSTRAINT fk_reviews_place FOREIGN KEY (place_id) REFERENCES places(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE favorites (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    place_id UUID NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_favorites_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorites_place FOREIGN KEY (place_id) REFERENCES places(id) ON DELETE CASCADE
);