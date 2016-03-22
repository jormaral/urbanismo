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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorConsultasLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 *
 * @author Arnaiz Consultores
 */
@Stateless (name="ServicioRefundido")
public class RefundidoBean implements RefundidoLocal {
	
	private static final Logger log = Logger.getLogger(RefundidoBean.class);
	
	private static final String SELECT_TABLAS_HUERFANAS = "Select Distinct Cast(tb.nombre As Text) From diccionario.Tabla tb";
	private static final String SELECT_RELACIONES_HUERFANAS = "Select r.iden From planeamiento.relacion r, planeamiento.vectorrelacion vr, " +
            "diccionario.defvector dv, diccionario.tabla tb " +
            "Where Trim(Upper(tb.nombre))='%s' " +
            "And dv.idtabla=tb.iden " +
            "And vr.iddefvector=dv.iden " +
            "And vr.idrelacion=r.iden " +
            "And vr.valor Not In (Select t.iden From planeamiento.%s t) " +
            "And Cast (vr.valor As Integer)>0 ";
	
	private final static Set<Integer> tipoTramitesRefundibles;

	private static final String COMENTARIO_TRAMITE_REFUNDIDO = "refundido.tramite.comentario";
	private static final String TRAMITES_ORDENADOS = "refundido.tramites.ordenados";
	

	private static final String DOCUMENTOS = "refundido.documentos";
    
    private SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
    
    @PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
    
    @Resource
    private SessionContext sessionContext;

    @EJB
    private GestorConsultasLocal gestorConsultas;
    @EJB
    private ServicioDiccionariosLocal servicioDiccionario;
    @EJB
    private GestorOperacionesPlanLocal gestorOperacionesPlan;
    @EJB
    private GestorOperacionesEntidadLocal gestorOperacionesEntidad;
    @EJB
    private GestorOperacionesDeterminacionLocal gestorOperacionesDeterminacion;
    @EJB (beanName = "GestorExportacion")
    private GestorExportacionLocal gestorExportacion;
    @EJB
    private EliminadorEntidadesLocal eliminadorEntidades;
    @EJB
    private GestorTramitesRefundidoLocal gestorTramites;
    @EJB
    private GestorContextosRefundidoLocal gestorContextos;

	private String versionBD = "Desconocida";
	
	static {
		tipoTramitesRefundibles = new HashSet<Integer>();
        
        String txtTtsRefundibles = Textos.getTexto("consola", "tipotramitesrefundibles");
        String[] arrTtsRefundibles = txtTtsRefundibles.split(",");
        for (String tt : arrTtsRefundibles) {
            try {
                tipoTramitesRefundibles.add(Integer.valueOf(tt));
            } catch (Exception e) {
                //Si no es numérico no lo añadimos a la caché y pasamos al siguiente
            }
        }
	}

	/**
     *  Actualiza el campo 'Comentario' de Tramite con información de los
     *  planes que han intervenido en el proceso de refundido.
     *  
     * @param tramiteRefundido
     * @param listaIdTramites
     */
    private void actualizarComentarioTramite(ContextoRefundido contexto){
    	Tramite tramiteRefundido = (Tramite)contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
    	StringBuffer sb = new StringBuffer();
        if (tramiteRefundido.getComentario() != null) {
        	sb.append(tramiteRefundido.getComentario());
        } 
        sb.append("Plan refundido: ["+tramiteRefundido.getPlan().getCodigo()+"]");
        
        sb.append("\nPlanes y trámites refundidos:");
        if (contexto.getParametro(COMENTARIO_TRAMITE_REFUNDIDO) != null) {
        	sb.append(contexto.getParametro(COMENTARIO_TRAMITE_REFUNDIDO));
        }
        
        tramiteRefundido.setComentario(sb.toString());
    }

    /**
	 * 
	 * @param listaIdTramites
	 * @return
	 * @throws ExcepcionRefundido 
	 */
    @SuppressWarnings("unchecked")
	private void agregarNoProcede(ContextoRefundido contexto) throws ExcepcionRefundido {
        
        int idInstrumento;
        
        contexto.log(ContextoRefundido.LOG.INFO,"Verificando operaciones 'No Procede'");
        
    	List<Plan> planes = em.createNamedQuery("Plan.obtenerNoProcede")
    		.setParameter("idTipoOperacionPlan", ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE)
    		.setParameter("listaTramites", contexto.getTramites()).getResultList();
        
        // Planes para los cuales hay que crear la operación "No procede". Sólo se
        //  hace si pertenecen a la lista de los que se van a refundir.
    	Instrumentotipooperacionplan itop;
        for(Plan plan : planes) {  
        	contexto.log(ContextoRefundido.LOG.INFO,"Verificando Plan [" + plan.getCodigo() +"]");
            
            // Averigua el instrumento del plan
            Operacionplan[] operaciones = plan.getOperacionplansForIdplanoperador().toArray(new Operacionplan[0]);
            
            if (operaciones.length > 0) {
            	itop = em.find(Instrumentotipooperacionplan.class, operaciones[0].getIdinstrumentotipooperacion());
            	
            	idInstrumento = itop.getInstrumentoplan().getIden();
            	
            	// Averigua el registro de InstrumentoTipoOperacionPlan correspondiente al
                //  instrumento del plan y la operación "No Procede". Si no existe, se intenta
                //  crear uno.
            	List<Instrumentotipooperacionplan> itops = em.createNamedQuery("Instrumentotipooperacionplan.buscarPorInstrumentoYOperacion")
            		.setParameter("idInstrumento", idInstrumento)
            		.setParameter("idTipoOperacion", ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE)
            		.getResultList();
            	
            	if (itops.size() == 0) {
            		contexto.log(ContextoRefundido.LOG.AVISO,"No se ha encontrado el registro de la tabla " + 
                            "diccionario.InstrumentoTipoOperacionPlan correspondiente " +
                            "a la operación de plan 'No Procede' y al " + 
                            "idInstrumento=" + idInstrumento + ". Se va a intentar " +
                            "crear un registro temporal en el diccionario.");
            		itop = new Instrumentotipooperacionplan();
            		itop.setInstrumentoplan(em.find(Instrumentoplan.class,idInstrumento));
            		itop.setTipooperacionplan(em.find(Tipooperacionplan.class, ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE));
       
            		try {
                		em.persist(itop);
                		em.flush();
                		contexto.log(ContextoRefundido.LOG.INFO,"Insertado registro temporal en diccionario.");
                		Operacionplan op = new Operacionplan();
                		op.setIdinstrumentotipooperacion(itop.getIden());
                		op.setPlanByIdplanoperador(plan);
                		em.persist(op);
                		contexto.log(ContextoRefundido.LOG.INFO,"Insertada la operación 'No Procede' para el plan [" + plan.getCodigo() + "]");
            		} catch (EntityExistsException eee) {
            			contexto.log(ContextoRefundido.LOG.ERROR,"No se ha conseguido insertar el registro en diccionario.");
            			throw new ExcepcionRefundido("No se ha conseguido vincular el plan con su instrumento y la operación 'No Procede'.", 99999);
            		} catch (PersistenceException pe) {
            			contexto.log(ContextoRefundido.LOG.ERROR,"No se ha conseguido insertar el registro en diccionario.");
            			throw new ExcepcionRefundido("No se ha conseguido vincular el plan con su instrumento y la operación 'No Procede'.", 99999);
            		} catch (Exception e) {
            			contexto.log(ContextoRefundido.LOG.ERROR,"No se ha conseguido insertar el registro en diccionario.");
            		}
            	} else {
            		itop = itops.get(0);
            	}
            	
            } else {
            	throw new ExcepcionRefundido("No se ha encontrado el instrumento del Plan [" + plan.getCodigo() + "]", 99999);
            }
        }
        
        contexto.log(ContextoRefundido.LOG.INFO,"------------------------------");     
    }
    
