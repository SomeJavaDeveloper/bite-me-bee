CREATE TABLE IF NOT EXISTS jobs (
	id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	closed_at timestamp NULL,
	created_at timestamp NULL,
	is_completed bool NULL,
	note varchar(1000) NULL,
	activity_id int8 NULL,
	hive_id int8 NULL,
	user_id int8 NULL,
	CONSTRAINT pk_jobs_id PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS jobs
    OWNER TO bitemebee;

ALTER TABLE IF EXISTS jobs
    ADD CONSTRAINT fk_job_on_activity FOREIGN KEY (activity_id) REFERENCES activities(id);

ALTER TABLE IF EXISTS jobs
    ADD CONSTRAINT fk_job_on_hive FOREIGN KEY (hive_id) REFERENCES hives(id);

ALTER TABLE IF EXISTS jobs
    ADD CONSTRAINT fk_job_on_user FOREIGN KEY (user_id) REFERENCES users(id);