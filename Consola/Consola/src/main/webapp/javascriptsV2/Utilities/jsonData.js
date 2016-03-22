
//Diccionarios de datos disponibles: PlanEsOperadoPor

var dictionaryAdministrationNames = 
	{'data':[
		{"id": "Boletin","name": "Boletin"},
		{"id": "CaracterDeterminacion","name": "Carácter Determinación"},
		{"id": "CentroProduccion","name": "Centro  de Producción"},
		{"id": "GrupoDocumento","name": "Grupo Documento"},
		{"id": "InstrumentoPlan","name": "Instrumento Plan"},
		{"id": "InstrumentoTipoOperacion","name": "Instrumento Tipo Operación"},
		{"id": "Naturaleza","name": "Naturaleza"},
		{"id": "OperacionCaracter","name": "Operación Caracter"},
		{"id": "Organo","name": "Órgano"},
		{"id": "TipoDocumento","name": "Tipo de Documento"},	
		{"id": "TipoOperacionPlan","name": "Tipo Operación Plan"},
		{"id": "TipoTramite","name": "Tipo de Trámite"}
	]};

var dictionarytypeBoletin =
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nombre","header":"Nombre"}];

var dictionaryTypeCaracterDeterminacion = 
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nmaxaplicaciones","header":"MaxAplicaciones"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nminaplicaciones","header":"MinAplicaciones"}];
	
var dictionaryTypeCentroProduccion =
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","hidden":true,"width":100,"dataIndex":"codigo","header":"Codigo"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nombre","editable":false,"header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":220,"dataIndex":"mail","header":"Email"}];

var dictionaryTypeGrupoDocumento = 
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nombre","header":"Nombre"}];

var dictionaryTypeInstrumentoPlan =
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":100,"dataIndex":"nemo","header":"Abreviatura"},
	 {"dataType":"string","refIdCol":"iden","isSelection":true,"hidden":false,"width":300,"dataIndex":"naturaleza","refNameCol":"nombre","header":"Naturaleza","refType":"Naturaleza"}];

var dictionaryTypeInstrumentoTipoOperacion = 
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","refIdCol":"iden","isSelection":true,"hidden":false,"width":300,"dataIndex":"tipooperacionplan","refNameCol":"nombre","header":"Tipo de operacion del plan","refType":"TipoOperacionPlan"},
	 {"dataType":"string","refIdCol":"iden","isSelection":true,"hidden":false,"width":300,"dataIndex":"instrumentoplan","refNameCol":"nombre","header":"Instrumento de plan","refType":"InstrumentoPlan"}];

var dictionaryTypeNaturaleza = 
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nombre","header":"Nombre"}];

var dictionaryTypeOperacionCaracter =
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","refIdCol":"iden","isSelection":true,"hidden":false,"width":300,"dataIndex":"idCaracterOperado","refNameCol":"nombre","header":"Caracter Operado","refType":"CaracterDeterminacion"},
	 {"dataType":"string","refIdCol":"iden","isSelection":true,"hidden":false,"width":300,"dataIndex":"idCaracterOperador","refNameCol":"nombre","header":"Caracter Operador","refType":"CaracterDeterminacion"},
	 {"dataType":"string","refIdCol":"iden","isSelection":true,"hidden":false,"width":300,"dataIndex":"idTipoOperacionDet","refNameCol":"nombre","header":"Tipo Operacion Det","refType":"TipoOperacionDeterminacion"}];

var dictionaryTypeOrgano = 
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nombre","header":"Nombre"}];

var dictionaryTypeTipoDocumento =
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nombre","header":"Nombre"}];

var dictionaryTypeTipoOperacionPlan = 
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nombre","header":"Nombre"}];

var dictionaryTypeTipoTramite =
	[{"dataType":"number","hidden":true,"width":126,"dataIndex":"iden","header":"Id"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"nombre","header":"Nombre"}];

var operationDailyGridColumns = 
	[{"dataType":"string","hidden":false,"width":100,"dataIndex":"user","header":"Usuario"},
	 {"dataType":"string","hidden":false,"width":180,"dataIndex":"fecha","header":"Fecha"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"operacion","header":"Operación"},
	 {"dataType":"string","hidden":false,"width":150,"dataIndex":"subsistema","header":"Subsistema"}];
	 //{"dataType":"String","hidden":true,"width":80,"dataIndex":"documentos","header":"Documentos"}];

var refundidoGridColumns = 
	[{"dataType":"string","hidden":false,"width":80,"dataIndex":"idTramite","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":180,"dataIndex":"nombreTramite","header":"Trámite"},
	 {"dataType":"string","hidden":false,"width":60,"dataIndex":"idPlan","header":"Id. Plan"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"nombrePlan","header":"Nombre del plan"}];

