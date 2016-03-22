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
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.tree.IceUserObject;
import com.icesoft.faces.component.tree.Tree;
import com.icesoft.faces.context.effects.Effect;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;

import com.mitc.redes.editorfip.servicios.genericos.JsfUtil;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionArbolCopiarCU")
public class GestionArbolCopiarCUBean  implements  GestionArbolCopiarCU
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger private Log log;
    
	@In (create = true, required = false)
	StatusMessages statusMessages;

    @In (create = true, required = false)
	ServicioBasicoEntidades servicioBasicoEntidades;
    
    @In (create = true, required = false)
    VariablesSesionUsuario variablesSesionUsuario;
    
    @In(create = true, required = false) 
    ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;
    
    private List<ArbolGenericoObject> seleccionados;
    
    private List<CondicionUrbanisticaSimplificadaDTO> condicionesSeleccionadas;
    
    private int idTramiteTrabajoArbol = 0;
    
   
    protected DefaultTreeModel model = null;
	protected ArbolGenericoObject selectedUserObject;
	protected com.icesoft.faces.component.tree.Tree treeComponent;
	 // effect that shows a value binding chance on there server
    protected Effect valueChangeEffect;
    // Del BaseBean
    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#effectChangeListener(javax.faces.event.ValueChangeEvent)
	 */
    public void effectChangeListener(ValueChangeEvent event){
        valueChangeEffect.setFired(false);
    }

    
	protected void init() {

    }

    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#getValueChangeEffect()
	 */
    public Effect getValueChangeEffect() {
        return valueChangeEffect;
    }

    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#setValueChangeEffect(com.icesoft.faces.context.effects.Effect)
	 */
    public void setValueChangeEffect(Effect valueChangeEffect) {
        this.valueChangeEffect = valueChangeEffect;
    }
    
    
	// Para los Arboles

	

	

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#getTreeComponent()
	 */
	public com.icesoft.faces.component.tree.Tree getTreeComponent() {
		return treeComponent;
	}

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#setTreeComponent(com.icesoft.faces.component.tree.Tree)
	 */
	public void setTreeComponent(
			com.icesoft.faces.component.tree.Tree treeComponent) {
		this.treeComponent = treeComponent;
	}

	

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#getSelectedUserObject()
	 */
	public ArbolGenericoObject getSelectedUserObject() {
		return selectedUserObject;
	}

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#getSelectedTreePath()
	 */
	public ArrayList getSelectedTreePath() {

		ArrayList treePath = new ArrayList();

		if (selectedUserObject != null) {
			Object[] objectPath = selectedUserObject.getWrapper()
					.getUserObjectPath();

			Object anObjectPath;
			for (int i = 0, max = objectPath.length - 1; i < max; i++) {
				anObjectPath = objectPath[i];
				IceUserObject userObject = (IceUserObject) anObjectPath;
				treePath.add(userObject.getText());
			}
		}

		return treePath;
	}
    
    public DefaultTreeModel getModel() {

		if (model == null || idTramiteTrabajoArbol != variablesSesionUsuario.getIdTramiteTrabajoActual()) {			
			
			log.debug("[getModel] cargarArbolInicial");
			idTramiteTrabajoArbol = variablesSesionUsuario.getIdTramiteTrabajoActual();
			cargarArbolInicial();
			seleccionados = new ArrayList<ArbolGenericoObject>();
			
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
	            List<ParIdentificadorTexto> result = servicioBasicoEntidades.getEntidadesHijas(selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos());
	        
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
	


	}
	
   

    

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolEntidades#nodeSelected(javax.faces.event.ActionEvent)
	 */
	public void nodeSelected(ActionEvent event) {
        // get employee id
        String nodoId = FacesUtils.getRequestParameter("nodoId");
        log.debug("[nodeSelected] nodoId= " +nodoId);
        log.debug("[nodeSelected] Sin implementar");
        
    }

   
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolEntidades#cargarArbolInicial()
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
	    	
	        	try
	        	{
	        			        		
	        		List<ParIdentificadorTexto> result = servicioBasicoEntidades.getEntidadesRaices(idTramiteTrabajo);

	                
	                DefaultMutableTreeNode rootNode = addNode(null, "Entidades",
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
	
	
	 
	 public void processValueChange(ValueChangeEvent e) {
		 
		 	log.debug("[processValueChange] inicio"); 
		 
		 	FacesContext context = FacesContext.getCurrentInstance();
	 		UIViewRoot uiView = context.getViewRoot();
	 	
	 		Tree tree = (Tree)uiView.findComponent("formularioCopiarCUEntidades:treeEntP");
	 		
	 		DefaultMutableTreeNode nod = tree.getCurrentNode();
	 		
	 		
	 		log.debug("[processValueChange] Nodo:"+nod.getUserObject()+" : "+((ArbolGenericoObject) nod.getUserObject()).isSeleccionado()); 
	 		
	 		((ArbolGenericoObject) nod.getUserObject()).setSeleccionado(!((ArbolGenericoObject) nod.getUserObject()).isSeleccionado());
	 		log.debug("[processValueChange] Lo (de)/selecciono: Nodo:"+nod.getUserObject()+" : "+((ArbolGenericoObject) nod.getUserObject()).isSeleccionado()); 
	 		
	 		boolean selec = ((ArbolGenericoObject) nod.getUserObject()).isSeleccionado();
	 		
	 
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
	            List<ParIdentificadorTexto> result = servicioBasicoEntidades.getEntidadesHijas(nodoArbol.getDatosIdTextoArbolGenerico().getIdBaseDatos());
	        
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
	        //marcarHijos(nod, selec);
	        
	        if(!nodoArbol.isExpanded() && selec)
	        	nodoArbol.setExpanded(true);
	        */
	        context.renderResponse();
	 		
	 		
	 		
	 }
	 
	 
	 
	 
	 public void processMultiValueChange(ActionEvent e) {
		 	
		 	String nodoId = FacesUtils.getRequestParameter("nodoId");
	       
	        // find employee node by id and make it the selected node
	        DefaultMutableTreeNode node = findTreeNode(nodoId);
	 		
	 		boolean selec = !((ArbolGenericoObject) node.getUserObject()).isSeleccionado();
	 		
	 		// Comprobando si es hoja para marcar o desmarcar los hijos.
	 		
	 		//marcarHijos(node, selec);
	 		
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


	public List<Integer> obtenerIdsTramitesSeleccionados() {
		List<Integer> listaIds = new ArrayList<Integer>();
			for(ArbolGenericoObject tramite : seleccionados) {
				
				if(tramite.getDatosIdTextoArbolGenerico().getTipo().equalsIgnoreCase("entidad")) {
					Integer idTramite = tramite.getDatosIdTextoArbolGenerico().getIdBaseDatos();
					listaIds.add(idTramite);
				}				
			}
			
		return listaIds;
	}
	
	public boolean renderizarBtnSelTramite() {
		
		boolean renderizar = false;
		
		if(seleccionados.size() > 0) {
			for(ArbolGenericoObject ago : seleccionados) {
				if(ago.getDatosIdTextoArbolGenerico().getTipo().equalsIgnoreCase("entidad")) {
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


	public void setCondicionesSeleccionadas(List<CondicionUrbanisticaSimplificadaDTO> condicionesSeleccionadas) {
		this.condicionesSeleccionadas = condicionesSeleccionadas;
	}


	public List<CondicionUrbanisticaSimplificadaDTO> getCondicionesSeleccionadas() {
		return condicionesSeleccionadas;
	} 
	
	public void copiarCUsSeleccionadasEnNuevaEntidad ()
	{
		
		List<CondicionUrbanisticaSimplificadaDTO> listaCUaCopiar =getCondicionesSeleccionadas();
		List<Integer> listaIdEntidadDestino = obtenerIdsTramitesSeleccionados();
			
		log.debug("[copiarCUsSeleccionadasEnNuevaEntidad] listaCUaCopiar="+listaCUaCopiar.size()+" / listaIdEntidadDestino="+listaIdEntidadDestino.size());


		// Cargamos la entidad donde iran las copias de las CUs
		try
		{

			for(ArbolGenericoObject tramite : seleccionados) 
			{
				
				if(tramite.getDatosIdTextoArbolGenerico().getTipo().equalsIgnoreCase("entidad")) 
				{
					Integer idEntCopia = tramite.getDatosIdTextoArbolGenerico().getIdBaseDatos();
					String nombreEntidadCopia = tramite.getDatosIdTextoArbolGenerico().getTexto();
					
					
					log.debug("[copiarCUsSeleccionadasEnNuevaEntidad] idEntCopia="+idEntCopia);

					int numCUNoCopiadas = 0;
					int numCUSeleccionadas = 0;


					// Si la cu no ha sido modificada, se podra elegir una nueva
					for (CondicionUrbanisticaSimplificadaDTO cuSimpl : listaCUaCopiar)
					{


						log.debug("[copiarCUsSeleccionadasEnNuevaEntidad] Seleccionada la CU: Entidad="+cuSimpl.getIdEntidad()+" / Det="+cuSimpl.getIdDeterminacion()+" / Regimen="+cuSimpl.getIdRegimen()+" Num RE="+cuSimpl.getRegimenesEspecificos().size());
						numCUSeleccionadas++;

						// Tendriamos que copiar esta CU en la entidad destino
						cuSimpl.setIdEntidad(idEntCopia);
						try
						{
							int idRegimen = servicioBasicoCondicionesUrbanisticas.crearCU(cuSimpl);

							if (idRegimen == 0)
							{
								JsfUtil.addErrorMessage("ATENCION: Error al copiar la Condicion Urbanistica de Determinacion= '"+cuSimpl.getNombreDeterminacion()+ "' en la Entidad Destino Seleccionada: "+nombreEntidadCopia+". Probablemente esa determinacion no tenga como grupo de aplicacion el aplicado para esa entidad");
								Object[] params = new Object[3];
								params[0]= cuSimpl.getNombreDeterminacion();
								params[1]= cuSimpl.getNombreDeterminacion();
								params[2]= nombreEntidadCopia;
								statusMessages.addFromResourceBundle(Severity.ERROR, "no_grupo_aplicacion", params);
								numCUNoCopiadas++;
							}

							if (idRegimen==-1)
							{
								JsfUtil.addErrorMessage("ATENCION: Error al copiar la Condicion Urbanistica de Determinacion= "+cuSimpl.getNombreDeterminacion()+ " en la Entidad Destino Seleccionada: "+nombreEntidadCopia+". La Entidad Destino Seleccionada aun no tiene aplicado ningun Grupo de Aplicacion. Debe aplicar primero un Grupo a la entidad antes de construir otras Condiciones Urbanisticas");
								Object[] params = new Object[3];
								params[0]= cuSimpl.getNombreDeterminacion();
								params[1]= cuSimpl.getNombreDeterminacion();
								params[2]= nombreEntidadCopia;
								statusMessages.addFromResourceBundle(Severity.ERROR, "no_grupo_aplicacion_agrupar", params);
								numCUNoCopiadas++;
							}
							if (idRegimen==-2)
							{
								JsfUtil.addErrorMessage("ATENCION: Error al copiar la Condicion Urbanistica de Determinacion= "+cuSimpl.getNombreDeterminacion()+ " en la Entidad Destino Seleccionada: "+nombreEntidadCopia+". No se ha encontrado en la determinacion a aplicar que tenga como grupo a la determinacion de grupo aplicada a la entidad. Agregue el Grupo de Aplicacion de la Entidad Destino a la determinacion: "+cuSimpl.getNombreDeterminacion());
								Object[] params = new Object[3];
								params[0]= cuSimpl.getNombreDeterminacion();
								params[1]= cuSimpl.getNombreDeterminacion();
								params[2]= nombreEntidadCopia;
								statusMessages.addFromResourceBundle(Severity.ERROR, "no_grupo_aplicacion_a_determinacion", params);
								numCUNoCopiadas++;
							}

						}
						catch (Exception ex)
						{
							JsfUtil.addErrorMessage("ATENCION: Error al copiar la Condicion Urbanistica de Determinacion= "+cuSimpl.getNombreDeterminacion()+" a la Entidad Destino: "+nombreEntidadCopia);
							Object[] params = new Object[3];
							params[0]= cuSimpl.getNombreDeterminacion();
							params[1]= cuSimpl.getNombreDeterminacion();
							params[2]= nombreEntidadCopia;
							statusMessages.addFromResourceBundle(Severity.ERROR, "no_copiar_cu", params);
							numCUNoCopiadas++;
						}


					}

					if (numCUNoCopiadas>0)
					{
						JsfUtil.addErrorMessage("ATENCION: No se han podido copiar "+numCUNoCopiadas+" Condiciones Urbanisticas de las "+numCUSeleccionadas+" seleccionadas a la Entidad Destino: "+nombreEntidadCopia);
						Object[] params = new Object[3];
						params[0]= numCUNoCopiadas;
						params[1]= numCUSeleccionadas;
						params[2]= nombreEntidadCopia;
						statusMessages.addFromResourceBundle(Severity.ERROR, "no_se_ha_podido_copiar_cu", params);
						
					}
					else
					{
						JsfUtil.addSuccessMessage("Se han copiado correctamente "+numCUSeleccionadas+" CUs a la Entidad Destino: "+nombreEntidadCopia);
						statusMessages.addFromResourceBundle(Severity.INFO, "se_ha_podido_copiar_cu",numCUSeleccionadas+" CUs a la Entidad Destino: "+nombreEntidadCopia);
						
					}
					
					
					
					
					
				}				
			

				
			}

			FacesManager.instance().redirect("/produccionfip/gestionentidades/VerEntidadPlanEncargado.xhtml");

		}

		catch (Exception ex2)
		{
			log.error("[copiarCUsSeleccionadasEnNuevaEntidad] Error");
			ex2.printStackTrace();
			statusMessages.addFromResourceBundle(Severity.ERROR, "Error al copiar las Condiciones Urbanisticas");

		}
	}
	
	protected DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent,String title, ParIdentificadorTexto datosIdTextoArbolGenerico) 
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		ArbolGenericoObject userObject = new ArbolGenericoObject(node);
		node.setUserObject(userObject);
		userObject.setDatosIdTextoArbolGenerico(datosIdTextoArbolGenerico);
		
		// non-employee node or branch
		if (title != null) {
			userObject.setText(title);
			userObject.setLeaf(false);
			userObject.setExpanded(true);
			node.setAllowsChildren(true);
		}

		else {

			if (!datosIdTextoArbolGenerico.isHoja()) {
				userObject.setText(datosIdTextoArbolGenerico.getTexto());
				userObject.setLeaf(false);
				userObject.setExpanded(false);
				node.setAllowsChildren(true);
			} else {
				// Es una hoja
				userObject.setText(datosIdTextoArbolGenerico.getTexto());
				userObject.setLeaf(true);
				node.setAllowsChildren(false);

			}

		}
		// finally add the node to the parent.
		if (parent != null) {
			parent.add(node);
		}

		return node;
	}
	
	

    protected DefaultMutableTreeNode findTreeNode(String nodeId) {
        DefaultMutableTreeNode rootNode =
                (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode node;
        ArbolGenericoObject tmp;
        Enumeration nodes = rootNode.depthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            node = (DefaultMutableTreeNode) nodes.nextElement();
            
            tmp = (ArbolGenericoObject) node.getUserObject();
            //logger.debug(tmp.getDatosIdTextoArbolGenerico().getTexto());
            if (nodeId.equals(String.valueOf(tmp.getDatosIdTextoArbolGenerico().getIdBaseDatos()))) {
                return node;
            }
        }
        return null;
    }
    
    @Remove
	public void destroy() {
	}

}
