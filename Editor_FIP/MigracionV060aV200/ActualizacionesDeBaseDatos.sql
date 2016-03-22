

-- Creo la funcion para borrar los constraints de l prerefundido

CREATE OR REPLACE FUNCTION gestionfip."borrarConstraintPrerefundido"()
  RETURNS boolean AS
$BODY$
DECLARE
  
BEGIN

if exists(select 1 from pg_constraint where conname = 'fkab9e4b5a7b0377f3')
then
ALTER TABLE gestionfip.planesencargados DROP CONSTRAINT fkab9e4b5a7b0377f3;
end if;

if exists(select 1 from pg_constraint where conname = 'fkab9e4b5ab1d6a0e5')
then
ALTER TABLE gestionfip.planesencargados DROP CONSTRAINT fkab9e4b5ab1d6a0e5;
end if;

if exists(select 1 from pg_constraint where conname = 'fkb1df1a42cae62d83')
then
ALTER TABLE administracionusuarios.usuariotramite DROP CONSTRAINT fkb1df1a42cae62d83;
end if;


  RETURN true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



-- Actualizo el resto de constraint de la v060 

CREATE OR REPLACE FUNCTION gestionfip."actualizarConstraintVersion060"()
  RETURNS boolean AS
$BODY$
DECLARE
  
BEGIN

if exists(select 1 from pg_constraint where conname = 'fk_identidades')
then
ALTER TABLE gestionfip.coloresentidades DROP CONSTRAINT fk_identidades;
end if;


ALTER TABLE gestionfip.coordenadasambito ADD proyeccion character varying(255);

ALTER TABLE gestionfip.fipscargados ADD idplaneamientoencargado integer;
ALTER TABLE gestionfip.fipscargados ALTER resultado TYPE text;

if exists(select 1 from pg_constraint where conname = 'fkab9e4b5a7b0377f3')
then
ALTER TABLE gestionfip.planesencargados DROP CONSTRAINT fkab9e4b5a7b0377f3;
end if;

if exists(select 1 from pg_constraint where conname = 'fkab9e4b5ab1d6a0e5')
then
ALTER TABLE gestionfip.planesencargados DROP CONSTRAINT fkab9e4b5ab1d6a0e5;
end if;

if exists(select 1 from pg_constraint where conname = 'fkf07978f941e3b02a')
then
ALTER TABLE gestionfip.prerefundido DROP CONSTRAINT fkf07978f941e3b02a;
end if;

if exists(select 1 from pg_constraint where conname = 'fkf07978f9600d53b5')
then
ALTER TABLE gestionfip.prerefundido DROP CONSTRAINT fkf07978f9600d53b5;
end if;

if exists(select 1 from pg_constraint where conname = 'fkf07978f97b0377f3')
then
ALTER TABLE gestionfip.prerefundido DROP CONSTRAINT fkf07978f97b0377f3;
end if;


ALTER TABLE gestionfip.prerefundido ADD idtramiteencargado integer;
ALTER TABLE gestionfip.prerefundido ADD idtramiteprerefundido integer;
ALTER TABLE gestionfip.prerefundido ADD idtramitevigente integer;


INSERT INTO diccionario.instrumentoplan(
            iden, nemo, idnaturaleza, idliteral)
    VALUES (195, 'REFPP', 1, 202);
    
INSERT INTO diccionario.tipooperacionplan(
            iden, idliteral)
    VALUES (10, 8574);
    
INSERT INTO diccionario.instrumentotipooperacionplan(
            iden, idinstrumentoplan, idtipooperacionplan)
    VALUES (100, 52, 10);
    
INSERT INTO diccionario.instrumentotipooperacionplan(
            iden, idinstrumentoplan, idtipooperacionplan)
    VALUES (101, 195, 10);


  RETURN true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;




-- Ejecuto la actualizacion de las constraints
select gestionfip."actualizarConstraintVersion060"();


-- actualizo entidades_capa


