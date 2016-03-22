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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenerico;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionArbolEntidadesBase")
public class GestionArbolEntidadesBaseBean extends GestionArbolGenerico
		implements Serializable, GestionArbolEntidadesBase {
	@Logger
	private Log log;

	@In(create = true)
	ServicioBasicoEntidades servicioBasicoEntidades;

	@In (create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
	
	@In (create = true, required = false)
	GestionEntidades gestionEntidades;
	
	
	private int idEntidadBaseSeleccionada = 0;
	
	

	

	public DefaultTreeModel getModel() {

		if (model == null) {

			
			cargarArbolInicial();
		}
		else
		{
			//log.debug("[getModel] ELSE:");
			// Si el treeComponent tiene mas de un nodo, le quito el primero
			if (treeComponent.getChildCount()>1)
			{
				log.debug("[getModel] Numero de child en treeComponent: "+treeComponent.getChildCount());
				log.debug("[getModel] Borro todos menos el primero: ");
				
				for (int i = treeComponent.getChildCount()-1; i>0;i-- )
				{
					treeComponent.getChildren().remove(i);
				}
				log.debug("[getModel] Despues de borrar. Numero de child en treeComponent: "+treeComponent.getChildCount());
				
			}
		}
		return model;
	}

	public void setModel(DefaultTreeModel model) {
		this.model = model;
	}
	
	

	 public void refrescar(ActionEvent e) {
	        
	        model=null;
	        getModel();
	    }
	 
	public void menuEvent(ActionEvent event) {

		if (treeComponent.getNavigationEventType().equals(
				treeComponent.NAVIGATION_EVENT_EXPAND)) {

			String nodoId = ((ArbolGenericoObject) treeComponent
					.getNavigatedNode().getUserObject())
					.getDatosIdTextoArbolGenerico().getIdBaseDatos()
					+ "";
			log.info("[menuEvent - NAVIGATION_EVENT_EXPAND] nodoId= " + nodoId);
			// find employee node by id and make it the selected node
			DefaultMutableTreeNode node = findTreeNode(nodoId);
			selectedUserObject = (ArbolGenericoObject) node.getUserObject();

			// -------------------------------------
			// Pido los hijos de la entidad sobre la que se ha pulsado
			// -------------------------------------
			// Si el nodo tiene ya hijos, no volvemos a recargarlos
			if (!node.children().hasMoreElements()) {
				// Busco en BD los posibles hijos de ese nodo
				List<ParIdentificadorTexto> result = servicioBasicoEntidades
						.getEntidadesHijas(selectedUserObject
								.getDatosIdTextoArbolGenerico()
								.getIdBaseDatos());

				if (result.size() > 0) {
					for (ParIdentificadorTexto r : result) {
						DefaultMutableTreeNode regionNode = addNode(node, null,
								r);
					}

					// Hago que el nodo 'padre' este expandido
					ArbolGenericoObject userObject = (ArbolGenericoObject) node
							.getUserObject();
					userObject.setExpanded(true);
				} else {
					// Hago que el nodo seleccionado sea una hoja ya que no
					// tiene hijos
					ArbolGenericoObject userObject = (ArbolGenericoObject) node
							.getUserObject();
					userObject.setLeaf(true);
				}
			}

			// fire effects.);
			valueChangeEffect.setFired(false);
		}

	}

	public void nodeSelected(ActionEvent event) {
		// get employee id
		String nodoId = FacesUtils.getRequestParameter("nodoId");
		log.info("[nodeSelected] nodoId= " + nodoId);

		// find employee node by id and make it the selected node
		DefaultMutableTreeNode node = findTreeNode(nodoId);
		selectedUserObject = (ArbolGenericoObject) node.getUserObject();

		if (!node.isRoot()) {
			// -------------------------------------
			// Pido los valores de la entidad sobre la que se ha pulsado
			// -------------------------------------
			int idEntidadBD = selectedUserObject.getDatosIdTextoArbolGenerico()
					.getIdBaseDatos();
			
			
			// Almaceno temporalmente la entidad base seleccionada
			idEntidadBaseSeleccionada = idEntidadBD;
			
			

			// -------------------------------------
			// Pido los hijos de la entidad sobre la que se ha pulsado
			// -------------------------------------
			// Si el nodo tiene ya hijos, no volvemos a recargarlos
			if (!node.children().hasMoreElements()) {
				// Busco en BD los posibles hijos de ese nodo
				List<ParIdentificadorTexto> result = servicioBasicoEntidades
						.getEntidadesHijas(selectedUserObject
								.getDatosIdTextoArbolGenerico()
								.getIdBaseDatos());

				if (result.size() > 0) {
					for (ParIdentificadorTexto r : result) {
						DefaultMutableTreeNode regionNode = addNode(node, null,
								r);
					}

					// Hago que el nodo 'padre' este expandido
					ArbolGenericoObject userObject = (ArbolGenericoObject) node
							.getUserObject();
					userObject.setExpanded(true);
				} else {
					// Hago que el nodo seleccionado sea una hoja ya que no
					// tiene hijos
					ArbolGenericoObject userObject = (ArbolGenericoObject) node
							.getUserObject();
					userObject.setLeaf(true);
				}
			}
		}

		// fire effects.);
		valueChangeEffect.setFired(false);
	}

	public void cargarArbolInicial() {

		
		// Si el treeComponent tiene mas de un nodo, le quito el primero
		if (treeComponent.getChildCount()>1)
		{
			log.debug("[cargarArbolInicial] Mas de un child en treeComponent, borro uno");
			
			treeComponent.getChildren().remove(1);
			
		}
		
		// Cargo el tramite de Trabajo
		int idTramiteTrabajo = variablesSesionUsuario.getIdTramiteBaseTrabajo();
		
		

		log.info("[cargarArbolInicial] Entra en esta accion con idTramiteTrabajo="+ idTramiteTrabajo);
		if (idTramiteTrabajo != 0) {
			try {

				List<ParIdentificadorTexto> result = servicioBasicoEntidades
						.getEntidadesRaices(idTramiteTrabajo);

				DefaultMutableTreeNode rootNode = addNode(null, "Entidades",
						new ParIdentificadorTexto(0, "", ""));
				model = new DefaultTreeModel(rootNode);
				selectedUserObject = (ArbolGenericoObject) rootNode
						.getUserObject();
				selectedUserObject.setExpanded(true);

				for (ParIdentificadorTexto r : result) {
					DefaultMutableTreeNode regionNode = addNode(rootNode, null,
							r);
				}
				
				
				

			} catch (NumberFormatException ex) {
				// TODO Sacar error
				ex.printStackTrace();
			}

		} else {
			// TODO Sacar error
			log.error("[cargarArbolInicial] ERROR: idTramiteTrabajo=0");
			DefaultMutableTreeNode rootNode = addNode(null,
					"No se ha seleccionado ning�n tr�mite",
					new ParIdentificadorTexto(0, "", ""));
			model = new DefaultTreeModel(rootNode);
			selectedUserObject = new ArbolGenericoObject(rootNode);
		}

		log.info("[cargarArbolInicial] FIN");

	}
	
	public void guardarEntidadBase (ActionEvent event)
	{
		if (idEntidadBaseSeleccionada!=0)
		{
			Entidad entBase = servicioBasicoEntidades.buscarEntidad(idEntidadBaseSeleccionada);
			
			// Almaceno la entidad base en la entidad sobre la que se esta trabajando
			gestionEntidades.getEntidad().setEntidadByIdpadre(entBase);
			
			log.info("[guardarEntidadBase] Almacenada la entidad base con ID="+ entBase.getIden() +" en la entidad de trabajo con ID="+gestionEntidades.getEntidad().getIden());
		}
	}

	
	@Remove
	public void destroy() {
	}

}