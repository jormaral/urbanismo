package com.mitc.redes.editorfip.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.international.StatusMessage.Severity;

import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Usuario;
import com.mitc.redes.editorfip.servicios.RememberPass;
import com.mitc.redes.editorfip.servicios.administracion.gestionusuario.UsuarioHome;

@Stateless 
@Name("rememberPass")
public class RememberPassBean implements RememberPass {
	
	@In(create=true)
	private Renderer renderer;
	
	@In(create = true)
	UsuarioHome usuarioHome;
	
	@In 
    EntityManager entityManager;
	
	@In 
	FacesMessages facesMessages;
	
	private String campo;
	private boolean byUsername = true;
	
	private boolean mostrarPopUp = false;
	
	
	public void remember() {
		try {
			
			facesMessages.clear();
			List<Usuario> listaUsr = new ArrayList<Usuario>();
			
			if(byUsername) {
				// Username
				if(campo.equalsIgnoreCase("")) {
					facesMessages.addFromResourceBundle(Severity.ERROR, "genericos_campo_obligatorio", "Username");
				} else {
					Usuario usr = entityManager.find(Usuario.class, campo);
					
					if(usr != null)
						listaUsr.add(usr);					
				}		
				
			} else {
				
				// Email
				if(campo.equalsIgnoreCase("")) {
					facesMessages.addFromResourceBundle(Severity.ERROR, "genericos_campo_obligatorio", "Mail");
				} else {
					
					Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
			        Matcher m = p.matcher(campo);
			        boolean matchFound = m.matches();
			        
			        if (!matchFound)
			        	facesMessages.addFromResourceBundle(Severity.ERROR, "validacion_mail", null);
			        else
			        	listaUsr = (List)entityManager.createQuery("FROM Usuario usr WHERE usr.detalle.mail LIKE '" + campo +"'")
			        	.getResultList();	        
				}		        
			}	
			
			if(listaUsr == null || listaUsr.isEmpty()) {
				facesMessages.addFromResourceBundle(Severity.ERROR, "rec_pass_nousr", null);
	        	
			} else {
				
				for(Usuario usr : listaUsr) {
					usuarioHome.setId(usr.getUsername());
					
					renderer.render("/mailRememberPass.xhtml");
					facesMessages.addFromResourceBundle(Severity.INFO, "rec_pass_success", null);
				}
			}			
			
			usuarioHome.clearInstance();
			mostrarPopUp = false;
		       
		} catch (Exception e) {
				e.printStackTrace();
				facesMessages.addFromResourceBundle(Severity.ERROR, "rec_pass_error", e.getLocalizedMessage());
		        mostrarPopUp = false;
		}
	}
	
	public boolean isByUsername() {
		return byUsername;
	}
	public void setByUsername(boolean byUsername) {
		this.byUsername = byUsername;
	}

	public boolean isMostrarPopUp() {
		return mostrarPopUp;
	}

	public void setMostrarPopUp(boolean mostrarPopUp) {
		this.mostrarPopUp = mostrarPopUp;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

}
