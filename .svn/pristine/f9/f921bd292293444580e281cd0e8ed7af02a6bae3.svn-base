--
-- PostgreSQL database dump
--

-- Started on 2012-01-12 18:44:36

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = validacion, pg_catalog;

--
-- TOC entry 3456 (class 0 OID 62802)
-- Dependencies: 3131
-- Data for Name: validacion; Type: TABLE DATA; Schema: validacion; Owner: postgres
--

INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (26, 4, 1, true, '2.1.3', 'El código del trámite debe estar conformado por el MD5 de la concatenación del identificador del ámbito, el código del plan, el identificador del tipo de trámite, y la iteración.', 'Select Distinct cast(T.codigofip as varchar) From planeamiento.Tramite T, planeamiento.Plan P
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And Upper(Trim(T.codigoFIP))<>Upper(MD5(LPad(Text(P.idAmbito),6, ''0'') || LPad(Text(P.codigo),6,''0'') || LPad(Text(T.idTipoTramite),6,''0'') || LPad(Text(T.iteracion),2,''0'')))', 'not null', 'tramite.codigoFip', 'Tramite: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (17, 3, 1, true, NULL, 'No puede haber una entidad con mas de una asignacion al mismo documento', 'Select Distinct cast(E.codigo as varchar) From planeamiento.Entidad E, planeamiento.Tramite T

Where E.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And E.iden In (Select DE.idEntidad From planeamiento.DocumentoEntidad DE

Where (DE.idEntidad, DE.idDocumento) In (

Select DE.idEntidad, DE.idDocumento From planeamiento.DocumentoEntidad DE

Group By DE.idEntidad, DE.idDocumento

Having Count(*)>1))', 'null', 'entidad.codigo', 'Entidad: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (27, 3, 1, true, '2.3.2', 'Las entidades usadas como entidades base, deben pertenecer al plan base.', 'Select Distinct cast(E.codigo as varchar) From planeamiento.Entidad E, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip)) 
And E.iden In (Select E.idEntidadBase From planeamiento.Entidad E)
And E.idTramite=T.iden And T.idTipoTramite<>15', 'null', 'entidad.codigo', 'Entidad: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (29, 3, 1, true, '2.3.4', 'A una entidad no se le puede asignar más de un documento del mismo nombre, grupo y tipo.', 'Select Distinct cast(E.codigo as varchar) From planeamiento.Entidad E Where E.iden In (
Select E.iden From planeamiento.Entidad E, planeamiento.Tramite T, planeamiento.Documento DOC, planeamiento.DocumentoEntidad DE
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And DE.idEntidad=E.iden And DE.idDocumento=DOC.iden
Group By E.iden, DOC.nombre, DOC.idTipoDocumento, DOC.idGrupoDocumento
Having Count(*)>1)', 'null', 'entidad.codigo', 'Entidad: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (0, 6, 1, false, NULL, 'Validacion integridad', 'N/A', 'not null', 'N/A', NULL);
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (4, 2, 1, true, NULL, 'Las determinaciones con carácter ''REGULACIÓN'' siempre han de tener texto', 'Select cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And Trim(Coalesce(D.texto,''''))=''''

And D.idCaracter=13', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (5, 2, 1, true, NULL, 'No puede haber una determinacion con mas de una asignacion al mismo documento', 'Select cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And D.iden In (Select DD.idDeterminacion From
planeamiento.DocumentoDeterminacion DD

Where (DD.idDeterminacion, DD.idDocumento) In (

Select DD.idDeterminacion, DD.idDocumento From
planeamiento.DocumentoDeterminacion DD

Group By DD.idDeterminacion, DD.idDocumento

Having Count(*)>1))
', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (7, 3, 1, true, NULL, 'Una entidad no puede tener aplicada dos veces la misma determinacion', 'Select Distinct cast(E.codigo as varchar) From planeamiento.Entidad E, planeamiento.Tramite T,
planeamiento.EntidadDeterminacion ED

Where E.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And ED.idEntidad=E.iden

And (ED.idEntidad, ED.idDeterminacion) In (

Select ED.idEntidad, ED.idDeterminacion From
planeamiento.EntidadDeterminacion ED

Group By ED.idEntidad, ED.idDeterminacion

Having Count(*)>1)', 'null', 'entidad.codigo', 'Entidad: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (6, 2, 1, true, NULL, 'No pueden existir dos determinaciones hijas al mismo nivel con el mismo nombre y caracter', 'Select Distinct cast(D.codigo as varchar), D.nombre From planeamiento.Determinacion D Where (D.nombre,
D.idCaracter, Coalesce(D.idPadre,0)) In (

Select D.nombre, D.idCaracter, Coalesce(D.idPadre,0) From
planeamiento.Determinacion D, planeamiento.Tramite T

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

Group By D.nombre, D.idCaracter, Coalesce(D.idPadre,0)

Having Count(*)>1)', 'null', 'determinacion.codigo,determinacion.nombre', 'Determinación: %1, Nombre: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (8, 2, 1, true, NULL, 'Una determinacion no puede estar aplicada a una entidad y no tener al menos un caso', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite
T, planeamiento.EntidadDeterminacion ED

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And ED.idDeterminacion=D.iden

And ED.iden Not In (Select CED.idEntidadDeterminacion From
planeamiento.CasoEntidadDeterminacion CED)', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (15, 5, 1, true, NULL, 'No puede haber mas de un documento del mismo tipo y grupo con el mismo nombre', 'Select Distinct D.nombre From planeamiento.Documento D, planeamiento.Tramite T

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And (D.idTipoDocumento, D.idGrupoDocumento, Trim(Upper(D.nombre))) In (

Select D.idTipoDocumento, D.idGrupoDocumento, Trim(Upper(D.nombre))

From planeamiento.Documento D, planeamiento.Tramite T

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

Group By D.idTipoDocumento, D.idGrupoDocumento, Trim(Upper(D.nombre))

Having Count(*)>1)', 'null', 'documento.nombre', 'Documento: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (16, 5, 1, true, '2.2.2', 'La geometria de los documentos debera ser siempre de tipo poligonal', 'Select Distinct D.nombre From planeamiento.Documento D, planeamiento.Tramite T,
planeamiento.DocumentoSHP DSHP

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And DSHP.idDocumento=D.iden

And Upper(geometryType(DSHP.geom)) Not Like ''%POLYGON%''', 'null', 'documento.nombre', 'Documento: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (1, 6, 1, false, NULL, 'Validacion usuario', 'N/A', 'not null', 'N/A', NULL);
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (19, 3, 1, true, NULL, 'La geometría de las entidades debe ser válida según el estandar OpenGIS.', 'Select Distinct cast(E.codigo as varchar) From planeamiento.Entidad E, planeamiento.Tramite T 
Where Upper(Trim(T.codigofip))=Upper(Trim(:codigoFip))
And E.idTramite=T.iden
And E.iden In (
Select EPOL.idEntidad From planeamiento.EntidadPol EPOL Where st_IsValid(EPOL.geom)=false
Union
Select ELIN.idEntidad From planeamiento.EntidadLin ELIN Where st_IsValid(ELIN.geom)=false
Union
Select EPNT.idEntidad From planeamiento.EntidadPnt EPNT Where st_IsValid(EPNT.geom)=false) ', 'null', 'entidad.codigo', 'Entidad: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (10, 3, 1, true, NULL, 'Un caso debe tener al menos un documento y/o un regimen', 'Select Distinct cast(E.codigo as varchar) As entidad_codigo, cast(D.codigo as varchar) As det_codigo From planeamiento.Entidad E,
planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 

planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED

Where E.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And CED.idEntidadDeterminacion=ED.iden

And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden

And CED.iden Not In (Select DC.idCaso From planeamiento.DocumentoCaso DC)

And CED.iden Not In (Select EDR.idCaso From
planeamiento.EntidadDeterminacionRegimen EDR

Union 

Select EDR.idCasoAplicacion From planeamiento.EntidadDeterminacionRegimen
EDR)', 'null', 'entidad.codigo,determinacion.codigo
', 'Entidad %1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (12, 4, 1, true, '2.1.1', 'El tramite debe contener al menos una determinacion distinta de la determinación virtual', 'Select Distinct cast(T.codigofip as varchar) From planeamiento.Tramite T

Where
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And T.iden NOT In (Select D.idTramite From planeamiento.Determinacion D
Where D.idCaracter<>16)', 'null', 'tramite.codigo', 'Trámite %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (14, 4, 1, true, '2.1.2', 'El tramite debe contener al menos una entidad', 'Select Distinct cast(T.codigofip as varchar) From planeamiento.Tramite T

Where
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And T.iden NOT In (Select E.idTramite From planeamiento.Entidad E)', 'null', 'tramite.codigo', 'Trámite: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (28, 3, 1, true, '2.3.3', 'La geometría de las entidades debe ser correcta, según el criterio de la función IsValid de PostGIS.', 'Select Distinct cast(E.codigo as varchar) From planeamiento.Entidad E, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip)) 
And E.idTramite=T.iden
And E.iden In (
Select EPOL.idEntidad From planeamiento.EntidadPol EPOL Where IsValid(geom)=false
Union
Select ELIN.idEntidad From planeamiento.EntidadLin ELIN Where IsValid(geom)=false
Union
Select EPNT.idEntidad From planeamiento.EntidadPnt EPNT Where IsValid(geom)=false)', 'null', 'entidad.codigo', 'Entidad: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (18, 2, 1, true, NULL, 'No pueden estar asignadas como unidades, las determinaciones que no tengan carácter de UNIDAD.', 'Select Distinct DU.codigo, DU.nombre From planeamiento.Determinacion DU, planeamiento.Determinacion D, planeamiento.Relacion R, planeamiento.VectorRelacion VRD, planeamiento.VectorRelacion VRDU, planeamiento.Tramite T Where R.idDefRelacion=2 And VRD.idRelacion=R.iden And VRDU.idRelacion=R.iden And VRD.idDefVector=2 And VRDU.idDefVector=3 And VRD.valor=D.iden And VRDU.valor=DU.iden And DU.idCaracter<>18 And DU.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))', 'null', 'determinacion.codigo,determinacion.nombre', 'Determinación: %1, Nombre: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (32, 2, 1, true, '2.4.2', 'Una determinación no puede tener propiedades de unidad si no tiene carácter ‘Unidad’.', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden
And D.idCaracter<>18
And D.iden In (Select VR.valor From planeamiento.VectorRelacion VR, diccionario.DefVector DV 
Where DV.idDefRelacion=1 And VR.idDefVector=DV.iden)', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (30, 3, 1, true, '2.3.5', 'Todas las entidades deben tener una, y sólo una, aplicación a una determinación de carácter ‘Grupo de Entidades’, con un único caso y valor de referencia.', 'Select Distinct cast(E.codigo as varchar) From planeamiento.Entidad E, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip)) 
And E.idTramite=T.iden
And (E.iden, 1, 1) Not In (
Select ED.idEntidad, Count(CED.iden), Count(EDR.iden) From planeamiento.EntidadDeterminacion ED,
planeamiento.Determinacion D, planeamiento.CasoEntidadDeterminacion CED,
planeamiento.EntidadDeterminacionRegimen EDR 
Where (EDR.idCaso=CED.iden Or EDR.idCasoAplicacion=CED.iden)
And CED.idEntidadDeterminacion=ED.iden And ED.idDeterminacion=D.iden And D.idCaracter=20
Group By ED.idEntidad Having Count(*)=1)', 'null', 'entidad.codigo', 'Entidad: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (31, 2, 1, true, '2.4.1', 'Una determinación no puede tener asignada como unidad, una determinación cuyo carácter no sea ‘Unidad’.', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden
And D.iden In (
Select VR1.valor From planeamiento.VectorRelacion VR1, planeamiento.VectorRelacion VR2, planeamiento.Determinacion D
Where VR1.idDefVector=2 And VR2.idDefVector=3 And VR1.idRelacion=VR2.idRelacion And VR2.valor=D.iden
And D.idCaracter<>18)', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (47, 1, 1, true, '2.5.6', 'Un caso debe tener, al menos, un documento y/o un valor.', 'Select Distinct cast(E.codigo as varchar) As ent_codigo, cast(D.codigo as varchar) As det_codigo  From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And CED.idEntidadDeterminacion=ED.iden
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And CED.iden Not In (Select DC.idCaso From planeamiento.DocumentoCaso DC)
And CED.iden Not In (Select EDR.idCaso From planeamiento.EntidadDeterminacionRegimen EDR
Union 
Select EDR.idCasoAplicacion From planeamiento.EntidadDeterminacionRegimen EDR)', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: %1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (34, 2, 1, true, '2.4.4', 'Las determinaciones que se aportan como regulación, deben tener carácter ‘Regulación’.', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden
And D.idCaracter<>13
And D.iden In (Select VR.valor From planeamiento.VectorRelacion VR
Where VR.idDefVector=Any(Array[9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39]))', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (35, 2, 1, true, '2.4.5', 'Las determinaciones que son valores de referencia de otras, deben tener carácter ‘Valor de Referencia’.', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T,
planeamiento.OpcionDeterminacion OD
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden
And D.idCaracter<>12
And OD.idDeterminacionValorRef=D.iden', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (38, 2, 1, true, '2.4.8', 'Las determinaciones de carácter ‘Norma General Literal’, ‘Acto de Ejecución’, ‘Uso’, ‘Grupo de Actos’, ‘Grupo de Usos’, ‘Régimen de Actos’ o ‘Régimen de Usos’, serán aplicables, al menos, a una  determinación que sea grupo de entidad.', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden
And D.idCaracter=Any(Array[2, 7, 8, 9, 10, 14, 15])
And D.iden Not In (Select DGE.idDeterminacion From planeamiento.DeterminacionGrupoEntidad DGE)', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (39, 2, 1, true, '2.4.9', 'Las determinaciones de carácter ‘Regulación’ deben tener texto de regulación específica.', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And Trim(Coalesce(D.texto,''''))='''' And D.idCaracter=13', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (40, 2, 1, true, '2.4.10', 'A una determinación no se le puede asignar más de un documento del mismo nombre, grupo y tipo.', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D Where D.iden In (
Select D.iden From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.Documento DOC, planeamiento.DocumentoDeterminacion DD
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And DD.idDeterminacion=D.iden And DD.idDocumento=DOC.iden
Group By D.iden, DOC.nombre, DOC.idTipoDocumento, DOC.idGrupoDocumento
Having Count(*)>1)', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (41, 2, 1, true, '2.4.11', 'No pueden existir dos determinaciones con el mismo nombre, carácter, unidad y precedente.', 'Select Distinct cast(D1.codigo as varchar) From planeamiento.Determinacion D1, planeamiento.Tramite T, planeamiento.Determinacion D2
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D1.idTramite=T.iden And D2.idTramite=T.iden
And D1.idCaracter=D2.idCaracter And Coalesce(D1.idPadre, 0)=Coalesce(D2.idPadre, 0)
And Trim(Upper(Coalesce(D1.nombre, '''')))=Trim(Upper(Coalesce(D2.nombre, '''')))
And D1.iden<>D2.iden
And (D1.iden, D2.iden) In (Select VR1.valor, VR3.valor From planeamiento.VectorRelacion VR1, planeamiento.VectorRelacion VR2,
planeamiento.VectorRelacion VR3, planeamiento.VectorRelacion VR4
Where VR1.idRelacion=VR2.idRelacion And VR3.idRelacion=VR4.idRelacion
And VR1.valor=D1.iden And VR3.valor=D2.iden And VR2.valor=VR4.valor
And VR1.idDefVector=2 And VR3.idDefVector=2 And VR2.idDefVector=3 And VR4.idDefVector=3)', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (45, 1, 1, true, '2.5.5', 'A un caso no se le puede asignar más de un documento del mismo nombre, grupo y tipo.', 'Select Distinct CED.iden, CED.identidaddeterminacion From planeamiento.CasoEntidadDeterminacion CED Where CED.iden In (
Select CED.iden From planeamiento.CasoEntidadDeterminacion CED, planeamiento.Tramite T, planeamiento.Documento DOC, planeamiento.DocumentoCaso DC,
planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And CED.idEntidadDeterminacion=ED.iden And ED.idEntidad=E.iden
And DC.idCaso=CED.iden And DC.idDocumento=DOC.iden
Group By CED.iden, DOC.nombre, DOC.idTipoDocumento, DOC.idGrupoDocumento
Having Count(*)>1)', 'null', 'casoentidaddeterminacion.iden, casoentidaddeterminacion.identidaddeterminacion', 'Caso: %1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (42, 3, 1, true, '2.5.3', 'Una entidad no puede tener aplicada dos veces la misma determinación.', 'Select Distinct cast(E.codigo as varchar) As ent_codigo, cast(D.codigo as varchar) As det_codigo From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And (E.iden, D.iden) In (Select ED.idEntidad, ED.idDeterminacion From planeamiento.EntidadDeterminacion ED
Group By ED.idEntidad, ED.idDeterminacion Having Count(*)>1)', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: %1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (55, 1, 1, true, '2.5.11', 'Una determinación no puede estar aplicada en algunos casos con valores de referencia, y con valores literales en otros.', 'Select Distinct cast(E.codigo as varchar) As Ent_codigo, cast(D.codigo as varchar) As Det_codigo From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED, planeamiento.EntidadDeterminacionRegimen EDR,
planeamiento.OpcionDeterminacion OD
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And CED.idEntidadDeterminacion=ED.iden
And (EDR.idCaso=CED.iden Or EDR.idCasoAplicacion=CED.iden)
And EDR.idOpcionDeterminacion=OD.iden And OD.idDeterminacion=D.iden
And EDR.valor Is Not Null', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: %1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (52, 2, 1, true, '2.5.9', 'Las determinaciones de carácter ‘Régimen de Uso’ o ‘Régimen de Acto’ que se expresen mediante valores de referencia, sólo podrán estar aplicadas con alguno de ellos.', 'Select Distinct cast(E.codigo as varchar) As Ent_codigo, cast(D.codigo as varchar) As Det_codigo From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED, planeamiento.EntidadDeterminacionRegimen EDR,
planeamiento.OpcionDeterminacion OD
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden And EDR.idDeterminacionRegimen=D.iden
And CED.idEntidadDeterminacion=ED.iden
And (EDR.idCaso=CED.iden Or EDR.idCasoAplicacion=CED.iden)
And EDR.idOpcionDeterminacion=OD.iden And OD.idDeterminacion<>D.iden
And D.idCaracter=Any(Array[7, 8])
Union
Select Distinct E.codigo As Ent_codigo, D.codigo As Det_codigo From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED, planeamiento.EntidadDeterminacionRegimen EDR,
planeamiento.OpcionDeterminacion OD
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden And EDR.idDeterminacionRegimen=D.iden
And CED.idEntidadDeterminacion=ED.iden
And (EDR.idCaso=CED.iden Or EDR.idCasoAplicacion=CED.iden)
And EDR.valor Is Not Null
And D.idCaracter=Any(Array[7, 8])
And OD.idDeterminacion=D.iden', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad:%1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (53, 1, 1, true, '2.5.10', 'El caso de aplicación de una condición urbanística debe pertenecer a la misma entidad que el caso principal.', 'Select Distinct cast(E1.codigo as varchar) As Ent_codigo, cast(D1.codigo as varchar) As Det_codigo From planeamiento.Entidad E1, planeamiento.EntidadDeterminacion ED1, planeamiento.Determinacion D1, 
planeamiento.Entidad E2, planeamiento.EntidadDeterminacion ED2, planeamiento.Determinacion D2,
planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED1, planeamiento.CasoEntidadDeterminacion CED2,
planeamiento.EntidadDeterminacionRegimen EDR1, planeamiento.EntidadDeterminacionRegimen EDR2
Where E1.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED1.idEntidad=E1.iden And ED1.idDeterminacion=D1.iden
And CED1.idEntidadDeterminacion=ED1.iden
And EDR1.idCaso=CED1.iden 
And ED2.idEntidad=E2.iden And ED2.idDeterminacion=D2.iden
And CED2.idEntidadDeterminacion=ED2.iden
And EDR2.idCasoAplicacion=CED2.iden
And (E1.iden<>E2.iden Or D1.iden<>D2.iden)', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad:%1, Determinación:%2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (60, 6, 1, true, '2.6.4', 'El orden de operación debe ser único para cada trámite, considerando las operaciones entre determinaciones, entidades y relaciones.
', 'Select Distinct OP.iden, OP.texto From planeamiento.Operacion OP, planeamiento.Tramite T
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And OP.idTramiteOrdenante=T.iden
And (OP.orden, OP.idTramiteOrdenante) In (Select OP.orden, OP.idTramiteOrdenante From planeamiento.Operacion OP, planeamiento.Tramite T
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And OP.idTramiteOrdenante=T.iden
Group By OP.orden, OP.idTramiteOrdenante Having Count(*)>1)', 'null', 'operacion.iden,operacion.texto', 'Operación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (58, 6, 1, true, '2.6.3', 'Las operaciones entre determinaciones sólo pueden efectuarse entre los caracteres permitidos en el diccionario.', 'Select Distinct OPD.iden, TRD.texto, cast(D1.codigo as varchar), cast(D2.codigo as varchar) From planeamiento.Determinacion D1, planeamiento.Tramite T, planeamiento.Determinacion D2,
planeamiento.OperacionDeterminacion OPD, diccionario.TipoOperacionDeterminacion TOD, diccionario.Traduccion TRD, 
planeamiento.Operacion OP
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And OP.idTramiteOrdenante=T.iden
And OPD.idOperacion=OP.iden And OPD.idTipoOperacionDet=TOD.iden And TOD.idLiteral=TRD.idLiteral
And OPD.idDeterminacion=D1.iden And OPD.idDeterminacionOperadora=D2.iden
And (D1.idCaracter, D2.idCaracter, OPD.idTipoOperacionDet) Not In (Select OC.idCaracterOperado,
OC.idCaracterOperador, OC.idTipoOperacionDet From diccionario.OperacionCaracter OC)', 'null', 'operaciondeterminacion.iden,traduccion.texto,determinacion.codigo,determinacion.codigo', 'Operación: %2, Determinación: %3, Determinación: %4 ');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (64, 6, 1, true, '2.6.5', 'No se pueden operaciones de creación de adscripción entre entidades de un mismo trámite.', 'Select Distinct R.iden From planeamiento.Relacion R, planeamiento.Tramite T, planeamiento.VectorRelacion VR1, planeamiento.VectorRelacion VR2,
planeamiento.Entidad E1, planeamiento.Entidad E2, planeamiento.OperacionRelacion OPR
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And R.idTramiteCreador=T.iden And R.idDefRelacion=3
And VR1.idRelacion=R.iden And VR2.idRelacion=R.iden
And VR1.idDefVector=4 And VR2.idDefVector=5
And VR1.valor=E1.iden And VR2.valor=E2.iden
And E1.idTramite=E2.idTramite
And OPR.idRelacion=R.iden And OPR.idTipoOperacionRel=2', 'null', 'relacion.iden', 'Relación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (2, 2, 1, true, NULL, 'Si la determinacion es un Enunciado, Valor de Referencia, Tipo de Adscripcion,  Unidad, Enunciado Complementario o Regulacion, no tendrá entidad de aplicación', 'Select cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T,
planeamiento.EntidadDeterminacion ED

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And ED.idDeterminacion=D.iden

And D.idCaracter=Any(Array[1, 11, 12, 13, 18, 19])

Union

Select D.codigo From planeamiento.Determinacion D, planeamiento.Tramite T,
planeamiento.DeterminacionGrupoEntidad DGE

Where D.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And DGE.idDeterminacion=D.iden

And D.idCaracter=Any(Array[1, 11, 12, 13, 18, 19])', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (9, 1, 1, true, NULL, 'No puede haber un caso con mas de una asignacion al mismo documento', 'Select Distinct cast(E.codigo as varchar) As entidad_codigo, cast(D.codigo as varchar) As det_codigo From planeamiento.Entidad E,
planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 

planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED,
planeamiento.DocumentoCaso DC, planeamiento.Documento DOC

Where E.idTramite=T.iden And
Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))

And DC.idDocumento=DOC.iden And DC.idCaso=CED.iden

And CED.idEntidadDeterminacion=ED.iden

And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden

And (DC.idCaso, DC.idDocumento) In (

Select DC.idCaso, DC.idDocumento From planeamiento.DocumentoCaso DC

Group By DC.idCaso, DC.idDocumento

Having Count(*)>1)', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad %1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (36, 2, 1, true, '2.4.6', 'Las determinaciones de carácter ‘Enunciado’, ‘Valor de Referencia’, ‘Adscripción’, ‘Unidad’ y ‘Regulación’, no serán aplicables a determinaciones que sean grupo de entidad.', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.EntidadDeterminacion ED
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idDeterminacion=D.iden
And D.idCaracter=Any(Array[1, 11, 12, 13, 18, 19])
Union
Select D.codigo From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.DeterminacionGrupoEntidad DGE
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And DGE.idDeterminacion=D.iden
And D.idCaracter=Any(Array[1, 11, 12, 13, 18, 19])', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (37, 2, 1, true, '2.4.7', 'Sólo tendrán valor de referencia las determinaciones de carácter ‘Norma General Literal’, ‘Régimen de Uso’, ‘Régimen de Acto’ y ‘Grupo de Entidad’.', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.EntidadDeterminacion ED,
planeamiento.CasoEntidadDeterminacion CED, planeamiento.EntidadDeterminacionRegimen EDR
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idDeterminacion=D.iden
And D.idCaracter<>All(Array[2, 7, 8, 17, 20])
And (EDR.idCaso=CED.iden Or EDR.idCasoAplicacion=CED.iden)
And CED.idEntidadDeterminacion=ED.iden
And EDR.idOpcionDeterminacion Is Not Null
And EDR.idDeterminacionRegimen Is Null
Union
Select Distinct D.codigo From planeamiento.Determinacion D, planeamiento.Tramite T, planeamiento.OpcionDeterminacion OD
Where D.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idCaracter<>All(Array[2, 7, 8, 17, 20])
And OD.idDeterminacion=D.iden', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (65, 3, 2, true, '2.3.6', 'La geometría de las entidades debe ser correcta, según el criterio de la función IsSimple de PostGIS.', 'Select Distinct cast(E.codigo as varchar) From planeamiento.Entidad E, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip)) 
And E.idTramite=T.iden
And E.iden In (
Select EPOL.idEntidad From planeamiento.EntidadPol EPOL Where IsSimple(EPOL.geom)=false
Union
Select ELIN.idEntidad From planeamiento.EntidadLin ELIN Where IsSimple(ELIN.geom)=false
Union
Select EPNT.idEntidad From planeamiento.EntidadPnt EPNT Where IsSimple(EPNT.geom)=false)', 'null', 'entidad.codigo', 'Entidad: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (66, 3, 2, true, '2.3.7', 'Las geometrías poligonales de las entidades de un mismo grupo, no se deben solapar.', 'Select Distinct cast(E1.codigo as varchar) From planeamiento.Entidad E1, planeamiento.Entidad E2, planeamiento.Tramite T,
planeamiento.EntidadDeterminacion ED1, planeamiento.EntidadDeterminacion ED2, planeamiento.Determinacion D1, planeamiento.Determinacion D2,
planeamiento.CasoEntidadDeterminacion CED1, planeamiento.CasoEntidadDeterminacion CED2,
planeamiento.EntidadDeterminacionRegimen EDR1, planeamiento.EntidadDeterminacionRegimen EDR2,
planeamiento.OpcionDeterminacion OD1, planeamiento.OpcionDeterminacion OD2,
planeamiento.Determinacion DVR1, planeamiento.Determinacion DVR2
Where E1.idTramite=T.iden
And EDR1.idOpcionDeterminacion=OD1.iden And EDR2.idOpcionDeterminacion=OD2.iden
And OD1.idDeterminacionValorRef=DVR1.iden And OD2.idDeterminacionValorRef=DVR2.iden
And (DVR1.iden=DVR2.iden Or DVR1.idDeterminacionBase=DVR2.idDeterminacionBase Or Upper(Trim(DVR1.nombre))=Upper(Trim(DVR2.nombre)))
And (EDR1.idCaso=CED1.iden Or EDR1.idCasoAplicacion=CED1.iden) And (EDR2.idCaso=CED2.iden Or EDR2.idCasoAplicacion=CED2.iden)
And CED1.idEntidadDeterminacion=ED1.iden And CED2.idEntidadDeterminacion=ED2.iden
And ED1.idDeterminacion=D1.iden And ED2.idDeterminacion=D2.iden
And D1.idCaracter=20 And D2.idCaracter=20
And ED1.idEntidad=E1.iden And ED2.idEntidad=E2.iden
And (E1.iden, E2.iden) In (
Select Distinct EPOL1.idEntidad, EPOL2.idEntidad From planeamiento.EntidadPol EPOL1, planeamiento.EntidadPol EPOL2, planeamiento.Entidad E1, planeamiento.Entidad E2,
planeamiento.Tramite T
Where EPOL1.idEntidad=E1.iden And EPOL2.idEntidad=E2.iden
And E1.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And E1.idTramite=E2.idTramite
And E1.iden<>E2.iden
And ST_Relate(EPOL1.geom, EPOL2.geom,''T********'')
And EPOL1.iden In (Select EPOL.iden From planeamiento.EntidadPol EPOL Where IsValid(EPOL.geom)=true And IsSimple(EPOL.geom)=true)
And EPOL2.iden In (Select EPOL.iden From planeamiento.EntidadPol EPOL Where IsValid(EPOL.geom)=true And IsSimple(EPOL.geom)=true))', 'null', 'entidad.codigo', 'Entidad:%1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (33, 2, 1, true, '2.4.3', 'Las determinaciones de carácter ‘Unidad’ deben tener definición y abreviatura.', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T 
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
)', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (44, 2, 1, true, '2.5.4', 'Una determinación no puede estar aplicada a una entidad y no tener, al menos, un caso.', 'Select Distinct cast(E.codigo as varchar) As ent_codigo, cast(D.codigo as varchar) As det_codigo From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And ED.iden Not In (Select CED.idEntidadDeterminacion From planeamiento.CasoEntidadDeterminacion CED)', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: %1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (49, 2, 1, true, '2.5.7', 'Una determinación sólo podrá estar aplicada a las entidades cuyo ‘Grupo de Entidad’ esté entre los asignados como grupo de aplicación de dicha determinación.', 'Select Distinct cast(E.codigo as varchar) As Ent_codigo, cast(D.codigo as varchar) As Det_codigo From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.DeterminacionGrupoEntidad DGE
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And D.idCaracter<>20
And DGE.idDeterminacion=D.iden
And (E.iden, D.iden) Not In (
Select E.iden, DGE.idDeterminacion From planeamiento.Entidad E, planeamiento.DeterminacionGrupoEntidad DGE,
planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D,
planeamiento.CasoEntidadDeterminacion CED, planeamiento.EntidadDeterminacionRegimen EDR,
planeamiento.OpcionDeterminacion OD
Where ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And D.idCaracter=20
And CED.idEntidadDeterminacion=ED.iden
And (EDR.idCaso=CED.iden Or EDR.idCasoAplicacion=CED.iden)
And EDR.idOpcionDeterminacion=OD.iden
And OD.idDeterminacion=D.iden
And OD.idDeterminacionValorRef=DGE.idDeterminacionGrupo)', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad: %1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (50, 2, 1, true, '2.5.8', 'Una determinación de carácter ‘Norma General Literal’ no podrá estar aplicada con un valor diferente a alguno de sus valores de referencia, salvo que la condición urbanística tenga una determinación de régimen.', 'Select Distinct cast(E.codigo as varchar) As Ent_codigo, cast(D.codigo as varchar) As Det_codigo From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED, planeamiento.EntidadDeterminacionRegimen EDR,
planeamiento.OpcionDeterminacion OD
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And CED.idEntidadDeterminacion=ED.iden
And (EDR.idCaso=CED.iden Or EDR.idCasoAplicacion=CED.iden)
And EDR.idOpcionDeterminacion=OD.iden And OD.idDeterminacion<>D.iden
And D.idCaracter=2
And EDR.idDeterminacionRegimen Is Null
Union
Select Distinct E.codigo As Ent_codigo, D.codigo As Det_codigo From planeamiento.Entidad E, planeamiento.EntidadDeterminacion ED, planeamiento.Determinacion D, 
planeamiento.Tramite T, planeamiento.CasoEntidadDeterminacion CED, planeamiento.EntidadDeterminacionRegimen EDR,
planeamiento.OpcionDeterminacion OD
Where E.idTramite=T.iden And Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And ED.idEntidad=E.iden And ED.idDeterminacion=D.iden
And CED.idEntidadDeterminacion=ED.iden
And (EDR.idCaso=CED.iden Or EDR.idCasoAplicacion=CED.iden)
And EDR.valor Is Not Null
And D.idCaracter=2
And OD.idDeterminacion=D.iden
And EDR.idDeterminacionRegimen Is Null', 'null', 'entidad.codigo,determinacion.codigo', 'Entidad:%1, Determinación: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (11, 3, 1, true, NULL, 'Toda operacion de adscripcion debe tener Tipo, Cuantia y Unidad', 'Select Distinct EO.codigo As origen_codigo, ED.codigo As destino_codigo From planeamiento.Relacion R,
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

)', 'null', 'entidad.origen.codigo,entidad.destino.codigo', 'Entidad Origen: %1, Entidad Destino: %2');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (67, 2, 1, true, NULL, 'No puede haber una determinación con más de una unidad asignada', 'Select Distinct cast(D.codigo as varchar) From planeamiento.Determinacion D, planeamiento.Tramite T 
Where Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
And D.idTramite=T.iden AND D.idcaracter NOT IN (1,12,18)
And (SELECT count(valor) FROM planeamiento.vectorrelacion
	WHERE idRelacion IN 
	(SELECT idrelacion FROM planeamiento.vectorrelacion WHERE valor = D.iden AND iddefvector= 2)
	AND idrelacion in (SELECT idrelacion FROM planeamiento.relacion WHERE iddefrelacion = 2)
	AND iddefvector = 3) <> 1', 'null', 'determinacion.codigo', 'Determinación: %1');
INSERT INTO validacion (iden, tipovalidacion, tipoerror, activado, nemo, descripcion, sentencia, resultadoesperado, columnas, mensaje) VALUES (68, 3, 1, true, NULL, 'Una entidad no puede ser origen y destino de adscripciones', 'SELECT DISTINCT cast(E.codigo as varchar) FROM planeamiento.Entidad E, planeamiento.Tramite T, planeamiento.relacion rel, planeamiento.vectorrelacion vrel WHERE 
	E.idTramite=T.iden AND Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
	AND (vrel.iddefvector = 4) AND rel.iddefrelacion = 3 AND vrel.valor = E.iden AND vrel.idrelacion = rel.iden
INTERSECT
SELECT DISTINCT cast(E.codigo as varchar) FROM planeamiento.Entidad E, planeamiento.Tramite T, planeamiento.relacion rel, planeamiento.vectorrelacion vrel WHERE 
	E.idTramite=T.iden AND Upper(Trim(T.codigoFIP))=Upper(Trim(:codigoFip))
	AND (vrel.iddefvector = 5) AND rel.iddefrelacion = 3 AND vrel.valor = E.iden AND vrel.idrelacion = rel.iden', 'null', 'entidad.codigo', 'Entidad:%1');


-- Completed on 2012-01-12 18:44:38

--
-- PostgreSQL database dump complete
--

