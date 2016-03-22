/*
* Copyright 2011 red.es
* Autores: IDOM.
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

/*
 * Adaptacion de la v1.0.87 de Urbanismo en Red
 */

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

/**
 *
 * @author fguerrero
 */
public class ClsOperacionDeterminacion {
    public static EntityManager gEM;
    private static final Logger mLog = Logger.getLogger(ClsOperacionDeterminacion.class);

    public static void operacionesDeterminaciones(Operaciondeterminacion iOperacionDeterminacion){
        int idTipoOperacion;
        Determinacion iDetO;
        Determinacion iDetR;
        String s;
        int idTramiteOrdenante;
        String codPlanO;
        String codPlanR;
        int idOperacion;

        idTipoOperacion=(int) iOperacionDeterminacion.getIdtipooperaciondet();
        iDetO=(Determinacion) ClsConexion.dameEntity(Determinacion.class, ClsMain.idDeterminacionPorIdOperacionDeterminacion(iOperacionDeterminacion.getIden()));
        iDetR=(Determinacion) ClsConexion.dameEntity(Determinacion.class, ClsMain.idDeterminacionOperadoraPorIdOperacionDeterminacion(iOperacionDeterminacion.getIden()));

        idOperacion=ClsMain.idOperacionPorIdOperacionDeterminacion(iOperacionDeterminacion.getIden());
        idTramiteOrdenante=ClsMain.idTramitePorIdOperacion(idOperacion);

        s="(" + String.valueOf(idOperacion) + ") ";

        // Se elimina la operación antes de ejecutarla, para evitar el "aviso" de que se ha eliminado
        //  una operación que aún no ha sido ejecutada.
        ClsMain.eliminarOperacion(idOperacion);

        codPlanR=ClsMain.codigoPlanPorIdDeterminacion(iDetR.getIden());
        codPlanO=ClsMain.codigoPlanPorIdDeterminacion(iDetO.getIden());

        if (!iDetR.isBsuspendida()){
            switch (idTipoOperacion) {
                case 1:;
                    // Eliminación
                    mLog.info(s + "[" + iDetR.getCodigo() + "] del plan [" + codPlanR + "] --> Eliminación --> [" + iDetO.getCodigo() + "] del plan [" + codPlanO + "]");
                    opDet_Eliminacion(iDetO, iDetR, idTramiteOrdenante);
                    break;

                case 4:;
                    // Adición Normativa
                    mLog.info(s + "[" + iDetR.getCodigo() + "] del plan [" + codPlanR+ "] --> Adición Normativa --> [" + iDetO.getCodigo() + "] del plan [" + codPlanO + "]");
                    opDet_AdicionNormativa(iDetO, iDetR);
                    break;

                case 6:;
                    // Sustitución Normativa Completa
                    mLog.info(s + "[" + iDetR.getCodigo() + "] del plan [" + codPlanR+ "] --> Sustitución Normativa Completa --> [" + iDetO.getCodigo() + "] del plan [" + codPlanO + "]");
                    opDet_SustitucionNormativaCompleta(iDetO, iDetR);
                    break;

                case 7:;
                    // Suspensión
                    mLog.info(s + "[" + iDetR.getCodigo() + "] del plan [" + codPlanR+ "] --> Suspensión Grafica --> [" + iDetO.getCodigo() + "] del plan [" + codPlanO + "]");
                    opDet_Suspension(iDetO, iDetR);
                    break;

                case 8:;
                    // Adición de Valor de Referencia
                    mLog.info(s + "[" + iDetR.getCodigo() + "] del plan [" + codPlanR+ "] --> Adición de Valor de Referencia --> [" + iDetO.getCodigo() + "] del plan [" + codPlanO + "]");
                    opDet_AdicionValorReferencia(iDetO, iDetR);
                    break;

                case 9:;
                    // Aportación de Determinación
                    mLog.info(s + "[" + iDetR.getCodigo() + "] del plan [" + codPlanR+ "] --> Aportación de Determinación --> [" + iDetO.getCodigo() + "] del plan [" + codPlanO + "]");
                    opDet_AportacionDeterminacion(iDetO, iDetR);
                    break;

                case 11:;
                    // Levantamiento de Suspensión
                    mLog.info(s + "[" + iDetR.getCodigo() + "] del plan [" + codPlanR+ "] --> Levantamiento de Suspensión --> [" + iDetO.getCodigo() + "] del plan [" + codPlanO + "]");
                    opDet_LevantamientoSuspension(iDetO, iDetR);
                    break;

                case 12:;
                    // Aportación de Determinación sobre Determinación Padre
                    mLog.info(s + "[" + iDetR.getCodigo() + "] del plan [" + codPlanR+ "] --> Aportación de Determinación sobre Determinación Padre --> [" + iDetO.getCodigo() + "] del plan [" + codPlanO + "]");
                    opDet_AportacionDeterminacionSobrePadre(iDetO, iDetR);
                    break;

                case 13:;
                    // Aportación de Determinación sobre Determinación Anterior
                    mLog.info(s + "[" + iDetR.getCodigo() + "] del plan [" + codPlanR+ "] --> Aportación de Determinación sobre Determinación Anterior --> [" + iDetO.getCodigo() + "] del plan [" + codPlanO + "]");
                    opDet_AportacionDeterminacionSobreAnterior(iDetO, iDetR);
                    break;

                default:
                    mLog.warn("*** " + s + "         No se opera. La operación iden=" + idTipoOperacion + " no está implementada en esta versión.");
            }

        } else {
            mLog.warn("*** " + s + "         No se opera. La determinación operadora [" + iDetR.getCodigo() + "] está suspendida.");
        }

         // Recolección de basura
        System.gc();
        
    }

