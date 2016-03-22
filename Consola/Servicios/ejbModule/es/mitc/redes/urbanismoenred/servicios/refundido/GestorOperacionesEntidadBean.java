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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Propiedadrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorConsultasLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;

/**
 * Session Bean implementation class GestorOperacionesEntidadBean
 */
@Stateless(name = "GestorOperacionesEntidad")
public class GestorOperacionesEntidadBean implements GestorOperacionesEntidadLocal {
	
	private static final Logger log = Logger.getLogger(GestorOperacionesEntidadBean.class);
	private static final String INSERT_ENTIDADLIN = "Insert Into planeamiento.Entidadlin (identidad, geom, bsuspendida) Select %d, geom, false From planeamiento.Entidadlin Where identidad= %d And bsuspendida=false"; 
	private static final String INSERT_ENTIDADPOL = "Insert Into planeamiento.Entidadpol (identidad, geom, bsuspendida) Select %d, geom, false From planeamiento.Entidadpol Where identidad= %d And bsuspendida=false";
	private static final String INSERT_ENTIDADPNT = "Insert Into planeamiento.Entidadpnt (identidad, geom, bsuspendida) Select %d, geom, false From planeamiento.Entidadpnt Where identidad= %d And bsuspendida=false";
	private static final String UPDATE_ENTIDADPOL = "Update planeamiento.EntidadPol Set bsuspendida=%s Where identidad In (Select iden From planeamiento.Entidad Where iden= %d )";
	private static final String UPDATE_TIPOGEOM = "Update planeamiento.%s Set geom=multi(geometryfromtext('%s', %d )) Where identidad=%d And bsuspendida=false";
	private static final String UPDATE_ENTIDADPNT = "Update planeamiento.EntidadPnt Set bsuspendida=%s Where identidad In (Select iden From planeamiento.Entidad Where iden= %d) ";
	private static final String UPDATE_ENTIDADLIN = "Update planeamiento.EntidadLin Set bsuspendida=%s Where identidad In (Select iden From planeamiento.Entidad Where iden= %d) ";
	
	private String TRUE_FALSE[]=  {"TRUE", "FALSE"};
    private String TIPOS_GEOMETRIA[]= {"Entidadlin", "Entidadpnt", "Entidadpol"};

	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	private static String format = "(%d) [%s] del plan [%s] --> %s -->[%s] del plan [%s]";
	
	@EJB
	private GestorConsultasLocal gestorConsultas;
	@EJB
	private GestorOperacionesDeterminacionLocal gestorOperacionesDeterminacion;
	@EJB
	private EliminadorEntidadesLocal eliminadorEntidades;
	
    /**
     * Default constructor. 
     */
    public GestorOperacionesEntidadBean() {
    }
    
    /**
     * Se tiene en cuenta el carácter de la determinación. Sólo se contemplan
     * las de carácter "Acto de Ejecución" y "Grupo de Actos"
     * Se añaden al recinto operado, las determinaciones del operador que no
     * le sean comunes.
     * Se llama a la función CopiarRecintoDeterminacion() con el argumento False de superposición
     * 
     * @param iEntO
     * @param iEntR
     */
    @SuppressWarnings("unchecked")
	private void acumulacionActos(Entidad iEntO, Entidad iEntR){
    	log.debug("Acumulación actos entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try {
            // Crea la lista de EntidadDeterminacion
            List <Integer> listaIdEntDet = em.createNamedQuery("Entidaddeterminacion.obtenerEntidadesAcumulacion")
            		.setParameter("idEntR", iEntR.getIden())
            		.setParameter("idCaracter1", ClsDatos.ID_CARACTER_ACTODEEJECUCION)
            		.setParameter("idCaracter2", ClsDatos.ID_CARACTER_GRUPODEACTOS)
            		.setParameter("idEntO", iEntO.getIden()).getResultList();

            if (listaIdEntDet.size() > 0) {
                copiarEntidadDeterminacion(iEntO, listaIdEntDet, false, 0);
            }
        } catch (Exception e) {
            log.error("Fallo en la operación acumulacionActos. " + e.getMessage() + ". Código 23114." );
        }
    }
    
    /**
	 * No se tiene en cuenta el carácter de la determinación
	 * Se añaden al recinto operado, las determinaciones del operador que no
	 * le sean comunes.
	 * Se llama a la función CopiarRecintoDeterminacion() con el argumento False 
	 * de superposición.
	 * 
	 * @param iEntO
	 * @param iEntR
	 */
    @SuppressWarnings("unchecked")
	private void acumulacionCompleta(Entidad iEntO, Entidad iEntR){ 
    	log.debug("Acumulacion completa entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try{
            // Crea la lista de EntidadDeterminacion
        	
        	List <Integer> listaIdEntDet = em.createNamedQuery("Entidaddeterminacion.obtenerEntidadesAcumulacionCompleta")
        			.setParameter("idEntidadR", iEntR.getIden())
        			.setParameter("idEntidadO", iEntO.getIden())
        			.getResultList();

            if(listaIdEntDet.size() > 0){
               copiarEntidadDeterminacion(iEntO, listaIdEntDet, false, 0);
            }
        } catch (Exception e) {
            log.error("Fallo en la operación acumulacionCompleta. " + e.getMessage() + ". Código 23102." );
        }

    }

    /**
     * Se tiene en cuenta el carácter de la determinación. Sólo se contemplan
     * las de carácter "Norma General Literal" y "Norma General Gráfica"
     * Se añaden al recinto operado, las determinaciones del operador que no
     * le sean comunes.
     * Se llama a la función CopiarRecintoDeterminacion() con el argumento False de superposición
     * @param iEntO
     * @param iEntR
     */
    @SuppressWarnings("unchecked")
	private void acumulacionNormasGenerales(Entidad iEntO, Entidad iEntR){
    	log.debug("Acumulación normas generales entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try {
            // Crea la lista de EntidadDeterminacion
        	List <Integer> listaIdEntDet = em.createNamedQuery("Entidaddeterminacion.obtenerEntidadesAcumulacion")
            		.setParameter("idEntR", iEntR.getIden())
            		.setParameter("idCaracter1", ClsDatos.ID_CARACTER_NORMAGENERALLITERAL)
            		.setParameter("idCaracter2", ClsDatos.ID_CARACTER_NORMAGENERALGRAFICA)
            		.setParameter("idEntO", iEntO.getIden()).getResultList();

            if (listaIdEntDet.size() > 0) {
                copiarEntidadDeterminacion(iEntO, listaIdEntDet, false, 0);
            }
        } catch (Exception e) {
            log.error("Fallo en la operación acumulacionNormasGenerales. " + e.getMessage() + ". Código 23112." );
        }
    }
	
	/**
     * Se tiene en cuenta el carácter de la determinación. Sólo se contemplan
     * las de carácter "Uso" y "Grupo de Usos"
     * Se añaden al recinto operado, las determinaciones del operador que no
     * le sean comunes.
     * Se llama a la función CopiarRecintoDeterminacion() con el argumento False de superposición
     * @param iEntO
     * @param iEntR
     */
    @SuppressWarnings("unchecked")
	private void acumulacionUsos(Entidad iEntO, Entidad iEntR){
    	log.debug("Acumulación de usos entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try {
            // Crea la lista de EntidadDeterminacion
            List <Integer> listaIdEntDet = em.createNamedQuery("Entidaddeterminacion.obtenerEntidadesAcumulacion")
            		.setParameter("idEntR", iEntR.getIden())
            		.setParameter("idCaracter1", ClsDatos.ID_CARACTER_USO)
            		.setParameter("idCaracter2", ClsDatos.ID_CARACTER_GRUPODEUSOS)
            		.setParameter("idEntO", iEntO.getIden()).getResultList();

            if (listaIdEntDet.size() > 0) {
                copiarEntidadDeterminacion(iEntO, listaIdEntDet, false, 0);
            }
        } catch (Exception e) {
            log.error("Fallo en la operación acumulacionUsos. " + e.getMessage() + ". Código 23113." );
        }
    }

