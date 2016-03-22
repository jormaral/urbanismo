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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;


@Stateless
@Name("refundido")
@TransactionManagement(TransactionManagementType.BEAN)
public class RefundidoBean implements Refundido {

	@Logger private Log mLog;
	
	 @PersistenceContext
	EntityManager em;
	 
	

    @Resource
    SessionContext sc;
    
    
    @In (create = true, required = false)
    ClsExportacion clsExportacion;
	

   
    private String nombreAmbitoRefundido = "";
    
    private UserTransaction ut;

    
   
   

    @Override
    public ParIdentificadorTexto refundirTramites(List listaIdTramites, String usuario) throws Exception {
    	ParIdentificadorTexto s;

        s=refundirTramites(listaIdTramites, usuario, false);
        return s;
    }

    @Override
    public ParIdentificadorTexto refundirTramites(List listaIdTramites, String usuario, boolean bCrearTramiteRefundido) throws Exception {
        
    	ParIdentificadorTexto resultadoRefundido = new ParIdentificadorTexto();
    	// Lo inicializo a cero
    	resultadoRefundido.setIdBaseDatos(0);
    	resultadoRefundido.setTexto("");
    	
    	String codigoFIPencriptado="";
        boolean resultado=false;
        this.nombreAmbitoRefundido = "";
        String s;
        int i;
        int j;
        int k;
        int soloMensajes;
        int idTramiteRef;
        int iden;
        Tramite iTramite;
        Tramite iTramiteO; // Trámite operado
        Tramite iTramiteR; // Trámite operador
        Iterator it;
        Iterator it2;
        Vector vTram = new Vector();
        int lb = 0;
        int ub = listaIdTramites.size() - 1;
        List listaTmp;
        String mensaje;
        Plan iPlan;
        List lista;
        Calendar cal;
        String fecha;
        int m0;
        
        // Inicia la transacción única del proceso de refundido
        ut=sc.getUserTransaction();
        ut.begin();

        // Asigna el EntityManager a las clases estáticas. Se hace esto aunque la
        //  variable B_USAR_HIBERNATE sea false, ya que se necesita para la exportación
        //  del FIP
        try {
            em.setFlushMode(FlushModeType.COMMIT);
            ClsMain.gEM = em;
            ClsOperacionEntidad.gEM = em;
            ClsOperacionPlan.gEM = em;
            ClsOperacionDeterminacion.gEM = em;
            ClsOperacionRelacion.gEM = em;
            // TODO FGA
            //ClsExportacion.gEM = em;
            ClsConexion.gEM = em;

        } catch (Exception e) {
            mLog.error("Entity Manager inactivo. " + e + ". Código 23001.");
            terminarTransaccion();
            return resultadoRefundido;
        }
        
        mLog.info("**********************************************************");
        cal = Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fecha=sdf.format(cal.getTime());
        mLog.info("Inicio de refundido: " + fecha);

        // Carga los valores de las constantes que se utilizan durante el proceso
        try {
            if (ClsDatos.bCargarDatos() == false) {
                terminarTransaccion();
                return resultadoRefundido;
            }
        } catch (Exception e) {
            mLog.error("Error en la carga de datos constantes. " + e + ". ");
            terminarTransaccion();
            return resultadoRefundido;
        }

        // Mensajes acerca de las versiones
        mLog.info("Versión del programa: " + ClsDatos.VERSION_REFUNDIDO);
        mLog.info("");
        mLog.info("Versión de recursos requerida : " + ClsDatos.VERSION_REQUERIDA_RECURSOS);
        mLog.info("Versión de recursos encontrada: " + ClsDatos.VERSION_RECURSOS);
        if (!ClsDatos.VERSION_REQUERIDA_RECURSOS.equalsIgnoreCase(ClsDatos.VERSION_RECURSOS)){
            mLog.warn("    Atención: la versión de los recursos puede no ser la adecuada.");
        }
        mLog.info("");
        mLog.info("Versión de modelo de datos requerida : " + ClsDatos.VERSION_REQUERIDA_MODELO);
        mLog.info("Versión de modelo de datos encontrada: " + ClsDatos.VERSION_MODELO);
        if (!ClsDatos.VERSION_REQUERIDA_MODELO.equalsIgnoreCase(ClsDatos.VERSION_MODELO)){
            mLog.warn("    Atención: la versión del modelo de diccionario puede no ser la adecuada.");
        }
        
        lista=new ArrayList();
        s="Select version();";
        lista=ClsConexion.recordList(s);
        if (lista.size()==0){
            s=" (función no disponible)";
        } else {
            s=lista.get(0).toString();
        }
        mLog.info("Versión de la base de datos: " + s);
        mLog.info("");

        // Guarda la lista de idens de Tramite, para su uso desde cualquier clase
        ClsDatos.gListaIdTramitesRefundir=new ArrayList();
        ClsDatos.gListaIdTramitesRefundir = listaIdTramites;
        
        // Muestra los datos del ámbito, planes, y trámites que van a ser refundidos.
        if (bMostrarDatos() == false) {
            mLog.error("Error en la presentación de datos. ");
            terminarTransaccion();
            return resultadoRefundido;
        }
        
        // Añade la operación de plan "No Procede" en los planes que no la tengan
        if (bAgregarNoProcede() == false) {
            mLog.error("Error al agregar la operación de plan 'No Procede'. ");
            terminarTransaccion();
            return resultadoRefundido;
        }

        // Averigua los identificadores de los trámites base usados por los
        //  trámites que se van a refundir.
        ClsDatos.gListaIdTramitesBase=new ArrayList();
        s="Select Distinct TB.iden From planeamiento.Tramite TB, planeamiento.Tramite T, " +
                "planeamiento.Plan P, planeamiento.Plan PB " +
                "Where T.idplan=P.iden And P.idplanbase=PB.iden And TB.idplan=PB.iden " +
                "And TB.idtipotramite=" + ClsDatos.ID_TIPOTRAMITE_PLANBASE + " " +
                "And T.iden=Any(Array[0";
        for (int idT: ClsDatos.gListaIdTramitesRefundir){
            s = s + ", " + idT;
        }
        s = s + "]) ";
        ClsDatos.gListaIdTramitesBase = em.createNativeQuery(s).getResultList();
        
        // Asigna autoreferencias en las determinaciones base para garantizar las búsquedas
        //  de grupos de entidad, tanto en planes normalizados como no normalizados.
        it = ClsDatos.gListaIdTramitesBase.iterator();
        while (it.hasNext()) {
            i = Integer.parseInt(it.next().toString());
            s="Update planeamiento.Determinacion Set idDeterminacionBase=iden " +
                "Where idTramite=" + i + " ";
            i=em.createNativeQuery(s).executeUpdate();
        }

        // Guarda los documentos de todos los tramites (exceptuando los de determinación, entidad y caso)
        // Lista de los documentos de cada trámite. Sólo los ligados a trámite, y no los de determinación, entidad y caso.
        ClsDatos.gListaDatosDoc=new ArrayList();
        ClsMain.guardarDocumentosDeTramite();

        // Ordena los trámites según su fecha y su nivel de anidamiento en el árbol de planes
        try {
            vTram = ordenarTramitesParaRefundido(listaIdTramites);

            // Si vTram es nulo, significa que alguno de los idTramite de la lista está repetido.
            if (vTram==null){
                terminarTransaccion();
                return resultadoRefundido;
            }

            // Comprueba si la lista está completa o si hay algún trámite que no existe.
            if (listaIdTramites.size() != vTram.size()) {
                mLog.error("No se ha encontrado alguno de los trámites de la lista. Código 23040.");
                terminarTransaccion();
                return resultadoRefundido;
            }

            mLog.info("     Trámites ordenados:");
            it = vTram.iterator();
            i = 1;
            while (it.hasNext()) {
                iTramite = (Tramite) it.next();
                iPlan = (Plan) ClsConexion.dameEntity(Plan.class, ClsMain.idPlanPorIdTramite(iTramite.getIden()));
                mensaje = "         " + i + " --> Plan [" + iPlan.getCodigo() +
                        "], Tramite iden=" + iTramite.getIden() + ", fecha=" + iTramite.getFecha();
                listaTmp = new ArrayList();
                s = "Select ' opera con <' || Cast(trd.texto As text) || '> sobre " + 
                    "el Plan [' || Cast(po.codigo As text) || ']' " + 
                    "From planeamiento.Plan po, planeamiento.Operacionplan opp, " +
                    "diccionario.Instrumentotipooperacionplan itop, diccionario.Tipooperacionplan tpop, " +
                    "diccionario.Traduccion trd " +
                    "Where opp.idplanoperado=po.iden " +
                    "And opp.idplanoperador=" + iPlan.getIden() + " " +
                    "And opp.idinstrumentotipooperacion=itop.iden And itop.idtipooperacionplan=tpop.iden " +
                    "And tpop.idliteral=trd.idliteral " +
                    "Union " +
                    "Select ' opera con <' || trd.texto || '>' " +
                    "From planeamiento.Operacionplan opp, diccionario.Instrumentotipooperacionplan itop, " +
                    "diccionario.Tipooperacionplan tpop, diccionario.Traduccion trd " +
                    "Where opp.idplanoperado Is Null And opp.idplanoperador=" + iPlan.getIden() + " " +
                    "And opp.idinstrumentotipooperacion=itop.iden " +
                    "And itop.idtipooperacionplan=tpop.iden And tpop.idliteral=trd.idliteral ";
                listaTmp = em.createNativeQuery(s).getResultList();
                it2 = listaTmp.iterator();
                if (listaTmp.size() > 0) {
                    while (it2.hasNext()) {
                        mLog.info(mensaje + it2.next());
                    }
                } else {
                    mLog.info(mensaje + " (sin operación)");
                }
                i = i + 1;
            }
        } catch (Exception e) {
            mLog.error("La lista de trámites a refundir ha causado un error. " + e + ". Código 23002.");
            terminarTransaccion();
            return resultadoRefundido;
        }

        // **********************
        // Algoritmo de refundido
        // **********************
        
        // Si no se desea crear el trámite refundido en la BD, se asume que se 
        //  trata de una ejecución en modo de prueba, y tampoco se muestra el
        //  resumen previo de operaciones, con lo cual se acelera el proceso.
        if (bCrearTramiteRefundido==true){
            m0=0;
        } else {
            m0=1;
        }
        
        for (soloMensajes = m0; soloMensajes <= 1; soloMensajes++) {
            mLog.info("--------------------------------");
            if (soloMensajes == 1) {
                mLog.info("Inicio de operaciones de refundido:");
            } else {
                mLog.info("Resumen previo de operaciones de refundido:");
            }

            ClsDatos.gOrdenOperacion = 1;
            lb = 0;
            ub = vTram.size() - 1;
            try {
                for (i = ub; i >= lb; i--) {
                    iTramiteO = (Tramite) vTram.get(i);
                    iden = iTramiteO.getIden();
                    iTramiteO = (Tramite) ClsConexion.dameEntity(Tramite.class, iden);
                    
                    // *************************************************************
                    // Aquí hay que hacer las modificaciones ordenadas por
                    //  el trámite j sobre el i.
                    for (k = 1; k >= 0; k--) {
                        // k=1 indica que se deben operar los elementos operadores del iTramiteO
                        // k=0 indica que se debe operar el resto de los elementos de iTramiteO
                        //  Esto se hace para asegurar que las operaciones internas del ItramiteO
                        //      se ejecutan después de que sus elementos operadores han sido operados
                        //      por los planes subsiguientes.
                        for (j = i + k; j <= ub; j++) {
                            iTramiteR = (Tramite) vTram.get(j);
                            iden = iTramiteR.getIden();
                            iTramiteR = (Tramite) ClsConexion.dameEntity(Tramite.class, iden);
                            iPlan = (Plan) ClsConexion.dameEntity(Plan.class, ClsMain.idPlanPorIdTramite(iTramiteR.getIden()));

                            // Modificación (Solamente si el iTramiteR NO es un desarrollo, en cuyo caso, sus
                            //  operaciones de modificación se harán después de desarrollar.
                            if (ClsMain.bTramiteEsDesarrollo(iTramiteR.getIden())==false){
                                ClsOperacionPlan.opPla_Modificacion(iTramiteO, iTramiteR, k, soloMensajes);
                            } else {
                                // Es desarrollo
                                if (k==0){
                                    if (ClsOperacionPlan.opPla_Desarrollo(iTramiteO, iTramiteR, soloMensajes)==true){
                                        // Modificaciones ordenadas por el desarrollo
                                        iPlan = (Plan) ClsConexion.dameEntity(Plan.class, ClsMain.idPlanPorIdTramite(iTramiteR.getIden()));
                                        mLog.info("           Modificaciones ordenadas por el desarrollo [" + iPlan.getCodigo() + "]");
                                        ClsOperacionPlan.opPla_Modificacion(iTramiteO, iTramiteR, 1, soloMensajes);
                                        ClsOperacionPlan.opPla_Modificacion(iTramiteO, iTramiteR, 0, soloMensajes);                                    
                                    }
                                }
                            }
                        
                            if (k==0){
                                // Suspensión total
                                ClsOperacionPlan.opPla_SuspensionTotal(iTramiteO, iTramiteR, soloMensajes);

                                // Revocación total
                                ClsOperacionPlan.opPla_RevocacionTotal(iTramiteO, iTramiteR, soloMensajes);

                                // Suspensión parcial
                                ClsOperacionPlan.opPla_SuspensionParcial(iTramiteO, iTramiteR, soloMensajes);

                                // Suspensión parcial
                                ClsOperacionPlan.opPla_RevocacionParcial(iTramiteO, iTramiteR, soloMensajes);

                                // Incorporacion de plan
                                ClsOperacionPlan.opPla_Incorporacion(iTramiteO, iTramiteR, soloMensajes);
                            }
                        }
                    }

                    // Recolección de basura
                    System.gc();
                    em.clear();
                }

                // *************************************************************
                // Aquí hay que hacer las sustituciones
                for (i = lb; i <= ub - 1; i++) {
                    iTramiteO = (Tramite) vTram.get(i);
                    iden = iTramiteO.getIden();
                    iTramiteO = (Tramite) ClsConexion.dameEntity(Tramite.class, iden);
                    if (iTramiteO!=null){
                        for (j = ub; j >= i + 1; j--) {
                            iTramiteR = (Tramite) vTram.get(j);
                            iden = iTramiteR.getIden();
                            iTramiteR = (Tramite) ClsConexion.dameEntity(Tramite.class, iden);
                            if (iTramiteR!=null){
                                // Sustitución
                                ClsOperacionPlan.opPla_Sustitucion(iTramiteO, iTramiteR, soloMensajes);
                            }
                        }
                    }
                }

                if (soloMensajes == 1) {
                    mLog.info("FIN de operaciones de refundido:");
                    mLog.info("--------------------------------");
                }

            } catch (Exception e) {
                mLog.error("Fallo no controlado durante el proceso. " + e + ". Código 23000.");
                terminarTransaccion();
                return resultadoRefundido;
            }

            // Recolección de basura
            System.gc();
            em.clear();
        }

        // Copiar los documentos de trámite de cada trámite operador en el trámite refundido. Para ello,
        //  se crea una carpeta de 'Entidades aportadas por...' (si no existe previamente), y se
        //  vinculan los documentos a dichas carpetas, según sea su trámite.
        if (ClsMain.bCopiarDocumentosDeTramite() == false) {
            terminarTransaccion();
            return resultadoRefundido;
        }
        // Recolección de basura
        ClsDatos.gListaDatosDoc = null;
        System.gc();
        em.clear();

        // Eliminar las relaciones huérfanas
        ClsOperacionRelacion.eliminarRelacionesHuerfanas();
        // Recolección de basura
        System.gc();
        em.clear();

        // Reasignar al trámite refundido las determinaciones dependientes de las entidades de dicho trámite
        ClsMain.reasignarDependencias();
        // Recolección de basura
        System.gc();
        em.clear();

        // Comprobar si quedan operaciones sin hacer
        ClsMain.listarOperacionesNoEjecutadas();
        // Recolección de basura
        System.gc();
        em.clear();

        // Eliminar elementos operadores: planes, entidades y determinaciones.
        ClsMain.eliminarOperadores();
        // Recolección de basura
        System.gc();
        em.clear();

        // Averigua cuál es el iden del trámite refundido.
        idTramiteRef = ClsMain.idTramiteRefundido();
        if (idTramiteRef == 0) {
            terminarTransaccion();
            return resultadoRefundido;
        }

        // Eliminar las carpetas vacías
        ClsMain.eliminarCarpetasVacias(idTramiteRef);
        // Recolección de basura
        System.gc();
        em.clear();
        
        // Unificar las unidades
        ClsMain.unificarUnidades(idTramiteRef);
        // Recolección de basura
        System.gc();
        em.clear();
        
        // Eliminar las relaciones huérfanas por segunda vez
        ClsOperacionRelacion.eliminarRelacionesHuerfanas();
        // Recolección de basura
        System.gc();
        em.clear();
        
        // Reubicar las carpetas de determinaciones y entidades aportadas, para
        //  que cuelguen de una carpeta "Aportadas".
        ClsMain.reubicarCarpetasAportadas(idTramiteRef);
        // Recolección de basura
        System.gc();
        em.clear();
        
        // Actualiza algunos datos en todos los planes para que cumplan ciertas
        //  validaciones.
        resultado=bDepurarDatosAlfanumericos();
        // Recolección de basura
        System.gc();
        em.clear();
        
        // Debido a las reiteradas copias de información entre entidades y 
        //  determinaciones, en ocasiones se duplican las vinculaciones a
        //  los documentos. Este procedimiento se encarga de eliminar
        //  estas duplicidades.
        resultado=bEliminarDocumentosDuplicados();
        // Recolección de basura
        System.gc();
        em.clear();
         
        // Exportar a FIP
        try {
        	
        	// TODO FGA
        	/*
        	mLog.info("[ClsExportacion.exportarFIP] Exportacion del FIP que no se hace de momento... FGA");
        	mLog.info("[ClsExportacion.exportarFIP] idTramiteRef="+idTramiteRef);
        	codigoFIPencriptado = "FGA";
        	*/
        	mLog.info("[ClsExportacion.exportarFIP] Exportacion del FIP... FGA");
        	// TODO FGA Original
            //codigoFIPencriptado = ClsExportacion.exportarFIP(idTramiteRef, ffb);
            
        	String codigoFIPencriptadoYRuta = clsExportacion.exportarFIP(idTramiteRef);
        	
        	String[] codigoFIPYRuta = codigoFIPencriptadoYRuta.split("%");
        	
        	codigoFIPencriptado = codigoFIPYRuta[0];
        	resultadoRefundido.setTexto(codigoFIPYRuta[1]);
        	
        	
            
        } catch (Exception e) {
            mLog.error("Fallo al exportar a FIP. " + e + ". Código 23005.");
            mLog.info("********************************************");
            terminarTransaccion();
            return resultadoRefundido;
        }

        // Mensaje de salida
        mensaje = "El proceso de refundido ha finalizado con ";
        if (!codigoFIPencriptado.trim().equalsIgnoreCase("")) {
            resultado=true;
            mensaje = mensaje + "éxito.";
        } else {
            resultado=false;
            mensaje = mensaje + "error.";
        }

        // Envío del mensaje por correo
        try {
            //mLog.info("");
            //mLog.info("Enviando mensaje por correo electrónico...");
            //servMail.sendEmail("Notificación Proceso de refundido", mensaje, usuario);
            //mLog.info("    Mensaje enviado al usuario '" + usuario + "'.");
        } catch (Exception e) {
            mLog.warn("*** No se ha conseguido enviar el correo electrónico. " + e + ". Código 23003.");
        }

        // Asegura que todo el proceso NO se reflejará en la base de datos
        terminarTransaccion();
        mLog.info(mensaje);

        //TODO :FGA Me creo esta variable para devolver el idTramitePrerefundido
        int idTramitePrerefundido = 0;
        
        // Crea el plan y trámite refundido en la base de datos. Se hace ahora, después de
        //  llamar al método terminarTransaccion(), para que no le afecte el RollBack
        if (resultado==true){
            if (bCrearTramiteRefundido==true){
            	//FGA: Comentado el antiguo
                //resultado=bCrearTramiteRefundido(idTramiteRef, codigoFIPencriptado);
                idTramitePrerefundido = bCrearTramiteRefundido(idTramiteRef, codigoFIPencriptado);
                resultadoRefundido.setIdBaseDatos(idTramitePrerefundido);
            } else {
                mLog.info("El usuario ha elegido no crear el trámite refundido en la base de datos.");
            }
        }

        mLog.info("FIN de proceso");
        mLog.info("********************************************");

        return resultadoRefundido;
    }