    private static void opDet_Eliminacion(Determinacion iDetO, Determinacion iDetR, int idTramiteOrdenante){
        // Se efectúan las siguientes acciones relacionadas con la determinación iDetO que
        //  se va a eliminar:
        //  1 - Sus determinaciones hijas se cuelgan de su determinación padre, en Determinacion.
        //  2 - Se ponen a "null" los idOpcionDeterminacion de EntidadDeterminacionRegimen que
        //      apunten alguna de las opciones que se van a eliminar, y se añade un régimen específico
        //      que avise de que las opciones se han eliminado debido a una operación.
        //  3 - Se ponen a "null" los idDeterminacionRegimen de EntidadDeterminacionRegimen que
        //      apunten a la determinación que se va a borrar, y se añade un régimen específico que
        //      avise de que la determinación de régimen se ha eliminado por una operación.
        //
        //      Se modifica lo anterior el 17/01/2011, para pasar a eliminar el valor completo. Por
        //      lo tanto ya no se añade ningún régimen específico; se borran los existentes, y
        //      los registros de EDR que apunten a la determinación a borrar. Si el caso queda
        //      huérfano también se borra. Y si como consecuencia de esto el ED queda huérfano,
        //      también se elimina.         
        //
        //  4 - Se eliminan la determinación iDetO y sus dependencias.
        
        String s;
        int nRegistros;
        List lista1;
        Iterator it1;
        List lista2;
        Iterator it2;
        String codPlanO;

        try {
            codPlanO=ClsMain.codigoPlanPorIdDeterminacion(iDetO.getIden());

            //  1 - Sus determinaciones hijas se cuelgan de su determinación padre, en Determinacion.
            if (ClsMain.idPadrePorIdDeterminacion(iDetO.getIden()) == 0) {
                s = "Update planeamiento.Determinacion Set idPadre=null Where idpadre=" + iDetO.getIden();
            } else {
                s = "Update planeamiento.Determinacion " +
                        "Set idPadre=" + ClsMain.idPadrePorIdDeterminacion(iDetO.getIden()) + " Where idpadre=" + iDetO.getIden();
            }
            nRegistros = ClsConexion.ejecutar(s);

            // 2 - EntidadDeterminacionRegimen --> idOpcionDeterminacion=null
            lista1 = new ArrayList();
            s = "Select iden From planeamiento.Opciondeterminacion " +
                    "Where iddeterminacion=" + iDetO.getIden() + " " +
                    "Or iddeterminacionvalorref=" + iDetO.getIden();
            it1 = lista1.iterator();
            while (it1.hasNext()) {
                lista2 = new ArrayList();
                s = "Select iden From planeamiento.Entidaddeterminacionregimen " +
                        "Where idopciondeterminacion=" + it1.next();
                it2 = lista2.iterator();
                while (it2.hasNext()) {
                    // Pone la opción a null
                    s = "Update planeamiento.Entidaddeterminacionregimen " +
                        "Set idopciondeterminacion=null " +
                        "Where iden=" + it2.next();
                    nRegistros = ClsConexion.ejecutar(s);
                    // Añade régimen específico
                    s="Insert Into planeamiento.Regimenespecifico (identidaddeterminacionregimen, " +
                            "orden, nombre, texto) Values (" + it2.next() + ", " +
                            "(Select 1+Max(orden) From planeamiento.Regimenespecifico " +
                            "Where idPadre Is Null And identidaddeterminacionregimen=" + it2.next() + "), " +
                            "'Valor modificado', 'La determinación valor [" + iDetO.getNombre() + "] " +
                            "ha sido eliminada por el plan [" + codPlanO + "]') ";
                    nRegistros = ClsConexion.ejecutar(s);
                }
            }

            // 3 - EntidadDeterminacionRegimen --> idDeterminacionRegimen=null
            lista2 = new ArrayList();
            s = "Select iden From planeamiento.Entidaddeterminacionregimen " +
                    "Where iddeterminacionregimen=" + iDetO.getIden();
            it2 = lista2.iterator();
            while (it2.hasNext()) {
                // Pone el puntero a null
                s = "Update planeamiento.Entidaddeterminacionregimen " +
                    "Set iddeterminacionregimen=null " +
                    "Where iden=" + it2.next();
                nRegistros = ClsConexion.ejecutar(s);
                // Añade régimen específico
                s="Insert Into planeamiento.Regimenespecifico (identidaddeterminacionregimen, " +
                        "orden, nombre, texto) Values (" + it2.next() + ", " +
                        "(Select 1+Max(orden) From planeamiento.Regimenespecifico " +
                        "Where idPadre Is Null And identidaddeterminacionregimen=" + it2.next() + "), " +
                        "'Valor modificado', 'La determinación de régimen [" + iDetO.getNombre() + "] " +
                        "ha sido eliminada por el plan [" + ClsMain.codigoPlanPorIdDeterminacion(iDetO.getIden()) + "]') ";
                nRegistros = ClsConexion.ejecutar(s);
            }

            //  4 - Elimina determinación
            ClsMain.eliminarObjeto("Determinacion", iDetO.getIden(), idTramiteOrdenante);

        } catch (Exception e) {
            mLog.error("Fallo en la operación opDet_Eliminacion. " + e + ". Código 23201." );
        }
        
    }

