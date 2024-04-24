--DML File
truncate table Users, MemberAttributes, Achievements, TrainerAvailability, Equipment, Sessions;

-- Insert into Users
INSERT INTO Users (first_name, last_name, password, user_type) VALUES
('Umar', 'Jan', 'umar123', 'Member'),
('Mohammed', 'Yaman', 'yaman123', 'Trainer'),
('Ammar', 'HT', 'ammar123', 'Admin');

-- Insert into MemberAttributes 
INSERT INTO MemberAttributes (id, goal_weight, timeline, goal_workout, height, weight, bf_percentage, routine, payment_status, plan) VALUES
(1, 70.0, '2024-12-31', 'Strength Training', 180, 75, 15, 'lower', 'true', '2');

-- Insert into Achievements 
INSERT INTO Achievements (id, achievements_user) VALUES
(1, 'Completed 20 workouts');

-- Insert into TrainerAvailability
INSERT INTO TrainerAvailability (
    trainer_id, 
    start_time, 
    end_time, 
    start_date, 
    end_date
) VALUES
(2, '09:00', '17:00', '2024-04-12', '2024-04-12');


-- Insert into Equipment
INSERT INTO Equipment ( name, check_update, is_available) VALUES
('Treadmill', CURRENT_TIMESTAMP, TRUE),
('Dumbbells', CURRENT_TIMESTAMP, FALSE),
('Barbell', CURRENT_TIMESTAMP, FALSE),
('Kettlebell', CURRENT_TIMESTAMP, FALSE);
-- Insert into Sessions
INSERT INTO Sessions (
    trainer_id,  
    start_time, 
    end_time, 
    date, 
    is_group, 
    room_number, 
    equipment_id
) VALUES
(2, '09:00', '10:00', '2024-04-12', FALSE, 50, 1),
(2, '11:00', '12:00', '2024-04-13', TRUE, 51, 2);