    /**
     * En cada carpeta "Entidades aportadas por..." se copia la entidad "Ámbito de Aplicación" de cada
     * plan operador, y se le vinculan todos los documentos, con el objetivo de que el plan refundido
     * contenga todos los documentos de todos los planes que lo componen.
     * 
     * @param contexto
     * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private void copiarDocumentosDeTramite(ContextoRefundido contexto) throws ExcepcionRefundido{
        int idTramite;
        Entidad iEntCarpeta;
        int idDocumento;

        try{
            contexto.log(LOG.INFO, "Copiando documentos de planes operadores...");
            List<Object[]> listaDocs = (List<Object[]>)contexto.getParametro(DOCUMENTOS);
            // Averigua cuál es el plan refundido. Debe ser el único plan que quede con
            //  un tipo de operación ID_TIPOOPERACIONPLAN_NOPROCEDE o ID_TIPOOPERACIONPLAN_SUSTITUCION
            Tramite tramiteRefundido = getTramiteRefundido(contexto.getTramites());
            
            // Recorre la lista de documentos para ir creando las carpetas de "Aportadas por..." en
            //  el trámite refundido (si no existen ya), y crear los documentos que provienen del
            //  resto de trámites.
            Tramite tramiteOperador;
            for (Object[] doc : listaDocs){
                // Recupera el texto de la entidad 'Aportadas por...' que se guardó en listaDocs
                idTramite=Integer.parseInt(doc[0].toString());

                tramiteOperador = em.find(Tramite.class, idTramite);
                // Crea los documentos que provengan de trámites diferentes del idTramiteRef
                // Siempre que no pertenezcan a trámites eliminados por una operación de sustitución.
                if (idTramite!=tramiteRefundido.getIden() && tramiteOperador != null){                       
                    // Averigua si la entidad "Aportadas por..." ya existe por haber sido creada 
                    //  con anterioridad. Si no existe, la crea.
                    iEntCarpeta=gestorOperacionesEntidad.crearCarpetaEntidadesAportadas(tramiteRefundido, tramiteOperador, contexto);
                    
                    // Crea el registro en la tabla Documento
                    idDocumento=Integer.parseInt(doc[12].toString());
                    
                    // Comprueba si existe el documento, o si ha sido eliminado
                    Documento documento = em.find(Documento.class, idDocumento);
                    if (documento != null){
                        // Mensaje
                    	contexto.log(LOG.INFO, "    Copiado documento '" + doc[3].toString() + "' del archivo '" +
                                doc[4].toString() + "' a la entidad '" + iEntCarpeta.getNombre() + "'");

                        // Crea el registro en DocumentoEntidad
                        Documentoentidad de = new Documentoentidad();
                        de.setDocumento(documento);
                        de.setEntidad(iEntCarpeta);
                        em.persist(de);
                    }
                }
            }
        } catch (Exception e) {
        	throw new ExcepcionRefundido("Fallo al copiar los documentos de los trámites: " + e.getMessage(), 23049);
        }
    }
    
    /**
     * En este procedimiento se deben incluir aquellos arreglos en los datos
     * que consistan en mejorar la calidad de los mismos. PE: eliminación
     * de espacios supérfluos.
     * @throws ExcepcionRefundido 
     * 
     */
    private void depurarDatosAlfanumericos(ContextoRefundido contexto) throws ExcepcionRefundido{
        try{
        	contexto.log(ContextoRefundido.LOG.INFO, "Inicio de la depuración de datos alfanuméricos:");

            // Suprimir los doble-espacios (si existen) en el campo "Apartado" de
            //  las determinaciones.
        	contexto.log(ContextoRefundido.LOG.INFO, "    - Eliminación de espacios dobles en el apartado de determinación.");
            em.createNativeQuery("Update planeamiento.Determinacion Set apartado=Replace(apartado, '  ', ' ') ").executeUpdate();

            // Poner un texto '-' en el campo "texto" de las determinaciones de 
            //  carácter "Regulación" que no tengan texto.
            contexto.log(ContextoRefundido.LOG.INFO, "    - Inserción de '-' en regulaciones sin texto.");

           	em.createNativeQuery("Update planeamiento.Determinacion Set texto='-' " +
                "Where idCaracter=" + ClsDatos.ID_CARACTER_REGULACION + " " +
                "And (texto Is Null Or Trim(texto)='') ").executeUpdate();
            
           	contexto.log(ContextoRefundido.LOG.INFO, "Fin de la depuración de datos alfanuméricos.");

        } catch (Exception e){
            throw new ExcepcionRefundido("No se han podido actualizar los datos antes de refundir. " + e.getMessage(),99999);
        }
    }

    /**
	 * Dada una determinación valor de referencia de 'Grupo de Entidades' y un
	 * trámite, se devuelve la determinación valor de
	 * referencia de 'Grupo de Entidades' del trámite, que se corresponde con
	 * ella. El nexo de unión es su vínculo con una determinación del
	 * plan base o, en su defecto, el nombre de la determinación.
	 * 
	 * @param determinacion
	 * @param tramite
	 * @return
     * @throws ExcepcionRefundido 
	 */
	@SuppressWarnings("unchecked")
	private Determinacion determinacionEquivalentePorDeterminacion(Determinacion determinacion, Tramite tramite) throws ExcepcionRefundido{
        try{
            List<Determinacion> detsEquivalentes = em.createNamedQuery("Determinacion.buscarEquivalentePorDetBase")
            		.setParameter("idDeterminacion", determinacion.getIden())
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            if (detsEquivalentes.size() > 0){
                return detsEquivalentes.get(0);
            }

            // Si no se ha encontrado la determinación equivalente a través del vínculo
            //  con la determinación base, se busca por el nombre y caracter.
            detsEquivalentes = em.createNamedQuery("Determinacion.buscarEquivalentePorNombreYCaracter")
            			.setParameter("idTramite", tramite.getIden())
            			.setParameter("nombre", determinacion.getNombre())
            			.setParameter("idCaracter", determinacion.getIdcaracter())
            			.getResultList();
            if (detsEquivalentes.size()>0){
                return detsEquivalentes.get(0);
            }

        } catch(Exception e){
        	throw new ExcepcionRefundido("Fallo en determinacionEquivalentePorDeterminacion: " + e.getMessage(), 23047);
        }

        return null;
    }

    /**
     * 
     * @param contexto
     * @param ejecutar
     * @throws ExcepcionRefundido 
     */
	private void ejecutarRefundido(ContextoRefundido contexto, boolean ejecutar) throws ExcepcionRefundido {
    	contexto.putParametro(ContextoRefundido.EJECUTAR, ejecutar);
    	contexto.log(ContextoRefundido.LOG.INFO,"--------------------------------");
        if (ejecutar) {
            contexto.log(ContextoRefundido.LOG.INFO,"Inicio de operaciones de refundido:");
        } else {
            contexto.log(ContextoRefundido.LOG.INFO,"Resumen previo de operaciones de refundido:");
        }
        
        Tramite tramiteOperador;
        Tramite[] tramites = (Tramite[])contexto.getParametro(TRAMITES_ORDENADOS);
        
        int ub = tramites.length - 1;
        
        for (int i = ub; i >= 0; i--) {
            // *************************************************************
            // Aquí hay que hacer las modificaciones ordenadas por
            //  el trámite j sobre el i.
        	contexto.putParametro(GestorOperacionesPlanLocal.TRAMITE_OPERADO, tramites[i]);
            for (int k = 1; k >= 0; k--) {
                // k=1 indica que se deben operar los elementos operadores del tramiteOperado
                // k=0 indica que se debe operar el resto de los elementos de tramiteOperado
                // Esto se hace para asegurar que las operaciones internas del tramiteOperado
                // se ejecutan después de que sus elementos operadores han sido operados
                // por los planes subsiguientes.
            	contexto.putParametro(GestorOperacionesPlanLocal.SOLO_OPERADORES, k);
                for (int j = i + k; j <= ub; j++) {
                    tramiteOperador = tramites[j];
                    // Modificación (Solamente si el tramiteOperador NO es un desarrollo, en cuyo caso, sus
                    // operaciones de modificación se harán después de desarrollar.
                    contexto.putParametro(GestorOperacionesPlanLocal.TRAMITE_OPERADOR, tramiteOperador);
                    
                    if (!gestorConsultas.esDesarrollo(tramiteOperador.getIden())){
                    	gestorOperacionesPlan.aplicarModificacion(contexto);
                    } else {
                        // Es desarrollo
                        if (k==0){
                        	// Modificaciones ordenadas por el desarrollo 
                            contexto.log(ContextoRefundido.LOG.INFO,"           Modificaciones ordenadas por el desarrollo [" + tramiteOperador.getPlan().getCodigo() + "]");
                            contexto.putParametro(GestorOperacionesPlanLocal.SOLO_OPERADORES, 1);
                            gestorOperacionesPlan.aplicarModificacion(contexto);
                            contexto.putParametro(GestorOperacionesPlanLocal.SOLO_OPERADORES, 0);
                            gestorOperacionesPlan.aplicarModificacion(contexto);
                            
                            gestorOperacionesPlan.aplicarDesarrollo(contexto);
                        }
                    }
                    
                    if (k==0){
                        // Suspensión total
                    	gestorOperacionesPlan.aplicarSuspensionTotal(contexto);

                        // Revocación total
                    	gestorOperacionesPlan.aplicarRevocacionTotal(contexto);

                        // Suspensión parcial
                    	gestorOperacionesPlan.aplicarSuspensionParcial(contexto);

                        // Suspensión parcial
                    	gestorOperacionesPlan.aplicarRevocacionParcial(contexto);

                        // Incorporacion de plan
                    	gestorOperacionesPlan.aplicarIncorporacion(contexto);
                    }
                }
            }
        }

        // *************************************************************
        // Aquí hay que hacer las sustituciones
        // ... y ahora aplicamos también los refundidos
        for (int i = 0; i <= ub - 1; i++) {
        	contexto.putParametro(GestorOperacionesPlanLocal.TRAMITE_OPERADO, tramites[i]);
        	for (int j = ub; j >= i + 1; j--) {
        		contexto.putParametro(GestorOperacionesPlanLocal.TRAMITE_OPERADOR, tramites[j]);
                // Sustitución
        		gestorOperacionesPlan.aplicarSustitucion(contexto);
                // Refundido
        		gestorOperacionesPlan.aplicarRefundido(contexto);
            }
        }

        if (ejecutar) {
            contexto.log(ContextoRefundido.LOG.INFO,"FIN de operaciones de refundido:");
            contexto.log(ContextoRefundido.LOG.INFO,"--------------------------------");
        }
	}
    
