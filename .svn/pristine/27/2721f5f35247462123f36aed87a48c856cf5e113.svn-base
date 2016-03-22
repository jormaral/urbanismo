--
-- PostgreSQL database dump
--

-- Started on 2012-01-17 14:01:14

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 10 (class 2615 OID 18720)
-- Name: diccionario; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA diccionario;


ALTER SCHEMA diccionario OWNER TO postgres;

--
-- TOC entry 6 (class 2615 OID 18721)
-- Name: explotacion; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA explotacion;


ALTER SCHEMA explotacion OWNER TO postgres;

--
-- TOC entry 14 (class 2615 OID 32774)
-- Name: ficha; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA ficha;


ALTER SCHEMA ficha OWNER TO postgres;

--
-- TOC entry 8 (class 2615 OID 18722)
-- Name: planeamiento; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA planeamiento;


ALTER SCHEMA planeamiento OWNER TO postgres;

--
-- TOC entry 9 (class 2615 OID 18723)
-- Name: seguridad; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA seguridad;


ALTER SCHEMA seguridad OWNER TO postgres;

--
-- TOC entry 11 (class 2615 OID 23975)
-- Name: validacion; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA validacion;


ALTER SCHEMA validacion OWNER TO postgres;

--
-- TOC entry 3975 (class 0 OID 0)
-- Dependencies: 11
-- Name: SCHEMA validacion; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA validacion IS 'standard public schema';


SET search_path = diccionario, pg_catalog;

--
-- TOC entry 2723 (class 1259 OID 18725)
-- Dependencies: 10
-- Name: diccionario_ambito_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_ambito_iden_seq
    START WITH 8322
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_ambito_iden_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 2724 (class 1259 OID 18727)
-- Dependencies: 3363 10
-- Name: ambito; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE ambito (
    iden integer DEFAULT nextval('diccionario_ambito_iden_seq'::regclass) NOT NULL,
    idtipoambito integer NOT NULL,
    codigoine character varying(6),
    idliteral integer
);


ALTER TABLE diccionario.ambito OWNER TO postgres;

--
-- TOC entry 2725 (class 1259 OID 18731)
-- Dependencies: 10
-- Name: diccionario_ambitoshp_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_ambitoshp_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_ambitoshp_iden_seq OWNER TO postgres;

--
-- TOC entry 2726 (class 1259 OID 18733)
-- Dependencies: 3364 3365 3366 3367 981 10
-- Name: ambitoshp; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE ambitoshp (
    idambito integer,
    iden integer DEFAULT nextval('diccionario_ambitoshp_iden_seq'::regclass) NOT NULL,
    geom public.geometry NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(geom) = 'MULTIPOLYGON'::text) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.srid(geom) = (-1)))
);


ALTER TABLE diccionario.ambitoshp OWNER TO postgres;

--
-- TOC entry 2727 (class 1259 OID 18743)
-- Dependencies: 10
-- Name: diccionario_boletin_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_boletin_iden_seq
    START WITH 4
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_boletin_iden_seq OWNER TO postgres;

--
-- TOC entry 2728 (class 1259 OID 18745)
-- Dependencies: 3368 10
-- Name: boletin; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE boletin (
    iden integer DEFAULT nextval('diccionario_boletin_iden_seq'::regclass) NOT NULL,
    idliteral integer
);


ALTER TABLE diccionario.boletin OWNER TO postgres;

--
-- TOC entry 2729 (class 1259 OID 18749)
-- Dependencies: 10
-- Name: diccionario_caracterdeterminacion_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_caracterdeterminacion_iden_seq
    START WITH 21
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_caracterdeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2730 (class 1259 OID 18751)
-- Dependencies: 3369 10
-- Name: caracterdeterminacion; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE caracterdeterminacion (
    iden integer DEFAULT nextval('diccionario_caracterdeterminacion_iden_seq'::regclass) NOT NULL,
    nmaxaplicaciones integer,
    nminaplicaciones integer,
    idliteral integer
);


ALTER TABLE diccionario.caracterdeterminacion OWNER TO postgres;

--
-- TOC entry 2731 (class 1259 OID 18755)
-- Dependencies: 10
-- Name: diccionario_centroproduccion_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_centroproduccion_iden_seq
    START WITH 2
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_centroproduccion_iden_seq OWNER TO postgres;

--
-- TOC entry 2732 (class 1259 OID 18757)
-- Dependencies: 3370 10
-- Name: centroproduccion; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE centroproduccion (
    iden integer DEFAULT nextval('diccionario_centroproduccion_iden_seq'::regclass) NOT NULL,
    codigo character(5) NOT NULL,
    idliteral integer,
    passwordmd5 character varying(32),
    correo character varying(40)
);


ALTER TABLE diccionario.centroproduccion OWNER TO postgres;

--
-- TOC entry 2733 (class 1259 OID 18761)
-- Dependencies: 10
-- Name: diccionario_defpropiedad_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_defpropiedad_iden_seq
    START WITH 9
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_defpropiedad_iden_seq OWNER TO postgres;

--
-- TOC entry 2734 (class 1259 OID 18763)
-- Dependencies: 3371 10
-- Name: defpropiedad; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE defpropiedad (
    iden integer DEFAULT nextval('diccionario_defpropiedad_iden_seq'::regclass) NOT NULL,
    iddefrelacion integer NOT NULL,
    idtipodefpropiedad integer NOT NULL,
    idliteral integer NOT NULL
);


ALTER TABLE diccionario.defpropiedad OWNER TO postgres;

--
-- TOC entry 2735 (class 1259 OID 18767)
-- Dependencies: 10
-- Name: diccionario_defrelacion_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_defrelacion_iden_seq
    START WITH 22
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_defrelacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2736 (class 1259 OID 18769)
-- Dependencies: 3372 10
-- Name: defrelacion; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE defrelacion (
    iden integer DEFAULT nextval('diccionario_defrelacion_iden_seq'::regclass) NOT NULL,
    nmininstancias integer NOT NULL,
    nmaxinstancias integer NOT NULL,
    idliteral integer NOT NULL
);


ALTER TABLE diccionario.defrelacion OWNER TO postgres;

--
-- TOC entry 2737 (class 1259 OID 18773)
-- Dependencies: 10
-- Name: diccionario_defvector_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_defvector_iden_seq
    START WITH 43
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_defvector_iden_seq OWNER TO postgres;

--
-- TOC entry 2738 (class 1259 OID 18775)
-- Dependencies: 3373 3374 10
-- Name: defvector; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE defvector (
    iden integer DEFAULT nextval('diccionario_defvector_iden_seq'::regclass) NOT NULL,
    iddefrelacion integer NOT NULL,
    idtabla integer NOT NULL,
    idliteral integer NOT NULL,
    basignacion boolean DEFAULT false NOT NULL
);


ALTER TABLE diccionario.defvector OWNER TO postgres;

--
-- TOC entry 2739 (class 1259 OID 18780)
-- Dependencies: 10
-- Name: diccionario_grupodocumento_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_grupodocumento_iden_seq
    START WITH 11
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_grupodocumento_iden_seq OWNER TO postgres;

--
-- TOC entry 2740 (class 1259 OID 18782)
-- Dependencies: 3375 10
-- Name: grupodocumento; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE grupodocumento (
    iden integer DEFAULT nextval('diccionario_grupodocumento_iden_seq'::regclass) NOT NULL,
    idliteral integer
);


ALTER TABLE diccionario.grupodocumento OWNER TO postgres;

--
-- TOC entry 2741 (class 1259 OID 18786)
-- Dependencies: 10
-- Name: diccionario_instrumentoplan_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_instrumentoplan_iden_seq
    START WITH 195
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_instrumentoplan_iden_seq OWNER TO postgres;

--
-- TOC entry 2742 (class 1259 OID 18788)
-- Dependencies: 3376 10
-- Name: instrumentoplan; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE instrumentoplan (
    iden integer DEFAULT nextval('diccionario_instrumentoplan_iden_seq'::regclass) NOT NULL,
    nemo character varying(12) NOT NULL,
    idnaturaleza integer,
    idliteral integer
);


ALTER TABLE diccionario.instrumentoplan OWNER TO postgres;

--
-- TOC entry 2743 (class 1259 OID 18792)
-- Dependencies: 10
-- Name: diccionario_instrumentotipooperacionplan_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_instrumentotipooperacionplan_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_instrumentotipooperacionplan_iden_seq OWNER TO postgres;

--
-- TOC entry 2744 (class 1259 OID 18794)
-- Dependencies: 3377 10
-- Name: instrumentotipooperacionplan; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE instrumentotipooperacionplan (
    iden integer DEFAULT nextval('diccionario_instrumentotipooperacionplan_iden_seq'::regclass) NOT NULL,
    idinstrumentoplan integer NOT NULL,
    idtipooperacionplan integer NOT NULL
);


ALTER TABLE diccionario.instrumentotipooperacionplan OWNER TO postgres;

--
-- TOC entry 2745 (class 1259 OID 18798)
-- Dependencies: 10
-- Name: diccionario_legislacion_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_legislacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_legislacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2746 (class 1259 OID 18800)
-- Dependencies: 3378 10
-- Name: legislacion; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE legislacion (
    iden integer DEFAULT nextval('diccionario_legislacion_iden_seq'::regclass) NOT NULL,
    idliteral integer NOT NULL
);


ALTER TABLE diccionario.legislacion OWNER TO postgres;

--
-- TOC entry 2747 (class 1259 OID 18804)
-- Dependencies: 10
-- Name: diccionario_literal_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_literal_iden_seq
    START WITH 8460
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_literal_iden_seq OWNER TO postgres;

--
-- TOC entry 2748 (class 1259 OID 18806)
-- Dependencies: 3379 10
-- Name: literal; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE literal (
    iden integer DEFAULT nextval('diccionario_literal_iden_seq'::regclass) NOT NULL,
    comentario character varying(1000)
);


ALTER TABLE diccionario.literal OWNER TO postgres;

--
-- TOC entry 2749 (class 1259 OID 18813)
-- Dependencies: 10
-- Name: diccionario_naturaleza_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_naturaleza_iden_seq
    START WITH 6
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_naturaleza_iden_seq OWNER TO postgres;

--
-- TOC entry 2750 (class 1259 OID 18815)
-- Dependencies: 3380 10
-- Name: naturaleza; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE naturaleza (
    iden integer DEFAULT nextval('diccionario_naturaleza_iden_seq'::regclass) NOT NULL,
    idliteral integer NOT NULL
);


ALTER TABLE diccionario.naturaleza OWNER TO postgres;

--
-- TOC entry 2751 (class 1259 OID 18819)
-- Dependencies: 10
-- Name: diccionario_operacioncaracter_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_operacioncaracter_iden_seq
    START WITH 284
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_operacioncaracter_iden_seq OWNER TO postgres;

--
-- TOC entry 2752 (class 1259 OID 18821)
-- Dependencies: 3381 10
-- Name: operacioncaracter; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE operacioncaracter (
    iden integer DEFAULT nextval('diccionario_operacioncaracter_iden_seq'::regclass) NOT NULL,
    idcaracteroperado integer NOT NULL,
    idcaracteroperador integer NOT NULL,
    idtipooperaciondet integer
);


ALTER TABLE diccionario.operacioncaracter OWNER TO postgres;

--
-- TOC entry 2753 (class 1259 OID 18825)
-- Dependencies: 10
-- Name: diccionario_organigramaambito_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_organigramaambito_iden_seq
    START WITH 8327
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_organigramaambito_iden_seq OWNER TO postgres;

--
-- TOC entry 2754 (class 1259 OID 18827)
-- Dependencies: 3382 10
-- Name: organigramaambito; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE organigramaambito (
    iden integer DEFAULT nextval('diccionario_organigramaambito_iden_seq'::regclass) NOT NULL,
    idambitohijo integer NOT NULL,
    idambitopadre integer NOT NULL
);


ALTER TABLE diccionario.organigramaambito OWNER TO postgres;

--
-- TOC entry 2755 (class 1259 OID 18831)
-- Dependencies: 10
-- Name: diccionario_organo_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_organo_iden_seq
    START WITH 8
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_organo_iden_seq OWNER TO postgres;

--
-- TOC entry 2756 (class 1259 OID 18833)
-- Dependencies: 3383 10
-- Name: organo; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE organo (
    iden integer DEFAULT nextval('diccionario_organo_iden_seq'::regclass) NOT NULL,
    idliteral integer
);


ALTER TABLE diccionario.organo OWNER TO postgres;

--
-- TOC entry 2757 (class 1259 OID 18837)
-- Dependencies: 10
-- Name: diccionario_sentido_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_sentido_iden_seq
    START WITH 8
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_sentido_iden_seq OWNER TO postgres;

--
-- TOC entry 2758 (class 1259 OID 18839)
-- Dependencies: 3384 10
-- Name: sentido; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE sentido (
    iden integer DEFAULT nextval('diccionario_sentido_iden_seq'::regclass) NOT NULL,
    idliteral integer
);


ALTER TABLE diccionario.sentido OWNER TO postgres;

--
-- TOC entry 2759 (class 1259 OID 18843)
-- Dependencies: 10
-- Name: diccionario_tabla_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_tabla_iden_seq
    START WITH 43
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_tabla_iden_seq OWNER TO postgres;

--
-- TOC entry 2760 (class 1259 OID 18845)
-- Dependencies: 3385 10
-- Name: tabla; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE tabla (
    iden integer DEFAULT nextval('diccionario_tabla_iden_seq'::regclass) NOT NULL,
    nombre character varying(32) NOT NULL,
    clausulawhere character varying(2048) NOT NULL,
    observaciones character varying(255),
    esquema character varying(32) NOT NULL
);


ALTER TABLE diccionario.tabla OWNER TO postgres;

--
-- TOC entry 2761 (class 1259 OID 18852)
-- Dependencies: 10
-- Name: diccionario_tipoambito_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_tipoambito_iden_seq
    START WITH 10
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_tipoambito_iden_seq OWNER TO postgres;

--
-- TOC entry 2762 (class 1259 OID 18854)
-- Dependencies: 3386 10
-- Name: tipoambito; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE tipoambito (
    iden integer DEFAULT nextval('diccionario_tipoambito_iden_seq'::regclass) NOT NULL,
    idpadre integer,
    idliteral integer
);


ALTER TABLE diccionario.tipoambito OWNER TO postgres;

--
-- TOC entry 2763 (class 1259 OID 18858)
-- Dependencies: 10
-- Name: diccionario_tipodefpropiedad_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_tipodefpropiedad_iden_seq
    START WITH 4
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_tipodefpropiedad_iden_seq OWNER TO postgres;

--
-- TOC entry 2764 (class 1259 OID 18860)
-- Dependencies: 3387 10
-- Name: tipodefpropiedad; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE tipodefpropiedad (
    iden integer DEFAULT nextval('diccionario_tipodefpropiedad_iden_seq'::regclass) NOT NULL,
    idliteral integer NOT NULL
);


ALTER TABLE diccionario.tipodefpropiedad OWNER TO postgres;

--
-- TOC entry 2765 (class 1259 OID 18864)
-- Dependencies: 10
-- Name: diccionario_tipodocumento_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_tipodocumento_iden_seq
    START WITH 7
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_tipodocumento_iden_seq OWNER TO postgres;

--
-- TOC entry 2766 (class 1259 OID 18866)
-- Dependencies: 3388 10
-- Name: tipodocumento; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE tipodocumento (
    iden integer DEFAULT nextval('diccionario_tipodocumento_iden_seq'::regclass) NOT NULL,
    idliteral integer
);


ALTER TABLE diccionario.tipodocumento OWNER TO postgres;

--
-- TOC entry 2767 (class 1259 OID 18870)
-- Dependencies: 10
-- Name: diccionario_tipoentidad_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_tipoentidad_iden_seq
    START WITH 4
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_tipoentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 2768 (class 1259 OID 18872)
-- Dependencies: 3389 10
-- Name: tipoentidad; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE tipoentidad (
    iden integer DEFAULT nextval('diccionario_tipoentidad_iden_seq'::regclass) NOT NULL,
    idliteral integer NOT NULL
);


ALTER TABLE diccionario.tipoentidad OWNER TO postgres;

--
-- TOC entry 2769 (class 1259 OID 18876)
-- Dependencies: 10
-- Name: diccionario_tipooperaciondeterminacion_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_tipooperaciondeterminacion_iden_seq
    START WITH 14
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_tipooperaciondeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2770 (class 1259 OID 18878)
-- Dependencies: 3390 10
-- Name: tipooperaciondeterminacion; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE tipooperaciondeterminacion (
    iden integer DEFAULT nextval('diccionario_tipooperaciondeterminacion_iden_seq'::regclass) NOT NULL,
    idliteral integer
);


ALTER TABLE diccionario.tipooperaciondeterminacion OWNER TO postgres;

--
-- TOC entry 2771 (class 1259 OID 18882)
-- Dependencies: 10
-- Name: diccionario_tipooperacionentidad_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_tipooperacionentidad_iden_seq
    START WITH 30
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_tipooperacionentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 2772 (class 1259 OID 18884)
-- Dependencies: 3391 10
-- Name: tipooperacionentidad; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE tipooperacionentidad (
    iden integer DEFAULT nextval('diccionario_tipooperacionentidad_iden_seq'::regclass) NOT NULL,
    idtipoentidad integer NOT NULL,
    idliteral integer NOT NULL
);


ALTER TABLE diccionario.tipooperacionentidad OWNER TO postgres;

--
-- TOC entry 2773 (class 1259 OID 18888)
-- Dependencies: 10
-- Name: diccionario_tipooperacionplan_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_tipooperacionplan_iden_seq
    START WITH 10
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_tipooperacionplan_iden_seq OWNER TO postgres;

--
-- TOC entry 2774 (class 1259 OID 18890)
-- Dependencies: 3392 10
-- Name: tipooperacionplan; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE tipooperacionplan (
    iden integer DEFAULT nextval('diccionario_tipooperacionplan_iden_seq'::regclass) NOT NULL,
    idliteral integer
);


ALTER TABLE diccionario.tipooperacionplan OWNER TO postgres;

--
-- TOC entry 2775 (class 1259 OID 18894)
-- Dependencies: 10
-- Name: diccionario_tipooperacionrelacion_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_tipooperacionrelacion_iden_seq
    START WITH 3
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_tipooperacionrelacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2776 (class 1259 OID 18896)
-- Dependencies: 3393 10
-- Name: tipooperacionrelacion; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE tipooperacionrelacion (
    iden integer DEFAULT nextval('diccionario_tipooperacionrelacion_iden_seq'::regclass) NOT NULL,
    idliteral integer NOT NULL
);


ALTER TABLE diccionario.tipooperacionrelacion OWNER TO postgres;

--
-- TOC entry 2777 (class 1259 OID 18900)
-- Dependencies: 10
-- Name: diccionario_tipotramite_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_tipotramite_iden_seq
    START WITH 16
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_tipotramite_iden_seq OWNER TO postgres;

--
-- TOC entry 2778 (class 1259 OID 18902)
-- Dependencies: 3394 10
-- Name: tipotramite; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE tipotramite (
    iden integer DEFAULT nextval('diccionario_tipotramite_iden_seq'::regclass) NOT NULL,
    idliteral integer
);


ALTER TABLE diccionario.tipotramite OWNER TO postgres;

--
-- TOC entry 2779 (class 1259 OID 18906)
-- Dependencies: 10
-- Name: diccionario_traduccion_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_traduccion_iden_seq
    START WITH 8460
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_traduccion_iden_seq OWNER TO postgres;

--
-- TOC entry 2780 (class 1259 OID 18908)
-- Dependencies: 3395 10
-- Name: traduccion; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE traduccion (
    iden integer DEFAULT nextval('diccionario_traduccion_iden_seq'::regclass) NOT NULL,
    idioma character(2) NOT NULL,
    idliteral integer NOT NULL,
    texto character varying(1000) NOT NULL
);


ALTER TABLE diccionario.traduccion OWNER TO postgres;

--
-- TOC entry 2781 (class 1259 OID 18915)
-- Dependencies: 10
-- Name: diccionario_version_iden_seq; Type: SEQUENCE; Schema: diccionario; Owner: postgres
--

CREATE SEQUENCE diccionario_version_iden_seq
    START WITH 11
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE diccionario.diccionario_version_iden_seq OWNER TO postgres;

--
-- TOC entry 2782 (class 1259 OID 18917)
-- Dependencies: 3396 10
-- Name: version; Type: TABLE; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE TABLE version (
    iden integer DEFAULT nextval('diccionario_version_iden_seq'::regclass) NOT NULL,
    version character varying(20) NOT NULL,
    comentario text NOT NULL
);


ALTER TABLE diccionario.version OWNER TO postgres;

SET search_path = explotacion, pg_catalog;

--
-- TOC entry 2783 (class 1259 OID 18924)
-- Dependencies: 6
-- Name: explotacion_capa_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_capa_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_capa_iden_seq OWNER TO postgres;

--
-- TOC entry 2784 (class 1259 OID 18926)
-- Dependencies: 3397 6
-- Name: capa; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE capa (
    iden integer DEFAULT nextval('explotacion_capa_iden_seq'::regclass) NOT NULL,
    nombre character varying(50) NOT NULL,
    orden integer NOT NULL
);


ALTER TABLE explotacion.capa OWNER TO postgres;

--
-- TOC entry 2785 (class 1259 OID 18930)
-- Dependencies: 6
-- Name: explotacion_capaentidadconjunto_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_capaentidadconjunto_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_capaentidadconjunto_iden_seq OWNER TO postgres;

--
-- TOC entry 2786 (class 1259 OID 18932)
-- Dependencies: 3398 6
-- Name: capaentidadconjunto; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE capaentidadconjunto (
    iden integer DEFAULT nextval('explotacion_capaentidadconjunto_iden_seq'::regclass) NOT NULL,
    idcapa integer NOT NULL,
    identidadconjunto integer NOT NULL
);


ALTER TABLE explotacion.capaentidadconjunto OWNER TO postgres;

--
-- TOC entry 2787 (class 1259 OID 18936)
-- Dependencies: 6
-- Name: explotacion_capaentidadlista_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_capaentidadlista_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_capaentidadlista_iden_seq OWNER TO postgres;

--
-- TOC entry 2788 (class 1259 OID 18938)
-- Dependencies: 3399 6
-- Name: capaentidadlista; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE capaentidadlista (
    iden integer DEFAULT nextval('explotacion_capaentidadlista_iden_seq'::regclass) NOT NULL,
    identidadlista integer NOT NULL,
    idcapa integer NOT NULL
);


ALTER TABLE explotacion.capaentidadlista OWNER TO postgres;

--
-- TOC entry 2789 (class 1259 OID 18942)
-- Dependencies: 6
-- Name: explotacion_capagrupo_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_capagrupo_iden_seq
    START WITH 38
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_capagrupo_iden_seq OWNER TO postgres;

--
-- TOC entry 2790 (class 1259 OID 18944)
-- Dependencies: 3400 6
-- Name: capagrupo; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE capagrupo (
    iden integer DEFAULT nextval('explotacion_capagrupo_iden_seq'::regclass) NOT NULL,
    idcapa integer NOT NULL,
    orden integer NOT NULL,
    codigodeterminaciongrupo character varying(10) NOT NULL
);


ALTER TABLE explotacion.capagrupo OWNER TO postgres;

--
-- TOC entry 2791 (class 1259 OID 18948)
-- Dependencies: 6
-- Name: explotacion_leyenda_al_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_leyenda_al_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_leyenda_al_iden_seq OWNER TO postgres;

--
-- TOC entry 2792 (class 1259 OID 18950)
-- Dependencies: 3401 6
-- Name: leyenda_al; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE leyenda_al (
    iden integer DEFAULT nextval('explotacion_leyenda_al_iden_seq'::regclass) NOT NULL,
    identidadbase integer NOT NULL,
    idcapa integer NOT NULL,
    orden_al character varying(10) NOT NULL
);


ALTER TABLE explotacion.leyenda_al OWNER TO postgres;

--
-- TOC entry 2793 (class 1259 OID 18954)
-- Dependencies: 6
-- Name: explotacion_leyenda_pd_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_leyenda_pd_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_leyenda_pd_iden_seq OWNER TO postgres;

--
-- TOC entry 2794 (class 1259 OID 18956)
-- Dependencies: 3402 6
-- Name: leyenda_pd; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE leyenda_pd (
    iden integer DEFAULT nextval('explotacion_leyenda_pd_iden_seq'::regclass) NOT NULL,
    idcapa integer NOT NULL,
    idpropd integer NOT NULL,
    orden_pd integer NOT NULL
);


ALTER TABLE explotacion.leyenda_pd OWNER TO postgres;

--
-- TOC entry 2795 (class 1259 OID 18960)
-- Dependencies: 6
-- Name: explotacion_leyenda_vt_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_leyenda_vt_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_leyenda_vt_iden_seq OWNER TO postgres;

--
-- TOC entry 2796 (class 1259 OID 18962)
-- Dependencies: 3403 6
-- Name: leyenda_vt; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE leyenda_vt (
    iden integer DEFAULT nextval('explotacion_leyenda_vt_iden_seq'::regclass) NOT NULL,
    idcapa integer NOT NULL,
    iddeterminacionbase integer NOT NULL,
    orden_vt integer NOT NULL
);


ALTER TABLE explotacion.leyenda_vt OWNER TO postgres;

--
-- TOC entry 2797 (class 1259 OID 18966)
-- Dependencies: 6
-- Name: explotacion_procedimiento_gr_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_procedimiento_gr_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_procedimiento_gr_iden_seq OWNER TO postgres;

--
-- TOC entry 2798 (class 1259 OID 18968)
-- Dependencies: 3404 6
-- Name: procedimiento_gr; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE procedimiento_gr (
    iden integer DEFAULT nextval('explotacion_procedimiento_gr_iden_seq'::regclass) NOT NULL,
    idcapagrupo integer NOT NULL,
    idsimbologia integer NOT NULL,
    valortematico character varying(50) NOT NULL,
    orden_gr integer NOT NULL
);


ALTER TABLE explotacion.procedimiento_gr OWNER TO postgres;

--
-- TOC entry 2799 (class 1259 OID 18972)
-- Dependencies: 6
-- Name: explotacion_procedimiento_pd_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_procedimiento_pd_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_procedimiento_pd_iden_seq OWNER TO postgres;

--
-- TOC entry 2800 (class 1259 OID 18974)
-- Dependencies: 3405 6
-- Name: procedimiento_pd; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE procedimiento_pd (
    iden integer DEFAULT nextval('explotacion_procedimiento_pd_iden_seq'::regclass) NOT NULL,
    iddetbasegrupoentidad integer NOT NULL
);


ALTER TABLE explotacion.procedimiento_pd OWNER TO postgres;

--
-- TOC entry 2801 (class 1259 OID 18978)
-- Dependencies: 6
-- Name: explotacion_procedimiento_rg_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_procedimiento_rg_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_procedimiento_rg_iden_seq OWNER TO postgres;

--
-- TOC entry 2802 (class 1259 OID 18980)
-- Dependencies: 3406 6
-- Name: procedimiento_rg; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE procedimiento_rg (
    iden integer DEFAULT nextval('explotacion_procedimiento_rg_iden_seq'::regclass) NOT NULL,
    idopciondeterminacionbase integer NOT NULL,
    idcapa integer NOT NULL,
    orden_rg integer
);


ALTER TABLE explotacion.procedimiento_rg OWNER TO postgres;

--
-- TOC entry 2803 (class 1259 OID 18984)
-- Dependencies: 6
-- Name: explotacion_procedimiento_vt_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_procedimiento_vt_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_procedimiento_vt_iden_seq OWNER TO postgres;

--
-- TOC entry 2804 (class 1259 OID 18986)
-- Dependencies: 3407 6
-- Name: procedimiento_vt; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE procedimiento_vt (
    iden integer DEFAULT nextval('explotacion_procedimiento_vt_iden_seq'::regclass) NOT NULL,
    idcapagrupo integer NOT NULL,
    iddeterminacionbase integer NOT NULL
);


ALTER TABLE explotacion.procedimiento_vt OWNER TO postgres;

--
-- TOC entry 2805 (class 1259 OID 18990)
-- Dependencies: 6
-- Name: explotacion_procedimientotematico_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_procedimientotematico_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_procedimientotematico_iden_seq OWNER TO postgres;

