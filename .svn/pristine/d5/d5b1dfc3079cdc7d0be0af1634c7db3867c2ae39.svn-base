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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.paneltabset.PanelTabSet;
import com.icesoft.faces.component.tree.Tree;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoAmbitos;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenerico;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionArbolAmbito")
public class GestionArbolAmbitoBean extends GestionArbolGenerico implements Serializable, GestionArbolAmbito
{
    @Logger private Log log;
    
    @In (create = true)
	ServicioBasicoAmbitos servicioBasicoAmbitos;
    
    private List<ArbolGenericoObject> seleccionados;

    public DefaultTreeModel getModel() {

		if (model == null ) {
			
			
			cargarArbolInicial();
			seleccionados = new ArrayList<ArbolGenericoObject>();
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
	        if (!node.children().hasMoreElements())
	        {
	        	 // Busco en BD los posibles hijos de ese nodo
	            List<ParIdentificadorTexto> result = servicioBasicoAmbitos.obtenerAmbitosHijos(selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos());
	        
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
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolEntidades#nodeSelected(javax.faces.event.ActionEvent)
	 */
	public void nodeSelected(ActionEvent event) {
        // get employee id
        String nodoId = FacesUtils.getRequestParameter("nodoId");
        log.info("[nodeSelected] nodoId= " +nodoId);

        // find employee node by id and make it the selected node
        //DefaultMutableTreeNode node = findTreeNode(nodoId);
        
        
        FacesContext context = FacesContext.getCurrentInstance();
 		UIViewRoot uiView = context.getViewRoot();
 		
 		PanelTabSet panelt = (PanelTabSet)uiView.findComponent("formularioUsuario:vistaPanelAmbitos:panelTabArbolAmb");
 	
 		Tree tree = (Tree)panelt.findComponent("treeAmb");
 		
 		DefaultMutableTreeNode node = tree.getCurrentNode();
 		
 		//boolean selec = !((ArbolGenericoObject) nod.getUserObject()).isSeleccionado();
 
 		//ArbolGenericoObject nodoArbol = (ArbolGenericoObject) nod.getUserObject();
        
        
        
        
        log.info("[IDENTIFICADOR DEL NODO] nodoId= " + nodoId);
        log.info("[NODO ENCONTRADO] NODO= " + node);
        
        selectedUserObject = (ArbolGenericoObject) node.getUserObject();
        if (!node.isRoot())
        {
        	 // -------------------------------------
            // Pido los valores de la entidad sobre la que se ha pulsado
            // -------------------------------------
            int idAmbito = selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos();
            
            
            log.info("[nodeSelected] Se ha guardado la variable idAmbito " +idAmbito );
        	
            // -------------------------------------
            // Pido los hijos de la entidad sobre la que se ha pulsado
            // -------------------------------------
            // Si el nodo tiene ya hijos, no volvemos a recargarlos
            if (!node.children().hasMoreElements())
            {
            	 // Busco en BD los posibles hijos de ese nodo
                List<ParIdentificadorTexto> result = servicioBasicoAmbitos.obtenerAmbitosHijos(selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos());
            
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

	public void desplegarNodo()
	{
		nodeSelected("258");
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolEntidades#nodeSelected(javax.faces.event.ActionEvent)
	 */
	public void nodeSelected(String nodoId) {
        // get employee id
        //String nodoId = FacesUtils.getRequestParameter("nodoId");
        log.info("[nodeSelected] nodoId= " +nodoId);

        // find employee node by id and make it the selected node
        //DefaultMutableTreeNode node = findTreeNode(nodoId);
        
        
        FacesContext context = FacesContext.getCurrentInstance();
 		UIViewRoot uiView = context.getViewRoot();
 		
 		PanelTabSet panelt = (PanelTabSet)uiView.findComponent("formularioUsuario:vistaPanelAmbitos:panelTabArbolAmb");
 	
 		Tree tree = (Tree)panelt.findComponent("treeAmb");
 		
 		DefaultMutableTreeNode node = findTreeNode(nodoId); 
 			tree.setCurrentNode(node);
 		
 		//boolean selec = !((ArbolGenericoObject) nod.getUserObject()).isSeleccionado();
 
 		//ArbolGenericoObject nodoArbol = (ArbolGenericoObject) nod.getUserObject();
        
        
        
        
        log.info("[IDENTIFICADOR DEL NODO] nodoId= " + nodoId);
        log.info("[NODO ENCONTRADO] NODO= " + node);
        
        selectedUserObject = (ArbolGenericoObject) node.getUserObject();
        if (!node.isRoot())
        {
        	 // -------------------------------------
            // Pido los valores de la entidad sobre la que se ha pulsado
            // -------------------------------------
            int idAmbito = selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos();
            log.info("[nodeSelected] Se ha guardado la variable idAmbito " +idAmbito );
        	
            // -------------------------------------
            // Pido los hijos de la entidad sobre la que se ha pulsado
            // -------------------------------------
            // Si el nodo tiene ya hijos, no volvemos a recargarlos
            if (!node.children().hasMoreElements())
            {
            	 // Busco en BD los posibles hijos de ese nodo
                List<ParIdentificadorTexto> result = servicioBasicoAmbitos.obtenerAmbitosHijos(selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos());
            
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
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolEntidades#cargarArbolInicial()
	 */
	public void cargarArbolInicial() {
	        
		// Si el treeComponent tiene mas de un nodo, le quito el primero
		if (treeComponent.getChildCount()>1)
		{
			log.debug("[cargarArbolInicial] Mas de un child en treeComponent, borro uno");
			
			treeComponent.getChildren().remove(1);
			
		}
		
	    	
	        	try
	        	{
	        			        		
	        		List<ParIdentificadorTexto> result = servicioBasicoAmbitos.obtenerAmbitosRaices();

	                
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
	 
	 public void processValueChange(ValueChangeEvent e) {
		 
	 	
		 	FacesContext context = FacesContext.getCurrentInstance();
	 		UIViewRoot uiView = context.getViewRoot();
	 		
	 		PanelTabSet panelt = (PanelTabSet)uiView.findComponent("formularioUsuario:vistaPanelAmbitos:panelTabArbolAmb");
	 	
	 		Tree tree = (Tree)panelt.findComponent("treeAmb");
	 		
	 		DefaultMutableTreeNode nod = tree.getCurrentNode();
	 		
	 		boolean selec = !((ArbolGenericoObject) nod.getUserObject()).isSeleccionado();
	 
	 		ArbolGenericoObject nodoArbol = (ArbolGenericoObject) nod.getUserObject();
	 		
	 		seleccionados.remove(nodoArbol);
	 		if (selec)
	    	 	seleccionados.add(nodoArbol);
	 		
	 		
	 	}
	 
	 public void processMultiValueChange(ActionEvent e) {
		 	
		 	String nodoId = FacesUtils.getRequestParameter("nodoId");
	       
	        // find employee node by id and make it the selected node
	        DefaultMutableTreeNode node = findTreeNode(nodoId);
	 		
	 		boolean selec = !((ArbolGenericoObject) node.getUserObject()).isSeleccionado();
	 		
	 		// Comprobando si es hoja para marcar o desmarcar los hijos.
	 		
	 		marcarHijos(node, selec);
	 		
	 		log.debug("TOTAL" + seleccionados.size()); 
	 	 
	 	}
	 
	 public void marcarHijos(DefaultMutableTreeNode nodoArbol, boolean seleccionado)
	 {
		if(nodoArbol == null)  return;
		
		DefaultMutableTreeNode node;
		ArbolGenericoObject tmp;
		Enumeration nodes = nodoArbol.depthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            node = (DefaultMutableTreeNode) nodes.nextElement();
            
            tmp = (ArbolGenericoObject) node.getUserObject();
            tmp.setSeleccionado(seleccionado);
            
            //log.debug("NODO A MARCAR--" + tmp.getDatosIdTextoArbolGenerico().getTexto() + "--" + seleccionado);
            seleccionados.remove(tmp);
            if (seleccionado)
    	 		seleccionados.add(tmp);
        }
		
	 }
	 
	 public boolean renderizarBtnSelTramite() {
			
			boolean renderizar = false;
			
			if(seleccionados!=null && seleccionados.size() > 0) {
				for(ArbolGenericoObject ago : seleccionados) {
					if(ago.getDatosIdTextoArbolGenerico().getTipo().equalsIgnoreCase("ambito")) {
						renderizar = true;
						break;
					}
				}
			}
			
			return renderizar;
		}
	 
 
	}
    

