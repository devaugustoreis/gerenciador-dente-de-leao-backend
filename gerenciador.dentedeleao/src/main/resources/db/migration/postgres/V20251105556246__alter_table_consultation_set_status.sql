ALTER TABLE consultations ALTER COLUMN status SET DEFAULT 'SCHEDULED';
UPDATE consultations SET status = 'SCHEDULED' WHERE status = 'scheduled';