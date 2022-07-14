ALTER TABLE usuario ADD COLUMN "valido" BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE usuario ADD COLUMN "data_alteracao" timestamp without time zone NOT NULL;