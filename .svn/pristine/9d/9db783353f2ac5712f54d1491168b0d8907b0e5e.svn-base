/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
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
package es.mitc.redes.urbanismoenred.servicios.seguridad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Traduccion;
import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuariorol;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless (name="ServicioUsuarios")
public class ServicioUsuariosBean implements ServicioUsuariosLocal {
	
	private static final Logger log = Logger.getLogger(ServicioUsuariosBean.class);
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal#borrarUsuario(int)
	 */
	@Override
	public void borrarUsuario(int identificador) throws ExcepcionPersistencia {
		es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario usuario = em.find(es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario.class, identificador);
		if (usuario != null) {
			em.remove(usuario);
		} else {
			throw new ExcepcionPersistencia("No existe usuario con id " + identificador);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal#borrarUsuario(java.lang.String)
	 */
	@Override
	public void borrarUsuario(String nombre) throws ExcepcionPersistencia {
		try {
			es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario usuario = (es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario)
					em.createNamedQuery("Usuario.findByNombre")
						.setParameter("nombre", nombre).getSingleResult();
			
			em.remove(usuario);
		} catch (NonUniqueResultException ex) {
			throw new ExcepcionPersistencia("Entontrado mas de un usuario con nombre: " + nombre,ex);
		} catch (NoResultException ex) {
			throw new ExcepcionPersistencia("No se encuentra el usuario con nombre: " + nombre,ex);
		} 
	}

	@Override
	public boolean existeNombre(String nombre) {
		boolean encontrado =false;
		try {
			em.createNamedQuery("Usuario.findByNombre")
						.setParameter("nombre", nombre).getSingleResult();
			encontrado = true;
		} catch (NonUniqueResultException ex) {
			log.warn("Entontrado mas de un usuario con nombre: " + nombre );
			encontrado = true;
		} catch (NoResultException ex) {
			log.warn("No se encuentra el usuario con nombre: " + nombre);
		} catch (Exception ex) {
			log.warn("No se ha podido realizar la busqueda del usuario: " + nombre );
		}
		return encontrado;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal#getAmbitos(java.lang.String, java.lang.String)
	 */
	@Override
	public Ambito[] getAmbitos(String nombre, String idioma) {
		@SuppressWarnings("unchecked")
		List<es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito> ambitos = (List<es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito>)em.createNamedQuery("Ambito.buscarPorNombre")
			.setParameter("nombre", nombre)
			.setParameter("idioma", idioma).getResultList();
		
		String nombreAmbito;
		String tipoAmbito;
		List<Ambito> resultado = new ArrayList<Ambito>();
		for (es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito ambito : ambitos) {
			nombreAmbito = "";
			tipoAmbito = "";
			Set<Traduccion> traducciones = ambito.getLiteral().getTraduccions();
			for (Traduccion traduccion : traducciones) {
				if (idioma.equalsIgnoreCase(traduccion.getIdioma())) {
					nombreAmbito = traduccion.getTexto();
					break;
				}
			}
			
			traducciones = ambito.getTipoambito().getLiteral().getTraduccions();
			
			for (Traduccion traduccion : traducciones) {
				if (idioma.equalsIgnoreCase(traduccion.getIdioma())) {
					tipoAmbito = traduccion.getTexto();
					break;
				}
			}
			
			resultado.add(new Ambito(ambito.getIden(),nombreAmbito, ambito.getTipoambito().getIden(),tipoAmbito));
		}
		
		return resultado.toArray(new Ambito[0]);
	}
	
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal#getRoles()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Rol[] getRoles() {
		List<es.mitc.redes.urbanismoenred.data.rpm.seguridad.Rol> rolesbd = (List<es.mitc.redes.urbanismoenred.data.rpm.seguridad.Rol>)em.createNamedQuery("Rol.findAll").getResultList();
		
		List<Rol> roles = new ArrayList<Rol>();
		
		for (es.mitc.redes.urbanismoenred.data.rpm.seguridad.Rol rol : rolesbd) {
			roles.add(new Rol(rol.getNombre(),rol.getCodigo()));
		}
		
		return roles.toArray(new Rol[0]);
	}

	/**
	 * 
	 * @return
	 */
	private Map<String, es.mitc.redes.urbanismoenred.data.rpm.seguridad.Rol> getRolesPorCodigo() {
		@SuppressWarnings("unchecked")
		List<es.mitc.redes.urbanismoenred.data.rpm.seguridad.Rol> rolesbd = (List<es.mitc.redes.urbanismoenred.data.rpm.seguridad.Rol>)em.createNamedQuery("Rol.findAll").getResultList();
		
		Map<String,es.mitc.redes.urbanismoenred.data.rpm.seguridad.Rol> rolesPorCodigo = new HashMap<String, es.mitc.redes.urbanismoenred.data.rpm.seguridad.Rol>();
		
		for (es.mitc.redes.urbanismoenred.data.rpm.seguridad.Rol rol : rolesbd) {
			rolesPorCodigo.put(rol.getCodigo(), rol);
		}
		
		return rolesPorCodigo;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal#getUsuario(int)
	 */
	@Override
	public Usuario getUsuario(int identificador) {
		es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario usuario = em.find(es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario.class, identificador);
		if (usuario != null) {
			return procesarUsuario(usuario);
		} else {
			return null;
		}
	}
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal#getUsuario(java.lang.String)
	 */
	@Override
	public Usuario getUsuario(String nombre) {
		try {
			return procesarUsuario((es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario)
					em.createNamedQuery("Usuario.findByNombre")
						.setParameter("nombre", nombre).getSingleResult());
		} catch (NonUniqueResultException ex) {
			log.warn("Entontrado mas de un usuario con nombre: " + nombre + ". Error:" + ex.getLocalizedMessage());
		} catch (NoResultException ex) {
			log.warn("No se encuentra el usuario con nombre: " + nombre + ". Error:" + ex.getLocalizedMessage());
		} catch (Exception ex) {
			log.warn("No se ha podido realizar la busqueda del usuario: " + nombre + ". Error:" + ex.getLocalizedMessage(), ex);
		}
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal#getUsuarioPorDNI(java.lang.String)
	 */
	@Override
	public Usuario getUsuarioPorDNI(String dni) {
		try {
			return procesarUsuario((es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario)
					em.createNamedQuery("Usuario.findByDNI")
						.setParameter("dni", dni).getSingleResult());
		} catch (NonUniqueResultException ex) {
			log.warn("Entontrado mas de un usuario con dni: " + dni + ". Error:" + ex.getLocalizedMessage());
		} catch (NoResultException ex) {
			log.warn("No se encuentra el usuario con dni: " + dni + ". Error:" + ex.getLocalizedMessage());
		} catch (Exception ex) {
			log.warn("No se ha podido realizar la busqueda del usuario: " + dni + ". Error:" + ex.getLocalizedMessage());
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal#getUsuarios()
	 */
	@Override
	public Usuario[] getUsuarios() {
		@SuppressWarnings("unchecked")
		List<es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario> usuarios = em.createNamedQuery("Usuario.findAll").getResultList();
		
		Usuario[] resultado = new Usuario[usuarios.size()];
		for (int i = 0; i < resultado.length; i++) {
			resultado[i] = procesarUsuario(usuarios.get(i));
		}
		return resultado;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal#guardarUsuario(es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario)
	 */
	@Override
	public void guardarUsuario(Usuario usuario) throws ExcepcionPersistencia {
		es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario existente = null;
		if (usuario.getIdentificador() > 0) {
			existente = em.find(es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario.class, usuario.getIdentificador());
			if (existente == null) {
				throw new ExcepcionPersistencia("No existe el usuario especificado en base de datos. Id: " + usuario.getIdentificador());
			}
			existente.setCorreo(usuario.getCorreo());
			existente.setDni(usuario.getDni());
			// Sólo permito el cambio de nombre si no existe ya un usuario con dicho nombre.
			if (existente.getNombre().equals(usuario.getNombre())) {
				try {
					if (((es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario) em.createNamedQuery("Usuario.findByNombre")
						.setParameter("nombre", usuario.getNombre()).getSingleResult()).getIden() != existente.getIden()) {
						throw new ExcepcionPersistencia("Ya existe otro usuario con nombre: " + usuario.getNombre());
					}
				} catch (NonUniqueResultException ex) {
					log.warn("Entontrado mas de un usuario con nombre: " + usuario.getNombre() + ". Error:" + ex.getLocalizedMessage());
					throw new ExcepcionPersistencia("Se ha encontrado más de un usuario con nombre " + usuario.getNombre());
				} catch (NoResultException ex) {
					log.info("No se encuentra el usuario con nombre: " + usuario.getNombre() + ". Se procede a modificar el nombre.");
				} 
			}
			existente.setNombre(usuario.getNombre());
			existente.setPasswordmd5(usuario.getClave());
		} else {
			existente = new es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario();
			existente.setCorreo(usuario.getCorreo());
			existente.setDni(usuario.getDni());
			existente.setNombre(usuario.getNombre());
			existente.setPasswordmd5(usuario.getClave());
			em.persist(existente);
			em.flush();
			usuario.setIdentificador(existente.getIden());
		}

		// Actualizamos los roles asociados al usuario
		List<Rol> roles = usuario.getRoles();
		Set<Usuariorol> setUsuRoles = existente.getUsuariorols();
		Map<String,Usuariorol> rolesUsuario = new HashMap<String, Usuariorol>();
		Map<String, es.mitc.redes.urbanismoenred.data.rpm.seguridad.Rol> rolesPorCodigo = getRolesPorCodigo();
		Usuariorol ur;
		if (setUsuRoles != null) {
			Iterator<Usuariorol> iterator = setUsuRoles.iterator();
			
			while (iterator.hasNext()) {
				ur = iterator.next();
				if (ur.getAmbito() != null) {
					rolesUsuario.put(ur.getRol().getCodigo()+"-"+ur.getAmbito().getIden(), ur);
				} else {
					rolesUsuario.put(ur.getRol().getCodigo(), ur);
				}
				
			}
		}
		
		for (Rol rol : roles) {
			Ambito[] ambitos = usuario.getAmbitos(rol.getCodigo());
			if (ambitos.length > 0) {
				for (int i = 0; i < ambitos.length; i++) {
					// Si tiene el rol asignado al ámbito se quita de la lista, si no se crea
					if (rolesUsuario.containsKey(rol.getCodigo()+"-"+ambitos[i].getCodigo())) {
						rolesUsuario.remove(rol.getCodigo()+"-"+ambitos[i].getCodigo());
					} else {
						ur = new Usuariorol();
						ur.setAmbito(em.find(es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito.class, ambitos[i].getCodigo()));
						ur.setRol(rolesPorCodigo.get(rol.getCodigo()));
						ur.setUsuario(existente);
						em.persist(ur);
					}
				}
			} else {
				// No tiene ámbito asociado
				if (rolesUsuario.containsKey(rol.getCodigo())) {
					rolesUsuario.remove(rol.getCodigo());
				} else {
					ur = new Usuariorol();
					ur.setRol(rolesPorCodigo.get(rol.getCodigo()));
					ur.setUsuario(existente);
					em.persist(ur);
				}
			}
		}
		
		// Todos los roles que hayan quedado en roles Usuario deben ser eliminados
		for (Usuariorol paraEliminar : rolesUsuario.values()) {
			em.remove(paraEliminar);
		}
	}

	/**
	 * 
	 * @param usuarioBD
	 * @return
	 */
	private Usuario procesarUsuario(es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario usuarioBD) {
		Set<Usuariorol> usuRoles = usuarioBD.getUsuariorols();
		
		Rol[] roles = getRoles();
		Map<String,Rol> rolesPorCodigo = new HashMap<String, Rol>();
		for (int i = 0; i < roles.length;i++) {
			rolesPorCodigo.put(roles[i].getCodigo(), roles[i]);
		}
		
		Map<Rol, List<Ambito>> rolesAmbito = new HashMap<Rol, List<Ambito>>();
		Ambito ambito;
		for (Usuariorol usurol : usuRoles) {
			if (!rolesAmbito.containsKey(rolesPorCodigo.get(usurol.getRol().getCodigo()))) {
				rolesAmbito.put(rolesPorCodigo.get(usurol.getRol().getCodigo())
						, new ArrayList<Ambito>());
			}
			
			// Un usuario puede tener un rol no asignado a ningún ámbito,
			// como es el caso del administrador.
			String nombre = null;
			if (usurol.getAmbito() != null) {
				Set<Traduccion> traducciones = usurol.getAmbito().getLiteral().getTraduccions();
				for (Traduccion traduccion : traducciones) {
					if ("es".equalsIgnoreCase(traduccion.getIdioma())) {
						nombre = traduccion.getTexto();
						break;
					}
				}
				traducciones = usurol.getAmbito().getTipoambito().getLiteral().getTraduccions();
				String tipoAmbito = "";
				for (Traduccion traduccion : traducciones) {
					if ("es".equalsIgnoreCase(traduccion.getIdioma())) {
						tipoAmbito = traduccion.getTexto();
						break;
					}
				}
				ambito = new Ambito(usurol.getAmbito().getIden(),
						nombre, 
						usurol.getAmbito().getTipoambito().getIden(),
						tipoAmbito
						);
				rolesAmbito.get(rolesPorCodigo.get(usurol.getRol().getCodigo()))
					.add(ambito);
			}
		}
		
		return new Usuario(
				usuarioBD.getIden(),
				usuarioBD.getNombre(),
				usuarioBD.getPasswordmd5(),
				usuarioBD.getCorreo(),
				usuarioBD.getDni(),
				rolesAmbito);
	}

}