	/**
     * Debido a las reiteradas copias de información entre entidades y 
     * determinaciones, en ocasiones se duplican las vinculaciones a
     * los documentos. Este procedimiento se encarga de eliminar
     * estas duplicidades.
	 * @throws ExcepcionRefundido 
     * 
     */
    private void eliminarDocumentosDuplicados(ContextoRefundido contexto) throws ExcepcionRefundido{
        String s;

        try{
        	contexto.log(ContextoRefundido.LOG.INFO, "Inicio de la depuración de documentos:");

            // Documentos ligados a entidades.
        	contexto.log(ContextoRefundido.LOG.INFO, "    - Documentos ligados a entidades.");
            s="Delete From planeamiento.DocumentoEntidad Where (iden, idEntidad, idDocumento) In (" +
                "Select iden, idEntidad, idDocumento From planeamiento.DocumentoEntidad " + 
                "Where (idEntidad, idDocumento) In (" +
                "Select idEntidad, idDocumento From planeamiento.DocumentoEntidad " +
                "Group By idEntidad, idDocumento Having Count(*)>1) " +
                "Except " +
                "Select Min(iden), idEntidad, idDocumento From planeamiento.DocumentoEntidad " +
                "Group By idEntidad, idDocumento Having Count(*)>1) ";
            em.createNativeQuery(s).executeUpdate();
            
            // Documentos ligados a determinaciones.
            contexto.log(ContextoRefundido.LOG.INFO, "    - Documentos ligados a determinaciones.");
            s="Delete From planeamiento.DocumentoDeterminacion Where (iden, idDeterminacion, idDocumento) In (" +
                "Select iden, idDeterminacion, idDocumento From planeamiento.DocumentoDeterminacion " + 
                "Where (idDeterminacion, idDocumento) In (" +
                "Select idDeterminacion, idDocumento From planeamiento.DocumentoDeterminacion " +
                "Group By idDeterminacion, idDocumento Having Count(*)>1) " +
                "Except " +
                "Select Min(iden), idDeterminacion, idDocumento From planeamiento.DocumentoDeterminacion " +
                "Group By idDeterminacion, idDocumento Having Count(*)>1) ";
            em.createNativeQuery(s).executeUpdate();

            // Documentos ligados a casos.
            contexto.log(ContextoRefundido.LOG.INFO, "    - Documentos ligados a casos.");
            s="Delete From planeamiento.DocumentoCaso Where (iden, idCaso, idDocumento) In (" +
                "Select iden, idCaso, idDocumento From planeamiento.DocumentoCaso " + 
                "Where (idCaso, idDocumento) In (" +
                "Select idCaso, idDocumento From planeamiento.DocumentoCaso " +
                "Group By idCaso, idDocumento Having Count(*)>1) " +
                "Except " +
                "Select Min(iden), idCaso, idDocumento From planeamiento.DocumentoCaso " +
                "Group By idCaso, idDocumento Having Count(*)>1) ";
            em.createNativeQuery(s).executeUpdate();
            
            em.flush();
            
            contexto.log(ContextoRefundido.LOG.INFO, "Fin de la depuración de documentos.");
        } catch (Exception e){
        	throw new ExcepcionRefundido("No se han podido depurar los documentos. " + e.getMessage(), 99999);
        }
    }
    
    /**
     * 
     * @param contexto
     * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private void eliminarOperadores(ContextoRefundido contexto) throws ExcepcionRefundido{
        Determinacion iDet;
        try{

            // Primero, se eliminan los planes operadores (los que tienen el
            //  tipo de operación diferente de ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE,
            //  de ClsDatos.ID_TIPOOPERACIONPLAN_SUSTITUCION y 
            //  ClsDatos.ID_TIPOOPERACIONPLAN_INCORPORACION
            contexto.log(ContextoRefundido.LOG.INFO,"Eliminando planes operadores...");
            em.flush();
            for (Integer iden: contexto.getTramites()){
                try {
                	
                	Plan plan = (Plan) em.createNamedQuery("Plan.obtenerPlanOperador")
                			.setParameter("idTramite", iden)
                			.setParameter("idAmbito", contexto.getIdAmbito())
                			.getSingleResult();
            
                	if (em.contains(plan))
                		eliminadorEntidades.eliminar(plan, null);
                	em.flush();
                	
                } catch (NoResultException nre) {
                	log.debug("El tramite " + iden + " no tiene plan operador");
                }
            }

            // Eliminar determinaciones operadoras
            contexto.log(ContextoRefundido.LOG.INFO,"Eliminando determinaciones operadoras...");
            List<Determinacion> listaDeterminaciones;
            for (Integer iden: contexto.getTramites()){
                listaDeterminaciones=em.createNamedQuery("Determinacion.obtenerPorTramiteYCaracter")
                		.setParameter("idTramite", iden)
                		.setParameter("idCaracter", ClsDatos.ID_CARACTER_OPERADORA)
                		.getResultList();
                for (Determinacion det : listaDeterminaciones){
                    contexto.log(ContextoRefundido.LOG.INFO,"         Eliminando determinación [" + det.getCodigo() + "] del plan [" +det.getTramite().getPlan().getCodigo() + "]");
                    
                    if (em.contains(det))
                    	eliminadorEntidades.eliminar(det, null);
                    em.flush();
                }
            }

            // Eliminar entidades operadoras
            contexto.log(ContextoRefundido.LOG.INFO,"Eliminando entidades operadoras...");
            List<Entidad> listaEntidades;
            for (Integer iden: contexto.getTramites()){
            	listaEntidades = em.createNamedQuery("Entidad.buscarPorTramite")
            			.setParameter("idTramite", iden)
            			.getResultList();
                for (Entidad iEnt : listaEntidades){
                    try {
	                    iDet=(Determinacion) em.createNamedQuery("Determinacion.obtenerGrupoEntidad")
	                    		.setParameter("idEntidad", iEnt.getIden())
	                    		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
	                    		.getSingleResult();
	                    if (iDet.getDeterminacionByIddeterminacionbase() != null 
	                    		&& iDet.getDeterminacionByIddeterminacionbase().getCodigo().equals(ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_OPERADOR)){
	                        contexto.log(ContextoRefundido.LOG.INFO,"         Eliminando entidad [" + iEnt.getCodigo() + "] del plan [" + iDet.getTramite().getPlan().getCodigo() + "]");
	                        if (em.contains(iEnt))
	                        	eliminadorEntidades.eliminar(iEnt, null);
	                        em.flush();
	                    } else {
	                        // No se ha encontrado el código de la determinación base correspondiente al
	                        //  grupo de entidad de la entidad. Se comprueba si el nombre es 'Operador'
	                        if (iDet.getNombre().equalsIgnoreCase("Operador") && 
	                                iDet.getIdcaracter()==ClsDatos.ID_CARACTER_VALORREFERENCIA){
	                            contexto.log(ContextoRefundido.LOG.INFO,"         La entidad tiene asignado un grupo llamado 'Operador', aunque no está vinculado al plan base. Se asume que la entidad es una operadora.");
	                            contexto.log(ContextoRefundido.LOG.INFO,"         Eliminando entidad [" + iEnt.getCodigo() + "] del plan [" + iDet.getTramite().getPlan().getCodigo() + "]");
	
	                            if (em.contains(iEnt))
	                            	eliminadorEntidades.eliminar(iEnt, null);
	                            em.flush();
	                        }
	                    }
                    } catch (NoResultException nre) {
                    	contexto.log(LOG.AVISO, "La entidad " + iEnt.getCodigo() + " no tiene valor par la detrminación 'Grupo de Entidades'");
                    }
                }
            }
        } catch (Exception e){
            throw new ExcepcionRefundido("Error al eliminar operadores: " + e.getMessage(), 23036);
        }
    }

    /**
     * 
     * @param contexto
     * @throws ExcepcionRefundido
     */
   @SuppressWarnings("unchecked")
	private void eliminarRelacionesHuerfanas(ContextoRefundido contexto) throws ExcepcionRefundido {
        Relacion iRelacion;

        try {
        	contexto.log(ContextoRefundido.LOG.INFO,"Eliminando relaciones huérfanas...");

            // Averigua las diferentes tablas en las que hay que buscar relaciones huérfanas.
            List<String> tablas = em.createNativeQuery(SELECT_TABLAS_HUERFANAS).getResultList();
            for (String nomTabla: tablas) {
            	log.debug("Eliminando relaciones huerfanas de " + nomTabla);
                List<Integer> idRelaciones = em.createNativeQuery(String.format(SELECT_RELACIONES_HUERFANAS, nomTabla.toUpperCase().trim(),nomTabla.toLowerCase().trim()))
                		.getResultList();
                
                for (Integer idR: idRelaciones) {
                    iRelacion=em.find(Relacion.class, idR);

                    if (iRelacion!=null){
                        // Mensaje
                    	contexto.log(ContextoRefundido.LOG.AVISO,"***    Eliminando relación iden=" + idR);
                    	
                    	eliminadorEntidades.eliminar(iRelacion, null);
                    }
                }
            }

        } catch (Exception e) {
        	throw new ExcepcionRefundido("Fallo en eliminarRelacionesHuerfanas. " + e.getMessage(), 23029);
        }
	}

