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
import java.util.List;
import java.util.Map;

/**
 * Clase que contiene la información referente a los usuarios del sistema
 * independientemente de la estructura utilizada para su persistencia.
 * 
 * @author jgarzon
 *
 */
public class Usuario {
	private static final String CODIGO_CONSOLIDACION = "CSD";
	private static final String CODIGO_VALIDACION = "VAL";
	
	private String clave;
	private String correo;

	private String dni;
	private int	identificador = -1;
	private String nombre;

	private List<Rol> roles = new ArrayList<Rol>();
	private Map<String, Map<Integer, Ambito>> rolesAmbito = new HashMap<String, Map<Integer, Ambito>>();
	
	public Usuario(int identificador, String nombre, String clave, String correo, String dni, Map<Rol,List<Ambito>> roles) {
		this.identificador = identificador;
		this.nombre = nombre;
		this.clave = clave;
		this.correo = correo;
		this.dni = dni;
		
		Map<Integer, Ambito> ambitosPorId;
		
		for (Rol rol : roles.keySet()) {
			this.roles.add(rol);
			if (!rolesAmbito.containsKey(rol.getCodigo())) {
				rolesAmbito.put(rol.getCodigo(), new HashMap<Integer, Ambito>());
			}
			ambitosPorId = rolesAmbito.get(rol.getCodigo());
			for (Ambito ambito : roles.get(rol)) {
				ambitosPorId.put(ambito.getCodigo(), ambito);
			}
		}
	}
	public Usuario( String nombre, String clave, String correo, String dni, Map<Rol,List<Ambito>> roles) {
		this.identificador = -1;
		this.nombre = nombre;
		this.clave = clave;
		this.correo = correo;
		this.dni = dni;
		
		Map<Integer, Ambito> ambitosPorId;
		
		for (Rol rol : roles.keySet()) {
			this.roles.add(rol);
			if (!rolesAmbito.containsKey(rol.getCodigo())) {
				rolesAmbito.put(rol.getCodigo(), new HashMap<Integer, Ambito>());
			}
			ambitosPorId = rolesAmbito.get(rol.getCodigo());
			for (Ambito ambito : roles.get(rol)) {
				ambitosPorId.put(ambito.getCodigo(), ambito);
			}
		}
	}
	
	public Ambito[] getAmbitos(String rol) {
		if (rolesAmbito.get(rol) != null) {
			return rolesAmbito.get(rol).values().toArray(new Ambito[0]);
		} else {
			return new Ambito[0];
		}
	}
	
	public String getClave() {
		return clave;
	}
	
	public String getCorreo() {
		return correo;
	}
	
	public String getDni() {
		return dni;
	}
	
	public int getIdentificador() {
		return identificador;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public List<Rol> getRoles() {
		return roles;
	}
	
	public boolean puedeConsolidar(int ambito) {
		if (rolesAmbito.containsKey(CODIGO_CONSOLIDACION)) {
			if (rolesAmbito.get(CODIGO_CONSOLIDACION).containsKey(ambito)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean puedeValidar(int ambito) {
		if (rolesAmbito.containsKey(CODIGO_VALIDACION)) {
			if (rolesAmbito.get(CODIGO_VALIDACION).containsKey(ambito)) {
				return true;
			}
		} 
		
		return false;
	}
	
	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}
	
	public boolean tieneRol(String rol) {
		return rolesAmbito.containsKey(rol);
	}
	
	public boolean tieneRol(String rol, int ambito) {
		
		if (rolesAmbito.containsKey(rol)) {
			if (rolesAmbito.get(rol).containsKey(ambito)) {
				return true;
			}
		} 
		
		return false;
	}
}
