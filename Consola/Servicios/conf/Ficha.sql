-- SQL Manager 2010 for PostgreSQL 4.7.0.8
-- ---------------------------------------
-- Host      : 192.168.252.114
-- Database  : RPM
-- Version   : PostgreSQL 8.3.15, compiled by Visual C++ build 1400



CREATE SCHEMA ficha AUTHORIZATION postgres;
SET check_function_bodies = false;
--
-- Definition for sequence ficha_iden_seq (OID = 21899) : 
--
SET search_path = ficha, pg_catalog;
CREATE SEQUENCE ficha.ficha_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table ficha (OID = 21901) : 
--
CREATE TABLE ficha.ficha (
    iden bigint DEFAULT nextval('ficha_iden_seq'::regclass) NOT NULL,
    nombre character varying(2000) NOT NULL,
    path character varying(2000) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence conjuntogrupo_iden_seq (OID = 21912) : 
--
CREATE SEQUENCE ficha.conjuntogrupo_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table conjuntogrupo (OID = 21914) : 
--
CREATE TABLE ficha.conjuntogrupo (
    iden bigint DEFAULT nextval('conjuntogrupo_iden_seq'::regclass) NOT NULL,
    nombre character varying(255) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence fichaconjuntogrupo_iden_seq (OID = 21930) : 
--
CREATE SEQUENCE ficha.fichaconjuntogrupo_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table fichaconjuntogrupo (OID = 21932) : 
--
CREATE TABLE ficha.fichaconjuntogrupo (
    iden bigint DEFAULT nextval('fichaconjuntogrupo_iden_seq'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    idconjunto bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence conjuntodeterminaciongrupo_iden_seq (OID = 30090) : 
--
CREATE SEQUENCE ficha.conjuntodeterminaciongrupo_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table conjuntodeterminaciongrupo (OID = 30092) : 
--
CREATE TABLE ficha.conjuntodeterminaciongrupo (
    iden bigint DEFAULT nextval('conjuntodeterminaciongrupo_iden_seq'::regclass) NOT NULL,
    idconjunto bigint NOT NULL,
    iddeterminacion bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence conjuntodeterminaciones_iden_seq (OID = 30118) : 
--
CREATE SEQUENCE ficha.conjuntodeterminaciones_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table grupodeterminacion (OID = 30120) : 
--
CREATE TABLE ficha.grupodeterminacion (
    iden bigint DEFAULT nextval('conjuntodeterminaciones_iden_seq'::regclass) NOT NULL,
    nombre character varying(255) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence fichaconjuntodeterminacion_iden_seq (OID = 30150) : 
--
CREATE SEQUENCE ficha.fichaconjuntodeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table fichagrupodeterminacion (OID = 30152) : 
--
CREATE TABLE ficha.fichagrupodeterminacion (
    iden bigint DEFAULT nextval('fichaconjuntodeterminacion_iden_seq'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    idgrupo bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence grupodeterminaciondeterminacion_iden_seq (OID = 30168) : 
--
CREATE SEQUENCE ficha.grupodeterminaciondeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table grupodeterminaciondeterminacion (OID = 30170) : 
--
CREATE TABLE ficha.grupodeterminaciondeterminacion (
    iden bigint DEFAULT nextval('grupodeterminaciondeterminacion_iden_seq'::regclass) NOT NULL,
    idgrupo bigint NOT NULL,
    iddeterminacion bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence determinacionRegimenUso_iden_seq (OID = 30186) : 
--
CREATE SEQUENCE ficha."determinacionRegimenUso_iden_seq"
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table determinacionclasifuso (OID = 30188) : 
--
CREATE TABLE ficha.determinacionclasifuso (
    iden bigint DEFAULT nextval('"determinacionRegimenUso_iden_seq"'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    iddeterminacion bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence determinacionRegimenActo_iden_seq (OID = 30194) : 
--
CREATE SEQUENCE ficha."determinacionRegimenActo_iden_seq"
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table determinacionclasifacto (OID = 30196) : 
--
CREATE TABLE ficha.determinacionclasifacto (
    iden bigint DEFAULT nextval('"determinacionRegimenActo_iden_seq"'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    iddeterminacion bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence regimenuso_iden_seq (OID = 30233) : 
--
CREATE SEQUENCE ficha.regimenuso_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table regimenuso (OID = 30235) : 
--
CREATE TABLE ficha.regimenuso (
    iden bigint DEFAULT nextval('regimenuso_iden_seq'::regclass) NOT NULL,
    iddeterminacionclasifuso bigint NOT NULL,
    iddeterminacionvalorregimen bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence regimenacto_iden_seq (OID = 30241) : 
--
CREATE SEQUENCE ficha.regimenacto_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table regimenacto (OID = 30243) : 
--
CREATE TABLE ficha.regimenacto (
    iden bigint DEFAULT nextval('regimenacto_iden_seq'::regclass) NOT NULL,
    iddeterminacionclasifacto bigint NOT NULL,
    iddeterminacionvalorregimen bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for view regimendirecto (OID = 30269) : 
--
CREATE VIEW ficha.regimendirecto AS
SELECT plan.idambito, ficha.iden, ficha.nombre AS ficha, capa.nombre AS
    capa, detgrupo.apartado AS apartadogrupo, detgrupo.nombre AS grupo,
    entidad.iden AS identidad, entidad.clave, entidad.nombre, gd.nombre AS
    grupodeterminacion, detaplicada.iden AS iddeterminacion,
    detaplicada.apartado AS apartadoaplicada, detaplicada.nombre AS
    determinacionaplicada, edr.iden AS idvalor, edr.valor,
    detvalor.apartado AS apartadovalor, detvalor.nombre AS
    determinacionvalor, detcasoaplicacion.apartado AS
    apartadocasoaplicacion, detcasoaplicacion.nombre AS detcasoaplicacion,
    re.nombre AS regimenespecifico, re.texto AS valorregimenespecifico
FROM ((((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON
    ((ficha.iden = cg.idficha))) LEFT JOIN conjuntogrupo capa ON
    ((cg.idconjunto = capa.iden))) LEFT JOIN conjuntodeterminaciongrupo cdg
    ON ((capa.iden = cdg.idconjunto))) LEFT JOIN planeamiento.determinacion
    detgrupo ON ((cdg.iddeterminacion = detgrupo.iden))) LEFT JOIN
    planeamiento.opciondeterminacion odgrupo ON ((detgrupo.iden =
    odgrupo.iddeterminacionvalorref))) LEFT JOIN
    planeamiento.entidaddeterminacionregimen edrgrupo ON ((odgrupo.iden =
    edrgrupo.idopciondeterminacion))) LEFT JOIN
    planeamiento.casoentidaddeterminacion casogrupo ON ((edrgrupo.idcaso =
    casogrupo.iden))) LEFT JOIN planeamiento.entidaddeterminacion edgrupo
    ON ((casogrupo.identidaddeterminacion = edgrupo.iden))) LEFT JOIN
    planeamiento.entidad ON ((edgrupo.identidad = entidad.iden))) LEFT JOIN
    planeamiento.entidaddeterminacion edaplicaciones ON ((edgrupo.identidad
    = edaplicaciones.identidad))) LEFT JOIN planeamiento.determinacion
    detaplicada ON ((edaplicaciones.iddeterminacion = detaplicada.iden)))
    LEFT JOIN planeamiento.casoentidaddeterminacion caso ON
    ((edaplicaciones.iden = caso.identidaddeterminacion))) LEFT JOIN
    planeamiento.entidaddeterminacionregimen edr ON ((caso.iden =
    edr.idcaso))) LEFT JOIN planeamiento.regimenespecifico re ON ((edr.iden
    = re.identidaddeterminacionregimen))) LEFT JOIN
    planeamiento.opciondeterminacion opcion ON ((edr.idopciondeterminacion
    = opcion.iden))) LEFT JOIN planeamiento.determinacion detvalor ON
    ((opcion.iddeterminacionvalorref = detvalor.iden))) LEFT JOIN
    planeamiento.casoentidaddeterminacion casoaplicacion ON
    ((edr.idcasoaplicacion = casoaplicacion.iden))) LEFT JOIN
    planeamiento.entidaddeterminacion edcasoaplicacion ON
    ((casoaplicacion.identidaddeterminacion = edcasoaplicacion.iden))) LEFT
    JOIN planeamiento.determinacion detcasoaplicacion ON
    ((detcasoaplicacion.iden = edcasoaplicacion.iddeterminacion))) LEFT
    JOIN fichagrupodeterminacion fgd ON ((ficha.iden = fgd.idficha))) LEFT
    JOIN grupodeterminacion gd ON ((fgd.idgrupo = gd.iden))) LEFT JOIN
    grupodeterminaciondeterminacion gdd ON (((gd.iden = gdd.idgrupo) AND
    (gdd.iddeterminacion = detaplicada.iden)))) LEFT JOIN
    planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN
    planeamiento.plan ON ((tramite.idplan = plan.iden)))
