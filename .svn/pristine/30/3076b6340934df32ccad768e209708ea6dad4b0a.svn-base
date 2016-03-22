package com.mitc.redes.editorfip.servicios.mapas;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaEntidadMapa")

public class ListaEntidadMapaBean implements ListaEntidadMapa {

	@Logger
	private Log log;
	
	@In StatusMessages statusMessages;
	
	@In (create = true, required = false)
	FacesMessages facesMessages;

	@In(create = true, required = false)
	ServicioEntidadMapa servicioEntidadMapa;
	
	
	@In(create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
	
		
	private List<EntidadMapaDTO> entidadMapaList = new ArrayList<EntidadMapaDTO>();
	
	private EntidadMapaDTO entidadMapaSeleccionada;
	
	
	
	public EntidadMapaDTO getEntidadMapaSeleccionada() {
		return entidadMapaSeleccionada;
	}

	public void setEntidadMapaSeleccionada(EntidadMapaDTO entidadMapaSeleccionada) {
		this.entidadMapaSeleccionada = entidadMapaSeleccionada;
	}

	public ListaEntidadMapaBean() {
		super();
		
	}

	@Override
	public void refrescarLista()
	{
		log.debug("[refrescarLista] refrescar Lista para tramite="+variablesSesionUsuario.getIdTramiteEncargadoTrabajo()+"y codigo = 7000000011");
		entidadMapaList = servicioEntidadMapa.listadoEntidadesColorMapa(variablesSesionUsuario.getIdTramiteEncargadoTrabajo(),"7000000011"); 
		
	}

	@Override
	public List<EntidadMapaDTO> getEntidadMapaList() {
		log.debug("[getEntiadMapaList] refrescar Lista para tramite="+variablesSesionUsuario.getIdTramiteEncargadoTrabajo()+"y codigo = 7000000011");
		entidadMapaList = servicioEntidadMapa.listadoEntidadesColorMapa(variablesSesionUsuario.getIdTramiteEncargadoTrabajo(),"7000000011"); 
		
		return entidadMapaList;
	}
	
	



	@Override
	public void llamarPopUpEditarColor() {
		// TODO: Implementar la llamada al PopUp de Editar Color con la entidad seleccionada
		
		FacesManager.instance().redirect("/administracion/administracionmapa/EditarColor.xhtml");
		
		

	}

	
	public void establecerEntidadMapaSeleccionada(int identidad) {
		
		
		log.info("[establecerEntidadMapaSeleccionada] Inicio. idEntidad="+identidad);

			int i;
			boolean encontrada = false;
			entidadMapaList = this.getEntidadMapaList();
			for (i=0; i<entidadMapaList.size() & !encontrada; i++)
			{
				if (entidadMapaList.get(i).getIdentidad() == identidad)
				{
					log.info("[establecerEntidadMapaSeleccionada] Seleccionada la Entidad con Id="+entidadMapaList.get(i).getIdentidad());
					entidadMapaSeleccionada = entidadMapaList.get(i);


					// Salgo del bucle
					encontrada = true;

				}
			}

			if (!encontrada)
			{
				log.warn("[establecerCUSeleccionada] No se ha seleccionado ninguna Entidad");
				statusMessages.addFromResourceBundle(Severity.ERROR,"ATENCION: No se ha podido cargar la Entidad Seleccionada");
			}
		

		}
		
	

}
