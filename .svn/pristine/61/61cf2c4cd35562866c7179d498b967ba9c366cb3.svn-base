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

package com.mitc.redes.editorfip.entidades.rpm.administracionusuarios;

import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("usuario")
@Scope(SESSION)
@Table(name="usuario", schema = "administracionusuarios")
public class Usuario implements Serializable {

	private String username;
	private String clave;
	private boolean dadoBaja=false;
	private String dni;
	private DetalleUsuario detalle;
	private List<UsuarioRol> rolesAsignados = new ArrayList<UsuarioRol>();	
	private List<UsuarioTramite> tramitesAsignados = new ArrayList<UsuarioTramite>();	
	private List<UsuarioAmbito> ambitosAsignados = new ArrayList<UsuarioAmbito>();


	
	
	public Usuario() {
		super();
		
	}
		
	public Usuario(String username, String clave) {
		super();
		this.username = username;
		this.clave = clave;
	}


	@Id
	@Length(min = 4, max = 150)
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

	@NotNull
	@Length(min = 5, max = 150)
	public String getClave() {
		return clave;
	}

	public void setClave(String password) {
		this.clave = password;
	}
	
	
	
	public boolean isDadoBaja() {
		return dadoBaja;
	}

	public void setDadoBaja(boolean dadoBaja) {
		this.dadoBaja = dadoBaja;
	}

	
	@Length(min = 5, max = 9)
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "usuario")
	public DetalleUsuario getDetalle() {
		return detalle;
	}

	public void setDetalle(DetalleUsuario detalle) {
		this.detalle = detalle;
	}

		
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public List<UsuarioRol> getRolesAsignados() {
		return rolesAsignados;
	}

	public void setRolesAsignados(List<UsuarioRol> rolesAsignados) {
		this.rolesAsignados = rolesAsignados;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public List<UsuarioTramite> getTramitesAsignados() {
		return tramitesAsignados;
	}

	public void setTramitesAsignados(List<UsuarioTramite> tramitesAsignados) {
		this.tramitesAsignados = tramitesAsignados;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public List<UsuarioAmbito> getAmbitosAsignados() {
		return ambitosAsignados;
	}

	public void setAmbitosAsignados(List<UsuarioAmbito> ambitosAsignados) {
		this.ambitosAsignados = ambitosAsignados;
	}

	@Override
	public String toString() {
		return "Usuario(" + username + ")";
	}
	
	@Transient
	public String getRoles() {
		String roles = "";
		
		if(rolesAsignados != null && !rolesAsignados.isEmpty()) {
			roles = rolesAsignados.get(0).getRol().getNombre();
			for(int i=1; i<rolesAsignados.size();i++) {
				roles += ", " + rolesAsignados.get(i).getRol().getNombre();
			}
		}
		
		return roles;
	}

}