WHERE (detaplicada.idcaracter = 2)
ORDER BY cg.orden, cdg.orden, entidad.orden, fgd.orden, gdd.orden,
    detgrupo.orden, re.orden;

--
-- Definition for view uso (OID = 30275) : 
--
CREATE VIEW ficha.uso AS
SELECT plan.idambito, ficha.iden, ficha.nombre AS ficha, capa.nombre AS
    capa, detgrupo.apartado AS apartadogrupo, detgrupo.nombre AS grupo,
    entidad.iden AS identidad, entidad.clave, entidad.nombre,
    detaplicada.iden AS iddeterminacion, detaplicada.apartado AS
    apartadoaplicada, detaplicada.nombre AS determinacionaplicada, edr.iden
    AS idvalor, edr.valor, detvalor.apartado AS apartadovalor,
    detvalor.nombre AS determinacionvalor, detregimen.iden AS
    iddeterminacionregimen, detregimen.apartado AS apartadoregimen,
    detregimen.nombre AS determinacionregimen, re.nombre AS
    regimenespecifico, re.texto AS valorregimenespecifico
FROM (((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON
    ((ficha.iden = cg.idficha))) LEFT JOIN conjuntogrupo capa ON
    ((cg.idconjunto = capa.iden))) LEFT JOIN conjuntodeterminaciongrupo cdg
    ON ((capa.iden = cdg.idconjunto))) LEFT JOIN planeamiento.determinacion
    detgrupo ON ((cdg.iddeterminacion = detgrupo.iden))) LEFT JOIN
    planeamiento.opciondeterminacion odgrupo ON ((detgrupo.iden =
    odgrupo.iddeterminacionvalorref))) LEFT JOIN
    planeamiento.entidaddeterminacionregimen edrgrupo ON ((odgrupo.iden =
    edrgrupo.idopciondeterminacion))) LEFT JOIN
    planeamiento.casoentidaddeterminacion casogrupo ON ((edrgrupo.idcaso =
    casogrupo.iden))) LEFT JOIN planeamiento.entidaddeterminacion edgrupo
    ON ((casogrupo.identidaddeterminacion = edgrupo.iden))) LEFT JOIN
    planeamiento.entidad ON ((edgrupo.identidad = entidad.iden))) LEFT JOIN
    planeamiento.entidaddeterminacion edaplicaciones ON ((edgrupo.identidad
    = edaplicaciones.identidad))) LEFT JOIN planeamiento.determinacion
    detaplicada ON ((edaplicaciones.iddeterminacion = detaplicada.iden)))
    LEFT JOIN planeamiento.casoentidaddeterminacion caso ON
    ((edaplicaciones.iden = caso.identidaddeterminacion))) LEFT JOIN
    planeamiento.entidaddeterminacionregimen edr ON ((caso.iden =
    edr.idcaso))) LEFT JOIN planeamiento.regimenespecifico re ON ((edr.iden
    = re.identidaddeterminacionregimen))) LEFT JOIN
    planeamiento.opciondeterminacion opcion ON ((edr.idopciondeterminacion
    = opcion.iden))) LEFT JOIN planeamiento.determinacion detvalor ON
    ((opcion.iddeterminacionvalorref = detvalor.iden))) LEFT JOIN
    planeamiento.determinacion detregimen ON ((edr.iddeterminacionregimen =
    detregimen.iden))) LEFT JOIN planeamiento.tramite ON
    ((entidad.idtramite = tramite.iden))) LEFT JOIN planeamiento.plan ON
    ((tramite.idplan = plan.iden)))
