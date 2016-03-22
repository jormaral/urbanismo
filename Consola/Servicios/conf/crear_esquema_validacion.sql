CREATE SCHEMA validacion
  AUTHORIZATION postgres;
COMMENT ON SCHEMA validacion IS 'standard public schema';

-- Sequence: validacion.validacion_elemento_sequence

-- DROP SEQUENCE validacion.validacion_elemento_sequence;

CREATE SEQUENCE validacion.validacion_elemento_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE validacion.validacion_elemento_sequence OWNER TO postgres;

-- Sequence: validacion.validacion_error_sequence

-- DROP SEQUENCE validacion.validacion_error_sequence;

CREATE SEQUENCE validacion.validacion_error_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE validacion.validacion_error_sequence OWNER TO postgres;

-- Sequence: validacion.validacion_proceso_sequence

-- DROP SEQUENCE validacion.validacion_proceso_sequence;

CREATE SEQUENCE validacion.validacion_proceso_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE validacion.validacion_proceso_sequence OWNER TO postgres;

-- Sequence: validacion.validacion_sequence

-- DROP SEQUENCE validacion.validacion_sequence;

CREATE SEQUENCE validacion.validacion_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE validacion.validacion_sequence OWNER TO postgres;

-- Table: validacion.validacion

-- DROP TABLE validacion.validacion;

CREATE TABLE validacion.validacion
(
  iden integer NOT NULL DEFAULT nextval('validacion.validacion_sequence'::regclass),
  tipovalidacion integer NOT NULL,
  tipoerror integer,
  activado boolean NOT NULL,
  nemo character varying(10),
  descripcion character varying(3000) NOT NULL,
  sentencia character varying(3000) NOT NULL,
  resultadoesperado character varying(1000) NOT NULL,
  columnas character varying(3000),
  mensaje character varying(3000),
  CONSTRAINT pk_validacion PRIMARY KEY (iden)
)
WITH (OIDS=FALSE);
ALTER TABLE validacion.validacion OWNER TO postgres;

-- Table: validacion.proceso

-- DROP TABLE validacion.proceso;

CREATE TABLE validacion.proceso
(
  iden integer NOT NULL DEFAULT nextval('validacion.validacion_proceso_sequence'::regclass),
  idfip character varying(256) NOT NULL,
  inicio timestamp with time zone NOT NULL,
  fin timestamp with time zone,
  backup character varying(3000),
  consolidado timestamp with time zone,
  CONSTRAINT pk_procesovalidacion PRIMARY KEY (iden)
)
WITH (OIDS=FALSE);
ALTER TABLE validacion.proceso OWNER TO postgres;

-- Table: validacion.resultado

-- DROP TABLE validacion.resultado;

CREATE TABLE validacion.resultado
(
  idproceso integer NOT NULL,
  idvalidacion integer NOT NULL,
  fecha date NOT NULL,
  exito boolean NOT NULL,
  CONSTRAINT pk_resultado PRIMARY KEY (idproceso, idvalidacion),
  CONSTRAINT fk_proceso FOREIGN KEY (idproceso)
      REFERENCES validacion.proceso (iden) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_validacion FOREIGN KEY (idvalidacion)
      REFERENCES validacion.validacion (iden) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE validacion.resultado OWNER TO postgres;

-- Table: validacion.error

-- DROP TABLE validacion.error;

CREATE TABLE validacion.error
(
  iden integer NOT NULL DEFAULT nextval('validacion.validacion_error_sequence'::regclass),
  idproceso integer NOT NULL,
  idvalidacion integer NOT NULL,
  mensaje character varying(1000) NOT NULL,
  CONSTRAINT pk_error PRIMARY KEY (iden),
  CONSTRAINT fk_resultado FOREIGN KEY (idproceso, idvalidacion)
      REFERENCES validacion.resultado (idproceso, idvalidacion) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE validacion.error OWNER TO postgres;
