-- name: create-tables!
CREATE TABLE gear (
    gear_id   varchar(20) NOT NULL PRIMARY KEY CHECK (gear_id <> ''),
    category varchar(100),
    description      varchar(100) NOT NULL CHECK (description <> '')
);
CREATE TABLE location_types (
    location_type_id   varchar(20) NOT NULL PRIMARY KEY CHECK (location_type_id <> ''),
    description        varchar(100)
);

CREATE TABLE desired_gear (
    location_type_id varchar(20) REFERENCES location_types,
    gear_id varchar(20) REFERENCES gear,
    sort_order integer NOT NULL,
    quantity integer NOT NULL CHECK (quantity > 0));

--name: insert-gear!
INSERT INTO gear (gear_id, category, description) values (:gear_id, :category, :description);

--name: update-gear!
UPDATE gear SET
category = :category, description = :description
WHERE gear_id = :gear_id

--name: clear-desired!
DELETE FROM desired_gear WHERE location_type_id = :location_type_id

--name: test-gear
SELECT count(*) from gear WHERE gear_id = :gear_id;

--name: test-location-type
SELECT count(*) from location_types where location_type_id = :location_type_id;

--name: insert-location-type!
INSERT INTO location_types (location_type_id) values (:location_type_id);

--name: insert-desired!
INSERT INTO desired_gear (location_type_id, gear_id, sort_order, quantity) values (:location_type_id, :gear_id, :sort_order, :quantity);

--name: drop-tables!
DROP TABLE IF EXISTS gear;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS desired_gear;
