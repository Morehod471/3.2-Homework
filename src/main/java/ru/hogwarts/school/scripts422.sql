CREATE TABLE persons (
    Id SERIAL PRIMARY KEY,
    Name TEXT,
    Age INTEGER,
    Driving_license BOOLEAN,
    car_id INTEGER REFERENCES cars (id)
);

CREATE TABLE cars (
    Id SERIAL PRIMARY KEY,
    Brand TEXT,
    Model TEXT,
    Cost INTEGER
);


