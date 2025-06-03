CREATE TYPE week_days AS ENUM ('monday', 'tuesday', 'wednesday', 'thursday', ' friday', 'saturday', 'sunday');

CREATE TABLE schedules (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  week_day WEEK_DAYS,
  open_at TIMETZ,
  close_at TIMETZ,
  place_id UUID,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TYPE food_type AS ENUM ('food', 'drink');

CREATE TABLE menus (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(200),
  type VARCHAR(10),
  price DECIMAL(8,2),
  place_id UUID,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);