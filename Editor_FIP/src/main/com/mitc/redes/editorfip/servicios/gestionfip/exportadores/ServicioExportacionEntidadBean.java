package com.mitc.redes.editorfip.servicios.gestionfip.exportadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DEExcelDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.EntidadExcelDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

/**
 * Session Bean implementation class ImportadorCUExcel
 */
@Stateless
@Name("servicioExportacionEntidad")
public class ServicioExportacionEntidadBean implements ServicioExportacionEntidad {
	
	@PersistenceContext
    private EntityManager em;
    
	@Logger 
	private Log log;
    
	@In (create = true, required = false)
	ServicioBasicoEntidades servicioBasicoEntidades;
	
	// Creacion del contexto
    @Resource
    private SessionContext contextoTransaccion;
    
    List<EntidadExcelDTO> listadoExcelDTO;
    
    private Tramite tramite;
    
    int nivel = 0;
    
    List<EntidadExcelDTO> listaAuxiliar;
    boolean ponerFinGrupo = false;
    
    @In (create = true, required = false)
   	VariablesSesionUsuario variablesSesionUsuario;
    
    List<Integer> listaIds;
    
    Map<String, String> mapaGrupo = new HashMap<String, String>();
	
	public DEExcelDTO crearExcel ()
	{
		DEExcelDTO dto = new DEExcelDTO();
		
		
		return dto;
	}
	
	public void setListadoExcelDTO(List<EntidadExcelDTO> listadoExcelDTO) {
		this.listadoExcelDTO = listadoExcelDTO;
	}

	public List<EntidadExcelDTO> getListadoExcelDTO ()
    {
    	List<String> listaErroresPersistir = new ArrayList<String>();
    	int idTramite = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
    	tramite  = em.find(Tramite.class, idTramite);
    	
    	ponerFinGrupo = false;
    	
    	listaAuxiliar = new ArrayList<EntidadExcelDTO>();
    	listaIds =  new ArrayList<Integer>(); 
    	
    	List<ParIdentificadorTexto> listaIdTexto = servicioBasicoEntidades.getEntidadesRaices(idTramite);
    	
    	log.info("Construyendo los IDS--" + listaIdTexto.size());
    	
    	try{
    		
        	for (ParIdentificadorTexto objetoId : listaIdTexto)
        	{
        		
        		listaIds.add(objetoId.getIdBaseDatos());
        		
        		cargarEntidadesHijas(objetoId.getIdBaseDatos());
        		
        		// Añadimos fin grupo y fin entidad
        		EntidadExcelDTO entDto = new EntidadExcelDTO();
        		entDto.setMunicipio("fingrupo");
        		listaAuxiliar.add(entDto);
        		
        		entDto = new EntidadExcelDTO();
        		entDto.setMunicipio("finentidad");
        		listaAuxiliar.add(entDto);
        		
        	}
        	
//        	log.info("Construyendo la lista de Objetos");
//        	String hql = "SELECT d " + "FROM Determinacion d "
//			+ "WHERE d.tramite.iden ="
//			+ idTramite + " " + "ORDER BY d.orden, d.nombre   ASC";
//        	List<Determinacion> ldet = em.createQuery(hql).getResultList();
//        	for (Determinacion det : ldet)
//        	{
//        		DEExcelDTO deExcel = crearDEExcelDTO(det);
//        		
//        		listaAuxiliar.add(deExcel);
//        	}
//        	String ambito = ((Ambito)em.find(Ambito.class, tramite.getPlan().getIdambito())).getCodigoine();
//        	
//        	log.info("AMBITO SELECCIONADO---" + ambito);
//        	for (Integer idDet : listaIds)
//        	{
//        		Entidad ent = em.find(Entidad.class, idDet);
//        		EntidadExcelDTO enExcel = crearEntidadDTO(ent);
//        		enExcel.setMunicipio(ambito);
//        		
//        		listaAuxiliar.add(enExcel);
//        	}
        	
    		
    	}
    	catch (Exception e)
    	{
    		log.error("[persistirCUdeExcel] ERROR: Hago un rollback");
    		contextoTransaccion.setRollbackOnly();
    		listaErroresPersistir.add("ERROR Desconocido, se ha hecho rollback y no se ha guardado nada en BD");
    		
    		e.printStackTrace();
    	}
    	
    	
    	return listaAuxiliar;
    }
	
	
	private EntidadExcelDTO crearEntidadDTO(Entidad entidad)
	{
		EntidadExcelDTO entidadDTO = new EntidadExcelDTO();
		
		entidadDTO.setPlan(tramite.getPlan().getCodigo());
		
		//Caso base de ámbito
		if (entidad.getNombre().equals("Ámbito")){
			entidadDTO.setCodGrupo("AMB");
			entidadDTO.setGrupo(entidad.getNombre());
		}
		
		if (entidad.getNombre()!=null && !entidad.equals("")){
			entidadDTO.setCodGrupo(obtenerCodigoGrupo(entidad.getNombre()));
			entidadDTO.setGrupo(entidad.getNombre());
		}
		
		
		
		return entidadDTO;
	}
	
