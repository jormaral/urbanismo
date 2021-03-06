-- SQL Manager 2011 for PostgreSQL 5.0.0.3
-- ---------------------------------------
-- Host      : 10.4.152.235
-- Database  : RPM_BURGOS_PUB
-- Version   : PostgreSQL 8.3.8 on x86_64-unknown-linux-gnu, compiled by GCC gcc (GCC) 3.4.6 20060404 (Red Hat 3.4.6-10)



CREATE SCHEMA ficha AUTHORIZATION postgres;
SET check_function_bodies = false;
--
-- Definition for function getXML (OID = 23133) : 
--
SET search_path = ficha, pg_catalog;
CREATE FUNCTION ficha."getXML" (
  ids_entidades integer[],
  id_ficha integer
)
RETURNS text
AS 
$body$
DECLARE
  idEntidadesOrdenadas refcursor;
  ret XML;
  ent XML;
  idEnt Integer;
  idCapa Integer;
  registro ficha.v_entidadficha%ROWTYPE;
  nombreEntidad Text;
  nombreCapa Text;
  nombreFicha Text;
  clave Text;
BEGIN
  OPEN idEntidadesOrdenadas FOR SELECT * from ficha.v_entidadficha where identidad = any (ids_entidades);
  LOOP
    FETCH idEntidadesOrdenadas INTO registro;
    IF NOT FOUND THEN 
    	EXIT; 
    END IF; 
    idEnt=registro.identidad;
    idCapa=registro.idcapa;
    nombreCapa=registro.capa;
    nombreEntidad=registro.nombre;
    nombreFicha=registro.ficha;
    clave=registro.clave;
	ent=xmlconcat(ent,xmlelement(name entidad,xmlattributes(nombreEntidad as nombre,idEnt as iden,clave as claveEnt,nombreCapa as capa,idCapa as idencapa),
			xmlconcat(
              xmlelement (name conjunto,xmlattributes('RD' as tipo),
                  xmlelement (name row),
                  query_to_xml('select * from ficha.v_regimendirecto where iden=' || id_ficha::text || ' and identidad =' || idEnt::text || ' and idcapa =' || idCapa::text,true,true,'')
              ),
              xmlelement (name conjunto,xmlattributes('USO' as tipo),
              	  xmlelement (name row),
                  query_to_xml('select * from ficha.v_uso where iden=' || id_ficha::text || ' and identidad =' || idEnt::text || ' and idcapa =' || idCapa::text,true,true,'')
              ),     
              xmlelement (name conjunto,xmlattributes('ACTO' as tipo),  
            	  xmlelement (name row),       
                  query_to_xml('select * from ficha.v_acto where iden=' || id_ficha::text || ' and identidad =' || idEnt::text || ' and idcapa =' || idCapa::text,true,true,'')
              )
            )
    	)
  	);
  END LOOP;
  ret=xmlelement(name entidades,xmlattributes(nombreFicha as ficha),ent);
  RETURN xmlserialize(DOCUMENT ret AS text);
EXCEPTION
WHEN division_by_zero  THEN
  RAISE NOTICE 'caught division_by_zero';
            RETURN null;
END;
$body$
    LANGUAGE plpgsql;