CREATE OR REPLACE VIEW planeamiento.entidades_capa AS 
 SELECT DISTINCT ent.iden AS identidad, cast ('#111111' as character varying(7)) as color, entidadbase.clave AS claveentidadbase, ent.identidadbase, ent.etiqueta, ent.idtramite, tramite.codigofip, tramite.idtipotramite, ent.clave, ent.nombre AS entidad, determinacion.codigo AS idgrupo, capa.nombre AS grupo,  determinacion.codigo, plan.idambito, 
        CASE
            WHEN entidadpol.geom IS NOT NULL THEN entidadpol.geom
            WHEN entidadlin.geom IS NOT NULL THEN entidadlin.geom
            WHEN entidadpnt.geom IS NOT NULL THEN entidadpnt.geom
            ELSE NULL::geometry
        END AS geometria, capa.iden AS idcapa
   FROM planeamiento.entidad ent
   LEFT JOIN planeamiento.entidad entidadbase ON ent.identidadbase = entidadbase.iden
   JOIN planeamiento.entidaddeterminacion ON ent.iden = entidaddeterminacion.identidad
   JOIN planeamiento.tramite ON ent.idtramite = tramite.iden
   JOIN planeamiento.plan ON tramite.idplan = plan.iden
   JOIN planeamiento.casoentidaddeterminacion ON entidaddeterminacion.iden = casoentidaddeterminacion.identidaddeterminacion
   JOIN planeamiento.entidaddeterminacionregimen ON casoentidaddeterminacion.iden = entidaddeterminacionregimen.idcaso
   JOIN planeamiento.opciondeterminacion ON entidaddeterminacionregimen.idopciondeterminacion = opciondeterminacion.iden
   JOIN planeamiento.determinacion ON opciondeterminacion.iddeterminacionvalorref = determinacion.iden
   JOIN planeamiento.determinacion determinacionaplicada ON entidaddeterminacion.iddeterminacion = determinacionaplicada.iden
   LEFT JOIN planeamiento.entidadlin ON ent.iden = entidadlin.identidad
   LEFT JOIN planeamiento.entidadpnt ON ent.iden = entidadpnt.identidad
   LEFT JOIN planeamiento.entidadpol ON ent.iden = entidadpol.identidad
   JOIN explotacion.capagrupo ON determinacion.codigo = capagrupo.codigodeterminaciongrupo::bpchar
   JOIN explotacion.capa ON capagrupo.idcapa = capa.iden
  WHERE determinacionaplicada.idcaracter = 20 AND (entidadpnt.geom IS NOT NULL OR entidadlin.geom IS NOT NULL OR entidadpol.geom IS NOT NULL) AND (determinacion.codigo IN ( SELECT determinacion.codigo
   FROM planeamiento.determinacion
  WHERE determinacion.idcaracter = 12))
  ORDER BY ent.iden, entidadbase.clave, ent.identidadbase, ent.etiqueta, ent.idtramite, tramite.codigofip, tramite.idtipotramite, ent.clave, ent.nombre, determinacion.codigo, capa.nombre, plan.idambito, 
CASE
    WHEN entidadpol.geom IS NOT NULL THEN entidadpol.geom
    WHEN entidadlin.geom IS NOT NULL THEN entidadlin.geom
    WHEN entidadpnt.geom IS NOT NULL THEN entidadpnt.geom
    ELSE NULL::geometry
END, capa.iden;


-- actualizo entidades_sincapa


CREATE OR REPLACE VIEW planeamiento.entidades_sincapa AS 
 SELECT ent.iden, cast ('#111111' as character varying(7)) as color, ent.idtramite, ent.nombre, ent.clave, 
        CASE
            WHEN entidadpol.geom IS NOT NULL THEN entidadpol.geom
            WHEN entidadlin.geom IS NOT NULL THEN entidadlin.geom
            WHEN entidadpnt.geom IS NOT NULL THEN entidadpnt.geom
            ELSE NULL::geometry
        END AS geometria
   FROM planeamiento.entidad ent
   LEFT JOIN planeamiento.entidadlin ON ent.iden = entidadlin.identidad
   LEFT JOIN planeamiento.entidadpnt ON ent.iden = entidadpnt.identidad
   LEFT JOIN planeamiento.entidadpol ON ent.iden = entidadpol.identidad
  WHERE (entidadpnt.geom IS NOT NULL OR entidadlin.geom IS NOT NULL OR entidadpol.geom IS NOT NULL) AND NOT (ent.iden IN ( SELECT DISTINCT ent.iden
   FROM planeamiento.entidad ent
   LEFT JOIN planeamiento.entidad entidadbase ON ent.identidadbase = entidadbase.iden
   JOIN planeamiento.entidaddeterminacion ON ent.iden = entidaddeterminacion.identidad
   JOIN planeamiento.tramite ON ent.idtramite = tramite.iden
   JOIN planeamiento.plan ON tramite.idplan = plan.iden
   JOIN planeamiento.casoentidaddeterminacion ON entidaddeterminacion.iden = casoentidaddeterminacion.identidaddeterminacion
   JOIN planeamiento.entidaddeterminacionregimen ON casoentidaddeterminacion.iden = entidaddeterminacionregimen.idcaso
   JOIN planeamiento.opciondeterminacion ON entidaddeterminacionregimen.idopciondeterminacion = opciondeterminacion.iden
   JOIN planeamiento.determinacion ON opciondeterminacion.iddeterminacionvalorref = determinacion.iden
   JOIN planeamiento.determinacion determinacionaplicada ON entidaddeterminacion.iddeterminacion = determinacionaplicada.iden
   LEFT JOIN planeamiento.entidadlin ON ent.iden = entidadlin.identidad
   LEFT JOIN planeamiento.entidadpnt ON ent.iden = entidadpnt.identidad
   LEFT JOIN planeamiento.entidadpol ON ent.iden = entidadpol.identidad
   JOIN explotacion.capagrupo ON determinacion.codigo = capagrupo.codigodeterminaciongrupo::bpchar
   JOIN explotacion.capa ON capagrupo.idcapa = capa.iden
  WHERE determinacionaplicada.idcaracter = 20 AND (entidadpnt.geom IS NOT NULL OR entidadlin.geom IS NOT NULL OR entidadpol.geom IS NOT NULL) AND (determinacion.codigo IN ( SELECT determinacion.codigo
   FROM planeamiento.determinacion
  WHERE determinacion.idcaracter = 12))
  ORDER BY ent.iden))
  ORDER BY ent.idtramite;



-- Borro el esquema de validacion

DROP SCHEMA validacion CASCADE;

-- Inserto el nuevo esquema de validacion


-- Inserto el nuevo esquema de refundido


-- Inserto el nuevo esquema de conversorfipsipu


