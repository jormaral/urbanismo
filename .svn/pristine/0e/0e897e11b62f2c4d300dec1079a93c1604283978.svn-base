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

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorConsultasLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GestorOperacionesDeterminacion")
public class GestorOperacionesDeterminacionBean implements GestorOperacionesDeterminacionLocal {
	
	private static final Logger log = Logger.getLogger(GestorOperacionesDeterminacionBean.class);
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	private String format = "(%d) [%s] del plan [%s] --> %s -->[%s] del plan [%s]";
	
	@EJB
	private GestorConsultasLocal gestorConsultas;
	@EJB
	private EliminadorEntidadesLocal eliminadorEntidades;

    /**
     * Default constructor. 
     */
    public GestorOperacionesDeterminacionBean() {
    }
    
    /**
     * Consiste en copiar las propiedades y vectores de Regulación de
     * la determinación iDetR a la determinación iDetO.
     * Sólo se tienen en cuenta las relaciones que no están apuntadas
     * desde OperacionRelacion con el tipo de operación "Adición", ya
     * que estas relaciones no son vigentes, sino que están pendientes de
     * ser ejecutadas como operaciones.
     * También se copian los documentos.
     * 
     * @param iDetO
     * @param iDetR
     * @throws ExcepcionRefundido 
     */
    private void adicionNormativa(Determinacion iDetO, Determinacion iDetR) throws ExcepcionRefundido{
         
    	log.debug("Adición normativa de determinacion operada: " + iDetO.getIden() + " con operador: " + iDetR.getIden());
    	// LOW jgarzon Se han hecho sólo pequeñas optimizaciones.
        String s;
        int idRelacion;
        TreeMap<Integer,Integer> tm;
        int iden;
        List<?> lista;
        int nIdensRegulaciones;
        int i;
        int idTramiteO;

        Iterator<Integer> it;
        
        try{
            // *********************
            // Regulación específica de iDetR
            idTramiteO=iDetO.getTramite().getIden();

            s="Select r.iden " +
                    "From planeamiento.Relacion r, planeamiento.Vectorrelacion vr " +
                    "Where r.iddefrelacion=" + ClsDatos.ID_DEFRELACION_REGULACIONESPECIFICA +
                    " And vr.iddefvector=" + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADAESPECIFICA + " " +
                    "And vr.idrelacion=r.iden And vr.valor=" + iDetR.getIden() + " " +
                    "And r.iden Not In (Select idrelacion " +
                    "From planeamiento.Operacionrelacion Where idtipooperacionrel=" +
                    ClsDatos.ID_TIPOOPERACIONRELACION_ADICION + ") ";
            
            tm=new TreeMap<Integer,Integer>();
            for(Object registro : em.createNativeQuery(s).getResultList()){
                idRelacion=Integer.parseInt(registro.toString());

                // Por cada relacion de regulación de iDetR, se inserta una
                //  relacion para iDetO y se guarda el mapeo en tm, ya que es
                //  necesario insertar todos los registros de la tabla Relacion
                //  antes de poder copiar las propiedades y vectores, debido a
                //  que uno de los vectores es un idRelacion que apunta a la
                //  regulación precedente (como un idPadre)
                s="Insert Into planeamiento.Relacion (iddefrelacion, idtramitecreador) " +
                        "Values ( " +ClsDatos.ID_DEFRELACION_REGULACIONESPECIFICA +", " +
                        idTramiteO + ") ";
                em.createNativeQuery(s).executeUpdate();
                iden=obtenerUltimoIden("planeamiento.Relacion");
                tm.put(idRelacion, iden);
            }

            // Copia las propiedades y vectores de regulación específica
            it = tm.keySet().iterator();
            while (it.hasNext()){
                idRelacion=it.next();
                iden=Integer.parseInt(tm.get(idRelacion).toString());

                // Propiedades
                s="Insert Into planeamiento.Propiedadrelacion (idrelacion, iddefpropiedad, " +
                        "valor) Select " + iden + ", iddefpropiedad, valor " +
                        "From planeamiento.Propiedadrelacion Where idrelacion=" + idRelacion;
                em.createNativeQuery(s).executeUpdate();

                // Vector que apunta a la determinacion idDefVector
                s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, valor) " +
                        "Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADAESPECIFICA +
                        ", " + iDetO.getIden() + ") ";
                em.createNativeQuery(s).executeUpdate();

                // Vector que apunta a la regulacion precedente idDefVector
                
                s="Select valor From planeamiento.Vectorrelacion " +
                        "Where idrelacion=" + idRelacion +
                        " And iddefvector=" + ClsDatos.ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA ;
                lista = em.createNativeQuery(s).getResultList();
                if (lista.size() == 0) {
                    s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, " +
                            "valor) Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA + ", 0) ";
                } else {
                    s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, " +
                            "valor) Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_REGULACIONPRECEDENTEESPECIFICA + ", " +
                            Integer.parseInt(lista.get(0).toString()) + ") ";
                }
                em.createNativeQuery(s).executeUpdate();
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
                
                tm=new TreeMap<Integer,Integer>();
                for (Object registro : em.createNativeQuery(s).getResultList()){
                    idRelacion=Integer.parseInt(registro.toString());

                    // Por cada relacion de regulación de iDetR, se inserta una
                    //  relacion para iDetO y se guarda el mapeo en tm, ya que es
                    //  necesario insertar todos los registros de la tabla Ralacion
                    //  antes de poder copiar las propiedades y vectores, debido a
                    //  que uno de los vectores es un idRelacion que apunta a la
                    //  regulación precedente (como un idPadre)
                    s="Insert Into planeamiento.Relacion (iddefrelacion, idtramitecreador) " +
                            "Values ( " +ClsDatos.ID_DEFRELACION_REGULACION[i] +", " +
                            idTramiteO + ") ";
                    em.createNativeQuery(s).executeUpdate();
                    iden=obtenerUltimoIden("planeamiento.Relacion");
                    tm.put(idRelacion, iden);
                }

                // Copia las propiedades y vectores de 'Determinación reguladora'
                it=tm.keySet().iterator();
                while (it.hasNext()){
                    idRelacion=it.next();
                    iden=tm.get(idRelacion);

                    // Propiedades
                    s="Insert Into planeamiento.Propiedadrelacion (idrelacion, iddefpropiedad, " +
                            "valor) Select " + iden + ", iddefpropiedad, valor " +
                            "From planeamiento.Propiedadrelacion Where idrelacion=" + idRelacion;
                    em.createNativeQuery(s).executeUpdate();

                    // Vector que apunta a la determinacion regulada idDefVector
                    s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, valor) " +
                            "Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADA[i] +
                            ", " + iDetO.getIden() + ") ";
                    em.createNativeQuery(s).executeUpdate();

                    // Vector que apunta a la determinacion reguladora idDefVector
                    s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, valor) " +
                            "Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADORA[i] +
                            ", " + iDetO.getIden() + ") ";
                    em.createNativeQuery(s).executeUpdate();
                }
            }

            // Copia los documentos
            s="Insert Into planeamiento.Documentodeterminacion (iddeterminacion, iddocumento) " +
                    "Select " + iDetO.getIden() + ", iddocumento From planeamiento.Documentodeterminacion " +
                    "Where iddeterminacion=" + iDetR.getIden();
            em.createNativeQuery(s).executeUpdate();
            
            em.flush();
        } catch (Exception e){
        	throw new ExcepcionRefundido("Fallo en la operación adición normativa. " + e.getMessage(), 23204);
        }

    }
    
    /**
     * Copiar los valores de referencia de la determinación operadora a la determinación operada, y
     * reasignar los punteros de EntidadDeterminacionRegimen que apuntan a las opciones iDetR para
     * que apunten a las nuevas opciones de iDetO.
     * 
     * @param determinacionOperada
     * @param determinacionOperadora
     * @param contexto
     * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private void adicionValorReferencia(Determinacion determinacionOperada, Determinacion determinacionOperadora, ContextoRefundido contexto) throws ExcepcionRefundido {

    	log.debug("Adición valor referencia de determinacion operada: " + determinacionOperada.getIden() + " con operador: " + determinacionOperadora.getIden());
        String s="";
        Determinacion iDetPadre;

        try {
        	List<Opciondeterminacion> lista = (List<Opciondeterminacion>)em.createNamedQuery("Opciondeterminacion.buscarPorDet")
        			.setParameter("idDeterminacion", determinacionOperadora.getIden()).getResultList();
            
        	Opciondeterminacion odNueva;
            for (Opciondeterminacion od : lista) {
                // Comprueba si ya existe la Opcion para la determinación operada
                List<Opciondeterminacion> existentes = em.createNamedQuery("Opciondeterminacion.buscarPorDetYValorRef")
                		.setParameter("idDeterminacion", determinacionOperada.getIden())
                		.setParameter("idValorRef", od.getDeterminacionByIddeterminacionvalorref().getIden())
                		.getResultList();
                if (existentes.size() > 0) {
                    // Existe
                	odNueva = existentes.get(0);
                } else {
                    // No existe
                    odNueva = new Opciondeterminacion();
                    odNueva.setDeterminacionByIddeterminacion(determinacionOperada);
                    odNueva.setDeterminacionByIddeterminacionvalorref(od.getDeterminacionByIddeterminacionvalorref());
                    em.persist(odNueva);
                    em.flush();
                }

                // Reasigna los idOpcion de EntidadDeterminacionRegimen que apuntaban a
                //  la opción de la determinación operadora, para que apunten a la
                //  nueva opción.
                s = "Update planeamiento.Entidaddeterminacionregimen " +
                        "Set iddeterminacionregimen=" + determinacionOperada.getIden() + " " +
                        "Where iddeterminacionregimen=" + determinacionOperadora.getIden() + " " +
                        "And idopciondeterminacion=" + od.getIden();
                em.createNativeQuery(s).executeUpdate();

                s = "Update planeamiento.Entidaddeterminacionregimen " +
                        "Set idopciondeterminacion=" + odNueva.getIden() + " " +
                        "Where idopciondeterminacion=" + od.getIden();
                em.createNativeQuery(s).executeUpdate();

                em.flush();
                
                // Averigua el iden de la carpeta de determinaciones aportadas
                if (od.getDeterminacionByIddeterminacionvalorref().getDeterminacionByIdpadre() != null && 
                		determinacionOperadora.getIden() == od.getDeterminacionByIddeterminacionvalorref().getDeterminacionByIdpadre().getIden()) {
                	iDetPadre = determinacionOperada;
                } else {
                	iDetPadre = crearCarpetaDeterminacionesAportadas(determinacionOperada.getTramite(), determinacionOperadora.getTramite());
                }

                // Reasigna el trámite de la determinacion valor de referencia
                
                reasignarDeterminacion(determinacionOperada, iDetPadre, od.getDeterminacionByIddeterminacionvalorref(), 0, contexto);
            }
        } catch (Exception e) {
            throw new ExcepcionRefundido("Fallo en la operación de adición valor de referencia. " + e.getMessage(), 23208 );
        }

    }
    
   
    /**
	 * Se cambia el idTramite de la determinación operada, y se cambia su idPadre para
	 * que apunte a la determinación "Determinaciones aportadas por..." que se
	 * debe crear en el trámite de determinación operadora.
	 * El orden se pone a cero para que el procedimiento de reasignarDeterminacion le ponga
	 * el último + 1
	 * 
	 * @param determinacionOperada
	 * @param determinacionOperadora
	 * @param contexto
     * @throws ExcepcionRefundido 
	 */
	private void aportacionDeterminacion(Determinacion determinacionOperada, Determinacion determinacionOperadora, ContextoRefundido contexto) throws ExcepcionRefundido{
         
		log.debug("Aportación de determinacion operada: " + determinacionOperada.getCodigo() + ", " + determinacionOperada.getNombre() 
    			+ " Determinacion Operadora " + determinacionOperadora.getCodigo() + ", " + determinacionOperadora.getNombre());
        Determinacion iDetPadre;
        try {
            if(determinacionOperada.getTramite().getIden()!=determinacionOperadora.getTramite().getIden()){
                // Determinación de la que va a colgar
                iDetPadre = crearCarpetaDeterminacionesAportadas(determinacionOperada.getTramite(), determinacionOperadora.getTramite());
                // Reasignar el IdTramite
                reasignarDeterminacion(determinacionOperada, iDetPadre, determinacionOperadora, 0, contexto);
            }
        } catch (Exception e){
        	throw new ExcepcionRefundido("Fallo en la operación de aportación de una determinación. " + e.getMessage(), 23209);
        }
        
    }
    
    /**
	 * Se cambia el idTramite de la determinación iDetR, y se cambia su idPadre para
	 * que apunte a la determinación padre de iDetO. El orden que se le asigna es el siguiente al
	 * orden que tiene iDetO
	 * 
	 * @param iDetO
	 * @param iDetR
	 * @param contexto
     * @throws ExcepcionRefundido 
	 */
	private void aportacionDeterminacionSobreAnterior(Determinacion iDetO, Determinacion iDetR, ContextoRefundido contexto) throws ExcepcionRefundido {
         
		log.debug("Aportación sobre anterior de determinacion operada: " + iDetO.getIden() + " con operador: " + iDetR.getIden());
        Determinacion iDetPadre;

        try {
            // Determinación de la que va a colgar
            iDetPadre = iDetO.getDeterminacionByIdpadre();
            // Reasignar el IdTramite
            reasignarDeterminacion(iDetO, iDetPadre, iDetR, iDetO.getOrden() +1,contexto);
            
        } catch (Exception e){
        	throw new ExcepcionRefundido("Fallo en la operación aportacion de determinación sobre padre. " + e.getMessage(), 23203);
        }

    }
    
    /**
	 * Se cambia el idTramite de la determinación iDetR, y se cambia su idPadre para
	 * que apunte a la determinación iDetO. Se le asigna el orden 1 para que sea la primera
	 * determinación hija.
	 * @param determinacionOperada
	 * @param determinacionOperadora
	 * @param contexto
     * @throws ExcepcionRefundido 
	 */
	private void aportacionDeterminacionSobrePadre(Determinacion determinacionOperada, Determinacion determinacionOperadora, ContextoRefundido contexto) throws ExcepcionRefundido{
        try {
        	log.debug("Aportación sobre padre de determinacion operada: " + determinacionOperada.getIden() + " con operador: " + determinacionOperadora.getIden());
            // Reasignar el IdTramite
            reasignarDeterminacion(determinacionOperada, determinacionOperada, determinacionOperadora, 0, contexto);

        } catch (Exception e){
        	throw new ExcepcionRefundido("Fallo en la operación Aportacion Determinacion Sobre Padre. " + e.getMessage(), 23203);
        }
    }
	
	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesDeterminacionLocal#crearCarpetaDeterminacionesAportadas(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite)
     */
    @Override
    @SuppressWarnings("unchecked")
	public Determinacion crearCarpetaDeterminacionesAportadas(Tramite tramiteO, Tramite tramiteR){
        String txtDeterminacion;
        Determinacion determinacion = null;
        String codigoDet="";
        String siguienteApartado;

        try {
            txtDeterminacion = ClsDatos.TEXTO_APORTADAS + "["+tramiteR.getPlan().getCodigo()+"] " +tramiteR.getPlan().getNombre().trim();
            if (txtDeterminacion.length() > 220) {
                txtDeterminacion = txtDeterminacion.substring(0, 220);
            }

            // Averigua si la determinación ya existe por haber sido creada con anterioridad
            List<Determinacion> listaDet = (List<Determinacion>)em.createNamedQuery("Determinacion.obtenerPorTramiteCaracterYNombre")
            		.setParameter("idTramite", tramiteO.getIden())
            		.setParameter("idCaracter", ClsDatos.ID_CARACTER_ENUNCIADO)
            		.setParameter("nombre", txtDeterminacion).getResultList();
            if (listaDet.size() == 0) {
                codigoDet = gestorConsultas.getSiguienteCodigoDeterminacion(tramiteO.getIden());
                siguienteApartado=gestorConsultas.obtenerUltimoApartadoDeterminacion(null, tramiteO.getIden(), true);
                determinacion = new Determinacion();
                determinacion.setApartado(siguienteApartado);
                determinacion.setBsuspendida(false);
                determinacion.setCodigo(codigoDet);
                determinacion.setIdcaracter(ClsDatos.ID_CARACTER_ENUNCIADO);
                determinacion.setNombre(txtDeterminacion);
                determinacion.setTramite(tramiteO);
                Integer orden = (Integer)em.createNamedQuery("Determinacion.obtenerOrdenMaximo")
                		.setParameter("idTramite", tramiteO.getIden())
                		.getSingleResult();
                // En ocasiones el orden se ha recibido a null
                if (orden != null) {
                	determinacion.setOrden(++orden);
                } else {
                	determinacion.setOrden(1);
                }
                
                em.persist(determinacion);
                em.flush();
                
                log.debug("Creada carpeta determinaciones aportadas: " 
                		+ determinacion.getApartado() + " " 
                		+ determinacion.getNombre() + 
                		" código: " + determinacion.getCodigo()
                		+ " sobre el trámite " + tramiteO.getIden()
                		+  " con código " + tramiteO.getCodigofip());
            } else {
                determinacion = listaDet.get(0);
            }
        } catch (Exception e) {
            log.error("Fallo en Crear Carpeta Determinaciones Aportadas. " + e.getMessage(),e );
            return null;
        }
        return determinacion;
    }
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesDeterminacionLocal#ejecutar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(Operaciondeterminacion operacionDeterminacion,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		
        int idTipoOperacion=(int) operacionDeterminacion.getIdtipooperaciondet();
         
        Determinacion iDetO= operacionDeterminacion.getDeterminacionByIddeterminacion();
        Determinacion iDetR= operacionDeterminacion.getDeterminacionByIddeterminacionoperadora();
        
        Operacion operacion= operacionDeterminacion.getOperacion();
        int idOperacion = operacion.getIden();
        // Se elimina la operación antes de ejecutarla, para evitar el "aviso" 
        // de que se ha eliminado una operación que aún no ha sido ejecutada.
        // Se ha configurado operacion para que borre en cascada sus dependencias.
        eliminadorEntidades.eliminar(operacion, null);
        em.flush();

        String codPlanR=iDetR.getTramite().getPlan().getCodigo();
        String codPlanO=iDetO.getTramite().getPlan().getCodigo();

        if (em.contains(iDetO) && em.contains(iDetR)) {
        	if (!iDetR.isBsuspendida()){
                switch (idTipoOperacion) {
                    case 1:
                        // Eliminación
                    	contexto.log(LOG.INFO, String.format(format, idOperacion,iDetR.getCodigo(),codPlanR,"Eliminación", iDetO.getCodigo(),codPlanO));
                        eliminar(iDetO, iDetR, operacion.getTramite());
                        break;

                    case 4:
                        // Adición Normativa
                    	contexto.log(LOG.INFO, String.format(format, idOperacion,iDetR.getCodigo(),codPlanR,"Adición Normativa", iDetO.getCodigo(),codPlanO));
                        adicionNormativa(iDetO, iDetR);
                        break;

                    case 6:
                    	// Sustitución Normativa Completa
                    	contexto.log(LOG.INFO, String.format(format, idOperacion,iDetR.getCodigo(),codPlanR,"Sustitución Normativa Completa", iDetO.getCodigo(),codPlanO));
                        sustitucionNormativaCompleta(iDetO, iDetR);
                        break;

                    case 7:
                        // Suspensión
                    	contexto.log(LOG.INFO, String.format(format, idOperacion,iDetR.getCodigo(),codPlanR,"Suspensión Grafica", iDetO.getCodigo(),codPlanO));
                        suspension(iDetO);
                        break;

                    case 8:
                        // Adición de Valor de Referencia
                    	contexto.log(LOG.INFO, String.format(format, idOperacion,iDetR.getCodigo(),codPlanR,"Adición de Valor de Referencia", iDetO.getCodigo(),codPlanO));
                        adicionValorReferencia(iDetO, iDetR,contexto);
                        break;

                    case 9:
                        // Aportación de Determinación 
                    	contexto.log(LOG.INFO, String.format(format, idOperacion,iDetR.getCodigo(),codPlanR,"Aportación de Determinación", iDetO.getCodigo(),codPlanO));
                        aportacionDeterminacion(iDetO, iDetR,contexto);
                        break;

                    case 11:
                        // Levantamiento de Suspensión 
                    	contexto.log(LOG.INFO, String.format(format, idOperacion,iDetR.getCodigo(),codPlanR,"Levantamiento de Suspensión", iDetO.getCodigo(),codPlanO));
                        levantamientoSuspension(iDetO, iDetR);
                        break;

                    case 12:
                        // Aportación de Determinación sobre Determinación Padre 
                    	contexto.log(LOG.INFO, String.format(format, idOperacion,iDetR.getCodigo(),codPlanR,"Aportación de Determinación sobre Determinación Padre", iDetO.getCodigo(),codPlanO));
                        aportacionDeterminacionSobrePadre(iDetO, iDetR,contexto);
                        break;

                    case 13:
                        // Aportación de Determinación sobre Determinación Anterior 
                    	contexto.log(LOG.INFO, String.format(format, idOperacion,iDetR.getCodigo(),codPlanR,"Aportación de Determinación sobre Determinación Anterior", iDetO.getCodigo(),codPlanO));
                        aportacionDeterminacionSobreAnterior(iDetO, iDetR,contexto);
                        break;

                    default:
                    	contexto.log(LOG.AVISO, idOperacion + " No se opera. La operación iden=" + idTipoOperacion + " no está implementada en esta versión.");
                        break;
                }

            } else {
            	contexto.log(LOG.AVISO, idOperacion + " No se opera. La determinación operadora [" + iDetR.getCodigo() + "] está suspendida.");
            }
        } else {
        	contexto.log(LOG.AVISO, idOperacion + " No se opera. " +
        			(!em.contains(iDetR) ?  " La determinación operadora [" + iDetR.getCodigo() + "] ha sido eliminada. ":"") +
        			(!em.contains(iDetO) ?  " La determinación operada [" + iDetO.getCodigo() + "] ha sido eliminada. ":"")
        		);
        }
        
	}
	
	/**
     * Se efectúan las siguientes acciones relacionadas con la determinación iDetO que
     * se va a eliminar:
     *           1 - Sus determinaciones hijas se cuelgan de su determinación padre, en Determinacion.
     *           2 - Se ponen a "null" los idOpcionDeterminacion de EntidadDeterminacionRegimen que
     *               apunten alguna de las opciones que se van a eliminar, y se añade un régimen específico
     *               que avise de que las opciones se han eliminado debido a una operación.
     *           3 - Se ponen a "null" los idDeterminacionRegimen de EntidadDeterminacionRegimen que
     *               apunten a la determinación que se va a borrar, y se añade un régimen específico que
     *               avise de que la determinación de régimen se ha eliminado por una operación.
     *                       
     *               Se modifica lo anterior el 17/01/2011, para pasar a eliminar el valor completo. Por
     *               lo tanto ya no se añade ningún régimen específico; se borran los existentes, y
     *               los registros de EDR que apunten a la determinación a borrar. Si el caso queda
     *               huérfano también se borra. Y si como consecuencia de esto el ED queda huérfano,
     *               también se elimina.         
     *           4 - Se eliminan la determinación iDetO y sus dependencias.
     *           
     * @param iDetO
     * @param iDetR
     * @param tramiteOrdenante
	 * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private void eliminar(Determinacion iDetO, Determinacion iDetR, Tramite tramiteOrdenante) throws ExcepcionRefundido{
    	
    	log.debug("Eliminación de determinacion operada: " + iDetO.getIden() + " con operador: " + iDetR.getIden());
         
        try {
            //  1 - Sus determinaciones hijas se cuelgan de su determinación padre, en Determinacion.
        	for(Determinacion hija : iDetO.getDeterminacionsForIdpadre()) {
        		hija.setDeterminacionByIdpadre(iDetO.getDeterminacionByIdpadre() == null? null: iDetO.getDeterminacionByIdpadre());
        	}
            
         // 2 - EntidadDeterminacionRegimen --> idOpcionDeterminacion=null
            List<Opciondeterminacion> ods = em.createNamedQuery("Opciondeterminacion.buscarPorDetOValorRef")
            		.setParameter("idDeterminacion", iDetO.getIden())
            		.setParameter("idValorRef", iDetO.getIden())
            		.getResultList();
            Regimenespecifico regesp;
            Query consulta = em.createNamedQuery("Regimenespecifico.obtenerMaxOrden");
            for (Opciondeterminacion od: ods) {
            	for (Entidaddeterminacionregimen edr : od.getEntidaddeterminacionregimens()) {
            		edr.setOpciondeterminacion(null);
            		regesp = new Regimenespecifico();
            		regesp.setEntidaddeterminacionregimen(edr);
            		regesp.setNombre("Valor modificado");
            		regesp.setTexto("La determinación valor [" + iDetO.getNombre() + "] " +
                            "ha sido eliminada por el plan [" + iDetO.getTramite().getPlan().getCodigo()  + "]");
            		regesp.setOrden((Integer)consulta.setParameter("idEntDetReg", edr.getIden()).getSingleResult());
            		em.persist(regesp);
            		em.flush();
            	}
            }

            // 3 - EntidadDeterminacionRegimen --> idDeterminacionRegimen=null
            
            for (Entidaddeterminacionregimen edr : iDetO.getEntidaddeterminacionregimens()) {
            	
            	edr.setDeterminacion(null);
            	
            	regesp = new Regimenespecifico();
        		regesp.setEntidaddeterminacionregimen(edr);
        		regesp.setNombre("Valor modificado");
        		regesp.setTexto("La determinación de régimen [" + iDetO.getNombre() + "] " +
                        "ha sido eliminada por el plan [" + iDetO.getTramite().getPlan().getCodigo() + "]");
        		regesp.setOrden((Integer)consulta.setParameter("idEntDetReg", edr.getIden()).getSingleResult());
        		em.persist(regesp);
            }

            em.flush();
            
            //  4 - Elimina determinación
            if (em.contains(iDetO)) {
            	log.debug("Eliminando determinacion: " + iDetO.getIden());
            	eliminadorEntidades.eliminar(iDetO, null);
            }
            
            em.flush();

        } catch (Exception e) {
            throw new ExcepcionRefundido("Fallo en la operación de eliminación de determinaciones. " + e.getMessage(), 23201);
        }
        
    }
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesDeterminacionLocal#getDeterminacionCarpetaTramiteBase(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, java.util.List)
	 */
    @SuppressWarnings("unchecked")
    @Override
	public Determinacion getDeterminacionCarpetaTramiteBase(Tramite iTramite, List<Integer> listaIdTramitesBase){
    	log.debug("determinacionCarpetaTramiteBase");
        String aptdo;
        String codigo;
        
        try{
            // Primero se busca en los trámites base.
            List<Determinacion> lista = em.createNamedQuery("Determinacion.buscarPorCodigoEnTramites")
            		.setParameter("codigo", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA)
            		.setParameter("listaTramites", listaIdTramitesBase)
            		.getResultList();
            if (lista.size()>0) {
                return lista.get(0);
            }    
            
            // Si no existe la determinación 'Carpeta' en un trámite base,
            //  se busca en el trámite solicitado. Para ello, se busca entre
            //  los valores de referencia de cualquiera de las determinaciones
            //  con caracter ClsDatos.ID_CARACTER_GRUPODEENTIDADES que están
            //  aplicadas a alguna entidad del trámite en curso. 
            //  Primero se intenta buscar una determinación 'Grupo de Entidades'
            //  dentro del propio trámite y, si no se encuentra, se busca en
            //  cualquier otro trámite, aunque siempre sobre entidades del
            //  trámite en curso.
            // Para localizar la determinación 'Carpeta' dentro de todos los
            //  valores de referencia encontrados, se miran dos cosas: puede que
            //  tenga el mismo código que la determinación carpeta del plan
            //  base, o puede que está vinculada a la determinación del plan base
            //  que tenga dicho código. Cualquiera de las dos condiciones sirve
            //  para identificar la determinación 'Carpeta'.
            
            // Primero, dentro de las determinaciones del trámite en curso.
            lista = em.createNamedQuery("Determinacion.obtenerCarpeta")
            		.setParameter("idTramite", iTramite.getIden())
            		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
            		.setParameter("codigoVR", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA)
            		.setParameter("codigo", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA)
            		.getResultList();
            
            if (lista.size()>0) {
                return lista.get(0);
            }
            
            // En segundo lugar, dentro de las determinaciones de cualquier otro
            //  trámite que están vinculadas a las entidades del trámite en curso.
            lista = em.createNamedQuery("Determinacion.obtenerCarpetaCualquierTramite")
            		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
            		.setParameter("codigoVR", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA)
            		.setParameter("codigo", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA)
            		.getResultList();
            
            if (lista.size()>0) {
                return lista.get(0);
            }
            
            // Si en este punto no se ha logrado identificar ninguna determinación
            //  'Carpeta', hay que añadir una en el trámite en curso, que se 
            //  añadirá como valor de referencia de la determinación 'Grupo de Entidades'
            Determinacion iDetGrupoDeEntidades=gestorConsultas.obtenerDeterminacionGrupoDeEntidadesPorTramite(iTramite);
            
            
            
            // Si no existe ninguna determinación 'Grupo de Entidades' o bien 
            //  es de un plan base, no se le pueden añadir valores de referencia. 
            //  Por lo tanto, también hay que crear una determinacion 'Grupo de Entidades' 
            //  en el trámite en curso.

            if (iDetGrupoDeEntidades==null || 
            		listaIdTramitesBase.contains(iDetGrupoDeEntidades.getTramite().getIden())){
            	log.warn("    No existe ninguna determinación 'Grupo de Entidades' disponible para el idTramite=" + iTramite.getIden());
                log.warn("        Se va a crear una en dicho trámite.");
                
                aptdo=gestorConsultas.obtenerUltimoApartadoDeterminacion(null, iTramite.getIden(),true);
                codigo=gestorConsultas.getSiguienteCodigoDeterminacion(iTramite.getIden());
                
                int orden = (Integer)em.createNamedQuery("Determinacion.obtenerOrdenMaximo")
                		.setParameter("idTramite", iTramite.getIden())
                		.getSingleResult();
                
                iDetGrupoDeEntidades = new Determinacion();
                iDetGrupoDeEntidades.setApartado(aptdo);
                iDetGrupoDeEntidades.setBsuspendida(false);
                iDetGrupoDeEntidades.setCodigo(codigo);
                iDetGrupoDeEntidades.setDeterminacionByIdpadre(null);
                iDetGrupoDeEntidades.setIdcaracter(ClsDatos.ID_CARACTER_GRUPODEENTIDADES);
                iDetGrupoDeEntidades.setTramite(iTramite);
                iDetGrupoDeEntidades.setNombre("Grupo de entidades");
                iDetGrupoDeEntidades.setOrden(++orden);
                em.persist(iDetGrupoDeEntidades);
                em.flush();
            }
            
            // Crear la determinación 'Carpeta'
            aptdo=gestorConsultas.obtenerUltimoApartadoDeterminacion(iDetGrupoDeEntidades, iTramite.getIden(),true);
            codigo=gestorConsultas.getSiguienteCodigoDeterminacion(iTramite.getIden());
            
            int orden = (Integer)em.createNamedQuery("Determinacion.obtenerOrdenMaximoPadre")
            		.setParameter("idTramite", iTramite.getIden())
            		.setParameter("idPadre", iDetGrupoDeEntidades.getIden())
            		.getSingleResult();
            
            Determinacion carpeta = new Determinacion();
            carpeta.setTramite(iTramite);
            carpeta.setDeterminacionByIdpadre(iDetGrupoDeEntidades);
            carpeta.setIdcaracter(ClsDatos.ID_CARACTER_VALORREFERENCIA);
            carpeta.setApartado(aptdo);
            carpeta.setNombre("Grupo de entidades");
            carpeta.setCodigo(codigo);
            carpeta.setBsuspendida(false);
            carpeta.setOrden(++orden);
            
            em.persist(carpeta);
            
            // Crear la opción correspondiente
            Opciondeterminacion od = new Opciondeterminacion();
            od.setDeterminacionByIddeterminacion(iDetGrupoDeEntidades);
            od.setDeterminacionByIddeterminacionvalorref(carpeta);
            em.persist(od);

            return carpeta;
        } catch (Exception e){
            log.error("Fallo en determinacionCarpetaPorTramite: " + e.getMessage() + ". Código 23021." );
        }
        log.warn("    No se ha encontrado la determinación 'Carpeta' del idTramite=" + iTramite.getIden());
        return null;
    }
	
	/**
	 * Marcar la determinación iDetO como 'No suspendida'
	 * 
	 * @param iDetO
	 * @param iDetR
	 */
	private void levantamientoSuspension(Determinacion iDetO, Determinacion iDetR){
		log.debug("Levantamiento de suspensión de determinacion operada: " + iDetO.getIden() + " con operador: " + iDetR.getIden());
        iDetO.setBsuspendida(false); 
    }

    /**
	 * 
	 * @param tabla
	 * @return
	 */
	private int obtenerUltimoIden(String tabla){
        String s;

        try {
            s="Select Max(t.iden) From " + tabla + " as t ";
            List<?> lista= em.createNativeQuery(s).getResultList();
            if (lista!=null && lista.size() > 0 && lista.get(0) != null){
            	return Integer.parseInt(lista.get(0).toString());
            } else {
            	return 0;
            }           
        } catch (Exception e){
            log.error("Fallo en ultimoIden. " +e.getMessage());
        }
        return 0;
    }

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesDeterminacionLocal#reasignarDeterminacion(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
    @SuppressWarnings("unchecked")
	@Override
    public boolean reasignarDeterminacion(Determinacion determinacionOperada, Determinacion determinacionPadre, Determinacion detOperador, int orden, ContextoRefundido contexto){
    	log.debug("Reasignando determinacion Operada " + determinacionOperada.getCodigo() + ", " + determinacionOperada.getNombre() + ", " + determinacionOperada.getOrden()
    			+ " Determinacion Operadora " + detOperador.getCodigo() + ", " + detOperador.getNombre() + ", " + detOperador.getOrden()
    			+ " Nuevo orden " + orden);
    	if (determinacionPadre != null) {
    		log.debug("Determinacion Padre " + determinacionPadre.getCodigo() + ", " + determinacionPadre.getNombre() + ", " + determinacionPadre.getOrden());
    	}
        String maxCod;
        String s;
        int nRegistros;
        Determinacion iDetGrupoDeEntidadesO;
        Determinacion iDetGrupoDeEntidadesR;
        Boolean esGrupoEntidad;
        Boolean esVRgrupoEntidad;

        try {
            // Si los trámites son el mismo, se aborta el procedimiento.
            if (determinacionOperada.getTramite().getIden() == detOperador.getTramite().getIden()){
            	log.debug("Tienen el mismo padre ("+ determinacionOperada.getTramite().getIden() +"), se aborta la reasignación " );
                return true;
            }
            
            // Si alguno de los dos es un trámite base, también se aborta.
            List<Integer> listaIdTramitesBase = (List<Integer>)contexto.getParametro(ContextoRefundidoBasico.ID_TRAMITES_BASE);
            if (listaIdTramitesBase.contains(determinacionOperada.getTramite().getIden()) ||
            		listaIdTramitesBase.contains(detOperador.getTramite().getIden())){
            	log.debug("Uno de los trámites pertenece a un plan base, se aborta la reasignación.");
                return true;
            }
 
            iDetGrupoDeEntidadesO=gestorConsultas.obtenerDeterminacionGrupoDeEntidadesPorTramite(determinacionOperada.getTramite());
            iDetGrupoDeEntidadesR=gestorConsultas.obtenerDeterminacionGrupoDeEntidadesPorTramite(detOperador.getTramite());

            // Averigua iDetR si es 'Grupo de Entidades' o alguno de sus valores de referencia
            esGrupoEntidad=false;
            esVRgrupoEntidad=false;
            if (iDetGrupoDeEntidadesR!=null){
                if (iDetGrupoDeEntidadesR.getIden()==detOperador.getIden()){
                    esGrupoEntidad=true;
                } else {
                	List<?> lista= em.createNamedQuery("Opciondeterminacion.buscarPorDetYValorRef")
                    		.setParameter("idDeterminacion", detOperador.getIden())
                    		.setParameter("idValorRef", iDetGrupoDeEntidadesR.getIden())
                    		.getResultList();
                    
                    esVRgrupoEntidad=lista.size() > 0;
                }
            }

            // Si detOperador esGrupoEntidad=true y ya existe otra determinación de este tipo
            //  en el iTramiteO, la determinación no se reasigna, y se fuerza la salida
            //  del procedimiento.
            if (esGrupoEntidad && iDetGrupoDeEntidadesO!=null){
            	log.debug("Es grupo de entidad");
                return true; 
            }

            // ídem si es un valor de referencia de la determinación 'Grupo de Entidades', y ya
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
                nRegistros=Integer.parseInt(em.createNativeQuery(s).getResultList().get(0).toString());
                if (nRegistros>0){
                	log.debug("Es un valor de referencia de grupo de entidades");
                    return true;
                }
            }
            
            // Reasigna los datos de la determinación iDetR para encajarla en el trámite iTramiteO
            // Código
            maxCod = gestorConsultas.getSiguienteCodigoDeterminacion(determinacionOperada.getTramite().getIden());
            // Al codigo que le corresponde, le vuelve a poner el primer caracter que
            //  tenia el código original, por si se trata de una determinación cuyo
            //  primer carater es significativo.
            s=detOperador.getCodigo().substring(0,1);
            
            log.debug("La determinacion con código " + detOperador.getCodigo() + " pasa a tener código " + s + maxCod.substring(1));
            // Se hace la modificación
            detOperador.setCodigo(s + maxCod.substring(1));

            // Cambia el padre de la determinación.
            nRegistros=0;
            
            detOperador.setDeterminacionByIdpadre(determinacionPadre);
               
            // Orden de la determinación
            if (orden==0){
                // Se calcula el máximo orden de las que tienen el mismo padre
                s="Select Max(orden) From planeamiento.Determinacion Where idpadre";
                if (determinacionPadre==null){
                    s = s + " Is Null And idtramite=" + determinacionOperada.getTramite().getIden();
                } else {
                    s = s +"=" + determinacionPadre.getIden();
                }
                
                log.debug("Calculando orden con consulta: " + s);
                
                List<?> lista= em.createNativeQuery(s).getResultList();
                if (lista.get(0)==null){
                	log.debug("No se han obtenido resultados");
                    orden=1;
                } else {
                    orden=1+(Integer)lista.get(0);
                    log.debug("Orden calculado: " + orden);
                }
            } else {
            	log.debug("Orden asignado, recalculando posteriores");
                // Se le suma 1 a todos los órdenes mayores o iguales al orden que se le va a asignar, y luego
                //  se le pone su orden.
                s="Update planeamiento.Determinacion Set orden=orden+1 Where orden>=" + orden + " And idpadre";
                if (determinacionPadre==null){
                    s = s + " Is Null And idtramite=" + determinacionOperada.getTramite().getIden();
                } else {
                    s = s +"=" + determinacionPadre.getIden();
                }
                em.createNativeQuery(s).executeUpdate();
                em.flush();
            }
            
            log.debug("Asignando orden " + orden +" a la determinacion " + detOperador.getCodigo());
            detOperador.setOrden(orden);
            
            // Reasigna los datos de sus dependencias

            // Sus documentos, si los tiene
            for ( Documentodeterminacion  docdet : detOperador.getDocumentodeterminacions()) {
                docdet.getDocumento().setTramite(determinacionOperada.getTramite());
            }

            // Reasigna el idTramite
            detOperador.setTramite(determinacionOperada.getTramite());
            
            log.debug("La determinación pasa al trámite: " + determinacionOperada.getTramite().getIden() + " (" + determinacionOperada.getTramite().getCodigofip() + ")");
            
            // Reasigna el idTramiteCreador de sus relaciones
            s="Update planeamiento.Relacion Set idtramitecreador=" + determinacionOperada.getTramite().getIden() + " " +
                    "Where iden In (Select VR.idrelacion From planeamiento.vectorrelacion VR, " +
                    "diccionario.tabla TB, diccionario.defvector DV " + 
                    "Where VR.valor=" + detOperador.getIden() + " And VR.iddefvector=DV.iden " +
                    "And DV.idtabla=TB.iden And TB.nombre='DETERMINACION')";
            em.createNativeQuery(s).executeUpdate();

            em.flush();
            
            List<Determinacion> dets = em.createNamedQuery("Determinacion.buscarHijas")
            		.setParameter("idPadre", detOperador.getIden())
            		.getResultList();
            
            // Sus hijas, si las tiene, 
            for (Determinacion hija: dets) {
                log.debug("Reasignando determinacion hija " + hija.getApartado() + " " + hija.getNombre() + " Codigo: " + hija.getCodigo() + " Orden: " + hija.getOrden());
                reasignarDeterminacion(detOperador, detOperador, hija, hija.getOrden() ,contexto);
            }
            
            dets = em.createNamedQuery("Determinacion.obtenerValoresReferenciaOpcion")
            		.setParameter("idDeterminacion", detOperador.getIden())
            		.getResultList();
            
            // Sus valores de referencia, si los tiene y no son hijas
            for (Determinacion detValorReferencia: dets) {
            	log.debug("Reasignando determinacion por opcion " + detValorReferencia.getApartado() + " " + detValorReferencia.getNombre() + " Codigo: " + detValorReferencia.getCodigo() + " Orden: " + detValorReferencia.getOrden());
                reasignarDeterminacion(detOperador, detOperador, detValorReferencia, 0, contexto);
            }
            
        } catch (Exception e) {
        	contexto.log(LOG.ERROR, "Fallo en reasignarDeterminacion. " + e.getMessage());
            return false;
        }

        em.flush();
        return true;
    }
	
	/**
	 * Marcar la determinación iDetO como 'suspendida'
	 * 
	 * @param iDetO
	 * @param iDetR
	 * @throws ExcepcionRefundido 
	 */
	private void suspension(Determinacion determinacionOperada) {
		determinacionOperada.setBsuspendida(true);
    }
	
	/**
	 * Se compone de cinco pasos:
	 * - Eliminación normativa. Se eliminan todas las propiedades y vectores
	 * relacionados con la regulación
	 * - Operación opDet_AdicionNormativa
	 * - Sustituir nombre y texto
	 * - Sustituir documentos
	 * @param iDetO
	 * @param iDetR
	 * @throws ExcepcionRefundido 
	 */
	private void sustitucionNormativaCompleta(Determinacion iDetO, Determinacion iDetR) throws ExcepcionRefundido{
		// LOW jgarzon Se han hecho sólo pequeñas optimizaciones.
        String s;
        int i;
        int idRelacion;

        try {
        	em.refresh(iDetO);
        	log.debug("Sustitucion normativa completa de determinacion operada: " 
    				+ iDetO.getIden() + "("+iDetO.getApartado() +", " 
    				+ iDetO.getNombre() +", "+ iDetO.getCodigo() + ", " + iDetO.getOrden() +") con operador: " 
    				+ iDetR.getIden()+ "("+iDetR.getApartado() +", " 
    				+ iDetR.getNombre() +", "+ iDetR.getCodigo()+ ", " + iDetR.getOrden()  +") ");
        	
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
  
            for( Object registro : em.createNativeQuery(s).getResultList()){
                idRelacion=Integer.parseInt(registro.toString());

                // Borrar propiedades
                em.createNativeQuery("Delete From planeamiento.Propiedadrelacion Where idrelacion=" + idRelacion).executeUpdate();

                // Borrar vectores
                em.createNativeQuery("Delete From planeamiento.Vectorrelacion Where idrelacion=" + idRelacion).executeUpdate();
            }

            // Relaciones donde iDetO es la determinación regulada
            for (i=0;i<ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADORA.length; i++){
                s="Select r.iden " +
                        "From planeamiento.Relacion r, planeamiento.Vectorrelacion vr " +
                        "Where r.iddefrelacion=" + ClsDatos.ID_DEFRELACION_REGULACION[i] +
                        " And vr.iddefvector=" + ClsDatos.ID_DEFVECTOR_DETERMINACIONREGULADA[i] + " " +
                        "And vr.idrelacion=r.iden And vr.valor=" + iDetO.getIden() + " " +
                        "And r.iden Not In (Select idrelacion " +
                        "From planeamiento.Operacionrelacion Where idtipooperacionrel=" +
                    ClsDatos.ID_TIPOOPERACIONRELACION_ADICION + ") ";
                
                for( Object registro : em.createNativeQuery(s).getResultList()){
                    idRelacion=Integer.parseInt(registro.toString());

                    // Borrar propiedades
                    s="Delete From planeamiento.Propiedadrelacion Where idrelacion=" + idRelacion;
                    em.createNativeQuery(s).executeUpdate();

                    // Borrar vectores
                    s="Delete From planeamiento.Vectorrelacion Where idrelacion=" + idRelacion;
                    em.createNativeQuery(s).executeUpdate();
                }
            }

            // Llamada a la operación de adición normativa
            adicionNormativa(iDetO, iDetR);
            
            // Sustituir nombre y texto
            s="Update planeamiento.Determinacion " +
                    "Set nombre=(Select nombre From planeamiento.Determinacion " +
                    "Where iden=" + iDetR.getIden() +") Where iden=" + iDetO.getIden();
            em.createNativeQuery(s).executeUpdate();
            s="Update planeamiento.Determinacion " +
                    "Set texto=(Select texto From planeamiento.Determinacion " +
                    "Where iden=" + iDetR.getIden() +") Where iden=" + iDetO.getIden();
            em.createNativeQuery(s).executeUpdate();

            // Sustituir documentos
            s="Delete From planeamiento.Documentodeterminacion " +
                    "Where iddeterminacion=" + iDetO.getIden();
            em.createNativeQuery(s).executeUpdate();
            s="Update planeamiento.Documentodeterminacion " +
                    "Set iddeterminacion=" + iDetO.getIden() + " " +
                    "Where iddeterminacion=" + iDetR.getIden();
            em.createNativeQuery(s).executeUpdate();
            
            
            iDetO.setApartado(iDetR.getApartado());
            iDetO.setNombre(iDetR.getNombre());
            em.flush();
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación de sustitución normativa completa. " + e.getMessage(), 23206);
        }

    }
}
