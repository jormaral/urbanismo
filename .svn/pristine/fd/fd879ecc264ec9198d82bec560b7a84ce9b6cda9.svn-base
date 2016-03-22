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
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegimenesEspecificosSimplificadosDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;

/**
 * Session Bean implementation class ImportadorCUExcel
 */
@Stateless
@Name("importadorCUExcel")
public class ImportadorCUExcelBean implements ImportadorCUExcel {

	@PersistenceContext
    private EntityManager em;
    
	@Logger 
	private Log log;
    
    Map<String, String> mapaGrupo = new HashMap<String, String>();

    
    // Creacion del contexto
    @Resource
    private SessionContext contextoTransaccion;
    
    @In(create = true, required = false)
	ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;
    
    @In(create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    public ImportadorCUExcelBean() {
		// TODO Auto-generated constructor stub
    	
    	mapaGrupo.put("amb", "7000000002");
        mapaGrupo.put("cla", "7000000003");
        mapaGrupo.put("cat", "7000000004");
        
        mapaGrupo.put("zon", "7000000011");
        mapaGrupo.put("ges", "7000000016");
        mapaGrupo.put("afe", "7000000031");
        
        mapaGrupo.put("pro", "7000000013");
        mapaGrupo.put("sis", "7000000008");
        mapaGrupo.put("res", "7000000030");
        mapaGrupo.put("acc", "7000000043");
	}
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<String> persistirCUdeExcel (int idTramite, List<CUExcelDTO> listaCuenExcel)
    {
    	List<String> listaErroresPersistir = new ArrayList<String>();
    	
    	try{
    		
    		
        	
        	CondicionUrbanisticaSimplificadaDTO cuSimplificadaAnterior=null;
        	CUExcelDTO cuExcelAnterior=null;
        	
        	// Lo inicializo a uno porque la fila en el excel empieza por la fila 3 (uno, mas uno por defecto, mas uno por grupo de aplicacion)
        	int indiceFilaExcel=1;
        	// Voy adquiriendo
        	for (CUExcelDTO cuExcel : listaCuenExcel)
        	{
        		indiceFilaExcel++;
        		
        		int resultadoPersistir=0;
        		boolean persistoCU = true;
        		CondicionUrbanisticaSimplificadaDTO cuSimplificada = new CondicionUrbanisticaSimplificadaDTO();
        		int idDeterminacion = 0;
        		
        		// --------------------
        		// Obtengo la entidad
        		// --------------------
        		
        		int idEntidad = buscarEntidadPorTramiteYClave(idTramite,cuExcel.getCodigoEntidad());
        		
        		if (idEntidad!=0)
        		{
        			cuSimplificada.setIdEntidad(idEntidad);
        			
        			String nombreEntidadBD = servicioBasicoCondicionesUrbanisticas.nombreEntidad(idEntidad);
        			// Compruebo el nombre de la entidad
        			if (cuExcel.getNombreEntidad().trim().equalsIgnoreCase(nombreEntidadBD.trim()))
        			{
        				cuSimplificada.setNombreEntidad(nombreEntidadBD);
        			}
        			else
        			{
        				// Al no ser igual el nombre hay algun error o algun problema. No persisto
        				persistoCU = false;
            			listaErroresPersistir.add("FILA "+(indiceFilaExcel+1)+": El nombre de la entidad en el EXCEL : '"+cuExcel.getNombreEntidad()+ "' no coincide con el de base de datos: '"+nombreEntidadBD+"'");

        			}
        			
        			
        		}
        		else
        		{
        			// Hay un error al coger la entidad, informo y no persisto
        			persistoCU = false;
        			listaErroresPersistir.add("FILA "+(indiceFilaExcel+1)+": No se ha encontrado la entidad con clave="+cuExcel.getCodigoEntidad()+ " para el tramite="+idTramite);
        		}
        		
        		
        		
        		
        		// ------------------------------------------------------
        		// Compruebo si la CU del excel es un Grupo de Aplicacion
        		// ------------------------------------------------------
        		
        		if (cuExcel.isGrupoaplicacion())
        		{
        			// Cuando hay un grupo de aplicacion, que ha habido un 'finentidad', por lo tanto aumentamos en uno el numero de la fila
        			indiceFilaExcel++;
        			
        			// -----------------------------------
        			// Si hay alguna anterior que no se haya persistido, la tengo que persistir antes de continuar con esta de Grupo Aplicacion
        			// -----------------------------------
        			// Tengo que comprobar si el anterior no era nulo para persistirlo
					if (cuSimplificadaAnterior!=null)
					{
						resultadoPersistir =  servicioBasicoCondicionesUrbanisticas.crearCU(cuSimplificadaAnterior);
						
						if (resultadoPersistir<=0)
						{
							if (resultadoPersistir==0)
								listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila ");
							if (resultadoPersistir==-1)
								listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
							if (resultadoPersistir==-2)
								listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
							if (resultadoPersistir<=-3)
								listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");

						}
						
						// Como ya he persistido la anterior, la pongo a null
						cuExcelAnterior = null;
						cuSimplificadaAnterior = null;
					}
        			
        			// ------------------------------------------
					// Empiezo a tratar el Grupo de Aplicacion
					// ------------------------------------------
        			
					/* ANTIGUO
        			// Como es un grupo de aplicacion, la determinacion es la de caracter grupo del Tramite
        			idDeterminacion = gestCUSimplSvc.obtenerIdDeterminacionGrupoAplicacionDeTramite(idTramite);
        			*/
					// Realmente en cuExcel.getApartadoGrupoAplicacion va el id del tramite base
					idDeterminacion = servicioBasicoCondicionesUrbanisticas.obtenerIdDeterminacionGrupoAplicacionDeTramite(Integer.parseInt(cuExcel.getApartadoGrupoAplicacion()));
        			if (idDeterminacion!=0)
        			{
        				cuSimplificada.setIdDeterminacion(idDeterminacion);
        				
        				// --------------------
        	    		// Obtengo el Grupo de Aplicacion
        	    		// --------------------
        	    		
        				String idDetGrupo = mapaGrupo.get(quitarTildesLower(cuExcel.getNombreGrupoAplicacion().trim()));
        				
        				Determinacion grupoAplicacion = null;
        				try {
        					grupoAplicacion = servicioBasicoDeterminaciones.findByBaseGrupo(Integer.parseInt(cuExcel.getApartadoGrupoAplicacion()),idDetGrupo);
						} catch (Exception e) {
							// TODO: handle exception
							//listaErroresPersistir.add("Hay un error en el Grupo Aplicacion indicado en la fila: "+(indiceFilaExcel));
						}
        				
        				if (grupoAplicacion!=null){
        					
        					int idDeterminacionGA = grupoAplicacion.getIden();
        					
        					String nombreDeterminacionGABD = servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idDeterminacionGA);
        					cuSimplificada.setIdDeterminacionValorReferencia(idDeterminacionGA);
        					cuSimplificada.setNombreDetValorRef(nombreDeterminacionGABD);
        				}
        				else
    	    			{
    	    				// Al no ser igual el nombre hay algun error o algun problema. No persisto
    	    				persistoCU = false;
    	        			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": El nombre del Grupo de Aplicacion en el EXCEL : '"+cuExcel.getNombreGrupoAplicacion()+ "' no coincide con el de base de datos: AMB,AFE,CAT,CLA,GES,PRO,RES,SIS,ZON,ACC");

    	    			}
        				
        				
        				/* ANTIGUO
        	    		int idDeterminacionGA = buscarDeterminacionPorTramiteYApartado(idTramite, cuExcel.getApartadoGrupoAplicacion());
        	    		
        	    		if (idDeterminacionGA!=0)
        	    		{
        	    			cuSimplificada.setIdDeterminacionValorReferencia(idDeterminacionGA);
        	    			
        	    			String nombreDeterminacionGABD = gestCUSimplSvc.nombreDeterminacion(idDeterminacionGA);
        	    			// Compruebo el nombre de la entidad
        	    			if (cuExcel.getNombreGrupoAplicacion().trim().equalsIgnoreCase(nombreDeterminacionGABD.trim()))
        	    			{
        	    				cuSimplificada.setNombreDetValorRef(nombreDeterminacionGABD);
        	    			}
        	    			else
        	    			{
        	    				// Al no ser igual el nombre hay algun error o algun problema. No persisto
        	    				persistoCU = false;
        	        			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": El nombre del Grupo de Aplicacion en el EXCEL : '"+cuExcel.getNombreGrupoAplicacion()+ "' no coincide con el de base de datos: '"+nombreDeterminacionGABD+"'");

        	    			}
        	    			
        	    			
        	    		}
        	    		else
        	    		{
        	    			// Hay un error al coger la determinacion, informo y no persisto
        	    			persistoCU = false;
        	    			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado la determinacion con apartado="+cuExcel.getApartadoDeterminacion()+ " para el tramite="+idTramite);
        	    		}
        	    		*/
        			}
        			else
        			{
        				// Hay un error al coger la determinacion, informo y no persisto
            			persistoCU = false;
        				listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado la determinacion Grupo de Aplicacion para el tramite seleccionado o hay mas de una determinacion Grupo de Aplicacion para ese tramite");

        			}
        			
        			
        			
        			
        			// Si no han habido problemas, persisto el Grupo de Aplicacion
        			if (persistoCU)
        			{
        				resultadoPersistir = servicioBasicoCondicionesUrbanisticas.crearCUGrupoAplicacion(cuSimplificada);
        				
        				if (resultadoPersistir==0)
						{
							listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": Ha ocurrido un error al persistir el grupo de aplicacion de Condicion Urbanistica de la fila");

						}
        			}
        			
        		}
        		else
        		{
        			// ----------------------------------------
        			// La CU no es un grupo de aplicacion
        			// -----------------------------------------
        			// Cojo los valores de determinacion, determinacionregimen, valor y regimen especifico
        			int idDeterminacionRegimen = 0;
        			int idDeterminacionValorReferencia = 0;
        			String valor ="";
        			String tituloRegEsp = "";
        			String textoRegEsp = "";
        			RegimenesEspecificosSimplificadosDTO regEspSimpl = new RegimenesEspecificosSimplificadosDTO();
        			RegimenesEspecificosSimplificadosDTO regEspSimplAnterior = new RegimenesEspecificosSimplificadosDTO();
        			List<RegimenesEspecificosSimplificadosDTO> listaRegEsp = new ArrayList<RegimenesEspecificosSimplificadosDTO>();
        			
        			// --------------------
            		// Obtengo la determinacion
            		// --------------------
            		
            		idDeterminacion = buscarDeterminacionPorTramiteYApartado(idTramite, cuExcel.getApartadoDeterminacion());
            		
            		if (idDeterminacion!=0)
            		{
            			cuSimplificada.setIdDeterminacion(idDeterminacion);
            			
            			String nombreDeterminacionBD = servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idDeterminacion);
            			// Compruebo el nombre de la entidad
            			if (cuExcel.getNombreDeterminacion().trim().equalsIgnoreCase(nombreDeterminacionBD.trim()))
            			{
            				cuSimplificada.setNombreDeterminacion(nombreDeterminacionBD);
            			}
            			else
            			{
            				// Al no ser igual el nombre hay algun error o algun problema. No persisto
            				persistoCU = false;
                			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": El nombre de la determinacion en el EXCEL : '"+cuExcel.getNombreDeterminacion()+ "' no coincide con el de base de datos: '"+nombreDeterminacionBD+"'");

            			}
            			
            			
            		}
            		else
            		{
            			// Hay un error al coger la determinacion, informo y no persisto
            			persistoCU = false;
            			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado la determinacion con apartado="+cuExcel.getApartadoDeterminacion()+ " para el tramite="+idTramite);
            		}
        			