--
-- TOC entry 2806 (class 1259 OID 18992)
-- Dependencies: 3408 6
-- Name: procedimientotematico; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE procedimientotematico (
    iden integer DEFAULT nextval('explotacion_procedimientotematico_iden_seq'::regclass) NOT NULL,
    nombre character varying(50) NOT NULL,
    alias character(2) NOT NULL
);


ALTER TABLE explotacion.procedimientotematico OWNER TO postgres;

--
-- TOC entry 2807 (class 1259 OID 18996)
-- Dependencies: 6
-- Name: explotacion_rangovalor_iden_seq; Type: SEQUENCE; Schema: explotacion; Owner: postgres
--

CREATE SEQUENCE explotacion_rangovalor_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE explotacion.explotacion_rangovalor_iden_seq OWNER TO postgres;

--
-- TOC entry 2808 (class 1259 OID 18998)
-- Dependencies: 3409 6
-- Name: rangovalor; Type: TABLE; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE TABLE rangovalor (
    iden integer DEFAULT nextval('explotacion_rangovalor_iden_seq'::regclass) NOT NULL,
    idcapa integer NOT NULL,
    idplanbase integer NOT NULL,
    valormax double precision NOT NULL,
    idsimbologia integer NOT NULL,
    valortematico character varying(50) NOT NULL
);


ALTER TABLE explotacion.rangovalor OWNER TO postgres;

SET search_path = ficha, pg_catalog;

--
-- TOC entry 2997 (class 1259 OID 32775)
-- Dependencies: 14
-- Name: conjuntodeterminaciongrupo_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE conjuntodeterminaciongrupo_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.conjuntodeterminaciongrupo_iden_seq OWNER TO postgres;

--
-- TOC entry 2998 (class 1259 OID 32777)
-- Dependencies: 3479 14
-- Name: conjuntodeterminaciongrupo; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE conjuntodeterminaciongrupo (
    iden bigint DEFAULT nextval('conjuntodeterminaciongrupo_iden_seq'::regclass) NOT NULL,
    idconjunto bigint NOT NULL,
    iddeterminacion bigint NOT NULL,
    orden bigint NOT NULL
);


ALTER TABLE ficha.conjuntodeterminaciongrupo OWNER TO postgres;

--
-- TOC entry 2999 (class 1259 OID 32781)
-- Dependencies: 14
-- Name: conjuntogrupo_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE conjuntogrupo_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.conjuntogrupo_iden_seq OWNER TO postgres;

--
-- TOC entry 3000 (class 1259 OID 32783)
-- Dependencies: 3480 14
-- Name: conjuntogrupo; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE conjuntogrupo (
    iden bigint DEFAULT nextval('conjuntogrupo_iden_seq'::regclass) NOT NULL,
    nombre character varying(255) NOT NULL
);


ALTER TABLE ficha.conjuntogrupo OWNER TO postgres;

--
-- TOC entry 3001 (class 1259 OID 32787)
-- Dependencies: 14
-- Name: determinacionclasifacto_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE determinacionclasifacto_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.determinacionclasifacto_iden_seq OWNER TO postgres;

--
-- TOC entry 3002 (class 1259 OID 32789)
-- Dependencies: 3481 14
-- Name: determinacionclasifacto; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE determinacionclasifacto (
    iden bigint DEFAULT nextval('determinacionclasifacto_iden_seq'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    iddeterminacion bigint NOT NULL
);


ALTER TABLE ficha.determinacionclasifacto OWNER TO postgres;

--
-- TOC entry 3003 (class 1259 OID 32793)
-- Dependencies: 3482 14
-- Name: determinacionclasifuso; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE determinacionclasifuso (
    iden bigint DEFAULT nextval(('ficha.determinacionclasifuso_iden_seq'::text)::regclass) NOT NULL,
    idficha bigint NOT NULL,
    iddeterminacion bigint NOT NULL
);


ALTER TABLE ficha.determinacionclasifuso OWNER TO postgres;

--
-- TOC entry 3004 (class 1259 OID 32797)
-- Dependencies: 14
-- Name: ficha_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE ficha_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.ficha_iden_seq OWNER TO postgres;

--
-- TOC entry 3005 (class 1259 OID 32799)
-- Dependencies: 3483 14
-- Name: ficha; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE ficha (
    iden bigint DEFAULT nextval('ficha_iden_seq'::regclass) NOT NULL,
    nombre character varying(2000) NOT NULL,
    path character varying(2000) NOT NULL,
    idtramite bigint NOT NULL,
    xpath text NOT NULL
);


ALTER TABLE ficha.ficha OWNER TO postgres;

--
-- TOC entry 3006 (class 1259 OID 32806)
-- Dependencies: 14
-- Name: fichaconjuntogrupo_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE fichaconjuntogrupo_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.fichaconjuntogrupo_iden_seq OWNER TO postgres;

--
-- TOC entry 3007 (class 1259 OID 32808)
-- Dependencies: 3484 14
-- Name: fichaconjuntogrupo; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE fichaconjuntogrupo (
    iden bigint DEFAULT nextval('fichaconjuntogrupo_iden_seq'::regclass) NOT NULL,
    idficha bigint NOT NULL,
    idconjunto bigint NOT NULL,
    orden bigint NOT NULL,
    regimen_directo boolean NOT NULL,
    usos boolean NOT NULL,
    actos boolean NOT NULL
);


ALTER TABLE ficha.fichaconjuntogrupo OWNER TO postgres;

--
-- TOC entry 3008 (class 1259 OID 32812)
-- Dependencies: 3485 14
-- Name: fichagrupodeterminacion; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE fichagrupodeterminacion (
    iden bigint DEFAULT nextval(('ficha.fichagrupodeterminacion_iden_seq'::text)::regclass) NOT NULL,
    idficha bigint NOT NULL,
    idgrupo bigint NOT NULL,
    orden bigint NOT NULL
);


ALTER TABLE ficha.fichagrupodeterminacion OWNER TO postgres;

--
-- TOC entry 3009 (class 1259 OID 32816)
-- Dependencies: 3486 14
-- Name: grupodeterminacion; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE grupodeterminacion (
    iden bigint DEFAULT nextval(('ficha.grupodeterminacion_iden_seq'::text)::regclass) NOT NULL,
    nombre character varying(255) NOT NULL,
    idconjuntogrupo bigint NOT NULL
);


ALTER TABLE ficha.grupodeterminacion OWNER TO postgres;

--
-- TOC entry 3010 (class 1259 OID 32820)
-- Dependencies: 14
-- Name: grupodeterminaciondeterminacion_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE grupodeterminaciondeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.grupodeterminaciondeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3011 (class 1259 OID 32822)
-- Dependencies: 3487 3488 3489 14
-- Name: grupodeterminaciondeterminacion; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE grupodeterminaciondeterminacion (
    iden bigint DEFAULT nextval('grupodeterminaciondeterminacion_iden_seq'::regclass) NOT NULL,
    idgrupo bigint NOT NULL,
    iddeterminacion bigint NOT NULL,
    orden bigint NOT NULL,
    regimenesp boolean DEFAULT true NOT NULL,
    sinvalor boolean DEFAULT false NOT NULL
);


ALTER TABLE ficha.grupodeterminaciondeterminacion OWNER TO postgres;

--
-- TOC entry 3012 (class 1259 OID 32828)
-- Dependencies: 3490 14
-- Name: regimenesacto; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE regimenesacto (
    iden bigint DEFAULT nextval(('ficha.regimenesacto_iden_seq'::text)::regclass) NOT NULL,
    idficha bigint NOT NULL,
    iddeterminacion bigint NOT NULL,
    orden integer NOT NULL
);


ALTER TABLE ficha.regimenesacto OWNER TO postgres;

--
-- TOC entry 3013 (class 1259 OID 32832)
-- Dependencies: 3491 14
-- Name: regimenesuso; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE regimenesuso (
    iden bigint DEFAULT nextval(('ficha.regimenesuso_iden_seq'::text)::regclass) NOT NULL,
    idficha bigint NOT NULL,
    iddeterminacion bigint NOT NULL,
    orden integer NOT NULL
);


ALTER TABLE ficha.regimenesuso OWNER TO postgres;

--
-- TOC entry 3014 (class 1259 OID 32836)
-- Dependencies: 14
-- Name: valoresclasifacto_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE valoresclasifacto_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.valoresclasifacto_iden_seq OWNER TO postgres;

--
-- TOC entry 3015 (class 1259 OID 32838)
-- Dependencies: 3492 14
-- Name: valoresclasifacto; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE valoresclasifacto (
    iden bigint DEFAULT nextval('valoresclasifacto_iden_seq'::regclass) NOT NULL,
    iddeterminacionclasifacto bigint NOT NULL,
    iddeterminacionvalorregimen bigint NOT NULL,
    orden bigint NOT NULL
);


ALTER TABLE ficha.valoresclasifacto OWNER TO postgres;

SET search_path = planeamiento, pg_catalog;

--
-- TOC entry 2809 (class 1259 OID 19002)
-- Dependencies: 8
-- Name: planeamiento_casoentidaddeterminacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_casoentidaddeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_casoentidaddeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2810 (class 1259 OID 19004)
-- Dependencies: 3410 3411 8
-- Name: casoentidaddeterminacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE casoentidaddeterminacion (
    iden integer DEFAULT nextval('planeamiento_casoentidaddeterminacion_iden_seq'::regclass) NOT NULL,
    identidaddeterminacion integer NOT NULL,
    nombre character varying(20),
    orden integer DEFAULT 0 NOT NULL
);


ALTER TABLE planeamiento.casoentidaddeterminacion OWNER TO postgres;

--
-- TOC entry 2811 (class 1259 OID 19009)
-- Dependencies: 8
-- Name: planeamiento_determinacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_determinacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_determinacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2812 (class 1259 OID 19011)
-- Dependencies: 3412 3413 3414 8
-- Name: determinacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE determinacion (
    iden integer DEFAULT nextval('planeamiento_determinacion_iden_seq'::regclass) NOT NULL,
    idtramite integer NOT NULL,
    idpadre integer,
    idcaracter integer NOT NULL,
    apartado character varying(100),
    nombre character varying(220) NOT NULL,
    texto text,
    iddeterminacionbase integer,
    etiqueta character varying(100),
    codigo character(10) NOT NULL,
    iddeterminacionoriginal integer,
    bsuspendida boolean DEFAULT false NOT NULL,
    orden integer DEFAULT 0 NOT NULL
);


ALTER TABLE planeamiento.determinacion OWNER TO postgres;

--
-- TOC entry 2813 (class 1259 OID 19020)
-- Dependencies: 8
-- Name: planeamiento_entidad_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_entidad_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_entidad_iden_seq OWNER TO postgres;

--
-- TOC entry 2814 (class 1259 OID 19022)
-- Dependencies: 3415 3416 3417 8
-- Name: entidad; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE entidad (
    iden integer DEFAULT nextval('planeamiento_entidad_iden_seq'::regclass) NOT NULL,
    idtramite integer NOT NULL,
    idpadre integer,
    nombre character varying(100),
    clave character varying(100),
    identidadbase integer,
    etiqueta character varying(100),
    codigo character(10) NOT NULL,
    identidadoriginal integer,
    orden integer DEFAULT 0 NOT NULL,
    bsuspendida boolean DEFAULT false NOT NULL
);


ALTER TABLE planeamiento.entidad OWNER TO postgres;

--
-- TOC entry 2815 (class 1259 OID 19028)
-- Dependencies: 8
-- Name: planeamiento_entidaddeterminacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_entidaddeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_entidaddeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2816 (class 1259 OID 19030)
-- Dependencies: 3418 8
-- Name: entidaddeterminacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE entidaddeterminacion (
    iden integer DEFAULT nextval('planeamiento_entidaddeterminacion_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    iddeterminacion integer NOT NULL
);


ALTER TABLE planeamiento.entidaddeterminacion OWNER TO postgres;

--
-- TOC entry 2817 (class 1259 OID 19034)
-- Dependencies: 8
-- Name: planeamiento_entidaddeterminacionregimen_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_entidaddeterminacionregimen_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_entidaddeterminacionregimen_iden_seq OWNER TO postgres;

--
-- TOC entry 2818 (class 1259 OID 19036)
-- Dependencies: 3419 8
-- Name: entidaddeterminacionregimen; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE entidaddeterminacionregimen (
    iden integer DEFAULT nextval('planeamiento_entidaddeterminacionregimen_iden_seq'::regclass) NOT NULL,
    idcaso integer NOT NULL,
    iddeterminacionregimen integer,
    idopciondeterminacion integer,
    valor character varying(50),
    superposicion integer,
    idcasoaplicacion integer
);


ALTER TABLE planeamiento.entidaddeterminacionregimen OWNER TO postgres;

--
-- TOC entry 2825 (class 1259 OID 19079)
-- Dependencies: 8
-- Name: planeamiento_opciondeterminacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_opciondeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_opciondeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2826 (class 1259 OID 19081)
-- Dependencies: 3435 8
-- Name: opciondeterminacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE opciondeterminacion (
    iden integer DEFAULT nextval('planeamiento_opciondeterminacion_iden_seq'::regclass) NOT NULL,
    iddeterminacion integer NOT NULL,
    iddeterminacionvalorref integer NOT NULL
);


ALTER TABLE planeamiento.opciondeterminacion OWNER TO postgres;

--
-- TOC entry 2827 (class 1259 OID 19085)
-- Dependencies: 8
-- Name: planeamiento_plan_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_plan_iden_seq
    START WITH 11698
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_plan_iden_seq OWNER TO postgres;

--
-- TOC entry 2828 (class 1259 OID 19087)
-- Dependencies: 3436 3437 8
-- Name: plan; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE plan (
    iden integer DEFAULT nextval('planeamiento_plan_iden_seq'::regclass) NOT NULL,
    idpadre integer,
    nombre character varying(100) NOT NULL,
    codigo character(5) NOT NULL,
    texto text,
    idplanbase integer,
    idambito integer NOT NULL,
    orden integer NOT NULL,
    bsuspendido boolean DEFAULT false NOT NULL
);


ALTER TABLE planeamiento.plan OWNER TO postgres;

--
-- TOC entry 2894 (class 1259 OID 19361)
-- Dependencies: 8
-- Name: planeamiento_regimenespecifico_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_regimenespecifico_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_regimenespecifico_iden_seq OWNER TO postgres;

--
-- TOC entry 2829 (class 1259 OID 19095)
-- Dependencies: 8
-- Name: planeamiento_tramite_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_tramite_iden_seq
    START WITH 12824
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_tramite_iden_seq OWNER TO postgres;

--
-- TOC entry 2895 (class 1259 OID 19363)
-- Dependencies: 3465 8
-- Name: regimenespecifico; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE regimenespecifico (
    iden integer DEFAULT nextval('planeamiento_regimenespecifico_iden_seq'::regclass) NOT NULL,
    identidaddeterminacionregimen integer NOT NULL,
    idpadre integer,
    orden integer NOT NULL,
    nombre character varying(100),
    texto text
);


ALTER TABLE planeamiento.regimenespecifico OWNER TO postgres;

--
-- TOC entry 2830 (class 1259 OID 19097)
-- Dependencies: 3438 3439 3440 8
-- Name: tramite; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE tramite (
    iden integer DEFAULT nextval('planeamiento_tramite_iden_seq'::regclass) NOT NULL,
    idtipotramite integer NOT NULL,
    idorgano integer,
    idsentido integer,
    idplan integer NOT NULL,
    fecha date,
    texto text,
    comentario text,
    numeroacuerdo character varying(20),
    nombre character varying(50),
    idcentroproduccion integer NOT NULL,
    codigofip character(32),
    iteracion integer DEFAULT 1 NOT NULL,
    fechaconsolidacion date,
    bsuspendido boolean DEFAULT false NOT NULL
);


ALTER TABLE planeamiento.tramite OWNER TO postgres;

SET search_path = ficha, pg_catalog;

--
-- TOC entry 3016 (class 1259 OID 32842)
-- Dependencies: 3153 14
-- Name: v_orden_actos; Type: VIEW; Schema: ficha; Owner: postgres
--

CREATE VIEW v_orden_actos AS
    SELECT DISTINCT plan.idambito, ficha.iden, entidad.iden AS identidad, detaplicada.iden AS iddeterminacion, cg.orden AS orden_conjuntogrupo, cdg.orden AS orden_conjuntodetgrupo, entidad.orden AS orden_entidad, entidad.iden AS orden_identidad, detgrupo.orden AS orden_grupo, vca.orden AS orden_valordetclasif, detaplicada.orden AS orden_detaplicada, detaplicada.iden AS orden_iddetaplicada, re.orden AS orden_regimenesp FROM ((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON ((ficha.iden = cg.idficha))) LEFT JOIN conjuntogrupo capa ON ((cg.idconjunto = capa.iden))) LEFT JOIN conjuntodeterminaciongrupo cdg ON ((capa.iden = cdg.idconjunto))) LEFT JOIN planeamiento.determinacion detgrupo ON ((cdg.iddeterminacion = detgrupo.iden))) LEFT JOIN planeamiento.opciondeterminacion odgrupo ON ((detgrupo.iden = odgrupo.iddeterminacionvalorref))) LEFT JOIN planeamiento.entidaddeterminacionregimen edrgrupo ON ((odgrupo.iden = edrgrupo.idopciondeterminacion))) LEFT JOIN planeamiento.casoentidaddeterminacion casogrupo ON ((edrgrupo.idcaso = casogrupo.iden))) LEFT JOIN planeamiento.entidaddeterminacion edgrupo ON ((casogrupo.identidaddeterminacion = edgrupo.iden))) LEFT JOIN planeamiento.entidad ON ((edgrupo.identidad = entidad.iden))) LEFT JOIN planeamiento.entidaddeterminacion edaplicaciones ON ((edgrupo.identidad = edaplicaciones.identidad))) LEFT JOIN planeamiento.determinacion detaplicada ON ((edaplicaciones.iddeterminacion = detaplicada.iden))) LEFT JOIN planeamiento.casoentidaddeterminacion caso ON ((edaplicaciones.iden = caso.identidaddeterminacion))) LEFT JOIN planeamiento.entidaddeterminacionregimen edr ON ((caso.iden = edr.idcaso))) LEFT JOIN planeamiento.regimenespecifico re ON ((edr.iden = re.identidaddeterminacionregimen))) LEFT JOIN planeamiento.opciondeterminacion opcion ON ((edr.idopciondeterminacion = opcion.iden))) LEFT JOIN planeamiento.determinacion detvalor ON ((opcion.iddeterminacionvalorref = detvalor.iden))) LEFT JOIN planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN planeamiento.plan ON ((tramite.idplan = plan.iden))) LEFT JOIN determinacionclasifacto dca ON ((ficha.iden = dca.idficha))) LEFT JOIN planeamiento.determinacion detclasif ON ((dca.iddeterminacion = detclasif.iden))) LEFT JOIN valoresclasifacto vca ON (((dca.iden = vca.iddeterminacionclasifacto) AND (detvalor.iden = vca.iddeterminacionvalorregimen)))) JOIN planeamiento.determinacion detregimen ON (((edr.iddeterminacionregimen = detregimen.iden) AND (detvalor.iden = vca.iddeterminacionvalorregimen)))) WHERE ((vca.iden IS NOT NULL) AND cg.usos) ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden, vca.orden, detaplicada.orden, detaplicada.iden, re.orden, plan.idambito, ficha.iden, entidad.iden, detaplicada.iden;


ALTER TABLE ficha.v_orden_actos OWNER TO postgres;

--
-- TOC entry 3017 (class 1259 OID 32847)
-- Dependencies: 3154 14
-- Name: v_acto; Type: VIEW; Schema: ficha; Owner: postgres
--

CREATE VIEW v_acto AS
    SELECT DISTINCT plan.idambito, ficha.iden, ficha.nombre AS ficha, capa.iden AS idcapa, capa.nombre AS capa, detgrupo.apartado AS apartadogrupo, detgrupo.nombre AS grupo, entidad.iden AS identidad, entidad.clave, entidad.nombre, detaplicada.iden AS iddeterminacion, detaplicada.apartado AS apartadoaplicada, detaplicada.nombre AS determinacionaplicada, caso.iden AS idcaso, caso.nombre AS nombrecaso, edr.iden AS idvalor, CASE WHEN ((edr.valor)::text = ''::text) THEN NULL::character varying ELSE edr.valor END AS valor, detvalor.iden AS iddetvalor, detvalor.apartado AS apartadovalor, detvalor.nombre AS determinacionvalor, detregimen.iden AS iddeterminacionregimen, detregimen.apartado AS apartadoregimen, detregimen.nombre AS determinacionregimen, re.iden AS idregimenespecifico, re.nombre AS regimenespecifico, re.texto AS valorregimenespecifico, detclasif.iden AS iddetclasif, detclasif.apartado AS apartadodetclasif, detclasif.nombre AS determinacionclasif, vca.iden AS id_valordetclasif, cg.orden AS orden_conjuntogrupo, cdg.orden AS orden_conjuntodetgrupo, entidad.orden AS orden_entidad, entidad.iden AS orden_identidad, detgrupo.orden AS orden_grupo, CASE WHEN (vca.iden IS NULL) THEN (SELECT orden_actos.orden_valordetclasif FROM v_orden_actos orden_actos WHERE ((orden_actos.identidad = entidad.iden) AND (orden_actos.iddeterminacion = detaplicada.iden)) LIMIT 1) ELSE vca.orden END AS orden_valordetclasif, detaplicada.orden AS orden_detaplicada, detaplicada.iden AS orden_iddetaplicada, re.orden AS orden_regimenesp, ra.orden AS orden_regimenacto FROM (((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON ((ficha.iden = cg.idficha))) LEFT JOIN conjuntogrupo capa ON ((cg.idconjunto = capa.iden))) LEFT JOIN conjuntodeterminaciongrupo cdg ON ((capa.iden = cdg.idconjunto))) LEFT JOIN planeamiento.determinacion detgrupo ON ((cdg.iddeterminacion = detgrupo.iden))) LEFT JOIN planeamiento.opciondeterminacion odgrupo ON ((detgrupo.iden = odgrupo.iddeterminacionvalorref))) LEFT JOIN planeamiento.entidaddeterminacionregimen edrgrupo ON ((odgrupo.iden = edrgrupo.idopciondeterminacion))) LEFT JOIN planeamiento.casoentidaddeterminacion casogrupo ON ((edrgrupo.idcaso = casogrupo.iden))) LEFT JOIN planeamiento.entidaddeterminacion edgrupo ON ((casogrupo.identidaddeterminacion = edgrupo.iden))) LEFT JOIN planeamiento.entidad ON ((edgrupo.identidad = entidad.iden))) LEFT JOIN planeamiento.entidaddeterminacion edaplicaciones ON ((edgrupo.identidad = edaplicaciones.identidad))) LEFT JOIN planeamiento.determinacion detaplicada ON ((edaplicaciones.iddeterminacion = detaplicada.iden))) LEFT JOIN planeamiento.casoentidaddeterminacion caso ON ((edaplicaciones.iden = caso.identidaddeterminacion))) LEFT JOIN planeamiento.entidaddeterminacionregimen edr ON ((caso.iden = edr.idcaso))) LEFT JOIN planeamiento.regimenespecifico re ON ((edr.iden = re.identidaddeterminacionregimen))) LEFT JOIN planeamiento.opciondeterminacion opcion ON ((edr.idopciondeterminacion = opcion.iden))) LEFT JOIN planeamiento.determinacion detvalor ON ((opcion.iddeterminacionvalorref = detvalor.iden))) LEFT JOIN planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN planeamiento.plan ON ((tramite.idplan = plan.iden))) LEFT JOIN determinacionclasifacto dca ON (((ficha.iden = dca.idficha) AND (dca.iddeterminacion IN (SELECT edrfiltro.iddeterminacionregimen FROM planeamiento.entidaddeterminacionregimen edrfiltro WHERE (edrfiltro.idcaso IN (SELECT casofiltro.iden FROM planeamiento.casoentidaddeterminacion casofiltro WHERE (casofiltro.identidaddeterminacion IN (SELECT edfiltro.iden FROM planeamiento.entidaddeterminacion edfiltro WHERE (edfiltro.identidad = entidad.iden)))))))))) LEFT JOIN planeamiento.determinacion detclasif ON ((dca.iddeterminacion = detclasif.iden))) LEFT JOIN valoresclasifacto vca ON (((dca.iden = vca.iddeterminacionclasifacto) AND (detvalor.iden = vca.iddeterminacionvalorregimen)))) LEFT JOIN regimenesacto ra ON (((ficha.iden = ra.idficha) AND (ra.iddeterminacion = edr.iddeterminacionregimen)))) JOIN planeamiento.determinacion detregimen ON (((edr.iddeterminacionregimen = detregimen.iden) AND ((ra.iddeterminacion = detregimen.iden) OR (detvalor.iden = vca.iddeterminacionvalorregimen))))) WHERE ((detaplicada.idcaracter = 10) AND cg.actos) ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden, CASE WHEN (vca.iden IS NULL) THEN (SELECT orden_actos.orden_valordetclasif FROM v_orden_actos orden_actos WHERE ((orden_actos.identidad = entidad.iden) AND (orden_actos.iddeterminacion = detaplicada.iden)) LIMIT 1) ELSE vca.orden END, detaplicada.orden, detaplicada.iden, caso.iden, vca.iden, ra.orden, re.orden, plan.idambito, ficha.iden, ficha.nombre, capa.iden, capa.nombre, detgrupo.apartado, detgrupo.nombre, entidad.clave, entidad.nombre, detaplicada.apartado, detaplicada.nombre, edr.iden, CASE WHEN ((edr.valor)::text = ''::text) THEN NULL::character varying ELSE edr.valor END, detvalor.iden, detvalor.apartado, detvalor.nombre, detregimen.iden, detregimen.apartado, detregimen.nombre, re.iden, re.nombre, re.texto, detclasif.iden, detclasif.apartado, detclasif.nombre, caso.nombre, entidad.iden, detaplicada.iden;


ALTER TABLE ficha.v_acto OWNER TO postgres;

--
-- TOC entry 3018 (class 1259 OID 32852)
-- Dependencies: 3155 14
-- Name: v_entidadficha; Type: VIEW; Schema: ficha; Owner: postgres
--

CREATE VIEW v_entidadficha AS
    SELECT plan.idambito, ficha.iden AS idficha, ficha.nombre AS ficha, capa.iden AS idcapa, capa.nombre AS capa, detgrupo.apartado AS apartadogrupo, detgrupo.nombre AS grupo, entidad.iden AS identidad, entidad.clave, entidad.nombre, cg.orden AS orden1, cdg.orden AS orden2, entidad.orden AS orden3, entidad.iden AS orden4, detgrupo.orden AS orden5 FROM (((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON ((ficha.iden = cg.idficha))) LEFT JOIN conjuntogrupo capa ON ((cg.idconjunto = capa.iden))) LEFT JOIN conjuntodeterminaciongrupo cdg ON ((capa.iden = cdg.idconjunto))) LEFT JOIN planeamiento.determinacion detgrupo ON ((cdg.iddeterminacion = detgrupo.iden))) LEFT JOIN planeamiento.opciondeterminacion odgrupo ON ((detgrupo.iden = odgrupo.iddeterminacionvalorref))) LEFT JOIN planeamiento.entidaddeterminacionregimen edrgrupo ON ((odgrupo.iden = edrgrupo.idopciondeterminacion))) LEFT JOIN planeamiento.casoentidaddeterminacion casogrupo ON ((edrgrupo.idcaso = casogrupo.iden))) LEFT JOIN planeamiento.entidaddeterminacion edgrupo ON ((casogrupo.identidaddeterminacion = edgrupo.iden))) JOIN planeamiento.entidad ON ((edgrupo.identidad = entidad.iden))) LEFT JOIN planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN planeamiento.plan ON ((tramite.idplan = plan.iden))) ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden;


ALTER TABLE ficha.v_entidadficha OWNER TO postgres;

--
-- TOC entry 3019 (class 1259 OID 32857)
-- Dependencies: 14
-- Name: valoresclasifuso_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE valoresclasifuso_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.valoresclasifuso_iden_seq OWNER TO postgres;

--
-- TOC entry 3020 (class 1259 OID 32859)
-- Dependencies: 3493 14
-- Name: valoresclasifuso; Type: TABLE; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE TABLE valoresclasifuso (
    iden bigint DEFAULT nextval('valoresclasifuso_iden_seq'::regclass) NOT NULL,
    iddeterminacionclasifuso bigint NOT NULL,
    iddeterminacionvalorregimen bigint NOT NULL,
    orden bigint NOT NULL
);