WHERE (detaplicada.idcaracter = 9)
ORDER BY cg.orden, cdg.orden, entidad.orden, detgrupo.orden, re.orden;

--
-- Definition for view acto (OID = 30280) : 
--
CREATE VIEW ficha.acto AS
SELECT plan.idambito, ficha.iden, ficha.nombre AS ficha, capa.nombre AS
    capa, detgrupo.apartado AS apartadogrupo, detgrupo.nombre AS grupo,
    entidad.iden AS identidad, entidad.clave, entidad.nombre,
    detaplicada.iden AS iddeterminacion, detaplicada.apartado AS
    apartadoaplicada, detaplicada.nombre AS determinacionaplicada, edr.iden
    AS idvalor, edr.valor, detvalor.apartado AS apartadovalor,
    detvalor.nombre AS determinacionvalor, detregimen.iden AS
    iddeterminacionregimen, detregimen.apartado AS apartadoregimen,
    detregimen.nombre AS determinacionregimen, re.nombre AS
    regimenespecifico, re.texto AS valorregimenespecifico
FROM (((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON
    ((ficha.iden = cg.idficha))) LEFT JOIN conjuntogrupo capa ON
    ((cg.idconjunto = capa.iden))) LEFT JOIN conjuntodeterminaciongrupo cdg
    ON ((capa.iden = cdg.idconjunto))) LEFT JOIN planeamiento.determinacion
    detgrupo ON ((cdg.iddeterminacion = detgrupo.iden))) LEFT JOIN
    planeamiento.opciondeterminacion odgrupo ON ((detgrupo.iden =
    odgrupo.iddeterminacionvalorref))) LEFT JOIN
    planeamiento.entidaddeterminacionregimen edrgrupo ON ((odgrupo.iden =
    edrgrupo.idopciondeterminacion))) LEFT JOIN
    planeamiento.casoentidaddeterminacion casogrupo ON ((edrgrupo.idcaso =
    casogrupo.iden))) LEFT JOIN planeamiento.entidaddeterminacion edgrupo
    ON ((casogrupo.identidaddeterminacion = edgrupo.iden))) LEFT JOIN
    planeamiento.entidad ON ((edgrupo.identidad = entidad.iden))) LEFT JOIN
    planeamiento.entidaddeterminacion edaplicaciones ON ((edgrupo.identidad
    = edaplicaciones.identidad))) LEFT JOIN planeamiento.determinacion
    detaplicada ON ((edaplicaciones.iddeterminacion = detaplicada.iden)))
    LEFT JOIN planeamiento.casoentidaddeterminacion caso ON
    ((edaplicaciones.iden = caso.identidaddeterminacion))) LEFT JOIN
    planeamiento.entidaddeterminacionregimen edr ON ((caso.iden =
    edr.idcaso))) LEFT JOIN planeamiento.regimenespecifico re ON ((edr.iden
    = re.identidaddeterminacionregimen))) LEFT JOIN
    planeamiento.opciondeterminacion opcion ON ((edr.idopciondeterminacion
    = opcion.iden))) LEFT JOIN planeamiento.determinacion detvalor ON
    ((opcion.iddeterminacionvalorref = detvalor.iden))) LEFT JOIN
    planeamiento.determinacion detregimen ON ((edr.iddeterminacionregimen =
    detregimen.iden))) LEFT JOIN planeamiento.tramite ON
    ((entidad.idtramite = tramite.iden))) LEFT JOIN planeamiento.plan ON
    ((tramite.idplan = plan.iden)))
