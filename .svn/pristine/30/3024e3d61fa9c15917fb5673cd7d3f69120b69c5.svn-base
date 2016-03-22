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

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.DetalleUsuario;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Usuario;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.UsuarioAmbito;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.UsuarioRol;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.UsuarioTramite;
import com.mitc.redes.editorfip.servicios.seam.EntidadHome;
import com.mitc.redes.editorfip.servicios.seam.TramiteHome;

@Name("usuarioHome")
public class UsuarioHome extends EntityHome<Usuario> {
	
	@In(create = true)
	EntidadHome entidadHome;
	@In(create = true)
	TramiteHome tramiteHome;

	public void setUsuarioIden(Integer id) {
		setId(id);
	}

	public Integer getEntidadIden() {
		return (Integer) getId();
	}

	@Override
	protected Usuario createInstance() {
		
		Usuario usuario = new Usuario();
		
		usuario.setRolesAsignados(new ArrayList<UsuarioRol>());
		usuario.setTramitesAsignados(new ArrayList<UsuarioTramite>());
		usuario.setAmbitosAsignados(new ArrayList<UsuarioAmbito>());
		
		DetalleUsuario detalle = new DetalleUsuario();
		usuario.setDetalle(detalle);
		
		setInstance(usuario);
		
		return usuario;
	}

	public Usuario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
}
