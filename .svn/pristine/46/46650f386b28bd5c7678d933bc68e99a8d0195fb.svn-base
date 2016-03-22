--
-- PostgreSQL database dump
--

-- Started on 2013-07-09 13:34:27

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 14 (class 2615 OID 59281)
-- Name: refundido; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA refundido;


ALTER SCHEMA refundido OWNER TO postgres;

SET search_path = refundido, pg_catalog;

--
-- TOC entry 729 (class 1255 OID 59302)
-- Dependencies: 1578 14
-- Name: copiarcasoentidaddeterminacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarcasoentidaddeterminacion(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	identidaddeterminacionrefundido integer;
BEGIN
	consulta := 'SELECT iden, identidaddeterminacion, nombre, orden FROM planeamiento.casoentidaddeterminacion  WHERE identidaddeterminacion IN (SELECT ed.iden FROM planeamiento.entidaddeterminacion ed WHERE ed.iddeterminacion IN (SELECT d.iden FROM planeamiento.determinacion d WHERE d.idtramite = ' || origen || ') OR ed.identidad IN (SELECT e.iden from planeamiento.entidad e where e.idtramite = '|| origen || ')) ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_casoentidaddeterminacion_iden_seq') INTO nuevoIden;
		
		SELECT INTO identidaddeterminacionrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Entidaddeterminacion' AND idplaneamiento = fila.identidaddeterminacion;
		
		IF identidaddeterminacionrefundido IS NOT NULL THEN
		
			INSERT INTO refundido.casoentidaddeterminacion (iden, identidaddeterminacion, nombre, orden) 
				VALUES (nuevoIden, identidaddeterminacionrefundido, fila.nombre, fila.orden);
			INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Casoentidaddeterminacion', fila.iden, nuevoIden);
			total := total +1;
		
		END IF;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarcasoentidaddeterminacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 730 (class 1255 OID 59303)
-- Dependencies: 1578 14
-- Name: copiardeterminacion(integer, integer, integer, integer, boolean); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiardeterminacion(proceso integer, origen integer, destino integer, padre integer, recalcular boolean) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	idpadrerefundido integer;
	idbaserefundido integer;
	idoriginalrefundido integer;
	codigorefundido varchar;
	nuevocodigo integer;
BEGIN
	
	IF recalcular THEN
		SELECT INTO nuevocodigo to_number(Max(SubStr(LPad(Trim(codigo), 10 ,'0'), 2, 9)),'999999999') FROM refundido.determinacion WHERE idtramite= destino;
	END IF;
	
	consulta := 'SELECT iden, idpadre, idcaracter, apartado, nombre, texto, iddeterminacionbase, etiqueta, codigo, iddeterminacionoriginal, bsuspendida, orden FROM planeamiento.determinacion WHERE idtramite = ' || origen || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_determinacion_iden_seq') INTO nuevoIden;
		
		IF fila.idpadre IS NULL THEN
			IF padre = 0 THEN
				idpadrerefundido := NULL;
			ELSE
				idpadrerefundido := padre;
			END IF;
		ELSE
			SELECT INTO idpadrerefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Determinacion' AND idplaneamiento = fila.idpadre;
		END IF;
		
		IF fila.iddeterminacionbase IS NULL THEN
			idbaserefundido := NULL;
		ELSE
			SELECT INTO idbaserefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Determinacion' AND idplaneamiento = fila.iddeterminacionbase;
		END IF;
		
		IF fila.iddeterminacionoriginal IS NULL THEN
			idoriginalrefundido := NULL;
		ELSE
			SELECT INTO idoriginalrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Determinacion' AND idplaneamiento = fila.iddeterminacionoriginal;
		END IF;
		
		IF recalcular THEN
			nuevocodigo := nuevocodigo + 1;
			SELECT INTO codigorefundido LPad('0'||nuevocodigo, 10 ,'0');
		ELSE
			codigorefundido := fila.codigo;
		END IF;
		
		INSERT INTO refundido.determinacion (iden, idtramite, idpadre, idcaracter, apartado, nombre, texto, iddeterminacionbase, etiqueta, codigo, iddeterminacionoriginal, bsuspendida, orden) 
			VALUES (nuevoIden , destino, idpadrerefundido,fila.idcaracter, fila.apartado, fila.nombre, fila.texto, idbaserefundido, fila.etiqueta, codigorefundido, idoriginalrefundido, fila.bsuspendida, fila.orden);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Determinacion', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiardeterminacion(proceso integer, origen integer, destino integer, padre integer, recalcular boolean) OWNER TO postgres;