    @Override
	public boolean esRefundible(int idTramite) {
		Tramite tramite = em.find(Tramite.class, idTramite);
		if (tramite != null) {
			return tipoTramitesRefundibles.contains(tramite.getIdtipotramite());
		}
		return false;
	}
    
    /**
     * Averigua cuál es el plan refundido. Debe ser el único plan que quede con
     * un tipo de operación ID_TIPOOPERACIONPLAN_NOPROCEDE o ID_TIPOOPERACIONPLAN_SUSTITUCION
     * y que pertenezca a la lista de trámites que han intervenido en el proceso de refundido.
     * 
     * @return
     * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private Tramite getTramiteRefundido(List<Integer> listaIdTramites) throws ExcepcionRefundido{      
        try{
            List<Plan> planesRefundidos = em.createNamedQuery("Plan.obtenerRefundido")
            	.setParameter("tipoOperadionNoProcede", ClsDatos.ID_TIPOOPERACIONPLAN_NOPROCEDE)
            	.setParameter("tipoOperacionSustitucion", ClsDatos.ID_TIPOOPERACIONPLAN_SUSTITUCION)
            	.setParameter("listaIdTramites", listaIdTramites)
            	.getResultList();
            
            if (planesRefundidos.size()==1){
                // Averigua cuál es el trámite del plan
                
                return (Tramite)em.createNamedQuery("Tramite.obtenerTramiteRefundido")
                		.setParameter("idPlan", planesRefundidos.get(0).getIden())
                		.setParameter("listaIdTramites", listaIdTramites)
                		.getSingleResult();
            } else {
            	StringBuffer sb = new StringBuffer();
                sb.append("Se han encontrado ");
                sb.append(planesRefundidos.size());
                sb.append(" planes como resultado del refundido. ");
                for (Plan plan : planesRefundidos){
                    sb.append(plan.getCodigo());
                    sb.append(" ");
                }
                throw new ExcepcionRefundido(sb.toString(), 23035);
            }
            
        } catch (Exception e){
        	throw new ExcepcionRefundido("Fallo al calcular el trámite refundido: "  + e.getMessage(), 23035);
        }
    }
    
    /**
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@PostConstruct
	private void inicializar() {
	
        List<Object> resultado = em.createNativeQuery("Select version();").getResultList();
        if (resultado.size() > 0){
            versionBD = resultado.get(0).toString();
        } 
	}
    
    /**
     * 
     * @param listaIdTramites
     */
    @SuppressWarnings("unchecked")
	private void listarOperacionesNoEjecutadas(ContextoRefundido contexto){
        try {
        	em.flush();
            contexto.log(ContextoRefundido.LOG.INFO,"Buscando operaciones sin ejecutar...");
            List<Operacion> lista;
            for (Integer idTramite : contexto.getTramites()){
                lista = em.createNamedQuery("Operacion.buscarPorTramite")
                		.setParameter("idTramite", idTramite)
                		.getResultList();
                if (lista.size()>0){
                	contexto.log(ContextoRefundido.LOG.AVISO,"No se han ejecutado " + lista.size() + " operaciones ordenadas por el trámite fecha=" + lista.get(0).getTramite().getFecha());
                }
            }
        } catch (Exception e){
            contexto.log(ContextoRefundido.LOG.AVISO,"Se ha producido un fallo al listar las operaciones no ejecutadas durante el proceso de refundido."  + e.getMessage());
        }
    }
    
    /**
     * 
     * @param listaIdTramites
     * @throws ExcepcionRefundido
     */
	private void mostrarDatos(ContextoRefundido contexto) throws ExcepcionRefundido {
        String nombreAmbitoRefundido = servicioDiccionario.getTraduccion(Ambito.class, contexto.getIdAmbito(), "es");
        
        contexto.log(ContextoRefundido.LOG.INFO,"------------------------------");
        contexto.log(ContextoRefundido.LOG.INFO,"Ámbito: " + nombreAmbitoRefundido);
     
        contexto.putParametro(ContextoRefundido.NOMBRE_AMBITO_REFUNDIDO, nombreAmbitoRefundido);
        // Trámites
        
        Tramite tramite;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < contexto.getTramites().size(); i++) {
        	tramite = em.find(Tramite.class, contexto.getTramites().get(i));
        	if (tramite != null) {
        		 contexto.log(ContextoRefundido.LOG.INFO,String.format("%d: Trámite <%s> de fecha %s, del plan[%s] %s"
        				 ,i
        				 ,servicioDiccionario.getTraduccion(Tipotramite.class, tramite.getIdtipotramite(), "es")
        				 ,tramite.getFecha() != null?sdf.format(tramite.getFecha()):"No especificada"
        				 ,tramite.getPlan().getCodigo()
        				 ,tramite.getPlan().getNombre()));
        		 sb.append("\n["+tramite.getPlan().getCodigo()+"]");
        		 sb.append(" it=");
        		 sb.append(tramite.getIteracion());
        		 sb.append(" fecha=");
        		 sb.append(tramite.getFecha() != null?sdf.format(tramite.getFecha()):"No especificada");
        		 sb.append(" tipo=");
        		 sb.append(servicioDiccionario.getTraduccion(Tipotramite.class, tramite.getIdtipotramite(), ClsDatos.TEXTO_IDIOMA));
        	} else {
        		throw new ExcepcionRefundido("No se encontró trámite con identificador: " +contexto.getTramites().get(i), 23056);
        	}
        }
      