    private Boolean bDepurarDatosAlfanumericos(){
        String s;

        // En este procedimiento se deben incluir aquellos arreglos en los datos
        //  que consistan en mejorar la calidad de los mismos. PE: eliminación
        //  de espacios supérfluos.
        
        try{
            mLog.info("");
            mLog.info("Inicio de la depuración de datos alfanuméricos:");

            // Suprimir los doble-espacios (si existen) en el campo "Apartado" de
            //  las determinaciones.
            mLog.info("    - Eliminación de espacios dobles en el apartado de determinación.");
            s="Update planeamiento.Determinacion Set apartado=Replace(apartado, '  ', ' ') ";
            ClsConexion.ejecutar(s);

            // Poner un texto '-' en el campo "texto" de las determinaciones de 
            //  carácter "Regulación" que no tengan texto.
            mLog.info("    - Inserción de '-' en regulaciones sin texto.");
            s="Update planeamiento.Determinacion Set texto='-' " +
                "Where idCaracter=" + ClsDatos.ID_CARACTER_REGULACION + " " +
                "And (texto Is Null Or Trim(texto)='') ";
            ClsConexion.ejecutar(s);
            
            mLog.info("Fin de la depuración de datos alfanuméricos.");
            return true;

        } catch (Exception e){
            mLog.warn("No se han podido actualizar los datos antes de refundir. " + e);
        }
        return false;
    }
    
