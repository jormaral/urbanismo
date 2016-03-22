--
-- PostgreSQL database dump
--

-- Started on 2013-07-09 13:24:55

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 16 (class 2615 OID 59283)
-- Name: validacion; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA validacion;


ALTER SCHEMA validacion OWNER TO postgres;

--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 16
-- Name: SCHEMA validacion; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA validacion IS 'standard public schema';


SET search_path = validacion, pg_catalog;

--
-- TOC entry 3099 (class 1259 OID 60408)
-- Dependencies: 16
-- Name: validacion_error_sequence; Type: SEQUENCE; Schema: validacion; Owner: postgres
--

CREATE SEQUENCE validacion_error_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE validacion.validacion_error_sequence OWNER TO postgres;

--
-- TOC entry 3449 (class 0 OID 0)
-- Dependencies: 3099
-- Name: validacion_error_sequence; Type: SEQUENCE SET; Schema: validacion; Owner: postgres
--

SELECT pg_catalog.setval('validacion_error_sequence', 100, true);


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 3100 (class 1259 OID 60410)
-- Dependencies: 3425 16
-- Name: error; Type: TABLE; Schema: validacion; Owner: postgres; Tablespace: 
--

CREATE TABLE error (
    iden integer DEFAULT nextval('validacion_error_sequence'::regclass) NOT NULL,
    idproceso integer NOT NULL,
    idvalidacion integer NOT NULL,
    mensaje text NOT NULL
);


ALTER TABLE validacion.error OWNER TO postgres;

--
-- TOC entry 3101 (class 1259 OID 60417)
-- Dependencies: 16
-- Name: validacion_proceso_sequence; Type: SEQUENCE; Schema: validacion; Owner: postgres
--

CREATE SEQUENCE validacion_proceso_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE validacion.validacion_proceso_sequence OWNER TO postgres;

--
-- TOC entry 3450 (class 0 OID 0)
-- Dependencies: 3101
-- Name: validacion_proceso_sequence; Type: SEQUENCE SET; Schema: validacion; Owner: postgres
--

SELECT pg_catalog.setval('validacion_proceso_sequence', 100, true);


--
-- TOC entry 3102 (class 1259 OID 60419)
-- Dependencies: 3426 16
-- Name: proceso; Type: TABLE; Schema: validacion; Owner: postgres; Tablespace: 
--

CREATE TABLE proceso (
    iden integer DEFAULT nextval('validacion_proceso_sequence'::regclass) NOT NULL,
    idfip character varying(256) NOT NULL,
    inicio timestamp with time zone NOT NULL,
    fin timestamp with time zone,
    backup character varying(3000),
    consolidado timestamp with time zone,
    estado character varying(255),
    idtramite integer,
    iteracionvalidacion integer
);


ALTER TABLE validacion.proceso OWNER TO postgres;

--
-- TOC entry 3103 (class 1259 OID 60426)
-- Dependencies: 16
-- Name: resultado; Type: TABLE; Schema: validacion; Owner: postgres; Tablespace: 
--

CREATE TABLE resultado (
    idproceso integer NOT NULL,
    idvalidacion integer NOT NULL,
    fecha date NOT NULL,
    exito boolean NOT NULL
);


ALTER TABLE validacion.resultado OWNER TO postgres;

--
-- TOC entry 3104 (class 1259 OID 60429)
-- Dependencies: 16
-- Name: validacion_sequence; Type: SEQUENCE; Schema: validacion; Owner: postgres
--

CREATE SEQUENCE validacion_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE validacion.validacion_sequence OWNER TO postgres;

--
-- TOC entry 3451 (class 0 OID 0)
-- Dependencies: 3104
-- Name: validacion_sequence; Type: SEQUENCE SET; Schema: validacion; Owner: postgres
--

SELECT pg_catalog.setval('validacion_sequence', 100, true);


--
-- TOC entry 3105 (class 1259 OID 60431)
-- Dependencies: 3427 16
-- Name: validacion; Type: TABLE; Schema: validacion; Owner: postgres; Tablespace: 
--

CREATE TABLE validacion (
    iden integer DEFAULT nextval('validacion_sequence'::regclass) NOT NULL,
    tipovalidacion integer NOT NULL,
    tipoerror integer,
    activado boolean NOT NULL,
    nemo character varying(10),
    descripcion character varying(3000) NOT NULL,
    sentencia character varying(3000) NOT NULL,
    resultadoesperado character varying(1000) NOT NULL,
    columnas character varying(3000),
    mensaje character varying(3000)
);


ALTER TABLE validacion.validacion OWNER TO postgres;

--
-- TOC entry 3106 (class 1259 OID 60438)
-- Dependencies: 16
-- Name: validacion_elemento_sequence; Type: SEQUENCE; Schema: validacion; Owner: postgres
--

CREATE SEQUENCE validacion_elemento_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE validacion.validacion_elemento_sequence OWNER TO postgres;

--
-- TOC entry 3452 (class 0 OID 0)
-- Dependencies: 3106
-- Name: validacion_elemento_sequence; Type: SEQUENCE SET; Schema: validacion; Owner: postgres
--

SELECT pg_catalog.setval('validacion_elemento_sequence', 100, true);


--
-- TOC entry 3442 (class 0 OID 60410)
-- Dependencies: 3100
-- Data for Name: error; Type: TABLE DATA; Schema: validacion; Owner: postgres
--



--
-- TOC entry 3443 (class 0 OID 60419)
-- Dependencies: 3102
-- Data for Name: proceso; Type: TABLE DATA; Schema: validacion; Owner: postgres
--



--
-- TOC entry 3444 (class 0 OID 60426)
-- Dependencies: 3103
-- Data for Name: resultado; Type: TABLE DATA; Schema: validacion; Owner: postgres
--



--
-- TOC entry 3445 (class 0 OID 60431)
-- Dependencies: 3105
-- Data for Name: validacion; Type: TABLE DATA; Schema: validacion; Owner: postgres
--

INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (3, 3, 1, false, 'Con001', 'Cualquier Entidad que tenga Carpeta como valor de la Determinación Grupo de Entidad sólo puede tener aplicada la Determinación de carácter Grupo de Enti', 'N/A', 'not null', 'N/A', NULL);
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (2, 3, 1, false, 'Ads001', 'Las entidades origen y destino de una Adscripción deben pertenecer al mismo trámite que crea la adscripción', 'select entOrigen.codigo as entidadOrigen,trmOrigen.codigofip as tramiteOrigen, entDestino.codigo as entidadDestino,trmDestino.codigofip as tramiteDestino
 from planeamiento.relacion rel
inner join planeamiento.tramite trm on rel.idtramitecreador=trm.iden
inner join planeamiento.vectorrelacion vecOrigen on rel.iden=vecOrigen.idrelacion and vecOrigen.iddefvector=4
inner join planeamiento.entidad entOrigen on entOrigen.iden=vecOrigen.valor
inner join planeamiento.tramite trmOrigen on entOrigen.idtramite=trmOrigen.iden
inner join planeamiento.vectorrelacion vecDestino on rel.iden=vecDestino.idrelacion and vecDestino.iddefvector=5
inner join planeamiento.entidad entDestino on entDestino.iden=vecDestino.valor
inner join planeamiento.tramite trmDestino on entDestino.idtramite=trmDestino.iden
 where rel.iddefrelacion=3 and (trm.iden<> entOrigen.idtramite or trm.iden<> entDestino.idtramite)
 and Upper(Trim(trm.codigoFIP))=Upper(Trim(:codigoFip))', 'null', 'entidad.origen.codigo,tramite.origen.codigo,entidad.destino.codigo,tramite.destino.codigo', 'Entidad Origen: %1 del trámite %2, Entidad Destino: %3 del trámite %4');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (4, 3, 1, false, 'Con002', 'Las Entidades a las que se asignan las Condiciones urbanísticas deben pertenecer al Trámite actual', 'N/A', 'null', 'entidad.codigo', NULL);
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (0, 6, 1, false, 'ValInt', 'Validacion integridad', 'N/A', 'not null', 'N/A', NULL);
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (1, 6, 1, false, 'ValUsu', 'Validacion usuario', 'N/A', 'not null', 'N/A', NULL);
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (5, 3, 1, false, 'Ope002', 'Si una operación entre Entidades no es de Creación de Adscripción, no deberá tener propiedades de Adscripción', 'N/A', 'not null', 'N/A', NULL);
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (12, 4, 1, true, 'Tra001', 'Todo Trámite debe contener, además de la obligada Determinación con Carácter de Virtual, al menos otra Determinación más de cualquier otro Carácter', 'Select Distinct cast(T.codigofip as varchar) From planeamiento.Tramite T

Where
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And T.iden In (Select D.idTramite From planeamiento.Determinacion D
Where D.idCaracter<>16)', 'not null', 'tramite.codigo', 'Trámite: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (15, 5, 1, true, 'Doc001', 'No puede haber mas de un documento del mismo tipo y grupo con el mismo nombre en el mismo trámite', 'Select Distinct D.nombre From planeamiento.Documento D, planeamiento.Tramite T

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And (D.idTipoDocumento, D.idGrupoDocumento, Trim(Upper(D.nombre))) In (

Select D.idTipoDocumento, D.idGrupoDocumento, Trim(Upper(D.nombre))

From planeamiento.Documento D, planeamiento.Tramite T

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

Group By D.idTipoDocumento, D.idGrupoDocumento, Trim(Upper(D.nombre))

Having Count(*)>1)', 'null', 'documento.nombre', 'Documento: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (14, 4, 1, false, 'Tra002', 'El Trámite debe contener al menos una Entidad', 'Select Distinct cast(T.codigofip as varchar) From planeamiento.Tramite T

Where
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And T.iden In (Select E.idTramite From planeamiento.Entidad E)', 'not null', 'tramite.codigo', 'Trámite: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (8, 2, 1, false, 'Con004', 'Una Determinacion no puede estar aplicada a una entidad y no tener al menos un caso', 'Select Distinct cast(E.codigo as varchar) as entidad , cast(D.codigo as varchar) as determinacion
From planeamiento.Determinacion D, planeamiento.Tramite T, 
planeamiento.EntidadDeterminacion ED,  planeamiento.Entidad E
Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idDeterminacion=D.iden
And ED.idEntidad=E.iden
And ED.iden Not In (Select CED.idEntidadDeterminacion From
planeamiento.CasoEntidadDeterminacion CED)', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: %1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (16, 5, 1, false, 'Doc002', 'La geometria de los documentos deberá ser siempre de tipo poligonal', 'Select D.nombre,DSHP.hoja From planeamiento.Documento D, planeamiento.Tramite T,
planeamiento.DocumentoSHP DSHP

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And DSHP.idDocumento=D.iden

And Upper(geometryType(DSHP.geom)) Not Like ''%POLYGON%''', 'null', 'documento.nombre,hoja.nombre', 'Documento: %1, Hoja: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (26, 4, 1, false, 'Tra003', 'El código del Trámite debe estar conformado por el MD5 de la concatenación del identificador del ámbito, el código del plan, el identificador del tipo de trámite, y la iteración.', 'Select Distinct cast(T.codigofip as varchar) From planeamiento.Tramite T, planeamiento.Plan P
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And Upper(Trim(T.codigoFIP))<>Upper(MD5(LPad(Text(P.idAmbito),6, ''0'') || LPad(Text(P.codigo),6,''0'') || LPad(Text(T.idTipoTramite),6,''0'') || LPad(Text(T.iteracion),2,''0'')))', 'not null', 'tramite.codigoFip', 'Tramite: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (55, 1, 1, false, 'Con010', 'Una Determinación no puede estar aplicada en algunos casos con valores de referencia, y con valores literales en otros.', 'Select Distinct cast(E.codigo as varchar) As Ent_codigo, cast(D.codigo as varchar) As Det_codigo From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED, planeamiento.EntidadDeterminacionRegimen EDR,
planeamiento.OpcionDeterminacion OD
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And CED.idEntidadDeterminacion=ED.iden
And (EDR.idCaso=CED.iden Or EDR.idCasoAplicacion=CED.iden)
And EDR.idOpcionDeterminacion=OD.iden And OD.idDeterminacion=D.iden
And EDR.valor Is Not Null', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: %1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (60, 6, 1, true, 'Ope004', 'El orden de operación debe ser único para cada Trámite, considerando las operaciones entre Determinaciones, Entidades y relaciones.', 'Select Distinct OP.iden, OP.orden From planeamiento.Operacion OP, planeamiento.Tramite T
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And OP.idTramiteOrdenante=T.iden
And (OP.orden, OP.idTramiteOrdenante) In (Select OP.orden, OP.idTramiteOrdenante From planeamiento.Operacion OP, planeamiento.Tramite T
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And OP.idTramiteOrdenante=T.iden
Group By OP.orden, OP.idTramiteOrdenante Having Count(*)>1)', 'null', 'operacion.iden,operacion.orden', 'Operación: %1 Orden: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (33, 2, 1, true, 'Det002', 'Las Determinaciones  de carácter "UNIDAD" deben tener propiedades de ''Unidad''', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
From planeamiento.Determinacion D, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden
And D.idCaracter=18
And D.iden Not In (Select VR.valor From planeamiento.VectorRelacion VR, diccionario.DefVector DV,
planeamiento.PropiedadRelacion PR1, planeamiento.PropiedadRelacion PR2, diccionario.DefPropiedad DP1,
diccionario.DefPropiedad DP2
Where DV.idDefRelacion=1 And VR.idDefVector=DV.iden
And DP1.idDefRelacion=1 And DP2.idDefRelacion=1
And PR1.idDefPropiedad=DP1.iden And PR2.idDefPropiedad=DP2.iden
And DP1.iden<>DP2.iden And Coalesce(PR1.valor,'''')<>'''' And Coalesce(PR2.valor,'''')<>''''
)', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (7, 3, 1, true, 'Con003', 'Una Entidad no puede tener aplicada dos veces la misma determinacion', 'Select Distinct E.iden as ent_iden, E.nombre as ent_nombre, E.clave as ent_clave, cast(E.codigo as varchar) as ent_codigo,
D.iden as det_iden, D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
From planeamiento.Entidad E, planeamiento.Tramite T,
planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D
Where E.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden
And ED.idDeterminacion=D.iden
And (ED.idEntidad, ED.idDeterminacion) In (
Select ED.idEntidad, ED.idDeterminacion From
planeamiento.EntidadDeterminacion ED
Group By ED.idEntidad, ED.idDeterminacion
Having Count(*)>1)', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: 
	Nombre: %2
	Clave: %3

Determinacion: 
	Nombre: %6
	Articulado: %7');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (9, 1, 1, true, 'Con005', 'Un Caso no debe tener asignado el mismo Documento más de una sola vez', 'Select Distinct E.iden as ent_iden, E.nombre as ent_nombre, E.clave as ent_clave, cast(E.codigo as varchar) as ent_codigo,
D.iden as det_iden, D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo 
From planeamiento.Entidad E,
planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED,
planeamiento.DocumentoCaso DC, planeamiento.Documento DOC
Where E.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And DC.idDocumento=DOC.iden And DC.idCaso=CED.iden
And CED.idEntidadDeterminacion=ED.iden
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And DC.idCaso In (
Select DC.idCaso From planeamiento.DocumentoCaso DC
inner join planeamiento.Documento DOC on DC.iddocumento=DOC.iden
Group By DC.idCaso, DOC.nombre, DOC.idTipoDocumento, DOC.idGrupoDocumento
Having Count(*)>1)', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: 
	Nombre: %2
	Clave: %3

Determinacion: 
	Nombre: %6
	Articulado: %7');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (10, 3, 1, true, 'Con006', 'Un Caso debe tener al menos un documento y/o un regimen', 'Select Distinct cast(E.codigo as varchar) As entidad_codigo, cast(D.codigo as varchar) As det_codigo 
From planeamiento.Entidad E,
planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED
Where E.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And CED.idEntidadDeterminacion=ED.iden
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And CED.iden Not In (Select DC.idCaso From planeamiento.DocumentoCaso DC)
and CED.iden Not In 
  (Select EDR.idCaso From planeamiento.EntidadDeterminacionRegimen EDR)', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: 
	Nombre: %2
	Clave: %3

Determinacion: 
	Nombre: %6
	Articulado: %7');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (49, 2, 1, true, 'Con007', 'Una Determinación sólo podrá estar aplicada a las entidades cuyo Grupo de Entidad esté entre los asignados como grupo de aplicación de dicha determinación', 'Select Distinct E.iden as ent_iden, E.nombre as ent_nombre, E.clave as ent_clave, cast(E.codigo as varchar) as ent_codigo,
D.iden as det_iden, D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.DeterminacionGrupoEntidad DGE
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And D.idCaracter<>20
And DGE.idDeterminacion=D.iden
And (E.iden, D.iden) Not In (
Select E.iden, DGE.idDeterminacion From planeamiento.Entidad E
inner join planeamiento.EntidadDeterminacion ED on E.iden=ED.identidad
inner join planeamiento.Determinacion D on ED.iddeterminacion=D.iden
inner join planeamiento.CasoEntidadDeterminacion CED on ED.iden=CED.idEntidadDeterminacion
inner join planeamiento.EntidadDeterminacionRegimen EDR on CED.iden=EDR.idcaso
inner join planeamiento.OpcionDeterminacion OD on EDR.idopciondeterminacion=OD.iden
inner join planeamiento.DeterminacionGrupoEntidad DGE on OD.idDeterminacionValorRef=DGE.idDeterminacionGrupo
inner join planeamiento.Tramite Tra on E.idTramite=Tra.Iden
where D.idCaracter=20 and Upper(Trim(Tra.codigoFIP))=Upper(Trim(:codigoFip)))', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: 
	Nombre: %2
	Clave: %3

Determinacion: 
	Nombre: %6
	Articulado: %7');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (52, 2, 1, true, 'Con008', 'Las Determinaciones que tengan Valores de Referencia, sólo podrán estar aplicadas con alguno de ellos.', 'Select ent.iden as ent_iden, ent.nombre as ent_nombre, ent.clave as ent_clave, cast(ent.codigo as varchar) as ent_codigo,
det.iden as det_iden, det.nombre as det_nombre, det.apartado as det_articulado, cast(det.codigo as varchar) as det_codigo
from planeamiento.determinacion det
inner join planeamiento.entidaddeterminacion ed on det.iden=ed.iddeterminacion
inner join planeamiento.entidad ent on ed.identidad=ent.iden
inner join planeamiento.tramite trm on ent.idtramite=trm.iden
inner join planeamiento.casoentidaddeterminacion caso on caso.identidaddeterminacion=ed.iden
left join planeamiento.entidaddeterminacionregimen edr on caso.iden=edr.idcaso
left join planeamiento.opciondeterminacion od on edr.idopciondeterminacion=od.iden
where
	CASE WHEN edr.iddeterminacionregimen is null THEN
	(det.iden in (select iddeterminacion from planeamiento.opciondeterminacion) and (od.iddeterminacion<>det.iden or od.iden is null))
    ELSE
    (edr.iddeterminacionregimen in (select iddeterminacion from planeamiento.opciondeterminacion) and (od.iddeterminacion<>edr.iddeterminacionregimen or od.iden is null))
    END
AND
Upper(Trim(trm.codigoFIP))=Upper(Trim(:codigoFip))', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: 
	Nombre: %2
	Clave: %3

Determinacion: 
	Nombre: %6
	Articulado: %7');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (53, 1, 1, true, 'Con009', 'El Caso de aplicación de una condición urbanística debe pertenecer a la misma entidad que el caso principal.', 'Select ent_rd.iden as ent_iden, ent_rd.nombre as ent_nombre, ent_rd.clave as ent_clave, cast(ent_rd.codigo as varchar) as ent_codigo,
det_rd.iden as det_iden, det_rd.nombre as det_nombre, det_rd.apartado as det_articulado, cast(det_rd.codigo as varchar) as det_codigo
from planeamiento.entidaddeterminacionregimen edr
inner join planeamiento.casoentidaddeterminacion caso_caso on edr.idcasoaplicacion=caso_caso.iden
inner join planeamiento.entidaddeterminacion ed_caso on caso_caso.identidaddeterminacion=ed_caso.iden
inner join planeamiento.entidad ent_caso on ed_caso.identidad=ent_caso.iden
inner join planeamiento.casoentidaddeterminacion caso_rd on edr.idcaso=caso_rd.iden
inner join planeamiento.entidaddeterminacion ed_rd on caso_rd.identidaddeterminacion=ed_rd.iden
inner join planeamiento.entidad ent_rd on ed_rd.identidad=ent_rd.iden
inner join planeamiento.determinacion det_rd on ed_rd.iddeterminacion=det_rd.iden
inner join planeamiento.tramite T on ent_rd.idtramite=T.iden 
where (ent_rd.iden<>ent_caso.iden)
and Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: 
	Nombre: %2
	Clave: %3

Determinacion: 
	Nombre: %6
	Articulado: %7');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (31, 2, 1, true, 'Det001', 'Una Determinación no puede tener asignada como unidad, una determinación cuyo carácter no sea ‘Unidad’.', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
 From planeamiento.Determinacion D, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden
And D.iden In (
Select VR1.valor From planeamiento.VectorRelacion VR1, planeamiento.VectorRelacion VR2, planeamiento.Determinacion D
Where VR1.idDefVector=2 And VR2.idDefVector=3 And VR1.idRelacion=VR2.idRelacion And VR2.valor=D.iden
And D.idCaracter<>18)', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (32, 2, 1, true, 'Det003', 'Una Determinación no puede tener propiedades de unidad si no tiene carácter ‘Unidad’.', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
 From planeamiento.Determinacion D, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden
And D.idCaracter<>18
And D.iden In (Select VR.valor From planeamiento.VectorRelacion VR, diccionario.DefVector DV 
Where DV.idDefRelacion=1 And VR.idDefVector=DV.iden)', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (35, 2, 1, true, 'Det005', 'Las Determinaciones que son Valores de Referencia de otra Determinación, deben tener carácter de “VALOR DE REFERENCIA”.', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
 From planeamiento.Determinacion D, planeamiento.Tramite T,
planeamiento.OpcionDeterminacion OD
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden
And D.idCaracter<>12
And OD.idDeterminacionValorRef=D.iden', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (39, 2, 1, true, 'Det009', 'Las Determinaciones con carácter Regulación siempre han de tener regulación específica', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
 From planeamiento.Determinacion D, planeamiento.Tramite T
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And Trim(Coalesce(D.texto,''''))='''' And D.idCaracter=13 
And (Select count(valor) From planeamiento.vectorrelacion
                Where idRelacion In 
                (Select idrelacion From planeamiento.vectorrelacion Where valor = D.iden And iddefvector= 40)
                And idrelacion in (Select idrelacion From planeamiento.relacion Where iddefrelacion = 20)
                And iddefvector = 40) = 0', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (40, 2, 1, true, 'Det010', 'No puede haber una Determinación con más de una asignación al mismo documento', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
 From planeamiento.Determinacion D Where D.iden In (
Select D.iden From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.Documento DOC, planeamiento.DocumentoDeterminacion DD
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And DD.idDeterminacion=D.iden And DD.idDocumento=DOC.iden
Group By D.iden, DOC.nombre, DOC.idTipoDocumento, DOC.idGrupoDocumento
Having Count(*)>1)', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (67, 2, 1, true, 'Det012', 'Toda Determinación de carácter Norma General, Régimen de Uso y Régimen de Acto que no se exprese mediante Valores de referencia debe tener asignada una Unidad.', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
 From planeamiento.Determinacion D, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden And D.idcaracter  In (2,7,8)
And d.Iden Not In (Select Iddeterminacion From planeamiento.Opciondeterminacion where iddeterminacion = D.iden)
And (Select count(valor) From planeamiento.vectorrelacion
                Where idRelacion In 
                (Select idrelacion From planeamiento.vectorrelacion Where valor = D.iden And iddefvector= 2)
                AND idrelacion in (SELECT idrelacion FROM planeamiento.relacion WHERE iddefrelacion = 2)
                AND iddefvector = 3) <> 1', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (13, 2, 1, true, 'Det013', 'Una Determinación no puede asignarse a sí misma como Determinación reguladora', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
 From planeamiento.Determinacion D, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip)) And
D.idTramite=T.iden And
D.iden In 
(Select VR1.valor From planeamiento.VectorRelacion VR1, planeamiento.VectorRelacion VR2
          Where VR1.idDefVector = Any(Array[8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40]) And 
VR2.idDefVector = Any(Array[9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39]) And
VR1.idRelacion = VR2.idRelacion And
VR1.Valor = VR2.Valor)', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (17, 3, 1, true, 'Ent002', 'No puede haber una entidad con mas de una asignacion al mismo documento', 'Select Distinct  D.nombre as ent_nombre, D.clave as ent_clave,D.etiqueta as ent_etiqueta, D.iden as ent_iden, cast(D.codigo as varchar) as ent_codigo
From planeamiento.Entidad D Where D.iden In (
Select D.iden From planeamiento.Entidad D, planeamiento.Tramite T, planeamiento.Documento DOC, 
planeamiento.DocumentoEntidad DD
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And DD.idEntidad=D.iden And DD.idDocumento=DOC.iden
Group By D.iden, DOC.nombre, DOC.idTipoDocumento, DOC.idGrupoDocumento
Having Count(*)>1)', 'null', 'entidad.codigo', 'Entidad: 
	Nombre: %1
	Clave: %2
	Etiqueta: %3');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (28, 3, 2, true, 'Ent004', 'La geometría de las Entidades debe ser correcta, según el criterio de la función IsValid de PostGIS.', 'Select Distinct  E.nombre as ent_nombre, E.clave as ent_clave,E.etiqueta as ent_etiqueta, E.iden as ent_iden, cast(E.codigo as varchar) as ent_codigo 
From planeamiento.Entidad E, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip)) 
And E.idTramite=T.iden
And E.iden In (
Select EPOL.idEntidad From planeamiento.EntidadPol EPOL Where st_IsValid(geom)=false
Union
Select ELIN.idEntidad From planeamiento.EntidadLin ELIN Where st_IsValid(geom)=false
Union
Select EPNT.idEntidad From planeamiento.EntidadPnt EPNT Where st_IsValid(geom)=false)', 'null', 'entidad.codigo', 'Entidad: 
	Nombre: %1
	Clave: %2
	Etiqueta: %3');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (66, 3, 2, true, 'Ent005', 'Las geometrías poligonales de las entidades de un mismo grupo, no se deben solapar.', 'select entidad1.ent1,entidad1.ent1nombre,entidad1.ent1clave,entidad2.ent2,entidad2.ent2nombre,entidad2.ent2clave from (
select idopciondeterminacion as opc1,ent.iden as ent1,ent.clave as ent1clave,ent.nombre as ent1nombre,pol.geom as geom1 from planeamiento.entidaddeterminacionregimen edr
inner join planeamiento.casoentidaddeterminacion caso on caso.iden=edr.idcaso
inner join planeamiento.entidaddeterminacion ed on caso.identidaddeterminacion=ed.iden
inner join planeamiento.determinacion det on ed.iddeterminacion=det.iden
inner join planeamiento.entidad ent on ed.identidad=ent.iden
inner join planeamiento.tramite trm on ent.idtramite=trm.iden
inner join planeamiento.entidadpol pol on pol.identidad=ent.iden
where edr.idopciondeterminacion is not null 
and det.idcaracter=20
and upper(trim(trm.codigofip))=upper(trim(:codigoFip))
group by edr.idopciondeterminacion,ent.iden,pol.geom,ent.clave,ent.nombre
) as entidad1,
(select idopciondeterminacion as opc2,ent.iden as ent2,ent.clave as ent2clave,ent.nombre as ent2nombre, pol.geom as geom2 from planeamiento.entidaddeterminacionregimen edr
inner join planeamiento.casoentidaddeterminacion caso on caso.iden=edr.idcaso
inner join planeamiento.entidaddeterminacion ed on caso.identidaddeterminacion=ed.iden
inner join planeamiento.determinacion det on ed.iddeterminacion=det.iden
inner join planeamiento.entidad ent on ed.identidad=ent.iden
inner join planeamiento.tramite trm on ent.idtramite=trm.iden
inner join planeamiento.entidadpol pol on pol.identidad=ent.iden
where edr.idopciondeterminacion is not null
and det.idcaracter=20
and upper(trim(trm.codigofip))=upper(trim(:codigoFip))
group by edr.idopciondeterminacion,ent.iden,pol.geom,ent.clave,ent.nombre
) as entidad2
where entidad2.opc2=entidad1.opc1
and entidad2.ent2<>entidad1.ent1
and ST_Relate(entidad1.geom1,entidad2.geom2,''T********'')', 'null', 'entidad.codigo', 'Entidad Solapante: 
	Nombre: %2
	Clave: %3

Entidad Solapada:
	Nombre: %5
	Clave: %6');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (30, 3, 1, true, 'Ent003', 'Toda Entidad debe tener una única asignación de una Determinación con carácter ‘Grupo de Entidad’, con un único caso y valor de referencia', 'Select Distinct  E.nombre as ent_nombre 
From planeamiento.Entidad E, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip)) 
And E.idTramite=T.iden
And (E.iden, 1, 1) Not In (
Select ED.idEntidad, Count(CED.iden), Count(EDR.iden) From planeamiento.EntidadDeterminacion ED,
planeamiento.Determinacion D, planeamiento.CasoEntidadDeterminacion CED,
planeamiento.EntidadDeterminacionRegimen EDR 
Where (EDR.idCaso=CED.iden Or EDR.idCasoAplicacion=CED.iden)
And CED.idEntidadDeterminacion=ED.iden And ED.idDeterminacion=D.iden And D.idCaracter=20
Group By ED.idEntidad Having Count(*)=1)', 'null', 'entidad.codigo', 'Entidad: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (11, 3, 1, true, 'Ope001', 'Toda operación de Creación de Adscripción debe tener asignadas propiedades de Adscripción (Tipo, Cuantía y Unidad).', 'Select Distinct EO.iden as ent_iden, EO.nombre as ent_nombre, EO.clave as ent_clave, cast(EO.codigo as varchar) as ent_codigo,
ED.iden as entd_iden, ED.nombre as entd_nombre, ED.clave as entd_clave, cast(ED.codigo as varchar) as entd_codigo
From planeamiento.Relacion R,
planeamiento.PropiedadRelacion PR, 
planeamiento.VectorRelacion VRO, planeamiento.VectorRelacion VRD,
planeamiento.VectorRelacion VRU, planeamiento.VectorRelacion VRT,
planeamiento.Entidad EO, planeamiento.Entidad ED, planeamiento.Tramite T
Where R.idTramiteCreador=T.iden 
And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And R.idDefRelacion=3 
And PR.idDefPropiedad=3 And PR.idRelacion=R.iden
And VRO.idDefVector=4 And VRO.idRelacion=R.iden And VRO.valor=EO.iden
And VRD.idDefVector=5 And VRD.idRelacion=R.iden And VRD.valor=ED.iden
And VRU.idDefVector=6 And VRU.idRelacion=R.iden
And VRT.idDefVector=7 And VRT.idRelacion=R.iden
And (
(Trim(Coalesce(PR.valor, ''''))='''')
Or
(VRT.valor Is Null)
Or
(VRU.valor Is Null)
)', 'null', 'entidad.origen.codigo,entidad.destino.codigo', 'Entidad Origen: 
	Nombre: %2
	Clave: %3
Entidad Destino: 
	Nombre: %6
	Clave: %7');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (58, 6, 1, true, 'Ope003', 'Las operaciones entre Determinaciones sólo pueden efectuarse entre los caracteres permitidos en el diccionario.', 'Select Distinct OPD.iden, TRD.texto, 
D1.iden as det_iden, D1.nombre as det_nombre, D1.apartado as det_articulado, cast(D1.codigo as varchar) as det_codigo,
D2.iden as det_iden, D2.nombre as det_nombre, D2.apartado as det_articulado, cast(D2.codigo as varchar) as det_codigo
From planeamiento.Determinacion D1, planeamiento.Tramite T, planeamiento.Determinacion D2,
planeamiento.OperacionDeterminacion OPD, diccionario.TipoOperacionDeterminacion TOD, diccionario.Traduccion TRD, 
planeamiento.Operacion OP
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And OP.idTramiteOrdenante=T.iden
And OPD.idOperacion=OP.iden And OPD.idTipoOperacionDet=TOD.iden And TOD.idLiteral=TRD.idLiteral
And OPD.idDeterminacion=D1.iden And OPD.idDeterminacionOperadora=D2.iden
And (D1.idCaracter, D2.idCaracter, OPD.idTipoOperacionDet) Not In (Select OC.idCaracterOperado,
OC.idCaracterOperador, OC.idTipoOperacionDet From diccionario.OperacionCaracter OC)', 'null', 'operaciondeterminacion.iden,traduccion.texto,determinacion.codigo,determinacion.codigo1', 'Operación: %2

Determinacion Operadora: 
	Nombre: %4
	Articulado: %5

Determinacion Operada: 
	Nombre: %8
	Articulado: %9');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (34, 2, 1, true, 'Det004', 'Las Determinaciones que se asignan como Regulación de otra determinación, deben tener carácter de ‘Regulación’', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
 From planeamiento.Determinacion D, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden
And D.idCaracter<>13
And D.iden In (Select VR.valor From planeamiento.VectorRelacion VR
Where VR.idDefVector=Any(Array[9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39]))', 'null', 'determinacion.codigo', 'Determinación usada como reguladora:
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (64, 6, 1, true, 'Ope005', 'No pueden existir operaciones de Creación de Adscripción donde el Trámite ordenante y el Trámite de las entidades afectadas sean el mismo Trámite.', 'Select Distinct E1.iden as ent_iden, E1.nombre as ent_nombre, E1.clave as ent_clave, cast(E1.codigo as varchar) as ent_codigo,
E2.iden as ent_iden, E2.nombre as ent_nombre, E2.clave as ent_clave, cast(E2.codigo as varchar) as ent_codigo 
From planeamiento.Relacion R, planeamiento.Tramite T, planeamiento.VectorRelacion VR1, planeamiento.VectorRelacion VR2,
planeamiento.Entidad E1, planeamiento.Entidad E2, planeamiento.OperacionRelacion OPR
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And R.idTramiteCreador=T.iden And R.idDefRelacion=3
And VR1.idRelacion=R.iden And VR2.idRelacion=R.iden
And VR1.idDefVector=4 And VR2.idDefVector=5
And VR1.valor=E1.iden And VR2.valor=E2.iden
And E1.idTramite=E2.idTramite
And OPR.idRelacion=R.iden And OPR.idTipoOperacionRel=2
And T.iden = E1.idTramite And T.iden = E2.idTramite', 'null', 'entidad.codigo,entidad.codigo', 'Operacion entre entidades: 

Entidad Operadora: 
	Nombre: %2
	Clave: %3

Entidad Operada: 
	Nombre: %6
	Clave: %7');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (27, 3, 1, true, 'Ent001', 'Las entidades usadas como entidades base, deben pertenecer al plan base.', 'Select ent.nombre as ent_nombre, ent.clave as ent_clave,ent.etiqueta as ent_etiqueta, ent.iden as ent_iden, cast(ent.codigo as varchar) as ent_codigo,
ent_base.nombre as entidad_basenom, ent_base.clave as entidad_basecla, ent_base.etiqueta as entidad_baseeti, ent_base.codigo as entidad_basecod,tram_base.codigofip as tram_base
from planeamiento.entidad ent
inner join planeamiento.tramite tram on ent.idtramite=tram.iden
inner join planeamiento.entidad ent_base on ent.identidadbase=ent_base.iden
inner join planeamiento.tramite tram_base on ent_base.idtramite=tram_base.iden
where tram_base.idTipoTramite<>15
and Upper(Trim(tram.codigoFIP))=Upper(Trim(:codigoFip)) ', 'null', 'entidad.codigo,entidadbase.codigo,tramitebase.codigo', 'Entidad: 
	Nombre: %1
	Clave: %2
	Etiqueta: %3

Entidad base: 
	Nombre: %6
	Clave: %7
	Etiqueta: %8

Trámite: %10');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (68, 3, 1, true, 'Ads002', 'Una Entidad solo puede ser Origen o Destino de una Adscripción pero no ambas cosas simultáneamente ni usarse de modo invertido ', 'Select entOrigen.iden as ent_iden, entOrigen.nombre as ent_nombre, entOrigen.clave as ent_clave, cast(entOrigen.codigo as varchar) as ent_codigo
 from planeamiento.relacion rel
inner join planeamiento.tramite trm on rel.idtramitecreador=trm.iden
inner join planeamiento.vectorrelacion vecOrigen on rel.iden=vecOrigen.idrelacion and vecOrigen.iddefvector=4
inner join planeamiento.entidad entOrigen on entOrigen.iden=vecOrigen.valor
inner join planeamiento.vectorrelacion vecDestino on rel.iden=vecDestino.idrelacion and vecDestino.iddefvector=5
inner join planeamiento.entidad entDestino on entDestino.iden=vecDestino.valor
 where rel.iddefrelacion=3 and (entOrigen.iden = entDestino.iden)
 and Upper(Trim(trm.codigoFIP))=Upper(Trim(:codigoFip))
Union
select entidad.iden as ent_iden, entidad.nombre as ent_nombre, entidad.clave as ent_clave, cast(entidad.codigo as varchar) as ent_codigo
 from planeamiento.relacion rel
inner join planeamiento.tramite trm on rel.idtramitecreador=trm.iden
inner join planeamiento.vectorrelacion vecOrigen on rel.iden=vecOrigen.idrelacion and vecOrigen.iddefvector =4 AND	
	vecOrigen.valor in(
    	select vecDestino.valor from  planeamiento.vectorrelacion vecDestino 
        	inner join planeamiento.relacion relDestino on relDestino.iden=vecDestino.idrelacion
            inner join planeamiento.tramite trmSub on relDestino.idtramitecreador=trmSub.iden
            where vecDestino.iddefvector =5
            and Upper(Trim(trmSub.codigoFIP))=Upper(Trim(:codigoFip))
    )
 inner join planeamiento.entidad on vecOrigen.valor=entidad.iden
 where rel.iddefrelacion=3 
 and Upper(Trim(trm.codigoFIP))=Upper(Trim(:codigoFip))', 'null', 'entidad.codigo', 'Entidad: 
	Nombre: %2
	Clave: %3');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (36, 2, 1, true, 'Det006', 'Si la Determinación tiene carácter de Enunciado, Valor de Referencia, Tipo de Adscripción, Unidad, o Regulación, no tendrá asignados Grupos de Entidad de aplicación', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
 From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.EntidadDeterminacion ED
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idDeterminacion=D.iden
And D.idCaracter=Any(Array[1, 11, 12, 13, 18, 19])
Union
Select D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.DeterminacionGrupoEntidad DGE
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And DGE.idDeterminacion=D.iden
And D.idCaracter=Any(Array[1, 11, 12, 13, 18, 19])', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (38, 2, 1, true, 'Det008', 'Si la Determinación es Norma General, Acto, Uso, Grupo de Actos, Grupo de Usos,  Régimen de Acto, o  Régimen de Uso, tendrá, al menos, un Grupo de Entidad de aplicación', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo 
From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.EntidadDeterminacion ED,
planeamiento.CasoEntidadDeterminacion CED, planeamiento.EntidadDeterminacionRegimen EDR
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idDeterminacion=D.iden
And D.idCaracter<>All(Array[2, 7, 8, 17, 20])
And EDR.idCaso=CED.iden
And CED.idEntidadDeterminacion=ED.iden
And EDR.idOpcionDeterminacion Is Not Null
And EDR.idDeterminacionRegimen Is Null
Union
Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.OpcionDeterminacion OD
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idCaracter<>All(Array[2, 7, 8, 17, 20])
And OD.idDeterminacion=D.iden', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (37, 2, 1, true, 'Det007', 'Sólo tendrán Valor de Referencia las Determinaciones con carácter de Régimen de Uso, Régimen de Acto,  Operadora, Norma General o Grupo de Entidad', 'Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo 
From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.EntidadDeterminacion ED,
planeamiento.CasoEntidadDeterminacion CED, planeamiento.EntidadDeterminacionRegimen EDR
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idDeterminacion=D.iden
And D.idCaracter<>All(Array[2, 7, 8, 17, 20])
And EDR.idCaso=CED.iden
And CED.idEntidadDeterminacion=ED.iden
And EDR.idOpcionDeterminacion Is Not Null
And EDR.idDeterminacionRegimen Is Null
Union
Select Distinct D.nombre as det_nombre, D.apartado as det_articulado, cast(D.codigo as varchar) as det_codigo
From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.OpcionDeterminacion OD
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idCaracter<>All(Array[2, 7, 8, 17, 20])
And OD.idDeterminacion=D.iden', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (6, 2, 1, true, 'Det011', 'No pueden existir dos Determinaciones hijas al mismo nivel con el mismo nombre, caracter y unidad', 'Select Distinct D1.nombre as det_nombre, D1.apartado as det_articulado, cast(D1.codigo as varchar) as det_codigo
 From planeamiento.Determinacion D1, planeamiento.Tramite T, planeamiento.Determinacion D2
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D1.idTramite=T.iden And D2.idTramite=T.iden
And D1.idCaracter=D2.idCaracter And Coalesce(D1.idPadre, 0)=Coalesce(D2.idPadre, 0)
And Trim(Upper(Coalesce(D1.nombre, '''')))=Trim(Upper(Coalesce(D2.nombre, '''')))
And D1.iden<>D2.iden
And (D1.iden, D2.iden) In (Select VR1.valor, VR3.valor From planeamiento.VectorRelacion VR1, planeamiento.VectorRelacion VR2,
planeamiento.VectorRelacion VR3, planeamiento.VectorRelacion VR4
Where VR1.idRelacion=VR2.idRelacion And VR3.idRelacion=VR4.idRelacion
And VR1.valor=D1.iden And VR3.valor=D2.iden And VR2.valor=VR4.valor
And VR1.idDefVector=2 And VR3.idDefVector=2 And VR2.idDefVector=3 And VR4.idDefVector=3)', 'null', 'determinacion.codigo', 'Determinación: 
	Nombre: %1
	Articulado: %2');


--
-- TOC entry 3429 (class 2606 OID 60740)
-- Dependencies: 3100 3100
-- Name: pk_error; Type: CONSTRAINT; Schema: validacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY error
    ADD CONSTRAINT pk_error PRIMARY KEY (iden);


--
-- TOC entry 3431 (class 2606 OID 60742)
-- Dependencies: 3102 3102
-- Name: pk_procesovalidacion; Type: CONSTRAINT; Schema: validacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY proceso
    ADD CONSTRAINT pk_procesovalidacion PRIMARY KEY (iden);


--
-- TOC entry 3433 (class 2606 OID 60744)
-- Dependencies: 3103 3103 3103
-- Name: pk_resultado; Type: CONSTRAINT; Schema: validacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY resultado
    ADD CONSTRAINT pk_resultado PRIMARY KEY (idproceso, idvalidacion);


--
-- TOC entry 3435 (class 2606 OID 60746)
-- Dependencies: 3105 3105
-- Name: pk_validacion; Type: CONSTRAINT; Schema: validacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY validacion
    ADD CONSTRAINT pk_validacion PRIMARY KEY (iden);


--
-- TOC entry 3436 (class 2606 OID 62782)
-- Dependencies: 3432 3103 3103 3100 3100
-- Name: fk5c4d208e0950d69; Type: FK CONSTRAINT; Schema: validacion; Owner: postgres
--

ALTER TABLE ONLY error
    ADD CONSTRAINT fk5c4d208e0950d69 FOREIGN KEY (idproceso, idvalidacion) REFERENCES resultado(idproceso, idvalidacion);


--
-- TOC entry 3438 (class 2606 OID 62787)
-- Dependencies: 3434 3105 3103
-- Name: fk938bdf6f3b6868ca; Type: FK CONSTRAINT; Schema: validacion; Owner: postgres
--

ALTER TABLE ONLY resultado
    ADD CONSTRAINT fk938bdf6f3b6868ca FOREIGN KEY (idvalidacion) REFERENCES validacion(iden);


--
-- TOC entry 3439 (class 2606 OID 62792)
-- Dependencies: 3430 3102 3103
-- Name: fk938bdf6ff2db3420; Type: FK CONSTRAINT; Schema: validacion; Owner: postgres
--

ALTER TABLE ONLY resultado
    ADD CONSTRAINT fk938bdf6ff2db3420 FOREIGN KEY (idproceso) REFERENCES proceso(iden);


--
-- TOC entry 3440 (class 2606 OID 62797)
-- Dependencies: 3430 3102 3103
-- Name: fk_proceso; Type: FK CONSTRAINT; Schema: validacion; Owner: postgres
--

ALTER TABLE ONLY resultado
    ADD CONSTRAINT fk_proceso FOREIGN KEY (idproceso) REFERENCES proceso(iden);


--
-- TOC entry 3437 (class 2606 OID 62802)
-- Dependencies: 3432 3103 3103 3100 3100
-- Name: fk_resultado; Type: FK CONSTRAINT; Schema: validacion; Owner: postgres
--

ALTER TABLE ONLY error
    ADD CONSTRAINT fk_resultado FOREIGN KEY (idproceso, idvalidacion) REFERENCES resultado(idproceso, idvalidacion);


--
-- TOC entry 3441 (class 2606 OID 62807)
-- Dependencies: 3434 3105 3103
-- Name: fk_validacion; Type: FK CONSTRAINT; Schema: validacion; Owner: postgres
--

ALTER TABLE ONLY resultado
    ADD CONSTRAINT fk_validacion FOREIGN KEY (idvalidacion) REFERENCES validacion(iden);


-- Completed on 2013-07-09 13:24:56

--
-- PostgreSQL database dump complete
--

