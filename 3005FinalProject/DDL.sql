DROP TABLE IF EXISTS Sessions;
DROP TABLE IF EXISTS equipment;
DROP TABLE IF EXISTS TrainerAvailability;
DROP TABLE IF EXISTS Achievements;
DROP TABLE IF EXISTS MemberAttributes;
DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL
);

CREATE TABLE MemberAttributes (
    id INT PRIMARY KEY,
    goal_weight DECIMAL,
    timeline DATE,
    goal_workout VARCHAR(255),
    height DECIMAL,
    weight DECIMAL,
    bf_percentage DECIMAL,
    routine VARCHAR(255),
    payment_status boolean,
    plan VARCHAR(255),   
    foreign key (id) references Users(id)
);

CREATE TABLE Achievements (
    id INT,
    achievements_user VARCHAR(255),
    foreign key (id) references Users(id)
);

CREATE TABLE TrainerAvailability (
    trainer_id INT PRIMARY KEY,
    start_time TIME,
    end_time TIME,
    start_date DATE,
	end_date DATE,
    foreign key (trainer_id) references Users(id)
);

CREATE TABLE equipment (
    equipment_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    check_update DATE,
    is_available BOOLEAN
);

CREATE TABLE Sessions (
    session_id SERIAL PRIMARY KEY,
    trainer_id INT,
    member_id INT,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    date DATE,
    is_group BOOLEAN,
    room_number INT,
    equipment_id INT,
    foreign key (equipment_id) references equipment,
    foreign key (trainer_id) references Users(id),
    foreign key (member_id) references Users(id)
);