WHERE (detaplicada.idcaracter = 10)
ORDER BY cg.orden, cdg.orden, entidad.orden, detgrupo.orden, re.orden;

--
-- Definition for index ficha_pkey (OID = 21908) : 
--
ALTER TABLE ONLY ficha
    ADD CONSTRAINT ficha_pkey PRIMARY KEY (iden);
--
-- Definition for index ficha_nombre_key (OID = 21910) : 
--
ALTER TABLE ONLY ficha
    ADD CONSTRAINT ficha_nombre_key UNIQUE (nombre);
--
-- Definition for index conjuntogrupo_pkey (OID = 21918) : 
--
ALTER TABLE ONLY conjuntogrupo
    ADD CONSTRAINT conjuntogrupo_pkey PRIMARY KEY (iden);
--
-- Definition for index fichaconjuntogrupo_pkey (OID = 21936) : 
--
ALTER TABLE ONLY fichaconjuntogrupo
    ADD CONSTRAINT fichaconjuntogrupo_pkey PRIMARY KEY (iden);
--
-- Definition for index fichaconjuntogrupo_fk1 (OID = 21943) : 
--
ALTER TABLE ONLY fichaconjuntogrupo
    ADD CONSTRAINT fichaconjuntogrupo_fk1 FOREIGN KEY (idconjunto) REFERENCES conjuntogrupo(iden) ON DELETE RESTRICT;
