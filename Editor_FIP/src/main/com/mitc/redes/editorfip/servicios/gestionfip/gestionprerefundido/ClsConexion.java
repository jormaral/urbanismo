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


import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

/**
 *
 * @author Miguel.Martin
 */
public class ClsConexion {
    public static EntityManager gEM;
    private static final Logger mLog = Logger.getLogger(ClsConexion.class);
    private static Connection gCon;
    private static int gLazy=0;

    public static void conectar(){
        String url = ClsDatos.URL_CONEXION;

        try {
            gCon = DriverManager.getConnection(url, "postgres", "postgres");

        } catch (SQLException e) {
            mLog.error(e);
        }

    }

    public static void desconectar(){
        try {
            gCon.close();
        } catch (SQLException e) {
            mLog.error(e);
        }
    }

    public static int ejecutar(String s){
        Statement st;
        int i;
        
        try {
            if (ClsDatos.B_USAR_HIBERNATE==false){
                st = gCon.createStatement();
                return st.executeUpdate(s);
            } else {
                i=gEM.createNativeQuery(s).executeUpdate();
                return i;
            }
        }catch( Exception e ){
            mLog.error("Error al ejecutar sentencia SQL: {" + s + "}. " + e + ". Código 23065. ");
            return 0;
        }
    }

    public static List recordList(String s){
        List lista=new ArrayList();
        ResultSet rs = null;
        int i;
        int j;
        Statement st;
        Object obj[];

        try {
            if (ClsDatos.B_USAR_HIBERNATE==false){
                st = gCon.createStatement();
                rs = st.executeQuery(s);
                while(rs.next()){
                    i=rs.getMetaData().getColumnCount();
                    if (i>1){
                        obj=new Object[i];
                        for (j=1;j<=i;j++){
                            obj[j-1]=rs.getObject(j);
                        }
                        lista.add(obj);
                    } else{
                        lista.add(rs.getObject(1));
                    }
                 }
            } else {
                lista=gEM.createNativeQuery(s).getResultList();
            }
        }catch( Exception e ){
            mLog.error("Error al recuperar lista SQL: {" + s + "}. " + e + ". Código 23066.");
        }

        return lista;
    }
    public static Object dameEntity(Class cls, Object iden){
        Object clsNew=null;
        Object args[] = null;
        String s="";
        ResultSet rs = null;
        int i;
        int j;
        int k;
        String nomColumna = null;
        Method metodos[];
        Statement st;
        String consulta = null;
        String esquema;
        boolean bGrabado;
        List lista;
        
        if (iden==null){
            return null;
        }
        if (Integer.parseInt(String.valueOf(iden))==0){
            return null;
        }

        if (ClsDatos.B_USAR_HIBERNATE==true){
            try {
                lista=new ArrayList();
                // Comprueba si la entity existe en la base de datos. Si no existe, se devuelve Null
                s="Select iden From planeamiento." + cls.getSimpleName() + " Where iden=" + iden + " ";
                lista=ClsConexion.recordList(s);

                if (lista==null){
                    return null;
                }
                if (lista.size()==0){
                    return null;
                }
                
                clsNew=gEM.find(cls, iden);
                if (clsNew==null){
                    return null;
                }

                // Si la entity existe, es necesario refrescarla para sincronizarla con la base de datos
                gEM.refresh(clsNew);

            } catch (Exception e) {
                // Se ha producido un error y hay que forzar la terminación del programa
                mLog.error("Error: fallo no controlado durante el proceso. " + cls.getSimpleName() + " iden=" + iden + ". " + e + ". Código 23000.");
                Runtime rt=Runtime.getRuntime();
                rt.runFinalization();
                rt.halt(23000);
                return null;
            }
        } else {
            try {
                st = gCon.createStatement();
                clsNew=cls.newInstance();

                // Contador de llamadas recursivas a la función dameEntity. Sólo se van a permitir cuatro
                //  niveles de anidamiento.
                gLazy=gLazy+1;
                if(gLazy>4){
                    gLazy=gLazy-1;
                    st.close();
                    return null;
                }

                // Composición de la consulta
                s=cls.getPackage().getName();
                k=s.lastIndexOf(".");
                esquema=s.substring(k+1, s.length());
                consulta = "Select * From "+ esquema + "." + cls.getSimpleName() + " Where iden=" + iden;
                rs = st.executeQuery(consulta);
                // Si la entidad no existe en la base de datos, no se hace nada. Sólo se devuelve un "null"
                if (rs.next()==false){
                    gLazy=gLazy-1;
                    st.close();
                    return null;
                }

                metodos=cls.getDeclaredMethods();
                for (Method m: metodos){
                    bGrabado=false;
                    if (!m.getName().toUpperCase().startsWith("GET")){

                        // Averigua si el campo es un int, string...
                        if (bGrabado==false){
                            i = rs.getMetaData().getColumnCount();
                            for (j = 1; j <= i; j++) {
                                nomColumna=rs.getMetaData().getColumnName(j);
                                if (m.getName().equalsIgnoreCase("set" + nomColumna)){
                                    // Es columna de tipo int, string...
                                    m.setAccessible(true);
                                    args=new Object[1];
                                    if (rs.getObject(j)==null){
                                        args[0]= rs.getObject(j);
                                    } else {
                                        if (rs.getObject(j).getClass().getName().equalsIgnoreCase("org.postgresql.util.pgobject")){
                                            args[0]= (Object) String.valueOf(rs.getObject(j));
                                        } else {
                                            args[0]= rs.getObject(j);
                                        }
                                    }
                                    m.invoke(clsNew, args[0]);
                                    bGrabado=true;
                                    break;
                                }
                            }
                        }
                    }
                }

                st.close();
                gLazy=gLazy-1;

            } catch (Exception e) {
                s = s + ", " + nomColumna + ", " + consulta;
                mLog.error("Error: imposible recuperar entity: " + cls.getSimpleName() + " iden=" + iden + ". " + s + e + ". Código 23067.");
                gLazy=gLazy-1;
            }
        }

        return clsNew;
    }
}
