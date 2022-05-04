CREATE TABLE IF NOT EXISTS bee_types (
	id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	is_deleted bool NOT NULL,
	aggression_level float8 NOT NULL,
	cold_resistance float8 NOT NULL,
	description text NOT NULL,
	disease_resistance float8 NOT NULL,
	egg_productivity float8 NOT NULL,
	honey_productivity float8 NOT NULL,
	max_co2 int4 NOT NULL,
	max_humidity int4 NOT NULL,
	max_temperature int4 NOT NULL,
	min_co2 int4 NOT NULL,
	min_humidity int4 NOT NULL,
	min_temperature int4 NOT NULL,
	roiling_level float8 NOT NULL,
	title varchar(255) NOT NULL,
	CONSTRAINT pk_bee_types_id PRIMARY KEY (id),
	CONSTRAINT unique_bee_types_title UNIQUE (title)
);

ALTER TABLE IF EXISTS bee_types
    OWNER TO bitemebee;

INSERT INTO bee_types (is_deleted, aggression_level, cold_resistance, description, disease_resistance, egg_productivity, honey_productivity, max_co2, max_humidity, max_temperature, min_co2, min_humidity, min_temperature, roiling_level, title)
    VALUES (false, 0.9, 0.9, 'Люди разводят медоносных пчёл для получения продуктов пчеловодства: воска, мёда и других.', 0.9, 0.9, 0.9, 640, 83, 34, 310, 75, 27, 0.9, 'Медоносная пчела');