--
-- Definition for index fichaconjuntogrupo_fk (OID = 21948) : 
--
ALTER TABLE ONLY fichaconjuntogrupo
    ADD CONSTRAINT fichaconjuntogrupo_fk FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;
--
-- Definition for index conjuntodeterminaciongrupo_pkey (OID = 30096) : 
--
ALTER TABLE ONLY conjuntodeterminaciongrupo
    ADD CONSTRAINT conjuntodeterminaciongrupo_pkey PRIMARY KEY (iden);
--
-- Definition for index conjuntodeterminaciongrupo_fk (OID = 30098) : 
--
ALTER TABLE ONLY conjuntodeterminaciongrupo
    ADD CONSTRAINT conjuntodeterminaciongrupo_fk FOREIGN KEY (idconjunto) REFERENCES conjuntogrupo(iden) ON DELETE CASCADE;
--
-- Definition for index conjuntodeterminaciongrupo_fk1 (OID = 30103) : 
--
ALTER TABLE ONLY conjuntodeterminaciongrupo
    ADD CONSTRAINT conjuntodeterminaciongrupo_fk1 FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index conjuntodeterminaciones_pkey (OID = 30124) : 
--
ALTER TABLE ONLY grupodeterminacion
    ADD CONSTRAINT conjuntodeterminaciones_pkey PRIMARY KEY (iden);
--
-- Definition for index fichaconjuntodeterminacion_pkey (OID = 30156) : 
--
ALTER TABLE ONLY fichagrupodeterminacion
    ADD CONSTRAINT fichaconjuntodeterminacion_pkey PRIMARY KEY (iden);
--
-- Definition for index fichaconjuntodeterminacion_fk (OID = 30158) : 
--
ALTER TABLE ONLY fichagrupodeterminacion
    ADD CONSTRAINT fichaconjuntodeterminacion_fk FOREIGN KEY (idgrupo) REFERENCES grupodeterminacion(iden) ON DELETE RESTRICT;