ALTER TABLE ficha.valoresclasifuso OWNER TO postgres;

--
-- TOC entry 3021 (class 1259 OID 32863)
-- Dependencies: 3156 14
-- Name: v_orden_usos; Type: VIEW; Schema: ficha; Owner: postgres
--

CREATE VIEW v_orden_usos AS
    SELECT DISTINCT plan.idambito, ficha.iden, entidad.iden AS identidad, detaplicada.iden AS iddeterminacion, cg.orden AS orden_conjuntogrupo, cdg.orden AS orden_conjuntodetgrupo, entidad.orden AS orden_entidad, entidad.iden AS orden_identidad, detgrupo.orden AS orden_grupo, vcu.orden AS orden_valordetclasif, detaplicada.orden AS orden_detaplicada, detaplicada.iden AS orden_iddetaplicada, re.orden AS orden_regimenesp FROM ((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON ((ficha.iden = cg.idficha))) LEFT JOIN conjuntogrupo capa ON ((cg.idconjunto = capa.iden))) LEFT JOIN conjuntodeterminaciongrupo cdg ON ((capa.iden = cdg.idconjunto))) LEFT JOIN planeamiento.determinacion detgrupo ON ((cdg.iddeterminacion = detgrupo.iden))) LEFT JOIN planeamiento.opciondeterminacion odgrupo ON ((detgrupo.iden = odgrupo.iddeterminacionvalorref))) LEFT JOIN planeamiento.entidaddeterminacionregimen edrgrupo ON ((odgrupo.iden = edrgrupo.idopciondeterminacion))) LEFT JOIN planeamiento.casoentidaddeterminacion casogrupo ON ((edrgrupo.idcaso = casogrupo.iden))) LEFT JOIN planeamiento.entidaddeterminacion edgrupo ON ((casogrupo.identidaddeterminacion = edgrupo.iden))) LEFT JOIN planeamiento.entidad ON ((edgrupo.identidad = entidad.iden))) LEFT JOIN planeamiento.entidaddeterminacion edaplicaciones ON ((edgrupo.identidad = edaplicaciones.identidad))) LEFT JOIN planeamiento.determinacion detaplicada ON ((edaplicaciones.iddeterminacion = detaplicada.iden))) LEFT JOIN planeamiento.casoentidaddeterminacion caso ON ((edaplicaciones.iden = caso.identidaddeterminacion))) LEFT JOIN planeamiento.entidaddeterminacionregimen edr ON ((caso.iden = edr.idcaso))) LEFT JOIN planeamiento.regimenespecifico re ON ((edr.iden = re.identidaddeterminacionregimen))) LEFT JOIN planeamiento.opciondeterminacion opcion ON ((edr.idopciondeterminacion = opcion.iden))) LEFT JOIN planeamiento.determinacion detvalor ON ((opcion.iddeterminacionvalorref = detvalor.iden))) LEFT JOIN planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN planeamiento.plan ON ((tramite.idplan = plan.iden))) LEFT JOIN determinacionclasifuso dcu ON ((ficha.iden = dcu.idficha))) LEFT JOIN planeamiento.determinacion detclasif ON ((dcu.iddeterminacion = detclasif.iden))) LEFT JOIN valoresclasifuso vcu ON (((dcu.iden = vcu.iddeterminacionclasifuso) AND (detvalor.iden = vcu.iddeterminacionvalorregimen)))) JOIN planeamiento.determinacion detregimen ON (((edr.iddeterminacionregimen = detregimen.iden) AND (detvalor.iden = vcu.iddeterminacionvalorregimen)))) WHERE ((vcu.iden IS NOT NULL) AND cg.usos) ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden, vcu.orden, detaplicada.orden, detaplicada.iden, re.orden, plan.idambito, ficha.iden, entidad.iden, detaplicada.iden;


ALTER TABLE ficha.v_orden_usos OWNER TO postgres;

--
-- TOC entry 3022 (class 1259 OID 32868)
-- Dependencies: 3157 14
-- Name: v_regimendirecto; Type: VIEW; Schema: ficha; Owner: postgres
--

CREATE VIEW v_regimendirecto AS
    SELECT plan.idambito, ficha.iden, ficha.nombre AS ficha, capa.iden AS idcapa, capa.nombre AS capa, detgrupo.apartado AS apartadogrupo, detgrupo.nombre AS grupo, entidad.iden AS identidad, entidad.clave, entidad.nombre, entidadbase.nombre AS entidadbase, gd.nombre AS grupodeterminacion, detaplicada.iden AS iddeterminacion, detaplicada.apartado AS apartadoaplicada, detaplicada.nombre AS determinacionaplicada, edr.iden AS idvalor, CASE WHEN ((edr.valor)::text = ''::text) THEN NULL::character varying ELSE edr.valor END AS valor, detvalor.iden AS iddetvalor, detvalor.apartado AS apartadovalor, detvalor.nombre AS determinacionvalor, detcasoaplicacion.apartado AS apartadocasoaplicacion, detcasoaplicacion.nombre AS detcasoaplicacion, CASE WHEN gdd.regimenesp THEN re.iden ELSE NULL::integer END AS idregimenespecifico, CASE WHEN gdd.regimenesp THEN re.nombre ELSE NULL::character varying END AS regimenespecifico, CASE WHEN gdd.regimenesp THEN re.texto ELSE NULL::text END AS valorregimenespecifico FROM (((((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON ((ficha.iden = cg.idficha))) LEFT JOIN conjuntogrupo capa ON ((cg.idconjunto = capa.iden))) LEFT JOIN conjuntodeterminaciongrupo cdg ON ((capa.iden = cdg.idconjunto))) LEFT JOIN planeamiento.determinacion detgrupo ON ((cdg.iddeterminacion = detgrupo.iden))) LEFT JOIN planeamiento.opciondeterminacion odgrupo ON ((detgrupo.iden = odgrupo.iddeterminacionvalorref))) LEFT JOIN planeamiento.entidaddeterminacionregimen edrgrupo ON ((odgrupo.iden = edrgrupo.idopciondeterminacion))) LEFT JOIN planeamiento.casoentidaddeterminacion casogrupo ON ((edrgrupo.idcaso = casogrupo.iden))) LEFT JOIN planeamiento.entidaddeterminacion edgrupo ON ((casogrupo.identidaddeterminacion = edgrupo.iden))) LEFT JOIN planeamiento.entidad ON ((edgrupo.identidad = entidad.iden))) LEFT JOIN planeamiento.entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) LEFT JOIN planeamiento.entidaddeterminacion edaplicaciones ON ((edgrupo.identidad = edaplicaciones.identidad))) LEFT JOIN planeamiento.determinacion detaplicada ON ((edaplicaciones.iddeterminacion = detaplicada.iden))) LEFT JOIN planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN planeamiento.plan ON ((tramite.idplan = plan.iden))) LEFT JOIN planeamiento.casoentidaddeterminacion caso ON ((edaplicaciones.iden = caso.identidaddeterminacion))) LEFT JOIN planeamiento.entidaddeterminacionregimen edr ON ((caso.iden = edr.idcaso))) LEFT JOIN planeamiento.regimenespecifico re ON ((edr.iden = re.identidaddeterminacionregimen))) LEFT JOIN planeamiento.opciondeterminacion opcion ON ((edr.idopciondeterminacion = opcion.iden))) LEFT JOIN planeamiento.determinacion detvalor ON ((opcion.iddeterminacionvalorref = detvalor.iden))) LEFT JOIN planeamiento.casoentidaddeterminacion casoaplicacion ON ((edr.idcasoaplicacion = casoaplicacion.iden))) LEFT JOIN planeamiento.entidaddeterminacion edcasoaplicacion ON ((casoaplicacion.identidaddeterminacion = edcasoaplicacion.iden))) LEFT JOIN planeamiento.determinacion detcasoaplicacion ON ((detcasoaplicacion.iden = edcasoaplicacion.iddeterminacion))) LEFT JOIN fichagrupodeterminacion fgd ON ((ficha.iden = fgd.idficha))) JOIN grupodeterminacion gd ON (((fgd.idgrupo = gd.iden) AND (gd.idconjuntogrupo = capa.iden)))) JOIN grupodeterminaciondeterminacion gdd ON (((gd.iden = gdd.idgrupo) AND (gdd.iddeterminacion = detaplicada.iden)))) WHERE ((detaplicada.idcaracter = 2) AND cg.regimen_directo) ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden, fgd.orden, gdd.orden, re.orden;


ALTER TABLE ficha.v_regimendirecto OWNER TO postgres;

--
-- TOC entry 3023 (class 1259 OID 32873)
-- Dependencies: 3158 14
-- Name: v_uso; Type: VIEW; Schema: ficha; Owner: postgres
--

CREATE VIEW v_uso AS
    SELECT DISTINCT plan.idambito, ficha.iden, ficha.nombre AS ficha, capa.iden AS idcapa, capa.nombre AS capa, detgrupo.apartado AS apartadogrupo, detgrupo.nombre AS grupo, entidad.iden AS identidad, entidad.clave, entidad.nombre, detaplicada.iden AS iddeterminacion, detaplicada.apartado AS apartadoaplicada, detaplicada.nombre AS determinacionaplicada, caso.iden AS idcaso, caso.nombre AS nombrecaso, edr.iden AS idvalor, CASE WHEN ((edr.valor)::text = ''::text) THEN NULL::character varying ELSE edr.valor END AS valor, detvalor.iden AS iddetvalor, detvalor.apartado AS apartadovalor, detvalor.nombre AS determinacionvalor, detregimen.iden AS iddeterminacionregimen, detregimen.apartado AS apartadoregimen, detregimen.nombre AS determinacionregimen, re.iden AS idregimenespecifico, re.nombre AS regimenespecifico, re.texto AS valorregimenespecifico, detclasif.iden AS iddetclasif, detclasif.apartado AS apartadodetclasif, detclasif.nombre AS determinacionclasif, vcu.iden AS id_valordetclasif, cg.orden AS orden_conjuntogrupo, cdg.orden AS orden_conjuntodetgrupo, entidad.orden AS orden_entidad, entidad.iden AS orden_identidad, detgrupo.orden AS orden_grupo, CASE WHEN (vcu.iden IS NULL) THEN (SELECT orden_usos.orden_valordetclasif FROM v_orden_usos orden_usos WHERE ((orden_usos.identidad = entidad.iden) AND (orden_usos.iddeterminacion = detaplicada.iden)) LIMIT 1) ELSE vcu.orden END AS orden_valordetclasif, detaplicada.orden AS orden_detaplicada, detaplicada.iden AS orden_iddetaplicada, re.orden AS orden_regimenesp, ru.orden AS orden_regimenuso FROM (((((((((((((((((((((((ficha LEFT JOIN fichaconjuntogrupo cg ON ((ficha.iden = cg.idficha))) LEFT JOIN conjuntogrupo capa ON ((cg.idconjunto = capa.iden))) LEFT JOIN conjuntodeterminaciongrupo cdg ON ((capa.iden = cdg.idconjunto))) LEFT JOIN planeamiento.determinacion detgrupo ON ((cdg.iddeterminacion = detgrupo.iden))) LEFT JOIN planeamiento.opciondeterminacion odgrupo ON ((detgrupo.iden = odgrupo.iddeterminacionvalorref))) LEFT JOIN planeamiento.entidaddeterminacionregimen edrgrupo ON ((odgrupo.iden = edrgrupo.idopciondeterminacion))) LEFT JOIN planeamiento.casoentidaddeterminacion casogrupo ON ((edrgrupo.idcaso = casogrupo.iden))) LEFT JOIN planeamiento.entidaddeterminacion edgrupo ON ((casogrupo.identidaddeterminacion = edgrupo.iden))) LEFT JOIN planeamiento.entidad ON ((edgrupo.identidad = entidad.iden))) LEFT JOIN planeamiento.entidaddeterminacion edaplicaciones ON ((edgrupo.identidad = edaplicaciones.identidad))) LEFT JOIN planeamiento.determinacion detaplicada ON ((edaplicaciones.iddeterminacion = detaplicada.iden))) LEFT JOIN planeamiento.casoentidaddeterminacion caso ON ((edaplicaciones.iden = caso.identidaddeterminacion))) LEFT JOIN planeamiento.entidaddeterminacionregimen edr ON ((caso.iden = edr.idcaso))) LEFT JOIN planeamiento.regimenespecifico re ON ((edr.iden = re.identidaddeterminacionregimen))) LEFT JOIN planeamiento.opciondeterminacion opcion ON ((edr.idopciondeterminacion = opcion.iden))) LEFT JOIN planeamiento.determinacion detvalor ON ((opcion.iddeterminacionvalorref = detvalor.iden))) LEFT JOIN planeamiento.tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN planeamiento.plan ON ((tramite.idplan = plan.iden))) LEFT JOIN determinacionclasifuso dcu ON (((ficha.iden = dcu.idficha) AND (dcu.iddeterminacion IN (SELECT edrfiltro.iddeterminacionregimen FROM planeamiento.entidaddeterminacionregimen edrfiltro WHERE (edrfiltro.idcaso IN (SELECT casofiltro.iden FROM planeamiento.casoentidaddeterminacion casofiltro WHERE (casofiltro.identidaddeterminacion IN (SELECT edfiltro.iden FROM planeamiento.entidaddeterminacion edfiltro WHERE (edfiltro.identidad = entidad.iden)))))))))) LEFT JOIN planeamiento.determinacion detclasif ON ((dcu.iddeterminacion = detclasif.iden))) LEFT JOIN valoresclasifuso vcu ON (((dcu.iden = vcu.iddeterminacionclasifuso) AND (detvalor.iden = vcu.iddeterminacionvalorregimen)))) LEFT JOIN regimenesuso ru ON (((ficha.iden = ru.idficha) AND (ru.iddeterminacion = edr.iddeterminacionregimen)))) JOIN planeamiento.determinacion detregimen ON (((edr.iddeterminacionregimen = detregimen.iden) AND ((ru.iddeterminacion = detregimen.iden) OR (detvalor.iden = vcu.iddeterminacionvalorregimen))))) WHERE ((detaplicada.idcaracter = 9) AND cg.usos) ORDER BY cg.orden, cdg.orden, entidad.orden, entidad.iden, detgrupo.orden, CASE WHEN (vcu.iden IS NULL) THEN (SELECT orden_usos.orden_valordetclasif FROM v_orden_usos orden_usos WHERE ((orden_usos.identidad = entidad.iden) AND (orden_usos.iddeterminacion = detaplicada.iden)) LIMIT 1) ELSE vcu.orden END, detaplicada.orden, detaplicada.iden, caso.iden, vcu.iden, ru.orden, re.orden, plan.idambito, ficha.iden, ficha.nombre, capa.iden, capa.nombre, detgrupo.apartado, detgrupo.nombre, entidad.clave, entidad.nombre, detaplicada.apartado, detaplicada.nombre, caso.nombre, edr.iden, CASE WHEN ((edr.valor)::text = ''::text) THEN NULL::character varying ELSE edr.valor END, detvalor.iden, detvalor.apartado, detvalor.nombre, detregimen.iden, detregimen.apartado, detregimen.nombre, re.iden, re.nombre, re.texto, detclasif.iden, detclasif.apartado, detclasif.nombre, entidad.iden, detaplicada.iden;


ALTER TABLE ficha.v_uso OWNER TO postgres;

SET search_path = planeamiento, pg_catalog;

--
-- TOC entry 2819 (class 1259 OID 19040)
-- Dependencies: 8
-- Name: planeamiento_entidadlin_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_entidadlin_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_entidadlin_iden_seq OWNER TO postgres;

--
-- TOC entry 2820 (class 1259 OID 19042)
-- Dependencies: 3420 3421 3422 3423 3424 8 981
-- Name: entidadlin; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE entidadlin (
    identidad integer,
    iden integer DEFAULT nextval('planeamiento_entidadlin_iden_seq'::regclass) NOT NULL,
    geom public.geometry,
    bsuspendida boolean DEFAULT false NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(geom) = 'MULTILINESTRING'::text) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.srid(geom) = (-1)))
);


ALTER TABLE planeamiento.entidadlin OWNER TO postgres;

--
-- TOC entry 2821 (class 1259 OID 19053)
-- Dependencies: 8
-- Name: planeamiento_entidadpnt_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_entidadpnt_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_entidadpnt_iden_seq OWNER TO postgres;

--
-- TOC entry 2822 (class 1259 OID 19055)
-- Dependencies: 3425 3426 3427 3428 3429 8 981
-- Name: entidadpnt; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE entidadpnt (
    identidad integer NOT NULL,
    iden integer DEFAULT nextval('planeamiento_entidadpnt_iden_seq'::regclass) NOT NULL,
    geom public.geometry,
    bsuspendida boolean DEFAULT false NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(geom) = 'MULTIPOINT'::text) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.srid(geom) = (-1)))
);


ALTER TABLE planeamiento.entidadpnt OWNER TO postgres;

--
-- TOC entry 2823 (class 1259 OID 19066)
-- Dependencies: 8
-- Name: planeamiento_entidadpol_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_entidadpol_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_entidadpol_iden_seq OWNER TO postgres;

--
-- TOC entry 2824 (class 1259 OID 19068)
-- Dependencies: 3430 3431 3432 3433 3434 981 8
-- Name: entidadpol; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE entidadpol (
    identidad integer NOT NULL,
    iden integer DEFAULT nextval('planeamiento_entidadpol_iden_seq'::regclass) NOT NULL,
    geom public.geometry,
    bsuspendida boolean DEFAULT false NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK ((((public.geometrytype(geom) = 'MULTIPOLYGON'::text) OR (public.geometrytype(geom) = 'POLYGON'::text)) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.srid(geom) = (-1)))
);


ALTER TABLE planeamiento.entidadpol OWNER TO postgres;

--
-- TOC entry 2831 (class 1259 OID 19106)
-- Dependencies: 3104 981 8
-- Name: entidades_capa; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW entidades_capa AS
    SELECT entidad.iden AS identidad, tramite.fechaconsolidacion, capa.nombre AS capa, capa.iden AS idcapa, capagrupo.orden, entidad.idtramite, tramite.codigofip, tramite.idtipotramite, entidad.clave, entidad.nombre AS entidad, determinacion.codigo AS idgrupo, determinacion.nombre AS grupo, determinacion.codigo, plan.idambito, CASE WHEN (entidadpol.geom IS NOT NULL) THEN entidadpol.geom WHEN (entidadlin.geom IS NOT NULL) THEN entidadlin.geom WHEN (entidadpnt.geom IS NOT NULL) THEN entidadpnt.geom ELSE NULL::public.geometry END AS geometria FROM (((((((((((((entidad LEFT JOIN entidaddeterminacion ON ((entidad.iden = entidaddeterminacion.identidad))) LEFT JOIN tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN plan ON ((tramite.idplan = plan.iden))) LEFT JOIN casoentidaddeterminacion ON ((entidaddeterminacion.iden = casoentidaddeterminacion.identidaddeterminacion))) LEFT JOIN entidaddeterminacionregimen ON ((casoentidaddeterminacion.iden = entidaddeterminacionregimen.idcaso))) LEFT JOIN opciondeterminacion ON ((entidaddeterminacionregimen.idopciondeterminacion = opciondeterminacion.iden))) LEFT JOIN determinacion ON ((opciondeterminacion.iddeterminacionvalorref = determinacion.iden))) LEFT JOIN determinacion determinacionaplicada ON ((entidaddeterminacion.iddeterminacion = determinacionaplicada.iden))) LEFT JOIN entidadlin ON ((entidad.iden = entidadlin.identidad))) LEFT JOIN entidadpnt ON ((entidad.iden = entidadpnt.identidad))) LEFT JOIN entidadpol ON ((entidad.iden = entidadpol.identidad))) LEFT JOIN explotacion.capagrupo ON ((determinacion.codigo = (capagrupo.codigodeterminaciongrupo)::bpchar))) LEFT JOIN explotacion.capa ON ((capagrupo.idcapa = capa.iden))) WHERE ((determinacionaplicada.idcaracter = 20) AND (((entidadpnt.geom IS NOT NULL) OR (entidadlin.geom IS NOT NULL)) OR (entidadpol.geom IS NOT NULL))) ORDER BY capa.orden, capagrupo.orden;


ALTER TABLE planeamiento.entidades_capa OWNER TO postgres;

--
-- TOC entry 2832 (class 1259 OID 19111)
-- Dependencies: 3105 981 8
-- Name: Zonas_de_Altura; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW "Zonas_de_Altura" AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 3);


ALTER TABLE planeamiento."Zonas_de_Altura" OWNER TO postgres;

--
-- TOC entry 2833 (class 1259 OID 19115)
-- Dependencies: 3106 8 981
-- Name: Zonas_de_Altura2; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW "Zonas_de_Altura2" AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom, edr.valor FROM (((entidades_capa entidades_capa LEFT JOIN entidaddeterminacion ed ON ((entidades_capa.identidad = ed.identidad))) LEFT JOIN casoentidaddeterminacion ced ON ((ed.iden = ced.identidaddeterminacion))) LEFT JOIN entidaddeterminacionregimen edr ON ((ced.iden = edr.idcaso))) WHERE (((entidades_capa.idcapa = 3) AND (edr.valor IS NOT NULL)) AND (entidades_capa.identidad IN (SELECT entidaddeterminacion.identidad FROM entidaddeterminacion WHERE (entidaddeterminacion.iddeterminacion = 201349))));


ALTER TABLE planeamiento."Zonas_de_Altura2" OWNER TO postgres;

--
-- TOC entry 2834 (class 1259 OID 19120)
-- Dependencies: 3107 981 8
-- Name: calificacion; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW calificacion AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 3);


ALTER TABLE planeamiento.calificacion OWNER TO postgres;

