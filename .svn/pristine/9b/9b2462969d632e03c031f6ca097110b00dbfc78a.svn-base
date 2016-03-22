/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.ValorReferenciaTramiteDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.JsfUtil;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;

@Stateless
@Name("listaValoresReferencia")
public class ListaValoresReferenciaBean extends SortableList implements
		ListaValoresReferencia {

	@Logger
	private Log log;

	@In(create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;

	@In
	GestionDeterminaciones gestionDeterminaciones;

	
	private List<ValorReferenciaTramiteDTO> valoresReferencia = null;
	private static final String nombreColumnName = "nombre";
	private static final String ordenColumnName = "orden";
	
	private Integer idDeterminacionOLD=0;
	
	private boolean listaRecargada = false;

	public ListaValoresReferenciaBean() {
		super(nombreColumnName);
		
	}

	@Override
	public boolean isDefaultAscending(String nombreColumnName) {
		
		return true;
	}

	
	@Override
	public void sort() {
		
		log.debug("[sort] Inicio");
		
		final Collator comparador = Collator.getInstance();
		comparador.setStrength(Collator.PRIMARY);
		
		

        Comparator<Object> comparator = new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
            	ValorReferenciaTramiteDTO d1 = (ValorReferenciaTramiteDTO) o1;
            	ValorReferenciaTramiteDTO d2 = (ValorReferenciaTramiteDTO) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(nombreColumnName)) {
                	return ascending ? comparador.compare(d1.getNombreDeterminacion(), d2.getNombreDeterminacion()) : comparador.compare(d2.getNombreDeterminacion(), d1.getNombreDeterminacion());
                } else {
                	if (sortColumnName.equals(ordenColumnName)) {
                    	return ascending ? comparador.compare(d1.getOrdenArbol(), d2.getOrdenArbol()) : comparador.compare(d2.getOrdenArbol(), d1.getOrdenArbol());
                    } else {
                        return 0;
                    }
                }
            }
        };
        Collections.sort(valoresReferencia, comparator);
        log.debug("[sort] Fin");

	}
	

	@Override
	public String borrarValorReferenciaDeterminacion(int idValorReferencia) {
		

		log.debug("[borrarValorReferenciaDeterminacion] idValorReferencia="+idValorReferencia );
		
		String resultado = "error";
		int detTrabajo = gestionDeterminaciones.getIdDeterminacion();
			
		try
		{
			boolean res = servicioBasicoDeterminaciones.borrarRelacionDeterminacionConValorReferencia(detTrabajo, idValorReferencia);
			listaRecargada = false;
			
			if (res)
			{

				resultado = "success";
				JsfUtil.addSuccessMessage("Borrada correctamente el valor de referencia");
				log.debug("[borrarValorReferenciaDeterminacion] Borrada ok");
				
			}
			else
			{
				JsfUtil.addErrorMessage("No se puede borrar el valor de referencia. Probablemente porque este asignado como Condicion Urbanistica. Debe borrar antes la Condicion Urbanistica");
				log.debug("[borrarValorReferenciaDeterminacion] Borrada fallo");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		
		
		return resultado;
	}

	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}
	

	
	public String getOrdenColumnName() {
		return ordenColumnName;
	}
	
	public int tamanoValoresReferencia()
	{
		return valoresReferencia.size();
	}

	@Override
	public List<ValorReferenciaTramiteDTO> getValoresReferencia() {
		int detTrabajo = gestionDeterminaciones.getIdDeterminacion();
		
		if(valoresReferencia == null)
			listaRecargada = false;
		
		if (detTrabajo!=0)
		{
			//log.debug("[getValoresReferencia] detTrabajo="+detTrabajo +" listaRecargada="+listaRecargada +" idDeterminacionOLD="+idDeterminacionOLD);
			
			if (!listaRecargada || detTrabajo!=idDeterminacionOLD)
			{
				log.debug("[getValoresReferencia] Pido los valores de Referencia");
				log.debug("[getValoresReferencia] detTrabajo!=idDeterminacionOLD: "+detTrabajo+"/"+idDeterminacionOLD);
				valoresReferencia = servicioBasicoDeterminaciones.obtenerValoresReferenciaDeterminacion(detTrabajo);
				log.debug("[getValoresReferencia] Pedido nuevo valoresReferencia="+valoresReferencia.size());
				listaRecargada = true;
				idDeterminacionOLD = detTrabajo;
			}
			
			
			if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
				  log.debug("[getValoresReferencia] sort()");
		          sort();
		          oldSort = sortColumnName;
		          oldAscending = ascending;
		    }
		}
		else
		{
			log.debug("[getValoresReferencia] detTrabajo=0");
			// No se ha seleccionado todavia ninguna determinacion
			valoresReferencia = new ArrayList();
		}
		
		log.debug("[getValoresReferencia] valoresReferencia="+valoresReferencia.size());
		return valoresReferencia;
	}
	
	public void guardarHijosComoValorReferencia(ActionEvent ae)
    {
		log.debug("[guardarHijosComoValorReferencia] Inicio");
        
		int detTrabajo = gestionDeterminaciones.getIdDeterminacion();
		log.debug("[guardarHijosComoValorReferencia] detTrabajo="+detTrabajo);
		
		if (detTrabajo!=0)
		{
			try
	        {
	            
	            
	            if (servicioBasicoDeterminaciones.guardarHijosComoValorReferencia(detTrabajo))
	            {
	            	listaRecargada = false;
	                getValoresReferencia();
	                JsfUtil.addSuccessMessage("Se han guardado los hijos como valor de referencia de la determinacion");
	            }
	            else
	            {
	                JsfUtil.addErrorMessage("No se han guardado los hijos como valor de referencia de la determinacion");
	            }
	        }
	        catch (Exception e) {
	            JsfUtil.addErrorMessage("No se han guardado los hijos como valor de referencia de la determinacion:"+detTrabajo);
	            e.printStackTrace();
	        }
		}
		else
		{
			JsfUtil.addErrorMessage("No hay determinacion seleccionada. La determinacion seleccionada es cero");
		}
        
        
        
        
        
        
        
    }
	
	@Override
	public void anadirValoresReferencia(List<ValorReferenciaTramiteDTO> valores, String nuevaPag) {
		
		List<Integer> idsValoresSel = new ArrayList<Integer>();
		
		for(ValorReferenciaTramiteDTO valorSel : valores) {
			if(valorSel.isSeleccionada() && !referenciaAnadida(valorSel.getIdDeterminacion()))
				idsValoresSel.add(valorSel.getIdDeterminacion());
		}	
		
		log.debug("[anadirValoresReferencia] guardarValoresReferencia: Numero det vr a guardar="+idsValoresSel.size());
		servicioBasicoDeterminaciones.guardarValoresReferencia(gestionDeterminaciones.getIdDeterminacion(), idsValoresSel);
		listaRecargada = false;
		gestionDeterminaciones.setMostrarPanelValRef(false);
		
		FacesManager.instance().redirect(nuevaPag);
		
	}
	
	private boolean referenciaAnadida(int idReferencia) {
		boolean anadida = false;
		for(int i=0; i<getValoresReferencia().size(); i++) {
			if(valoresReferencia.get(i).getIdDeterminacion() == idReferencia) {
				anadida = true;
				break;
			}
		}
		return anadida;
	}

	public void setListaRecargada(boolean listaRecargada) {
		this.listaRecargada = listaRecargada;
	}
	
	

}
