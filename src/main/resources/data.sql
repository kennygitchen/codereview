DROP TABLE IF EXISTS score;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS reviewer;
DROP TABLE IF EXISTS applicant;
DROP TABLE IF EXISTS `position`;
DROP TABLE IF EXISTS criteria;



CREATE TABLE position
(
    name VARCHAR(120) PRIMARY KEY NOT NULL CHECK (name <> '')
);

CREATE TABLE criteria
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(120) NOT NULL CHECK (name <> ''),
    description VARCHAR(500) NOT NULL CHECK (description <> '')
);

CREATE TABLE reviewer
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(150) NOT NULL CHECK (name <> ''),
    position VARCHAR(120) NOT NULL,
    FOREIGN KEY (position) REFERENCES position (name)
);

CREATE TABLE applicant
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(150) NOT NULL CHECK (name <> ''),
    email          VARCHAR(200) NOT NULL CHECK (email <> ''),
    mobile         VARCHAR(15)  NOT NULL CHECK (mobile <> ''),
    position_apply VARCHAR(120) NOT NULL,
    FOREIGN KEY (position_apply) REFERENCES position (name)
);

CREATE TABLE review
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    applicant_id   INT          NOT NULL,
    assessed_by    INT          NOT NULL,
    feedback       VARCHAR(500) NOT NULL CHECK (feedback <> ''),
    comments       VARCHAR(500) NOT NULL CHECK (comments <> ''),
    date_submitted TIMESTAMP    NOT NULL,
    FOREIGN KEY (applicant_id) REFERENCES applicant (id),
    FOREIGN KEY (assessed_by) REFERENCES reviewer (id)
);

CREATE TABLE score
(
    review_id   INT          NOT NULL,
    criteria_id INT          NOT NULL,
    comments    VARCHAR(500) NOT NULL CHECK (comments <> ''),
    score       DECIMAL      NOT NULL,
    PRIMARY KEY (review_id, criteria_id),
    FOREIGN KEY (review_id) REFERENCES review (id),
    FOREIGN KEY (criteria_id) REFERENCES criteria (id)
);

INSERT INTO position(name)
VALUES ('Graduate');
INSERT INTO position(name)
VALUES ('Associate');
INSERT INTO position(name)
VALUES ('Senior');
INSERT INTO position(name)
VALUES ('Tech Lead');
INSERT INTO position(name)
VALUES ('Principle');

INSERT INTO criteria(name, description)
VALUES ('Requirements', 'Are all requirements been met?');
INSERT INTO criteria(name, description)
VALUES ('Design', 'How the well the application is designed (extendability, simplicity, easy to maintain, etc)? ');
INSERT INTO criteria(name, description)
VALUES ('Maintainability', 'Is code easy to read and maintain?');
INSERT INTO criteria(name, description)
VALUES ('Testing', 'How well the application is covered? Does application has right testing approach?');
INSERT INTO criteria(name, description)
VALUES ('Documentation', 'Does applicant include any documentation? How well the solution/problem is explained?');

INSERT INTO applicant(name, email, mobile, position_apply)
VALUES ('Applicant 1', 'applicant1@gmail.com', '0433333333', 'Senior');
INSERT INTO applicant(name, email, mobile, position_apply)
VALUES ('Applicant 2', 'applicant2@gmail.com', '0434444444', 'Senior');

INSERT INTO reviewer(name, position)
VALUES ('reviewer 1', 'Graduate');
INSERT INTO reviewer(name, position)
VALUES ('reviewer 2', 'Associate');
INSERT INTO reviewer(name, position)
VALUES ('reviewer 3', 'Senior');
INSERT INTO reviewer(name, position)
VALUES ('reviewer 4', 'Tech Lead');
INSERT INTO reviewer(name, position)
VALUES ('reviewer 5', 'Principle');

INSERT INTO review(applicant_id, assessed_by, feedback, comments, date_submitted)
VALUES (1, 1, 'test feedback', 'test comments', '2020-01-01 10:10:10');

INSERT INTO score(review_id, criteria_id, comments, score)
VALUES (1, 1, 'Most fo the requirements are met', 4);
INSERT INTO score(review_id, criteria_id, comments, score)
VALUES (1, 2, 'n/a', 3);
INSERT INTO score(review_id, criteria_id, comments, score)
VALUES (1, 3, 'Simple and easy to read code', 3.5);
INSERT INTO score(review_id, criteria_id, comments, score)
VALUES (1, 4, 'Need more test coverage', 3);
INSERT INTO score(review_id, criteria_id, comments, score)
VALUES (1, 5, 'Has problem/solution statements, but no instruction to run', 3);