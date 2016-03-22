/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.DetalleUsuario;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Rol;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Usuario;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.UsuarioAmbito;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.UsuarioRol;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.UsuarioTramite;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoAmbitos;
import com.mitc.redes.editorfip.servicios.basicos.fip.tramites.ServicioBasicoTramites;

@Stateful
@Name("servicioUsuario")
public class ServicioUsuarioBean implements ServicioUsuario {
	
	@In 
    EntityManager entityManager;
	
	@In(create=true)
    FacesMessages facesMessages;
	
	@In(create = true)
	UsuarioHome usuarioHome;
	
	@In
	private Map<String,String> messages;
	
	@Logger 
	private Log log;
	   
	@In (create = true)
	ServicioBasicoAmbitos servicioBasicoAmbitos;
	
	@In (create = true)
	ServicioBasicoTramites servicioBasicoTramites;
	
	@In (create = true)
	ServicioBasicoRoles servicioBasicoRoles;
	
	
	
	
	
	private boolean esUsuarioNuevo;
	
	private boolean mostrarPanelAmbitos;
	private boolean mostrarPanelTramites;
	
	private Integer idAmbSel;
	private Integer idTramiteSel;
	
	private List<ParIdentificadorTexto> ambitosUsuario = new ArrayList<ParIdentificadorTexto> ();
	private List<SelectItem> listaRoles = new ArrayList<SelectItem>();
	private String codigoRolSeleccionado;
	private List<UsuarioTramite> tramitesAEliminar = new ArrayList<UsuarioTramite>();
	
	
	// TODO: TEMPORAL!!!
	private List<SelectItem> ambitosPadres = new ArrayList<SelectItem> ();
	private List<SelectItem> planesPadres = new ArrayList<SelectItem> ();
	
	private String nuevaPassword = "";
	private String confirmPassword = "";
	
	public void cargarUsuario(String username) {
		
		esUsuarioNuevo = username == null || username.equalsIgnoreCase("");
		mostrarPanelAmbitos = false;
		mostrarPanelTramites = false;
		tramitesAEliminar = new ArrayList<UsuarioTramite>();
		
		if(esUsuarioNuevo){
			
			usuarioHome.createInstance();			
			ambitosUsuario = new ArrayList<ParIdentificadorTexto> ();
			idAmbSel = null;
			idTramiteSel = null;
			codigoRolSeleccionado = "SIS";
			
		} else {
			
			usuarioHome.setInstance((Usuario)entityManager.find(Usuario.class, username));
			ambitosUsuario = new ArrayList<ParIdentificadorTexto> ();
			
			if(usuarioHome.getInstance().getRolesAsignados() != null && 
			   !usuarioHome.getInstance().getRolesAsignados().isEmpty()) {
				if (usuarioHome.getInstance().getRolesAsignados()!=null &&
						usuarioHome.getInstance().getRolesAsignados().size()>0)
					codigoRolSeleccionado = usuarioHome.getInstance().getRolesAsignados().get(0).getRol().getCodigo();
			}
			
			for(UsuarioAmbito usrAmb : usuarioHome.getInstance().getAmbitosAsignados()) {
				
				try {
					Integer idAmbito = usrAmb.getAmbito().getIden();
					String strAmbito = servicioBasicoAmbitos.ambitoString(idAmbito);
					ambitosUsuario.add(new ParIdentificadorTexto(idAmbito, strAmbito));
				} catch (Exception e) {
					log.error("[servicioUsuarioBean - cargarUsuario] Error al cargar el usuario: " + e.getMessage());
					// TODO: Messages
					facesMessages.addFromResourceBundle("Error al obtener los ambitos: " + e.getLocalizedMessage(), null);
					break;
				}
			}
		}
		
		// Inicializacion de listas para los modalpanel's
		try {
			if(ambitosPadres == null || ambitosPadres.isEmpty()) {
				ambitosPadres = new ArrayList<SelectItem> (); 
				for(ParIdentificadorTexto padre : servicioBasicoAmbitos.obtenerAmbitosRaices()) {
					ambitosPadres.add(new SelectItem(padre.getIdBaseDatos(), padre.getTexto()));
				}
			}
			
			if(planesPadres == null || planesPadres.isEmpty()) {
				planesPadres = new ArrayList<SelectItem> ();
				for(Tramite t : servicioBasicoTramites.obtenerTramitesRaizes()) {
					planesPadres.add(new SelectItem(t.getIden(), t.getNombre()));
				}
			}   
			
			if(listaRoles == null || listaRoles.isEmpty()) {
				List<Rol> roles = servicioBasicoRoles.obtenerRoles();
				
				for(Rol r : roles) {
					if(!r.getCodigo().equalsIgnoreCase("ADM")) {
						listaRoles.add(new SelectItem(r.getCodigo(), r.getNombre()));
					}					 
				}
			}
			
			facesMessages.clear();
			facesMessages.clearGlobalMessages();
			
		} catch (Exception e) {
			log.error("[servicioUsuarioBean - cargarUsuario] Error al obtener las listas de los modalPanels: " + e.getMessage());
			// TODO: Messages
			facesMessages.addFromResourceBundle("Error al obtener las listas de los modalPanels: " + e.getLocalizedMessage(), null);
		}
		
	}
	
