package com.mitc.redes.editorfip.servicios;

import static org.jboss.seam.ScopeType.SESSION;

import java.util.Iterator;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Usuario;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.UsuarioRol;

@Stateless
@Name("authenticator")
public class AuthenticatorBean implements Authenticator {
	@Logger
	private Log log;

	@PersistenceContext
	EntityManager entityManager;

	@In
	Identity identity;
	@In
	Credentials credentials;

	@In(required = false)
	@Out(required = false, scope = SESSION)
	private Usuario user;

	public boolean authenticate() {
		try {

			user = (Usuario) entityManager
					.createQuery(
							"select usuario from Usuario usuario where usuario.username = :username and usuario.clave = :clave and usuario.dadoBaja is false")
					.setParameter("username", credentials.getUsername())
					.setParameter("clave", credentials.getPassword())
					.getSingleResult();

			if (user.getDetalle() != null) {
				if (user.getRolesAsignados() != null && user.getRolesAsignados().size()>0) {
					
					Iterator it = user.getRolesAsignados().iterator();
					while (it.hasNext())
					{
						UsuarioRol usuarioRol = (UsuarioRol) it.next();
						identity.addRole((usuarioRol.getRol().getCodigo()));
					}					
				}
			}

			log.info("authenticating {0}", credentials.getUsername());
			return true;

		}

		catch (NoResultException ex) {

			log.warn("Error authenticating {0}", credentials.getUsername());		
			return false;

		}
	}
	
	

}