--
-- TOC entry 2835 (class 1259 OID 19124)
-- Dependencies: 3108 8 981
-- Name: Zonas_de_Altura_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW "Zonas_de_Altura_refundido" AS
    SELECT calificacion.idambito, calificacion.identidad, calificacion.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, determinacionbase.nombre AS determinacionbase, determinacionbase.iden AS iddeterminacionbase, calificacion.clave, calificacion.tramite, calificacion.idgrupo, calificacion.grupo, calificacion.geom FROM ((((((((calificacion LEFT JOIN entidad ON ((calificacion.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) LEFT JOIN entidaddeterminacion ON ((entidad.iden = entidaddeterminacion.identidad))) LEFT JOIN casoentidaddeterminacion caso ON ((entidaddeterminacion.iden = caso.identidaddeterminacion))) LEFT JOIN entidaddeterminacionregimen reg ON ((caso.iden = reg.idcaso))) LEFT JOIN opciondeterminacion opc ON ((reg.idopciondeterminacion = opc.iden))) LEFT JOIN determinacion ON ((opc.iddeterminacionvalorref = determinacion.iden))) LEFT JOIN determinacion determinacionbase ON ((determinacion.iddeterminacionbase = determinacionbase.iden))) WHERE ((calificacion.idgrupo <> determinacionbase.codigo) AND (calificacion.idtramite IN (SELECT max(calificacion.idtramite) AS max FROM calificacion calificacion WHERE ((calificacion.idtipotramite = 11) AND (calificacion.fechaconsolidacion IS NOT NULL)) GROUP BY calificacion.idambito)));


ALTER TABLE planeamiento."Zonas_de_Altura_refundido" OWNER TO postgres;

--
-- TOC entry 2836 (class 1259 OID 19129)
-- Dependencies: 3109 981 8
-- Name: Zonas_de_Altura_refundido2; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW "Zonas_de_Altura_refundido2" AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom, edr.valor FROM (((entidades_capa entidades_capa LEFT JOIN entidaddeterminacion ed ON ((entidades_capa.identidad = ed.identidad))) LEFT JOIN casoentidaddeterminacion ced ON ((ed.iden = ced.identidaddeterminacion))) LEFT JOIN entidaddeterminacionregimen edr ON ((ced.iden = edr.idcaso))) WHERE (((entidades_capa.idcapa = 3) AND (edr.valor IS NOT NULL)) AND (entidades_capa.identidad IN (SELECT entidaddeterminacion.identidad FROM entidaddeterminacion WHERE (entidaddeterminacion.iddeterminacion = 203141))));


ALTER TABLE planeamiento."Zonas_de_Altura_refundido2" OWNER TO postgres;

--
-- TOC entry 2837 (class 1259 OID 19134)
-- Dependencies: 3110 8 981
-- Name: acciones; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW acciones AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 7);


ALTER TABLE planeamiento.acciones OWNER TO postgres;

--
-- TOC entry 2838 (class 1259 OID 19138)
-- Dependencies: 3111 8 981
-- Name: acciones_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW acciones_refundido AS
    SELECT acciones.idambito, acciones.identidad, acciones.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, acciones.clave, acciones.tramite, acciones.idgrupo, acciones.grupo, acciones.geom FROM ((acciones acciones LEFT JOIN entidad ON ((acciones.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (acciones.idtramite IN (SELECT max(acciones.idtramite) AS max FROM acciones acciones WHERE ((acciones.idtipotramite = 11) AND (acciones.fechaconsolidacion IS NOT NULL)) GROUP BY acciones.idambito));


ALTER TABLE planeamiento.acciones_refundido OWNER TO postgres;

--
-- TOC entry 2839 (class 1259 OID 19143)
-- Dependencies: 3112 981 8
-- Name: afecciones; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW afecciones AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 5);


ALTER TABLE planeamiento.afecciones OWNER TO postgres;

--
-- TOC entry 2840 (class 1259 OID 19147)
-- Dependencies: 3113 8 981
-- Name: afecciones_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW afecciones_refundido AS
    SELECT afecciones.idambito, afecciones.identidad, afecciones.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, afecciones.clave, afecciones.tramite, afecciones.idgrupo, afecciones.grupo, afecciones.geom FROM ((afecciones afecciones LEFT JOIN entidad ON ((afecciones.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (afecciones.idtramite IN (SELECT max(afecciones.idtramite) AS max FROM afecciones afecciones WHERE ((afecciones.idtipotramite = 11) AND (afecciones.fechaconsolidacion IS NOT NULL)) GROUP BY afecciones.idambito));


ALTER TABLE planeamiento.afecciones_refundido OWNER TO postgres;

--
-- TOC entry 2841 (class 1259 OID 19152)
-- Dependencies: 3114 981 8
-- Name: alineaciones; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW alineaciones AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 9);


ALTER TABLE planeamiento.alineaciones OWNER TO postgres;

--
-- TOC entry 2842 (class 1259 OID 19156)
-- Dependencies: 3115 981 8
-- Name: alineaciones2; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW alineaciones2 AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom, dvr.codigo, dvr.nombre FROM (((((entidades_capa entidades_capa LEFT JOIN entidaddeterminacion ed ON ((entidades_capa.identidad = ed.identidad))) LEFT JOIN casoentidaddeterminacion ced ON ((ed.iden = ced.identidaddeterminacion))) LEFT JOIN entidaddeterminacionregimen edr ON ((ced.iden = edr.idcaso))) LEFT JOIN opciondeterminacion od ON ((od.iden = edr.idopciondeterminacion))) LEFT JOIN determinacion dvr ON ((od.iddeterminacionvalorref = dvr.iden))) WHERE ((entidades_capa.idcapa = 9) AND (od.iddeterminacion = 195303));


ALTER TABLE planeamiento.alineaciones2 OWNER TO postgres;

--
-- TOC entry 2843 (class 1259 OID 19161)
-- Dependencies: 3116 981 8
-- Name: alineaciones_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW alineaciones_refundido AS
    SELECT alineaciones.idambito, alineaciones.identidad, alineaciones.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, alineaciones.clave, alineaciones.tramite, alineaciones.idgrupo, alineaciones.grupo, alineaciones.geom FROM ((alineaciones alineaciones LEFT JOIN entidad ON ((alineaciones.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (alineaciones.idtramite IN (SELECT max(alineaciones.idtramite) AS max FROM alineaciones alineaciones WHERE ((alineaciones.idtipotramite = 11) AND (alineaciones.fechaconsolidacion IS NOT NULL)) GROUP BY alineaciones.idambito));


ALTER TABLE planeamiento.alineaciones_refundido OWNER TO postgres;

--
-- TOC entry 2844 (class 1259 OID 19166)
-- Dependencies: 3117 8 981
-- Name: alineaciones_refundido2; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW alineaciones_refundido2 AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom, dvr.codigo, dvr.nombre FROM (((((entidades_capa entidades_capa LEFT JOIN entidaddeterminacion ed ON ((entidades_capa.identidad = ed.identidad))) LEFT JOIN casoentidaddeterminacion ced ON ((ed.iden = ced.identidaddeterminacion))) LEFT JOIN entidaddeterminacionregimen edr ON ((ced.iden = edr.idcaso))) LEFT JOIN opciondeterminacion od ON ((od.iden = edr.idopciondeterminacion))) LEFT JOIN determinacion dvr ON ((od.iddeterminacionvalorref = dvr.iden))) WHERE ((entidades_capa.idcapa = 9) AND (od.iddeterminacion = 201939));


ALTER TABLE planeamiento.alineaciones_refundido2 OWNER TO postgres;

--
-- TOC entry 2845 (class 1259 OID 19171)
-- Dependencies: 8
-- Name: planeamiento_ambitoaplicacionambito_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_ambitoaplicacionambito_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_ambitoaplicacionambito_iden_seq OWNER TO postgres;

--
-- TOC entry 2846 (class 1259 OID 19173)
-- Dependencies: 3441 8
-- Name: ambitoaplicacionambito; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE ambitoaplicacionambito (
    iden integer DEFAULT nextval('planeamiento_ambitoaplicacionambito_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    idambito integer NOT NULL
);


ALTER TABLE planeamiento.ambitoaplicacionambito OWNER TO postgres;

--
-- TOC entry 2847 (class 1259 OID 19177)
-- Dependencies: 8
-- Name: planeamiento_operacionplan_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_operacionplan_iden_seq
    START WITH 323
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_operacionplan_iden_seq OWNER TO postgres;

--
-- TOC entry 2848 (class 1259 OID 19179)
-- Dependencies: 3442 8
-- Name: operacionplan; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE operacionplan (
    iden integer DEFAULT nextval('planeamiento_operacionplan_iden_seq'::regclass) NOT NULL,
    idplanoperado integer,
    idinstrumentotipooperacion integer NOT NULL,
    idplanoperador integer NOT NULL
);


ALTER TABLE planeamiento.operacionplan OWNER TO postgres;

--
-- TOC entry 2849 (class 1259 OID 19183)
-- Dependencies: 8
-- Name: planeamiento_planshp_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_planshp_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_planshp_iden_seq OWNER TO postgres;

--
-- TOC entry 2850 (class 1259 OID 19185)
-- Dependencies: 3443 3444 3445 3446 981 8
-- Name: planshp; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE planshp (
    idplan integer,
    iden integer DEFAULT nextval('planeamiento_planshp_iden_seq'::regclass) NOT NULL,
    geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(geom) = 'MULTIPOLYGON'::text) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.srid(geom) = (-1)))
);


ALTER TABLE planeamiento.planshp OWNER TO postgres;

--
-- TOC entry 2851 (class 1259 OID 19195)
-- Dependencies: 3118 981 8
-- Name: ambitos_aplicacion; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW ambitos_aplicacion AS
    SELECT shp.geom, pln.nombre AS plan, ins.iden AS idinstrumento, trd.texto AS instrumento FROM (((((planshp shp JOIN plan pln ON ((shp.idplan = pln.iden))) JOIN operacionplan opr ON ((pln.iden = opr.idplanoperador))) JOIN diccionario.instrumentotipooperacionplan ito ON ((opr.idinstrumentotipooperacion = ito.iden))) JOIN diccionario.instrumentoplan ins ON ((ito.idinstrumentoplan = ins.iden))) JOIN diccionario.traduccion trd ON (((ins.idliteral = trd.idliteral) AND (trd.idioma = 'es'::bpchar))));


ALTER TABLE planeamiento.ambitos_aplicacion OWNER TO postgres;

--
-- TOC entry 2852 (class 1259 OID 19200)
-- Dependencies: 8
-- Name: planeamiento_boletintramite_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_boletintramite_iden_seq
    START WITH 215
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_boletintramite_iden_seq OWNER TO postgres;

--
-- TOC entry 2853 (class 1259 OID 19202)
-- Dependencies: 3447 8
-- Name: boletintramite; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE boletintramite (
    iden integer DEFAULT nextval('planeamiento_boletintramite_iden_seq'::regclass) NOT NULL,
    idboletin integer NOT NULL,
    idtramite integer NOT NULL,
    fecha date,
    numero character varying(20)
);


ALTER TABLE planeamiento.boletintramite OWNER TO postgres;

--
-- TOC entry 2854 (class 1259 OID 19206)
-- Dependencies: 3119 981 8
-- Name: calificacion_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW calificacion_refundido AS
    SELECT calificacion.idambito, calificacion.identidad, calificacion.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, calificacion.clave, calificacion.tramite, calificacion.idgrupo, calificacion.grupo, calificacion.geom FROM ((calificacion calificacion LEFT JOIN entidad ON ((calificacion.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (calificacion.idtramite IN (SELECT max(calificacion.idtramite) AS max FROM calificacion calificacion WHERE ((calificacion.idtipotramite = 11) AND (calificacion.fechaconsolidacion IS NOT NULL)) GROUP BY calificacion.idambito));


ALTER TABLE planeamiento.calificacion_refundido OWNER TO postgres;

--
-- TOC entry 2855 (class 1259 OID 19211)
-- Dependencies: 3120 981 8
-- Name: categorias; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW categorias AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 2);


ALTER TABLE planeamiento.categorias OWNER TO postgres;

--
-- TOC entry 2856 (class 1259 OID 19215)
-- Dependencies: 3121 8 981
-- Name: categorias_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW categorias_refundido AS
    SELECT categorias.idambito, categorias.identidad, categorias.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, categorias.clave, categorias.tramite, categorias.idgrupo, categorias.grupo, categorias.geom FROM ((categorias categorias LEFT JOIN entidad ON ((categorias.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (categorias.idtramite IN (SELECT max(categorias.idtramite) AS max FROM categorias categorias WHERE ((categorias.idtipotramite = 11) AND (categorias.fechaconsolidacion IS NOT NULL)) GROUP BY categorias.idambito));


ALTER TABLE planeamiento.categorias_refundido OWNER TO postgres;

--
-- TOC entry 2857 (class 1259 OID 19220)
-- Dependencies: 3122 981 8
-- Name: clasificacion; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW clasificacion AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 1);


ALTER TABLE planeamiento.clasificacion OWNER TO postgres;

--
-- TOC entry 2858 (class 1259 OID 19224)
-- Dependencies: 3123 981 8
-- Name: clasificacion_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW clasificacion_refundido AS
    SELECT clasificacion.idambito, clasificacion.identidad, clasificacion.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, clasificacion.clave, clasificacion.tramite, clasificacion.idgrupo, clasificacion.grupo, clasificacion.geom FROM ((clasificacion clasificacion LEFT JOIN entidad ON ((clasificacion.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (clasificacion.idtramite IN (SELECT max(clasificacion.idtramite) AS max FROM clasificacion clasificacion WHERE ((clasificacion.idtipotramite = 11) AND (clasificacion.fechaconsolidacion IS NOT NULL)) GROUP BY clasificacion.idambito));


ALTER TABLE planeamiento.clasificacion_refundido OWNER TO postgres;

--
-- TOC entry 2859 (class 1259 OID 19229)
-- Dependencies: 8
-- Name: planeamiento_conjuntoentidad_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_conjuntoentidad_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_conjuntoentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 2860 (class 1259 OID 19231)
-- Dependencies: 3448 8
-- Name: conjuntoentidad; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE conjuntoentidad (
    iden integer DEFAULT nextval('planeamiento_conjuntoentidad_iden_seq'::regclass) NOT NULL,
    identidadconjunto integer NOT NULL,
    identidadmiembro integer NOT NULL
);


ALTER TABLE planeamiento.conjuntoentidad OWNER TO postgres;

--
-- TOC entry 2861 (class 1259 OID 19235)
-- Dependencies: 8
-- Name: planeamiento_determinaciongrupoentidad_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_determinaciongrupoentidad_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_determinaciongrupoentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 2862 (class 1259 OID 19237)
-- Dependencies: 3449 8
-- Name: determinaciongrupoentidad; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE determinaciongrupoentidad (
    iden integer DEFAULT nextval('planeamiento_determinaciongrupoentidad_iden_seq'::regclass) NOT NULL,
    iddeterminaciongrupo integer,
    iddeterminacion integer NOT NULL
);


ALTER TABLE planeamiento.determinaciongrupoentidad OWNER TO postgres;

--
-- TOC entry 2863 (class 1259 OID 19241)
-- Dependencies: 8
-- Name: planeamiento_documento_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_documento_iden_seq
    START WITH 95229
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_documento_iden_seq OWNER TO postgres;

--
-- TOC entry 2864 (class 1259 OID 19243)
-- Dependencies: 3450 8
-- Name: documento; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE documento (
    iden integer DEFAULT nextval('planeamiento_documento_iden_seq'::regclass) NOT NULL,
    nombre character varying(255) NOT NULL,
    archivo character varying(255) NOT NULL,
    comentario text,
    idtramite integer NOT NULL,
    escala integer,
    iddocumentooriginal integer,
    idtipodocumento integer,
    idgrupodocumento integer
);


ALTER TABLE planeamiento.documento OWNER TO postgres;

--
-- TOC entry 2865 (class 1259 OID 19250)
-- Dependencies: 8
-- Name: planeamiento_documentocaso_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_documentocaso_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_documentocaso_iden_seq OWNER TO postgres;

--
-- TOC entry 2866 (class 1259 OID 19252)
-- Dependencies: 3451 8
-- Name: documentocaso; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE documentocaso (
    iden integer DEFAULT nextval('planeamiento_documentocaso_iden_seq'::regclass) NOT NULL,
    idcaso integer NOT NULL,
    iddocumento integer NOT NULL
);


ALTER TABLE planeamiento.documentocaso OWNER TO postgres;

--
-- TOC entry 2867 (class 1259 OID 19256)
-- Dependencies: 8
-- Name: planeamiento_documentodeterminacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_documentodeterminacion_iden_seq
    START WITH 58
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_documentodeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2868 (class 1259 OID 19258)
-- Dependencies: 3452 8
-- Name: documentodeterminacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE documentodeterminacion (
    iden integer DEFAULT nextval('planeamiento_documentodeterminacion_iden_seq'::regclass) NOT NULL,
    iddeterminacion integer NOT NULL,
    iddocumento integer NOT NULL
);


ALTER TABLE planeamiento.documentodeterminacion OWNER TO postgres;

--
-- TOC entry 2869 (class 1259 OID 19262)
-- Dependencies: 8
-- Name: planeamiento_documentoentidad_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_documentoentidad_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_documentoentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 2870 (class 1259 OID 19264)
-- Dependencies: 3453 8
-- Name: documentoentidad; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE documentoentidad (
    iden integer DEFAULT nextval('planeamiento_documentoentidad_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    iddocumento integer NOT NULL
);


ALTER TABLE planeamiento.documentoentidad OWNER TO postgres;

--
-- TOC entry 2871 (class 1259 OID 19268)
-- Dependencies: 8
-- Name: planeamiento_documentoshp_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_documentoshp_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_documentoshp_iden_seq OWNER TO postgres;

--
-- TOC entry 2872 (class 1259 OID 19270)
-- Dependencies: 3454 3455 3456 3457 8 981
-- Name: documentoshp; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE documentoshp (
    iddocumento integer,
    iden integer DEFAULT nextval('planeamiento_documentoshp_iden_seq'::regclass) NOT NULL,
    geom public.geometry,
    hoja character varying(50),
    CONSTRAINT enforce_dims_the_geom CHECK ((public.ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK ((((public.geometrytype(geom) = 'MULTIPOLYGON'::text) OR (public.geometrytype(geom) = 'POLYGON'::text)) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.srid(geom) = (-1)))
);


ALTER TABLE planeamiento.documentoshp OWNER TO postgres;

--
-- TOC entry 2873 (class 1259 OID 19280)
-- Dependencies: 3124 8 981
-- Name: entidades_capa_ambitos; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW entidades_capa_ambitos AS
    SELECT entidad.iden AS identidad, tramite.fechaconsolidacion, capa.nombre AS capa, capa.iden AS idcapa, capagrupo.orden, entidad.idtramite, tramite.codigofip, tramite.idtipotramite, entidad.clave, entidad.nombre AS entidad, determinacion.codigo AS idgrupo, determinacion.nombre AS grupo, determinacion.codigo, plan.idambito, CASE WHEN (entidadpol.geom IS NOT NULL) THEN entidadpol.geom WHEN (entidadlin.geom IS NOT NULL) THEN entidadlin.geom WHEN (entidadpnt.geom IS NOT NULL) THEN entidadpnt.geom ELSE NULL::public.geometry END AS geometria FROM (((((((((((((entidad LEFT JOIN entidaddeterminacion ON ((entidad.iden = entidaddeterminacion.identidad))) LEFT JOIN tramite ON ((entidad.idtramite = tramite.iden))) LEFT JOIN plan ON ((tramite.idplan = plan.iden))) LEFT JOIN casoentidaddeterminacion ON ((entidaddeterminacion.iden = casoentidaddeterminacion.identidaddeterminacion))) LEFT JOIN entidaddeterminacionregimen ON ((casoentidaddeterminacion.iden = entidaddeterminacionregimen.idcaso))) LEFT JOIN opciondeterminacion ON ((entidaddeterminacionregimen.idopciondeterminacion = opciondeterminacion.iden))) LEFT JOIN determinacion ON ((opciondeterminacion.iddeterminacionvalorref = determinacion.iden))) LEFT JOIN determinacion determinacionaplicada ON ((entidaddeterminacion.iddeterminacion = determinacionaplicada.iden))) LEFT JOIN entidadlin ON ((entidad.iden = entidadlin.identidad))) LEFT JOIN entidadpnt ON ((entidad.iden = entidadpnt.identidad))) LEFT JOIN entidadpol ON ((entidad.iden = entidadpol.identidad))) LEFT JOIN explotacion.capagrupo ON ((determinacion.codigo = (capagrupo.codigodeterminaciongrupo)::bpchar))) LEFT JOIN explotacion.capa ON ((capagrupo.idcapa = capa.iden))) WHERE ((determinacionaplicada.idcaracter = 20) AND (((entidadpnt.geom IS NOT NULL) OR (entidadlin.geom IS NOT NULL)) OR (entidadpol.geom IS NOT NULL))) ORDER BY capa.orden, capagrupo.orden;


ALTER TABLE planeamiento.entidades_capa_ambitos OWNER TO postgres;

--
-- TOC entry 2874 (class 1259 OID 19285)
-- Dependencies: 3125 981 8
-- Name: gestion; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW gestion AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 4);


ALTER TABLE planeamiento.gestion OWNER TO postgres;

--
-- TOC entry 2875 (class 1259 OID 19289)
-- Dependencies: 3126 981 8
-- Name: gestion_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW gestion_refundido AS
    SELECT gestion.idambito, gestion.identidad, gestion.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, gestion.clave, gestion.tramite, gestion.idgrupo, gestion.grupo, gestion.geom FROM ((gestion gestion LEFT JOIN entidad ON ((gestion.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (gestion.idtramite IN (SELECT max(gestion.idtramite) AS max FROM gestion gestion WHERE ((gestion.idtipotramite = 11) AND (gestion.fechaconsolidacion IS NOT NULL)) GROUP BY gestion.idambito));


ALTER TABLE planeamiento.gestion_refundido OWNER TO postgres;

--
-- TOC entry 2876 (class 1259 OID 19294)
-- Dependencies: 8
-- Name: planeamiento_operacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_operacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_operacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2877 (class 1259 OID 19296)
-- Dependencies: 3458 8
-- Name: operacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE operacion (
    iden integer DEFAULT nextval('planeamiento_operacion_iden_seq'::regclass) NOT NULL,
    texto text,
    orden integer NOT NULL,
    idtramiteordenante integer NOT NULL
);


ALTER TABLE planeamiento.operacion OWNER TO postgres;

--
-- TOC entry 2878 (class 1259 OID 19303)
-- Dependencies: 8
-- Name: planeamiento_operaciondeterminacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_operaciondeterminacion_iden_seq
    START WITH 144
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_operaciondeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2879 (class 1259 OID 19305)
-- Dependencies: 3459 8
-- Name: operaciondeterminacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE operaciondeterminacion (
    iden integer DEFAULT nextval('planeamiento_operaciondeterminacion_iden_seq'::regclass) NOT NULL,
    iddeterminacion integer NOT NULL,
    iddeterminacionoperadora integer NOT NULL,
    idtipooperaciondet integer NOT NULL,
    idoperacion integer NOT NULL
);


ALTER TABLE planeamiento.operaciondeterminacion OWNER TO postgres;

--
-- TOC entry 2880 (class 1259 OID 19309)
-- Dependencies: 8
-- Name: planeamiento_operacionentidad_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_operacionentidad_iden_seq
    START WITH 2860
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_operacionentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 2881 (class 1259 OID 19311)
-- Dependencies: 3460 8
-- Name: operacionentidad; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE operacionentidad (
    iden integer DEFAULT nextval('planeamiento_operacionentidad_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    identidadoperadora integer NOT NULL,
    idtipooperacionent integer NOT NULL,
    idoperacion integer NOT NULL
);


ALTER TABLE planeamiento.operacionentidad OWNER TO postgres;

--
-- TOC entry 2882 (class 1259 OID 19315)
-- Dependencies: 8
-- Name: planeamiento_operacionrelacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_operacionrelacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_operacionrelacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2883 (class 1259 OID 19317)
-- Dependencies: 3461 8
-- Name: operacionrelacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE operacionrelacion (
    iden integer DEFAULT nextval('planeamiento_operacionrelacion_iden_seq'::regclass) NOT NULL,
    idrelacion integer NOT NULL,
    idtipooperacionrel integer NOT NULL,
    idoperacion integer NOT NULL
);


ALTER TABLE planeamiento.operacionrelacion OWNER TO postgres;

--
-- TOC entry 2884 (class 1259 OID 19321)
-- Dependencies: 8
-- Name: planeamiento_planentidadordenacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_planentidadordenacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_planentidadordenacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2885 (class 1259 OID 19323)
-- Dependencies: 3462 8
-- Name: planentidadordenacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE planentidadordenacion (
    iden integer DEFAULT nextval('planeamiento_planentidadordenacion_iden_seq'::regclass) NOT NULL,
    idplan integer NOT NULL,
    identidadordenacion integer NOT NULL
);


ALTER TABLE planeamiento.planentidadordenacion OWNER TO postgres;

--
-- TOC entry 2886 (class 1259 OID 19327)
-- Dependencies: 8
-- Name: planeamiento_planlegislacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_planlegislacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_planlegislacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2887 (class 1259 OID 19329)
-- Dependencies: 3463 8
-- Name: planlegislacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE planlegislacion (
    iden integer DEFAULT nextval('planeamiento_planlegislacion_iden_seq'::regclass) NOT NULL,
    idplan integer NOT NULL,
    idlegislacion integer NOT NULL
);


ALTER TABLE planeamiento.planlegislacion OWNER TO postgres;

--
-- TOC entry 2888 (class 1259 OID 19333)
-- Dependencies: 8
-- Name: planeamiento_propiedadrelacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_propiedadrelacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_propiedadrelacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2889 (class 1259 OID 19335)
-- Dependencies: 3464 8
-- Name: propiedadrelacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE propiedadrelacion (
    iden integer DEFAULT nextval('planeamiento_propiedadrelacion_iden_seq'::regclass) NOT NULL,
    idrelacion integer NOT NULL,
    iddefpropiedad integer NOT NULL,
    valor text NOT NULL
);


ALTER TABLE planeamiento.propiedadrelacion OWNER TO postgres;

--
-- TOC entry 2890 (class 1259 OID 19342)
-- Dependencies: 3127 981 8
-- Name: protecciones; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW protecciones AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 6);


ALTER TABLE planeamiento.protecciones OWNER TO postgres;

--
-- TOC entry 2891 (class 1259 OID 19346)
-- Dependencies: 3128 981 8
-- Name: protecciones2; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW protecciones2 AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 6) EXCEPT SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.identidad IN (SELECT entidad.iden FROM entidad WHERE (entidad.idpadre = ANY (ARRAY[146417, 146567]))));


ALTER TABLE planeamiento.protecciones2 OWNER TO postgres;

--
-- TOC entry 2892 (class 1259 OID 19351)
-- Dependencies: 3129 981 8
-- Name: protecciones_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW protecciones_refundido AS
    SELECT protecciones.idambito, protecciones.identidad, protecciones.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, protecciones.clave, protecciones.tramite, protecciones.idgrupo, protecciones.grupo, protecciones.geom FROM ((protecciones protecciones LEFT JOIN entidad ON ((protecciones.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (protecciones.idtramite IN (SELECT max(protecciones.idtramite) AS max FROM protecciones protecciones WHERE ((protecciones.idtipotramite = 11) AND (protecciones.fechaconsolidacion IS NOT NULL)) GROUP BY protecciones.idambito));


ALTER TABLE planeamiento.protecciones_refundido OWNER TO postgres;

--
-- TOC entry 2893 (class 1259 OID 19356)
-- Dependencies: 3130 981 8
-- Name: protecciones_refundido2; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW protecciones_refundido2 AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa, entidad e, tramite t WHERE ((((entidades_capa.idcapa = 6) AND (entidades_capa.identidad = e.iden)) AND (e.idtramite = t.iden)) AND (t.idtipotramite = 11)) EXCEPT SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.identidad IN (SELECT entidad.iden FROM entidad WHERE (entidad.idpadre = ANY (ARRAY[320430, 319328]))));


ALTER TABLE planeamiento.protecciones_refundido2 OWNER TO postgres;

--
-- TOC entry 2896 (class 1259 OID 19370)
-- Dependencies: 8
-- Name: planeamiento_relacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_relacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_relacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2897 (class 1259 OID 19372)
-- Dependencies: 3466 8
-- Name: relacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE relacion (
    iden integer DEFAULT nextval('planeamiento_relacion_iden_seq'::regclass) NOT NULL,
    iddefrelacion integer NOT NULL,
    idtramitecreador integer NOT NULL
);


ALTER TABLE planeamiento.relacion OWNER TO postgres;

--
-- TOC entry 2898 (class 1259 OID 19376)
-- Dependencies: 3131 981 8
-- Name: sin_nivel_de_proteccion; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW sin_nivel_de_proteccion AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 6);


ALTER TABLE planeamiento.sin_nivel_de_proteccion OWNER TO postgres;

--
-- TOC entry 2899 (class 1259 OID 19380)
-- Dependencies: 3132 8 981
-- Name: sin_nivel_de_proteccion2; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW sin_nivel_de_proteccion2 AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 6) EXCEPT SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.identidad IN (SELECT entidad.iden FROM entidad WHERE (entidad.idpadre = ANY (ARRAY[146417, 146567]))));


ALTER TABLE planeamiento.sin_nivel_de_proteccion2 OWNER TO postgres;

--
-- TOC entry 2900 (class 1259 OID 19385)
-- Dependencies: 3133 981 8
-- Name: sin_nivel_de_proteccion_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW sin_nivel_de_proteccion_refundido AS
    SELECT protecciones.idambito, protecciones.identidad, protecciones.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, protecciones.clave, protecciones.tramite, protecciones.idgrupo, protecciones.grupo, protecciones.geom FROM ((protecciones protecciones LEFT JOIN entidad ON ((protecciones.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (protecciones.idtramite IN (SELECT max(protecciones.idtramite) AS max FROM protecciones protecciones WHERE ((protecciones.idtipotramite = 11) AND (protecciones.fechaconsolidacion IS NOT NULL)) GROUP BY protecciones.idambito));


ALTER TABLE planeamiento.sin_nivel_de_proteccion_refundido OWNER TO postgres;

--
-- TOC entry 2901 (class 1259 OID 19390)
-- Dependencies: 3134 8 981
-- Name: sin_nivel_de_proteccion_refundido2; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW sin_nivel_de_proteccion_refundido2 AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa, entidad e, tramite t WHERE ((((entidades_capa.idcapa = 6) AND (entidades_capa.identidad = e.iden)) AND (e.idtramite = t.iden)) AND (t.idtipotramite = 11)) EXCEPT SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.identidad IN (SELECT entidad.iden FROM entidad WHERE (entidad.idpadre = ANY (ARRAY[320430, 319328]))));


ALTER TABLE planeamiento.sin_nivel_de_proteccion_refundido2 OWNER TO postgres;

--
-- TOC entry 2902 (class 1259 OID 19395)
-- Dependencies: 3135 8 981
-- Name: sistemas; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW sistemas AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 8);


ALTER TABLE planeamiento.sistemas OWNER TO postgres;

--
-- TOC entry 2903 (class 1259 OID 19399)
-- Dependencies: 3136 8 981
-- Name: sistemas_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW sistemas_refundido AS
    SELECT sistemas.idambito, sistemas.identidad, sistemas.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, sistemas.clave, sistemas.tramite, sistemas.idgrupo, sistemas.grupo, sistemas.geom FROM ((sistemas sistemas LEFT JOIN entidad ON ((sistemas.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (sistemas.idtramite IN (SELECT max(sistemas.idtramite) AS max FROM sistemas sistemas WHERE ((sistemas.idtipotramite = 11) AND (sistemas.fechaconsolidacion IS NOT NULL)) GROUP BY sistemas.idambito));


ALTER TABLE planeamiento.sistemas_refundido OWNER TO postgres;

--
-- TOC entry 2904 (class 1259 OID 19404)
-- Dependencies: 8
-- Name: planeamiento_vectorrelacion_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_vectorrelacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_vectorrelacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2905 (class 1259 OID 19406)
-- Dependencies: 3467 3468 8
-- Name: vectorrelacion; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE vectorrelacion (
    iden integer DEFAULT nextval('planeamiento_vectorrelacion_iden_seq'::regclass) NOT NULL,
    idrelacion integer NOT NULL,
    iddefvector integer NOT NULL,
    valor integer DEFAULT 0 NOT NULL
);


ALTER TABLE planeamiento.vectorrelacion OWNER TO postgres;

--
-- TOC entry 2906 (class 1259 OID 19411)
-- Dependencies: 8
-- Name: planeamiento_vinculocaso_iden_seq; Type: SEQUENCE; Schema: planeamiento; Owner: postgres
--

CREATE SEQUENCE planeamiento_vinculocaso_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE planeamiento.planeamiento_vinculocaso_iden_seq OWNER TO postgres;

--
-- TOC entry 2907 (class 1259 OID 19413)
-- Dependencies: 3469 8
-- Name: vinculocaso; Type: TABLE; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE TABLE vinculocaso (
    iden integer DEFAULT nextval('planeamiento_vinculocaso_iden_seq'::regclass) NOT NULL,
    idcaso integer NOT NULL,
    idcasovinculado integer NOT NULL
);


ALTER TABLE planeamiento.vinculocaso OWNER TO postgres;

--
-- TOC entry 2908 (class 1259 OID 19417)
-- Dependencies: 3137 8 981
-- Name: zonas_de_proteccion_arqueologica; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW zonas_de_proteccion_arqueologica AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 6);


ALTER TABLE planeamiento.zonas_de_proteccion_arqueologica OWNER TO postgres;

--
-- TOC entry 2909 (class 1259 OID 19421)
-- Dependencies: 3138 981 8
-- Name: zonas_de_proteccion_arqueologica2; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW zonas_de_proteccion_arqueologica2 AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.idcapa = 6) UNION SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.identidad IN (SELECT entidad.iden FROM entidad WHERE (entidad.idpadre = ANY (ARRAY[146417, 146567]))));


ALTER TABLE planeamiento.zonas_de_proteccion_arqueologica2 OWNER TO postgres;

--
-- TOC entry 2910 (class 1259 OID 19426)
-- Dependencies: 3139 981 8
-- Name: zonas_de_proteccion_arqueologica_refundido; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW zonas_de_proteccion_arqueologica_refundido AS
    SELECT protecciones.idambito, protecciones.identidad, protecciones.entidad, entidadbase.nombre AS entidadbase, entidadbase.iden AS identidadbase, protecciones.clave, protecciones.tramite, protecciones.idgrupo, protecciones.grupo, protecciones.geom FROM ((protecciones protecciones LEFT JOIN entidad ON ((protecciones.identidad = entidad.iden))) LEFT JOIN entidad entidadbase ON ((entidad.identidadbase = entidadbase.iden))) WHERE (protecciones.idtramite IN (SELECT max(protecciones.idtramite) AS max FROM protecciones protecciones WHERE ((protecciones.idtipotramite = 11) AND (protecciones.fechaconsolidacion IS NOT NULL)) GROUP BY protecciones.idambito));


ALTER TABLE planeamiento.zonas_de_proteccion_arqueologica_refundido OWNER TO postgres;

--
-- TOC entry 2911 (class 1259 OID 19431)
-- Dependencies: 3140 981 8
-- Name: zonas_de_proteccion_arqueologica_refundido2; Type: VIEW; Schema: planeamiento; Owner: postgres
--

CREATE VIEW zonas_de_proteccion_arqueologica_refundido2 AS
    SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa, entidad e, tramite t WHERE ((((entidades_capa.idcapa = 6) AND (entidades_capa.identidad = e.iden)) AND (e.idtramite = t.iden)) AND (t.idtipotramite = 11)) UNION SELECT entidades_capa.fechaconsolidacion, entidades_capa.idambito, entidades_capa.identidad, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom FROM entidades_capa entidades_capa WHERE (entidades_capa.identidad IN (SELECT entidad.iden FROM entidad WHERE (entidad.idpadre = ANY (ARRAY[320430, 319328]))));


ALTER TABLE planeamiento.zonas_de_proteccion_arqueologica_refundido2 OWNER TO postgres;

SET search_path = seguridad, pg_catalog;

--
-- TOC entry 2912 (class 1259 OID 19436)
-- Dependencies: 9
-- Name: diario; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE diario (
    iden integer NOT NULL,
    usuario integer NOT NULL,
    operaciones integer,
    fechalogin timestamp with time zone,
    modulo integer,
    log character(300)
);


ALTER TABLE seguridad.diario OWNER TO postgres;

--
-- TOC entry 2913 (class 1259 OID 19439)
-- Dependencies: 3470 9
-- Name: fip1; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE fip1 (
    iden integer NOT NULL,
    codfip character(32),
    fechacreacion date DEFAULT now(),
    fecharefundido date,
    ruta character(100),
    obsoleto boolean,
    fechadescarga date,
    idambito integer NOT NULL
);


ALTER TABLE seguridad.fip1 OWNER TO postgres;

--
-- TOC entry 2914 (class 1259 OID 19443)
-- Dependencies: 9
-- Name: operacion_iden_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE operacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE seguridad.operacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2915 (class 1259 OID 19445)
-- Dependencies: 3471 9
-- Name: operaciones; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE operaciones (
    iden integer DEFAULT nextval('operacion_iden_seq'::regclass) NOT NULL,
    horainicio timestamp with time zone,
    nombre character(2048)
);


ALTER TABLE seguridad.operaciones OWNER TO postgres;

--
-- TOC entry 2916 (class 1259 OID 19452)
-- Dependencies: 3472 9
-- Name: procesos; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE procesos (
    proceso character(20),
    enproceso boolean DEFAULT false
);


ALTER TABLE seguridad.procesos OWNER TO postgres;

--
-- TOC entry 2917 (class 1259 OID 19456)
-- Dependencies: 9
-- Name: rol_iden_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE rol_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE seguridad.rol_iden_seq OWNER TO postgres;

--
-- TOC entry 2918 (class 1259 OID 19458)
-- Dependencies: 3473 9
-- Name: rol; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE rol (
    iden integer DEFAULT nextval('rol_iden_seq'::regclass) NOT NULL,
    nombre character varying(255) NOT NULL,
    codigo character varying(3) NOT NULL
);


ALTER TABLE seguridad.rol OWNER TO postgres;

--
-- TOC entry 2919 (class 1259 OID 19462)
-- Dependencies: 9
-- Name: subsistema; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE subsistema (
    iden integer NOT NULL,
    nombre character(25)
);


ALTER TABLE seguridad.subsistema OWNER TO postgres;

--
-- TOC entry 2920 (class 1259 OID 19465)
-- Dependencies: 3474 9
-- Name: usuario; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario (
    iden integer DEFAULT nextval(('"seguridad"."usuarios_iden_seq"'::text)::regclass) NOT NULL,
    nombre character varying(255) NOT NULL,
    passwordmd5 character varying(32) NOT NULL,
    correo character varying(40),
    dni character varying(9),
    baja boolean
);


ALTER TABLE seguridad.usuario OWNER TO postgres;

--
-- TOC entry 2921 (class 1259 OID 19469)
-- Dependencies: 9
-- Name: usuariorol_iden_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE usuariorol_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE seguridad.usuariorol_iden_seq OWNER TO postgres;

--
-- TOC entry 2922 (class 1259 OID 19471)
-- Dependencies: 3475 9
-- Name: usuariorol; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE usuariorol (
    iden integer DEFAULT nextval('usuariorol_iden_seq'::regclass) NOT NULL,
    idusuario integer NOT NULL,
    idrol integer NOT NULL,
    idambito integer
);


ALTER TABLE seguridad.usuariorol OWNER TO postgres;

SET search_path = validacion, pg_catalog;

--
-- TOC entry 2928 (class 1259 OID 23978)
-- Dependencies: 11
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
-- TOC entry 2934 (class 1259 OID 24017)
-- Dependencies: 3478 11
-- Name: error; Type: TABLE; Schema: validacion; Owner: postgres; Tablespace: 
--

CREATE TABLE error (
    iden integer DEFAULT nextval('validacion_error_sequence'::regclass) NOT NULL,
    idproceso integer NOT NULL,
    idvalidacion integer NOT NULL,
    mensaje character varying(1000) NOT NULL
);


ALTER TABLE validacion.error OWNER TO postgres;

--
-- TOC entry 2929 (class 1259 OID 23980)
-- Dependencies: 11
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
-- TOC entry 2932 (class 1259 OID 23993)
-- Dependencies: 3477 11
-- Name: proceso; Type: TABLE; Schema: validacion; Owner: postgres; Tablespace: 
--

CREATE TABLE proceso (
    iden integer DEFAULT nextval('validacion_proceso_sequence'::regclass) NOT NULL,
    idfip character varying(256) NOT NULL,
    inicio timestamp with time zone NOT NULL,
    fin timestamp with time zone,
    backup character varying(3000),
    consolidado timestamp with time zone
);


ALTER TABLE validacion.proceso OWNER TO postgres;

--
-- TOC entry 2933 (class 1259 OID 24002)
-- Dependencies: 11
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
-- TOC entry 2930 (class 1259 OID 23982)
-- Dependencies: 11
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
-- TOC entry 2931 (class 1259 OID 23984)
-- Dependencies: 3476 11
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

SET search_path = ficha, pg_catalog;

--
-- TOC entry 707 (class 1255 OID 32878)
-- Dependencies: 14 1509
-- Name: getXML(integer[], integer); Type: FUNCTION; Schema: ficha; Owner: postgres
--

CREATE FUNCTION "getXML"(ids_entidades integer[], id_ficha integer) RETURNS text
    AS $$
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION ficha."getXML"(ids_entidades integer[], id_ficha integer) OWNER TO postgres;

SET search_path = planeamiento, pg_catalog;

--
-- TOC entry 703 (class 1255 OID 19942)
-- Dependencies: 8 1509
-- Name: limpiaTramite(integer); Type: FUNCTION; Schema: planeamiento; Owner: postgres
--

CREATE FUNCTION "limpiaTramite"(iden_tramite integer) RETURNS boolean
    AS $$
DECLARE
  
BEGIN
  delete from planeamiento.regimenespecifico where identidaddeterminacionregimen in 
  	(select iden from planeamiento.entidaddeterminacionregimen where idcaso in 
  		(select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
    		(select iden from planeamiento.entidaddeterminacion where identidad in 
	  			(select iden from planeamiento.entidad where idtramite=iden_tramite))));
            
  delete from planeamiento.entidaddeterminacionregimen where idcasoaplicacion in 
  	(select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
    	(select iden from planeamiento.entidaddeterminacion where identidad in 
	  		(select iden from planeamiento.entidad where idtramite=iden_tramite)));
            
  delete from planeamiento.entidaddeterminacionregimen where idcaso in 
  	(select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
    	(select iden from planeamiento.entidaddeterminacion where identidad in 
	  		(select iden from planeamiento.entidad where idtramite=iden_tramite)));
    
  delete from planeamiento.documentocaso where idcaso in 
  	(select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
    	(select iden from planeamiento.entidaddeterminacion where identidad in 
	  		(select iden from planeamiento.entidad where idtramite=iden_tramite)));
            
  delete from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
    (select iden from planeamiento.entidaddeterminacion where identidad in 
	  	(select iden from planeamiento.entidad where idtramite=iden_tramite));
    
  delete from planeamiento.entidaddeterminacion where identidad in 
  	(select iden from planeamiento.entidad where idtramite=iden_tramite);
    
  delete from planeamiento.operacionentidad where identidad in 
  	(select iden from planeamiento.entidad where idtramite=iden_tramite);
    
  delete from planeamiento.operacionentidad where identidadoperadora in 
  	(select iden from planeamiento.entidad where idtramite=iden_tramite);
    
  delete from planeamiento.documentoentidad where identidad in 
  	(select iden from planeamiento.entidad where idtramite=iden_tramite);
    
  delete from planeamiento.ambitoaplicacionambito where identidad in 
  	(select iden from planeamiento.entidad where idtramite=iden_tramite);
    
  delete from planeamiento.entidadpol where identidad in 
  	(select iden from planeamiento.entidad where idtramite=iden_tramite);

  delete from planeamiento.entidadlin where identidad in 
  	(select iden from planeamiento.entidad where idtramite=iden_tramite);
            
  delete from planeamiento.entidadpnt where identidad in 
  	(select iden from planeamiento.entidad where idtramite=iden_tramite);
    
  delete from planeamiento.planentidadordenacion where identidadordenacion in 
  	(select iden from planeamiento.entidad where idtramite=iden_tramite);
    
  delete from planeamiento.entidad where idtramite=iden_tramite;

  
  delete from planeamiento.regimenespecifico where identidaddeterminacionregimen in 
  	(select iden from planeamiento.entidaddeterminacionregimen where idopciondeterminacion in 
  		(select iden from planeamiento.opciondeterminacion where iddeterminacion in
  			(select iden from planeamiento.determinacion where idtramite=iden_tramite)));
        
  delete from planeamiento.entidaddeterminacionregimen where idopciondeterminacion in 
  	(select iden from planeamiento.opciondeterminacion where iddeterminacion in
  		(select iden from planeamiento.determinacion where idtramite=iden_tramite));
  
  delete from planeamiento.determinaciongrupoentidad where iddeterminacion in 
  	(select iden from planeamiento.determinacion where idtramite=iden_tramite);
  
  delete from planeamiento.determinaciongrupoentidad where iddeterminaciongrupo in 
  	(select iden from planeamiento.determinacion where idtramite=iden_tramite);
    
  delete from planeamiento.opciondeterminacion where iddeterminacion in 
  	(select iden from planeamiento.determinacion where idtramite=iden_tramite);
  
  delete from planeamiento.regimenespecifico where identidaddeterminacionregimen in 
  	(select iden from planeamiento.entidaddeterminacionregimen where idcaso in 
  		(select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
    		(select iden from planeamiento.entidaddeterminacion where iddeterminacion in 
	  			(select iden from planeamiento.determinacion where idtramite=iden_tramite))));
            
  delete from planeamiento.entidaddeterminacionregimen where idcasoaplicacion in 
  	(select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
    	(select iden from planeamiento.entidaddeterminacion where iddeterminacion in 
	  		(select iden from planeamiento.determinacion where idtramite=iden_tramite)));
            
  delete from planeamiento.entidaddeterminacionregimen where idcaso in 
  	(select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
    	(select iden from planeamiento.entidaddeterminacion where iddeterminacion in 
	  		(select iden from planeamiento.determinacion where idtramite=iden_tramite)));
    
  delete from planeamiento.documentocaso where idcaso in 
  	(select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
    	(select iden from planeamiento.entidaddeterminacion where iddeterminacion in 
	  		(select iden from planeamiento.determinacion where idtramite=iden_tramite)));
            
  delete from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
    (select iden from planeamiento.entidaddeterminacion where iddeterminacion in 
	  	(select iden from planeamiento.determinacion where idtramite=iden_tramite));
    
  delete from planeamiento.entidaddeterminacion where iddeterminacion in 
  	(select iden from planeamiento.determinacion where idtramite=iden_tramite);
    
  delete from planeamiento.operaciondeterminacion where iddeterminacion in 
  	(select iden from planeamiento.determinacion where idtramite=iden_tramite);
    
  delete from planeamiento.operaciondeterminacion where iddeterminacionoperadora in 
  	(select iden from planeamiento.determinacion where idtramite=iden_tramite);
    
  delete from planeamiento.documentodeterminacion where iddeterminacion in 
  	(select iden from planeamiento.determinacion where idtramite=iden_tramite);
    
  delete from planeamiento.determinacion where idtramite=iden_tramite;
  
  delete from planeamiento.documentoshp where iddocumento in 
  	(select iden from planeamiento.documento where idtramite=iden_tramite);
    
  delete from planeamiento.documento where idtramite=iden_tramite;
  
  delete from planeamiento.operacion where idtramiteordenante=iden_tramite;

  delete from planeamiento.propiedadrelacion where idrelacion In
	(Select iden from planeamiento.relacion where idtramitecreador=iden_tramite);

  delete from planeamiento.vectorrelacion where idrelacion In
	(Select iden from planeamiento.relacion where idtramitecreador=iden_tramite);

  delete from planeamiento.relacion where idtramitecreador=iden_tramite;
  RETURN true;
END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION planeamiento."limpiaTramite"(iden_tramite integer) OWNER TO postgres;

--
-- TOC entry 704 (class 1255 OID 19943)
-- Dependencies: 8 1509
-- Name: limpiaTramiteOK(integer); Type: FUNCTION; Schema: planeamiento; Owner: postgres
--

CREATE FUNCTION "limpiaTramiteOK"(iden_tramite integer) RETURNS boolean
    AS $$
DECLARE
 
BEGIN
 delete from planeamiento.regimenespecifico where identidaddeterminacionregimen in 
 (select iden from planeamiento.entidaddeterminacionregimen where idcaso in 
 (select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite))));
 
 delete from planeamiento.entidaddeterminacionregimen where idcasoaplicacion in 
 (select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite)));
 
 delete from planeamiento.entidaddeterminacionregimen where idcaso in 
 (select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite)));
 
 delete from planeamiento.vinculocaso where idcaso in 
 (select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite)));
 
 delete from planeamiento.documentocaso where idcaso in 
 (select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite)));
 
 delete from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite));
 
 delete from planeamiento.entidaddeterminacion where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite);
 
 delete from planeamiento.operacionentidad where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite);
 
 delete from planeamiento.operacionentidad where identidadoperadora in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite);
 
 delete from planeamiento.documentoentidad where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite);
 
 delete from planeamiento.ambitoaplicacionambito where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite);
 
 delete from planeamiento.entidadpol where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite);

 delete from planeamiento.entidadlin where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite);
 
 delete from planeamiento.entidadpnt where identidad in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite);
 
 delete from planeamiento.planentidadordenacion where identidadordenacion in 
 (select iden from planeamiento.entidad where idtramite=iden_tramite);
 
 delete from planeamiento.entidad where idtramite=iden_tramite;

 
 delete from planeamiento.regimenespecifico where identidaddeterminacionregimen in 
 (select iden from planeamiento.entidaddeterminacionregimen where idopciondeterminacion in 
 (select iden from planeamiento.opciondeterminacion where iddeterminacion in
 (select iden from planeamiento.determinacion where idtramite=iden_tramite)));
 
 delete from planeamiento.entidaddeterminacionregimen where idopciondeterminacion in 
 (select iden from planeamiento.opciondeterminacion where iddeterminacion in
 (select iden from planeamiento.determinacion where idtramite=iden_tramite));
 
 delete from planeamiento.determinaciongrupoentidad where iddeterminacion in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite);
 
 delete from planeamiento.determinaciongrupoentidad where iddeterminaciongrupo in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite);
 
 delete from planeamiento.opciondeterminacion where iddeterminacion in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite);
 
 delete from planeamiento.opciondeterminacion where iddeterminacionvalorref in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite);
 
 delete from planeamiento.regimenespecifico where identidaddeterminacionregimen in 
 (select iden from planeamiento.entidaddeterminacionregimen where idcaso in 
 (select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where iddeterminacion in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite))));
 
 delete from planeamiento.entidaddeterminacionregimen where idcasoaplicacion in 
 (select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where iddeterminacion in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite)));
 
 delete from planeamiento.entidaddeterminacionregimen where idcaso in 
 (select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where iddeterminacion in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite)));
 
 delete from planeamiento.documentocaso where idcaso in 
 (select iden from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where iddeterminacion in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite)));
 
 delete from planeamiento.casoentidaddeterminacion where identidaddeterminacion in 
 (select iden from planeamiento.entidaddeterminacion where iddeterminacion in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite));
 
 delete from planeamiento.entidaddeterminacion where iddeterminacion in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite);
 
 delete from planeamiento.operaciondeterminacion where iddeterminacion in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite);
 
 delete from planeamiento.operaciondeterminacion where iddeterminacionoperadora in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite);
 
 delete from planeamiento.documentodeterminacion where iddeterminacion in 
 (select iden from planeamiento.determinacion where idtramite=iden_tramite);
 
 delete from planeamiento.determinacion where idtramite=iden_tramite;
 
 delete from planeamiento.documentoshp where iddocumento in 
 (select iden from planeamiento.documento where idtramite=iden_tramite);
 
 delete from planeamiento.documento where idtramite=iden_tramite;
 
 delete from planeamiento.vectorrelacion where idrelacion in
 (select iden from planeamiento.relacion where idtramitecreador=iden_tramite);
 
 delete from planeamiento.propiedadrelacion where idrelacion in
 (select iden from planeamiento.relacion where idtramitecreador=iden_tramite);
 
 delete from planeamiento.relacion where idtramitecreador=iden_tramite;
 
 delete from planeamiento.operacion where idtramiteordenante=iden_tramite;
 
 RETURN true;