var refundidoTypeFromAmbito = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"idAmb","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombreAmb","header":"Ámbito"},
	 {"dataType":"string","hidden":false,"width":50,"dataIndex":"numPlanes","header":"Total de planes"},
	 {"dataType":"string","hidden":false,"width":50,"dataIndex":"numPlanesRef","header":"Planes refundibles"},
	 {"dataType":"string","hidden":false,"width":50,"dataIndex":"numTrams","header":"Total de trámites"},
	 {"dataType":"string","hidden":false,"width":50,"dataIndex":"numTramsRef","header":"Trámites refundibles"}];

var refundidoTypeFromTramite = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"idTram","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombreTram","header":"Nombre"},
	 {"dataType":"string","hidden":true,"width":64,"dataIndex":"idPlan","header":"Plan"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombrePlan","header":"Plan"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombreTipoTramite","header":"Tipo"},
	 {"dataType":"date","hidden":false,"width":50,"dataIndex":"fecha","header":"Fecha"},
	 {"dataType":"string","hidden":false,"width":50,"dataIndex":"refundible","header":"Refundible"}];

var ViewerPlanesTypePlan = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":100,"dataIndex":"codigo","header":"Código"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"ambito","header":"Ámbito"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"instrumento","header":"Instrumento"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"orden","header":"Orden"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"texto","header":"Texto"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"planPadre","header":"Plan Padre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"planBase","header":"Plan Base"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"suspendida","header":"Suspendido"}];

var viewerPlanesTypePlanEsOperadoPor = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"instrumento","header":"Instrumento"},
	 {"dataType":"string","hidden":false,"width":250,"dataIndex":"planOperador","header":"Plan Operador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"operacion","header":"Operación"}];

var viewerPlanesTypePlanOperaA = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"instrumento","header":"Instrumento"},
	 {"dataType":"string","hidden":false,"width":300,"dataIndex":"planOperado","header":"Plan operado"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"operacion","header":"Operación"}];

var viewerPlanesTypeTramite = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"ambito","header":"Ámbito de Aplicación"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"tipoTramite","header":"Tipo de Trámite"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"organo","header":"Órgano"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"sentido","header":"Sentido"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"iteracion","header":"Iteración"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"fecha","header":"Fecha"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"texto","header":"Texto"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"comentario","header":"Comentario"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"centroProduccion","header":"Centro de Producción"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"codigoFip","header":"Código FIP"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"fechaConsolidacion","header":"Fecha de Consolidación"}];

var viewerPlanesTypePublicacionTramite = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"id","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"boletin","header":"Boletín"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"fecha","header":"Fecha"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"numero","header":"Número"}];

var viewerPlanesTypeAmbitoAplicacionTramite = 
	[{"dataType":"number","hidden":false,"width":50,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"nombre","header":"Ámbito"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"tipoambito","header":"Tipo"}];

var viewerPlanesTypeDocumentos = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":500,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"grupoDocumento","header":"Grupo Documento"},
	 {"dataType":"string","hidden":false,"width":100,"dataIndex":"tipoDocumento","header":"Tipo Documento"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"comentario","header":"Comentario"}];

var viewerPlanesTypeDeterminacion = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"codigo","header":"Código"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"apartado","header":"Apartado"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"caracter","header":"Caracter"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"unidad","header":"Unidad"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"texto","header":"Definición"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"etiqueta","header":"Etiqueta"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"detPadre","header":"Determinación Padre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"base","header":"Determinación Base"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"suspendida","header":"Suspendido"}];

var viewerPlanesTypeValoresReferencia = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"codigo","header":"Código"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"apartado","header":"Apartado"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"detPadre","header":"Determinación Padre"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"caracter","header":"Caracter"},	 
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"texto","header":"Texto"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"etiqueta","header":"Etiqueta"},	 
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"base","header":"Determinación Base"}];

var viewerPlanesTypeDocumentosDeterminacion = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":400,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":150,"dataIndex":"grupoDocumento","header":"Grupo Documento"},
	 {"dataType":"string","hidden":false,"width":110,"dataIndex":"tipoDocumento","header":"Tipo Documento"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"comentario","header":"Comentario"},
	 {"dataType":"string","hidden":true,"width":220,"dataIndex":"archivo","header":"Archivo"}];