        contexto.putParametro(COMENTARIO_TRAMITE_REFUNDIDO, sb.toString());
    }
    
    /**
     * Muestra las operaciones que se van a realizar
     * @param tramites
     */
    @SuppressWarnings("unchecked")
	private void mostrarOperaciones(ContextoRefundido contexto) {
    	Tramite[] tramites = (Tramite[])contexto.getParametro(TRAMITES_ORDENADOS);
    	
    	String patron = "         %d --> Plan [%s], Tramite iden=%d, fecha=%s opera con <%s> %s";
    			
    	Query query = em.createNamedQuery("Tramite.mostrarOperaciones");
    	List<Object[]> datos;
		for (int i = 0; i< tramites.length;i++) {
			datos = query.setParameter("idPlan", tramites[i].getPlan().getIden())
				.setParameter("idioma", "es").getResultList();
			for (Object[] fila : datos) {
				contexto.log(LOG.INFO, String.format(patron, 
						i,
						tramites[i].getPlan().getCodigo(),
						tramites[i].getIden(),
						tramites[i].getFecha() != null?sdf.format(tramites[i].getFecha()):"No especificada",
						fila[0],
						fila[1] != null? "sobre " + fila[1]:""));
			}
		}
	}
    
    /**
     * 
     * @param contexto
     * @return
     * @throws ExcepcionRefundido
     */
    @SuppressWarnings("unchecked")
	private void obtenerAmbito(ContextoRefundido contexto) throws ExcepcionRefundido {
    	 // Ámbito de los trámites
    	List<Object> ambitos = em.createNamedQuery("Ambito.obtenerAmbitoRefundido")
    		.setParameter("listaTramites", contexto.getTramites()).getResultList();
        
        if (ambitos.size() == 0) {
        	throw new ExcepcionRefundido("No se ha encontrado el ámbito para refundir.", 23054);
        }
        if (ambitos.size() > 1) {
        	throw new ExcepcionRefundido("Se han encontrado " + ambitos.size() + " ámbitos para refundir.", 23055);
        }
                        
        contexto.setIdAmbito(Integer.parseInt(ambitos.get(0).toString()));
    }

	/**
     * Obtiene los trámites a los que pertenecen aquellos documentos asociados 
     * a entidades, determinaciones o casos.
     * 
     * @param tramites
     * @return
     */
	@SuppressWarnings("unchecked")
	private Map<String,List<Integer>> obtenerReferenciaDocumentos(Tramite[] tramites) {
		Map<String,List<Integer>> documentosDEC = new HashMap<String, List<Integer>>();
		List<Documento> documentos;
		List<Integer> listaIdDocumentos;
		for (Tramite tramite : tramites) {
			documentos = em.createNamedQuery("Documento.buscarPorTramiteDEC")
					.setParameter("idTramite", tramite.getIden())
					.getResultList();
			
			// No debería haber dos trámites con el mismo código.
			listaIdDocumentos = new ArrayList<Integer>();
			documentosDEC.put(tramite.getCodigofip(), listaIdDocumentos);
			
			for (Documento doc : documentos) {
				if (!listaIdDocumentos.contains(doc.getIden()))
					listaIdDocumentos.add(doc.getIden());
			}
		}
		return documentosDEC;
	}

	/**
	 * 
	 * @param listaIdTramites
	 * @return
	 * @throws ExcepcionRefundido 
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
	private Tramite[] ordenarTramitesParaRefundido(List<Integer> listaIdTramites) throws ExcepcionRefundido {
        // El orden de los trámites viene dado por el orden del plan.
    	List<Tramite> tramites = em.createNamedQuery("Tramite.ordenar")
			.setParameter("listaTramites", listaIdTramites)
			.getResultList();
    	
    	// Comprueba si la lista está completa o si hay algún trámite que no existe.
    	if (tramites.size() == listaIdTramites.size()) {
    		return tramites.toArray(new Tramite[0]);
    	} else {
    		throw new ExcepcionRefundido("No se ha encontrado alguno de los trámites de la lista.", 23040);
    	}
        
    }
	
	/**
	 * Reasignar al trámite refundido todas las determinaciones de otros trámites que están ligadas de
	 * alguna manera a las entidades del refundido.
	 * Los vínculos que se tienen en cuenta son:
	 * 		- Tabla EntidadDeterminacion
	 *   	- Tabla EntidadDeterminacionRegimen
	 *   	- Tabla OpcionDeterminacion apuntada por EntidadDeterminacionRegimen
	 *   	- Tabla DeterminacionGrupoEntidad
	 *   	- Tabla Determinacion o Entidad que están ligadas vía tabla Relacion con determinaciones
	 *   		o entidades del trámite refundido.
	 *   	- Tabla Relacion que apunta vía tabla VectorRelacion a Determinaciones o Entidades del trámite refundido
	 * @param tramites 
	 * @throws ExcepcionRefundido 
	 */
	@SuppressWarnings("unchecked")
	private void reasignarDependencias(ContextoRefundido contexto) throws ExcepcionRefundido{
         
        Determinacion iDetEquivalente;
        Tramite tramiteRefundido;
        Determinacion iDetPadre;
        Determinacion iDetGrupo;
        List<Integer> listaIdTramites=new ArrayList<Integer>();

        try{
            contexto.log(ContextoRefundido.LOG.INFO,"Moviendo determinaciones dependientes al trámite refundido...");
            tramiteRefundido= getTramiteRefundido(contexto.getTramites());
            
            // Hace una copia dela lista de los iden de los trámites a refundir 
            //  para controlar que no se muevan elementos del plan base, del 
            //  propio trámite refundido, ni de ningún trámite que no haya
            //  sido seleccionado para refundir.
            listaIdTramites.addAll(contexto.getTramites());
            
            listaIdTramites.remove(new Integer(tramiteRefundido.getIden()));

            // Determinaciones ligadas por EntidadDeterminacion
            contexto.log(ContextoRefundido.LOG.INFO,"    -> Condiciones urbanísticas...");
            List<Determinacion> determinaciones = em.createNamedQuery("Determinacion.buscarCondicionesUrbanisticas")
            			.setParameter("idTramite", tramiteRefundido.getIden())
            			.getResultList();
            for (Determinacion iDet : determinaciones){
                // Se excluyen las determinaciones del plan base y las de trámites que no están en la lista de los refundibles
                
                if (iDet.getTramite().getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE){
                    if (listaIdTramites.contains(iDet.getTramite().getIden())){
                        // Averigua el iden de la carpeta de determinaciones aportadas
                        iDetPadre = gestorOperacionesDeterminacion.crearCarpetaDeterminacionesAportadas(tramiteRefundido, iDet.getTramite());
                        // Reasigna la determinación al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                        if (iDet.getTramite().getIden()!=iDetPadre.getTramite().getIden()){
                            contexto.log(ContextoRefundido.LOG.INFO,"        Moviendo la determinación [" + iDet.getCodigo() +
                                    "] desde el plan [" + iDet.getTramite().getPlan().getCodigo() +
                                    "] al [" + iDetPadre.getTramite().getPlan().getCodigo() + "]");
                            gestorOperacionesDeterminacion.reasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0, contexto);
                        }
                    }
                }
            }

            // Determinaciones ligadas por EntidadDeterminacionRegimen
            contexto.log(ContextoRefundido.LOG.INFO,"    -> Determinaciones de régimen...");
            
            determinaciones = em.createNamedQuery("Determinacion.bucarDeterminacionesRegimen")
            		.setParameter("idTramite", tramiteRefundido.getIden())
            		.getResultList();
            for (Determinacion iDet : determinaciones){
                // Se excluyen las determinaciones del plan base y las de trámites que no están en la lista de los refundibles
                if (iDet.getTramite().getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE){
                    if (listaIdTramites.contains(iDet.getTramite().getIden())){
                        // Averigua el iden de la carpeta de determinaciones aportadas
                        iDetPadre = gestorOperacionesDeterminacion.crearCarpetaDeterminacionesAportadas(tramiteRefundido, iDet.getTramite());
                        // Reasigna la determinación al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                        if (iDet.getTramite().getIden()!=iDetPadre.getTramite().getIden()){
                            contexto.log(ContextoRefundido.LOG.INFO,"        Moviendo la determinación [" + iDet.getCodigo() +
                                    "] desde el plan [" + iDet.getTramite().getPlan().getCodigo() +
                                    "] al [" + iDetPadre.getTramite().getPlan().getCodigo() + "]");
                            gestorOperacionesDeterminacion.reasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0, contexto);
                        }
                    }
                }
            }

            // Determinaciones ligadas por OpcionDeterminacion
            contexto.log(ContextoRefundido.LOG.INFO,"    -> Valores de referencia...");
            determinaciones = em.createNamedQuery("Determinacion.buscarValoresReferencia")
            		.setParameter("idTramite", tramiteRefundido.getIden())
            		.getResultList();
            for (Determinacion iDet: determinaciones){
                
                // Se vuelve a hacer la comprobación de idTramite porque el procedimiento para reasignar
                //  una determinación cambia algunas de sus dependencias, y éstas pueden estar inluídas
                //  en la lista de determinaciones seleccionadas.

                // Se excluyen las determinaciones del plan base y las de trámites que no están en la lista de los refundibles
                
                if (iDet.getTramite().getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE){
                    if (listaIdTramites.contains(iDet.getTramite().getIden())){
                        // Averigua el iden de la carpeta de determinaciones aportadas
                        iDetPadre = gestorOperacionesDeterminacion.crearCarpetaDeterminacionesAportadas(tramiteRefundido, iDet.getTramite());
                        // Reasigna la determinación al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                        if (iDet.getTramite().getIden() != iDetPadre.getTramite().getIden()){
                            contexto.log(ContextoRefundido.LOG.INFO,"        Moviendo la determinación [" + iDet.getCodigo() +
                                    "] desde el plan [" + iDet.getTramite().getPlan().getCodigo() +
                                    "] al [" + iDetPadre.getTramite().getPlan().getCodigo() + "]");
                            gestorOperacionesDeterminacion.reasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0, contexto);
                        }
                    }
                }
            }

            // Determinaciones ligadas por DeterminacionGrupoEntidad
            contexto.log(ContextoRefundido.LOG.INFO,"    -> Grupos de aplicación...");
            List<Determinaciongrupoentidad> gruposAplicacion = em.createNamedQuery("Determinaciongrupoentidad.buscarGruposAplicacion")
            		.setParameter("idTramite", tramiteRefundido.getIden())
            		.getResultList();
            for (Determinaciongrupoentidad grupoAplicacion : gruposAplicacion){
                // Se excluyen las determinaciones del plan base y las de trámites que no están en la lista de los refundibles
                if (grupoAplicacion.getDeterminacionByIddeterminacion().getTramite().getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE){
                    if (listaIdTramites.contains(grupoAplicacion.getDeterminacionByIddeterminacion().getTramite().getIden())){
                        iDetGrupo=grupoAplicacion.getDeterminacionByIddeterminaciongrupo();

                        // Se comprueba si en el trámite refundido ya existe una determinación de Grupo que
                        //  sea equivalente a esta. Si no existe, se le cambia el idTramite. Si ya existe, se
                        //  comprueba si hay un registro en DeterminacionGrupoEntidad, y si no lo hay, se crea.
                        iDetEquivalente=determinacionEquivalentePorDeterminacion(iDetGrupo, tramiteRefundido);
                        if(iDetEquivalente==null){
                            // Averigua el iden de la carpeta de determinaciones aportadas
                            iDetPadre = gestorOperacionesDeterminacion.crearCarpetaDeterminacionesAportadas(tramiteRefundido, grupoAplicacion.getDeterminacionByIddeterminacion().getTramite());
                            // Reasigna la determinación al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                            if (grupoAplicacion.getDeterminacionByIddeterminacion().getTramite().getIden()!=iDetPadre.getTramite().getIden()){
                                contexto.log(ContextoRefundido.LOG.INFO,"        Moviendo la determinación [" + grupoAplicacion.getDeterminacionByIddeterminacion().getCodigo() +
                                        "] desde el plan [" + grupoAplicacion.getDeterminacionByIddeterminacion().getTramite().getPlan().getCodigo() +
                                        "] al [" + iDetPadre.getTramite().getPlan().getCodigo() + "]");
                                gestorOperacionesDeterminacion.reasignarDeterminacion(iDetPadre, iDetPadre, iDetGrupo, 0, contexto);
                                // Recarga los datos
                                iDetEquivalente=determinacionEquivalentePorDeterminacion(iDetGrupo, tramiteRefundido);
                            }
                        }

                        // Comprueba si ya existe el registro en DeterminacionGrupoEntidad
                        List<Determinaciongrupoentidad> existentes = em.createNamedQuery("Determinaciongrupoentidad.buscarPorDetYGrupo")
                        		.setParameter("idDeterminacion", grupoAplicacion.getDeterminacionByIddeterminacion().getIden())
                        		.setParameter("idGrupo", iDetEquivalente.getIden())
                        		.getResultList();
                        if (existentes.size()==0){
                        	grupoAplicacion.setDeterminacionByIddeterminaciongrupo(iDetEquivalente);
                            contexto.log(ContextoRefundido.LOG.INFO,"        La determinación de grupo [" + iDetEquivalente.getCodigo() + "] del " +
                                   "plan [" + iDetEquivalente.getTramite().getPlan().getCodigo() + "] se ha " +
                                   "asignado a la determinación [" + grupoAplicacion.getDeterminacionByIddeterminacion().getCodigo() + "] del " +
                                   "plan [" + grupoAplicacion.getDeterminacionByIddeterminacion().getTramite().getPlan().getCodigo() + "].");
                        } else {
                        	em.remove(grupoAplicacion);
                        	contexto.log(ContextoRefundido.LOG.INFO,"        Se ha eliminado un registro duplicado en DeterminacionGrupoEntidad " +
                                   "correspondiente a la determinación [" + grupoAplicacion.getDeterminacionByIddeterminacion().getCodigo() + "] del " +
                                   "plan [" + grupoAplicacion.getDeterminacionByIddeterminacion().getTramite().getPlan().getCodigo() + "] y la " +
                                   "determinación de grupo [" + iDetEquivalente.getCodigo() + "] del " +
                                   "plan [" + iDetEquivalente.getTramite().getPlan().getCodigo() + "].");
                        }
                    }
                }
            }

            // Determinaciones de un trámite distinto del refundido ligadas por Relacion a determinaciones
            //  o entidades del trámite refundido.
            contexto.log(ContextoRefundido.LOG.INFO,"    -> Determinaciones de trámites diferentes del refundido relacionadas con determinaciones o entidades del refundido...");
            
            List<Object[]> listaDatos = em.createNamedQuery("Determinacion.buscarRelacionadasRefundido")
            		.setParameter("idTramite", tramiteRefundido.getIden())
            		.getResultList();
            Determinacion det;
            for (Object[] obj : listaDatos){
                det=em.find(Determinacion.class, obj[0]);

                // Se excluyen las determinaciones del plan base y las del propio plan refundido, y las de trámites que no están en la lista de los refundibles
                if (det.getTramite().getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE &&
                        det.getTramite().getIden()!=tramiteRefundido.getIden()){
                    if (listaIdTramites.contains(det.getTramite().getIden())){
                        // Averigua el iden de la carpeta de determinaciones aportadas
                        iDetPadre = gestorOperacionesDeterminacion.crearCarpetaDeterminacionesAportadas(tramiteRefundido, det.getTramite());
                        

                        // Reasigna la determinación al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                        if (det.getTramite().getIden()!=iDetPadre.getTramite().getIden()){
                            contexto.log(ContextoRefundido.LOG.INFO,"        " + obj[1]);
                            contexto.log(ContextoRefundido.LOG.INFO,"            Moviendo la determinación [" + det.getCodigo() +
                                    "] desde el plan [" + det.getTramite().getPlan().getCodigo() +
                                    "] al [" + iDetPadre.getTramite().getPlan().getCodigo() + "]");
                            
                            gestorOperacionesDeterminacion.reasignarDeterminacion(iDetPadre, iDetPadre, det, 0, contexto);

                            // Cambia el idTramite creador de la relación al trámite refundido
                            em.createNativeQuery("Update planeamiento.relacion Set idtramitecreador=" + tramiteRefundido.getIden() + " Where iden=" + obj[2].toString()).executeUpdate();
                        }
                    }
                }
            }

            // *****************************************************************

            // Entidades ligadas por Relacion a entidades o determinaciones
            //  del trámite refundido.
            // Se excluyen las entidades del plan base, del refundido, y las de 
            //  trámites que no están en la lista de los refundibles
            contexto.log(ContextoRefundido.LOG.INFO,"    -> Entidades de trámites diferentes del refundido relacionadas con determinaciones o entidades del refundido...");
           
            listaDatos = em.createNamedQuery("Entidad.buscarRelacionadasRefundido")
            		.setParameter("idTramite", tramiteRefundido.getIden())
            		.getResultList();
            Entidad entidad, entidadPadre;
            for (Object[] obj : listaDatos){
                entidad=em.find(Entidad.class, obj[0]);
                
                // Se excluyen las entidades del plan base y las del refundido, y las de trámites que no están en la lista de los refundibles
                if (entidad.getTramite().getIdtipotramite()!=ClsDatos.ID_TIPOTRAMITE_PLANBASE &&
                		entidad.getTramite().getIdtipotramite()!= tramiteRefundido.getIden()){
                    if (listaIdTramites.contains(entidad.getTramite().getIden())){
                        // Averigua el iden de la carpeta de entidades aportadas
                        entidadPadre = gestorOperacionesEntidad.crearCarpetaEntidadesAportadas(tramiteRefundido, entidad.getTramite(), contexto);
                     
                        // Reasigna la entidad al trámite refundido (si no lo ha sido en una iteración anterior de este bucle)
                        if (entidad.getTramite().getIden()!=entidadPadre.getTramite().getIden()){
                            contexto.log(ContextoRefundido.LOG.INFO,"        " + obj[1]);
                            contexto.log(ContextoRefundido.LOG.INFO,"            Moviendo la entidad [" + entidad.getCodigo() +
                                    "] desde el plan [" + entidad.getTramite().getPlan().getCodigo() +
                                    "] al [" + entidadPadre.getTramite().getPlan().getCodigo() + "]");
                            gestorOperacionesEntidad.reasignarEntidad(entidadPadre, entidadPadre, entidad, 0, contexto);

                            // Cambia el idTramite creador de la relación al trámite refundido
                            em.createNativeQuery("Update planeamiento.relacion Set idtramitecreador=" + tramiteRefundido.getIden() + " Where iden=" + obj[2]).executeUpdate();
                        }
                    }
                }
            }
            em.flush();
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo al reasignar dependencias: "  + e.getMessage(),999999);
        }
    }
	/**
     * Al final del proceso de refundido, se han creado una serie de 
     * determinaciones y entidades que contienen las aportaciones de otros 
     * planes. Estas carpetas están en la raiz de los árboles de entidades
     * y determinaciones, y este procedimiento sirve para reubicarlas
     * dentro de una única carpeta de "Aportadas" (se entiende que, una para 
     * las determinaciones, y otra para las entidades).
     * 
     * @param tramiteRefundido
     */
	@SuppressWarnings("unchecked")
	private void reubicarCarpetasAportadas(Tramite tramiteRefundido, List<Integer> listaIdTramitesBase)    {
    	log.debug("reubicarCarpetasAportadas");
        
        String s;
        String codigo;
        Determinacion iDetGrupoDeEntidades;
        Determinacion iDetCarpeta;
        Tramite iTramiteBase;
        
        try{
            // Crea la determinación "Aportadas", de la que van a colgar todas las
            //  determinaciones "Aportadas por..." que se han creado durante el
            //  proceso de refundido.
        	em.flush();
            codigo=gestorConsultas.getSiguienteCodigoDeterminacion(tramiteRefundido.getIden());
            int orden = (Integer)em.createNamedQuery("Determinacion.obtenerOrdenMaximo")
            		.setParameter("idTramite", tramiteRefundido.getIden())
            		.getSingleResult();
            Determinacion dAportadas = new Determinacion();
            dAportadas.setTramite(tramiteRefundido);
            dAportadas.setIdcaracter(ClsDatos.ID_CARACTER_ENUNCIADO);
            dAportadas.setApartado("Anexo");
            dAportadas.setNombre("Aportadas por refundido");
            dAportadas.setCodigo(codigo);
            dAportadas.setBsuspendida(false);
            dAportadas.setOrden(orden);
            em.persist(dAportadas);
            em.flush();
            
            // Actualiza el idPadre de las determinaciones aportadas que están
            //  en la raiz, para que cuelguen de la determinación recién creada.
            s="Update planeamiento.determinacion Set idPadre=" + dAportadas.getIden() + " " +
                "Where idPadre Is Null " + 
                "And idcaracter=" + ClsDatos.ID_CARACTER_ENUNCIADO + " " +
                "And nombre Like '" + ClsDatos.TEXTO_APORTADAS + "%' " +
                "And iden<>" + dAportadas.getIden() + " And idTramite=" + tramiteRefundido.getIden() + " ";

            if (em.createNativeQuery(s).executeUpdate() >0){
            	em.flush();
                // Actualiza el apartado y el orden de dichas determinaciones
                List<Determinacion> determinaciones = em.createNamedQuery("Determinacion.buscarHijas")
                		.setParameter("idPadre", dAportadas.getIden())
                		.getResultList();
                for (int i = 0; i < determinaciones.size();i++){
                	em.refresh(determinaciones.get(i));
                    determinaciones.get(i).setApartado(String.valueOf(i+1));
                    determinaciones.get(i).setOrden(i+1);
                }

            } else {
                log.warn("No se han podido reubicar las determinaciones " + 
                         ClsDatos.TEXTO_APORTADAS + "...");
            }
            
            // Crea la entidad "Aportadas", de la que van a colgar todas las
            //  entidades "Aportadas por..." que se han creado durante el
            //  proceso de refundido.
            codigo=gestorConsultas.getSiguienteCodigoEntidad(tramiteRefundido.getIden());
            Entidad eAportadas = new Entidad();
            eAportadas.setTramite(tramiteRefundido);
            eAportadas.setClave("");
            eAportadas.setNombre("Aportadas por refundido");
            eAportadas.setCodigo(codigo);
            eAportadas.setBsuspendida(false);
            em.persist(eAportadas);
            em.flush();
            
            // Añadir su valor para la determinación 'Grupo de entidades' (es 
            //  la determinación 'Carpeta', lo que anteriormente era el grupo 'Grupo')
            iDetGrupoDeEntidades=null;
            iDetCarpeta=null;
            for (Integer idTramiteBase : listaIdTramitesBase){
                
                iTramiteBase=em.find(Tramite.class, idTramiteBase);
                iDetGrupoDeEntidades=gestorConsultas.obtenerDeterminacionGrupoDeEntidadesPorTramite(iTramiteBase);
                if (iDetGrupoDeEntidades!=null){
                    iDetCarpeta=gestorOperacionesDeterminacion.getDeterminacionCarpetaTramiteBase(iTramiteBase, listaIdTramitesBase);
                    break;
                }
            }
            
            try {
            	Opciondeterminacion iOpcion=(Opciondeterminacion) em.createNamedQuery("Opciondeterminacion.buscarPorDetYValorRef")
        			.setParameter("idDeterminacion", iDetGrupoDeEntidades.getIden())
        			.setParameter("idValorRef", iDetCarpeta.getIden()).getSingleResult();

            	// Inserta el valor en las tablas de valores
            	Entidaddeterminacion ed = new Entidaddeterminacion();
            	ed.setEntidad(eAportadas);
            	ed.setDeterminacion(iDetGrupoDeEntidades);
            	em.persist(ed);
            	
            	Casoentidaddeterminacion ced = new Casoentidaddeterminacion();
            	ced.setEntidaddeterminacion(ed);
            	em.persist(ced);
            	
            	Entidaddeterminacionregimen edr = new Entidaddeterminacionregimen();
            	edr.setCasoentidaddeterminacionByIdcaso(ced);
            	edr.setOpciondeterminacion(iOpcion);
            	edr.setSuperposicion(0);
            	em.persist(edr);
            	

                // Actualiza el idPadre de las entidades aportadas que están
                //  en la raiz, para que cuelguen de la entidad recién creada.
                s="Update planeamiento.entidad Set idPadre=" + eAportadas.getIden() + " " +
                    "Where idPadre Is Null " + 
                    "And nombre Like '" + ClsDatos.TEXTO_APORTADAS + "%' " +
                    "And iden<>" + eAportadas.getIden() + " And idTramite=" + tramiteRefundido.getIden() + " ";
                 
                if (em.createNativeQuery(s).executeUpdate()>0){
                	em.flush();
                    // Actualiza el apartado y el orden de dichas entidades
                    List<Entidad> hijas = em.createNamedQuery("Entidad.buscarHijas")
                    		.setParameter("idPadre", eAportadas.getIden())
                    		.getResultList(); 
                    for (int i = 0; i < hijas.size();i++){
                    	em.refresh(hijas.get(i));
                    	hijas.get(i).setOrden(i+1);
                    }

                } else {
                    log.warn("No se han podido reubicar las entidades " + 
                              ClsDatos.TEXTO_APORTADAS + "...");
                }

            } catch (NoResultException nre) {
            	// No se encuentra la opcion para la pareja de determinaciones 
                //  del plan base. No es posible reubicar las entidades, y hay
                //  que eliminar la entidad recien creada.
            	if (em.contains(eAportadas)) {
            		log.info("Eliminando entidad [" + eAportadas.getCodigo() + "] " + eAportadas.getNombre());
                	eliminadorEntidades.eliminar(eAportadas, null);
                	em.flush();
            	}
                log.warn("No se han podido reubicar las entidades " + 
                         ClsDatos.TEXTO_APORTADAS + "...");
            }
        } catch (Exception e) {
            log.warn("No se han conseguido agrupar las determinaciones y " + 
                "entidades en carpetas únicas. " + e.getMessage());
        } 
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoLocal#refundirTramites(java.util.List, java.lang.String, boolean)
     */
    @SuppressWarnings("unchecked")
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void refundirTramites (ContextoRefundido contexto) throws ExcepcionRefundido {
        
        // Inicia la transacción única del proceso de refundido
        try {
        	gestorContextos.iniciarProceso(contexto);
	        
			em.setFlushMode(FlushModeType.COMMIT);
                   
            contexto.log(ContextoRefundido.LOG.INFO,"**********************************************************");
            contexto.log(ContextoRefundido.LOG.INFO,"Inicio de refundido: " + sdf.format(GregorianCalendar.getInstance().getTime()));

            // Mensajes acerca de las versiones
            contexto.log(ContextoRefundido.LOG.INFO,"Versión del programa: " + ClsDatos.VERSION_REFUNDIDO);
            contexto.log(ContextoRefundido.LOG.INFO,"Versión de recursos requerida : " + ClsDatos.VERSION_REQUERIDA_RECURSOS);
            contexto.log(ContextoRefundido.LOG.INFO,"Versión de recursos encontrada: " + ClsDatos.VERSION_RECURSOS);
            
            if (!ClsDatos.VERSION_REQUERIDA_RECURSOS.equalsIgnoreCase(ClsDatos.VERSION_RECURSOS)){
            	contexto.log(ContextoRefundido.LOG.AVISO,"    Atención: la versión de los recursos puede no ser la adecuada.");
            }
            
            contexto.log(ContextoRefundido.LOG.INFO,"Versión de la base de datos: " + versionBD );
            
            obtenerAmbito(contexto);
            
            // Muestra los datos del ámbito, planes, y trámites que van a ser refundidos.
            mostrarDatos(contexto);
            
            // Añade la operación de plan "No Procede" en los planes que no la tengan
            agregarNoProcede(contexto);
            
            // Averigua los identificadores de los trámites base usados por los
            //  trámites que se van a refundir.
            
            // Lista de trámites Base. Los trámites que se van a refundir pueden
            //  usar diferentes trámites base que, incluso, pueden ser de diferentes
            //  planes.
            List<Integer> gListaIdTramitesBase = em.createNamedQuery("Tramite.obtenerIdTramitesBase")
            	.setParameter("idTipoTramite", ClsDatos.ID_TIPOTRAMITE_PLANBASE)
            	.setParameter("listaTramites", contexto.getTramites()).getResultList();
            
            contexto.putParametro(ContextoRefundido.ID_TRAMITES_BASE, gListaIdTramitesBase);
            
            // Asigna autoreferencias en las determinaciones base para garantizar las búsquedas
            //  de grupos de entidad, tanto en planes normalizados como no normalizados.
            for (Integer i : gListaIdTramitesBase) {
               em.createNativeQuery("Update planeamiento.Determinacion Set idDeterminacionBase=iden " +
                        "Where idTramite=" + i + " ").executeUpdate();
            }
            
            // Ordena los trámites según su fecha y su nivel de anidamiento en el árbol de planes
            Tramite[] tramites = ordenarTramitesParaRefundido(contexto.getTramites());
            
            contexto.putParametro(TRAMITES_ORDENADOS, tramites);
            
            contexto.log(ContextoRefundido.LOG.INFO,"Trámites ordenados:");
            
            // Guarda los documentos de todos los tramites (exceptuando los de determinación, entidad y caso)
            // Lista de los documentos de cada trámite. Sólo los ligados a trámite, y no los de determinación, entidad y caso.
            contexto.putParametro(DOCUMENTOS, gestorConsultas.obtenerDocumentos(tramites));
            
            contexto.putParametro("refundido.documentos.dec", obtenerReferenciaDocumentos(tramites));
            
            mostrarOperaciones(contexto);
            
            // **********************
            // Algoritmo de refundido
            // **********************
           
            // Si no se desea crear el trámite refundido en la BD, se asume que se 
            //  trata de una ejecución en modo de prueba, y tampoco se muestra el
            //  resumen previo de operaciones, con lo cual se acelera el proceso.
            ejecutarRefundido(contexto, false);
            if (contexto.isCrearTramite()) {
            	ejecutarRefundido(contexto, true);
            }
            
            // Copiar los documentos de trámite de cada trámite operador en el trámite refundido. Para ello,
            //  se crea una carpeta de 'Entidades aportadas por...' (si no existe previamente), y se
            //  vinculan los documentos a dichas carpetas, según sea su trámite.
            copiarDocumentosDeTramite(contexto);
            
            // Eliminar las relaciones huérfanas
            eliminarRelacionesHuerfanas(contexto);

            // Reasignar al trámite refundido las determinaciones dependientes de las entidades de dicho trámite
            
            reasignarDependencias(contexto);

            // Comprobar si quedan operaciones sin hacer
            listarOperacionesNoEjecutadas(contexto);

            // Eliminar elementos operadores: planes, entidades y determinaciones.

            eliminarOperadores(contexto);
         
            // Averigua cuál es el iden del trámite refundido.
            Tramite refundido = getTramiteRefundido(contexto.getTramites());
            
            contexto.putParametro(ContextoRefundido.TRAMITE_REFUNDIDO, refundido);
            
            // Eliminar las carpetas vacías
            gestorOperacionesEntidad.eliminarCarpetasVacias(refundido, contexto);
            
            // Unificar las unidades
            unificarUnidades(contexto);
         
            // Eliminar las relaciones huérfanas por segunda vez
            eliminarRelacionesHuerfanas(contexto);
            
            // Reubicar las carpetas de determinaciones y entidades aportadas, para
            //  que cuelguen de una carpeta "Aportadas".
            reubicarCarpetasAportadas(refundido, gListaIdTramitesBase);
            
            // Actualiza algunos datos en todos los planes para que cumplan ciertas
            //  validaciones.
            depurarDatosAlfanumericos(contexto);
            
            // Debido a las reiteradas copias de información entre entidades y 
            //  determinaciones, en ocasiones se duplican las vinculaciones a
            //  los documentos. Este procedimiento se encarga de eliminar
            //  estas duplicidades.
            eliminarDocumentosDuplicados(contexto);
            
            // Exportar a FIP
            
            refundido.getPlan().setCodigo(gestorConsultas.getSiguienteCodigoPlan());
        	// Actualiza el campo "comentario" del trámite con los datos del refundido.
            //  --> Plan creado:
            actualizarComentarioTramite(contexto);
            
            // Cambia el tipo de trámite a Refundido (11)
            refundido.setIdtipotramite(ClsDatos.ID_TIPOTRAMITE_REFUNDIDO);
            
            contexto.log(ContextoRefundido.LOG.INFO,"Código de Trámite sin encriptar: [" + refundido.getPlan().getCodigo() + "]"); 

            // Crea el plan y trámite refundido en la base de datos.
            if (contexto.isCrearTramite()){
            	Tramite tramitecreado = gestorTramites.crearTramiteRefundido(contexto);
            	// Existe una restricción que impide que dos trámites tengan el
            	// mismo código, así que tengo que intercambiar los datos de
            	// código entre el trámite creado y el refundido. Como luego se
            	// cancelarán estas modificaciones no tiene mayor importancia.
            	
            	// Al estar creado en otra transacción este EM no contiene este 
            	// trámite, así que para poder modificar su contenido y que no salte
            	// la restricción hay que hacer esto.
            	tramitecreado = em.find(Tramite.class, tramitecreado.getIden());
            	em.refresh(refundido);
            	
            	log.debug("CodigoFip tramite creado: " + tramitecreado.getCodigofip());
            	log.debug("CodigoFip refundido: " + refundido.getCodigofip());
            	
            	String codigoFipTramiteCreado = tramitecreado.getCodigofip();
            	
            	SimpleDateFormat sdftemp=new SimpleDateFormat("ddMMyyyyHHmmss");
            	tramitecreado.setCodigofip(sdftemp.format(GregorianCalendar.getInstance().getTime()));
            	em.flush();
            	refundido.setCodigofip(codigoFipTramiteCreado); 	
            	
            	refundido.setIteracion(tramitecreado.getIteracion());
            	refundido.setFecha(tramitecreado.getFecha());
            	em.flush();
            	
            	contexto.putParametro(ContextoRefundido.TRAMITE_REFUNDIDO, refundido);
            	
            	contexto.setFIP(gestorExportacion.exportarFIP(contexto));
            	
            	gestorTramites.registrarFip1(contexto);
            } else {
                contexto.log(ContextoRefundido.LOG.INFO,"El usuario ha elegido no crear el trámite refundido en la base de datos.");
            }

            contexto.log(ContextoRefundido.LOG.INFO, "El proceso de refundido ha finalizado con éxito.");
            
            contexto.log(ContextoRefundido.LOG.INFO, "FIN de proceso");
            
		} catch (ExcepcionRefundido er) {
        	contexto.log(ContextoRefundido.LOG.ERROR,er.getMessage() + " Codigo: " + er.getCodigo());
        	
        	throw er;
        } catch (Exception e) {
        	contexto.log(ContextoRefundido.LOG.ERROR,e.getMessage());
        	
        	throw new ExcepcionRefundido("Error inesperado. " + e.getMessage(),99999, e);
        } finally {
        	gestorContextos.finalizarProceso(contexto);
	        // Asegura que todo el proceso NO se reflejará en la base de datos
	        sessionContext.setRollbackOnly();
        }
    }

	/**
     * Se hace una lista de todas las unidades, y se cambian
     * todos los idDeterminacion de las duplicadas a una de ellas.
     * Se eliminan las determinaciones duplicadas.
     * 
     * TODO Esta función debería estar en el gestorOperacionesDeterminacion
     * 
     * @param idTramite
	 * @throws ExcepcionRefundido 
     */
	@SuppressWarnings("unchecked")
	private void unificarUnidades(ContextoRefundido contexto) throws ExcepcionRefundido{
        try{
        	contexto.log(ContextoRefundido.LOG.INFO, "Unificando unidades...");

            Tramite tramite = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
            // Determinaciones Unidad
            List<Determinacion> determinaciones = em.createNamedQuery("Determinacion.obtenerPorTramiteYCaracter")
            		.setParameter("idTramite", tramite.getIden())
            		.setParameter("idCaracter", ClsDatos.ID_CARACTER_UNIDAD)
            		.getResultList();
            List<Determinacion> listaRepetidas;
            List<Vectorrelacion> referenciasAUnidadRepetida;
            String s;
            for (Determinacion determinacion : determinaciones){
            	if (em.contains(determinacion)) {
	            	contexto.log(ContextoRefundido.LOG.INFO, "Unidad: " + determinacion.getNombre());
	                // Lista de determinaciones unidad con el mismo nombre
	                
	                listaRepetidas= em.createNamedQuery("Determinacion.buscarRepetidas")
	                		.setParameter("idTramite", tramite.getIden())
	                		.setParameter("idCaracter", ClsDatos.ID_CARACTER_UNIDAD)
	                		.setParameter("nombre", determinacion.getNombre())
	                		.setParameter("idDeterminacion", determinacion.getIden())
	                		.getResultList();
	                
	                for (Determinacion repetida : listaRepetidas){
                	
                		contexto.log(ContextoRefundido.LOG.INFO, "Eliminando unidad duplicada...");

                        s="Update planeamiento.Documentodeterminacion " +
                                "Set iddeterminacion=" + determinacion.getIden() + " " +
                                "Where iddeterminacion=" + repetida.getIden() + " ";
                        em.createNativeQuery(s).executeUpdate();
                        s="Update planeamiento.Opciondeterminacion " +
                                "Set iddeterminacion=" + determinacion.getIden() + " " +
                                "Where iddeterminacion=" + repetida.getIden() + " ";
                        em.createNativeQuery(s).executeUpdate();
                        s="Update planeamiento.Opciondeterminacion " +
                                "Set iddeterminacionValorRef=" + determinacion.getIden() + " " +
                                "Where iddeterminacionValorRef=" + repetida.getIden() + " ";
                        em.createNativeQuery(s).executeUpdate();
                        s="Update planeamiento.Entidaddeterminacionregimen " +
                                "Set iddeterminacionRegimen=" + determinacion.getIden() + " " +
                                "Where iddeterminacionRegimen=" + repetida.getIden() + " ";
                        em.createNativeQuery(s).executeUpdate();
                        s="Update planeamiento.Entidaddeterminacion " +
                                "Set iddeterminacion=" + determinacion.getIden() + " " +
                                "Where iddeterminacion=" + repetida.getIden() + " ";
                        em.createNativeQuery(s).executeUpdate();
                        s="Update planeamiento.Determinaciongrupoentidad " +
                                "Set iddeterminacion=" + determinacion.getIden() + " " +
                                "Where iddeterminacion=" + repetida.getIden() + " ";
                        em.createNativeQuery(s).executeUpdate();
                        s="Update planeamiento.Determinaciongrupoentidad " +
                                "Set iddeterminacionGrupo=" + determinacion.getIden() + " " +
                                "Where iddeterminacionGrupo=" + repetida.getIden() + " ";
                        em.createNativeQuery(s).executeUpdate();
                        s="Update planeamiento.OperacionDeterminacion " +
                                "Set iddeterminacion=" + determinacion.getIden() + " " +
                                "Where iddeterminacion=" + repetida.getIden() + " ";
                        em.createNativeQuery(s).executeUpdate();
                        s="Update planeamiento.OperacionDeterminacion " +
                                "Set iddeterminacionoperadora=" + determinacion.getIden() + " " +
                                "Where iddeterminacionoperadora=" + repetida.getIden() + " ";
                        em.createNativeQuery(s).executeUpdate();
                        
                        // Obtenemos todas las referencias a la UNIDAD repetida
                        referenciasAUnidadRepetida = em.createNamedQuery("Vectorrelacion.obtenerVectorUnidad")
                        		.setParameter("valor", repetida.getIden())
                        		.getResultList();
                        for (Vectorrelacion vr : referenciasAUnidadRepetida) {
                        	if (vr.getIddefvector() == 3)
                        		vr.setValor(determinacion.getIden());
                        }
                        
                        // Elimina la determinación duplicada           
                        if (em.contains(repetida)) {
                        	contexto.log(LOG.INFO, "Eliminando determinación [" + repetida.getCodigo() + "] " + repetida.getNombre());
                        	em.remove(repetida);
                        }
                        
                        em.flush();
                	}
                }
            }

        } catch (Exception e){
        	throw new ExcepcionRefundido("Fallo al unificar unidades: "  + e.getMessage(), 23039);
        }
    }
}


