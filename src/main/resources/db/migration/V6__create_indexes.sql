CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS users_phone_idx ON users (phone) where is_phone_confirmed = true;
CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS users_email_idx ON users (email) where is_email_confirmed = true;

CREATE INDEX CONCURRENTLY IF NOT EXISTS phone_verification_user_id_idx ON phone_verification (user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS email_verification_user_id_idx ON email_verification (user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS phone_verification_expire_time_idx ON phone_verification (expire_time);
CREATE INDEX CONCURRENTLY IF NOT EXISTS email_verification_expire_time_idx ON email_verification (expire_time);