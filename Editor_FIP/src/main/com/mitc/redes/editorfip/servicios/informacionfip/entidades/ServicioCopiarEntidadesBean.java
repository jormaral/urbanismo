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


package com.mitc.redes.editorfip.servicios.informacionfip.entidades;

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
import javax.persistence.NoResultException;
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
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.basicos.fip.tramites.ServicioBasicoTramites;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenerico;
import com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades.GestionArbolEntidades;
import com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades.GestionEntidades;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Scope(ScopeType.SESSION)
@Stateful
@Name("servicioCopiarEntidades")
public class ServicioCopiarEntidadesBean extends GestionArbolGenerico implements Serializable, ServicioCopiarEntidades
{
    @Logger private Log log;

    @In (create = true)
	ServicioBasicoEntidades servicioBasicoEntidades;
    
    @In(create = true, required = false)
	ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;
    
    private List<ArbolGenericoObject> seleccionados;
    
    @In (create = true, required = false)
    VariablesSesionUsuario variablesSesionUsuario;
    
    @In (create = true, required = false)
    GestionArbolEntidades gestionArbolEntidades;
    
    @In (create = true, required = false)
    GestionArbolEntidadesCopiar gestionArbolEntidadesCopiar;
    
    @In(create=true, required=false)
    FacesMessages facesMessages;
    
    @PersistenceContext
	EntityManager em;
    
    private int idTramiteTrabajoArbol = 0;
    private int nodoSeleccionado = 0;
    
