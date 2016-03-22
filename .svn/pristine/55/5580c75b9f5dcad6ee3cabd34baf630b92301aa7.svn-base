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

package es.mitc.redes.urbanismoenred.servicios.refundido;

import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import org.apache.log4j.Logger;

/**
 *
 * @author Arnaiz Consultores
 */
public class ClsDatos{
    private static final Logger mLog = Logger.getLogger(ClsDatos.class);

    // Versión del programa de refundido
    public static final String VERSION_REFUNDIDO="1.9.60";

    // Versión del archivo de recursos requerido por el programa
    public static final String VERSION_REQUERIDA_RECURSOS="1.13.0";
    public static String VERSION_RECURSOS;

    // Versiones del modelo de base de datos requeridas por el programa
    public static final String VERSION_REQUERIDA_MODELO="3.1.1";
    public static String VERSION_MODELO;
    
    // *******************
    // Constantes globales
    // *******************
    
    public static boolean B_ELIMINAR_CARPETAS_VACIAS;

    public static int ID_TIPOOPERACIONPLAN_MODIFICACION;
    public static int ID_TIPOOPERACIONPLAN_DESARROLLO;
    public static int ID_TIPOOPERACIONPLAN_SUSPENSIONTOTAL;
    public static int ID_TIPOOPERACIONPLAN_SUSTITUCION;
    public static int ID_TIPOOPERACIONPLAN_NOPROCEDE;
    public static int ID_TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL;
    public static int ID_TIPOOPERACIONPLAN_SUSPENSIONPARCIAL;
    public static int ID_TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL;
    public static int ID_TIPOOPERACIONPLAN_INCORPORACION;
    public static int ID_TIPOOPERACIONPLAN_REFUNDIDO;

    public static int ID_TIPOTRAMITE_PLANBASE;
    public static int ID_TIPOTRAMITE_REFUNDIDO;
    
    public static int ID_INSTRUMENTOPLAN_REFUNDIDO;
    public static int ID_INSTRUMENTOPLAN_REFUNDIDO_PARCIAL;

    public static int ID_DEFRELACION_REGULACIONESPECIFICA;
    public static int ID_DEFVECTOR_DETERMINACIONREGULADAESPECIFICA;
    public static int ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA;

    public static int ID_DEFRELACION_HERENCIACLAVE;
    public static int ID_DEFVECTOR_HERENCIACLAVE;
    public static int ID_DEFPROPIEDAD_HERENCIACLAVE;

    public static int ID_DEFRELACION_REGULACION[];
    public static int ID_DEFVECTOR_DETERMINACIONREGULADA[];
    public static int ID_DEFVECTOR_DETERMINACIONREGULADORA[];

    public static int ID_TIPOOPERACIONRELACION_ADICION;

    public static int ID_CARACTER_ENUNCIADO;
    public static int ID_CARACTER_ENUNCIADOCOMPLEMENTARIO;
    public static int ID_CARACTER_NORMAGENERALLITERAL;
    public static int ID_CARACTER_NORMAGENERALGRAFICA;
    public static int ID_CARACTER_USO;
    public static int ID_CARACTER_GRUPODEUSOS;
    public static int ID_CARACTER_ACTODEEJECUCION;
    public static int ID_CARACTER_VALORREFERENCIA;
    public static int ID_CARACTER_GRUPODEACTOS;
    public static int ID_CARACTER_OPERADORA;
    public static int ID_CARACTER_UNIDAD;
    public static int ID_CARACTER_GRUPODEENTIDADES;
    public static int ID_CARACTER_REGULACION;
    public static int ID_CARACTER_VIRTUAL;

    public static String CODIGO_DETERMINACIONBASE_GRUPO_CARPETA;
    public static String CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION;
    public static String CODIGO_DETERMINACIONBASE_GRUPO_OPERADOR;
    public static String CODIGO_DETERMINACIONBASE_GRUPO_AFECCION;

    public static String CODIFICACION;

    public static int LONG_CODIGO_DETERMINACION;
    public static int LONG_CODIGO_ENTIDAD;
    public static int LONG_CODIGO_PLAN;

    public static String TEXTO_APORTADAS;

    // Idioma
    public static String TEXTO_IDIOMA;

    // ******************
    // Variables globales
    // ******************
    
