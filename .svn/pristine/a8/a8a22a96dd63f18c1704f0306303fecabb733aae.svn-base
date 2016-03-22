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

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorConsultasLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;

/**
 * Session Bean implementation class GestorOperacionesPlanBean
 */
@Stateless(name = "GestorOperacionesPlan")
public class GestorOperacionesPlanBean implements GestorOperacionesPlanLocal {

	private static final String SELECT_INTERSECCION = "Select e2.iden From planeamiento.Entidad e2, " +
            "planeamiento.%s eg1, planeamiento.%s eg2 " +
            "Where eg2.identidad=e2.iden " +
            "And eg1.identidad= %d " +
            "And e2.idtramite= %d  " +
            "And Intersects(eg1.geom, eg2.geom)=true ";
	
	private static final Logger log = Logger.getLogger(GestorOperacionesPlanBean.class);
	
	// Según sea el valor del parámetro soloOperadores, se presentará un mensaje diferente
	private static final String[] sOperadores  = new String[] {"operadas","operadoras"};
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private GestorConsultasLocal gestorConsultas;
	@EJB
	private GestorOperacionesDeterminacionLocal gestorOperacionesDeterminacion;
	@EJB
	private GestorOperacionesEntidadLocal gestorOperacionesEntidad;
	@EJB
	private GestorOperacionesRelacionLocal gestorOperacionesRelacion;
	@EJB
	private EliminadorEntidadesLocal eliminadorEntidades;
	
    /**
     * Default constructor. 
     */
    public GestorOperacionesPlanBean() {
    }

