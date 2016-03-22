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

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;

/**
 *
 * @author Miguel.Martin
 */
public class ClsOperacionRelacion {
    public static EntityManager gEM;
    private static final Logger mLog = Logger.getLogger(ClsOperacionRelacion.class);

    public static void operacionesRelaciones(Operacionrelacion iOperacionRelacion){
        int idTipoOperacion;
        Relacion iRelO;
        String s;
        int idTramiteOrdenante;
        Plan iPlan;
        String codPlan;
        int idOperacion;
        int idRelacion;
        
        idTipoOperacion=(int) iOperacionRelacion.getIdtipooperacionrel();
        idRelacion=ClsMain.idRelacionPorIdOperacionRelacion(iOperacionRelacion.getIden());
        iRelO=(Relacion) ClsConexion.dameEntity(Relacion.class, idRelacion);
        
        idOperacion=ClsMain.idOperacionPorIdOperacionRelacion(iOperacionRelacion.getIden());
        idTramiteOrdenante=ClsMain.idTramitePorIdOperacion(idOperacion);

        s="(" + String.valueOf(idOperacion) + ") ";

        iPlan=(Plan) ClsConexion.dameEntity(Plan.class, ClsMain.idPlanPorIdTramite(idTramiteOrdenante));
        codPlan=iPlan.getCodigo();

        mLog.info("         Relación operada: iden=" + iRelO.getIden());
        mLog.info("         Tipo de operación: " + idTipoOperacion);

        // Se elimina la operación antes de ejecutarla, para evitar el "aviso" de que se ha eliminado
        //  una operación que aún no ha sido ejecutada.
        ClsMain.eliminarOperacion(idOperacion);

        switch (idTipoOperacion) {
            case 1:;
                // Eliminación
                mLog.info(s + "Relación iden=" + iRelO.getIden() + " eliminada por el plan [" + codPlan + "]");
                opRel_Eliminacion(iRelO, idTramiteOrdenante);
                break;

            case 2:;
                // Adición
                mLog.info(s + "Relación iden=" + iRelO.getIden() + " adicionada por el plan [" + codPlan + "]");
                opRel_Adicion(iRelO);
                break;

            default:
                mLog.warn("*** " + s + "         No se opera. La operación iden=" + idTipoOperacion + " no está implementada en esta versión.");
        }

        // Recolección de basura
        System.gc();
        
    }

    public static void opRel_Eliminacion(Relacion iRelO, int idTramiteOrdenante){

        try{
            ClsMain.eliminarObjeto("Relacion", iRelO.getIden(), idTramiteOrdenante);

        } catch (Exception e){
            mLog.error("Fallo en la operación oRel_Eliminacion. " + e + ". Código 23401." );
        }
    }

    private static void opRel_Adicion(Relacion iRelO){
        String s;
        int nRegistros;

        // Para que una relacion se convierta en vigente, sólo debe eliminarse
        //  de la tabla OperacionRelacion
        try{
            s = "Delete From planeamiento.Operacionrelacion Where idrelacion=" + iRelO.getIden();
            nRegistros = ClsConexion.ejecutar(s);

        } catch (Exception e){
            mLog.error("Fallo en la operación oRel_Adicion. " + e + ". Código 23402." );
        }
    }

    public static void eliminarRelacionesHuerfanas(){
        String s;
        Iterator itTB;
        Iterator itR;
        String nomTabla;
        List lista;
        int idR;
        Relacion iRelacion;

        try {
            mLog.info("");
            mLog.info("Eliminando relaciones huérfanas...");

            // Averigua ls diferentes tablas en las que hay que buscar relaciones huérfanas.
            s="Select Distinct Cast(tb.nombre As Text) From diccionario.Tabla tb ";
            itTB = ClsConexion.recordList(s).iterator();
            while (itTB.hasNext()) {
                nomTabla = String.valueOf(itTB.next());

                s="Select r.iden From planeamiento.relacion r, planeamiento.vectorrelacion vr, " +
                    "diccionario.defvector dv, diccionario.tabla tb " +
                    "Where Trim(Upper(tb.nombre))='" + nomTabla.toUpperCase().trim() +"' " +
                    "And dv.idtabla=tb.iden " +
                    "And vr.iddefvector=dv.iden " +
                    "And vr.idrelacion=r.iden " +
                    "And vr.valor Not In (Select t.iden From planeamiento." + nomTabla.toLowerCase().trim() + " t) " +
                    "And Cast (vr.valor As Integer)>0 ";
                itR = ClsConexion.recordList(s).iterator();
                while (itR.hasNext()) {
                    idR = Integer.parseInt(String.valueOf(itR.next()));
                    iRelacion=(Relacion) ClsConexion.dameEntity(Relacion.class, idR);

                    if (iRelacion!=null){
                        // Mensaje
                        lista=new ArrayList();
                        s="Select Cast(trd.texto As Text) From diccionario.defrelacion dr, planeamiento.relacion r, " +
                                "diccionario.traduccion trd " +
                                "Where r.iden=" + idR + " And r.iddefrelacion=dr.iden " +
                                "And dr.idliteral=trd.idliteral ";
                        lista=ClsConexion.recordList(s);
                        s="    Eliminando relación iden=" + idR + " (" + lista.get(0).toString() + ")...";
                        mLog.warn("*** " + s);

                        // Borra relacion
                        opRel_Eliminacion(iRelacion, 0);
                    }
                }
            }

        } catch (Exception e) {
            mLog.error("Fallo en eliminarRelacionesHuerfanas. " + e + ". Código 23029.");
        }

    }