    static {
        try{
            // *******************
            // Constantes globales
            // *******************
            // Fichero de recursos
            VERSION_RECURSOS=Textos.getTexto("refundido", "VERSION_RECURSOS");

            B_ELIMINAR_CARPETAS_VACIAS=Boolean.parseBoolean(Textos.getTexto("refundido", "B_ELIMINAR_CARPETAS_VACIAS"));
            
            ID_TIPOOPERACIONPLAN_MODIFICACION=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONPLAN_MODIFICACION"));
            ID_TIPOOPERACIONPLAN_DESARROLLO=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONPLAN_DESARROLLO"));
            ID_TIPOOPERACIONPLAN_SUSPENSIONTOTAL=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONPLAN_SUSPENSIONTOTAL"));
            ID_TIPOOPERACIONPLAN_SUSTITUCION=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONPLAN_SUSTITUCION"));
            ID_TIPOOPERACIONPLAN_NOPROCEDE=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONPLAN_NOPROCEDE"));
            ID_TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL"));
            ID_TIPOOPERACIONPLAN_SUSPENSIONPARCIAL=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONPLAN_SUSPENSIONPARCIAL"));
            ID_TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL"));
            ID_TIPOOPERACIONPLAN_INCORPORACION=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONPLAN_INCORPORACION"));
            ID_TIPOOPERACIONPLAN_REFUNDIDO=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONPLAN_REFUNDIDO"));

            ID_TIPOTRAMITE_PLANBASE=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOTRAMITE_PLANBASE"));
            ID_TIPOTRAMITE_REFUNDIDO=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOTRAMITE_REFUNDIDO"));
            
            ID_INSTRUMENTOPLAN_REFUNDIDO=Integer.parseInt(Textos.getTexto("refundido", "ID_INSTRUMENTOPLAN_REFUNDIDO"));
            ID_INSTRUMENTOPLAN_REFUNDIDO_PARCIAL=Integer.parseInt(Textos.getTexto("refundido", "ID_INSTRUMENTOPLAN_REFUNDIDO_PARCIAL"));

            ID_DEFRELACION_REGULACIONESPECIFICA=Integer.parseInt(Textos.getTexto("refundido", "ID_DEFRELACION_REGULACIONESPECIFICA"));
            ID_DEFVECTOR_DETERMINACIONREGULADAESPECIFICA=Integer.parseInt(Textos.getTexto("refundido", "ID_DEFVECTOR_DETERMINACIONREGULADAESPECIFICA"));
            ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA=Integer.parseInt(Textos.getTexto("refundido", "ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA"));

            ID_DEFRELACION_HERENCIACLAVE=Integer.parseInt(Textos.getTexto("refundido", "ID_DEFRELACION_HERENCIACLAVE"));
            ID_DEFVECTOR_HERENCIACLAVE=Integer.parseInt(Textos.getTexto("refundido", "ID_DEFVECTOR_HERENCIACLAVE"));
            ID_DEFPROPIEDAD_HERENCIACLAVE=Integer.parseInt(Textos.getTexto("refundido", "ID_DEFPROPIEDAD_HERENCIACLAVE"));

            ID_DEFRELACION_REGULACION=valoresDeMatriz(Textos.getTexto("refundido", "ID_DEFRELACION_REGULACION[]"));
            ID_DEFVECTOR_DETERMINACIONREGULADA=valoresDeMatriz(Textos.getTexto("refundido", "ID_DEFVECTOR_DETERMINACIONREGULADA[]"));
            ID_DEFVECTOR_DETERMINACIONREGULADORA=valoresDeMatriz(Textos.getTexto("refundido", "ID_DEFVECTOR_DETERMINACIONREGULADORA[]"));

            ID_TIPOOPERACIONRELACION_ADICION=Integer.parseInt(Textos.getTexto("refundido", "ID_TIPOOPERACIONRELACION_ADICION"));

            ID_CARACTER_ENUNCIADO=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_ENUNCIADO"));
            ID_CARACTER_ENUNCIADOCOMPLEMENTARIO=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_ENUNCIADOCOMPLEMENTARIO"));
            ID_CARACTER_NORMAGENERALLITERAL=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_NORMAGENERALLITERAL"));
            ID_CARACTER_NORMAGENERALGRAFICA=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_NORMAGENERALGRAFICA"));
            ID_CARACTER_USO=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_USO"));
            ID_CARACTER_GRUPODEUSOS=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_GRUPODEUSOS"));
            ID_CARACTER_ACTODEEJECUCION=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_ACTODEEJECUCION"));
            ID_CARACTER_VALORREFERENCIA=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_VALORREFERENCIA"));
            ID_CARACTER_GRUPODEACTOS=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_GRUPODEACTOS"));
            ID_CARACTER_OPERADORA=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_OPERADORA"));
            ID_CARACTER_UNIDAD=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_UNIDAD"));
            ID_CARACTER_GRUPODEENTIDADES=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_GRUPODEENTIDADES"));
            ID_CARACTER_REGULACION=Integer.parseInt(Textos.getTexto("refundido", "ID_CARACTER_REGULACION"));

            CODIGO_DETERMINACIONBASE_GRUPO_CARPETA=Textos.getTexto("refundido", "CODIGO_DETERMINACIONBASE_GRUPO_CARPETA");
            CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION=Textos.getTexto("refundido", "CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION");
            CODIGO_DETERMINACIONBASE_GRUPO_OPERADOR=Textos.getTexto("refundido", "CODIGO_DETERMINACIONBASE_GRUPO_OPERADOR");
            CODIGO_DETERMINACIONBASE_GRUPO_AFECCION=Textos.getTexto("refundido", "CODIGO_DETERMINACIONBASE_GRUPO_AFECCION");

            CODIFICACION=Textos.getTexto("refundido", "CODIFICACION");

            LONG_CODIGO_DETERMINACION=Integer.parseInt(Textos.getTexto("refundido", "LONG_CODIGO_DETERMINACION"));
            LONG_CODIGO_ENTIDAD=Integer.parseInt(Textos.getTexto("refundido", "LONG_CODIGO_ENTIDAD"));
            LONG_CODIGO_PLAN=Integer.parseInt(Textos.getTexto("refundido", "LONG_CODIGO_PLAN"));

            TEXTO_APORTADAS=Textos.getTexto("refundido", "TEXTO_APORTADAS");

            // Idioma
            TEXTO_IDIOMA=Textos.getTexto("refundido", "TEXTO_IDIOMA");

            
        } catch (Exception e){
            mLog.error("Error en la carga de datos constantes. "  + e + ". Código 23053.");
        }
    }

    private static int[] valoresDeMatriz(String valores){
            int[] i=new int[valores.split(",").length];
            int j=0;

            try{
                for(String s: valores.split(",")){
                    i[j]=Integer.parseInt(s);
                    j++;
                }
            } catch(Exception e){
                mLog.error("Error en la carga de datos constantes. "  + e + ". Código 23053.");
                return null;
            }
            return i;
    }

}
