--liquibase formatted sql

--changeset rusikvolc:1

INSERT into patients (name) VALUES('Patient 1');
INSERT into patients (name) VALUES('Patient 2');
INSERT into patients (name) VALUES('Patient 3.');

INSERT into doctors (type, name) VALUES(1, 'Doctor');
INSERT into doctors (type, name) VALUES(0, 'Nurse');

INSERT into prescriptions(patient_id, doctor_id, diagnosis, treatment) VALUES(1,1,'Sick',2);
INSERT into prescriptions(patient_id, doctor_id, diagnosis, treatment) VALUES(2,2,'Very sick',1);
INSERT into prescriptions(patient_id, doctor_id, diagnosis, treatment) VALUES(3,1,'Really very sick',0);