var viewerPlanesTypeDeterminacionEsOperadoPor = 
	[{"dataType":"number","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"instrumento","header":"Instrumento"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"plan","header":"Plan"},
	 {"dataType":"string","hidden":false,"width":100,"dataIndex":"codigo","header":"Código"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"determ","header":"Determinación Operadora"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"tipo","header":"Tipo Operación"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"regimen","header":"Regimen"}];

var viewerPlanesTypeDeterminacionOperaA = 
	[{"dataType":"number","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"instrumento","header":"Instrumento"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"plan","header":"Plan"},
	 {"dataType":"string","hidden":false,"width":100,"dataIndex":"codigo","header":"Código"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"determ","header":"Determinación Operada"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"tipo","header":"Tipo Operación"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"regimen","header":"Regimen"}];

var viewerPlanesTypeAplicacionesDeDet = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"clave","header":"Clave"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"grupo","header":"Grupo"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"entPadre","header":"Padre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"etiqueta","header":"Etiqueta"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"codigo","header":"Código"}];

var viewerPlanesTypeGruposAplicacion = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"apartado","header":"Apartado"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"base","header":"Determinación Base"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"detPadre","header":"Determinación Padre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"plan","header":"Plan"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"caracter","header":"Caracter"},	 
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"texto","header":"Texto"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"etiqueta","header":"Etiqueta"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"codigo","header":"Código"}];

var viewerPlanesTypeDetReguladoras = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"apartado","header":"Apartado"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"detPadre","header":"Determinación Padre"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"caracter","header":"Caracter"},	 
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"texto","header":"Texto"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"etiqueta","header":"Etiqueta"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"codigo","header":"Código"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"base","header":"Determinación Base"}];

var viewerPlanesTypeEntidad = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"codigo","header":"Código"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"grupo","header":"Grupo"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"clave","header":"Clave"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"base","header":"Entidad base"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"etiqueta","header":"Etiqueta"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"suspendida","header":"Suspendido"}];

var viewerPlanesTypeRegimenDirectoDocumento = 
	 [{"dataType":"string","hidden":false,"width":164,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"tipoDocumento","header":"Tipo"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"comentario","header":"Comentario"}];

var viewerPlanesTypePropiedadesAdscripcionOrigen = 
	[{"dataType":"long","hidden":true,"width":40,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"origen","header":"Origen"},	 
	 {"dataType":"string","hidden":false,"width":60,"dataIndex":"tipo","header":"Tipo"},
	 {"dataType":"string","hidden":false,"width":60,"dataIndex":"cuantia","header":"Cuantía"},
	 {"dataType":"string","hidden":false,"width":60,"dataIndex":"unidad","header":"Unidad"},
	 {"dataType":"string","hidden":false,"width":100,"dataIndex":"texto","header":"Texto"}];

var viewerPlanesTypePropiedadesAdscripcionDestino = 
	[{"dataType":"long","hidden":true,"width":40,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"destino","header":"Destino"}, 
	 {"dataType":"string","hidden":false,"width":60,"dataIndex":"tipo","header":"Tipo"},
	 {"dataType":"string","hidden":false,"width":60,"dataIndex":"cuantia","header":"Cuantía"},
	 {"dataType":"string","hidden":false,"width":60,"dataIndex":"unidad","header":"Unidad"},
	 {"dataType":"string","hidden":false,"width":100,"dataIndex":"texto","header":"Texto"}];

var viewerPlanesTypeDocumentosEntidad = 
	[{"dataType":"string","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":164,"dataIndex":"nombre","header":"Nombre"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"grupoDocumento","header":"Grupo Documento"},
	 {"dataType":"string","hidden":false,"width":100,"dataIndex":"tipoDocumento","header":"Tipo Documento"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"comentario","header":"Comentario"},
	 {"dataType":"string","hidden":true,"width":220,"dataIndex":"archivo","header":"Archivo"}];

var viewerTypeEntidadEsOperadoPor = 
	[{"dataType":"number","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"instrumento","header":"Instrumento"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"plan","header":"Plan"},
	 {"dataType":"string","hidden":false,"width":100,"dataIndex":"codigo","header":"Código"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"entidad","header":"Entidad Operadora"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"tipo","header":"Tipo Operación"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"regimen","header":"Regimen"}];

var viewerPlanesTypeEntidadOperaA = 
	[{"dataType":"number","hidden":true,"width":64,"dataIndex":"iden","header":"Identificador"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"instrumento","header":"Instrumento"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"plan","header":"Plan"},
	 {"dataType":"string","hidden":false,"width":100,"dataIndex":"codigo","header":"Código"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"entidad","header":"Entidad Operada"},
	 {"dataType":"string","hidden":false,"width":200,"dataIndex":"tipo","header":"Tipo Operación"},
	 {"dataType":"string","hidden":true,"width":164,"dataIndex":"regimen","header":"Regimen"}];


