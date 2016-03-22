--
-- PostgreSQL database dump
--

-- Started on 2013-07-09 13:35:12

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 9 (class 2615 OID 59276)
-- Name: conversorfipsipu; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA conversorfipsipu;


ALTER SCHEMA conversorfipsipu OWNER TO postgres;

SET search_path = conversorfipsipu, pg_catalog;

--
-- TOC entry 2823 (class 1259 OID 59381)
-- Dependencies: 9
-- Name: conversorfipsipu_datosauxiliaresimportarentidad_id_seq; Type: SEQUENCE; Schema: conversorfipsipu; Owner: postgres
--

CREATE SEQUENCE conversorfipsipu_datosauxiliaresimportarentidad_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE conversorfipsipu.conversorfipsipu_datosauxiliaresimportarentidad_id_seq OWNER TO postgres;

--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 2823
-- Name: conversorfipsipu_datosauxiliaresimportarentidad_id_seq; Type: SEQUENCE SET; Schema: conversorfipsipu; Owner: postgres
--

SELECT pg_catalog.setval('conversorfipsipu_datosauxiliaresimportarentidad_id_seq', 1, false);


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 2824 (class 1259 OID 59383)
-- Dependencies: 9
-- Name: datosauxiliaresimportarentidad; Type: TABLE; Schema: conversorfipsipu; Owner: postgres; Tablespace: 
--

CREATE TABLE datosauxiliaresimportarentidad (
    id bigint NOT NULL,
    descripcionetiqueta character varying(255),
    observaciones character varying(255),
    version integer,
    entidad_iden integer
);


ALTER TABLE conversorfipsipu.datosauxiliaresimportarentidad OWNER TO postgres;

--
-- TOC entry 3428 (class 0 OID 59383)
-- Dependencies: 2824
-- Data for Name: datosauxiliaresimportarentidad; Type: TABLE DATA; Schema: conversorfipsipu; Owner: postgres
--



--
-- TOC entry 3426 (class 2606 OID 60478)
-- Dependencies: 2824 2824
-- Name: datosauxiliaresimportarentidad_pkey; Type: CONSTRAINT; Schema: conversorfipsipu; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY datosauxiliaresimportarentidad
    ADD CONSTRAINT datosauxiliaresimportarentidad_pkey PRIMARY KEY (id);


--
-- TOC entry 3427 (class 2606 OID 60982)
-- Dependencies: 2942 2824
-- Name: fka5852317bed43923; Type: FK CONSTRAINT; Schema: conversorfipsipu; Owner: postgres
--

ALTER TABLE ONLY datosauxiliaresimportarentidad
    ADD CONSTRAINT fka5852317bed43923 FOREIGN KEY (entidad_iden) REFERENCES planeamiento.entidad(iden);


-- Completed on 2013-07-09 13:35:12

--
-- PostgreSQL database dump complete
--

