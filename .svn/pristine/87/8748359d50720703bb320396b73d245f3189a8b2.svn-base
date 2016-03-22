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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenerico;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionArbolDeterminacionesPadre")
public class GestionArbolDeterminacionesPadreBean extends GestionArbolGenerico
		implements Serializable, GestionArbolDeterminacionesPadre {
	@Logger
	private Log log;
	
	@In 
	Map<String,String> messages;

	@In(create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;

	@In (create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
	
	@In (create = true, required = false)
	GestionDeterminaciones gestionDeterminaciones;
	
	
	private int idDeterminacionBaseSeleccionada = 0;
	
	

	

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
			// Pido los hijos de la determinacion sobre la que se ha pulsado
			// -------------------------------------
			// Si el nodo tiene ya hijos, no volvemos a recargarlos
			if (!node.children().hasMoreElements()) {
				// Busco en BD los posibles hijos de ese nodo
				List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones
						.getDeterminacionesHijasDeDet(selectedUserObject
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
			// Pido los valores de la determinacion sobre la que se ha pulsado
			// -------------------------------------
			int idDeterminacionBD = selectedUserObject.getDatosIdTextoArbolGenerico()
					.getIdBaseDatos();
			
			
			// Almaceno temporalmente la determinacion base seleccionada
			idDeterminacionBaseSeleccionada = idDeterminacionBD;
			
			

			// -------------------------------------
			// Pido los hijos de la determinacion sobre la que se ha pulsado
			// -------------------------------------
			// Si el nodo tiene ya hijos, no volvemos a recargarlos
			if (!node.children().hasMoreElements()) {
				// Busco en BD los posibles hijos de ese nodo
				List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones
						.getDeterminacionesHijasDeDet(selectedUserObject
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
		int idTramiteTrabajo = variablesSesionUsuario.getIdTramiteTrabajoActual();
		
		

		log.info("[cargarArbolInicial] Entra en esta accion con idTramiteTrabajo="+ idTramiteTrabajo);
		if (idTramiteTrabajo != 0) {
			try {

				List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones
						.getDeterminacionesRaices(idTramiteTrabajo);

				DefaultMutableTreeNode rootNode = addNode(null, "Determinaciones",
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
					messages.get("determinacion_no_tramite"),
					new ParIdentificadorTexto(0, "", ""));
			model = new DefaultTreeModel(rootNode);
			selectedUserObject = new ArbolGenericoObject(rootNode);
		}

		log.info("[cargarArbolInicial] FIN");

	}
	
	public void guardarDeterminacionBase (ActionEvent event)
	{
		if (idDeterminacionBaseSeleccionada!=0)
		{
			Determinacion detBase = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacionBaseSeleccionada);
			
			// Almaceno la determinacion base en la determinacion sobre la que se esta trabajando
			gestionDeterminaciones.getDeterminacion().setDeterminacionByIddeterminacionbase(detBase);
			
			log.info("[guardarDeterminacionBase] Almacenada la determinacion base con ID="+ detBase.getIden() +" en la determinacion de trabajo con ID="+gestionDeterminaciones.getDeterminacion().getIden());
		}
	}

	
	@Remove
	public void destroy() {
	}

}
