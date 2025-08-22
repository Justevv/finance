CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS users_phone_idx ON users (phone,is_phone_confirmed) where is_phone_confirmed = true;
CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS users_email_idx ON users (email,is_email_confirmed) where is_email_confirmed = true;

CREATE INDEX CONCURRENTLY IF NOT EXISTS verification_user_id_idx ON verification (user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS verification_expire_time_idx ON verification (expire_time);

