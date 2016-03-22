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


package com.mitc.redes.editorfip.servicios.administracion.gestionusuario;

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
import org.jboss.seam.security.Credentials;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.servicios.basicos.gestionfip.ServicioSeleccionTramitesTrabajo;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenerico;

@Scope(ScopeType.SESSION)
@Stateful
@Name("seleccionArbolPlanEncargado")
public class SeleccionArbolPlanEncargadoBean extends GestionArbolGenerico implements Serializable,SeleccionArbolPlanEncargado
{
    @Logger private Log log;
    
    @In
	Credentials credentials;
    
    @In(create = true)
    ServicioSeleccionTramitesTrabajo servicioSeleccionTramitesTrabajo;
    
    private String usuarioRegistradoAnterior="";
    
    private boolean mostrarPanelPopup = false;
    
    public void mostrarPanelPopupSelector(ActionEvent actionEvent) {
    	mostrarPanelPopup = true;
	}
    
    public void cerrarPanelPopupSelector(ActionEvent actionEvent) {
    	mostrarPanelPopup = false;
	}
    
    

    public boolean isMostrarPanelPopup() {
		return mostrarPanelPopup;
	}

	public void setMostrarPanelPopup(boolean mostrarPanelPopup) {
		this.mostrarPanelPopup = mostrarPanelPopup;
	}

	public DefaultTreeModel getModel() {
    	
    	
    	if (credentials.getUsername()!=null)
    	{
    		// Si no se ha establecido el modelo, o el usuario ha cambiado, tengo que recargar el arbol
    		if (model == null || !credentials.getUsername().contentEquals(usuarioRegistradoAnterior)) {
    			
    			usuarioRegistradoAnterior = credentials.getUsername();
    			log.info("[getModel] Obteniendo Modelo del Arbol del Plan Encargado para el usuario:"+credentials.getUsername());
    			cargarArbolInicial();
    		}
    	}
    	else
    	{
    		
    		log.debug("[getModel] ELSE:");
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
    		
    		// Creo un arbol inicial informando que no hay usuario
			DefaultMutableTreeNode rootNode = addNode(null, "No se ha seleccionado usuario",
                    new ParIdentificadorTexto(0, "", ""));
			model = new DefaultTreeModel(rootNode);
			selectedUserObject = (ArbolGenericoObject) rootNode.getUserObject();
            selectedUserObject.setExpanded(true);
    	}

		
		return model;
	}

	
	public void setModel(DefaultTreeModel model) {
		this.model = model;
	}
   
	
	
	public void menuEvent(ActionEvent event)
	{
		
		if (treeComponent.getNavigationEventType().equals(treeComponent.NAVIGATION_EVENT_EXPAND))
		{
			
			
			String nodoId=((ArbolGenericoObject)treeComponent.getNavigatedNode().getUserObject()).getDatosIdTextoArbolGenerico().getIdBaseDatos()+"";
			log.info("[menuEvent - NAVIGATION_EVENT_EXPAND] nodoId= " +nodoId);
			 // find employee node by id and make it the selected node
	        DefaultMutableTreeNode node = findTreeNode(nodoId);
	        selectedUserObject = (ArbolGenericoObject) node.getUserObject();
	        
	        
	        // -------------------------------------
	        // Pido los hijos del ambito sobre la que se ha pulsado
	        // -------------------------------------
	        // Si el nodo tiene ya hijos, no volvemos a recargarlos
	        if (!node.children().hasMoreElements() & node.getAllowsChildren())
	        {
	        	 // Busco en BD los posibles hijos de ese nodo
	            List<ParIdentificadorTexto> result = servicioSeleccionTramitesTrabajo.getArbolTramitesEncargadosHijosAsignadoPorUsuario(credentials.getUsername(),selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos());
	        
	            if (result.size() > 0)
	            {
	            	for (ParIdentificadorTexto r : result)
	                {
	                	DefaultMutableTreeNode regionNode = addNode(node, null,r);
	                }
	                
	                // Hago que el nodo 'padre' este expandido
	            	ArbolGenericoObject userObject = (ArbolGenericoObject) node.getUserObject();
	                userObject.setExpanded(true);
	            }
	            else
	            {
	            	 // Hago que el nodo seleccionado sea una hoja ya que no tiene hijos
	            	ArbolGenericoObject userObject = (ArbolGenericoObject) node.getUserObject();
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
        log.info("[nodeSelected] nodoId= " +nodoId);

        // find employee node by id and make it the selected node
        DefaultMutableTreeNode node = findTreeNode(nodoId);
        selectedUserObject = (ArbolGenericoObject) node.getUserObject();
        
        if (!node.isRoot())
        {
        	 // -------------------------------------
            // Pido los valores sobre la que se ha pulsado
            // -------------------------------------
            int idAmbito = selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos();
            
            
            log.info("[nodeSelected] Se ha guardado la variable idAmbito " +idAmbito );
        	
            
    		 
            
            
            // -------------------------------------
            // Pido los hijos sobre la que se ha pulsado
            // -------------------------------------
            // Si el nodo tiene ya hijos, no volvemos a recargarlos
            if (!node.children().hasMoreElements() & node.getAllowsChildren())
            {
            	 // Busco en BD los posibles hijos de ese nodo
                List<ParIdentificadorTexto> result = servicioSeleccionTramitesTrabajo.getArbolTramitesEncargadosHijosAsignadoPorUsuario(credentials.getUsername(),selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos());
            
                if (result.size() > 0)
                {
                	for (ParIdentificadorTexto r : result)
                    {
                    	DefaultMutableTreeNode regionNode = addNode(node, null,r);
                    }
                    
                    // Hago que el nodo 'padre' este expandido
                	ArbolGenericoObject userObject = (ArbolGenericoObject) node.getUserObject();
                    userObject.setExpanded(true);
                }
                else
                {
                	 // Hago que el nodo seleccionado sea una hoja ya que no tiene hijos
                	ArbolGenericoObject userObject = (ArbolGenericoObject) node.getUserObject();
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
	    	
	        	try
	        	{
	        			        		
	        		List<ParIdentificadorTexto> result = servicioSeleccionTramitesTrabajo.getArbolTramitesEncargadosRaicesAsignadoPorUsuario(credentials.getUsername());


	                
	                DefaultMutableTreeNode rootNode = addNode(null, "Ambitos",
	                        new ParIdentificadorTexto(0, "", ""));
	                model = new DefaultTreeModel(rootNode);
	                selectedUserObject = (ArbolGenericoObject) rootNode.getUserObject();
	                selectedUserObject.setExpanded(true);
	                
	                
	                for (ParIdentificadorTexto r : result)
	                {
	                	DefaultMutableTreeNode regionNode = addNode(rootNode, null,r);
	                }
	                 
	               
	        		
	        		
	        	}
	        	catch (NumberFormatException ex)
	        	{
	        		// TODO Sacar error
	        		ex.printStackTrace();
	        	}
	        	
	        
	        
	        log.info("[cargarArbolInicial] FIN");  
	        
	        
	    }
	
	 @Remove
	    public void destroy() {}
	

}
