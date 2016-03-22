/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
* sean aprobadas por la Comision Europea- versiones
* posteriores de la EUPL (la <<Licencia>>);
* Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
* Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
* SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
* ni implicitas.
* Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.consola.servlets;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author Arnaiz Consultores
 */
public class ServiciosConsola {

    private Map<String, String> hm = new HashMap<String, String>();

    public ServiciosConsola() {

        //SEGURIDAD ////////////////////////////////////////////////////////////////////////

        hm.put("VALIDAR_USUARIO", "ActionServletSeguridad");
        hm.put("USUARIO_ESTA_LOGUEADO", "ActionServletSeguridad");
        hm.put("CHECK_IS_ADMIN_USER", "ActionServletSeguridad");
        hm.put("CHECK_IS_GESTOR_PLANES_USER", "ActionServletSeguridad");
        hm.put("ALLOWED_MODULE", "ActionServletSeguridad");
        hm.put("GET_ROL_NEEDS_AMBITO","ActionServletSeguridad");
        hm.put("DISCONNECT_USER","ActionServletSeguridad");
        hm.put("GET_USUARIO","ActionServletSeguridad");


        //VISUALIZADOR RPM ////////////////////////////////////////////////////////////////////////
        hm.put("GET_ARBOL_PLANES", "ActionServletRPM");
        hm.put("BORRAR_TRAMITE", "ActionServletRPM");
        hm.put("OBTENER_DATOS_GENERALES_DE_UNA_DETERMINACION", "ActionServletRPM");
        hm.put("OBTENER_DATOS_GENERALES_DE_UN_TRAMITE", "ActionServletRPM");
        hm.put("OBTENER_DATOS_GENERALES_DE_UN_PLAN", "ActionServletRPM");
        hm.put("OBTENER_DETERMINACIONES_DE_UN_TRAMITE", "ActionServletRPM");
        hm.put("OBTENER_OPERACIONES_PLAN_OPERADO", "ActionServletRPM");
        hm.put("OBTENER_OPERACIONES_PLAN_OPERADOR", "ActionServletRPM");
        hm.put("OBTENER_DOCUMENTOS_DE_DETERMINACION", "ActionServletRPM");
        hm.put("OBTENER_OPERADORES_DE_UNA_DETERMINACION", "ActionServletRPM");
        hm.put("OBTENER_OPERADOS_POR_UNA_DETERMINACION", "ActionServletRPM");
        hm.put("GET_DATA", "ActionServletRPM");
        hm.put("GET_NODOS_AMBITOS", "ActionServletRPM");
        hm.put("GET_NODOS_PLANES_DE_AMBITO", "ActionServletRPM");
        hm.put("GET_NODOS_TRAMS_DE_PLAN", "ActionServletRPM");
        hm.put("GET_NODOS_PLANES_DE_PLAN", "ActionServletRPM");
        hm.put("GET_NODOS_INFO_DE_PLAN", "ActionServletRPM");
        hm.put("GET_NODOS_DETERM_RAIZ", "ActionServletRPM");
        hm.put("GET_NODOS_DETERM_DE_DETERM", "ActionServletRPM");
        hm.put("GET_NODOS_DETERM_DE_TRAMITE", "ActionServletRPM");
        hm.put("GET_NODOS_ENTIDAD_DE_TRAMITE", "ActionServletRPM");
        hm.put("GET_NODOS_ENTIDAD_DE_ENTIDAD", "ActionServletRPM");
        hm.put("GET_DETS_REGIMEN_DIRECTO_DE_ENTIDAD", "ActionServletRPM");
        hm.put("GET_DETS_USOS_DE_ENTIDAD", "ActionServletRPM");
        hm.put("GET_DETS_ACTOS_DE_ENTIDAD", "ActionServletRPM");
        hm.put("GET_WKT_FROM_ENTIDAD", "ActionServletRPM");
        hm.put("GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO", "ActionServletRPM");
        hm.put("GET_NODOS_DETS_HIJOS_POR_TIPO", "ActionServletRPM");
        hm.put("GET_NODOS_REGIMENES_DE_CASO_RPM", "ActionServletRPM");
        hm.put("GET_INFO_REGIMEN_RPM", "ActionServletRPM");
        hm.put("GET_NODOS_REGESP_DE_REGIMEN_RPM", "ActionServletRPM");
        hm.put("GET_NODOS_REGESP_HIJOS_RPM", "ActionServletRPM");
        hm.put("GET_DATA_VAL", "ActionServletRPM");
        hm.put("GET_TEXTO_REGDIR", "ActionServletRPM");
        hm.put("GET_DOC_URL", "ActionServletRPM");
        hm.put("ADD_PLAN", "ActionServletRPM");
        hm.put("ADD_TRAMITE", "ActionServletRPM");
        hm.put("ASOCIAR_OPERADOS_A_PLAN", "ActionServletRPM");
        hm.put("GET_INSTRUMENTOSTIPOOPERACION", "ActionServletRPM");
        hm.put("CHECK_NO_PROCEDE_EN_NINGUN_CASO", "ActionServletRPM");
        hm.put("GET_TEXT_NO_PROCEDE", "ActionServletRPM");
        hm.put("GET_NODOS_REGESP_DET_RPM", "ActionServletRPM");
        hm.put("GET_PROJECTION", "ActionServletRPM");
        hm.put("GET_ID_AMBITO_DE_ENTIDAD", "ActionServletRPM");
        hm.put("GET_CAPAS_WMS","ActionServletRPM");
        hm.put("GET_EXTENSION_DE_AMBITO", "ActionServletRPM");
        hm.put("GET_EXTENSION_DE_TRAMITE", "ActionServletRPM");
        hm.put("GET_ID_AMBITO_DE_TRAMITE", "ActionServletRPM");
        hm.put("GET_ID_AMBITO_DE_ENTIDAD", "ActionServletRPM");
        hm.put("GET_NODOS_INSTRUMENTOPLAN_DE_PLAN", "ActionServletRPM");
        hm.put("GET_PLANSHP_DE_TRAMITE", "ActionServletRPM");
        hm.put("GET_INSTRUMENTOSPLAN", "ActionServletRPM");
        hm.put("GET_NODOS_PLANES_APLICACION", "ActionServletRPM");
        hm.put("GET_NODOS_ENTIDADES_APLICACION", "ActionServletRPM");
        hm.put("GET_NODOS_CASOS_APLICACION", "ActionServletRPM");
        hm.put("GET_NODOS_VALORES_APLICACION", "ActionServletRPM");
        hm.put("UPDATE_TRAMITE", "ActionServletRPM");
        hm.put("SEARCH_ON_TREE", "ActionServletRPM");    

        //VISUALIZADOR REFUNDIDO ///////////////////////////////////////////////////////////////
        hm.put("TRAMITE_ES_REFUNDIBLE", "ActionServletRefundido");
        hm.put("GET_INFO_AMBITO_REFUNDIDO", "ActionServletRefundido");
        hm.put("GET_INFO_AMBITO_REFUNDIDO_TABLE", "ActionServletRefundido");
        hm.put("GET_INFO_TRAMITE_REFUNDIDO", "ActionServletRefundido");
        hm.put("GET_INFO_TRAMITE_REFUNDIDO_TABLE", "ActionServletRefundido");
        hm.put("GET_TRAMITES_REFUNDIBLES_DE_AMBITO", "ActionServletRefundido");
        hm.put("LOG_REFUNDIDO", "ActionServletRefundido");
        hm.put("REFUNDIR_TRAMITES", "ActionServletRefundido");
        hm.put("GET_PROCESOS_REFUNDIDO", "ActionServletRefundido");
        hm.put("GET_ARCHIVO_REFUNDIDO", "ActionServletRefundido");


        //VISUALIZADOR GESTION USUARIOS ////////////////////////////////////////////////////////
        hm.put("wsNameService", "YourServlet");


        //GESTION DE DICCIONARIO ///////////////////////////////////////////////////////////////
        hm.put("ADD_ITEM_DIC", "ActionServletDic");
        hm.put("MOD_ITEM_DIC", "ActionServletDic");
        hm.put("DEL_ITEM_DIC", "ActionServletDic");
        hm.put("DIC_VISIBLE", "ActionServletDic");
        hm.put("GET_NAME", "ActionServletDic");
        hm.put("PLANBASE_VISIBLE", "ActionServletDic");

        //VISUALIZADOR GESTION USUARIOS ////////////////////////////////////////////////////////
        hm.put("FORM_USUARIO", "ActionServletUsuarios");
        hm.put("GET_LISTA_USUARIOS", "ActionServletUsuarios");
        hm.put("GUARDAR_USUARIO", "ActionServletUsuarios");
        hm.put("GET_LISTA_ROLES_USUARIO", "ActionServletUsuarios");
        hm.put("GET_LISTA_ROLES", "ActionServletUsuarios");
        hm.put("FORM_ROL", "ActionServletUsuarios");
        hm.put("GUARDAR_ROL", "ActionServletUsuarios");
        hm.put("GET_LISTA_AMBITOS", "ActionServletUsuarios");
        hm.put("GET_LISTA_AMBITOS_POR_NOMBRE", "ActionServletUsuarios");
        hm.put("EXISTE_NOMBRE_USUARIO", "ActionServletUsuarios");
        hm.put("GET_DATOS_USUARIO_JSON", "ActionServletUsuarios");
        hm.put("GET_AMBITO_ID", "ActionServletUsuarios");
        hm.put("BORRAR_USUARIO", "ActionServletUsuarios");
        hm.put("REACTIVA_USUARIO", "ActionServletUsuarios");

        //VISUALIZADOR DE CONSOLIDACION /////////////////////////////////////////////////////////
        hm.put("CONSOLIDAR_TRAMITES", "ActionServletConsolidacion");
        hm.put("LOG_CONSOLIDACION", "ActionServletConsolidacion");

        ///GENERADOR DE FIP1
        hm.put("GENERAR_FIP1", "ActionServletFip1");

        //PLAN BASE ////////////////////////////////////////////////////////////////////////////
        hm.put("GET_NODOS_PLAN_BASE", "ActionServletPlanBase");
        hm.put("GET_NODOS_TRAMS_DE_PLAN_BASE", "ActionServletPlanBase");
        hm.put("GET_NODOS_INFO_DE_TRAM", "ActionServletPlanBase");
        hm.put("GUARDAR_DET_PLAN_BASE", "ActionServletPlanBase");
        hm.put("GUARDAR_ENT_PLAN_BASE", "ActionServletPlanBase");
        hm.put("GET_TRAMID_DE_ENT", "ActionServletPlanBase");
        hm.put("GET_TRAMID_DE_DET", "ActionServletPlanBase");
        hm.put("BORRAR_DET_PLAN_BASE", "ActionServletPlanBase");
        hm.put("BORRAR_ENT_PLAN_BASE", "ActionServletPlanBase");

        //Visualizador Diario de Operaciones

        hm.put("GET_DIA_OP", "ActionServletDiarioDeOperaciones");
        hm.put("GET_COLUMNMODEL_DIA_OP", "ActionServletDiarioDeOperaciones");
        hm.put("GET_LISTA_SUBSISTEMAS", "ActionServletDiarioDeOperaciones");
        hm.put("DOWNLOAD_LOG_OP", "ActionServletDiarioDeOperaciones");
        hm.put("DOWNLOAD_FIP2","ActionServletDiarioDeOperaciones");
        hm.put("GET_TYPE_FILES","ActionServletDiarioDeOperaciones");
        hm.put("DOWNLOAD_FILE","ActionServletDiarioDeOperaciones");

        //CONFIGURADOR VISOR
        hm.put("CROSS-DOMAIN", "ActionServletConfig");
        hm.put("GET_AMBITOS_CONFIGURACION", "ActionServletConfig");
        hm.put("GET_WMS_SERVER_DE_ENT", "ActionServletConfig");
        hm.put("GET_LAYERCONFIG_DE_AMBITO", "ActionServletConfig");
        hm.put("SAVE_LAYERCONFIG_DE_AMBITO", "ActionServletConfig");
        hm.put("GET_TEXTO_SEGUN_IDIOMA", "ActionServletConfig");
        

        //Validacion
        hm.put("GET_UPLOAD_STATUS", "ActionServletValidacionFIP");
        hm.put("UPLOAD_FIP_FILE", "ActionServletValidacionFIP");
        hm.put("START_VALIDATION","ActionServletValidacionFIP");
        hm.put("GET_VALIDATION_STATUS","ActionServletValidacionFIP");
        hm.put("DESCARGAR_INFORME_VALIDACION","ActionServletValidacionFIP");
        hm.put("DESCARGAR_FIP","ActionServletValidacionFIP");
        
        // Gestión de planes
        hm.put("CREAR_PUBLICACION","ActionServletGestionPlanes");
        hm.put("MODIFICAR_PUBLICACION","ActionServletGestionPlanes");
        hm.put("BORRAR_PUBLICACION","ActionServletGestionPlanes");

        //CONFIGURADOR FICHAS
        hm.put("GET_NODOS_FICHAS_DE_TRAM", "ActionServletConfiguradorFichas");
        hm.put("GET_GRUPOS_FICHA", "ActionServletConfiguradorFichas");
        hm.put("GET_GRUPOS_FROM_CONJUNTO_FICHA", "ActionServletConfiguradorFichas");
        hm.put("GET_DETERMINACIONES_FROM_GRUPO_DETERMINACION", "ActionServletConfiguradorFichas");
        hm.put("GET_VALORES_FICHA_DET_CLASIF_USO", "ActionServletConfiguradorFichas");
        hm.put("GET_VALORES_FICHA_DET_CLASIF_ACTO", "ActionServletConfiguradorFichas");
        hm.put("GUARDAR_FICHA", "ActionServletConfiguradorFichas");
        hm.put("DEL_FICHA", "ActionServletConfiguradorFichas");
        hm.put("GUARDAR_CONJUNTO_FICHA", "ActionServletConfiguradorFichas");
        hm.put("GUARDAR_GRUPO_CONJUNTO_FICHA", "ActionServletConfiguradorFichas");
        hm.put("DEL_GRUPO_FICHA", "ActionServletConfiguradorFichas");
        hm.put("DEL_CAPA_FICHA", "ActionServletConfiguradorFichas");
        hm.put("GUARDAR_GRUPO_DETERMINACION_FICHA", "ActionServletConfiguradorFichas");
        hm.put("DEL_GRUPO_DETERMINACION_FICHA", "ActionServletConfiguradorFichas");
        hm.put("GUARDAR_DETERMINACION_GRUPO_DET", "ActionServletConfiguradorFichas");
        hm.put("DEL_DETERMINACION_GRUPO_FICHA", "ActionServletConfiguradorFichas");
        hm.put("GUARDAR_DET_CLASIF_FICHA", "ActionServletConfiguradorFichas");
        hm.put("DEL_DETERMINACION_CLASIF_USO", "ActionServletConfiguradorFichas");
        hm.put("DEL_DETERMINACION_CLASIF_ACTO", "ActionServletConfiguradorFichas");
        hm.put("GUARDAR_VALOR_DET_CLASIF_FICHA", "ActionServletConfiguradorFichas");
        hm.put("DEL_VALOR_DETERMINACION_CLASIF_USO", "ActionServletConfiguradorFichas");
        hm.put("DEL_VALOR_DETERMINACION_CLASIF_ACTO", "ActionServletConfiguradorFichas");
        hm.put("GUARDAR_DET_REGIMEN_FICHA", "ActionServletConfiguradorFichas"); 
        hm.put("DEL_DETERMINACION_REGIMEN_ACTO", "ActionServletConfiguradorFichas"); 
        hm.put("DEL_DETERMINACION_REGIMEN_USO", "ActionServletConfiguradorFichas"); 
        hm.put("SUBIR_ELEMENTO_FICHA", "ActionServletConfiguradorFichas"); 
        hm.put("BAJAR_ELEMENTO_FICHA", "ActionServletConfiguradorFichas");
        hm.put("GET_URL_FICHAS", "ActionServletConfiguradorFichas");    
        hm.put("GET_PLANTILLAS_FICHAS", "ActionServletConfiguradorFichas"); 
        
        //PREVIEW DE VISOR
        hm.put("GET_PERFILES_VISOR", "ActionServletPreviewVisor"); 
        hm.put("GET_CAPAS_VISOR", "ActionServletPreviewVisor"); 
        hm.put("GET_INFO_PREVIEW_VISOR", "ActionServletPreviewVisor"); 
        hm.put("CROSS-DOMAIN", "ActionServletPreviewVisor"); 
        hm.put("GET_GRUPOS_TRAMITE", "ActionServletPreviewVisor"); 
        hm.put("CREAR_WMS", "ActionServletPreviewVisor");
    }

    public String getServive(String servicio) {
        return (String) hm.get(servicio);
    }
}
