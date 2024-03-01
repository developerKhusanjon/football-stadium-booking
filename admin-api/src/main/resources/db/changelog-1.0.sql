--changelog-file

-- liquibase formatted sql

--changeset ofbadmin:modify and inset-stadoim-tables
DROP TABLE IF EXISTS admin_sessions;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS submissions;
DROP TABLE IF EXISTS question_pair_options;
DROP TABLE IF EXISTS question_pairs;
DROP TABLE IF EXISTS question_options;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS questionnaires;
DROP TABLE IF EXISTS scoring_results;
DROP TABLE IF EXISTS scoring_formulas;

ALTER TABLE stadium_info
    ADD CONSTRAINT fk_stadium_id FOREIGN KEY (stadium_id) REFERENCES stadium (id);

INSERT INTO stadium_image values (1, current_timestamp, current_timestamp, '/compress/stadium1.jpg', 'JPEG', 400.0, 'LANDSCAPE', '/stadium1.jpg', 800.0);
INSERT INTO stadium_image values (2, current_timestamp, current_timestamp, '/compress/stadium2.jpg', 'JPEG', 400.0, 'LANDSCAPE', '/stadium2.jpg', 800.0);
INSERT INTO stadium_image values (3, current_timestamp, current_timestamp, '/compress/stadium3.jpg', 'JPEG', 400.0, 'LANDSCAPE', '/stadium3.jpg', 800.0);
INSERT INTO stadium_image values (4, current_timestamp, current_timestamp, '/compress/stadium4.jpg', 'JPEG', 400.0, 'LANDSCAPE', '/stadium4.jpg', 800.0);
INSERT INTO stadium_image values (5, current_timestamp, current_timestamp, '/compress/stadium5.jpg', 'JPEG', 400.0, 'LANDSCAPE', '/stadium5.jpg', 800.0);
INSERT INTO stadium_image values (6, current_timestamp, current_timestamp, '/compress/stadium6.jpg', 'JPEG', 400.0, 'LANDSCAPE', '/stadium6.jpg', 800.0);
INSERT INTO stadium_image values (7, current_timestamp, current_timestamp, '/compress/stadium7.jpg', 'JPEG', 400.0, 'LANDSCAPE', '/stadium7.jpg', 800.0);
INSERT INTO stadium_image values (8, current_timestamp, current_timestamp, '/compress/stadium8.jpg', 'JPEG', 400.0, 'LANDSCAPE', '/stadium8.jpg', 800.0);

INSERT INTO stadium values (1, current_timestamp, current_timestamp, 'Yunusobod 14 17, Toshkent', 'Clean and new Mini-Field with sitting spaces', 'stadium1@mail.com', 'stadium1', '998901114567', '998911114567', 100.00, 1);
INSERT INTO stadium values (2, current_timestamp, current_timestamp, 'Yunusobod 11 12, Toshkent', 'Clean and new Mini-Field with sitting spaces', 'stadium2@mail.com', 'stadium2', '998902234567', '998912234567', 125.00, 2);
INSERT INTO stadium values (3, current_timestamp, current_timestamp, 'Yunusobod 4 13, Toshkent', 'Clean and new Mini-Field with sitting spaces', 'stadium3@mail.com', 'stadium3', '998901144567', '998911144567', 130.00, 3);
INSERT INTO stadium values (4, current_timestamp, current_timestamp, 'Yunusobod 2 7, Toshkent', 'Clean and new Mini-Field with sitting spaces', 'stadium4@mail.com', 'stadium4', '998901154567', '998911154567', 140.00, 4);
INSERT INTO stadium values (5, current_timestamp, current_timestamp, 'Yunusobod 1 8, Toshkent', 'Clean and new Mini-Field with sitting spaces', 'stadium5@mail.com', 'stadium5', '998901174567', '998911174567', 110.00, 5);
INSERT INTO stadium values (6, current_timestamp, current_timestamp, 'Yunusobod 11 7, Toshkent', 'Clean and new Mini-Field with sitting spaces', 'stadium6@mail.com', 'stadium6', '998901714567', '998911714567', 140.00, 6);
INSERT INTO stadium values (7, current_timestamp, current_timestamp, 'Yunusobod 4 77, Toshkent', 'Clean and new Mini-Field with sitting spaces', 'stadium7@mail.com', 'stadium7', '998901014567', '998911014567', 120.00, 7);
INSERT INTO stadium values (8, current_timestamp, current_timestamp, 'Yunusobod 2 45, Toshkent', 'Clean and new Mini-Field with sitting spaces', 'stadium8@mail.com', 'stadium8', '998900014567', '998910014567', 150.00, 8);

INSERT INTO stadium_info values (1, current_timestamp, current_timestamp, 'Yunusobod-14-17', 1, 41.373604, 69.298188, 'stadium1', 100.00, 1);
INSERT INTO stadium_info values (2, current_timestamp, current_timestamp, 'Yunusobod-11-12', 2, 41.278828, 69.196265, 'stadium2', 125.00, 2);
INSERT INTO stadium_info values (3, current_timestamp, current_timestamp, 'Yunusobod-4-13', 3, 41.284865, 69.218076, 'stadium3', 130.00, 3);
INSERT INTO stadium_info values (4, current_timestamp, current_timestamp, 'Yunusobod-2-7', 4, 41.362715, 69.286330, 'stadium4', 140.00, 4);
INSERT INTO stadium_info values (5, current_timestamp, current_timestamp, 'Yunusobod-1-8', 5, 41.294995, 69.213719, 'stadium5', 110.00, 5);
INSERT INTO stadium_info values (6, current_timestamp, current_timestamp, 'Yunusobod-11-7', 6, 41.406712, 69.411546, 'stadium6', 140.00, 6);
INSERT INTO stadium_info values (7, current_timestamp, current_timestamp, 'Yunusobod-4-77', 7, 41.123809, 69.334327, 'stadium7', 120.00, 7);
INSERT INTO stadium_info values (8, current_timestamp, current_timestamp, 'Yunusobod-2-45', 8, 41.456342, 69.308654, 'stadium8', 150.00, 8);