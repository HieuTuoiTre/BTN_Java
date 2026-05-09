CREATE DATABASE IF NOT EXISTS Scheduler;
USE Scheduler;

CREATE TABLE Appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL
);

CREATE TABLE Reminders (
    reminder_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    reminder_type ENUM('24 hours', '12 hours', '1 hour', '30 minutes', '10 minutes') NOT NULL,
    target_time DATETIME NOT NULL,
    message VARCHAR(255),
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id) ON DELETE CASCADE
);

INSERT INTO Appointments (name, start_time, end_time)
VALUES ('Bảo vệ Đồ án PBL3',  '2026-06-10 07:00:00', '2026-06-10 10:00:00');

INSERT INTO Appointments (name, start_time, end_time)
VALUES ('Họp cố vấn học tập sinh viên',  '2026-05-10 19:00:00', '2026-05-10 20:00:00');

INSERT INTO Reminders (appointment_id, reminder_type, target_time, message)
VALUES (1 , '1 hour', '2026-06-10 06:00:00', 'Chuẩn bị slide bảo vệ!');

