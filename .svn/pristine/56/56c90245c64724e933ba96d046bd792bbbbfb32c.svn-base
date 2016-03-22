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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;

import com.icesoft.faces.component.selectinputtext.SelectInputText;
import com.mitc.redes.editorfip.entidades.interfazgrafico.BusquedaGenericaDTO;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Usuario;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.UsuarioRol;
import com.mitc.redes.editorfip.servicios.basicos.comunes.plantillas.ListaGenericaBean;

@Stateful
@Name("servicioUsuarioLista")
public class ServicioUsuarioListaBean extends ListaGenericaBean implements ServicioUsuarioLista {
	
	@In(create = true)
	UsuarioHome usuarioHome;
	
	@In(create=true)
    FacesMessages facesMessages;
	
	private List<SelectItem> resultadosBusqueda = new ArrayList<SelectItem>();
	
	private HashMap<String,Boolean> seleccionUsuarios;
	
	private String usernameUsuarioSel;
	private String usuarioSel;
	
	@In(create = true, required = false)
	ServicioCRUDGestionUsuario servicioCRUDGestionUsuario;
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getListaUsuarios() {
		
		
		/** Codigo para el seleccionado de usuarios por CheckBox **/
		List<Usuario> listaUsuarios = (List<Usuario>) super.listaInicial;
		
		seleccionUsuarios = new HashMap<String,Boolean>();
		if(listaUsuarios!=null) {
			for(Usuario usrTemp : listaUsuarios) {
				seleccionUsuarios.put(usrTemp.getUsername(), Boolean.FALSE);
			}
		}
		return (List<Usuario>) super.listaInicial;
	}
	
	public HashMap getSeleccionUsuarios() {
		return seleccionUsuarios;
	}

	public void setSeleccionUsuarios(HashMap seleccionUsuarios) {
		this.seleccionUsuarios = seleccionUsuarios;
	}
	
	/** Da de baja un usuario en el sistema **/
	public void darDebaja() {
		
		List<Usuario> listaUsr = (List<Usuario>)super.listaInicial;
		// List<Usuario> listaUsr = (List<Usuario>) super.getObjetosSeleccionados();
		
		Integer numSeleccionados = 0;
		for(Iterator<Boolean> it = seleccionUsuarios.values().iterator(); it.hasNext();) {
			if(it.next()) {
				numSeleccionados++;
			}
		}  
		
		if(numSeleccionados == 0) {
			facesMessages.addFromResourceBundle("gestionusuario_almenosuno", null);
		} else {
			for(Usuario usr : listaUsr) {			
				try {
					Boolean seleccionado = seleccionUsuarios.get(usr.getUsername());
					if(seleccionado) {
						Usuario usrTemp = entityManager.find(Usuario.class,usr.getUsername());
						
						boolean esAdministrador = false;
						for(UsuarioRol rol : usrTemp.getRolesAsignados()) {
							if("ADM".equalsIgnoreCase(rol.getRol().getCodigo())) {
								esAdministrador = true;
								break;
							}
						}
						
						if(esAdministrador) {
							facesMessages.addFromResourceBundle("gestionusuario_errorbajaadmin", null);
						}
						else if (usrTemp.isDadoBaja()){
							facesMessages.addFromResourceBundle("gestionusuario_yaestadadodebaja", usrTemp.getUsername());
						}
						else {
							usrTemp.setDadoBaja(true);
							usuarioHome.setInstance(usrTemp);
							usuarioHome.update();
							usuarioHome.clearInstance();
							
							facesMessages.clearGlobalMessages();
							facesMessages.addFromResourceBundle("gestionusuario_dadodebajaok", null);
						}						
						
					}				
					
				} catch (Exception e) {
					facesMessages.addFromResourceBundle("gestionusuario_errordardebaja", null);
					e.printStackTrace();
				} 
			}
		}		
	}
	
	
	/** Da de baja un usuario en el sistema **/
	public void darDealta() {
		
		List<Usuario> listaUsr = (List<Usuario>)super.listaInicial;
		// List<Usuario> listaUsr = (List<Usuario>) super.getObjetosSeleccionados();
		
		Integer numSeleccionados = 0;
		for(Iterator<Boolean> it = seleccionUsuarios.values().iterator(); it.hasNext();) {
			if(it.next()) {
				numSeleccionados++;
			}
		}  
		
		if(numSeleccionados == 0) {
			facesMessages.addFromResourceBundle("gestionusuario_almenosuno", null);
		} else {
			for(Usuario usr : listaUsr) {			
				try {
					Boolean seleccionado = seleccionUsuarios.get(usr.getUsername());
					if(seleccionado) {
						Usuario usrTemp = entityManager.find(Usuario.class,usr.getUsername());
						
						boolean esAdministrador = false;
						for(UsuarioRol rol : usrTemp.getRolesAsignados()) {
							if("ADM".equalsIgnoreCase(rol.getRol().getCodigo())) {
								esAdministrador = true;
								break;
							}
						}
						
						if(esAdministrador) {
							facesMessages.addFromResourceBundle("gestionusuario_errorbajaadmin", null);
						}
						else if (!usrTemp.isDadoBaja()){
							facesMessages.addFromResourceBundle("gestionusuario_yaestadadodealta", usrTemp.getUsername());
						}
						else {
							usrTemp.setDadoBaja(false);
							usuarioHome.setInstance(usrTemp);
							usuarioHome.update();
							usuarioHome.clearInstance();
							
							facesMessages.clearGlobalMessages();
							facesMessages.addFromResourceBundle("gestionusuario_dadodealtaok", null);
						}						
						
					}				
					
				} catch (Exception e) {
					facesMessages.addFromResourceBundle("gestionusuario_errordardealta", null);
					e.printStackTrace();
				} 
			}
		}		
	}
	
