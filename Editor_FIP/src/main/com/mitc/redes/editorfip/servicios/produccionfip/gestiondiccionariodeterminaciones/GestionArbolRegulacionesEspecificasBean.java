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
import java.util.TreeMap;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenerico;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionArbolRegulacionesEspecificas")
public class GestionArbolRegulacionesEspecificasBean extends GestionArbolGenerico
		implements Serializable, GestionArbolRegulacionesEspecificas {
	@Logger
	private Log log;

	@In(create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;

	@In
	Map<String,String> messages;
	
	@In (create = true, required = false)
	GestionDeterminaciones gestionDeterminaciones;
	
	@In (create = true, required = false)
	GestionRegulacionEspecifica gestionRegulacionEspecifica;
	
	int idDeterminacionTrabajoOLD = -1;
	int idDeterminacionTrabajo = 0;

	
	
	

	

	public DefaultTreeModel getModel() {

		if (model == null) {

			cargarArbolInicial();
		}
		else
		{
			
			// Cargo el tramite de Trabajo
			idDeterminacionTrabajo =gestionDeterminaciones.getIdDeterminacion() ;
			
			//Recargo el arbol porque es otra determinacion
    		if (idDeterminacionTrabajoOLD!=idDeterminacionTrabajo)
    		{
    			log.debug("[getModel] Recargo el arbol porque es otra determinacion");
    			cargarArbolInicial();
    		}
			
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
			int idRegimenespecificoBD = selectedUserObject.getDatosIdTextoArbolGenerico()
			.getIdBaseDatos();
	
	
			// Meto el nodo en idRegimenespecificoBD		
			gestionRegulacionEspecifica.setIdRelacion(idRegimenespecificoBD);
			
			// Visualizo los datos en el popup
			gestionDeterminaciones.setMostrarRegulacionesEspecificas(true);
			
			Map parametros = new TreeMap<String,Object>();
			String paginaActual = FacesContext.getCurrentInstance().getViewRoot().getViewId();
			parametros.put("pageRedirect", paginaActual);
			FacesManager.instance().redirect("/produccionfip/gestiondiccionariodeterminaciones/ContenidoRegulacionesEspecificas.xhtml",
					parametros, true);
			

			log
			.info("[nodeSelected] Se ha guardado la variable idRegimenespecificoBD "
					+ idRegimenespecificoBD + " en gestionRegulacionEspecifica");

			// -------------------------------------
			// Pido los hijos de la determinacion sobre la que se ha pulsado
			// -------------------------------------
			// Si el nodo tiene ya hijos, no volvemos a recargarlos
			if (!node.children().hasMoreElements()) {
				// Busco en BD los posibles hijos de ese nodo
				List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones
						.getRegulacionespecificaHijas(selectedUserObject
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
		idDeterminacionTrabajo =gestionDeterminaciones.getIdDeterminacion() ;
		
		// Si se carga el arbol inicial, ponemos una regulacio nespecifica en blanco		
		gestionRegulacionEspecifica.nuevoRegulacionEspecifica();
		
		

		log.info("[cargarArbolInicial] Entra en esta accion con idDeterminacionTrabajo="+ idDeterminacionTrabajo);
		if (idDeterminacionTrabajo != 0) {
			
			idDeterminacionTrabajoOLD = idDeterminacionTrabajo;
			
			try {

				List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones
						.getRegulacionespecificaRaices(idDeterminacionTrabajo);
				
				if (result.size()==0)
				{
					DefaultMutableTreeNode rootNode = addNode(null,messages.get("no_hay_datos") ,
							new ParIdentificadorTexto(0, "", ""));
					model = new DefaultTreeModel(rootNode);
					selectedUserObject = (ArbolGenericoObject) rootNode
							.getUserObject();					
					selectedUserObject.setLeaf(true);
				}
				else
				{
					DefaultMutableTreeNode rootNode = addNode(null, "Regulaciones Especificas",
							new ParIdentificadorTexto(0, "", ""));
					model = new DefaultTreeModel(rootNode);
					selectedUserObject = (ArbolGenericoObject) rootNode
							.getUserObject();
					selectedUserObject.setExpanded(true);

					for (ParIdentificadorTexto r : result) {
						DefaultMutableTreeNode regionNode = addNode(rootNode, null,
								r);
					}
				}

				
				

			} catch (NumberFormatException ex) {
				// TODO Sacar error
				ex.printStackTrace();
			}

		} else {
			// TODO Sacar error
			log.error("[cargarArbolInicial] ERROR: idDeterminacionTrabajo=0");
			DefaultMutableTreeNode rootNode = addNode(null,
					messages.get("determinacion_no_det"),
					new ParIdentificadorTexto(0, "", ""));
			model = new DefaultTreeModel(rootNode);
			selectedUserObject = new ArbolGenericoObject(rootNode);
		}

		log.info("[cargarArbolInicial] FIN");

	}
	
	
	
	
	public void nodeSelected(String nodoId) {
		// get employee id
		log.info("[nodeSelected] nodoId= " + nodoId);

		// find employee node by id and make it the selected node
		DefaultMutableTreeNode node = findTreeNode(nodoId);
		selectedUserObject = (ArbolGenericoObject) node.getUserObject();

		if (!node.isRoot()) {
			// -------------------------------------
			// Pido los valores de la determinacion sobre la que se ha pulsado
			// -------------------------------------
			int idRegimenespecificoBD = selectedUserObject.getDatosIdTextoArbolGenerico()
					.getIdBaseDatos();
			
			
			// Meto el nodo en idRegimenespecificoBD		
			gestionRegulacionEspecifica.setIdRelacion(idRegimenespecificoBD);
			
			

			log
					.info("[nodeSelected] Se ha guardado la variable idRegimenespecificoBD "
							+ idRegimenespecificoBD + " en gestionRegulacionEspecifica");

			// -------------------------------------
			// Pido los hijos de la determinacion sobre la que se ha pulsado
			// -------------------------------------
			// Si el nodo tiene ya hijos, no volvemos a recargarlos
			if (!node.children().hasMoreElements()) {
				// Busco en BD los posibles hijos de ese nodo
				List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones
						.getRegulacionespecificaHijas(selectedUserObject
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
	
	
	
	
	

	
	@Remove
	public void destroy() {
	}

}
