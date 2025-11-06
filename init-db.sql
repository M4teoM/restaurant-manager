CREATE TABLE IF NOT EXISTS restaurants (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS menu_items (
  id SERIAL PRIMARY KEY,
  restaurant_id INTEGER REFERENCES restaurants(id),
  name VARCHAR(255) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reservations (
  id SERIAL PRIMARY KEY,
  restaurant_id INTEGER REFERENCES restaurants(id),
  customer_name VARCHAR(255) NOT NULL,
  party_size INTEGER NOT NULL,
  reservation_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO restaurants (name) VALUES ('La Pizzeria Demo');
INSERT INTO menu_items (restaurant_id, name, price) VALUES
  (1, 'Pizza Margherita', 12.99),
  (1, 'Pasta Carbonara', 10.50),
  (1, 'Tiramisu', 6.99);

SELECT 'Database initialized successfully!' AS status;
