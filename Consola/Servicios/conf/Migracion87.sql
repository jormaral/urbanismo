CREATE SEQUENCE "explotacion"."explotacion_peticiongeom_iden_seq"
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;


CREATE TABLE "explotacion"."peticiongeom" (
  "iden" BIGINT NOT NULL, 
  "geom" "public"."geometry" NOT NULL, 
  "fecha" DATE DEFAULT now() NOT NULL, 
  "codigo" VARCHAR(36) NOT NULL, 
  CONSTRAINT "peticionesgeom_pkey" PRIMARY KEY("iden"), 
  CONSTRAINT "peticiongeom_codigo_key" UNIQUE("codigo")
) WITHOUT OIDS;

ALTER TABLE "explotacion"."peticiongeom"
  ALTER COLUMN "geom" SET STATISTICS 0;