END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION planeamiento."limpiaTramiteOK"(iden_tramite integer) OWNER TO postgres;

SET search_path = ficha, pg_catalog;

--
-- TOC entry 3024 (class 1259 OID 32879)
-- Dependencies: 14
-- Name: determinacionclasifuso_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE determinacionclasifuso_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.determinacionclasifuso_iden_seq OWNER TO postgres;

--
-- TOC entry 3025 (class 1259 OID 32881)
-- Dependencies: 14
-- Name: fichagrupodeterminacion_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE fichagrupodeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.fichagrupodeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3026 (class 1259 OID 32883)
-- Dependencies: 14
-- Name: grupodeterminacion_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE grupodeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.grupodeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3027 (class 1259 OID 32885)
-- Dependencies: 14
-- Name: regimenesacto_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE regimenesacto_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.regimenesacto_iden_seq OWNER TO postgres;

--
-- TOC entry 3028 (class 1259 OID 32887)
-- Dependencies: 14
-- Name: regimenesuso_iden_seq; Type: SEQUENCE; Schema: ficha; Owner: postgres
--

CREATE SEQUENCE regimenesuso_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE ficha.regimenesuso_iden_seq OWNER TO postgres;

SET search_path = seguridad, pg_catalog;

--
-- TOC entry 2923 (class 1259 OID 19944)
-- Dependencies: 9
-- Name: diario_iden_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE diario_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE seguridad.diario_iden_seq OWNER TO postgres;

--
-- TOC entry 2924 (class 1259 OID 19946)
-- Dependencies: 9
-- Name: fip1_iden_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE fip1_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE seguridad.fip1_iden_seq OWNER TO postgres;

--
-- TOC entry 2925 (class 1259 OID 19948)
-- Dependencies: 9
-- Name: subsistema_iden_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE subsistema_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE seguridad.subsistema_iden_seq OWNER TO postgres;

--
-- TOC entry 2926 (class 1259 OID 19950)
-- Dependencies: 9
-- Name: usuarios_iden_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE usuarios_iden_seq
    INCREMENT BY 1
    MAXVALUE 2147483647
    NO MINVALUE
    CACHE 1;


ALTER TABLE seguridad.usuarios_iden_seq OWNER TO postgres;

SET search_path = validacion, pg_catalog;

--
-- TOC entry 2927 (class 1259 OID 23976)
-- Dependencies: 11
-- Name: validacion_elemento_sequence; Type: SEQUENCE; Schema: validacion; Owner: postgres
--

CREATE SEQUENCE validacion_elemento_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE validacion.validacion_elemento_sequence OWNER TO postgres;

SET search_path = diccionario, pg_catalog;

--
-- TOC entry 3497 (class 2606 OID 19955)
-- Dependencies: 2724 2724
-- Name: pk_am; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ambito
    ADD CONSTRAINT pk_am PRIMARY KEY (iden);


--
-- TOC entry 3499 (class 2606 OID 19957)
-- Dependencies: 2726 2726
-- Name: pk_ashp; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ambitoshp
    ADD CONSTRAINT pk_ashp PRIMARY KEY (iden);


--
-- TOC entry 3502 (class 2606 OID 19959)
-- Dependencies: 2728 2728
-- Name: pk_b; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY boletin
    ADD CONSTRAINT pk_b PRIMARY KEY (iden);


--
-- TOC entry 3505 (class 2606 OID 19961)
-- Dependencies: 2730 2730
-- Name: pk_cd; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY caracterdeterminacion
    ADD CONSTRAINT pk_cd PRIMARY KEY (iden);


--
-- TOC entry 3508 (class 2606 OID 19963)
-- Dependencies: 2732 2732
-- Name: pk_cp; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY centroproduccion
    ADD CONSTRAINT pk_cp PRIMARY KEY (iden);


--
-- TOC entry 3513 (class 2606 OID 19965)
-- Dependencies: 2734 2734
-- Name: pk_dp; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY defpropiedad
    ADD CONSTRAINT pk_dp PRIMARY KEY (iden);


--
-- TOC entry 3516 (class 2606 OID 19967)
-- Dependencies: 2736 2736
-- Name: pk_dr; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY defrelacion
    ADD CONSTRAINT pk_dr PRIMARY KEY (iden);


--
-- TOC entry 3521 (class 2606 OID 19969)
-- Dependencies: 2738 2738
-- Name: pk_dv; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY defvector
    ADD CONSTRAINT pk_dv PRIMARY KEY (iden);


--
-- TOC entry 3524 (class 2606 OID 19971)
-- Dependencies: 2740 2740
-- Name: pk_gd; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY grupodocumento
    ADD CONSTRAINT pk_gd PRIMARY KEY (iden);


--
-- TOC entry 3528 (class 2606 OID 19973)
-- Dependencies: 2742 2742
-- Name: pk_ip; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY instrumentoplan
    ADD CONSTRAINT pk_ip PRIMARY KEY (iden);


--
-- TOC entry 3532 (class 2606 OID 19975)
-- Dependencies: 2744 2744
-- Name: pk_itop; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY instrumentotipooperacionplan
    ADD CONSTRAINT pk_itop PRIMARY KEY (iden);


--
-- TOC entry 3534 (class 2606 OID 19977)
-- Dependencies: 2746 2746
-- Name: pk_leg; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY legislacion
    ADD CONSTRAINT pk_leg PRIMARY KEY (iden);


--
-- TOC entry 3536 (class 2606 OID 19979)
-- Dependencies: 2748 2748
-- Name: pk_lit; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY literal
    ADD CONSTRAINT pk_lit PRIMARY KEY (iden);


--
-- TOC entry 3539 (class 2606 OID 19981)
-- Dependencies: 2750 2750
-- Name: pk_n; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY naturaleza
    ADD CONSTRAINT pk_n PRIMARY KEY (iden);


--
-- TOC entry 3548 (class 2606 OID 19983)
-- Dependencies: 2754 2754
-- Name: pk_oa; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY organigramaambito
    ADD CONSTRAINT pk_oa PRIMARY KEY (iden);


--
-- TOC entry 3544 (class 2606 OID 19985)
-- Dependencies: 2752 2752
-- Name: pk_oc; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacioncaracter
    ADD CONSTRAINT pk_oc PRIMARY KEY (iden);


--
-- TOC entry 3551 (class 2606 OID 19987)
-- Dependencies: 2756 2756
-- Name: pk_org; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY organo
    ADD CONSTRAINT pk_org PRIMARY KEY (iden);


--
-- TOC entry 3554 (class 2606 OID 19989)
-- Dependencies: 2758 2758
-- Name: pk_sen; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sentido
    ADD CONSTRAINT pk_sen PRIMARY KEY (iden);


--
-- TOC entry 3556 (class 2606 OID 19991)
-- Dependencies: 2760 2760
-- Name: pk_tab; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tabla
    ADD CONSTRAINT pk_tab PRIMARY KEY (iden);


--
-- TOC entry 3562 (class 2606 OID 19993)
-- Dependencies: 2764 2764
-- Name: pk_tdp; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipodefpropiedad
    ADD CONSTRAINT pk_tdp PRIMARY KEY (iden);


--
-- TOC entry 3571 (class 2606 OID 19995)
-- Dependencies: 2770 2770
-- Name: pk_tod; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipooperaciondeterminacion
    ADD CONSTRAINT pk_tod PRIMARY KEY (iden);


--
-- TOC entry 3575 (class 2606 OID 19997)
-- Dependencies: 2772 2772
-- Name: pk_toe; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipooperacionentidad
    ADD CONSTRAINT pk_toe PRIMARY KEY (iden);


--
-- TOC entry 3581 (class 2606 OID 19999)
-- Dependencies: 2776 2776
-- Name: pk_topr; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipooperacionrelacion
    ADD CONSTRAINT pk_topr PRIMARY KEY (iden);


--
-- TOC entry 3559 (class 2606 OID 20001)
-- Dependencies: 2762 2762
-- Name: pk_tpa; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipoambito
    ADD CONSTRAINT pk_tpa PRIMARY KEY (iden);


--
-- TOC entry 3565 (class 2606 OID 20003)
-- Dependencies: 2766 2766
-- Name: pk_tpdoc; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipodocumento
    ADD CONSTRAINT pk_tpdoc PRIMARY KEY (iden);


--
-- TOC entry 3568 (class 2606 OID 20005)
-- Dependencies: 2768 2768
-- Name: pk_tpent; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipoentidad
    ADD CONSTRAINT pk_tpent PRIMARY KEY (iden);


--
-- TOC entry 3578 (class 2606 OID 20007)
-- Dependencies: 2774 2774
-- Name: pk_tpop; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipooperacionplan
    ADD CONSTRAINT pk_tpop PRIMARY KEY (iden);


--
-- TOC entry 3587 (class 2606 OID 20009)
-- Dependencies: 2780 2780
-- Name: pk_trd; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY traduccion
    ADD CONSTRAINT pk_trd PRIMARY KEY (iden);


--
-- TOC entry 3584 (class 2606 OID 20011)
-- Dependencies: 2778 2778
-- Name: pk_ttra; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipotramite
    ADD CONSTRAINT pk_ttra PRIMARY KEY (iden);


--
-- TOC entry 3589 (class 2606 OID 20013)
-- Dependencies: 2782 2782
-- Name: pk_ver; Type: CONSTRAINT; Schema: diccionario; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY version
    ADD CONSTRAINT pk_ver PRIMARY KEY (iden);


SET search_path = explotacion, pg_catalog;

--
-- TOC entry 3591 (class 2606 OID 20015)
-- Dependencies: 2784 2784
-- Name: capa_orden_key; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY capa
    ADD CONSTRAINT capa_orden_key UNIQUE (orden);


--
-- TOC entry 3593 (class 2606 OID 20017)
-- Dependencies: 2784 2784
-- Name: pk_c; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY capa
    ADD CONSTRAINT pk_c PRIMARY KEY (iden);


--
-- TOC entry 3602 (class 2606 OID 20019)
-- Dependencies: 2790 2790
-- Name: pk_cg; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY capagrupo
    ADD CONSTRAINT pk_cg PRIMARY KEY (iden);