	public String guardarUsuario() {
		try {
			
			if(validacionesSecundarias(usuarioHome.getInstance().getDni(), usuarioHome.getInstance().getUsername()))
			{
				
				if(!nuevaPassword.equalsIgnoreCase("")) {
					usuarioHome.getInstance().setClave(nuevaPassword);
				}
				
				if(esUsuarioNuevo) {
					// entityManager.persist(usuarioHome.getInstance()); 	
					DetalleUsuario detalle = new DetalleUsuario();
					detalle.setMail(usuarioHome.getInstance().getDetalle().getMail());
					detalle.setNombre(usuarioHome.getInstance().getDetalle().getNombre());
					detalle.setPrimerApellido(usuarioHome.getInstance().getDetalle().getPrimerApellido());
					detalle.setSegundoApellido(usuarioHome.getInstance().getDetalle().getSegundoApellido());
					
					usuarioHome.getInstance().setDetalle(null);
					usuarioHome.persist();
					
					detalle.setUsuario(usuarioHome.getInstance());
					detalle.setFechaAlta(new Date());
					entityManager.persist(detalle);		
					
					usuarioHome.getInstance().setDetalle(detalle);		
				} 
				
				if(!this.esAdministrador()) {
					// Eliminar roles antiguos
					for(Iterator<UsuarioRol> it = usuarioHome.getInstance().getRolesAsignados().iterator(); it.hasNext();) {
						UsuarioRol rolAEliminar = it.next();
						it.remove();
						entityManager.remove(rolAEliminar);
					}
					
					for(Rol rolSeleccionado : servicioBasicoRoles.obtenerRoles()) {
						if(rolSeleccionado.getCodigo().equalsIgnoreCase(codigoRolSeleccionado)) {
							UsuarioRol rol = new UsuarioRol();
							rol.setRol(rolSeleccionado);
							rol.setUsuario(usuarioHome.getInstance());
							
							entityManager.persist(rol);					
							
							usuarioHome.getInstance().getRolesAsignados().add(rol);
							break;
						}
					}
				}
				
				
				for(UsuarioTramite tram : tramitesAEliminar) {
					
					if(tram.getId() != null)
						entityManager.remove(tram);
				}
				
				for(UsuarioTramite ut : usuarioHome.getInstance().getTramitesAsignados()) {
					if(ut.getId() == null) {
						entityManager.persist(ut);
					}
				}
				
				// Asignacion de ambitos de usuario			
				// Eliminacion de ambitos	
				for(Iterator<UsuarioAmbito> it = usuarioHome.getInstance().getAmbitosAsignados().iterator(); it.hasNext();) {
					UsuarioAmbito amb = it.next();
					it.remove();
					entityManager.remove(amb);
				}
				
				// Se guardan los nuevos.
				for(ParIdentificadorTexto ambito : ambitosUsuario) {
					
					UsuarioAmbito nuevoAmbito = new UsuarioAmbito();
					nuevoAmbito.setAmbito(entityManager.find(Ambito.class, ambito.getIdBaseDatos()));
					nuevoAmbito.setUsuario(usuarioHome.getInstance());
					entityManager.persist(nuevoAmbito);
					
					usuarioHome.getInstance().getAmbitosAsignados().add(nuevoAmbito);
				}				
				
				// entityManager.merge(usuarioHome.getInstance());
				usuarioHome.update();
				
				facesMessages.clearGlobalMessages();
				facesMessages.clear();
				
				if(esUsuarioNuevo)
					facesMessages.addFromResourceBundle("gestionusuario_creado_ok", null);
				else
					facesMessages.addFromResourceBundle("gestionusuario_modificado_ok", null);
				
				
				return "success";
				
			} else {
				
				return "";
			}
			
		} catch (Exception e) {
			log.error("[servicioUsuarioBean - guardarUsuario] Error al guardar el usuario: " + e.getLocalizedMessage());
			facesMessages.addFromResourceBundle("gestionusuario_error", null);
			e.printStackTrace();
			return "";
		} 
	}
	
