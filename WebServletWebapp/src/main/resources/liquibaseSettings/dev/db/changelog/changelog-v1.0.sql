--liquibase formatted sql

--changeset rusikvolc:1

CREATE TABLE IF NOT EXISTS public.patients
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying COLLATE pg_catalog."default" UNIQUE NOT NULL,
    CONSTRAINT patient_id PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.patients
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.doctors
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    type integer DEFAULT 1,
    name character varying COLLATE pg_catalog."default" UNIQUE NOT NULL,
    CONSTRAINT doctor_id PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.doctors
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.prescriptions
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    patient_id integer NOT NULL,
    doctor_id integer NOT NULL,
    CONSTRAINT patient_id_fkey FOREIGN KEY (patient_id)
        REFERENCES public.patients (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT doctor_id_fkey FOREIGN KEY (doctor_id)
        REFERENCES public.doctors (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    UNIQUE (patient_id, doctor_id),
    diagnosis character varying DEFAULT '',
    treatment integer DEFAULT 0
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.prescriptions
    OWNER to postgres;
