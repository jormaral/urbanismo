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


package com.mitc.redes.editorfip.servicios.informacionfip.determinaciones;

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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.paneltabset.PanelTabSet;
import com.icesoft.faces.component.tree.Tree;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioCRUDDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenerico;
import com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones.GestionArbolDeterminaciones;
import com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones.GestionDeterminaciones;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Scope(ScopeType.SESSION)
@Stateful
@Name("servicioCopiarDeterminaciones")
public class ServicioCopiarDeterminacionesBean extends GestionArbolGenerico implements Serializable, ServicioCopiarDeterminaciones
{
    @Logger private Log log; 

    @In (create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    private List<ArbolGenericoObject> seleccionados;
    List<ParIdentificadorTexto> listaNodosEHijos = new ArrayList<ParIdentificadorTexto>();
    
    @In (create = true, required = false)
    VariablesSesionUsuario variablesSesionUsuario;
    
    @In (create = true, required = false)
    GestionArbolDeterminacionesCopiar gestionArbolDeterminacionesCopiar; 
    
    @In (create = true, required = false)
    GestionArbolDeterminaciones gestionArbolDeterminaciones; 
    
    @In (create = true, required = false)
	GestionDeterminaciones gestionDeterminaciones;

    @In (create = true, required = false)
    ServicioCRUDDeterminaciones servicioCRUDDeterminaciones;
    
    @In(create=true, required=false)
    FacesMessages facesMessages;
    
    @PersistenceContext
	EntityManager em;
    
    private int idTramiteTrabajoArbol = 0;
    private int nodoSeleccionado = 0;
    
    List<ParIdentificadorTexto> resultDeterminacionesHijas = new ArrayList<ParIdentificadorTexto>();
    
    public DefaultTreeModel getModel() {

    	if (seleccionados==null)
    		seleccionados = new ArrayList<ArbolGenericoObject>();
    	
    	if (model == null || idTramiteTrabajoArbol != variablesSesionUsuario.getIdTramiteTrabajoActual()) {

			idTramiteTrabajoArbol = variablesSesionUsuario.getIdTramiteTrabajoActual();
			
			seleccionados = new ArrayList<ArbolGenericoObject>();
			cargarArbolInicial();
			
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
	            //List<ParIdentificadorTexto> result = servicioBasicoTramites.getArbolAsignacionTramiteUsuarioHijos(selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos());
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
        log.info("[nodeSelected] nodoId= " +nodoId);

        // find employee node by id and make it the selected node
        // DefaultMutableTreeNode node = findTreeNode(nodoId);
        // selectedUserObject = (ArbolGenericoObject) node.getUserObject();
        
        FacesContext context = FacesContext.getCurrentInstance();
 		UIViewRoot uiView = context.getViewRoot();
 		
 		PanelTabSet panelt = (PanelTabSet)uiView.findComponent("formulario:panelTabArbol");
 	
 		Tree tree = (Tree)panelt.findComponent("arbolDeterminacion");
 		
 		DefaultMutableTreeNode node = tree.getCurrentNode();
 		
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
      
        
        // fire effects.);
        valueChangeEffect.setFired(false);
    }

   
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolDeterminaciones#cargarArbolInicial()
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
	        			        		
	        		List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones.getDeterminacionesRaices(idTramiteTrabajoArbol);

	                
	                DefaultMutableTreeNode rootNode = addNode(null, "Determinacion",
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
	 		
	 		//PanelTabSet panelt = (PanelTabSet)uiView.findComponent("formularioUsuario:vistaPanelTramites:panelTabArbolTram");
	 		//Tree tree = (Tree)panelt.findComponent("treeTram");
	 		
	 		PanelTabSet panelt = (PanelTabSet)uiView.findComponent("formulario:panelTabArbol");
	 	 	
	 		Tree tree = (Tree)panelt.findComponent("arbolDeterminacion");
	 		
	 		DefaultMutableTreeNode nod = tree.getCurrentNode();
	 		
	 		boolean selec = !((ArbolGenericoObject) nod.getUserObject()).isSeleccionado();
	 
	 		ArbolGenericoObject nodoArbol = (ArbolGenericoObject) nod.getUserObject();
	 		
	 		seleccionados.remove(nodoArbol);
	 		if (selec)
	    	 	seleccionados.add(nodoArbol);
	 		
	 		/*
	 		
	 		// -------------------------------------
	        // Pido los hijos del ambito sobre la que se ha pulsado
	        // -------------------------------------
	        // Si el nodo tiene ya hijos, no volvemos a recargarlos
	        if (!nod.children().hasMoreElements())
	        {
	        	 // Busco en BD los posibles hijos de ese nodo
	            List<ParIdentificadorTexto> result = servicioBasicoDeterminaciones.getDeterminacionesHijasDeDet(nodoArbol.getDatosIdTextoArbolGenerico().getIdBaseDatos());
	        
	            if (result.size() > 0)
	            {
	            	for (ParIdentificadorTexto r : result)
	                {
	                	DefaultMutableTreeNode regionNode = addNode(nod, null,r);
	        	 		
	                }
	            }
	            else
	            {
	            	 // Hago que el nodo seleccionado sea una hoja ya que no tiene hijos
	            	ArbolGenericoObject userObject = (ArbolGenericoObject) nod.getUserObject();
	                userObject.setLeaf(true);
	            }
	        }
	        
	        // Marcamos los hijos
	        marcarHijos(nod, selec);
	        
	        if(!nodoArbol.isExpanded() && selec)
	        	nodoArbol.setExpanded(true);
	        
	        context.renderResponse();
	 		
	 		
	 		*/
	 }
	 
	 
	 
	 
	 public void processMultiValueChange(ActionEvent e) {
		 	
		 	String nodoId = FacesUtils.getRequestParameter("nodoId");
	       
	        // find employee node by id and make it the selected node
	        DefaultMutableTreeNode node = findTreeNode(nodoId);
	 		
	 		boolean selec = !((ArbolGenericoObject) node.getUserObject()).isSeleccionado();
	 		
	 		log.debug("[processMultiValueChange] Nodo Seleccionado= " +nodoId+ "Seleccionado="+ selec); 
	 		
	 		// Comprobando si es hoja para marcar o desmarcar los hijos.
	 		//expandirNodo(Integer.parseInt(nodoId));
	 		//marcarHijos(node, selec);
	 		
	 		// Tendria que seleccionar a el y a todos sus hijos
	 		resultDeterminacionesHijas.clear();
	 		// Meto el primero como Nodo Padre
	 		listaNodosEHijos.add(new ParIdentificadorTexto(Integer.parseInt(nodoId),"nodopadre"));
	 		listaNodosEHijos = devolverIdDeterminacionEHijos(Integer.parseInt(nodoId));
	 		log.debug("[processMultiValueChange] listaNodosEHijos= " + listaNodosEHijos.size()); 
	 		
	 		log.debug("[processMultiValueChange] TOTAL= " + seleccionados.size()); 
	 	 
	 	}
	 
	 
	 
	 private List<ParIdentificadorTexto> devolverIdDeterminacionEHijos(int idDetPadre)
	 {
		 
		 log.debug("[devolverIdDeterminacionEHijos] Recopilar los hijos de= " + idDetPadre);
		 
		 

			String hql = "SELECT d " + "FROM Determinacion d "
					+ "WHERE d.determinacionByIdpadre.iden = " + idDetPadre + " "
					+ "ORDER BY d.orden,d.nombre  ASC";
			List<Determinacion> ldet = em.createQuery(hql).getResultList();

			for (Determinacion det : ldet) {

				String textoArbol = "";

				if (det.getApartado() != "" & det.getApartado() != null & det.getApartado() != "null") {
					textoArbol = det.getApartado() + " - " + det.getNombre();
				} else {
					textoArbol = det.getNombre();
				}

				ParIdentificadorTexto item = new ParIdentificadorTexto(det
						.getIden(), textoArbol,
						"determinacion");
				// Compruebo si es una hoja (no tiene hijos)
				item.setHoja(!tieneHija(det.getIden()));
				
				// Si tiene hijos, obtengo los nodos
				if (tieneHija(det.getIden()))
				{
					devolverIdDeterminacionEHijos(det.getIden());
				}

				resultDeterminacionesHijas.add(item);

			}

			return resultDeterminacionesHijas;
	 }
	 
	 private boolean tieneHija(int idEntidad)
	    {
	    	boolean resul = false;
	    	
	    	// Obtengo todas las entidades hijas
			String queryTodasEntidades = "SELECT d " +
	        "FROM Determinacion d " +
	        "WHERE d.determinacionByIdpadre.iden = " + idEntidad;
			
			List<Determinacion> listaTodasEntidades = em.createQuery(queryTodasEntidades).getResultList();
	    	
			if (listaTodasEntidades.size()>0)
				resul = true;
	    	
	    	
	    	return resul;
	    }
	 
	 
	public void expandirNodo(int nodo)
	{
		if (nodo!=-1){
			log.debug("EL NODO ES: " + nodo);
			if (nodo>0){
				
				Determinacion detPadre = new Determinacion();
				Determinacion determinacion = new Determinacion();
				List lista = new ArrayList<Integer>();
			
				detPadre.setIden(nodo);
				
				while (detPadre!=null){
					determinacion = servicioBasicoDeterminaciones.buscarDeterminacion(detPadre.getIden());
					lista.add(detPadre.getIden());
					detPadre = determinacion.getDeterminacionByIdpadre();
				}
				int i = lista.size();
				while (i!=0)
				{
					nodeSelected(((Integer)lista.get(i-1)).toString());
					i--;
				}
			}
		}
	}
	 
	public void nodeSelected(String nodoId) {
        // get employee id
        log.info("[nodeSelected] nodoId= " +nodoId);
        
        nodoSeleccionado = Integer.parseInt(nodoId);

        // find employee node by id and make it the selected node
        DefaultMutableTreeNode node = findTreeNode(nodoId);
        selectedUserObject = (ArbolGenericoObject) findTreeNode(nodoId).getUserObject(); 
        
        if (!node.isRoot())
        {
        	 // -------------------------------------
            // Pido los valores de la entidad sobre la que se ha pulsado
            // -------------------------------------
            int idEntidadBD = selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos();
            
            gestionDeterminaciones.setIdDeterminacion(idEntidadBD);
           
            
            log.info("[nodeSelected] Se ha guardado la variable EntidadTrabajo " +idEntidadBD +" en variablesSesionUsuario");
        	
            
    		 
            
            
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
      
        
        // fire effects.);
        valueChangeEffect.setFired(false);
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
	         
	        //desplegamos y marcamos
	        if (node.children()!=null){
	        	expandirNodo(tmp.getDatosIdTextoArbolGenerico().getIdBaseDatos());
	        	marcarHijos(node, seleccionado);
	        }
	     }
	 }


	public List<Integer> obtenerIdsTramitesSeleccionados() {
		List<Integer> listaIds = new ArrayList<Integer>();
			for(ArbolGenericoObject tramite : seleccionados) {
				
				if(tramite.getDatosIdTextoArbolGenerico().getTipo().equalsIgnoreCase("tramite")) {
					Integer idTramite = tramite.getDatosIdTextoArbolGenerico().getIdBaseDatos();
					listaIds.add(idTramite);
				}				
			}
			
		return listaIds;
	}
	
	public boolean renderizarBtnSelTramite() {
		
		boolean renderizar = false;
		if(seleccionados!=null && seleccionados.size() > 0) {
			for(ArbolGenericoObject ago : seleccionados) {
				if(ago.getDatosIdTextoArbolGenerico().getTipo().equalsIgnoreCase("determinacion")) {
					renderizar = true;
					break;
				}
			}
		}
		
		return renderizar;
	}


	public List<ArbolGenericoObject> getSeleccionados() {
		return seleccionados;
	}


	public void setSeleccionados(List<ArbolGenericoObject> seleccionados) {
		this.seleccionados = seleccionados;
	}


	public int getIdTramiteTrabajoArbol() {
		return idTramiteTrabajoArbol;
	}


	public void setIdTramiteTrabajoArbol(int idTramiteTrabajoArbol) {
		this.idTramiteTrabajoArbol = idTramiteTrabajoArbol;
	}


	public int getNodoSeleccionado() {
		return nodoSeleccionado;
	}


	public void setNodoSeleccionado(int nodoSeleccionado) {
		this.nodoSeleccionado = nodoSeleccionado;
	}  
	
	public void copiarSeleccionados(int idDeterminacion)
	{
		Tramite tramiteVigente = em.find(Tramite.class, variablesSesionUsuario.getIdTramiteVigenteTrabajo());
		Tramite tramiteEncargado = em.find(Tramite.class, variablesSesionUsuario.getIdTramiteEncargadoTrabajo());
		
		Determinacion entidadPadre = em.find(Determinacion.class, idDeterminacion);
		
		// Copio los seleccionados mediante el check
		if(seleccionados!=null && seleccionados.size() > 0) {
			for(ArbolGenericoObject ago : seleccionados) {
				Determinacion determinacion = em.find(Determinacion.class, ago.getDatosIdTextoArbolGenerico().getIdBaseDatos());
				
				Determinacion det_2 = new Determinacion();
				
				det_2.setNombre(determinacion.getNombre());
				det_2.setApartado(determinacion.getApartado());
				det_2.setEtiqueta(determinacion.getEtiqueta());
				det_2.setIdcaracter(determinacion.getIdcaracter());
				det_2.setOrden(determinacion.getOrden());
				det_2.setCodigo(servicioCRUDDeterminaciones.nextCodigo(tramiteEncargado.getIden()));
				det_2.setTramite(tramiteEncargado);
				det_2.setDeterminacionByIdpadre(entidadPadre);
				
				em.persist(det_2);
				
			}
		}
		
		// Copio los seleccionados mediante hijos
		
			if(listaNodosEHijos!=null && listaNodosEHijos.size() > 0) {
				for(ParIdentificadorTexto ago : listaNodosEHijos) {
					Determinacion determinacion = em.find(Determinacion.class, ago.getIdBaseDatos());
					
					Determinacion det_2 = new Determinacion();
					
					det_2.setNombre(determinacion.getNombre());
					det_2.setApartado(determinacion.getApartado());
					det_2.setEtiqueta(determinacion.getEtiqueta());
					det_2.setIdcaracter(determinacion.getIdcaracter());
					det_2.setOrden(determinacion.getOrden());
					det_2.setCodigo(servicioCRUDDeterminaciones.nextCodigo(tramiteEncargado.getIden()));
					det_2.setTramite(tramiteEncargado);
					
					// Si tiene escrito "nodopadre" ira al nodo correspondiente del plan encargado, si no, se mantiene en el mismo padre
					if (ago.getTexto().equalsIgnoreCase("nodopadre"))
					{
						det_2.setDeterminacionByIdpadre(entidadPadre);
					}
					else
					{
						// FGA: Esto es mal, lo tendria que poner con su padre, pero es complicado pq tendria que ver con que iden va al plan encargado
						det_2.setDeterminacionByIdpadre(entidadPadre);
					}
					
					
					em.persist(det_2);
					
				}
			}
		
		em.flush();
		

		gestionArbolDeterminaciones.setModel(null);	
		
		gestionArbolDeterminacionesCopiar.setModel(null);
		gestionArbolDeterminacionesCopiar.cargarYExpandirEntidad(idDeterminacion);
		
		facesMessages.addFromResourceBundle(Severity.INFO,"determinaciones_copiados_correctamente", null);
	}
}