	public void eliminarUsuario() {
		
		try {
			//entityManager.remove(usuarioHome.getInstance());
			usuarioHome.remove();
		} catch (Exception e) {
			log.error("[servicioUsuarioBean - eliminarUsuario] Error al eliminar el usuario: " + e.getMessage());
			facesMessages.addFromResourceBundle(e.getMessage(), null);
		} finally {
			entityManager.close();
		}
	}
	
	public void seleccionarAmbito(Integer idAmbitoSel) {
		try {
			
			// Por si ya se ha añadido ...
			boolean aniadido = false;
			for(ParIdentificadorTexto ambUsr : ambitosUsuario) {
				if(ambUsr.getIdBaseDatos().equals(idAmbitoSel)) {
					aniadido = true;
					break;
				}
					
			}
			
			if(!aniadido) {
				String textoAmbito = servicioBasicoAmbitos.ambitoString(idAmbitoSel);
				ParIdentificadorTexto nuevoAmbito = new ParIdentificadorTexto(idAmbitoSel, textoAmbito);
				ambitosUsuario.add(nuevoAmbito);
				
			}
			
			setMostrarPanelAmbitos(false);
			
		} catch (Exception e) {
			log.error("[servicioUsuarioBean - seleccionarPlan] Error al seleccionar el Plan: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void eliminarAmbito(Integer idAmbito) {
		for(Iterator<ParIdentificadorTexto> it = ambitosUsuario.iterator(); it.hasNext();) {
			ParIdentificadorTexto ambito = it.next();
			if(ambito.getIdBaseDatos().equals(idAmbito)) {
				it.remove();
				break;
			}
		}
	}
	
	public String obtenerNombreAmbito(Integer idAmbito) {
		try {
			return servicioBasicoAmbitos.ambitoString(idAmbito);
		} catch (Exception e) {
			log.error("[servicioUsuarioBean - obtenerNombreAmbito] Error al obtener el nombre del ambito: " + e.getMessage());
			return "";
		}
	}
	
	public void seleccionarTramite(List<Integer> idsTramites) {
		try {
			
			for(Integer idTram : idsTramites) {
				
				// Busco el tramite en la lista de tramites del usuario.
				boolean aniadido = false;
				for(UsuarioTramite ut : usuarioHome.getInstance().getTramitesAsignados()) {
					if(ut.getTramite().getIden() == idTram) {
						aniadido = true;
						break;
					}
				}
				
				// Si aun no se encuentra añadido, lo añado.
				if(!aniadido) {
					Tramite planSeleccionado = entityManager.find(Tramite.class, idTram);
					UsuarioTramite nuevoTramite = new UsuarioTramite();
					nuevoTramite.setTramite(planSeleccionado);
					nuevoTramite.setUsuario(usuarioHome.getInstance());
					usuarioHome.getInstance().getTramitesAsignados().add(nuevoTramite);
					
				}	
				
				// Lo elimino de la lista de tramites a eliminar
				for(Iterator<UsuarioTramite> it = tramitesAEliminar.iterator(); it.hasNext();) {
					UsuarioTramite usrTram = it.next();
					if(idTram.equals(usrTram.getTramite().getIden())) {
						it.remove();
						break;
					}
				}
			}
						
			setMostrarPanelTramites(false);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("[servicioUsuarioBean - seleccionarPlan] Error al seleccionar el Plan: " + e.getMessage());
		}
	}
	
	public void eliminarTramite(Integer idTramite) {
		for(Iterator<UsuarioTramite> it = usuarioHome.getInstance().getTramitesAsignados().iterator(); it.hasNext();) {
			UsuarioTramite tramite = it.next();
			if(tramite.getTramite().getIden() == idTramite) {
				tramitesAEliminar.add(tramite);
				it.remove();
				break;
			}
		}
	}
	
	public void descartarCambios() {
		
		
		idAmbSel = null;
		idTramiteSel = null;
		entityManager.clear();
		if(esUsuarioNuevo){
			usuarioHome.createInstance();
		}
		else
			this.cargarUsuario(usuarioHome.getInstance().getUsername());
	}
	
	public void mostrarPanelAmbitosAL(ActionEvent actionEvent) {
		mostrarPanelAmbitos = true;
	}
	
	public void mostrarPanelTramitesAL(ActionEvent actionEvent) {
		mostrarPanelTramites = true;
	} 
	
	public void validatorEmail(FacesContext context, UIComponent component, Object value) {
	
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		String email = (String) value;
		Matcher m = p.matcher(email);
		if (!m.matches()) {
			((UIInput)component).setValid(false);
			FacesMessage message = new FacesMessage(messages.get("validacion_mail"));
			context.addMessage(component.getClientId(context), message);
		} 
	
	}
	
	public void validatorUsername(FacesContext context, UIComponent component, Object value) {
		String username = (String) value;
		Long totalUsuarios = (Long)entityManager.createQuery("SELECT COUNT(*) FROM Usuario WHERE username LIKE '" + username + "'").getSingleResult();
		if(totalUsuarios > 0) {
			((UIInput)component).setValid(false);
			FacesMessage message = new FacesMessage(messages.get("validacion_username_existente"));
			context.addMessage(component.getClientId(context), message);
		}
	}
	
	private boolean validacionesSecundarias(String dni, String username) {
		
		// Clave
		String password = this.nuevaPassword;
		String confirm = this.confirmPassword;	
		
		if(((password == null || password.equalsIgnoreCase("")) && (confirm!= null && !confirm.equalsIgnoreCase("")))  || 
		   ((confirm == null  || confirm.equalsIgnoreCase(""))  && (password!=null && !password.equalsIgnoreCase(""))) ||
		   ((!confirm.equalsIgnoreCase(password)))) {
			
			facesMessages.addFromResourceBundle(Severity.ERROR, "validacion_error_pass", null);
			return false;
		}
		
		if(esUsuarioNuevo) {
			
			if (servicioBasicoRoles.existeDniUsuario(dni)){
				facesMessages.addFromResourceBundle(Severity.ERROR, "validacion_error_dni_existente", null);
				return false;
			}
			
		} else {
			
			if (servicioBasicoRoles.existeDniUsuario(dni, username)){
				facesMessages.addFromResourceBundle(Severity.ERROR, "validacion_error_dni_existente", null);
				return false;
			}
			
		}
		
		// VALIDO!!
		return true;
		
	}

	public Integer getIdAmbSel() {
		return idAmbSel;
	}

	public void setIdAmbSel(Integer idAmbSel) {
		this.idAmbSel = idAmbSel;
	}

	public List<SelectItem> getAmbitosPadres() {
		return ambitosPadres;
	}

	public void setAmbitosPadres(List<SelectItem> ambitosPadres) {
		this.ambitosPadres = ambitosPadres;
	}

	public List<ParIdentificadorTexto> getAmbitosUsuario() {
		return ambitosUsuario;
	}

	public void setAmbitosUsuario(List<ParIdentificadorTexto> ambitosUsuario) {
		this.ambitosUsuario = ambitosUsuario;
	}

	public List<SelectItem> getPlanesPadres() {
		return planesPadres;
	}

	public void setPlanesPadres(List<SelectItem> planesPadres) {
		this.planesPadres = planesPadres;
	}

	public Integer getIdTramiteSel() {
		return idTramiteSel;
	}

	public void setIdTramiteSel(Integer idTramiteSel) {
		this.idTramiteSel = idTramiteSel;
	}

	public List<SelectItem> getListaRoles() {
		return listaRoles;
	}

	public void setListaRoles(List<SelectItem> listaRoles) {
		this.listaRoles = listaRoles;
	}
	
	
	public String getCodigoRolSeleccionado() {
		return codigoRolSeleccionado;
	}

	public void setCodigoRolSeleccionado(String codigoRolSeleccionado) {
		this.codigoRolSeleccionado = codigoRolSeleccionado;
	}

	public void setEsUsuarioNuevo(boolean esUsuarioNuevo) {
		this.esUsuarioNuevo = esUsuarioNuevo;
	}

	public boolean isEsUsuarioNuevo() {
		return esUsuarioNuevo;
	}

	public String getNuevaPassword() {
		return nuevaPassword;
	}

	public void setNuevaPassword(String nuevaPassword) {
		this.nuevaPassword = nuevaPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean isMostrarPanelAmbitos() {
		return mostrarPanelAmbitos;
	}

	public void setMostrarPanelAmbitos(boolean mostrarPanelAmbitos) {
		this.mostrarPanelAmbitos = mostrarPanelAmbitos;
	}

	public boolean isMostrarPanelTramites() {
		return mostrarPanelTramites;
	}

	public void setMostrarPanelTramites(boolean mostrarPanelTramites) {
		this.mostrarPanelTramites = mostrarPanelTramites;
	}
	
	
	
	// METODOS PRIVADOS
	private boolean poseeRol(String codigoRol) {	
		boolean resultado = false;
		if(usuarioHome.getInstance() != null && usuarioHome.getInstance().getRolesAsignados() != null) {
			for(UsuarioRol rolUsr : usuarioHome.getInstance().getRolesAsignados()) {
				if(rolUsr.getRol().getCodigo().equalsIgnoreCase(codigoRol)) {
					resultado = true;
					break;
				}
			}
		}
		
		return resultado;
	}

	public boolean esAdministrador(){
		boolean esAdministrador = this.poseeRol("ADM");		
		return esAdministrador;
	}
	
	/* CODIGO PARA EL TREEE
	 * public List<ParIdentificadorTexto> obtenerAmbitosHijos(Integer idPadre) {
		List<ParIdentificadorTexto> hijos = new ArrayList<ParIdentificadorTexto>();
		
		try {
			hijos = servicioBasicoAmbitos.obtenerAmbitosHijos(idPadre);
		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.addFromResourceBundle("Error al obtener los ambitos Hijos: " + e.getLocalizedMessage(), null);
		}
		
		return hijos;
	}
	
	public void contraerExpadir(NodeExpandedEvent event) {
		// get the source or the component who fired this event.  
		Object source = event.getSource();  
		if (source instanceof HtmlTree) {  
		    // It should be a html tree node, if yes get  
		    // the ui tree which contains this node.  
		    UITree tree = ((HtmlTree) source);
		// get the row key i.e. id of the given node.  
		  Object rowKey = tree.getRowKey();  
		     // get the model node of this node.  
		     TreeNode<ParIdentificadorTexto> ambitoPadreSeleccionado =  tree.getModelTreeNode(rowKey);  
		     if(null != ambitoPadreSeleccionado){  
		        // add the children nodes.  
		        aniadirAmbitosHijos(ambitoPadreSeleccionado);  
		     }  
		 } 
	}
	
	private void aniadirAmbitosHijos(TreeNode<ParIdentificadorTexto> nodoPadre) {
		 //process your node somehow. For this example I add only one node.  
		 ParIdentificadorTexto padre = (ParIdentificadorTexto)nodoPadre.getData();
		 List<ParIdentificadorTexto> hijos = servicioBasicoAmbitos.obtenerAmbitosHijos(padre.getIdBaseDatos());
		 
		 for(ParIdentificadorTexto hijo : hijos) {
			 TreeNodeImpl<ParIdentificadorTexto> nuevoNodoHijo = new TreeNodeImpl<ParIdentificadorTexto>();  
			 nuevoNodoHijo.setData(hijo);  
			 nuevoNodoHijo.setParent(nodoPadre); 
			 nodoPadre.addChild(null,nuevoNodoHijo); 
		 }	 
	}
	 */
	
	@Remove
	public void remove() {
	}
	
}
