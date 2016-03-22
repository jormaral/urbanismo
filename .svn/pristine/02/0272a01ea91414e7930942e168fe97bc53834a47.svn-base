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

package com.mitc.redes.editorfip.servicios.gestionfip.validaciones;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.validacion.Resultado;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaResultadosValidacionNuevo")
public class ListaResultadosValidacionNuevoBean  implements ListaResultadosValidacionNuevo {

	
	private List<Resultado> listaResultado;
	
	@Logger private Log log;
	
	@PersistenceContext
	EntityManager em;
	
	@In (create = true)
	FacesMessages facesMessages;
	
	@In (create = true)
	ServicioGestionValidacionesNuevo servicioGestionValidacionesNuevo;
	
	
	@In
    VariablesSesionUsuario variablesSesionUsuario;

	
	/*
	public void cargarLista(int idProceso)
	{
		log.debug("[refrescarLista] Refresco la lista de Procesos");
		int idTramiteVigente = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
    	int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
    	
    	String queryListadoNormas = " select proc from Proceso proc where proc.idTramite="
    		+idTramiteVigente+" or proc.idTramite="+idTramiteEncargado+" order by proc.inicio desc";
    	listaProcesos = em.createQuery(queryListadoNormas).getResultList();
    	
    	log.info("[refrescarLista] Elementos listaProcesos="+listaProcesos.size());
	}
	
	public void refrescarLista()
	{
		log.debug("[refrescarLista] Refresco la lista de Procesos");
		int idTramiteVigente = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
    	int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
    	
    	String queryListadoNormas = " select proc from Proceso proc where proc.idTramite="
    		+idTramiteVigente+" or proc.idTramite="+idTramiteEncargado+" order by proc.inicio desc";
    	listaProcesos = em.createQuery(queryListadoNormas).getResultList();
    	
    	log.info("[refrescarLista] Elementos listaProcesos="+listaProcesos.size());
	}
	*/

	@SuppressWarnings("unchecked")
	public List<Resultado> getListaResultado(int idProceso) {
		
		
		listaResultado = em.createQuery("select res from Resultado res where res.proceso.iden="+idProceso +" order by res.exito ASC, res.validacion.tipovalidacion ASC").getResultList();
		
		log.debug("[getListaResultado] Numero de listaResultado Obtenidos= "+listaResultado.size());
		return listaResultado;
	}
	
	
	
}