--
-- Definition for sequence ficha_iden_seq (OID = 22823) : 
--
CREATE SEQUENCE ficha.ficha_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table ficha (OID = 22825) : 
--
CREATE TABLE ficha.ficha (
    iden bigint DEFAULT nextval('ficha_iden_seq'::regclass) NOT NULL,
    nombre varchar(2000) NOT NULL,
    path varchar(2000) NOT NULL,
    idtramite bigint NOT NULL,
    xpath text NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence conjuntogrupo_iden_seq (OID = 22832) : 
--
CREATE SEQUENCE ficha.conjuntogrupo_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table conjuntogrupo (OID = 22834) : 
--
CREATE TABLE ficha.conjuntogrupo (
    iden bigint DEFAULT nextval('conjuntogrupo_iden_seq'::regclass) NOT NULL,
    nombre varchar(255) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence fichaconjuntogrupo_iden_seq (OID = 22838) : 
--
CREATE SEQUENCE ficha.fichaconjuntogrupo_iden_seq
    START WITH 12
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table fichaconjuntogrupo (OID = 22840) : 
--
CREATE TABLE ficha.fichaconjuntogrupo (
    iden bigint DEFAULT nextval('fichaconjuntogrupo_iden_seq'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    idconjunto bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence conjuntodeterminaciongrupo_iden_seq (OID = 22844) : 
--
CREATE SEQUENCE ficha.conjuntodeterminaciongrupo_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table conjuntodeterminaciongrupo (OID = 22846) : 
--
CREATE TABLE ficha.conjuntodeterminaciongrupo (
    iden bigint DEFAULT nextval('conjuntodeterminaciongrupo_iden_seq'::regclass) NOT NULL,
    idconjunto bigint NOT NULL,
    iddeterminacion bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence conjuntodeterminaciones_iden_seq (OID = 22850) : 
--
CREATE SEQUENCE ficha.conjuntodeterminaciones_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table grupodeterminacion (OID = 22852) : 
--
CREATE TABLE ficha.grupodeterminacion (
    iden bigint DEFAULT nextval('conjuntodeterminaciones_iden_seq'::regclass) NOT NULL,
    nombre varchar(255) NOT NULL,
    idconjuntogrupo bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence fichaconjuntodeterminacion_iden_seq (OID = 22856) : 
--
CREATE SEQUENCE ficha.fichaconjuntodeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table fichagrupodeterminacion (OID = 22858) : 
--
CREATE TABLE ficha.fichagrupodeterminacion (
    iden bigint DEFAULT nextval('fichaconjuntodeterminacion_iden_seq'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    idgrupo bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence grupodeterminaciondeterminacion_iden_seq (OID = 22862) : 
--
CREATE SEQUENCE ficha.grupodeterminaciondeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table grupodeterminaciondeterminacion (OID = 22864) : 
--
CREATE TABLE ficha.grupodeterminaciondeterminacion (
    iden bigint DEFAULT nextval('grupodeterminaciondeterminacion_iden_seq'::regclass) NOT NULL,
    idgrupo bigint NOT NULL,
    iddeterminacion bigint NOT NULL,
    orden bigint NOT NULL,
    regimenesp boolean DEFAULT true NOT NULL,
    sinvalor boolean DEFAULT false NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence determinacionRegimenUso_iden_seq (OID = 22870) : 
--
CREATE SEQUENCE ficha."determinacionRegimenUso_iden_seq"
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table determinacionclasifuso (OID = 22872) : 
--
CREATE TABLE ficha.determinacionclasifuso (
    iden bigint DEFAULT nextval('"determinacionRegimenUso_iden_seq"'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    iddeterminacion bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence determinacionRegimenActo_iden_seq (OID = 22876) : 
--
CREATE SEQUENCE ficha."determinacionRegimenActo_iden_seq"
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table determinacionclasifacto (OID = 22878) : 
--
CREATE TABLE ficha.determinacionclasifacto (
    iden bigint DEFAULT nextval('"determinacionRegimenActo_iden_seq"'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    iddeterminacion bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence regimenuso_iden_seq (OID = 22882) : 
--
CREATE SEQUENCE ficha.regimenuso_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table valoresclasifuso (OID = 22884) : 
--
CREATE TABLE ficha.valoresclasifuso (
    iden bigint DEFAULT nextval('regimenuso_iden_seq'::regclass) NOT NULL,
    iddeterminacionclasifuso bigint NOT NULL,
    iddeterminacionvalorregimen bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence regimenacto_iden_seq (OID = 22888) : 
--
CREATE SEQUENCE ficha.regimenacto_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table valoresclasifacto (OID = 22890) : 
--
CREATE TABLE ficha.valoresclasifacto (
    iden bigint DEFAULT nextval('regimenacto_iden_seq'::regclass) NOT NULL,
    iddeterminacionclasifacto bigint NOT NULL,
    iddeterminacionvalorregimen bigint NOT NULL,
    orden bigint NOT NULL
) WITHOUT OIDS;
--
-- Structure for table regimenesuso (OID = 22904) : 
--
CREATE TABLE ficha.regimenesuso (
    iden bigint DEFAULT nextval('"determinacionRegimenUso_iden_seq"'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    iddeterminacion bigint NOT NULL
) WITHOUT OIDS;
--
-- Structure for table regimenesacto (OID = 22908) : 
--
CREATE TABLE ficha.regimenesacto (
    iden bigint DEFAULT nextval('"determinacionRegimenUso_iden_seq"'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    iddeterminacion bigint NOT NULL
) WITHOUT OIDS;
--
-- Definition for view v_orden_usos (OID = 22912) : 
--
CREATE VIEW ficha.v_orden_usos AS
SELECT DISTINCT plan.idambito, ficha.iden, entidad.iden AS identidad,
    detaplicada.iden AS iddeterminacion, cg.orden AS orden_conjuntogrupo,
    cdg.orden AS orden_conjuntodetgrupo, entidad.orden AS orden_entidad,
    entidad.iden AS orden_identidad, detgrupo.orden AS orden_grupo,
    vcu.orden AS orden_valordetclasif, detaplicada.orden AS
    orden_detaplicada, detaplicada.iden AS orden_iddetaplicada, re.orden AS
    orden_regimenesp
FROM ((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON
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
    planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN
    planeamiento.plan ON ((tramite.idplan = plan.iden))) LEFT JOIN
    determinacionclasifuso dcu ON ((ficha.iden = dcu.idficha))) LEFT JOIN
    planeamiento.determinacion detclasif ON ((dcu.iddeterminacion =
    detclasif.iden))) LEFT JOIN valoresclasifuso vcu ON (((dcu.iden =
    vcu.iddeterminacionclasifuso) AND (detvalor.iden =
    vcu.iddeterminacionvalorregimen)))) JOIN planeamiento.determinacion
    detregimen ON (((edr.iddeterminacionregimen = detregimen.iden) AND
    (detvalor.iden = vcu.iddeterminacionvalorregimen))))
WHERE (vcu.iden IS NOT NULL)
ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden,
    vcu.orden, detaplicada.orden, detaplicada.iden, re.orden,
    plan.idambito, ficha.iden, entidad.iden, detaplicada.iden;

--
-- Definition for view v_uso (OID = 23095) : 
--
CREATE VIEW ficha.v_uso AS
SELECT DISTINCT plan.idambito, ficha.iden, ficha.nombre AS ficha, capa.iden
    AS idcapa, capa.nombre AS capa, detgrupo.apartado AS apartadogrupo,
    detgrupo.nombre AS grupo, entidad.iden AS identidad, entidad.clave,
    entidad.nombre, detaplicada.iden AS iddeterminacion,
    detaplicada.apartado AS apartadoaplicada, detaplicada.nombre AS
    determinacionaplicada, edr.iden AS idvalor, CASE WHEN
    ((edr.valor)::text = ''::text) THEN NULL::character varying ELSE
    edr.valor END AS valor, detvalor.iden AS iddetvalor, detvalor.apartado
    AS apartadovalor, detvalor.nombre AS determinacionvalor,
    detregimen.iden AS iddeterminacionregimen, detregimen.apartado AS
    apartadoregimen, detregimen.nombre AS determinacionregimen, re.iden AS
    idregimenespecifico, re.nombre AS regimenespecifico, re.texto AS
    valorregimenespecifico, detclasif.iden AS iddetclasif,
    detclasif.apartado AS apartadodetclasif, detclasif.nombre AS
    determinacionclasif, vcu.iden AS id_valordetclasif, cg.orden AS
    orden_conjuntogrupo, cdg.orden AS orden_conjuntodetgrupo, entidad.orden
    AS orden_entidad, entidad.iden AS orden_identidad, detgrupo.orden AS
    orden_grupo, CASE WHEN (vcu.iden IS NULL) THEN (
    SELECT orden_usos.orden_valordetclasif
    FROM v_orden_usos orden_usos
    WHERE ((orden_usos.identidad = entidad.iden) AND
        (orden_usos.iddeterminacion = detaplicada.iden))
    LIMIT 1
    ) ELSE vcu.orden END AS orden_valordetclasif, detaplicada.orden AS
        orden_detaplicada, detaplicada.iden AS orden_iddetaplicada,
        re.orden AS orden_regimenesp
FROM (((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON
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
    planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN
    planeamiento.plan ON ((tramite.idplan = plan.iden))) LEFT JOIN
    determinacionclasifuso dcu ON (((ficha.iden = dcu.idficha) AND
    (dcu.iddeterminacion IN (
    SELECT edrfiltro.iddeterminacionregimen
    FROM planeamiento.entidaddeterminacionregimen edrfiltro
    WHERE (edrfiltro.idcaso IN (
        SELECT casofiltro.iden
        FROM planeamiento.casoentidaddeterminacion casofiltro
        WHERE (casofiltro.identidaddeterminacion IN (
            SELECT edfiltro.iden
            FROM planeamiento.entidaddeterminacion edfiltro
            WHERE (edfiltro.identidad = entidad.iden)
            ))
        ))
    ))))) LEFT JOIN planeamiento.determinacion detclasif ON
        ((dcu.iddeterminacion = detclasif.iden))) LEFT JOIN
        valoresclasifuso vcu ON (((dcu.iden = vcu.iddeterminacionclasifuso)
        AND (detvalor.iden = vcu.iddeterminacionvalorregimen)))) LEFT JOIN
        regimenesuso ru ON ((ficha.iden = ru.idficha))) JOIN
        planeamiento.determinacion detregimen ON
        (((edr.iddeterminacionregimen = detregimen.iden) AND
        ((ru.iddeterminacion = detregimen.iden) OR (detvalor.iden =
        vcu.iddeterminacionvalorregimen)))))
WHERE ((detaplicada.idcaracter = 9) AND (NOT (capa.iden IN (
    SELECT grupodeterminacion.idconjuntogrupo
    FROM grupodeterminacion
    ))))
ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden,
    CASE WHEN (vcu.iden IS NULL) THEN (
    SELECT orden_usos.orden_valordetclasif
    FROM v_orden_usos orden_usos
    WHERE ((orden_usos.identidad = entidad.iden) AND
        (orden_usos.iddeterminacion = detaplicada.iden))
    LIMIT 1
    ) ELSE vcu.orden END, detaplicada.orden, detaplicada.iden, re.orden,
        plan.idambito, ficha.iden, ficha.nombre, capa.iden, capa.nombre,
        detgrupo.apartado, detgrupo.nombre, entidad.clave, entidad.nombre,
        detaplicada.apartado, detaplicada.nombre, edr.iden, CASE WHEN
        ((edr.valor)::text = ''::text) THEN NULL::character varying ELSE
        edr.valor END, detvalor.iden, detvalor.apartado, detvalor.nombre,
        detregimen.iden, detregimen.apartado, detregimen.nombre, re.iden,
        re.nombre, re.texto, detclasif.iden, detclasif.apartado,
        detclasif.nombre, vcu.iden, entidad.iden, detaplicada.iden;

--
-- Definition for view v_entidadficha (OID = 23122) : 
--
CREATE VIEW ficha.v_entidadficha AS
SELECT plan.idambito, ficha.iden AS idficha, ficha.nombre AS ficha,
    capa.iden AS idcapa, capa.nombre AS capa, detgrupo.apartado AS
    apartadogrupo, detgrupo.nombre AS grupo, entidad.iden AS identidad,
    entidad.clave, entidad.nombre, cg.orden AS orden1, cdg.orden AS orden2,
    entidad.orden AS orden3, entidad.iden AS orden4, detgrupo.orden AS orden5
FROM (((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON ((ficha.iden =
    cg.idficha))) LEFT JOIN conjuntogrupo capa ON ((cg.idconjunto =
    capa.iden))) LEFT JOIN conjuntodeterminaciongrupo cdg ON ((capa.iden =
    cdg.idconjunto))) LEFT JOIN planeamiento.determinacion detgrupo ON
    ((cdg.iddeterminacion = detgrupo.iden))) LEFT JOIN
    planeamiento.opciondeterminacion odgrupo ON ((detgrupo.iden =
    odgrupo.iddeterminacionvalorref))) LEFT JOIN
    planeamiento.entidaddeterminacionregimen edrgrupo ON ((odgrupo.iden =
    edrgrupo.idopciondeterminacion))) LEFT JOIN
    planeamiento.casoentidaddeterminacion casogrupo ON ((edrgrupo.idcaso =
    casogrupo.iden))) LEFT JOIN planeamiento.entidaddeterminacion edgrupo
    ON ((casogrupo.identidaddeterminacion = edgrupo.iden))) JOIN
    planeamiento.entidad ON ((edgrupo.identidad = entidad.iden))) LEFT JOIN
    planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN
    planeamiento.plan ON ((tramite.idplan = plan.iden)))
ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden;

--
-- Definition for view v_regimendirecto (OID = 23148) : 
--
CREATE VIEW ficha.v_regimendirecto AS
SELECT plan.idambito, ficha.iden, ficha.nombre AS ficha, capa.iden AS
    idcapa, capa.nombre AS capa, detgrupo.apartado AS apartadogrupo,
    detgrupo.nombre AS grupo, entidad.iden AS identidad, entidad.clave,
    entidad.nombre, entidadbase.nombre AS entidadbase, gd.nombre AS
    grupodeterminacion, detaplicada.iden AS iddeterminacion,
    detaplicada.apartado AS apartadoaplicada, detaplicada.nombre AS
    determinacionaplicada, edr.iden AS idvalor, CASE WHEN
    ((edr.valor)::text = ''::text) THEN NULL::character varying ELSE
    edr.valor END AS valor, detvalor.iden AS iddetvalor, detvalor.apartado
    AS apartadovalor, detvalor.nombre AS determinacionvalor,
    detcasoaplicacion.apartado AS apartadocasoaplicacion,
    detcasoaplicacion.nombre AS detcasoaplicacion, CASE WHEN gdd.regimenesp
    THEN re.iden ELSE NULL::integer END AS idregimenespecifico, CASE WHEN
    gdd.regimenesp THEN re.nombre ELSE NULL::character varying END AS
    regimenespecifico, CASE WHEN gdd.regimenesp THEN re.texto ELSE
    NULL::text END AS valorregimenespecifico
FROM (((((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON
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
    planeamiento.entidad entidadbase ON ((entidad.identidadbase =
    entidadbase.iden))) LEFT JOIN planeamiento.entidaddeterminacion
    edaplicaciones ON ((edgrupo.identidad = edaplicaciones.identidad)))
    LEFT JOIN planeamiento.determinacion detaplicada ON
    ((edaplicaciones.iddeterminacion = detaplicada.iden))) LEFT JOIN
    planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN
    planeamiento.plan ON ((tramite.idplan = plan.iden))) LEFT JOIN
    planeamiento.casoentidaddeterminacion caso ON ((edaplicaciones.iden =
    caso.identidaddeterminacion))) LEFT JOIN
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
    JOIN fichagrupodeterminacion fgd ON ((ficha.iden = fgd.idficha))) JOIN
    grupodeterminacion gd ON (((fgd.idgrupo = gd.iden) AND
    (gd.idconjuntogrupo = capa.iden)))) JOIN
    grupodeterminaciondeterminacion gdd ON (((gd.iden = gdd.idgrupo) AND
    (gdd.iddeterminacion = detaplicada.iden))))
