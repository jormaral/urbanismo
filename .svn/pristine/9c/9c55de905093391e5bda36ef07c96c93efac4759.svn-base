/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.gestionfip.exportadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DEExcelDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegulacionEspecificaDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

/**
 * Session Bean implementation class ImportadorCUExcel
 */
@Stateful
@Name("servicioExportacionDE")
public class ServicioExportacionDEBean implements ServicioExportacionDE {
	
	@PersistenceContext
    private EntityManager em;
    
	@Logger 
	private Log log;
    
	@In (create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	
	// Creacion del contexto
    @Resource
    private SessionContext contextoTransaccion;
    
    List<DEExcelDTO> listadoDEExcelDTO;
    
    List<DEExcelDTO> listaAuxiliar =null;
    
    @In (create = true, required = false)
   	VariablesSesionUsuario variablesSesionUsuario;
    
    List<Integer> listaIds;
    
    Map<String, String> mapaGrupo = new HashMap<String, String>();
	
	public DEExcelDTO crearExcel ()
	{
		DEExcelDTO dto = new DEExcelDTO();
		
		
		return dto;
	}
	
	public void setListadoDEExcelDTO(List<DEExcelDTO> listadoDEExcelDTO) {
		this.listadoDEExcelDTO = listadoDEExcelDTO;
	}

	public List<DEExcelDTO> getListadoDEExcelDTO ()
    {
		if (listaAuxiliar==null)
		{
			List<String> listaErroresPersistir = new ArrayList<String>();
	    	int idTramite = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
	    	Tramite tramite  = em.find(Tramite.class, idTramite);
	    	listaAuxiliar = new ArrayList<DEExcelDTO>();
	    	listaIds =  new ArrayList<Integer>(); 
	    	
	    	List<ParIdentificadorTexto> listaIdTexto = servicioBasicoDeterminaciones.getDeterminacionesRaices(idTramite);
	    	
	    	log.info("Construyendo los IDS--" + listaIdTexto.size());
	    	
	    	try{
	    		
	        	for (ParIdentificadorTexto determinacionId : listaIdTexto)
	        	{
	        		
	        		listaIds.add(determinacionId.getIdBaseDatos());
	        		
	        		cargarDeterminacionesHijas(determinacionId.getIdBaseDatos());
	        		
	        	}
	        	
//	        	log.info("Construyendo la lista de Objetos");
//	        	String hql = "SELECT d " + "FROM Determinacion d "
//				+ "WHERE d.tramite.iden ="
//				+ idTramite + " " + "ORDER BY d.orden, d.nombre   ASC";
//	        	List<Determinacion> ldet = em.createQuery(hql).getResultList();
//	        	for (Determinacion det : ldet)
//	        	{
//	        		DEExcelDTO deExcel = crearDEExcelDTO(det);
//	        		
//	        		listaAuxiliar.add(deExcel);
//	        	}
	        	
	        	for (Integer idDet : listaIds)
	        	{
	        		Determinacion det = em.find(Determinacion.class, idDet);
	        		System.out.println(det.getIden() + det.getNombre());
	        		DEExcelDTO deExcel = crearDEExcelDTO(det);
	        		
	        		listaAuxiliar.add(deExcel);
	        	}
	        	
	    		
	    	}
	    	catch (Exception e)
	    	{
	    		log.error("[persistirCUdeExcel] ERROR: Hago un rollback");
	    		contextoTransaccion.setRollbackOnly();
	    		listaErroresPersistir.add("ERROR Desconocido, se ha hecho rollback y no se ha guardado nada en BD");
	    		
	    		e.printStackTrace();
	    	}
		}
    	
    	
    	
    	return listaAuxiliar;
    }
	
	
	public void cargarDeterminacionesHijas(int idDeterminacion)
	{
		List<ParIdentificadorTexto> listaIdTexto = servicioBasicoDeterminaciones.getDeterminacionesHijasDeDet(idDeterminacion);
		if(listaIdTexto!=null && listaIdTexto.size()>0){
			for (ParIdentificadorTexto determinacionId : listaIdTexto)
	    	{
	    		
	    		//DEExcelDTO deExcel = crearDEExcelDTO(determinacionId.getIdBaseDatos());
	    		
	    		listaIds.add(determinacionId.getIdBaseDatos());
	    		
	    		cargarDeterminacionesHijas(determinacionId.getIdBaseDatos());
	    	}
		}
	}
	public DEExcelDTO crearDEExcelDTO(int determinacionId)
	{
		DEExcelDTO deExcel = new DEExcelDTO();
		Determinacion determinacion = em.find(Determinacion.class, determinacionId);
		
		// Cargamos los valores
		
		//log.info(determinacion.getNombre());
		deExcel.setNombreDeterminacion(determinacion.getNombre());
		deExcel.setApartadoArticulo(determinacion.getApartado());
		deExcel.setEtiqueta(determinacion.getEtiqueta());
		
		//determinacion.setTramite(tramite);
		if (determinacion.getDeterminacionByIddeterminacionbase()!=null)
			deExcel.setIdDeterminacionBase(determinacion.getDeterminacionByIddeterminacionbase().getIden());
		
		// Caracter
		deExcel.setTextoCaracter(servicioBasicoDeterminaciones.caracterString(determinacion.getIdcaracter()));
		
		// Referencia
		if (deExcel.getTextoCaracter().toLowerCase().equals("valor de referencia"))
			deExcel.setReferencia("SI");
		else
			deExcel.setReferencia("");
		
		//Equivalente
		if (determinacion.getDeterminacionByIddeterminacionbase()!=null)
				deExcel.setEquivale(determinacion.getDeterminacionByIddeterminacionbase().getNombre());
		
		// Unidad
		ParIdentificadorTexto unidad = servicioBasicoDeterminaciones.obtenerUnidadDeDeterminacion(determinacion.getIden());
		if (unidad!=null)
			deExcel.setTextoUnidad(unidad.getTexto());
		
		//Grupo aplicacion
		
		Determinacion[] arrayGruposApl = servicioBasicoDeterminaciones.getArrayGruposAplicacion(determinacion.getIden());
		
		if (arrayGruposApl!=null)
		{
			String codigosGrupos="";
			for (int i=0; i<arrayGruposApl.length;i++)
			{
				codigosGrupos = codigosGrupos+arrayGruposApl[i].getEtiqueta()+" / ";
			}
			
			deExcel.setTextoGrupoAplicacion(codigosGrupos);
			
		}
		
		/*
		
		if(determinacion.getGrupoAplicacionDeterminacion()!=null)
			deExcel.setTextoGrupoAplicacion(determinacion.getGrupoAplicacionDeterminacion());
		else
			deExcel.setTextoGrupoAplicacion("");
			*/
		
		//Etiqueta
		deExcel.setEtiqueta(determinacion.getEtiqueta());
		
		deExcel.setOrden(determinacion.getOrden());
		
		// Esto no tengo claro si está bien
		if (determinacion.getDeterminacionByIdpadre()!=null)
			deExcel.setIdListaPadre(determinacion.getDeterminacionByIdpadre().getIden());
			
		
		//Buscamos las regulaciones específicas 
//		List<ParIdentificadorTexto> listaRegulaciones = servicioBasicoDeterminaciones.getRegulacionespecificaRaices(determinacion.getIden());
//		int cont = 1;
//		for (ParIdentificadorTexto regPar : listaRegulaciones)
//		{
//			RegulacionEspecificaDTO regDto = servicioBasicoDeterminaciones.buscarRegulacionEspecifica(regPar.getIdBaseDatos());
//			if (cont==1)
//			{
//				deExcel.setNombreRegulacion1(regDto.getNombre());
//				deExcel.setTextoRegulacion1(regDto.getTexto());
//			}
//			else if (cont==2)
//			{
//				deExcel.setNombreRegulacion2(regDto.getNombre());
//				deExcel.setTextoRegulacion2(regDto.getTexto());
//			}
//			else if (cont==3)
//			{
//				deExcel.setNombreRegulacion3(regDto.getNombre());
//				deExcel.setTextoRegulacion3(regDto.getTexto());
//			}
//			else if (cont==4)
//			{
//				deExcel.setNombreRegulacion4(regDto.getNombre());
//				deExcel.setTextoRegulacion4(regDto.getTexto());
//			}
//			else if (cont==5)
//			{
//				deExcel.setNombreRegulacion5(regDto.getNombre());
//				deExcel.setTextoRegulacion5(regDto.getTexto());
//			}
//			cont++;
//			
//		}
		
//		Determinacion[] arrayDets = servicioBasicoDeterminaciones.getArrayGruposAplicacion(determinacion.getIden());
//		// Creamos el grupo aplicación
//		int i = 0;
//		String grupos = "";
//		while (i<arrayDets.length)
//		{
//			Determinacion detGrupo = (Determinacion) arrayDets[i];
//			
//			grupos = grupos + detGrupo.getCodigo() + ",";
//			i++;
//		}
//		// Quitamos la ultima coma
//		if (grupos.length()>2)
//			grupos = grupos.substring(0, grupos.length()-2);
//		
//		deExcel.setTextoGrupoAplicacion(grupos);
//			
//		//Creamos la unidad
//		ParIdentificadorTexto unidad = servicioBasicoDeterminaciones.obtenerUnidadDeDeterminacion(determinacion.getIden());
//		
//		deExcel.setTextoUnidad(unidad.getTexto());
		
		return deExcel;
	}
	
	public DEExcelDTO crearDEExcelDTO(Determinacion determinacion)
	{
		DEExcelDTO deExcel = new DEExcelDTO();
		// Cargamos los valores
		
		String orden = servicioBasicoDeterminaciones.obtenerOrdenCompletoDeterminacion(determinacion.getIden());
		
		//log.info("ORDEN ENCONTRADO--" + orden + "-" + determinacion.getNombre());
		
		int contador = numeroOcurrencias(".", orden);
		
		orden = orden.substring(0, orden.length()-1);
		
		if (contador==1)
		{
			deExcel.setNombreNivel_1(determinacion.getNombre());
			deExcel.setOrdenNivel_1(orden);
		}
		else if (contador==2)
		{
			deExcel.setNombreNivel_2(determinacion.getNombre());
			deExcel.setOrdenNivel_2(orden);
		}
		else if (contador==3)
		{
			deExcel.setNombreNivel_3(determinacion.getNombre());
			deExcel.setOrdenNivel_3(orden);
		}
		else if (contador==4)
		{
			deExcel.setNombreNivel_4(determinacion.getNombre());
			deExcel.setOrdenNivel_4(orden);
		}
		else if (contador==5)
		{
			deExcel.setNombreNivel_5(determinacion.getNombre());
			deExcel.setOrdenNivel_5(orden);
		}
		else if (contador==6)
		{
			deExcel.setNombreNivel_6(determinacion.getNombre());
			deExcel.setOrdenNivel_6(orden);
		}
		else if (contador==7)
		{
			deExcel.setNombreNivel_7(determinacion.getNombre());
			deExcel.setOrdenNivel_7(orden);
		}
		else if (contador==8)
		{
			deExcel.setNombreNivel_8(determinacion.getNombre());
			deExcel.setOrdenNivel_8(orden);
		}
		
		deExcel.setNombreDeterminacion(determinacion.getNombre());
		deExcel.setApartadoArticulo(determinacion.getApartado());
		deExcel.setEtiqueta(determinacion.getEtiqueta());
		
		//determinacion.setTramite(tramite);
		if (determinacion.getDeterminacionByIddeterminacionbase()!=null)
			deExcel.setIdDeterminacionBase(determinacion.getDeterminacionByIddeterminacionbase().getIden());
		
		deExcel.setOrden(determinacion.getOrden());
		
		
		// Caracter
		deExcel.setTextoCaracter(servicioBasicoDeterminaciones.caracterString(determinacion.getIdcaracter()));
		
		// Referencia
		if (deExcel.getTextoCaracter().toLowerCase().equals("valor de referencia"))
			deExcel.setReferencia("SI");
		else
			deExcel.setReferencia("");
		
		//Equivalente
		if (determinacion.getDeterminacionByIddeterminacionbase()!=null)
				deExcel.setEquivale(determinacion.getDeterminacionByIddeterminacionbase().getNombre());
		
		// Unidad
		ParIdentificadorTexto unidad = servicioBasicoDeterminaciones.obtenerUnidadDeDeterminacion(determinacion.getIden());
		if (unidad!=null)
			deExcel.setTextoUnidad(unidad.getTexto());
		
		//Grupo aplicacion
		Determinacion[] arrayGruposApl = servicioBasicoDeterminaciones.getArrayGruposAplicacion(determinacion.getIden());
		
		if (arrayGruposApl!=null)
		{
			String codigosGrupos="";
			for (int i=0; i<arrayGruposApl.length;i++)
			{
				codigosGrupos = codigosGrupos+arrayGruposApl[i].getEtiqueta()+" / ";
			}
			
			deExcel.setTextoGrupoAplicacion(codigosGrupos);
			
		}
		//deExcel.setTextoGrupoAplicacion(determinacion.getGrupoAplicacionDeterminacion());
		
		//Apartado artículo
		if (determinacion.getApartado()==null)
			deExcel.setApartadoArticulo("");
		else
			deExcel.setApartadoArticulo(determinacion.getApartado());
		
		//Etiqueta
		deExcel.setEtiqueta(determinacion.getEtiqueta());
		
		
		//Buscamos las regulaciones específicas 
		List<ParIdentificadorTexto> listaRegulaciones = servicioBasicoDeterminaciones.getRegulacionespecificaRaices(determinacion.getIden());
		int cont = 1;
		for (ParIdentificadorTexto regPar : listaRegulaciones)
		{
			RegulacionEspecificaDTO regDto = servicioBasicoDeterminaciones.buscarRegulacionEspecifica(regPar.getIdBaseDatos());
			if (cont==1)
			{
				deExcel.setNombreRegulacion1(regDto.getNombre());
				deExcel.setTextoRegulacion1(regDto.getTexto());
			}
			else if (cont==2)
			{
				deExcel.setNombreRegulacion2(regDto.getNombre());
				deExcel.setTextoRegulacion2(regDto.getTexto());
			}
			else if (cont==3)
			{
				deExcel.setNombreRegulacion3(regDto.getNombre());
				deExcel.setTextoRegulacion3(regDto.getTexto());
			}
			else if (cont==4)
			{
				deExcel.setNombreRegulacion4(regDto.getNombre());
				deExcel.setTextoRegulacion4(regDto.getTexto());
			}
			else if (cont==5)
			{
				deExcel.setNombreRegulacion5(regDto.getNombre());
				deExcel.setTextoRegulacion5(regDto.getTexto());
			}
			cont++;
			
		}
		
//		Determinacion[] arrayDets = servicioBasicoDeterminaciones.getArrayGruposAplicacion(determinacion.getIden());
//		// Creamos el grupo aplicación
//		int i = 0;
//		String grupos = "";
//		while (i<arrayDets.length)
//		{
//			Determinacion detGrupo = (Determinacion) arrayDets[i];
//			
//			grupos = grupos + detGrupo.getCodigo() + ",";
//			i++;
//		}
//		// Quitamos la ultima coma
//		if (grupos.length()>2)
//			grupos = grupos.substring(0, grupos.length()-2);
//		
//		deExcel.setTextoGrupoAplicacion(grupos);
			
		//Creamos la unidad
		
		return deExcel;
	}
	
	private int numeroOcurrencias(String sTextoBuscado, String sTexto)
	{
		int contador=0;
		
		while (sTexto.indexOf(sTextoBuscado) > -1) {
		      sTexto = sTexto.substring(sTexto.indexOf(
		        sTextoBuscado)+sTextoBuscado.length(),sTexto.length());
		      contador++;
		}
		
		return contador;
	}
	
	@Remove
	public void destroy(){}

}