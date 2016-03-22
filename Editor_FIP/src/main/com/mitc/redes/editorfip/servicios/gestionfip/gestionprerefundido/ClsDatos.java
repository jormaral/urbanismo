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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.mitc.redes.editorfip.utilidades.Textos;

/**
 *
 * @author Miguel.Martin
 */
public class ClsDatos{
    private static final Logger mLog = Logger.getLogger(ClsMain.class);

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

    // Conexión a base de datos si no se usa Hibernate y se hace una conexión directa
    public static boolean B_USAR_HIBERNATE;
    public static String URL_CONEXION;
    
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

    public static int ID_TIPOTRAMITE_PLANBASE;
    public static int ID_TIPOTRAMITE_REFUNDIDO;
    
    public static int ID_INSTRUMENTOPLAN_REFUNDIDO;

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
  
    // Lista de trámites a refundir
    public static List <Integer> gListaIdTramitesRefundir=new ArrayList();

    // Lista de trámites Base. Los trámites que se van a refundir pueden
    //  usar diferentes trámites base que, incluso, pueden ser de difernetes
    //  planes.
    public static List <Integer> gListaIdTramitesBase=new ArrayList();
    
    // Lista de los documentos de cada trámite. Sólo los ligados a trámite, y no los de determinación, entidad y caso.
    public static List <Vector>gListaDatosDoc=new ArrayList();

    // Orden en el que se van a ejecutar las operaciones entre planes
    public static int gOrdenOperacion;

    // Código sin encriptar del trámite refundido a crear
    public static String gCodigoFIPrefundido;
    
    // Código del plan refundido creado
    public static String gCodigoPlanRefundido;
    
    // Dependencias entre tablas
    public static String[][] gDependencias;
    
    // Identificador del ámbito
    public static int gIdAmbito;
    