WHERE (detaplicada.idcaracter = 2)
ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden,
    fgd.orden, gdd.orden, re.orden;

--
-- Definition for view v_orden_actos (OID = 23162) : 
--
CREATE VIEW ficha.v_orden_actos AS
SELECT DISTINCT plan.idambito, ficha.iden, entidad.iden AS identidad,
    detaplicada.iden AS iddeterminacion, cg.orden AS orden_conjuntogrupo,
    cdg.orden AS orden_conjuntodetgrupo, entidad.orden AS orden_entidad,
    entidad.iden AS orden_identidad, detgrupo.orden AS orden_grupo,
    vca.orden AS orden_valordetclasif, detaplicada.orden AS
    orden_detaplicada, detaplicada.iden AS orden_iddetaplicada, re.orden AS
    orden_regimenesp
FROM ((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON
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
    planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN
    planeamiento.plan ON ((tramite.idplan = plan.iden))) LEFT JOIN
    determinacionclasifacto dca ON ((ficha.iden = dca.idficha))) LEFT JOIN
    planeamiento.determinacion detclasif ON ((dca.iddeterminacion =
    detclasif.iden))) LEFT JOIN valoresclasifacto vca ON (((dca.iden =
    vca.iddeterminacionclasifacto) AND (detvalor.iden =
    vca.iddeterminacionvalorregimen)))) JOIN planeamiento.determinacion
    detregimen ON (((edr.iddeterminacionregimen = detregimen.iden) AND
    (detvalor.iden = vca.iddeterminacionvalorregimen))))