--
-- TOC entry 731 (class 1255 OID 59304)
-- Dependencies: 1578 14
-- Name: copiardeterminaciongrupo(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiardeterminaciongrupo(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	iddeterminacionrefundido integer;
	iddeterminaciongruporefundido integer;
BEGIN
	consulta := 'SELECT dge.iden, dge.iddeterminaciongrupo, dge.iddeterminacion FROM planeamiento.determinaciongrupoentidad dge, planeamiento.determinacion d WHERE dge.iddeterminacion = d.iden AND d.idtramite = ' || origen || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_determinaciongrupoentidad_iden_seq') INTO nuevoIden;
		
		IF fila.iddeterminacion IS NULL THEN
			iddeterminacionrefundido := NULL;
		ELSE
			SELECT INTO iddeterminacionrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Determinacion' AND idplaneamiento = fila.iddeterminacion;
		END IF;
		
		SELECT INTO iddeterminaciongruporefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Determinacion' AND idplaneamiento = fila.iddeterminaciongrupo;
		
		INSERT INTO refundido.determinaciongrupoentidad (iden, iddeterminaciongrupo, iddeterminacion) 
			VALUES (nuevoIden , iddeterminaciongruporefundido, iddeterminacionrefundido);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Determinaciongrupoentidad', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiardeterminaciongrupo(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 732 (class 1255 OID 59305)
-- Dependencies: 1578 14
-- Name: copiardocumentocaso(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiardocumentocaso(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	iddocumentorefundido integer;
	idcasorefundido integer;
BEGIN
	consulta := 'SELECT dc.iden, dc.idcaso, dc.iddocumento FROM planeamiento.documentocaso dc, planeamiento.documento d WHERE dc.iddocumento = d.iden AND d.idtramite = ' || origen || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_documentocaso_iden_seq') INTO nuevoIden;
		
		SELECT INTO idcasorefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Casoentidaddeterminacion' AND idplaneamiento = fila.idcaso;
		
		SELECT INTO iddocumentorefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Documento' AND idplaneamiento = fila.iddocumento;
		
		INSERT INTO refundido.documentocaso (iden, idcaso, iddocumento) 
			VALUES (nuevoIden, idcasorefundido, iddocumentorefundido);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Documentocaso', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiardocumentocaso(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 733 (class 1255 OID 59306)
-- Dependencies: 1578 14
-- Name: copiardocumentodeterminacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiardocumentodeterminacion(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	iddocumentorefundido integer;
	iddeterminacionrefundido integer;
BEGIN
	consulta := 'SELECT dd.iden, dd.iddeterminacion, dd.iddocumento FROM planeamiento.documentodeterminacion dd, planeamiento.documento d WHERE dd.iddocumento = d.iden AND d.idtramite = ' || origen || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_documentodeterminacion_iden_seq') INTO nuevoIden;
		
		SELECT INTO iddeterminacionrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Determinacion' AND idplaneamiento = fila.iddeterminacion;
		
		SELECT INTO iddocumentorefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Documento' AND idplaneamiento = fila.iddocumento;
		
		INSERT INTO refundido.documentodeterminacion (iden, iddeterminacion, iddocumento) 
			VALUES (nuevoIden, iddeterminacionrefundido, iddocumentorefundido);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Documentodeterminacion', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiardocumentodeterminacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 734 (class 1255 OID 59307)
-- Dependencies: 1578 14
-- Name: copiardocumentoentidad(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiardocumentoentidad(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	iddocumentorefundido integer;
	identidadrefundido integer;
BEGIN
	consulta := 'SELECT de.iden, de.identidad, de.iddocumento FROM planeamiento.documentoentidad de, planeamiento.documento d WHERE de.iddocumento = d.iden AND d.idtramite = ' || origen || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_documentoentidad_iden_seq') INTO nuevoIden;
		
		SELECT INTO identidadrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Entidad' AND idplaneamiento = fila.identidad;
		
		SELECT INTO iddocumentorefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Documento' AND idplaneamiento = fila.iddocumento;
		
		INSERT INTO refundido.documentoentidad (iden, identidad, iddocumento) 
			VALUES (nuevoIden, identidadrefundido, iddocumentorefundido);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Documentoentidad', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiardocumentoentidad(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 735 (class 1255 OID 59308)
-- Dependencies: 1578 14
-- Name: copiarentidad(integer, integer, integer, integer, boolean); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarentidad(proceso integer, origen integer, destino integer, padre integer, recalcular boolean) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	idpadrerefundido integer;
	idbaserefundido integer;
	idoriginalrefundido integer;
	codigorefundido varchar;
	nuevocodigo integer;
BEGIN
	
	IF recalcular THEN
		SELECT INTO nuevocodigo to_number(Max(SubStr(LPad(Trim(codigo), 10 ,'0'), 2, 9)),'999999999') FROM refundido.entidad WHERE idtramite = destino;
	END IF;
	
	consulta := 'SELECT iden, idpadre, nombre, clave, identidadbase, etiqueta, codigo, identidadoriginal, bsuspendida, orden FROM planeamiento.entidad WHERE idtramite = ' || origen || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_entidad_iden_seq') INTO nuevoIden;
		
		IF fila.idpadre IS NULL THEN
			IF padre = 0 THEN
				idpadrerefundido := NULL;
			ELSE
				idpadrerefundido := padre;
			END IF;
		ELSE
			SELECT INTO idpadrerefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Entidad' AND idplaneamiento = fila.idpadre;
		END IF;
		
		IF fila.identidadbase IS NULL THEN
			idbaserefundido := NULL;
		ELSE
			SELECT INTO idbaserefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Entidad' AND idplaneamiento = fila.identidadbase;
		END IF;
		
		IF fila.identidadoriginal IS NULL THEN
			idoriginalrefundido := NULL;
		ELSE
			SELECT INTO idoriginalrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Entidad' AND idplaneamiento = fila.identidadoriginal;
		END IF;
		
		IF recalcular THEN
			nuevocodigo := nuevocodigo + 1;
			SELECT INTO codigorefundido LPad('0'||nuevocodigo, 10 ,'0');
		ELSE
			codigorefundido := fila.codigo;
		END IF;
		
		INSERT INTO refundido.entidad (iden, idtramite, idpadre, nombre, clave, identidadbase, etiqueta, codigo, identidadoriginal, bsuspendida, orden) 
			VALUES (nuevoIden , destino, idpadrerefundido, fila.nombre, fila.clave, idbaserefundido, fila.etiqueta, codigorefundido, idoriginalrefundido, fila.bsuspendida, fila.orden);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Entidad', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarentidad(proceso integer, origen integer, destino integer, padre integer, recalcular boolean) OWNER TO postgres;

--
-- TOC entry 736 (class 1255 OID 59309)
-- Dependencies: 1578 14
-- Name: copiarentidaddeterminacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarentidaddeterminacion(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	iddeterminacionrefundido integer;
	identidadrefundido integer;
BEGIN
	consulta := 'SELECT ed.iden, ed.identidad, ed.iddeterminacion FROM planeamiento.entidaddeterminacion ed WHERE ed.iddeterminacion IN (SELECT d.iden FROM planeamiento.determinacion d WHERE d.idtramite = ' || origen || ') OR ed.identidad IN (SELECT e.iden from planeamiento.entidad e where e.idtramite = '|| origen || ') ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_entidaddeterminacion_iden_seq') INTO nuevoIden;
		
		SELECT INTO iddeterminacionrefundido t.idrefundido FROM refundido.traza t, refundido.determinacion d WHERE t.idproceso = proceso AND t.tabla = 'Determinacion' AND t.idplaneamiento = fila.iddeterminacion AND t.idrefundido = d.iden;
		
		SELECT INTO identidadrefundido t.idrefundido FROM refundido.traza t, refundido.entidad e WHERE t.idproceso = proceso AND t.tabla = 'Entidad' AND t.idplaneamiento = fila.identidad AND t.idrefundido = e.iden;
		
		IF iddeterminacionrefundido IS NOT NULL AND identidadrefundido IS NOT NULL THEN
			INSERT INTO refundido.entidaddeterminacion (iden, identidad, iddeterminacion) 
				VALUES (nuevoIden, identidadrefundido, iddeterminacionrefundido);
			INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Entidaddeterminacion', fila.iden, nuevoIden);
			total := total +1;
		END IF;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarentidaddeterminacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 737 (class 1255 OID 59310)
-- Dependencies: 1578 14
-- Name: copiarentidaddeterminacionregimen(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarentidaddeterminacionregimen(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	idcasorefundido integer;
	idcasoaplicacionrefundido integer;
	iddeterminacionrefundido integer;
	identidadrefrefundido integer;
	idopciondeterminacionrefundido integer;
BEGIN
	consulta := 'SELECT iden, idcaso, iddeterminacionregimen, idopciondeterminacion, valor, superposicion, idcasoaplicacion FROM planeamiento.entidaddeterminacionregimen WHERE idcaso IN (SELECT iden FROM planeamiento.casoentidaddeterminacion  WHERE identidaddeterminacion IN (SELECT ed.iden FROM planeamiento.entidaddeterminacion ed WHERE ed.iddeterminacion IN (SELECT d.iden FROM planeamiento.determinacion d WHERE d.idtramite = ' || origen || ') OR ed.identidad IN (SELECT e.iden from planeamiento.entidad e where e.idtramite = '|| origen || '))) ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_entidaddeterminacionregimen_iden_seq') INTO nuevoIden;
		
		SELECT INTO idcasorefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Casoentidaddeterminacion' AND idplaneamiento = fila.idcaso;
		
		IF idcasorefundido IS NOT NULL THEN
		
			IF fila.iddeterminacionregimen IS NULL THEN
				iddeterminacionrefundido := NULL;
			ELSE
				SELECT INTO iddeterminacionrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Determinacion' AND idplaneamiento = fila.iddeterminacionregimen;
			END IF;
			
			IF fila.idopciondeterminacion IS NULL THEN
				idopciondeterminacionrefundido := NULL;
			ELSE
				SELECT INTO idopciondeterminacionrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Opciondeterminacion' AND idplaneamiento = fila.idopciondeterminacion;
			END IF;
			
			IF fila.idcasoaplicacion IS NULL THEN
				idcasoaplicacionrefundido := NULL;
			ELSE
				SELECT INTO idcasoaplicacionrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Casoentidaddeterminacion' AND idplaneamiento = fila.idcasoaplicacion;
			END IF;
			
			INSERT INTO refundido.entidaddeterminacionregimen (iden, idcaso, iddeterminacionregimen, idopciondeterminacion, valor, superposicion, idcasoaplicacion) 
				VALUES (nuevoIden, idcasorefundido, iddeterminacionrefundido, idopciondeterminacionrefundido, fila.valor, fila.superposicion, idcasoaplicacionrefundido);
			INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Entidaddeterminacionregimen', fila.iden, nuevoIden);
			total := total +1;
		END IF;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarentidaddeterminacionregimen(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 738 (class 1255 OID 59311)
-- Dependencies: 1578 14
-- Name: copiarlinea(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarlinea(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	identidadrefundido integer;
BEGIN
	consulta := 'SELECT ep.iden, ep.identidad, ep.geom, ep.bsuspendida FROM planeamiento.entidadlin ep, planeamiento.entidad e WHERE ep.identidad = e.iden AND e.idtramite = ' || origen || ' ORDER BY ep.iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_entidadlin_iden_seq') INTO nuevoIden;
		
		SELECT INTO identidadrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Entidad' AND idplaneamiento = fila.identidad;
		
		INSERT INTO refundido.entidadlin (iden, identidad, geom, bsuspendida) 
			VALUES (nuevoIden, identidadrefundido, fila.geom, fila.bsuspendida);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Entidadlin', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarlinea(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 739 (class 1255 OID 59312)
-- Dependencies: 1578 14
-- Name: copiaropciondeterminacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiaropciondeterminacion(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	iddeterminacionrefundido integer;
	iddeterminacionvalorrefrefundido integer;
BEGIN
	consulta := 'SELECT DISTINCT od.iden, od.iddeterminacion, od.iddeterminacionvalorref FROM planeamiento.opciondeterminacion od, planeamiento.determinacion d, planeamiento.determinacion dvr WHERE od.iddeterminacion = d.iden AND od.iddeterminacionvalorref = dvr.iden AND (d.idtramite = ' || origen || ' OR dvr.idtramite = ' || origen ||')  ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT INTO iddeterminacionrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Determinacion' AND idplaneamiento = fila.iddeterminacion;
		
		SELECT INTO iddeterminacionvalorrefrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Determinacion' AND idplaneamiento = fila.iddeterminacionvalorref;
		
		IF iddeterminacionrefundido IS NOT NULL AND iddeterminacionvalorrefrefundido IS NOT NULL THEN
			SELECT nextval('refundido.refundido_opciondeterminacion_iden_seq') INTO nuevoIden;
			INSERT INTO refundido.opciondeterminacion (iden, iddeterminacion, iddeterminacionvalorref) 
				VALUES (nuevoIden, iddeterminacionrefundido, iddeterminacionvalorrefrefundido);
			INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Opciondeterminacion', fila.iden, nuevoIden);
			total := total +1;
		END IF;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiaropciondeterminacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 740 (class 1255 OID 59313)
-- Dependencies: 1578 14
-- Name: copiarpoligono(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarpoligono(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	identidadrefundido integer;
BEGIN
	consulta := 'SELECT ep.iden, ep.identidad, ep.geom, ep.bsuspendida FROM planeamiento.entidadpol ep, planeamiento.entidad e WHERE ep.identidad = e.iden AND e.idtramite = ' || origen || ' ORDER BY ep.iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_entidadpol_iden_seq') INTO nuevoIden;
		
		SELECT INTO identidadrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Entidad' AND idplaneamiento = fila.identidad;
		
		INSERT INTO refundido.entidadpol (iden, identidad, geom, bsuspendida) 
			VALUES (nuevoIden, identidadrefundido, fila.geom, fila.bsuspendida);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Entidadpol', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarpoligono(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 741 (class 1255 OID 59314)
-- Dependencies: 1578 14
-- Name: copiarpropiedadrelacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarpropiedadrelacion(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	idrelacionrefundido integer;
BEGIN
	consulta := 'SELECT p.iden, p.idrelacion, p.iddefpropiedad, p.valor FROM planeamiento.propiedadrelacion p, planeamiento.relacion r WHERE p.idrelacion = r.iden AND r.idtramitecreador = '|| origen || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_propiedadrelacion_iden_seq') INTO nuevoIden;

		SELECT INTO idrelacionrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Relacion' AND idplaneamiento = fila.idrelacion;
		
		INSERT INTO refundido.propiedadrelacion (iden, idrelacion, iddefpropiedad, valor) 
			VALUES (nuevoIden, idrelacionrefundido, fila.iddefpropiedad, fila.valor);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Propiedadrelacion', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarpropiedadrelacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 742 (class 1255 OID 59315)
-- Dependencies: 1578 14
-- Name: copiarpunto(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarpunto(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	identidadrefundido integer;
BEGIN
	consulta := 'SELECT ep.iden, ep.identidad, ep.geom, ep.bsuspendida FROM planeamiento.entidadpnt ep, planeamiento.entidad e WHERE ep.identidad = e.iden AND e.idtramite = ' || origen || ' ORDER BY ep.iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_entidadpnt_iden_seq') INTO nuevoIden;
		
		SELECT INTO identidadrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Entidad' AND idplaneamiento = fila.identidad;
		
		INSERT INTO refundido.entidadpnt (iden, identidad, geom, bsuspendida) 
			VALUES (nuevoIden, identidadrefundido, fila.geom, fila.bsuspendida);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Entidadpnt', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarpunto(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 743 (class 1255 OID 59316)
-- Dependencies: 1578 14
-- Name: copiarregimenespecifico(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarregimenespecifico(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	idedrrefundido integer;
	idpadrerefundido integer;
BEGIN
	consulta := 'SELECT iden, identidaddeterminacionregimen, idpadre, orden, nombre, texto FROM planeamiento.regimenespecifico WHERE identidaddeterminacionregimen IN (SELECT iden FROM planeamiento.entidaddeterminacionregimen WHERE idcaso IN (SELECT iden FROM planeamiento.casoentidaddeterminacion  WHERE identidaddeterminacion IN (SELECT ed.iden FROM planeamiento.entidaddeterminacion ed WHERE ed.iddeterminacion IN (SELECT d.iden FROM planeamiento.determinacion d WHERE d.idtramite = ' || origen || ') OR ed.identidad IN (SELECT e.iden from planeamiento.entidad e where e.idtramite = '|| origen || ')))) ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_regimenespecifico_iden_seq') INTO nuevoIden;
		
		SELECT INTO idedrrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Entidaddeterminacionregimen' AND idplaneamiento = fila.identidaddeterminacionregimen;
		
		IF idedrrefundido IS NOT NULL THEN
		
			IF fila.idpadre IS NULL THEN
				idpadrerefundido := NULL;
			ELSE
				SELECT INTO idpadrerefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Regimenespecifico' AND idplaneamiento = fila.idpadre;
			END IF;
			
			INSERT INTO refundido.regimenespecifico (iden, identidaddeterminacionregimen, idpadre, orden, nombre, texto) 
				VALUES (nuevoIden, idedrrefundido, idpadrerefundido, fila.orden, fila.nombre, fila.texto);
			INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Regimenespecifico', fila.iden, nuevoIden);
			total := total +1;
		END IF;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarregimenespecifico(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 744 (class 1255 OID 59317)
-- Dependencies: 1578 14
-- Name: copiarrelacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarrelacion(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
BEGIN
	consulta := 'SELECT iden, iddefrelacion, idtramitecreador FROM planeamiento.relacion WHERE idtramitecreador = '|| origen || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_relacion_iden_seq') INTO nuevoIden;

		INSERT INTO refundido.relacion (iden, iddefrelacion, idtramitecreador) 
			VALUES (nuevoIden, fila.iddefrelacion, destino);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Relacion', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarrelacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 745 (class 1255 OID 59318)
-- Dependencies: 1578 14
-- Name: copiarvectorrelacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarvectorrelacion(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	tablaReferenciada varchar;
	idrelacionrefundido integer;
	idvalorrefundido integer;
BEGIN
	consulta := 'SELECT v.iden, v.idrelacion, v.iddefvector, v.valor FROM planeamiento.vectorrelacion v, planeamiento.relacion r WHERE v.idrelacion = r.iden AND r.idtramitecreador = '|| origen || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_vectorrelacion_iden_seq') INTO nuevoIden;

		SELECT INTO idrelacionrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Relacion' AND idplaneamiento = fila.idrelacion;
		
		IF fila.valor = 0 THEN
			idvalorrefundido := 0;
		ELSE
			IF fila.iddefvector = 4 THEN
				tablaReferenciada := 'Entidad';
			ELSIF fila.iddefvector = 5 THEN
				tablaReferenciada := 'Entidad';
			ELSIF fila.iddefvector = 42 THEN
				tablaReferenciada := 'Entidad';
			ELSIF fila.iddefvector = 41 THEN
				tablaReferenciada := 'Relacion';
			ELSE
				tablaReferenciada := 'Determinacion';
			END IF;
			
			SELECT INTO idvalorrefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = tablaReferenciada AND idplaneamiento = fila.valor;
		END IF;
		
		INSERT INTO refundido.vectorrelacion (iden, idrelacion, iddefvector, valor) 
			VALUES (nuevoIden, idrelacionrefundido, fila.iddefvector, idvalorrefundido);
		INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Vectorrelacion', fila.iden, nuevoIden);
		total := total +1;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarvectorrelacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 746 (class 1255 OID 59319)
-- Dependencies: 1578 14
-- Name: copiarvinculocaso(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarvinculocaso(proceso integer, origen integer, destino integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	nuevoIden integer;
	fila RECORD;
	consulta varchar;
	total integer;
	idcasorefundido integer;
	idcasovinculadorefundido integer;
BEGIN
	consulta := 'SELECT iden, idcaso, idcasovinculado FROM planeamiento.vinculocaso WHERE idcaso IN (SELECT iden FROM planeamiento.casoentidaddeterminacion WHERE identidaddeterminacion IN (SELECT ed.iden FROM planeamiento.entidaddeterminacion ed WHERE ed.iddeterminacion IN (SELECT d.iden FROM planeamiento.determinacion d WHERE d.idtramite = ' || origen || ') OR ed.identidad IN (SELECT e.iden from planeamiento.entidad e where e.idtramite = '|| origen || '))) ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		SELECT nextval('refundido.refundido_vinculocaso_iden_seq') INTO nuevoIden;
		
		SELECT INTO idcasorefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Casoentidaddeterminacion' AND idplaneamiento = fila.idcaso;
		
		IF idcasorefundido IS NOT NULL THEN
		
			IF fila.idcasovinculado IS NULL THEN
				idcasovinculadorefundido := NULL;
			ELSE
				SELECT INTO idcasovinculadorefundido idrefundido FROM refundido.traza WHERE idproceso = proceso AND tabla = 'Casoentidaddeterminacion' AND idplaneamiento = fila.idcasovinculado;
			END IF;
			
			INSERT INTO refundido.vinculocaso (iden, idcaso, idcasovinculado) 
				VALUES (nuevoIden, idcasorefundido, idcasovinculadorefundido);
			INSERT INTO refundido.traza (idproceso, tabla, idplaneamiento, idrefundido) VALUES (proceso, 'Vinculocaso', fila.iden, nuevoIden);
			total := total +1;
		END IF;
	END LOOP;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.copiarvinculocaso(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 747 (class 1255 OID 59320)
-- Dependencies: 1578 14
-- Name: eliminarcasoentidaddeterminacion(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarcasoentidaddeterminacion(ideliminar integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	fila RECORD;
	consulta varchar;
	total integer;
BEGIN
	
	consulta := 'SELECT iddocumento FROM refundido.documentocaso WHERE idcaso = ' || idEliminar || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminardocumento( fila.iddocumento);
	END LOOP;
	
	consulta := 'SELECT iden FROM refundido.entidaddeterminacionregimen WHERE idcaso = ' || idEliminar || ' OR idcasoaplicacion = ' || idEliminar || ' ORDER BY iden';
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarentidaddeterminacionregimen( fila.iden);
	END LOOP;
	
	consulta := 'DELETE FROM refundido.vinculocaso WHERE idcaso = ' || idEliminar || ' OR idcasovinculado = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.casoentidaddeterminacion WHERE iden = ' ||idEliminar;
	
	EXECUTE consulta;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.eliminarcasoentidaddeterminacion(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 748 (class 1255 OID 59321)
-- Dependencies: 1578 14
-- Name: eliminardeterminacion(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminardeterminacion(ideliminar integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	fila RECORD;
	consulta varchar;
	total integer;
BEGIN
	
	consulta := 'SELECT iden FROM refundido.determinacion WHERE idpadre = ' || idEliminar || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminardeterminacion( fila.iden);
	END LOOP;
	
	consulta := 'SELECT iden FROM refundido.determinacion WHERE iddeterminacionbase = ' || idEliminar || ' ORDER BY iden';
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminardeterminacion( fila.iden);
	END LOOP;
	
	consulta := 'SELECT iden FROM refundido.determinacion WHERE iddeterminacionoriginal = ' || idEliminar || ' ORDER BY iden';
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminardeterminacion( fila.iden);
	END LOOP;
	
	consulta := 'SELECT DISTINCT iddocumento FROM refundido.documentodeterminacion WHERE iddeterminacion = ' || idEliminar;
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminardocumento( fila.iddocumento);
	END LOOP;
	
	consulta := 'SELECT DISTINCT edr.iden FROM refundido.entidaddeterminacionregimen edr WHERE edr.iddeterminacionregimen = ' || idEliminar;
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarentidaddeterminacionregimen( fila.iden);
	END LOOP;
	
	consulta := 'SELECT DISTINCT iden FROM refundido.opciondeterminacion WHERE iddeterminacion = ' || idEliminar || ' OR iddeterminacionvalorref = ' || idEliminar;
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminaropciondeterminacion( fila.iden);
	END LOOP;
	
	consulta := 'SELECT DISTINCT iden FROM refundido.entidaddeterminacion WHERE iddeterminacion = ' || idEliminar;
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarentidaddeterminacion( fila.iden);
	END LOOP;
	
	consulta := 'DELETE FROM refundido.determinaciongrupoentidad WHERE iddeterminacion = ' || idEliminar || ' OR iddeterminaciongrupo = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.determinacion WHERE iden = ' ||idEliminar;
	
	EXECUTE consulta;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.eliminardeterminacion(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 749 (class 1255 OID 59322)
-- Dependencies: 1578 14
-- Name: eliminardocumento(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminardocumento(ideliminar integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	fila RECORD;
	consulta varchar;
	total integer;
BEGIN
	
	consulta := 'SELECT iden FROM refundido.documento WHERE iddocumentooriginal = ' || idEliminar || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminardocumento( fila.iden);
	END LOOP;
	
	consulta := 'DELETE FROM refundido.documentoentidad WHERE iddocumento = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.documentodeterminacion WHERE iddocumento = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.documentocaso WHERE iddocumento = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.documentoshp WHERE iddocumento = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.documento WHERE iden = ' || idEliminar;
	
	EXECUTE consulta;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.eliminardocumento(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 750 (class 1255 OID 59323)
-- Dependencies: 1578 14
-- Name: eliminarentidad(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarentidad(ideliminar integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	fila RECORD;
	consulta varchar;
	total integer;
BEGIN
	
	consulta := 'SELECT iden FROM refundido.entidad WHERE idpadre = ' || idEliminar || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarentidad( fila.iden);
	END LOOP;
	
	consulta := 'SELECT iden FROM refundido.entidad WHERE identidadbase = ' || idEliminar || ' ORDER BY iden';
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarentidad( fila.iden);
	END LOOP;
	
	consulta := 'SELECT iden FROM refundido.entidad WHERE identidadoriginal = ' || idEliminar || ' ORDER BY iden';
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarentidad( fila.iden);
	END LOOP;
	
	consulta := 'SELECT iden FROM refundido.entidaddeterminacion WHERE identidad = ' || idEliminar || ' ORDER BY iden';
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarentidaddeterminacion( fila.iden);
	END LOOP;
	
	consulta := 'SELECT DISTINCT iddocumento FROM refundido.documentoentidad WHERE identidad = ' || idEliminar;
	
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminardocumento( fila.iddocumento);
	END LOOP;
	
	consulta := 'DELETE FROM refundido.entidadpol WHERE identidad = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.entidadlin WHERE identidad = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.entidadpnt WHERE identidad = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.ambitoaplicacionambito WHERE identidad = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.entidad WHERE iden = ' ||idEliminar;
	
	EXECUTE consulta;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.eliminarentidad(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 751 (class 1255 OID 59324)
-- Dependencies: 1578 14
-- Name: eliminarentidaddeterminacion(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarentidaddeterminacion(ideliminar integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	fila RECORD;
	consulta varchar;
	total integer;
BEGIN
	
	consulta := 'SELECT iden FROM refundido.casoentidaddeterminacion WHERE identidaddeterminacion = ' || idEliminar || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarcasoentidaddeterminacion( fila.iden);
	END LOOP;
	
	consulta := 'DELETE FROM refundido.entidaddeterminacion WHERE iden = ' ||idEliminar;
	
	EXECUTE consulta;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.eliminarentidaddeterminacion(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 752 (class 1255 OID 59325)
-- Dependencies: 1578 14
-- Name: eliminarentidaddeterminacionregimen(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarentidaddeterminacionregimen(ideliminar integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	fila RECORD;
	consulta varchar;
	total integer;
BEGIN
	
	consulta := 'SELECT iden FROM refundido.regimenespecifico WHERE identidaddeterminacionregimen = ' || idEliminar || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarregimen( fila.iden);
	END LOOP;
	
	consulta := 'DELETE FROM refundido.entidaddeterminacionregimen WHERE iden = ' || idEliminar;
	
	EXECUTE consulta;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.eliminarentidaddeterminacionregimen(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 753 (class 1255 OID 59326)
-- Dependencies: 1578 14
-- Name: eliminaropciondeterminacion(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminaropciondeterminacion(ideliminar integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	fila RECORD;
	consulta varchar;
	total integer;
BEGIN
	
	consulta := 'SELECT iden FROM refundido.entidaddeterminacionregimen WHERE idopciondeterminacion = ' || idEliminar || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarentidaddeterminacionregimen( fila.iden);
	END LOOP;
	
	consulta := 'DELETE FROM refundido.opciondeterminacion WHERE iden = ' || idEliminar;
	
	EXECUTE consulta;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.eliminaropciondeterminacion(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 754 (class 1255 OID 59327)
-- Dependencies: 1578 14
-- Name: eliminarregimen(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarregimen(ideliminar integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	fila RECORD;
	consulta varchar;
	total integer;
BEGIN
	
	consulta := 'SELECT iden FROM refundido.regimenespecifico WHERE idpadre = ' || idEliminar || ' ORDER BY iden';
	
	total := 0;
	FOR fila IN EXECUTE consulta
	LOOP
		PERFORM refundido.eliminarregimen( fila.iden);
	END LOOP;
	
	consulta := 'DELETE FROM refundido.regimenespecifico WHERE iden = ' || idEliminar;
	
	EXECUTE consulta;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.eliminarregimen(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 755 (class 1255 OID 59328)
-- Dependencies: 1578 14
-- Name: eliminarrelacion(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarrelacion(ideliminar integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	fila RECORD;
	consulta varchar;
	total integer;
BEGIN
	total := 0;
	
	consulta := 'DELETE FROM refundido.propiedadrelacion WHERE idrelacion = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.vectorrelacion WHERE idrelacion = ' || idEliminar;
	
	EXECUTE consulta;
	
	consulta := 'DELETE FROM refundido.relacion WHERE iden = ' ||idEliminar;
	
	EXECUTE consulta;
	
 RETURN total;
END;
$$;


ALTER FUNCTION refundido.eliminarrelacion(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 3022 (class 1259 OID 60116)
-- Dependencies: 14
-- Name: refundido_ambitoaplicacionambito_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_ambitoaplicacionambito_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_ambitoaplicacionambito_iden_seq OWNER TO postgres;

--
-- TOC entry 3727 (class 0 OID 0)
-- Dependencies: 3022
-- Name: refundido_ambitoaplicacionambito_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_ambitoaplicacionambito_iden_seq', 1, false);


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 3023 (class 1259 OID 60118)
-- Dependencies: 3425 14
-- Name: ambitoaplicacionambito; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE ambitoaplicacionambito (
    iden integer DEFAULT nextval('refundido_ambitoaplicacionambito_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    idambito integer NOT NULL
);


ALTER TABLE refundido.ambitoaplicacionambito OWNER TO postgres;

--
-- TOC entry 3024 (class 1259 OID 60122)
-- Dependencies: 14
-- Name: refundido_archivo_sequence; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_archivo_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_archivo_sequence OWNER TO postgres;

--
-- TOC entry 3728 (class 0 OID 0)
-- Dependencies: 3024
-- Name: refundido_archivo_sequence; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_archivo_sequence', 1, false);


--
-- TOC entry 3025 (class 1259 OID 60124)
-- Dependencies: 3426 14
-- Name: archivo; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE archivo (
    iden integer DEFAULT nextval('refundido_archivo_sequence'::regclass) NOT NULL,
    proceso integer NOT NULL,
    tipo character varying(256) NOT NULL,
    contenido character varying(1000) NOT NULL
);


ALTER TABLE refundido.archivo OWNER TO postgres;

--
-- TOC entry 3026 (class 1259 OID 60131)
-- Dependencies: 14
-- Name: refundido_casoentidaddeterminacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_casoentidaddeterminacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_casoentidaddeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3729 (class 0 OID 0)
-- Dependencies: 3026
-- Name: refundido_casoentidaddeterminacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_casoentidaddeterminacion_iden_seq', 1, false);


--
-- TOC entry 3027 (class 1259 OID 60133)
-- Dependencies: 3427 3428 14
-- Name: casoentidaddeterminacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE casoentidaddeterminacion (
    iden integer DEFAULT nextval('refundido_casoentidaddeterminacion_iden_seq'::regclass) NOT NULL,
    identidaddeterminacion integer NOT NULL,
    nombre character varying(150),
    orden integer DEFAULT 0 NOT NULL
);


ALTER TABLE refundido.casoentidaddeterminacion OWNER TO postgres;

--
-- TOC entry 3028 (class 1259 OID 60138)
-- Dependencies: 14
-- Name: refundido_determinacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_determinacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_determinacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3730 (class 0 OID 0)
-- Dependencies: 3028
-- Name: refundido_determinacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_determinacion_iden_seq', 1, false);


--
-- TOC entry 3029 (class 1259 OID 60140)
-- Dependencies: 3429 3430 3431 14
-- Name: determinacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE determinacion (
    iden integer DEFAULT nextval('refundido_determinacion_iden_seq'::regclass) NOT NULL,
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
    orden integer DEFAULT 1 NOT NULL
);


ALTER TABLE refundido.determinacion OWNER TO postgres;

--
-- TOC entry 3030 (class 1259 OID 60149)
-- Dependencies: 14
-- Name: refundido_determinaciongrupoentidad_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_determinaciongrupoentidad_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_determinaciongrupoentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 3731 (class 0 OID 0)
-- Dependencies: 3030
-- Name: refundido_determinaciongrupoentidad_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_determinaciongrupoentidad_iden_seq', 1, false);


--
-- TOC entry 3031 (class 1259 OID 60151)
-- Dependencies: 3432 14
-- Name: determinaciongrupoentidad; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE determinaciongrupoentidad (
    iden integer DEFAULT nextval('refundido_determinaciongrupoentidad_iden_seq'::regclass) NOT NULL,
    iddeterminaciongrupo integer,
    iddeterminacion integer NOT NULL
);


ALTER TABLE refundido.determinaciongrupoentidad OWNER TO postgres;

--
-- TOC entry 3032 (class 1259 OID 60155)
-- Dependencies: 14
-- Name: refundido_documento_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_documento_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_documento_iden_seq OWNER TO postgres;

--
-- TOC entry 3732 (class 0 OID 0)
-- Dependencies: 3032
-- Name: refundido_documento_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_documento_iden_seq', 1, false);


--
-- TOC entry 3033 (class 1259 OID 60157)
-- Dependencies: 3433 14
-- Name: documento; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE documento (
    iden integer DEFAULT nextval('refundido_documento_iden_seq'::regclass) NOT NULL,
    nombre character varying(255) NOT NULL,
    archivo character varying(255) NOT NULL,
    comentario text,
    idtramite integer NOT NULL,
    escala integer,
    iddocumentooriginal integer,
    idtipodocumento integer,
    idgrupodocumento integer
);


ALTER TABLE refundido.documento OWNER TO postgres;

--
-- TOC entry 3034 (class 1259 OID 60164)
-- Dependencies: 14
-- Name: refundido_documentocaso_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_documentocaso_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_documentocaso_iden_seq OWNER TO postgres;

--
-- TOC entry 3733 (class 0 OID 0)
-- Dependencies: 3034
-- Name: refundido_documentocaso_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_documentocaso_iden_seq', 1, false);


--
-- TOC entry 3035 (class 1259 OID 60166)
-- Dependencies: 3434 14
-- Name: documentocaso; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE documentocaso (
    iden integer DEFAULT nextval('refundido_documentocaso_iden_seq'::regclass) NOT NULL,
    idcaso integer NOT NULL,
    iddocumento integer NOT NULL
);


ALTER TABLE refundido.documentocaso OWNER TO postgres;

--
-- TOC entry 3036 (class 1259 OID 60170)
-- Dependencies: 14
-- Name: refundido_documentodeterminacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_documentodeterminacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_documentodeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3734 (class 0 OID 0)
-- Dependencies: 3036
-- Name: refundido_documentodeterminacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_documentodeterminacion_iden_seq', 1, false);


--
-- TOC entry 3037 (class 1259 OID 60172)
-- Dependencies: 3435 14
-- Name: documentodeterminacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE documentodeterminacion (
    iden integer DEFAULT nextval('refundido_documentodeterminacion_iden_seq'::regclass) NOT NULL,
    iddeterminacion integer NOT NULL,
    iddocumento integer NOT NULL
);


ALTER TABLE refundido.documentodeterminacion OWNER TO postgres;

--
-- TOC entry 3038 (class 1259 OID 60176)
-- Dependencies: 14
-- Name: refundido_documentoentidad_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_documentoentidad_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_documentoentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 3735 (class 0 OID 0)
-- Dependencies: 3038
-- Name: refundido_documentoentidad_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_documentoentidad_iden_seq', 1, false);


--
-- TOC entry 3039 (class 1259 OID 60178)
-- Dependencies: 3436 14
-- Name: documentoentidad; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE documentoentidad (
    iden integer DEFAULT nextval('refundido_documentoentidad_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    iddocumento integer NOT NULL
);


ALTER TABLE refundido.documentoentidad OWNER TO postgres;

--
-- TOC entry 3040 (class 1259 OID 60182)
-- Dependencies: 14
-- Name: refundido_documentoshp_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_documentoshp_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_documentoshp_iden_seq OWNER TO postgres;

--
-- TOC entry 3736 (class 0 OID 0)
-- Dependencies: 3040
-- Name: refundido_documentoshp_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_documentoshp_iden_seq', 1, false);


--
-- TOC entry 3041 (class 1259 OID 60184)
-- Dependencies: 3437 3438 3439 3440 14 1041
-- Name: documentoshp; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE documentoshp (
    iddocumento integer,
    iden integer DEFAULT nextval('refundido_documentoshp_iden_seq'::regclass) NOT NULL,
    geom public.geometry,
    hoja character varying(100) NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK ((((public.geometrytype(geom) = 'MULTIPOLYGON'::text) OR (public.geometrytype(geom) = 'POLYGON'::text)) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.srid(geom) = (-1)))
);


ALTER TABLE refundido.documentoshp OWNER TO postgres;

--
-- TOC entry 3042 (class 1259 OID 60194)
-- Dependencies: 14
-- Name: refundido_entidad_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidad_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidad_iden_seq OWNER TO postgres;

--
-- TOC entry 3737 (class 0 OID 0)
-- Dependencies: 3042
-- Name: refundido_entidad_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_entidad_iden_seq', 1, false);


--
-- TOC entry 3043 (class 1259 OID 60196)
-- Dependencies: 3441 3442 3443 14
-- Name: entidad; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE entidad (
    iden integer DEFAULT nextval('refundido_entidad_iden_seq'::regclass) NOT NULL,
    idtramite integer NOT NULL,
    idpadre integer,
    nombre character varying(100),
    clave character varying,
    identidadbase integer,
    etiqueta character varying(100),
    codigo character(10) NOT NULL,
    identidadoriginal integer,
    bsuspendida boolean DEFAULT false NOT NULL,
    orden integer DEFAULT 1 NOT NULL
);


ALTER TABLE refundido.entidad OWNER TO postgres;

--
-- TOC entry 3044 (class 1259 OID 60205)
-- Dependencies: 14
-- Name: refundido_entidaddeterminacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidaddeterminacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidaddeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3738 (class 0 OID 0)
-- Dependencies: 3044
-- Name: refundido_entidaddeterminacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_entidaddeterminacion_iden_seq', 1, false);


--
-- TOC entry 3045 (class 1259 OID 60207)
-- Dependencies: 3444 14
-- Name: entidaddeterminacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE entidaddeterminacion (
    iden integer DEFAULT nextval('refundido_entidaddeterminacion_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    iddeterminacion integer NOT NULL
);


ALTER TABLE refundido.entidaddeterminacion OWNER TO postgres;

--
-- TOC entry 3046 (class 1259 OID 60211)
-- Dependencies: 14
-- Name: refundido_entidaddeterminacionregimen_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidaddeterminacionregimen_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidaddeterminacionregimen_iden_seq OWNER TO postgres;

--
-- TOC entry 3739 (class 0 OID 0)
-- Dependencies: 3046
-- Name: refundido_entidaddeterminacionregimen_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_entidaddeterminacionregimen_iden_seq', 1, false);


--
-- TOC entry 3047 (class 1259 OID 60213)
-- Dependencies: 3445 14
-- Name: entidaddeterminacionregimen; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE entidaddeterminacionregimen (
    iden integer DEFAULT nextval('refundido_entidaddeterminacionregimen_iden_seq'::regclass) NOT NULL,
    idcaso integer NOT NULL,
    iddeterminacionregimen integer,
    idopciondeterminacion integer,
    valor character varying(50),
    superposicion integer,
    idcasoaplicacion integer
);


ALTER TABLE refundido.entidaddeterminacionregimen OWNER TO postgres;

--
-- TOC entry 3048 (class 1259 OID 60217)
-- Dependencies: 14
-- Name: refundido_entidadlin_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidadlin_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidadlin_iden_seq OWNER TO postgres;

--
-- TOC entry 3740 (class 0 OID 0)
-- Dependencies: 3048
-- Name: refundido_entidadlin_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_entidadlin_iden_seq', 1, false);


--
-- TOC entry 3049 (class 1259 OID 60219)
-- Dependencies: 3446 3447 3448 3449 3450 14 1041
-- Name: entidadlin; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE entidadlin (
    identidad integer,
    iden integer DEFAULT nextval('refundido_entidadlin_iden_seq'::regclass) NOT NULL,
    geom public.geometry,
    bsuspendida boolean DEFAULT false NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK ((((public.geometrytype(geom) = 'LINESTRING'::text) OR (public.geometrytype(geom) = 'MULTILINESTRING'::text)) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.srid(geom) = (-1)))
);


ALTER TABLE refundido.entidadlin OWNER TO postgres;

--
-- TOC entry 3050 (class 1259 OID 60230)
-- Dependencies: 14
-- Name: refundido_entidadpnt_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidadpnt_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidadpnt_iden_seq OWNER TO postgres;

--
-- TOC entry 3741 (class 0 OID 0)
-- Dependencies: 3050
-- Name: refundido_entidadpnt_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_entidadpnt_iden_seq', 1, false);


--
-- TOC entry 3051 (class 1259 OID 60232)
-- Dependencies: 3451 3452 3453 3454 3455 14 1041
-- Name: entidadpnt; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE entidadpnt (
    identidad integer NOT NULL,
    iden integer DEFAULT nextval('refundido_entidadpnt_iden_seq'::regclass) NOT NULL,
    geom public.geometry,
    bsuspendida boolean DEFAULT false NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK ((((public.geometrytype(geom) = 'MULTIPOINT'::text) OR (public.geometrytype(geom) = 'POINT'::text)) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.srid(geom) = (-1)))
);


ALTER TABLE refundido.entidadpnt OWNER TO postgres;

--
-- TOC entry 3052 (class 1259 OID 60243)
-- Dependencies: 14
-- Name: refundido_entidadpol_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidadpol_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidadpol_iden_seq OWNER TO postgres;

--
-- TOC entry 3742 (class 0 OID 0)
-- Dependencies: 3052
-- Name: refundido_entidadpol_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_entidadpol_iden_seq', 1, false);


--
-- TOC entry 3053 (class 1259 OID 60245)
-- Dependencies: 3456 3457 3458 3459 3460 14 1041
-- Name: entidadpol; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE entidadpol (
    identidad integer NOT NULL,
    iden integer DEFAULT nextval('refundido_entidadpol_iden_seq'::regclass) NOT NULL,
    geom public.geometry,
    bsuspendida boolean DEFAULT false NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK ((((public.geometrytype(geom) = 'MULTIPOLYGON'::text) OR (public.geometrytype(geom) = 'POLYGON'::text)) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.srid(geom) = (-1)))
);


ALTER TABLE refundido.entidadpol OWNER TO postgres;

--
-- TOC entry 3054 (class 1259 OID 60256)
-- Dependencies: 14
-- Name: refundido_opciondeterminacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_opciondeterminacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_opciondeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3743 (class 0 OID 0)
-- Dependencies: 3054
-- Name: refundido_opciondeterminacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_opciondeterminacion_iden_seq', 1, false);


--
-- TOC entry 3055 (class 1259 OID 60258)
-- Dependencies: 3461 14
-- Name: opciondeterminacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE opciondeterminacion (
    iden integer DEFAULT nextval('refundido_opciondeterminacion_iden_seq'::regclass) NOT NULL,
    iddeterminacion integer NOT NULL,
    iddeterminacionvalorref integer NOT NULL
);


ALTER TABLE refundido.opciondeterminacion OWNER TO postgres;

--
-- TOC entry 3056 (class 1259 OID 60262)
-- Dependencies: 14
-- Name: refundido_operacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_operacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_operacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3744 (class 0 OID 0)
-- Dependencies: 3056
-- Name: refundido_operacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_operacion_iden_seq', 1, false);


--
-- TOC entry 3057 (class 1259 OID 60264)
-- Dependencies: 3462 14
-- Name: operacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE operacion (
    iden integer DEFAULT nextval('refundido_operacion_iden_seq'::regclass) NOT NULL,
    texto text,
    orden integer NOT NULL,
    idtramiteordenante integer NOT NULL
);


ALTER TABLE refundido.operacion OWNER TO postgres;

--
-- TOC entry 3058 (class 1259 OID 60271)
-- Dependencies: 14
-- Name: refundido_operaciondeterminacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_operaciondeterminacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_operaciondeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3745 (class 0 OID 0)
-- Dependencies: 3058
-- Name: refundido_operaciondeterminacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_operaciondeterminacion_iden_seq', 1, false);


--
-- TOC entry 3059 (class 1259 OID 60273)
-- Dependencies: 3463 14
-- Name: operaciondeterminacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE operaciondeterminacion (
    iden integer DEFAULT nextval('refundido_operaciondeterminacion_iden_seq'::regclass) NOT NULL,
    iddeterminacion integer NOT NULL,
    iddeterminacionoperadora integer NOT NULL,
    idtipooperaciondet integer NOT NULL,
    idoperacion integer NOT NULL
);


ALTER TABLE refundido.operaciondeterminacion OWNER TO postgres;

--
-- TOC entry 3060 (class 1259 OID 60277)
-- Dependencies: 14
-- Name: refundido_operacionentidad_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_operacionentidad_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_operacionentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 3746 (class 0 OID 0)
-- Dependencies: 3060
-- Name: refundido_operacionentidad_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_operacionentidad_iden_seq', 1, false);


--
-- TOC entry 3061 (class 1259 OID 60279)
-- Dependencies: 3464 14
-- Name: operacionentidad; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE operacionentidad (
    iden integer DEFAULT nextval('refundido_operacionentidad_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    identidadoperadora integer NOT NULL,
    idtipooperacionent integer NOT NULL,
    idoperacion integer NOT NULL
);


ALTER TABLE refundido.operacionentidad OWNER TO postgres;

--
-- TOC entry 3062 (class 1259 OID 60283)
-- Dependencies: 14
-- Name: refundido_operacionplan_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_operacionplan_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_operacionplan_iden_seq OWNER TO postgres;

--
-- TOC entry 3747 (class 0 OID 0)
-- Dependencies: 3062
-- Name: refundido_operacionplan_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_operacionplan_iden_seq', 1, false);


--
-- TOC entry 3063 (class 1259 OID 60285)
-- Dependencies: 3465 14
-- Name: operacionplan; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE operacionplan (
    iden integer DEFAULT nextval('refundido_operacionplan_iden_seq'::regclass) NOT NULL,
    idplanoperado integer,
    idinstrumentotipooperacion integer NOT NULL,
    idplanoperador integer NOT NULL
);


ALTER TABLE refundido.operacionplan OWNER TO postgres;

--
-- TOC entry 3064 (class 1259 OID 60289)
-- Dependencies: 14
-- Name: refundido_operacionrelacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_operacionrelacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_operacionrelacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3748 (class 0 OID 0)
-- Dependencies: 3064
-- Name: refundido_operacionrelacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_operacionrelacion_iden_seq', 1, false);


--
-- TOC entry 3065 (class 1259 OID 60291)
-- Dependencies: 3466 14
-- Name: operacionrelacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE operacionrelacion (
    iden integer DEFAULT nextval('refundido_operacionrelacion_iden_seq'::regclass) NOT NULL,
    idrelacion integer NOT NULL,
    idtipooperacionrel integer NOT NULL,
    idoperacion integer NOT NULL
);


ALTER TABLE refundido.operacionrelacion OWNER TO postgres;

--
-- TOC entry 3066 (class 1259 OID 60295)
-- Dependencies: 14
-- Name: refundido_plan_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_plan_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_plan_iden_seq OWNER TO postgres;

--
-- TOC entry 3749 (class 0 OID 0)
-- Dependencies: 3066
-- Name: refundido_plan_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_plan_iden_seq', 1, false);


--
-- TOC entry 3067 (class 1259 OID 60297)
-- Dependencies: 3467 3468 3469 14
-- Name: plan; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE plan (
    iden integer DEFAULT nextval('refundido_plan_iden_seq'::regclass) NOT NULL,
    idpadre integer,
    nombre character varying(100) NOT NULL,
    codigo character(5) NOT NULL,
    texto text,
    idplanbase integer,
    idambito integer NOT NULL,
    bsuspendido boolean DEFAULT false NOT NULL,
    orden integer DEFAULT 0 NOT NULL
);


ALTER TABLE refundido.plan OWNER TO postgres;

--
-- TOC entry 3068 (class 1259 OID 60306)
-- Dependencies: 14
-- Name: refundido_proceso_sequence; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_proceso_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_proceso_sequence OWNER TO postgres;

--
-- TOC entry 3750 (class 0 OID 0)
-- Dependencies: 3068
-- Name: refundido_proceso_sequence; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_proceso_sequence', 1, false);


--
-- TOC entry 3069 (class 1259 OID 60308)
-- Dependencies: 3470 14
-- Name: proceso; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE proceso (
    iden integer DEFAULT nextval('refundido_proceso_sequence'::regclass) NOT NULL,
    usuario integer NOT NULL,
    ambito integer NOT NULL,
    inicio timestamp with time zone NOT NULL,
    fin timestamp with time zone,
    correcto boolean
);


ALTER TABLE refundido.proceso OWNER TO postgres;

--
-- TOC entry 3070 (class 1259 OID 60312)
-- Dependencies: 14
-- Name: refundido_propiedadrelacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_propiedadrelacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_propiedadrelacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3751 (class 0 OID 0)
-- Dependencies: 3070
-- Name: refundido_propiedadrelacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_propiedadrelacion_iden_seq', 1, false);


--
-- TOC entry 3071 (class 1259 OID 60314)
-- Dependencies: 3471 14
-- Name: propiedadrelacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE propiedadrelacion (
    iden integer DEFAULT nextval('refundido_propiedadrelacion_iden_seq'::regclass) NOT NULL,
    idrelacion integer NOT NULL,
    iddefpropiedad integer NOT NULL,
    valor text NOT NULL
);


ALTER TABLE refundido.propiedadrelacion OWNER TO postgres;

--
-- TOC entry 3072 (class 1259 OID 60321)
-- Dependencies: 14
-- Name: refundido_planentidadordenacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_planentidadordenacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_planentidadordenacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3752 (class 0 OID 0)
-- Dependencies: 3072
-- Name: refundido_planentidadordenacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_planentidadordenacion_iden_seq', 1, false);


--
-- TOC entry 3073 (class 1259 OID 60323)
-- Dependencies: 14
-- Name: refundido_regimenespecifico_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_regimenespecifico_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_regimenespecifico_iden_seq OWNER TO postgres;

--
-- TOC entry 3753 (class 0 OID 0)
-- Dependencies: 3073
-- Name: refundido_regimenespecifico_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_regimenespecifico_iden_seq', 1, false);


--
-- TOC entry 3074 (class 1259 OID 60325)
-- Dependencies: 14
-- Name: refundido_relacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_relacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_relacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3754 (class 0 OID 0)
-- Dependencies: 3074
-- Name: refundido_relacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_relacion_iden_seq', 1, false);


--
-- TOC entry 3075 (class 1259 OID 60327)
-- Dependencies: 14
-- Name: refundido_tramite_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_tramite_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_tramite_iden_seq OWNER TO postgres;

--
-- TOC entry 3755 (class 0 OID 0)
-- Dependencies: 3075
-- Name: refundido_tramite_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_tramite_iden_seq', 1, false);


--
-- TOC entry 3076 (class 1259 OID 60329)
-- Dependencies: 14
-- Name: refundido_vectorrelacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_vectorrelacion_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_vectorrelacion_iden_seq OWNER TO postgres;

--
-- TOC entry 3756 (class 0 OID 0)
-- Dependencies: 3076
-- Name: refundido_vectorrelacion_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_vectorrelacion_iden_seq', 1, false);


--
-- TOC entry 3077 (class 1259 OID 60331)
-- Dependencies: 14
-- Name: refundido_vinculocaso_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_vinculocaso_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_vinculocaso_iden_seq OWNER TO postgres;

--
-- TOC entry 3757 (class 0 OID 0)
-- Dependencies: 3077
-- Name: refundido_vinculocaso_iden_seq; Type: SEQUENCE SET; Schema: refundido; Owner: postgres
--

SELECT pg_catalog.setval('refundido_vinculocaso_iden_seq', 1, false);


--
-- TOC entry 3078 (class 1259 OID 60333)
-- Dependencies: 3472 14
-- Name: regimenespecifico; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE regimenespecifico (
    iden integer DEFAULT nextval('refundido_regimenespecifico_iden_seq'::regclass) NOT NULL,
    identidaddeterminacionregimen integer NOT NULL,
    idpadre integer,
    orden integer NOT NULL,
    nombre character varying(100),
    texto text
);


ALTER TABLE refundido.regimenespecifico OWNER TO postgres;

--
-- TOC entry 3079 (class 1259 OID 60340)
-- Dependencies: 3473 14
-- Name: relacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE relacion (
    iden integer DEFAULT nextval('refundido_relacion_iden_seq'::regclass) NOT NULL,
    iddefrelacion integer NOT NULL,
    idtramitecreador integer NOT NULL,
    idtramiteborrador integer
);


ALTER TABLE refundido.relacion OWNER TO postgres;

--
-- TOC entry 3080 (class 1259 OID 60344)
-- Dependencies: 3474 3475 14
-- Name: tramite; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE tramite (
    iden integer DEFAULT nextval('refundido_tramite_iden_seq'::regclass) NOT NULL,
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
    codigofip character(32) NOT NULL,
    iteracion integer DEFAULT 1 NOT NULL,
    fechaconsolidacion date
);


ALTER TABLE refundido.tramite OWNER TO postgres;

--
-- TOC entry 3081 (class 1259 OID 60352)
-- Dependencies: 14
-- Name: traza; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE traza (
    idproceso integer NOT NULL,
    tabla character varying(256) NOT NULL,
    idplaneamiento integer NOT NULL,
    idrefundido integer NOT NULL
);


ALTER TABLE refundido.traza OWNER TO postgres;

--
-- TOC entry 3082 (class 1259 OID 60355)
-- Dependencies: 3476 3477 14
-- Name: vectorrelacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE vectorrelacion (
    iden integer DEFAULT nextval('refundido_vectorrelacion_iden_seq'::regclass) NOT NULL,
    idrelacion integer NOT NULL,
    iddefvector integer NOT NULL,
    valor integer DEFAULT 0 NOT NULL
);


ALTER TABLE refundido.vectorrelacion OWNER TO postgres;

--
-- TOC entry 3083 (class 1259 OID 60360)
-- Dependencies: 3478 14
-- Name: vinculocaso; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE vinculocaso (
    iden integer DEFAULT nextval('refundido_vinculocaso_iden_seq'::regclass) NOT NULL,
    idcaso integer NOT NULL,
    idcasovinculado integer NOT NULL
);


ALTER TABLE refundido.vinculocaso OWNER TO postgres;

--
-- TOC entry 3694 (class 0 OID 60118)
-- Dependencies: 3023
-- Data for Name: ambitoaplicacionambito; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3695 (class 0 OID 60124)
-- Dependencies: 3025
-- Data for Name: archivo; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3696 (class 0 OID 60133)
-- Dependencies: 3027
-- Data for Name: casoentidaddeterminacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3697 (class 0 OID 60140)
-- Dependencies: 3029
-- Data for Name: determinacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3698 (class 0 OID 60151)
-- Dependencies: 3031
-- Data for Name: determinaciongrupoentidad; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3699 (class 0 OID 60157)
-- Dependencies: 3033
-- Data for Name: documento; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3700 (class 0 OID 60166)
-- Dependencies: 3035
-- Data for Name: documentocaso; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3701 (class 0 OID 60172)
-- Dependencies: 3037
-- Data for Name: documentodeterminacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3702 (class 0 OID 60178)
-- Dependencies: 3039
-- Data for Name: documentoentidad; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3703 (class 0 OID 60184)
-- Dependencies: 3041
-- Data for Name: documentoshp; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3704 (class 0 OID 60196)
-- Dependencies: 3043
-- Data for Name: entidad; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3705 (class 0 OID 60207)
-- Dependencies: 3045
-- Data for Name: entidaddeterminacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3706 (class 0 OID 60213)
-- Dependencies: 3047
-- Data for Name: entidaddeterminacionregimen; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3707 (class 0 OID 60219)
-- Dependencies: 3049
-- Data for Name: entidadlin; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3708 (class 0 OID 60232)
-- Dependencies: 3051
-- Data for Name: entidadpnt; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3709 (class 0 OID 60245)
-- Dependencies: 3053
-- Data for Name: entidadpol; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3710 (class 0 OID 60258)
-- Dependencies: 3055
-- Data for Name: opciondeterminacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3711 (class 0 OID 60264)
-- Dependencies: 3057
-- Data for Name: operacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3712 (class 0 OID 60273)
-- Dependencies: 3059
-- Data for Name: operaciondeterminacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3713 (class 0 OID 60279)
-- Dependencies: 3061
-- Data for Name: operacionentidad; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3714 (class 0 OID 60285)
-- Dependencies: 3063
-- Data for Name: operacionplan; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3715 (class 0 OID 60291)
-- Dependencies: 3065
-- Data for Name: operacionrelacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3716 (class 0 OID 60297)
-- Dependencies: 3067
-- Data for Name: plan; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3717 (class 0 OID 60308)
-- Dependencies: 3069
-- Data for Name: proceso; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3718 (class 0 OID 60314)
-- Dependencies: 3071
-- Data for Name: propiedadrelacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3719 (class 0 OID 60333)
-- Dependencies: 3078
-- Data for Name: regimenespecifico; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3720 (class 0 OID 60340)
-- Dependencies: 3079
-- Data for Name: relacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3721 (class 0 OID 60344)
-- Dependencies: 3080
-- Data for Name: tramite; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3722 (class 0 OID 60352)
-- Dependencies: 3081
-- Data for Name: traza; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3723 (class 0 OID 60355)
-- Dependencies: 3082
-- Data for Name: vectorrelacion; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3724 (class 0 OID 60360)
-- Dependencies: 3083
-- Data for Name: vinculocaso; Type: TABLE DATA; Schema: refundido; Owner: postgres
--



--
-- TOC entry 3482 (class 2606 OID 60658)
-- Dependencies: 3023 3023
-- Name: pk_ambitoaplicacionambito; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ambitoaplicacionambito
    ADD CONSTRAINT pk_ambitoaplicacionambito PRIMARY KEY (iden);


--
-- TOC entry 3484 (class 2606 OID 60660)
-- Dependencies: 3025 3025
-- Name: pk_archivo; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY archivo
    ADD CONSTRAINT pk_archivo PRIMARY KEY (iden);


--
-- TOC entry 3488 (class 2606 OID 60662)
-- Dependencies: 3027 3027
-- Name: pk_casoentidaddeterminacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY casoentidaddeterminacion
    ADD CONSTRAINT pk_casoentidaddeterminacion PRIMARY KEY (iden);


--
-- TOC entry 3498 (class 2606 OID 60664)
-- Dependencies: 3029 3029
-- Name: pk_determinacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT pk_determinacion PRIMARY KEY (iden);


--
-- TOC entry 3502 (class 2606 OID 60666)
-- Dependencies: 3031 3031
-- Name: pk_dge; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY determinaciongrupoentidad
    ADD CONSTRAINT pk_dge PRIMARY KEY (iden);


--
-- TOC entry 3508 (class 2606 OID 60668)
-- Dependencies: 3033 3033
-- Name: pk_doc; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT pk_doc PRIMARY KEY (iden);


--
-- TOC entry 3512 (class 2606 OID 60670)
-- Dependencies: 3035 3035
-- Name: pk_documentocaso; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentocaso
    ADD CONSTRAINT pk_documentocaso PRIMARY KEY (iden);


--
-- TOC entry 3516 (class 2606 OID 60672)
-- Dependencies: 3037 3037
-- Name: pk_documentodeterminacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentodeterminacion
    ADD CONSTRAINT pk_documentodeterminacion PRIMARY KEY (iden);


--
-- TOC entry 3520 (class 2606 OID 60674)
-- Dependencies: 3039 3039
-- Name: pk_documentoentidad; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentoentidad
    ADD CONSTRAINT pk_documentoentidad PRIMARY KEY (iden);


--
-- TOC entry 3524 (class 2606 OID 60676)
-- Dependencies: 3041 3041
-- Name: pk_documentoshp; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentoshp
    ADD CONSTRAINT pk_documentoshp PRIMARY KEY (iden);


--
-- TOC entry 3542 (class 2606 OID 60678)
-- Dependencies: 3047 3047
-- Name: pk_edr; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT pk_edr PRIMARY KEY (iden);


--
-- TOC entry 3532 (class 2606 OID 60680)
-- Dependencies: 3043 3043
-- Name: pk_entidad; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT pk_entidad PRIMARY KEY (iden);


--
-- TOC entry 3536 (class 2606 OID 60682)
-- Dependencies: 3045 3045
-- Name: pk_entidaddeterminacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidaddeterminacion
    ADD CONSTRAINT pk_entidaddeterminacion PRIMARY KEY (iden);


--
-- TOC entry 3546 (class 2606 OID 60684)
-- Dependencies: 3049 3049
-- Name: pk_entidadlin; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidadlin
    ADD CONSTRAINT pk_entidadlin PRIMARY KEY (iden);


--
-- TOC entry 3550 (class 2606 OID 60686)
-- Dependencies: 3051 3051
-- Name: pk_entidadpnt; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidadpnt
    ADD CONSTRAINT pk_entidadpnt PRIMARY KEY (iden);


--
-- TOC entry 3554 (class 2606 OID 60688)
-- Dependencies: 3053 3053
-- Name: pk_entidadpol; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidadpol
    ADD CONSTRAINT pk_entidadpol PRIMARY KEY (iden);


--
-- TOC entry 3558 (class 2606 OID 60690)
-- Dependencies: 3055 3055
-- Name: pk_opciondeterminacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY opciondeterminacion
    ADD CONSTRAINT pk_opciondeterminacion PRIMARY KEY (iden);


--
-- TOC entry 3562 (class 2606 OID 60692)
-- Dependencies: 3057 3057
-- Name: pk_operacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacion
    ADD CONSTRAINT pk_operacion PRIMARY KEY (iden);


--
-- TOC entry 3568 (class 2606 OID 60694)
-- Dependencies: 3059 3059
-- Name: pk_operaciondeterminacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT pk_operaciondeterminacion PRIMARY KEY (iden);


--
-- TOC entry 3574 (class 2606 OID 60696)
-- Dependencies: 3061 3061
-- Name: pk_operacionentidad; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT pk_operacionentidad PRIMARY KEY (iden);


--
-- TOC entry 3579 (class 2606 OID 60698)
-- Dependencies: 3063 3063
-- Name: pk_operacionplan; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT pk_operacionplan PRIMARY KEY (iden);


--
-- TOC entry 3584 (class 2606 OID 60700)
-- Dependencies: 3065 3065
-- Name: pk_operacionrelacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT pk_operacionrelacion PRIMARY KEY (iden);


--
-- TOC entry 3591 (class 2606 OID 60702)
-- Dependencies: 3067 3067
-- Name: pk_plan; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT pk_plan PRIMARY KEY (iden);


--
-- TOC entry 3593 (class 2606 OID 60704)
-- Dependencies: 3069 3069
-- Name: pk_proceso; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY proceso
    ADD CONSTRAINT pk_proceso PRIMARY KEY (iden);


--
-- TOC entry 3597 (class 2606 OID 60706)
-- Dependencies: 3071 3071
-- Name: pk_propiedadrelacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY propiedadrelacion
    ADD CONSTRAINT pk_propiedadrelacion PRIMARY KEY (iden);


--
-- TOC entry 3602 (class 2606 OID 60708)
-- Dependencies: 3078 3078
-- Name: pk_regimenespecifico; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY regimenespecifico
    ADD CONSTRAINT pk_regimenespecifico PRIMARY KEY (iden);


--
-- TOC entry 3606 (class 2606 OID 60710)
-- Dependencies: 3079 3079
-- Name: pk_rl; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY relacion
    ADD CONSTRAINT pk_rl PRIMARY KEY (iden);


--
-- TOC entry 3614 (class 2606 OID 60712)
-- Dependencies: 3080 3080
-- Name: pk_tramite; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tramite
    ADD CONSTRAINT pk_tramite PRIMARY KEY (iden);


--
-- TOC entry 3621 (class 2606 OID 60714)
-- Dependencies: 3082 3082
-- Name: pk_vectorrelacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vectorrelacion
    ADD CONSTRAINT pk_vectorrelacion PRIMARY KEY (iden);


--
-- TOC entry 3625 (class 2606 OID 60716)
-- Dependencies: 3083 3083
-- Name: pk_vinculocaso; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vinculocaso
    ADD CONSTRAINT pk_vinculocaso PRIMARY KEY (iden);


--
-- TOC entry 3616 (class 2606 OID 60718)
-- Dependencies: 3081 3081 3081 3081
-- Name: traza_pk; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY traza
    ADD CONSTRAINT traza_pk PRIMARY KEY (idproceso, tabla, idplaneamiento);


--
-- TOC entry 3603 (class 1259 OID 60864)
-- Dependencies: 3079
-- Name: fki_relacion_idtramiteborrador; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_relacion_idtramiteborrador ON relacion USING btree (idtramiteborrador);


--
-- TOC entry 3604 (class 1259 OID 60865)
-- Dependencies: 3079
-- Name: fki_relacion_idtramitecreador; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_relacion_idtramitecreador ON relacion USING btree (idtramitecreador);


--
-- TOC entry 3479 (class 1259 OID 60866)
-- Dependencies: 3023
-- Name: idx_ambitoaplicacionambito_idambito; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_ambitoaplicacionambito_idambito ON ambitoaplicacionambito USING btree (idambito);


--
-- TOC entry 3480 (class 1259 OID 60867)
-- Dependencies: 3023
-- Name: idx_ambitoaplicacionambito_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_ambitoaplicacionambito_identidad ON ambitoaplicacionambito USING btree (identidad);


--
-- TOC entry 3485 (class 1259 OID 60868)
-- Dependencies: 3027
-- Name: idx_casoentidaddeterminacion_identidaddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_casoentidaddeterminacion_identidaddeterminacion ON casoentidaddeterminacion USING btree (identidaddeterminacion);


--
-- TOC entry 3486 (class 1259 OID 60869)
-- Dependencies: 3027
-- Name: idx_casoentidaddeterminacion_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_casoentidaddeterminacion_orden ON casoentidaddeterminacion USING btree (orden);


--
-- TOC entry 3489 (class 1259 OID 60870)
-- Dependencies: 3029
-- Name: idx_determinacion_apartado; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_apartado ON determinacion USING btree (apartado);


--
-- TOC entry 3490 (class 1259 OID 60871)
-- Dependencies: 3029
-- Name: idx_determinacion_codigo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_codigo ON determinacion USING btree (codigo);


--
-- TOC entry 3491 (class 1259 OID 60872)
-- Dependencies: 3029
-- Name: idx_determinacion_idcaracter; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_idcaracter ON determinacion USING btree (idcaracter);


--
-- TOC entry 3492 (class 1259 OID 60873)
-- Dependencies: 3029
-- Name: idx_determinacion_iddeterminacionbase; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_iddeterminacionbase ON determinacion USING btree (iddeterminacionbase);


--
-- TOC entry 3493 (class 1259 OID 60874)
-- Dependencies: 3029
-- Name: idx_determinacion_iddeterminacionoriginal; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_iddeterminacionoriginal ON determinacion USING btree (iddeterminacionoriginal);


--
-- TOC entry 3494 (class 1259 OID 60875)
-- Dependencies: 3029
-- Name: idx_determinacion_idpadre; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_idpadre ON determinacion USING btree (idpadre);


--
-- TOC entry 3495 (class 1259 OID 60876)
-- Dependencies: 3029
-- Name: idx_determinacion_idtramite; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_idtramite ON determinacion USING btree (idtramite);


--
-- TOC entry 3496 (class 1259 OID 60877)
-- Dependencies: 3029
-- Name: idx_determinacion_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_orden ON determinacion USING btree (orden);


--
-- TOC entry 3499 (class 1259 OID 60878)
-- Dependencies: 3031
-- Name: idx_determinaciongrupoentidad_iddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinaciongrupoentidad_iddeterminacion ON determinaciongrupoentidad USING btree (iddeterminacion);


--
-- TOC entry 3500 (class 1259 OID 60879)
-- Dependencies: 3031
-- Name: idx_determinaciongrupoentidad_iddeterminaciongrupo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinaciongrupoentidad_iddeterminaciongrupo ON determinaciongrupoentidad USING btree (iddeterminaciongrupo);


--
-- TOC entry 3503 (class 1259 OID 60880)
-- Dependencies: 3033
-- Name: idx_documento_iddocumentooriginal; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documento_iddocumentooriginal ON documento USING btree (iddocumentooriginal);


--
-- TOC entry 3504 (class 1259 OID 60881)
-- Dependencies: 3033
-- Name: idx_documento_idgrupo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documento_idgrupo ON documento USING btree (idgrupodocumento);


--
-- TOC entry 3505 (class 1259 OID 60882)
-- Dependencies: 3033
-- Name: idx_documento_idtipo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documento_idtipo ON documento USING btree (idtipodocumento);


--
-- TOC entry 3506 (class 1259 OID 60883)
-- Dependencies: 3033
-- Name: idx_documento_idtramite; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documento_idtramite ON documento USING btree (idtramite);


--
-- TOC entry 3509 (class 1259 OID 60884)
-- Dependencies: 3035
-- Name: idx_documentocaso_idcaso; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentocaso_idcaso ON documentocaso USING btree (idcaso);


--
-- TOC entry 3510 (class 1259 OID 60885)
-- Dependencies: 3035
-- Name: idx_documentocaso_iddocumento; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentocaso_iddocumento ON documentocaso USING btree (iddocumento);


--
-- TOC entry 3513 (class 1259 OID 60886)
-- Dependencies: 3037
-- Name: idx_documentodeterminacion_iddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentodeterminacion_iddeterminacion ON documentodeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3514 (class 1259 OID 60887)
-- Dependencies: 3037
-- Name: idx_documentodeterminacion_iddocumento; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentodeterminacion_iddocumento ON documentodeterminacion USING btree (iddocumento);


--
-- TOC entry 3517 (class 1259 OID 60888)
-- Dependencies: 3039
-- Name: idx_documentoentidad_iddocumento; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentoentidad_iddocumento ON documentoentidad USING btree (iddocumento);


--
-- TOC entry 3518 (class 1259 OID 60889)
-- Dependencies: 3039
-- Name: idx_documentoentidad_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentoentidad_identidad ON documentoentidad USING btree (identidad);


--
-- TOC entry 3521 (class 1259 OID 60890)
-- Dependencies: 2432 3041
-- Name: idx_documentoshp_geom; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentoshp_geom ON documentoshp USING gist (geom);


--
-- TOC entry 3522 (class 1259 OID 60891)
-- Dependencies: 3041
-- Name: idx_documentoshp_iddocumento; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentoshp_iddocumento ON documentoshp USING btree (iddocumento);


--
-- TOC entry 3525 (class 1259 OID 60892)
-- Dependencies: 3043
-- Name: idx_entidad_codigo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_codigo ON entidad USING btree (codigo);


--
-- TOC entry 3526 (class 1259 OID 60893)
-- Dependencies: 3043
-- Name: idx_entidad_identidadbase; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_identidadbase ON entidad USING btree (identidadbase);


--
-- TOC entry 3527 (class 1259 OID 60894)
-- Dependencies: 3043
-- Name: idx_entidad_identidadoriginal; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_identidadoriginal ON entidad USING btree (identidadoriginal);


--
-- TOC entry 3528 (class 1259 OID 60895)
-- Dependencies: 3043
-- Name: idx_entidad_idpadre; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_idpadre ON entidad USING btree (idpadre);


--
-- TOC entry 3529 (class 1259 OID 60896)
-- Dependencies: 3043
-- Name: idx_entidad_idtramite; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_idtramite ON entidad USING btree (idtramite);


--
-- TOC entry 3530 (class 1259 OID 60897)
-- Dependencies: 3043
-- Name: idx_entidad_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_orden ON entidad USING btree (orden);


--
-- TOC entry 3533 (class 1259 OID 60898)
-- Dependencies: 3045
-- Name: idx_entidaddeterminacion_iddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacion_iddeterminacion ON entidaddeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3534 (class 1259 OID 60899)
-- Dependencies: 3045
-- Name: idx_entidaddeterminacion_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacion_identidad ON entidaddeterminacion USING btree (identidad);


--
-- TOC entry 3537 (class 1259 OID 60900)
-- Dependencies: 3047
-- Name: idx_entidaddeterminacionregimen_idcaso; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacionregimen_idcaso ON entidaddeterminacionregimen USING btree (idcaso);


--
-- TOC entry 3538 (class 1259 OID 60901)
-- Dependencies: 3047
-- Name: idx_entidaddeterminacionregimen_idcasoaplicacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacionregimen_idcasoaplicacion ON entidaddeterminacionregimen USING btree (idcasoaplicacion);


--
-- TOC entry 3539 (class 1259 OID 60902)
-- Dependencies: 3047
-- Name: idx_entidaddeterminacionregimen_iddeterminacionregimen; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacionregimen_iddeterminacionregimen ON entidaddeterminacionregimen USING btree (iddeterminacionregimen);


--
-- TOC entry 3540 (class 1259 OID 60903)
-- Dependencies: 3047
-- Name: idx_entidaddeterminacionregimen_idopciondeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacionregimen_idopciondeterminacion ON entidaddeterminacionregimen USING btree (idopciondeterminacion);


--
-- TOC entry 3543 (class 1259 OID 60904)
-- Dependencies: 2432 3049
-- Name: idx_entidadlin_geom; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadlin_geom ON entidadlin USING gist (geom);


--
-- TOC entry 3544 (class 1259 OID 60905)
-- Dependencies: 3049
-- Name: idx_entidadlin_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadlin_identidad ON entidadlin USING btree (identidad);


--
-- TOC entry 3547 (class 1259 OID 60906)
-- Dependencies: 2432 3051
-- Name: idx_entidadpnt_geom; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadpnt_geom ON entidadpnt USING gist (geom);


--
-- TOC entry 3548 (class 1259 OID 60907)
-- Dependencies: 3051
-- Name: idx_entidadpnt_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadpnt_identidad ON entidadpnt USING btree (identidad);


--
-- TOC entry 3551 (class 1259 OID 60908)
-- Dependencies: 2432 3053
-- Name: idx_entidadpol_geom; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadpol_geom ON entidadpol USING gist (geom);


--
-- TOC entry 3552 (class 1259 OID 60909)
-- Dependencies: 3053
-- Name: idx_entidadpol_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadpol_identidad ON entidadpol USING btree (identidad);


--
-- TOC entry 3555 (class 1259 OID 60910)
-- Dependencies: 3055
-- Name: idx_opciondeterminacion_iddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_opciondeterminacion_iddeterminacion ON opciondeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3556 (class 1259 OID 60911)
-- Dependencies: 3055
-- Name: idx_opciondeterminacion_iddeterminacionvalorref; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_opciondeterminacion_iddeterminacionvalorref ON opciondeterminacion USING btree (iddeterminacionvalorref);


--
-- TOC entry 3559 (class 1259 OID 60912)
-- Dependencies: 3057
-- Name: idx_operacion_idtramiteordenante; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacion_idtramiteordenante ON operacion USING btree (idtramiteordenante);


--
-- TOC entry 3560 (class 1259 OID 60913)
-- Dependencies: 3057
-- Name: idx_operacion_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacion_orden ON operacion USING btree (orden);


--
-- TOC entry 3563 (class 1259 OID 60914)
-- Dependencies: 3059
-- Name: idx_operaciondeterminacion_iddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operaciondeterminacion_iddeterminacion ON operaciondeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3564 (class 1259 OID 60915)
-- Dependencies: 3059
-- Name: idx_operaciondeterminacion_iddeterminacionoperadora; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operaciondeterminacion_iddeterminacionoperadora ON operaciondeterminacion USING btree (iddeterminacionoperadora);


--
-- TOC entry 3565 (class 1259 OID 60916)
-- Dependencies: 3059
-- Name: idx_operaciondeterminacion_idoperacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operaciondeterminacion_idoperacion ON operaciondeterminacion USING btree (idoperacion);


--
-- TOC entry 3566 (class 1259 OID 60917)
-- Dependencies: 3059
-- Name: idx_operaciondeterminacion_idtipooper; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operaciondeterminacion_idtipooper ON operaciondeterminacion USING btree (idtipooperaciondet);


--
-- TOC entry 3569 (class 1259 OID 60918)
-- Dependencies: 3061
-- Name: idx_operacionentidad_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionentidad_identidad ON operacionentidad USING btree (identidad);


--
-- TOC entry 3570 (class 1259 OID 60919)
-- Dependencies: 3061
-- Name: idx_operacionentidad_identidadoperadora; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionentidad_identidadoperadora ON operacionentidad USING btree (identidadoperadora);


--
-- TOC entry 3571 (class 1259 OID 60920)
-- Dependencies: 3061
-- Name: idx_operacionentidad_idoperacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionentidad_idoperacion ON operacionentidad USING btree (idoperacion);


--
-- TOC entry 3572 (class 1259 OID 60921)
-- Dependencies: 3061
-- Name: idx_operacionentidad_idtipooper; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionentidad_idtipooper ON operacionentidad USING btree (idtipooperacionent);


--
-- TOC entry 3575 (class 1259 OID 60922)
-- Dependencies: 3063
-- Name: idx_operacionplan_idinstrumenttipoop; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionplan_idinstrumenttipoop ON operacionplan USING btree (idinstrumentotipooperacion);


--
-- TOC entry 3576 (class 1259 OID 60923)
-- Dependencies: 3063
-- Name: idx_operacionplan_idplanoperado; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionplan_idplanoperado ON operacionplan USING btree (idplanoperado);


--
-- TOC entry 3577 (class 1259 OID 60924)
-- Dependencies: 3063
-- Name: idx_operacionplan_idplanoperador; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionplan_idplanoperador ON operacionplan USING btree (idplanoperador);


--
-- TOC entry 3580 (class 1259 OID 60925)
-- Dependencies: 3065
-- Name: idx_operacionrelacion_idoperacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionrelacion_idoperacion ON operacionrelacion USING btree (idoperacion);


--
-- TOC entry 3581 (class 1259 OID 60926)
-- Dependencies: 3065
-- Name: idx_operacionrelacion_idrelacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionrelacion_idrelacion ON operacionrelacion USING btree (idrelacion);


--
-- TOC entry 3582 (class 1259 OID 60927)
-- Dependencies: 3065
-- Name: idx_operacionrelacion_idtipoopera; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionrelacion_idtipoopera ON operacionrelacion USING btree (idtipooperacionrel);


--
-- TOC entry 3585 (class 1259 OID 60928)
-- Dependencies: 3067
-- Name: idx_plan_codigo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_plan_codigo ON plan USING btree (codigo);


--
-- TOC entry 3586 (class 1259 OID 60929)
-- Dependencies: 3067
-- Name: idx_plan_idambito; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_plan_idambito ON plan USING btree (idambito);


--
-- TOC entry 3587 (class 1259 OID 60930)
-- Dependencies: 3067
-- Name: idx_plan_idpadre; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_plan_idpadre ON plan USING btree (idpadre);


--
-- TOC entry 3588 (class 1259 OID 60931)
-- Dependencies: 3067
-- Name: idx_plan_idplanbase; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_plan_idplanbase ON plan USING btree (idplanbase);


--
-- TOC entry 3589 (class 1259 OID 60932)
-- Dependencies: 3067
-- Name: idx_plan_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_plan_orden ON plan USING btree (orden);


--
-- TOC entry 3594 (class 1259 OID 60933)
-- Dependencies: 3071
-- Name: idx_propiedadrelacion_iddefpropiedad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_propiedadrelacion_iddefpropiedad ON propiedadrelacion USING btree (iddefpropiedad);


--
-- TOC entry 3595 (class 1259 OID 60934)
-- Dependencies: 3071
-- Name: idx_propiedadrelacion_idrelacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_propiedadrelacion_idrelacion ON propiedadrelacion USING btree (idrelacion);


--
-- TOC entry 3598 (class 1259 OID 60935)
-- Dependencies: 3078
-- Name: idx_regimenespecifico_identidaddeterminacionregimen; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_regimenespecifico_identidaddeterminacionregimen ON regimenespecifico USING btree (identidaddeterminacionregimen);


--
-- TOC entry 3599 (class 1259 OID 60936)
-- Dependencies: 3078
-- Name: idx_regimenespecifico_idpadre; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_regimenespecifico_idpadre ON regimenespecifico USING btree (idpadre);


--
-- TOC entry 3600 (class 1259 OID 60937)
-- Dependencies: 3078
-- Name: idx_regimenespecifico_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_regimenespecifico_orden ON regimenespecifico USING btree (orden);


--
-- TOC entry 3608 (class 1259 OID 60938)
-- Dependencies: 3080
-- Name: idx_tramite_idcentroproduccion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_tramite_idcentroproduccion ON tramite USING btree (idcentroproduccion);


--
-- TOC entry 3609 (class 1259 OID 60939)
-- Dependencies: 3080
-- Name: idx_tramite_idplan; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_tramite_idplan ON tramite USING btree (idplan);


--
-- TOC entry 3610 (class 1259 OID 60940)
-- Dependencies: 3080
-- Name: idx_tramite_idtipotramite; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_tramite_idtipotramite ON tramite USING btree (idtipotramite);


--
-- TOC entry 3611 (class 1259 OID 60941)
-- Dependencies: 3080
-- Name: idx_tramite_organo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_tramite_organo ON tramite USING btree (idorgano);


--
-- TOC entry 3612 (class 1259 OID 60942)
-- Dependencies: 3080
-- Name: idx_tramite_sentido; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_tramite_sentido ON tramite USING btree (idsentido);


--
-- TOC entry 3617 (class 1259 OID 60943)
-- Dependencies: 3082
-- Name: idx_vectorrelacion_iddefvector; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_vectorrelacion_iddefvector ON vectorrelacion USING btree (iddefvector);


--
-- TOC entry 3618 (class 1259 OID 60944)
-- Dependencies: 3082
-- Name: idx_vectorrelacion_idrelacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_vectorrelacion_idrelacion ON vectorrelacion USING btree (idrelacion);


--
-- TOC entry 3619 (class 1259 OID 60945)
-- Dependencies: 3082
-- Name: idx_vectorrelacion_valor; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_vectorrelacion_valor ON vectorrelacion USING hash (valor);


--
-- TOC entry 3622 (class 1259 OID 60946)
-- Dependencies: 3083
-- Name: idx_vinculocaso_idcaso; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_vinculocaso_idcaso ON vinculocaso USING btree (idcaso);


--
-- TOC entry 3623 (class 1259 OID 60947)
-- Dependencies: 3083
-- Name: idx_vinculocaso_idcasovinculado; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_vinculocaso_idcasovinculado ON vinculocaso USING btree (idcasovinculado);


--
-- TOC entry 3607 (class 1259 OID 60948)
-- Dependencies: 3079
-- Name: relacion_idx_iddefrelacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX relacion_idx_iddefrelacion ON relacion USING btree (iddefrelacion);


--
-- TOC entry 3626 (class 2606 OID 62422)
-- Dependencies: 2826 3023
-- Name: ambitoaplicacionambito_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY ambitoaplicacionambito
    ADD CONSTRAINT ambitoaplicacionambito_fk FOREIGN KEY (idambito) REFERENCES diccionario.ambito(iden);


--
-- TOC entry 3630 (class 2606 OID 62427)
-- Dependencies: 2833 3029
-- Name: determinacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT determinacion_fk FOREIGN KEY (idcaracter) REFERENCES diccionario.caracterdeterminacion(iden);


--
-- TOC entry 3637 (class 2606 OID 62432)
-- Dependencies: 2876 3033
-- Name: documento_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT documento_fk FOREIGN KEY (idtipodocumento) REFERENCES diccionario.tipodocumento(iden);


--
-- TOC entry 3638 (class 2606 OID 62437)
-- Dependencies: 2863 3033
-- Name: documento_fk1; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT documento_fk1 FOREIGN KEY (idgrupodocumento) REFERENCES diccionario.grupodocumento(iden);


--
-- TOC entry 3627 (class 2606 OID 62442)
-- Dependencies: 3531 3043 3023
-- Name: fk_aaa_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY ambitoaplicacionambito
    ADD CONSTRAINT fk_aaa_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3681 (class 2606 OID 62447)
-- Dependencies: 2826 3069
-- Name: fk_ambito; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY proceso
    ADD CONSTRAINT fk_ambito FOREIGN KEY (ambito) REFERENCES diccionario.ambito(iden);


--
-- TOC entry 3629 (class 2606 OID 62452)
-- Dependencies: 3535 3045 3027
-- Name: fk_ced_identidaddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY casoentidaddeterminacion
    ADD CONSTRAINT fk_ced_identidaddeterminacion FOREIGN KEY (identidaddeterminacion) REFERENCES entidaddeterminacion(iden);


--
-- TOC entry 3631 (class 2606 OID 62457)
-- Dependencies: 3497 3029 3029
-- Name: fk_d_iddeterminacionbase; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_iddeterminacionbase FOREIGN KEY (iddeterminacionbase) REFERENCES determinacion(iden);


--
-- TOC entry 3632 (class 2606 OID 62462)
-- Dependencies: 3497 3029 3029
-- Name: fk_d_iddeterminacionoriginal; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_iddeterminacionoriginal FOREIGN KEY (iddeterminacionoriginal) REFERENCES determinacion(iden);


--
-- TOC entry 3633 (class 2606 OID 62467)
-- Dependencies: 3497 3029 3029
-- Name: fk_d_idpadre; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_idpadre FOREIGN KEY (idpadre) REFERENCES determinacion(iden);


--
-- TOC entry 3634 (class 2606 OID 62472)
-- Dependencies: 3613 3080 3029
-- Name: fk_d_idtramite; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_idtramite FOREIGN KEY (idtramite) REFERENCES tramite(iden);


--
-- TOC entry 3635 (class 2606 OID 62477)
-- Dependencies: 3497 3029 3031
-- Name: fk_dge_iddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinaciongrupoentidad
    ADD CONSTRAINT fk_dge_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3636 (class 2606 OID 62482)
-- Dependencies: 3497 3029 3031
-- Name: fk_dge_iddeterminaciongrupo; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinaciongrupoentidad
    ADD CONSTRAINT fk_dge_iddeterminaciongrupo FOREIGN KEY (iddeterminaciongrupo) REFERENCES determinacion(iden);


--
-- TOC entry 3639 (class 2606 OID 62487)
-- Dependencies: 3507 3033 3033
-- Name: fk_doc_iddocumentooriginal; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT fk_doc_iddocumentooriginal FOREIGN KEY (iddocumentooriginal) REFERENCES documento(iden);


--
-- TOC entry 3640 (class 2606 OID 62492)
-- Dependencies: 3613 3080 3033
-- Name: fk_doc_idtramite; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT fk_doc_idtramite FOREIGN KEY (idtramite) REFERENCES tramite(iden);


--
-- TOC entry 3641 (class 2606 OID 62497)
-- Dependencies: 3487 3027 3035
-- Name: fk_docc_idcaso; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentocaso
    ADD CONSTRAINT fk_docc_idcaso FOREIGN KEY (idcaso) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3642 (class 2606 OID 62502)
-- Dependencies: 3507 3033 3035
-- Name: fk_docc_iddocumento; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentocaso
    ADD CONSTRAINT fk_docc_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3643 (class 2606 OID 62507)
-- Dependencies: 3497 3029 3037
-- Name: fk_docd_iddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentodeterminacion
    ADD CONSTRAINT fk_docd_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3644 (class 2606 OID 62512)
-- Dependencies: 3507 3033 3037
-- Name: fk_docd_iddocumento; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentodeterminacion
    ADD CONSTRAINT fk_docd_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3645 (class 2606 OID 62517)
-- Dependencies: 3507 3033 3039
-- Name: fk_doce_iddocumento; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentoentidad
    ADD CONSTRAINT fk_doce_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3646 (class 2606 OID 62522)
-- Dependencies: 3531 3043 3039
-- Name: fk_doce_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentoentidad
    ADD CONSTRAINT fk_doce_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3647 (class 2606 OID 62527)
-- Dependencies: 3507 3033 3041
-- Name: fk_docshp_iddocumento; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentoshp
    ADD CONSTRAINT fk_docshp_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3648 (class 2606 OID 62532)
-- Dependencies: 3531 3043 3043
-- Name: fk_e_identidadbase; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_identidadbase FOREIGN KEY (identidadbase) REFERENCES entidad(iden);


--
-- TOC entry 3649 (class 2606 OID 62537)
-- Dependencies: 3531 3043 3043
-- Name: fk_e_identidadoriginal; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_identidadoriginal FOREIGN KEY (identidadoriginal) REFERENCES entidad(iden);


--
-- TOC entry 3650 (class 2606 OID 62542)
-- Dependencies: 3531 3043 3043
-- Name: fk_e_idpadre; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_idpadre FOREIGN KEY (idpadre) REFERENCES entidad(iden);


--
-- TOC entry 3651 (class 2606 OID 62547)
-- Dependencies: 3613 3080 3043
-- Name: fk_e_idtramite; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_idtramite FOREIGN KEY (idtramite) REFERENCES tramite(iden);


--
-- TOC entry 3652 (class 2606 OID 62552)
-- Dependencies: 3497 3029 3045
-- Name: fk_ed_iddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacion
    ADD CONSTRAINT fk_ed_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3653 (class 2606 OID 62557)
-- Dependencies: 3531 3043 3045
-- Name: fk_ed_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacion
    ADD CONSTRAINT fk_ed_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3654 (class 2606 OID 62562)
-- Dependencies: 3487 3027 3047
-- Name: fk_edr_idcaso; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_idcaso FOREIGN KEY (idcaso) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3655 (class 2606 OID 62567)
-- Dependencies: 3487 3027 3047
-- Name: fk_edr_idcasoaplicacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_idcasoaplicacion FOREIGN KEY (idcasoaplicacion) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3656 (class 2606 OID 62572)
-- Dependencies: 3497 3029 3047
-- Name: fk_edr_iddeterminacionregimen; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_iddeterminacionregimen FOREIGN KEY (iddeterminacionregimen) REFERENCES determinacion(iden);


--
-- TOC entry 3657 (class 2606 OID 62577)
-- Dependencies: 3557 3055 3047
-- Name: fk_edr_idopciondeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_idopciondeterminacion FOREIGN KEY (idopciondeterminacion) REFERENCES opciondeterminacion(iden);


--
-- TOC entry 3658 (class 2606 OID 62582)
-- Dependencies: 3531 3043 3049
-- Name: fk_elin_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidadlin
    ADD CONSTRAINT fk_elin_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3659 (class 2606 OID 62587)
-- Dependencies: 3531 3043 3051
-- Name: fk_epnt_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidadpnt
    ADD CONSTRAINT fk_epnt_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3660 (class 2606 OID 62592)
-- Dependencies: 3531 3043 3053
-- Name: fk_epol_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidadpol
    ADD CONSTRAINT fk_epol_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3664 (class 2606 OID 62597)
-- Dependencies: 3497 3029 3059
-- Name: fk_od_iddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT fk_od_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3665 (class 2606 OID 62602)
-- Dependencies: 3497 3029 3059
-- Name: fk_od_iddeterminacionoperadora; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT fk_od_iddeterminacionoperadora FOREIGN KEY (iddeterminacionoperadora) REFERENCES determinacion(iden);


--
-- TOC entry 3666 (class 2606 OID 62607)
-- Dependencies: 3561 3057 3059
-- Name: fk_od_idoperacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT fk_od_idoperacion FOREIGN KEY (idoperacion) REFERENCES operacion(iden);


--
-- TOC entry 3661 (class 2606 OID 62612)
-- Dependencies: 3497 3029 3055
-- Name: fk_odet_iddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY opciondeterminacion
    ADD CONSTRAINT fk_odet_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3662 (class 2606 OID 62617)
-- Dependencies: 3497 3029 3055
-- Name: fk_odet_iddetvalorref; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY opciondeterminacion
    ADD CONSTRAINT fk_odet_iddetvalorref FOREIGN KEY (iddeterminacionvalorref) REFERENCES determinacion(iden);


--
-- TOC entry 3668 (class 2606 OID 62622)
-- Dependencies: 3531 3043 3061
-- Name: fk_oe_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT fk_oe_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3669 (class 2606 OID 62627)
-- Dependencies: 3531 3043 3061
-- Name: fk_oe_identidadoperadora; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT fk_oe_identidadoperadora FOREIGN KEY (identidadoperadora) REFERENCES entidad(iden);


--
-- TOC entry 3670 (class 2606 OID 62632)
-- Dependencies: 3561 3057 3061
-- Name: fk_oe_idoperacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT fk_oe_idoperacion FOREIGN KEY (idoperacion) REFERENCES operacion(iden);


--
-- TOC entry 3663 (class 2606 OID 62637)
-- Dependencies: 3613 3080 3057
-- Name: fk_op_idtramiteordenante; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacion
    ADD CONSTRAINT fk_op_idtramiteordenante FOREIGN KEY (idtramiteordenante) REFERENCES tramite(iden);


--
-- TOC entry 3672 (class 2606 OID 62642)
-- Dependencies: 3590 3067 3063
-- Name: fk_opp_idplanoperado; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT fk_opp_idplanoperado FOREIGN KEY (idplanoperado) REFERENCES plan(iden);


--
-- TOC entry 3673 (class 2606 OID 62647)
-- Dependencies: 3590 3067 3063
-- Name: fk_opp_idplanoperador; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT fk_opp_idplanoperador FOREIGN KEY (idplanoperador) REFERENCES plan(iden);


--
-- TOC entry 3675 (class 2606 OID 62652)
-- Dependencies: 3561 3057 3065
-- Name: fk_or_idoperacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT fk_or_idoperacion FOREIGN KEY (idoperacion) REFERENCES operacion(iden);


--
-- TOC entry 3676 (class 2606 OID 62657)
-- Dependencies: 3605 3079 3065
-- Name: fk_or_idrelacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT fk_or_idrelacion FOREIGN KEY (idrelacion) REFERENCES relacion(iden);


--
-- TOC entry 3678 (class 2606 OID 62662)
-- Dependencies: 3590 3067 3067
-- Name: fk_p_idpadre; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT fk_p_idpadre FOREIGN KEY (idpadre) REFERENCES plan(iden);


--
-- TOC entry 3679 (class 2606 OID 62667)
-- Dependencies: 3590 3067 3067
-- Name: fk_p_idplanbase; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT fk_p_idplanbase FOREIGN KEY (idplanbase) REFERENCES plan(iden);


--
-- TOC entry 3682 (class 2606 OID 62672)
-- Dependencies: 3605 3079 3071
-- Name: fk_pr_idrelacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY propiedadrelacion
    ADD CONSTRAINT fk_pr_idrelacion FOREIGN KEY (idrelacion) REFERENCES relacion(iden);


--
-- TOC entry 3628 (class 2606 OID 62677)
-- Dependencies: 3592 3069 3025
-- Name: fk_proceso; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY archivo
    ADD CONSTRAINT fk_proceso FOREIGN KEY (proceso) REFERENCES proceso(iden);


--
-- TOC entry 3684 (class 2606 OID 62682)
-- Dependencies: 3541 3047 3078
-- Name: fk_re_idedr; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY regimenespecifico
    ADD CONSTRAINT fk_re_idedr FOREIGN KEY (identidaddeterminacionregimen) REFERENCES entidaddeterminacionregimen(iden);


--
-- TOC entry 3685 (class 2606 OID 62687)
-- Dependencies: 3601 3078 3078
-- Name: fk_re_idpadre; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY regimenespecifico
    ADD CONSTRAINT fk_re_idpadre FOREIGN KEY (idpadre) REFERENCES regimenespecifico(iden);


--
-- TOC entry 3686 (class 2606 OID 62692)
-- Dependencies: 3613 3080 3079
-- Name: fk_rl_idtramitecreador; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY relacion
    ADD CONSTRAINT fk_rl_idtramitecreador FOREIGN KEY (idtramitecreador) REFERENCES tramite(iden);


--
-- TOC entry 3689 (class 2606 OID 62697)
-- Dependencies: 3590 3067 3080
-- Name: fk_tra_idplan; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY tramite
    ADD CONSTRAINT fk_tra_idplan FOREIGN KEY (idplan) REFERENCES plan(iden);


--
-- TOC entry 3692 (class 2606 OID 62702)
-- Dependencies: 3487 3027 3083
-- Name: fk_vc_idcasovinculado; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY vinculocaso
    ADD CONSTRAINT fk_vc_idcasovinculado FOREIGN KEY (idcasovinculado) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3690 (class 2606 OID 62707)
-- Dependencies: 3605 3079 3082
-- Name: fk_vr_idrelacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY vectorrelacion
    ADD CONSTRAINT fk_vr_idrelacion FOREIGN KEY (idrelacion) REFERENCES relacion(iden);


--
-- TOC entry 3667 (class 2606 OID 62712)
-- Dependencies: 2878 3059
-- Name: operaciondeterminacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT operaciondeterminacion_fk FOREIGN KEY (idtipooperaciondet) REFERENCES diccionario.tipooperaciondeterminacion(iden);


--
-- TOC entry 3671 (class 2606 OID 62717)
-- Dependencies: 2879 3061
-- Name: operacionentidad_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT operacionentidad_fk FOREIGN KEY (idtipooperacionent) REFERENCES diccionario.tipooperacionentidad(iden);


--
-- TOC entry 3674 (class 2606 OID 62722)
-- Dependencies: 2865 3063
-- Name: operacionplan_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT operacionplan_fk FOREIGN KEY (idinstrumentotipooperacion) REFERENCES diccionario.instrumentotipooperacionplan(iden);


--
-- TOC entry 3677 (class 2606 OID 62727)
-- Dependencies: 2881 3065
-- Name: operacionrelacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT operacionrelacion_fk FOREIGN KEY (idtipooperacionrel) REFERENCES diccionario.tipooperacionrelacion(iden);


--
-- TOC entry 3680 (class 2606 OID 62732)
-- Dependencies: 2826 3067
-- Name: plan_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT plan_fk FOREIGN KEY (idambito) REFERENCES diccionario.ambito(iden);


--
-- TOC entry 3683 (class 2606 OID 62737)
-- Dependencies: 2837 3071
-- Name: propiedadrelacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY propiedadrelacion
    ADD CONSTRAINT propiedadrelacion_fk FOREIGN KEY (iddefpropiedad) REFERENCES diccionario.defpropiedad(iden);


--
-- TOC entry 3687 (class 2606 OID 62742)
-- Dependencies: 2839 3079
-- Name: relacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY relacion
    ADD CONSTRAINT relacion_fk FOREIGN KEY (iddefrelacion) REFERENCES diccionario.defrelacion(iden);


--
-- TOC entry 3688 (class 2606 OID 62747)
-- Dependencies: 3613 3080 3079
-- Name: relacion_fk1; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY relacion
    ADD CONSTRAINT relacion_fk1 FOREIGN KEY (idtramiteborrador) REFERENCES tramite(iden);


--
-- TOC entry 3691 (class 2606 OID 62752)
-- Dependencies: 2841 3082
-- Name: vectorrelacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY vectorrelacion
    ADD CONSTRAINT vectorrelacion_fk FOREIGN KEY (iddefvector) REFERENCES diccionario.defvector(iden);


--
-- TOC entry 3693 (class 2606 OID 62757)
-- Dependencies: 3487 3027 3083
-- Name: vinculocaso_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY vinculocaso
    ADD CONSTRAINT vinculocaso_fk FOREIGN KEY (idcaso) REFERENCES casoentidaddeterminacion(iden);


-- Completed on 2013-07-09 13:34:29

--
-- PostgreSQL database dump complete
--