    private Boolean bEliminarDocumentosDuplicados(){
        String s;
        
        // Debido a las reiteradas copias de información entre entidades y 
        //  determinaciones, en ocasiones se duplican las vinculaciones a
        //  los documentos. Este procedimiento se encarga de eliminar
        //  estas duplicidades.
        
        try{
            mLog.info("");
            mLog.info("Inicio de la depuración de documentos:");

            // Documentos ligados a entidades.
            mLog.info("    - Documentos ligados a entidades.");
            s="Delete From planeamiento.DocumentoEntidad Where (iden, idEntidad, idDocumento) In (" +
                "Select iden, idEntidad, idDocumento From planeamiento.DocumentoEntidad " + 
                "Where (idEntidad, idDocumento) In (" +
                "Select idEntidad, idDocumento From planeamiento.DocumentoEntidad " +
                "Group By idEntidad, idDocumento Having Count(*)>1) " +
                "Except " +
                "Select Min(iden), idEntidad, idDocumento From planeamiento.DocumentoEntidad " +
                "Group By idEntidad, idDocumento Having Count(*)>1) ";
            ClsConexion.ejecutar(s);
            
            // Documentos ligados a determinaciones.
            mLog.info("    - Documentos ligados a determinaciones.");
            s="Delete From planeamiento.DocumentoDeterminacion Where (iden, idDeterminacion, idDocumento) In (" +
                "Select iden, idDeterminacion, idDocumento From planeamiento.DocumentoDeterminacion " + 
                "Where (idDeterminacion, idDocumento) In (" +
                "Select idDeterminacion, idDocumento From planeamiento.DocumentoDeterminacion " +
                "Group By idDeterminacion, idDocumento Having Count(*)>1) " +
                "Except " +
                "Select Min(iden), idDeterminacion, idDocumento From planeamiento.DocumentoDeterminacion " +
                "Group By idDeterminacion, idDocumento Having Count(*)>1) ";
            ClsConexion.ejecutar(s);

            // Documentos ligados a casos.
            mLog.info("    - Documentos ligados a casos.");
            s="Delete From planeamiento.DocumentoCaso Where (iden, idCaso, idDocumento) In (" +
                "Select iden, idCaso, idDocumento From planeamiento.DocumentoCaso " + 
                "Where (idCaso, idDocumento) In (" +
                "Select idCaso, idDocumento From planeamiento.DocumentoCaso " +
                "Group By idCaso, idDocumento Having Count(*)>1) " +
                "Except " +
                "Select Min(iden), idCaso, idDocumento From planeamiento.DocumentoCaso " +
                "Group By idCaso, idDocumento Having Count(*)>1) ";
            ClsConexion.ejecutar(s);
            
            mLog.info("Fin de la depuración de documentos.");
            return true;

        } catch (Exception e){
            mLog.warn("No se han podido depurar los documentos. " + e);
        }
        return false;
    }