WHERE (vca.iden IS NOT NULL)
ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden,
    vca.orden, detaplicada.orden, detaplicada.iden, re.orden,
    plan.idambito, ficha.iden, entidad.iden, detaplicada.iden;

--
-- Definition for view v_acto (OID = 23168) : 
--
CREATE VIEW ficha.v_acto AS
SELECT DISTINCT plan.idambito, ficha.iden, ficha.nombre AS ficha, capa.iden
    AS idcapa, capa.nombre AS capa, detgrupo.apartado AS apartadogrupo,
    detgrupo.nombre AS grupo, entidad.iden AS identidad, entidad.clave,
    entidad.nombre, detaplicada.iden AS iddeterminacion,
    detaplicada.apartado AS apartadoaplicada, detaplicada.nombre AS
    determinacionaplicada, edr.iden AS idvalor, CASE WHEN
    ((edr.valor)::text = ''::text) THEN NULL::character varying ELSE
    edr.valor END AS valor, detvalor.iden AS iddetvalor, detvalor.apartado
    AS apartadovalor, detvalor.nombre AS determinacionvalor,
    detregimen.iden AS iddeterminacionregimen, detregimen.apartado AS
    apartadoregimen, detregimen.nombre AS determinacionregimen, re.iden AS
    idregimenespecifico, re.nombre AS regimenespecifico, re.texto AS
    valorregimenespecifico, detclasif.iden AS iddetclasif,
    detclasif.apartado AS apartadodetclasif, detclasif.nombre AS
    determinacionclasif, vca.iden AS id_valordetclasif, cg.orden AS
    orden_conjuntogrupo, cdg.orden AS orden_conjuntodetgrupo, entidad.orden
    AS orden_entidad, entidad.iden AS orden_identidad, detgrupo.orden AS
    orden_grupo, CASE WHEN (vca.iden IS NULL) THEN (
    SELECT orden_actos.orden_valordetclasif
    FROM v_orden_actos orden_actos
    WHERE ((orden_actos.identidad = entidad.iden) AND
        (orden_actos.iddeterminacion = detaplicada.iden))
    LIMIT 1
    ) ELSE vca.orden END AS orden_valordetclasif, detaplicada.orden AS
        orden_detaplicada, detaplicada.iden AS orden_iddetaplicada,
        re.orden AS orden_regimenesp
