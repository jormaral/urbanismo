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
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

/**
 *
 * @author Miguel.Martin
 */
public class ClsOperacionPlan {
    public static EntityManager gEM;
    private static final Logger mLog = Logger.getLogger(ClsOperacionPlan.class);

    public static void opPla_Modificacion(Tramite iTramiteO, Tramite iTramiteR, int soloOperadores, int soloMensajes) {
        Iterator it1;
        Iterator it2;
        Operacion iOperacion;
        Operaciondeterminacion iOperacionDeterminacion;
        Operacionentidad iOperacionEntidad;
        Operacionrelacion iOperacionRelacion;
        int idEntidadO;
        SortedMap sm = new TreeMap();
        String sTo;
        String sTr;
        long orden;
        Determinacion iDeterminacionO;
        Entidad iEntidadO;
        Relacion iRelacionO;
        String s;
        List lista;
        int iden;
        boolean operacionIntentada;
        int tipoModificacion;
        boolean operar;
        boolean existeOperacion;
        String[] sOperadores;
        String codPlanO;
        String codPlanR;
        int idTramiteOrdenante;
        int idTramiteCreador;
        int idRelacion;
     
        try{
            sTo = iTramiteO.getFecha().toString();
            sTr = iTramiteR.getFecha().toString();

            // Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            codPlanO=ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden());
            codPlanR=ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden());
            // Según sea el valor del parámetro soloOperadores, se presentará un mensaje diferente
            sOperadores  = new String[] {"operadas","operadoras"};

            // Para controlar el contenido de los mensajes, se usa la variable tipoModificación
            //      1 = Operaciones entre determinaciones
            //      2 = Operaciones entre entidades
            //      3 = Operaciones sobre relaciones
            tipoModificacion=0;

            // Ordena las operaciones del trámite operador por el campo 'orden'
            lista=new ArrayList();
            s="Select o.iden " +
            "From planeamiento.Operacion o " +
            "Where o.idtramiteordenante=" + iTramiteR.getIden() + " " +
            "Order By o.orden ";
            lista= ClsConexion.recordList(s);
            if (lista.size()==0){
                return;
            }

            // Si el trámite operador está suspendido, no se ejecuta la operación
            if(soloMensajes==1){
                if (ClsMain.bTramiteSuspendido(iTramiteR)==true){
                    s="        ** SUSPENDIDO** El trámite operador está suspendido " + 
                        "y no se ejecuta la modificación del plan [" + 
                        ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden()) + "] sobre " +
                        "el plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden()) + "].";
                    mLog.info(s);
                    ClsMain.copiarAmbitoAplicacion(iTramiteO, iTramiteR);
                    return;
                }
            }

            idTramiteOrdenante=iTramiteR.getIden();
            it1 = lista.iterator();
            while (it1.hasNext()) {
                iden=Integer.parseInt(it1.next().toString());
                iOperacion=(Operacion) ClsConexion.dameEntity(Operacion.class, iden);
                orden = iOperacion.getOrden();
                sm.put(orden, iOperacion);
            }

            it1 = sm.values().iterator();
            while (it1.hasNext()) {
                iOperacion = (Operacion) it1.next();
                operacionIntentada=false;
                existeOperacion=false;  // Sirve para detectar operaciones que han sido borradas antes de ser ejecutadas.

                // Se da por hecho que una Operacion sólo puede ser apuntada por
                //  una OperacionDeterminacion o una OperacionEntidad. Por lo tanto,
                //  aunque la clase Operacion contiene un Set de OperacionDeterminacion,
                //  sólo debe contener un elemento y, por consiguiente, solamente se
                //  toma el primero.

                // *******************************************
                // Si la operación es entre determinaciones...
                s="Select od.iden From planeamiento.operaciondeterminacion od " +
                        "Where od.idoperacion=" + iOperacion.getIden();
                it2=ClsConexion.recordList(s).iterator();
                if (it2.hasNext()) {
                    existeOperacion=true;
                    iOperacionDeterminacion = (Operaciondeterminacion) ClsConexion.dameEntity(Operaciondeterminacion.class, it2.next());
                    iden=ClsMain.idDeterminacionPorIdOperacionDeterminacion(iOperacionDeterminacion.getIden());
                    iDeterminacionO=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                    operar=false;
                    // Solo se opera si la determinacion operada pertenece al trámite operado.
                    //  La operadora puede o no pertenecer al trámite operador.
                    if (ClsMain.idTramitePorIdDeterminacion(iDeterminacionO.getIden())==iTramiteO.getIden()){
                        if(soloOperadores==1){
                            // Si soloOperadores=1, sólo se opera la iDeterminacionO si ésta, a su
                            //  vez, es operadora.
                            s="Select Count(*) From planeamiento.operaciondeterminacion od " +
                                    "Where od.iddeterminacionoperadora=" + iDeterminacionO.getIden();
                            if (Integer.parseInt(ClsConexion.recordList(s).get(0).toString())>0){
                                operar=true;
                            }
                        } else{
                            operar=true;
                        }

                        if(operar==true){
                            if(tipoModificacion!=1){
                                mLog.info("");
                                mLog.info("==> " + ClsDatos.gOrdenOperacion + "º --> Operaciones sobre determinaciones " + sOperadores[soloOperadores] + " del plan [" + codPlanO + "] ordenadas por el plan [" + codPlanR + "]");
                                ClsDatos.gOrdenOperacion=ClsDatos.gOrdenOperacion + 1;
                            }
                            tipoModificacion=1;
                            operacionIntentada=true;
                            if(soloMensajes==1){
                                // Recolección de basura
                                System.gc();
                                ClsOperacionDeterminacion.operacionesDeterminaciones(iOperacionDeterminacion);
                            }
                        }
                    }
                }

                // *************************************
                // Si la operación es entre entidades...
                s="Select oe.iden From planeamiento.operacionentidad oe " +
                        "Where oe.idoperacion=" + iOperacion.getIden();
                it2=ClsConexion.recordList(s).iterator();
                if (it2.hasNext()) {
                    existeOperacion=true;
                    iOperacionEntidad = (Operacionentidad) ClsConexion.dameEntity(Operacionentidad.class, it2.next());
                    idEntidadO=ClsMain.idEntidadPorIdOperacionEntidad(iOperacionEntidad.getIden());
                    iEntidadO=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidadO);
                    operar=false;
                    // Solo se opera si la entidad operada pertenece al trámite operado.
                    //  La operadora puede o no pertenecer al trámite operador.
                    if (ClsMain.idTramitePorIdEntidad(iEntidadO.getIden())==iTramiteO.getIden()){
                        if(soloOperadores==1){
                            // Si soloOperadores=1, sólo se opera la iEntidadO si ésta, a su
                            //  vez, es operadora.
                            s="Select Count(*) From planeamiento.operacionentidad oe " +
                                    "Where oe.identidadoperadora=" + iEntidadO.getIden();
                            if (Integer.parseInt(ClsConexion.recordList(s).get(0).toString())>0){
                                operar=true;
                            }
                        } else{
                            operar=true;
                        }

                        if(operar==true){
                            if(tipoModificacion!=2){
                                mLog.info("");
                                mLog.info("==> " + ClsDatos.gOrdenOperacion + "º --> Operaciones sobre entidades " + sOperadores[soloOperadores] + " del plan [" + codPlanO + "] ordenadas por el plan [" + codPlanR + "]");
                                ClsDatos.gOrdenOperacion=ClsDatos.gOrdenOperacion +1 ;
                            }
                            tipoModificacion=2;
                            operacionIntentada=true;
                            if(soloMensajes==1){
                                // Recolección de basura
                                System.gc();
                                ClsOperacionEntidad.OperacionesEntidades(iOperacionEntidad);
                            }
                        }
                    }
                }

                // *************************************
                // Si la operación es de relaciones...
                s="Select opr.iden From planeamiento.operacionrelacion opr " +
                        "Where opr.idoperacion=" + iOperacion.getIden();
                it2=ClsConexion.recordList(s).iterator();
                if (it2.hasNext()) {
                    existeOperacion=true;
                    iOperacionRelacion = (Operacionrelacion) ClsConexion.dameEntity(Operacionrelacion.class, it2.next());
                    idRelacion=ClsMain.idRelacionPorIdOperacionRelacion(iOperacionRelacion.getIden());
                    iRelacionO=(Relacion) ClsConexion.dameEntity(Relacion.class, idRelacion);
                    // Solo se opera si la relacion operada ha sido creada por el trámite operado.
                    idTramiteCreador=ClsMain.idTramiteCreadorPorIdRelacion(iRelacionO.getIden());
                    if (idTramiteCreador==iTramiteO.getIden()){
                        // Si soloOperadores=1, no se opera.
                        if(soloOperadores==0){
                            if(tipoModificacion!=3){
                                mLog.info("");
                                mLog.info("==> " + ClsDatos.gOrdenOperacion + "º --> Operaciones sobre relaciones " + sOperadores[soloOperadores] + " del plan [" + codPlanO + "] ordenadas por el plan [" + codPlanR + "]");
                                ClsDatos.gOrdenOperacion=ClsDatos.gOrdenOperacion + 1;
                            }
                            tipoModificacion=3;
                            operacionIntentada=true;
                            if(soloMensajes==1){
                                // Recolección de basura
                                System.gc();
                                ClsOperacionRelacion.operacionesRelaciones(iOperacionRelacion);
                            }
                        }
                    }
                }

                if(existeOperacion==false && soloMensajes==1){
                    // La operación ha sido borrada antes de ser ejecutada y ha quedado un
                    //  registro huérfano en la tabla Operacion. Probablemente, una operación
                    //  anterior haya eliminado alguno de los elementos que intervenían en ella.
                    mLog.warn("*** La operación iden=" + iOperacion.getIden() + " no se puede ejecutar. Alguno de sus elementos ha sido eliminado en una operación anterior.");
                    s="Delete From planeamiento.Operacion Where iden=" + iOperacion.getIden() + "";
                    ClsConexion.ejecutar(s);
                }
            }
        } catch (Exception e){
            mLog.error("Fallo en la operación opPla_Modificacion. " + e + ". Código 23301." );
        }

    }

    public static boolean opPla_Desarrollo(Tramite iTramiteO, Tramite iTramiteR, int soloMensajes) {
        // Un plan desarrollador NO desarrolla directamente a su plan padre, sino al
        //  plan raiz de su rama en el árbol de planes; es decir, aquel precedente que
        //  tiene idPadre nulo.

        // El procedimiento consiste recortar gráficamente todas las entidades del
        //  trámite operado que están dentro de la entidad idAA (entidad 'Ámbito de Aplicación' del
        //  trámite operador) y que pertenecen a alguno de los grupos de entidades contemplados
        //  en el trámite operador, y añadir éste al trámite operado.

        String s;
        int idPlan;
        int idPlanPadre;
        List lista;
        int iden;
        Iterator it;
        Tramite iTramite;
        Tramite jTramite;
        Tramite iTramiteRaiz;
        Entidad iEntidadO;
        Entidad iEntidadR;
        Entidad iEntidadAAr;
        Entidad iEntidadAAo;
        Determinacion iDetGrupoDeEntidadesO;
        Determinacion iDetGrupoDeEntidadesR;
        Determinacion iDeterminacion;
        Determinacion iDetPadre;
        Determinacion iDetGrupo;
        String codPlanO;
        String codPlanR;
        int idEntidadR;
        int idTramEntidadR;

        try{
            // Las condiciones para ejecutar el desarrollo son:
            //
            //  1 - El trámite operador ejecuta una operación de desarrollo sobre
            //          otro trámite que puede ser o no el tramite operado.
            //  2 - El trámite operado es el antecesor de nivel más alto en el
            //          árbol ( el que tiene idPadre nulo)

            //  Se averigua si el trámite operador ejecuta una operación
            //      de desarrollo, y contra qué trámite lo hace.
            lista=new ArrayList();
            s="Select tro.iden From planeamiento.Operacionplan op, " +
                    "planeamiento.Tramite tro, planeamiento.Tramite trr, " +
                    "diccionario.Instrumentotipooperacionplan itop " +
                    "Where trr.iden=" + iTramiteR.getIden() + " " +
                    "And tro.idplan=op.idplanoperado And trr.idplan=op.idplanoperador " +
                    "And op.idinstrumentotipooperacion=itop.iden " +
                    "And itop.idtipooperacionplan=" + ClsDatos.ID_TIPOOPERACIONPLAN_DESARROLLO + " ";
            lista=ClsConexion.recordList(s);
            if(lista.size()==0){
                // Si el trámite operador no ejecuta un desarrollo, no se hace nada.
                return false;
            }
            iden=Integer.parseInt(lista.get(0).toString());
            jTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, iden);
            
            // Averigua cuál es el iden del plan raiz, y su trámite
            idPlan=ClsMain.idPlanPorIdTramite(jTramite.getIden());
            idPlanPadre=0;
            iTramiteRaiz=null;
            do{
                lista=new ArrayList();
                s="Select idPadre From planeamiento.Plan Where iden=" + idPlan + " " +
                        "And idPadre Is Not Null ";
                lista=ClsConexion.recordList(s);
                if (lista.size()==1){
                    idPlanPadre=Integer.parseInt(lista.get(0).toString());
                    idPlan=idPlanPadre;
                } else {
                    idPlanPadre=0;
                    // Averigua cuál es el trámite correspondiente al idPlan que hay
                    //  que desarrollar.
                    it=ClsDatos.gListaIdTramitesRefundir.iterator();
                    while (it.hasNext()){
                        iden=Integer.parseInt(it.next().toString());
                        iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, iden);
                        if (ClsMain.idPlanPorIdTramite(iTramite.getIden())==idPlan){
                            iTramiteRaiz=iTramite;
                        }
                    }
                }
            }while(idPlanPadre>0);

            if (iTramiteRaiz==null){
                return false;
            }

            // Se ejecuta el desarrollo si el trámite raiz es el iTramiteO
            if (iTramiteO.getIden()!=iTramiteRaiz.getIden()){
                return false;
            }

            // Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            codPlanO=ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden());
            codPlanR=ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden());

            mLog.info("");
            mLog.info("==> " + ClsDatos.gOrdenOperacion + "º --> Desarrollo.");
            mLog.info("        Trámite operado: iden=" + iTramiteO.getIden() + ", fecha=" + iTramiteO.getFecha() + ", Plan [" + codPlanO + "]");
            mLog.info("        Trámite operador: iden=" + iTramiteR.getIden() + ", fecha=" + iTramiteR.getFecha() + ", Plan [" + codPlanR + "]");
            ClsDatos.gOrdenOperacion=ClsDatos.gOrdenOperacion +1 ;

            // Si el trámite operador está suspendido, no se ejecuta la operación
            if (ClsMain.bTramiteSuspendido(iTramiteR)==true){
                s="        ** SUSPENDIDO** El trámite operador está suspendido " + 
                        "y no se ejecuta el desarrollo del plan [" + 
                        ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden()) + "] sobre " +
                        "el plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden()) + "].";
                mLog.info(s);
                ClsMain.copiarAmbitoAplicacion(iTramiteO, iTramiteR);
                return false;
            }

            if(soloMensajes==0){
                return true;
            }

            // Averigua cuál es la entidad 'Ámbito de Aplicación' del trámite operador.
            iEntidadAAr=ClsMain.ambitoAplicacionPorTramite(iTramiteR);
            if (iEntidadAAr==null){
                mLog.warn("*** Fallo en la operación opPla_Desarrollo. " +
                        "No se ha encontrado la entidad 'Ámbito de Aplicación' " +
                        "del trámite iden=" + iTramiteR.getIden() + ". ");
                // Aunque no se ejecuta el desarrollo, se devuelve TRUE para que se ejecuten
                //  sus modificaciones, si las tiene
                return true;
            }

            // Averigua cuál es la entidad 'Ámbito de Aplicación' del trámite operado.
            iEntidadAAo=ClsMain.ambitoAplicacionPorTramite(iTramiteO);
            if (iEntidadAAo==null){
                mLog.warn("*** Fallo en la operación opPla_Desarrollo. " +
                        "No se ha encontrado la entidad 'Ámbito de Aplicación' " +
                        "del trámite iden=" + iTramiteO.getIden() + ". ");
                // Aunque no se ejecuta el desarrollo, se devuelve TRUE para que se ejecuten
                //  sus modificaciones, si las tiene
                return true;
            }

            // Averigua cuál es la determinación 'Grupo de Entidades' de los
            //  trámites operador y operado.
            iDetGrupoDeEntidadesR=ClsMain.determinacionGrupoDeEntidadesPorTramite(iTramiteR);
            iDetGrupoDeEntidadesO=ClsMain.determinacionGrupoDeEntidadesPorTramite(iTramiteO);

            // Selecciona las entidades del trámite operado que pertenecen a alguno de
            //  los grupos de las entidades del trámite operador.
            lista=new ArrayList();
            s="Select Distinct e1.iden From planeamiento.Entidad e1, planeamiento.Entidad e2, " +
                    "planeamiento.Entidaddeterminacion ed1, planeamiento.Entidaddeterminacion ed2, " +
                    "planeamiento.Casoentidaddeterminacion ced1, planeamiento.Casoentidaddeterminacion ced2, " +
                    "planeamiento.Entidaddeterminacionregimen edr1, planeamiento.Entidaddeterminacionregimen edr2, " +
                    "planeamiento.Opciondeterminacion od1, planeamiento.Opciondeterminacion od2, " +
                    "planeamiento.Determinacion dvr1, planeamiento.Determinacion dvr2 " +
                    "Where ed1.identidad=e1.iden And ed2.identidad=e2.iden " +
                    "And ed1.iddeterminacion=" + iDetGrupoDeEntidadesO.getIden() + " " +
                    "And ed2.iddeterminacion=" + iDetGrupoDeEntidadesR.getIden() + " " +
                    "And ced1.identidaddeterminacion=ed1.iden " +
                    "And ced2.identidaddeterminacion=ed2.iden " +
                    "And (edr1.idcaso=ced1.iden Or edr1.idcasoaplicacion=ced1.iden) " +
                    "And (edr2.idcaso=ced2.iden Or edr2.idcasoaplicacion=ced2.iden) " +
                    "And edr1.idopciondeterminacion=od1.iden " +
                    "And edr2.idopciondeterminacion=od2.iden " +
                    "And od1.iddeterminacionvalorref=dvr1.iden " +
                    "And od2.iddeterminacionvalorref=dvr2.iden " +
                    "And dvr1.iddeterminacionbase=dvr2.iddeterminacionbase " +
                    "And e1.idtramite=" + iTramiteO.getIden() + " " +
                    "And e2.idtramite=" + iTramiteR.getIden() + " " +
                    "And e1.iden<>" + iEntidadAAo.getIden() + " " +
                    "And e1.iden In(Select identidad From planeamiento.Entidadpol) ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                iEntidadO=(Entidad) ClsConexion.dameEntity(Entidad.class, it.next());
                iDetGrupo=ClsMain.determinacionGrupoPorEntidad(iEntidadO);
                s="        Recortando [" + iEntidadO.getCodigo() + "] (" + 
                    iDetGrupo.getNombre() + ") del plan [" +
                    ClsMain.codigoPlanPorIdEntidad(iEntidadO.getIden()) + "]";
                mLog.info(s);
                // Efectúa la sustracción gráfica.
                ClsOperacionEntidad.opEnt_SustraccionGrafica(iEntidadO, iEntidadAAr, iTramiteR.getIden());
            }

            // Añade las entidades del trámite operador al trámite operado.
            mLog.info("        Copia de entidades desde [" + codPlanR + "] a [" + codPlanO +"]");
            lista=new ArrayList();
            s="Select Distinct iden From planeamiento.Entidad Where idtramite=" + iTramiteR.getIden() + " " +
                    "And iden<>" + iEntidadAAr.getIden();
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                idEntidadR=Integer.parseInt(it.next().toString());
                iEntidadR=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidadR);
                idTramEntidadR=iEntidadR.getTramite().getIden();
                iDetGrupo=ClsMain.determinacionGrupoPorEntidad(iEntidadR);
                s="        Aportando  [" + iEntidadR.getCodigo() + "] (" + 
                    iDetGrupo.getNombre() + ") del plan [" +
                    ClsMain.codigoPlanPorIdEntidad(iEntidadR.getIden()) + "]";
                mLog.info(s);

                // Efectúa la aportación de entidad.
                ClsOperacionEntidad.opEnt_AportacionEntidad(iEntidadAAo, iEntidadR);
            }

            // Añadir también las determinaciones, excepto virtual, grupo de 
            //  entidades (y los grupos que son sus valores de referencia)
            mLog.info("        Copia de determinaciones desde [" + codPlanR + "] a [" + codPlanO +"]");
            iDetPadre = ClsOperacionDeterminacion.crearCarpetaDeterminacionesAportadas(iTramiteO.getIden(), iTramiteR.getIden());
            lista=new ArrayList();
            s="Select Distinct iden From planeamiento.Determinacion Where idtramite=" + iTramiteR.getIden() + " " +
                "And idCaracter<>" + ClsDatos.ID_CARACTER_VIRTUAL + " " +
                "And idCaracter<>" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " " +
                "And iden Not In (Select OD.idDeterminacionValorRef From planeamiento.OpcionDeterminacion OD, " + 
                "planeamiento.Determinacion D Where OD.idDeterminacion=D.iden "+ 
                "And D.idCaracter=" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + ") ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                iDeterminacion=(Determinacion) ClsConexion.dameEntity(Determinacion.class, it.next());

                // Reasignar el IdTramite. Se comprueba de nuevo si su trámite es iTramiteR, por si acaso ya
                //  ha sido cambiada de trámite dentro de bReasignarDeterminacion, por ser hija de una de la
                //  lista que ya ha sido procesada.
                if (ClsMain.idTramitePorIdDeterminacion(iDeterminacion.getIden())==iTramiteR.getIden()){
                    s="        Aportando  [" + iDeterminacion.getCodigo() + "] (" + 
                        iDeterminacion.getNombre() + ") del plan [" +
                        ClsMain.codigoPlanPorIdDeterminacion(iDeterminacion.getIden()) + "]";
                    mLog.info(s);
                    ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetPadre, iDeterminacion, 0);
                }
            }

        }catch(Exception e){
            mLog.error("Fallo en la operación opPla_Desarrollo. " + e + ". Código 23302." );
            return false;
        }

        return true;

    }

    public static void opPla_SuspensionTotal(Tramite iTramiteO, Tramite iTramiteR, int soloMensajes) {
        // No se tiene en cuenta el Ámbito de Aplicación del trámite suspendedor iTramiteR para capturar
        //  las entidades del trámite que se va a suspender. De éste, se seleccionan todas las entidades.

        String s;
        int nRegistros;
        int n;
        String codPlanO;
        String codPlanR;

        try{
            // Averigua si hay una operación de Suspensión entre ambos trámites
            s="Select Count(*) From planeamiento.Operacionplan op, " +
                    "planeamiento.Tramite tro, planeamiento.Tramite trr, " +
                    "diccionario.Instrumentotipooperacionplan itop " +
                    "Where tro.iden=" + iTramiteO.getIden() + " " +
                    "And trr.iden=" + iTramiteR.getIden() + " " +
                    "And tro.idplan=op.idplanoperado And trr.idplan=op.idplanoperador " +
                    "And op.idinstrumentotipooperacion=itop.iden " +
                    "And itop.idtipooperacionplan=" + ClsDatos.ID_TIPOOPERACIONPLAN_SUSPENSIONTOTAL + " ";
            n=Integer.parseInt(ClsConexion.recordList(s).get(0).toString());
            if(n==0){
                return;
            }

            // Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            codPlanO=ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden());
            codPlanR=ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden());

            mLog.info("");
            mLog.info("==> " + ClsDatos.gOrdenOperacion + "º --> Suspensión Total.");
            mLog.info("        Trámite operado: iden=" + iTramiteO.getIden() + ", fecha=" + iTramiteO.getFecha() + ", Plan [" + codPlanO + "]");
            mLog.info("        Trámite operador: iden=" + iTramiteR.getIden() + ", fecha=" + iTramiteR.getFecha() + ", Plan [" + codPlanR + "]");
            ClsDatos.gOrdenOperacion=ClsDatos.gOrdenOperacion +1 ;

            if(soloMensajes==0){
                return;
            }

            // Si el trámite operador está suspendido, no se ejecuta la operación
            if (ClsMain.bTramiteSuspendido(iTramiteR)==true){
                s="        ** SUSPENDIDO** El trámite operador está suspendido " + 
                        "y no se ejecuta la suspensión total del plan [" + 
                        ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden()) + "] sobre " +
                        "el plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden()) + "].";
                mLog.info(s);
                ClsMain.copiarAmbitoAplicacion(iTramiteO, iTramiteR);
                return;
            }

            // Pone la marca de bSuspendido en el
            s="Update planeamiento.Plan Set bsuspendido=true " +
                    "Where iden=" + ClsMain.idPlanPorIdTramite(iTramiteO.getIden()) + " ";
            nRegistros=ClsConexion.ejecutar(s);
            if (nRegistros>0){
                mLog.info("         Se ha suspendido el plan del trámite iden=" + iTramiteO.getIden());
            } else {
                mLog.warn("***      No se ha suspendido el plan del trámite iden=" + iTramiteO.getIden());
            }

        }catch(Exception e){
            mLog.error("Fallo en la operación opPla_SuspensionTotal. " + e + ". Código 23303." );
        }

    }
    
    public static void opPla_RevocacionTotal(Tramite iTramiteO, Tramite iTramiteR, int soloMensajes) {
        // No se tiene en cuenta el Ámbito de Aplicación del trámite suspendedor iTramiteR para capturar
        //  las entidades del trámite que se va a suspender. De éste, se seleccionan todas las entidades.

        String s;
        int nRegistros;
        int n;
        String codPlanO;
        String codPlanR;

        try{
            // Averigua si hay una operación de Revocación entre ambos trámites
            s="Select Count(*) From planeamiento.Operacionplan op, " +
                    "planeamiento.Tramite tro, planeamiento.Tramite trr, " +
                    "diccionario.Instrumentotipooperacionplan itop " +
                    "Where tro.iden=" + iTramiteO.getIden() + " " +
                    "And trr.iden=" + iTramiteR.getIden() + " " +
                    "And tro.idplan=op.idplanoperado And trr.idplan=op.idplanoperador " +
                    "And op.idinstrumentotipooperacion=itop.iden " +
                    "And itop.idtipooperacionplan=" + ClsDatos.ID_TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL + " ";
            n=Integer.parseInt(ClsConexion.recordList(s).get(0).toString());
            if(n==0){
                return;
            }

            // Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            codPlanO=ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden());
            codPlanR=ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden());

            mLog.info("");
            mLog.info("==> " + ClsDatos.gOrdenOperacion + "º --> Revocación Total.");
            mLog.info("        Trámite operado: iden=" + iTramiteO.getIden() + ", fecha=" + iTramiteO.getFecha() + ", Plan [" + codPlanO + "]");
            mLog.info("        Trámite operador: iden=" + iTramiteR.getIden() + ", fecha=" + iTramiteR.getFecha() + ", Plan [" + codPlanR + "]");
            ClsDatos.gOrdenOperacion=ClsDatos.gOrdenOperacion +1 ;

            if(soloMensajes==0){
                return;
            }

            // Si el trámite operador está suspendido, no se ejecuta la operación
            if (ClsMain.bTramiteSuspendido(iTramiteR)==true){
                s="        ** SUSPENDIDO** El trámite operador está suspendido " + 
                        "y no se ejecuta la suspensión total del plan [" + 
                        ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden()) + "] sobre " +
                        "el plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden()) + "].";
                mLog.info(s);
                ClsMain.copiarAmbitoAplicacion(iTramiteO, iTramiteR);
                return;
            }

            // Quita la marca de bSuspendido en el
            s="Update planeamiento.Plan Set bsuspendido=false " +
                    "Where iden=" + ClsMain.idPlanPorIdTramite(iTramiteO.getIden()) + " ";
            nRegistros=ClsConexion.ejecutar(s);
            if (nRegistros>0){
                mLog.info("         Se ha revocado la suspensión del plan del trámite iden=" + iTramiteO.getIden());
            } else {
                mLog.warn("***      No se ha revocado la suspensión del plan del trámite iden=" + iTramiteO.getIden());
            }

        }catch(Exception e){
            mLog.error("Fallo en la operación opPla_RevocacionTotal. " + e + ". Código 23306." );
        }

    }

    public static void opPla_Sustitucion(Tramite iTramiteO, Tramite iTramiteR, int soloMensajes) {
        String s;
        Plan iPlanO;
        Plan iPlanR;
        List lista1;
        List lista2;
        Entidad iEntO;
        Entidad iEntR;
        int iden1;
        int iden2;
        Iterator it1;
        Iterator it2;
        String tipoGeom;
        Determinacion iDetGrupoO;
        Determinacion iDetGrupoR;

        try{
            // Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            iden1=ClsMain.idPlanPorIdTramite(iTramiteO.getIden());
            if (iden1==0){
                // Corresponde a un plan eliminado
                return;
            }
            iPlanO=(Plan) ClsConexion.dameEntity(Plan.class, iden1);
            iden2=ClsMain.idPlanPorIdTramite(iTramiteR.getIden());
            if (iden2==0){
                // Corresponde a un plan eliminado
                return;
            }
            iPlanR=(Plan) ClsConexion.dameEntity(Plan.class, iden2);

            // Averigua si el plan del trámite iTramiteR sustituye al del iTramiteO
            lista1=new ArrayList();
            s="Select op.iden From planeamiento.Operacionplan op, diccionario.Instrumentotipooperacionplan ito " +
                    "Where op.idinstrumentotipooperacion=ito.iden " +
                    "And ito.idtipooperacionplan=" + ClsDatos.ID_TIPOOPERACIONPLAN_SUSTITUCION + " " +
                    "And op.idplanoperado=" + iPlanO.getIden() + " " +
                    "And op.idplanoperador=" + iPlanR.getIden() + " ";
            lista1=ClsConexion.recordList(s);
            if (lista1.size()==0){
                return;
            }

            mLog.info("");
            mLog.info("==> " + ClsDatos.gOrdenOperacion + "º --> Sustitución.");
            mLog.info("        Trámite operado: iden=" + iTramiteO.getIden() + ", fecha=" + iTramiteO.getFecha() + ", Plan [" + iPlanO.getCodigo() + "] ");
            mLog.info("        Trámite operador: iden=" + iTramiteR.getIden() + ", fecha=" + iTramiteR.getFecha() + ", Plan [" + iPlanR.getCodigo() + "] ");
            ClsDatos.gOrdenOperacion=ClsDatos.gOrdenOperacion + 1;

            if(soloMensajes==0){
                return;
            }

            // Si el trámite operador está suspendido, no se ejecuta la operación
            if (ClsMain.bTramiteSuspendido(iTramiteR)==true){
                s="        ** SUSPENDIDO** El trámite operador está suspendido " + 
                        "y no se ejecuta la sustitución del plan [" + 
                        ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden()) + "] sobre " +
                        "el plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden()) + "].";
                mLog.info(s);
                ClsMain.copiarAmbitoAplicacion(iTramiteO, iTramiteR);
                return;
            }

            // Si el trámite sustituto tiene algunas entidades suspendidas, hay que incorporar del
            //  trámite sustituído, las entidades que caen debajo de aquellas y que son
            //  del mismo grupo de entidades. De esta manera, quedará vigente todo el trámite
            //  sustituto, excepto en aquellos sitios donde esté suspendido, donde seguirá
            //  vigente el contenido del trámite sustituído.
            lista1=new ArrayList();
            s="Select e.iden From planeamiento.Entidad e Where e.idtramite=" + iTramiteR.getIden() + " " +
                    "And e.bsuspendida=true ";
            lista1=ClsConexion.recordList(s);
            if (lista1.size()>0){
                mLog.info("El trámite sustituto tiene entidades suspendidas. Se dejarán vigentes las correspondientes del trámite sustituído.");
                it1=lista1.iterator();
                while (it1.hasNext()){
                    iden1=Integer.parseInt(it1.next().toString());
                    iEntO=(Entidad) ClsConexion.dameEntity(Entidad.class, iden1);
                    tipoGeom=ClsOperacionEntidad.tieneGeometria(iEntO);
                    iDetGrupoO=ClsMain.determinacionGrupoPorEntidad(iEntO);
                    if (!tipoGeom.equals("") && !tipoGeom.equalsIgnoreCase("Error")){
                        // Averigua qué entidades de iTramiteR intersectan con la entidad suspendida
                        lista2=new ArrayList();
                        s="Select e2.iden From planeamiento.Entidad e2 " +
                                "planeamiento." + tipoGeom + " eg1, " +
                                "planeamiento." + tipoGeom + " eg2 " +
                                "Where eg2.identidad=e2.iden " +
                                "And eg1.identidad=" + iEntO.getIden() + " " +
                                "And e2.idtramite=" + iTramiteR.getIden() + " " +
                                "And Intersects(eg1.geom, eg2.geom)=true ";
                        lista2=ClsConexion.recordList(s);
                        it2=lista1.iterator();
                        while (it2.hasNext()){
                            // Las entidades de iTramiteR que intersectan con las
                            //  entidades suspendidas de iTramiteO, y que son del
                            //  mismo grupo que ellas, se incorporan
                            //  al iTramiteO antes de hacer la sustitución de plan.
                            iden2=Integer.parseInt(it2.next().toString());
                            iEntR=(Entidad) ClsConexion.dameEntity(Entidad.class, iden2);
                            iDetGrupoR=ClsMain.determinacionGrupoEquivalentePorEntidad(iEntR, iTramiteO);
                            if(iDetGrupoO.getIden()==iDetGrupoR.getIden()){
                                mLog.info("La entidad [" + iEntO.getCodigo() + "] del plan [" + iPlanO.getCodigo() + "] está suspendida, y queda vigente la entidad [" + iEntR.getCodigo() + "] del plan [" + iPlanR.getCodigo() + "]");
                                ClsOperacionEntidad.opEnt_IncorporacionEntidad(iEntO, iEntR, false);
                            }
                        }
                    }
                }
            }

            mLog.info("         Eliminando Plan [" + iPlanO.getCodigo()+ "]");
            
            // TODO FGA
            eliminarPlanesEncargados(iPlanO.getIden());
            eliminarPlanesVigentes(iPlanO.getIden());
            eliminarPlanesPrerefundidos(iPlanO.getIden());
            
            ClsMain.eliminarObjeto("Plan", iPlanO.getIden(), iTramiteR.getIden());
            
        } catch(Exception e){
            mLog.error("Fallo en la operación opPla_Sustitucion. " + e + ". Código 23304." );
        }

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
    	
    	// Elimino ahora el resto de registros que tienen asociado planesvigente
    	String sComprobacion2="select pe.id from gestionfip.planesencargados pe, planeamiento.plan plan, planeamiento.tramite tram "+
    	" where tram.idplan = plan.iden and tram.iden = pe.tramitevigente_iden and plan.iden =" + iden;
    	
    	int nRegistrosComprobacion2=0;
    	List regComprobacion2=ClsConexion.recordList(sComprobacion2);
    	
    	nRegistrosComprobacion2 = regComprobacion2.size();
    	
    	if (nRegistrosComprobacion2!=0)
    	{
    		// Borro el registro
    		mLog.info("[eliminarPlanesEncargados] Borro el registro sComprobacion2");
    		String s2="Delete From gestionfip.planesencargados Where id in " +
    				" ( " +sComprobacion2+
    				" ) ";
    		nRegistros+=ClsConexion.ejecutar(s2);
    	}
    	else
    	{
    		mLog.info("[eliminarPlanesEncargados] No existe ese iden como plan encargado");
    	}
    	 
    	
    	return nRegistros;
    }
    
    public static int eliminarPlanesPrerefundidos(int iden)
    {
    	int nRegistros=0;
    	mLog.info("[eliminarPlanesPrerefundidos] iden"+iden );
    	
    	
    	
    	// Elimino ahora el resto de registros que tienen asociado planesvigente
    	String sComprobacion2="select pe.id from gestionfip.prerefundido pe, planeamiento.plan plan, planeamiento.tramite tram "+
    	" where tram.idplan = plan.iden and tram.iden = pe.idtramitevigente" +
    	" and plan.iden =" + iden;
    	
    	int nRegistrosComprobacion2=0;
    	List regComprobacion2=ClsConexion.recordList(sComprobacion2);
    	
    	nRegistrosComprobacion2 = regComprobacion2.size();
    	
    	if (nRegistrosComprobacion2!=0)
    	{
    		// Borro el registro
    		mLog.info("[eliminarPlanesPrerefundidos] Borro el registro sComprobacion2");
    		String s2="Delete From gestionfip.prerefundido Where id in " +
    				" ( " +sComprobacion2+
    				" ) ";
    		nRegistros+=ClsConexion.ejecutar(s2);
    	}
    	else
    	{
    		mLog.info("[eliminarPlanesPrerefundidos] No existe ese iden como plan prerefundido");
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

    public static void opPla_SuspensionParcial(Tramite iTramiteO, Tramite iTramiteR, int soloMensajes) {
        String s;
        int nRegistros;
        int n;
        String codPlanO;
        String codPlanR;
        Entidad iEntR; // Entidad Ámbito de aplicación del plan que suspende
        Entidad iEntO; // Entidades del plan que se van a suspender parcialmente
        List lista;
        Iterator it;
        int idEntidadO;

        try{
            // Averigua si hay una operación de Suspensión Parcial entre ambos trámites
            s="Select Count(*) From planeamiento.Operacionplan op, " +
                    "planeamiento.Tramite tro, planeamiento.Tramite trr, " +
                    "diccionario.Instrumentotipooperacionplan itop " +
                    "Where tro.iden=" + iTramiteO.getIden() + " " +
                    "And trr.iden=" + iTramiteR.getIden() + " " +
                    "And tro.idplan=op.idplanoperado And trr.idplan=op.idplanoperador " +
                    "And op.idinstrumentotipooperacion=itop.iden " +
                    "And itop.idtipooperacionplan=" + ClsDatos.ID_TIPOOPERACIONPLAN_SUSPENSIONPARCIAL + " ";
            n=Integer.parseInt(ClsConexion.recordList(s).get(0).toString());
            if(n==0){
                return;
            }

            // Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            codPlanO=ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden());
            codPlanR=ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden());
            
            mLog.info("");
            mLog.info("==> " + ClsDatos.gOrdenOperacion + "º --> Suspensión Parcial.");
            mLog.info("        Trámite operado: iden=" + iTramiteO.getIden() + ", fecha=" + iTramiteO.getFecha() + ", Plan [" + codPlanO + "]");
            mLog.info("        Trámite operador: iden=" + iTramiteR.getIden() + ", fecha=" + iTramiteR.getFecha() + ", Plan [" + codPlanR + "]");
            ClsDatos.gOrdenOperacion=ClsDatos.gOrdenOperacion +1 ;

            if(soloMensajes==0){
                return;
            }

            // Si el trámite operador está suspendido, no se ejecuta la operación
            if (ClsMain.bTramiteSuspendido(iTramiteR)==true){
                s="        ** SUSPENDIDO** El trámite operador está suspendido " + 
                        "y no se ejecuta la suspensión parcial del plan [" + 
                        ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden()) + "] sobre " +
                        "el plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden()) + "].";
                mLog.info("        ** SUSPENDIDO** El trámite operador está suspendido y no se ejecuta la suspensión.");
                ClsMain.copiarAmbitoAplicacion(iTramiteO, iTramiteR);
                return;
            }
            
            // Se suspenden parcialmente todas las entidades del tramite
            //  operado mediante el ámbito de aplicación del trámite operador.
            iEntR=ClsMain.ambitoAplicacionPorTramite(iTramiteR);
            if (iEntR!=null){
                s="Select E.iden From planeamiento.Entidad E " +
                    "Where E.idtramite=" + iTramiteO + " ";
                lista=new ArrayList();
                lista=ClsConexion.recordList(s);
                it=lista.iterator();
                while (it.hasNext()){
                    idEntidadO=Integer.parseInt(it.next().toString());
                    iEntO=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidadO);
                    ClsOperacionEntidad.opEnt_SuspensionParcial(iEntO, iEntR);
                }
                s="         Se han suspendido parcialmente las entidades del " + 
                    "plan " + ClsMain.nombrePlanPorIdTramite(iTramiteO.getIden());
                mLog.info(s);
            } else {
                s="         No se ha suspendido parcialmente el " + 
                    "plan <" + ClsMain.nombrePlanPorIdTramite(iTramiteO.getIden())+
                    "> porque no se ha encontrado el ámbito de aplicación del " +
                    "plan operador <" + 
                    ClsMain.nombrePlanPorIdTramite(iTramiteR.getIden()) + ">";
                mLog.warn(s);
            }
            
        }catch(Exception e){
            mLog.error("Fallo en la operación opPla_SuspensionParcial. " + e + ". Código 23303." );
        }
    }
    
    public static void opPla_RevocacionParcial(Tramite iTramiteO, Tramite iTramiteR, int soloMensajes) {
        String s;
        int nRegistros;
        int n;
        String codPlanO;
        String codPlanR;
        Entidad iEntR; // Entidad Ámbito de aplicación del plan que revoca la suspensión
        Entidad iEntO; // Entidades del plan que se van a revocar parcialmente
        List lista;
        Iterator it;
        int idEntidadO;

        try{
            // Averigua si hay una operación de Revocación Parcial entre ambos trámites
            s="Select Count(*) From planeamiento.Operacionplan op, " +
                    "planeamiento.Tramite tro, planeamiento.Tramite trr, " +
                    "diccionario.Instrumentotipooperacionplan itop " +
                    "Where tro.iden=" + iTramiteO.getIden() + " " +
                    "And trr.iden=" + iTramiteR.getIden() + " " +
                    "And tro.idplan=op.idplanoperado And trr.idplan=op.idplanoperador " +
                    "And op.idinstrumentotipooperacion=itop.iden " +
                    "And itop.idtipooperacionplan=" + ClsDatos.ID_TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL + " ";
            n=Integer.parseInt(ClsConexion.recordList(s).get(0).toString());
            if(n==0){
                return;
            }

            // Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            codPlanO=ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden());
            codPlanR=ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden());
            
            mLog.info("");
            mLog.info("==> " + ClsDatos.gOrdenOperacion + "º --> Revocación Parcial.");
            mLog.info("        Trámite operado: iden=" + iTramiteO.getIden() + ", fecha=" + iTramiteO.getFecha() + ", Plan [" + codPlanO + "]");
            mLog.info("        Trámite operador: iden=" + iTramiteR.getIden() + ", fecha=" + iTramiteR.getFecha() + ", Plan [" + codPlanR + "]");
            ClsDatos.gOrdenOperacion=ClsDatos.gOrdenOperacion +1 ;

            if(soloMensajes==0){
                return;
            }

            // Si el trámite operador está suspendido, no se ejecuta la operación
            if (ClsMain.bTramiteSuspendido(iTramiteR)==true){
                s="        ** SUSPENDIDO** El trámite operador está suspendido " + 
                        "y no se ejecuta la suspensión parcial del plan [" + 
                        ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden()) + "] sobre " +
                        "el plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden()) + "].";
                mLog.info("        ** SUSPENDIDO** El trámite operador está suspendido y no se ejecuta la revocación.");
                ClsMain.copiarAmbitoAplicacion(iTramiteO, iTramiteR);
                return;
            }
            
            // Se revoca parcialmente la suspensión de todas las entidades del tramite
            //  operado mediante el ámbito de aplicación del trámite operador.
            iEntR=ClsMain.ambitoAplicacionPorTramite(iTramiteR);
            if (iEntR!=null){
                s="Select E.iden From planeamiento.Entidad E " +
                    "Where E.idtramite=" + iTramiteO + " ";
                lista=new ArrayList();
                lista=ClsConexion.recordList(s);
                it=lista.iterator();
                while (it.hasNext()){
                    idEntidadO=Integer.parseInt(it.next().toString());
                    iEntO=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidadO);
                    ClsOperacionEntidad.opEnt_LevantamientoParcialSuspension(iEntO, iEntR);
                }
                s="         Se ha revocado parcialmente la suspensión de las entidades del " + 
                    "plan " + ClsMain.nombrePlanPorIdTramite(iTramiteO.getIden());
                mLog.info(s);
            } else {
                s="         No se ha revocado parcialmente la suspensión del " + 
                    "plan <" + ClsMain.nombrePlanPorIdTramite(iTramiteO.getIden())+
                    "> porque no se ha encontrado el ámbito de aplicación del " +
                    "plan operador <" + 
                    ClsMain.nombrePlanPorIdTramite(iTramiteR.getIden()) + ">";
                mLog.warn(s);
            }
            
        }catch(Exception e){
            mLog.error("Fallo en la operación opPla_RevocacionParcial. " + e + ". Código 23307." );
        }
    }
    
    public static void opPla_Incorporacion(Tramite iTramiteO, Tramite iTramiteR, int soloMensajes) {
        String s;
        int n;
        String codPlanO;
        String codPlanR;
        Entidad iEntR; // Entidad Ámbito de aplicación del plan que incorpora
        Entidad iEntO; // Entidades del plan que se van a incorporar
        Entidad iEntidadAAo; // Entidad AA del plan que se va a incorporar (el plan operado)
        List lista;
        Iterator it;
        int idEntidadO;
        int idEntidadR;
        int nTotalIncorporar;
        int nIncorporadas=0;

        try{
            // Averigua si hay una operación de Incorporacion entre ambos trámites
            s="Select Count(*) From planeamiento.Operacionplan op, " +
                    "planeamiento.Tramite tro, planeamiento.Tramite trr, " +
                    "diccionario.Instrumentotipooperacionplan itop " +
                    "Where tro.iden=" + iTramiteO.getIden() + " " +
                    "And trr.iden=" + iTramiteR.getIden() + " " +
                    "And tro.idplan=op.idplanoperado And trr.idplan=op.idplanoperador " +
                    "And op.idinstrumentotipooperacion=itop.iden " +
                    "And itop.idtipooperacionplan=" + ClsDatos.ID_TIPOOPERACIONPLAN_INCORPORACION + " ";
            n=Integer.parseInt(ClsConexion.recordList(s).get(0).toString());
            if(n==0){
                return;
            }
            
            
            mLog.warn("**********************");
            mLog.warn("Aviso: la operación 'Incorporación de Plan' está obsoleta desde la versión 1.9.55 del proceso de refundido.");
            mLog.warn("**********************");
            if (1==1){
                return;
            }
            
            
            // Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            codPlanO=ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden());
            codPlanR=ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden());

            mLog.info("");
            mLog.info("==> " + ClsDatos.gOrdenOperacion + "º --> Incorporacion de Plan.");
            mLog.info("        Trámite incorporado : iden=" + iTramiteO.getIden() + ", fecha=" + iTramiteO.getFecha() + ", Plan [" + codPlanO + "]");
            mLog.info("        Trámite incorporador: iden=" + iTramiteR.getIden() + ", fecha=" + iTramiteR.getFecha() + ", Plan [" + codPlanR + "]");
            mLog.info("");
            ClsDatos.gOrdenOperacion=ClsDatos.gOrdenOperacion +1 ;

            if(soloMensajes==0){
                return;
            }

            // Si el trámite operador está suspendido, no se ejecuta la operación
            if (ClsMain.bTramiteSuspendido(iTramiteR)==true){
                s="        ** SUSPENDIDO** El trámite operador está suspendido " + 
                        "y no se ejecuta la incorporación del plan [" + 
                        ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden()) + "] en " +
                        "el plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden()) + "].";
                mLog.info("        ** SUSPENDIDO** El trámite operador está suspendido y no se ejecuta la incorporación.");
                return;
            }
            
            // Antes de efectuar la incorporación, se recortan, mediante el ámbito de aplicación del
            //  plan operado, las entidades del operador que pertenecen a alguno de los
            //  grupos de entidad del operado.
            // Averigua cuál es la entidad 'Ámbito de Aplicación' del trámite operador.
            iEntidadAAo=ClsMain.ambitoAplicacionPorTramite(iTramiteO);
            if (iEntidadAAo==null){
                s="         No se ha incorporado el " + 
                    "plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden())+
                    "] porque no se ha encontrado su ámbito de aplicación. ";
                mLog.warn(s);
                return;
            }
            
            // Consulta para seleccionar las entidades del trámite operador que deben
            //  ser recortadas. 1=operado, 2=operador
            s="Select Distinct E2.iden From planeamiento.Entidad E1, planeamiento.Determinacion D1, " + 
                    "planeamiento.EntidadDeterminacion ED1, planeamiento.CasoEntidadDeterminacion CED1, " + 
                    "planeamiento.EntidadDeterminacionRegimen EDR1, planeamiento.Determinacion DVR1, " +
                    "planeamiento.OpcionDeterminacion OD1, planeamiento.Entidad E2, " + 
                    "planeamiento.Determinacion D2, planeamiento.EntidadDeterminacion ED2, " +
                    "planeamiento.CasoEntidadDeterminacion CED2, " + 
                    "planeamiento.EntidadDeterminacionRegimen EDR2, planeamiento.Determinacion DVR2, " +
                    "planeamiento.OpcionDeterminacion OD2 " +
                    "Where E1.idTramite=" + iTramiteO.getIden() + " " +
                    "And ED1.idEntidad=E1.iden And ED1.idDeterminacion=D1.iden " +
                    "And CED1.idEntidadDeterminacion=ED1.iden " +
                    "And (EDR1.idCaso=CED1.iden Or EDR1.idCasoAplicacion=CED1.iden) " +
                    "And EDR1.idOpcionDeterminacion=OD1.iden " +
                    "And OD1.idDeterminacion=D1.iden " +
                    "And D1.idCaracter=" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " " +
                    "And OD1.idDeterminacionValorRef=DVR1.iden " +
                    "And E2.idTramite=" + iTramiteR.getIden() + " " +
                    "And ED2.idEntidad=E2.iden And ED2.idDeterminacion=D2.iden " +
                    "And CED2.idEntidadDeterminacion=ED2.iden " +
                    "And (EDR2.idCaso=CED2.iden Or EDR2.idCasoAplicacion=CED2.iden) " +
                    "And EDR2.idOpcionDeterminacion=OD2.iden " +
                    "And OD2.idDeterminacion=D2.iden " +
                    "And D2.idCaracter=" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " " +
                    "And OD2.idDeterminacionValorRef=DVR2.iden " +
                    "And DVR1.codigo=DVR2.codigo " +
                    "And (E2.iden In (Select idEntidad From planeamiento.EntidadPol) " + 
                    "Or E2.iden In (Select idEntidad From planeamiento.EntidadLin) " + 
                    "Or E2.iden In (Select idEntidad From planeamiento.EntidadPnt))";
            lista=new ArrayList();
            lista=ClsConexion.recordList(s);
            s="        Se van a recortar " + lista.size() + " entidades del " + 
                    "plan [" + codPlanR + "] " +
                    "con el Ámbito de Aplicación [" + iEntidadAAo.getCodigo() + "] " +
                    "del plan [" + codPlanO + "].";
            mLog.info(s);
            it=lista.iterator();
            while (it.hasNext()){
                idEntidadR=Integer.parseInt(it.next().toString());
                iEntR=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidadR);
                s="            Recortando [" + iEntR.getCodigo() + "]... ";
                mLog.info(s);
                ClsOperacionEntidad.opEnt_SustraccionGrafica(iEntR, iEntidadAAo, iTramiteR.getIden());
            }   
            
            // Se incorporan todas las entidades del tramite operado (que tengan
            //  geometria) mediante el ámbito de aplicación del trámite operador,
            //  y exceptuando el ambito de aplicacion del tramite operado.
            iEntR=ClsMain.ambitoAplicacionPorTramite(iTramiteR);
            iEntO=ClsMain.ambitoAplicacionPorTramite(iTramiteO);
            if (iEntR!=null){
                s="Select E.iden From planeamiento.Entidad E " +
                    "Where E.idtramite=" + iTramiteO.getIden() + " " +
                    "And (E.iden In (Select idEntidad From planeamiento.Entidadpol) " +
                    "Or E.iden In (Select idEntidad From planeamiento.Entidadlin) " +
                    "Or E.iden In (Select idEntidad From planeamiento.Entidadpnt)) ";
                if (iEntO!=null){
                    s=s + "And E.iden<>" + iEntO.getIden() + " ";
                }
                lista=new ArrayList();
                lista=ClsConexion.recordList(s);
                nTotalIncorporar=lista.size();
                s="        Se van a incorporar " + nTotalIncorporar + " entidades.";
                mLog.info(s);
                it=lista.iterator();
                while (it.hasNext()){
                    idEntidadO=Integer.parseInt(it.next().toString());
                    iEntO=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidadO);
                    if (ClsOperacionEntidad.opEnt_IncorporacionEntidad(iEntO, iEntR, true)==true){
                        nIncorporadas=nIncorporadas+1;
                    }
                }
                s="        Se han incorporado " + nIncorporadas + " entidades de " +
                    nTotalIncorporar + " del " + 
                    "plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden()) + "] " +
                    "en el plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden()) + "]";
                mLog.info(s);
            } else {
                s="         No se ha incorporado el " + 
                    "plan [" + ClsMain.codigoPlanPorIdTramite(iTramiteO.getIden())+
                    "] porque no se ha encontrado el ámbito de aplicación del " +
                    "plan [" + 
                    ClsMain.codigoPlanPorIdTramite(iTramiteR.getIden()) + "] ";
                mLog.warn(s);
            }
            
        }catch(Exception e){
            mLog.error("Fallo en la operación opPla_Incorporacion. " + e + ". Código 23305." );
        }
    }
}
