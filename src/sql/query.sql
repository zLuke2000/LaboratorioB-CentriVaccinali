/* admin laboratorio */

DROP ROLE IF EXISTS "admin_laboratorioB";

CREATE ROLE "admin_laboratorioB" WITH
  LOGIN
  SUPERUSER
  INHERIT
  CREATEDB
  CREATEROLE
  NOREPLICATION
  ENCRYPTED PASSWORD 'SCRAM-SHA-256$4096:3DzxrHqY3hoyjmWVVmqhLA==$42UArch2yjWv6dzNY7LL2NbxofjmfxTihKDP9BoY/Vk=:n+v8EZzi9M9cplYbjL6gFReqjwhVa8HPUv/ABk2e5XI=';

/* Cittadini_Registrati */

CREATE TABLE public."Cittadini_Registrati" (
  nome            varchar(50) NOT NULL,
  cognome         varchar(50) NOT NULL,
  codice_fiscale  varchar(16) NOT NULL UNIQUE,
  email           varchar(50) NOT NULL UNIQUE,
  userid          varchar(16) NOT NULL UNIQUE,
  password        varchar(32) NOT NULL,
  id_vaccinazione numeric(16, 0) NOT NULL,
  CONSTRAINT Cittadini_Registrati_pkey PRIMARY KEY (id_vaccinazione));

/* IndirizzoCV */

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE public."IndirizzoCV" (
  id_indirizzo  uuid DEFAULT uuid_generate_v4() NOT NULL,
  qualificatore varchar(10) NOT NULL,
  nome          varchar(50) NOT NULL,
  civico        varchar(5) NOT NULL,
  comune        varchar(50) NOT NULL,
  provincia     varchar(2) NOT NULL,
  cap           numeric(5, 0) NOT NULL,
  CONSTRAINT IndirizzoCV_pkey PRIMARY KEY (id_indirizzo));

/* CentriVaccinali */

CREATE TABLE public."CentriVaccinali" (
  nome      varchar(50) NOT NULL,
  indirizzo uuid NOT NULL REFERENCES public."IndirizzoCV" (id_indirizzo),
  tipologia varchar(50) NOT NULL,
  CONSTRAINT CentriVaccinali_pkey PRIMARY KEY (nome),
  CONSTRAINT indirizzo FOREIGN KEY (indirizzo) REFERENCES public."IndirizzoCV" (id_indirizzo));

/* EventiAvversi */

CREATE TABLE public."EventiAvversi" (
  id_vaccinazione numeric(16, 0) NOT NULL,
  evento          varchar(30) NOT NULL,
  severita        numeric(1, 0) NOT NULL,
  note            varchar(256) NOT NULL,
  CONSTRAINT EventiAvversi_pkey PRIMARY KEY (id_vaccinazione, evento),
  CONSTRAINT id_vaccino_fkey FOREIGN KEY (id_vaccinazione) REFERENCES public."Cittadini_Registrati" (id_vaccinazione));

/* vaccinati */

CREATE SCHEMA "tabelle_cv";

CREATE TABLE tabelle_cv."vaccinati" (
  id_vaccinazione BIGSERIAL NOT NULL,
  codice_fiscale  varchar(16) NOT NULL,
  nome_centro     varchar(50),
  CONSTRAINT vaccinati_pkey PRIMARY KEY (id_vaccinazione));

/* vaccinati_nomeCentro */

/* InfoCV */

CREATE VIEW InfoCV AS SELECT "CentriVaccinali".nome AS nome_centro,
    "IndirizzoCV".qualificatore,
    "IndirizzoCV".nome,
    "IndirizzoCV".civico,
    "IndirizzoCV".comune,
    "IndirizzoCV".provincia,
    "IndirizzoCV".cap,
    "CentriVaccinali".tipologia
   FROM ("CentriVaccinali" JOIN "IndirizzoCV" ON (("CentriVaccinali".indirizzo = "IndirizzoCV".id_indirizzo)));