    /**
     * 
     * @param iEntO
     * @param iEntR
     * @throws ExcepcionRefundido 
     */
    private void adicionGrafica(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("Adicion gráfica entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        String s="";
        String tipoGeomO="";
        String tipoGeomR="";
        Geometry geomO = null;
        Geometry geomR = null;
        Geometry geomUnion;
        int srid;
        int nRegistros;

        try{
            // Comprueba si las entidades tienen geometría y, además, son del
            //  mismo tipo.
            tipoGeomO=getGeometria(iEntO, contexto);
            tipoGeomR=getGeometria(iEntR, contexto);

            if (tipoGeomO.equals("") || tipoGeomR.equals("")){
                contexto.log(LOG.AVISO,"No se puede efectuar la operación de Adición Gráfica porque, al menos, una entidad no tiene geometría. Operada: " + iEntO.getCodigo() + ". Operadora: " + iEntR.getCodigo());
                return;
            }
            if (tipoGeomO.equals("Error") || tipoGeomR.equals("Error")){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Adición Gráfica porque, al menos, una entidad tiene más de una geometría. Operada: " + iEntO.getCodigo() + ". Operadora: " + iEntR.getCodigo());
                return;
            }
            if (!tipoGeomO.equals(tipoGeomR)){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Adición Gráfica porque las entidades tiene distinto tipo de geometría. Operada: " + iEntO.getCodigo() + ". Operadora: " + iEntR.getCodigo());
                return;
            }

            // Recupera la geometría de iEntO
            geomO=geometria(tipoGeomO, iEntO.getIden(), false, contexto);
            if (geomO==null){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Adición Gráfica " + 
                    "porque la entidad operada no tiene geometría o está suspendida. Operada: " + iEntO.getCodigo() + ". Operadora: " + iEntR.getCodigo());
                return;
            }
            
            // Recupera la geometría de iEntR
            geomR=geometria(tipoGeomR, iEntR.getIden(), false, contexto);
            if (geomR==null){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Adición Gráfica " + 
                    "porque la entidad operadora no tiene geometría o está suspendida. Operada: " + iEntO.getCodigo() + ". Operadora: " + iEntR.getCodigo());
                return;
            }

            // Ejecuta la unión de las dos geometrías
            srid=srId(tipoGeomO, iEntO.getIden());
            geomUnion=operarGeometrias(geomO, geomR, 1, contexto);
            geomUnion=limpiarGeometria(tipoGeomO, geomUnion, contexto);
            nRegistros=0;
            if((tipoGeomO.equalsIgnoreCase("ENTIDADPOL") && (geomUnion.getGeometryType().equalsIgnoreCase("MULTIPOLYGON") || 
                            geomUnion.getGeometryType().equalsIgnoreCase("POLYGON")))
                            ||
                            (tipoGeomO.equalsIgnoreCase("ENTIDADLIN") && (geomUnion.getGeometryType().equalsIgnoreCase("MULTILINESTRING") || 
                            geomUnion.getGeometryType().equalsIgnoreCase("LINESTRING")))
                            ||
                            (tipoGeomO.equalsIgnoreCase("ENTIDADPNT") && (geomUnion.getGeometryType().equalsIgnoreCase("MULTIPOINT") || 
                            geomUnion.getGeometryType().equalsIgnoreCase("POINT")))
                            ){
            s="Update planeamiento." + tipoGeomO + " Set geom=multi(geometryfromtext('" +
                geomUnion + "', " + srid + ")) " + 
                "Where identidad=" + iEntO.getIden() + " And bsuspendida=false ";
            nRegistros=em.createNativeQuery(s).executeUpdate();
            }
            if (nRegistros!=1){
                log.warn("No se ha conseguido actualizar la geometría " +
                        "de tipo " + geomUnion.getGeometryType() + " en la tabla " + tipoGeomO + " con la instrucción <" +
                        s + ">");
            }

            // Descarga variables
            geomO = null;
            geomR = null;
            geomUnion = null;
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación Adicion Grafica. " + e.getMessage(), 23104);
        }
    }
    
	/**
     * 
     * @param iEntO
     * @param iEntR
	 * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private void adicionNormativa(Entidad iEntO, Entidad iEntR) throws ExcepcionRefundido{
    	log.debug("Adición normativa entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        // Se borran los valores de iEntO que sean comunes a iEntR, y después se
        //  copian todos los valores de iEntR a iEntO

        try{
            // Borrado de los valores comunes.
        	List<Entidaddeterminacion> eds = em.createNamedQuery("Entidaddeterminacion.buscarPorEntidadDet")
        			.setParameter("idEntidadO", iEntO.getIden())
        			.setParameter("idEntidadR", iEntR.getIden()).getResultList();
            for (Entidaddeterminacion ed: eds){  
                eliminarEntidadDeterminacion(ed);
            }

            // Copia de valores de iEntR en iEntO
            
            List <Integer> lista= em.createNamedQuery("Entidaddeterminacion.buscarPorEntidad")
            		.setParameter("idEntidad", iEntR.getIden())
            		.getResultList();
            if (lista.size()>0){
                copiarEntidadDeterminacion(iEntO, lista, false, 0);
            }
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación adicionNormativa. " + e.getMessage(), 23103);
        }
    }

    /**
     * Este método se encarga de unir las diferentes geometrías de una entidad en una o dos geometrías
     * múltiples, dependiendo de si todas tienen el mismo valor del campo "bSuspendida", o bien existen
     * geometrías con valor "false" y otras con valor "true".
     * 
     * @param iEnt
     * @param contexto 
     * @throws ExcepcionRefundido 
     */
    private void agruparGeometrias(Entidad iEnt, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("agruparGeometrias");
        String s="";     
        int iden1;
        int idenN;
        int nRegistros;
        Geometry geom1;
        Geometry geomN;
        int srid;

        try {
            
            if (iEnt!=null){
                // Para cada valor (true o false)...
                for (int i=0;i<=1;i++){
                    // Para cada tipo de geometría...
                    for (int j=0;j<=2;j++){
                        // Selecciona la primera geometría, que es sobre la cual se van a efectuar todas las uniones
                        s="Select iden From planeamiento." + TIPOS_GEOMETRIA[j] + " " +
                                "Where identidad=" + iEnt.getIden() + " " +
                                "And bSuspendida=" + TRUE_FALSE[i] + " Order By iden ";
                        List <?> lista=em.createNativeQuery(s).getResultList();
                        if (lista.size()>0){
                            iden1=Integer.parseInt(String.valueOf(lista.get(0)));
                            geom1=geometria(TIPOS_GEOMETRIA[j], iden1, TRUE_FALSE[i].equalsIgnoreCase("TRUE"), contexto);

                            // Selecciona el resto de geometrías del mismo tipo y con el mismo valor de bSuspendida
                            s="Select iden From planeamiento." + TIPOS_GEOMETRIA[j] + " " +
                                "Where identidad=" + iEnt.getIden() + " " +
                                "And bSuspendida=" + TRUE_FALSE[i] + " " +
                                "And iden<>" + iden1 + " Order By iden ";
                            lista=em.createNativeQuery(s).getResultList();
                            
                            for (Object id: lista){
                                idenN=Integer.parseInt(String.valueOf(id));
                                geomN=geometria(TIPOS_GEOMETRIA[j], idenN, TRUE_FALSE[i].equalsIgnoreCase("TRUE"), contexto);
                                srid=srId(TIPOS_GEOMETRIA[j], idenN);
                                // Efectúa la unión con la geometría iden1
                                geom1=operarGeometrias(geom1, geomN, 1, contexto);
                                geom1=limpiarGeometria(TIPOS_GEOMETRIA[j], geom1, contexto);
                                s="Update planeamiento." + TIPOS_GEOMETRIA[j] + " " + 
                                    "Set geom=multi(geometryfromtext('" +
                                    geom1 + "', " + srid + ")) " + 
                                    "Where identidad=" + iden1 + " " + 
                                    "And bsuspendida=" + TRUE_FALSE[i];
                                nRegistros=em.createNativeQuery(s).executeUpdate();

                                // Si se ha conseguido hacer la unión, se borra el registro que se ha unido
                                if (nRegistros==1){
                                    s = "Delete From planeamiento." + TIPOS_GEOMETRIA[j] + " " +
                                        "Where iden=" + idenN +  " ";
                                    em.createNativeQuery(s).executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            throw new ExcepcionRefundido("Error: fallo en la agrupación de geometrías. " + e.getMessage(), 23129);
        }
    }

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesEntidadLocal#aportacionEntidad(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, java.util.List)
	 */
    @Override
    public void aportacionEntidad(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("Aportación entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try{
            int idTramiteO=iEntO.getTramite().getIden();
            int idTramiteR=iEntR.getTramite().getIden();

            if(idTramiteO!=idTramiteR){
                // Entidad de la que va a colgar
            	Entidad iEntPadre = crearCarpetaEntidadesAportadas(iEntO.getTramite(), iEntR.getTramite(),contexto);
                // Reasignar el IdTramite
                reasignarEntidad(iEntO, iEntPadre, iEntR, 0, contexto);
            }
        } catch (Exception e) {
            throw new ExcepcionRefundido("Fallo en la operación aportacionEntidad. " + e.getMessage(), 23111);
        }
    }
    
    /**
     * Se cambia el idTramite de la entidad iEntR, y se cambia su idPadre para
     * que apunte a la entidad padre de iEntO. El orden que se le asigna es el siguiente al
     * orden que tiene iEntO
     * 
     * @param iEntO
     * @param iEntR
     * @param listaIdTramitesBase
     * @throws ExcepcionRefundido 
     */
    private void aportacionEntidadSobreAnterior(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("Aportación entidad sobre anterior entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try {
            // Entidad de la que va a colgar
        	Entidad iEntPadre = iEntO.getEntidadByIdpadre();
            // Orden que se le va a asignar
            int orden = 1 + iEntO.getOrden();
            // Reasignar el IdTramite
            reasignarEntidad(iEntO, iEntPadre, iEntR, orden, contexto);
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación aportacionEntidadSobreAnterior. " + e.getMessage(), 23203);
        }

    }

    /**
     * Se cambia el idTramite de la entidad iEntR, y se cambia su idPadre para
     * que apunte a la entidad iEntO. Se le asigna el orden 1 para que sea la primera
     * entidad hija.
     * 
     * @param iEntO
     * @param iEntR
     * @param listaIdTramitesBase
     */
    private void aportacionEntidadSobrePadre(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto){
    	log.debug("Aportación entidad sobre padre entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try {
            // Reasignar el IdTramite
            reasignarEntidad(iEntO, iEntO, iEntR, 1,contexto); 
        } catch (Exception e){
            log.error("Fallo en la operación aportacionEntidadSobrePadre. " + e.getMessage() + ". Código 23203." );
        }

    }

    /**
	 * Copiar los elementos contenidos en listaEntDet (y sus dependencias) a la entidad iEntO
	 * Se usan un TreeMap temporal para guardar el mapeo entre los iden originales y los nuevos
	 * que se van creando.
	 * No se copia la determinación 'Grupo de Entidades' ya que se supone que iEntO ya
	 * tiene obligatoriamente una.
	 * 
	 * @param entidadOperada
	 * @param listaIdEntDet
	 * @param superponer
	 * @param superposicion
     * @throws ExcepcionRefundido 
	 */
	@SuppressWarnings("unchecked")
	private void copiarEntidadDeterminacion(Entidad entidadOperada, List <Integer> listaIdEntDet, boolean superponer, int superposicion) throws ExcepcionRefundido{
		log.debug("Copiando entidad determinacion para la entidad " + entidadOperada.getIden());
        TreeMap<Integer, Casoentidaddeterminacion> tm =new TreeMap<Integer, Casoentidaddeterminacion>();
        SortedMap<Integer,Regimenespecifico> regimenesEspecificos=new TreeMap<Integer,Regimenespecifico>();
        
        // Incrementa el valor de superposicion, ya que el valor recibido es el máximo encontrado
        //  para la entidad operada, y los siguientes registros deben ser añadidos con el
        //  contador de superposicion incrementado.
        // Si superponer=false, se pone la superposición que tiene la EntidadDeterminacionRegimen
        if (superponer){
            superposicion++;
        }

        // Inserta registros
        Entidaddeterminacion ed;
        Entidaddeterminacion nuevaed;
        Casoentidaddeterminacion nuevoced;
        
        try {
	        for (int iden : listaIdEntDet) {
	
	            // Averigua si la determinación apuntada por esta EntidadDeterminacion es
	            //  la determinación 'Grupo de Entidades', en cuyo caso no se copian los
	            //  valores.
	        	ed = em.find(Entidaddeterminacion.class, iden);
	            
	            if (ed.getDeterminacion().getIdcaracter() != ClsDatos.ID_CARACTER_GRUPODEENTIDADES){
	                //  Inserta registro en EntidadDeterminacion, salvo que ya exista, en cuyo caso
	                //      toma el registro existente, ya que una pareja de entidad y determinación
	                //      sólo puede figurar una vez en la tabla EntidadDeterminacion
	                List<Entidaddeterminacion> eds = em.createNamedQuery("Entidaddeterminacion.obtenerConMismaEntidad")
	                		.setParameter("iden",iden)
	                		.setParameter("idEntidad", entidadOperada.getIden())
	                		.getResultList();
	                log.debug("EntidadDeterminacion para entidad " + entidadOperada.getCodigo() + " determinacion " + ed.getDeterminacion().getCodigo());
	                if (eds.size()==0){
	                	nuevaed = new Entidaddeterminacion();
	                	nuevaed.setEntidad(entidadOperada);
	                	nuevaed.setDeterminacion(ed.getDeterminacion());
	                	em.persist(nuevaed);
	                	em.flush();
	                	log.debug("ED creada: " + nuevaed.getIden());
	                } else {
	                	nuevaed=eds.get(0);
	                	log.debug("ED recuperada de consulta: " + nuevaed.getIden());
	                }
	
	                // En CasoEntidadDeterminacion
	                if (ed.getCasoentidaddeterminacions().size() == 0) {
	                	log.warn("La entidadDeterminacion no tiene CASOS Entidad (" + ed.getEntidad().getCodigo() + ") Determinacion(" + ed.getDeterminacion()+")");
	                }
	                
	                for (Casoentidaddeterminacion ced : ed.getCasoentidaddeterminacions()){
	                	nuevoced = new Casoentidaddeterminacion();
	                	log.debug("Caso creado para ed: "+ nuevaed.getIden() + " nombre " + ced.getNombre());
	                	nuevoced.setEntidaddeterminacion(nuevaed);
	                	nuevoced.setNombre(ced.getNombre());
	                	em.persist(nuevoced);
	                	em.flush();

	                	log.debug("CED creado: " + nuevoced.getIden() + " asociado a ced " + ced.getIden());
	                	tm.put(ced.getIden(), nuevoced);
	                }
	            }
            }
	        
	     // En VinculoCaso
            List<Vinculocaso> vcs = em.createNamedQuery("Vinculocaso.obtenerTodos").getResultList();
            
            for (Vinculocaso vc : vcs){
            	if (tm.containsKey(vc.getCasoentidaddeterminacionByIdcaso().getIden()) 
                		&& tm.containsKey(vc.getCasoentidaddeterminacionByIdcasovinculado().getIden())){    
            		log.debug("Creando vínculo para los casos: " + vc.getCasoentidaddeterminacionByIdcaso().getIden() + " y " +vc.getCasoentidaddeterminacionByIdcasovinculado().getIden());
                	Vinculocaso nuevovc = new Vinculocaso();
                    nuevovc.setCasoentidaddeterminacionByIdcaso(tm.get(vc.getCasoentidaddeterminacionByIdcaso().getIden()));
                    nuevovc.setCasoentidaddeterminacionByIdcasovinculado(tm.get(vc.getCasoentidaddeterminacionByIdcasovinculado().getIden()));
                    em.persist(nuevovc);
                }
            }

            // En EntidadDeterminacionRegimen
            for (int id : tm.keySet()){
            	List<Entidaddeterminacionregimen> edrs = em.createNamedQuery("Entidaddeterminacionregimen.buscarPorCaso")
                		.setParameter("idCaso", id)
                		.getResultList();

                for (Entidaddeterminacionregimen edr: edrs){
                	Entidaddeterminacionregimen nuevoedr = new Entidaddeterminacionregimen();
                	log.debug("Creando régimen para el caso: " + edr.getCasoentidaddeterminacionByIdcaso().getIden() + (edr.getCasoentidaddeterminacionByIdcasoaplicacion()!= null? " aplicado al caso " +edr.getCasoentidaddeterminacionByIdcasoaplicacion().getIden():""));
                	nuevoedr.setCasoentidaddeterminacionByIdcaso(tm.get(id));
                	nuevoedr.setCasoentidaddeterminacionByIdcasoaplicacion(edr.getCasoentidaddeterminacionByIdcasoaplicacion());
                	nuevoedr.setDeterminacion(edr.getDeterminacion());
                	nuevoedr.setOpciondeterminacion(edr.getOpciondeterminacion());
                	if (!superponer) {
                		if (edr.getSuperposicion() == null) {
                			nuevoedr.setSuperposicion(0);
                		} else {
                			nuevoedr.setSuperposicion(edr.getSuperposicion());
                		}
                	} else {
                		nuevoedr.setSuperposicion(superposicion);
                	}
                	nuevoedr.setSuperposicion(edr.getSuperposicion());
                	nuevoedr.setValor(edr.getValor());
                	em.persist(nuevoedr);
                    em.flush();

                    // Régimen Específico. Debido a la existencia del campo idPadre, se hacen
                    //  los insert por orden de iden e idPadre
                    List<Regimenespecifico> res = em.createNamedQuery("Regimenespecifico.buscarPorEntidad")
                    		.setParameter("idEnt", edr.getIden())
                    		.getResultList();
                    
                    Regimenespecifico nuevore = null;
                    for (Regimenespecifico re : res){

                        nuevore = null;
                        if (re.getRegimenespecifico() != null){
                            if (regimenesEspecificos.containsKey(re.getRegimenespecifico().getIden())){   
                                nuevore = new Regimenespecifico();
                                nuevore.setRegimenespecifico(regimenesEspecificos.get(re.getRegimenespecifico().getIden()));
                            }
                        } else{
                        	nuevore = new Regimenespecifico();
                        }
                        if (nuevore != null){
                        	nuevore.setEntidaddeterminacionregimen(nuevoedr);
                            nuevore.setNombre(re.getNombre());
                            nuevore.setOrden(re.getOrden());
                            nuevore.setTexto(re.getTexto());
                            em.persist(nuevore);
                            
                            regimenesEspecificos.put(re.getIden(), nuevore);            
                        }
                    }
                }
            }

            // DocumentoCaso
            List<Documentocaso> dcs = em.createNamedQuery("Documentocaso.obtenerTodos")
            		.getResultList();
            for (Documentocaso dc : dcs){
            	if (tm.containsKey(dc.getCasoentidaddeterminacion().getIden())){
            		log.debug("Creando documento caso: " + dc.getCasoentidaddeterminacion().getIden());
                	Documentocaso nuevodc = new Documentocaso();
                	nuevodc.setCasoentidaddeterminacion(tm.get(dc.getCasoentidaddeterminacion().getIden()));
                	nuevodc.setDocumento(dc.getDocumento());
                	em.persist(nuevodc);
                }
            }
        }catch (Exception e){
            throw new ExcepcionRefundido("Error insertando registro; " + e.getMessage(), 23038);
        } finally {
        	tm.clear();
            regimenesEspecificos.clear();
        }
    }

	/**
     * Se copian los registros gráficos de iEntR a iEntO. Primero se
     * comprueba que iEntO no tenga previamente ninguna geometría, y
     * que iEntR tenga alguna.
     * 
     * @param iEntO
     * @param iEntR
	 * @throws ExcepcionRefundido 
     */
    private void creacionGrafica(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("Creación gráfica entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try {
            if (!getGeometria(iEntO, contexto).equals("")) { 
                contexto.log(LOG.AVISO,"No se puede efectuar la operación de Creación Gráfica porque la entidad operada ya tiene geometría. Entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
                return;
            }
            
            if (getGeometria(iEntR, contexto).equals("")){ 
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Creación Gráfica porque la entidad operadora no tiene geometría. Entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
                return;
            }
            
            int nRegistros=0;
         
            nRegistros += em.createNativeQuery(String.format(INSERT_ENTIDADPOL, iEntO.getIden(),iEntR.getIden())).executeUpdate();
            nRegistros += em.createNativeQuery(String.format(INSERT_ENTIDADLIN, iEntO.getIden(),iEntR.getIden())).executeUpdate();
            nRegistros += em.createNativeQuery(String.format(INSERT_ENTIDADPNT, iEntO.getIden(),iEntR.getIden())).executeUpdate();
            if (nRegistros!=1){
                log.warn("No se ha conseguido actualizar la geometría.");
            }
        } catch (Exception e) {
            throw new ExcepcionRefundido("Fallo en la operación creacion gráfica. " + e.getMessage(), 23115);
        }

    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesEntidadLocal#crearCarpetaEntidadesAportadas(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, java.util.List)
     */
    @Override
    @SuppressWarnings("unchecked")
	public Entidad crearCarpetaEntidadesAportadas(Tramite tramiteO, Tramite iTramiteR, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("Creando Carpeta Entidades Aportadas...");
        String txtEntidad;
        Entidad iEntidad = null;
        Determinacion iDetGrupoDeEntidades;
        Determinacion iDetCarpeta;
        Opciondeterminacion iOpcion;
            
        txtEntidad = ClsDatos.TEXTO_APORTADAS + "["+iTramiteR.getPlan().getCodigo() +"] " +iTramiteR.getPlan().getNombre().trim();
        
        // Se limita la longitud del nombre de carpeta a 100 caracteres
        if (txtEntidad.length() > 100) {
            txtEntidad = txtEntidad.substring(0, 100);
        }

        // Averigua si la entidad ya existe por haber sido creada con anterioridad
        List<Entidad> lista = em.createNamedQuery("Entidad.buscarPorTramiteYNombre")
        	.setParameter("idTramite", tramiteO.getIden())
        	.setParameter("nombre", txtEntidad)
        	.getResultList();
        
        if (lista.size() == 0) {
            iEntidad = new Entidad();
            iEntidad.setTramite(tramiteO);
            iEntidad.setClave(tramiteO.getPlan().getCodigo());
            iEntidad.setNombre(txtEntidad);
            iEntidad.setCodigo(gestorConsultas.getSiguienteCodigoEntidad(tramiteO.getIden()));
            iEntidad.setBsuspendida(false);
            em.persist(iEntidad);
            
            // Añadir su valor para la determinación 'Grupo de entidades' (es la determinación 'Carpeta', lo que anteriormente era el grupo 'Grupo')
            if (tramiteO.getPlan().getPlanByIdplanbase() != null) {
            	Tramite iTramiteBase=tramiteO.getPlan()
                		.getPlanByIdplanbase()
                		.getTramites()
                		.toArray(new Tramite[0])[0];
                iDetGrupoDeEntidades=gestorConsultas.obtenerDeterminacionGrupoDeEntidadesPorTramite(iTramiteBase);
                iDetCarpeta=gestorOperacionesDeterminacion.getDeterminacionCarpetaTramiteBase(tramiteO,(List<Integer>)contexto.getParametro(ContextoRefundido.ID_TRAMITES_BASE));
                if (iDetGrupoDeEntidades !=null && iDetCarpeta!=null){
                	try {
	                	iOpcion=(Opciondeterminacion) em.createNamedQuery("Opciondeterminacion.buscarPorDetYValorRef")
	                			.setParameter("idDeterminacion", iDetGrupoDeEntidades.getIden())
	                			.setParameter("idValorRef", iDetCarpeta.getIden()).getSingleResult();
	
	                    // Inserta el valor en las tablas de valores
	                    Entidaddeterminacion ed = new Entidaddeterminacion();
	                    ed.setDeterminacion(iDetGrupoDeEntidades);
	                    ed.setEntidad(iEntidad);
	                    em.persist(ed);
	                    
	                    Casoentidaddeterminacion ced = new Casoentidaddeterminacion();
	                    ced.setEntidaddeterminacion(ed);
	                    
	                    em.persist(ced);
	                    
	                    Entidaddeterminacionregimen edr = new Entidaddeterminacionregimen();
	                    edr.setCasoentidaddeterminacionByIdcaso(ced);
	                    edr.setOpciondeterminacion(iOpcion);
	                    edr.setSuperposicion(0);
	                    em.persist(edr);
                	} catch (NoResultException nre) {
                		throw new ExcepcionRefundido("No se ha podido crear carpeta entidades aportadas. No se ha encontrado opciondeterminacion para iddeterminacion " 
                				+ iDetGrupoDeEntidades.getIden()
                				+ " idValorRef " + iDetCarpeta.getIden(),99999);
                	} catch (NonUniqueResultException nure) {
                		throw new ExcepcionRefundido("No se ha podido crear carpeta entidades aportadas. Se han encontrado múltiples opciondeterminacion para iddeterminacion " 
                				+ iDetGrupoDeEntidades.getIden()
                				+ " idValorRef " + iDetCarpeta.getIden(),99999);
                	}
                } else {
                	log.warn("No se ha encontrado ni se ha podido crear la carpeta de 'Entidades aportadas'" );
                    return null;
                }
            } else {
            	log.warn("No se ha encontrado ni se ha podido crear la carpeta de 'Entidades aportadas' porque no hay plan base." );
                return null;
            }
        } else {
            return lista.get(0);
        }
        // Es necesario confirmar los cambios para que la asignación de código
        // tenga efecto y no se duplique en futuras llamadas.
        em.flush();
        return iEntidad;
    }

    /**
     * 
     * @param iEntO
     * @param iEntR
     * @throws ExcepcionRefundido 
     */
    private void destruccionGrafica(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("Destrucción gráfica entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        String s;
        try{
            if (!getGeometria(iEntO, contexto).equals("")){
            	s="Delete From planeamiento.Entidadpol Where identidad=" + iEntO.getIden();
                em.createNativeQuery(s).executeUpdate();
                s="Delete From planeamiento.Entidadlin Where identidad=" + iEntO.getIden();
                em.createNativeQuery(s).executeUpdate();
                s="Delete From planeamiento.Entidadpnt Where identidad=" + iEntO.getIden();
                em.createNativeQuery(s).executeUpdate();
            } else {
            	contexto.log(LOG.AVISO, "No se puede efectuar la operación de Destrucción Gráfica porque la entidad operada no tiene geometría.");
            }
        } catch (Exception e) {
        	throw new ExcepcionRefundido("Fallo en la operación de destrucción gráfica. " + e.getMessage(), 23120);
        }
    }
    
    /**
     * Muestra mensaje de aviso cuando no se puede ejecutar una operación porque
     * alguna de las entidades ha sido eliminada.
     * 
     * @param entidadOperada
     * @param entidadOperadora
     * @param contexto
     */
    private void mostrarMensajeCancelacion(Entidad entidadOperada, Entidad entidadOperadora, ContextoRefundido contexto) {
    	StringBuffer sb= new StringBuffer();
    	sb.append("Operación cancelada por: ");
		if (!em.contains(entidadOperada)) {
			sb.append(" Entidad operada ");
			sb.append(entidadOperada.getCodigo());
			sb.append(" eliminada ");
		}
		if (!em.contains(entidadOperadora)) {
			sb.append(" Entidad operadora ");
			sb.append(entidadOperadora.getCodigo());
			sb.append(" eliminada");
		}
		contexto.log(LOG.AVISO, sb.toString());
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesEntidadLocal#ejecutar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
    @Override
    public void ejecutar(Operacionentidad operacionEntidad, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("ejecutar");
        String sNoEjecutar ="";
        
        try{
            
        	Entidad entidadOperada= operacionEntidad.getEntidadByIdentidad();
        	Entidad entidadOperadora=operacionEntidad.getEntidadByIdentidadoperadora();

        	int idOperacion=operacionEntidad.getOperacion().getIden();
            Tramite tramiteOrdenante=operacionEntidad.getOperacion().getTramite();
            
            // Se elimina la operación antes de ejecutarla, para evitar el "aviso" de que se ha eliminado
            //  una operación que aún no ha sido ejecutada.
            if (em.contains(operacionEntidad.getOperacion())) {
            	Operacion operacion = operacionEntidad.getOperacion();
        		
            	if (!em.contains(operacionEntidad)) {
            		em.refresh(operacion);
                }
            	eliminadorEntidades.eliminar(operacion, null);
                em.flush();
            }

            String codPlanR=entidadOperadora.getTramite().getPlan().getCodigo();
            String codPlanO=entidadOperada.getTramite().getPlan().getCodigo();
            
            // El orden del plan ordenante debe ser mayor o igual que los órdenes de las entidades
            
            int ordenPlanOrdenante=tramiteOrdenante.getPlan().getOrden();
           
            int ordenO=entidadOperada.getTramite().getPlan().getOrden();
            int ordenR=entidadOperadora.getTramite().getPlan().getOrden();
            boolean ejecutarOperacion=true;
            
            if (ordenPlanOrdenante < ordenO || ordenPlanOrdenante < ordenR){
                ejecutarOperacion=false;
                sNoEjecutar="No se ejecuta la operación porque el orden del plan ordenante es menor que el orden del plan ";
                if (ordenPlanOrdenante < ordenO){
                    sNoEjecutar=sNoEjecutar + "operado. ";
                } else {
                    sNoEjecutar=sNoEjecutar + "operador. ";
                }
                sNoEjecutar=sNoEjecutar + "Probablemente, una de las entidades ha sufrido una operación previa que la ha movido a otro plan.";
            }

            if (em.contains(entidadOperadora) && em.contains(entidadOperada)) {
            	if (!entidadOperadora.isBsuspendida()){
                    switch (operacionEntidad.getIdtipooperacionent()) {
                        case 1:
                            // Eliminación
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Eliminación", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		eliminar(entidadOperada, entidadOperadora, tramiteOrdenante, contexto);
                            	} else {
                            		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
                            	}
                            } else {
                            	contexto.log(LOG.AVISO, sNoEjecutar);
                            }
                            break;
                        case 2:
                            // Acumulación completa
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Acumulación completa", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		acumulacionCompleta(entidadOperada, entidadOperadora);
                            	} else {
                            		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
                            	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 3:
                            // Adición Normativa
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Adición Normativa", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		adicionNormativa(entidadOperada, entidadOperadora);
                            	} else {
                            		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
                            	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            
                            break;
                        case 4:
                            // Adición Gráfica
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Adición Gráfica", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion) {
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		adicionGrafica(entidadOperada, entidadOperadora, contexto);
    	                        } else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            
                            break;
                        case 5:
                            // Sustracción Gráfica
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Sustracción Gráfica", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		sustraccionGrafica(entidadOperada, entidadOperadora, tramiteOrdenante, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}	
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 6:
                            // Sustitución Normativa Completa
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Sustitución Normativa Completa", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		sustitucionNormativa(entidadOperada, entidadOperadora, true, tramiteOrdenante);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 7:
                            // Sustitución Gráfica
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Sustitución Gráfica", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		sustitucionGrafica(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 8:
                            // Suspensión Total
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Suspensión Total", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		suspensionTotal(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}	
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 9:
                            // Sustitución Normativa Parcial
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Sustitución Normativa Parcial", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		sustitucionNormativa(entidadOperada, entidadOperadora, false, tramiteOrdenante);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 10:
                            // Superposición Completa
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Superposición Completa", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		superposicionCompleta(entidadOperada, entidadOperadora);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 11:
                            // Aportación de Entidad
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Aportación de Entidad", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		aportacionEntidad(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 12:
                            // Acumulación de Normas Generales
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Acumulación de Normas Generales", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		acumulacionNormasGenerales(entidadOperada, entidadOperadora);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;

                        case 13:
                            // Acumulación de Usos
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Acumulación de Usos", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		acumulacionUsos(entidadOperada, entidadOperadora);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 14:
                            // Acumulación de Actos
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Acumulación de Actos", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		acumulacionActos(entidadOperada, entidadOperadora);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 15:
                            // Creación Gráfica
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Creación Gráfica", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		creacionGrafica(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 17:
                            // Superposición de Normas Generales
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Superposición de Normas Generales", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		superposicionNormasGenerales(entidadOperada, entidadOperadora);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 18:
                            // Superposición de Usos
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Superposición de Usos", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		superposicionUsos(entidadOperada, entidadOperadora);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 19:
                            // Superposición de Actos
                       		contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Superposición de Actos", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		superposicionActos(entidadOperada, entidadOperadora);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 20:
                            // Destrucción Gráfica
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Destrucción Gráfica", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		destruccionGrafica(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 21:
                            // Herencia de Clave
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Herencia de Clave", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		herenciaClave(entidadOperada, entidadOperadora, tramiteOrdenante);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 22:
                            // Suspensión Parcial
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Suspensión Parcial", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		suspensionParcial(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 25:
                            // Incorporación de Entidad
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Incorporación de Entidad", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		incorporacionEntidad(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            } 
                            break;
                        case 26:
                            // Levantamiento Total de Suspensión
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Levantamiento Total de Suspensión", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		levantamientoTotalSuspension(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 27:
                            // Aportación de Entidad sobre Entidad Padre
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Aportación de Entidad sobre Entidad Padre", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		aportacionEntidadSobrePadre(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 28:
                            // Aportación de Entidad sobre Entidad Anterior
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Aportación de Entidad sobre Entidad Anterior", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		aportacionEntidadSobreAnterior(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }                       
                            break;
                        case 29:
                            // Levantamiento Parcial de Suspensión
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Levantamiento Parcial de Suspensión", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		levantamientoParcialSuspension(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        case 30:
                            // Incorporación de Trámite por Entidad
                        	contexto.log(LOG.INFO, String.format(format, idOperacion,entidadOperadora.getCodigo(),codPlanR,"Incorporación de Trámite por Entidad", entidadOperada.getCodigo(),codPlanO));
                            if (ejecutarOperacion){
                            	if (em.contains(entidadOperada) && em.contains(entidadOperadora)) {
                            		incorporacionEntidad(entidadOperada, entidadOperadora, contexto);
                            	} else {
    	                    		mostrarMensajeCancelacion(entidadOperada, entidadOperadora, contexto);
    	                    	}
                            } else {
                            	contexto.log(LOG.AVISO,sNoEjecutar);
                            }
                            break;
                        default:
                        	contexto.log(LOG.AVISO, idOperacion + " No se opera. La operación iden=" + operacionEntidad.getIdtipooperacionent() + " no está implementada en esta versión.");
                        	break;
                    }
                }else {
                	contexto.log(LOG.AVISO, idOperacion + " No se opera. La entidad operadora [" + entidadOperadora.getCodigo() + "] está suspendida.");
                }
            } else {
            	contexto.log(LOG.AVISO, idOperacion + " No se opera. " +
            			(!em.contains(entidadOperadora) ?  " La entidad operadora [" + entidadOperadora.getCodigo() + "] ha sido eliminada. ":"") +
            			(!em.contains(entidadOperada) ?  " La entidad operada [" + entidadOperada.getCodigo() + "] ha sido eliminada. ":"")
            		);
            }
            
        } catch (Exception e){
            log.error("Error en OperacionEntidad: " + e.getMessage() + ". Código 23130.");
            throw new ExcepcionRefundido("Error en OperacionEntidad: " + e.getMessage(), 23130);
        }
    }

	/**
     * Se efectúan las siguientes acciones relacionadas con la entidad iEntO que
     * se va a eliminar:
     * 1 - Reasigna los idPadre de los hijos, al idPadre de la entidad.
     * 2 - Se elimina la entidad.
     * 
     * @param iEntO
     * @param iEntR
     * @param tramiteOrdenante
     * @throws ExcepcionRefundido 
     */
	private void eliminar(Entidad iEntO, Entidad iEntR, Tramite tramiteOrdenante, ContextoRefundido contexto) throws ExcepcionRefundido{
		log.debug("Eliminar entidad operada " + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try {
            // 1.
            String s = "Update planeamiento.Entidad Set idpadre=" +
                   (iEntO.getEntidadByIdpadre() != null ? iEntO.getEntidadByIdpadre().getIden(): "null") 
                   + " " + "Where idpadre=" + iEntO.getIden();
                    
            em.createNativeQuery(s).executeUpdate();

            // 2. 
//            gestorConsultas.eliminarObjeto("Entidad", iEntO.getIden(), tramiteOrdenante);
            if (em.contains(iEntO)) {
            	contexto.log(LOG.INFO, "Eliminando entidad [" + iEntO.getCodigo() + "] " + iEntO.getNombre());
                eliminadorEntidades.eliminar(iEntO, null);
                em.flush();
            }
        } catch (Exception e) {
            throw new ExcepcionRefundido("Fallo en la operación eliminar. " + e.getMessage(), 23101);
        }
    }

	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesEntidadLocal#eliminarCarpetasVacias(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
	@SuppressWarnings("unchecked")
	@Override
	public void eliminarCarpetasVacias(Tramite tramite, ContextoRefundido contexto) throws ExcepcionRefundido {
		log.debug("eliminarCarpetasVacias");
        try{
        	em.flush();
            if(ClsDatos.B_ELIMINAR_CARPETAS_VACIAS==true){
                contexto.log(LOG.INFO, "Eliminando carpetas vacías...");
                // 1 - Determinaciones
                List<Determinacion> determinaciones = em.createNamedQuery("Determinacion.obtenerParaEliminacion")
                		.setParameter("idTramite", tramite.getIden())
                		.setParameter("idCaracterEnunciado", ClsDatos.ID_CARACTER_ENUNCIADO)
                		.setParameter("idCaracterEnunciadoCompleto", ClsDatos.ID_CARACTER_ENUNCIADOCOMPLEMENTARIO)
                		.getResultList();
                for (Determinacion determinacion: determinaciones){
                	if (em.contains(determinacion)) {
                		contexto.log(LOG.INFO, "Eliminando determinación [" + determinacion.getCodigo() + "] " + determinacion.getNombre());
	
                		eliminadorEntidades.eliminar(determinacion, null);
	                    em.flush();
                	}
                }
                
                // 2 - Entidades
                Determinacion detCarpeta=gestorOperacionesDeterminacion.getDeterminacionCarpetaTramiteBase(tramite,
                		(List<Integer>) contexto.getParametro(ContextoRefundido.ID_TRAMITES_BASE));
                
                if (detCarpeta!=null){
                    em.flush();
                    List<Entidad> listaEnt=getEntidadesPorGrupoTramite(detCarpeta, tramite);
                    
                    for (Entidad entidad: listaEnt){
                        try {
                        	em.createNamedQuery("Entidad.obtenerParaEliminacion").setParameter("idEntidad", entidad.getIden()).getSingleResult();
//                        	gestorConsultas.eliminarObjeto("entidad", entidad.getIden(), tramite);
                    		contexto.log(LOG.INFO, "Eliminando entidad [" + entidad.getCodigo() + "] " + entidad.getNombre());
                        	if (em.contains(entidad)) {
                        		eliminadorEntidades.eliminar(entidad, null);      		
                            	em.flush();
                        	}
                        } catch(NoResultException nre) {
                        	log.debug("No es para eliminacion ("+entidad.getIden()+")");
                        }
                    }
                }
            } else {
                log.warn("*** Se ha optado por no eliminar las carpetas vacías...");
            }
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo al eliminar carpetas vacías: "  + e.getMessage(), 23046);
        }
	}

	/**
	 * 
	 * @param ed
	 * @throws ExcepcionRefundido 
	 */
    private void eliminarEntidadDeterminacion(Entidaddeterminacion ed) throws ExcepcionRefundido{
    	log.debug("eliminarEntidadDeterminacion");
        try {
            // Averigua si la determinación apuntada por esta EntidadDeterminacion es
            //  la determinación 'Grupo de Entidades', en cuyo caso no se borra.
            
            if(ed.getDeterminacion().getIdcaracter() != ClsDatos.ID_CARACTER_GRUPODEENTIDADES){
//              RegimenEspecifico
            	em.createNamedQuery("Regimenespecifico.borrarPorCaso")
            		.setParameter("iden", ed.getIden())
            		.executeUpdate();
            	em.createNamedQuery("Regimenespecifico.borrarPorCasoAplicacion")
	        		.setParameter("iden", ed.getIden())
	        		.executeUpdate();
                //      EntidadDeterminacionRegimen
            	em.createNamedQuery("Entidaddeterminacionregimen.borrarPorCaso")
	        		.setParameter("iden", ed.getIden())
	        		.executeUpdate();
            	em.createNamedQuery("Entidaddeterminacionregimen.borrarPorCasoAplicacion")
	        		.setParameter("iden", ed.getIden())
	        		.executeUpdate();
                //      VinculoCaso
            	em.createNamedQuery("Vinculocaso.borrarPorCaso")
	        		.setParameter("iden", ed.getIden())
	        		.executeUpdate();
            	em.createNamedQuery("Vinculocaso.borrarPorCasoVinculado")
	        		.setParameter("iden", ed.getIden())
	        		.executeUpdate();
                //      DocumentoCaso
            	em.createNamedQuery("Documentocaso.borraroPorCaso")
	        		.setParameter("iden", ed.getIden())
	        		.executeUpdate();
                //      CasoEntidadDeterminacion
            	em.createNamedQuery("Casoentidaddeterminacion.borrarPorEntidadDet")
	        		.setParameter("iden", ed.getIden())
	        		.executeUpdate();
                //      EntidadDeterminacion
            	log.debug("Borrando entidaddeterminacion " + ed.getIden());
            	if (em.contains(ed)) {
            		eliminadorEntidades.eliminar(ed, null);
            	} 
            	em.flush();
            }
        } catch (Exception e) {
            throw new ExcepcionRefundido("Fallo en eliminarEntidadDeterminacion. " + e.getMessage(), 23010);
        }

    }

    /**
     * 
     * @param iEnt1
     * @param iEnt2
     * @return
     */
	@SuppressWarnings("unchecked")
	private boolean existeSuperposicionPoligonal(Entidad iEnt1, Entidad iEnt2){
        // Se devuelve TRUE si las geometrías se superponen.
		log.debug("existeSuperposicionPoligonal");
        String s;
        try{

            // Si Ent1 es POL y Ent2 es POL
            List<Object> lista=em.createNamedQuery("Entidad.existeSuperposicionPolPol")
            		.setParameter("idEnt1", iEnt1.getIden())
            		.setParameter("idEnt2", iEnt2.getIden()).getResultList();
            if (lista.size()==0){
                return false;
            }
            s=lista.get(0).toString().toUpperCase().trim();
            if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("TRUE")){
                return true;
            }

            // Si Ent1 es POL y Ent2 es LIN
            lista=em.createNamedQuery("Entidad.existeSuperposicionPolLin")
            		.setParameter("idEnt1", iEnt1.getIden())
            		.setParameter("idEnt2", iEnt2.getIden()).getResultList();
            if (lista.size()==0){
                return false;
            }
            s=lista.get(0).toString().toUpperCase().trim();
            if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("TRUE")){
                return true;
            }

            // Si Ent1 es POL y Ent2 es PNT
            lista=em.createNamedQuery("Entidad.existeSuperposicionPolPnt")
            		.setParameter("idEnt1", iEnt1.getIden())
            		.setParameter("idEnt2", iEnt2.getIden()).getResultList();
            if (lista.size()==0){
                return false;
            }
            s=lista.get(0).toString().toUpperCase().trim();
            if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("TRUE")){
                return true;
            }

            // Si Ent1 es LIN y Ent2 es POL
            lista=em.createNamedQuery("Entidad.existeSuperposicionLinPol")
            		.setParameter("idEnt1", iEnt1.getIden())
            		.setParameter("idEnt2", iEnt2.getIden()).getResultList();
            if (lista.size()==0){
                return false;
            }
            s=lista.get(0).toString().toUpperCase().trim();
            if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("TRUE")){
                return true;
            }

            // Si Ent1 es PNT y Ent2 es POL
            lista=em.createNamedQuery("Entidad.existeSuperposicionPntPol")
            		.setParameter("idEnt1", iEnt1.getIden())
            		.setParameter("idEnt2", iEnt2.getIden()).getResultList();
            if (lista.size()==0){
                return false;
            }
            s=lista.get(0).toString().toUpperCase().trim();
            if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("TRUE")){
                return true;
            }
        } catch (Exception e) {
            log.error("Error al evaluar la superposición de geometrías. " + e.getMessage() + ". Código 23132.");
        }
        return false;
    }

    /**
     * 
     * @param tabla
     * @param idEntidad
     * @param suspendida
     * @param contexto 
     * @return
     * @throws ExcepcionRefundido 
     */
    private Geometry geometria(String tabla, int idEntidad, boolean suspendida, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("geometria");
        agruparGeometrias(em.find(Entidad.class, idEntidad), contexto);
        
        String s="Select astext(geom) From planeamiento." + tabla + " " + 
                "Where identidad=" + idEntidad + " And bsuspendida=" + suspendida;
        List <?> lista = em.createNativeQuery(s).getResultList();
        if (lista.size()>0){            
        	String geomWKT=lista.get(0).toString();
            try {
            	WKTReader reader = new WKTReader();
            	return reader.read(geomWKT);
            } catch (ParseException e) {
                throw new ExcepcionRefundido("Error al leer la geometría de la entidad iden= " + idEntidad, 23127);
            }
        }    
        return null;        
    }

    /**
     * 
     * @param iDetGrupo
     * @param iTramite
     * @return
     * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private List <Entidad> getEntidadesPorGrupoTramite(Determinacion iDetGrupo, Tramite iTramite) throws ExcepcionRefundido{
    	
        List<Entidad> resultado =new ArrayList<Entidad>();

        // Se devuelve una lista de iden de entidades cuyo grupo de entidad es la
        //  determinación iDetGrupo o cualquier otra que tenga la misma determinación
        //  base que ella.

        try{
            // 1 Entidades que tienen como alguno de sus valores de referencia a iDetGrupo
            resultado = em.createNamedQuery("Entidad.obtenerPorValorRefTramite")
            		.setParameter("idValorRef", iDetGrupo.getIden())
            		.setParameter("idTramite", iTramite.getIden())
            		.getResultList();

            // 2 Entidades que tienen como alguno de sus valores de referencia a alguna
            //      determinación cuyo determinación base es la misma que la de iDetGrupo
            resultado.addAll(em.createNamedQuery("Entidad.obtenerPorValorRefPadreTramite")
            		.setParameter("idValorRef", iDetGrupo.getIden())
            		.setParameter("idBase",iDetGrupo.getDeterminacionByIddeterminacionbase().getIden())
            		.setParameter("idTramite", iTramite.getIden())
            		.getResultList());
        } catch (Exception e) {
            throw new ExcepcionRefundido("Fallo en entidadesPorGrupoTramite. " + e.getMessage(), 23020);
        }
        return resultado;
    }
    
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesEntidadLocal#getGeometria(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad)
     */
    @Override
    public String getGeometria(Entidad iEnt, ContextoRefundido contexto){
    	log.debug("getGeometria");
        String s;
        String tipoGeom="";
        List<?> lista;

        try{
            // Primero, se agrupan las geometrías del elemento, para dejar un
            //  máximo de dos.
            agruparGeometrias(iEnt, contexto);
            
            s="Select Count(geom) From planeamiento.Entidadpol Where identidad=" + iEnt.getIden();
            lista = em.createNativeQuery(s).getResultList();
            int nPol=Integer.parseInt(lista.get(0).toString());
            
            s="Select Count(geom) From planeamiento.Entidadpnt Where identidad=" + iEnt.getIden();
            lista = em.createNativeQuery(s).getResultList();
            int nPnt=Integer.parseInt(lista.get(0).toString());
            
            s="Select Count(geom) From planeamiento.Entidadlin Where identidad=" + iEnt.getIden();
            lista = em.createNativeQuery(s).getResultList();
            int nLin=Integer.parseInt(lista.get(0).toString());

            tipoGeom="";
            if (nPol+nPnt+nLin!=0) {
                // Si sólo tiene geometría de un tipo, es correcto.
                if (nPol<=2 && (nPnt+nLin)==0){
                    tipoGeom="Entidadpol";
                }
                if (nPnt<=2 && (nPol+nLin)==0){
                    tipoGeom="Entidadpnt";
                }
                if (nLin<=2 && (nPnt+nPol)==0){
                    tipoGeom="Entidadlin";
                }
                if (tipoGeom.equals("")){
                    // Error. Tiene más de un tipo de geometría.
                    tipoGeom="Error";
                    log.error("Error: la entidad iden=" + iEnt.getIden() + " tiene más de un tipo de geometría. Código 23129.");    
                }                
            }
        } catch(Exception e){
            log.warn("Fallo en tieneGeometria. " + e.getMessage() + ". Código 23028." );
            tipoGeom="Error";
        }
        return tipoGeom;
    }

    /**
     * 
     * @param iden
     * @return
     * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private List <Relacion> getRelacionesEntidad(int iden) throws ExcepcionRefundido{
    	log.debug("getRelacionesEntidad");
        List<Relacion> listaR;
        List<Relacion> lista=new ArrayList<Relacion>();
        int idDR;
        int idDV;
        try{
            List<Object[]> listaDR=em.createNamedQuery("Defrelacion.obtenerDefinicionesTabla").getResultList();
            
            for (Object[] obj : listaDR){
                idDR=Integer.parseInt(obj[0].toString());
                idDV=Integer.parseInt(obj[1].toString());
           
                listaR=em.createNamedQuery("Relacion.obtenerPorVector")
                		.setParameter("idDV", idDV).setParameter("idDR", idDR)
                		.setParameter("iden", iden).getResultList();
                lista.addAll(listaR);
            }
        } catch (Exception e) {
            throw new ExcepcionRefundido("Fallo en relacionesPorElemento. " + e.getMessage(), 23012);
        }
        return lista;
    }
    
    /**
     * 
     * @param iEntO
     * @param iEntR
     * @param tramiteOrdenante
     * @throws ExcepcionRefundido
     */
    private void herenciaClave(Entidad iEntO, Entidad iEntR, Tramite tramiteOrdenante) throws ExcepcionRefundido{
    	log.debug("Herencia clave entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try{
            // Primero comprueba si la entidad operadora ya tiene aplicada la relación
            //  de 'Herencia de clave'. Si la tiene, actualiza su valor, y si no la tiene,
            //  la crea nueva.
            // La defrelación es la ID_DEFRELACION_HERENCIACLAVE
            List<Relacion> relaciones = getRelacionesEntidad(iEntO.getIden());
            
            boolean tieneHerencia=false;
            for(Relacion relacion :relaciones){
            	// La entidad tiene una relación de Herencia de clave
                if (relacion.getIddefrelacion() == ClsDatos.ID_DEFRELACION_HERENCIACLAVE) {
                	tieneHerencia=true;
                    em.createNativeQuery("Update planeamiento.PropiedadRelacion Set valor='" + iEntR.getClave() + "' " +
                            "Where idRelacion=" + relacion.getIden()).executeUpdate();
                }
            }

            if (!tieneHerencia){
                // Inserta la nueva relación
                Relacion relacion = new Relacion();
                relacion.setIddefrelacion(ClsDatos.ID_DEFRELACION_HERENCIACLAVE);
                relacion.setTramiteByIdtramitecreador(tramiteOrdenante);
                em.persist(relacion);
                
                // Inserta el vector que apunta a la entidad operada
                Vectorrelacion vr = new Vectorrelacion();
                vr.setIddefvector(ClsDatos.ID_DEFVECTOR_HERENCIACLAVE);
                vr.setRelacion(relacion);
                vr.setValor(iEntO.getIden());
                em.persist(vr);
                
                // Inserta la propiedad que contiene el valor=clave de la entidad operadora
                Propiedadrelacion pr = new Propiedadrelacion();
                pr.setIddefpropiedad(ClsDatos.ID_DEFPROPIEDAD_HERENCIACLAVE);
                pr.setRelacion(relacion);
                pr.setValor(iEntR.getClave());
                em.persist(pr);
            }
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación herenciaClave. " + e.getMessage(), 23121);
        }

    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesEntidadLocal#incorporacionEntidad(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
    @SuppressWarnings("unchecked")
    @Override
	public boolean incorporacionEntidad(Entidad entidadOperada, Entidad iEntR, ContextoRefundido contexto) {
    	log.debug("Incorporación entidad operada" + entidadOperada.getIden() + " codigo " + entidadOperada.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        String s;
        String tipoGeomO="";
        String tipoGeomR="";
        String tipoGeom="";
        boolean bSuspendidaO;
        Geometry geomO = null;
        Geometry geomR = null;
        Geometry geomInter;
        Geometry geom1;
        Geometry geom2;
        Geometry geomDif;
        WKTReader reader;
        String geomWKT;
        int srid;
        int nRegistros;
        Entidad iEntPadre;
        boolean incorporadoOK=false;
        boolean bRecortada;
        boolean bRecortadaAlMenosUna;
        boolean bRecortar;
        String txtEntidad;
        String codPlan;
        
        // Las geometrías suspendidas también deben incorporarse. 
        // Entidad incorporada    : iEntO
        // Entidad incorporadora  : iEntR
        try{
        	
        	contexto.log(LOG.INFO, "Incorporación de la entidad [" + entidadOperada.getCodigo() + "] " + 
                    "del plan [" + entidadOperada.getTramite().getPlan().getCodigo() + 
                    "] por la entidad [" + iEntR.getCodigo() + "] del " + 
                    "plan [" + iEntR.getTramite().getPlan().getCodigo() +"]");
        	
        	Determinacion carpetaDeterminacionesAportadas = gestorOperacionesDeterminacion.crearCarpetaDeterminacionesAportadas(iEntR.getTramite(), entidadOperada.getTramite());
            
            // Se incorporan todas las determinaciones del trámite operado porque
            // se ha detectado que es necesario cuando se incorporan entidades de un trámite
            
            List<Determinacion> determinacionesRaiz = em.createNamedQuery("Determinacion.buscarRaiz")
            		.setParameter("idTramite", entidadOperada.getTramite().getIden())
            		.getResultList();
            
            for (Determinacion raiz : determinacionesRaiz) {
            	gestorOperacionesDeterminacion.reasignarDeterminacion(carpetaDeterminacionesAportadas, carpetaDeterminacionesAportadas, raiz, 0, contexto);
            }

            // Agrupa las geometrías, para garantizar la correcta ejecución de
            //  la operación.
            agruparGeometrias(entidadOperada, contexto);
            agruparGeometrias(iEntR, contexto);

            // Comprueba si las entidades tienen geometría y, además, son del
            //  tipo adecuado: la incorporadora siempre debe ser poligonal, y la 
            //  incorporada puede ser de cualquier tipo.
            tipoGeomO=getGeometria(entidadOperada, contexto);
            tipoGeomR=getGeometria(iEntR, contexto);

            if (tipoGeomO.equals("") || tipoGeomR.equals("")){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Incorporación porque, al menos, una entidad no tiene geometría.");
                return false;
            }
            if (tipoGeomO.equals("Error") || tipoGeomR.equals("Error")){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Incorporación porque, al menos, una entidad tiene más de un tipo de geometría.");
            	return false;
            }
            if (!tipoGeomR.equalsIgnoreCase("Entidadpol")){
                contexto.log(LOG.AVISO,"No se puede efectuar la operación de Incorporación porque la entidad incorporadora no es poligonal.");
                return false;
            }

            // Recupera la geometría de iEntR que no está suspendida.
            geomR=geometria(tipoGeomR, iEntR.getIden(), false, contexto);
            if (geomR==null){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Incorporación porque la entidad incorporadora tiene su geometría suspendida.");
            	return false;
            }

            // Recupera las geometrías de iEntO.
            reader = new WKTReader();
            s="Select astext(geom), bsuspendida From planeamiento." + tipoGeomO + " " + 
                "Where identidad=" + entidadOperada.getIden();
            List<Object[]> listaO = em.createNativeQuery(s).getResultList();

            incorporadoOK=false;
            for (Object[] obj : listaO){
                bSuspendidaO=Boolean.parseBoolean(obj[1].toString());
                geomWKT=obj[0].toString();
                try {
                    geomO = reader.read(geomWKT);
                } catch (ParseException e) {
                    log.error("Error al leer la geometría de la entidad iden=" + entidadOperada.getIden() + ". " + e.getMessage() + ". Código 23127.");
                    continue;
                }

                // Ejecuta la intersección de las dos geometrías
                srid=srId(tipoGeomR, iEntR.getIden());
                geomInter=operarGeometrias(geomO, geomR, 2, contexto);
                geomInter=limpiarGeometria(tipoGeomO, geomInter, contexto);

                if (geomInter==null && bSuspendidaO==false){
                    // No se ejecuta la incorporación porque las entidades no intersectan
                    continue;
                }
                if(geomInter.isEmpty() && bSuspendidaO==false){
                    // No se ejecuta la incorporación porque las entidades no intersectan
                    continue;
                }

                nRegistros=0;
                if((tipoGeomO.equalsIgnoreCase("ENTIDADPOL") && (geomInter.getGeometryType().equalsIgnoreCase("MULTIPOLYGON") || 
                            geomInter.getGeometryType().equalsIgnoreCase("POLYGON")))
                            ||
                            (tipoGeomO.equalsIgnoreCase("ENTIDADLIN") && (geomInter.getGeometryType().equalsIgnoreCase("MULTILINESTRING") || 
                            geomInter.getGeometryType().equalsIgnoreCase("LINESTRING")))
                            ||
                            (tipoGeomO.equalsIgnoreCase("ENTIDADPNT") && (geomInter.getGeometryType().equalsIgnoreCase("MULTIPOINT") || 
                            geomInter.getGeometryType().equalsIgnoreCase("POINT")))
                            ){
                    s="Update planeamiento." + tipoGeomO + " Set geom=multi(geometryfromtext('" +
                        geomInter + "', " + srid + ")) Where identidad=" + entidadOperada.getIden() + " " +
                        "And bsuspendida=" + bSuspendidaO;
                    
                    if (em.createNativeQuery(s).executeUpdate()>=1){
                        incorporadoOK=true;
                    }
                }
            }

            if (incorporadoOK==true){
                // Reasigna el trámite a la entidad incorporada iEntO, poniéndole el
                //  de la entidad incorporadora iEntR.
                iEntPadre=crearCarpetaEntidadesAportadas(iEntR.getTramite(), entidadOperada.getTramite(), contexto);
                reasignarEntidad(iEntR, iEntPadre, entidadOperada, 0, contexto);

                // Recorta las entidades del trámite incorporador cuyo grupo (valor
                //  para la determinación 'Grupo de entidades' sea el grupo de la
                //  entidad incorporada.
                // Se excluyen: la entidad incorporadora y las contenidas en 
                //  la lista de entidades para incorporar.
                //  También se excluyen aquellas entidades que hayan sido 
                //  previamente incorporadas al plan incorporador, y siempre que la 
                //  carpeta que las contenga pertenezca a un plan de orden 
                //  anterior, de modo que sólo queden excluídas las entidades
                //  aportadas por la operación de incorporación, y no las que
                //  provengan de desarrollos o aportaciones. La carpeta 'padre'
                //  debe ser una de las 'Aportadas por...'
                geom1=geometria(tipoGeomO, entidadOperada.getIden(), false, contexto);
                Determinacion iDetGrupo=(Determinacion) em.createNamedQuery("Determinacion.obtenerGrupoEntidad")
                		.setParameter("idEntidad", entidadOperada.getIden())
                		.setParameter("idCaracter",ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
                		.getSingleResult();
                
                List<Entidad> lista=getEntidadesPorGrupoTramite(iDetGrupo, iEntR.getTramite());
                
                contexto.log(LOG.INFO, "Recortando entidades del Grupo: " + iDetGrupo.getNombre());
                bRecortadaAlMenosUna=false;
                for (Entidad iEnt: lista){
                    tipoGeom=getGeometria(iEnt, contexto);            

                    if (!tipoGeom.equalsIgnoreCase("") && !tipoGeom.equalsIgnoreCase("ERROR")){
                        // Sólo se recortan las entidades diferentes de las incorporadas y de
                        //  la incorporadora.
                        if (iEnt.getIden()!=iEntR.getIden()){
                            // Comprueba si la entidad está alojada en una 
                            //  carpeta 'Aportadas por...'. Se busca la carpeta de
                            //  más alto nivel en el árbol, ya que la entidad puede
                            //  haber sido aportada indirectamente, por ser hija de
                            //  una que haya sido directamente aportada.
                            bRecortar=true;
                            iEntPadre=iEnt.getEntidadByIdpadre();
                            
                            while(iEntPadre!= null) {
                                txtEntidad = ClsDatos.TEXTO_APORTADAS; // Sólo es el comienzo del nombre
                                if (iEntPadre.getNombre().startsWith(txtEntidad)){
                                    // Es una entidad carpeta con el nombre 'Aportadas por...'
                                    codPlan=iEntPadre.getNombre().substring(1+txtEntidad.length(), 6+txtEntidad.length());
                                    // Con este código de plan hay que averiguar si su orden es 
                                    //  anterior al plan incorporador, en cuyo caso, la
                                    //  entidad idEntidad no debe ser recortada.                                
                                    s="Select Count(*) From planeamiento.plan P " + 
                                        "Where P.codigo='" + codPlan + "' " +
                                        "And P.orden<=" + iEntPadre.getTramite().getPlan().getOrden();
                                    if (Integer.parseInt(em.createNativeQuery(s).getSingleResult().toString())>0){
                                        // La entidad NO debe ser recortada
                                        contexto.log(LOG.INFO, "No se recorta la entidad [" + iEnt.getCodigo() + "] " + 
                                                "porque ya fue aportada por el plan [" + codPlan + "]");
                                        bRecortar=false;
                                        break;
                                    }
                                }
                                iEntPadre=iEntPadre.getEntidadByIdpadre();
                            }
                            
                            if (!bRecortar){
                                continue;
                            }

                            // Averigua si las entidades se solapan. Si no es así, no se operan.
                            bRecortada=false;
                            //  - Parte suspendida
                            geom2=geometria(tipoGeom, iEnt.getIden(), true, contexto);
                            if (geom2!=null){
                                geomInter=operarGeometrias(geom1, geom2, 2, contexto);
                                if (geomInter!=null){
                                    if (!geomInter.isEmpty()){
                                        bRecortada=true;
                                    }
                                }
                            }
                            //  - Parte no suspendida
                            geom2=geometria(tipoGeom, iEnt.getIden(), false, contexto);
                            if (geom2!=null){
                                geomInter=operarGeometrias(geom1, geom2, 2, contexto);
                                if (geomInter!=null){
                                    if (!geomInter.isEmpty()){
                                        bRecortada=true;
                                    }
                                }
                            }
                            
                            // Si las entidades no se solapan, no se continúa con el recorte.
                            if (bRecortada){
                            	contexto.log(LOG.INFO, "Recortando [" + iEnt.getCodigo() + "] del plan [" +
                                        iEnt.getTramite().getPlan().getCodigo() + "]");

                                // Averigua el srid
                                srid=srId(tipoGeomR, iEntR.getIden());
                                
                                // Controla si la entidad se ha recortado
                                bRecortada=false;

                                // Parte suspendida
                                geom2=geometria(tipoGeom, iEnt.getIden(), true, contexto);
                                if (geom2!=null){
                                    geomDif=operarGeometrias(geom2, geom1, 3, contexto);
                                    geomDif=limpiarGeometria(tipoGeom, geomDif, contexto);
                                    if (geomDif!=null){
                                        s="Update planeamiento." +tipoGeom + " Set geom=multi(geometryfromtext('" +
                                        geomDif + "', " + srid + ")) Where identidad=" + iEnt.getIden() + " " +
                                        "And bsuspendida=true";
                                        nRegistros=em.createNativeQuery(s).executeUpdate();
                                        if (nRegistros>0){
                                            bRecortada=true;
                                        }
                                    }
                                }
                                // Parte no suspendida
                                geom2=geometria(tipoGeom, iEnt.getIden(), false, contexto);
                                if (geom2!=null){
                                    geomDif=operarGeometrias(geom2, geom1, 3, contexto);
                                    geomDif=limpiarGeometria(tipoGeom, geomDif, contexto);
                                    if (geomDif!=null){
                                        s="Update planeamiento." + tipoGeom + " Set geom=multi(geometryfromtext('" +
                                        geomDif + "', " + srid + ")) Where identidad=" + iEnt.getIden() + " " +
                                        "And bsuspendida=false";
                                        nRegistros=em.createNativeQuery(s).executeUpdate();
                                        if (nRegistros>0){
                                            bRecortada=true;
                                        }
                                    }
                                }
                                
                                // Si no se ha conseguido recortar la entidad, se envía un mensaje.
                                if (!bRecortada){
                                    contexto.log(LOG.INFO, "No se ha conseguido recortar la entidad.");
                                } else {
                                    bRecortadaAlMenosUna=true;
                                }

                                // Si a la entidad no le queda geometria, hay que eliminarla.
                                if (getGeometria(iEnt, contexto).equalsIgnoreCase("")){
                                    contexto.log(LOG.INFO, "Se elimina la entidad [" + iEnt.getCodigo() + "] " +
                                            "del plan [" + iEntR.getTramite().getPlan().getCodigo() + "]");
                                    if (em.contains(iEnt)) {
                                    	eliminadorEntidades.eliminar(iEnt, null);
                                        em.flush();
                                    }
                                }
                            }
                        }
                    } else {
                        // La entidad del plan incorporador no tiene geometría.
                    	log.warn("La entidad "+ iEnt.getIden()+" del plan incorporado no tiene geometría");
                    }
                }

                // Mensajes de incorporación.
                if (!bRecortadaAlMenosUna){
                    contexto.log(LOG.INFO, "No hay entidades para recortar.");
                }
              
                contexto.log(LOG.INFO, "Incorporada en el plan [" + iEntR.getTramite().getPlan().getCodigo() + "] " +
                        "con código de entidad [" + entidadOperada.getCodigo() + "]");
            
            } else {
                contexto.log(LOG.INFO,"No intersecta con la entidad [" + iEntR.getCodigo() + "] " + 
                        "del plan [" + iEntR.getTramite().getPlan().getCodigo() + "] ");
            }
            return true;
        } catch (Exception e){
            log.error("Fallo en la operación de incorporacion de Entidad. " + e.getMessage() + ". Código 23125." );
            
            return false;
        }
        
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesEntidadLocal#levantamientoParcialSuspension(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad)
     */
    @SuppressWarnings("unchecked")
	@Override
    public void levantamientoParcialSuspension(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("Levantamiento parcial suspension entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
    	
    	List<Entidad> suspendidas = em.createNamedQuery("Entidad.obtenerSuspensionesParciales").setParameter("idEntidad", iEntO.getIden()).getResultList();
        for (Entidad suspendida : suspendidas) {
        	suspendida.setBsuspendida(false);
        }
    }

    /**
     * Pone la marca bSuspendida=false en todas las geometrías de iEntO, y en la propia Entidad.
     * @param iEntO
     * @param iEntR
     * @param contexto 
     * @throws ExcepcionRefundido 
     */
    private void levantamientoTotalSuspension(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("Levantamiento total suspensión entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        int nRegistros=0;

        try {
        	nRegistros += em.createNativeQuery(String.format(UPDATE_ENTIDADPOL,"false", iEntO.getIden())).executeUpdate();
            
            nRegistros += em.createNativeQuery(String.format(UPDATE_ENTIDADPNT,"false", iEntO.getIden())).executeUpdate();
            
            nRegistros += em.createNativeQuery(String.format(UPDATE_ENTIDADLIN,"false", iEntO.getIden())).executeUpdate();
            iEntO.setBsuspendida(false);

            log.warn("Se ha levantado la suspensión a " + nRegistros + " geometrías.");

            // Por último, se llama al procedimiento que agrupa geometrías por el campo "bSuspendida"
            agruparGeometrias(iEntO, contexto);
        } catch (Exception e) {
            throw new ExcepcionRefundido("Error: fallo en la operación levantamientoTotalSuspension. " + e.getMessage(), 23131);
        }

    }

    /**
     * 
     * @param tipoGeom
     * @param aGeom
     * @param contexto 
     * @return
     * @throws ExcepcionRefundido 
     */
    private Geometry limpiarGeometria(String tipoGeom, Geometry aGeom, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("limpiarGeometria");
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
                    iGeomR=operarGeometrias(iGeomR, iGeom, 1, contexto);
                }
            }
            return iGeomR;
            
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo al limpiar la geometría de tipo " + sTipo + ". " + e.getMessage(), 23129);
        }
    }
    
    /**
     * 
     * @param idEntidad
     * @return
     */
    private int maximaSuperposicionDeEntidad(int idEntidad){
    	log.debug("maximaSuperposicionDeEntidad");
        int maxSup=0;

        try{
            // Primero se asegura que no haya valores nulos
            em.createNativeQuery("Update planeamiento.Entidaddeterminacionregimen Set superposicion=0 Where superposicion Is Null").executeUpdate();

            maxSup=Integer.parseInt(em.createNamedQuery("Entidaddeterminacionregimen.obtenerMaximaSuperposicion").getSingleResult().toString());
        } catch (Exception e){
            log.error("Fallo en maximaSuperposicionDeEntidad. " + e.getMessage() + ". Código 23023." );
            return 0;
        }
        return maxSup;
    }
    /**
     * 
     * @param geomO
     * @param geomR
     * @param tipoOperacion
     * @return
     */
    private Geometry operarGeometrias(Geometry geomO, Geometry geomR, int tipoOperacion, ContextoRefundido contexto){
    	log.debug("operarGeometrias");
        boolean fallo=true;
        double tol=0.000001;
        Geometry geomResult=null;
        int nIntentos=0;
        
        // 1=Unión
        // 2=Intersección
        // 3=Diferencia
        
        while (fallo) {
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
                log.warn(nIntentos + " - La operación de geometrías genera un error. Se reintenta con tolerancia " + tol  + ". ");
                geomO=geomO.buffer(tol);
                geomR=geomR.buffer(tol);
                tol=tol*2;
                geomResult=null;
                if (nIntentos>=10){
                    contexto.log(LOG.AVISO, "No se ha conseguido efectuar la operación en " + nIntentos + " intentos. El resultado es una geometría nula.");
                    fallo=false;
                }
            }
        }
        if (nIntentos>0){
            nIntentos++;
            log.warn(nIntentos + " - Se ha conseguido efectuar la operación de geometrías. ");
        }
        return geomResult;    
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesEntidadLocal#reasignarEntidad(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, int, java.util.List)
     */
    @SuppressWarnings("unchecked")
	@Override
    public boolean reasignarEntidad(Entidad iEntO, Entidad iEntPadre, Entidad iEntR, int orden, ContextoRefundido contexto){
    	log.debug("Reasignar entidad operada " + iEntO.getCodigo() + " " + iEntO.getNombre() + " Entidad operadora "+ iEntR.getCodigo() + " " + iEntR.getNombre());
        String s;
        int nRegistros;
        Determinacion iDetP;
        Determinacion iDetGrupoDeEntidadesR;
        Determinacion iDetGrupoDeEntidadesO;
        Determinacion iDetVRequivalente;
        int ordenPlanDet;
        
        try {
            int idTramiteO=iEntO.getTramite().getIden();
            int idTramiteR=iEntR.getTramite().getIden();
            List<Integer> listaIdTramitesBase = (List<Integer>)contexto.getParametro(ContextoRefundido.ID_TRAMITES_BASE);

            // Si los dos trámites son el mismo, se aborta el procedimiento.
            // Si alguno de los dos es un trámite base, tambián se aborta.
            if (idTramiteO != idTramiteR && 
            		!listaIdTramitesBase.contains(idTramiteO) && 
            		!listaIdTramitesBase.contains(idTramiteR)){
                int ordenPlanR=iEntR.getTramite().getPlan().getOrden();

                // Averigua cuál es la determinación 'Grupo de Entidades' de
                //  ambos trámites, para ignorar la del operador a la hora de
                //  reasignar el trámite. A la entidad iEntR hay que asignarle la
                //  determinación 'Grupo de Entidades' del trámite operado. 
                // Hay dos criterios para buscar la determinación grupo:
                // 1. En principio, podría ser una determinación del plan base
                // 2. Si no es así, se supone que es un grupo creado por el plan, y no
                //  debe tener equivalencia con el plan base (ya que si la tuviera, se debería
                //  haber usado esa).
                // Por lo tanto, basta con buscar la determinación de caracter valor de referencia
                //  que tiene, como valor, la entidad en cuestión para la determinación
                //  de caracter 20 (Grupo de Entidades), independientemente de si es de un
                //  plan u otro.
                log.debug("Obteniendo grupo equivalente para " + iEntR.getIden());
                iDetVRequivalente=gestorConsultas.obtenerDeterminacionGrupoEquivalentePorEntidad(iEntR, iEntO.getTramite(),listaIdTramitesBase);
                log.debug("Obteniendo grupo de entidades para tramite " + iEntR.getTramite().getCodigofip());
                iDetGrupoDeEntidadesR=gestorConsultas.obtenerDeterminacionGrupoDeEntidadesPorTramite(iEntR.getTramite());
                
                if (iDetVRequivalente != null) {
                	log.debug("Obteniendo grupo de entidades para tramite VR " + iDetVRequivalente.getTramite().getCodigofip());
                } else {
                	log.debug("Grupo de entidades para tramite VR es nulo" );
                }
                iDetGrupoDeEntidadesO=gestorConsultas.obtenerDeterminacionGrupoDeEntidadesPorTramite(iDetVRequivalente.getTramite());
                
				List<Opciondeterminacion> opciones = em.createNamedQuery("Opciondeterminacion.buscarPorDetYValorRef")
                		.setParameter("idDeterminacion", iDetGrupoDeEntidadesO.getIden())
                		.setParameter("idValorRef", iDetVRequivalente.getIden()).getResultList();
                Opciondeterminacion opcion = null;
                if (opciones.size() >= 1) {
                	opcion = opciones.get(0);
                }
        
                // Orden de la entidad
                if (orden==0){
                    // Hay que poner el 1+orden de los que tienen el mismo padre
                    // Se calcula el máximo orden de las que tienen el mismo padre
                    if (iEntPadre==null){
                        s="Select Max(orden) From planeamiento.Entidad Where idpadre Is Null " +
                                "And idTramite=" + iEntO.getTramite().getIden();
                    } else {
                        s="Select Max(orden) From planeamiento.Entidad Where idpadre=" + iEntPadre.getIden();
                    }
					List<Integer> lista =em.createNativeQuery(s).getResultList();
                    if (lista.get(0)==null){
                        orden=1;
                    } else {
                        orden=1+lista.get(0);
                    }
                } else {
                    // Se le suma 1 a todos los órdenes mayores o iguales al orden que se le va a asignar, y luego
                    //  se le pone su orden.
                    s="Update planeamiento.Entidad Set orden=orden+1 Where orden>=" + orden + " And idpadre";
                    if (iEntPadre==null){
                        s = s + " Is Null And idtramite=" + iEntO.getTramite().getIden();
                    } else {
                        s = s +"=" + iEntPadre.getIden();
                    }
                    em.createNativeQuery(s).executeUpdate();
                }

				List<Documento> documentos = em.createNamedQuery("Documentoentidad.obtenerPorEntidad")
                		.setParameter("idEntidad", iEntR.getIden())
                		.getResultList();
                // Reasigna sus documentos, si los tiene
                for (Documento documento : documentos) {
                    documento.setTramite(iEntO.getTramite());
                }

                // Elimina el registro de PlanEntidadOrdenacion si los planes origen y
                //  destino son diferentes
                if (iEntO.getTramite().getPlan().getIden()!=iEntR.getTramite().getPlan().getIden()){
                    s="Delete From planeamiento.Planentidadordenacion " +
                            "Where identidadordenacion=" + iEntR.getIden();
                    nRegistros = em.createNativeQuery(s).executeUpdate();
                    if (nRegistros>0){
                        contexto.log(LOG.AVISO, "La entidad [" + iEntR.getCodigo() + "] ha dejado de ser entidad de ordenación del plan [" + iEntR.getTramite().getPlan().getCodigo() +"].");
                    }
                }
                
                // Reasigna sus valores
                Determinacion detAportadas=gestorOperacionesDeterminacion.crearCarpetaDeterminacionesAportadas(iEntO.getTramite(), iEntR.getTramite());

                // Determinaciones aplicadas (excepto la determinación 'Grupo de Entidades')
                //  Sólo se tienen en cuenta las determinaciones cuyo orden de plan sea igual
                //  o inferior al de la entidad iEntR, de modo que no se reasignen aquellas que
                //  han sido reasignadas en virtud de una incorporación.
				List<Entidaddeterminacion> eds = em.createNamedQuery("Entidaddeterminacion.obtenerAplicacion")
                		.setParameter("idEntidad", iEntR.getIden())
                		.setParameter("idDeterminacion", iDetGrupoDeEntidadesR.getIden())
                		.getResultList();
				log.debug("Procesando " + eds.size() + " determinaciones aplicadas para entidad operadora " + iEntR.getIden() + " y grupo de entidades " + iDetGrupoDeEntidadesR.getIden());
                for (Entidaddeterminacion ed : eds){
                    ordenPlanDet=ed.getDeterminacion().getTramite().getPlan().getOrden();
                    if (ordenPlanDet<=ordenPlanR){
                    	if (ed.getDeterminacion().getTramite().getIden() == iEntR.getTramite().getIden()) {
                    		iDetP=ed.getDeterminacion().getDeterminacionByIdpadre();
                            if (iDetP!=null){
                                if (iDetP.getTramite().getIden()==detAportadas.getTramite().getIden()){
                                	log.debug("Las determinaciones tienen el mismo tramite " + iDetP.getTramite().getIden() + " " + detAportadas.getTramite().getIden());
                                	gestorOperacionesDeterminacion.reasignarDeterminacion(detAportadas, iDetP, ed.getDeterminacion(), 0, contexto);
                                } else {
                                	log.debug("Las determinaciones NO tienen el mismo tramite " + iDetP.getTramite().getIden() + " " + detAportadas.getTramite().getIden());
                                	gestorOperacionesDeterminacion.reasignarDeterminacion(detAportadas, detAportadas, ed.getDeterminacion(), 0,contexto);
                                }
                            } else {
                            	log.debug("La determinacion NO tiene padre");
                            	gestorOperacionesDeterminacion.reasignarDeterminacion(detAportadas, detAportadas, ed.getDeterminacion(), 0,contexto);
                            }
                    	} else {
                    		log.debug("La determinación aplicada " + ed.getDeterminacion().getIden() + " no pertenece al mismo plan que la entidad " + iEntR.getIden());
                    	}  
                    } else {
                    	log.warn("Orden del plan de la determinacion es mayor que el orden del plan operador " + ordenPlanDet + " " + ordenPlanR);
                    }
                }

                // Determinaciones de régimen. Sólo las de orden de plan igual o inferior
                //  al orden de plan de iEntR
				List<Determinacion> regimenes = em.createNamedQuery("Determinacion.obtenerRegimenPorEntidad")
                		.setParameter("idEntidad", iEntR.getIden())
                		.getResultList();
             
				log.debug("Procesando " + regimenes.size() + " regimenes.");
                for (Determinacion regimen: regimenes){
                    if (regimen.getTramite().getPlan().getOrden()<=ordenPlanR){
                        gestorOperacionesDeterminacion.reasignarDeterminacion(detAportadas, detAportadas, regimen, 0,contexto);
                    }
                }

                //      Valores de referencia (excepto los de la determinación 'Grupo de Entidades')
				List<Determinacion> valoresReferencia = em.createNamedQuery("Determinacion.obtenerValoresReferenciaPorEntidadYGrupoEntidad")
                		.setParameter("idEntidad", iEntR.getIden())
                		.setParameter("idGrupoEntidad", iDetGrupoDeEntidadesR.getIden())
                		.getResultList(); 
                
				log.debug("Procesando " + valoresReferencia.size() + " valores de referencia.");
                for (Determinacion vr: valoresReferencia){
                    if (vr.getTramite().getPlan().getOrden()<=ordenPlanR){
                    	gestorOperacionesDeterminacion.reasignarDeterminacion(detAportadas, detAportadas, vr, 0,contexto);
                    }
                }

                // El puntero a la determinación 'Grupo de Entidades' del iTramiteR se cambia
                //  por un puntero a la del trámite iTramiteO
                s="Update planeamiento.Entidaddeterminacion " +
                        "Set iddeterminacion=" + iDetGrupoDeEntidadesO.getIden() + " "+
                        "Where identidad=" + iEntR.getIden() + " " +
                        "And iddeterminacion=" + iDetGrupoDeEntidadesR.getIden() + " ";
                em.createNativeQuery(s).executeUpdate();

                if(opcion==null){
                	opcion = new Opciondeterminacion();
                	opcion.setDeterminacionByIddeterminacion(iDetGrupoDeEntidadesO);
                	opcion.setDeterminacionByIddeterminacionvalorref(iDetVRequivalente);
                    // La opción no existe y hay que crearla
                    em.persist(opcion);
                    em.flush();
                }
                
                s="Update planeamiento.Entidaddeterminacionregimen " +
                        "Set idopciondeterminacion=" + opcion.getIden() + " " +
                        "Where iden In (Select edr.iden From planeamiento.Entidaddeterminacionregimen edr, " +
                        "planeamiento.Entidaddeterminacion ed, planeamiento.Casoentidaddeterminacion ced " +
                        "Where (edr.idcaso=ced.iden Or edr.idcasoaplicacion=ced.iden) " +
                        "And ced.identidaddeterminacion=ed.iden And ed.identidad=" + iEntR.getIden() + " " +
                        "And ed.iddeterminacion=" + iDetGrupoDeEntidadesO.getIden() + ") ";
                em.createNativeQuery(s).executeUpdate();

                // Reasigna los datos de la entidad iEntR para encajarla en el trámite iTramiteO
                iEntR.setCodigo(gestorConsultas.getSiguienteCodigoEntidad(iEntO.getTramite().getIden()));

                iEntR.setOrden(orden);
                // Se cambia el padre de la entidad
                iEntR.setEntidadByIdpadre(iEntPadre);
                // Por último, cambia el trámite y las entidades hijas
                iEntR.setTramite(iEntO.getTramite());
                
                // Aplicamos el cambio, porque de lo contrario puede sucerder que a las
                // entidades hijas se les asigne el mismo código.
                em.flush();
                
                // En ocasiones la referencia a la entidad padre no se refresca 
                // correctamente ¿?
                // Se debería evitar mezclar sql y JPA...
                if (iEntPadre != null) {
                	em.refresh(iEntPadre);
                }
                // Sus hijas, si las tiene
                for (Entidad hija : iEntR.getEntidadsForIdpadre()) {
                    reasignarEntidad(iEntO, iEntR, hija, 0,contexto);
                }
            }
        } catch (Exception e) {
            log.error("Fallo en reasignarEntidad. " + e.getMessage() + ". Código 23026." );
            return false;
        }

        // Nos aseguramos de aplicar todos los cambios realizados con consultas
        // sql y que de este modo se reflejen en las siguientes ooperaciones.
        em.flush();
        return true;
    }

    /**
     * 
     * @param tabla
     * @param idEntidad
     * @return
     */
    private int srId(String tabla, int idEntidad){
        int srid=-1;
        
        try{
            List <?> lista = em.createNativeQuery("Select Distinct srid(geom) From planeamiento." + tabla + " Where identidad=" + idEntidad).getResultList();
            if (lista!=null){
                if (lista.size()==1){
                    srid=Integer.parseInt(lista.get(0).toString());                    
                }
            }
        } catch (Exception e){
            log.error("Error: fallo al evaluar el SRID de la entidad iden=" + idEntidad + ". " + e );
        }
        return srid;
    }
    
    /**
     * Se tiene en cuenta el carácter de la determinación. Sólo se contamplan
     * las de carácter "Acto de Ejecución" y "Grupo de Actos"
     * Se añaden al recinto operado, las determinaciones del operador que no
     * le sean comunes.
     * Se llama a la función CopiarRecintoDeterminacion() con el argumento TRUE de superposición, y
     * con el máximo valor encontrado de superposición
     * 
     * @param iEntO
     * @param iEntR
     * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private void superposicionActos(Entidad iEntO, Entidad iEntR) throws ExcepcionRefundido{
    	log.debug("Superposición de actos entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());

        try{
            // Crea la lista de EntidadDeterminacion    
            List <Integer> listaIdEntDet = em.createNamedQuery("Entidaddeterminacion.obtenerEntidadesAcumulacion")
            		.setParameter("idEntR", iEntR.getIden())
            		.setParameter("idCaracter1", ClsDatos.ID_CARACTER_ACTODEEJECUCION)
            		.setParameter("idCaracter2", ClsDatos.ID_CARACTER_GRUPODEACTOS)
            		.setParameter("idEntO", iEntO.getIden()).getResultList();
            
            int superposicion=maximaSuperposicionDeEntidad(iEntO.getIden());

            if(listaIdEntDet.size()>0){
                copiarEntidadDeterminacion(iEntO, listaIdEntDet, true, superposicion);
            }
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación superposicionActos. " + e.getMessage(), 23119);
        }
    }

    /**
     * No se tiene en cuenta el carácter de la determinación
     * Se añaden al recinto operado, las determinaciones del operador que no
     * le sean comunes.
     * Se llama a la función CopiarRecintoDeterminacion() con el argumento TRUE de superposición, y
     * con el máximo valor encontrado de superposición
     * 
     * @param iEntO
     * @param iEntR
     */
    private void superposicionCompleta(Entidad iEntO, Entidad iEntR){
    	log.debug("Superposición completa entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try{
            // Crea la lista de EntidadDeterminacion
        	@SuppressWarnings("unchecked")
			List <Integer> listaIdEntDet=em.createNamedQuery("Entidaddeterminacion.obtenerParaSuperposicion")
            		.setParameter("idEntidad", iEntR.getIden())
            		.setParameter("idEntR", iEntR.getIden())
            		.setParameter("idEntO", iEntO.getIden()).getResultList();
        	
            if(listaIdEntDet.size()>0){
                copiarEntidadDeterminacion(iEntO, listaIdEntDet, true, maximaSuperposicionDeEntidad(iEntO.getIden()));
            }
        } catch (Exception e){
            log.error("Fallo en la operación opEnt_SuperposicionCompleta. " + e + ". Código 23110." );
        }
    }
    
    /**
     * Se tiene en cuenta el carácter de la determinación. Sólo se contemplan
     * las de carácter "Norma General Literal" y "Norma General Gráfica"
     * Se añaden al recinto operado, las determinaciones del operador que no
     * le sean comunes.
     * Se llama a la función CopiarRecintoDeterminacion() con el argumento TRUE de superposición, y
     * con el máximo valor encontrado de superposición
     * 
     * @param iEntO
     * @param iEntR
     */
    @SuppressWarnings("unchecked")
	private void superposicionNormasGenerales(Entidad iEntO, Entidad iEntR){
    	log.debug("Superposición normals generales entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        int superposicion;
        try{
            // Crea la lista de EntidadDeterminacion
            List <Integer> listaIdEntDet = em.createNamedQuery("Entidaddeterminacion.obtenerEntidadesAcumulacion")
            		.setParameter("idEntR", iEntR.getIden())
            		.setParameter("idCaracter1", ClsDatos.ID_CARACTER_NORMAGENERALLITERAL)
            		.setParameter("idCaracter2", ClsDatos.ID_CARACTER_NORMAGENERALGRAFICA)
            		.setParameter("idEntO", iEntO.getIden()).getResultList();
            
            superposicion=maximaSuperposicionDeEntidad(iEntO.getIden());

            if(listaIdEntDet.size()>0){
                copiarEntidadDeterminacion(iEntO, listaIdEntDet, true, superposicion);
            }
        } catch (Exception e){
            log.error("Fallo en la operación superposicionNormasGenerales. " + e.getMessage() + ". Código 23117." );
        }
    }
    
    /**
     * Se tiene en cuenta el carácter de la determinación. Sólo se contamplan
     * las de carácter "Uso" y "Grupo de Usos"
     * Se añaden al recinto operado, las determinaciones del operador que no
     * le sean comunes.
     * Se llama a la función CopiarRecintoDeterminacion() con el argumento TRUE de superposición, y
     * con el máximo valor encontrado de superposición
     * 
     * @param iEntO
     * @param iEntR
     * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private void superposicionUsos(Entidad iEntO, Entidad iEntR) throws ExcepcionRefundido{
    	log.debug("Superposición de usos entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try{
            List <Integer> listaIdEntDet = em.createNamedQuery("Entidaddeterminacion.obtenerEntidadesAcumulacion")
            		.setParameter("idEntR", iEntR.getIden())
            		.setParameter("idCaracter1", ClsDatos.ID_CARACTER_USO)
            		.setParameter("idCaracter2", ClsDatos.ID_CARACTER_GRUPODEUSOS)
            		.setParameter("idEntO", iEntO.getIden()).getResultList();

            int superposicion=maximaSuperposicionDeEntidad(iEntO.getIden());

            if(listaIdEntDet.size()>0){
                copiarEntidadDeterminacion(iEntO, listaIdEntDet, true, superposicion);
            }
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación superposicionUsos. " + e.getMessage(), 23118);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesEntidadLocal#suspensionParcial(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad)
     */
    @Override
    public void suspensionParcial(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("Suspensión parcial entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        suspensionYlevantamientoParcial(iEntO, iEntR, true, contexto);
    }
    
    /**
     * Pone la marca bSuspendida=true en todas las geometrías de iEntO, y en la propia entidad.
     * 
     * @param iEntO
     * @param iEntR
     * @param contexto 
     * @throws ExcepcionRefundido 
     */
    private void suspensionTotal(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido{ 
    	log.debug("Suspensión total entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try {
        	int nRegistros=0;
            nRegistros += em.createNativeQuery(String.format(UPDATE_ENTIDADPOL,"true", iEntO.getIden())).executeUpdate();
            
            nRegistros += em.createNativeQuery(String.format(UPDATE_ENTIDADPNT,"true", iEntO.getIden())).executeUpdate();
            
            nRegistros += em.createNativeQuery(String.format(UPDATE_ENTIDADLIN,"true", iEntO.getIden())).executeUpdate();
            if (nRegistros>0){
                log.warn("        Se han suspendido " + nRegistros + " geometrías en la entidad [" + iEntO.getCodigo() + "]");
            }
            iEntO.setBsuspendida(true);

            // Por último, se llama al procedimiento que agrupa geometrías por el campo "bSuspendida"
            agruparGeometrias(iEntO, contexto);
        } catch (Exception e) {
            throw new ExcepcionRefundido("Fallo en la operación suspensionTotal. " + e.getMessage(), 23108);
        }

    }

    /**
     * Si el parámetro "suspender" es "true" se trata de una suspensión parcial, y si es "false", se trata de
     * un levantamiento parcial de suspensión.
     * Consiste en dividir su geometría en dos: la entidad original
     * quedará con la geometría resultante de la sustracción gráfica con la operadora, y
     * a la entidad nueva se le asigna la intersección de la geometría original con la
     * de la operadora. Además, a esta nueva entidad se le pone la marca de 'Suspendida=<suspender>'
     * Si la iEntR no tiene geometría, se considera que es una suspensión o levantamiento total.
     * Si iEntO no tiene geometría, no se hace nada, ya que este tipo de operación sólo tiene
     * carácter gráfico.
     * 
     * @param iEntO
     * @param iEntR
     * @param suspender
     * @param contexto 
     * @throws ExcepcionRefundido 
     */
    private void suspensionYlevantamientoParcial(Entidad iEntO, Entidad iEntR, boolean suspender, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("suspensionYlevantamientoParcial");
        // Sólo se contempla si la geometría operadora es poligonal
        String s="";
        int idEntOperadora;
        int idEntOperada;
        String tipoGeomR;
        String tipoGeomO;
        
        try{
            idEntOperada=iEntO.getIden();
            idEntOperadora=iEntR.getIden();

            // Comprueba si la entidad operada tiene alguna geometría. Si no es así, no se hace nada.
            if (!getGeometria(iEntO, contexto).isEmpty()){
            	// Comprueba si la entidad operadora tiene alguna geometría. Si no es así, se considera que la
                //  suspensión o el levantamiento es total.
                tipoGeomR=getGeometria(iEntR, contexto);
                if (tipoGeomR.isEmpty()){
                    if (suspender==true){
                        suspensionTotal(iEntO, iEntR, contexto);
                    } else {
                        levantamientoTotalSuspension(iEntO, iEntR, contexto);
                    }
                } else {
                	// Si la entidad operadora no es poligonal, no se puede efectuar la operación
                    if (!tipoGeomR.equalsIgnoreCase("EntidadPol")){
                        contexto.log(LOG.AVISO, "No puede efectuarse la operación de suspensión parcial debido a que la entidad operadora no es poligonal.");
                        return;
                    }

                    // Comprueba si las geometrías intersectan. Si no es así, no se hace nada.
                    if (!existeSuperposicionPoligonal(iEntO, iEntR)){
                        return;
                    }

                    // Averigua el tipo de geometría de la entidad operada. Si no tiene, se 
                    //  considera que la suspensión o el levantamiento es total.
                    tipoGeomO=getGeometria(iEntO, contexto);
                    if (tipoGeomO.equalsIgnoreCase("")){
                        if (suspender==true){
                            suspensionTotal(iEntO, iEntR, contexto);
                        } else {
                            levantamientoTotalSuspension(iEntO, iEntR, contexto);
                        }
                    } else {
                    	// Calcula la geometría. La nueva geometría es igual a la intersección de
                        //  la operada con la operadora (con bSuspendida=<suspender>), y la geometría de la
                        //  operada queda como la sustracción con la operadora (sin cambiar bSuspendida)
                    	Geometry geomO=geometria( tipoGeomO, idEntOperada, !suspender, contexto);
                    	Geometry geomR=geometria( tipoGeomR, idEntOperadora, false, contexto);
                    	Geometry geomIntersect=operarGeometrias(geomO, geomR, 2, contexto);
                        geomIntersect=limpiarGeometria(tipoGeomO, geomIntersect, contexto);
                        if (geomIntersect !=null && !geomIntersect.isEmpty()){
                        	// Modifica la geometría de la entidad operada, haciendo la sustracción con la operadora
                            sustraccionGrafica(iEntO, iEntR, null, contexto);
                            
                            // Insert la nueva geometría, a la cual se le pone el valor de bSuspendida
                            Entidad copia = copiarEntidad(iEntO,
                            		crearCarpetaEntidadesAportadas(iEntO.getTramite(), iEntR.getTramite(), contexto));
                            s = "Insert Into planeamiento." + tipoGeomO + " (identidad, geom, bsuspendida) " +
                                    "Values (" + copia.getIden() + ", multi(geometryfromtext('" + 
                                    geomIntersect.toText() + "', "+ (geomIntersect.getSRID() != 0 ? geomIntersect.getSRID():-1) + ")), false)";
                            if (em.createNativeQuery(s).executeUpdate()==0){
                                log.warn("Aviso: no se ha conseguido insertar la geometría de la " + 
                                        "entidad iden=" + idEntOperada);
                            } 
                            
                            em.flush();
                        }
                    }
                }
            }    
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación suspensionParcial. " + e.getMessage(), 23122);
        }
    }
    
    /**
     * Realiza una copia de una entidad que ha sufrido una suspensión parcial.
     * La copia se deja en la carpeta de entidades aportadas.
     * @param padre 
     * 
     * @param original
     * @return
     */
    private Entidad copiarEntidad(Entidad original, Entidad padre) {
		Entidad copia = new Entidad();
		copia.setBsuspendida(true);
		copia.setClave(original.getClave());
		copia.setCodigo(gestorConsultas.getSiguienteCodigoEntidad(original.getTramite().getIden()));
		copia.setEtiqueta(original.getEtiqueta());
		copia.setNombre(original.getNombre());
		copia.setOrden(original.getOrden());
		copia.setTramite(original.getTramite());
		
		copia.setEntidadByIdpadre(padre);
		copia.setEntidadByIdentidadbase(original.getEntidadByIdentidadbase());
		copia.setEntidadByIdentidadoriginal(original);
		
		em.persist(copia);
		
		for (Entidaddeterminacion ed : original.getEntidaddeterminacions()) {
			Entidaddeterminacion edcopia = new Entidaddeterminacion();
			edcopia.setDeterminacion(ed.getDeterminacion());
			edcopia.setEntidad(copia);
			
			em.persist(edcopia);
			
			for (Casoentidaddeterminacion ced : ed.getCasoentidaddeterminacions()) {
				copiar(ced,edcopia);
			}
		}
		
		
		em.flush();
		
		return copia;
	}

    /**
     * 
     * @param original
     * @param edcopia
     */
	private void copiar(Casoentidaddeterminacion original,
			Entidaddeterminacion edcopia) {
		Casoentidaddeterminacion copia = new Casoentidaddeterminacion();
		
		copia.setNombre(original.getNombre());
		copia.setOrden(original.getOrden());
		copia.setEntidaddeterminacion(edcopia);
		
		em.persist(copia);
		
		for (Entidaddeterminacionregimen edr : original.getEntidaddeterminacionregimensForIdcaso()) {
			Entidaddeterminacionregimen edrCopia = new Entidaddeterminacionregimen();
			edrCopia.setCasoentidaddeterminacionByIdcaso(copia);
			edrCopia.setCasoentidaddeterminacionByIdcasoaplicacion(edr.getCasoentidaddeterminacionByIdcasoaplicacion());
			
			edrCopia.setDeterminacion(edr.getDeterminacion());
			
			edrCopia.setOpciondeterminacion(edr.getOpciondeterminacion());
			
			edrCopia.setSuperposicion(edr.getSuperposicion());
			
			edrCopia.setValor(edr.getValor());
			
			em.persist(edrCopia);
			
			for (Regimenespecifico re : edr.getRegimenespecificos()) {
				copiar(re,edrCopia);
			}
		}
		
		for (Entidaddeterminacionregimen edr : original.getEntidaddeterminacionregimensForIdcasoaplicacion()) {
			Entidaddeterminacionregimen edrCopia = new Entidaddeterminacionregimen();
			edrCopia.setCasoentidaddeterminacionByIdcaso(edr.getCasoentidaddeterminacionByIdcaso());
			edrCopia.setCasoentidaddeterminacionByIdcasoaplicacion(copia);
			
			edrCopia.setDeterminacion(edr.getDeterminacion());
			
			edrCopia.setOpciondeterminacion(edr.getOpciondeterminacion());
			
			edrCopia.setSuperposicion(edr.getSuperposicion());
			
			edrCopia.setValor(edr.getValor());
			
			em.persist(edrCopia);
			
			for (Regimenespecifico re : edr.getRegimenespecificos()) {
				copiar(re,edrCopia);
			}
		}
		
		for (Vinculocaso vc : original.getVinculocasosForIdcaso()) {
			Vinculocaso vcCopia = new Vinculocaso();
			
			vcCopia.setCasoentidaddeterminacionByIdcaso(copia);
			vcCopia.setCasoentidaddeterminacionByIdcasovinculado(vc.getCasoentidaddeterminacionByIdcasovinculado());
			
			em.persist(vcCopia);
		}
		
		for (Vinculocaso vc : original.getVinculocasosForIdcasovinculado()) {
			Vinculocaso vcCopia = new Vinculocaso();
			
			vcCopia.setCasoentidaddeterminacionByIdcaso(vc.getCasoentidaddeterminacionByIdcaso());
			vcCopia.setCasoentidaddeterminacionByIdcasovinculado(copia);
			
			em.persist(vcCopia);
		}
	}

	/**
	 * 
	 * @param original
	 * @param edrCopia
	 * @return
	 */
	private Regimenespecifico copiar(Regimenespecifico original,
			Entidaddeterminacionregimen edrCopia) {
		Regimenespecifico copia = new Regimenespecifico();
		
		copia.setEntidaddeterminacionregimen(edrCopia);
		copia.setNombre(original.getNombre());
		copia.setOrden(original.getOrden());
		if (original.getRegimenespecifico() != null) {
			copia.setRegimenespecifico(copiar(original.getRegimenespecifico(),edrCopia));
		}
		copia.setTexto(original.getTexto());
		em.persist(copia);
		
		return copia;
	}

	/**
     * 
     * @param iEntO
     * @param iEntR
     * @param contexto 
     * @throws ExcepcionRefundido 
     */
    private void sustitucionGrafica(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido{
    	log.debug("Sustitución gráfica entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try{
            destruccionGrafica(iEntO, iEntR, contexto);
            creacionGrafica(iEntO, iEntR, contexto);
        } catch (Exception e) {
            throw new ExcepcionRefundido("Fallo en la operación sustitución gráfica. " + e.getMessage(), 23107);
        }
    }

    /**
     * Parámetro Boolean completa:
     * true = Se tienen en cuenta todos los valores
     * false = sólo se tienen en cuenta los valores comunes
     * Se borran los valores de la entidad operada, y se copian los valores
     * de la entidad operadora.
     * Se borran las propiedades y vectores de la entidad operada, y se
     * reasignan todas las de la operadora a la operada.
     * Se sustituye el nombre de la operada por el de la operadora.
     * Se sustituye la clave de la operada por la de la operadora.
     * Se borran los documentos de la entidad operada, y se reasignan los de la operadora
     * a la operada.
     * Se sustituye el idEntidadBase de la entidad operada por el de la operadora.
     * 
     * @param entidadOperada
     * @param entidadOperadora
     * @param completa
     * @param tramiteOrdenante
     * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private void sustitucionNormativa(Entidad entidadOperada, Entidad entidadOperadora, Boolean completa, Tramite tramiteOrdenante) throws ExcepcionRefundido{
    	log.debug("Sustitucion normativa completa de entidad operada: " + entidadOperada.getIden() + " con operador: " + entidadOperadora.getIden());
        String s="";
        List<Entidaddeterminacion> listaValoresO;
        List<Entidaddeterminacion> listaValoresR;

        try {
            // Guarda los valores de iEntO que se deben borrar, y los de iEntR que se deben copiar.

            // Valores a borrar (excepto 'Grupo de Entidades')
        	if (completa) {
        		listaValoresO = em.createNamedQuery("Entidaddeterminacion.buscarPorEntidadCaracter")
        			.setParameter("idEntidad", entidadOperada.getIden())
        			.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
        			.getResultList();
        	} else {
        		listaValoresO = em.createNamedQuery("Entidaddeterminacion.buscarPorEntidadCaracterParcial")
            			.setParameter("idEntidad", entidadOperada.getIden())
            			.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
            			.setParameter("idEnt2", entidadOperadora.getIden())
            			.getResultList();
        	}
            
            // Valores a copiar (excepto 'Grupo de Entidades', aunque este filtro también se hace
            //  en el procedimiento ClsMain.copiarEntidadDeterminacion)
        	if (completa) {
        		listaValoresR = em.createNamedQuery("Entidaddeterminacion.buscarPorEntidadCaracter")
            			.setParameter("idEntidad", entidadOperadora.getIden())
            			.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
            			.getResultList();
            } else {
            	listaValoresR = em.createNamedQuery("Entidaddeterminacion.buscarPorEntidadCaracterParcial")
                		.setParameter("idEntidad", entidadOperadora.getIden())
                		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
                		.setParameter("idEnt2", entidadOperada.getIden())
                		.getResultList();
            }
           
            // Eliminar los valores de la entidad operada.
            for (Entidaddeterminacion ed : listaValoresO){
                eliminarEntidadDeterminacion(ed);
            }

            // Copiar los valores de la entidad operadora a la operada
            if(listaValoresR.size()>0){
            	List<Integer> ids = new ArrayList<Integer>();
            	for (Entidaddeterminacion ed : listaValoresR) {
            		ids.add(ed.getIden());
            	}
                copiarEntidadDeterminacion(entidadOperada, ids, false, 0);
            } else {
            	log.debug("La entidad operadora no tiene valores.");
            }

            // Se eliminan todas las relaciones de la entidad operada.
            List<Relacion> listaRelaciones=getRelacionesEntidad(entidadOperada.getIden());
            for (Relacion relacion : listaRelaciones){
            	eliminadorEntidades.eliminar(relacion, null);
            }
            em.flush();

            // Se reasignan todas las relaciones de la entidad operadora a la operada.
            listaRelaciones=getRelacionesEntidad(entidadOperadora.getIden());
            
            for (Relacion relacion : listaRelaciones){
                s="Update planeamiento.Vectorrelacion Set valor=" + entidadOperada.getIden() + " " +
                        "Where iden In (Select vr.iden From planeamiento.Vectorrelacion vr, " +
                        "diccionario.Defvector dv, diccionario.tabla t " +
                        "Where vr.idrelacion=" + relacion.getIden() + " And vr.iddefvector=dv.iden " +
                        "And dv.idtabla=t.iden And Upper(Trim(t.nombre))='ENTIDAD') " ;

                em.createNativeQuery(s).executeUpdate();
            }

            // Se sustituye el nombre de la operada por el de la operadora.
            entidadOperada.setNombre(entidadOperadora.getNombre());

            // Se sustituye la clave de la operada por la de la operadora.
            entidadOperada.setClave(entidadOperadora.getClave());

            // Se borran los documentos de la entidad operada, y se reasignan los de la operadora
            //  a la operada.
            s="Delete From planeamiento.Documentoentidad Where identidad=" + entidadOperada.getIden() + " ";
            em.createNativeQuery(s).executeUpdate();
            s="Update planeamiento.Documentoentidad Set identidad=" + entidadOperada.getIden() + " " +
                    "Where identidad=" + entidadOperadora.getIden() + " ";
            em.createNativeQuery(s).executeUpdate();

            // Se sustituye el idEntidadBase, siempre que la operadora no lo
            //  tenga nulo.
            if (entidadOperadora.getEntidadByIdentidadbase() != null)
            	entidadOperada.setEntidadByIdentidadbase(entidadOperadora.getEntidadByIdentidadbase());
        } catch (Exception e){	
        	throw new ExcepcionRefundido("Fallo en la operación sustitucionNormativa" +
        			(completa? "Completa. ": "Parcial. ")
        			+ e.getMessage(),
        			completa? 23106: 23109);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesEntidadLocal#sustraccionGrafica(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite)
     */
    public void sustraccionGrafica(Entidad iEntO, Entidad iEntR, Tramite tramiteOrdenante, ContextoRefundido contexto) throws ExcepcionRefundido{
        int nRegistros;
        log.debug("Sustracción gráfica entidad operada" + iEntO.getIden() + " codigo " + iEntO.getCodigo() + " operador " + iEntR.getIden() + " codigo " + iEntR.getCodigo());
        try{
             
        	String tipoGeomO=getGeometria(iEntO, contexto);
        	String tipoGeomR=getGeometria(iEntR, contexto);

            if (tipoGeomO.equals("") || tipoGeomR.equals("")){
                contexto.log(LOG.AVISO,"No se puede efectuar la operación de Sustracción Gráfica porque, al menos, una entidad no tiene geometría.");
                return;
            }
            if (tipoGeomO.equals("Error") || tipoGeomR.equals("Error")){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Sustracción Gráfica porque, al menos, una entidad tiene más de una geometría.");
                return;
            }
            if (!tipoGeomR.equalsIgnoreCase("Entidadpol")){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Sustracción Gráfica porque la entidad operadora no es un polígono.");
                return;
            }

            // Código del plan operado (para mensajes)
            String codPlanO=iEntO.getTramite().getPlan().getCodigo();
            
            // Averigua el SRID
            int srid=srId(tipoGeomO, iEntO.getIden());

            // Recupera la geometría de iEntO
            Geometry geomO = geometria(tipoGeomO, iEntO.getIden(), false, contexto);
            if (geomO==null){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Sustracción Gráfica porque la entidad operada no tiene geometría o está suspendida.");
                return;
            }
            
            // Recupera la geometría de iEntR
            Geometry geomR=geometria(tipoGeomR, iEntR.getIden(), false, contexto);
            if (geomR==null){
            	contexto.log(LOG.AVISO,"No se puede efectuar la operación de Sustracción Gráfica porque la entidad operadora no tiene geometría o está suspendida.");
                return;
            }

            // Ejecuta la diferencia de las dos geometrías
            Geometry geomDif=operarGeometrias(geomO, geomR, 3, contexto);
            geomDif=limpiarGeometria(tipoGeomO, geomDif, contexto);

            if(geomDif==null){
                // Si no queda ningún trozo de geometría, se elimina la entidad completa.
            	contexto.log(LOG.AVISO,"El resultado de la sustracción es una geometría nula. Se elimina la entidad [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                eliminar(iEntO, iEntR, tramiteOrdenante, contexto);
            } else {
                if(!geomDif.isEmpty()){
                    nRegistros=0;
                    if((tipoGeomO.equalsIgnoreCase("ENTIDADPOL") && (geomDif.getGeometryType().equalsIgnoreCase("MULTIPOLYGON") || 
                            geomDif.getGeometryType().equalsIgnoreCase("POLYGON")))
                            ||
                            (tipoGeomO.equalsIgnoreCase("ENTIDADLIN") && (geomDif.getGeometryType().equalsIgnoreCase("MULTILINESTRING") || 
                            geomDif.getGeometryType().equalsIgnoreCase("LINESTRING")))
                            ||
                            (tipoGeomO.equalsIgnoreCase("ENTIDADPNT") && (geomDif.getGeometryType().equalsIgnoreCase("MULTIPOINT") || 
                            geomDif.getGeometryType().equalsIgnoreCase("POINT")))
                            ){
                        
                        nRegistros=em.createNativeQuery(String.format(UPDATE_TIPOGEOM, tipoGeomO,geomDif,srid,iEntO.getIden())).executeUpdate();
                    }
                    if (nRegistros!=1){
                        log.warn("No se ha conseguido actualizar la geometría " +
                                "de tipo " + geomDif.getGeometryType() + " en la tabla " + tipoGeomO);
                    }

                } else {
                    // Si no queda ningún trozo de geometría, se elimina la entidad completa.
                	contexto.log(LOG.AVISO, "El resultado de la sustracción es una geometría vacía. Se elimina la entidad [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                    eliminar(iEntO, iEntR, tramiteOrdenante, contexto);
                }
            }
            geomDif = null;
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación sustraccionGrafica. " + e.getMessage(), 23105);
        }

    }

}
