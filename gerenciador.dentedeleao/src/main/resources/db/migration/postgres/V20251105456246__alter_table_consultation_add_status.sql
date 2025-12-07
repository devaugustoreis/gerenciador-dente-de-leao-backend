ALTER TABLE consultations ADD COLUMN status VARCHAR(20) DEFAULT 'scheduled' NOT NULL;
UPDATE consultations SET status = 'COMPLETED' WHERE concluded = TRUE;
ALTER TABLE consultations DROP COLUMN concluded;