    private int bCrearTramiteRefundido(int idTramiteRef, String codigoFIPencriptado){
        String s="";
        String iteracion;
        String nombre;
        Calendar cal;
        String fecha;
        String codigoPlan;
        List lista;
        int i;
        int idPlan;

        try{
            mLog.info("");
            mLog.info("Inicio de la creación del trámite refundido en la base de datos.");
            ut.begin();

            // Fecha que se va a asignar al trámite refundido
            cal = Calendar.getInstance();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            fecha=sdf.format(cal.getTime());

            // Código del plan
            codigoPlan=ClsDatos.gCodigoPlanRefundido;

            // Nombre del trámite
            nombre="Plan prerefundido de <" + this.nombreAmbitoRefundido + "> " +fecha;

            // Crea el nuevo registro en Plan
            s="Insert Into planeamiento.Plan (idPadre, nombre, codigo, idPlanBase, " +
                "idAmbito, bSuspendido, orden) " +
                "Select Null, '" + nombre + "', '" + codigoPlan + "', " +
                "P.idPlanBase, P.idAmbito, P.bSuspendido, (Select 1+Max(orden) " +
                "From planeamiento.Plan Where idAmbito=" + ClsDatos.gIdAmbito + ") " +
                "From planeamiento.Plan P, planeamiento.Tramite T " +
                "Where T.idPlan=P.iden And T.iden=" + idTramiteRef + " ";
            i=ClsConexion.ejecutar(s);
            if (i!=1){
                mLog.warn("No se ha podido crear el nuevo registro de la tabla Plan con la instrucción: " + s);
                ut.rollback();
                return 0;
            }
            // Recupera el iden del plan recién creado.
            s="Select Max(iden) From planeamiento.Plan ";
            lista=ClsConexion.recordList(s);
            idPlan=Integer.parseInt(lista.get(0).toString());

            // Crea el registro en OperacionPlan
            s="Insert Into planeamiento.OperacionPlan (idPlanOperador, idInstrumentoTipoOperacion) " +
                "Select " + idPlan + ", ITOP.iden " +
                "From diccionario.InstrumentoTipoOperacionPlan ITOP " +
                "Where ITOP.idTipoOperacionPlan=" + ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE + " " +
                "And ITOP.idInstrumentoPlan=" + ClsDatos.ID_INSTRUMENTOPLAN_REFUNDIDO +" ";
            i=ClsConexion.ejecutar(s);
            if (i!=1){
                mLog.warn("No se ha podido crear el nuevo registro de la tabla OperacionPlan con la instrucción: " + s);
                ut.rollback();
                return 0;
            }

            // Iteracion. Debido a que el resultado del refundido es siempre un
            //  nuevo plan, siempre habrá un único trámite para él, y tendrá
            //  la iteración 1.
            iteracion="01";
            
            // Mensaje con los datos del trámite que se va a crear
            mLog.info("    Nombre   : " + nombre);
            mLog.info("    Plan     : " + codigoPlan);
            mLog.info("    Fecha    : " + fecha);
            mLog.info("    Iteración: " + iteracion);
            mLog.info("    Código   : " + codigoFIPencriptado);

            // Alguno de los datos (el centro de producción) para el nuevo
            //  trámite se toma del trámite que ha quedado refundido.
            s="Insert Into planeamiento.tramite (idtipotramite, idorgano, idsentido, idplan, " +
                "fecha, texto, nombre, idcentroproduccion, " +
                "codigofip, iteracion) " +
                "Select " + ClsDatos.ID_TIPOTRAMITE_REFUNDIDO + ", idorgano, " +
                "idsentido, " + idPlan + ", " +
                "'" + fecha + "', 'Plan prerefundido: [" + codigoPlan + "]', "  +
                "'" + nombre + "', idcentroproduccion, '" + codigoFIPencriptado + "', " +
                "'" + iteracion + "' From planeamiento.tramite Where iden=" + idTramiteRef + " ";
            i=ClsConexion.ejecutar(s);
            if (i!=1){
                mLog.warn("No se ha podido crear el nuevo registro de la tabla Tramite con la instrucción: " + s);
                ut.rollback();
                return 0;
            }

            // Recupera el iden del trámite recién creado.
            s="Select Max(iden) From planeamiento.Tramite ";
            lista=ClsConexion.recordList(s);
            idTramiteRef=Integer.parseInt(lista.get(0).toString());

            // Actualiza el campo comentario con información de los planes que han intervenido
            // TODO FGA Comentado porque da algun fallo y no es necesario
            //ClsMain.actualizarComentarioTramite(idTramiteRef);

            // Se valida la creación del plan refundido
            ut.commit();
            mLog.info("Creado el trámite refundido en la base de datos. idTramiteRef="+idTramiteRef);
            return idTramiteRef;

        } catch (Exception e){
            mLog.warn("No se ha podido crear el nuevo trámite en la base de datos. La última instrucción ejecutada es: " + s + e);
        }
        try {
            ut.rollback();
        } catch (IllegalStateException ex) {
            java.util.logging.Logger.getLogger(RefundidoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            java.util.logging.Logger.getLogger(RefundidoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            java.util.logging.Logger.getLogger(RefundidoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        mLog.warn("No se ha podido crear el nuevo trámite en la base de datos. La última instrucción ejecutada es: " + s );
        return 0;
    }
 
    private Vector ordenarTramitesParaRefundido(List listaIdTramites) throws Exception {
        // El orden de los trámites viene dado por el orden del plan.
        
        Iterator it;
        Tramite iTramite;
        int idTramite = 0;
        Vector iVector = new Vector();
        String s="";
        List lista;

        try {
            s="Select T.iden From planeamiento.Tramite T, planeamiento.Plan P " +
                "Where T.idPlan=P.iden And T.iden=Any(Array[0";
            it = listaIdTramites.iterator();
            while (it.hasNext()) {
                idTramite = (Integer) it.next();
                s = s + ", " + idTramite;
            }
            s= s + "]) Order By P.orden, T.fecha ";
            lista=new ArrayList();
            lista=ClsConexion.recordList(s);
            
            it = lista.iterator();
            while (it.hasNext()) {
                idTramite = (Integer) it.next();
                iTramite = (Tramite) ClsConexion.dameEntity(Tramite.class, idTramite);
                iVector.add(iTramite);
            }
            
        } catch (Exception e) {
            mLog.error("Error en ordenarTramitesParaRefundido: " + e + " idTramite=" + idTramite + ". Código 23039.");
            return null;
        }
        return iVector;
    }

    private Vector agregarTramitesModificadores(Vector vTram){
        Iterator it;
        Tramite iTramite;
        Tramite jTramite;
        Tramite kTramite;
        int i;
        int j;
        int k;
        String s;
        List lista;
        Vector vTram2;

        // Lista de trámites donde se van a duplicar los que modifican a algún otro que esté en
        //  otra rama del árbol de planes.
        vTram2=(Vector) vTram.clone();

        for (i=1;i<=vTram.size();i++){
            iTramite=(Tramite) vTram.get(i-1);
            for(j=vTram.size();j>=i+1;j--){
                jTramite=(Tramite) vTram.get(j-1);

                // ***************
                // Determinaciones
                // Comprueba si i opera sobre j con alguna operación de modificación de determinación
                lista = new ArrayList();
                s="Select * From planeamiento.OperacionDeterminacion OD, planeamiento.Determinacion D, " +
                        "planeamiento.Operacion O " +
                        "Where OD.idOperacion=O.iden And OD.idDeterminacion=D.iden " +
                        "And O.idTramiteOrdenante=" + iTramite.getIden() + " " +
                        "And D.idTramite=" + jTramite.getIden() + " ";
                lista=ClsConexion.recordList(s);
                if (lista.size()>0){
                    // Averigua cuáles son los trámites hijo del trámite j
                    lista = new ArrayList();
                    s="Select TH.iden From planeamiento.Tramite T, planeamiento.Plan P, " +
                            "planeamiento.Plan PH, planeamiento.Tramite TH " +
                            "Where T.iden=" + jTramite.getIden() + " And T.idPlan=P.iden " +
                            "And PH.idPadre=P.iden And TH.idPlan=PH.iden " +
                            "Order By TH.fecha ";
                    lista=ClsConexion.recordList(s);
                    if (lista.size()==0){
                        // Si no tiene hijos, el trámite i se copia justo detrás del j
                        for(k=vTram2.size();k>=1;k--){
                            kTramite=(Tramite) vTram2.get(k-1);
                            if (jTramite.getIden()==kTramite.getIden()){
                                vTram2.insertElementAt(iTramite, k);
                                break;
                            }
                        }
                    } else {
                        // De todos los hijos de j (trámites k), se pone el trámite i debajo de aquel que
                        //  tenga fecha inmediatamente anterior. Antes, se comprueba que el trámite hijo
                        //  sea uno de los contenidos en la lista.
                        for (k=1;k>=lista.size();k++){
                            kTramite=(Tramite) vTram2.get(k-1);
                            if (vTram.contains(kTramite)){
                                if (kTramite.getFecha().before(iTramite.getFecha())){
                                    // Inserta el trámite i justo detrás del k
                                    for(k=vTram2.size();k>=1;k--){
                                        kTramite=(Tramite) vTram2.get(k-1);
                                        if (jTramite.getIden()==kTramite.getIden()){
                                            vTram2.insertElementAt(iTramite, k);
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }

                // *********
                // Entidades
                // Comprueba si i opera sobre j con alguna operación de modificación de entidad
                lista = new ArrayList();
                s="Select * From planeamiento.OperacionEntidad OE, planeamiento.Entidad E, " +
                        "planeamiento.Operacion O " +
                        "Where OE.idOperacion=O.iden And OE.idEntidad=E.iden " +
                        "And O.idTramiteOrdenante=" + iTramite.getIden() + " " +
                        "And E.idTramite=" + jTramite.getIden() + " ";
                lista=ClsConexion.recordList(s);
                if (lista.size()>0){
                    // Averigua cuáles son los trámites hijo del trámite j
                    lista = new ArrayList();
                    s="Select TH.iden From planeamiento.Tramite T, planeamiento.Plan P, " +
                            "planeamiento.Plan PH, planeamiento.Tramite TH " +
                            "Where T.iden=" + jTramite.getIden() + " And T.idPlan=P.iden " +
                            "And PH.idPadre=P.iden And TH.idPlan=PH.iden " +
                            "Order By TH.fecha ";
                    lista=ClsConexion.recordList(s);
                    if (lista.size()==0){
                        // Si no tiene hijos, el trámite i se copia justo detrás del j
                        for(k=vTram2.size();k>=1;k--){
                            kTramite=(Tramite) vTram2.get(k-1);
                            if (jTramite.getIden()==kTramite.getIden()){
                                vTram2.insertElementAt(iTramite, k);
                                break;
                            }
                        }
                    } else {
                        // De todos los hijos de j (trámites k), se pone el trámite i debajo de aquel que
                        //  tenga fecha inmediatamente anterior. Antes, se comprueba que el trámite hijo
                        //  sea uno de los contenidos en la lista.
                        for (k=1;k>=lista.size();k++){
                            kTramite=(Tramite) vTram2.get(k-1);
                            if (vTram.contains(kTramite)){
                                if (kTramite.getFecha().before(iTramite.getFecha())){
                                    // Inserta el trámite i justo detrás del k
                                    for(k=vTram2.size();k>=1;k--){
                                        kTramite=(Tramite) vTram2.get(k-1);
                                        if (jTramite.getIden()==kTramite.getIden()){
                                            vTram2.insertElementAt(iTramite, k);
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return vTram2;
    }
    
    private boolean bMostrarDatos() {
        String s = "";
        List lista;
        int i;
        Iterator it;
        Object obj;
        Object[] obj2;

        try {
            // Ámbito de los trámites
            lista = new ArrayList();
            s = "Select Distinct Cast(trd.texto As Text), a.iden " + 
                "From diccionario.Ambito a, planeamiento.Tramite t, " +
                "planeamiento.Plan p, diccionario.traduccion trd " +
                "Where a.idliteral=trd.idliteral And t.idplan=p.iden And p.idambito=a.iden " +
                "And t.iden In (0";
            for (i = 0; i < ClsDatos.gListaIdTramitesRefundir.size(); i++) {
                s = s + ", " + ClsDatos.gListaIdTramitesRefundir.get(i);
            }
            s = s + ") ";
            lista = em.createNativeQuery(s).getResultList();
            if (lista.size() == 0) {
                mLog.error("Error: No se ha encontrado el ámbito para refundir. Código 23054.");
                return false;
            }
            if (lista.size() > 1) {
                mLog.error("Error: Se han encontrado " + lista.size() + " ámbitos para refundir. Código 23055.");
                return false;
            }
            obj2=(Object[]) lista.get(0);
                            
            this.nombreAmbitoRefundido = obj2[0].toString();
            mLog.info("------------------------------");
            mLog.info("Ámbito: " + obj2[0].toString());
            ClsDatos.gIdAmbito=Integer.parseInt(obj2[1].toString());

            // Trámites
            lista = new ArrayList();
            s = "Select ': Trámite <' || Cast(trdtt.texto As Text) || '> de fecha ' || " +
                    "Cast(t.fecha As Text) || ', del plan [' || Cast(p.codigo As Text) || '] ' || Cast(p.nombre As Text) " +
                    "From planeamiento.Plan p, planeamiento.Tramite t, diccionario.Tipotramite tt, " +
                    "diccionario.Traduccion trdtt " +
                    "Where t.idplan=p.iden And t.idtipotramite=tt.iden " +
                    "And trdtt.idliteral=tt.idliteral " +
                    "And t.iden In (0";
            for (i = 0; i < ClsDatos.gListaIdTramitesRefundir.size(); i++) {
                s = s + ", " + ClsDatos.gListaIdTramitesRefundir.get(i);
            }
            s = s + ") Order By t.fecha ";
            lista = em.createNativeQuery(s).getResultList();
            if (lista.size() == 0) {
                mLog.error("Error: No se han encontrado los datos de los trámites solicitados. Código 23056.");
                return false;
            }

            it = lista.iterator();
            i = 0;
            while (it.hasNext()) {
                i = i + 1;
                obj = (Object) it.next();
                s = String.valueOf(i) + obj.toString();
                mLog.info(s);
            }
            mLog.info("------------------------------");

        } catch (Exception e) {
            mLog.error("Error al mostrar los datos aportados para el proceso de refundido: " + e + ". Código 23053. " + s);
            return false;
        }

        return true;
    }

    private void terminarTransaccion() {
        String s;

        try {
            ut.rollback();
            if (ClsDatos.B_USAR_HIBERNATE == false) {
                s = "Rollback Transaction";
                ClsConexion.ejecutar(s);
            }
        } catch (IllegalStateException ex) {
            java.util.logging.Logger.getLogger(RefundidoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            java.util.logging.Logger.getLogger(RefundidoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            java.util.logging.Logger.getLogger(RefundidoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RefundidoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNombreAmbitoRefundido() {
        return nombreAmbitoRefundido;
    }
    
    private boolean bAgregarNoProcede() {
        String s = "";
        List lista;
        Iterator it;
        Object obj;
        int idPlan;
        int idInstrumento;
        int idITOP;
        int i;
        Plan iPlan;
        int idTramite;
        
        try {
            mLog.info(s);
            s="Verificando operaciones 'No Procede'";
            mLog.info(s);
            
            // Busca los planes que no tienen precedente  y que no tienen la operación "No Procede"
            lista = new ArrayList();
            s="Select P.iden From planeamiento.Plan P Where P.idPadre Is Null " +
                "And P.iden Not In (Select OP.idPlanOperador From planeamiento.OperacionPlan OP, " +
                "diccionario.InstrumentoTipoOperacionPlan ITOP " + 
                "Where OP.idInstrumentoTipoOperacion=ITOP.iden " + 
                "And ITOP.idTipoOperacionPlan=" + ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE +") ";
            s=s + "And P.iden In (Select T.idPlan From planeamiento.Tramite T " + 
                "Where T.iden=Any(Array[0";
            it=ClsDatos.gListaIdTramitesRefundir.iterator();
            while (it.hasNext()) {
                obj = (Object) it.next();
                idTramite = Integer.parseInt(obj.toString());
                s=s + ", " + String.valueOf(idTramite);
            }
            s=s + "])) ";
            
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            
            // Planes para los cuales hay que crear la operación "No procede". Sólo se
            //  hace si pertenecen a la lista de los que se van a refundir.
            while (it.hasNext()) {
                obj = (Object) it.next();
                idPlan = Integer.parseInt(obj.toString());
                iPlan=(Plan) ClsConexion.dameEntity(Plan.class, idPlan);
                s="    Verificando Plan [" + iPlan.getCodigo() +"]";
                mLog.info(s);
                // Averigua el instrumento del plan
                s="Select ITOP.idInstrumentoPlan From planeamiento.OperacionPlan OP, " +
                    "diccionario.InstrumentoTipoOperacionPlan ITOP " +
                    "Where OP.idInstrumentoTipoOperacion=ITOP.iden " +
                    "And OP.idPlanOperador=" + idPlan + " ";
                lista=ClsConexion.recordList(s);
                if (lista.size()==0){
                    s="Error: No se ha encontrado el instrumento del Plan [" + iPlan.getCodigo() + "]";
                    mLog.error(s);
                    s="------------------------------";
                    mLog.info(s);
                    return false;
                }
                idInstrumento=Integer.parseInt(lista.get(0).toString());
                
                // Averigua el registro de InstrumentoTipoOperacionPlan correspondiente al
                //  instrumento del plan y la operación "No Procede". Si no existe, se intenta
                //  crear uno.
                idITOP=0;
                for (i=0;i<2;i++){
                    s="Select ITOP.iden From diccionario.InstrumentoTipoOperacionPlan ITOP " +
                        "Where ITOP.idInstrumentoPlan=" + idInstrumento + " " +
                        "And ITOP.idTipoOperacionPlan=" + ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE + " ";
                    lista=ClsConexion.recordList(s);
                    if (lista.size()==0){
                        s="Aviso: No se ha encontrado el registro de la tabla " + 
                            "diccionario.InstrumentoTipoOperacionPlan correspondiente " +
                            "a la operación de plan 'No Procede' y al " + 
                            "idInstrumento=" + idInstrumento + ". Se va a intentar " +
                            "crear un registro temporal en el diccionario.";
                        mLog.warn(s );
                        // Insertar nuevo registro en diccionario
                        s="Insert Into diccionario.InstrumentoTipoOperacionPlan " +
                            "(idInstrumentoPlan, idTipoOperacionPlan) Values " +
                            "(" + idInstrumento + ", " + ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE + ") ";
                        ClsConexion.ejecutar(s);
                        if (ClsConexion.ejecutar(s)==1){
                            s="    Insertado registro temporal en diccionario.";
                            mLog.info(s);
                        } else {
                            s="Error: No se ha conseguido insertar el registro en diccionario.";
                            mLog.error(s);
                        }
                    } else {
                        idITOP=Integer.parseInt(lista.get(0).toString());
                        break;
                    }
                }
                if (idITOP==0){
                    s="Error: No se ha conseguido vincular el plan con su instrumento y la operación 'No Procede'.";
                    mLog.error(s);
                    s="------------------------------";
                    mLog.info(s);
                    return false;
                }
                
                // Insertar el registro en OperacionPlan
                s="Insert Into planeamiento.OperacionPlan (idInstrumentoTipoOperacion, " +
                    "idPlanOperador) Values (" + idITOP + ", " + idPlan + ") ";
                if (ClsConexion.ejecutar(s)==1){
                    s="    Insertada la operación 'No Procede' para el plan [" + iPlan.getCodigo() + "]";
                    mLog.info(s);
                } else {
                    s="Error: No se ha conseguido insertar la operación 'No Procede' para el plan [" + iPlan.getCodigo() + "]";
                    mLog.info(s);
                }
            }
            s="------------------------------";
            mLog.info(s);
            return true;

        } catch (Exception e) {
            mLog.error("Error al verificar la operación 'No Procede' " + e);
            s="------------------------------";
            mLog.info(s);
            return false;
        }        
    }
}
