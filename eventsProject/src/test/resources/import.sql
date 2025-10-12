-- ------------------------------------------------------
-- TABLE EVENTS
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS event (
    id_event INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    date_debut DATE,
    date_fin DATE,
    cout FLOAT
);

-- ------------------------------------------------------
-- TABLE PARTICIPANTS
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS participant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    tache VARCHAR(50)
);

-- ------------------------------------------------------
-- TABLE LOGISTICS
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS logistics (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    reserve BOOLEAN,
    prix_unit FLOAT,
    quantite INT
);

-- ------------------------------------------------------
-- TABLE PARTICIPANT_EVENTS (ManyToMany)
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS participant_events (
    events_id_event INT,
    participant_id INT,
    PRIMARY KEY(events_id_event, participant_id),
    FOREIGN KEY(events_id_event) REFERENCES event(id_event),
    FOREIGN KEY(participant_id) REFERENCES participant(id)
);

-- ------------------------------------------------------
-- TABLE EVENT_LOGISTICS (OneToMany)
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS event_logistics (
    event_id_event INT,
    logistics_id_log INT,
    PRIMARY KEY(event_id_event, logistics_id_log),
    FOREIGN KEY(event_id_event) REFERENCES event(id_event),
    FOREIGN KEY(logistics_id_log) REFERENCES logistics(id_log)
);

-- ------------------------------------------------------
-- INSERT EVENTS
-- ------------------------------------------------------
INSERT INTO event (description, date_debut, date_fin, cout)
VALUES 
  ('Test Event 1', '2025-10-20', '2025-10-21', 100.0),
  ('Test Event 2', '2025-11-01', '2025-11-02', 200.0);

-- ------------------------------------------------------
-- INSERT PARTICIPANTS
-- ------------------------------------------------------
INSERT INTO participant (nom, prenom, tache)
VALUES 
  ('John', 'Doe', 'TACHE1'),
  ('Jane', 'Smith', 'TACHE2'),
  ('Alice', 'Brown', 'TACHE3');

-- ------------------------------------------------------
-- INSERT LOGISTICS
-- ------------------------------------------------------
INSERT INTO logistics (description, reserve, prix_unit, quantite)
VALUES 
  ('Logistique 1', FALSE, 50.0, 10),
  ('Logistique 2', TRUE, 100.0, 5),
  ('Logistique 3', FALSE, 75.0, 20);

-- ------------------------------------------------------
-- RELATION PARTICIPANT <-> EVENT (ManyToMany)
-- ------------------------------------------------------
INSERT INTO participant_events (events_id_event, participant_id)
VALUES 
  (1, 1),
  (1, 2),
  (2, 2),
  (2, 3);

-- ------------------------------------------------------
-- RELATION EVENT <-> LOGISTICS (OneToMany)
-- ------------------------------------------------------
INSERT INTO event_logistics (event_id_event, logistics_id_log)
VALUES 
  (1, 1),
  (1, 2),
  (2, 3);