        			// --------------------------------
            		// Obtengo la determinacion regimen
            		// --------------------------------
            		
        			if (cuExcel.getApartadoDeterminacionRegimen()!=null & !cuExcel.getApartadoDeterminacionRegimen().equalsIgnoreCase("") & !cuExcel.getApartadoDeterminacionRegimen().equalsIgnoreCase("null"))
        			{
        				idDeterminacionRegimen = buscarDeterminacionPorTramiteYApartado(idTramite, cuExcel.getApartadoDeterminacionRegimen());
        				
        				if (idDeterminacionRegimen!=0)
                		{
        					cuSimplificada.setIdDeterminacionRegimen(idDeterminacionRegimen);
                			
                			String nombreDeterminacionRegimenBD = servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idDeterminacionRegimen);
                			// Compruebo el nombre de la entidad
                			if (cuExcel.getNombreDeterminacionRegimen().trim().equalsIgnoreCase(nombreDeterminacionRegimenBD.trim()))
                			{
                				cuSimplificada.setNombreDetRegimen(nombreDeterminacionRegimenBD);
                			}
                			else
                			{
                				// Al no ser igual el nombre hay algun error o algun problema. No persisto
                				persistoCU = false;
                    			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": El nombre de la determinacion regimen en el EXCEL : '"+cuExcel.getNombreDeterminacionRegimen()+ "' no coincide con el de base de datos: '"+nombreDeterminacionRegimenBD+"'");

                			}
                		}
        				else
                		{
                			// Hay un error al coger la determinacion, informo y no persisto
                			persistoCU = false;
                			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado la determinacion regimen con apartado="+cuExcel.getApartadoDeterminacionRegimen()+ " para el tramite="+idTramite);
                		}
        				
        			}
        			
        			
        			// --------------------------------
            		// Obtengo la determinacion valor referencia
            		// --------------------------------
            		
        			if (cuExcel.getApartadoValorReferencia()!=null & !cuExcel.getApartadoValorReferencia().equalsIgnoreCase("") & !cuExcel.getApartadoValorReferencia().equalsIgnoreCase("null"))
        			{
        				idDeterminacionValorReferencia = buscarDeterminacionPorTramiteYApartado(idTramite, cuExcel.getApartadoValorReferencia());
        				
        				if (idDeterminacionValorReferencia!=0)
                		{
        					cuSimplificada.setIdDeterminacionValorReferencia(idDeterminacionValorReferencia);
                			
                			String nombreDeterminacionValorReferenciaBD = servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idDeterminacionValorReferencia);
                			// Compruebo el nombre de la determinacion
                			if (cuExcel.getNombreValor().trim().equalsIgnoreCase(nombreDeterminacionValorReferenciaBD.trim()))
                			{
                				cuSimplificada.setNombreDetValorRef(nombreDeterminacionValorReferenciaBD);
                			}
                			else
                			{
                				// Al no ser igual el nombre hay algun error o algun problema. No persisto
                				persistoCU = false;
                    			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": El nombre de la determinacion Valor de Referencia en el EXCEL : '"+cuExcel.getNombreValor()+ "' no coincide con el de base de datos: '"+nombreDeterminacionValorReferenciaBD+"'");

                			}
                		}
        				else
                		{
                			// Hay un error al coger la determinacion, informo y no persisto
                			persistoCU = false;
                			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado la determinacion Valor de Referencia con apartado="+cuExcel.getApartadoValorReferencia()+ " para el tramite="+idTramite);
                		}
        				
        			}
        			else
        			{
        				// --------------------------------
                		// Obtengo el valor (si no hay valor de referencia)
                		// --------------------------------
        				
        				if (cuExcel.getNombreValor()!=null & !cuExcel.getNombreValor().equalsIgnoreCase("") & !cuExcel.getNombreValor().equalsIgnoreCase("null"))
        				{
        					valor = cuExcel.getNombreValor();
        					cuSimplificada.setValor(valor);
        					
        				}
        			}
        			
        			// --------------------------------
            		// Obtengo el regimen especifico
            		// --------------------------------
    				
    				if ((cuExcel.getTituloRegEsp()!=null & !cuExcel.getTituloRegEsp().equalsIgnoreCase("") & !cuExcel.getTituloRegEsp().equalsIgnoreCase("null"))||(cuExcel.getTextoRegEsp()!=null & !cuExcel.getTextoRegEsp().equalsIgnoreCase("") & !cuExcel.getTextoRegEsp().equalsIgnoreCase("null")))
    				{
    					if (cuExcel.getTituloRegEsp()!=null & !cuExcel.getTituloRegEsp().equalsIgnoreCase("") & !cuExcel.getTituloRegEsp().equalsIgnoreCase("null"))
    					{
    						tituloRegEsp = cuExcel.getTituloRegEsp();
    						regEspSimpl.setNombreRegimenEspecifico(tituloRegEsp);
    					}
    					
    					if (cuExcel.getTextoRegEsp()!=null & !cuExcel.getTextoRegEsp().equalsIgnoreCase("") & !cuExcel.getTextoRegEsp().equalsIgnoreCase("null"))
    					{
    						textoRegEsp = cuExcel.getTextoRegEsp();
    						regEspSimpl.setTextoRegimenEspecifico(textoRegEsp);
    					}
    					
    					listaRegEsp.add(regEspSimpl);					
    					
    					
    					cuSimplificada.setRegimenesEspecificos(listaRegEsp);
    					
    					//------------------------------------
    					// Compruebo si es un regimen especifico de la anterior Condicion Urbanistica
    					//------------------------------------
    					
    					if (cuExcelAnterior!=null)
    					{
    						// La anterior CU tuvo Regimen Especifico, tengo que ver si esta CU es del mismo regimen
    						if (cuExcelAnterior.getNombreDeterminacion().equalsIgnoreCase(cuExcel.getNombreDeterminacion()) & cuExcelAnterior.getNombreValor().equalsIgnoreCase(cuExcel.getNombreValor()))
    						{
    							for (RegimenesEspecificosSimplificadosDTO re : cuSimplificadaAnterior.getRegimenesEspecificos())
    							{
    								listaRegEsp.add(re);
    							}
    							/*
    							// Obtengo el Regimen Especifico de la anterior cuSimplificada
    							regEspSimplAnterior = cuSimplificadaAnterior.getRegimenesEspecificos().get(0);
    							// La añado a la lista
    							listaRegEsp.add(regEspSimplAnterior);
    							*/								
    							// La añado a la Condicion Urbanistica
    							cuSimplificada.setRegimenesEspecificos(listaRegEsp);
    							
    							// Actualizo lo anterior con lo de ahora
    							cuExcelAnterior = cuExcel;
    							cuSimplificadaAnterior = cuSimplificada;
    						}
    						else
    						{
    							// Como no pertenecen al mismo regimen, tengo que persistir ambos (el anterior y el de ahora)
    							
    							resultadoPersistir = servicioBasicoCondicionesUrbanisticas.crearCU(cuSimplificadaAnterior);
    							if (resultadoPersistir<=0)
    							{
    								if (resultadoPersistir==0)
    									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
    								if (resultadoPersistir==-1)
    									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
    								if (resultadoPersistir==-2)
    									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
    								if (resultadoPersistir<=-3)
    									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");

    							}
    								
    							// Como ya he persistido la anterior, la pongo a null
    							cuExcelAnterior = null;
    							cuSimplificadaAnterior = null;
    							
    							// Pongo la anterior con esta nueva
    							cuExcelAnterior = cuExcel;
    							cuSimplificadaAnterior = cuSimplificada;
    								
    							/*
    							
    							// Persisto la cu actual si no han habido errores
    							if (persistoCU)
    							{
    								resultadoPersistir = gestCUSimplSvc.crearCU(cuSimplificada);
    								if (resultadoPersistir<=0)
        							{
        								if (resultadoPersistir==0)
        									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
        								if (resultadoPersistir==-1)
        									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
        								if (resultadoPersistir==-2)
        									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
        								if (resultadoPersistir<=-3)
        									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila: "+(indiceFilaExcel));

        							}
    							}
    							*/
    						}
    						
    					}
    					else
    					{
    						// Las meto en anterior para la siguiente CU si es de regimen, puede ser que sea del mismo
    						cuExcelAnterior = cuExcel;
    						cuSimplificadaAnterior = cuSimplificada;
    					}
    					
    				}
    				else
    				{
    					// Tengo que comprobar si el anterior no era nulo para persistirlo
    					if (cuSimplificadaAnterior!=null)
    					{
    						resultadoPersistir =  servicioBasicoCondicionesUrbanisticas.crearCU(cuSimplificadaAnterior);
    						
    						if (resultadoPersistir<=0)
							{
								if (resultadoPersistir==0)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir==-1)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir==-2)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir<=-3)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");

							}
    						
    						// Como ya he persistido la anterior, la pongo a null
    						cuExcelAnterior = null;
    						cuSimplificadaAnterior = null;
    					}
    					
    					// Persisto la cu actual si no han habido errores
    					if (persistoCU)
    					{
    						resultadoPersistir = servicioBasicoCondicionesUrbanisticas.crearCU(cuSimplificada);
    						
    						if (resultadoPersistir<=0)
							{
								if (resultadoPersistir==0)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir==-1)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir==-2)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir<=-3)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");

							}
    					}
    					
    					
    					
    				}
        			
        			
        		}
        		
        		
        	}
        	
        	// --------------------------------------------------------
        	// Si la ultima CU tiene Regimen Especifico no se persiste: Lo hago ahora
        	// --------------------------------------------------------
        	if (cuSimplificadaAnterior!=null)
			{
				int resultadoPersistir =  servicioBasicoCondicionesUrbanisticas.crearCU(cuSimplificadaAnterior);
				
				if (resultadoPersistir<=0)
				{
					if (resultadoPersistir==0)
						listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila ");
					if (resultadoPersistir==-1)
						listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
					if (resultadoPersistir==-2)
						listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
					if (resultadoPersistir<=-3)
						listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");

				}
				
				// Como ya he persistido la anterior, la pongo a null
				cuExcelAnterior = null;
				cuSimplificadaAnterior = null;
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
    		log.error("[persistirCUdeExcel] ERROR: Hago un rollback");
    		contextoTransaccion.setRollbackOnly();
    		listaErroresPersistir.add("ERROR Desconocido, se ha hecho rollback y no se ha guardado nada en BD");
    		
    		e.printStackTrace();
    	}
    	
    	
    	
    	return listaErroresPersistir;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<String> persistirCUdeExcelRus (int idTramite, List<CUExcelDTO> listaCuenExcel)
    {
    	List<String> listaErroresPersistir = new ArrayList<String>();
    	
    	try{
    		
    		
        	
        	CondicionUrbanisticaSimplificadaDTO cuSimplificadaAnterior=null;
        	CUExcelDTO cuExcelAnterior=null;
        	
        	// Lo inicializo a uno porque la fila en el excel empieza por la fila 3 (uno, mas uno por defecto, mas uno por grupo de aplicacion)
        	int indiceFilaExcel=1;
        	// Voy adquiriendo
        	for (CUExcelDTO cuExcel : listaCuenExcel)
        	{
        		indiceFilaExcel++;
        		
        		int resultadoPersistir=0;
        		boolean persistoCU = true;
        		CondicionUrbanisticaSimplificadaDTO cuSimplificada = new CondicionUrbanisticaSimplificadaDTO();
        		int idDeterminacion = 0;
        		
        		// --------------------
        		// Obtengo la entidad
        		// --------------------
        		
        		int idEntidad = buscarEntidadPorTramiteYClave(idTramite,cuExcel.getCodigoEntidad());
        		
        		if (idEntidad!=0)
        		{
        			cuSimplificada.setIdEntidad(idEntidad);
        			
        			String nombreEntidadBD = servicioBasicoCondicionesUrbanisticas.nombreEntidad(idEntidad);
        			// Compruebo el nombre de la entidad
        			if (cuExcel.getNombreEntidad().trim().equalsIgnoreCase(nombreEntidadBD.trim()))
        			{
        				cuSimplificada.setNombreEntidad(nombreEntidadBD);
        			}
        			else
        			{
        				// Al no ser igual el nombre hay algun error o algun problema. No persisto
        				persistoCU = false;
            			listaErroresPersistir.add("FILA "+(indiceFilaExcel+1)+": El nombre de la entidad en el EXCEL : '"+cuExcel.getNombreEntidad()+ "' no coincide con el de base de datos: '"+nombreEntidadBD+"'");

        			}
        			
        			
        		}
        		else
        		{
        			// Hay un error al coger la entidad, informo y no persisto
        			persistoCU = false;
        			listaErroresPersistir.add("FILA "+(indiceFilaExcel+1)+": No se ha encontrado la entidad con clave="+cuExcel.getCodigoEntidad()+ " para el tramite="+idTramite);
        		}
        		
        		
        		
        		
        		// ------------------------------------------------------
        		// Compruebo si la CU del excel es un Grupo de Aplicacion
        		// ------------------------------------------------------
        		
        		if (cuExcel.isGrupoaplicacion())
        		{
        			// Cuando hay un grupo de aplicacion, que ha habido un 'finentidad', por lo tanto aumentamos en uno el numero de la fila
        			indiceFilaExcel++;
        			
        			// -----------------------------------
        			// Si hay alguna anterior que no se haya persistido, la tengo que persistir antes de continuar con esta de Grupo Aplicacion
        			// -----------------------------------
        			// Tengo que comprobar si el anterior no era nulo para persistirlo
					if (cuSimplificadaAnterior!=null)
					{
						resultadoPersistir =  servicioBasicoCondicionesUrbanisticas.crearCU(cuSimplificadaAnterior);
						
						if (resultadoPersistir<=0)
						{
							if (resultadoPersistir==0)
								listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila ");
							if (resultadoPersistir==-1)
								listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
							if (resultadoPersistir==-2)
								listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
							if (resultadoPersistir<=-3)
								listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");

						}
						
						// Como ya he persistido la anterior, la pongo a null
						cuExcelAnterior = null;
						cuSimplificadaAnterior = null;
					}
        			
        			// ------------------------------------------
					// Empiezo a tratar el Grupo de Aplicacion
					// ------------------------------------------
        			
					/* ANTIGUO
        			// Como es un grupo de aplicacion, la determinacion es la de caracter grupo del Tramite
        			idDeterminacion = gestCUSimplSvc.obtenerIdDeterminacionGrupoAplicacionDeTramite(idTramite);
        			*/
					// Realmente en cuExcel.getApartadoGrupoAplicacion va el id del tramite base
					idDeterminacion = servicioBasicoCondicionesUrbanisticas.obtenerIdDeterminacionGrupoAplicacionDeTramite(Integer.parseInt(cuExcel.getApartadoGrupoAplicacion()));
        			if (idDeterminacion!=0)
        			{
        				cuSimplificada.setIdDeterminacion(idDeterminacion);
        				
        				// --------------------
        	    		// Obtengo el Grupo de Aplicacion
        	    		// --------------------
        	    		
        				String idDetGrupo = mapaGrupo.get(quitarTildesLower(cuExcel.getNombreGrupoAplicacion().trim()));
        				
        				
        				if (cuExcel.getDetGrupoAplicacion()!=null){
        					
        					int idDeterminacionGA = cuExcel.getDetGrupoAplicacion().getIden();
        					
        					String nombreDeterminacionGABD = servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idDeterminacionGA);
        					cuSimplificada.setIdDeterminacionValorReferencia(idDeterminacionGA);
        					cuSimplificada.setNombreDetValorRef(nombreDeterminacionGABD);
        				}
        				else
    	    			{
    	    				// Al no ser igual el nombre hay algun error o algun problema. No persisto
    	    				persistoCU = false;
    	        			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": El nombre del Grupo de Aplicacion en el EXCEL : '"+cuExcel.getNombreGrupoAplicacion()+ "' no coincide con el de base de datos: AMB,AFE,CAT,CLA,GES,PRO,RES,SIS,ZON,ACC");

    	    			}
        				
        				
        				/* ANTIGUO
        	    		int idDeterminacionGA = buscarDeterminacionPorTramiteYApartado(idTramite, cuExcel.getApartadoGrupoAplicacion());
        	    		
        	    		if (idDeterminacionGA!=0)
        	    		{
        	    			cuSimplificada.setIdDeterminacionValorReferencia(idDeterminacionGA);
        	    			
        	    			String nombreDeterminacionGABD = gestCUSimplSvc.nombreDeterminacion(idDeterminacionGA);
        	    			// Compruebo el nombre de la entidad
        	    			if (cuExcel.getNombreGrupoAplicacion().trim().equalsIgnoreCase(nombreDeterminacionGABD.trim()))
        	    			{
        	    				cuSimplificada.setNombreDetValorRef(nombreDeterminacionGABD);
        	    			}
        	    			else
        	    			{
        	    				// Al no ser igual el nombre hay algun error o algun problema. No persisto
        	    				persistoCU = false;
        	        			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": El nombre del Grupo de Aplicacion en el EXCEL : '"+cuExcel.getNombreGrupoAplicacion()+ "' no coincide con el de base de datos: '"+nombreDeterminacionGABD+"'");

        	    			}
        	    			
        	    			
        	    		}
        	    		else
        	    		{
        	    			// Hay un error al coger la determinacion, informo y no persisto
        	    			persistoCU = false;
        	    			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado la determinacion con apartado="+cuExcel.getApartadoDeterminacion()+ " para el tramite="+idTramite);
        	    		}
        	    		*/
        			}
        			else
        			{
        				// Hay un error al coger la determinacion, informo y no persisto
            			persistoCU = false;
        				listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado la determinacion Grupo de Aplicacion para el tramite seleccionado o hay mas de una determinacion Grupo de Aplicacion para ese tramite");

        			}
        			
        			
        			
        			
        			// Si no han habido problemas, persisto el Grupo de Aplicacion
        			if (persistoCU)
        			{
        				resultadoPersistir = servicioBasicoCondicionesUrbanisticas.crearCUGrupoAplicacion(cuSimplificada);
        				
        				if (resultadoPersistir==0)
						{
							listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": Ha ocurrido un error al persistir el grupo de aplicacion de Condicion Urbanistica de la fila");

						}
        			}
        			
        		}
        		else
        		{
        			// ----------------------------------------
        			// La CU no es un grupo de aplicacion
        			// -----------------------------------------
        			// Cojo los valores de determinacion, determinacionregimen, valor y regimen especifico
        			int idDeterminacionRegimen = 0;
        			int idDeterminacionValorReferencia = 0;
        			String valor ="";
        			String tituloRegEsp = "";
        			String textoRegEsp = "";
        			RegimenesEspecificosSimplificadosDTO regEspSimpl = new RegimenesEspecificosSimplificadosDTO();
        			RegimenesEspecificosSimplificadosDTO regEspSimplAnterior = new RegimenesEspecificosSimplificadosDTO();
        			List<RegimenesEspecificosSimplificadosDTO> listaRegEsp = new ArrayList<RegimenesEspecificosSimplificadosDTO>();
        			
        			// --------------------
            		// Obtengo la determinacion
            		// --------------------
            		
            		idDeterminacion = buscarDeterminacionPorTramiteYApartado(idTramite, cuExcel.getApartadoDeterminacion());
            		
            		if (idDeterminacion!=0)
            		{
            			cuSimplificada.setIdDeterminacion(idDeterminacion);
            			
            			String nombreDeterminacionBD = servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idDeterminacion);
            			// Compruebo el nombre de la entidad
            			if (cuExcel.getNombreDeterminacion().trim().equalsIgnoreCase(nombreDeterminacionBD.trim()))
            			{
            				cuSimplificada.setNombreDeterminacion(nombreDeterminacionBD);
            			}
            			else
            			{
            				// Al no ser igual el nombre hay algun error o algun problema. No persisto
            				persistoCU = false;
                			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": El nombre de la determinacion en el EXCEL : '"+cuExcel.getNombreDeterminacion()+ "' no coincide con el de base de datos: '"+nombreDeterminacionBD+"'");

            			}
            			
            			
            		}
            		else
            		{
            			// Hay un error al coger la determinacion, informo y no persisto
            			persistoCU = false;
            			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado la determinacion con apartado="+cuExcel.getApartadoDeterminacion()+ " para el tramite="+idTramite);
            		}
        			
        			// --------------------------------
            		// Obtengo la determinacion regimen
            		// --------------------------------
            		
        			if (cuExcel.getApartadoDeterminacionRegimen()!=null & !cuExcel.getApartadoDeterminacionRegimen().equalsIgnoreCase("") & !cuExcel.getApartadoDeterminacionRegimen().equalsIgnoreCase("null"))
        			{
        				idDeterminacionRegimen = buscarDeterminacionPorTramiteYApartado(idTramite, cuExcel.getApartadoDeterminacionRegimen());
        				
        				if (idDeterminacionRegimen!=0)
                		{
        					cuSimplificada.setIdDeterminacionRegimen(idDeterminacionRegimen);
                			
                			String nombreDeterminacionRegimenBD = servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idDeterminacionRegimen);
                			// Compruebo el nombre de la entidad
                			if (cuExcel.getNombreDeterminacionRegimen().trim().equalsIgnoreCase(nombreDeterminacionRegimenBD.trim()))
                			{
                				cuSimplificada.setNombreDetRegimen(nombreDeterminacionRegimenBD);
                			}
                			else
                			{
                				// Al no ser igual el nombre hay algun error o algun problema. No persisto
                				persistoCU = false;
                    			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": El nombre de la determinacion regimen en el EXCEL : '"+cuExcel.getNombreDeterminacionRegimen()+ "' no coincide con el de base de datos: '"+nombreDeterminacionRegimenBD+"'");

                			}
                		}
        				else
                		{
                			// Hay un error al coger la determinacion, informo y no persisto
                			persistoCU = false;
                			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado la determinacion regimen con apartado="+cuExcel.getApartadoDeterminacionRegimen()+ " para el tramite="+idTramite);
                		}
        				
        			}
        			
        			
        			// --------------------------------
            		// Obtengo la determinacion valor referencia
            		// --------------------------------
            		
        			if (cuExcel.getApartadoValorReferencia()!=null & !cuExcel.getApartadoValorReferencia().equalsIgnoreCase("") & !cuExcel.getApartadoValorReferencia().equalsIgnoreCase("null"))
        			{
        				idDeterminacionValorReferencia = buscarDeterminacionPorTramiteYApartado(idTramite, cuExcel.getApartadoValorReferencia());
        				
        				if (idDeterminacionValorReferencia!=0)
                		{
        					cuSimplificada.setIdDeterminacionValorReferencia(idDeterminacionValorReferencia);
                			
                			String nombreDeterminacionValorReferenciaBD = servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idDeterminacionValorReferencia);
                			// Compruebo el nombre de la determinacion
                			if (cuExcel.getNombreValor().trim().equalsIgnoreCase(nombreDeterminacionValorReferenciaBD.trim()))
                			{
                				cuSimplificada.setNombreDetValorRef(nombreDeterminacionValorReferenciaBD);
                			}
                			else
                			{
                				// Al no ser igual el nombre hay algun error o algun problema. No persisto
                				persistoCU = false;
                    			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": El nombre de la determinacion Valor de Referencia en el EXCEL : '"+cuExcel.getNombreValor()+ "' no coincide con el de base de datos: '"+nombreDeterminacionValorReferenciaBD+"'");

                			}
                		}
        				else
                		{
                			// Hay un error al coger la determinacion, informo y no persisto
                			persistoCU = false;
                			listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado la determinacion Valor de Referencia con apartado="+cuExcel.getApartadoValorReferencia()+ " para el tramite="+idTramite);
                		}
        				
        			}
        			else
        			{
        				// --------------------------------
                		// Obtengo el valor (si no hay valor de referencia)
                		// --------------------------------
        				
        				if (cuExcel.getNombreValor()!=null & !cuExcel.getNombreValor().equalsIgnoreCase("") & !cuExcel.getNombreValor().equalsIgnoreCase("null"))
        				{
        					valor = cuExcel.getNombreValor();
        					cuSimplificada.setValor(valor);
        					
        				}
        			}
        			
        			// --------------------------------
            		// Obtengo el regimen especifico
            		// --------------------------------
    				
    				if ((cuExcel.getTituloRegEsp()!=null & !cuExcel.getTituloRegEsp().equalsIgnoreCase("") & !cuExcel.getTituloRegEsp().equalsIgnoreCase("null"))||(cuExcel.getTextoRegEsp()!=null & !cuExcel.getTextoRegEsp().equalsIgnoreCase("") & !cuExcel.getTextoRegEsp().equalsIgnoreCase("null")))
    				{
    					if (cuExcel.getTituloRegEsp()!=null & !cuExcel.getTituloRegEsp().equalsIgnoreCase("") & !cuExcel.getTituloRegEsp().equalsIgnoreCase("null"))
    					{
    						tituloRegEsp = cuExcel.getTituloRegEsp();
    						regEspSimpl.setNombreRegimenEspecifico(tituloRegEsp);
    					}
    					
    					if (cuExcel.getTextoRegEsp()!=null & !cuExcel.getTextoRegEsp().equalsIgnoreCase("") & !cuExcel.getTextoRegEsp().equalsIgnoreCase("null"))
    					{
    						textoRegEsp = cuExcel.getTextoRegEsp();
    						regEspSimpl.setTextoRegimenEspecifico(textoRegEsp);
    					}
    					
    					listaRegEsp.add(regEspSimpl);					
    					
    					
    					cuSimplificada.setRegimenesEspecificos(listaRegEsp);
    					
    					//------------------------------------
    					// Compruebo si es un regimen especifico de la anterior Condicion Urbanistica
    					//------------------------------------
    					
    					if (cuExcelAnterior!=null)
    					{
    						// La anterior CU tuvo Regimen Especifico, tengo que ver si esta CU es del mismo regimen
    						if (cuExcelAnterior.getNombreDeterminacion().equalsIgnoreCase(cuExcel.getNombreDeterminacion()) & cuExcelAnterior.getNombreValor().equalsIgnoreCase(cuExcel.getNombreValor()))
    						{
    							for (RegimenesEspecificosSimplificadosDTO re : cuSimplificadaAnterior.getRegimenesEspecificos())
    							{
    								listaRegEsp.add(re);
    							}
    							/*
    							// Obtengo el Regimen Especifico de la anterior cuSimplificada
    							regEspSimplAnterior = cuSimplificadaAnterior.getRegimenesEspecificos().get(0);
    							// La añado a la lista
    							listaRegEsp.add(regEspSimplAnterior);
    							*/								
    							// La añado a la Condicion Urbanistica
    							cuSimplificada.setRegimenesEspecificos(listaRegEsp);
    							
    							// Actualizo lo anterior con lo de ahora
    							cuExcelAnterior = cuExcel;
    							cuSimplificadaAnterior = cuSimplificada;
    						}
    						else
    						{
    							// Como no pertenecen al mismo regimen, tengo que persistir ambos (el anterior y el de ahora)
    							
    							resultadoPersistir = servicioBasicoCondicionesUrbanisticas.crearCU(cuSimplificadaAnterior);
    							if (resultadoPersistir<=0)
    							{
    								if (resultadoPersistir==0)
    									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
    								if (resultadoPersistir==-1)
    									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
    								if (resultadoPersistir==-2)
    									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
    								if (resultadoPersistir<=-3)
    									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");

    							}
    								
    							// Como ya he persistido la anterior, la pongo a null
    							cuExcelAnterior = null;
    							cuSimplificadaAnterior = null;
    							
    							// Pongo la anterior con esta nueva
    							cuExcelAnterior = cuExcel;
    							cuSimplificadaAnterior = cuSimplificada;
    								
    							/*
    							
    							// Persisto la cu actual si no han habido errores
    							if (persistoCU)
    							{
    								resultadoPersistir = gestCUSimplSvc.crearCU(cuSimplificada);
    								if (resultadoPersistir<=0)
        							{
        								if (resultadoPersistir==0)
        									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
        								if (resultadoPersistir==-1)
        									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
        								if (resultadoPersistir==-2)
        									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
        								if (resultadoPersistir<=-3)
        									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila: "+(indiceFilaExcel));

        							}
    							}
    							*/
    						}
    						
    					}
    					else
    					{
    						// Las meto en anterior para la siguiente CU si es de regimen, puede ser que sea del mismo
    						cuExcelAnterior = cuExcel;
    						cuSimplificadaAnterior = cuSimplificada;
    					}
    					
    				}
    				else
    				{
    					// Tengo que comprobar si el anterior no era nulo para persistirlo
    					if (cuSimplificadaAnterior!=null)
    					{
    						resultadoPersistir =  servicioBasicoCondicionesUrbanisticas.crearCU(cuSimplificadaAnterior);
    						
    						if (resultadoPersistir<=0)
							{
								if (resultadoPersistir==0)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir==-1)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir==-2)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir<=-3)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");

							}
    						
    						// Como ya he persistido la anterior, la pongo a null
    						cuExcelAnterior = null;
    						cuSimplificadaAnterior = null;
    					}
    					
    					// Persisto la cu actual si no han habido errores
    					if (persistoCU)
    					{
    						resultadoPersistir = servicioBasicoCondicionesUrbanisticas.crearCU(cuSimplificada);
    						
    						if (resultadoPersistir<=0)
							{
								if (resultadoPersistir==0)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir==-1)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir==-2)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								if (resultadoPersistir<=-3)
									listaErroresPersistir.add("FILA "+(indiceFilaExcel)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");

							}
    					}
    					
    					
    					
    				}
        			
        			
        		}
        		
        		
        	}
        	
        	// --------------------------------------------------------
        	// Si la ultima CU tiene Regimen Especifico no se persiste: Lo hago ahora
        	// --------------------------------------------------------
        	if (cuSimplificadaAnterior!=null)
			{
				int resultadoPersistir =  servicioBasicoCondicionesUrbanisticas.crearCU(cuSimplificadaAnterior);
				
				if (resultadoPersistir<=0)
				{
					if (resultadoPersistir==0)
						listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila ");
					if (resultadoPersistir==-1)
						listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
					if (resultadoPersistir==-2)
						listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
					if (resultadoPersistir<=-3)
						listaErroresPersistir.add("FILA "+(indiceFilaExcel-1)+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");

				}
				
				// Como ya he persistido la anterior, la pongo a null
				cuExcelAnterior = null;
				cuSimplificadaAnterior = null;
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
    		log.error("[persistirCUdeExcel] ERROR: Hago un rollback");
    		contextoTransaccion.setRollbackOnly();
    		listaErroresPersistir.add("ERROR Desconocido, se ha hecho rollback y no se ha guardado nada en BD");
    		
    		e.printStackTrace();
    	}
    	
    	
    	
    	return listaErroresPersistir;
    }
    
    public int buscarEntidadPorTramiteYClave (int idTramite, String claveEntidad)
    {
    	int idEnt=0;
    	
    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
    	String query="SELECT ent.iden " +
        " FROM com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad ent " + 
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
    
    public int buscarDeterminacionPorTramiteYApartado (int idTramite, String apartado)
    {
    	int idDet=0;
    	
    	String[] numApartados = apartado.split("\\.");
		 
		String queryNueva="";
		
		// Construyo la query para que rescate la determinacion a partir del apartado
		for (int i=(numApartados.length-1); i>-1; i--)
		{
			if (i==0)
			{
				queryNueva+= " SELECT d.iden " +
		         " FROM com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion d " +
		         " WHERE d.determinacionByIdpadre IS NULL AND d.tramite.iden = "+idTramite+
		         " AND d.orden = '"+numApartados[0]+"'";
				
				for (int j=0; j<numApartados.length; j++)
				{
					queryNueva+= ")";
				}
			}
			else
			{
				queryNueva+=" SELECT d"+i+".iden " +
		         " FROM com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion d"+i+" " +
		         " WHERE  d"+i+".tramite.iden = "+idTramite+" AND d"+i+".orden = '"+numApartados[i]+"' " +
		         " AND d"+i+".determinacionByIdpadre = (";
			}
		}
    	
		// Lanzo la query para que encuentre la determinacion
    	try
    	{
    		idDet = (Integer)em.createQuery(queryNueva).getSingleResult();
    	}
    	catch (Exception ex)
    	{
    		log.warn("[buscarDeterminacionPorTramiteYApartado] Fallo al buscar  la determinacion con idTramite="+idTramite+" y apartado="+apartado +". ERROR:"+ex.getMessage());
    		ex.printStackTrace();
    	}
    	
    	
    	
    	return idDet;
    }
    
    @Remove
	public void destroy(){};

}