FROM (((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON
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
    planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN
    planeamiento.plan ON ((tramite.idplan = plan.iden))) LEFT JOIN
    determinacionclasifacto dca ON (((ficha.iden = dca.idficha) AND
    (dca.iddeterminacion IN (
    SELECT edrfiltro.iddeterminacionregimen
    FROM planeamiento.entidaddeterminacionregimen edrfiltro
    WHERE (edrfiltro.idcaso IN (
        SELECT casofiltro.iden
        FROM planeamiento.casoentidaddeterminacion casofiltro
        WHERE (casofiltro.identidaddeterminacion IN (
            SELECT edfiltro.iden
            FROM planeamiento.entidaddeterminacion edfiltro
            WHERE (edfiltro.identidad = entidad.iden)
            ))
        ))
    ))))) LEFT JOIN planeamiento.determinacion detclasif ON
        ((dca.iddeterminacion = detclasif.iden))) LEFT JOIN
        valoresclasifacto vca ON (((dca.iden =
        vca.iddeterminacionclasifacto) AND (detvalor.iden =
        vca.iddeterminacionvalorregimen)))) LEFT JOIN regimenesacto ra ON
        ((ficha.iden = ra.idficha))) JOIN planeamiento.determinacion
        detregimen ON (((edr.iddeterminacionregimen = detregimen.iden) AND
        ((ra.iddeterminacion = detregimen.iden) OR (detvalor.iden =
        vca.iddeterminacionvalorregimen)))))
WHERE ((detaplicada.idcaracter = 9) AND (NOT (capa.iden IN (
    SELECT grupodeterminacion.idconjuntogrupo
    FROM grupodeterminacion
    ))))
ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden,
    CASE WHEN (vca.iden IS NULL) THEN (
    SELECT orden_actos.orden_valordetclasif
    FROM v_orden_actos orden_actos
    WHERE ((orden_actos.identidad = entidad.iden) AND
        (orden_actos.iddeterminacion = detaplicada.iden))
    LIMIT 1
    ) ELSE vca.orden END, detaplicada.orden, detaplicada.iden, re.orden,
        plan.idambito, ficha.iden, ficha.nombre, capa.iden, capa.nombre,
        detgrupo.apartado, detgrupo.nombre, entidad.clave, entidad.nombre,
        detaplicada.apartado, detaplicada.nombre, edr.iden, CASE WHEN
        ((edr.valor)::text = ''::text) THEN NULL::character varying ELSE
        edr.valor END, detvalor.iden, detvalor.apartado, detvalor.nombre,
        detregimen.iden, detregimen.apartado, detregimen.nombre, re.iden,
        re.nombre, re.texto, detclasif.iden, detclasif.apartado,
        detclasif.nombre, vca.iden, entidad.iden, detaplicada.iden;

