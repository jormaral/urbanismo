/*
/*
/*
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Ambitoaplicacionambito;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Boletintramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Conjuntoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentocaso;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoshp;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionplan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Planentidadordenacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Planshp;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Propiedadrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 *
 * @author Miguel.Martin
 */
public class ClsMain {
    
    private static final Logger mLog = Logger.getLogger(ClsMain.class);
    public static EntityManager gEM;
    public static String maximoCodigoDeterminacion(int idTramite, int incremento){
        int maxCod=0;
        String s="";
        int i;
        List lista=new ArrayList();

        try{
            // Se ignora el primer carácter, ya que éste se ha usado para que, en los
            //  ficheros FIP, se puedan distinguir las determinaciones que provienen
            //  de grupos de entidades, unidades, y adscripciones.
            s="Select Max(SubStr(LPad(Trim(codigo)," + ClsDatos.LONG_CODIGO_DETERMINACION + 
                    ",'0'),2," + (ClsDatos.LONG_CODIGO_DETERMINACION-1) + ")) " +
                    "From planeamiento.Determinacion " +
                    "Where idtramite=" + idTramite;
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                maxCod=0;
            } else {
                maxCod=Integer.parseInt(lista.get(0).toString());
            }
            s=cadenaNcaracteres(ClsDatos.LONG_CODIGO_DETERMINACION,"0") + String.valueOf(maxCod+incremento);
            i=s.length();
            s=s.substring(i-ClsDatos.LONG_CODIGO_DETERMINACION, i);
        } catch (Exception e){
            mLog.error("Fallo en maximoCodigoDeterminacion. " + e + ". Código 23032." );
            return "";
        }
        return s;
    }

    public static String maximoCodigoEntidad(long idTramite, long incremento){
        long maxCod=0;
        String s="";
        int i;
        List lista=new ArrayList();

        try {
            // Se ignora el primer carácter, ya que es posible que, al igual que con
            //  las determinaciones, dicho carácter sea usado para que, en los
            //  ficheros FIP, se puedan distinguir entidades de diferentes
            //  características.
            s="Select Max(SubStr(LPad(Trim(codigo)," + ClsDatos.LONG_CODIGO_ENTIDAD +
                    ",'0'),2," + (ClsDatos.LONG_CODIGO_ENTIDAD-1) + ")) " +
                    "From planeamiento.Entidad " +
                    "Where idtramite=" + idTramite;
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                maxCod=0;
            } else {
                maxCod=Integer.parseInt(lista.get(0).toString());
            }
            s=cadenaNcaracteres(ClsDatos.LONG_CODIGO_ENTIDAD,"0") + String.valueOf(maxCod+incremento);
            i=s.length();
            s=s.substring(i-ClsDatos.LONG_CODIGO_ENTIDAD, i);
        } catch (Exception e){
            mLog.error("Fallo en maximoCodigoEntidad. " + e + ". Código 23007." );
            return "";
        }
        return s;
    }

    public static String maximoCodigoPlan(long incremento){
        long maxCod=0;
        String s="";
        int i;
        List lista=new ArrayList();
        
        try{
            s="Select Cast(Max(Trim(codigo)) As text) From planeamiento.Plan ";
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                maxCod=0;
            } else {
                maxCod=Integer.parseInt(lista.get(0).toString());
            }
            s=cadenaNcaracteres(ClsDatos.LONG_CODIGO_PLAN,"0") + String.valueOf(maxCod+incremento);
            i=s.length();
            s=s.substring(i-ClsDatos.LONG_CODIGO_PLAN, i);
        } catch (Exception e){
            mLog.error("Fallo en maximoCodigoPlan. " + e + ". Código 23094." );
            return "";
        }
        return s;
    }
    
    public static int ultimoIden(String tabla){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select Max(t.iden) From " + tabla + " as t ";
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
            
        } catch (Exception e){
            mLog.error("Fallo en ultimoIden. " + e + ". Código 23008." );
        }
        return 0;
    }

    public static String datosPlan(Plan iPlan){
        String s="";

        try {
            s = "[" + iPlan.getCodigo() +"] ";
            s = s + iPlan.getNombre().trim();
        } catch (Exception e){
            mLog.error("Fallo en datosPlan. " + e + ". Código 23009." );
            return "";
        }
        return s;
    }

    public static void eliminarEntidadDeterminacion(Entidaddeterminacion iEntDet){
        int nRegistros;
        String s;

        try {
            // Averigua si la determinación apuntada por esta EntidadDeterminacion es
            //  la determinación 'Grupo de Entidades', en cuyo caso no se borra.
            s="Select Count(*) From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Determinacion d Where ed.iden=" + iEntDet.getIden() + " " +
                    "And ed.iddeterminacion=d.iden " +
                    "And d.idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " ";
            nRegistros=Integer.parseInt(ClsConexion.recordList(s).get(0).toString());
            if(nRegistros>0){
                return;
            }

            //      RegimenEspecifico
            s = "Delete From planeamiento.Regimenespecifico " +
                    "Where identidaddeterminacionregimen In (" +
                    "Select iden From planeamiento.Entidaddeterminacionregimen " +
                    "Where idcaso In (" +
                    "Select iden From planeamiento.Casoentidaddeterminacion " +
                    "Where identidaddeterminacion In (" +
                    "Select iden From planeamiento.Entidaddeterminacion " +
                    "Where iden=" + iEntDet.getIden() + "))) ";
            nRegistros = ClsConexion.ejecutar(s);
            s = "Delete From planeamiento.Regimenespecifico " +
                    "Where identidaddeterminacionregimen In (" +
                    "Select iden From planeamiento.Entidaddeterminacionregimen " +
                    "Where idcasoaplicacion In (" +
                    "Select iden From planeamiento.Casoentidaddeterminacion " +
                    "Where identidaddeterminacion In (" +
                    "Select iden From planeamiento.Entidaddeterminacion " +
                    "Where iden=" + iEntDet.getIden() + "))) ";
            nRegistros = ClsConexion.ejecutar(s);
            //      EntidadDeterminacionRegimen
            s = "Delete From planeamiento.Entidaddeterminacionregimen " +
                    "Where idcaso In (" +
                    "Select iden From planeamiento.Casoentidaddeterminacion " +
                    "Where identidaddeterminacion In (" +
                    "Select iden From planeamiento.Entidaddeterminacion " +
                    "Where iden=" + iEntDet.getIden() + ")) ";
            nRegistros = ClsConexion.ejecutar(s);
            s = "Delete From planeamiento.Entidaddeterminacionregimen " +
                    "Where idcasoaplicacion In (" +
                    "Select iden From planeamiento.Casoentidaddeterminacion " +
                    "Where identidaddeterminacion In (" +
                    "Select iden From planeamiento.Entidaddeterminacion " +
                    "Where iden=" + iEntDet.getIden() + ")) ";
            nRegistros = ClsConexion.ejecutar(s);
            //      VinculoCaso
            s = "Delete From planeamiento.Vinculocaso " +
                    "Where idcaso In (" +
                    "Select iden From planeamiento.Casoentidaddeterminacion " +
                    "Where identidaddeterminacion In (" +
                    "Select iden From planeamiento.Entidaddeterminacion " +
                    "Where iden=" + iEntDet.getIden() + ")) ";
            nRegistros = ClsConexion.ejecutar(s);
            s = "Delete From planeamiento.Vinculocaso " +
                    "Where idcasovinculado In (" +
                    "Select iden From planeamiento.Casoentidaddeterminacion " +
                    "Where identidaddeterminacion In (" +
                    "Select iden From planeamiento.Entidaddeterminacion " +
                    "Where iden=" + iEntDet.getIden() + ")) ";
            nRegistros = ClsConexion.ejecutar(s);
            //      DocumentoCaso
            s = "Delete From planeamiento.Documentocaso " +
                    "Where idcaso In (" +
                    "Select iden From planeamiento.Casoentidaddeterminacion " +
                    "Where identidaddeterminacion In (" +
                    "Select iden From planeamiento.Entidaddeterminacion " +
                    "Where iden=" + iEntDet.getIden() + ")) ";
            nRegistros = ClsConexion.ejecutar(s);
            //      CasoEntidadDeterminacion
            s = "Delete From planeamiento.Casoentidaddeterminacion " +
                    "Where identidaddeterminacion In (" +
                    "Select iden From planeamiento.Entidaddeterminacion " +
                    "Where iden=" + iEntDet.getIden() + ") ";
            nRegistros = ClsConexion.ejecutar(s);
            //      EntidadDeterminacion
            s = "Delete From planeamiento.Entidaddeterminacion " +
                    "Where iden=" + iEntDet.getIden() + " ";
            nRegistros = ClsConexion.ejecutar(s);

        } catch (Exception e) {
            mLog.error("Fallo en eliminarEntidadDeterminacion. " + e + ". Código 23010." );
        }

    }

    public static void copiarEntidadDeterminacion(Entidad iEntO, List <Integer> listaIdEntDet, boolean superponer, int superposicion){
        // Copiar los elementos contenidos en listaEntDet (y sus dependencias) a la entidad iEntO
        // Se usan un TreeMap temporal para guardar el mapeo entre los iden originales y los nuevos
        //  que se van creando.
        // No se copia la determinación 'Grupo de Entidades' ya que se supone que iEntO ya
        //  tiene obligatoriamente una.
        Iterator <Integer> it1;
        Iterator it2;
        Iterator it3;
        Iterator it4;
        int idenEntDet;
        int idenCasoEntDet;
        int idenEntDetReg;
        int idenRegEsp;
        int idenCaso;
        int idenCasoVinculado;
        int idenCasoAplicacion;
        int idenDetRegimen;
        int idenOpcion;
        int idenPadre;
        int idenDocumento;
        int iden;
        String nombre;
        String valor;
        TreeMap tm=new TreeMap();
        SortedMap tm3;
        String s;
        int nRegistros;
        Object[] obj;
        List lista;

        try{
            // Incrementa el valor de superposicion, ya que el valor recibido es el máximo encontrado
            //  para la entidad operada, y los siguientes registros deben ser añadidos con el
            //  contador de superposicion incrementado.
            // Si superponer=false, se pone la superposición que tiene la EntidadDeterminacionRegimen
            if (superponer==true){
                superposicion=superposicion+1;
            }

            // Inserta registros
            it1=listaIdEntDet.iterator();
            while (it1.hasNext()){
                iden=it1.next();

                // Averigua si la determinación apuntada por esta EntidadDeterminacion es
                //  la determinación 'Grupo de Entidades', en cuyo caso no se copian los
                //  valores.
                s="Select Count(*) From planeamiento.Entidaddeterminacion ed, " +
                        "planeamiento.Determinacion d Where ed.iden=" + iden + " " +
                        "And ed.iddeterminacion=d.iden " +
                        "And d.idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " ";
                nRegistros=Integer.parseInt(ClsConexion.recordList(s).get(0).toString());

                if (nRegistros==0){
                    //  Inserta registro en EntidadDeterminacion, salvo que ya exista, en cuyo caso
                    //      toma el registro existente, ya que una pareja de entidad y determinación
                    //      sólo puede figurar una vez en la tabla EntidadDeterminacion
                    lista=new ArrayList();
                    s="Select ed.iden From planeamiento.entidaddeterminacion ed, " +
                            "planeamiento.entidaddeterminacion ed2 " +
                            "Where ed2.iden=" + iden + " " +
                            "And ed2.iddeterminacion=ed.iddeterminacion " +
                            "And ed.identidad=" + iEntO.getIden() + " ";
                    lista=ClsConexion.recordList(s);
                    if (lista.size()==0){
                        s = "Insert Into planeamiento.Entidaddeterminacion (identidad, iddeterminacion) " +
                        "(Select " + iEntO.getIden() + ", iddeterminacion " +
                        "From planeamiento.Entidaddeterminacion Where iden=" + iden + ") ";
                        nRegistros=ClsConexion.ejecutar(s);
                        idenEntDet=ClsMain.ultimoIden("planeamiento.Entidaddeterminacion");
                    } else {
                        idenEntDet=Integer.parseInt(lista.get(0).toString());
                    }

                    // En CasoEntidadDeterminacion
                    s="Select ced.iden As C1, Cast(ced.nombre As Text) As C2 " + 
                        "From planeamiento.Casoentidaddeterminacion ced " +
                        "Where ced.identidaddeterminacion=" + iden;
                    it2=ClsConexion.recordList(s).iterator();
                    tm=new TreeMap();
                    while (it2.hasNext()){
                       try{
                            obj=(Object[]) it2.next();
                            iden=Integer.parseInt(obj[0].toString());
                            if(obj[1]==null){
                                nombre="";
                            }else {
                                nombre=obj[1].toString();
                            }
                            s="Insert Into planeamiento.Casoentidaddeterminacion " +
                            "(identidaddeterminacion, nombre) Values (" +
                            idenEntDet + ", '" + nombre + "') ";
                            nRegistros=ClsConexion.ejecutar(s);
                            idenCasoEntDet=ClsMain.ultimoIden("planeamiento.Casoentidaddeterminacion");
                            tm.put(iden, idenCasoEntDet);

                       }catch (Exception e){
                           mLog.error("Error insertando registro con " + s + "; " + e + ". Código 23038." );
                           return;
                       }
                    }

                    // En VinculoCaso
                    s="Select vc.idcaso As C1, vc.idcasovinculado As C2 " + 
                        "From planeamiento.Vinculocaso vc ";
                    it2=ClsConexion.recordList(s).iterator();
                    while (it2.hasNext()){
                       try{
                            idenCaso=0;
                            idenCasoVinculado=0;
                            obj=(Object[]) it2.next();

                            iden=Integer.parseInt(obj[0].toString());
                            if (tm.containsKey(iden)){
                                idenCaso=Integer.parseInt(tm.get(iden).toString());

                                iden=Integer.parseInt(obj[1].toString());
                                if (tm.containsKey(iden)){
                                    idenCasoVinculado=Integer.parseInt(tm.get(iden).toString());
                                }
                            }

                            if (idenCaso!=0 && idenCasoVinculado!=0){
                                s="Insert Into planeamiento.Vinculocaso (idcaso, idcasovinculado) " +
                                "Values (" + idenCaso + ", " + idenCasoVinculado + ") ";
                                nRegistros=ClsConexion.ejecutar(s);
                            }
                       }catch (Exception e){
                           mLog.error("Error insertando registro con " + s + "; " + e + ". Código 23038." );
                           return;
                       }
                    }

                    // En EntidadDeterminacionRegimen
                    it2=tm.keySet().iterator();
                    while (it2.hasNext()){
                       try{
                            iden=Integer.parseInt(it2.next().toString());
                            idenCaso=Integer.parseInt(tm.get(iden).toString());

                            s="Select edr.iden As C1, edr.iddeterminacionregimen As C2," + 
                            " edr.idopciondeterminacion As C3, " +
                            "edr.valor As C4, edr.idcasoaplicacion As C5, " + 
                            "edr.superposicion As C6 " +
                            "From planeamiento.Entidaddeterminacionregimen edr " +
                            "Where edr.idcaso=" + iden + " ";
                            it3=ClsConexion.recordList(s).iterator();
                            while (it3.hasNext()){
                                obj=(Object[]) it3.next();
                                iden=Integer.parseInt(obj[0].toString());

                                idenDetRegimen=0;
                                if (obj[1]!=null){
                                    idenDetRegimen=Integer.parseInt(obj[1].toString());
                                }

                                idenOpcion=0;
                                if (obj[2]!=null){
                                    idenOpcion=Integer.parseInt(obj[2].toString());
                                }

                                valor="";
                                if (obj[3]!=null){
                                    valor=obj[3].toString();
                                }

                                idenCasoAplicacion=0;
                                if (obj[4]!=null){
                                    idenCasoAplicacion=Integer.parseInt(obj[4].toString());
                                }

                                if (superponer==false){
                                    if (obj[5]==null){
                                        superposicion=0;
                                    } else {
                                        superposicion=Integer.parseInt(obj[5].toString());
                                    }
                                }

                                // Construir la sentencia Insert de EntidadDeterminacionRegimen
                                s = "Insert Into planeamiento.Entidaddeterminacionregimen (" +
                                "idcaso, iddeterminacionregimen, idopciondeterminacion, valor, " +
                                "superposicion, idcasoaplicacion) Values (";

                                s = s + idenCaso + ", ";
                                if (idenDetRegimen==0){
                                    s = s + "null, ";
                                } else {
                                    s = s + idenDetRegimen + ", ";
                                }
                                if (idenOpcion==0){
                                    s = s + "null, ";
                                } else {
                                    s = s + idenOpcion + ", ";
                                }
                                if (valor.equalsIgnoreCase("")){
                                    s = s + "'', ";
                                } else {
                                    s = s + "'" + valor + "', ";
                                }
                                s = s + superposicion + ", ";
                                if (idenCasoAplicacion==0){
                                    s = s + "null) ";
                                } else {
                                    s = s + idenCasoAplicacion + ") ";
                                }
                                nRegistros=ClsConexion.ejecutar(s);
                                idenEntDetReg=ClsMain.ultimoIden("planeamiento.Entidaddeterminacionregimen");

                                // Régimen Específico. Debido a la existencia del campo idPadre, se hacen
                                //  los insert por orden de iden e idPadre
                                s = "Select iden As C1, idpadre As C2, orden As C3, " + 
                                "nombre As C4, texto As C5 " +
                                "From planeamiento.Regimenespecifico " +
                                "Where identidaddeterminacionregimen=" + iden + " Order By iden, idpadre ";
                                it4=ClsConexion.recordList(s).iterator();
                                tm3=new TreeMap();
                                while (it4.hasNext()){
                                    obj=(Object[]) it4.next();
                                    iden=Integer.parseInt(obj[0].toString());
                                    s="";
                                    if (obj[1]!=null){
                                        idenPadre=Integer.parseInt(obj[1].toString());
                                        if (tm3.containsKey(idenPadre)){
                                            s=tm3.get(idenPadre).toString();
                                        }
                                    } else{
                                        s="null";
                                    }
                                    if (!s.equalsIgnoreCase("")){
                                        s = "Insert Into planeamiento.Regimenespecifico " +
                                        "(identidaddeterminacionregimen, idpadre, orden, nombre, texto) " +
                                        "Select " + idenEntDetReg + ", " + s + ", orden, " +
                                        "nombre, texto From planeamiento.Regimenespecifico " +
                                        "Where iden=" + iden;
                                        nRegistros=ClsConexion.ejecutar(s);
                                        if (nRegistros==1){
                                            idenRegEsp=ClsMain.ultimoIden("planeamiento.Regimenespecifico");
                                            tm3.put(iden, idenRegEsp);
                                        }
                                    }
                                }
                            }
                       }catch (Exception e){
                           mLog.error("Error insertando registro con " + s + "; " + e + ". Código 23038." );
                           return;
                       }
                    }

                    // DocumentoCaso
                    s="Select dc.idcaso As C1, dc.iddocumento As C2 " + 
                    "From planeamiento.Documentocaso dc ";
                    it2=ClsConexion.recordList(s).iterator();
                    while (it2.hasNext()){
                        try{
                            obj=(Object[]) it2.next();
                            iden=Integer.parseInt(obj[0].toString());
                            if (tm.containsKey(iden)){
                                idenCaso=Integer.parseInt(tm.get(iden).toString());
                                idenDocumento=Integer.parseInt(obj[1].toString());
                                s="Insert Into planeamiento.Documentocaso (idcaso, iddocumento) " +
                                "Values (" + idenCaso + ", " + idenDocumento + ") ";
                                nRegistros=ClsConexion.ejecutar(s);
                            }
                       }catch (Exception e){
                           mLog.error("Error insertando registro con " + s + "; " + e + ". Código 23038." );
                           return;
                       }
                    }
                }
            }
        } catch (Exception e){
            mLog.error("Fallo en copiarEntidadDeterminacion. " + e + ". Código 23011." );
        }
    }

    public static List <Integer> relacionesPorElemento(String tabla, int iden){
        String s="";
        List listaDR;
        List listaR;
        List lista=new ArrayList();
        Iterator it;
        Object[] obj;
        int idDR;
        int idDV;

        try{
            listaDR=new ArrayList();
            listaR=new ArrayList();

            s="Select dr.iden As idendr, dv.iden As idendv " +
                    "From diccionario.Defrelacion dr, diccionario.Defvector dv, " +
                    "diccionario.Tabla t " +
                    "Where Upper(Trim(t.nombre))='" + tabla.toUpperCase().trim() + "' " +
                    "And dv.idtabla=t.iden ";
            listaDR=ClsConexion.recordList(s);
            it=listaDR.iterator();
            while (it.hasNext()){
                obj=(Object[]) it.next();
                idDR=Integer.parseInt(obj[0].toString());
                idDV=Integer.parseInt(obj[1].toString());
                s="Select r.iden From planeamiento.Relacion r, planeamiento.Vectorrelacion vr " +
                        "Where vr.idrelacion=r.iden And vr.iddefvector=" + idDV + " " +
                        "And r.iddefrelacion=" + idDR + " And vr.valor=" + iden;
                listaR=ClsConexion.recordList(s);
                lista.addAll(listaR);
            }
        } catch (Exception e) {
            mLog.error("Fallo en relacionesPorElemento. " + e + ". Código 23012." );
        }

        return lista;
    }

    public static String ultimoApartadoDeterminacion(Object idPadre, int idTramite, boolean incrementar){
        String s;
        List lista;
        String apartado="";
        Iterator it;

        // Debido a que se ha constatado la imposibilidad de ordenar correctamente los
        //  apartados de forma que se pueda establecer cuál es el último, se opta por
        //  generar el siguiente apartado de todos los leídos hasta que se obtenga uno
        //  que no exista en el listado original.
        // Si el parámetro 'incrementar==false' se devuelve el máximo apartado que
        //  devuelve la consulta, aunque puede no ser el máximo buscado.
        try{
            lista=new ArrayList();
            if (idPadre==null){
                s="Select Translate(Replace(Trim(apartado),'  ',' '),'áéíóúÁÉÍÓÚ.','aeiouAEIOU ') As campo From planeamiento.Determinacion " +
                        "Where idPadre Is Null " +
                        "And idTramite=" + idTramite + " " +
                        "And apartado Is Not Null " +
                        "Order By campo Desc ";
            } else {
                s="Select Translate(Replace(Trim(apartado),'  ',' '),'áéíóúÁÉÍÓÚ.','aeiouAEIOU ') As campo From planeamiento.Determinacion " +
                        "Where idPadre=" + idPadre.toString() + " " +
                        "And idTramite=" + idTramite + " " +
                        "And apartado Is Not Null " +
                        "Order By campo Desc ";
            }
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                apartado="";
                if (incrementar==true){
                    apartado=ClsMain.siguienteApartadoDeterminacion(apartado);
                }
                return apartado;
                
            } else {
                it=lista.iterator();
                while (it.hasNext()){
                    apartado=it.next().toString();
                    apartado=ClsMain.siguienteApartadoDeterminacion(apartado);
                    if (!lista.contains(apartado)){
                        return apartado;
                    }
                }
            }
        } catch (Exception e){
            mLog.error("Fallo en ultimoApartadoDeterminacion. " + e + ". Código 23050." );
            return "1";
        }
        
        // Si no se ha conseguido crear un nuevo apartado a partir de los leídos, se
        //  crea uno nuevo como si fuera el primero.
        apartado="";
        if (incrementar==true){
            apartado=ClsMain.siguienteApartadoDeterminacion(apartado);
        }
        return apartado;
    }
    
    public static String siguienteApartadoDeterminacion(String aptdo){
        // Analiza los caracteres que componen el apartado para, obviando
        //  los signos de puntuación y tomando solamente las letras y
        //  los números, devolver una cadena compuesta por los mismos
        //  caracteres pero incrementando el último en 1.

        // Dada una cadena, se debe calcular un valor que la identifique
        //  unívocamente, y después de incrementar dicho valor, hay que
        //  construir una cadena que sea su representación.

        // Se van a intentar identificar tres tipos de caracteres que
        //  puedan ser incrementados:
        //      1. Una letras suelta (a-z y A-Z)
        //      2. Números romanos
        //      3. Números arábigos

        // Empezando por la derecha, se intenta localizar el primer paquete de
        //  caracteres de cada uno de esos tres tipos. El que resulte estar más a la
        //  derecha, será el que se incremente.

        int pos1;
        int pos2;
        int pos3;
        int pos;
        int i;
        int j;
        int n;
        String aptdoTMP=" ";
        String c="ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        String ch;
        String s1="";
        String s2="";
        String s3="";
        int flag;
        int criterio;
        String s;

        try{
            // Si el apartado es una cadena vacía, por defecto se devuelve "1"
            s=aptdo;
            s=s.trim();
            if (s.trim().equalsIgnoreCase("")){
                s="1";
                return s;
            }
            
            aptdoTMP=" " + aptdo.toUpperCase() + " ";

            // 1. Intenta localizar una letra suelta. La condición es que, a ambos lados,
            //  no tenga ninguna otra letra y, además, que no sea un nº romano.
            pos1=-1;
            for (i=aptdoTMP.length()-1;i>0;i--){
                ch=String.valueOf(aptdoTMP.charAt(i-1));
                if (c.contains(ch)){
                    ch=String.valueOf(aptdoTMP.charAt(i));
                    if(!c.contains(ch)){
                        ch=String.valueOf(aptdoTMP.charAt(i-2));
                        if(!c.contains(ch)){
                            pos1=i;
                            s1=String.valueOf(aptdoTMP.charAt(i-1));
                            break;
                        }
                    }
                }
            }

            // 2. Intenta localizar un número romano. Se hace un doble bucle para empezar
            //  con los de un único carácter, después con los de dos, y así sucesivamente
            //  hasta la longitud completa de la cadena.
            pos2=-1;
            for (j=1; j<=aptdoTMP.length()-2;j++){
                flag=0;
                for (i=aptdoTMP.length()-j;i>0;i--){
                    ch=aptdoTMP.substring(i-1, i-1+j);
                    n=Integer.parseInt(numeroArabigoDeRomano(ch));
                    if (n>0){
                        if(!(c.contains(aptdoTMP.substring(i-2, i-2+j)) &&
                                Integer.parseInt(numeroArabigoDeRomano(aptdoTMP.substring(i-2, i-2+j)))==0)){
                            if(!(c.contains(aptdoTMP.substring(i, i+j)) &&
                                    Integer.parseInt(numeroArabigoDeRomano(aptdoTMP.substring(i, i+j)))==0)){
                                s2=ch;
                                pos2=i;
                                flag=1;
                            }
                        }
                    } else{
                        if (pos2>-1 && flag==1){
                            break;
                        }
                    }
                }
            }

            // 3. Intenta localizar un número arábigo, con los mismos criterios que se
            //  han considerado para los romanos.
            pos3=-1;
            for (j=1; j<=aptdoTMP.length()-2;j++){
                flag=0;
                for (i=aptdoTMP.length()-j;i>0;i--){
                    ch=aptdoTMP.substring(i-1, i-1+j);
                    try{
                        n=Integer.parseInt(ch);
                        if (n>0 && flag==0 & (i==(pos3-1) || pos3==-1 )){
                            s3=ch;
                            pos3=i;
                            flag=1;
                            break;
                        }
                    } catch(Exception e){
                        if (pos3>-1 && flag==1){
                            break;
                        }
                    }
                }
            }

            // Selecciona cuál de los paquetes se va a incrementar.
            pos=0;
            criterio=0;
            if(pos1>pos){
                pos=pos1;
                criterio=1;
            }
            if(pos2>pos){
                pos=pos2;
                criterio=2;
            }
            if(pos3>pos){
                pos=pos3;
                criterio=3;
            }

            if (pos==0){
                // No se ha encontrado ninguna secuencia de caracteres que pueda ser incrementada
                //  Al apartado original, se le agrega '.1'
                return aptdo + ".1";
            }

            switch (criterio){
                case 1:
                    // Letra suelta
                    n=-1;
                    for (i=0; i<c.length();i++){
                        if(c.substring(i, i+1).equals(s1)){
                            if(i==c.length()-1){
                                s1="AA";
                            } else{
                                s1=c.substring(i+1, i+2);
                            }
                            // Si la letra original es minúscula, la letra calculada
                            //  se restaura a ese estado.
                            ch=aptdoTMP.substring(pos-1, pos);
                            if(!ch.equals(ch.toUpperCase())){
                                s1=s1.toLowerCase();
                            }
                            break;
                        }
                    }
                    aptdoTMP=aptdoTMP.substring(0,pos-1) + s1 + aptdoTMP.substring(pos);
                    break;

                case 2:
                    // Número romano
                    n=Integer.parseInt(numeroArabigoDeRomano(s2));
                    i=s2.length();
                    n=n+1;
                    s2=numeroRomanoDeArabigo(n);
                    s1=aptdoTMP.substring(pos+i-1);
                    aptdoTMP=aptdoTMP.substring(0,pos-1) + s2 + s1;
                    break;

                case 3:
                    // Numero arábigo
                    n=Integer.parseInt(s3)+1;
                    i=String.valueOf(n).length();
                    aptdoTMP=aptdoTMP.substring(0,pos-1) + String.valueOf(n) + aptdoTMP.substring(pos+i-1);
                    break;

            }
            aptdo=aptdoTMP.trim();

        } catch (Exception e){
            mLog.error("Fallo en siguienteApartadoDeterminacion. " + e + ". Código 23015." );
            return aptdo + ".1";
        }

        return aptdo;
    }

    public static String numeroRomanoDeArabigo(int arabigo){
        int i; // 1
        int v; // 5
        int x; // 10
        int l; // 50
        int c; // 100
        int d; // 500
        int m; // 1000
        int q; // 5000
        int h; // 10000
        String s="";
        int n;

        try{
            // 10 Miles
            h=arabigo/10000;
            arabigo=arabigo-h*10000;

            // 5 Miles
            q=arabigo/5000;
            arabigo=arabigo-q*5000;

            // 1 Miles
            m=arabigo/1000;
            arabigo=arabigo-m*1000;

            // Quinientos
            d=arabigo/500;
            arabigo=arabigo-d*500;

            // Centenas
            c=arabigo/100;
            arabigo=arabigo-c*100;

            // Cincuentenas
            l=arabigo/50;
            arabigo=arabigo-l*50;

            // Decenas
            x=arabigo/10;
            arabigo=arabigo-x*10;

            // Quintas
            v=arabigo/5;
            arabigo=arabigo-v*5;

            // Unidades
            i=arabigo;

            // Calcula la representación en caracteres romanos.
            s="";
            if(h>0){
                for(n=1;n<=h;n++){
                    s=s+"H";
                }
            }

            if(q>0){
                for(n=1;n<=q;n++){
                    s=s+"Q";
                }
            }

            if(m>0){
                if(m<=3){
                    for(n=1;n<=m;n++){
                        s=s+"M";
                    }
                }else {
                    s=s+"MQ";
                }
            }

            if(d>0){
                for(n=1;n<=d;n++){
                    s=s+"D";
                }
            }

            if(c>0){
                if(c<=3){
                    for(n=1;n<=c;n++){
                        s=s+"C";
                    }
                }else {
                    s=s+"CD";
                }
            }

            if(l>0 && x<=3){
                for(n=1;n<=l;n++){
                    s=s+"L";
                }
            }

            if(x>0){
                if(x<=3){
                    for(n=1;n<=x;n++){
                        s=s+"X";
                    }
                }else {
                    if(l==0){
                        s=s+"XL";
                    } else{
                        s=s+"XC";
                    }
                }
            }

            if(v==1){
                if(i<=3){
                    s=s+"V";
                    for(n=1;n<=i;n++){
                        s=s+"I";
                    }
                }else {
                    s=s+"IX";
                }
            } else {
                if(i<=3){
                    for(n=1;n<=i;n++){
                        s=s+"I";
                    }
                }else {
                    s=s+"IV";
                }    
            }
            
        } catch(Exception e){
            mLog.error("Fallo en numeroRomanoDeArabigo. " + e + ". Código 23034." );
            return "";
        }

        return s;
    }

    public static String numeroArabigoDeRomano(String romano){
        int numero=-1;
        int iValor;
        int iValorAnt;
        int i;
        char c;

        try{
            if(romano.length()==0){
                return "0";
            }

            numero = 0;
            iValorAnt=0;
            for (i=romano.length();i>0;i--){
                c=romano.charAt(i-1);
                iValor=valorDeCaracterRomano(String.valueOf(c));
                if(iValor==-1){
                    return "0";
                }
                if(iValor<iValorAnt){
                    numero=numero-iValor;
                }
                else{
                    numero=numero+iValor;
                }
                iValorAnt=iValor;
            }
        } catch(Exception e){
            mLog.error("Fallo en numeroArabigoDeRomano. " + e + ". Código 23016." );
            return "0";
        }

        return String.valueOf(numero);
    }

    private static int valorDeCaracterRomano(String c){

        try{
           if (c.equalsIgnoreCase("I")){
               return 1;
           }

           if (c.equalsIgnoreCase("V")){
               return 5;
           }

           if (c.equalsIgnoreCase("X")){
               return 10;
           }

           if (c.equalsIgnoreCase("L")){
               return 50;
           }

           if (c.equalsIgnoreCase("C")){
               return 100;
           }

           if (c.equalsIgnoreCase("D")){
               return 500;
           }

           if (c.equalsIgnoreCase("M")){
               return 1000;
           }

           if (c.equalsIgnoreCase("Q")){
               return 5000;
           }

           if (c.equalsIgnoreCase("H")){
               return 10000;
           }
        } catch (Exception e){
            mLog.error("Fallo en valorDeCaracterRomano. " + e + ". Código 23017." );
        }

       return -1;
    }

    public static Determinacion determinacionGrupoDeEntidadesPorTramite(Tramite iTramite){
        Determinacion iDet;
        String s;
        List lista;
        int idDet;

        try{
            s="Select iden From planeamiento.Determinacion " + 
                    "Where idtramite=" + iTramite.getIden() + " " +
                    "And idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " ";
            lista = ClsConexion.recordList(s);
            if (lista.size() > 0) {
                // Se toma la primera, aunque puede haber más, debido a que se van
                //  reasignando las determinaciones desde un trámite hasta otro.
                idDet=Integer.parseInt(lista.get(0).toString());
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDet);
                return iDet;
            } else {
                // Si no se ha encontrado en el trámite en cuestión, es porque el trámite
                //  está usando la determinación 'Grupo de Entidades' de otro 
                //  trámite, que puede ser del plan base.
                // Por lo tanto, se toma cualquier determinación de caracter 'GRUPODEENTIDADES'
                //  que esté aplicada en alguna de las entidades del trámite.
                s="Select d.iden From planeamiento.Determinacion d, planeamiento.Entidad e, " +
                        "planeamiento.Entidaddeterminacion ed " +
                        "Where ed.identidad=e.iden And e.idtramite=" + iTramite.getIden() + " " +
                        "And ed.iddeterminacion=d.iden " +
                        "And d.idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " ";
                lista = ClsConexion.recordList(s);
                if (lista.size() > 0) {
                    // Se toma la primera, aunque puede haber más
                    idDet=Integer.parseInt(lista.get(0).toString());
                    iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDet);
                    return iDet;
                }
            }
        } catch (Exception e){
            mLog.error("Fallo en determinacionGrupoDeEntidadesPorTramite. " + e + ". Código 23018." );
            return null;
        }

        mLog.warn("Aviso: no se ha encontrado ninguna determinación 'Grupo de Entidades' entre los trámites. Código 23018." );
        return null;
    }

    public static Determinacion determinacionGrupoPorEntidad(Entidad iEnt){
        // Hay dos criterios para buscar la determinación grupo:
        // 1º. En principio, podría ser una determinación del plan base
        // 2º. Si no es así, se supone que es un grupo creado por el plan, y no
        //  debe tener equivalencia con el plan base (ya que si la tuviera, se debería
        //  haber usado esa).
        // Por lo tanto, basta con buscar la determinación de caracter valor de referencia
        //  que tiene, como valor, la entidad en cuestión para la determinación
        //  de caracter 20 (Grupo de Entidades), independientemente de si es de un
        //  plan u otro.
        Determinacion iDetGrupo=null;
        String s="";
        List lista;

        try{
            lista=new ArrayList();
            s="Select od.iddeterminacionvalorref From planeamiento.Entidaddeterminacion ed, " +
                        "planeamiento.Casoentidaddeterminacion ced, " +
                        "planeamiento.Opciondeterminacion od, " +
                        "planeamiento.Entidaddeterminacionregimen edr, " +
                        "planeamiento.Determinacion d " +
                        "Where ed.identidad=" + iEnt.getIden() + " " +
                        "And ced.identidaddeterminacion=ed.iden " +
                        "And edr.idopciondeterminacion=od.iden " +
                        "And od.iddeterminacion=d.iden " +
                        "And (edr.idcaso=ced.iden Or edr.idcasoaplicacion=ced.iden) " +
                        "And ed.iddeterminacion=d.iden " +
                        "And d.idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " ";
            lista=ClsConexion.recordList(s);
            if(lista.size()==1){
                iDetGrupo=(Determinacion) ClsConexion.dameEntity(Determinacion.class, lista.get(0));
            } else {
                mLog.warn("    La entidad no tiene valor para la determinación 'Grupo de Entidades'" );
                iDetGrupo=null;
            }

        } catch (Exception e){
            mLog.error("Fallo en determinacionGrupoPorEntidad: " + e + ". Código 23019." );
            return null;
        }
        return iDetGrupo;
    }

    public static Determinacion determinacionGrupoEquivalentePorEntidad(Entidad iEnt1, Tramite iTramite2){
        String s;
        Determinacion iDetGrupo1;
        Determinacion iDetGrupoEntidades2;
        List lista;
        Determinacion iDetVR;
        int iden;

        // Dada una entidad y un trámite, se devuelve la determinación valor de
        //  referencia de 'Grupo de Entidades' del trámite, que se corresponde con
        //  la determinación valor de referencia de 'Grupo de Entidades' cargada en
        //  la entidad. El nexo de unión es su vínculo con una determinación del
        //  plan base o, en su defecto, el nombre de la determinación.
        // Si la determinación VR de iEnt1 es del plan base, se devuelve ésta.

        try{
            iDetGrupo1=determinacionGrupoPorEntidad(iEnt1);
            iDetGrupoEntidades2=determinacionGrupoDeEntidadesPorTramite(iTramite2);

            if(iDetGrupo1==null || iDetGrupoEntidades2==null){
                return null;
            }

            if (ClsDatos.gListaIdTramitesBase.contains(iDetGrupo1.getTramite().getIden())){
                return iDetGrupo1;
            }

            lista=new ArrayList();
            s="Select od.iddeterminacionvalorref From planeamiento.Opciondeterminacion od, " +
                    "planeamiento.Determinacion d " +
                    "Where od.iddeterminacion=" + iDetGrupoEntidades2.getIden() + " " +
                    "And od.iddeterminacionvalorref=d.iden " +
                    "And d.iddeterminacionbase=" + idDeterminacionBasePorIdDeterminacion(iDetGrupo1.getIden()) + " ";
            lista=ClsConexion.recordList(s);
            if (lista.size()==1){
                iden=Integer.parseInt(lista.get(0).toString());
                iDetVR=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                return iDetVR;
            }

            // Si no se ha encontrado la determinación equivalente a través del vínculo
            //  con la determinación base, se busca por el nombre y caracter.
            lista=new ArrayList();
            s="Select iden From planeamiento.Determinacion " +
                    "Where idtramite=" + iTramite2.getIden() + " " +
                    "And Upper(Trim(nombre))='" + iDetGrupo1.getNombre().toUpperCase().trim() +"' " +
                    "And idcaracter=" + iDetGrupo1.getIdcaracter() + " ";
            lista=ClsConexion.recordList(s);
            if (lista.size()==1){
                iden=Integer.parseInt(lista.get(0).toString());
                iDetVR=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                return iDetVR;
            }
            
            // Si tampoco se encuentra, se devuelve la determinación base
            iden=idDeterminacionBasePorIdDeterminacion(iDetGrupo1.getIden());
            iDetVR=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
            return iDetVR;

        } catch(Exception e){
            mLog.error("Fallo en determinacionGrupoEquivalentePorEntidad: " + e + ". Código 23033." );
        }

        return null;
    }

    public static Determinacion determinacionEquivalentePorDeterminacion(Determinacion iDet1, Tramite iTramite2){
        String s;
        List lista;
        Determinacion iDetVR;
        Integer iden;

        // Dada una determinación valor de referencia de 'Grupo de Entidades' y un
        //  trámite, se devuelve la determinación valor de
        //  referencia de 'Grupo de Entidades' del trámite, que se corresponde con
        //  ella. El nexo de unión es su vínculo con una determinación del
        //  plan base o, en su defecto, el nombre de la determinación.

        try{
            lista=new ArrayList();
            s="Select d2.iden From planeamiento.Determinacion d1, " +
                    "planeamiento.Determinacion d2 " +
                    "Where d1.iden=" + iDet1.getIden() + " " +
                    "And d1.iddeterminacionbase=d2.iddeterminacionbase " +
                    "And d2.idtramite=" + iTramite2.getIden() + " ";
            lista=ClsConexion.recordList(s);
            if (lista.size()==1){
                iden=Integer.parseInt(lista.get(0).toString());
                iDetVR=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                return iDetVR;
            }

            // Si no se ha encontrado la determinación equivalente a través del vínculo
            //  con la determinación base, se busca por el nombre y caracter.
            lista=new ArrayList();
            s="Select iden From planeamiento.Determinacion " +
                    "Where idtramite=" + iTramite2.getIden() + " " +
                    "And Upper(Trim(nombre))='" + iDet1.getNombre().toUpperCase().trim() +"' " +
                    "And idcaracter=" + iDet1.getIdcaracter() + " ";
            lista=ClsConexion.recordList(s);
            if (lista.size()==1){
                iden=Integer.parseInt(lista.get(0).toString());
                iDetVR=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                return iDetVR;
            }

        } catch(Exception e){
            mLog.error("Fallo en determinacionEquivalentePorDeterminacion: " + e + ". Código 23047." );
        }

        return null;
    }

    public static List <Integer> idEntidadesPorGrupoTramite(Determinacion iDetGrupo, Tramite iTramite){
        List lista1=new ArrayList();
        List lista2=new ArrayList();
        String s="";

        // Se devuelve una lista de iden de entidades cuyo grupo de entidad es la
        //  determinación iDetGrupo o cualquier otra que tenga la misma determinación
        //  base que ella.

        try{
            // 1º Entidades que tienen como alguno de sus valores de referencia a iDetGrupo
            s="Select ed.identidad From planeamiento.Entidaddeterminacion ed, " +
                        "planeamiento.Casoentidaddeterminacion ced, " +
                        "planeamiento.Opciondeterminacion od, " +
                        "planeamiento.Entidad e, " +
                        "planeamiento.Entidaddeterminacionregimen edr " +
                        "Where od.iddeterminacionvalorref=" + iDetGrupo.getIden() + " "+
                        "And ced.identidaddeterminacion=ed.iden " +
                        "And edr.idopciondeterminacion=od.iden " +
                        "And (edr.idcaso=ced.iden Or edr.idcasoaplicacion=ced.iden) " +
                        "And ed.identidad=e.iden And e.idtramite=" + iTramite.getIden() + " ";
            lista1=ClsConexion.recordList(s);

            // 2º Entidades que tienen como alguno de sus valores de referencia a alguna
            //      determinación cuyo determinación base es la misma que la de iDetGrupo
            s="Select ed.identidad From planeamiento.Entidaddeterminacion ed, " +
                        "planeamiento.Casoentidaddeterminacion ced, " +
                        "planeamiento.Opciondeterminacion od, " +
                        "planeamiento.Determinacion d, " +
                        "planeamiento.Entidad e, " +
                        "planeamiento.Entidaddeterminacionregimen edr " +
                        "Where od.iddeterminacionvalorref=d.iden " +
                        "And d.iden<>" + iDetGrupo.getIden() + " " +
                        "And d.iddeterminacionbase=" + idDeterminacionBasePorIdDeterminacion(iDetGrupo.getIden()) + " " +
                        "And ced.identidaddeterminacion=ed.iden " +
                        "And edr.idopciondeterminacion=od.iden " +
                        "And (edr.idcaso=ced.iden Or edr.idcasoaplicacion=ced.iden) " +
                        "And ed.identidad=e.iden And e.idtramite=" + iTramite.getIden() + " ";
            lista2=ClsConexion.recordList(s);

            lista1.addAll(lista2);
        } catch (Exception e) {
            mLog.error("Fallo en entidadesPorGrupoTramite. " + e + ". Código 23020." );
            return lista1;
        }

        return lista1;
    }

    public static Determinacion determinacionCarpetaTramiteBase(Tramite iTramite){
        Determinacion iDet;
        Determinacion iDetGrupoDeEntidades;
        List lista;
        int idDet;
        String s;
        String aptdo;
        String codigo;
        int i;
        boolean bCrearDet=false;

        // Devuelve la determinación "Carpeta" de uno de los trámites base 
        //  usados, si es que se está usando alguno.
        //  En caso contrario, se busca la determinación en el trámite 
        //  solicitado y, si no existe, se crea en él.
        
        try{
            // Primero se busca en los trámites base.
            s="Select Distinct D.iden From planeamiento.Determinacion D, planeamiento.Tramite T " +
                "Where D.codigo='" + ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA + "' " +
                "And idtramite=Any(Array[0";
            for (int idT: ClsDatos.gListaIdTramitesBase){
                s = s + ", " + idT;
            }
            s = s + "]) ";
            lista = ClsConexion.recordList(s);
            if (lista.size()>0) {
                idDet=Integer.parseInt(lista.get(0).toString());
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDet);
                return iDet;
            }    
            
            // Si no existe la determinación 'Carpeta' en un trámite base,
            //  se busca en el trámite solicitado. Para ello, se busca entre
            //  los valores de referencia de cualquiera de las determinaciones
            //  con caracter ClsDatos.ID_CARACTER_GRUPODEENTIDADES que estén
            //  aplicadas a alguna entidad del trámite en curso. 
            //  Primero se intenta buscar una determinación 'Grupo de Entidades'
            //  dentro del propio trámite y, si no se encuentra, se busca en
            //  cualquier otro trámite, aunque siempre sobre entidades del
            //  trámite en curso.
            // Para localizar la determinación 'Carpeta' dentro de todos los
            //  valores de referencia encontrados, se miran dos cosas: puede que
            //  tenga el mismo código que la determinación carpeta del plan
            //  base, o puede que esté vinculada a la determinación del plan base
            //  que tenga dicho código. Cualquiera de las dos condiciones sirve
            //  para identificar la determinación 'Carpeta'.
            
            // Primero, dentro de las determinaciones del trámite en curso.
            s="Select DVR.iden From planeamiento.Determinacion DVR, " + 
                "planeamiento.Entidad E, planeamiento.Determinacion D, " +
                "planeamiento.OpcionDeterminacion OD, " + 
                "planeamiento.EntidadDeterminacion ED " +
                "Where E.idTramite=" + iTramite.getIden() + " " +
                "And D.idTramite=" + iTramite.getIden() + " " +
                "And ED.identidad=E.iden And ED.iddeterminacion=D.iden " +
                "And D.idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " " +
                "And OD.iddeterminacion=D.iden " + 
                "And OD.iddeterminacionvalorref=DVR.iden " + 
                "And (DVR.codigo='" + ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA + "' " +
                "Or DVR.iddeterminacionbase In(Select D.iden " + 
                "From planeamiento.Determinacion D " + 
                "Where D.codigo='" + ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA + "' " + 
                "And D.idtramite=Any(Array[0";
            for (int idT: ClsDatos.gListaIdTramitesBase){
                s = s + ", " + idT;
            }
            s = s + "]))) ";
            lista = ClsConexion.recordList(s);
            if (lista.size()>0) {
                idDet=Integer.parseInt(lista.get(0).toString());
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDet);
                return iDet;
            }
            
            // En segundo lugar, dentro de las determinaciones de cualquier otro
            //  trámite que estén vinculadas a las entidades del trámite en curso.
            s="Select DVR.iden From planeamiento.Determinacion DVR, " + 
                "planeamiento.Entidad E, planeamiento.Determinacion D, " +
                "planeamiento.OpcionDeterminacion OD, " + 
                "planeamiento.EntidadDeterminacion ED " +
                "Where E.idTramite=" + iTramite.getIden() + " " +
                "And D.idTramite=" + iTramite.getIden() + " " +
                "And ED.identidad=E.iden And ED.iddeterminacion=D.iden " +
                "And D.idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " " +
                "And OD.iddeterminacion=D.iden " + 
                "And OD.iddeterminacionvalorref=DVR.iden " + 
                "And (DVR.codigo='" + ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA + "' " +
                "Or DVR.iddeterminacionbase In(Select D.iden " + 
                "From planeamiento.Determinacion D " + 
                "Where D.codigo='" + ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA + "' " + 
                "And D.idtramite=Any(Array[0";
            for (int idT: ClsDatos.gListaIdTramitesBase){
                s = s + ", " + idT;
            }
            s = s + "]))) ";
            lista = ClsConexion.recordList(s);
            if (lista.size()>0) {
                idDet=Integer.parseInt(lista.get(0).toString());
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDet);
                return iDet;
            }
            
            // Si en este punto no se ha logrado identificar ninguna determinación
            //  'Carpeta', hay que añadir una en el trámite en curso, que se 
            //  añadirá como valor de referencia de la determinación 'Grupo de Entidades'
            iDetGrupoDeEntidades=ClsMain.determinacionGrupoDeEntidadesPorTramite(iTramite);
            // Si no existe ninguna determinación 'Grupo de Entidades' o bien 
            //  es de un plan base, no se le pueden añadir valores de referencia. 
            //  Por lo tanto, también hay que crear una determinacion 'Grupo de Entidades' 
            //  en el trámite en curso.
            if (iDetGrupoDeEntidades==null){
                bCrearDet=true;
            } else {
                if (ClsDatos.gListaIdTramitesBase.contains(iDetGrupoDeEntidades.getTramite().getIden())){
                    bCrearDet=true;
                }
            }
            
            if (bCrearDet==true){
                mLog.warn("    No existe ninguna determinación 'Grupo de Entidades' disponible para el idTramite=" + iTramite.getIden());
                mLog.warn("        Se va a crear una en dicho trámite.");
                aptdo=ultimoApartadoDeterminacion(null, iTramite.getIden(),true);
                codigo=maximoCodigoDeterminacion(iTramite.getIden(),1);
                s="Insert Into planeamiento.Determinacion (idtramite, idpadre, idcaracter, " +
                    "apartado, nombre, codigo, bsuspendida, orden) " +
                    "Values (" + iTramite.getIden() + ", Null, " +
                    ClsDatos.ID_CARACTER_GRUPODEENTIDADES + ", '" + aptdo + "', " +
                    "'Grupo de entidades', '" + codigo +"', false, " + 
                    "(Select 1+Max(orden) From planeamiento.Determinacion " + 
                    "Where idTramite=" + iTramite.getIden() +" And idPadre Is Null)) ";
                i=ClsConexion.ejecutar(s);
                if (i==1){
                    idDet=ultimoIden("planeamiento.Determinacion");
                    iDetGrupoDeEntidades=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDet);
                } else {
                    mLog.warn("    No se ha podido crear la determinación 'Grupo de entidades' en el idTramite=" + iTramite.getIden());
                    return null;
                }
            }
            
            // Identificador de la determinación 'Grupo de entidades'
            idDet=iDetGrupoDeEntidades.getIden();
            
            // Crear la determinación 'Carpeta'
            aptdo=ultimoApartadoDeterminacion(iDetGrupoDeEntidades.getIden(), iTramite.getIden(),true);
            codigo=maximoCodigoDeterminacion(iTramite.getIden(),1);
            s="Insert Into planeamiento.Determinacion (idtramite, idpadre, idcaracter, " +
                "apartado, nombre, codigo, bsuspendida, orden) " +
                "Values (" + iTramite.getIden() + ", " + idDet + ", " +
                ClsDatos.ID_CARACTER_VALORREFERENCIA + ", '" + aptdo + "', " +
                "'Grupo de entidades', '" + codigo +"', false, (Select 1+Max(orden) From planeamiento.Determinacion " + 
                    "Where idTramite=" + iTramite.getIden() +" And idPadre=" + idDet + ")) ";
            i=ClsConexion.ejecutar(s);
            if (i==1){
                idDet=ultimoIden("planeamiento.Determinacion");
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDet);
                // Crear la opción correspondiente
                s="Insert Into planeamiento.opciondeterminacion (iddeterminacion, " +
                    "iddeterminacionvalorref) Values (" + iDetGrupoDeEntidades.getIden() + ", " +
                    idDet + ") ";
                i=ClsConexion.ejecutar(s);
                if (i!=1){
                    mLog.warn("    Se ha creado la determinación 'Carpeta' en el idTramite=" + iTramite.getIden() + ", pero no se ha podido crear la OpcionDeterminacion");
                }
                return iDet;
            } else {
                mLog.warn("    No se ha podido crear la determinación 'Carpeta' en el idTramite=" + iTramite.getIden());
                return null;
            }
            
        } catch (Exception e){
            mLog.error("Fallo en determinacionCarpetaPorTramite: " + e + ". Código 23021." );
        }
        mLog.warn("    No se ha encontrado la determinación 'Carpeta' del idTramite=" + iTramite.getIden());
        return null;
    }

    public static Opciondeterminacion opcionPorDeterminaciones(Determinacion iDet, Determinacion iDetValorRef){
        String s;
        Opciondeterminacion iOpcion;
        List lista;
        int idOpcion;

        try{
            // Averigua cuál es la opción que relaciona a ambas determinaciones
            if (iDet!=null && iDetValorRef!=null){
                s="Select iden From planeamiento.Opciondeterminacion " +
                "Where iddeterminacion=" + iDet.getIden() + " " +
                "And iddeterminacionvalorref=" + iDetValorRef.getIden();
                lista = ClsConexion.recordList(s);
                if (lista!=null){
                    if (lista.size() == 1) {
                        idOpcion=Integer.parseInt(lista.get(0).toString());
                        iOpcion=(Opciondeterminacion) ClsConexion.dameEntity(Opciondeterminacion.class, idOpcion);
                        return iOpcion;
                    }
                }
            }
        } catch (Exception e){
            mLog.error("Fallo en opcionPorDeterminaciones. " + e + ". Código 23022." );
        }
        return null;
    }

    public static int maximaSuperposicionDeEntidad(int idEntidad){
        int maxSup=0;
        String s="";
        int i;

        try{
            // Primero se asegura que no haya valores nulos
            s="Update planeamiento.Entidaddeterminacionregimen Set superposicion=0 " +
                    "Where superposicion Is Null";
            ClsConexion.ejecutar(s);

            s="Select Max(superposicion) " +
                    "From planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced, " +
                    "planeamiento.Entidaddeterminacion ed " +
                    "Where edr.idCaso=ced.iden " +
                    "And ced.idEntidadDeterminacion=ed.iden ";
            maxSup=Integer.parseInt(String.valueOf(ClsConexion.recordList(s).get(0).toString()));
        } catch (Exception e){
            mLog.error("Fallo en maximaSuperposicionDeEntidad. " + e + ". Código 23023." );
            return 0;
        }

        return maxSup;
    }    

    public static List <Integer> entidadesPorDeterminacionValorReferencia(Tramite iTramite, Determinacion iDet, Determinacion iDetVR){
        String s="";
        List <Integer> lista=null;

        try{
            lista=new ArrayList();
            s="Select Distinct ed.identidad From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Casoentidaddeterminacion ced, " +
                    "planeamiento.Entidad e, " +
                    "planeamiento.Entidaddeterminacionregimen edr,  " +
                    "planeamiento.Opciondeterminacion od " +
                    "Where ced.identidaddeterminacion=ed.iden " +
                    "And (edr.idcaso=ced.iden Or edr.idcasoaplicacion=ced.iden) " +
                    "And edr.idopciondeterminacion=od.iden " +
                    "And od.iddeterminacion=" + iDet.getIden() + " " +
                    "And od.iddeterminacionvalorref=" + iDetVR.getIden() + " " +
                    "And ed.iddeterminacion=" + iDet.getIden() + " " +
                    "And ed.identidad=e.iden And e.idtramite=" + iTramite.getIden() + " ";
            lista=ClsConexion.recordList(s);
        } catch (Exception e) {
            mLog.error("Fallo en entidadesPorDeterminacionValorReferencia. " + e + ". Código 23024." );
            return null;
        }

        return lista;
    }

    // TODO FGA
    
    public static int eliminarPlanesEncargados(int iden)
    {
    	int nRegistros=0;
    	mLog.info("[eliminarPlanesEncargados] iden"+iden );
    	
    	
    	// Compruebo si hay planes vigente, y si hay los borro
    	String sComprobacion="Select id From gestionfip.planesencargados Where planencargado_iden=" + iden;
    	int nRegistrosComprobacion=0;
    	List regComprobacion=ClsConexion.recordList(sComprobacion);
    	
    	nRegistrosComprobacion = regComprobacion.size();
    	
    	if (nRegistrosComprobacion!=0)
    	{
    		// Borro el registro
    		mLog.info("[eliminarPlanesEncargados] Borro el registro");
    		String s="Delete From gestionfip.planesencargados Where planencargado_iden=" + iden;
            nRegistros=ClsConexion.ejecutar(s);
    	}
    	else
    	{
    		mLog.info("[eliminarPlanesEncargados] No existe ese iden como plan encargado");
    	}
    	
    	return nRegistros;
    }
    
    public static int eliminarPlanesVigentes(int iden)
    {
    	int nRegistros=0;
    	mLog.info("[eliminarPlanesVigentes] iden"+iden );
    	// Compruebo si hay planes vigente, y si hay los borro
    	String sComprobacion="Select id From gestionfip.planesvigente Where plan_iden=" + iden;
    	int nRegistrosComprobacion=0;
    	List regComprobacion=ClsConexion.recordList(sComprobacion);
    	
    	nRegistrosComprobacion = regComprobacion.size();
    	
    	if (nRegistrosComprobacion!=0)
    	{
    		// Borro el registro
    		mLog.info("[eliminarPlanesVigentes] Borro el registro");
    		String s="Delete From gestionfip.planesvigente Where plan_iden=" + iden;
            nRegistros=ClsConexion.ejecutar(s);
    	}
    	else
    	{
    		mLog.info("[eliminarPlanesVigentes] No existe ese iden como plan vigente");
    	}
    	
    	
    	
    	
    	
    	return nRegistros;
    }
    
    public static int eliminarObjeto(String tabla, int iden, int idTramiteOrdenante){
        
    	//mLog.info("    		eliminarObjeto[tabla=" + tabla + " | iden="+iden+" | idTramiteOrdenante="+idTramiteOrdenante+"]...");
    	String s="";
        int i;
        int idTabla;
        int idOperacion;
        List lista;
        Iterator it;
        int nRegistros=0;
        Operacion iOperacion;
        Plan iPlan;
        int idTramite;
        int nDep;
        String codigoPlanOrdenante;
        String codigoPlan;

        // Dependencias entre tablas: nDependencias y 3 columnas:
        //      0=TablaPrincipal
        //      1=TablaDependiente
        //      2=Campo

        try{
            codigoPlanOrdenante=ClsMain.codigoPlanPorIdTramite(idTramiteOrdenante);
            
            nDep=ClsDatos.gDependencias.length;
            for (i=0;i<nDep;i++){
                if (tabla.equalsIgnoreCase(ClsDatos.gDependencias[i][1])){
                    lista=new ArrayList();
                    s="Select t.iden From planeamiento." + ClsDatos.gDependencias[i][0] + " t "+
                            "Where t." + ClsDatos.gDependencias[i][2] + "=" + iden + " ";
                    lista=ClsConexion.recordList(s);
                    it=lista.iterator();
                    while (it.hasNext()){
                        idTabla=Integer.parseInt(it.next().toString());
                        eliminarObjeto(ClsDatos.gDependencias[i][0], idTabla, idTramiteOrdenante);
                    }
                }
            }

            s="Select Count(*) From planeamiento." + tabla + " Where iden=" + iden + " ";
            nRegistros=Integer.parseInt(ClsConexion.recordList(s).get(0).toString());

            // Si hay algo que borrar...
            if (nRegistros>0){
                // Si la tabla es de operaciones, se manda un aviso, ya que, según
                //  se van procesando las operaciones, se van borrando, y, por lo tanto,
                //  no debería quedar ninguna que pueda ser borrada en este
                //  procedimiento.
                idOperacion=0;
                if (nRegistros>0 && (tabla.equalsIgnoreCase("Operacion") ||
                        tabla.equalsIgnoreCase("OperacionEntidad") ||
                        tabla.equalsIgnoreCase("OperacionDeterminacion") ||
                        tabla.equalsIgnoreCase("OperacionRelacion"))){

                    if (tabla.equalsIgnoreCase("Operacion")){
                        idOperacion=iden;
                    } else {
                        lista=new ArrayList();
                        s="Select idoperacion From planeamiento." + tabla + " Where iden=" + iden;
                        lista=ClsConexion.recordList(s);
                        idOperacion=Integer.parseInt(lista.get(0).toString());
                    }
                }

                // Si es un plan, se manda un mensaje informativo
                if (nRegistros>0 && tabla.equalsIgnoreCase("Plan")){
                    iPlan=(Plan) ClsConexion.dameEntity(Plan.class, iden);
                    if(iPlan!=null){
                        mLog.info("    Eliminando Plan [" + iPlan.getCodigo() + "]...");
                    }
                }

                // Elimina el objeto solicitado.
                s="Delete From planeamiento." + tabla + " Where iden=" + iden + " ";
                nRegistros=ClsConexion.ejecutar(s);

                // Si se trata de una operación, también se elimina el registro correspondiente de la
                //  tabla Operacion, aunque no es dependiente de OperacionEntidad, OperacionDeterminacion,
                //  ni de OperacionRelacion.
                if (idOperacion>0){
                    iOperacion=(Operacion) ClsConexion.dameEntity(Operacion.class, idOperacion);
                    idTramite=idTramitePorIdOperacion(iOperacion.getIden());
                    codigoPlan=ClsMain.codigoPlanPorIdTramite(idTramite);
                    s="Delete From planeamiento.Operacion Where iden=" + idOperacion + " ";
                    nRegistros=ClsConexion.ejecutar(s);
                    if (nRegistros>0){
                        s="        Aviso: El plan [" + codigoPlanOrdenante + "] " + 
                            "ha eliminado la operación iden=" + idOperacion + " " + 
                            "del plan [" + codigoPlan + "] sin haber sido ejecutada.";
                        mLog.warn(s);
                    }
                }
            }
            
        } catch (Exception e){
            mLog.error("Fallo en eliminarObjeto. " + e + ". Código 23025." );
            return -1;
        }
        return nRegistros;
    }

    public static Entidad ambitoAplicacionPorTramite(Tramite iTramite){
        Entidad iEntidad=null;
        String s="";
        Determinacion iDetGrupoDeEntidades;
        Determinacion iDetGrupo;
        List lista;
        Iterator it;

        try {
            // Determinación 'Grupo de Entidades' del iTramite
            iDetGrupoDeEntidades=determinacionGrupoDeEntidadesPorTramite(iTramite);

            lista=new ArrayList();
            s="Select iden From planeamiento.Entidad Where idtramite=" + iTramite.getIden();
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                iEntidad=(Entidad) ClsConexion.dameEntity(Entidad.class, it.next());
                iDetGrupo=determinacionGrupoPorEntidad(iEntidad);
                if (iDetGrupo!=null){
                    if (iDetGrupo.getCodigo().equals(ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION)){
                        break;
                    }
                    if (codigoDeterminacionBasePorIdDeterminacion(iDetGrupo.getIden()).equals(ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION)){
                        break;
                    }
                }
                iEntidad=null;
            }

        } catch (Exception e) {
            mLog.error("Fallo en ambitoAplicacionPorTramite. " + e + ". Código 23062." );
            return null;
        }

        return iEntidad;
    }

    public static List <Integer> idDeterminacionesValorReferenciaPorDeterminacion(Determinacion iDet){
        String s;
        List lista=new ArrayList();

        try{
            s="Select iddeterminacionvalorref From planeamiento.Opciondeterminacion " +
                    "Where iddeterminacion=" + iDet.getIden() + " ";
            lista=ClsConexion.recordList(s);
        } catch (Exception e){
            mLog.error("Error al construir la lista de valores de referencia de la determinación [" +
                iDet.getCodigo() + "] del plan [" +
                codigoPlanPorIdDeterminacion(iDet.getIden()) + "]: " + e + ". Código 23041." );
            return null;
        }
        return lista;
    }

    public static void eliminarOperadores(){
        String s;
        Iterator it1;
        Iterator it2;
        int iden;
        List lista;
        Entidad iEnt;
        Determinacion iDet;
        Plan iPlan;
        int idAmbito;

        try{
            // Recupera el identificador del ámbito.
            idAmbito=ClsDatos.gIdAmbito;

            // Primero, se eliminan los planes operadores (los que tienen el
            //  tipo de operación diferente de ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE,
            //  de ClsDatos.ID_TIPOOPERACIONPLAN_SUSTITUCION y 
            //  ClsDatos.ID_TIPOOPERACIONPLAN_INCORPORACION
            mLog.info("");
            mLog.info("Eliminando planes operadores...");
            it1=ClsDatos.gListaIdTramitesRefundir.iterator();
            while (it1.hasNext()){
                iden=Integer.parseInt(it1.next().toString());
                lista=new ArrayList();
                s="Select Distinct p.iden From planeamiento.Plan p, planeamiento.Tramite t, " +
                        "planeamiento.Operacionplan op, diccionario.Instrumentotipooperacionplan itop " +
                        "Where p.idambito=" + idAmbito + " " +
                        "And t.iden=" + iden + " And t.idplan=p.iden " +
                        "And op.idplanoperador=p.iden And op.idinstrumentotipooperacion=itop.iden " +
                        "And itop.idtipooperacionplan<>" + ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE + " " +
                        "And itop.idtipooperacionplan<>" + ClsDatos.ID_TIPOOPERACIONPLAN_SUSTITUCION + " " +
                        "And itop.idtipooperacionplan<>" + ClsDatos.ID_TIPOOPERACIONPLAN_INCORPORACION + " ";
                lista=ClsConexion.recordList(s);
                if (lista.size()==1){
                    iden=Integer.parseInt(lista.get(0).toString());
                    iPlan=(Plan) ClsConexion.dameEntity(Plan.class, iden);
                    eliminarPlanesEncargados(iden);
                    eliminarPlanesVigentes(iden);
                    ClsMain.eliminarObjeto("Plan", iden, 0);
                } 
            }

            // Eliminar determinaciones operadoras
            mLog.info("Eliminando determinaciones operadoras...");
            it1=ClsDatos.gListaIdTramitesRefundir.iterator();
            while (it1.hasNext()){
                iden=Integer.parseInt(it1.next().toString());
                lista=new ArrayList();
                s="Select d.iden From planeamiento.Determinacion d " +
                        "Where d.idtramite=" + iden + " " +
                        "And d.idcaracter=" + ClsDatos.ID_CARACTER_OPERADORA + " ";
                lista=ClsConexion.recordList(s);
                it2=lista.iterator();
                while (it2.hasNext()){
                    iden=Integer.parseInt(it2.next().toString());
                    iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                    mLog.info("         Eliminando determinación [" + iDet.getCodigo() + "] del plan [" + codigoPlanPorIdDeterminacion(iDet.getIden()) + "]");
                    ClsMain.eliminarObjeto("Determinacion", iden, 0);
                }
            }

            // Eliminar entidades operadoras
            mLog.info("Eliminando entidades operadoras...");
            it1=ClsDatos.gListaIdTramitesRefundir.iterator();
            while (it1.hasNext()){
                iden=Integer.parseInt(it1.next().toString());
                lista=new ArrayList();
                s="Select e.iden From planeamiento.Entidad e " +
                        "Where e.idtramite=" + iden + " ";
                lista=ClsConexion.recordList(s);
                it2=lista.iterator();
                while (it2.hasNext()){
                    iden=Integer.parseInt(it2.next().toString());
                    iEnt=(Entidad) ClsConexion.dameEntity(Entidad.class, iden);
                    iDet=ClsMain.determinacionGrupoPorEntidad(iEnt);
                    if(iDet!=null){
                        if (codigoDeterminacionBasePorIdDeterminacion(iDet.getIden()).equals(ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_OPERADOR)){
                            mLog.info("         Eliminando entidad [" + iEnt.getCodigo() + "] del plan [" + codigoPlanPorIdDeterminacion(iDet.getIden()) + "]");
                            ClsMain.eliminarObjeto("Entidad", iden, 0);
                        } else {
                            // No se ha encontrado el código de la determinación base correspondiente al
                            //  grupo de entidad de la entidad. Se comprueba si el nombre es 'Operador'
                            if (iDet.getNombre().equalsIgnoreCase("Operador") && 
                                    iDet.getIdcaracter()==ClsDatos.ID_CARACTER_VALORREFERENCIA){
                                mLog.info("         La entidad tiene asignado un grupo llamado 'Operador', aunque no está vinculado al plan base. Se asume que la entidad es una operadora.");
                                mLog.info("         Eliminando entidad [" + iEnt.getCodigo() + "] del plan [" + codigoPlanPorIdDeterminacion(iDet.getIden()) + "]");
                                ClsMain.eliminarObjeto("Entidad", iden, 0);
                            }
                        }
                    } else {
                        mLog.warn("***           La entidad [" + iEnt.getCodigo() + "] del trámite iden=" + iEnt.getTramite().getIden() + " no tiene grupo asignado.");
                    }
                }
            }
        } catch (Exception e){
            mLog.error("Error al eliminar operadores: " + e + ". Código 23036." );
        }
    }

    public static void listarOperacionesNoEjecutadas(){
        String s;
        List lista;
        Tramite iTramite;
        int idTramite;
        Iterator it;

        try {
            mLog.info("");
            mLog.info("Buscando operaciones sin ejecutar...");
            it=ClsDatos.gListaIdTramitesRefundir.iterator();
            while (it.hasNext()){
                idTramite=Integer.parseInt(it.next().toString());
                iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramite);
                lista=new ArrayList();
                s="Select o.iden From planeamiento.Operacion o " +
                        "Where o.idtramiteordenante=" + idTramite;
                lista=ClsConexion.recordList(s);
                if (lista.size()>0){
                    mLog.warn("*** No se han ejecutado " + lista.size() + " operaciones ordenadas por el trámite fecha=" + iTramite.getFecha());
                }
            }
        } catch (Exception e){
            mLog.error("Se ha producido un fallo al listar las operaciones no ejecutadas durante el proceso de refundido."  + e + ". Código 23042.");
        }
    }

    public static String codigoTramite(Tramite iTramite){
        String s;
        String codigo="";
        Plan iPlan;
        int i;
        int idPlan;

        // El código del trámite refundido (sin encriptar en MD5) se compone de
        //  la concatenación de los siguientes 20 caracteres: aaaaaapppppp0000ttii
        //      donde:
        //      aaaaaa = iden del ámbito
        //      pppppp = iden del plan
        //      0000tt = iden del tipo de trámite
        //          ii = iteración

        try{
            idPlan=idPlanPorIdTramite(iTramite.getIden());
            iPlan=(Plan) ClsConexion.dameEntity(Plan.class, idPlan);

            // Ámbito
            s="000000" + String.valueOf(iPlan.getIdambito());
            i=s.length();
            codigo=codigo + s.substring(i-6, i);

            // Plan
            s="000000" + Integer.parseInt(iPlan.getCodigo());
            i=s.length();
            codigo=codigo + s.substring(i-6, i);

            // Tipo de trámite
            s="000000" + String.valueOf(iTramite.getIdtipotramite());
            i=s.length();
            codigo=codigo + s.substring(i-6, i);

            // Iteracion
            s="00" + String.valueOf(iTramite.getIteracion());
            i=s.length();
            codigo=codigo + s.substring(i-2, i);
            
        } catch (Exception e){
            mLog.error("No se ha logrado calcular el código del trámite."  + e + ". Código 23043.");
            return "";
        }

        return codigo;
    }

    public static String iteracionPorPlanYtipoTramite(int id_tt,Plan plan){
        // Devuelve un texto de dos caracteres con la siguiente iteración disponible para los
        //  trámites del tipo y plan indicados. Si no existe ningún tramite que cumpla dichos
        //  requisitos, se devuelve un 1. En caso de error, se devuelve 0.
        String s;
        List lista=new ArrayList();
        int iteracion;
        DecimalFormat format2 = new DecimalFormat("00");

        try{
            s="Select t.iteracion From planeamiento.tramite t Where t.idtipotramite=" + id_tt + " " +
                    "And t.idplan=" + plan.getIden() + " Order By t.iteracion Desc ";
            lista=gEM.createNativeQuery(s).getResultList();
            if (lista.size()==0){
                iteracion=1;
            } else {
                iteracion=1+Integer.parseInt(lista.get(0).toString());
            }
        } catch (Exception e){
            iteracion=0;
        }

        return format2.format(iteracion);

    }

    private static String cadenaNcaracteres(int n, String s ){
        String cadena="";
        int i;

        for (i=1;i<=n;i++){
            cadena=cadena + s;
        }
        return cadena;
    }

    public static void eliminarOperacion(int iden){
        String s;

        // Borra la operación antes de ejecutarla para que no salga un "aviso" al borrar las dependencias
        //  de los elementos que se eliminen en virtud de dicha operación. No se usa la
        //  función ClsMain.eliminarObjeto() para que no se produzca el aviso de que se está borrando una
        //  operación antes de haber sido ejecutada.
        s="Delete From planeamiento.Operacionentidad Where idoperacion=" + iden;
        ClsConexion.ejecutar(s);
        s="Delete From planeamiento.Operaciondeterminacion Where idoperacion=" + iden;
        ClsConexion.ejecutar(s);
        s="Delete From planeamiento.Operacionrelacion Where idoperacion=" + iden;
        ClsConexion.ejecutar(s);
        s="Delete From planeamiento.Operacion Where iden=" + iden;
        ClsConexion.ejecutar(s);
    }

    public static void copiarAmbitoAplicacion(Tramite iTramiteHasta, Tramite iTramiteDesde){
        // Se copia la entidad 'Ámbito de Aplicación' del trámite iTramiteDesde al
        //  trámite iTramiteHasta, cambiándolo al grupo 'Afección' y poniéndole el nombre,
        //  compuesto por el texto '(Suspendido)' y el nombre del plan del iTramiteDesde.

        String codigoEnt;
        String s="";
        Entidad iEntDesde;
        int nRegistros;
        String txtEntidad;
        Determinacion iDet;
        Determinacion iDetPadre;
        Determinacion iDetVR;
        Opciondeterminacion iOpcion;
        int iden;
        List lista;
        String aptdo;
        int idPlanBase;
        int idDetPadre;

        try {
            // Averigua cuál es el Plan Base
            lista=new ArrayList();
            s="Select p.idplanbase From planeamiento.plan p, planeamiento.tramite t " +
                    "Where t.idplan=p.iden And t.iden=" + iTramiteHasta.getIden() + " ";
            lista=ClsConexion.recordList(s);
            idPlanBase=Integer.parseInt(lista.get(0).toString());

            // Ámbito de aplicación del iTramiteDesde
            iEntDesde=ambitoAplicacionPorTramite(iTramiteDesde);

            // Crea la entidad en iTramiteHasta
            codigoEnt = ClsMain.maximoCodigoEntidad(iTramiteHasta.getIden(), 1);
            txtEntidad="(Suspendido) " + nombrePlanPorIdTramite(iTramiteDesde.getIden());
            if (txtEntidad.length()>100){
                txtEntidad=txtEntidad.substring(0, 99);
            }

            s = "Insert Into planeamiento.Entidad (idtramite, idpadre, clave, " +
                    "nombre, codigo, bsuspendida)" +
                    "Values (" + iTramiteHasta.getIden() + ", null, 'Plan suspendido', '" +
                    txtEntidad + "', '" + codigoEnt + "', false) ";
            nRegistros = ClsConexion.ejecutar(s);
            if (nRegistros!=1){
                mLog.error("Error al copiar el ámbito de aplicación. Código 23045.");
                return;
            }

            // Averigua cuál es la determinación 'Grupo de Entidades' del iTramiteHasta
            iDet=ClsMain.determinacionGrupoDeEntidadesPorTramite(iTramiteHasta);
            
            // Averigua cuál es la determinación 'Valor de Referencia' correspondiente 
            //  al grupo de entidades 'Afección'. Si no existe, se crea. Y, además, se crea
            //  la opción correspondiente.
            lista=new ArrayList();
            s="Select d.iden From planeamiento.determinacion d, planeamiento.determinacion db " + 
                    "Where d.iddeterminacionbase=db.iden " + 
                    "And db.codigo='" + ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_AFECCION + "' " +
                    "And d.idcaracter=" + ClsDatos.ID_CARACTER_VALORREFERENCIA + " " +
                    "And d.idtramite=" + iTramiteHasta.getIden();
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                // No existe la determinación 'Afección' como grupo de entidad en iTramiteHasta y, por lo
                //  tanto, hay que crearla.

                // Para ello, en primer lugar hay que averiguar cuál es
                //  el siguiente 'apartado' que le va a corresponder como hija de iDet
                aptdo=ClsMain.ultimoApartadoDeterminacion(iDet.getIden(), iTramiteHasta.getIden(),true);

                // Averigua cual es la carpeta 'Determinaciones aportadas...'
                iDetPadre=ClsOperacionDeterminacion.crearCarpetaDeterminacionesAportadas(iTramiteHasta.getIden(), iTramiteDesde.getIden());

                // Crea determinación 'Afección' como valor de referencia.
                s="Insert Into planeamiento.determinacion (idtramite, idpadre, idcaracter, " +
                    "apartado, nombre, codigo, iddeterminacionbase, bsuspendida, orden) " +
                    "Values (" + iTramiteHasta.getIden() + ", " + iDetPadre.getIden() + ", " +
                    ClsDatos.ID_CARACTER_VALORREFERENCIA + ", '" + aptdo + "', 'Afección', '" +
                    ClsMain.maximoCodigoDeterminacion(iTramiteHasta.getIden(), 1) + "', Null, " + 
                    "false,(Select 1+Max(orden) From planeamiento.Determinacion " + 
                    "Where idTramite=" + iTramiteHasta.getIden() + " " + 
                    "And idPadre" + iDetPadre.getIden() + ")) ";
                if (ClsConexion.ejecutar(s)!=1){
                    mLog.error("Error al copiar el ámbito de aplicación. Código 23045.");
                    return;
                }
                iden=ClsMain.ultimoIden("planeamiento.determinacion");
                iDetVR=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                // Crea la opción correspondiente a iDet e iDetVR
                s="Insert Into planeamiento.opciondeterminacion (iddeterminacion, iddeterminacionvalorref) " +
                        "Values (" + iDet.getIden() + ", " + iDetVR.getIden() + ") ";
                if (ClsConexion.ejecutar(s)!=1){
                    mLog.error("Error al copiar el ámbito de aplicación. Código 23045.");
                    return;
                }
                iden=ClsMain.ultimoIden("planeamiento.opciondeterminacion");
                iOpcion=(Opciondeterminacion) ClsConexion.dameEntity(Opciondeterminacion.class, iden);

            } else {
                iden=Integer.parseInt(lista.get(0).toString());
                iDetVR=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                iOpcion=ClsMain.opcionPorDeterminaciones(iDet, iDetVR);
            }
            
            // Inserta los registros de valor en EntidadDeterminacion, CasoEntidadDeterminacion, y
            //  EntidadDeterminacionRegimen

            // EntidadDeterminacion
            s="Insert Into planeamiento.entidaddeterminacion (identidad, iddeterminacion) " +
                    "(Select Max(iden), " + iDet.getIden() + " From planeamiento.entidad)";
            nRegistros = ClsConexion.ejecutar(s);
            if (nRegistros!=1){
                mLog.error("Error al copiar el ámbito de aplicación. Código 23045.");
            }

            // CasoEntidadDeterminacion
            s="Insert Into planeamiento.casoentidaddeterminacion (identidaddeterminacion) " +
                    "(Select Max(iden) From planeamiento.entidaddeterminacion)";
            nRegistros = ClsConexion.ejecutar(s);
            if (nRegistros!=1){
                mLog.error("Error al copiar el ámbito de aplicación. Código 23045.");
            }

            // EntidadDeterminacionRegimen
            s="Insert Into planeamiento.entidaddeterminacionregimen (idcaso, idopciondeterminacion, " +
                    "superposicion) " +
                    "(Select Max(iden), " + iOpcion.getIden() + ", 0 " +
                    "From planeamiento.casoentidaddeterminacion)";
            nRegistros = ClsConexion.ejecutar(s);
            if (nRegistros!=1){
                mLog.error("Error al copiar el ámbito de aplicación. Código 23045.");
            }

        } catch (Exception e){
            mLog.error("Error al copiar el ámbito de aplicación: " + s + "\n" + e + ". Código 23045.");
        }
    }

    public static boolean bCopiarDocumentosDeTramite(){
        // En cada carpeta "Entidades aportadas por..." se copia la entidad "Ámbito de Aplicación" de cada
        //  plan operador, y se le vinculan todos los documentos, con el objetivo de que el plan refundido
        //  contenga todos los documentos de todos los planes que lo componen.
        String s="";
        int idTramiteRef;
        Tramite iTramiteRef;
        int idTramite;
        int idEntCarpeta;
        Entidad iEntCarpeta;
        Iterator it1;
        Vector v;
        String clave;
        int nRegistros;
        Object obj[];
        int i;
        int idDocumento;
        String sGeom;
        String hoja;
        List lista;

        try{
            mLog.info("Copiando documentos de planes operadores...");

            // Averigua cuál es el plan refundido. Debe ser el único plan que quede con
            //  un tipo de operación ID_TIPOOPERACIONPLAN_NOPROCEDE o ID_TIPOOPERACIONPLAN_SUSTITUCION
            idTramiteRef=idTramiteRefundido();
            if (idTramiteRef==0){
                return false;
            }
            iTramiteRef=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteRef);

            // Recorre la lista de documentos para ir creando las carpetas de "Aportadas por..." en
            //  el trámite refundido (si no existen ya), y crear los documentos que provienen del
            //  resto de trámites.
            it1=ClsDatos.gListaDatosDoc.iterator();
            while (it1.hasNext()){
                // Recupera el texto de la entidad 'Aportadas por...' que se guardó en gListaDatosDoc
                v=new Vector();
                v=(Vector) it1.next();
                idTramite=Integer.parseInt(v.get(0).toString());

                // Crea los documentos que provengan de trámites diferentes del idTramiteRef
                if (idTramite!=idTramiteRef){
                    clave=v.get(2).toString();
                    sGeom=v.get(10).toString();
                    hoja=v.get(11).toString();

                    // Averigua si la entidad "Aportadas por..." ya existe por haber sido creada 
                    //  con anterioridad. Si no existe, la crea.
                    iEntCarpeta=ClsOperacionEntidad.crearCarpetaEntidadesAportadas(idTramiteRef, idTramite);
                    idEntCarpeta=iEntCarpeta.getIden();
                    
                    // Crea el registro en la tabla Documento
                    s="Insert Into planeamiento.Documento (idtramite, nombre, archivo, comentario, escala, " +
                            "iddocumentooriginal, idTipoDocumento, idgrupodocumento) " +
                            "Values (" + idTramiteRef;
                    for (i=3;i<=9; i++){
                        if(v.get(i).toString().trim().equalsIgnoreCase("null")){
                            s = s + ", null";
                        } else {
                            s = s + ", '" + v.get(i).toString()+"'";
                        }
                    }
                    s = s + ") ";
                    s = s.replace("\\", "\\\\");

                    idDocumento=Integer.parseInt(v.get(12).toString());
                    
                    // Comprueba si existe el documento, o si ha sido eliminado
                    lista=new ArrayList();
                    s="Select D.iden From planeamiento.documento D Where D.iden=" + idDocumento;
                    lista=ClsConexion.recordList(s);
                    if (lista.size()>0){
                        // Mensaje
                        s="    Copiado documento '" + v.get(3).toString() + "' del archivo '" +
                                v.get(4).toString() + "' a la entidad '" + iEntCarpeta.getNombre() + "'";
                        mLog.info(s);

                        // Crea el registro en DocumentoEntidad
                        s="Insert Into planeamiento.Documentoentidad (iddocumento, identidad) " +
                                "Values (" + idDocumento + ", " + idEntCarpeta + ") ";
                        nRegistros = ClsConexion.ejecutar(s);
                    }
                }
            }
            
        } catch (Exception e){
            mLog.error("Fallo al copiar los documentos de los trámites: " + e + ". Código 23049." );
            return false;
        }
        return true;
    }

    public static void guardarDocumentosDeTramite(){
        int idTramite;
        String s;
        Iterator it1;
        Iterator it2;
        Tramite iTramite;
        String texto;
        List lista;
        int i;
        Vector v;
        Object obj[];
        String codigoPlan;
        Plan iPlan;

        try{
            it1=ClsDatos.gListaIdTramitesRefundir.iterator();
            while (it1.hasNext()){
                idTramite=Integer.parseInt(it1.next().toString());
                iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramite);
                iPlan=(Plan) ClsConexion.dameEntity(Plan.class, idPlanPorIdTramite(idTramite));
                texto= ClsDatos.TEXTO_APORTADAS + ClsMain.datosPlan(iPlan);
                codigoPlan=iPlan.getCodigo();

                lista=new ArrayList();
                s="Select Cast(d.nombre As Text) As C1, Cast(d.archivo As Text) As C2, " + 
                        "Cast(d.comentario As Text) As C3, " +
                        "d.escala As C4, d.iddocumentooriginal As C5, " +
                        "d.idtipodocumento As C6, d.idgrupodocumento As C7, " + 
                        "AsText(dshp.geom) As C7, Cast(dshp.hoja As Text) As C8, " +
                        "d.iden As C9 " +
                        "From planeamiento.Documento d " + 
                        "Left Join planeamiento.documentoshp dshp On (d.iden=dshp.iddocumento) " +
                        "Where d.idtramite=" + idTramite + " " +
                        "And d.iden Not In(Select iddocumento From planeamiento.DocumentoDeterminacion " +
                        "Union " +
                        "Select iddocumento From planeamiento.DocumentoEntidad " +
                        "Union " +
                        "Select iddocumento From planeamiento.DocumentoDeterminacion) ";
                lista=ClsConexion.recordList(s);
                if (lista.size()>0){
                    it2=lista.iterator();
                    while (it2.hasNext()){
                        v=new Vector();
                        v.add(idTramite);
                        v.add(texto);
                        v.add(codigoPlan);

                        obj=(Object[]) it2.next();
                        for (i=0;i<obj.length;i++){
                            if(obj[i]==null){
                                v.add("null");
                            } else {
                                v.add(obj[i]);
                            }
                        }
                        ClsDatos.gListaDatosDoc.add(v);
                    }
                }
            }
        } catch (Exception e){
            mLog.error("Fallo al recuperar los documentos de los trámites: "  + e + ". Código 23052.");
        }
    }

    public static void reasignarDependencias(){
        // Reasignar al trámite refundido todas las determinaciones de otros trámites que están ligadas de
        //  alguna manera a las entidades del refundido.
        // Los vínculos que se tienen en cuenta son:
        //      - Tabla EntidadDeterminacion
        //      - Tabla EntidadDeterminacionRegimen
        //      - Tabla OpcionDeterminacion apuntada por EntidadDeterminacionRegimen
        //      - Tabla DeterminacionGrupoEntidad
        //      - Tabla Determinacion o Entidad que están ligadas vía tabla Relacion con determinaciones
        //          o entidades del trámite refundido.
        //      - Tabla Relacion que apunta vía tabla VectorRelacion a Determinaciones o Entidades del trámite refundido

        int idTramiteRef;
        int idDeterminacion;
        int idEntidad;
        int idDetGrupo;
        int idDGE;
        Determinacion iDetEquivalente;
        Tramite iTramiteRef;
        Tramite iTramite;
        Tramite iTram;
        Tramite iTramPadre;
        String s="";
        List lista;
        Iterator it;
        Determinacion iDetPadre;
        Determinacion iDet;
        Determinacion iDetGrupo;
        Entidad iEntPadre;
        Entidad iEnt;
        int nRegistros;
        Object obj[];
        List listaIdTramites=new ArrayList();
        Object o;

        try{
            mLog.info("");
            mLog.info("Moviendo determinaciones dependientes al trámite refundido...");
            idTramiteRef=idTramiteRefundido();
            iTramiteRef=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteRef);
            
            // Hace una copia dela lista de los iden de los trámites a refundir 
            //  para controlar que no se muevan elementos del plan base, del 
            //  propio trámite refundido, ni de ningún trámite que no haya
            //  sido seleccionado para refundir.
            listaIdTramites.addAll(ClsDatos.gListaIdTramitesRefundir);
            o=idTramiteRef;
            listaIdTramites.remove(o);

            // Determinaciones ligadas por EntidadDeterminacion
            mLog.info("    -> Condiciones urbanísticas...");
            lista=new ArrayList();
            s="Select Distinct d.iden From planeamiento.Entidad e, planeamiento.Determinacion d, " +
                    "planeamiento.Entidaddeterminacion ed " +
                    "Where e.idtramite=" + idTramiteRef + " And d.idtramite<>" + idTramiteRef + " " +
                    "And ed.identidad=e.iden And ed.iddeterminacion=d.iden ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                idDeterminacion=Integer.parseInt(it.next().toString());
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDeterminacion);

                // Se excluyen las determinaciones del plan base y las de trámites que no están en la lista de los refundibles
                iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramitePorIdDeterminacion(iDet.getIden()));
                if (iTramite.getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE){
                    if (listaIdTramites.contains(iTramite.getIden())){
                        // Averigua el iden de la carpeta de determinaciones aportadas
                        iDetPadre = ClsOperacionDeterminacion.crearCarpetaDeterminacionesAportadas(idTramiteRef, idTramitePorIdDeterminacion(iDet.getIden()));
                        // Reasigna la determinación al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                        if (idTramitePorIdDeterminacion(iDet.getIden())!=idTramitePorIdDeterminacion(iDetPadre.getIden())){
                            s="        Moviendo la determinación [" + iDet.getCodigo() +
                                    "] desde el plan [" + codigoPlanPorIdDeterminacion(iDet.getIden()) +
                                    "] al [" + codigoPlanPorIdDeterminacion(iDetPadre.getIden()) + "]";
                            mLog.info(s);
                            ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0);
                        }
                    }
                }
            }

            // Determinaciones ligadas por EntidadDeterminacionRegimen
            mLog.info("    -> Determinaciones de régimen...");
            lista=new ArrayList();
            s="Select Distinct d.iden From planeamiento.Entidad e, planeamiento.Determinacion d, " +
                    "planeamiento.Entidaddeterminacion ed, planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where e.idtramite=" + idTramiteRef + " And d.idtramite<>" + idTramiteRef + " " +
                    "And ed.identidad=e.iden And ced.identidaddeterminacion=ed.iden " +
                    "And (edr.idcaso=ced.iden Or edr.idcasoaplicacion=ced.iden) " +
                    "And edr.iddeterminacionregimen=d.iden ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                idDeterminacion=Integer.parseInt(it.next().toString());
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDeterminacion);

                // Se excluyen las determinaciones del plan base y las de trámites que no están en la lista de los refundibles
                iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramitePorIdDeterminacion(iDet.getIden()));
                if (iTramite.getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE){
                    if (listaIdTramites.contains(iTramite.getIden())){
                        // Averigua el iden de la carpeta de determinaciones aportadas
                        iDetPadre = ClsOperacionDeterminacion.crearCarpetaDeterminacionesAportadas(idTramiteRef, idTramitePorIdDeterminacion(iDet.getIden()));
                        // Reasigna la determinación al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                        if (idTramitePorIdDeterminacion(iDet.getIden())!=idTramitePorIdDeterminacion(iDetPadre.getIden())){
                            s="        Moviendo la determinación [" + iDet.getCodigo() +
                                    "] desde el plan [" + codigoPlanPorIdDeterminacion(iDet.getIden()) +
                                    "] al [" + codigoPlanPorIdDeterminacion(iDetPadre.getIden()) + "]";
                            mLog.info(s);
                            ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0);
                        }
                    }
                }
            }

            // Determinaciones ligadas por OpcionDeterminacion
            mLog.info("    -> Valores de referencia...");
            lista=new ArrayList();
            s="Select Distinct d.iden From planeamiento.Entidad e, planeamiento.Determinacion d, " +
                    "planeamiento.Entidaddeterminacion ed, planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced, planeamiento.Opciondeterminacion od " +
                    "Where e.idtramite=" + idTramiteRef + " And d.idtramite<>" + idTramiteRef + " " +
                    "And ed.identidad=e.iden And ced.identidaddeterminacion=ed.iden " +
                    "And (edr.idcaso=ced.iden Or edr.idcasoaplicacion=ced.iden) " +
                    "And edr.idopciondeterminacion=od.iden " +
                    "And od.iddeterminacionvalorref=d.iden ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                idDeterminacion=Integer.parseInt(it.next().toString());
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDeterminacion);
                // Se vuelve a hacer la comprobación de idTramite porque el procedimiento para reasignar
                //  una determinaciÃ³n cambia algunas de sus dependencias, y éstas pueden estar inluídas
                //  en la lista de determinaciones seleccionadas.

                // Se excluyen las determinaciones del plan base y las de trámites que no están en la lista de los refundibles
                iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramitePorIdDeterminacion(iDet.getIden()));
                if (iTramite.getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE){
                    if (listaIdTramites.contains(iTramite.getIden())){
                        // Averigua el iden de la carpeta de determinaciones aportadas
                        iDetPadre = ClsOperacionDeterminacion.crearCarpetaDeterminacionesAportadas(idTramiteRef, idTramitePorIdDeterminacion(iDet.getIden()));
                        // Reasigna la determinación al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                        if (idTramitePorIdDeterminacion(iDet.getIden())!=idTramitePorIdDeterminacion(iDetPadre.getIden())){
                            s="        Moviendo la determinación [" + iDet.getCodigo() +
                                    "] desde el plan [" + codigoPlanPorIdDeterminacion(iDet.getIden()) +
                                    "] al [" + codigoPlanPorIdDeterminacion(iDetPadre.getIden()) + "]";
                            mLog.info(s);
                            ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0);
                        }
                    }
                }
            }

            // Determinaciones ligadas por DeterminacionGrupoEntidad
            mLog.info("    -> Grupos de aplicación...");
            lista=new ArrayList();
            s="Select Distinct dge.iden As C1, dge.iddeterminacion As C2, " + 
                "dge.iddeterminaciongrupo As C3 " +
                "From planeamiento.Determinacion dgrupo, planeamiento.Determinacion d, " +
                "planeamiento.Determinaciongrupoentidad dge " +
                "Where d.idtramite=" + idTramiteRef + " And dgrupo.idtramite<>" + idTramiteRef + " " +
                "And dge.iddeterminacion=d.iden And dge.iddeterminaciongrupo=dgrupo.iden ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                obj=(Object[]) it.next();
                idDGE=Integer.parseInt(obj[0].toString());
                idDeterminacion=Integer.parseInt(obj[1].toString());
                idDetGrupo=Integer.parseInt(obj[2].toString());
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDeterminacion);

                // Se excluyen las determinaciones del plan base y las de trámites que no están en la lista de los refundibles
                iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramitePorIdDeterminacion(iDet.getIden()));
                if (iTramite.getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE){
                    if (listaIdTramites.contains(iTramite.getIden())){
                        iDetGrupo=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDetGrupo);

                        // Se comprueba si en el trámite refundido ya existe una determinación de Grupo que
                        //  sea equivalente a esta. Si no existe, se le cambia el idTramite. Si ya existe, se
                        //  comprueba si hay un registro en DeterminacionGrupoEntidad, y si no lo hay, se crea.
                        iDetEquivalente=determinacionEquivalentePorDeterminacion(iDetGrupo, iTramiteRef);
                        if(iDetEquivalente==null){
                            // Averigua el iden de la carpeta de determinaciones aportadas
                            iDetPadre = ClsOperacionDeterminacion.crearCarpetaDeterminacionesAportadas(idTramiteRef, idTramitePorIdDeterminacion(iDet.getIden()));
                            // Reasigna la determinación al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                            if (idTramitePorIdDeterminacion(iDet.getIden())!=idTramitePorIdDeterminacion(iDetPadre.getIden())){
                                s="        Moviendo la determinación [" + iDet.getCodigo() +
                                        "] desde el plan [" + codigoPlanPorIdDeterminacion(iDet.getIden()) +
                                        "] al [" + codigoPlanPorIdDeterminacion(iDetPadre.getIden()) + "]";
                                mLog.info(s);
                                ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetPadre, iDetGrupo, 0);
                                // Recarga los datos
                                iDetGrupo=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDetGrupo);
                                iDetEquivalente=determinacionEquivalentePorDeterminacion(iDetGrupo, iTramiteRef);
                            }
                        }

                        // Comprueba si ya existe el registro en DeterminacionGrupoEntidad
                        lista=new ArrayList();
                        s="Select dge.iden From planeamiento.Determinaciongrupoentidad dge " +
                                "Where dge.iddeterminacion=" + idDeterminacion + " " +
                                "And dge.iddeterminaciongrupo=" + iDetEquivalente.getIden() + " ";
                        lista=ClsConexion.recordList(s);
                        if (lista.size()==0){
                            s="Update planeamiento.Determinaciongrupoentidad " +
                                    "Set iddeterminaciongrupo=" + iDetEquivalente.getIden() + " " +
                                    "Where iden=" + idDGE;
                           nRegistros=ClsConexion.ejecutar(s);
                           s="        La determinación de grupo [" + iDetEquivalente.getCodigo() + "] del " +
                                   "plan [" + codigoPlanPorIdDeterminacion(iDetEquivalente.getIden()) + "] se ha " +
                                   "asignado a la determinación [" + iDet.getCodigo() + "] del " +
                                   "plan [" + codigoPlanPorIdDeterminacion(iDet.getIden()) + "].";
                           mLog.info(s);
                        } else {
                            s="Delete From planeamiento.Determinaciongrupoentidad " +
                                    "Where iden=" + idDGE;
                           nRegistros=ClsConexion.ejecutar(s);
                           s="        Se ha eliminado un registro duplicado en DeterminacionGrupoEntidad " +
                                   "correspondiente a la determinación [" + iDet.getCodigo() + "] del " +
                                   "plan [" + codigoPlanPorIdDeterminacion(iDet.getIden()) + "] y la " +
                                   "determinación de grupo [" + iDetEquivalente.getCodigo() + "] del " +
                                   "plan [" + codigoPlanPorIdDeterminacion(iDetEquivalente.getIden()) + "].";
                           mLog.info(s);
                        }
                    }
                }
            }

            // Determinaciones de un trámite distinto del refundido ligadas por Relacion a determinaciones
            //  o entidades del trámite refundido.
            mLog.info("    -> Determinaciones de trámites diferentes del refundido relacionadas con determinaciones o entidades del refundido...");
            lista=new ArrayList();
            s="Select Distinct detf.iden as idendetf, Cast(trddr.texto As Text), r.iden as idenr " +
                    "From planeamiento.Determinacion detf, planeamiento.Determinacion dett, " +
                    "planeamiento.Vectorrelacion vrt, planeamiento.Vectorrelacion vrf, " +
                    "diccionario.Defvector dvt, diccionario.tabla tbt, diccionario.Defvector dvf, " +
                    "diccionario.tabla tbf, planeamiento.Relacion r, diccionario.Defrelacion dr,  " +
                    "diccionario.traduccion trddr " +
                    "Where Upper(Trim(tbf.nombre))='DETERMINACION' " +
                    "And Upper(Trim(tbt.nombre))='DETERMINACION' " +
                    "And dvf.idtabla=tbf.iden " +
                    "And dvt.idtabla=tbt.iden " +
                    "And vrf.iddefvector=dvf.iden " +
                    "And vrt.iddefvector=dvt.iden " +
                    "And vrf.valor=detf.iden " +
                    "And detf.idtramite<>" + idTramiteRef + " " +
                    "And dett.idtramite=" + idTramiteRef + " " +
                    "And vrt.valor=dett.iden "  +
                    "And dvf.bAsignacion=false " +
                    "And dvt.bAsignacion=true " +
                    "And vrt.idrelacion=r.iden " +
                    "And vrf.idrelacion=r.iden " +
                    "And r.iddefrelacion=dr.iden " +
                    "And dvt.iddefrelacion=dr.iden " +
                    "And dvf.iddefrelacion=dr.iden " +
                    "And trddr.idliteral=dr.idliteral " +
                    "Union " +
                    "Select Distinct detf.iden as idendetf, Cast(trddr.texto As Text), r.iden as idenr " +
                    "From planeamiento.Determinacion detf, planeamiento.Entidad entt, " +
                    "planeamiento.Vectorrelacion vrt, planeamiento.Vectorrelacion vrf, " +
                    "diccionario.Defvector dvt, diccionario.tabla tbt, diccionario.Defvector dvf, " +
                    "diccionario.tabla tbf, planeamiento.Relacion r, diccionario.Defrelacion dr,  " +
                    "diccionario.traduccion trddr " +
                    "Where Upper(Trim(tbf.nombre))='DETERMINACION' " +
                    "And Upper(Trim(tbt.nombre))='ENTIDAD' " +
                    "And dvf.idtabla=tbf.iden " +
                    "And dvt.idtabla=tbt.iden " +
                    "And vrf.iddefvector=dvf.iden " +
                    "And vrt.iddefvector=dvt.iden " +
                    "And vrf.valor=detf.iden " +
                    "And detf.idtramite<>" + idTramiteRef + " " +
                    "And entt.idtramite=" + idTramiteRef + " " +
                    "And vrt.valor=entt.iden " +
                    "And dvf.bAsignacion=false " +
                    "And dvt.bAsignacion=true " +
                    "And vrt.idrelacion=r.iden " +
                    "And vrf.idrelacion=r.iden " +
                    "And r.iddefrelacion=dr.iden " +
                    "And dvt.iddefrelacion=dr.iden " +
                    "And dvf.iddefrelacion=dr.iden " +
                    "And trddr.idliteral=dr.idliteral ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                obj=(Object[]) it.next();
                idDeterminacion=Integer.parseInt(obj[0].toString());
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDeterminacion);
                iTram= (Tramite) ClsConexion.dameEntity(Tramite.class, idTramitePorIdDeterminacion(iDet.getIden()));

                // Se excluyen las determinaciones del plan base y las del propio plan refundido, y las de trámites que no están en la lista de los refundibles
                if (iTram.getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE &&
                        iTram.getIden()!=idTramiteRef){
                    if (listaIdTramites.contains(iTram.getIden())){
                        // Averigua el iden de la carpeta de determinaciones aportadas
                        iDetPadre = ClsOperacionDeterminacion.crearCarpetaDeterminacionesAportadas(idTramiteRef, idTramitePorIdDeterminacion(iDet.getIden()));
                        iTramPadre= (Tramite) ClsConexion.dameEntity(Tramite.class, idTramitePorIdDeterminacion(iDetPadre.getIden()));

                        // Reasigna la determinación al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                        if (iTram.getIden()!=iTramPadre.getIden()){
                            s="        " + obj[1].toString();
                            mLog.info(s);
                            s="            Moviendo la determinación [" + iDet.getCodigo() +
                                    "] desde el plan [" + codigoPlanPorIdDeterminacion(iDet.getIden()) +
                                    "] al [" + codigoPlanPorIdDeterminacion(iDetPadre.getIden()) + "]";
                            mLog.info(s);
                            ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0);

                            // Cambia el idTramite creador de la relación al trámite refundido
                            s="Update planeamiento.relacion Set idtramitecreador=" + idTramiteRef + " Where iden=" + obj[2].toString();
                            nRegistros=ClsConexion.ejecutar(s);
                        }
                    }
                }
            }

            // *****************************************************************

            // Entidades ligadas por Relacion a entidades o determinaciones
            //  del trámite refundido.
            // Se excluyen las entidades del plan base, del refundido, y las de 
            //  trámites que no están en la lista de los refundibles
            mLog.info("    -> Entidades de trámites diferentes del refundido relacionadas con determinaciones o entidades del refundido...");
            lista=new ArrayList();
            s="Select Distinct entf.iden as idenentf, Cast(trddr.texto As Text), r.iden as idenr " +
                    "From planeamiento.Entidad entf, " +
                    "planeamiento.Vectorrelacion vrt, planeamiento.Vectorrelacion vrf, " +
                    "diccionario.Defvector dvt, diccionario.tabla tbt, diccionario.Defvector dvf, " +
                    "diccionario.tabla tbf, planeamiento.Relacion r, diccionario.defrelacion dr, " +
                    "diccionario.traduccion trddr, planeamiento.Determinacion dett " +
                    "Where Upper(Trim(tbf.nombre))='ENTIDAD' " +
                    "And Upper(Trim(tbt.nombre))='DETERMINACION' " +
                    "And dvf.idtabla=tbf.iden " +
                    "And dvt.idtabla=tbt.iden " +
                    "And vrf.iddefvector=dvf.iden " +
                    "And vrt.iddefvector=dvt.iden " +
                    "And vrf.valor=entf.iden " +
                    "And vrt.valor=dett.iden " +
                    "And entf.idtramite<>" + idTramiteRef + " " +
                    "And dett.idtramite=" + idTramiteRef + " " +
                    "And vrt.valor=dett.iden "  +
                    "And dvf.bAsignacion=false " +
                    "And dvt.bAsignacion=true " +
                    "And vrt.idrelacion=r.iden " +
                    "And vrf.idrelacion=r.iden " +
                    "And r.iddefrelacion=dr.iden " +
                    "And dvt.iddefrelacion=dr.iden " +
                    "And dvf.iddefrelacion=dr.iden " +
                    "And trddr.idliteral=dr.idliteral " +
                    "Union " +
                    "Select Distinct entf.iden as idenentf, Cast(trddr.texto As Text), r.iden as idenr " +
                    "From planeamiento.Entidad entf, planeamiento.Entidad entt, " +
                    "planeamiento.Vectorrelacion vrt, planeamiento.Vectorrelacion vrf, " +
                    "diccionario.Defvector dvt, diccionario.tabla tbt, diccionario.Defvector dvf, " +
                    "diccionario.tabla tbf, planeamiento.Relacion r, diccionario.defrelacion dr, " +
                    "diccionario.traduccion trddr " +
                    "Where Upper(Trim(tbf.nombre))='ENTIDAD' " +
                    "And Upper(Trim(tbt.nombre))='ENTIDAD' " +
                    "And dvf.idtabla=tbf.iden " +
                    "And dvt.idtabla=tbt.iden " +
                    "And vrf.iddefvector=dvf.iden " +
                    "And vrt.iddefvector=dvt.iden " +
                    "And vrf.valor=entf.iden " +
                    "And vrt.valor=entt.iden " +
                    "And entf.idtramite<>" + idTramiteRef + " " +
                    "And entt.idtramite=" + idTramiteRef + " " +
                    "And vrt.valor=entt.iden " +
                    "And dvf.bAsignacion=false " +
                    "And dvt.bAsignacion=true " +
                    "And vrt.idrelacion=r.iden " +
                    "And vrf.idrelacion=r.iden " +
                    "And r.iddefrelacion=dr.iden " +
                    "And dvt.iddefrelacion=dr.iden " +
                    "And dvf.iddefrelacion=dr.iden " +
                    "And trddr.idliteral=dr.idliteral ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                obj=(Object[]) it.next();
                idEntidad=Integer.parseInt(obj[0].toString());
                iEnt=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidad);
                iTram= (Tramite) ClsConexion.dameEntity(Tramite.class, idTramitePorIdEntidad(iEnt.getIden()));

                // Se excluyen las entidades del plan base y las del refundido, y las de trámites que no están en la lista de los refundibles
                if (iTram.getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE &&
                        iTram.getIdtipotramite()!= idTramiteRef){
                    if (listaIdTramites.contains(iTram.getIden())){
                        // Averigua el iden de la carpeta de entidades aportadas
                        iEntPadre = ClsOperacionEntidad.crearCarpetaEntidadesAportadas(idTramiteRef, idTramitePorIdEntidad(iEnt.getIden()));
                        iTramPadre= (Tramite) ClsConexion.dameEntity(Tramite.class, idTramitePorIdEntidad(iEntPadre.getIden()));

                        // Reasigna la entidad al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                        if (iTram.getIden()!=iTramPadre.getIden()){
                            s="        " + obj[1].toString();
                            mLog.info(s);
                            s="            Moviendo la entidad [" + iEnt.getCodigo() +
                                    "] desde el plan [" + codigoPlanPorIdEntidad(iEnt.getIden()) +
                                    "] al [" + codigoPlanPorIdEntidad(iEntPadre.getIden()) + "]";
                            mLog.info(s);
                            ClsOperacionEntidad.bReasignarEntidad(iEntPadre, iEntPadre, iEnt, 0);

                            // Cambia el idTramite creador de la relación al trámite refundido
                            s="Update planeamiento.relacion Set idtramitecreador=" + idTramiteRef + " Where iden=" + obj[2].toString();
                            nRegistros=ClsConexion.ejecutar(s);
                        }
                    }
                }
            }

        } catch (Exception e){
            mLog.error("Fallo al reasignar dependencias: "  + e + ". " + s);
        }

    }

    public static int idTramiteRefundido(){
        String s;
        int iden;
        int idPlanRef;
        int idTramiteRef;
        Tramite iTramiteRef;
        List lista;
        Iterator it1;
        Object obj[];

        // Averigua cuál es el plan refundido. Debe ser el único plan que quede con
        //  un tipo de operación ID_TIPOOPERACIONPLAN_NOPROCEDE o ID_TIPOOPERACIONPLAN_SUSTITUCION
        //  y que pertenezca a la lista de trámites que han intervenido en el proceso de refundido.

        try{
            lista=new ArrayList();
            s="Select Distinct p.iden As C1, '[' || Cast(p.codigo As text) || ']' As C1 " + 
                "From planeamiento.Plan p, planeamiento.Tramite t, " +
                "planeamiento.Operacionplan op, diccionario.Instrumentotipooperacionplan itop " +
                "Where op.idplanoperador=p.iden And op.idinstrumentotipooperacion=itop.iden " +
                "And (itop.idtipooperacionplan=" + ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE + " " +
                "Or itop.idtipooperacionplan=" + ClsDatos.ID_TIPOOPERACIONPLAN_SUSTITUCION + ") " +
                "And t.idplan=p.iden And t.iden In (0 ";
            for (int idT: ClsDatos.gListaIdTramitesRefundir){
                s = s + ", " + idT;
            }
            s = s + ") ";
            lista=ClsConexion.recordList(s);
            if (lista.size()!=1){
                it1=lista.iterator();
                s="Error: se han encontrado " + lista.size() + " planes como resultado del refundido. ";
                while (it1.hasNext()){
                    obj=(Object[]) it1.next();
                    s=s + obj[1] + " ";
                }
                mLog.error(s);
                return 0;
            }
            obj=(Object[]) lista.get(0);
            idPlanRef=Integer.parseInt(String.valueOf(obj[0]));

            // Averigua cuál es el trámite del plan
            lista=new ArrayList();
            s="Select iden From planeamiento.Tramite Where idplan=" + idPlanRef + " " +
                    "And iden In (0";
            it1=ClsDatos.gListaIdTramitesRefundir.iterator();
            while (it1.hasNext()){
                iden=Integer.parseInt(it1.next().toString());
                s = s + ", " + iden;
            }
            s= s + ") ";
            lista=ClsConexion.recordList(s);
            idTramiteRef=Integer.parseInt(lista.get(0).toString());
            iTramiteRef=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteRef);
            if (iTramiteRef==null){
                idTramiteRef=0;
            }

        } catch (Exception e){
            mLog.error("Fallo al calcular el identificador del trámite refundido: "  + e + ". Códico 23035.");
            return 0;
        }

        return idTramiteRef;

    }

    public static void refrescarTodosObjetos(){
        
        // OBSOLETO
        
        // Lee todos los registros de todas las tablas, para refrescar las entidades antes de escribir el FIP

        int nTab=32;
        int i;
        int iden;
        List lista;
        Iterator it;
        Class[] tab = new Class[nTab];
        String s;
        Object obj;

        tab[0]=Plan.class;
        tab[1]=Planshp.class;
        tab[2]=Operacionplan.class;
        tab[3]=Planentidadordenacion.class;
        tab[4]=Tramite.class;
        tab[5]=Documento.class;
        tab[6]=Documentoentidad.class;
        tab[7]=Documentodeterminacion.class;
        tab[8]=Documentocaso.class;
        tab[9]=Documentoshp.class;
        tab[10]=Determinacion.class;
        tab[11]=Opciondeterminacion.class;
        tab[12]=Regimenespecifico.class;
        tab[13]=Entidaddeterminacionregimen.class;
        tab[14]=Vinculocaso.class;
        tab[15]=Entidaddeterminacion.class;
        tab[16]=Casoentidaddeterminacion.class;
        tab[17]=Determinaciongrupoentidad.class;
        tab[18]=Entidad.class;
        tab[19]=Entidadpol.class;
        tab[20]=Entidadlin.class;
        tab[21]=Entidadpnt.class;
        tab[22]=Operacion.class;
        tab[23]=Operacionentidad.class;
        tab[24]=Operaciondeterminacion.class;
        tab[25]=Conjuntoentidad.class;
        tab[26]=Ambitoaplicacionambito.class;
        tab[27]=Relacion.class;
        tab[28]=Propiedadrelacion.class;
        tab[29]=Vectorrelacion.class;
        tab[30]=Operacionrelacion.class;
        tab[31]=Boletintramite.class;

        try{
            mLog.info("");
            mLog.info("Actualizando tablas... ");
            gEM.clear();
            System.gc();
            for (i=0; i<nTab; i++){
                mLog.info("    Actualizando " + tab[i].getSimpleName() + "...");
                lista=new ArrayList();
                s="Select iden From planeamiento." + tab[i].getSimpleName();
                lista=ClsConexion.recordList(s);
                it=lista.iterator();
                while(it.hasNext()){
                    iden=Integer.parseInt(it.next().toString());
                    obj=ClsConexion.dameEntity(tab[i], iden);
                    obj=null;
                    gEM.clear();
                }
                obj=null;
            }
            mLog.info("Fin de la actualizacion de tablas. ");
        } catch (Exception e){
            mLog.error("Fallo al actualizar las tablas del esquema 'planeamiento': "  + e + ". Códico 23051.");
        }
    }

    public static boolean bTramiteSuspendido(Tramite iTramite){
        String s;
        List lista;
        boolean resultado=false;

        try{
            lista=new ArrayList();
            s="Select p.bsuspendido From planeamiento.Tramite t, planeamiento.Plan p " +
                    "Where t.iden=" + iTramite.getIden() + " And t.idplan=p.iden ";
            lista=ClsConexion.recordList(s);
            s=lista.get(0).toString().toUpperCase();
            resultado=s.startsWith("T");
            
        } catch (Exception e){
            mLog.error("Fallo al evaluar la suspensión de un plan: "  + e + ". Códico 23057.");
            return false;
        }
        return resultado;
    }

    public static void eliminarCarpetasVacias(int idTramite){
        // 1 - Elimina las determinaciones de carácter 'Enunciado' (1) o 'Enunciado Complementario' (11) que
        //  no tengan hijas, y que no esten vinculadas a ningún documento ni relación.
        // 2 - Elimina las entidades de grupo 'Carpeta' que no tengan hijas, y que no esten vinculadas a
        //  ningún documento ni relación.

        String s;
        List lista;
        List listaEnt;
        Iterator it;
        int iden;
        Tramite iTramite;
        Determinacion iDeterminacion;
        Determinacion iDetCarpeta;
        Entidad iEntidad;

        try{
            mLog.info("");
            if(ClsDatos.B_ELIMINAR_CARPETAS_VACIAS==true){
                mLog.info("Eliminando carpetas vacías...");
            } else {
                mLog.warn("*** Se ha optado por no eliminar las carpetas vacías...");
                return;
            }

            // 1 - Determinaciones
            lista=new ArrayList();
            s="Select d.iden From planeamiento.determinacion d " +
                    "Where d.idtramite=" + idTramite + " " + 
                    "And (d.idcaracter=" + ClsDatos.ID_CARACTER_ENUNCIADO + " " +
                    "Or d.idcaracter=" + ClsDatos.ID_CARACTER_ENUNCIADOCOMPLEMENTARIO + ") " +
                    "And d.iden Not In (Select d1.idpadre From planeamiento.determinacion d1 " +
                    "Where d1.idpadre Is Not Null) " +
                    "And d.iden Not In (Select iddeterminacion From planeamiento.documentodeterminacion) " +
                    "And d.iden Not In (Select vr.valor From planeamiento.vectorrelacion vr, " +
                    "diccionario.defvector dv, diccionario.tabla tb " +
                    "Where Trim(Upper(tb.nombre))='DETERMINACION' And vr.iddefvector=dv.iden " +
                    "And dv.basignacion=true And dv.idtabla=tb.iden) ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                iden=Integer.parseInt(it.next().toString());
                iDeterminacion=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                mLog.info("    Eliminando determinación [" + iDeterminacion.getCodigo() + "] " + iDeterminacion.getNombre());
                ClsMain.eliminarObjeto("determinacion", iden, 0);
            }
            
            // 2 - Entidades
            iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramite);
            iDetCarpeta=determinacionCarpetaTramiteBase(iTramite);
            if (iDetCarpeta!=null){
                listaEnt=new ArrayList();
                listaEnt=idEntidadesPorGrupoTramite(iDetCarpeta, iTramite);
                it=listaEnt.iterator();
                while (it.hasNext()){
                    iden=Integer.parseInt(it.next().toString());
                    lista=new ArrayList();
                    s="Select e.iden From planeamiento.entidad e " +
                            "Where e.iden Not In (Select e1.idpadre From planeamiento.entidad e1 " +
                            "Where e1.idpadre Is Not Null) " +
                            "And e.iden Not In (Select identidad From planeamiento.documentoentidad) " +
                            "And e.iden=" + iden + " " +
                            "And e.iden Not In (Select vr.valor From planeamiento.vectorrelacion vr, " +
                            "diccionario.defvector dv, diccionario.tabla tb " +
                            "Where Trim(Upper(tb.nombre))='ENTIDAD' And vr.iddefvector=dv.iden " +
                            "And dv.basignacion=true And dv.idtabla=tb.iden) ";
                    lista=ClsConexion.recordList(s);
                    if (lista.size()>0){
                        iEntidad=(Entidad) ClsConexion.dameEntity(Entidad.class, iden);
                        mLog.info("    Eliminando entidad [" + iEntidad.getCodigo() + "] " + iEntidad.getNombre());
                        ClsMain.eliminarObjeto("entidad", iden, 0);
                    }
                }
            }

        } catch (Exception e){
            mLog.error("Fallo al eliminar carpetas vacías: "  + e + ". Códico 23046.");
        }
    }

    public static void unificarUnidades(int idTramite){
        // Se hace una lista de todas las unidades, y se cambian
        //  todos los idDeterminacion de las duplicadas a una de ellas.
        // Se eliminan las determinaciones duplicadas.

        String s;
        List lista;
        List listaRepetidas;
        Iterator it;
        Iterator it2;
        int iden;
        int iden2;
        String nombre;
        Tramite iTramite;
        Determinacion iDeterminacion;
        Determinacion iDetCarpeta;
        Entidad iEntidad;
        int numRegistros;

        try{
            mLog.info("");
            mLog.info("Unificando unidades...");

            // Determinaciones Unidad
            lista=new ArrayList();
            s="Select d.iden From planeamiento.determinacion d " +
                    "Where d.idtramite=" + idTramite + " " +
                    "And d.idcaracter=" + ClsDatos.ID_CARACTER_UNIDAD + " ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                iden=Integer.parseInt(it.next().toString());
                iDeterminacion=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                if (iDeterminacion!=null){
                    nombre=iDeterminacion.getNombre();
                    if (nombre!=null){
                        mLog.info("    Unidad: " + nombre);
                        // Lista de determinaciones unidad con el mismo nombre
                        listaRepetidas=new ArrayList();
                        s="Select d.iden From planeamiento.determinacion d " +
                            "Where d.idtramite=" + idTramite + " " +
                            "And d.idcaracter=" + ClsDatos.ID_CARACTER_UNIDAD + " " +
                            "And d.nombre='" + nombre + "' And d.iden<>" + iden + " ";
                        listaRepetidas=ClsConexion.recordList(s);
                        it2=listaRepetidas.iterator();
                        while (it2.hasNext()){
                            mLog.info("        Eliminando unidad duplicada...");
                            // Todos los idDeterminacion ide2 se renumeran a iden
                            iden2=Integer.parseInt(it2.next().toString());

                            s="Update planeamiento.Documentodeterminacion " +
                                    "Set iddeterminacion=" + iden + " " +
                                    "Where iddeterminacion=" + iden2 + " ";
                            numRegistros=ClsConexion.ejecutar(s);
                            s="Update planeamiento.Opciondeterminacion " +
                                    "Set iddeterminacion=" + iden + " " +
                                    "Where iddeterminacion=" + iden2 + " ";
                            numRegistros=ClsConexion.ejecutar(s);
                            s="Update planeamiento.Opciondeterminacion " +
                                    "Set iddeterminacionValorRef=" + iden + " " +
                                    "Where iddeterminacionValorRef=" + iden2 + " ";
                            numRegistros=ClsConexion.ejecutar(s);
                            s="Update planeamiento.Entidaddeterminacionregimen " +
                                    "Set iddeterminacionRegimen=" + iden + " " +
                                    "Where iddeterminacionRegimen=" + iden2 + " ";
                            numRegistros=ClsConexion.ejecutar(s);
                            s="Update planeamiento.Entidaddeterminacion " +
                                    "Set iddeterminacion=" + iden + " " +
                                    "Where iddeterminacion=" + iden2 + " ";
                            numRegistros=ClsConexion.ejecutar(s);
                            s="Update planeamiento.Determinaciongrupoentidad " +
                                    "Set iddeterminacion=" + iden + " " +
                                    "Where iddeterminacion=" + iden2 + " ";
                            numRegistros=ClsConexion.ejecutar(s);
                            s="Update planeamiento.Determinaciongrupoentidad " +
                                    "Set iddeterminacionGrupo=" + iden + " " +
                                    "Where iddeterminacionGrupo=" + iden2 + " ";
                            numRegistros=ClsConexion.ejecutar(s);
                            s="Update planeamiento.OperacionDeterminacion " +
                                    "Set iddeterminacion=" + iden + " " +
                                    "Where iddeterminacion=" + iden2 + " ";
                            numRegistros=ClsConexion.ejecutar(s);
                            s="Update planeamiento.OperacionDeterminacion " +
                                    "Set iddeterminacionoperadora=" + iden + " " +
                                    "Where iddeterminacionoperadora=" + iden2 + " ";
                            numRegistros=ClsConexion.ejecutar(s);
                            // Elimina la determinación duplicada
                            ClsMain.eliminarObjeto("determinacion", iden2, idTramite);
                        }
                    }
                }
            }

        } catch (Exception e){
            mLog.error("Fallo al eliminar carpetas vacías: "  + e + ". Códico 23046.");
        }
    }

    public static int idDeterminacionBasePorIdDeterminacion(int idDet){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s = "Select d.iddeterminacionbase From planeamiento.determinacion d " +
                    "Where d.iden=" + idDet + " ";
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            mLog.error("Error al obtener determinación base. "  + e + ". Código 23068.");
            return 0;
        }
    }

    public static String codigoDeterminacionBasePorIdDeterminacion(int idDet){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select Cast(db.codigo As text) From planeamiento.determinacion d, planeamiento.determinacion db " +
                    "Where d.iddeterminacionbase=db.iden And d.iden=" + idDet;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return "";
            }
            if (lista.size()==0){
                return "";
            }
            obj=lista.get(0);
            if (obj==null){
                return "";
            }
            return lista.get(0).toString();
        } catch (Exception e) {
            mLog.error("Error al obtener código determinación base. "  + e + ". Código 23069.");
            return "";
        }
    }

    public static int idTramitePorIdOperacion(int idOperacion){
        String s;
        List lista=new ArrayList();
        Object obj;
        
        try {
            s="Select o.idtramiteordenante From planeamiento.operacion o " +
                    "Where o.iden=" + idOperacion;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener el trámite. "  + e + ". Código 23070.");
            return 0;
        }
    }

    public static int idTramitePorIdDeterminacion(int idDet){
        String s;
        Determinacion iDet;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select d.idtramite From planeamiento.determinacion d " +
                    "Where d.iden=" + idDet;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
            
        } catch (Exception e) {
            mLog.error("Error al obtener el trámite. "  + e + ". Código 23071.");
            return 0;
        }
    }

    public static int idTramitePorIdEntidad(int idEnt){
        String s;
        List lista=new ArrayList();
        Object obj;
        
        try {
            s="Select e.idtramite From planeamiento.entidad e " +
                "Where e.iden=" + idEnt;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
            
        } catch (Exception e) {
            mLog.error("Error al obtener el trámite. "  + e + ". Código 23072.");
            return 0;
        }
    }

    public static String codigoPlanPorIdTramite(int idTramite){
        String s;
        List lista=new ArrayList();
        
        try {
            s="Select Cast(p.codigo As text) From planeamiento.tramite t, planeamiento.plan p " +
                "Where t.iden=" + idTramite + " " +
                "And t.idplan=p.iden ";
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return "";
            }
            if (lista.size()==0){
                return "";
            }
            return lista.get(0).toString();
        } catch (Exception e) {
            mLog.error("Error al obtener el código del plan. "  + e + ". Código 23073.");
            return "";
        }
    }
    
    public static String codigoPlanPorIdDeterminacion(int idDet){
        String s;
        List lista=new ArrayList();
        
        try {
            s="Select Cast(p.codigo As text) From planeamiento.determinacion d, planeamiento.tramite t, planeamiento.plan p " +
                "Where d.iden=" + idDet + " " +
                "And d.idtramite=t.iden And t.idplan=p.iden ";
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return "";
            }
            if (lista.size()==0){
                return "";
            }
            return lista.get(0).toString();
        } catch (Exception e) {
            mLog.error("Error al obtener el código del plan. "  + e + ". Código 23074.");
            return "";
        }
    }

    public static String codigoPlanPorIdEntidad(int idEnt){
        String s;
        List lista=new ArrayList();
        
        try {
            s="Select Cast(p.codigo As text) From planeamiento.entidad e, " + 
                "planeamiento.tramite t, planeamiento.plan p " +
                "Where e.iden=" + idEnt + " " +
                "And e.idtramite=t.iden And t.idplan=p.iden ";
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return "";
            }
            if (lista.size()==0){
                return "";
            }
            return lista.get(0).toString();
        } catch (Exception e) {
            mLog.error("Error al obtener el código del plan. "  + e + ". Código 23074.");
            return "";
        }
    }

    public static int idAmbitoPorIdTramite(int idTramite){
        String s;
        List lista=new ArrayList();
        Object obj;
                
        try {
            s="Select p.idAmbito From planeamiento.plan p, planeamiento.tramite t " +
                "Where t.iden=" + idTramite + " And t.idplan=p.iden ";
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener el ámbito. "  + e + ". Código 23075.");
            return 0;
        }
    }

    public static int idPlanPorIdTramite(int idTramite){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select t.idPlan From planeamiento.tramite t " +
                "Where t.iden=" + idTramite + " ";
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
            
        } catch (Exception e) {
            mLog.error("Error al obtener el plan. "  + e + ". Código 23076.");
            return 0;
        }
    }

    public static String nombrePlanPorIdTramite(int idTramite){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select Cast(p.nombre As text) From planeamiento.plan p, planeamiento.tramite t " +
                "Where t.iden=" + idTramite + " And t.idplan=p.iden ";
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return "";
            }
            if (lista.size()==0){
                return "";
            }
            obj=lista.get(0);
            if (obj==null){
                return "";
            }
            return lista.get(0).toString();
        } catch (Exception e) {
            mLog.error("Error al obtener el nombre del plan. "  + e + ". Código 23077.");
            return "";
        }
    }

    public static int idDeterminacionPorIdOperacionDeterminacion(int idOpDet){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select opd.iddeterminacion From planeamiento.operaciondeterminacion opd " +
                "Where opd.iden=" + idOpDet;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());

        } catch (Exception e) {
            mLog.error("Error al obtener la determinación. "  + e + ". Código 23078.");
            return 0;
        }
    }

    public static int idDeterminacionOperadoraPorIdOperacionDeterminacion(int idOpDet){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select opd.iddeterminacionoperadora From planeamiento.operaciondeterminacion opd " +
                "Where opd.iden=" + idOpDet;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener la determinación. "  + e + ". Código 23079.");
            return 0;
        }
    }

    public static int idPadrePorIdDeterminacion(int idDet){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select d.idPadre From planeamiento.determinacion d " +
                "Where d.iden=" + idDet;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            mLog.error("Error al obtener el padre de la determinación. "  + e + ". Código 23080.");
            return 0;
        }
    }

    public static int idPadrePorIdEntidad(int idEnt){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select e.idPadre From planeamiento.entidad e " +
                    "Where e.iden=" + idEnt;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(obj.toString());
            
        } catch (Exception e) {
            mLog.error("Error en la lectura del idPadre de la entidad. "  + e + ". Código 23064.");
            return 0;
        }
    }

    public static int idPadrePorIdPlan(int idPlan){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select p.idPadre From planeamiento.plan p " +
                    "Where p.iden=" + idPlan + " ";
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            mLog.error("Error al obtener el padre del plan. "  + e + ". Código 23081.");
            return 0;
        }
    }

    public static int idEntidadPorIdOperacionEntidad(int idOpEnt){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select ope.identidad From planeamiento.operacionentidad ope " +
                "Where ope.iden=" + idOpEnt;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());

        } catch (Exception e) {
            mLog.error("Error al obtener la entidad. "  + e + ". Código 23082.");
            return 0;
        }
    }

    public static int idEntidadOperadoraPorIdOperacionEntidad(int idOpEnt){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select ope.identidadoperadora From planeamiento.operacionentidad ope " +
                "Where ope.iden=" + idOpEnt;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener la entidad. "  + e + ". Código 23083.");
            return 0;
        }
    }

    public static int idOperacionPorIdOperacionEntidad(int idOpEnt){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select ope.idoperacion From planeamiento.operacionentidad ope " +
                "Where ope.iden=" + idOpEnt;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener la operación. "  + e + ". Código 23083.");
            return 0;
        }
    }

    public static int idOperacionPorIdOperacionDeterminacion(int idOpDet){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select opd.idoperacion From planeamiento.operaciondeterminacion opd " +
                "Where opd.iden=" + idOpDet;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener la operación. "  + e + ". Código 23084.");
            return 0;
        }
    }

    public static int idOperacionPorIdOperacionRelacion(int idOpRel){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select opr.idoperacion From planeamiento.operacionrelacion opr " +
                "Where opr.iden=" + idOpRel;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(ClsConexion.recordList(s).get(0).toString());
            
        } catch (Exception e) {
            mLog.error("Error al obtener la operación. "  + e + ". Código 23085.");
            return 0;
        }
    }

    public static int idRelacionPorIdOperacionRelacion(int idOpRel){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select opr.idrelacion From planeamiento.operacionrelacion opr " +
                "Where opr.iden=" + idOpRel;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener la relación. "  + e + ". Código 23086.");
            return 0;
        }
    }

    public static int idRelacionPorIdVectorRelacion(int idVectorRel){
        String s;
        List lista=new ArrayList();
        Object obj;
        
        try {
            s="Select vr.idrelacion From planeamiento.vectorrelacion vr " +
                "Where vr.iden=" + idVectorRel;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener la relación. "  + e + ". Código 23087.");
            return 0;
        }
    }

    public static int idTramiteCreadorPorIdRelacion(int idRel){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select r.idtramitecreador From planeamiento.relacion r " +
                 "Where r.iden=" + idRel;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
            
        } catch (Exception e) {
            mLog.error("Error al obtener el trámite. "  + e + ". Código 23088.");
            return 0;
        }
    }

    public static int ordenPlanPorIden(int iden){
        String s;
        List lista=new ArrayList();
        Object obj;
        
        try {
            s="Select p.orden From planeamiento.plan p " +
                    "Where p.iden=" + iden;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener el orden del plan iden=" + iden + ". " + e + ". Código 23090.");
            return 0;
        }
    }

    public static int ordenEntidadPorIden(int iden){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select e.orden From planeamiento.entidad e " +
                    "Where e.iden=" + iden;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener el orden de la entidad iden=" + iden + ". " + e + ". Código 23091.");
            return 0;
        }
    }

    public static int ordenDeterminacionPorIden(int iden){
        String s;
        List lista=new ArrayList();
        Object obj;

        try {
            s="Select d.orden From planeamiento.determinacion d " +
                    "Where d.iden=" + iden;
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return 0;
            }
            if (lista.size()==0){
                return 0;
            }
            obj=lista.get(0);
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(lista.get(0).toString());
        } catch (Exception e) {
            mLog.error("Error al obtener el orden de la determinacion iden=" + iden + ". " + e + ". Código 23092.");
            return 0;
        }
    }

    public static boolean bTramiteEsDesarrollo(int  idTramite) {
        String s;
        List lista=new ArrayList();

        try{
            // Averigua si el trámite pertenece a un plan que opere con una operación de desarrollo
            s="Select p.iden From planeamiento.plan p, planeamiento.tramite t, planeamiento.operacionplan op, " +
                "diccionario.instrumentotipooperacionplan itop " +
                "Where p.iden=t.idplan And t.iden=" + idTramite + " And op.idplanoperador=p.iden " +
                "And op.idinstrumentotipooperacion=itop.iden " +
                "And itop.idtipooperacionplan=" + ClsDatos.ID_TIPOOPERACIONPLAN_DESARROLLO;
            lista=ClsConexion.recordList(s);
            if (lista.size()>0){
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            mLog.error("Error: imposible evaluar si el trámite iden=" + idTramite + " es un desarrollo. " + e + ". Código 23093.");
            return false;
        }

    }

    public static void actualizarComentarioTramite(int idTramite){
        String s;
        List lista=new ArrayList();
        Iterator it;
        Object obj[];
        int i;
     
        try {
            // Actualiza el campo 'Comentario' de Tramite con información de los
            //  planes que han intervenido en el proceso de refundido.
            s="Update planeamiento.Tramite Set comentario='' " +
                "Where iden=" + idTramite + "And comentario Is Null ";
            i=ClsConexion.ejecutar(s);
            if (i==1) {
                s="Update planeamiento.Tramite " + 
                    "Set comentario='Plan refundido: [" +
                    ClsDatos.gCodigoPlanRefundido + "]' Where iden=" + idTramite;
                ClsConexion.ejecutar(s);
            } else {
                s="Update planeamiento.Tramite " + 
                    "Set comentario=comentario || '\nPlan refundido: [" +
                    ClsDatos.gCodigoPlanRefundido + "]' Where iden=" + idTramite;
                ClsConexion.ejecutar(s);
            }
            
            //  --> Lista de planes y trámites que han intervenido
            s="Select Distinct Cast(P.codigo As Text) As C1, T.iteracion As C2, " + 
                "T.fecha As C3, TRD.texto As C4, P.orden As C5 " +
                "From planeamiento.Plan P, planeamiento.Tramite T, " +
                "diccionario.TipoTramite TT, diccionario.Traduccion TRD " +
                "Where T.idPlan=P.iden And T.idTipoTramite=TT.iden " +
                "And TT.idLiteral=TRD.idLiteral " + 
                "And Upper(Trim(TRD.idioma))='" + ClsDatos.TEXTO_IDIOMA.toUpperCase() + "' " +
                "And T.iden=Any(Array[0";
            for (int idT: ClsDatos.gListaIdTramitesRefundir){
                s = s + ", " + idT;
            }
            s = s +"]) Order By P.orden ";
            lista=ClsConexion.recordList(s);
            if (lista.size()>0){
                s="Update planeamiento.Tramite " + 
                    "Set comentario=comentario || '\nPlanes y trámites refundidos:' ";
                ClsConexion.ejecutar(s);
                it=lista.iterator();
                while (it.hasNext()){
                    obj=(Object[]) it.next();
                    s="Update planeamiento.Tramite Set comentario=comentario || '\n[" +
                        obj[0].toString() + "] it=" + obj[1].toString() + " fecha=" +
                        obj[2].toString() + " tipo=" + obj[3].toString() + "' " +
                        "Where iden=" + idTramite + " ";
                    ClsConexion.ejecutar(s);
                }
            } 
        } catch (Exception e) {
            mLog.warn("Aviso: no se ha conseguido actualizar el campo 'Comentario' " + 
                "del trámite con la información de los planes que se han refundido.");
        }
    }
    
    public static void reubicarCarpetasAportadas(int idTramiteRef)    {
        // Al final del proceso de refundido, se han creado una serie de 
        //  determinaciones y entidades que contienen las aportaciones de otros 
        //  planes. Estas carpetas están en la raiz de los árboles de entidades
        //  y determinaciones, y este procedimiento sirve para reubicarlas
        //  dentro de una única carpeta de "Aportadas" (se entiende que, una para 
        //  las determinaciones, y otra para las entidades).
        
        String s;
        String aptdo;
        String codigo;
        int i;
        int idDet;
        int idEnt;
        List lista;
        Iterator it;
        Tramite iTramiteRef;
        Determinacion iDetGrupoDeEntidades;
        Determinacion iDetCarpeta;
        Entidad iEntidad;
        Opciondeterminacion iOpcion;
        int idOpcion;
        int idEntDet;
        int idCaso;
        int idTramiteBase;
        Tramite iTramiteBase;
        
        iTramiteRef=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteRef);
        
        try{
            // Crea la determinación "Aportadas", de la que van a colgar todas las
            //  determinaciones "Aportadas por..." que se han creado durante el
            //  proceso de refundido.
            aptdo="Aportadas";
            codigo=maximoCodigoDeterminacion(idTramiteRef,1);
            s="Insert Into planeamiento.Determinacion (idtramite, idpadre, idcaracter, " +
                "apartado, nombre, codigo, bsuspendida, orden) " +
                "Values (" + idTramiteRef + ", Null, " +
                ClsDatos.ID_CARACTER_ENUNCIADO + ", '" + aptdo + "', " +
                "'Aportadas por refundido', '" + codigo +"', false, " + 
                "(Select 1+Max(orden) From planeamiento.Determinacion " + 
                "Where idTramite=" + idTramiteRef +" And idPadre Is Null)) ";
            i=ClsConexion.ejecutar(s);
            if (i==1){
                idDet=ultimoIden("planeamiento.Determinacion");
                // Actualiza el idPadre de las determinaciones aportadas que están
                //  en la raiz, para que cuelguen de la determinación recién creada.
                s="Update planeamiento.determinacion Set idPadre=" + idDet + " " +
                    "Where idPadre Is Null " + 
                    "And idcaracter=" + ClsDatos.ID_CARACTER_ENUNCIADO + " " +
                    "And nombre Like '" + ClsDatos.TEXTO_APORTADAS + "%' " +
                    "And iden<>" + idDet + " And idTramite=" + idTramiteRef + " ";
                i=ClsConexion.ejecutar(s);
                if (i>0){
                    // Actualiza el apartado y el orden de dichas determinaciones
                    s="Select D.iden From planeamiento.determinacion D " +
                        "Where D.idPadre=" + idDet + " Order By D.nombre ";
                    lista=ClsConexion.recordList(s);
                    it=lista.iterator();
                    i=1;
                    while (it.hasNext()){
                        idDet=Integer.parseInt(it.next().toString());
                        s="Update planeamiento.determinacion " + 
                            "Set apartado='" + i + "-' Where iden=" + idDet;
                        ClsConexion.ejecutar(s);
                        s="Update planeamiento.determinacion " + 
                            "Set orden=" + i + " Where iden=" + idDet;
                        ClsConexion.ejecutar(s);
                        i=i+1;
                    }

                } else {
                    s="    No se han podido reubicar las determinacioenes " + 
                            "'" + ClsDatos.TEXTO_APORTADAS + "...";
                    mLog.warn(s);
                }

            } else {
                mLog.warn("    No se ha podido crear la determinación 'Aportadas por refundido'.");
            }

            // Crea la entidad "Aportadas", de la que van a colgar todas las
            //  entidades "Aportadas por..." que se han creado durante el
            //  proceso de refundido.
            codigo=maximoCodigoEntidad(idTramiteRef,1);
            s = "Insert Into planeamiento.Entidad (idtramite, idpadre, clave, " +
                "nombre, codigo, bsuspendida) Values (" + idTramiteRef + ", " +
                "null, 'Aportadas', 'Aportadas por refundido', '" + codigo + "', false) ";
            i=ClsConexion.ejecutar(s);

            if (i==1){
                idEnt = ClsMain.ultimoIden("planeamiento.Entidad");
                iEntidad=(Entidad) ClsConexion.dameEntity(Entidad.class, idEnt);

                // Añadir su valor para la determinación 'Grupo de entidades' (es 
                //  la determinación 'Carpeta', lo que anteriormente era el grupo 'Grupo')
                it=ClsDatos.gListaIdTramitesBase.iterator();
                iDetGrupoDeEntidades=null;
                iDetCarpeta=null;
                while (it.hasNext()){
                    idTramiteBase=Integer.parseInt(it.next().toString());
                    iTramiteBase=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteBase);
                    iDetGrupoDeEntidades=ClsMain.determinacionGrupoDeEntidadesPorTramite(iTramiteBase);
                    if (iDetGrupoDeEntidades!=null){
                        iDetCarpeta=ClsMain.determinacionCarpetaTramiteBase(iTramiteBase);
                        break;
                    }
                }
                
                iOpcion=ClsMain.opcionPorDeterminaciones(iDetGrupoDeEntidades, iDetCarpeta);
                if (iOpcion==null){
                    // No se encuentra la opcion para la pareja de determinaciones 
                    //  del plan base. No es posible reubicar las entidades, y hay
                    //  que eliminar la entidad recien creada.
                    ClsMain.eliminarObjeto("Entidad", idEnt, idTramiteRef);
                    s="    No se han podido reubicar las entidades " + 
                            "'" + ClsDatos.TEXTO_APORTADAS + "...";
                    mLog.warn(s);
                    return;
                }
                idOpcion=iOpcion.getIden();

                // Inserta el valor en las tablas de valores
                s="Insert Into planeamiento.Entidaddeterminacion " +
                    "(identidad, iddeterminacion) " +
                    "Values (" + idEnt + ", " + iDetGrupoDeEntidades.getIden() + ") ";
                i = ClsConexion.ejecutar(s);
                idEntDet = ClsMain.ultimoIden("planeamiento.Entidaddeterminacion");
                s="Insert Into planeamiento.Casoentidaddeterminacion (identidaddeterminacion) " +
                    "Values (" + idEntDet + ") ";
                i = ClsConexion.ejecutar(s);
                idCaso = ClsMain.ultimoIden("planeamiento.Casoentidaddeterminacion");
                s="Insert Into planeamiento.Entidaddeterminacionregimen (idcaso, idopciondeterminacion, " +
                    "superposicion) Values (" + idCaso + ", " + idOpcion + ", 0) ";
                i = ClsConexion.ejecutar(s);

                // Actualiza el idPadre de las entidades aportadas que están
                //  en la raiz, para que cuelguen de la entidad recién creada.
                s="Update planeamiento.entidad Set idPadre=" + idEnt + " " +
                    "Where idPadre Is Null " + 
                    "And nombre Like '" + ClsDatos.TEXTO_APORTADAS + "%' " +
                    "And iden<>" + idEnt + " And idTramite=" + idTramiteRef + " ";
                i=ClsConexion.ejecutar(s);
                if (i>0){
                    // Actualiza el apartado y el orden de dichas entidades
                    s="Select E.iden From planeamiento.entidad E " +
                        "Where E.idPadre=" + idEnt + " Order By E.nombre ";
                    lista=ClsConexion.recordList(s);
                    it=lista.iterator();
                    i=1;
                    while (it.hasNext()){
                        idEnt=Integer.parseInt(it.next().toString());
                        s="Update planeamiento.entidad " + 
                            "Set orden=" + i + " Where iden=" + idEnt;
                        ClsConexion.ejecutar(s);
                        i=i+1;
                    }

                } else {
                    s="    No se han podido reubicar las entidades " + 
                            "'" + ClsDatos.TEXTO_APORTADAS + "...";
                    mLog.warn(s);
                }
            } else {
                mLog.warn("    No se ha podido crear la entidad 'Aportadas por refundido'.");
            }
        } catch (Exception e) {
            mLog.warn("Aviso: no se han conseguido agrupar las determinaciones y " + 
                "entidades en carpetás únicas. " + e.toString());
        } 
    }
    
    public static Tramite tramiteBasePorTramite(Tramite iTramite){
        // Devuelve el trámite base de un trámite
        String s;
        List lista;
        int idTB;
        Tramite iTramiteBase;
 
        try {
            s="Select TB.iden From planeamiento.Tramite T, planeamiento.Tramite TB, " +
                "planeamiento.Plan P, planeamiento.Plan PB " + 
                "Where T.iden=" + iTramite.getIden() + " And T.idPlan=P.iden " +
                "And P.idPlanBase=PB.iden And TB.idPlan=PB.iden ";
            lista=ClsConexion.recordList(s);
            if (lista==null){
                return null;
            }
            if (lista.size()==0){
                return null;
            }
            idTB=Integer.parseInt(lista.get(0).toString());
            iTramiteBase=(Tramite) ClsConexion.dameEntity(Tramite.class, idTB);
            return iTramiteBase;
        } catch (Exception e) {
            mLog.error("Error: no se ha conseguido averiguar el trámite base del " + 
                "plan [" + ClsMain.codigoPlanPorIdTramite(iTramite.getIden()) + "]. " + e.toString());
            return null;
        }
    }
    
    public static Geometry geometria(String tabla, int idEntidad, boolean suspendida){
        String s;
        List lista;
        String geomWKT;
        WKTReader reader;
        Geometry geomR=null;
        Entidad iEnt;
        
        // Agrupa las geometrías
        iEnt=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidad);
        agruparGeometrias(iEnt);
        
        s="Select astext(geom) From planeamiento." + tabla + " " + 
                "Where identidad=" + idEntidad + " And bsuspendida=" + suspendida;
            lista = ClsConexion.recordList(s);
            if (lista.size()>0){            
                geomWKT=lista.get(0).toString();
                try {
                    reader = new WKTReader();
                    geomR = reader.read(geomWKT);
                    if (geomWKT.toUpperCase().contains("POLYGON")==true){
                    }
                } catch (ParseException e) {
                    mLog.error("Error al leer la geometría de la entidad iden=" + idEntidad + ". " + e + ". Código 23127.");
                    return null;
                }
            }
        return geomR;        
    }

    public static void agruparGeometrias(Entidad iEnt){
        // Este método se encarga de unir las diferentes geometrías de una entidad en una o dos geometrías
        //  múltiples, dependiendo de si todas tienen el mismo valor del campo "bSuspendida", o bien existen
        //  geometrías con valor "false" y otras con valor "true".

        String s="";
        String valor[]= new String [2];
        String tipoGeom[]= new String [3];
        int i;
        int j;
        List lista;
        int iden1;
        int idenN;
        int nRegistros;
        Iterator it;
        Geometry geom1;
        Geometry geomN;
        int srid;

        try {
            
            if (iEnt==null){
                return;
            }

            valor[0]="TRUE";
            valor[1]="FALSE";
            tipoGeom[0]="Entidadlin";
            tipoGeom[1]="Entidadpnt";
            tipoGeom[2]="Entidadpol";

            // Para cada valor (true o false)...
            for (i=0;i<=1;i++){
                // Para cada tipo de geometría...
                for (j=0;j<=2;j++){
                    // Selecciona la primera geometría, que es sobre la cual se van a efectuar todas las uniones
                    lista=new ArrayList();
                    s="Select iden From planeamiento." + tipoGeom[j] + " " +
                            "Where identidad=" + iEnt.getIden() + " " +
                            "And bSuspendida=" + valor[i] + " Order By iden ";
                    lista=ClsConexion.recordList(s);
                    if (lista.size()>0){
                        iden1=Integer.parseInt(String.valueOf(lista.get(0)));
                        geom1=ClsMain.geometria(tipoGeom[j], iden1, valor[i].equalsIgnoreCase("TRUE"));

                        // Selecciona el resto de geometrías del mismo tipo y con el mismo valor de bSuspendida
                        s="Select iden From planeamiento." + tipoGeom[j] + " " +
                            "Where identidad=" + iEnt.getIden() + " " +
                            "And bSuspendida=" + valor[i] + " " +
                            "And iden<>" + iden1 + " Order By iden ";
                        lista=ClsConexion.recordList(s);
                        it=lista.iterator();
                        while (it.hasNext()){
                            idenN=Integer.parseInt(String.valueOf(it.next()));
                            geomN=ClsMain.geometria(tipoGeom[j], idenN, valor[i].equalsIgnoreCase("TRUE"));
                            srid=ClsMain.srId(tipoGeom[j], idenN);
                            // Efectúa la unión con la geometría iden1
                            geom1=ClsMain.operarGeometrias(geom1, geomN, 1);
                            geom1=ClsMain.limpiarGeometria(tipoGeom[j], geom1);
                            s="Update planeamiento." + tipoGeom[j] + " " + 
                                "Set geom=multi(geometryfromtext('" +
                                geom1 + "', " + srid + ")) " + 
                                "Where identidad=" + iden1 + " " + 
                                "And bsuspendida=" + valor[i];
                            nRegistros=ClsConexion.ejecutar(s);

                            // Si se ha conseguido hacer la unión, se borra el registro que se ha unido
                            if (nRegistros==1){
                                s = "Delete From planeamiento." + tipoGeom[j] + " " +
                                    "Where iden=" + idenN +  " ";
                                nRegistros=ClsConexion.ejecutar(s);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            mLog.error("Error: fallo en la agrupación de geometrías. " + e + ". Código 23129." );
        }
    }

    public static Geometry limpiarGeometria(String tipoGeom, Geometry aGeom){
        int numGeom=0;
        int i;
        Geometry iGeom;
        Geometry iGeomR=null;
        String sTipo="";
        
        try {
            if (aGeom==null){
                return null;
            }
            if (!aGeom.getGeometryType().toUpperCase().contains("COLLECTION")){
                return aGeom;
            }
            numGeom=aGeom.getNumGeometries();
            
            if (tipoGeom.equalsIgnoreCase("ENTIDADPOL")){
                sTipo="POLYGON";
            }
            if (tipoGeom.equalsIgnoreCase("ENTIDADLIN")){
                sTipo="LINE";
            }
            if (tipoGeom.equalsIgnoreCase("ENTIDADPNT")){
                sTipo="POINT";
            }
            
            for (i=0;i<numGeom;i++){
                iGeom=aGeom.getGeometryN(i);
                if (iGeom.getGeometryType().toUpperCase().contains(sTipo)){
                    if (iGeomR==null){
                        iGeomR=iGeom;
                    }
                    iGeomR=ClsMain.operarGeometrias(iGeomR, iGeom, 1);
                }
            }
            return iGeomR;
            
        }catch (Exception e){
            mLog.error("Error: fallo al limpiar la geometría de tipo " + sTipo + ". " + e + ". Código 23129." );
        }
        
        return null;
    }
    
    public static int srId(String tabla, int idEntidad){
        String s;
        List lista;
        int srid=-1;
        
        try{
            lista=new ArrayList();
            s="Select Distinct srid(geom) From planeamiento." + tabla + " Where identidad=" + idEntidad;
            lista = ClsConexion.recordList(s);
            if (lista!=null){
                if (lista.size()==1){
                    srid=Integer.parseInt(lista.get(0).toString());                    
                }
            }
        } catch (Exception e){
            mLog.error("Error: fallo al evaluar el SRID de la entidad iden=" + idEntidad + ". " + e );
        }
        return srid;
    }
    
    public static Geometry operarGeometrias(Geometry geomO, Geometry geomR, int tipoOperacion){
        boolean fallo=true;
        double tol=0.000001;
        Geometry geomResult=null;
        int nIntentos=0;
        
        // 1=Unión
        // 2=Intersección
        // 3=Diferencia
        
        try{
                while (fallo==true){
                    try{
                        switch (tipoOperacion){
                            case 1:
                                // Unión
                                geomResult=geomO.union(geomR);
                                break;
                            case 2:
                                // Intersección
                                geomResult=geomO.intersection(geomR);
                                break;
                            case 3:
                                // Diferencia
                                geomResult=geomO.difference(geomR);
                                break;
                        }
                        fallo=false;
                    } catch (Exception e){
                        // La operación da error y hay que repetirla con tolerancia.
                        nIntentos++;
                        fallo=true;
                        mLog.warn("            " + nIntentos + "-La operación de geometrías genera un error. Se reintenta con tolerancia " + tol  + ". ");
                        geomO=geomO.buffer(tol);
                        geomR=geomR.buffer(tol);
                        tol=tol*2;
                        geomResult=null;
                        if (nIntentos>=10){
                            mLog.warn("            No se ha conseguido efectuar la operación en " + nIntentos + " intentos. El resultado es una geometría nula.");
                            fallo=false;
                        }
                    }
                }
                if (nIntentos>0){
                    nIntentos++;
                    mLog.warn("            " + nIntentos + "-Se ha conseguido efectuar la operación de geometrías. ");
                }
        } catch (Exception e){
            geomResult=null;
            mLog.error("Error: fallo al operar geometrías. " + e );
        }
        return geomResult;    
    }
}
