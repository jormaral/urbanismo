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

import com.mitc.redes.editorfip.entidades.interfazgrafico.CUExcelDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegimenesEspecificosSimplificadosDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

/**
 * Exportación de CU en Excel
 */
@Stateless
@Name("servicioExportacionCU")
public class ServicioExportacionCUBean implements ServicioExportacionCU {
	
	@PersistenceContext
    private EntityManager em;
    
	@Logger 
	private Log log;
    
	@In (create = true, required = false)
	ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;
	
	@In (create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	
	// Creacion del contexto
    @Resource
    private SessionContext contextoTransaccion;
    
    List<CUExcelDTO> listadoExcelDTO;
    
    private Tramite tramite;
    
    int nivel = 0;
    
    List<CUExcelDTO> listaAuxiliar;
    
    @In (create = true, required = false)
   	VariablesSesionUsuario variablesSesionUsuario;
    
    List<Integer> listaIds;
    
    Map<String, String> mapaGrupo = new HashMap<String, String>();
	
	public void setListadoExcelDTO(List<CUExcelDTO> listadoExcelDTO) {
		this.listadoExcelDTO = listadoExcelDTO;
	}

	public List<CUExcelDTO> getListadoExcelDTO ()
    {
    	List<String> listaErroresPersistir = new ArrayList<String>();
    	int idTramite = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
    	tramite  = em.find(Tramite.class, idTramite);

    	listaAuxiliar = new ArrayList<CUExcelDTO>();
    	listaIds =  new ArrayList<Integer>(); 
    	
    	List<CondicionUrbanisticaSimplificadaDTO> listaCU = servicioBasicoCondicionesUrbanisticas.listadoTodasCUSimplificadaDeTramite(idTramite);
    	
    	log.info("Construyendo los IDS--" + listaCU.size());
    	
    	try{
    		
    		boolean primero = true;
        	for (CondicionUrbanisticaSimplificadaDTO objeto : listaCU)
        	{	
        		CUExcelDTO excelDto = crearExcelDTO(objeto);
        		
        		// Comprobamos si es el primero de la nueva ronda
        		if (objeto.getNombreDeterminacion().equalsIgnoreCase("Grupo de entidades") && !primero){
        			CUExcelDTO excelDtoFinEnt = new CUExcelDTO();
        			excelDtoFinEnt.setCodigoEntidad("finentidad");
        			listaAuxiliar.add(excelDtoFinEnt);
        		}
        		primero = false;
        		listaAuxiliar.add(excelDto);
        		
        		// Añadimos los regimenes especificos
        		
        		if (objeto.getRegimenesEspecificos()!=null && objeto.getRegimenesEspecificos().size()>1)
        		{
        			int i = 1;
        			while (i<objeto.getRegimenesEspecificos().size()){
	        			CUExcelDTO entidadDTO = new CUExcelDTO();
	        			RegimenesEspecificosSimplificadosDTO reDto = objeto.getRegimenesEspecificos().get(i);
	        			entidadDTO.setTituloRegEsp(reDto.getNombreRegimenEspecifico());
	        			entidadDTO.setTextoRegEsp(reDto.getTextoRegimenEspecifico());
	        			listaAuxiliar.add(entidadDTO);
	        			i++;
	        			log.info("PASANDO POR LOS RÉGIMENTES ESPECIFICOS--" + entidadDTO.getTextoRegEsp() + "-.-" + entidadDTO.getTituloRegEsp());
        			}
        		}
        		
        	}
        	
        	//Cerramos
        	CUExcelDTO excelDtoFinEnt = new CUExcelDTO();
        	excelDtoFinEnt.setCodigoEntidad("finentidad");
			listaAuxiliar.add(excelDtoFinEnt);
			
			CUExcelDTO excelDtoEmpty = new CUExcelDTO();
			excelDtoEmpty.setCodigoEntidad("finfichero");
			listaAuxiliar.add(excelDtoEmpty);
        	
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
//        		CUExcelDTO enExcel = crearEntidadDTO(ent);
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
	
	
	private CUExcelDTO crearEntidadDTO(Entidad entidad)
	{
		CUExcelDTO entidadDTO = new CUExcelDTO();
		
		return entidadDTO;
	}
	
	private CUExcelDTO crearExcelDTO(CondicionUrbanisticaSimplificadaDTO entidad)
	{
		CUExcelDTO entidadDTO = new CUExcelDTO();
		
		entidadDTO.setCodigoEntidad(entidad.getClaveEntidad());
		entidadDTO.setNombreEntidad(entidad.getNombreEntidad());
		
		if (!entidad.getNombreDeterminacion().equalsIgnoreCase("Grupo de entidades"))
		{
			String orden = "";
			
			if (entidad.getIdDeterminacion()!=0){
				orden = servicioBasicoDeterminaciones.obtenerOrdenCompletoDeterminacion(entidad.getIdDeterminacion());
				if(!orden.equals("."))
					entidadDTO.setApartadoDeterminacion(orden);
			}
			entidadDTO.setNombreDeterminacion(entidad.getNombreDeterminacion());
			
			orden = servicioBasicoDeterminaciones.obtenerOrdenCompletoDeterminacion(entidad.getIdDeterminacionRegimen());
			if(!orden.equals("."))
				entidadDTO.setApartadoDeterminacionRegimen(orden);
			entidadDTO.setNombreDeterminacionRegimen(entidad.getNombreDetRegimen());
			
			orden = servicioBasicoDeterminaciones.obtenerOrdenCompletoDeterminacion(entidad.getIdDeterminacionValorReferencia());
			if(!orden.equals("."))
				entidadDTO.setApartadoValorReferencia(orden);
			entidadDTO.setNombreValor(entidad.getNombreDetValorRef());
			
			// Añadimos el regimen especifico si lo hay
			if (entidad.getRegimenesEspecificos()!=null && entidad.getRegimenesEspecificos().size()>0)
			{
				RegimenesEspecificosSimplificadosDTO reDto = entidad.getRegimenesEspecificos().get(0);
				entidadDTO.setTituloRegEsp(reDto.getNombreRegimenEspecifico());
				entidadDTO.setTextoRegEsp(reDto.getTextoRegimenEspecifico());
			}
		}
		else
		{
			// Guardamos el grupo de aplicacion
			//nombreDetValorRef
			entidadDTO.setNombreGrupoAplicacion(entidad.getNombreDetValorRef());
		}
		
		
		// Grupo aplicacion
		
		
		
		//entidadDTO.setTituloRegEsp(entidad.getr)
		//entidadDTO.setApartadoValorReferencia(entidad.getIdDeterminacionValorReferencia());
		
		return entidadDTO;
	}
	
	public void cargarEntidadesHijas(int idObjeto)
	{	
//		List<ParIdentificadorTexto> listaIdTexto = servicioBasicoEntidades.getEntidadesHijas(idObjeto);
//		
//		Entidad ent = em.find(Entidad.class, idObjeto);
//		
//		if (nivel!=0)
//			listaAuxiliar.add(crearExcelDTO(ent, nivel));
//		
//		nivel++;
//		if(listaIdTexto!=null && listaIdTexto.size()>0){
//			for (ParIdentificadorTexto determinacionId : listaIdTexto)
//	    	{
//	    		listaIds.add(determinacionId.getIdBaseDatos());
//	    		
//	    		cargarEntidadesHijas(determinacionId.getIdBaseDatos());
//	    	}
//		}
//		
//		CUExcelDTO entDto = new CUExcelDTO();
//		entDto.setMunicipio("fingrupo");
//		if (nivel==3)
//			listaAuxiliar.add(entDto);
//		
//		nivel--;
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
	
	@Remove
	public void destroy(){}

}