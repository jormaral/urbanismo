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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionoperaciones;

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

import com.icesoft.faces.component.tree.Tree;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenerico;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionArbolOperacionesDeterminacionesOperadas")
public class GestionArbolOperacionesDeterminacionesOperadasBean extends GestionArbolGenerico implements Serializable, GestionArbolOperacionesDeterminacionesOperadas
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger private Log log;
    
    @In (create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    @In (create = true, required = false)
    VariablesSesionUsuario variablesSesionUsuario;
    


    private int idTramiteTrabajoArbol = 0;
    private int idTipoTramiteTrabajo = 3;
    private int idTipoTramiteTrabajoOLD = 0;
    private int nodoSeleccionado = 0;
    private int idDeterminacionSeleccionada = 0;

    
    
    
   

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#getModel()
	 */
	public DefaultTreeModel getModel() {
		
		boolean actualizaArbol = false;
		
		// Si se ha cambiado el tipo de tramite
		if(idTipoTramiteTrabajo != idTipoTramiteTrabajoOLD) {
			
			
			// Permito actualizar la lista
			actualizaArbol = true;
			
			switch(idTipoTramiteTrabajo) {
				case 2 : idTramiteTrabajoArbol = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
						 break;
				case 3 : idTramiteTrabajoArbol = variablesSesionUsuario.getIdTramiteTrabajoActual();
						 break;										 
			}
			
			idTipoTramiteTrabajoOLD = idTipoTramiteTrabajo;
		}
		
		

		if (model == null || actualizaArbol) {
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

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#setModel(javax.swing.tree.DefaultTreeModel)
	 */
	public void setModel(DefaultTreeModel model) {
		this.model = model;
	}
   
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolDeterminaciones#menuEvent(javax.faces.event.ActionEvent)
	 */
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
	        // Pido los hijos de la entidad sobre la que se ha pulsado
	        // -------------------------------------
	        // Si el nodo tiene ya hijos, no volvemos a recargarlos
	        if (!node.children().hasMoreElements())
	        {
	        	 // Busco en BD los posibles hijos de ese nodo
	            List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones.getDeterminacionesHijasDeDet(selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos());
	        
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
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolDeterminaciones#nodeSelected(javax.faces.event.ActionEvent)
	 */
	public void nodeSelected(ActionEvent event) {
        // get employee id
        String nodoId = FacesUtils.getRequestParameter("nodoId");
        
        nodoSeleccionado = Integer.parseInt(nodoId);
        log.info("[nodeSelected] nodoId= " +nodoId);

        // find employee node by id and make it the selected node
        DefaultMutableTreeNode node = findTreeNode(nodoId);
        selectedUserObject = (ArbolGenericoObject) node.getUserObject();
        
        if (!node.isRoot())
        {
        	 // -------------------------------------
            // Pido los valores de la entidad sobre la que se ha pulsado
            // -------------------------------------
            int idDeterminacionBD = selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos();
            
            idDeterminacionSeleccionada = idDeterminacionBD;
           
            
            log.info("[nodeSelected] Se ha guardado la variable idDeterminacionSeleccionada " +idDeterminacionBD );
        	
            
    		 
            
            
            // -------------------------------------
            // Pido los hijos de la entidad sobre la que se ha pulsado
            // -------------------------------------
            // Si el nodo tiene ya hijos, no volvemos a recargarlos
            if (!node.children().hasMoreElements())
            {
            	 // Busco en BD los posibles hijos de ese nodo
                List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones.getDeterminacionesHijasDeDet(selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos());
            
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
        else
		{
			// Como es el nodo raiz, pongo la entidad de trabajo a cero
        	idDeterminacionSeleccionada = 0;
		}
      
        
        // fire effects.);
        valueChangeEffect.setFired(false);
    }
	
	
	
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolDeterminaciones#cargarArbolInicial()
	 */
	public void cargarArbolInicial() {
	        
	    	
			if(treeComponent == null)
				treeComponent = new Tree();
			
			// Si el treeComponent tiene mas de un nodo, le quito el primero
			if (treeComponent.getChildCount()>1)
			{
				log.debug("[cargarArbolInicial] Numero de child en treeComponent: "+treeComponent.getChildCount());
				log.debug("[cargarArbolInicial] Borro todos menos el primero: ");
				
				for (int i = treeComponent.getChildCount()-1; i>0;i-- )
				{
					treeComponent.getChildren().remove(i);
				}
				log.debug("[cargarArbolInicial] Despues de borrar. Numero de child en treeComponent: "+treeComponent.getChildCount());
				
			}
			
			
			// Cargo el tramite de Trabajo
			int idTramiteTrabajo = idTramiteTrabajoArbol;
			
				    	
	    	log.info("[cargarArbolInicial] Entra en esta accion con idTramiteTrabajo="+idTramiteTrabajo);  
	        if (idTramiteTrabajo!=0)
	        {
	        	try
	        	{
	        			        		
	        		List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones.getDeterminacionesRaices(idTramiteTrabajo);

	                
	                DefaultMutableTreeNode rootNode = addNode(null, "Determinaciones",
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
	        	
	        }
	        else
	        {
	        	// TODO Sacar error
	        	log.error("[cargarArbolInicial] ERROR: idTramiteTrabajo=0");
	        	DefaultMutableTreeNode rootNode = addNode(null, "No hay datos",
                        new ParIdentificadorTexto(0, "", ""));
                model = new DefaultTreeModel(rootNode);
                selectedUserObject = new ArbolGenericoObject(rootNode);
	        }
	        
	        log.info("[cargarArbolInicial] FIN");  
	        
	        
	    }
	
   
	

  
    
    public int getIdDeterminacionSeleccionada() {
		return idDeterminacionSeleccionada;
	}

	public int getIdTipoTramiteTrabajo() {
		return idTipoTramiteTrabajo;
	}

	public void setIdTipoTramiteTrabajo(int idTipoTramiteTrabajo) {
		this.idTipoTramiteTrabajo = idTipoTramiteTrabajo;
	}

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolDeterminaciones#destroy()
	 */
    @Remove
    public void destroy() {}
}