	/** Verifica que solo se ha seleccionado un usuario en 
	 * la lista de usuarios y redirige a la pagina de detalles
	 */
	public String verDetalle() {
		
		String resultado = "";
		// List<Usuario> listaUsr = (List<Usuario>) super.getObjetosSeleccionados();
		
		Integer numSeleccionados = 0;
		for(Iterator<Boolean> it = seleccionUsuarios.values().iterator(); it.hasNext();) {
			if(it.next()) {
				numSeleccionados++;
			}
		}
		
		if(numSeleccionados == 0) {
			facesMessages.addFromResourceBundle("gestionusuario_nosel", null);
		} else if (numSeleccionados > 1) { 
			facesMessages.addFromResourceBundle("gestionusuario_solouno", null);
		} else {
			List<Usuario> listaUsr = (List<Usuario>) super.listaInicial;
			for(Usuario usr : listaUsr) {
				if(seleccionUsuarios.get(usr.getUsername())) {
					usernameUsuarioSel = usr.getUsername();
					break;
				}
					
			}
			resultado = "success";
		}
		
		return resultado;
	}
	
	public String verDetalleAL(ActionEvent ae) {
		
		String resultado = "";
		// List<Usuario> listaUsr = (List<Usuario>) super.getObjetosSeleccionados();
		
		Integer numSeleccionados = 0;
		for(Iterator<Boolean> it = seleccionUsuarios.values().iterator(); it.hasNext();) {
			if(it.next()) {
				numSeleccionados++;
			}
		}
		
		if(numSeleccionados == 0) {
			facesMessages.addFromResourceBundle("gestionusuario_nosel", null);
		} else if (numSeleccionados > 1) { 
			facesMessages.addFromResourceBundle("gestionusuario_solouno", null);
		} else {
			List<Usuario> listaUsr = (List<Usuario>) super.listaInicial;
			for(Usuario usr : listaUsr) {
				if(seleccionUsuarios.get(usr.getUsername())) {
					usernameUsuarioSel = usr.getUsername();
					break;
				}
					
			}
			resultado = "success";
		}
		
		return resultado;
	}
	
	public String verDetalleBuscar(){
			if (usuarioSel=="-1"){
				return "nosuccess";
			}
			else{
				return "success";
			}
			
	}
	
	public String getUsernameUsuarioSel() {
		return usernameUsuarioSel;
	}

	public void setUsernameUsuarioSel(String usernameUsuarioSel) {
		this.usernameUsuarioSel = usernameUsuarioSel;
	}
	
	public List<SelectItem> getResultadosBusqueda() {
		return resultadosBusqueda;
	}

	public void setResultadosBusqueda(List<SelectItem> resultadosBusqueda) {
		this.resultadosBusqueda = resultadosBusqueda;
	}

	public void setUsuarioSel(String usuarioSel) {
		this.usuarioSel = usuarioSel;
	}

	public String getUsuarioSel() {
		return usuarioSel;
	}

	public void actualizarListaUsuarios(ValueChangeEvent event) {
		
		String filtro = ((SelectInputText) event.getComponent()).getValue().toString();
		List<BusquedaGenericaDTO> resultados;
		
		SelectInputText autoComplete = (SelectInputText) event.getComponent();

		// si no se selecciona ponemos el valor a -1 para sacar un listado de elementos.
		
		
		if (autoComplete.getSelectedItem() != null)
			
			setUsuarioSel(((BusquedaGenericaDTO) autoComplete.getSelectedItem().getValue()).getIdenUsuario());
		else
			setUsuarioSel("-1");
		
		if(!filtro.equalsIgnoreCase("")) {
			resultados = servicioCRUDGestionUsuario.autocompleteFiltros(filtro);
			
			resultadosBusqueda.clear();
			
			for(BusquedaGenericaDTO resul : resultados)
				resultadosBusqueda.add(new SelectItem(resul, resul.getIdenUsuario()));
		}
			
		
	}
	
	
	
	
	@Remove
	public void remove() {
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
}