   /*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesPlanLocal#aplicarDesarrollo(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, boolean, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean aplicarDesarrollo(ContextoRefundido contexto) {
        try{
        	Tramite tramiteOperador = (Tramite)contexto.getParametro(TRAMITE_OPERADOR);
        	Tramite tramiteOperado = (Tramite)contexto.getParametro(TRAMITE_OPERADO);
        	List<Integer> listaIdTramites = contexto.getTramites();
        	boolean ejecutar = (Boolean)contexto.getParametro(ContextoRefundido.EJECUTAR);
        	
        	log.debug("Aplicar desarrollo. Operado: " + tramiteOperado.getCodigofip() + " Operador: " + tramiteOperador.getCodigofip());
        	
            // Las condiciones para ejecutar el desarrollo son:
            //
            //  1 - El trámite operador ejecuta una operación de desarrollo sobre
            //          otro trámite que puede ser o no el tramite operado.
            //  2 - El trámite operado es el antecesor de nivel más alto en el
            //          árbol ( el que tiene idPadre nulo)
            //  Se averigua si el trámite operador ejecuta una operación
            //      de desarrollo, y contra qué trámite lo hace.
            Tramite jTramite = (Tramite)em.createNamedQuery("Tramite.obtenerDesarrollo")
            		.setParameter("idTramite", tramiteOperador.getIden())
            		.setParameter("idTipoOperacion", ClsDatos.ID_TIPOOPERACIONPLAN_DESARROLLO)
            		.getSingleResult();
            
            // Averigua cuál es el iden del plan raiz, y su trámite
            Tramite tramiteRaiz=null;
            Plan actual = jTramite.getPlan();
            
            do{
                if (actual.getPlanByIdpadre() != null){
                    actual = actual.getPlanByIdpadre();
                } else {
                    // Averigua cuál es el trámite correspondiente al idPlan que hay
                    //  que desarrollar.
                	// No se puede refundir más de un trámite de un mismo plan
                    for (Tramite tramite: actual.getTramites()){
                        if (listaIdTramites.contains(tramite.getIden())){
                            tramiteRaiz=tramite;
                            break;
                        }
                    }
                    actual = null;
                }
            } while (actual != null);

            // Se ejecuta el desarrollo si el trámite raiz es el tramiteO
            if (tramiteRaiz!=null && tramiteOperado.getIden()==tramiteRaiz.getIden()){
                // Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            	contexto.log(LOG.INFO, "==> Desarrollo.");
                contexto.log(LOG.INFO, "Trámite operado: iden=" + tramiteOperado.getIden() + ", fecha=" + tramiteOperado.getFecha() + ", Plan [" + tramiteOperado.getPlan().getCodigo() + "]");
                contexto.log(LOG.INFO, "Trámite operador: iden=" + tramiteOperador.getIden() + ", fecha=" + tramiteOperador.getFecha() + ", Plan [" + tramiteOperador.getPlan().getCodigo() + "]");

                // Si el trámite operador está suspendido, no se ejecuta la operación
                if (ejecutar) {
                	if (!tramiteOperador.getPlan().getBsuspendido()) {
                    	// Averigua cuál es la entidad 'Ámbito de Aplicación' del trámite operador.
                        Entidad iEntidadAAr=gestorConsultas.obtenerAmbitoAplicacionPorTramite(tramiteOperador);
                        if (iEntidadAAr==null){
                        	contexto.log(LOG.AVISO,"Fallo en la operación aplicar desarrollo. " +
                                    "No se ha encontrado la entidad 'Ámbito de Aplicación' " +
                                    "del trámite iden=" + tramiteOperador.getIden() + ". ");
                            // Aunque no se ejecuta el desarrollo, se devuelve TRUE para que se ejecuten
                            //  sus modificaciones, si las tiene
                            return true;
                        }

                        // Averigua cuál es la entidad 'Ámbito de Aplicación' del trámite operado.
                        Entidad ambitoAplicacionTramiteOperado = gestorConsultas.obtenerAmbitoAplicacionPorTramite(tramiteOperado);
                        if (ambitoAplicacionTramiteOperado==null){
                        	contexto.log(LOG.AVISO, "Fallo en la operación aplicar desarrollo. " +
                                    "No se ha encontrado la entidad 'Ámbito de Aplicación' " +
                                    "del trámite iden=" + tramiteOperado.getIden() + ". ");
                            // Aunque no se ejecuta el desarrollo, se devuelve TRUE para que se ejecuten
                            //  sus modificaciones, si las tiene
                            return true;
                        }
                        
                     // Añadir también las determinaciones, excepto virtual, grupo de 
                        //  entidades (y los grupos que son sus valores de referencia)
                        contexto.log(LOG.INFO, "Copia de determinaciones desde [" + tramiteOperador.getPlan().getCodigo() + "] a [" + tramiteOperado.getPlan().getCodigo() +"]");
                        Determinacion iDetPadre = gestorOperacionesDeterminacion.crearCarpetaDeterminacionesAportadas(tramiteOperado, tramiteOperador);
                        
                        List<Determinacion> determinaciones = em.createNamedQuery("Determinacion.buscarAplicacionDesarrollo")
                        		.setParameter("idTramite", tramiteOperador.getIden())
                        		.setParameter("idCaracterVirtual", ClsDatos.ID_CARACTER_VIRTUAL)
                        		.setParameter("idCaracterGrupoEntidades", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
                        		.getResultList();
                        
                        for (Determinacion iDeterminacion : determinaciones){
                            // Reasignar el IdTramite. Se comprueba de nuevo si su trámite es tramiteR, por si acaso ya
                            //  ha sido cambiada de trámite dentro de bReasignarDeterminacion, por ser hija de una de la
                            //  lista que ya ha sido procesada.
//                            if (iDeterminacion.getTramite().getIden()==tramiteOperador.getIden()){
                            	contexto.log(LOG.INFO, "Aportando  [" + iDeterminacion.getCodigo() + "] (" + 
                                        iDeterminacion.getNombre() + ") del plan [" +
                                        iDeterminacion.getTramite().getPlan().getCodigo() + "]");
                                gestorOperacionesDeterminacion.reasignarDeterminacion(iDetPadre, iDetPadre, iDeterminacion, 0, contexto);
//                            }
                        }

                        // Averigua cuál es la determinación 'Grupo de Entidades' de los
                        //  trámites operador y operado.
                        Determinacion iDetGrupoDeEntidadesR=gestorConsultas.obtenerDeterminacionGrupoDeEntidadesPorTramite(tramiteOperador);
                        Determinacion iDetGrupoDeEntidadesO=gestorConsultas.obtenerDeterminacionGrupoDeEntidadesPorTramite(tramiteOperado);

                        // Selecciona las entidades del trámite operado que pertenecen a alguno de
                        //  los grupos de las entidades del trámite operador.
                        
                        List<Entidad> entidadesOperadas = em.createNamedQuery("Entidad.buscarEntidadesTramiteOperado")
                        	.setParameter("idDetGrupoEntidadesO", iDetGrupoDeEntidadesO.getIden())
                        	.setParameter("idDetGrupoEntidadesR", iDetGrupoDeEntidadesR.getIden())
                        	.setParameter("idTramiteO", tramiteOperado.getIden())
                        	.setParameter("idTramiteR", tramiteOperador.getIden())
                        	.setParameter("idEntidad", ambitoAplicacionTramiteOperado.getIden())
                        	.getResultList();
                        
                        for (Entidad iEntidadO : entidadesOperadas){
                        	contexto.log(LOG.INFO,"Recortando [" + iEntidadO.getCodigo() + "] del plan [" +
                                    iEntidadO.getTramite().getPlan().getCodigo() + "]");
                            // Efectúa la sustracción gráfica.
                            gestorOperacionesEntidad.sustraccionGrafica(iEntidadO, iEntidadAAr, tramiteOperador, contexto);
                        }

                        // Añade las entidades del trámite operador al trámite operado.
                        contexto.log(LOG.INFO,"Copia de entidades desde [" + tramiteOperador.getPlan().getCodigo() + "] a [" + tramiteOperado.getPlan().getCodigo() +"]");
                       
                        List<Entidad> entidadesOperadoras = em.createNamedQuery("Entidad.buscarRaiz")
                        		.setParameter("idTramite", tramiteOperador.getIden())
                        		.getResultList();
                        
                        for (Entidad entidadOperadora : entidadesOperadoras){
                        	if (entidadOperadora.getIden() == iEntidadAAr.getIden()) {
                        		List<Entidad> hijas = em.createNamedQuery("Entidad.buscarHijas")
                                		.setParameter("idPadre", entidadOperadora.getIden())
                                		.getResultList();
                        		for (Entidad hija : hijas) {
                        			contexto.log(LOG.INFO, "Aportando  [" + hija.getCodigo() + "] del plan [" +
                                            entidadOperadora.getTramite().getPlan().getCodigo() + "]");
                            		
                                    // Efectúa la aportación de entidad.
                                    gestorOperacionesEntidad.aportacionEntidad(ambitoAplicacionTramiteOperado, hija, contexto);
                        		}
                        	} else {
                        		contexto.log(LOG.INFO, "Aportando  [" + entidadOperadora.getCodigo() + "] del plan [" +
                                        entidadOperadora.getTramite().getPlan().getCodigo() + "]");

                                // Efectúa la aportación de entidad.
                                gestorOperacionesEntidad.aportacionEntidad(ambitoAplicacionTramiteOperado, entidadOperadora, contexto);
                        	}
                        }
                    } else {
                    	contexto.log(LOG.INFO, "** SUSPENDIDO** El trámite operador está suspendido " + 
                                "y no se ejecuta el desarrollo del plan [" + 
                                tramiteOperador.getPlan().getCodigo() + "] sobre " +
                                "el plan [" + tramiteOperado.getPlan().getCodigo() + "].");
                        copiarAmbitoAplicacion(tramiteOperado, tramiteOperador);
                        return false;
                    }
                } 
            } else {
            	return false;
            }

        } catch (NoResultException nre) {
        	return false;
        } catch(Exception e){
        	contexto.log(LOG.ERROR, "Fallo en la operación aplicarDesarrollo. " + e.getMessage() + ". Código 23302." );
            return false;
        }

        return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesPlanLocal#aplicarIncorporacion(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void aplicarIncorporacion(ContextoRefundido contexto) {
        try{
            Tramite tramiteOperado = (Tramite) contexto.getParametro(TRAMITE_OPERADO);
            Tramite tramiteOperador = (Tramite) contexto.getParametro(TRAMITE_OPERADOR);
            
			// Averigua si hay una operación de Incorporacion entre ambos trámites
            if(em.createNamedQuery("Operacionplan.buscarOperacion")
                	.setParameter("idPlanOperado", tramiteOperado.getPlan().getIden())
                	.setParameter("idPlanOperador", tramiteOperador.getPlan().getIden())
                	.setParameter("idTipoOperacion", ClsDatos.ID_TIPOOPERACIONPLAN_INCORPORACION)
                	.getResultList().size() >0) {
            	
                contexto.log(LOG.AVISO, "La operación 'Incorporación de Plan' está obsoleta desde la versión 1.9.55 del proceso de refundido.");
               
            }
        }catch(Exception e){
            contexto.log(LOG.AVISO, "Fallo en la operación incorporacion. " + e.getMessage());
        }
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesPlanLocal#aplicarModificacion(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void aplicarModificacion(ContextoRefundido contexto) throws ExcepcionRefundido {
        Operaciondeterminacion iOperacionDeterminacion;
        Operacionentidad iOperacionEntidad;
        Operacionrelacion iOperacionRelacion;
        Determinacion iDeterminacionO;
        Entidad iEntidadO;
        Relacion iRelacionO;
        boolean operar;
        boolean existeOperacion;
        
        boolean ejecutar = (Boolean) contexto.getParametro(ContextoRefundido.EJECUTAR);
        Tramite tramiteR = (Tramite) contexto.getParametro(TRAMITE_OPERADOR);
        Tramite tramiteO = (Tramite) contexto.getParametro(TRAMITE_OPERADO);
        int soloOperadores = (Integer) contexto.getParametro(SOLO_OPERADORES);
        
        log.debug("Aplicar modificación. Operado: " + tramiteO.getCodigofip() + " Operador: " + tramiteR.getCodigofip());
        
        // Para controlar el contenido de los mensajes, se usa la variable tipoModificación
        //      1 = Operaciones entre determinaciones
        //      2 = Operaciones entre entidades
        //      3 = Operaciones sobre relaciones
    	int tipoModificacion=0;

        // Ordena las operaciones del trámite operador por el campo 'orden'
        List<Operacion> operaciones = em.createNamedQuery("Operacion.buscarPorTramite")
        		.setParameter("idTramite", tramiteR.getIden())
        		.getResultList();
        if (operaciones.size() >0) {
        	// Si el trámite operador está suspendido, no se ejecuta la operación
            if(ejecutar){
                if (tramiteR.getPlan().getBsuspendido()){
                    contexto.log(LOG.INFO,"** SUSPENDIDO** El trámite operador está suspendido " + 
                            "y no se ejecuta la modificación del plan [" + 
                            tramiteR.getPlan().getCodigo() + "] sobre " +
                            "el plan [" + tramiteO.getPlan().getCodigo() + "].");
                    copiarAmbitoAplicacion(tramiteO, tramiteR);
                    return;
                }
            }

            for (Operacion iOperacion : operaciones) {
                existeOperacion=false;  // Sirve para detectar operaciones que han sido borradas antes de ser ejecutadas.

                // Se da por hecho que una Operacion sólo puede ser apuntada por
                //  una OperacionDeterminacion o una OperacionEntidad. Por lo tanto,
                //  aunque la clase Operacion contiene un Set de OperacionDeterminacion,
                //  sólo debe contener un elemento y, por consiguiente, solamente se
                //  toma el primero.

                // *******************************************
                // Si la operación es entre determinaciones...
                 
                if (iOperacion.getOperaciondeterminacions().size() > 0) {
                    existeOperacion=true;
                    iOperacionDeterminacion = iOperacion.getOperaciondeterminacions().toArray(new Operaciondeterminacion[0])[0];
                    
                    iDeterminacionO=iOperacionDeterminacion.getDeterminacionByIddeterminacion();
                    operar=false;
                    // Solo se opera si la determinacion operada pertenece al trámite operado.
                    //  La operadora puede o no pertenecer al trámite operador.
                    if (iDeterminacionO.getTramite().getIden()==tramiteO.getIden()){
                        if(soloOperadores==1){
                            // Si soloOperadores=1, sólo se opera la iDeterminacionO si ésta, a su
                            //  vez, es operadora.
                            if (iDeterminacionO.getOperaciondeterminacionsForIddeterminacionoperadora().size() > 0){
                                operar=true;
                            }
                        } else{
                            operar=true;
                        }

                        if(operar){
                            if(tipoModificacion!=1){
                                
                            	contexto.log(LOG.INFO,"==> Operaciones sobre determinaciones " + sOperadores[soloOperadores] 
                                		+ " del plan [" + tramiteO.getPlan().getCodigo() 
                                		+ "] ordenadas por el plan [" + tramiteR.getPlan().getCodigo() + "]");
                            }
                            tipoModificacion=1;
                            if(ejecutar){
                                gestorOperacionesDeterminacion.ejecutar(iOperacionDeterminacion,contexto);
                            }
                        }
                    }
                } else if (iOperacion.getOperacionentidads().size() > 0) {
                	// Si la operación es entre entidades...
                    existeOperacion=true;
                    iOperacionEntidad = iOperacion.getOperacionentidads().toArray(new Operacionentidad[0])[0];
                    iEntidadO=iOperacionEntidad.getEntidadByIdentidad();
                    operar=false;
                    // Solo se opera si la entidad operada pertenece al trámite operado.
                    //  La operadora puede o no pertenecer al trámite operador.
                    if (iEntidadO.getTramite().getIden()==tramiteO.getIden()){
                        if(soloOperadores==1){
                            // Si soloOperadores=1, sólo se opera la iEntidadO si ésta, a su
                            //  vez, es operadora.
                            if (iEntidadO.getOperacionentidadsForIdentidadoperadora().size()>0){
                                operar=true;
                            }
                        } else{
                            operar=true;
                        }

                        if(operar){
                            if(tipoModificacion!=2){
                            	contexto.log(LOG.INFO,"==> Operaciones sobre entidades " + sOperadores[soloOperadores] 
                                		+ " del plan [" + tramiteO.getPlan().getCodigo() 
                                		+ "] ordenadas por el plan [" + tramiteR.getPlan().getCodigo() + "]");
                            }
                            tipoModificacion=2;
                            if(ejecutar){
                            	gestorOperacionesEntidad.ejecutar(iOperacionEntidad, contexto);
                            }
                        }
                    }
                } else if (iOperacion.getOperacionrelacions().size() > 0) {
                	// Si la operación es de relaciones...
                    existeOperacion=true;
                    iOperacionRelacion = iOperacion.getOperacionrelacions().toArray(new Operacionrelacion[0])[0];
                    iRelacionO=iOperacionRelacion.getRelacion();
                    
                    // Solo se opera si la relacion operada ha sido creada por el trámite operado.
                    if (iRelacionO.getTramiteByIdtramitecreador().getIden()==tramiteO.getIden()){
                        // Si soloOperadores=1, no se opera.
                        if(soloOperadores==0){
                            if(tipoModificacion!=3){
                            	contexto.log(LOG.INFO,"==> Operaciones sobre relaciones " + sOperadores[soloOperadores] 
                                		+ " del plan [" + tramiteO.getPlan().getCodigo() 
                                		+ "] ordenadas por el plan [" + tramiteR.getPlan().getCodigo()+ "]");
                            }
                            tipoModificacion=3;
                            if(ejecutar){
                            	contexto.putParametro(GestorOperacionesRelacionLocal.OPERACION_RELACION, iOperacionRelacion);
                            	gestorOperacionesRelacion.ejecutar(contexto);
                            }
                        }
                    }
                }

                if(!existeOperacion && ejecutar){
                    // La operación ha sido borrada antes de ser ejecutada y ha quedado un
                    //  registro huérfano en la tabla Operacion. Probablemente, una operación
                    //  anterior haya eliminado alguno de los elementos que intervenían en ella.
                	contexto.log(LOG.AVISO,"*** La operación iden=" + iOperacion.getIden() + " no se puede ejecutar. Alguno de sus elementos ha sido eliminado en una operación anterior.");
                	eliminadorEntidades.eliminar(iOperacion, null);   
                }
            }
        } else {
        	log.debug("No tiene operaciones");
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesPlanLocal#aplicarRefundido(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void aplicarRefundido(ContextoRefundido contexto) throws ExcepcionRefundido {
		Tramite tramiteOperador = (Tramite)contexto.getParametro(TRAMITE_OPERADOR);
    	Tramite tramiteOperado = (Tramite)contexto.getParametro(TRAMITE_OPERADO);
    	
    	log.debug("Aplicar sustitución. Operado: " + tramiteOperado.getCodigofip() + " Operador: " + tramiteOperador.getCodigofip());
    	
    	boolean ejecutar = (Boolean)contexto.getParametro(ContextoRefundido.EJECUTAR);
    	
        // Averigua si el plan del trámite tramiteOperador sustituye al del tramiteOperado
        if(em.createNamedQuery("Operacionplan.buscarOperacion")
            	.setParameter("idPlanOperado", tramiteOperado.getPlan().getIden())
            	.setParameter("idPlanOperador", tramiteOperador.getPlan().getIden())
            	.setParameter("idTipoOperacion", 10)
            	.getResultList().size() >0){

        	contexto.log(LOG.INFO, "==> Refundido.");
        	contexto.log(LOG.INFO, "        Trámite operado: iden=" + tramiteOperado.getIden() + ", fecha=" + tramiteOperado.getFecha() + ", Plan [" + tramiteOperado.getPlan().getCodigo() + "] ");
        	contexto.log(LOG.INFO, "        Trámite operador: iden=" + tramiteOperador.getIden() + ", fecha=" + tramiteOperador.getFecha() + ", Plan [" + tramiteOperador.getPlan().getCodigo() + "] ");
          
            if(ejecutar){
            	// Si el trámite operador está suspendido, no se ejecuta la operación
            	
                if (tramiteOperador.getPlan().getBsuspendido()){
                	contexto.log(LOG.INFO, "** SUSPENDIDO** El trámite operador está suspendido " + 
                            "y no se ejecuta la sustitución del plan [" + 
                            tramiteOperador.getPlan().getCodigo() + "] sobre " +
                            "el plan [" + tramiteOperado.getPlan().getCodigo() + "].");
                    copiarAmbitoAplicacion(tramiteOperado, tramiteOperador);
                    return;
                }

                contexto.log(LOG.INFO, "Eliminando Plan [" + tramiteOperado.getPlan().getCodigo()+ "]");

                Plan planOperado = em.find(Plan.class,tramiteOperado.getPlan().getIden());

                if (planOperado != null) {
                	eliminadorEntidades.eliminar(planOperado, null);
                    em.flush();
                }
            } else {
            	log.debug("No es ciclo de ejecución");
            }
        } else {
        	log.debug("No tiene operación de refundido");
        }
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesPlanLocal#aplicarRevocacionParcial(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void aplicarRevocacionParcial(ContextoRefundido contexto) {
        try{
            // Averigua si hay una operación de Revocación Parcial entre ambos trámites
        	Tramite tramiteOperado = (Tramite)contexto.getParametro(TRAMITE_OPERADO);
        	Tramite tramiteOperador = (Tramite)contexto.getParametro(TRAMITE_OPERADOR);
        	boolean ejecutar = (Boolean)contexto.getParametro(ContextoRefundido.EJECUTAR);
        	
            if(em.createNamedQuery("Operacionplan.buscarOperacion")
                	.setParameter("idPlanOperado", tramiteOperado.getPlan().getIden())
                	.setParameter("idPlanOperador", tramiteOperador.getPlan().getIden())
                	.setParameter("idTipoOperacion", ClsDatos.ID_TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL)
                	.getResultList().size() >0) {
            	
            	// Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
                contexto.log(LOG.INFO, "==> Revocación Parcial.");
                contexto.log(LOG.INFO, "        Trámite operado: iden=" + tramiteOperado.getIden() + ", fecha=" + tramiteOperado.getFecha() + ", Plan [" + tramiteOperado.getPlan().getCodigo() + "]");
                contexto.log(LOG.INFO, "        Trámite operador: iden=" + tramiteOperador.getIden() + ", fecha=" + tramiteOperador.getFecha() + ", Plan [" + tramiteOperador.getPlan().getCodigo() + "]");

                if(ejecutar){
                	// Si el trámite operador está suspendido, no se ejecuta la operación
                    if (tramiteOperador.getPlan().getBsuspendido()){
                    	contexto.log(LOG.INFO, "        ** SUSPENDIDO** El trámite operador está suspendido y no se ejecuta la revocación.");
                        copiarAmbitoAplicacion(tramiteOperado, tramiteOperador);
                        return;
                    }
                    
                    // Se revoca parcialmente la suspensión de todas las entidades del tramite
                    //  operado mediante el ámbito de aplicación del trámite operador.
                    Entidad iEntR=gestorConsultas.obtenerAmbitoAplicacionPorTramite(tramiteOperador);
                    if (iEntR!=null){
                    	List<Entidad> entidadesOperadas = em.createNamedQuery("Entidad.buscarPorTramite")
                        		.setParameter("idTramite", tramiteOperado.getIden())
                        		.getResultList();
                        for (Entidad entidadOperada: entidadesOperadas){
                        	gestorOperacionesEntidad.levantamientoParcialSuspension(entidadOperada, iEntR, contexto);
                        }
                        contexto.log(LOG.INFO, "         Se ha revocado parcialmente la suspensión de las entidades del " + 
                                "plan " + tramiteOperado.getPlan().getNombre());
                    } else {
                    	contexto.log(LOG.AVISO, "         No se ha revocado parcialmente la suspensión del " + 
                                "plan <" + tramiteOperado.getPlan().getNombre()+
                                "> porque no se ha encontrado el ámbito de aplicación del " +
                                "plan operador <" + 
                                tramiteOperador.getPlan().getNombre() + ">");
                    }
                }
            }
            
        } catch(Exception e){
        	contexto.log(LOG.ERROR, "Fallo en la operación aplicarRevocacionParcial. " + e.getMessage() + ". Código 23307.");
            log.error("Fallo en la operación aplicarRevocacionParcial. " + e.getMessage() + ". Código 23307." );
        }
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesPlanLocal#aplicarRevocacionTotal(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void aplicarRevocacionTotal(ContextoRefundido contexto) {
        try{
            // Averigua si hay una operación de Revocación entre ambos trámites
        	Tramite tramiteOperado = (Tramite)contexto.getParametro(TRAMITE_OPERADO);
        	Tramite tramiteOperador = (Tramite)contexto.getParametro(TRAMITE_OPERADOR);
        	boolean ejecutar = (Boolean)contexto.getParametro(ContextoRefundido.EJECUTAR);
        	
            if(em.createNamedQuery("Operacionplan.buscarOperacion")
                	.setParameter("idPlanOperado", tramiteOperado.getPlan().getIden())
                	.setParameter("idPlanOperador", tramiteOperador.getPlan().getIden())
                	.setParameter("idTipoOperacion", ClsDatos.ID_TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL)
                	.getResultList().size() >0) {
            	// Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            	contexto.log(LOG.INFO, "==> Revocación Total.");
            	contexto.log(LOG.INFO, "        Trámite operado: iden=" + tramiteOperado.getIden() + ", fecha=" + tramiteOperado.getFecha() + ", Plan [" + tramiteOperado.getPlan().getCodigo() + "]");
            	contexto.log(LOG.INFO, "        Trámite operador: iden=" + tramiteOperador.getIden() + ", fecha=" + tramiteOperador.getFecha() + ", Plan [" + tramiteOperador.getPlan().getCodigo() + "]");

                if(ejecutar){
                	// Si el trámite operador está suspendido, no se ejecuta la operación
                    if (tramiteOperador.getPlan().getBsuspendido()){
                    	contexto.log(LOG.INFO, "        ** SUSPENDIDO** El trámite operador está suspendido " + 
                                "y no se ejecuta la suspensión total del plan [" + 
                                tramiteOperador.getPlan().getCodigo() + "] sobre " +
                                "el plan [" + tramiteOperado.getPlan().getCodigo() + "].");
                        copiarAmbitoAplicacion(tramiteOperado, tramiteOperador);
                        return;
                    }

                    // Quita la marca de bSuspendido en el
                    tramiteOperado.getPlan().setBsuspendido(false);
                   
                    contexto.log(LOG.INFO, "         Se ha revocado la suspensión del plan del trámite iden=" + tramiteOperado.getIden());
                }
            }

        }catch(Exception e){
        	contexto.log(LOG.ERROR, "Fallo en la operación aplicarRevocacionTotal. " + e.getMessage() + ". Código 23306." );
            log.error("Fallo en la operación aplicarRevocacionTotal. " + e.getMessage() + ". Código 23306." );
        }
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesPlanLocal#aplicarSuspensionParcial(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void aplicarSuspensionParcial(ContextoRefundido contexto) {
        try{
            // Averigua si hay una operación de Suspensión Parcial entre ambos trámites
        	Tramite tramiteOperado = (Tramite)contexto.getParametro(TRAMITE_OPERADO);
        	Tramite tramiteOperador = (Tramite)contexto.getParametro(TRAMITE_OPERADOR);
        	boolean ejecutar = (Boolean)contexto.getParametro(ContextoRefundido.EJECUTAR);
        	
            if(em.createNamedQuery("Operacionplan.buscarOperacion")
                	.setParameter("idPlanOperado", tramiteOperado.getPlan().getIden())
                	.setParameter("idPlanOperador", tramiteOperador.getPlan().getIden())
                	.setParameter("idTipoOperacion", ClsDatos.ID_TIPOOPERACIONPLAN_SUSPENSIONPARCIAL)
                	.getResultList().size() >0) {
            	// Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)
            	contexto.log(LOG.INFO, "==> Suspensión Parcial.");
            	contexto.log(LOG.INFO, "        Trámite operado: iden=" + tramiteOperado.getIden() + ", fecha=" + tramiteOperado.getFecha() + ", Plan [" + tramiteOperado.getPlan().getCodigo() + "]");
            	contexto.log(LOG.INFO, "        Trámite operador: iden=" + tramiteOperador.getIden() + ", fecha=" + tramiteOperador.getFecha() + ", Plan [" + tramiteOperador.getPlan().getCodigo() + "]");

                if(ejecutar){
                	// Si el trámite operador está suspendido, no se ejecuta la operación
                    if (tramiteOperador.getPlan().getBsuspendido()){
                    	contexto.log(LOG.INFO, "        ** SUSPENDIDO** El trámite operador está suspendido y no se ejecuta la suspensión.");
                        copiarAmbitoAplicacion(tramiteOperado, tramiteOperador);
                        return;
                    }
                    
                    // Se suspenden parcialmente todas las entidades del tramite
                    //  operado mediante el ámbito de aplicación del trámite operador.
                    Entidad iEntR = gestorConsultas.obtenerAmbitoAplicacionPorTramite(tramiteOperador);
                    if (iEntR!=null){ 
                        List<Entidad> entidadesOperadas = em.createNamedQuery("Entidad.buscarPorTramite")
                        		.setParameter("idTramite", tramiteOperado.getIden())
                        		.getResultList();
                        for (Entidad entidadOperada: entidadesOperadas){
                            gestorOperacionesEntidad.suspensionParcial(entidadOperada, iEntR, contexto);
                        }
                        contexto.log(LOG.INFO, "Se han suspendido parcialmente las entidades del " + 
                                "plan " +tramiteOperado.getPlan().getNombre());
                    } else {
                    	contexto.log(LOG.AVISO, "No se ha suspendido parcialmente el " + 
                                "plan <" + tramiteOperado.getPlan().getNombre()+
                                "> porque no se ha encontrado el ámbito de aplicación del " +
                                "plan operador <" + 
                                tramiteOperador.getPlan().getNombre() + ">");
                    }
                }
            }
        }catch(Exception e){
        	contexto.log(LOG.ERROR, "Fallo en la operación aplicarSuspensionParcial. " + e.getMessage() + ". Código 23303." );
            log.error("Fallo en la operación aplicarSuspensionParcial. " + e.getMessage() + ". Código 23303." );
        }
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesPlanLocal#aplicarSuspensionTotal(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void aplicarSuspensionTotal(ContextoRefundido contexto) {
        try{
            // Averigua si hay una operación de Suspensión entre ambos trámites
        	Tramite tramiteOperado = (Tramite)contexto.getParametro(TRAMITE_OPERADO);
        	Tramite tramiteOperador = (Tramite)contexto.getParametro(TRAMITE_OPERADOR);
        	boolean ejecutar = (Boolean)contexto.getParametro(ContextoRefundido.EJECUTAR);
        	
            if(em.createNamedQuery("Operacionplan.buscarOperacion")
                	.setParameter("idPlanOperado", tramiteOperado.getPlan().getIden())
                	.setParameter("idPlanOperador", tramiteOperador.getPlan().getIden())
                	.setParameter("idTipoOperacion", ClsDatos.ID_TIPOOPERACIONPLAN_SUSPENSIONTOTAL)
                	.getResultList().size() >0){
            	// Averigua los códigos de plan de los trámites (se hace sólo para los mensajes)

            	contexto.log(LOG.INFO, "==> Suspensión Total.");
            	contexto.log(LOG.INFO, "        Trámite operado: iden=" + tramiteOperado.getIden() + ", fecha=" + tramiteOperado.getFecha() + ", Plan [" + tramiteOperado.getPlan().getCodigo() + "]");
            	contexto.log(LOG.INFO, "        Trámite operador: iden=" + tramiteOperador.getIden() + ", fecha=" + tramiteOperador.getFecha() + ", Plan [" + tramiteOperador.getPlan().getCodigo() + "]");

                if(ejecutar){
                	// Si el trámite operador está suspendido, no se ejecuta la operación
                    if (tramiteOperador.getPlan().getBsuspendido()){
                    	contexto.log(LOG.INFO, "        ** SUSPENDIDO** El trámite operador está suspendido " + 
                                "y no se ejecuta la suspensión total del plan [" + 
                                tramiteOperador.getPlan().getCodigo() + "] sobre " +
                                "el plan [" + tramiteOperado.getPlan().getCodigo() + "].");
                        copiarAmbitoAplicacion(tramiteOperado, tramiteOperador);
                        return;
                    }

                    // Pone la marca de bSuspendido en el
                    tramiteOperado.getPlan().setBsuspendido(true);
                    
                    contexto.log(LOG.INFO, "         Se ha suspendido el plan del trámite iden=" + tramiteOperado.getIden());
                }
            }

        }catch(Exception e){
        	contexto.log(LOG.ERROR, "Fallo en la operación aplicarSuspensionTotal. " + e.getMessage() + ". Código 23303." );
            log.error("Fallo en la operación aplicarSuspensionTotal. " + e.getMessage() + ". Código 23303." );
        }
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesPlanLocal#aplicarSustitucion(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, boolean, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void aplicarSustitucion(ContextoRefundido contexto) throws ExcepcionRefundido {
        Entidad iEntR;
        String tipoGeom;
        Determinacion iDetGrupoO;
        Determinacion iDetGrupoR;

        try{
        	Tramite tramiteOperador = (Tramite)contexto.getParametro(TRAMITE_OPERADOR);
        	Tramite tramiteOperado = (Tramite)contexto.getParametro(TRAMITE_OPERADO);
        	
        	log.debug("Aplicar sustitución. Operado: " + tramiteOperado.getCodigofip() + " Operador: " + tramiteOperador.getCodigofip());
        	
        	List<Integer> listaIdTramites = contexto.getTramites();
        	boolean ejecutar = (Boolean)contexto.getParametro(ContextoRefundido.EJECUTAR);
        	
            // Averigua si el plan del trámite tramiteOperador sustituye al del tramiteOperado
            if(em.createNamedQuery("Operacionplan.buscarOperacion")
                	.setParameter("idPlanOperado", tramiteOperado.getPlan().getIden())
                	.setParameter("idPlanOperador", tramiteOperador.getPlan().getIden())
                	.setParameter("idTipoOperacion", ClsDatos.ID_TIPOOPERACIONPLAN_SUSTITUCION)
                	.getResultList().size() >0){

            	contexto.log(LOG.INFO, "==> Sustitución.");
            	contexto.log(LOG.INFO, "        Trámite operado: iden=" + tramiteOperado.getIden() + ", fecha=" + tramiteOperado.getFecha() + ", Plan [" + tramiteOperado.getPlan().getCodigo() + "] ");
            	contexto.log(LOG.INFO, "        Trámite operador: iden=" + tramiteOperador.getIden() + ", fecha=" + tramiteOperador.getFecha() + ", Plan [" + tramiteOperador.getPlan().getCodigo() + "] ");
              
                if(ejecutar){
                	// Si el trámite operador está suspendido, no se ejecuta la operación
                	
                    if (tramiteOperador.getPlan().getBsuspendido()){
                    	contexto.log(LOG.INFO, "** SUSPENDIDO** El trámite operador está suspendido " + 
                                "y no se ejecuta la sustitución del plan [" + 
                                tramiteOperador.getPlan().getCodigo() + "] sobre " +
                                "el plan [" + tramiteOperado.getPlan().getCodigo() + "].");
                        copiarAmbitoAplicacion(tramiteOperado, tramiteOperador);
                        return;
                    }

                    // Si el trámite sustituto tiene algunas entidades suspendidas, hay que incorporar del
                    //  trámite sustituído, las entidades que caen debajo de aquellas y que son
                    //  del mismo grupo de entidades. De esta manera, quedará vigente todo el trámite
                    //  sustituto, excepto en aquellos sitios donde esté suspendido, donde seguirá
                    //  vigente el contenido del trámite sustituído.
                    List<Entidad> entidadesSuspendidas = em.createNamedQuery("Entidad.BuscarSuspendidasPorTramite")
                    		.setParameter("idTramite", tramiteOperador.getIden())
                    		.getResultList();
                    if (entidadesSuspendidas.size()>0){
                    	contexto.log(LOG.INFO, "El trámite sustituto tiene entidades suspendidas. Se dejarán vigentes las correspondientes del trámite sustituído.");
                        
                        for (Entidad entidad : entidadesSuspendidas){
                           
                            tipoGeom=gestorOperacionesEntidad.getGeometria(entidad, contexto);
                            
                            iDetGrupoO=(Determinacion) em.createNamedQuery("Determinacion.obtenerGrupoEntidad")
                            		.setParameter("idEntidad", entidad.getIden())
                            		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
                            		.getSingleResult();
                            
                            if (!tipoGeom.equals("") && !tipoGeom.equalsIgnoreCase("Error")){
                                // Averigua qué entidades de tramiteOperador intersectan con la entidad suspendida
                                List<Integer> eInterseccion =em.createNativeQuery(String.format(SELECT_INTERSECCION, tipoGeom, tipoGeom, entidad.getIden(), tramiteOperador.getIden()))
                                		.getResultList();
                                
                                for (Integer iden : eInterseccion){
                                    // Las entidades de tramiteOperador que intersectan con las
                                    //  entidades suspendidas de tramiteOperado, y que son del
                                    //  mismo grupo que ellas, se incorporan
                                    //  al tramiteOperado antes de hacer la sustitución de plan.
                                    
                                    iEntR=em.find(Entidad.class, iden);
                                    iDetGrupoR=gestorConsultas.obtenerDeterminacionGrupoEquivalentePorEntidad(iEntR, tramiteOperado, listaIdTramites);
                                    iDetGrupoR = (Determinacion) em.createNamedQuery("Determinacion.obtenerGrupoEntidad")
    	                        		.setParameter("idEntidad", iEntR.getIden())
    	                        		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
    	                        		.getSingleResult();
                                    if(iDetGrupoO.getIden()==iDetGrupoR.getIden()){
                                    	contexto.log(LOG.INFO, "La entidad [" + entidad.getCodigo() + "] del plan [" + entidad.getTramite().getPlan().getCodigo() + "] está suspendida, y queda vigente la entidad [" + iEntR.getCodigo() + "] del plan [" + iEntR.getTramite().getPlan().getCodigo() + "]");
                                        gestorOperacionesEntidad.incorporacionEntidad(entidad, iEntR, contexto);
                                    }
                                }
                            }
                        }
                    }

                    em.flush();
                    contexto.log(LOG.INFO, "Eliminando Plan [" + tramiteOperado.getPlan().getCodigo()+ "]");

                    Plan planOperado = em.find(Plan.class,tramiteOperado.getPlan().getIden());

                    if (planOperado != null) {
                    	eliminadorEntidades.eliminar(planOperado, null);
                        em.flush();
                    }
                } else {
                	log.debug("No es ciclo de ejecución");
                }
            } else {
            	log.debug("No tiene operación de sustitución");
            }
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación aplicarSustitucion. " + e.getMessage(), 23304);
        }
	}

	/**
	 * Se copia la entidad 'Ámbito de aplicación' del trámite iTramiteDesde al
	 * trámite iTramiteHasta, cambiándolo al grupo 'Afección' y poniéndole el nombre,
	 * compuesto por el texto '(Suspendido)' y el nombre del plan del iTramiteDesde.
	 * 
	 * @param iTramiteHasta
	 * @param iTramiteDesde
	 * @throws ExcepcionRefundido 
	 */
	@SuppressWarnings("unchecked")
	private void copiarAmbitoAplicacion(Tramite iTramiteHasta, Tramite iTramiteDesde) throws ExcepcionRefundido{
         
        Opciondeterminacion iOpcion;
        
     // Crea la entidad en iTramiteHasta
        String codigoEnt = gestorConsultas.getSiguienteCodigoEntidad(iTramiteHasta.getIden());
        String txtEntidad="(Suspendido) " + iTramiteDesde.getPlan().getNombre();
        if (txtEntidad.length()>100){
            txtEntidad=txtEntidad.substring(0, 99);
        }
        Entidad entidad = new Entidad();
        entidad.setTramite(iTramiteHasta);
        entidad.setEntidadByIdpadre(null);
        entidad.setClave("Plan suspendido");
        entidad.setNombre(txtEntidad);
        entidad.setCodigo(codigoEnt);
        entidad.setBsuspendida(false);
        em.persist(entidad);
        
        Plan planBase;
        if (iTramiteHasta.getPlan().getPlanByIdplanbase() != null) {
        	planBase = iTramiteHasta.getPlan().getPlanByIdplanbase();
        } else if (iTramiteDesde.getPlan().getPlanByIdplanbase() != null) {
        	planBase = iTramiteDesde.getPlan().getPlanByIdplanbase();
        } else {
        	throw new ExcepcionRefundido("No se ha podido encontrar el plan base para el ámbito de aplicación", 99999);
        }
        
        List<Tramite> tramitesPlanBase = em.createNamedQuery("Tramite.findVigente")
        		.setParameter("idPlan", planBase.getIden())
        		.getResultList();

        // Averigua cuál es la determinación 'Grupo de Entidades' del trámite del plan base
        Determinacion iDet=gestorConsultas.obtenerDeterminacionGrupoDeEntidadesPorTramite(tramitesPlanBase.get(0));
        
        // Averigua cuál es la determinación 'Valor de Referencia' correspondiente 
        //  al grupo de entidades 'Afección'. Si no existe, se crea. Y, además, se crea
        //  la opción correspondiente.
        List<Determinacion> valoresReferencia = em.createNamedQuery("Determinacion.obtenerValorDeReferencia")
        		.setParameter("codigoAfeccion", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_AFECCION)
        		.setParameter("caracterValorRef", ClsDatos.ID_CARACTER_VALORREFERENCIA)
        		.setParameter("idTramite", tramitesPlanBase.get(0).getIden())
        		.getResultList();
        		
        if (valoresReferencia.size() > 0){
        	List<Opciondeterminacion> opciones = em.createNamedQuery("Opciondeterminacion.buscarPorDetYValorRef")
            		.setParameter("idDeterminacion", iDet.getIden())
            		.setParameter("idValorRef", valoresReferencia.get(0).getIden())
            		.getResultList();
            if (opciones.size() >0) {
            	iOpcion = opciones.get(0);
            	
            	// Inserta los registros de valor en EntidadDeterminacion, CasoEntidadDeterminacion, y
                //  EntidadDeterminacionRegimen

                // EntidadDeterminacion
                Entidaddeterminacion ed = new Entidaddeterminacion();
                
                ed.setEntidad(entidad);
                ed.setDeterminacion(iDet);
                em.persist(ed);

                // CasoEntidadDeterminacion
                Casoentidaddeterminacion ced = new Casoentidaddeterminacion();
                ced.setEntidaddeterminacion(ed);
                em.persist(ced);

                // EntidadDeterminacionRegimen
                Entidaddeterminacionregimen edr = new Entidaddeterminacionregimen();
                edr.setCasoentidaddeterminacionByIdcaso(ced);
                edr.setOpciondeterminacion(iOpcion);
                edr.setSuperposicion(0);
                em.persist(edr);
            } else {
            	throw new ExcepcionRefundido("No existe Opciondeterminacion para " + iDet.getIden() + " y " + valoresReferencia.get(0).getIden(), 23045);
            }
        } else {
        	throw new ExcepcionRefundido("No existe valor de referencia en el plan base" + planBase.getCodigo(), 23045);
        }
    }

}
