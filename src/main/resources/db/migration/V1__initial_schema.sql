CREATE TABLE app_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    country VARCHAR(80) NOT NULL,
    city VARCHAR(100),
    bio VARCHAR(500),
    adult_confirmed BOOLEAN NOT NULL DEFAULT TRUE,
    accepted_terms BOOLEAN NOT NULL DEFAULT FALSE,
    accepted_privacy_policy BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_app_users_email (email),
    INDEX idx_app_users_country (country)
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role ENUM('CUSTOMER', 'CREATOR', 'ADMIN') NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES app_users (id)
);

CREATE TABLE user_preferences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    email_notifications BOOLEAN NOT NULL DEFAULT TRUE,
    platform_notifications BOOLEAN NOT NULL DEFAULT TRUE,
    marketing_notifications BOOLEAN NOT NULL DEFAULT FALSE,
    profile_visibility ENUM('PUBLIC', 'PRIVATE') NOT NULL DEFAULT 'PUBLIC',
    locale VARCHAR(10) NOT NULL DEFAULT 'es',
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_user_preferences_user FOREIGN KEY (user_id) REFERENCES app_users (id)
);

CREATE TABLE creator_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    display_name VARCHAR(80) NOT NULL UNIQUE,
    headline VARCHAR(120),
    description VARCHAR(2000),
    avatar_url VARCHAR(255),
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    online BOOLEAN NOT NULL DEFAULT FALSE,
    rating DECIMAL(3, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_creator_profiles_user FOREIGN KEY (user_id) REFERENCES app_users (id),
    INDEX idx_creator_display_name (display_name)
);

CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    slug VARCHAR(80) NOT NULL UNIQUE,
    type ENUM('STREAM', 'SERVICE', 'PRODUCT') NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_categories_type (type)
);

CREATE TABLE tags (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    slug VARCHAR(80) NOT NULL UNIQUE,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

CREATE TABLE live_streams (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator_id BIGINT NOT NULL,
    category_id BIGINT,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(2000),
    playback_url VARCHAR(255) NOT NULL,
    cover_image_url VARCHAR(255),
    status ENUM('SCHEDULED', 'LIVE', 'ENDED') NOT NULL DEFAULT 'SCHEDULED',
    viewer_count INT NOT NULL DEFAULT 0,
    access_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    started_at TIMESTAMP(6) NULL,
    ended_at TIMESTAMP(6) NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_live_streams_creator FOREIGN KEY (creator_id) REFERENCES creator_profiles (id),
    CONSTRAINT fk_live_streams_category FOREIGN KEY (category_id) REFERENCES categories (id),
    INDEX idx_live_streams_status (status),
    INDEX idx_live_streams_creator (creator_id)
);

CREATE TABLE live_stream_tags (
    stream_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (stream_id, tag_id),
    CONSTRAINT fk_live_stream_tags_stream FOREIGN KEY (stream_id) REFERENCES live_streams (id),
    CONSTRAINT fk_live_stream_tags_tag FOREIGN KEY (tag_id) REFERENCES tags (id)
);

CREATE TABLE service_offerings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator_id BIGINT NOT NULL,
    category_id BIGINT,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(2000),
    mode ENUM('DIGITAL', 'PRESENTIAL', 'HYBRID') NOT NULL DEFAULT 'DIGITAL',
    price DECIMAL(10, 2) NOT NULL,
    duration_minutes INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    location_label VARCHAR(255),
    meeting_link VARCHAR(255),
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_service_offerings_creator FOREIGN KEY (creator_id) REFERENCES creator_profiles (id),
    CONSTRAINT fk_service_offerings_category FOREIGN KEY (category_id) REFERENCES categories (id),
    INDEX idx_service_offerings_creator (creator_id),
    INDEX idx_service_offerings_active (active)
);

CREATE TABLE service_bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    service_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    creator_id BIGINT NOT NULL,
    scheduled_at TIMESTAMP(6) NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'COMPLETED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    notes VARCHAR(1000),
    internal_notes VARCHAR(1000),
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_service_bookings_service FOREIGN KEY (service_id) REFERENCES service_offerings (id),
    CONSTRAINT fk_service_bookings_customer FOREIGN KEY (customer_id) REFERENCES app_users (id),
    CONSTRAINT fk_service_bookings_creator FOREIGN KEY (creator_id) REFERENCES creator_profiles (id),
    INDEX idx_service_bookings_customer (customer_id),
    INDEX idx_service_bookings_creator (creator_id),
    INDEX idx_service_bookings_status (status)
);

CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator_id BIGINT NOT NULL,
    category_id BIGINT,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(2000),
    image_url VARCHAR(255),
    type ENUM('DIGITAL', 'PHYSICAL') NOT NULL DEFAULT 'DIGITAL',
    price DECIMAL(10, 2) NOT NULL,
    inventory INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_products_creator FOREIGN KEY (creator_id) REFERENCES creator_profiles (id),
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories (id),
    INDEX idx_products_creator (creator_id),
    INDEX idx_products_active (active)
);

CREATE TABLE customer_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    buyer_id BIGINT NOT NULL,
    status ENUM('PENDING', 'PAID', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_customer_orders_buyer FOREIGN KEY (buyer_id) REFERENCES app_users (id),
    INDEX idx_customer_orders_buyer (buyer_id),
    INDEX idx_customer_orders_status (status)
);

CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(150) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES customer_orders (id),
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE payment_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL UNIQUE,
    provider VARCHAR(30) NOT NULL DEFAULT 'SIMULATED',
    status ENUM('PENDING', 'APPROVED', 'DECLINED') NOT NULL DEFAULT 'PENDING',
    amount DECIMAL(10, 2) NOT NULL,
    external_reference VARCHAR(80) NOT NULL UNIQUE,
    paid_at TIMESTAMP(6) NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_payment_transactions_order FOREIGN KEY (order_id) REFERENCES customer_orders (id)
);

CREATE TABLE conversations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    type ENUM('DIRECT', 'SERVICE') NOT NULL DEFAULT 'DIRECT',
    subject VARCHAR(200),
    last_message_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_conversations_last_message_at (last_message_at)
);

CREATE TABLE conversation_participants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_conversation_participants_conversation FOREIGN KEY (conversation_id) REFERENCES conversations (id),
    CONSTRAINT fk_conversation_participants_user FOREIGN KEY (user_id) REFERENCES app_users (id),
    CONSTRAINT uq_conversation_participants UNIQUE (conversation_id, user_id)
);

CREATE TABLE chat_messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content VARCHAR(2000) NOT NULL,
    read_at TIMESTAMP(6) NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_chat_messages_conversation FOREIGN KEY (conversation_id) REFERENCES conversations (id),
    CONSTRAINT fk_chat_messages_sender FOREIGN KEY (sender_id) REFERENCES app_users (id),
    INDEX idx_chat_messages_conversation (conversation_id),
    INDEX idx_chat_messages_sender (sender_id)
);