    private static void opDet_AdicionNormativa(Determinacion iDetO, Determinacion iDetR){
        // Consiste en copiar las propiedades y vectores de Regulación de
        //  la determinación iDetR a la determinación iDetO.
        // Sólo se tienen en cuenta las relaciones que no están apuntadas
        //  desde OperacionRelacion con el tipo de operación "Adición", ya
        //  que estas relaciones no son vigentes, sino que están pendientes de
        //  ser ejecutadas como operaciones.
        // También se copian los documentos.

        String s;
        Iterator it;
        int idRelacion;
        TreeMap tm;
        int nRegistros;
        int iden;
        List lista;
        int nIdensRegulaciones;
        int i;
        int idTramiteO;

        try{
            // *********************
            // Regulación específica de iDetR
            idTramiteO=ClsMain.idTramitePorIdDeterminacion(iDetO.getIden());

            s="Select r.iden " +
                    "From planeamiento.Relacion r, planeamiento.Vectorrelacion vr " +
                    "Where r.iddefrelacion=" + ClsDatos.ID_DEFRELACION_REGULACIONESPECIFICA +
                    " And vr.iddefvector=" + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADAESPECIFICA + " " +
                    "And vr.idrelacion=r.iden And vr.valor=" + iDetR.getIden() + " " +
                    "And r.iden Not In (Select idrelacion " +
                    "From planeamiento.Operacionrelacion Where idtipooperacionrel=" +
                    ClsDatos.ID_TIPOOPERACIONRELACION_ADICION + ") ";
            it=ClsConexion.recordList(s).iterator();
            tm=new TreeMap();
            while (it.hasNext()){
                idRelacion=Integer.parseInt(it.next().toString());

                // Por cada relacion de regulación de iDetR, se inserta una
                //  relacion para iDetO y se guarda el mapeo en tm, ya que es
                //  necesario insertar todos los registros de la tabla Ralacion
                //  antes de poder copiar las propiedades y vectores, debido a
                //  que uno de los vectores es un idRelacion que apunta a la
                //  regulación precedente (como un idPadre)
                s="Insert Into planeamiento.Relacion (iddefrelacion, idtramitecreador) " +
                        "Values ( " +ClsDatos.ID_DEFRELACION_REGULACIONESPECIFICA +", " +
                        idTramiteO + ") ";
                nRegistros=ClsConexion.ejecutar(s);
                iden=ClsMain.ultimoIden("planeamiento.Relacion");
                tm.put(idRelacion, iden);
            }

            // Copia las propiedades y vectores de regulación específica
            it=tm.keySet().iterator();
            while (it.hasNext()){
                idRelacion=Integer.parseInt(it.next().toString());
                iden=Integer.parseInt(tm.get(idRelacion).toString());

                // Propiedades
                s="Insert Into planeamiento.Propiedadrelacion (idrelacion, iddefpropiedad, " +
                        "valor) Select " + iden + ", iddefpropiedad, valor " +
                        "From planeamiento.Propiedadrelacion Where idrelacion=" + idRelacion;
                nRegistros=ClsConexion.ejecutar(s);

                // Vector que apunta a la determinacion idDefVector
                s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, valor) " +
                        "Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADAESPECIFICA +
                        ", " + iDetO.getIden() + ") ";
                nRegistros=ClsConexion.ejecutar(s);

                // Vector que apunta a la regulacion precedente idDefVector
                lista=new ArrayList();
                s="Select valor From planeamiento.Vectorrelacion " +
                        "Where idrelacion=" + idRelacion +
                        " And iddefvector=" + ClsDatos.ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA ;
                lista = ClsConexion.recordList(s);
                if (lista.size() == 0) {
                    s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, " +
                            "valor) Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA + ", null) ";
                } else {
                    s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, " +
                            "valor) Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA + ", " +
                            Integer.parseInt(lista.get(0).toString()) + ") ";
                }
                nRegistros=ClsConexion.ejecutar(s);
            }