    public DefaultTreeModel getModel() {

		if (model == null || idTramiteTrabajoArbol != variablesSesionUsuario.getIdTramiteTrabajoActual()) {

			idTramiteTrabajoArbol = variablesSesionUsuario.getIdTramiteTrabajoActual();
			
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
	            //List<ParIdentificadorTexto> result = servicioBasicoTramites.getArbolAsignacionTramiteUsuarioHijos(selectedUserObject.getDatosIdTextoArbolGenerico().getIdBaseDatos());
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
	        			        		
	        		List<ParIdentificadorTexto> result = servicioBasicoEntidades.getEntidadesRaices(idTramiteTrabajoArbol);

	                
	                DefaultMutableTreeNode rootNode = addNode(null, "Entidad",
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
	 	 	
	 		Tree tree = (Tree)panelt.findComponent("arbolEntidad");
	 		
	 		DefaultMutableTreeNode nod = tree.getCurrentNode();
	 		
	 		boolean selec = !((ArbolGenericoObject) nod.getUserObject()).isSeleccionado();
	 
	 		ArbolGenericoObject nodoArbol = (ArbolGenericoObject) nod.getUserObject();
	 		
	 		seleccionados.remove(nodoArbol);
	 		if (selec)
	    	 	seleccionados.add(nodoArbol);
	 		
	 		
	 		
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
	        marcarHijos(nod, selec);
	        
	        if(!nodoArbol.isExpanded() && selec)
	        	nodoArbol.setExpanded(true);
	        
	        context.renderResponse();
	 		
	 		
	 		
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
	
	public void copiarSeleccionados(int idEntidad)
	{
		Tramite tramite = em.find(Tramite.class, variablesSesionUsuario.getIdTramiteEncargadoTrabajo());
		Entidad entidadPadre = em.find(Entidad.class, idEntidad);
		if(seleccionados.size() > 0) {
			for(ArbolGenericoObject ago : seleccionados) {
				Entidad entidad = em.find(Entidad.class, ago.getDatosIdTextoArbolGenerico().getIdBaseDatos());
				
				Entidad entidad_2 = new Entidad();
				
				entidad_2.setNombre(entidad.getNombre());
				entidad_2.setClave(entidad.getClave());
				entidad_2.setEtiqueta(entidad.getEtiqueta());
				
				entidad_2.setCodigo(servicioBasicoEntidades.nextCodigo(tramite.getIden()+""));
				entidad_2.setTramite(tramite);
				entidad_2.setEntidadByIdpadre(entidadPadre);
				
				entidad_2.setEntidadByIdentidadbase(entidad.getEntidadByIdentidadbase());
				em.persist(entidad_2);
				
				// FGA Copio la geometria
				
				// Puntual
				String buscarGeomPuntual = " select pnt from Entidadpnt pnt where pnt.entidad.iden = "+entidad.getIden();
				try
				{
					Entidadpnt geomPuntual = (Entidadpnt) em.createQuery(buscarGeomPuntual).getSingleResult();
					
					// La Copio
					Entidadpnt geomPuntualCopia = new Entidadpnt();
					
					geomPuntualCopia.setBsuspendida(geomPuntual.isBsuspendida());
					geomPuntualCopia.setGeom(geomPuntual.getGeom());
					geomPuntualCopia.setEntidad(entidad_2);
					
					em.persist(geomPuntualCopia);
					log.info("[copiarSeleccionados] Copiada geometria puntual.");
					
				}
				catch (NoResultException e) {
		            log.debug("[copiarSeleccionados] No hay geometria puntual. No se copia nada de geometria puntual");

		        } catch (Exception ex) {
		            log.debug("[copiarSeleccionados] Error al buscar geometria puntual. No se copia nada de geometria puntual" + ex.getMessage());
		            ex.printStackTrace();
		        } 
		        
		        // Lineal
				String buscarGeomLineal = " select lin from Entidadlin lin where lin.entidad.iden = "+entidad.getIden();
				try
				{
					Entidadlin geomLineal = (Entidadlin) em.createQuery(buscarGeomLineal).getSingleResult();
					
					// La Copio
					Entidadlin geomLinealCopia = new Entidadlin();
					
					geomLinealCopia.setBsuspendida(geomLineal.isBsuspendida());
					geomLinealCopia.setGeom(geomLineal.getGeom());
					geomLinealCopia.setEntidad(entidad_2);
					
					em.persist(geomLinealCopia);
					log.info("[copiarSeleccionados] Copiada geometria Lineal.");
					
				}
				catch (NoResultException e) {
		            log.debug("[copiarSeleccionados] No hay geometria Lineal. No se copia nada de geometria Lineal");

		        } catch (Exception ex) {
		            log.debug("[copiarSeleccionados] Error al buscar geometria Lineal. No se copia nada de geometria Lineal" + ex.getMessage());
		            ex.printStackTrace();
		        } 
		        
		        // Poligonal
				String buscarGeomPoligonal = " select pol from Entidadpol pol where pol.entidad.iden = "+entidad.getIden();
				try
				{
					Entidadpol geomPoligonal = (Entidadpol) em.createQuery(buscarGeomPoligonal).getSingleResult();
					
					// La Copio
					Entidadpol geomPoligonalCopia = new Entidadpol();
					
					geomPoligonalCopia.setBsuspendida(geomPoligonal.isBsuspendida());
					geomPoligonalCopia.setGeom(geomPoligonal.getGeom());
					geomPoligonalCopia.setEntidad(entidad_2);
					
					em.persist(geomPoligonalCopia);
					log.info("[copiarSeleccionados] Copiada geometria Poligonal.");
					
				}
				catch (NoResultException e) {
		            log.debug("[copiarSeleccionados] No hay geometria Poligonal. No se copia nada de geometria Poligonal");

		        } catch (Exception ex) {
		            log.debug("[copiarSeleccionados] Error al buscar geometria Poligonal. No se copia nada de geometria Poligonal" + ex.getMessage());
		            ex.printStackTrace();
		        } 
				
				
				// FGA: Persisto ahora las CUs asociadas
				
				List<CondicionUrbanisticaSimplificadaDTO> cuSimplificadaList = servicioBasicoCondicionesUrbanisticas.listadoTodasCUSimplificadaDeEntidad(entidad.getIden());
				log.debug("[copiarSeleccionados] entidad a Copiar= " +entidad.getIden()+" numero cuSimplificadaList="+cuSimplificadaList.size());
				
				// Lo primero es localizar e insertar el Grupo de Aplicacion
				CondicionUrbanisticaSimplificadaDTO cuGrupoEntidad = null;
				
				// Obtengo el id de la determinacion del Grupo de Aplicacion del plan base de esa entidad
				
				String queryIdPlanBase = "select distinct p.idplanbase from planeamiento.plan p "+
						" join planeamiento.tramite t on t.idplan = p.iden "+
						" join planeamiento.entidad e on e.idtramite = t.iden "+
						" where e.iden = "+entidad.getIden();
				
				int idPlanBase = (Integer) em.createNativeQuery(queryIdPlanBase).getSingleResult();
				log.debug("[copiarSeleccionados] entidad a Copiar= " +entidad.getIden()+" idPlanBase="+idPlanBase);
				
				// Obtengo el id de la detemrinacion 'Grupo de Aplicacion' (7777777777) del plan base
				
				String queryIdDetGA = "select d.iden from planeamiento.plan p "+
						" join planeamiento.tramite t on t.idplan = p.iden "+
						" join planeamiento.determinacion d on d.idtramite = t.iden "+
						" where p.iden = "+idPlanBase+" and d.codigo='7777777777'";	
				
				int idDetGA = (Integer) em.createNativeQuery(queryIdDetGA).getSingleResult();
				log.debug("[copiarSeleccionados] entidad a Copiar= " +entidad.getIden()+" idDetGA="+idDetGA);
				
				cuGrupoEntidad = new CondicionUrbanisticaSimplificadaDTO();
				
				cuGrupoEntidad.setIdEntidad(entidad_2.getIden());
				cuGrupoEntidad.setIdDeterminacion(idDetGA);
				
				// El Valor de Referencia es el tuviera la entidad origen aplicado en Grupo de Aplicacion
				
				
				int idVRGA = 0;
				
				// Recorremos todas las CU para buscar donde esta aplicada a idDetGA
				log.debug("[copiarSeleccionados] Recorremos todas las CU para buscar donde esta aplicada a idDetGA = "+idDetGA);
				for (CondicionUrbanisticaSimplificadaDTO cuSimpl : cuSimplificadaList)
				{
					log.debug("[copiarSeleccionados] cuSimpl.getIdDeterminacion() = "+cuSimpl.getIdDeterminacion());
					if (cuSimpl.getIdDeterminacion()==idDetGA)
					{
						idVRGA = cuSimpl.getIdDeterminacionValorReferencia();
						break;
					}
				}
				
				// Si ha encontrado, pues persistimos.
				if (idVRGA!=0)
				{
					cuGrupoEntidad.setIdDeterminacionValorReferencia(idVRGA);
					int idRegimen = servicioBasicoCondicionesUrbanisticas.crearCUGrupoAplicacion(cuGrupoEntidad);
					log.debug("[copiarSeleccionados] entidad a Copiar= " +entidad.getIden()+" Creada CU de GA con idRegimen="+idRegimen);
					
					
					// Recorro el resto de CU copiandolas y persistiendo
					for (CondicionUrbanisticaSimplificadaDTO cuSimpl : cuSimplificadaList)
					{
						if (cuSimpl.getIdDeterminacion()==idDetGA)
						{
							// Esta no la persisto porque ya esta persistida de antes
						}
						else
						{
							// Copio la CU
							CondicionUrbanisticaSimplificadaDTO nuevaCUCopiada = new CondicionUrbanisticaSimplificadaDTO();
							
							nuevaCUCopiada.setIdEntidad(entidad_2.getIden());
							
							nuevaCUCopiada.setIdDeterminacion(cuSimpl.getIdDeterminacion());
							nuevaCUCopiada.setIdDeterminacionRegimen(cuSimpl.getIdDeterminacionRegimen());
							nuevaCUCopiada.setIdDeterminacionValorReferencia(cuSimpl.getIdDeterminacionValorReferencia());
							nuevaCUCopiada.setValor(cuSimpl.getValor());
							
							if (cuSimpl.getRegimenesEspecificos()!=null)
							{
								nuevaCUCopiada.setRegimenesEspecificos(cuSimpl.getRegimenesEspecificos());
							}
							
							nuevaCUCopiada.setIdRegimen(cuSimpl.getIdRegimen());
							nuevaCUCopiada.setIdCasoAplicacion(cuSimpl.getIdCasoAplicacion());
							
							// La persisto
							int idRegimenCUNueva = servicioBasicoCondicionesUrbanisticas.crearCU(nuevaCUCopiada);
							log.debug("[copiarSeleccionados] Persistida nuevaCUCopiada con idRegimenCUNueva="+idRegimenCUNueva);
							
							
						}
					}
					
					
					facesMessages.addFromResourceBundle(Severity.INFO,"Entidades Copiadas Correctamente", null);
				}
				else
				{
					facesMessages.addFromResourceBundle(Severity.INFO,"Se ha copiado la entidad, pero no Su Grupo de Aplicacion", null);
				}
							
				
			}
		}
		em.flush();
		
		gestionArbolEntidades.setModel(null);	
		
		gestionArbolEntidadesCopiar.setModel(null);
		gestionArbolEntidadesCopiar.cargarYExpandirEntidad(idEntidad);
		
		
	}
	
	public void duplicarEntidad(int idEntidad)
	{
		log.debug("[duplicarEntidad] Se va a duplicar la Entidad: "+idEntidad);
		Tramite tramite = em.find(Tramite.class, variablesSesionUsuario.getIdTramiteEncargadoTrabajo());
		
		
			
				Entidad entidad = em.find(Entidad.class, idEntidad);
				Entidad entidadPadre = entidad.getEntidadByIdpadre();
				
				Entidad entidad_2 = new Entidad();
				
				entidad_2.setNombre(entidad.getNombre());
				entidad_2.setClave(entidad.getClave());
				entidad_2.setEtiqueta(entidad.getEtiqueta());
				
				// FGA: El Codigo deberia ser nuevo
				entidad_2.setCodigo(servicioBasicoEntidades.nextCodigo(tramite.getIden()+""));
				
				entidad_2.setTramite(tramite);
				entidad_2.setEntidadByIdpadre(entidadPadre);
				
				entidad_2.setEntidadByIdentidadbase(entidad.getEntidadByIdentidadbase());
				em.persist(entidad_2);
				
				// FGA Copio la geometria
				
				// Puntual
				String buscarGeomPuntual = " select pnt from Entidadpnt pnt where pnt.entidad.iden = "+entidad.getIden();
				try
				{
					Entidadpnt geomPuntual = (Entidadpnt) em.createQuery(buscarGeomPuntual).getSingleResult();
					
					// La Copio
					Entidadpnt geomPuntualCopia = new Entidadpnt();
					
					geomPuntualCopia.setBsuspendida(geomPuntual.isBsuspendida());
					geomPuntualCopia.setGeom(geomPuntual.getGeom());
					geomPuntualCopia.setEntidad(entidad_2);
					
					em.persist(geomPuntualCopia);
					log.info("[duplicarEntidad] Copiada geometria puntual.");
					
				}
				catch (NoResultException e) {
		            log.debug("[duplicarEntidad] No hay geometria puntual. No se copia nada de geometria puntual");

		        } catch (Exception ex) {
		            log.debug("[duplicarEntidad] Error al buscar geometria puntual. No se copia nada de geometria puntual" + ex.getMessage());
		            ex.printStackTrace();
		        } 
		        
		        // Lineal
				String buscarGeomLineal = " select lin from Entidadlin lin where lin.entidad.iden = "+entidad.getIden();
				try
				{
					Entidadlin geomLineal = (Entidadlin) em.createQuery(buscarGeomLineal).getSingleResult();
					
					// La Copio
					Entidadlin geomLinealCopia = new Entidadlin();
					
					geomLinealCopia.setBsuspendida(geomLineal.isBsuspendida());
					geomLinealCopia.setGeom(geomLineal.getGeom());
					geomLinealCopia.setEntidad(entidad_2);
					
					em.persist(geomLinealCopia);
					log.info("[duplicarEntidad] Copiada geometria Lineal.");
					
				}
				catch (NoResultException e) {
		            log.debug("[duplicarEntidad] No hay geometria Lineal. No se copia nada de geometria Lineal");

		        } catch (Exception ex) {
		            log.debug("[duplicarEntidad] Error al buscar geometria Lineal. No se copia nada de geometria Lineal" + ex.getMessage());
		            ex.printStackTrace();
		        } 
		        
		        // Poligonal
				String buscarGeomPoligonal = " select pol from Entidadpol pol where pol.entidad.iden = "+entidad.getIden();
				try
				{
					Entidadpol geomPoligonal = (Entidadpol) em.createQuery(buscarGeomPoligonal).getSingleResult();
					
					// La Copio
					Entidadpol geomPoligonalCopia = new Entidadpol();
					
					geomPoligonalCopia.setBsuspendida(geomPoligonal.isBsuspendida());
					geomPoligonalCopia.setGeom(geomPoligonal.getGeom());
					geomPoligonalCopia.setEntidad(entidad_2);
					
					em.persist(geomPoligonalCopia);
					log.info("[duplicarEntidad] Copiada geometria Poligonal.");
					
				}
				catch (NoResultException e) {
		            log.debug("[duplicarEntidad] No hay geometria Poligonal. No se copia nada de geometria Poligonal");

		        } catch (Exception ex) {
		            log.debug("[duplicarEntidad] Error al buscar geometria Poligonal. No se copia nada de geometria Poligonal" + ex.getMessage());
		            ex.printStackTrace();
		        } 
				
				
				// FGA: Persisto ahora las CUs asociadas
				
				List<CondicionUrbanisticaSimplificadaDTO> cuSimplificadaList = servicioBasicoCondicionesUrbanisticas.listadoTodasCUSimplificadaDeEntidad(entidad.getIden());
				log.debug("[duplicarEntidad] entidad a Duplicar= " +entidad.getIden()+" numero cuSimplificadaList="+cuSimplificadaList.size());
				
				// Lo primero es localizar e insertar el Grupo de Aplicacion
				CondicionUrbanisticaSimplificadaDTO cuGrupoEntidad = null;
				
				// Obtengo el id de la determinacion del Grupo de Aplicacion del plan base de esa entidad
				
				String queryIdPlanBase = "select distinct p.idplanbase from planeamiento.plan p "+
						" join planeamiento.tramite t on t.idplan = p.iden "+
						" join planeamiento.entidad e on e.idtramite = t.iden "+
						" where e.iden = "+entidad.getIden();
				
				int idPlanBase = (Integer) em.createNativeQuery(queryIdPlanBase).getSingleResult();
				log.debug("[duplicarEntidad] entidad a Copiar= " +entidad.getIden()+" idPlanBase="+idPlanBase);
				
				// Obtengo el id de la detemrinacion 'Grupo de Aplicacion' (7777777777) del plan base
				
				String queryIdDetGA = "select d.iden from planeamiento.plan p "+
						" join planeamiento.tramite t on t.idplan = p.iden "+
						" join planeamiento.determinacion d on d.idtramite = t.iden "+
						" where p.iden = "+idPlanBase+" and d.codigo='7777777777'";	
				
				int idDetGA = (Integer) em.createNativeQuery(queryIdDetGA).getSingleResult();
				log.debug("[duplicarEntidad] entidad a Copiar= " +entidad.getIden()+" idDetGA="+idDetGA);
				
				cuGrupoEntidad = new CondicionUrbanisticaSimplificadaDTO();
				
				cuGrupoEntidad.setIdEntidad(entidad_2.getIden());
				cuGrupoEntidad.setIdDeterminacion(idDetGA);
				
				// El Valor de Referencia es el tuviera la entidad origen aplicado en Grupo de Aplicacion
				
				
				int idVRGA = 0;
				
				// Recorremos todas las CU para buscar donde esta aplicada a idDetGA
				log.debug("[duplicarEntidad] Recorremos todas las CU para buscar donde esta aplicada a idDetGA = "+idDetGA);
				for (CondicionUrbanisticaSimplificadaDTO cuSimpl : cuSimplificadaList)
				{
					log.debug("[duplicarEntidad] cuSimpl.getIdDeterminacion() = "+cuSimpl.getIdDeterminacion());
					if (cuSimpl.getIdDeterminacion()==idDetGA)
					{
						idVRGA = cuSimpl.getIdDeterminacionValorReferencia();
						break;
					}
				}
				
				// Si ha encontrado, pues persistimos.
				if (idVRGA!=0)
				{
					cuGrupoEntidad.setIdDeterminacionValorReferencia(idVRGA);
					int idRegimen = servicioBasicoCondicionesUrbanisticas.crearCUGrupoAplicacion(cuGrupoEntidad);
					log.debug("[duplicarEntidad] entidad a Copiar= " +entidad.getIden()+" Creada CU de GA con idRegimen="+idRegimen);
					
					
					// Recorro el resto de CU copiandolas y persistiendo
					for (CondicionUrbanisticaSimplificadaDTO cuSimpl : cuSimplificadaList)
					{
						if (cuSimpl.getIdDeterminacion()==idDetGA)
						{
							// Esta no la persisto porque ya esta persistida de antes
						}
						else
						{
							// Copio la CU
							CondicionUrbanisticaSimplificadaDTO nuevaCUCopiada = new CondicionUrbanisticaSimplificadaDTO();
							
							nuevaCUCopiada.setIdEntidad(entidad_2.getIden());
							
							nuevaCUCopiada.setIdDeterminacion(cuSimpl.getIdDeterminacion());
							nuevaCUCopiada.setIdDeterminacionRegimen(cuSimpl.getIdDeterminacionRegimen());
							nuevaCUCopiada.setIdDeterminacionValorReferencia(cuSimpl.getIdDeterminacionValorReferencia());
							nuevaCUCopiada.setValor(cuSimpl.getValor());
							
							if (cuSimpl.getRegimenesEspecificos()!=null)
							{
								nuevaCUCopiada.setRegimenesEspecificos(cuSimpl.getRegimenesEspecificos());
							}
							
							nuevaCUCopiada.setIdRegimen(cuSimpl.getIdRegimen());
							nuevaCUCopiada.setIdCasoAplicacion(cuSimpl.getIdCasoAplicacion());
							
							// La persisto
							int idRegimenCUNueva = servicioBasicoCondicionesUrbanisticas.crearCU(nuevaCUCopiada);
							log.debug("[duplicarEntidad] Persistida nuevaCUCopiada con idRegimenCUNueva="+idRegimenCUNueva);
							
							
						}
					}
					
					
					facesMessages.addFromResourceBundle(Severity.INFO,"Entidad Duplicada Correctamente", null);
				}
				else
				{
					facesMessages.addFromResourceBundle(Severity.WARN,"Se ha duplicado la entidad, pero no Su Grupo de Aplicacion", null);
				}
							
				
			
		
		em.flush();
		
		
		
		
	}
}