--
-- TOC entry 3596 (class 2606 OID 20021)
-- Dependencies: 2786 2786
-- Name: pk_crc; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY capaentidadconjunto
    ADD CONSTRAINT pk_crc PRIMARY KEY (iden);


--
-- TOC entry 3599 (class 2606 OID 20023)
-- Dependencies: 2788 2788
-- Name: pk_crl; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY capaentidadlista
    ADD CONSTRAINT pk_crl PRIMARY KEY (iden);


--
-- TOC entry 3605 (class 2606 OID 20025)
-- Dependencies: 2792 2792
-- Name: pk_lal; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY leyenda_al
    ADD CONSTRAINT pk_lal PRIMARY KEY (iden);


--
-- TOC entry 3609 (class 2606 OID 20027)
-- Dependencies: 2794 2794
-- Name: pk_lpd; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY leyenda_pd
    ADD CONSTRAINT pk_lpd PRIMARY KEY (iden);


--
-- TOC entry 3612 (class 2606 OID 20029)
-- Dependencies: 2796 2796
-- Name: pk_lvt; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY leyenda_vt
    ADD CONSTRAINT pk_lvt PRIMARY KEY (iden);


--
-- TOC entry 3615 (class 2606 OID 20031)
-- Dependencies: 2798 2798
-- Name: pk_progr; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY procedimiento_gr
    ADD CONSTRAINT pk_progr PRIMARY KEY (iden);


--
-- TOC entry 3617 (class 2606 OID 20033)
-- Dependencies: 2800 2800
-- Name: pk_propd; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY procedimiento_pd
    ADD CONSTRAINT pk_propd PRIMARY KEY (iden);


--
-- TOC entry 3620 (class 2606 OID 20035)
-- Dependencies: 2802 2802
-- Name: pk_prorg; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY procedimiento_rg
    ADD CONSTRAINT pk_prorg PRIMARY KEY (iden);


--
-- TOC entry 3623 (class 2606 OID 20037)
-- Dependencies: 2804 2804
-- Name: pk_provt; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY procedimiento_vt
    ADD CONSTRAINT pk_provt PRIMARY KEY (iden);


--
-- TOC entry 3625 (class 2606 OID 20039)
-- Dependencies: 2806 2806
-- Name: pk_ptem; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY procedimientotematico
    ADD CONSTRAINT pk_ptem PRIMARY KEY (iden);


--
-- TOC entry 3628 (class 2606 OID 20041)
-- Dependencies: 2808 2808
-- Name: pk_rval; Type: CONSTRAINT; Schema: explotacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rangovalor
    ADD CONSTRAINT pk_rval PRIMARY KEY (iden);


SET search_path = ficha, pg_catalog;

--
-- TOC entry 3808 (class 2606 OID 32890)
-- Dependencies: 3009 3009
-- Name: conjuntodeterminacion_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY grupodeterminacion
    ADD CONSTRAINT conjuntodeterminacion_pkey PRIMARY KEY (iden);


--
-- TOC entry 3784 (class 2606 OID 32892)
-- Dependencies: 2998 2998
-- Name: conjuntodeterminaciongrupo_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY conjuntodeterminaciongrupo
    ADD CONSTRAINT conjuntodeterminaciongrupo_pkey PRIMARY KEY (iden);


--
-- TOC entry 3786 (class 2606 OID 32894)
-- Dependencies: 3000 3000
-- Name: conjuntogrupo_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY conjuntogrupo
    ADD CONSTRAINT conjuntogrupo_pkey PRIMARY KEY (iden);


--
-- TOC entry 3790 (class 2606 OID 32896)
-- Dependencies: 3002 3002
-- Name: determinacionclasifacto_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY determinacionclasifacto
    ADD CONSTRAINT determinacionclasifacto_pkey PRIMARY KEY (iden);


--
-- TOC entry 3794 (class 2606 OID 32898)
-- Dependencies: 3003 3003
-- Name: determinacionclasifuso_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY determinacionclasifuso
    ADD CONSTRAINT determinacionclasifuso_pkey PRIMARY KEY (iden);


--
-- TOC entry 3796 (class 2606 OID 32900)
-- Dependencies: 3005 3005
-- Name: ficha_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ficha
    ADD CONSTRAINT ficha_pkey PRIMARY KEY (iden);


--
-- TOC entry 3801 (class 2606 OID 32902)
-- Dependencies: 3007 3007
-- Name: fichaconjuntogrupo_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY fichaconjuntogrupo
    ADD CONSTRAINT fichaconjuntogrupo_pkey PRIMARY KEY (iden);


--
-- TOC entry 3806 (class 2606 OID 32904)
-- Dependencies: 3008 3008
-- Name: fichagrupodeterminacion_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY fichagrupodeterminacion
    ADD CONSTRAINT fichagrupodeterminacion_pkey PRIMARY KEY (iden);


--
-- TOC entry 3814 (class 2606 OID 32906)
-- Dependencies: 3011 3011
-- Name: grupodeterminaciondeterminacion_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY grupodeterminaciondeterminacion
    ADD CONSTRAINT grupodeterminaciondeterminacion_pkey PRIMARY KEY (iden);


--
-- TOC entry 3818 (class 2606 OID 32908)
-- Dependencies: 3012 3012
-- Name: regimenesacto_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY regimenesacto
    ADD CONSTRAINT regimenesacto_pkey PRIMARY KEY (iden);


--
-- TOC entry 3822 (class 2606 OID 32910)
-- Dependencies: 3013 3013
-- Name: regimenesuso_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY regimenesuso
    ADD CONSTRAINT regimenesuso_pkey PRIMARY KEY (iden);


--
-- TOC entry 3829 (class 2606 OID 32912)
-- Dependencies: 3020 3020
-- Name: regimenuso_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY valoresclasifuso
    ADD CONSTRAINT regimenuso_pkey PRIMARY KEY (iden);


--
-- TOC entry 3827 (class 2606 OID 32914)
-- Dependencies: 3015 3015
-- Name: valoresclasifacto_pkey; Type: CONSTRAINT; Schema: ficha; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY valoresclasifacto
    ADD CONSTRAINT valoresclasifacto_pkey PRIMARY KEY (iden);


SET search_path = planeamiento, pg_catalog;

--
-- TOC entry 3673 (class 2606 OID 20043)
-- Dependencies: 2846 2846
-- Name: pk_aaa; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ambitoaplicacionambito
    ADD CONSTRAINT pk_aaa PRIMARY KEY (iden);


--
-- TOC entry 3682 (class 2606 OID 20045)
-- Dependencies: 2853 2853
-- Name: pk_bt; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY boletintramite
    ADD CONSTRAINT pk_bt PRIMARY KEY (iden);


--
-- TOC entry 3631 (class 2606 OID 20047)
-- Dependencies: 2810 2810
-- Name: pk_ced; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY casoentidaddeterminacion
    ADD CONSTRAINT pk_ced PRIMARY KEY (iden);


--
-- TOC entry 3686 (class 2606 OID 20049)
-- Dependencies: 2860 2860
-- Name: pk_cent; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY conjuntoentidad
    ADD CONSTRAINT pk_cent PRIMARY KEY (iden);


--
-- TOC entry 3637 (class 2606 OID 20051)
-- Dependencies: 2812 2812
-- Name: pk_d; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT pk_d PRIMARY KEY (iden);


--
-- TOC entry 3690 (class 2606 OID 20053)
-- Dependencies: 2862 2862
-- Name: pk_dge; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY determinaciongrupoentidad
    ADD CONSTRAINT pk_dge PRIMARY KEY (iden);


--
-- TOC entry 3694 (class 2606 OID 20055)
-- Dependencies: 2864 2864
-- Name: pk_doc; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT pk_doc PRIMARY KEY (iden);


--
-- TOC entry 3698 (class 2606 OID 20057)
-- Dependencies: 2866 2866
-- Name: pk_docc; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentocaso
    ADD CONSTRAINT pk_docc PRIMARY KEY (iden);


--
-- TOC entry 3702 (class 2606 OID 20059)
-- Dependencies: 2868 2868
-- Name: pk_docd; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentodeterminacion
    ADD CONSTRAINT pk_docd PRIMARY KEY (iden);


--
-- TOC entry 3706 (class 2606 OID 20061)
-- Dependencies: 2870 2870
-- Name: pk_doce; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentoentidad
    ADD CONSTRAINT pk_doce PRIMARY KEY (iden);


--
-- TOC entry 3708 (class 2606 OID 20063)
-- Dependencies: 2872 2872
-- Name: pk_docshp; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentoshp
    ADD CONSTRAINT pk_docshp PRIMARY KEY (iden);


--
-- TOC entry 3643 (class 2606 OID 20065)
-- Dependencies: 2814 2814
-- Name: pk_e; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT pk_e PRIMARY KEY (iden);


--
-- TOC entry 3647 (class 2606 OID 20067)
-- Dependencies: 2816 2816
-- Name: pk_ed; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidaddeterminacion
    ADD CONSTRAINT pk_ed PRIMARY KEY (iden);


--
-- TOC entry 3653 (class 2606 OID 20069)
-- Dependencies: 2818 2818
-- Name: pk_edr; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT pk_edr PRIMARY KEY (iden);


--
-- TOC entry 3655 (class 2606 OID 20071)
-- Dependencies: 2820 2820
-- Name: pk_elin; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidadlin
    ADD CONSTRAINT pk_elin PRIMARY KEY (iden);


--
-- TOC entry 3657 (class 2606 OID 20073)
-- Dependencies: 2822 2822
-- Name: pk_epnt; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidadpnt
    ADD CONSTRAINT pk_epnt PRIMARY KEY (iden);


--
-- TOC entry 3659 (class 2606 OID 20075)
-- Dependencies: 2824 2824
-- Name: pk_epol; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidadpol
    ADD CONSTRAINT pk_epol PRIMARY KEY (iden);


--
-- TOC entry 3716 (class 2606 OID 20077)
-- Dependencies: 2879 2879
-- Name: pk_od; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT pk_od PRIMARY KEY (iden);


--
-- TOC entry 3663 (class 2606 OID 20079)
-- Dependencies: 2826 2826
-- Name: pk_odet; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY opciondeterminacion
    ADD CONSTRAINT pk_odet PRIMARY KEY (iden);


--
-- TOC entry 3721 (class 2606 OID 20081)
-- Dependencies: 2881 2881
-- Name: pk_oe; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT pk_oe PRIMARY KEY (iden);


--
-- TOC entry 3711 (class 2606 OID 20083)
-- Dependencies: 2877 2877
-- Name: pk_op; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacion
    ADD CONSTRAINT pk_op PRIMARY KEY (iden);


--
-- TOC entry 3677 (class 2606 OID 20085)
-- Dependencies: 2848 2848
-- Name: pk_opp; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT pk_opp PRIMARY KEY (iden);


--
-- TOC entry 3725 (class 2606 OID 20087)
-- Dependencies: 2883 2883
-- Name: pk_or; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT pk_or PRIMARY KEY (iden);


--
-- TOC entry 3667 (class 2606 OID 20089)
-- Dependencies: 2828 2828
-- Name: pk_p; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT pk_p PRIMARY KEY (iden);


--
-- TOC entry 3729 (class 2606 OID 20091)
-- Dependencies: 2885 2885
-- Name: pk_peord; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY planentidadordenacion
    ADD CONSTRAINT pk_peord PRIMARY KEY (iden);


--
-- TOC entry 3731 (class 2606 OID 20093)
-- Dependencies: 2887 2887
-- Name: pk_pleg; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY planlegislacion
    ADD CONSTRAINT pk_pleg PRIMARY KEY (iden);


--
-- TOC entry 3734 (class 2606 OID 20095)
-- Dependencies: 2889 2889
-- Name: pk_pr; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY propiedadrelacion
    ADD CONSTRAINT pk_pr PRIMARY KEY (iden);


--
-- TOC entry 3679 (class 2606 OID 20097)
-- Dependencies: 2850 2850
-- Name: pk_pshp; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY planshp
    ADD CONSTRAINT pk_pshp PRIMARY KEY (iden);


--
-- TOC entry 3738 (class 2606 OID 20099)
-- Dependencies: 2895 2895
-- Name: pk_re; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY regimenespecifico
    ADD CONSTRAINT pk_re PRIMARY KEY (iden);


--
-- TOC entry 3741 (class 2606 OID 20101)
-- Dependencies: 2897 2897
-- Name: pk_rl; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY relacion
    ADD CONSTRAINT pk_rl PRIMARY KEY (iden);


--
-- TOC entry 3670 (class 2606 OID 20103)
-- Dependencies: 2830 2830
-- Name: pk_tra; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tramite
    ADD CONSTRAINT pk_tra PRIMARY KEY (iden);


--
-- TOC entry 3748 (class 2606 OID 20105)
-- Dependencies: 2907 2907
-- Name: pk_vc; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vinculocaso
    ADD CONSTRAINT pk_vc PRIMARY KEY (iden);


--
-- TOC entry 3744 (class 2606 OID 20107)
-- Dependencies: 2905 2905
-- Name: pk_vr; Type: CONSTRAINT; Schema: planeamiento; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vectorrelacion
    ADD CONSTRAINT pk_vr PRIMARY KEY (iden);


SET search_path = seguridad, pg_catalog;

--
-- TOC entry 3750 (class 2606 OID 20109)
-- Dependencies: 2912 2912
-- Name: diario_pkey; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY diario
    ADD CONSTRAINT diario_pkey PRIMARY KEY (iden);


--
-- TOC entry 3755 (class 2606 OID 20111)
-- Dependencies: 2913 2913
-- Name: fip1_pk; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY fip1
    ADD CONSTRAINT fip1_pk PRIMARY KEY (iden);


--
-- TOC entry 3757 (class 2606 OID 20113)
-- Dependencies: 2915 2915
-- Name: operacion_pkey; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operaciones
    ADD CONSTRAINT operacion_pkey PRIMARY KEY (iden);


--
-- TOC entry 3759 (class 2606 OID 20115)
-- Dependencies: 2918 2918
-- Name: rol_codigo_key; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rol
    ADD CONSTRAINT rol_codigo_key UNIQUE (codigo);


--
-- TOC entry 3761 (class 2606 OID 20117)
-- Dependencies: 2918 2918
-- Name: rol_nombre_key; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rol
    ADD CONSTRAINT rol_nombre_key UNIQUE (nombre);


--
-- TOC entry 3763 (class 2606 OID 20119)
-- Dependencies: 2918 2918
-- Name: rol_pkey; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (iden);


--
-- TOC entry 3765 (class 2606 OID 20121)
-- Dependencies: 2919 2919
-- Name: subsistema_pk; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY subsistema
    ADD CONSTRAINT subsistema_pk PRIMARY KEY (iden);


--
-- TOC entry 3771 (class 2606 OID 20123)
-- Dependencies: 2922 2922
-- Name: usuarioRol_pkey; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuariorol
    ADD CONSTRAINT "usuarioRol_pkey" PRIMARY KEY (iden);


--
-- TOC entry 3767 (class 2606 OID 20125)
-- Dependencies: 2920 2920
-- Name: usuarios_nombre_key; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuarios_nombre_key UNIQUE (nombre);


--
-- TOC entry 3769 (class 2606 OID 20127)
-- Dependencies: 2920 2920
-- Name: usuarios_pkey; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (iden);


SET search_path = validacion, pg_catalog;

--
-- TOC entry 3779 (class 2606 OID 24025)
-- Dependencies: 2934 2934
-- Name: pk_error; Type: CONSTRAINT; Schema: validacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY error
    ADD CONSTRAINT pk_error PRIMARY KEY (iden);


--
-- TOC entry 3775 (class 2606 OID 24001)
-- Dependencies: 2932 2932
-- Name: pk_procesovalidacion; Type: CONSTRAINT; Schema: validacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY proceso
    ADD CONSTRAINT pk_procesovalidacion PRIMARY KEY (iden);


--
-- TOC entry 3777 (class 2606 OID 24006)
-- Dependencies: 2933 2933 2933
-- Name: pk_resultado; Type: CONSTRAINT; Schema: validacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY resultado
    ADD CONSTRAINT pk_resultado PRIMARY KEY (idproceso, idvalidacion);


--
-- TOC entry 3773 (class 2606 OID 23992)
-- Dependencies: 2931 2931
-- Name: pk_validacion; Type: CONSTRAINT; Schema: validacion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY validacion
    ADD CONSTRAINT pk_validacion PRIMARY KEY (iden);


SET search_path = diccionario, pg_catalog;

--
-- TOC entry 3494 (class 1259 OID 20398)
-- Dependencies: 2724
-- Name: fki_ambito_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_ambito_idliteral ON ambito USING btree (idliteral);


--
-- TOC entry 3495 (class 1259 OID 20399)
-- Dependencies: 2724
-- Name: fki_ambito_idtipoambito; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_ambito_idtipoambito ON ambito USING btree (idtipoambito);


--
-- TOC entry 3500 (class 1259 OID 20400)
-- Dependencies: 2728
-- Name: fki_boletin_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_boletin_idliteral ON boletin USING btree (idliteral);


--
-- TOC entry 3503 (class 1259 OID 20401)
-- Dependencies: 2730
-- Name: fki_caracterdeterminacion_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_caracterdeterminacion_idliteral ON caracterdeterminacion USING btree (idliteral);


--
-- TOC entry 3506 (class 1259 OID 20402)
-- Dependencies: 2732
-- Name: fki_centroproduccion_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_centroproduccion_idliteral ON centroproduccion USING btree (idliteral);


--
-- TOC entry 3509 (class 1259 OID 20403)
-- Dependencies: 2734
-- Name: fki_defpropiedad_iddefrelacion; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_defpropiedad_iddefrelacion ON defpropiedad USING btree (iddefrelacion);


--
-- TOC entry 3510 (class 1259 OID 20404)
-- Dependencies: 2734
-- Name: fki_defpropiedad_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_defpropiedad_idliteral ON defpropiedad USING btree (idliteral);


--
-- TOC entry 3511 (class 1259 OID 20405)
-- Dependencies: 2734
-- Name: fki_defpropiedad_idtipodefpropiedad; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_defpropiedad_idtipodefpropiedad ON defpropiedad USING btree (idtipodefpropiedad);


--
-- TOC entry 3514 (class 1259 OID 20406)
-- Dependencies: 2736
-- Name: fki_defrelacion_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_defrelacion_idliteral ON defrelacion USING btree (idliteral);


--
-- TOC entry 3517 (class 1259 OID 20407)
-- Dependencies: 2738
-- Name: fki_defvector_iddefrelacion; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_defvector_iddefrelacion ON defvector USING btree (iddefrelacion);


--
-- TOC entry 3518 (class 1259 OID 20408)
-- Dependencies: 2738
-- Name: fki_defvector_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_defvector_idliteral ON defvector USING btree (idliteral);


--
-- TOC entry 3519 (class 1259 OID 20409)
-- Dependencies: 2738
-- Name: fki_defvector_idtabla; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_defvector_idtabla ON defvector USING btree (idtabla);


--
-- TOC entry 3522 (class 1259 OID 20410)
-- Dependencies: 2740
-- Name: fki_grupodocumento_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_grupodocumento_idliteral ON grupodocumento USING btree (idliteral);


--
-- TOC entry 3525 (class 1259 OID 20411)
-- Dependencies: 2742
-- Name: fki_instrumentoplan_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_instrumentoplan_idliteral ON instrumentoplan USING btree (idliteral);


--
-- TOC entry 3526 (class 1259 OID 20412)
-- Dependencies: 2742
-- Name: fki_instrumentoplan_idnaturaleza; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_instrumentoplan_idnaturaleza ON instrumentoplan USING btree (idnaturaleza);


--
-- TOC entry 3529 (class 1259 OID 20413)
-- Dependencies: 2744
-- Name: fki_instrumentotipooperacionplan_idinstrumentoplan; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_instrumentotipooperacionplan_idinstrumentoplan ON instrumentotipooperacionplan USING btree (idinstrumentoplan);


--
-- TOC entry 3530 (class 1259 OID 20414)
-- Dependencies: 2744
-- Name: fki_instrumentotipooperacionplan_idtipooperacionplan; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_instrumentotipooperacionplan_idtipooperacionplan ON instrumentotipooperacionplan USING btree (idtipooperacionplan);


--
-- TOC entry 3537 (class 1259 OID 20415)
-- Dependencies: 2750
-- Name: fki_naturaleza_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_naturaleza_idliteral ON naturaleza USING btree (idliteral);


--
-- TOC entry 3540 (class 1259 OID 20416)
-- Dependencies: 2752
-- Name: fki_operacioncaracter_idcaracteroperado; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacioncaracter_idcaracteroperado ON operacioncaracter USING btree (idcaracteroperado);


--
-- TOC entry 3541 (class 1259 OID 20417)
-- Dependencies: 2752
-- Name: fki_operacioncaracter_idcaracteroperador; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacioncaracter_idcaracteroperador ON operacioncaracter USING btree (idcaracteroperador);


--
-- TOC entry 3542 (class 1259 OID 20418)
-- Dependencies: 2752
-- Name: fki_operacioncaracter_idtipooperaciondet; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacioncaracter_idtipooperaciondet ON operacioncaracter USING btree (idtipooperaciondet);


--
-- TOC entry 3545 (class 1259 OID 20419)
-- Dependencies: 2754
-- Name: fki_organigramaambito_idambitohijo; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_organigramaambito_idambitohijo ON organigramaambito USING btree (idambitohijo);


--
-- TOC entry 3546 (class 1259 OID 20420)
-- Dependencies: 2754
-- Name: fki_organigramaambito_idambitopadre; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_organigramaambito_idambitopadre ON organigramaambito USING btree (idambitopadre);


--
-- TOC entry 3549 (class 1259 OID 20421)
-- Dependencies: 2756
-- Name: fki_organo_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_organo_idliteral ON organo USING btree (idliteral);


--
-- TOC entry 3552 (class 1259 OID 20422)
-- Dependencies: 2758
-- Name: fki_sentido_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_sentido_idliteral ON sentido USING btree (idliteral);


--
-- TOC entry 3557 (class 1259 OID 20423)
-- Dependencies: 2762
-- Name: fki_tipoambito_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tipoambito_idliteral ON tipoambito USING btree (idliteral);


--
-- TOC entry 3560 (class 1259 OID 20424)
-- Dependencies: 2764
-- Name: fki_tipodefpropiedad_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tipodefpropiedad_idliteral ON tipodefpropiedad USING btree (idliteral);


--
-- TOC entry 3563 (class 1259 OID 20425)
-- Dependencies: 2766
-- Name: fki_tipodocumento_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tipodocumento_idliteral ON tipodocumento USING btree (idliteral);


--
-- TOC entry 3566 (class 1259 OID 20426)
-- Dependencies: 2768
-- Name: fki_tipoentidad_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tipoentidad_idliteral ON tipoentidad USING btree (idliteral);


--
-- TOC entry 3569 (class 1259 OID 20427)
-- Dependencies: 2770
-- Name: fki_tipooperaciondeterminacion_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tipooperaciondeterminacion_idliteral ON tipooperaciondeterminacion USING btree (idliteral);


--
-- TOC entry 3572 (class 1259 OID 20428)
-- Dependencies: 2772
-- Name: fki_tipooperacionentidad_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tipooperacionentidad_idliteral ON tipooperacionentidad USING btree (idliteral);


--
-- TOC entry 3573 (class 1259 OID 20429)
-- Dependencies: 2772
-- Name: fki_tipooperacionentidad_idtipoentidad; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tipooperacionentidad_idtipoentidad ON tipooperacionentidad USING btree (idtipoentidad);


--
-- TOC entry 3576 (class 1259 OID 20430)
-- Dependencies: 2774
-- Name: fki_tipooperacionplan_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tipooperacionplan_idliteral ON tipooperacionplan USING btree (idliteral);


--
-- TOC entry 3579 (class 1259 OID 20431)
-- Dependencies: 2776
-- Name: fki_tipooperacionrelacion_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tipooperacionrelacion_idliteral ON tipooperacionrelacion USING btree (idliteral);


--
-- TOC entry 3582 (class 1259 OID 20432)
-- Dependencies: 2778
-- Name: fki_tipotramite_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tipotramite_idliteral ON tipotramite USING btree (idliteral);


--
-- TOC entry 3585 (class 1259 OID 20433)
-- Dependencies: 2780
-- Name: fki_traduccion_idliteral; Type: INDEX; Schema: diccionario; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_traduccion_idliteral ON traduccion USING btree (idliteral);


SET search_path = explotacion, pg_catalog;

--
-- TOC entry 3600 (class 1259 OID 20434)
-- Dependencies: 2790
-- Name: fki_capagrupo_idcapa; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_capagrupo_idcapa ON capagrupo USING btree (idcapa);


--
-- TOC entry 3594 (class 1259 OID 20435)
-- Dependencies: 2786
-- Name: fki_caparecintoconjunto_idcapa; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_caparecintoconjunto_idcapa ON capaentidadconjunto USING btree (idcapa);


--
-- TOC entry 3597 (class 1259 OID 20436)
-- Dependencies: 2788
-- Name: fki_caparecintolista_idcapa; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_caparecintolista_idcapa ON capaentidadlista USING btree (idcapa);


--
-- TOC entry 3603 (class 1259 OID 20437)
-- Dependencies: 2792
-- Name: fki_leyenda_al_idcapa; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_leyenda_al_idcapa ON leyenda_al USING btree (idcapa);


--
-- TOC entry 3606 (class 1259 OID 20438)
-- Dependencies: 2794
-- Name: fki_leyenda_pd_idcapa; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_leyenda_pd_idcapa ON leyenda_pd USING btree (idcapa);


--
-- TOC entry 3607 (class 1259 OID 20439)
-- Dependencies: 2794
-- Name: fki_leyenda_pd_idpropd; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_leyenda_pd_idpropd ON leyenda_pd USING btree (idpropd);


--
-- TOC entry 3610 (class 1259 OID 20440)
-- Dependencies: 2796
-- Name: fki_leyenda_vt_idcapa; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_leyenda_vt_idcapa ON leyenda_vt USING btree (idcapa);


--
-- TOC entry 3613 (class 1259 OID 20441)
-- Dependencies: 2798
-- Name: fki_procedimiento_gr_idcapagrupo; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_procedimiento_gr_idcapagrupo ON procedimiento_gr USING btree (idcapagrupo);


--
-- TOC entry 3618 (class 1259 OID 20442)
-- Dependencies: 2802
-- Name: fki_procedimiento_rg_idcapa; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_procedimiento_rg_idcapa ON procedimiento_rg USING btree (idcapa);


--
-- TOC entry 3621 (class 1259 OID 20443)
-- Dependencies: 2804
-- Name: fki_procedimiento_vt_idcapagrupo; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_procedimiento_vt_idcapagrupo ON procedimiento_vt USING btree (idcapagrupo);


--
-- TOC entry 3626 (class 1259 OID 20444)
-- Dependencies: 2808
-- Name: fki_rangovalor_idcapa; Type: INDEX; Schema: explotacion; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_rangovalor_idcapa ON rangovalor USING btree (idcapa);


SET search_path = ficha, pg_catalog;

--
-- TOC entry 3780 (class 1259 OID 32915)
-- Dependencies: 2998
-- Name: conjuntodeterminaciongrupo_idconjunto_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX conjuntodeterminaciongrupo_idconjunto_idx ON conjuntodeterminaciongrupo USING btree (idconjunto);


--
-- TOC entry 3781 (class 1259 OID 32916)
-- Dependencies: 2998
-- Name: conjuntodeterminaciongrupo_iddeterminacion_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX conjuntodeterminaciongrupo_iddeterminacion_idx ON conjuntodeterminaciongrupo USING btree (iddeterminacion);


--
-- TOC entry 3782 (class 1259 OID 32917)
-- Dependencies: 2998
-- Name: conjuntodeterminaciongrupo_orden_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX conjuntodeterminaciongrupo_orden_idx ON conjuntodeterminaciongrupo USING btree (orden);


--
-- TOC entry 3787 (class 1259 OID 32918)
-- Dependencies: 3002
-- Name: determinacionclasifacto_determinacion_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX determinacionclasifacto_determinacion_idx ON determinacionclasifacto USING btree (iddeterminacion);


--
-- TOC entry 3788 (class 1259 OID 32919)
-- Dependencies: 3002
-- Name: determinacionclasifacto_ficha_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX determinacionclasifacto_ficha_idx ON determinacionclasifacto USING btree (idficha);


