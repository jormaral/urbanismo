--
-- PostgreSQL database dump
--

-- Started on 2012-10-03 11:55:25

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 11 (class 2615 OID 275528)
-- Name: refundido; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA refundido;


ALTER SCHEMA refundido OWNER TO postgres;

SET search_path = refundido, pg_catalog;

--
-- TOC entry 715 (class 1255 OID 285134)
-- Dependencies: 11 1472
-- Name: copiarcasoentidaddeterminacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarcasoentidaddeterminacion(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarcasoentidaddeterminacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 708 (class 1255 OID 285127)
-- Dependencies: 11 1472
-- Name: copiardeterminacion(integer, integer, integer, integer, boolean); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiardeterminacion(proceso integer, origen integer, destino integer, padre integer, recalcular boolean) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiardeterminacion(proceso integer, origen integer, destino integer, padre integer, recalcular boolean) OWNER TO postgres;

--
-- TOC entry 712 (class 1255 OID 285131)
-- Dependencies: 1472 11
-- Name: copiardeterminaciongrupo(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiardeterminaciongrupo(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiardeterminaciongrupo(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 716 (class 1255 OID 285135)
-- Dependencies: 11 1472
-- Name: copiardocumentocaso(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiardocumentocaso(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiardocumentocaso(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 717 (class 1255 OID 285136)
-- Dependencies: 11 1472
-- Name: copiardocumentodeterminacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiardocumentodeterminacion(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiardocumentodeterminacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 718 (class 1255 OID 285137)
-- Dependencies: 1472 11
-- Name: copiardocumentoentidad(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiardocumentoentidad(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiardocumentoentidad(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 707 (class 1255 OID 285126)
-- Dependencies: 1472 11
-- Name: copiarentidad(integer, integer, integer, integer, boolean); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarentidad(proceso integer, origen integer, destino integer, padre integer, recalcular boolean) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarentidad(proceso integer, origen integer, destino integer, padre integer, recalcular boolean) OWNER TO postgres;

--
-- TOC entry 714 (class 1255 OID 285133)
-- Dependencies: 11 1472
-- Name: copiarentidaddeterminacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarentidaddeterminacion(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarentidaddeterminacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 719 (class 1255 OID 285138)
-- Dependencies: 11 1472
-- Name: copiarentidaddeterminacionregimen(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarentidaddeterminacionregimen(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarentidaddeterminacionregimen(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 710 (class 1255 OID 285129)
-- Dependencies: 11 1472
-- Name: copiarlinea(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarlinea(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarlinea(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 713 (class 1255 OID 285132)
-- Dependencies: 11 1472
-- Name: copiaropciondeterminacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiaropciondeterminacion(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiaropciondeterminacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 709 (class 1255 OID 285128)
-- Dependencies: 11 1472
-- Name: copiarpoligono(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarpoligono(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarpoligono(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 723 (class 1255 OID 285142)
-- Dependencies: 11 1472
-- Name: copiarpropiedadrelacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarpropiedadrelacion(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarpropiedadrelacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 711 (class 1255 OID 285130)
-- Dependencies: 11 1472
-- Name: copiarpunto(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarpunto(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarpunto(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 721 (class 1255 OID 285140)
-- Dependencies: 1472 11
-- Name: copiarregimenespecifico(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarregimenespecifico(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarregimenespecifico(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 722 (class 1255 OID 285141)
-- Dependencies: 11 1472
-- Name: copiarrelacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarrelacion(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarrelacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 724 (class 1255 OID 285143)
-- Dependencies: 11 1472
-- Name: copiarvectorrelacion(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarvectorrelacion(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarvectorrelacion(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 720 (class 1255 OID 285139)
-- Dependencies: 11 1472
-- Name: copiarvinculocaso(integer, integer, integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION copiarvinculocaso(proceso integer, origen integer, destino integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.copiarvinculocaso(proceso integer, origen integer, destino integer) OWNER TO postgres;

--
-- TOC entry 732 (class 1255 OID 290835)
-- Dependencies: 11 1472
-- Name: eliminarcasoentidaddeterminacion(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarcasoentidaddeterminacion(ideliminar integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.eliminarcasoentidaddeterminacion(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 725 (class 1255 OID 290828)
-- Dependencies: 11 1472
-- Name: eliminardeterminacion(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminardeterminacion(ideliminar integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.eliminardeterminacion(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 726 (class 1255 OID 290830)
-- Dependencies: 11 1472
-- Name: eliminardocumento(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminardocumento(ideliminar integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.eliminardocumento(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 727 (class 1255 OID 290829)
-- Dependencies: 1472 11
-- Name: eliminarentidad(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarentidad(ideliminar integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.eliminarentidad(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 731 (class 1255 OID 290834)
-- Dependencies: 1472 11
-- Name: eliminarentidaddeterminacion(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarentidaddeterminacion(ideliminar integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.eliminarentidaddeterminacion(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 728 (class 1255 OID 290831)
-- Dependencies: 11 1472
-- Name: eliminarentidaddeterminacionregimen(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarentidaddeterminacionregimen(ideliminar integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.eliminarentidaddeterminacionregimen(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 730 (class 1255 OID 290833)
-- Dependencies: 1472 11
-- Name: eliminaropciondeterminacion(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminaropciondeterminacion(ideliminar integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.eliminaropciondeterminacion(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 729 (class 1255 OID 290832)
-- Dependencies: 11 1472
-- Name: eliminarregimen(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarregimen(ideliminar integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.eliminarregimen(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 733 (class 1255 OID 290836)
-- Dependencies: 11 1472
-- Name: eliminarrelacion(integer); Type: FUNCTION; Schema: refundido; Owner: postgres
--

CREATE FUNCTION eliminarrelacion(ideliminar integer) RETURNS integer
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
$$
    LANGUAGE plpgsql;


ALTER FUNCTION refundido.eliminarrelacion(ideliminar integer) OWNER TO postgres;

--
-- TOC entry 2859 (class 1259 OID 276197)
-- Dependencies: 11
-- Name: refundido_ambitoaplicacionambito_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_ambitoaplicacionambito_iden_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_ambitoaplicacionambito_iden_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 2860 (class 1259 OID 276199)
-- Dependencies: 3254 11
-- Name: ambitoaplicacionambito; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE ambitoaplicacionambito (
    iden integer DEFAULT nextval('refundido_ambitoaplicacionambito_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    idambito integer NOT NULL
);


ALTER TABLE refundido.ambitoaplicacionambito OWNER TO postgres;

--
-- TOC entry 2861 (class 1259 OID 276203)
-- Dependencies: 11
-- Name: refundido_archivo_sequence; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_archivo_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_archivo_sequence OWNER TO postgres;

--
-- TOC entry 2862 (class 1259 OID 276205)
-- Dependencies: 3255 11
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
-- TOC entry 2863 (class 1259 OID 276212)
-- Dependencies: 11
-- Name: refundido_casoentidaddeterminacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_casoentidaddeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_casoentidaddeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2864 (class 1259 OID 276214)
-- Dependencies: 3256 3257 11
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
-- TOC entry 2865 (class 1259 OID 276219)
-- Dependencies: 11
-- Name: refundido_determinacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_determinacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_determinacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2866 (class 1259 OID 276221)
-- Dependencies: 3258 3259 3260 11
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
-- TOC entry 2867 (class 1259 OID 276230)
-- Dependencies: 11
-- Name: refundido_determinaciongrupoentidad_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_determinaciongrupoentidad_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_determinaciongrupoentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 2868 (class 1259 OID 276232)
-- Dependencies: 3261 11
-- Name: determinaciongrupoentidad; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE determinaciongrupoentidad (
    iden integer DEFAULT nextval('refundido_determinaciongrupoentidad_iden_seq'::regclass) NOT NULL,
    iddeterminaciongrupo integer,
    iddeterminacion integer NOT NULL
);


ALTER TABLE refundido.determinaciongrupoentidad OWNER TO postgres;

--
-- TOC entry 2869 (class 1259 OID 276236)
-- Dependencies: 11
-- Name: refundido_documento_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_documento_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_documento_iden_seq OWNER TO postgres;

--
-- TOC entry 2870 (class 1259 OID 276238)
-- Dependencies: 3262 11
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
-- TOC entry 2871 (class 1259 OID 276245)
-- Dependencies: 11
-- Name: refundido_documentocaso_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_documentocaso_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_documentocaso_iden_seq OWNER TO postgres;

--
-- TOC entry 2872 (class 1259 OID 276247)
-- Dependencies: 3263 11
-- Name: documentocaso; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE documentocaso (
    iden integer DEFAULT nextval('refundido_documentocaso_iden_seq'::regclass) NOT NULL,
    idcaso integer NOT NULL,
    iddocumento integer NOT NULL
);


ALTER TABLE refundido.documentocaso OWNER TO postgres;

--
-- TOC entry 2873 (class 1259 OID 276251)
-- Dependencies: 11
-- Name: refundido_documentodeterminacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_documentodeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_documentodeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2874 (class 1259 OID 276253)
-- Dependencies: 3264 11
-- Name: documentodeterminacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE documentodeterminacion (
    iden integer DEFAULT nextval('refundido_documentodeterminacion_iden_seq'::regclass) NOT NULL,
    iddeterminacion integer NOT NULL,
    iddocumento integer NOT NULL
);


ALTER TABLE refundido.documentodeterminacion OWNER TO postgres;

--
-- TOC entry 2875 (class 1259 OID 276257)
-- Dependencies: 11
-- Name: refundido_documentoentidad_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_documentoentidad_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_documentoentidad_iden_seq OWNER TO postgres;

--
-- TOC entry 2876 (class 1259 OID 276259)
-- Dependencies: 3265 11
-- Name: documentoentidad; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE documentoentidad (
    iden integer DEFAULT nextval('refundido_documentoentidad_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    iddocumento integer NOT NULL
);


ALTER TABLE refundido.documentoentidad OWNER TO postgres;

--
-- TOC entry 2877 (class 1259 OID 276263)
-- Dependencies: 11
-- Name: refundido_documentoshp_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_documentoshp_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_documentoshp_iden_seq OWNER TO postgres;

--
-- TOC entry 2878 (class 1259 OID 276265)
-- Dependencies: 3266 3267 3268 3269 11 1007
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
-- TOC entry 2879 (class 1259 OID 276275)
-- Dependencies: 11
-- Name: refundido_entidad_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidad_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidad_iden_seq OWNER TO postgres;

--
-- TOC entry 2880 (class 1259 OID 276277)
-- Dependencies: 3270 3271 3272 11
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
-- TOC entry 2881 (class 1259 OID 276286)
-- Dependencies: 11
-- Name: refundido_entidaddeterminacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidaddeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidaddeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2882 (class 1259 OID 276288)
-- Dependencies: 3273 11
-- Name: entidaddeterminacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE entidaddeterminacion (
    iden integer DEFAULT nextval('refundido_entidaddeterminacion_iden_seq'::regclass) NOT NULL,
    identidad integer NOT NULL,
    iddeterminacion integer NOT NULL
);


ALTER TABLE refundido.entidaddeterminacion OWNER TO postgres;

--
-- TOC entry 2883 (class 1259 OID 276292)
-- Dependencies: 11
-- Name: refundido_entidaddeterminacionregimen_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidaddeterminacionregimen_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidaddeterminacionregimen_iden_seq OWNER TO postgres;

--
-- TOC entry 2884 (class 1259 OID 276294)
-- Dependencies: 3274 11
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
-- TOC entry 2885 (class 1259 OID 276298)
-- Dependencies: 11
-- Name: refundido_entidadlin_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidadlin_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidadlin_iden_seq OWNER TO postgres;

--
-- TOC entry 2886 (class 1259 OID 276300)
-- Dependencies: 3275 3276 3277 3278 3279 1007 11
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
-- TOC entry 2887 (class 1259 OID 276311)
-- Dependencies: 11
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
-- TOC entry 2888 (class 1259 OID 276313)
-- Dependencies: 3280 3281 3282 3283 3284 1007 11
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
-- TOC entry 2889 (class 1259 OID 276324)
-- Dependencies: 11
-- Name: refundido_entidadpol_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_entidadpol_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_entidadpol_iden_seq OWNER TO postgres;

--
-- TOC entry 2890 (class 1259 OID 276326)
-- Dependencies: 3285 3286 3287 3288 3289 1007 11
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
-- TOC entry 2891 (class 1259 OID 276337)
-- Dependencies: 11
-- Name: refundido_opciondeterminacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_opciondeterminacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_opciondeterminacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2892 (class 1259 OID 276339)
-- Dependencies: 3290 11
-- Name: opciondeterminacion; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE opciondeterminacion (
    iden integer DEFAULT nextval('refundido_opciondeterminacion_iden_seq'::regclass) NOT NULL,
    iddeterminacion integer NOT NULL,
    iddeterminacionvalorref integer NOT NULL
);


ALTER TABLE refundido.opciondeterminacion OWNER TO postgres;

--
-- TOC entry 2893 (class 1259 OID 276343)
-- Dependencies: 11
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
-- TOC entry 2894 (class 1259 OID 276345)
-- Dependencies: 3291 11
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
-- TOC entry 2895 (class 1259 OID 276352)
-- Dependencies: 11
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
-- TOC entry 2896 (class 1259 OID 276354)
-- Dependencies: 3292 11
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
-- TOC entry 2897 (class 1259 OID 276358)
-- Dependencies: 11
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
-- TOC entry 2898 (class 1259 OID 276360)
-- Dependencies: 3293 11
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
-- TOC entry 2899 (class 1259 OID 276364)
-- Dependencies: 11
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
-- TOC entry 2900 (class 1259 OID 276366)
-- Dependencies: 3294 11
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
-- TOC entry 2901 (class 1259 OID 276370)
-- Dependencies: 11
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
-- TOC entry 2902 (class 1259 OID 276372)
-- Dependencies: 3295 11
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
-- TOC entry 2903 (class 1259 OID 276376)
-- Dependencies: 11
-- Name: refundido_plan_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_plan_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_plan_iden_seq OWNER TO postgres;

--
-- TOC entry 2904 (class 1259 OID 276378)
-- Dependencies: 3296 3297 3298 11
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
-- TOC entry 2905 (class 1259 OID 276387)
-- Dependencies: 11
-- Name: refundido_proceso_sequence; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_proceso_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_proceso_sequence OWNER TO postgres;

--
-- TOC entry 2906 (class 1259 OID 276389)
-- Dependencies: 3299 11
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
-- TOC entry 2907 (class 1259 OID 276393)
-- Dependencies: 11
-- Name: refundido_propiedadrelacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_propiedadrelacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_propiedadrelacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2908 (class 1259 OID 276395)
-- Dependencies: 3300 11
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
-- TOC entry 2943 (class 1259 OID 276523)
-- Dependencies: 11
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
-- TOC entry 2909 (class 1259 OID 276402)
-- Dependencies: 11
-- Name: refundido_regimenespecifico_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_regimenespecifico_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_regimenespecifico_iden_seq OWNER TO postgres;

--
-- TOC entry 2911 (class 1259 OID 276411)
-- Dependencies: 11
-- Name: refundido_relacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_relacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_relacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2913 (class 1259 OID 276417)
-- Dependencies: 11
-- Name: refundido_tramite_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_tramite_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_tramite_iden_seq OWNER TO postgres;

--
-- TOC entry 2915 (class 1259 OID 276427)
-- Dependencies: 11
-- Name: refundido_vectorrelacion_iden_seq; Type: SEQUENCE; Schema: refundido; Owner: postgres
--

CREATE SEQUENCE refundido_vectorrelacion_iden_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE refundido.refundido_vectorrelacion_iden_seq OWNER TO postgres;

--
-- TOC entry 2917 (class 1259 OID 276434)
-- Dependencies: 11
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
-- TOC entry 2910 (class 1259 OID 276404)
-- Dependencies: 3301 11
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
-- TOC entry 2912 (class 1259 OID 276413)
-- Dependencies: 3302 11
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
-- TOC entry 2914 (class 1259 OID 276419)
-- Dependencies: 3303 3304 11
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
-- TOC entry 2949 (class 1259 OID 280430)
-- Dependencies: 11
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
-- TOC entry 2916 (class 1259 OID 276429)
-- Dependencies: 3305 3306 11
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
-- TOC entry 2918 (class 1259 OID 276436)
-- Dependencies: 3307 11
-- Name: vinculocaso; Type: TABLE; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE TABLE vinculocaso (
    iden integer DEFAULT nextval('refundido_vinculocaso_iden_seq'::regclass) NOT NULL,
    idcaso integer NOT NULL,
    idcasovinculado integer NOT NULL
);


ALTER TABLE refundido.vinculocaso OWNER TO postgres;

--
-- TOC entry 3311 (class 2606 OID 276698)
-- Dependencies: 2860 2860
-- Name: pk_ambitoaplicacionambito; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ambitoaplicacionambito
    ADD CONSTRAINT pk_ambitoaplicacionambito PRIMARY KEY (iden);


--
-- TOC entry 3313 (class 2606 OID 276700)
-- Dependencies: 2862 2862
-- Name: pk_archivo; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY archivo
    ADD CONSTRAINT pk_archivo PRIMARY KEY (iden);


--
-- TOC entry 3317 (class 2606 OID 276702)
-- Dependencies: 2864 2864
-- Name: pk_casoentidaddeterminacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY casoentidaddeterminacion
    ADD CONSTRAINT pk_casoentidaddeterminacion PRIMARY KEY (iden);


--
-- TOC entry 3327 (class 2606 OID 276704)
-- Dependencies: 2866 2866
-- Name: pk_determinacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT pk_determinacion PRIMARY KEY (iden);


--
-- TOC entry 3331 (class 2606 OID 276706)
-- Dependencies: 2868 2868
-- Name: pk_dge; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY determinaciongrupoentidad
    ADD CONSTRAINT pk_dge PRIMARY KEY (iden);


--
-- TOC entry 3337 (class 2606 OID 276708)
-- Dependencies: 2870 2870
-- Name: pk_doc; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT pk_doc PRIMARY KEY (iden);


--
-- TOC entry 3341 (class 2606 OID 276710)
-- Dependencies: 2872 2872
-- Name: pk_documentocaso; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentocaso
    ADD CONSTRAINT pk_documentocaso PRIMARY KEY (iden);


--
-- TOC entry 3345 (class 2606 OID 276712)
-- Dependencies: 2874 2874
-- Name: pk_documentodeterminacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentodeterminacion
    ADD CONSTRAINT pk_documentodeterminacion PRIMARY KEY (iden);


--
-- TOC entry 3349 (class 2606 OID 276714)
-- Dependencies: 2876 2876
-- Name: pk_documentoentidad; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentoentidad
    ADD CONSTRAINT pk_documentoentidad PRIMARY KEY (iden);


--
-- TOC entry 3353 (class 2606 OID 276716)
-- Dependencies: 2878 2878
-- Name: pk_documentoshp; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documentoshp
    ADD CONSTRAINT pk_documentoshp PRIMARY KEY (iden);


--
-- TOC entry 3371 (class 2606 OID 276718)
-- Dependencies: 2884 2884
-- Name: pk_edr; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT pk_edr PRIMARY KEY (iden);


--
-- TOC entry 3361 (class 2606 OID 276720)
-- Dependencies: 2880 2880
-- Name: pk_entidad; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT pk_entidad PRIMARY KEY (iden);


--
-- TOC entry 3365 (class 2606 OID 276722)
-- Dependencies: 2882 2882
-- Name: pk_entidaddeterminacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidaddeterminacion
    ADD CONSTRAINT pk_entidaddeterminacion PRIMARY KEY (iden);


--
-- TOC entry 3375 (class 2606 OID 276724)
-- Dependencies: 2886 2886
-- Name: pk_entidadlin; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidadlin
    ADD CONSTRAINT pk_entidadlin PRIMARY KEY (iden);


--
-- TOC entry 3379 (class 2606 OID 276726)
-- Dependencies: 2888 2888
-- Name: pk_entidadpnt; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidadpnt
    ADD CONSTRAINT pk_entidadpnt PRIMARY KEY (iden);


--
-- TOC entry 3383 (class 2606 OID 276728)
-- Dependencies: 2890 2890
-- Name: pk_entidadpol; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entidadpol
    ADD CONSTRAINT pk_entidadpol PRIMARY KEY (iden);


--
-- TOC entry 3387 (class 2606 OID 276730)
-- Dependencies: 2892 2892
-- Name: pk_opciondeterminacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY opciondeterminacion
    ADD CONSTRAINT pk_opciondeterminacion PRIMARY KEY (iden);


--
-- TOC entry 3391 (class 2606 OID 276732)
-- Dependencies: 2894 2894
-- Name: pk_operacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacion
    ADD CONSTRAINT pk_operacion PRIMARY KEY (iden);


--
-- TOC entry 3397 (class 2606 OID 276734)
-- Dependencies: 2896 2896
-- Name: pk_operaciondeterminacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT pk_operaciondeterminacion PRIMARY KEY (iden);


--
-- TOC entry 3403 (class 2606 OID 276736)
-- Dependencies: 2898 2898
-- Name: pk_operacionentidad; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT pk_operacionentidad PRIMARY KEY (iden);


--
-- TOC entry 3408 (class 2606 OID 276738)
-- Dependencies: 2900 2900
-- Name: pk_operacionplan; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT pk_operacionplan PRIMARY KEY (iden);


--
-- TOC entry 3413 (class 2606 OID 276740)
-- Dependencies: 2902 2902
-- Name: pk_operacionrelacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT pk_operacionrelacion PRIMARY KEY (iden);


--
-- TOC entry 3420 (class 2606 OID 276742)
-- Dependencies: 2904 2904
-- Name: pk_plan; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT pk_plan PRIMARY KEY (iden);


--
-- TOC entry 3422 (class 2606 OID 276744)
-- Dependencies: 2906 2906
-- Name: pk_proceso; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY proceso
    ADD CONSTRAINT pk_proceso PRIMARY KEY (iden);


--
-- TOC entry 3427 (class 2606 OID 276746)
-- Dependencies: 2908 2908
-- Name: pk_propiedadrelacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY propiedadrelacion
    ADD CONSTRAINT pk_propiedadrelacion PRIMARY KEY (iden);


--
-- TOC entry 3432 (class 2606 OID 276748)
-- Dependencies: 2910 2910
-- Name: pk_regimenespecifico; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY regimenespecifico
    ADD CONSTRAINT pk_regimenespecifico PRIMARY KEY (iden);


--
-- TOC entry 3436 (class 2606 OID 276750)
-- Dependencies: 2912 2912
-- Name: pk_rl; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY relacion
    ADD CONSTRAINT pk_rl PRIMARY KEY (iden);


--
-- TOC entry 3444 (class 2606 OID 276752)
-- Dependencies: 2914 2914
-- Name: pk_tramite; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tramite
    ADD CONSTRAINT pk_tramite PRIMARY KEY (iden);


--
-- TOC entry 3449 (class 2606 OID 276754)
-- Dependencies: 2916 2916
-- Name: pk_vectorrelacion; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vectorrelacion
    ADD CONSTRAINT pk_vectorrelacion PRIMARY KEY (iden);


--
-- TOC entry 3453 (class 2606 OID 276756)
-- Dependencies: 2918 2918
-- Name: pk_vinculocaso; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vinculocaso
    ADD CONSTRAINT pk_vinculocaso PRIMARY KEY (iden);


--
-- TOC entry 3455 (class 2606 OID 280434)
-- Dependencies: 2949 2949 2949 2949
-- Name: traza_pk; Type: CONSTRAINT; Schema: refundido; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY traza
    ADD CONSTRAINT traza_pk PRIMARY KEY (idproceso, tabla, idplaneamiento);


--
-- TOC entry 3433 (class 1259 OID 276944)
-- Dependencies: 2912
-- Name: fki_relacion_idtramiteborrador; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_relacion_idtramiteborrador ON relacion USING btree (idtramiteborrador);


--
-- TOC entry 3434 (class 1259 OID 276945)
-- Dependencies: 2912
-- Name: fki_relacion_idtramitecreador; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_relacion_idtramitecreador ON relacion USING btree (idtramitecreador);


--
-- TOC entry 3308 (class 1259 OID 276946)
-- Dependencies: 2860
-- Name: idx_ambitoaplicacionambito_idambito; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_ambitoaplicacionambito_idambito ON ambitoaplicacionambito USING btree (idambito);


--
-- TOC entry 3309 (class 1259 OID 276947)
-- Dependencies: 2860
-- Name: idx_ambitoaplicacionambito_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_ambitoaplicacionambito_identidad ON ambitoaplicacionambito USING btree (identidad);


--
-- TOC entry 3314 (class 1259 OID 276948)
-- Dependencies: 2864
-- Name: idx_casoentidaddeterminacion_identidaddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_casoentidaddeterminacion_identidaddeterminacion ON casoentidaddeterminacion USING btree (identidaddeterminacion);


--
-- TOC entry 3315 (class 1259 OID 276949)
-- Dependencies: 2864
-- Name: idx_casoentidaddeterminacion_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_casoentidaddeterminacion_orden ON casoentidaddeterminacion USING btree (orden);


--
-- TOC entry 3318 (class 1259 OID 276950)
-- Dependencies: 2866
-- Name: idx_determinacion_apartado; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_apartado ON determinacion USING btree (apartado);


--
-- TOC entry 3319 (class 1259 OID 276951)
-- Dependencies: 2866
-- Name: idx_determinacion_codigo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_codigo ON determinacion USING btree (codigo);


--
-- TOC entry 3320 (class 1259 OID 276952)
-- Dependencies: 2866
-- Name: idx_determinacion_idcaracter; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_idcaracter ON determinacion USING btree (idcaracter);


--
-- TOC entry 3321 (class 1259 OID 276953)
-- Dependencies: 2866
-- Name: idx_determinacion_iddeterminacionbase; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_iddeterminacionbase ON determinacion USING btree (iddeterminacionbase);


--
-- TOC entry 3322 (class 1259 OID 276954)
-- Dependencies: 2866
-- Name: idx_determinacion_iddeterminacionoriginal; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_iddeterminacionoriginal ON determinacion USING btree (iddeterminacionoriginal);


--
-- TOC entry 3323 (class 1259 OID 276955)
-- Dependencies: 2866
-- Name: idx_determinacion_idpadre; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_idpadre ON determinacion USING btree (idpadre);


--
-- TOC entry 3324 (class 1259 OID 276956)
-- Dependencies: 2866
-- Name: idx_determinacion_idtramite; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_idtramite ON determinacion USING btree (idtramite);


--
-- TOC entry 3325 (class 1259 OID 276957)
-- Dependencies: 2866
-- Name: idx_determinacion_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinacion_orden ON determinacion USING btree (orden);


--
-- TOC entry 3328 (class 1259 OID 276958)
-- Dependencies: 2868
-- Name: idx_determinaciongrupoentidad_iddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinaciongrupoentidad_iddeterminacion ON determinaciongrupoentidad USING btree (iddeterminacion);


--
-- TOC entry 3329 (class 1259 OID 276959)
-- Dependencies: 2868
-- Name: idx_determinaciongrupoentidad_iddeterminaciongrupo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_determinaciongrupoentidad_iddeterminaciongrupo ON determinaciongrupoentidad USING btree (iddeterminaciongrupo);


--
-- TOC entry 3332 (class 1259 OID 276960)
-- Dependencies: 2870
-- Name: idx_documento_iddocumentooriginal; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documento_iddocumentooriginal ON documento USING btree (iddocumentooriginal);


--
-- TOC entry 3333 (class 1259 OID 276961)
-- Dependencies: 2870
-- Name: idx_documento_idgrupo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documento_idgrupo ON documento USING btree (idgrupodocumento);


--
-- TOC entry 3334 (class 1259 OID 276962)
-- Dependencies: 2870
-- Name: idx_documento_idtipo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documento_idtipo ON documento USING btree (idtipodocumento);


--
-- TOC entry 3335 (class 1259 OID 276963)
-- Dependencies: 2870
-- Name: idx_documento_idtramite; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documento_idtramite ON documento USING btree (idtramite);


--
-- TOC entry 3338 (class 1259 OID 276964)
-- Dependencies: 2872
-- Name: idx_documentocaso_idcaso; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentocaso_idcaso ON documentocaso USING btree (idcaso);


--
-- TOC entry 3339 (class 1259 OID 276965)
-- Dependencies: 2872
-- Name: idx_documentocaso_iddocumento; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentocaso_iddocumento ON documentocaso USING btree (iddocumento);


--
-- TOC entry 3342 (class 1259 OID 276966)
-- Dependencies: 2874
-- Name: idx_documentodeterminacion_iddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentodeterminacion_iddeterminacion ON documentodeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3343 (class 1259 OID 276967)
-- Dependencies: 2874
-- Name: idx_documentodeterminacion_iddocumento; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentodeterminacion_iddocumento ON documentodeterminacion USING btree (iddocumento);


--
-- TOC entry 3346 (class 1259 OID 276968)
-- Dependencies: 2876
-- Name: idx_documentoentidad_iddocumento; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentoentidad_iddocumento ON documentoentidad USING btree (iddocumento);


--
-- TOC entry 3347 (class 1259 OID 276969)
-- Dependencies: 2876
-- Name: idx_documentoentidad_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentoentidad_identidad ON documentoentidad USING btree (identidad);


--
-- TOC entry 3350 (class 1259 OID 276970)
-- Dependencies: 2323 2878
-- Name: idx_documentoshp_geom; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentoshp_geom ON documentoshp USING gist (geom);


--
-- TOC entry 3351 (class 1259 OID 276971)
-- Dependencies: 2878
-- Name: idx_documentoshp_iddocumento; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_documentoshp_iddocumento ON documentoshp USING btree (iddocumento);


--
-- TOC entry 3354 (class 1259 OID 276972)
-- Dependencies: 2880
-- Name: idx_entidad_codigo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_codigo ON entidad USING btree (codigo);


--
-- TOC entry 3355 (class 1259 OID 276973)
-- Dependencies: 2880
-- Name: idx_entidad_identidadbase; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_identidadbase ON entidad USING btree (identidadbase);


--
-- TOC entry 3356 (class 1259 OID 276974)
-- Dependencies: 2880
-- Name: idx_entidad_identidadoriginal; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_identidadoriginal ON entidad USING btree (identidadoriginal);


--
-- TOC entry 3357 (class 1259 OID 276975)
-- Dependencies: 2880
-- Name: idx_entidad_idpadre; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_idpadre ON entidad USING btree (idpadre);


--
-- TOC entry 3358 (class 1259 OID 276976)
-- Dependencies: 2880
-- Name: idx_entidad_idtramite; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_idtramite ON entidad USING btree (idtramite);


--
-- TOC entry 3359 (class 1259 OID 276977)
-- Dependencies: 2880
-- Name: idx_entidad_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidad_orden ON entidad USING btree (orden);


--
-- TOC entry 3362 (class 1259 OID 276978)
-- Dependencies: 2882
-- Name: idx_entidaddeterminacion_iddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacion_iddeterminacion ON entidaddeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3363 (class 1259 OID 276979)
-- Dependencies: 2882
-- Name: idx_entidaddeterminacion_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacion_identidad ON entidaddeterminacion USING btree (identidad);


--
-- TOC entry 3366 (class 1259 OID 276980)
-- Dependencies: 2884
-- Name: idx_entidaddeterminacionregimen_idcaso; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacionregimen_idcaso ON entidaddeterminacionregimen USING btree (idcaso);


--
-- TOC entry 3367 (class 1259 OID 276981)
-- Dependencies: 2884
-- Name: idx_entidaddeterminacionregimen_idcasoaplicacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacionregimen_idcasoaplicacion ON entidaddeterminacionregimen USING btree (idcasoaplicacion);


--
-- TOC entry 3368 (class 1259 OID 276982)
-- Dependencies: 2884
-- Name: idx_entidaddeterminacionregimen_iddeterminacionregimen; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacionregimen_iddeterminacionregimen ON entidaddeterminacionregimen USING btree (iddeterminacionregimen);


--
-- TOC entry 3369 (class 1259 OID 276983)
-- Dependencies: 2884
-- Name: idx_entidaddeterminacionregimen_idopciondeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidaddeterminacionregimen_idopciondeterminacion ON entidaddeterminacionregimen USING btree (idopciondeterminacion);


--
-- TOC entry 3372 (class 1259 OID 276984)
-- Dependencies: 2323 2886
-- Name: idx_entidadlin_geom; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadlin_geom ON entidadlin USING gist (geom);


--
-- TOC entry 3373 (class 1259 OID 276985)
-- Dependencies: 2886
-- Name: idx_entidadlin_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadlin_identidad ON entidadlin USING btree (identidad);


--
-- TOC entry 3376 (class 1259 OID 276986)
-- Dependencies: 2323 2888
-- Name: idx_entidadpnt_geom; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadpnt_geom ON entidadpnt USING gist (geom);


--
-- TOC entry 3377 (class 1259 OID 276987)
-- Dependencies: 2888
-- Name: idx_entidadpnt_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadpnt_identidad ON entidadpnt USING btree (identidad);


--
-- TOC entry 3380 (class 1259 OID 276988)
-- Dependencies: 2890 2323
-- Name: idx_entidadpol_geom; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadpol_geom ON entidadpol USING gist (geom);


--
-- TOC entry 3381 (class 1259 OID 276989)
-- Dependencies: 2890
-- Name: idx_entidadpol_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_entidadpol_identidad ON entidadpol USING btree (identidad);


--
-- TOC entry 3384 (class 1259 OID 276990)
-- Dependencies: 2892
-- Name: idx_opciondeterminacion_iddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_opciondeterminacion_iddeterminacion ON opciondeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3385 (class 1259 OID 276991)
-- Dependencies: 2892
-- Name: idx_opciondeterminacion_iddeterminacionvalorref; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_opciondeterminacion_iddeterminacionvalorref ON opciondeterminacion USING btree (iddeterminacionvalorref);


--
-- TOC entry 3388 (class 1259 OID 276992)
-- Dependencies: 2894
-- Name: idx_operacion_idtramiteordenante; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacion_idtramiteordenante ON operacion USING btree (idtramiteordenante);


--
-- TOC entry 3389 (class 1259 OID 276993)
-- Dependencies: 2894
-- Name: idx_operacion_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacion_orden ON operacion USING btree (orden);


--
-- TOC entry 3392 (class 1259 OID 276994)
-- Dependencies: 2896
-- Name: idx_operaciondeterminacion_iddeterminacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operaciondeterminacion_iddeterminacion ON operaciondeterminacion USING btree (iddeterminacion);


--
-- TOC entry 3393 (class 1259 OID 276995)
-- Dependencies: 2896
-- Name: idx_operaciondeterminacion_iddeterminacionoperadora; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operaciondeterminacion_iddeterminacionoperadora ON operaciondeterminacion USING btree (iddeterminacionoperadora);


--
-- TOC entry 3394 (class 1259 OID 276996)
-- Dependencies: 2896
-- Name: idx_operaciondeterminacion_idoperacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operaciondeterminacion_idoperacion ON operaciondeterminacion USING btree (idoperacion);


--
-- TOC entry 3395 (class 1259 OID 276997)
-- Dependencies: 2896
-- Name: idx_operaciondeterminacion_idtipooper; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operaciondeterminacion_idtipooper ON operaciondeterminacion USING btree (idtipooperaciondet);


--
-- TOC entry 3398 (class 1259 OID 276998)
-- Dependencies: 2898
-- Name: idx_operacionentidad_identidad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionentidad_identidad ON operacionentidad USING btree (identidad);


--
-- TOC entry 3399 (class 1259 OID 276999)
-- Dependencies: 2898
-- Name: idx_operacionentidad_identidadoperadora; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionentidad_identidadoperadora ON operacionentidad USING btree (identidadoperadora);


--
-- TOC entry 3400 (class 1259 OID 277000)
-- Dependencies: 2898
-- Name: idx_operacionentidad_idoperacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionentidad_idoperacion ON operacionentidad USING btree (idoperacion);


--
-- TOC entry 3401 (class 1259 OID 277001)
-- Dependencies: 2898
-- Name: idx_operacionentidad_idtipooper; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionentidad_idtipooper ON operacionentidad USING btree (idtipooperacionent);


--
-- TOC entry 3404 (class 1259 OID 277002)
-- Dependencies: 2900
-- Name: idx_operacionplan_idinstrumenttipoop; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionplan_idinstrumenttipoop ON operacionplan USING btree (idinstrumentotipooperacion);


--
-- TOC entry 3405 (class 1259 OID 277003)
-- Dependencies: 2900
-- Name: idx_operacionplan_idplanoperado; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionplan_idplanoperado ON operacionplan USING btree (idplanoperado);


--
-- TOC entry 3406 (class 1259 OID 277004)
-- Dependencies: 2900
-- Name: idx_operacionplan_idplanoperador; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionplan_idplanoperador ON operacionplan USING btree (idplanoperador);


--
-- TOC entry 3409 (class 1259 OID 277005)
-- Dependencies: 2902
-- Name: idx_operacionrelacion_idoperacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionrelacion_idoperacion ON operacionrelacion USING btree (idoperacion);


--
-- TOC entry 3410 (class 1259 OID 277006)
-- Dependencies: 2902
-- Name: idx_operacionrelacion_idrelacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionrelacion_idrelacion ON operacionrelacion USING btree (idrelacion);


--
-- TOC entry 3411 (class 1259 OID 277007)
-- Dependencies: 2902
-- Name: idx_operacionrelacion_idtipoopera; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_operacionrelacion_idtipoopera ON operacionrelacion USING btree (idtipooperacionrel);


--
-- TOC entry 3414 (class 1259 OID 277008)
-- Dependencies: 2904
-- Name: idx_plan_codigo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_plan_codigo ON plan USING btree (codigo);


--
-- TOC entry 3415 (class 1259 OID 277009)
-- Dependencies: 2904
-- Name: idx_plan_idambito; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_plan_idambito ON plan USING btree (idambito);


--
-- TOC entry 3416 (class 1259 OID 277010)
-- Dependencies: 2904
-- Name: idx_plan_idpadre; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_plan_idpadre ON plan USING btree (idpadre);


--
-- TOC entry 3417 (class 1259 OID 277011)
-- Dependencies: 2904
-- Name: idx_plan_idplanbase; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_plan_idplanbase ON plan USING btree (idplanbase);


--
-- TOC entry 3418 (class 1259 OID 277012)
-- Dependencies: 2904
-- Name: idx_plan_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_plan_orden ON plan USING btree (orden);


--
-- TOC entry 3423 (class 1259 OID 277013)
-- Dependencies: 2908
-- Name: idx_propiedadrelacion_iddefpropiedad; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_propiedadrelacion_iddefpropiedad ON propiedadrelacion USING btree (iddefpropiedad);


--
-- TOC entry 3424 (class 1259 OID 277014)
-- Dependencies: 2908
-- Name: idx_propiedadrelacion_idrelacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_propiedadrelacion_idrelacion ON propiedadrelacion USING btree (idrelacion);


--
-- TOC entry 3428 (class 1259 OID 277016)
-- Dependencies: 2910
-- Name: idx_regimenespecifico_identidaddeterminacionregimen; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_regimenespecifico_identidaddeterminacionregimen ON regimenespecifico USING btree (identidaddeterminacionregimen);


--
-- TOC entry 3429 (class 1259 OID 277017)
-- Dependencies: 2910
-- Name: idx_regimenespecifico_idpadre; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_regimenespecifico_idpadre ON regimenespecifico USING btree (idpadre);


--
-- TOC entry 3430 (class 1259 OID 277018)
-- Dependencies: 2910
-- Name: idx_regimenespecifico_orden; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_regimenespecifico_orden ON regimenespecifico USING btree (orden);


--
-- TOC entry 3438 (class 1259 OID 277019)
-- Dependencies: 2914
-- Name: idx_tramite_idcentroproduccion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_tramite_idcentroproduccion ON tramite USING btree (idcentroproduccion);


--
-- TOC entry 3439 (class 1259 OID 277020)
-- Dependencies: 2914
-- Name: idx_tramite_idplan; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_tramite_idplan ON tramite USING btree (idplan);


--
-- TOC entry 3440 (class 1259 OID 277021)
-- Dependencies: 2914
-- Name: idx_tramite_idtipotramite; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_tramite_idtipotramite ON tramite USING btree (idtipotramite);


--
-- TOC entry 3441 (class 1259 OID 277022)
-- Dependencies: 2914
-- Name: idx_tramite_organo; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_tramite_organo ON tramite USING btree (idorgano);


--
-- TOC entry 3442 (class 1259 OID 277023)
-- Dependencies: 2914
-- Name: idx_tramite_sentido; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_tramite_sentido ON tramite USING btree (idsentido);


--
-- TOC entry 3445 (class 1259 OID 277024)
-- Dependencies: 2916
-- Name: idx_vectorrelacion_iddefvector; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_vectorrelacion_iddefvector ON vectorrelacion USING btree (iddefvector);


--
-- TOC entry 3446 (class 1259 OID 277025)
-- Dependencies: 2916
-- Name: idx_vectorrelacion_idrelacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_vectorrelacion_idrelacion ON vectorrelacion USING btree (idrelacion);


--
-- TOC entry 3447 (class 1259 OID 277026)
-- Dependencies: 2916
-- Name: idx_vectorrelacion_valor; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_vectorrelacion_valor ON vectorrelacion USING hash (valor);


--
-- TOC entry 3450 (class 1259 OID 277027)
-- Dependencies: 2918
-- Name: idx_vinculocaso_idcaso; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_vinculocaso_idcaso ON vinculocaso USING btree (idcaso);


--
-- TOC entry 3451 (class 1259 OID 277028)
-- Dependencies: 2918
-- Name: idx_vinculocaso_idcasovinculado; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_vinculocaso_idcasovinculado ON vinculocaso USING btree (idcasovinculado);


--
-- TOC entry 3437 (class 1259 OID 277029)
-- Dependencies: 2912
-- Name: relacion_idx_iddefrelacion; Type: INDEX; Schema: refundido; Owner: postgres; Tablespace: 
--

CREATE INDEX relacion_idx_iddefrelacion ON relacion USING btree (iddefrelacion);


--
-- TOC entry 3456 (class 2606 OID 277703)
-- Dependencies: 2860 2687
-- Name: ambitoaplicacionambito_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY ambitoaplicacionambito
    ADD CONSTRAINT ambitoaplicacionambito_fk FOREIGN KEY (idambito) REFERENCES diccionario.ambito(iden);


--
-- TOC entry 3460 (class 2606 OID 277708)
-- Dependencies: 2866 2693
-- Name: determinacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT determinacion_fk FOREIGN KEY (idcaracter) REFERENCES diccionario.caracterdeterminacion(iden);


--
-- TOC entry 3467 (class 2606 OID 277713)
-- Dependencies: 2870 2729
-- Name: documento_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT documento_fk FOREIGN KEY (idtipodocumento) REFERENCES diccionario.tipodocumento(iden);


--
-- TOC entry 3468 (class 2606 OID 277718)
-- Dependencies: 2870 2703
-- Name: documento_fk1; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT documento_fk1 FOREIGN KEY (idgrupodocumento) REFERENCES diccionario.grupodocumento(iden);


--
-- TOC entry 3457 (class 2606 OID 277723)
-- Dependencies: 2860 2880 3360
-- Name: fk_aaa_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY ambitoaplicacionambito
    ADD CONSTRAINT fk_aaa_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3511 (class 2606 OID 277728)
-- Dependencies: 2906 2687
-- Name: fk_ambito; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY proceso
    ADD CONSTRAINT fk_ambito FOREIGN KEY (ambito) REFERENCES diccionario.ambito(iden);


--
-- TOC entry 3459 (class 2606 OID 277733)
-- Dependencies: 2864 2882 3364
-- Name: fk_ced_identidaddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY casoentidaddeterminacion
    ADD CONSTRAINT fk_ced_identidaddeterminacion FOREIGN KEY (identidaddeterminacion) REFERENCES entidaddeterminacion(iden);


--
-- TOC entry 3461 (class 2606 OID 277738)
-- Dependencies: 2866 2866 3326
-- Name: fk_d_iddeterminacionbase; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_iddeterminacionbase FOREIGN KEY (iddeterminacionbase) REFERENCES determinacion(iden);


--
-- TOC entry 3462 (class 2606 OID 277743)
-- Dependencies: 2866 2866 3326
-- Name: fk_d_iddeterminacionoriginal; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_iddeterminacionoriginal FOREIGN KEY (iddeterminacionoriginal) REFERENCES determinacion(iden);


--
-- TOC entry 3463 (class 2606 OID 277748)
-- Dependencies: 2866 2866 3326
-- Name: fk_d_idpadre; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_idpadre FOREIGN KEY (idpadre) REFERENCES determinacion(iden);


--
-- TOC entry 3464 (class 2606 OID 277753)
-- Dependencies: 2866 2914 3443
-- Name: fk_d_idtramite; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinacion
    ADD CONSTRAINT fk_d_idtramite FOREIGN KEY (idtramite) REFERENCES tramite(iden);


--
-- TOC entry 3465 (class 2606 OID 277758)
-- Dependencies: 2868 2866 3326
-- Name: fk_dge_iddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinaciongrupoentidad
    ADD CONSTRAINT fk_dge_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3466 (class 2606 OID 277763)
-- Dependencies: 2868 2866 3326
-- Name: fk_dge_iddeterminaciongrupo; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY determinaciongrupoentidad
    ADD CONSTRAINT fk_dge_iddeterminaciongrupo FOREIGN KEY (iddeterminaciongrupo) REFERENCES determinacion(iden);


--
-- TOC entry 3469 (class 2606 OID 277768)
-- Dependencies: 2870 2870 3336
-- Name: fk_doc_iddocumentooriginal; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT fk_doc_iddocumentooriginal FOREIGN KEY (iddocumentooriginal) REFERENCES documento(iden);


--
-- TOC entry 3470 (class 2606 OID 277773)
-- Dependencies: 2914 2870 3443
-- Name: fk_doc_idtramite; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT fk_doc_idtramite FOREIGN KEY (idtramite) REFERENCES tramite(iden);


--
-- TOC entry 3471 (class 2606 OID 277778)
-- Dependencies: 2872 2864 3316
-- Name: fk_docc_idcaso; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentocaso
    ADD CONSTRAINT fk_docc_idcaso FOREIGN KEY (idcaso) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3472 (class 2606 OID 277783)
-- Dependencies: 3336 2872 2870
-- Name: fk_docc_iddocumento; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentocaso
    ADD CONSTRAINT fk_docc_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3473 (class 2606 OID 277788)
-- Dependencies: 2866 2874 3326
-- Name: fk_docd_iddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentodeterminacion
    ADD CONSTRAINT fk_docd_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3474 (class 2606 OID 277793)
-- Dependencies: 2870 2874 3336
-- Name: fk_docd_iddocumento; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentodeterminacion
    ADD CONSTRAINT fk_docd_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3475 (class 2606 OID 277798)
-- Dependencies: 2870 2876 3336
-- Name: fk_doce_iddocumento; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentoentidad
    ADD CONSTRAINT fk_doce_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3476 (class 2606 OID 277803)
-- Dependencies: 3360 2876 2880
-- Name: fk_doce_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentoentidad
    ADD CONSTRAINT fk_doce_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3477 (class 2606 OID 277808)
-- Dependencies: 2878 2870 3336
-- Name: fk_docshp_iddocumento; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY documentoshp
    ADD CONSTRAINT fk_docshp_iddocumento FOREIGN KEY (iddocumento) REFERENCES documento(iden);


--
-- TOC entry 3478 (class 2606 OID 277813)
-- Dependencies: 3360 2880 2880
-- Name: fk_e_identidadbase; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_identidadbase FOREIGN KEY (identidadbase) REFERENCES entidad(iden);


--
-- TOC entry 3479 (class 2606 OID 277818)
-- Dependencies: 2880 2880 3360
-- Name: fk_e_identidadoriginal; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_identidadoriginal FOREIGN KEY (identidadoriginal) REFERENCES entidad(iden);


--
-- TOC entry 3480 (class 2606 OID 277823)
-- Dependencies: 2880 2880 3360
-- Name: fk_e_idpadre; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_idpadre FOREIGN KEY (idpadre) REFERENCES entidad(iden);


--
-- TOC entry 3481 (class 2606 OID 277828)
-- Dependencies: 2914 2880 3443
-- Name: fk_e_idtramite; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidad
    ADD CONSTRAINT fk_e_idtramite FOREIGN KEY (idtramite) REFERENCES tramite(iden);


--
-- TOC entry 3482 (class 2606 OID 277833)
-- Dependencies: 2866 2882 3326
-- Name: fk_ed_iddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacion
    ADD CONSTRAINT fk_ed_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3483 (class 2606 OID 277838)
-- Dependencies: 2880 3360 2882
-- Name: fk_ed_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacion
    ADD CONSTRAINT fk_ed_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3484 (class 2606 OID 277843)
-- Dependencies: 3316 2884 2864
-- Name: fk_edr_idcaso; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_idcaso FOREIGN KEY (idcaso) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3485 (class 2606 OID 277848)
-- Dependencies: 3316 2884 2864
-- Name: fk_edr_idcasoaplicacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_idcasoaplicacion FOREIGN KEY (idcasoaplicacion) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3486 (class 2606 OID 277853)
-- Dependencies: 2884 2866 3326
-- Name: fk_edr_iddeterminacionregimen; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_iddeterminacionregimen FOREIGN KEY (iddeterminacionregimen) REFERENCES determinacion(iden);


--
-- TOC entry 3487 (class 2606 OID 277858)
-- Dependencies: 3386 2884 2892
-- Name: fk_edr_idopciondeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidaddeterminacionregimen
    ADD CONSTRAINT fk_edr_idopciondeterminacion FOREIGN KEY (idopciondeterminacion) REFERENCES opciondeterminacion(iden);


--
-- TOC entry 3488 (class 2606 OID 277863)
-- Dependencies: 2886 3360 2880
-- Name: fk_elin_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidadlin
    ADD CONSTRAINT fk_elin_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3489 (class 2606 OID 277868)
-- Dependencies: 2888 3360 2880
-- Name: fk_epnt_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidadpnt
    ADD CONSTRAINT fk_epnt_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3490 (class 2606 OID 277873)
-- Dependencies: 2890 3360 2880
-- Name: fk_epol_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY entidadpol
    ADD CONSTRAINT fk_epol_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3494 (class 2606 OID 277878)
-- Dependencies: 2866 2896 3326
-- Name: fk_od_iddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT fk_od_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3495 (class 2606 OID 277883)
-- Dependencies: 3326 2866 2896
-- Name: fk_od_iddeterminacionoperadora; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT fk_od_iddeterminacionoperadora FOREIGN KEY (iddeterminacionoperadora) REFERENCES determinacion(iden);


--
-- TOC entry 3496 (class 2606 OID 277888)
-- Dependencies: 2894 2896 3390
-- Name: fk_od_idoperacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT fk_od_idoperacion FOREIGN KEY (idoperacion) REFERENCES operacion(iden);


--
-- TOC entry 3491 (class 2606 OID 277893)
-- Dependencies: 3326 2892 2866
-- Name: fk_odet_iddeterminacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY opciondeterminacion
    ADD CONSTRAINT fk_odet_iddeterminacion FOREIGN KEY (iddeterminacion) REFERENCES determinacion(iden);


--
-- TOC entry 3492 (class 2606 OID 277898)
-- Dependencies: 3326 2892 2866
-- Name: fk_odet_iddetvalorref; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY opciondeterminacion
    ADD CONSTRAINT fk_odet_iddetvalorref FOREIGN KEY (iddeterminacionvalorref) REFERENCES determinacion(iden);


--
-- TOC entry 3498 (class 2606 OID 277903)
-- Dependencies: 3360 2898 2880
-- Name: fk_oe_identidad; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT fk_oe_identidad FOREIGN KEY (identidad) REFERENCES entidad(iden);


--
-- TOC entry 3499 (class 2606 OID 277908)
-- Dependencies: 2898 2880 3360
-- Name: fk_oe_identidadoperadora; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT fk_oe_identidadoperadora FOREIGN KEY (identidadoperadora) REFERENCES entidad(iden);


--
-- TOC entry 3500 (class 2606 OID 277913)
-- Dependencies: 3390 2898 2894
-- Name: fk_oe_idoperacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT fk_oe_idoperacion FOREIGN KEY (idoperacion) REFERENCES operacion(iden);


--
-- TOC entry 3493 (class 2606 OID 277918)
-- Dependencies: 2914 2894 3443
-- Name: fk_op_idtramiteordenante; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacion
    ADD CONSTRAINT fk_op_idtramiteordenante FOREIGN KEY (idtramiteordenante) REFERENCES tramite(iden);


--
-- TOC entry 3502 (class 2606 OID 277923)
-- Dependencies: 2904 2900 3419
-- Name: fk_opp_idplanoperado; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT fk_opp_idplanoperado FOREIGN KEY (idplanoperado) REFERENCES plan(iden);


--
-- TOC entry 3503 (class 2606 OID 277928)
-- Dependencies: 2904 2900 3419
-- Name: fk_opp_idplanoperador; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT fk_opp_idplanoperador FOREIGN KEY (idplanoperador) REFERENCES plan(iden);


--
-- TOC entry 3505 (class 2606 OID 277933)
-- Dependencies: 2894 2902 3390
-- Name: fk_or_idoperacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT fk_or_idoperacion FOREIGN KEY (idoperacion) REFERENCES operacion(iden);


--
-- TOC entry 3506 (class 2606 OID 277938)
-- Dependencies: 2912 2902 3435
-- Name: fk_or_idrelacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT fk_or_idrelacion FOREIGN KEY (idrelacion) REFERENCES relacion(iden);


--
-- TOC entry 3508 (class 2606 OID 277943)
-- Dependencies: 2904 2904 3419
-- Name: fk_p_idpadre; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT fk_p_idpadre FOREIGN KEY (idpadre) REFERENCES plan(iden);


--
-- TOC entry 3509 (class 2606 OID 277948)
-- Dependencies: 2904 2904 3419
-- Name: fk_p_idplanbase; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT fk_p_idplanbase FOREIGN KEY (idplanbase) REFERENCES plan(iden);


--
-- TOC entry 3513 (class 2606 OID 277953)
-- Dependencies: 2908 2912 3435
-- Name: fk_pr_idrelacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY propiedadrelacion
    ADD CONSTRAINT fk_pr_idrelacion FOREIGN KEY (idrelacion) REFERENCES relacion(iden);


--
-- TOC entry 3458 (class 2606 OID 277958)
-- Dependencies: 3421 2862 2906
-- Name: fk_proceso; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY archivo
    ADD CONSTRAINT fk_proceso FOREIGN KEY (proceso) REFERENCES proceso(iden);


--
-- TOC entry 3515 (class 2606 OID 277963)
-- Dependencies: 2884 2910 3370
-- Name: fk_re_idedr; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY regimenespecifico
    ADD CONSTRAINT fk_re_idedr FOREIGN KEY (identidaddeterminacionregimen) REFERENCES entidaddeterminacionregimen(iden);


--
-- TOC entry 3516 (class 2606 OID 277968)
-- Dependencies: 2910 2910 3431
-- Name: fk_re_idpadre; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY regimenespecifico
    ADD CONSTRAINT fk_re_idpadre FOREIGN KEY (idpadre) REFERENCES regimenespecifico(iden);


--
-- TOC entry 3517 (class 2606 OID 277973)
-- Dependencies: 2914 2912 3443
-- Name: fk_rl_idtramitecreador; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY relacion
    ADD CONSTRAINT fk_rl_idtramitecreador FOREIGN KEY (idtramitecreador) REFERENCES tramite(iden);


--
-- TOC entry 3520 (class 2606 OID 277978)
-- Dependencies: 2904 2914 3419
-- Name: fk_tra_idplan; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY tramite
    ADD CONSTRAINT fk_tra_idplan FOREIGN KEY (idplan) REFERENCES plan(iden);


--
-- TOC entry 3512 (class 2606 OID 277983)
-- Dependencies: 2927 2906
-- Name: fk_usuario; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY proceso
    ADD CONSTRAINT fk_usuario FOREIGN KEY (usuario) REFERENCES seguridad.usuario(iden);


--
-- TOC entry 3523 (class 2606 OID 277988)
-- Dependencies: 2918 3316 2864
-- Name: fk_vc_idcasovinculado; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY vinculocaso
    ADD CONSTRAINT fk_vc_idcasovinculado FOREIGN KEY (idcasovinculado) REFERENCES casoentidaddeterminacion(iden);


--
-- TOC entry 3521 (class 2606 OID 277993)
-- Dependencies: 2916 3435 2912
-- Name: fk_vr_idrelacion; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY vectorrelacion
    ADD CONSTRAINT fk_vr_idrelacion FOREIGN KEY (idrelacion) REFERENCES relacion(iden);


--
-- TOC entry 3497 (class 2606 OID 277998)
-- Dependencies: 2733 2896
-- Name: operaciondeterminacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operaciondeterminacion
    ADD CONSTRAINT operaciondeterminacion_fk FOREIGN KEY (idtipooperaciondet) REFERENCES diccionario.tipooperaciondeterminacion(iden);


--
-- TOC entry 3501 (class 2606 OID 278003)
-- Dependencies: 2898 2735
-- Name: operacionentidad_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionentidad
    ADD CONSTRAINT operacionentidad_fk FOREIGN KEY (idtipooperacionent) REFERENCES diccionario.tipooperacionentidad(iden);


--
-- TOC entry 3504 (class 2606 OID 278008)
-- Dependencies: 2900 2707
-- Name: operacionplan_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionplan
    ADD CONSTRAINT operacionplan_fk FOREIGN KEY (idinstrumentotipooperacion) REFERENCES diccionario.instrumentotipooperacionplan(iden);


--
-- TOC entry 3507 (class 2606 OID 278013)
-- Dependencies: 2902 2739
-- Name: operacionrelacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY operacionrelacion
    ADD CONSTRAINT operacionrelacion_fk FOREIGN KEY (idtipooperacionrel) REFERENCES diccionario.tipooperacionrelacion(iden);


--
-- TOC entry 3510 (class 2606 OID 278018)
-- Dependencies: 2904 2687
-- Name: plan_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT plan_fk FOREIGN KEY (idambito) REFERENCES diccionario.ambito(iden);


--
-- TOC entry 3514 (class 2606 OID 278023)
-- Dependencies: 2908 2697
-- Name: propiedadrelacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY propiedadrelacion
    ADD CONSTRAINT propiedadrelacion_fk FOREIGN KEY (iddefpropiedad) REFERENCES diccionario.defpropiedad(iden);


--
-- TOC entry 3518 (class 2606 OID 278028)
-- Dependencies: 2912 2699
-- Name: relacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY relacion
    ADD CONSTRAINT relacion_fk FOREIGN KEY (iddefrelacion) REFERENCES diccionario.defrelacion(iden);


--
-- TOC entry 3519 (class 2606 OID 278033)
-- Dependencies: 2912 2914 3443
-- Name: relacion_fk1; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY relacion
    ADD CONSTRAINT relacion_fk1 FOREIGN KEY (idtramiteborrador) REFERENCES tramite(iden);


--
-- TOC entry 3522 (class 2606 OID 278038)
-- Dependencies: 2701 2916
-- Name: vectorrelacion_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY vectorrelacion
    ADD CONSTRAINT vectorrelacion_fk FOREIGN KEY (iddefvector) REFERENCES diccionario.defvector(iden);


--
-- TOC entry 3524 (class 2606 OID 278043)
-- Dependencies: 2864 2918 3316
-- Name: vinculocaso_fk; Type: FK CONSTRAINT; Schema: refundido; Owner: postgres
--

ALTER TABLE ONLY vinculocaso
    ADD CONSTRAINT vinculocaso_fk FOREIGN KEY (idcaso) REFERENCES casoentidaddeterminacion(iden);


-- Completed on 2012-10-03 11:55:26

--
-- PostgreSQL database dump complete
--