--
-- Definition for index conjuntodeterminaciongrupo_idconjunto_idx (OID = 23174) : 
--
CREATE INDEX conjuntodeterminaciongrupo_idconjunto_idx ON conjuntodeterminaciongrupo USING btree (idconjunto);
--
-- Definition for index conjuntodeterminaciongrupo_iddeterminacion_idx (OID = 23175) : 
--
CREATE INDEX conjuntodeterminaciongrupo_iddeterminacion_idx ON conjuntodeterminaciongrupo USING btree (iddeterminacion);
--
-- Definition for index conjuntodeterminaciongrupo_orden_idx (OID = 23176) : 
--
CREATE INDEX conjuntodeterminaciongrupo_orden_idx ON conjuntodeterminaciongrupo USING btree (orden);
--
-- Definition for index determinacionclasifacto_ficha_idx (OID = 23177) : 
--
CREATE INDEX determinacionclasifacto_ficha_idx ON determinacionclasifacto USING btree (idficha);
--
-- Definition for index determinacionclasifacto_determinacion_idx (OID = 23178) : 
--
CREATE INDEX determinacionclasifacto_determinacion_idx ON determinacionclasifacto USING btree (iddeterminacion);
--
-- Definition for index determinacionclasifuso_ficha_idx (OID = 23179) : 
--
CREATE INDEX determinacionclasifuso_ficha_idx ON determinacionclasifuso USING btree (idficha);
--
-- Definition for index determinacionclasifuso_determinacion_idx (OID = 23180) : 
--
CREATE INDEX determinacionclasifuso_determinacion_idx ON determinacionclasifuso USING btree (iddeterminacion);
--
-- Definition for index fichaconjuntogrupo_ficha_idx (OID = 23181) : 
--
CREATE INDEX fichaconjuntogrupo_ficha_idx ON fichaconjuntogrupo USING btree (idficha);
--
-- Definition for index fichaconjuntogrupo_conjunto_idx (OID = 23182) : 
--
CREATE INDEX fichaconjuntogrupo_conjunto_idx ON fichaconjuntogrupo USING btree (idconjunto);
--
-- Definition for index fichaconjuntogrupo_orden_idx (OID = 23183) : 
--
CREATE INDEX fichaconjuntogrupo_orden_idx ON fichaconjuntogrupo USING btree (orden);
--
-- Definition for index fichagrupodeterminacion_ficha_idx (OID = 23184) : 
--
CREATE INDEX fichagrupodeterminacion_ficha_idx ON fichagrupodeterminacion USING btree (idficha);
--
-- Definition for index fichagrupodeterminacion_grupo_idx (OID = 23185) : 
--
CREATE INDEX fichagrupodeterminacion_grupo_idx ON fichagrupodeterminacion USING btree (idgrupo);
--
-- Definition for index fichagrupodeterminacion_orden_idx (OID = 23186) : 
--
CREATE INDEX fichagrupodeterminacion_orden_idx ON fichagrupodeterminacion USING btree (orden);
--
-- Definition for index grupodeterminacion_conjuntogrupo_idx (OID = 23187) : 
--
CREATE INDEX grupodeterminacion_conjuntogrupo_idx ON grupodeterminacion USING btree (idconjuntogrupo);
--
-- Definition for index grupodeterminaciondeterminacion_grupo_idx (OID = 23188) : 
--
CREATE INDEX grupodeterminaciondeterminacion_grupo_idx ON grupodeterminaciondeterminacion USING btree (idgrupo);
--
-- Definition for index grupodeterminaciondeterminacion_determinacion_idx (OID = 23189) : 
--
CREATE INDEX grupodeterminaciondeterminacion_determinacion_idx ON grupodeterminaciondeterminacion USING btree (iddeterminacion);
--
-- Definition for index grupodeterminaciondeterminacion_orden_idx (OID = 23190) : 
--
CREATE INDEX grupodeterminaciondeterminacion_orden_idx ON grupodeterminaciondeterminacion USING btree (orden);
--
-- Definition for index regimenesacto_ficha_idx (OID = 23191) : 
--
CREATE INDEX regimenesacto_ficha_idx ON regimenesacto USING btree (idficha);
--
-- Definition for index regimenesacto_determinacion_idx (OID = 23192) : 
--
CREATE INDEX regimenesacto_determinacion_idx ON regimenesacto USING btree (iddeterminacion);
--
-- Definition for index regimenesuso_ficha_idx (OID = 23193) : 
--
CREATE INDEX regimenesuso_ficha_idx ON regimenesuso USING btree (idficha);
--
-- Definition for index regimenesuso_determinacion_idx (OID = 23194) : 
--
CREATE INDEX regimenesuso_determinacion_idx ON regimenesuso USING btree (iddeterminacion);
--
-- Definition for index valoresclasifacto_detclasif_idx (OID = 23195) : 
--
CREATE INDEX valoresclasifacto_detclasif_idx ON valoresclasifacto USING btree (iddeterminacionclasifacto);
--
-- Definition for index valoresclasifacto_detvalorreg_idx (OID = 23196) : 
--
CREATE INDEX valoresclasifacto_detvalorreg_idx ON valoresclasifacto USING btree (iddeterminacionvalorregimen);
--
-- Definition for index valoresclasifacto_orden_idx (OID = 23197) : 
--
CREATE INDEX valoresclasifacto_orden_idx ON valoresclasifacto USING btree (orden);
--
-- Definition for index valoresclasifuso_detclasif_idx (OID = 23198) : 
--
CREATE INDEX valoresclasifuso_detclasif_idx ON valoresclasifuso USING btree (iddeterminacionclasifuso);
--
-- Definition for index valoresclasifuso_detvalorreg_idx (OID = 23199) : 
--
CREATE INDEX valoresclasifuso_detvalorreg_idx ON valoresclasifuso USING btree (iddeterminacionvalorregimen);
--
-- Definition for index valoresclasifuso_orden_idx (OID = 23200) : 
--
CREATE INDEX valoresclasifuso_orden_idx ON valoresclasifuso USING btree (orden);
--
-- Definition for index ficha_pkey (OID = 22927) : 
--
ALTER TABLE ONLY ficha
    ADD CONSTRAINT ficha_pkey
    PRIMARY KEY (iden);