            // Relaciones donde iDetR es la determinación regulada.
            nIdensRegulaciones=ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADA.length;
            for (i=0;i<nIdensRegulaciones; i++){
                s="Select r.iden " +
                    "From planeamiento.Relacion r, planeamiento.Vectorrelacion vr " +
                    "Where r.iddefrelacion=" + ClsDatos.ID_DEFRELACION_REGULACION[i] +
                    " And vr.iddefvector=" + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADA[i] + " " +
                    "And vr.idrelacion=r.iden And vr.valor=" + iDetR.getIden() + " " +
                    "And r.iden Not In (Select idrelacion " +
                    "From planeamiento.Operacionrelacion Where idtipooperacionrel=" +
                    ClsDatos.ID_TIPOOPERACIONRELACION_ADICION + ") ";
                it=ClsConexion.recordList(s).iterator();
                tm=new TreeMap();
                while (it.hasNext()){
                    idRelacion=Integer.parseInt(it.next().toString());

                    // Por cada relacion de regulación de iDetR, se inserta una
                    //  relacion para iDetO y se guarda el mapeo en tm, ya que es
                    //  necesario insertar todos los registros de la tabla Ralacion
                    //  antes de poder copiar las propiedades y vectores, debido a
                    //  que uno de los vectores es un idRelacion que apunta a la
                    //  regulación precedente (como un idPadre)
                    s="Insert Into planeamiento.Relacion (iddefrelacion, idtramitecreador) " +
                            "Values ( " +ClsDatos.ID_DEFRELACION_REGULACION[i] +", " +
                            idTramiteO + ") ";
                    nRegistros=ClsConexion.ejecutar(s);
                    iden=ClsMain.ultimoIden("planeamiento.Relacion");
                    tm.put(idRelacion, iden);
                }

                // Copia las propiedades y vectores de 'Determinación reguladora'
                it=tm.keySet().iterator();
                while (it.hasNext()){
                    idRelacion=Integer.parseInt(it.next().toString());
                    iden=Integer.parseInt(tm.get(idRelacion).toString());

                    // Propiedades
                    s="Insert Into planeamiento.Propiedadrelacion (idrelacion, iddefpropiedad, " +
                            "valor) Select " + iden + ", iddefpropiedad, valor " +
                            "From planeamiento.Propiedadrelacion Where idrelacion=" + idRelacion;
                    nRegistros=ClsConexion.ejecutar(s);

                    // Vector que apunta a la determinacion regulada idDefVector
                    s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, valor) " +
                            "Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADA[i] +
                            ", " + iDetO.getIden() + ") ";
                    nRegistros=ClsConexion.ejecutar(s);

                    // Vector que apunta a la determinacion reguladora idDefVector
                    s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, valor) " +
                            "Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADORA[i] +
                            ", " + iDetO.getIden() + ") ";
                    nRegistros=ClsConexion.ejecutar(s);
                }
            }

            // Copia los documentos
            s="Insert Into planeamiento.Documentodeterminacion (iddeterminacion, iddocumento) " +
                    "Select " + iDetO.getIden() + ", iddocumento From planeamiento.Documentodeterminacion " +
                    "Where iddeterminacion=" + iDetR.getIden();
            nRegistros=ClsConexion.ejecutar(s);
            
        } catch (Exception e){
            mLog.error("Fallo en la operación opDet_AdicionNormativa. " + e + ". Código 23204." );
        }

    }

    private static void opDet_SustitucionNormativaCompleta(Determinacion iDetO, Determinacion iDetR){
        // Se compone de cinco pasos:
        //  - Eliminación normativa. Se eliminan todas las propiedades y vectores
        //      relacionados con la regulación
        //  - Operación opDet_AdicionNormativa
        //  - Sustituir nombre y texto
        //  - Sustituir documentos

        String s;
        Iterator it;
        int idRelacion;
        int nRegistros;
        int nIdensRegulacion;
        int i;

        try {
            // *********************
            // Eliminación normativa

            // Regulación específica de iDetO
            s="Select r.iden " +
                    "From planeamiento.Relacion r, planeamiento.Vectorrelacion vr " +
                    "Where r.iddefrelacion=" + ClsDatos.ID_DEFRELACION_REGULACIONESPECIFICA +
                    " And vr.iddefvector=" + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADAESPECIFICA + " " +
                    "And vr.idrelacion=r.iden And vr.valor=" + iDetO.getIden() +" " +
                    "And r.iden Not In (Select idrelacion " +
                    "From planeamiento.Operacionrelacion Where idtipooperacionrel=" +
                    ClsDatos.ID_TIPOOPERACIONRELACION_ADICION + ") ";
            it=ClsConexion.recordList(s).iterator();
            while (it.hasNext()){
                idRelacion=Integer.parseInt(it.next().toString());

                // Borrar propiedades
                s="Delete From planeamiento.Propiedadrelacion Where idrelacion=" + idRelacion;
                nRegistros=ClsConexion.ejecutar(s);

                // Borrar vectores
                s="Delete From planeamiento.Vectorrelacion Where idrelacion=" + idRelacion;
                nRegistros=ClsConexion.ejecutar(s);
            }

            // Relaciones donde iDetO es la determinación regulada
            nIdensRegulacion=ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADORA.length;
            for (i=0;i<nIdensRegulacion; i++){
                s="Select r.iden " +
                        "From planeamiento.Relacion r, planeamiento.Vectorrelacion vr " +
                        "Where r.iddefrelacion=" + ClsDatos.ID_DEFRELACION_REGULACION[i] +
                        " And vr.iddefvector=" + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADA[i] + " " +
                        "And vr.idrelacion=r.iden And vr.valor=" + iDetO.getIden() + " " +
                        "And r.iden Not In (Select idrelacion " +
                        "From planeamiento.Operacionrelacion Where idtipooperacionrel=" +
                    ClsDatos.ID_TIPOOPERACIONRELACION_ADICION + ") ";
                it=ClsConexion.recordList(s).iterator();
                while (it.hasNext()){
                    idRelacion=Integer.parseInt(it.next().toString());

                    // Borrar propiedades
                    s="Delete From planeamiento.Propiedadrelacion Where idrelacion=" + idRelacion;
                    nRegistros=ClsConexion.ejecutar(s);

                    // Borrar vectores
                    s="Delete From planeamiento.Vectorrelacion Where idrelacion=" + idRelacion;
                    nRegistros=ClsConexion.ejecutar(s);
                }
            }

            // Llamada a la operación de adición normativa
            opDet_AdicionNormativa(iDetO, iDetR);

            // Sustituir nombre y texto
            s="Update planeamiento.Determinacion " +
                    "Set nombre=(Select nombre From planeamiento.Determinacion " +
                    "Where iden=" + iDetR.getIden() +") Where iden=" + iDetO.getIden();
            nRegistros=ClsConexion.ejecutar(s);
            s="Update planeamiento.Determinacion " +
                    "Set texto=(Select texto From planeamiento.Determinacion " +
                    "Where iden=" + iDetR.getIden() +") Where iden=" + iDetO.getIden();
            nRegistros=ClsConexion.ejecutar(s);

            // Sustituir documentos
            s="Delete From planeamiento.Documentodeterminacion " +
                    "Where iddeterminacion=" + iDetO.getIden();
            nRegistros=ClsConexion.ejecutar(s);
            s="Update planeamiento.Documentodeterminacion " +
                    "Set iddeterminacion=" + iDetO.getIden() + " " +
                    "Where iddeterminacion=" + iDetR.getIden();
            nRegistros=ClsConexion.ejecutar(s);
        } catch (Exception e){
            mLog.error("Fallo en la operación opDet_SustitucionNormativaCompleta. " + e + ". Código 23206." );
        }

    }

    private static void opDet_Suspension(Determinacion iDetO, Determinacion iDetR){
        // Marcar la determinación iDetO como 'suspendida'
        String s;
        int nRegistros;

        try {
            s = "Update planeamiento.Determinacion Set bsuspendida=true " +
                    "Where iden=" + iDetO.getIden();
            nRegistros = ClsConexion.ejecutar(s);
        } catch (Exception e) {
            mLog.error("Fallo en la operación opDet_Suspension. " + e + ". Código 23207." );
        }

    }

    private static void opDet_AdicionValorReferencia(Determinacion iDetO, Determinacion iDetR){
        // Copiar los valores de referencia de la determinación operadora a la determinación operada, y
        //  reasignar los punteros de EntidadDeterminacionRegimen que apuntan a las opciones iDetR para
        //  que apunten a las nuevas opciones de iDetO.

        String s="";
        int nRegistros;
        Iterator it;
        Object[] obj;
        int idOpcion;
        int idOpcionNueva;
        int idDetVR;
        List lista;
        Determinacion iDetPadre;
        Determinacion iDetVR;
        int idTramiteO;
        int idTramiteR;

        try {
            idTramiteO=ClsMain.idTramitePorIdDeterminacion(iDetO.getIden());
            idTramiteR=ClsMain.idTramitePorIdDeterminacion(iDetR.getIden());

            s = "Select iden As C1, iddeterminacionvalorref As C2 " + 
                "From planeamiento.Opciondeterminacion " +
                "Where iddeterminacion=" + iDetR.getIden();
            it = ClsConexion.recordList(s).iterator();
            while (it.hasNext()) {
                obj = (Object[]) it.next();
                idOpcion = Integer.parseInt(obj[0].toString());
                idDetVR = Integer.parseInt(obj[1].toString());

                // Comprueba si ya existe la Opcion para la determinación operada
                lista = new ArrayList();
                s = "Select iden From planeamiento.Opciondeterminacion " +
                        "Where iddeterminacion=" + iDetO.getIden() + " " +
                        "And iddeterminacionvalorref=" + idDetVR;
                lista = ClsConexion.recordList(s);
                if (lista.size() > 0) {
                    // Existe
                    idOpcionNueva = Integer.parseInt(lista.get(0).toString());
                } else {
                    // No existe
                    s = "Insert Into planeamiento.Opciondeterminacion (iddeterminacion, " +
                            "iddeterminacionvalorref) Values (" + iDetO.getIden() + ", " +
                            idDetVR + ") ";
                    nRegistros = ClsConexion.ejecutar(s);
                    idOpcionNueva = ClsMain.ultimoIden("planeamiento.Opciondeterminacion");
                }

                // Reasigna los idOpcion de EntidadDeterminacionRegimen que apuntaban a
                //  la opción de la determinación operadora, para que apunten a la
                //  nueva opción.
                s = "Update planeamiento.Entidaddeterminacionregimen " +
                        "Set iddeterminacionregimen=" + iDetO.getIden() + " " +
                        "Where iddeterminacionregimen=" + iDetR.getIden() + " " +
                        "And idopciondeterminacion=" + idOpcion;
                nRegistros = ClsConexion.ejecutar(s);

                s = "Update planeamiento.Entidaddeterminacionregimen " +
                        "Set idopciondeterminacion=" + idOpcionNueva + " " +
                        "Where idopciondeterminacion=" + idOpcion;
                nRegistros = ClsConexion.ejecutar(s);

                // Averigua el iden de la carpeta de determinaciones aportadas
                iDetPadre = crearCarpetaDeterminacionesAportadas(idTramiteO, idTramiteR);

                // Reasigna el trámite de la determinacion valor de referencia
                iDetVR=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDetVR);
                bReasignarDeterminacion(iDetO, iDetPadre, iDetVR, 0);
            }
        } catch (Exception e) {
            mLog.error("Fallo en la operación opDet_AdicionValorReferencia. " + e + ". Código 23208." );
        }

    }

    private static void opDet_AportacionDeterminacion(Determinacion iDetO, Determinacion iDetR){
        // Se cambia el idTramite de la determinación iDetR, y se cambia su idPadre para
        //  que apunte a la determinación "Determinaciones aportadas por..." que se
        //  debe crear en el trámite de iDetO.
        // El orden se pone a cero para que el procedimiento de bReasignarDeterminacion le ponga
        //  el último + 1
        Determinacion iDetPadre;
        int idTramiteO;
        int idTramiteR;

        try {
            idTramiteO=ClsMain.idTramitePorIdDeterminacion(iDetO.getIden());
            idTramiteR=ClsMain.idTramitePorIdDeterminacion(iDetR.getIden());

            if(idTramiteO!=idTramiteR){
                // Determinación de la que va a colgar
                iDetPadre = crearCarpetaDeterminacionesAportadas(idTramiteO, idTramiteR);
                // Reasignar el IdTramite
                bReasignarDeterminacion(iDetO, iDetPadre, iDetR, 0);
            }
        } catch (Exception e){
            mLog.error("Fallo en la operación opDet_AportacionDeterminacion. " + e + ". Código 23209." );
        }
        
    }

    public static boolean bReasignarDeterminacion(Determinacion iDetO, Determinacion iDetPadre, Determinacion iDetR, int orden){
        // Reasignar el idTramite de la determinación iDetR al de iDetO, cambiando su
        //  código para evitar la restricción que impide la duplicación de códigos en
        //  un mismo trámite.

        // Primero, se averigua cuál es el último código de determinación en
        //  el trámite iTramiteO.
        // Segundo, se reasigna el trámite de iDetR desde iTramiteR a iTramiteO

        // Si la determinación es 'Grupo de Entidades' o algunos de sus valores de referencia, y
        //  ya existe en el trámite iTramiteO, no se hace el cambio, ya que en cada trámite sólo
        //  puede haber un conjunto de determinaciones de este tipo.

        // El parámetro "orden" indica el orden que se le debe asignar a la determinación. Si es cero, significa
        //  que debe asignarse el último + 1. En cualquier caso, el orden del resto de determinaciones debe
        //  moverse para acomodar iDetR en su sitio.

        // Reasigna el idTramiteCreador de sus relaciones

        String maxCod;
        Iterator it;
        Opciondeterminacion iOpcion;
        Documentodeterminacion iDocDet;
        Documento iDocumento;
        Determinacion iDet;
        Tramite iTramiteO;
        Tramite iTramiteR;
        String s;
        int nRegistros;
        Determinacion iDetGrupoDeEntidadesO;
        Determinacion iDetGrupoDeEntidadesR;
        List <Integer> lista;
        Boolean esGrupoEntidad;
        Boolean esVRgrupoEntidad;
        int idTramiteO;
        int idTramiteR;
        
        try {
            idTramiteO=ClsMain.idTramitePorIdDeterminacion(iDetO.getIden());
            idTramiteR=ClsMain.idTramitePorIdDeterminacion(iDetR.getIden());

            // Si los trámites son el mismo, se aborta el procedimiento.
            if (idTramiteO==idTramiteR){
                return true;
            }
            
            // Si alguno de los dos es un trámite base, también se aborta.
            if (ClsDatos.gListaIdTramitesBase.contains(idTramiteO)==true){
                return true;
            }
            if (ClsDatos.gListaIdTramitesBase.contains(idTramiteR)==true){
                return true;
            }
            
            iTramiteO= (Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteO);
            iTramiteR= (Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteR);
 
            iDetGrupoDeEntidadesO=ClsMain.determinacionGrupoDeEntidadesPorTramite(iTramiteO);
            iDetGrupoDeEntidadesR=ClsMain.determinacionGrupoDeEntidadesPorTramite(iTramiteR);

            // Averigua iDetR si es 'Grupo de Entidades' o alguno de sus valores de referencia
            esGrupoEntidad=false;
            esVRgrupoEntidad=false;
            if (iDetGrupoDeEntidadesR!=null){
                if (iDetGrupoDeEntidadesR.getIden()==iDetR.getIden()){
                    esGrupoEntidad=true;
                }
                lista=new ArrayList();
                lista=ClsMain.idDeterminacionesValorReferenciaPorDeterminacion(iDetGrupoDeEntidadesR);
                if (lista.contains(iDetR.getIden())){
                    esVRgrupoEntidad=true;
                }
            }

            // Si iDetR esGrupoEntidad=true y ya existe otra determinación de este tipo
            //  en el iTramiteO, la determinación no se reasigna, y se fuerza la salida
            //  del procedimiento.
            if (esGrupoEntidad==true){
                if (iDetGrupoDeEntidadesO!=null){
                    return true;
                }
            }

            // Ídem si es un valor de referencia de la determinación 'Grupo de Entidades', y ya
            //  existe un valor de referencia en iTramiteO que apunte a la misma determinación 
            //  base (lo que significa que se trata del mismo grupo de entidades).
            if (esVRgrupoEntidad==true) {
                s="Select Count(*) From planeamiento.Determinacion do1, " +
                        "planeamiento.Determinacion dr, planeamiento.Opciondeterminacion odo, " +
                        "planeamiento.Opciondeterminacion odr " +
                        "Where odo.iddeterminacion=" + iDetGrupoDeEntidadesO.getIden() + " " +
                        "And odo.iddeterminacionvalorref=do1.iden " +
                        "And odr.iddeterminacion=" + iDetGrupoDeEntidadesR.getIden() + " " +
                        "And odr.iddeterminacionvalorref=dr.iden " + 
                        "And do1.iddeterminacionbase=dr.iddeterminacionbase ";
                nRegistros=Integer.parseInt(ClsConexion.recordList(s).get(0).toString());
                if (nRegistros>0){
                    return true;
                }
            }
            
            // Reasigna los datos de la determinación iDetR para encajarla en el trámite iTramiteO

            // Código
            maxCod = ClsMain.maximoCodigoDeterminacion(iTramiteO.getIden(), 1);
            // Al codigo que le corresponde, le vuelve a poner el primer caracter que
            //  tenia el código original, por si se trata de una determinación cuyo
            //  primer carater es significativo.
            s=iDetR.getCodigo().substring(0,1);
            maxCod=s + maxCod.substring(1);
            s="Update planeamiento.Determinacion Set codigo='" + maxCod + "' " +
                    "Where iden=" + iDetR.getIden();
            nRegistros=ClsConexion.ejecutar(s);

            // Cambia el padre de la determinación.
            nRegistros=0;
            if (iDetPadre==null){
                s="Update planeamiento.Determinacion Set idpadre=Null " +
                    "Where iden=" + iDetR.getIden();
            } else {
                s="Update planeamiento.Determinacion Set idpadre=" + iDetPadre.getIden() + " " +
                    "Where iden=" + iDetR.getIden();
            }
            nRegistros=ClsConexion.ejecutar(s);   

            // Orden de la determinación
            if (orden==0){
                // Se calcula el máximo orden de las que tienen el mismo padre
                s="Select Max(orden) From planeamiento.Determinacion Where idpadre";
                if (iDetPadre==null){
                    s = s + " Is Null And idtramite=" + iTramiteO.getIden();
                } else {
                    s = s +"=" + iDetPadre.getIden();
                }
                lista=new ArrayList();
                lista=ClsConexion.recordList(s);
                if (lista.get(0)==null){
                    orden=1;
                } else {
                    orden=1+lista.get(0);
                }
            } else {
                // Se le suma 1 a todos los órdenes mayores o iguales al orden que se le va a asignar, y luego
                //  se le pone su orden.
                s="Update planeamiento.Determinacion Set orden=orden+1 Where orden>=" + orden + " And idpadre";
                if (iDetPadre==null){
                    s = s + " Is Null And idtramite=" + iTramiteO.getIden();
                } else {
                    s = s +"=" + iDetPadre.getIden();
                }
                nRegistros=ClsConexion.ejecutar(s);
            }
            s="Update planeamiento.Determinacion Set orden=" + orden + " Where iden=" + iDetR.getIden();
            nRegistros=ClsConexion.ejecutar(s);

            // Reasigna los datos de sus dependencias

            // Sus documentos, si los tiene
            s="Select iddocumento From planeamiento.Documentodeterminacion " +
                    "Where iddeterminacion=" + iDetR.getIden();
            it = ClsConexion.recordList(s).iterator();
            while (it.hasNext()) {
                iDocumento=(Documento) ClsConexion.dameEntity(Documento.class, it.next());
                s="Update planeamiento.Documento Set idtramite=" + iTramiteO.getIden() + " " +
                    "Where iden=" + iDocumento.getIden();
                nRegistros=ClsConexion.ejecutar(s);
            }

            // Reasigna el idTramite
            s="Update planeamiento.Determinacion Set idtramite=" + iTramiteO.getIden() + " " +
                    "Where iden=" + iDetR.getIden();
            nRegistros=ClsConexion.ejecutar(s);

            // Reasigna el idTramiteCreador de sus relaciones
            s="Update planeamiento.Relacion Set idtramitecreador=" + iTramiteO.getIden() + " " +
                    "Where iden In (Select VR.idrelacion From planeamiento.vectorrelacion VR, " +
                    "diccionario.tabla TB, diccionario.defvector DV " + 
                    "Where VR.valor=" + iDetR.getIden() + " And VR.iddefvector=DV.iden " +
                    "And DV.idtabla=TB.iden And TB.nombre='DETERMINACION')";
            nRegistros=ClsConexion.ejecutar(s);

            // Sus valores de referencia, si los tiene
            s="Select iddeterminacionvalorref From planeamiento.Opciondeterminacion " +
                    "Where iddeterminacion=" + iDetR.getIden();
            it = ClsConexion.recordList(s).iterator();
            while (it.hasNext()) {
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, it.next());
                bReasignarDeterminacion(iDetR, iDetR, iDet, 0);
            }
            // Sus hijas, si las tiene, y no son sus valores de referencia (que ya
            //  han debido ser cambiados más arriba)
            s="Select iden From planeamiento.Determinacion " +
                    "Where idpadre=" + iDetR.getIden() + " " +
                    "And iden Not In (Select iddeterminacionvalorref " +
                    "From planeamiento.Opciondeterminacion " +
                    "Where iddeterminacion=" + iDetR.getIden() +") ";
            it = ClsConexion.recordList(s).iterator();
            while (it.hasNext()) {
                iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, it.next());
                bReasignarDeterminacion(iDetR, iDetR, iDet, 0);
            }
            
        } catch (Exception e) {
            mLog.error("Fallo en reasignarDeterminacion. " + e + ". Código 23030." );
            return false;
        }

        return true;
    }

    public static Determinacion crearCarpetaDeterminacionesAportadas(int idTramiteO, int idTramiteR){
        // Crea la carpeta de "Determinaciones aportadas" en el trámite iTramiteO con
        //  el nombre del trámite iTramiteR, si no existe en iTramiteO.
        //   De ella, se colgarán todas las determinaciones del iTramiteR que deban
        //  ser reasignadas al iTramiteO

        String s="";
        String txtDeterminacion;
        Iterator it;
        int nRegistros;
        Determinacion iDeterminacion = null;
        Tramite iTramiteR;
        Tramite iTramiteO;
        int idDet;
        String codigoDet="";
        List listaDet;
        String siguienteApartado;
        int idPlan;
        Plan iPlan;

        try {
            iTramiteO=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteO);
            iTramiteR=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteR);

            idPlan=ClsMain.idPlanPorIdTramite(idTramiteR);
            iPlan=(Plan) ClsConexion.dameEntity(Plan.class, idPlan);
            txtDeterminacion = ClsDatos.TEXTO_APORTADAS + ClsMain.datosPlan(iPlan);
            if (txtDeterminacion.length() > 220) {
                txtDeterminacion = txtDeterminacion.substring(0, 220);
            }

            // Averigua si la determinación ya existe por haber sido creada con anterioridad
            listaDet=new ArrayList();
            s = "Select d.iden " +
                    "From planeamiento.Determinacion as d " +
                    "Where d.idtramite=" + iTramiteO.getIden() + " " +
                    "And d.idcaracter=" + ClsDatos.ID_CARACTER_ENUNCIADO + " " +
                    "And d.nombre='" + txtDeterminacion + "' ";
            listaDet = ClsConexion.recordList(s);
            if (listaDet.size() == 0) {
                codigoDet = ClsMain.maximoCodigoDeterminacion(idTramiteO, 1);
                siguienteApartado=ClsMain.ultimoApartadoDeterminacion(null, iTramiteO.getIden(), true);
                s = "Insert Into planeamiento.Determinacion (idtramite, idpadre, idcaracter, " +
                        "nombre, codigo, bsuspendida, apartado, orden) Values (" + iTramiteO.getIden() + ", " +
                        "null, "+ ClsDatos.ID_CARACTER_ENUNCIADO + ", '" + txtDeterminacion + "', '" + 
                        codigoDet + "', false, '" + siguienteApartado + "', " + 
                        "(Select 1+Max(orden) From planeamiento.Determinacion " + 
                        "Where idTramite=" + iTramiteO.getIden() +" And idPadre Is Null)) ";
                nRegistros = ClsConexion.ejecutar(s);
                idDet = ClsMain.ultimoIden("planeamiento.Determinacion");
            } else {
                idDet = Integer.parseInt(listaDet.get(0).toString());
            }
            iDeterminacion=(Determinacion) ClsConexion.dameEntity(Determinacion.class, idDet);
        } catch (Exception e) {
            mLog.error("Fallo en crearCarpetaDeterminacionesAportadas. " + e + ". Código 23031." );
            return null;
        }
        return iDeterminacion;
    }

    private static void opDet_LevantamientoSuspension(Determinacion iDetO, Determinacion iDetR){
        // Marcar la determinación iDetO como 'No suspendida'
        String s;
        int nRegistros;

        try {
            s = "Update planeamiento.Determinacion Set bsuspendida=false " +
                    "Where iden=" + iDetO.getIden();
            nRegistros = ClsConexion.ejecutar(s);
        } catch (Exception e) {
            mLog.error("Fallo en la operación opDet_LevantamientoSuspension. " + e + ". Código 23202." );
        }

    }

    private static void opDet_AportacionDeterminacionSobrePadre(Determinacion iDetO, Determinacion iDetR){
        // Se cambia el idTramite de la determinación iDetR, y se cambia su idPadre para
        //  que apunte a la determinación iDetO. Se le asigna el orden 1 para que sea la primera
        //  determinación hija.
        Determinacion iDetPadre;
        int idTramiteO;
        int idTramiteR;

        try {
            idTramiteO=ClsMain.idTramitePorIdDeterminacion(iDetO.getIden());
            idTramiteR=ClsMain.idTramitePorIdDeterminacion(iDetR.getIden());

            // Determinación de la que va a colgar
            iDetPadre = iDetO;
            // Reasignar el IdTramite
            bReasignarDeterminacion(iDetO, iDetPadre, iDetR, 1);

        } catch (Exception e){
            mLog.error("Fallo en la operación opDet_AportacionDeterminacionSobrePadre. " + e + ". Código 23203." );
        }

    }

    private static void opDet_AportacionDeterminacionSobreAnterior(Determinacion iDetO, Determinacion iDetR){
        // Se cambia el idTramite de la determinación iDetR, y se cambia su idPadre para
        //  que apunte a la determinación padre de iDetO. El orden que se le asigna es el siguiente al
        //  orden que tiene iDetO
        Determinacion iDetPadre;
        int idTramiteO;
        int idTramiteR;
        int orden;

        try {
            idTramiteO=ClsMain.idTramitePorIdDeterminacion(iDetO.getIden());
            idTramiteR=ClsMain.idTramitePorIdDeterminacion(iDetR.getIden());
            // Determinación de la que va a colgar
            iDetPadre = iDetO.getDeterminacionByIdpadre();
            // Orden que se le va a asignar
            orden = 1 + ClsMain.ordenDeterminacionPorIden(iDetO.getIden());
            // Reasignar el IdTramite
             bReasignarDeterminacion(iDetO, iDetPadre, iDetR, orden);
            
        } catch (Exception e){
            mLog.error("Fallo en la operación opDet_AportacionDeterminacionSobrePadre. " + e + ". Código 23203." );
        }

    }
}
