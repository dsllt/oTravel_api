CREATE TABLE schedules (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  week_day VARCHAR(10) CHECK (week_day IN ('monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday')),
  open_at TIME,
  close_at TIME,
  place_id UUID,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE menus (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200),
  type VARCHAR(10) CHECK (type IN ('food', 'drink')),
  price DECIMAL(8,2),
  place_id UUID,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);