--
-- Definition for index conjuntogrupo_pkey (OID = 22931) : 
--
ALTER TABLE ONLY conjuntogrupo
    ADD CONSTRAINT conjuntogrupo_pkey
    PRIMARY KEY (iden);
--
-- Definition for index fichaconjuntogrupo_pkey (OID = 22933) : 
--
ALTER TABLE ONLY fichaconjuntogrupo
    ADD CONSTRAINT fichaconjuntogrupo_pkey
    PRIMARY KEY (iden);
--
-- Definition for index fichaconjuntogrupo_fk1 (OID = 22935) : 
--
ALTER TABLE ONLY fichaconjuntogrupo
    ADD CONSTRAINT fichaconjuntogrupo_fk1
    FOREIGN KEY (idconjunto) REFERENCES conjuntogrupo(iden) ON DELETE RESTRICT;
--
-- Definition for index fichaconjuntogrupo_fk (OID = 22940) : 
--
ALTER TABLE ONLY fichaconjuntogrupo
    ADD CONSTRAINT fichaconjuntogrupo_fk
    FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;
--
-- Definition for index conjuntodeterminaciongrupo_pkey (OID = 22945) : 
--
ALTER TABLE ONLY conjuntodeterminaciongrupo
    ADD CONSTRAINT conjuntodeterminaciongrupo_pkey
    PRIMARY KEY (iden);
--
-- Definition for index conjuntodeterminaciongrupo_fk (OID = 22947) : 
--
ALTER TABLE ONLY conjuntodeterminaciongrupo
    ADD CONSTRAINT conjuntodeterminaciongrupo_fk
    FOREIGN KEY (idconjunto) REFERENCES conjuntogrupo(iden) ON DELETE CASCADE;
--
-- Definition for index conjuntodeterminaciongrupo_fk1 (OID = 22952) : 
--
ALTER TABLE ONLY conjuntodeterminaciongrupo
    ADD CONSTRAINT conjuntodeterminaciongrupo_fk1
    FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index conjuntodeterminacion_pkey (OID = 22957) : 
--
ALTER TABLE ONLY grupodeterminacion
    ADD CONSTRAINT conjuntodeterminacion_pkey
    PRIMARY KEY (iden);
--
-- Definition for index fichagrupodeterminacion_pkey (OID = 22959) : 
--
ALTER TABLE ONLY fichagrupodeterminacion
    ADD CONSTRAINT fichagrupodeterminacion_pkey
    PRIMARY KEY (iden);
--
-- Definition for index fichaconjuntodeterminacion_fk (OID = 22961) : 
--
ALTER TABLE ONLY fichagrupodeterminacion
    ADD CONSTRAINT fichaconjuntodeterminacion_fk
    FOREIGN KEY (idgrupo) REFERENCES grupodeterminacion(iden) ON DELETE RESTRICT;
--
-- Definition for index fichaconjuntodeterminacion_fk1 (OID = 22966) : 
--
ALTER TABLE ONLY fichagrupodeterminacion
    ADD CONSTRAINT fichaconjuntodeterminacion_fk1
    FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE RESTRICT;
--
-- Definition for index grupodeterminaciondeterminacion_pkey (OID = 22971) : 
--
ALTER TABLE ONLY grupodeterminaciondeterminacion
    ADD CONSTRAINT grupodeterminaciondeterminacion_pkey
    PRIMARY KEY (iden);
--
-- Definition for index grupodeterminaciondeterminacion_fk (OID = 22973) : 
--
ALTER TABLE ONLY grupodeterminaciondeterminacion
    ADD CONSTRAINT grupodeterminaciondeterminacion_fk
    FOREIGN KEY (idgrupo) REFERENCES grupodeterminacion(iden) ON DELETE CASCADE;
