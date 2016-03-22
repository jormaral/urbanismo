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

package com.mitc.redes.editorfip.servicios.gestionfip.importadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.CUExcelDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DEExcelDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegulacionEspecificaDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;

/**
 * Session Bean implementation class ImportadorCUExcel
 */
@Stateless
@Name("importadorDEExcel")
public class ImportadorDEExcelBean implements ImportadorDEExcel{

	@PersistenceContext
    private EntityManager em;
    
	@Logger 
	private Log log;
    
	@In (create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    // Creacion del contexto
    @Resource
    private SessionContext contextoTransaccion;
    
    Map<String, String> mapaGrupo = new HashMap<String, String>();

    // -- METODOS -- //
    
    public ImportadorDEExcelBean() {
		// TODO Auto-generated constructor stub
    	 
         mapaGrupo.put("ACC","7000000043");
         mapaGrupo.put("AFE","7000000031");
         mapaGrupo.put("AFE(L)","7000000066");
         mapaGrupo.put("AH","7000000007");
         
         mapaGrupo.put("ALN","7000000053");
         mapaGrupo.put("AMB","7000000002");
         mapaGrupo.put("CAR","7000000001");
         mapaGrupo.put("CLA","7000000003");
         
         mapaGrupo.put("DES","7100000007");
         mapaGrupo.put("GES","7100000006");
         mapaGrupo.put("GES_AP","7000000018");
         mapaGrupo.put("GES_S","7000000017");
         
         mapaGrupo.put("GES_UA","7000000016");
         mapaGrupo.put("GES_UE","7000000021");
         mapaGrupo.put("LDO","7100000001");
         mapaGrupo.put("OP","7000000029");
         
         mapaGrupo.put("OP(L)","7000000076");
         mapaGrupo.put("OP(P)","7000000077");
         mapaGrupo.put("PRO","7000000013");
         mapaGrupo.put("PRO(L)","7000000057");
         
         mapaGrupo.put("PRO(P)","7000000059");
         mapaGrupo.put("RES","7000000030");
         mapaGrupo.put("RG_ENP","7100000004");
         mapaGrupo.put("RST","7000000055");
         mapaGrupo.put("RUS","7100000010");
         
         mapaGrupo.put("RUS_SUB","7100000011");
         mapaGrupo.put("RUS_SUB(L)","7100000013");
         mapaGrupo.put("RUS_SUB(P)","7100000012");
         mapaGrupo.put("SG","7100000009");
         mapaGrupo.put("SG(L)","7100000017");
         
         mapaGrupo.put("SG(P)","7100000016");
         mapaGrupo.put("SIS","7000000008");
         mapaGrupo.put("SIS(L)","7000000050");
         mapaGrupo.put("SL","7100000008");
         mapaGrupo.put("SL(L)","7100000015");
         mapaGrupo.put("SL(P)","7100000014");
         
         mapaGrupo.put("SNU","7000000006");
         mapaGrupo.put("SU","7000000004");
         mapaGrupo.put("SUR","7000000005");
         mapaGrupo.put("SUR_SUB","7000000041");
         mapaGrupo.put("SU_SUB","7000000040");
         
         mapaGrupo.put("UG","7000000025");
         mapaGrupo.put("ZON","7000000011");
         mapaGrupo.put("ZON_EDIF","7100000002");
         mapaGrupo.put("ZON_ENP","7100000005");
         mapaGrupo.put("ZON_USO","7100000003");

	}
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<String> persistirDEdeExcel (int idTramite, List<DEExcelDTO> listaDetExcel)
    {
    	List<String> listaErroresPersistir = new ArrayList<String>();
    	Tramite tramite  = em.find(Tramite.class, idTramite);
    	try{
    		
        	CondicionUrbanisticaSimplificadaDTO cuSimplificadaAnterior=null;
        	CUExcelDTO cuExcelAnterior=null;
        	List<Determinacion> listaAuxiliar = new ArrayList<Determinacion>();
        	
        	// Lo inicializo a uno porque la fila en el excel empieza por la fila 3 (uno, mas uno por defecto, mas uno por grupo de aplicacion)
        	int indiceFilaExcel=2;
        	// Voy adquiriendo
        	for (DEExcelDTO deExcel : listaDetExcel)
        	{
        		indiceFilaExcel++;
        		
        		int resultadoPersistir=0;
        		boolean persistoCU = true;
        		Determinacion determinacion = new Determinacion();
        		
        		// Cargamos los valores
        		determinacion.setNombre(deExcel.getNombreDeterminacion());
        		determinacion.setApartado(deExcel.getApartadoArticulo());
        		determinacion.setEtiqueta(deExcel.getEtiqueta());
        		determinacion.setTramite(tramite);
        		
        		Determinacion detBase = em.find(Determinacion.class, deExcel.getIdDeterminacionBase());
        		determinacion.setDeterminacionByIddeterminacionbase(detBase);
        		determinacion.setIdcaracter(deExcel.getIdCaracter());
        		determinacion.setOrden(deExcel.getOrden());
        		
        		System.out.println("========" + deExcel.getIdListaPadre() + "==" + deExcel.getOrden() + "==" +  listaAuxiliar.size() + deExcel.getNombreDeterminacion());
        		if (deExcel.getIdListaPadre()!=-1)
        			determinacion.setDeterminacionByIdpadre(listaAuxiliar.get(deExcel.getIdListaPadre()));
        		
        		determinacion.setCodigo(servicioBasicoDeterminaciones.nextCodigo(tramite.getIden()+""));
        		em.persist(determinacion);
        		
        		int id = determinacion.getIden();
        		
        		listaAuxiliar.add(determinacion);
        		
        		if (determinacion.getDeterminacionByIdpadre()!=null && deExcel.getReferencia()!=null && deExcel.getReferencia().equalsIgnoreCase("si"))
				{
					if ((deExcel.getTextoCaracter()==null || !deExcel.getTextoCaracter().equalsIgnoreCase("valor de referencia")))
						listaErroresPersistir.add("El campo carácter no es Valor de Referencia en la fila: "+(indiceFilaExcel) + 
								". Esa determinación no puede ser Valor de Referencia del padre.");
					else if (!servicioBasicoDeterminaciones.crearValorRefenciaDeterminacionPadre(determinacion, determinacion.getDeterminacionByIdpadre().getIden())){
						listaErroresPersistir.add("La Opcion Determinación no se ha podido guardar en la fila: "+(indiceFilaExcel));
					}
				}
        		// --------------------
        		// Persistimos las relaciones
        		// --------------------
        		if (deExcel.getTextoRegulacion1()!=null && !deExcel.getTextoRegulacion1().equals(""))
        		{
        			RegulacionEspecificaDTO regEspec = new RegulacionEspecificaDTO();
        			regEspec.setNombre(deExcel.getNombreRegulacion1());
        			regEspec.setTexto(deExcel.getTextoRegulacion1());
        			regEspec.setOrden(1);
        			servicioBasicoDeterminaciones.crearRegulacionespecifica (regEspec, id);
        		}
        		
        		if (deExcel.getTextoRegulacion2()!=null && !deExcel.getTextoRegulacion2().equals(""))
        		{
        			RegulacionEspecificaDTO regEspec = new RegulacionEspecificaDTO();
        			regEspec.setNombre(deExcel.getNombreRegulacion2());
        			regEspec.setTexto(deExcel.getTextoRegulacion2());
        			regEspec.setOrden(2);
        			servicioBasicoDeterminaciones.crearRegulacionespecifica (regEspec, id);
        		}
        		
        		if (deExcel.getTextoRegulacion3()!=null && !deExcel.getTextoRegulacion3().equals(""))
        		{
        			RegulacionEspecificaDTO regEspec = new RegulacionEspecificaDTO();
        			regEspec.setNombre(deExcel.getNombreRegulacion3());
        			regEspec.setTexto(deExcel.getTextoRegulacion3());
        			regEspec.setOrden(3);
        			servicioBasicoDeterminaciones.crearRegulacionespecifica (regEspec, id);
        		}
        		
        		if (deExcel.getTextoRegulacion4()!=null && !deExcel.getTextoRegulacion4().equals(""))
        		{
        			RegulacionEspecificaDTO regEspec = new RegulacionEspecificaDTO();
        			regEspec.setNombre(deExcel.getNombreRegulacion4());
        			regEspec.setTexto(deExcel.getTextoRegulacion4());
        			regEspec.setOrden(4);
        			servicioBasicoDeterminaciones.crearRegulacionespecifica (regEspec, id);
        		}
        		
        		if (deExcel.getTextoRegulacion5()!=null && !deExcel.getTextoRegulacion5().equals(""))
        		{
        			RegulacionEspecificaDTO regEspec = new RegulacionEspecificaDTO();
        			regEspec.setNombre(deExcel.getNombreRegulacion5());
        			regEspec.setTexto(deExcel.getTextoRegulacion5());
        			regEspec.setOrden(5);
        			servicioBasicoDeterminaciones.crearRegulacionespecifica (regEspec, id);
        		}
        		
        		
        		// Creamos el grupo aplicación
        		boolean aplicadoCorrectamente = true;
        		if (deExcel.getTextoGrupoAplicacion()!=null && !deExcel.getTextoGrupoAplicacion().equals(""))
        		{
        			String[] grupos = deExcel.getTextoGrupoAplicacion().split(",");
        			for (String grupo: grupos)
        			{
        				String idDetGrupo = mapaGrupo.get(quitarTildes(grupo).trim());
        				Determinacion grupoAplicacion = null;
        				try {
        					grupoAplicacion = servicioBasicoDeterminaciones.findByBaseGrupo(deExcel.getIdTramiteBaseTrabajo(),idDetGrupo);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
        				
        				if (grupoAplicacion!=null){
        					//System.outout.println("La hemos encontrado----- " + deExcel.getIdDeterminacionBase() + "--" + idDetGrupo);
	        				Determinaciongrupoentidad detGrupoEntidad = new Determinaciongrupoentidad();
	        				detGrupoEntidad.setDeterminacionByIddeterminacion(determinacion);
	        				detGrupoEntidad.setDeterminacionByIddeterminaciongrupo(grupoAplicacion);
	        				em.persist(detGrupoEntidad);
	        				
	        				// Veamos si existe el equivalente, si está bien aplicado
	        				if (detBase!=null){
	        					if (!servicioBasicoDeterminaciones.determinacionAplicadaPorGrupo(detBase.getIden(), grupoAplicacion.getIden()))
	        						aplicadoCorrectamente = false;
	        				}
	        				
	        					
	        			
        				}
        				else
        					listaErroresPersistir.add("Hay un error en el Grupo Aplicacion indicado en la fila: "+(indiceFilaExcel));
        				
        			}
        		}
        		
        		// Comentamos esto hasta nueva orden
        		/*
        		if (deExcel.getTextoGrupoAplicacion()!=null && !deExcel.getTextoGrupoAplicacion().equals("") && 
        				detBase!=null && !aplicadoCorrectamente)
        			listaErroresPersistir.add("El equivalente indicado no está aplicado a ninguno de los grupos en la fila: "+(indiceFilaExcel));
        		*/
        		//Creamos la unidad
        		if (deExcel.getTextoUnidad()!=null && !deExcel.getTextoUnidad().equals(""))
        		{
    				Determinacion unidad = null;
    				try {
    					unidad = servicioBasicoDeterminaciones.findByNombreTramite(deExcel.getIdTramiteBaseTrabajo(),deExcel.getTextoUnidad());
    					if (unidad==null)
    						unidad = servicioBasicoDeterminaciones.findByNombreTramite(idTramite,deExcel.getTextoUnidad());
					} catch (Exception e) {
						// TODO: handle exception
						//listaErroresPersistir.add("Hay un error en el Grupo Aplicacion indicado en la fila: "+(indiceFilaExcel));
					}
    				
    				if (unidad!=null && 
    						servicioBasicoDeterminaciones.guardarUnidadDeDeterminacion(determinacion.getIden(), unidad.getIden())){
    					//System.outout.println("La hemos encontrado----- " + deExcel.getIdDeterminacionBase() + "--" + idDetGrupo);
    					
    				}
    				else
    					listaErroresPersistir.add("Hay un error en la Unidad indicada en la fila: "+(indiceFilaExcel));
        		}
        		
        	}
        	// ------------------------------------------------------------------
        	// Si ha habido algun error tengo que volver atras la transaccion
        	// ------------------------------------------------------------------
        	if (listaErroresPersistir.size()!=0)
        	{
        		contextoTransaccion.setRollbackOnly();
        		log.error("[persistirCUdeExcel] Han habido errores. Se vuelve atrás en la transacción");
        	}
    		
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		log.error("[persistirCUdeExcel] ERROR: Hago un rollback");
    		contextoTransaccion.setRollbackOnly();
    		listaErroresPersistir.add("ERROR Desconocido, se ha hecho rollback y no se ha guardado nada en BD");
    		
    	}
    	
    	
    	
    	return listaErroresPersistir;
    }
    
    public int buscarEntidadPorTramiteYClave (int idTramite, String claveEntidad)
    {
    	int idEnt=0;
    	
    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
    	String query="SELECT ent.iden " +
        " FROM es.idom.sev.cs.urbanismoenred.editorfip.data.rpm.planeamiento.Entidad ent " + 
        " WHERE ent.tramite.iden =  " +idTramite+
        " AND ent.clave = '"+claveEntidad+"'";
    	
    	try
    	{
    		idEnt = (Integer)em.createQuery(query).getSingleResult();
    	}
    	catch (Exception ex)
    	{
    		log.warn("[buscarEntidadPorTramiteYClave] Fallo al buscar  la entidad con idTramite="+idTramite+" y clave="+claveEntidad +". ERROR:"+ex.getMessage());
    		ex.printStackTrace();
    	}
    	
    	
    	
    	return idEnt;
    }
    
   
    
    private static String quitarTildesLower(String cadena)
	{
		////System.outout.println(cadena);
		if (cadena!=null)
		{
			cadena = cadena.replace ('á','a');
			cadena = cadena.replace ('é','e');
			cadena = cadena.replace ('í','i');
			cadena = cadena.replace ('ó','o');
			cadena = cadena.replace ('ú','u');
			
			return cadena.toLowerCase().trim();
		}
		else
			return "";
	}
    
    private static String quitarTildes(String cadena)
	{
		////System.outout.println(cadena);
		if (cadena!=null)
		{
			cadena = cadena.replace ('á','a');
			cadena = cadena.replace ('é','e');
			cadena = cadena.replace ('í','i');
			cadena = cadena.replace ('ó','o');
			cadena = cadena.replace ('ú','u');
			
			cadena = cadena.replace ('Á','A');
			cadena = cadena.replace ('É','E');
			cadena = cadena.replace ('Í','I');
			cadena = cadena.replace ('Ó','O');
			cadena = cadena.replace ('Ú','U');
			
			return cadena.trim();
		}
		else
			return "";
	}
    
    public static void main(String[] args) {
		
    	Long.parseLong("7000000011");
	}
    
    @Remove
	public void destroy(){};
}