--
-- TOC entry 3791 (class 1259 OID 32920)
-- Dependencies: 3003
-- Name: determinacionclasifuso_determinacion_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX determinacionclasifuso_determinacion_idx ON determinacionclasifuso USING btree (iddeterminacion);


--
-- TOC entry 3792 (class 1259 OID 32921)
-- Dependencies: 3003
-- Name: determinacionclasifuso_ficha_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX determinacionclasifuso_ficha_idx ON determinacionclasifuso USING btree (idficha);


--
-- TOC entry 3797 (class 1259 OID 32922)
-- Dependencies: 3007
-- Name: fichaconjuntogrupo_conjunto_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX fichaconjuntogrupo_conjunto_idx ON fichaconjuntogrupo USING btree (idconjunto);


--
-- TOC entry 3798 (class 1259 OID 32923)
-- Dependencies: 3007
-- Name: fichaconjuntogrupo_ficha_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX fichaconjuntogrupo_ficha_idx ON fichaconjuntogrupo USING btree (idficha);


--
-- TOC entry 3799 (class 1259 OID 32924)
-- Dependencies: 3007
-- Name: fichaconjuntogrupo_orden_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX fichaconjuntogrupo_orden_idx ON fichaconjuntogrupo USING btree (orden);


--
-- TOC entry 3802 (class 1259 OID 32925)
-- Dependencies: 3008
-- Name: fichagrupodeterminacion_ficha_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX fichagrupodeterminacion_ficha_idx ON fichagrupodeterminacion USING btree (idficha);


--
-- TOC entry 3803 (class 1259 OID 32926)
-- Dependencies: 3008
-- Name: fichagrupodeterminacion_grupo_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX fichagrupodeterminacion_grupo_idx ON fichagrupodeterminacion USING btree (idgrupo);


--
-- TOC entry 3804 (class 1259 OID 32927)
-- Dependencies: 3008
-- Name: fichagrupodeterminacion_orden_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX fichagrupodeterminacion_orden_idx ON fichagrupodeterminacion USING btree (orden);


--
-- TOC entry 3809 (class 1259 OID 32928)
-- Dependencies: 3009
-- Name: grupodeterminacion_conjuntogrupo_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX grupodeterminacion_conjuntogrupo_idx ON grupodeterminacion USING btree (idconjuntogrupo);


--
-- TOC entry 3810 (class 1259 OID 32929)
-- Dependencies: 3011
-- Name: grupodeterminaciondeterminacion_determinacion_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX grupodeterminaciondeterminacion_determinacion_idx ON grupodeterminaciondeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3811 (class 1259 OID 32930)
-- Dependencies: 3011
-- Name: grupodeterminaciondeterminacion_grupo_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX grupodeterminaciondeterminacion_grupo_idx ON grupodeterminaciondeterminacion USING btree (idgrupo);


--
-- TOC entry 3812 (class 1259 OID 32931)
-- Dependencies: 3011
-- Name: grupodeterminaciondeterminacion_orden_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX grupodeterminaciondeterminacion_orden_idx ON grupodeterminaciondeterminacion USING btree (orden);


--
-- TOC entry 3815 (class 1259 OID 32932)
-- Dependencies: 3012
-- Name: regimenesacto_determinacion_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX regimenesacto_determinacion_idx ON regimenesacto USING btree (iddeterminacion);


--
-- TOC entry 3816 (class 1259 OID 32933)
-- Dependencies: 3012
-- Name: regimenesacto_ficha_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX regimenesacto_ficha_idx ON regimenesacto USING btree (idficha);


--
-- TOC entry 3819 (class 1259 OID 32934)
-- Dependencies: 3013
-- Name: regimenesuso_determinacion_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX regimenesuso_determinacion_idx ON regimenesuso USING btree (iddeterminacion);


--
-- TOC entry 3820 (class 1259 OID 32935)
-- Dependencies: 3013
-- Name: regimenesuso_ficha_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX regimenesuso_ficha_idx ON regimenesuso USING btree (idficha);


--
-- TOC entry 3823 (class 1259 OID 32936)
-- Dependencies: 3015
-- Name: valoresclasifacto_detclasif_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX valoresclasifacto_detclasif_idx ON valoresclasifacto USING btree (iddeterminacionclasifacto);


--
-- TOC entry 3824 (class 1259 OID 32937)
-- Dependencies: 3015
-- Name: valoresclasifacto_detvalorreg_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX valoresclasifacto_detvalorreg_idx ON valoresclasifacto USING btree (iddeterminacionvalorregimen);


--
-- TOC entry 3825 (class 1259 OID 32938)
-- Dependencies: 3015
-- Name: valoresclasifacto_orden_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX valoresclasifacto_orden_idx ON valoresclasifacto USING btree (orden);


--
-- TOC entry 3830 (class 1259 OID 32939)
-- Dependencies: 3020
-- Name: valoresclasifuso_detclasif_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX valoresclasifuso_detclasif_idx ON valoresclasifuso USING btree (iddeterminacionclasifuso);


--
-- TOC entry 3831 (class 1259 OID 32940)
-- Dependencies: 3020
-- Name: valoresclasifuso_detvalorreg_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX valoresclasifuso_detvalorreg_idx ON valoresclasifuso USING btree (iddeterminacionvalorregimen);


--
-- TOC entry 3832 (class 1259 OID 32941)
-- Dependencies: 3020
-- Name: valoresclasifuso_orden_idx; Type: INDEX; Schema: ficha; Owner: postgres; Tablespace: 
--

CREATE INDEX valoresclasifuso_orden_idx ON valoresclasifuso USING btree (orden);


SET search_path = planeamiento, pg_catalog;

--
-- TOC entry 3671 (class 1259 OID 20445)
-- Dependencies: 2846
-- Name: fki_ambitoaplicacionambito_identidad; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_ambitoaplicacionambito_identidad ON ambitoaplicacionambito USING btree (identidad);


--
-- TOC entry 3680 (class 1259 OID 20446)
-- Dependencies: 2853
-- Name: fki_boletintramite_idtramite; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_boletintramite_idtramite ON boletintramite USING btree (idtramite);


--
-- TOC entry 3629 (class 1259 OID 20447)
-- Dependencies: 2810
-- Name: fki_casoentidaddeterminacion_identidaddeterminacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_casoentidaddeterminacion_identidaddeterminacion ON casoentidaddeterminacion USING btree (identidaddeterminacion);


--
-- TOC entry 3683 (class 1259 OID 20448)
-- Dependencies: 2860
-- Name: fki_conjuntoentidad_identidadconjunto; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_conjuntoentidad_identidadconjunto ON conjuntoentidad USING btree (identidadconjunto);


--
-- TOC entry 3684 (class 1259 OID 20449)
-- Dependencies: 2860
-- Name: fki_conjuntoentidad_identidadmiembro; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_conjuntoentidad_identidadmiembro ON conjuntoentidad USING btree (identidadmiembro);


--
-- TOC entry 3632 (class 1259 OID 20450)
-- Dependencies: 2812
-- Name: fki_determinacion_iddeterminacionbase; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_determinacion_iddeterminacionbase ON determinacion USING btree (iddeterminacionbase);


--
-- TOC entry 3633 (class 1259 OID 20451)
-- Dependencies: 2812
-- Name: fki_determinacion_iddeterminacionoriginal; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_determinacion_iddeterminacionoriginal ON determinacion USING btree (iddeterminacionoriginal);


--
-- TOC entry 3634 (class 1259 OID 20452)
-- Dependencies: 2812
-- Name: fki_determinacion_idpadre; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_determinacion_idpadre ON determinacion USING btree (idpadre);


--
-- TOC entry 3635 (class 1259 OID 20453)
-- Dependencies: 2812
-- Name: fki_determinacion_idtramite; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_determinacion_idtramite ON determinacion USING btree (idtramite);


--
-- TOC entry 3687 (class 1259 OID 20454)
-- Dependencies: 2862
-- Name: fki_determinaciongrupoentidad_iddeterminacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_determinaciongrupoentidad_iddeterminacion ON determinaciongrupoentidad USING btree (iddeterminacion);


--
-- TOC entry 3688 (class 1259 OID 20455)
-- Dependencies: 2862
-- Name: fki_determinaciongrupoentidad_iddeterminaciongrupo; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_determinaciongrupoentidad_iddeterminaciongrupo ON determinaciongrupoentidad USING btree (iddeterminaciongrupo);


--
-- TOC entry 3691 (class 1259 OID 20456)
-- Dependencies: 2864
-- Name: fki_documento_iddocumentooriginal; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_documento_iddocumentooriginal ON documento USING btree (iddocumentooriginal);


--
-- TOC entry 3692 (class 1259 OID 20457)
-- Dependencies: 2864
-- Name: fki_documento_idtramite; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_documento_idtramite ON documento USING btree (idtramite);


--
-- TOC entry 3695 (class 1259 OID 20458)
-- Dependencies: 2866
-- Name: fki_documentocaso_idcaso; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_documentocaso_idcaso ON documentocaso USING btree (idcaso);


--
-- TOC entry 3696 (class 1259 OID 20459)
-- Dependencies: 2866
-- Name: fki_documentocaso_iddocumento; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_documentocaso_iddocumento ON documentocaso USING btree (iddocumento);


--
-- TOC entry 3699 (class 1259 OID 20460)
-- Dependencies: 2868
-- Name: fki_documentodeterminacion_iddeterminacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_documentodeterminacion_iddeterminacion ON documentodeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3700 (class 1259 OID 20461)
-- Dependencies: 2868
-- Name: fki_documentodeterminacion_iddocumento; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_documentodeterminacion_iddocumento ON documentodeterminacion USING btree (iddocumento);


--
-- TOC entry 3703 (class 1259 OID 20462)
-- Dependencies: 2870
-- Name: fki_documentoentidad_iddocumento; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_documentoentidad_iddocumento ON documentoentidad USING btree (iddocumento);


--
-- TOC entry 3704 (class 1259 OID 20463)
-- Dependencies: 2870
-- Name: fki_documentoentidad_identidad; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_documentoentidad_identidad ON documentoentidad USING btree (identidad);


--
-- TOC entry 3639 (class 1259 OID 20464)
-- Dependencies: 2814
-- Name: fki_entidad_identidadoriginal; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entidad_identidadoriginal ON entidad USING btree (identidadoriginal);


--
-- TOC entry 3640 (class 1259 OID 20465)
-- Dependencies: 2814
-- Name: fki_entidad_idpadre; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entidad_idpadre ON entidad USING btree (idpadre);


--
-- TOC entry 3641 (class 1259 OID 20466)
-- Dependencies: 2814
-- Name: fki_entidad_idtramite; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entidad_idtramite ON entidad USING btree (idtramite);


--
-- TOC entry 3644 (class 1259 OID 20467)
-- Dependencies: 2816
-- Name: fki_entidaddeterminacion_iddeterminacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entidaddeterminacion_iddeterminacion ON entidaddeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3645 (class 1259 OID 20468)
-- Dependencies: 2816
-- Name: fki_entidaddeterminacion_identidad; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entidaddeterminacion_identidad ON entidaddeterminacion USING btree (identidad);


--
-- TOC entry 3648 (class 1259 OID 20469)
-- Dependencies: 2818
-- Name: fki_entidaddeterminacionregimen_idcaso; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entidaddeterminacionregimen_idcaso ON entidaddeterminacionregimen USING btree (idcaso);


--
-- TOC entry 3649 (class 1259 OID 20470)
-- Dependencies: 2818
-- Name: fki_entidaddeterminacionregimen_idcasoaplicacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entidaddeterminacionregimen_idcasoaplicacion ON entidaddeterminacionregimen USING btree (idcasoaplicacion);


--
-- TOC entry 3650 (class 1259 OID 20471)
-- Dependencies: 2818
-- Name: fki_entidaddeterminacionregimen_iddeterminacionregimen; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entidaddeterminacionregimen_iddeterminacionregimen ON entidaddeterminacionregimen USING btree (iddeterminacionregimen);


--
-- TOC entry 3651 (class 1259 OID 20472)
-- Dependencies: 2818
-- Name: fki_entidaddeterminacionregimen_idopciondeterminacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entidaddeterminacionregimen_idopciondeterminacion ON entidaddeterminacionregimen USING btree (idopciondeterminacion);


--
-- TOC entry 3660 (class 1259 OID 20473)
-- Dependencies: 2826
-- Name: fki_opciondeterminacion_iddeterminacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_opciondeterminacion_iddeterminacion ON opciondeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3661 (class 1259 OID 20474)
-- Dependencies: 2826
-- Name: fki_opciondeterminacion_iddeterminacionvalorref; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_opciondeterminacion_iddeterminacionvalorref ON opciondeterminacion USING btree (iddeterminacionvalorref);


--
-- TOC entry 3709 (class 1259 OID 20475)
-- Dependencies: 2877
-- Name: fki_operacion_idtramiteordenante; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacion_idtramiteordenante ON operacion USING btree (idtramiteordenante);


--
-- TOC entry 3712 (class 1259 OID 20476)
-- Dependencies: 2879
-- Name: fki_operaciondeterminacion_iddeterminacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operaciondeterminacion_iddeterminacion ON operaciondeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3713 (class 1259 OID 20477)
-- Dependencies: 2879
-- Name: fki_operaciondeterminacion_iddeterminacionoperadora; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operaciondeterminacion_iddeterminacionoperadora ON operaciondeterminacion USING btree (iddeterminacionoperadora);


--
-- TOC entry 3714 (class 1259 OID 20478)
-- Dependencies: 2879
-- Name: fki_operaciondeterminacion_idoperacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operaciondeterminacion_idoperacion ON operaciondeterminacion USING btree (idoperacion);


--
-- TOC entry 3717 (class 1259 OID 20479)
-- Dependencies: 2881
-- Name: fki_operacionentidad_identidad; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacionentidad_identidad ON operacionentidad USING btree (identidad);


--
-- TOC entry 3718 (class 1259 OID 20480)
-- Dependencies: 2881
-- Name: fki_operacionentidad_identidadoperadora; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacionentidad_identidadoperadora ON operacionentidad USING btree (identidadoperadora);


--
-- TOC entry 3719 (class 1259 OID 20481)
-- Dependencies: 2881
-- Name: fki_operacionentidad_idoperacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacionentidad_idoperacion ON operacionentidad USING btree (idoperacion);


--
-- TOC entry 3674 (class 1259 OID 20482)
-- Dependencies: 2848
-- Name: fki_operacionplan_idplanoperado; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacionplan_idplanoperado ON operacionplan USING btree (idplanoperado);


--
-- TOC entry 3675 (class 1259 OID 20483)
-- Dependencies: 2848
-- Name: fki_operacionplan_idplanoperador; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacionplan_idplanoperador ON operacionplan USING btree (idplanoperador);


--
-- TOC entry 3722 (class 1259 OID 20484)
-- Dependencies: 2883
-- Name: fki_operacionrelacion_idoperacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacionrelacion_idoperacion ON operacionrelacion USING btree (idoperacion);


--
-- TOC entry 3723 (class 1259 OID 20485)
-- Dependencies: 2883
-- Name: fki_operacionrelacion_idrelacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_operacionrelacion_idrelacion ON operacionrelacion USING btree (idrelacion);


--
-- TOC entry 3664 (class 1259 OID 20486)
-- Dependencies: 2828
-- Name: fki_plan_idpadre; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_plan_idpadre ON plan USING btree (idpadre);


--
-- TOC entry 3665 (class 1259 OID 20487)
-- Dependencies: 2828
-- Name: fki_plan_idplanbase; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_plan_idplanbase ON plan USING btree (idplanbase);


--
-- TOC entry 3726 (class 1259 OID 20488)
-- Dependencies: 2885
-- Name: fki_planentidadordenacion_identidadordenacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_planentidadordenacion_identidadordenacion ON planentidadordenacion USING btree (identidadordenacion);


--
-- TOC entry 3727 (class 1259 OID 20489)
-- Dependencies: 2885
-- Name: fki_planentidadordenacion_idplan; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_planentidadordenacion_idplan ON planentidadordenacion USING btree (idplan);


--
-- TOC entry 3732 (class 1259 OID 20490)
-- Dependencies: 2889
-- Name: fki_propiedadrelacion_idrelacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_propiedadrelacion_idrelacion ON propiedadrelacion USING btree (idrelacion);


--
-- TOC entry 3735 (class 1259 OID 20491)
-- Dependencies: 2895
-- Name: fki_regimenespecifico_identidaddeterminacionregimen; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_regimenespecifico_identidaddeterminacionregimen ON regimenespecifico USING btree (identidaddeterminacionregimen);


--
-- TOC entry 3736 (class 1259 OID 20492)
-- Dependencies: 2895
-- Name: fki_regimenespecifico_idpadre; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_regimenespecifico_idpadre ON regimenespecifico USING btree (idpadre);


--
-- TOC entry 3739 (class 1259 OID 20493)
-- Dependencies: 2897
-- Name: fki_relacion_idtramitecreador; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_relacion_idtramitecreador ON relacion USING btree (idtramitecreador);


--
-- TOC entry 3668 (class 1259 OID 20494)
-- Dependencies: 2830
-- Name: fki_tramite_idplan; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_tramite_idplan ON tramite USING btree (idplan);


--
-- TOC entry 3742 (class 1259 OID 20495)
-- Dependencies: 2905
-- Name: fki_vectorrelacion_idrelacion; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_vectorrelacion_idrelacion ON vectorrelacion USING btree (idrelacion);


--
-- TOC entry 3745 (class 1259 OID 20496)
-- Dependencies: 2907
-- Name: fki_vinculocaso_idcaso; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_vinculocaso_idcaso ON vinculocaso USING btree (idcaso);


--
-- TOC entry 3746 (class 1259 OID 20497)
-- Dependencies: 2907
-- Name: fki_vinculocaso_idcasovinculado; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_vinculocaso_idcasovinculado ON vinculocaso USING btree (idcasovinculado);


--
-- TOC entry 3638 (class 1259 OID 20498)
-- Dependencies: 2812
-- Name: pki_determinacion_iden; Type: INDEX; Schema: planeamiento; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX pki_determinacion_iden ON determinacion USING btree (iden);


SET search_path = seguridad, pg_catalog;

--
-- TOC entry 3751 (class 1259 OID 20499)
-- Dependencies: 2912
-- Name: fki_diario_fk; Type: INDEX; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_diario_fk ON diario USING btree (usuario);


--
-- TOC entry 3752 (class 1259 OID 20500)
-- Dependencies: 2912
-- Name: fki_diario_operacion_fk; Type: INDEX; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_diario_operacion_fk ON diario USING btree (operaciones);


--
-- TOC entry 3753 (class 1259 OID 20501)
-- Dependencies: 2912
-- Name: fki_diario_subsistema_fk; Type: INDEX; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_diario_subsistema_fk ON diario USING btree (modulo);


SET search_path = diccionario, pg_catalog;

--
-- TOC entry 3837 (class 2606 OID 20520)
-- Dependencies: 2748 3535 2730
-- Name: f_cd_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY caracterdeterminacion
    ADD CONSTRAINT f_cd_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3833 (class 2606 OID 20525)
-- Dependencies: 3535 2724 2748
-- Name: fk_am_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY ambito
    ADD CONSTRAINT fk_am_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3834 (class 2606 OID 20530)
-- Dependencies: 2762 2724 3558
-- Name: fk_am_idtipoambito; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY ambito
    ADD CONSTRAINT fk_am_idtipoambito FOREIGN KEY (idtipoambito) REFERENCES tipoambito(iden);


--
-- TOC entry 3835 (class 2606 OID 20535)
-- Dependencies: 2724 2726 3496
-- Name: fk_ashp_idambito; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY ambitoshp
    ADD CONSTRAINT fk_ashp_idambito FOREIGN KEY (idambito) REFERENCES ambito(iden);


--
-- TOC entry 3836 (class 2606 OID 20540)
-- Dependencies: 3535 2748 2728
-- Name: fk_b_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY boletin
    ADD CONSTRAINT fk_b_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3838 (class 2606 OID 20545)
-- Dependencies: 2748 3535 2732
-- Name: fk_cp_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY centroproduccion
    ADD CONSTRAINT fk_cp_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3839 (class 2606 OID 20550)
-- Dependencies: 3515 2736 2734
-- Name: fk_dp_iddefrelacion; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY defpropiedad
    ADD CONSTRAINT fk_dp_iddefrelacion FOREIGN KEY (iddefrelacion) REFERENCES defrelacion(iden);


--
-- TOC entry 3840 (class 2606 OID 20555)
-- Dependencies: 3535 2734 2748
-- Name: fk_dp_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY defpropiedad
    ADD CONSTRAINT fk_dp_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3841 (class 2606 OID 20560)
-- Dependencies: 2734 3561 2764
-- Name: fk_dp_idtipodefpropiedad; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY defpropiedad
    ADD CONSTRAINT fk_dp_idtipodefpropiedad FOREIGN KEY (idtipodefpropiedad) REFERENCES tipodefpropiedad(iden);


--
-- TOC entry 3842 (class 2606 OID 20565)
-- Dependencies: 2736 2748 3535
-- Name: fk_dr_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY defrelacion
    ADD CONSTRAINT fk_dr_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3843 (class 2606 OID 20570)
-- Dependencies: 2736 2738 3515
-- Name: fk_dv_iddefrelacion; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY defvector
    ADD CONSTRAINT fk_dv_iddefrelacion FOREIGN KEY (iddefrelacion) REFERENCES defrelacion(iden);


--
-- TOC entry 3844 (class 2606 OID 20575)
-- Dependencies: 2748 2738 3535
-- Name: fk_dv_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY defvector
    ADD CONSTRAINT fk_dv_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3845 (class 2606 OID 20580)
-- Dependencies: 3555 2760 2738
-- Name: fk_dv_idtabla; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY defvector
    ADD CONSTRAINT fk_dv_idtabla FOREIGN KEY (idtabla) REFERENCES tabla(iden);


--
-- TOC entry 3846 (class 2606 OID 20585)
-- Dependencies: 2740 3535 2748
-- Name: fk_gd_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY grupodocumento
    ADD CONSTRAINT fk_gd_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3847 (class 2606 OID 20590)
-- Dependencies: 3535 2748 2742
-- Name: fk_ip_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY instrumentoplan
    ADD CONSTRAINT fk_ip_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3848 (class 2606 OID 20595)
-- Dependencies: 3538 2750 2742
-- Name: fk_ip_idnaturaleza; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY instrumentoplan
    ADD CONSTRAINT fk_ip_idnaturaleza FOREIGN KEY (idnaturaleza) REFERENCES naturaleza(iden);


--
-- TOC entry 3849 (class 2606 OID 20600)
-- Dependencies: 2742 2744 3527
-- Name: fk_itop_idinstrumentoplan; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY instrumentotipooperacionplan
    ADD CONSTRAINT fk_itop_idinstrumentoplan FOREIGN KEY (idinstrumentoplan) REFERENCES instrumentoplan(iden);


--
-- TOC entry 3850 (class 2606 OID 20605)
-- Dependencies: 2774 2744 3577
-- Name: fk_itop_idtipooperacionplan; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY instrumentotipooperacionplan
    ADD CONSTRAINT fk_itop_idtipooperacionplan FOREIGN KEY (idtipooperacionplan) REFERENCES tipooperacionplan(iden);


--
-- TOC entry 3851 (class 2606 OID 20610)
-- Dependencies: 2746 2748 3535
-- Name: fk_leg_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY legislacion
    ADD CONSTRAINT fk_leg_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3852 (class 2606 OID 20615)
-- Dependencies: 2750 3535 2748
-- Name: fk_n_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY naturaleza
    ADD CONSTRAINT fk_n_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3856 (class 2606 OID 20620)
-- Dependencies: 2754 3496 2724
-- Name: fk_oa_idambitohijo; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY organigramaambito
    ADD CONSTRAINT fk_oa_idambitohijo FOREIGN KEY (idambitohijo) REFERENCES ambito(iden);


--
-- TOC entry 3857 (class 2606 OID 20625)
-- Dependencies: 3496 2724 2754
-- Name: fk_oa_idambitopadre; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY organigramaambito
    ADD CONSTRAINT fk_oa_idambitopadre FOREIGN KEY (idambitopadre) REFERENCES ambito(iden);


--
-- TOC entry 3853 (class 2606 OID 20630)
-- Dependencies: 3504 2730 2752
-- Name: fk_oc_idcaracteroperado; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY operacioncaracter
    ADD CONSTRAINT fk_oc_idcaracteroperado FOREIGN KEY (idcaracteroperado) REFERENCES caracterdeterminacion(iden);


--
-- TOC entry 3854 (class 2606 OID 20635)
-- Dependencies: 2752 3504 2730
-- Name: fk_oc_idcaracteroperador; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY operacioncaracter
    ADD CONSTRAINT fk_oc_idcaracteroperador FOREIGN KEY (idcaracteroperador) REFERENCES caracterdeterminacion(iden);


--
-- TOC entry 3855 (class 2606 OID 20640)
-- Dependencies: 2752 3570 2770
-- Name: fk_oc_idtipooperaciondet; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY operacioncaracter
    ADD CONSTRAINT fk_oc_idtipooperaciondet FOREIGN KEY (idtipooperaciondet) REFERENCES tipooperaciondeterminacion(iden);


--
-- TOC entry 3858 (class 2606 OID 20645)
-- Dependencies: 3535 2748 2756
-- Name: fk_org_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY organo
    ADD CONSTRAINT fk_org_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3859 (class 2606 OID 20650)
-- Dependencies: 3535 2748 2758
-- Name: fk_sen_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY sentido
    ADD CONSTRAINT fk_sen_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3862 (class 2606 OID 20655)
-- Dependencies: 2764 2748 3535
-- Name: fk_tdp_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipodefpropiedad
    ADD CONSTRAINT fk_tdp_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3865 (class 2606 OID 20660)
-- Dependencies: 2770 3535 2748
-- Name: fk_tod_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipooperaciondeterminacion
    ADD CONSTRAINT fk_tod_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3866 (class 2606 OID 20665)
-- Dependencies: 2748 3535 2772
-- Name: fk_toe_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipooperacionentidad
    ADD CONSTRAINT fk_toe_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3867 (class 2606 OID 20670)
-- Dependencies: 3567 2772 2768
-- Name: fk_toe_idtipoentidad; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipooperacionentidad
    ADD CONSTRAINT fk_toe_idtipoentidad FOREIGN KEY (idtipoentidad) REFERENCES tipoentidad(iden);


--
-- TOC entry 3869 (class 2606 OID 20675)
-- Dependencies: 2748 2776 3535
-- Name: fk_topr_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipooperacionrelacion
    ADD CONSTRAINT fk_topr_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3860 (class 2606 OID 20680)
-- Dependencies: 3535 2748 2762
-- Name: fk_tpa_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipoambito
    ADD CONSTRAINT fk_tpa_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3861 (class 2606 OID 20685)
-- Dependencies: 3558 2762 2762
-- Name: fk_tpa_idpadre; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipoambito
    ADD CONSTRAINT fk_tpa_idpadre FOREIGN KEY (idpadre) REFERENCES tipoambito(iden);


--
-- TOC entry 3863 (class 2606 OID 20690)
-- Dependencies: 3535 2748 2766
-- Name: fk_tpdoc_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipodocumento
    ADD CONSTRAINT fk_tpdoc_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3864 (class 2606 OID 20695)
-- Dependencies: 2768 2748 3535
-- Name: fk_tpent_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipoentidad
    ADD CONSTRAINT fk_tpent_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3868 (class 2606 OID 20700)
-- Dependencies: 2774 2748 3535
-- Name: fk_tpop_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipooperacionplan
    ADD CONSTRAINT fk_tpop_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3871 (class 2606 OID 20705)
-- Dependencies: 2780 2748 3535
-- Name: fk_trd_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY traduccion
    ADD CONSTRAINT fk_trd_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


--
-- TOC entry 3870 (class 2606 OID 20710)
-- Dependencies: 2778 2748 3535
-- Name: fk_ttra_idliteral; Type: FK CONSTRAINT; Schema: diccionario; Owner: postgres
--

ALTER TABLE ONLY tipotramite
    ADD CONSTRAINT fk_ttra_idliteral FOREIGN KEY (idliteral) REFERENCES literal(iden);