--
-- Definition for index grupodeterminaciondeterminacion_fk1 (OID = 22978) : 
--
ALTER TABLE ONLY grupodeterminaciondeterminacion
    ADD CONSTRAINT grupodeterminaciondeterminacion_fk1
    FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index determinacionclasifuso_pkey (OID = 22983) : 
--
ALTER TABLE ONLY determinacionclasifuso
    ADD CONSTRAINT determinacionclasifuso_pkey
    PRIMARY KEY (iden);
--
-- Definition for index determinacionclasifacto_pkey (OID = 22985) : 
--
ALTER TABLE ONLY determinacionclasifacto
    ADD CONSTRAINT determinacionclasifacto_pkey
    PRIMARY KEY (iden);
--
-- Definition for index regimenuso_pkey (OID = 23007) : 
--
ALTER TABLE ONLY valoresclasifuso
    ADD CONSTRAINT regimenuso_pkey
    PRIMARY KEY (iden);
--
-- Definition for index valoresclasifacto_pkey (OID = 23009) : 
--
ALTER TABLE ONLY valoresclasifacto
    ADD CONSTRAINT valoresclasifacto_pkey
    PRIMARY KEY (iden);
--
-- Definition for index regimenesuso_pkey (OID = 23031) : 
--
ALTER TABLE ONLY regimenesuso
    ADD CONSTRAINT regimenesuso_pkey
    PRIMARY KEY (iden);
--
-- Definition for index regimenesacto_pkey (OID = 23043) : 
--
ALTER TABLE ONLY regimenesacto
    ADD CONSTRAINT regimenesacto_pkey
    PRIMARY KEY (iden);
--
-- Definition for index ficha_fk (OID = 23055) : 
--
ALTER TABLE ONLY ficha
    ADD CONSTRAINT ficha_fk
    FOREIGN KEY (idtramite) REFERENCES planeamiento.tramite(iden) ON DELETE RESTRICT;
--
-- Definition for index grupodeterminacion_fk (OID = 23100) : 
--
ALTER TABLE ONLY grupodeterminacion
    ADD CONSTRAINT grupodeterminacion_fk
    FOREIGN KEY (idconjuntogrupo) REFERENCES conjuntogrupo(iden) ON DELETE RESTRICT;
--
-- Definition for index determinacionclasifacto_fk (OID = 24725) : 
--
ALTER TABLE ONLY determinacionclasifacto
    ADD CONSTRAINT determinacionclasifacto_fk
    FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index determinacionclasifacto_fk1 (OID = 24730) : 
--
ALTER TABLE ONLY determinacionclasifacto
    ADD CONSTRAINT determinacionclasifacto_fk1
    FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;
--
-- Definition for index determinacionclasifuso_fk (OID = 24735) : 
--
ALTER TABLE ONLY determinacionclasifuso
    ADD CONSTRAINT determinacionclasifuso_fk
    FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index determinacionclasifuso_fk1 (OID = 24740) : 
--
ALTER TABLE ONLY determinacionclasifuso
    ADD CONSTRAINT determinacionclasifuso_fk1
    FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;
--
-- Definition for index regimenesacto_fk (OID = 24745) : 
--
ALTER TABLE ONLY regimenesacto
    ADD CONSTRAINT regimenesacto_fk
    FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index regimenesacto_fk1 (OID = 24750) : 
--
ALTER TABLE ONLY regimenesacto
    ADD CONSTRAINT regimenesacto_fk1
    FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;
--
-- Definition for index regimenesuso_fk (OID = 24755) : 
--
ALTER TABLE ONLY regimenesuso
    ADD CONSTRAINT regimenesuso_fk
    FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index regimenesuso_fk1 (OID = 24760) : 
--
ALTER TABLE ONLY regimenesuso
    ADD CONSTRAINT regimenesuso_fk1
    FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;
--
-- Definition for index valoresclasifacto_fk (OID = 24765) : 
--
ALTER TABLE ONLY valoresclasifacto
    ADD CONSTRAINT valoresclasifacto_fk
    FOREIGN KEY (iddeterminacionclasifacto) REFERENCES determinacionclasifacto(iden) ON DELETE CASCADE;
--
-- Definition for index valoresclasifacto_fk1 (OID = 24770) : 
--
ALTER TABLE ONLY valoresclasifacto
    ADD CONSTRAINT valoresclasifacto_fk1
    FOREIGN KEY (iddeterminacionvalorregimen) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
--
-- Definition for index valoresclasifuso_fk (OID = 24775) : 
--
ALTER TABLE ONLY valoresclasifuso
    ADD CONSTRAINT valoresclasifuso_fk
    FOREIGN KEY (iddeterminacionclasifuso) REFERENCES determinacionclasifuso(iden) ON DELETE CASCADE;
--
-- Definition for index valoresclasifuso_fk1 (OID = 24780) : 
--
ALTER TABLE ONLY valoresclasifuso
    ADD CONSTRAINT valoresclasifuso_fk1
    FOREIGN KEY (iddeterminacionvalorregimen) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;