--
-- Definition for index fichaconjuntodeterminacion_fk1 (OID = 30163) : 
--
ALTER TABLE ONLY fichagrupodeterminacion
    ADD CONSTRAINT fichaconjuntodeterminacion_fk1 FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE RESTRICT;
--
-- Definition for index grupodeterminaciondeterminacion_pkey (OID = 30174) : 
--
ALTER TABLE ONLY grupodeterminaciondeterminacion
    ADD CONSTRAINT grupodeterminaciondeterminacion_pkey PRIMARY KEY (iden);
--
-- Definition for index grupodeterminaciondeterminacion_fk (OID = 30176) : 
--
ALTER TABLE ONLY grupodeterminaciondeterminacion
    ADD CONSTRAINT grupodeterminaciondeterminacion_fk FOREIGN KEY (idgrupo) REFERENCES grupodeterminacion(iden) ON DELETE CASCADE;
--
-- Definition for index grupodeterminaciondeterminacion_fk1 (OID = 30181) : 
--
ALTER TABLE ONLY grupodeterminaciondeterminacion
    ADD CONSTRAINT grupodeterminaciondeterminacion_fk1 FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index determinacionRegimenUso_pkey (OID = 30192) : 
--
ALTER TABLE ONLY determinacionclasifuso
    ADD CONSTRAINT "determinacionRegimenUso_pkey" PRIMARY KEY (iden);
--
-- Definition for index determinacionRegimenActo_pkey (OID = 30200) : 
--
ALTER TABLE ONLY determinacionclasifacto
    ADD CONSTRAINT "determinacionRegimenActo_pkey" PRIMARY KEY (iden);
--
-- Definition for index determinacionRegimenActo_fk (OID = 30202) : 
--
ALTER TABLE ONLY determinacionclasifacto
    ADD CONSTRAINT "determinacionRegimenActo_fk" FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index determinacionRegimenUso_fk (OID = 30207) : 
--
ALTER TABLE ONLY determinacionclasifuso
    ADD CONSTRAINT "determinacionRegimenUso_fk" FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index determinacionRegimenActo_fk1 (OID = 30212) : 
--
ALTER TABLE ONLY determinacionclasifacto
    ADD CONSTRAINT "determinacionRegimenActo_fk1" FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;
--
-- Definition for index determinacionRegimenUso_fk1 (OID = 30217) : 
--
ALTER TABLE ONLY determinacionclasifuso
    ADD CONSTRAINT "determinacionRegimenUso_fk1" FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;
--
-- Definition for index regimenuso_pkey (OID = 30239) : 
--
ALTER TABLE ONLY regimenuso
    ADD CONSTRAINT regimenuso_pkey PRIMARY KEY (iden);
--
-- Definition for index regimenacto_pkey (OID = 30247) : 
--
ALTER TABLE ONLY regimenacto
    ADD CONSTRAINT regimenacto_pkey PRIMARY KEY (iden);
--
-- Definition for index regimenuso_fk (OID = 30249) : 
--
ALTER TABLE ONLY regimenuso
    ADD CONSTRAINT regimenuso_fk FOREIGN KEY (iddeterminacionclasifuso) REFERENCES determinacionclasifuso(iden) ON DELETE CASCADE;
--
-- Definition for index regimenacto_fk (OID = 30254) : 
--
ALTER TABLE ONLY regimenacto
    ADD CONSTRAINT regimenacto_fk FOREIGN KEY (iddeterminacionclasifacto) REFERENCES determinacionclasifacto(iden) ON DELETE CASCADE;
--
-- Definition for index regimenuso_fk1 (OID = 30259) : 
--
ALTER TABLE ONLY regimenuso
    ADD CONSTRAINT regimenuso_fk1 FOREIGN KEY (iddeterminacionvalorregimen) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index regimenacto_fk1 (OID = 30264) : 
--
ALTER TABLE ONLY regimenacto
    ADD CONSTRAINT regimenacto_fk1 FOREIGN KEY (iddeterminacionvalorregimen) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