    public static void eliminarRelacionesHuerfanas_ANT(){
        String s;
        Iterator itVR;
        List listaTabla;
        List listaMsg;
        String nomTabla;
        int idVR;
        int iden;
        Vectorrelacion iVectorRelacion;
        Relacion iRelacion;
        int i=0;
        int idRelacion;

        // Se recorre la tabla VectorRelacion para averiguar a qué tabla y con
        //  qué iden apunta cada registro. A continuación se hace una "Select" de
        //  ese iden sobre esa tabla para ver si existe. Si no es así, se elimina
        //  toda la relación. No se tienen en cuenta los iden=0, ya que significan NULL
        try {
            mLog.info("");
            mLog.info("Eliminando relaciones huérfanas.");
            s = "Select iden From planeamiento.Vectorrelacion  ";
            itVR = ClsConexion.recordList(s).iterator();
            while (itVR.hasNext()) {
                idVR = Integer.parseInt(String.valueOf(itVR.next()));
                iVectorRelacion=(Vectorrelacion) ClsConexion.dameEntity(Vectorrelacion.class, idVR);
                if(iVectorRelacion!=null){
                    listaTabla=new ArrayList();
                    s = "Select Cast(t.nombre As Text) From diccionario.Tabla t, diccionario.Defvector dv " +
                            "Where dv.idtabla=t.iden And dv.iden=" + iVectorRelacion.getIddefvector();
                    listaTabla = ClsConexion.recordList(s);
                    nomTabla = listaTabla.get(0).toString();
                    iden = iVectorRelacion.getValor();
                    if (iden>0){
                        listaTabla=new ArrayList();
                        s = "Select iden From planeamiento." + nomTabla.toLowerCase() + " " +
                                "Where iden=" + iden;
                        listaTabla = ClsConexion.recordList(s);
                        if (listaTabla.size() == 0) {
                            // Averigua cuál es la Relacion correspondiente a este VectorRelacion, y
                            //  llama al procedimiento opRel_Eliminacion
                            idRelacion=ClsMain.idRelacionPorIdVectorRelacion(iVectorRelacion.getIden());
                            iRelacion=(Relacion) ClsConexion.dameEntity(Relacion.class, idRelacion);
                            
                            // Mensaje
                            listaMsg=new ArrayList();
                            s="Select Cast(trd.texto As Text) From diccionario.Traduccion trd, " +
                                    "planeamiento.Relacion r, " +
                                    "diccionario.Defrelacion dr Where r.iden=" + iRelacion.getIden() + " " +
                                    "And r.iddefrelacion=dr.iden And dr.idliteral=trd.idliteral ";
                            listaMsg = ClsConexion.recordList(s);
                            s="        Se elimina la relación iden=" + iRelacion.getIden() + " (" +
                                    listaMsg.get(0).toString() + ") debido a que no existe el iden=" + iden + " " +
                                    "en la tabla '" + nomTabla.toLowerCase() + "' ";
                            mLog.info(s);

                            opRel_Eliminacion(iRelacion, 0);
                            i=i+1;
                        }
                    }
                }
            }
            mLog.info("     " + i + " relaciones eliminadas. ");

        } catch (Exception e) {
            mLog.error("Fallo en eliminarRelacionesHuerfanas. " + e + ". Código 23029.");
        }

    }
}