	private EntidadExcelDTO crearEntidadDTO(Entidad entidad, int nivel)
	{
		EntidadExcelDTO entidadDTO = new EntidadExcelDTO();
		
		entidadDTO.setPlan(tramite.getPlan().getCodigo());
		
		//Caso base de ámbito
//		if (entidad.getNombre().equals("Ámbito")){
//			entidadDTO.setCodGrupo("AMB");
//			entidadDTO.setGrupo(entidad.getNombre());
//		}
//		
//		if (entidad.getNombre()!=null && !entidad.equals("")){
//			entidadDTO.setCodGrupo(obtenerCodigoGrupo(entidad.getNombre()));
//			entidadDTO.setGrupo(entidad.getNombre());
//		}
		
		if (nivel==1){
			entidadDTO.setCodGrupo(obtenerCodigoGrupo(entidad.getNombre()));
			entidadDTO.setGrupo(entidad.getNombre());
		}
		else if(nivel==2){
			entidadDTO.setSubgrupo(entidad.getNombre());
		}
		else if (nivel ==3){
			entidadDTO.setEntidad(entidad.getNombre());
		}
		else if (nivel ==4){
			entidadDTO.setNivel4(entidad.getNombre());
		}
		
		//Etiqueta
		entidadDTO.setEtiqueta(entidad.getEtiqueta());
		//Equivalencias
		if (entidad.getEntidadByIdentidadbase()!=null)
			entidadDTO.setEquivalencias(entidad.getEntidadByIdentidadbase().getNombre());	
		
		//Fuente
		entidadDTO.setFuente("");
		
		entidadDTO.setClave((entidad.getClave()));
		
		return entidadDTO;
	}
	
	public void cargarEntidadesHijas(int idObjeto)
	{	
		List<ParIdentificadorTexto> listaIdTexto = servicioBasicoEntidades.getEntidadesHijas(idObjeto);
		
		Entidad ent = em.find(Entidad.class, idObjeto);
		
		EntidadExcelDTO entDto = new EntidadExcelDTO();
		entDto.setMunicipio("fingrupo");
		if (nivel==1 && ponerFinGrupo)
			listaAuxiliar.add(entDto);
		else if(nivel==1 && !ponerFinGrupo)
			ponerFinGrupo = true;
		
		if (nivel!=0)
			listaAuxiliar.add(crearEntidadDTO(ent, nivel));
		
		nivel++;
		if(listaIdTexto!=null && listaIdTexto.size()>0){
			for (ParIdentificadorTexto determinacionId : listaIdTexto)
	    	{
	    		listaIds.add(determinacionId.getIdBaseDatos());
	    		
	    		cargarEntidadesHijas(determinacionId.getIdBaseDatos());
	    	}
		}
		
//		EntidadExcelDTO entDto = new EntidadExcelDTO();
//		entDto.setMunicipio("fingrupo");
//		if (nivel==3)
//			listaAuxiliar.add(entDto);
		
		nivel--;
	}
	
	private String obtenerCodigoGrupo(String nombre)
	{
		if (nombre.equalsIgnoreCase("zonas"))
			return "ZON";
		else if (nombre.equalsIgnoreCase("ámbito"))
			return "AMB";
		else if (nombre.equalsIgnoreCase("sistemas"))
			return "SIS";
		else
			return "NULO";
	}
	
	public boolean isPonerFinGrupo() {
		return ponerFinGrupo;
	}

	public void setPonerFinGrupo(boolean ponerFinGrupo) {
		this.ponerFinGrupo = ponerFinGrupo;
	}

	@Remove
	public void destroy(){}

}