    @SuppressWarnings("empty-statement")
    public static boolean bCargarDatos(){
        String s;
        List lista;
        Object[] obj;
        Iterator it;
        int i;
        int nDep;
        
        try{
            // *******************
            // Constantes globales
            // *******************

            // Versiones de los modelos de base de datos y del fichero de recursos

            // Fichero de recursos
            VERSION_RECURSOS=Textos.getCadena("refundido", "VERSION_RECURSOS");

            // Opción de usar o no Hibernate
            B_USAR_HIBERNATE=Boolean.parseBoolean(Textos.getCadena("refundido", "B_USAR_HIBERNATE"));
            URL_CONEXION=Textos.getCadena("refundido", "URL_CONEXION");

            // Conectar a la base de datos
            if (ClsDatos.B_USAR_HIBERNATE == false) {
                ClsConexion.conectar();
                s = "Begin Transaction";
                ClsConexion.ejecutar(s);
            }

            // Modelo de datos de la BD
            lista=new ArrayList();
            s="Select version From diccionario.version Order By iden Desc";
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                mLog.warn ("No se ha conseguido leer la tabla Diccionario.VERSION");
            } else {
                VERSION_MODELO=lista.get(0).toString();
            }

            B_ELIMINAR_CARPETAS_VACIAS=Boolean.parseBoolean(Textos.getCadena("refundido", "B_ELIMINAR_CARPETAS_VACIAS"));
            
            ID_TIPOOPERACIONPLAN_MODIFICACION=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOOPERACIONPLAN_MODIFICACION"));
            ID_TIPOOPERACIONPLAN_DESARROLLO=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOOPERACIONPLAN_DESARROLLO"));
            ID_TIPOOPERACIONPLAN_SUSPENSIONTOTAL=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOOPERACIONPLAN_SUSPENSIONTOTAL"));
            ID_TIPOOPERACIONPLAN_SUSTITUCION=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOOPERACIONPLAN_SUSTITUCION"));
            ID_TIPOOPERACIONPLAN_NOPROCEDE=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOOPERACIONPLAN_NOPROCEDE"));
            ID_TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL"));
            ID_TIPOOPERACIONPLAN_SUSPENSIONPARCIAL=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOOPERACIONPLAN_SUSPENSIONPARCIAL"));
            ID_TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL"));
            ID_TIPOOPERACIONPLAN_INCORPORACION=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOOPERACIONPLAN_INCORPORACION"));

            ID_TIPOTRAMITE_PLANBASE=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOTRAMITE_PLANBASE"));
            ID_TIPOTRAMITE_REFUNDIDO=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOTRAMITE_REFUNDIDO"));
            
            ID_INSTRUMENTOPLAN_REFUNDIDO=Integer.parseInt(Textos.getCadena("refundido", "ID_INSTRUMENTOPLAN_REFUNDIDO"));

            ID_DEFRELACION_REGULACIONESPECIFICA=Integer.parseInt(Textos.getCadena("refundido", "ID_DEFRELACION_REGULACIONESPECIFICA"));
            ID_DEFVECTOR_DETERMINACIONREGULADAESPECIFICA=Integer.parseInt(Textos.getCadena("refundido", "ID_DEFVECTOR_DETERMINACIONREGULADAESPECIFICA"));
            ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA=Integer.parseInt(Textos.getCadena("refundido", "ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA"));

            ID_DEFRELACION_HERENCIACLAVE=Integer.parseInt(Textos.getCadena("refundido", "ID_DEFRELACION_HERENCIACLAVE"));
            ID_DEFVECTOR_HERENCIACLAVE=Integer.parseInt(Textos.getCadena("refundido", "ID_DEFVECTOR_HERENCIACLAVE"));
            ID_DEFPROPIEDAD_HERENCIACLAVE=Integer.parseInt(Textos.getCadena("refundido", "ID_DEFPROPIEDAD_HERENCIACLAVE"));

            ID_DEFRELACION_REGULACION=valoresDeMatriz(Textos.getCadena("refundido", "ID_DEFRELACION_REGULACION[]"));
            ID_DEFVECTOR_DETERMINACIONREGULADA=valoresDeMatriz(Textos.getCadena("refundido", "ID_DEFVECTOR_DETERMINACIONREGULADA[]"));
            ID_DEFVECTOR_DETERMINACIONREGULADORA=valoresDeMatriz(Textos.getCadena("refundido", "ID_DEFVECTOR_DETERMINACIONREGULADORA[]"));

            ID_TIPOOPERACIONRELACION_ADICION=Integer.parseInt(Textos.getCadena("refundido", "ID_TIPOOPERACIONRELACION_ADICION"));

            ID_CARACTER_ENUNCIADO=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_ENUNCIADO"));
            ID_CARACTER_ENUNCIADOCOMPLEMENTARIO=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_ENUNCIADOCOMPLEMENTARIO"));
            ID_CARACTER_NORMAGENERALLITERAL=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_NORMAGENERALLITERAL"));
            ID_CARACTER_NORMAGENERALGRAFICA=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_NORMAGENERALGRAFICA"));
            ID_CARACTER_USO=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_USO"));
            ID_CARACTER_GRUPODEUSOS=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_GRUPODEUSOS"));
            ID_CARACTER_ACTODEEJECUCION=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_ACTODEEJECUCION"));
            ID_CARACTER_VALORREFERENCIA=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_VALORREFERENCIA"));
            ID_CARACTER_GRUPODEACTOS=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_GRUPODEACTOS"));
            ID_CARACTER_OPERADORA=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_OPERADORA"));
            ID_CARACTER_UNIDAD=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_UNIDAD"));
            ID_CARACTER_GRUPODEENTIDADES=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_GRUPODEENTIDADES"));
            ID_CARACTER_REGULACION=Integer.parseInt(Textos.getCadena("refundido", "ID_CARACTER_REGULACION"));

            CODIGO_DETERMINACIONBASE_GRUPO_CARPETA=Textos.getCadena("refundido", "CODIGO_DETERMINACIONBASE_GRUPO_CARPETA");
            CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION=Textos.getCadena("refundido", "CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION");
            CODIGO_DETERMINACIONBASE_GRUPO_OPERADOR=Textos.getCadena("refundido", "CODIGO_DETERMINACIONBASE_GRUPO_OPERADOR");
            CODIGO_DETERMINACIONBASE_GRUPO_AFECCION=Textos.getCadena("refundido", "CODIGO_DETERMINACIONBASE_GRUPO_AFECCION");

            CODIFICACION=Textos.getCadena("refundido", "CODIFICACION");

            LONG_CODIGO_DETERMINACION=Integer.parseInt(Textos.getCadena("refundido", "LONG_CODIGO_DETERMINACION"));
            LONG_CODIGO_ENTIDAD=Integer.parseInt(Textos.getCadena("refundido", "LONG_CODIGO_ENTIDAD"));
            LONG_CODIGO_PLAN=Integer.parseInt(Textos.getCadena("refundido", "LONG_CODIGO_PLAN"));

            TEXTO_APORTADAS=Textos.getCadena("refundido", "TEXTO_APORTADAS");

            // Idioma
            TEXTO_IDIOMA=Textos.getCadena("refundido", "TEXTO_IDIOMA");

            // Averigua las dependencias entre tablas de planeamiento
            // Dependencias entre tablas: nDependencias y 3 columnas:
            //      0=TablaPrincipal
            //      1=TablaDependiente
            //      2=Campo
            lista=new ArrayList();
            s = "Select Cast(c1.relname As text) As C1, " + 
                "Cast(c2.relname As text) As C2, Cast(a.attname As text) As C3 " +
                "From pg_catalog.pg_depend d1, pg_catalog.pg_constraint cns , " +
                "pg_catalog.pg_attribute a, pg_catalog.pg_class c1, " +
                "pg_catalog.pg_namespace ns1, pg_catalog.pg_class c2, " +
                "pg_catalog.pg_namespace ns2 " +
                "Where d1.objid=cns.oid " +
                "And Upper(Trim(c1.relname)) In (Select Upper(Trim(relname)) " +
                "From pg_class Where relnamespace=(Select oid " +
                "From pg_namespace " +
                "Where Upper(Trim(nspname))='PLANEAMIENTO') " +
                "And Upper(Trim(relkind))='R') " +
                "And Upper(Trim(ns1.nspname))='PLANEAMIENTO' " +
                "And c1.relnamespace=ns1.oid " +
                "And d1.refobjid=c1.oid  " +
                "And Upper(Trim(d1.deptype))='A'  " +
                "And Upper(Trim(cns.contype))='F'  " +
                "And d1.refobjid=a.attrelid " +
                "And d1.refobjsubid=a.attnum " +
                "And cns.confrelid=c2.oid " +
                "And c2.relnamespace=ns2.oid " +
                "Order By ns1.nspname, c1.relname, ns2.nspname, c2.relname, a.attname ";
            lista=ClsConexion.recordList(s);
            nDep=lista.size();
            gDependencias = new String[nDep][3];
            it=lista.iterator();
            i=0;
            while (it.hasNext()){
                obj=(Object[]) it.next();
                gDependencias[i][0]=obj[0].toString();
                gDependencias[i][1]=obj[1].toString();
                gDependencias[i][2]=obj[2].toString();
                i=i+1;
            }        
            
        } catch (Exception e){
            mLog.error("Error en la carga de datos constantes. "  + e + ". Código 23053.");
            return false;
        }
        return true;
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