SET search_path = explotacion, pg_catalog;

--
-- TOC entry 3874 (class 2606 OID 20715)
-- Dependencies: 2790 2784 3592
-- Name: fk_cg_idcapa; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY capagrupo
    ADD CONSTRAINT fk_cg_idcapa FOREIGN KEY (idcapa) REFERENCES capa(iden);


--
-- TOC entry 3872 (class 2606 OID 20720)
-- Dependencies: 2786 2784 3592
-- Name: fk_crc_idcapa; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY capaentidadconjunto
    ADD CONSTRAINT fk_crc_idcapa FOREIGN KEY (idcapa) REFERENCES capa(iden);


--
-- TOC entry 3873 (class 2606 OID 20725)
-- Dependencies: 2788 2784 3592
-- Name: fk_crl_idcapa; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY capaentidadlista
    ADD CONSTRAINT fk_crl_idcapa FOREIGN KEY (idcapa) REFERENCES capa(iden);


--
-- TOC entry 3875 (class 2606 OID 20730)
-- Dependencies: 2792 2784 3592
-- Name: fk_lal_idcapa; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY leyenda_al
    ADD CONSTRAINT fk_lal_idcapa FOREIGN KEY (idcapa) REFERENCES capa(iden);


--
-- TOC entry 3876 (class 2606 OID 20735)
-- Dependencies: 2794 2784 3592
-- Name: fk_lpd_idcapa; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY leyenda_pd
    ADD CONSTRAINT fk_lpd_idcapa FOREIGN KEY (idcapa) REFERENCES capa(iden);


--
-- TOC entry 3877 (class 2606 OID 20740)
-- Dependencies: 2794 2800 3616
-- Name: fk_lpd_idpropd; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY leyenda_pd
    ADD CONSTRAINT fk_lpd_idpropd FOREIGN KEY (idpropd) REFERENCES procedimiento_pd(iden);


--
-- TOC entry 3878 (class 2606 OID 20745)
-- Dependencies: 2796 2784 3592
-- Name: fk_lvt_idcapa; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY leyenda_vt
    ADD CONSTRAINT fk_lvt_idcapa FOREIGN KEY (idcapa) REFERENCES capa(iden);


--
-- TOC entry 3879 (class 2606 OID 20750)
-- Dependencies: 2798 2790 3601
-- Name: fk_progr_idcapagrupo; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY procedimiento_gr
    ADD CONSTRAINT fk_progr_idcapagrupo FOREIGN KEY (idcapagrupo) REFERENCES capagrupo(iden);


--
-- TOC entry 3880 (class 2606 OID 20755)
-- Dependencies: 2784 3592 2802
-- Name: fk_prorg_idcapa; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY procedimiento_rg
    ADD CONSTRAINT fk_prorg_idcapa FOREIGN KEY (idcapa) REFERENCES capa(iden);


--
-- TOC entry 3881 (class 2606 OID 20760)
-- Dependencies: 2804 2790 3601
-- Name: fk_provt_idcapagrupo; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY procedimiento_vt
    ADD CONSTRAINT fk_provt_idcapagrupo FOREIGN KEY (idcapagrupo) REFERENCES capagrupo(iden);


--
-- TOC entry 3882 (class 2606 OID 20765)
-- Dependencies: 2808 2784 3592
-- Name: fk_rval_idcapa; Type: FK CONSTRAINT; Schema: explotacion; Owner: postgres
--

ALTER TABLE ONLY rangovalor
    ADD CONSTRAINT fk_rval_idcapa FOREIGN KEY (idcapa) REFERENCES capa(iden);


SET search_path = ficha, pg_catalog;

--
-- TOC entry 3950 (class 2606 OID 32942)
-- Dependencies: 3000 3785 2998
-- Name: conjuntodeterminaciongrupo_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY conjuntodeterminaciongrupo
    ADD CONSTRAINT conjuntodeterminaciongrupo_fk FOREIGN KEY (idconjunto) REFERENCES conjuntogrupo(iden) ON DELETE CASCADE;


--
-- TOC entry 3951 (class 2606 OID 32947)
-- Dependencies: 2998 2812 3636
-- Name: conjuntodeterminaciongrupo_fk1; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY conjuntodeterminaciongrupo
    ADD CONSTRAINT conjuntodeterminaciongrupo_fk1 FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;


--
-- TOC entry 3952 (class 2606 OID 32952)
-- Dependencies: 2812 3002 3636
-- Name: determinacionclasifacto_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY determinacionclasifacto
    ADD CONSTRAINT determinacionclasifacto_fk FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;


--
-- TOC entry 3953 (class 2606 OID 32957)
-- Dependencies: 3795 3002 3005
-- Name: determinacionclasifacto_fk1; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY determinacionclasifacto
    ADD CONSTRAINT determinacionclasifacto_fk1 FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;


--
-- TOC entry 3954 (class 2606 OID 32962)
-- Dependencies: 2812 3003 3636
-- Name: determinacionclasifuso_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY determinacionclasifuso
    ADD CONSTRAINT determinacionclasifuso_fk FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;


--
-- TOC entry 3955 (class 2606 OID 32967)
-- Dependencies: 3795 3005 3003
-- Name: determinacionclasifuso_fk1; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY determinacionclasifuso
    ADD CONSTRAINT determinacionclasifuso_fk1 FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;


--
-- TOC entry 3956 (class 2606 OID 32972)
-- Dependencies: 3669 3005 2830
-- Name: ficha_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY ficha
    ADD CONSTRAINT ficha_fk FOREIGN KEY (idtramite) REFERENCES planeamiento.tramite(iden) ON DELETE RESTRICT;


--
-- TOC entry 3959 (class 2606 OID 32977)
-- Dependencies: 3009 3807 3008
-- Name: fichaconjuntodeterminacion_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY fichagrupodeterminacion
    ADD CONSTRAINT fichaconjuntodeterminacion_fk FOREIGN KEY (idgrupo) REFERENCES grupodeterminacion(iden) ON DELETE RESTRICT;


--
-- TOC entry 3960 (class 2606 OID 32982)
-- Dependencies: 3795 3005 3008
-- Name: fichaconjuntodeterminacion_fk1; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY fichagrupodeterminacion
    ADD CONSTRAINT fichaconjuntodeterminacion_fk1 FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE RESTRICT;


--
-- TOC entry 3957 (class 2606 OID 32987)
-- Dependencies: 3005 3795 3007
-- Name: fichaconjuntogrupo_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY fichaconjuntogrupo
    ADD CONSTRAINT fichaconjuntogrupo_fk FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;


--
-- TOC entry 3958 (class 2606 OID 32992)
-- Dependencies: 3007 3000 3785
-- Name: fichaconjuntogrupo_fk1; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY fichaconjuntogrupo
    ADD CONSTRAINT fichaconjuntogrupo_fk1 FOREIGN KEY (idconjunto) REFERENCES conjuntogrupo(iden) ON DELETE RESTRICT;


--
-- TOC entry 3961 (class 2606 OID 32997)
-- Dependencies: 3009 3785 3000
-- Name: grupodeterminacion_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY grupodeterminacion
    ADD CONSTRAINT grupodeterminacion_fk FOREIGN KEY (idconjuntogrupo) REFERENCES conjuntogrupo(iden) ON DELETE RESTRICT;


--
-- TOC entry 3962 (class 2606 OID 33002)
-- Dependencies: 3011 3807 3009
-- Name: grupodeterminaciondeterminacion_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY grupodeterminaciondeterminacion
    ADD CONSTRAINT grupodeterminaciondeterminacion_fk FOREIGN KEY (idgrupo) REFERENCES grupodeterminacion(iden) ON DELETE CASCADE;


--
-- TOC entry 3963 (class 2606 OID 33007)
-- Dependencies: 3011 3636 2812
-- Name: grupodeterminaciondeterminacion_fk1; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY grupodeterminaciondeterminacion
    ADD CONSTRAINT grupodeterminaciondeterminacion_fk1 FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;


--
-- TOC entry 3964 (class 2606 OID 33012)
-- Dependencies: 3636 2812 3012
-- Name: regimenesacto_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY regimenesacto
    ADD CONSTRAINT regimenesacto_fk FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;


--
-- TOC entry 3965 (class 2606 OID 33017)
-- Dependencies: 3005 3012 3795
-- Name: regimenesacto_fk1; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY regimenesacto
    ADD CONSTRAINT regimenesacto_fk1 FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;


--
-- TOC entry 3966 (class 2606 OID 33022)
-- Dependencies: 3636 2812 3013
-- Name: regimenesuso_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY regimenesuso
    ADD CONSTRAINT regimenesuso_fk FOREIGN KEY (iddeterminacion) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;


--
-- TOC entry 3967 (class 2606 OID 33027)
-- Dependencies: 3795 3005 3013
-- Name: regimenesuso_fk1; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY regimenesuso
    ADD CONSTRAINT regimenesuso_fk1 FOREIGN KEY (idficha) REFERENCES ficha(iden) ON DELETE CASCADE;


--
-- TOC entry 3968 (class 2606 OID 33032)
-- Dependencies: 3002 3789 3015
-- Name: valoresclasifacto_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY valoresclasifacto
    ADD CONSTRAINT valoresclasifacto_fk FOREIGN KEY (iddeterminacionclasifacto) REFERENCES determinacionclasifacto(iden) ON DELETE CASCADE;


--
-- TOC entry 3969 (class 2606 OID 33037)
-- Dependencies: 3636 3015 2812
-- Name: valoresclasifacto_fk1; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY valoresclasifacto
    ADD CONSTRAINT valoresclasifacto_fk1 FOREIGN KEY (iddeterminacionvalorregimen) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;


--
-- TOC entry 3970 (class 2606 OID 33042)
-- Dependencies: 3020 3793 3003
-- Name: valoresclasifuso_fk; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY valoresclasifuso
    ADD CONSTRAINT valoresclasifuso_fk FOREIGN KEY (iddeterminacionclasifuso) REFERENCES determinacionclasifuso(iden) ON DELETE CASCADE;


--
-- TOC entry 3971 (class 2606 OID 33047)
-- Dependencies: 3020 3636 2812
-- Name: valoresclasifuso_fk1; Type: FK CONSTRAINT; Schema: ficha; Owner: postgres
--

ALTER TABLE ONLY valoresclasifuso
    ADD CONSTRAINT valoresclasifuso_fk1 FOREIGN KEY (iddeterminacionvalorregimen) REFERENCES planeamiento.determinacion(iden) ON DELETE RESTRICT;


SET search_path = planeamiento, pg_catalog;

--
-- TOC entry 3906 (class 2606 OID 20770)
-- Dependencies: 2846 2814 3642
-- Name: fk_aaa_identidad; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY ambitoaplicacionambito
    ADD CONSTRAINT fk_aaa_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3910 (class 2606 OID 20775)
-- Dependencies: 3669 2853 2830
-- Name: fk_bt_idtramite; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY boletintramite
    ADD CONSTRAINT fk_bt_idtramite FOREIGN KEY (idtramite) REFERENCES tramite(iden);


--
-- TOC entry 3883 (class 2606 OID 20780)
-- Dependencies: 2810 2816 3646
-- Name: fk_ced_identidaddeterminacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY casoentidaddeterminacion
    ADD CONSTRAINT fk_ced_identidaddeterminacion FOREIGN KEY (identidaddeterminacion) REFERENCES entidaddeterminacion(iden);


--
-- TOC entry 3911 (class 2606 OID 20785)
-- Dependencies: 3642 2860 2814
-- Name: fk_cent_identidadconjunto; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY conjuntoentidad
    ADD CONSTRAINT fk_cent_identidadconjunto FOREIGN KEY (identidadconjunto) REFERENCES entidad(iden);


--
-- TOC entry 3912 (class 2606 OID 20790)
-- Dependencies: 2814 2860 3642
-- Name: fk_cent_identidadmiembro; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY conjuntoentidad
    ADD CONSTRAINT fk_cent_identidadmiembro FOREIGN KEY (identidadmiembro) REFERENCES entidad(iden);


--
-- TOC entry 3884 (class 2606 OID 20795)
-- Dependencies: 2812 2812 3636
-- Name: fk_d_iddeterminacionbase; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_iddeterminacionbase FOREIGN KEY (iddeterminacionbase) REFERENCES determinacion(iden);


--
-- TOC entry 3885 (class 2606 OID 20800)
-- Dependencies: 2812 2812 3636
-- Name: fk_d_iddeterminacionoriginal; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_iddeterminacionoriginal FOREIGN KEY (iddeterminacionoriginal) REFERENCES determinacion(iden);


--
-- TOC entry 3886 (class 2606 OID 20805)
-- Dependencies: 2812 3636 2812
-- Name: fk_d_idpadre; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_idpadre FOREIGN KEY (idpadre) REFERENCES determinacion(iden);


--
-- TOC entry 3887 (class 2606 OID 20810)
-- Dependencies: 2812 2830 3669
-- Name: fk_d_idtramite; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_idtramite FOREIGN KEY (idtramite) REFERENCES tramite(iden);


--
-- TOC entry 3913 (class 2606 OID 20815)
-- Dependencies: 3636 2862 2812
-- Name: fk_dge_iddeterminacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY determinaciongrupoentidad
    ADD CONSTRAINT fk_dge_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3914 (class 2606 OID 20820)
-- Dependencies: 2812 2862 3636
-- Name: fk_dge_iddeterminaciongrupo; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY determinaciongrupoentidad
    ADD CONSTRAINT fk_dge_iddeterminaciongrupo FOREIGN KEY (iddeterminaciongrupo) REFERENCES determinacion(iden);


--
-- TOC entry 3915 (class 2606 OID 20825)
-- Dependencies: 2864 2864 3693
-- Name: fk_doc_iddocumentooriginal; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT fk_doc_iddocumentooriginal FOREIGN KEY (iddocumentooriginal) REFERENCES documento(iden);


--
-- TOC entry 3916 (class 2606 OID 20830)
-- Dependencies: 2830 2864 3669
-- Name: fk_doc_idtramite; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT fk_doc_idtramite FOREIGN KEY (idtramite) REFERENCES tramite(iden);


--
-- TOC entry 3917 (class 2606 OID 20835)
-- Dependencies: 2810 2866 3630
-- Name: fk_docc_idcaso; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY documentocaso
    ADD CONSTRAINT fk_docc_idcaso FOREIGN KEY (idcaso) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3918 (class 2606 OID 20840)
-- Dependencies: 2866 3693 2864
-- Name: fk_docc_iddocumento; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY documentocaso
    ADD CONSTRAINT fk_docc_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3919 (class 2606 OID 20845)
-- Dependencies: 2868 3636 2812
-- Name: fk_docd_iddeterminacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY documentodeterminacion
    ADD CONSTRAINT fk_docd_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3920 (class 2606 OID 20850)
-- Dependencies: 3693 2864 2868
-- Name: fk_docd_iddocumento; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY documentodeterminacion
    ADD CONSTRAINT fk_docd_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3921 (class 2606 OID 20855)
-- Dependencies: 2870 3693 2864
-- Name: fk_doce_iddocumento; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY documentoentidad
    ADD CONSTRAINT fk_doce_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3922 (class 2606 OID 20860)
-- Dependencies: 3642 2814 2870
-- Name: fk_doce_identidad; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY documentoentidad
    ADD CONSTRAINT fk_doce_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3923 (class 2606 OID 20865)
-- Dependencies: 2864 2872 3693
-- Name: fk_docshp_iddocumento; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY documentoshp
    ADD CONSTRAINT fk_docshp_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3888 (class 2606 OID 20870)
-- Dependencies: 2814 2814 3642
-- Name: fk_e_identidadbase; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_identidadbase FOREIGN KEY (identidadbase) REFERENCES entidad(iden);


--
-- TOC entry 3889 (class 2606 OID 20875)
-- Dependencies: 2814 2814 3642
-- Name: fk_e_identidadoriginal; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_identidadoriginal FOREIGN KEY (identidadoriginal) REFERENCES entidad(iden);


--
-- TOC entry 3890 (class 2606 OID 20880)
-- Dependencies: 2814 2814 3642
-- Name: fk_e_idpadre; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_idpadre FOREIGN KEY (idpadre) REFERENCES entidad(iden);


--
-- TOC entry 3891 (class 2606 OID 20885)
-- Dependencies: 3669 2830 2814
-- Name: fk_e_idtramite; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_idtramite FOREIGN KEY (idtramite) REFERENCES tramite(iden);


--
-- TOC entry 3892 (class 2606 OID 20890)
-- Dependencies: 2812 3636 2816
-- Name: fk_ed_iddeterminacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacion
    ADD CONSTRAINT fk_ed_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3893 (class 2606 OID 20895)
-- Dependencies: 3642 2814 2816
-- Name: fk_ed_identidad; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacion
    ADD CONSTRAINT fk_ed_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3894 (class 2606 OID 20900)
-- Dependencies: 2818 2810 3630
-- Name: fk_edr_idcaso; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_idcaso FOREIGN KEY (idcaso) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3895 (class 2606 OID 20905)
-- Dependencies: 2810 2818 3630
-- Name: fk_edr_idcasoaplicacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_idcasoaplicacion FOREIGN KEY (idcasoaplicacion) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3896 (class 2606 OID 20910)
-- Dependencies: 2812 3636 2818
-- Name: fk_edr_iddeterminacionregimen; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_iddeterminacionregimen FOREIGN KEY (iddeterminacionregimen) REFERENCES determinacion(iden);


--
-- TOC entry 3897 (class 2606 OID 20915)
-- Dependencies: 2818 2826 3662
-- Name: fk_edr_idopciondeterminacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_idopciondeterminacion FOREIGN KEY (idopciondeterminacion) REFERENCES opciondeterminacion(iden);


--
-- TOC entry 3898 (class 2606 OID 20920)
-- Dependencies: 3642 2820 2814
-- Name: fk_elin_identidad; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidadlin
    ADD CONSTRAINT fk_elin_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3899 (class 2606 OID 20925)
-- Dependencies: 2814 2822 3642
-- Name: fk_epnt_identidad; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidadpnt
    ADD CONSTRAINT fk_epnt_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3900 (class 2606 OID 20930)
-- Dependencies: 2824 2814 3642
-- Name: fk_epol_identidad; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY entidadpol
    ADD CONSTRAINT fk_epol_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3925 (class 2606 OID 20935)
-- Dependencies: 2812 2879 3636
-- Name: fk_od_iddeterminacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT fk_od_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3926 (class 2606 OID 20940)
-- Dependencies: 2812 2879 3636
-- Name: fk_od_iddeterminacionoperadora; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT fk_od_iddeterminacionoperadora FOREIGN KEY (iddeterminacionoperadora) REFERENCES determinacion(iden);


--
-- TOC entry 3927 (class 2606 OID 20945)
-- Dependencies: 2879 2877 3710
-- Name: fk_od_idoperacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT fk_od_idoperacion FOREIGN KEY (idoperacion) REFERENCES operacion(iden);


--
-- TOC entry 3901 (class 2606 OID 20950)
-- Dependencies: 2826 2812 3636
-- Name: fk_odet_iddeterminacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY opciondeterminacion
    ADD CONSTRAINT fk_odet_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3902 (class 2606 OID 20955)
-- Dependencies: 2812 2826 3636
-- Name: fk_odet_iddetvalorref; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY opciondeterminacion
    ADD CONSTRAINT fk_odet_iddetvalorref FOREIGN KEY (iddeterminacionvalorref) REFERENCES determinacion(iden);


--
-- TOC entry 3928 (class 2606 OID 20960)
-- Dependencies: 2881 2814 3642
-- Name: fk_oe_identidad; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT fk_oe_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3929 (class 2606 OID 20965)
-- Dependencies: 2814 2881 3642
-- Name: fk_oe_identidadoperadora; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT fk_oe_identidadoperadora FOREIGN KEY (identidadoperadora) REFERENCES entidad(iden);


--
-- TOC entry 3930 (class 2606 OID 20970)
-- Dependencies: 2877 3710 2881
-- Name: fk_oe_idoperacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT fk_oe_idoperacion FOREIGN KEY (idoperacion) REFERENCES operacion(iden);


--
-- TOC entry 3924 (class 2606 OID 20975)
-- Dependencies: 2830 3669 2877
-- Name: fk_op_idtramiteordenante; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operacion
    ADD CONSTRAINT fk_op_idtramiteordenante FOREIGN KEY (idtramiteordenante) REFERENCES tramite(iden);


--
-- TOC entry 3907 (class 2606 OID 20980)
-- Dependencies: 2828 2848 3666
-- Name: fk_opp_idplanoperado; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT fk_opp_idplanoperado FOREIGN KEY (idplanoperado) REFERENCES plan(iden);


--
-- TOC entry 3908 (class 2606 OID 20985)
-- Dependencies: 2848 3666 2828
-- Name: fk_opp_idplanoperador; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT fk_opp_idplanoperador FOREIGN KEY (idplanoperador) REFERENCES plan(iden);


--
-- TOC entry 3931 (class 2606 OID 20990)
-- Dependencies: 2883 3710 2877
-- Name: fk_or_idoperacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT fk_or_idoperacion FOREIGN KEY (idoperacion) REFERENCES operacion(iden);


--
-- TOC entry 3932 (class 2606 OID 20995)
-- Dependencies: 2897 3740 2883
-- Name: fk_or_idrelacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT fk_or_idrelacion FOREIGN KEY (idrelacion) REFERENCES relacion(iden);


--
-- TOC entry 3903 (class 2606 OID 21000)
-- Dependencies: 2828 3666 2828
-- Name: fk_p_idpadre; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT fk_p_idpadre FOREIGN KEY (idpadre) REFERENCES plan(iden);


--
-- TOC entry 3904 (class 2606 OID 21005)
-- Dependencies: 2828 2828 3666
-- Name: fk_p_idplanbase; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT fk_p_idplanbase FOREIGN KEY (idplanbase) REFERENCES plan(iden);


--
-- TOC entry 3933 (class 2606 OID 21010)
-- Dependencies: 2814 2885 3642
-- Name: fk_peord_identidad; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY planentidadordenacion
    ADD CONSTRAINT fk_peord_identidad FOREIGN KEY (identidadordenacion) REFERENCES entidad(iden);


--
-- TOC entry 3934 (class 2606 OID 21015)
-- Dependencies: 2885 3666 2828
-- Name: fk_peord_idplan; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY planentidadordenacion
    ADD CONSTRAINT fk_peord_idplan FOREIGN KEY (idplan) REFERENCES plan(iden);


--
-- TOC entry 3935 (class 2606 OID 21020)
-- Dependencies: 2887 2828 3666
-- Name: fk_pleg_idplan; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY planlegislacion
    ADD CONSTRAINT fk_pleg_idplan FOREIGN KEY (idplan) REFERENCES plan(iden);


--
-- TOC entry 3936 (class 2606 OID 21025)
-- Dependencies: 2889 3740 2897
-- Name: fk_pr_idrelacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY propiedadrelacion
    ADD CONSTRAINT fk_pr_idrelacion FOREIGN KEY (idrelacion) REFERENCES relacion(iden);


--
-- TOC entry 3909 (class 2606 OID 21030)
-- Dependencies: 2850 2828 3666
-- Name: fk_pshp_idplan; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY planshp
    ADD CONSTRAINT fk_pshp_idplan FOREIGN KEY (idplan) REFERENCES plan(iden);


--
-- TOC entry 3937 (class 2606 OID 21035)
-- Dependencies: 2818 2895 3652
-- Name: fk_re_idedr; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY regimenespecifico
    ADD CONSTRAINT fk_re_idedr FOREIGN KEY (identidaddeterminacionregimen) REFERENCES entidaddeterminacionregimen(iden);


--
-- TOC entry 3938 (class 2606 OID 21040)
-- Dependencies: 2895 2895 3737
-- Name: fk_re_idpadre; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY regimenespecifico
    ADD CONSTRAINT fk_re_idpadre FOREIGN KEY (idpadre) REFERENCES regimenespecifico(iden);


--
-- TOC entry 3939 (class 2606 OID 21045)
-- Dependencies: 2830 3669 2897
-- Name: fk_rl_idtramitecreador; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY relacion
    ADD CONSTRAINT fk_rl_idtramitecreador FOREIGN KEY (idtramitecreador) REFERENCES tramite(iden);


--
-- TOC entry 3905 (class 2606 OID 21050)
-- Dependencies: 3666 2830 2828
-- Name: fk_tra_idplan; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY tramite
    ADD CONSTRAINT fk_tra_idplan FOREIGN KEY (idplan) REFERENCES plan(iden);


--
-- TOC entry 3941 (class 2606 OID 21055)
-- Dependencies: 2810 2907 3630
-- Name: fk_vc_idcaso; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY vinculocaso
    ADD CONSTRAINT fk_vc_idcaso FOREIGN KEY (idcaso) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3942 (class 2606 OID 21060)
-- Dependencies: 3630 2810 2907
-- Name: fk_vc_idcasovinculado; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY vinculocaso
    ADD CONSTRAINT fk_vc_idcasovinculado FOREIGN KEY (idcasovinculado) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3940 (class 2606 OID 21065)
-- Dependencies: 2897 2905 3740
-- Name: fk_vr_idrelacion; Type: FK CONSTRAINT; Schema: planeamiento; Owner: postgres
--

ALTER TABLE ONLY vectorrelacion
    ADD CONSTRAINT fk_vr_idrelacion FOREIGN KEY (idrelacion) REFERENCES relacion(iden);


SET search_path = seguridad, pg_catalog;

--
-- TOC entry 3943 (class 2606 OID 21070)
-- Dependencies: 2912 2915 3756
-- Name: diario_operaciones; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY diario
    ADD CONSTRAINT diario_operaciones FOREIGN KEY (operaciones) REFERENCES operaciones(iden);


--
-- TOC entry 3944 (class 2606 OID 21075)
-- Dependencies: 2919 2912 3764
-- Name: diario_subsistema_fk; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY diario
    ADD CONSTRAINT diario_subsistema_fk FOREIGN KEY (modulo) REFERENCES subsistema(iden);


--
-- TOC entry 3945 (class 2606 OID 21080)
-- Dependencies: 2918 3762 2922
-- Name: usuarioRol_fk1; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY usuariorol
    ADD CONSTRAINT "usuarioRol_fk1" FOREIGN KEY (idrol) REFERENCES rol(iden);


--
-- TOC entry 3946 (class 2606 OID 21085)
-- Dependencies: 2920 2922 3768
-- Name: usuariorol_fk2; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY usuariorol
    ADD CONSTRAINT usuariorol_fk2 FOREIGN KEY (idusuario) REFERENCES usuario(iden);


SET search_path = validacion, pg_catalog;

--
-- TOC entry 3947 (class 2606 OID 24007)
-- Dependencies: 3774 2933 2932
-- Name: fk_proceso; Type: FK CONSTRAINT; Schema: validacion; Owner: postgres
--

ALTER TABLE ONLY resultado
    ADD CONSTRAINT fk_proceso FOREIGN KEY (idproceso) REFERENCES proceso(iden);


--
-- TOC entry 3949 (class 2606 OID 24026)
-- Dependencies: 2934 3776 2933 2933 2934
-- Name: fk_resultado; Type: FK CONSTRAINT; Schema: validacion; Owner: postgres
--

ALTER TABLE ONLY error
    ADD CONSTRAINT fk_resultado FOREIGN KEY (idproceso, idvalidacion) REFERENCES resultado(idproceso, idvalidacion);


--
-- TOC entry 3948 (class 2606 OID 24012)
-- Dependencies: 3772 2933 2931
-- Name: fk_validacion; Type: FK CONSTRAINT; Schema: validacion; Owner: postgres
--

ALTER TABLE ONLY resultado
    ADD CONSTRAINT fk_validacion FOREIGN KEY (idvalidacion) REFERENCES validacion(iden);


--
-- TOC entry 3974 (class 0 OID 0)
-- Dependencies: 10
-- Name: diccionario; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA diccionario FROM PUBLIC;
REVOKE ALL ON SCHEMA diccionario FROM postgres;
GRANT ALL ON SCHEMA diccionario TO postgres;
GRANT ALL ON SCHEMA diccionario TO PUBLIC;


-- Completed on 2012-01-17 14:01:23

--
-- PostgreSQL database dump complete
--

