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

package com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.TokenUtils;
import com.mitc.redes.editorfip.servicios.basicos.comunes.ServicioBasicoGeneral;
import com.mitc.redes.editorfip.servicios.basicos.comunes.plantillas.ListaGenericaBean;
import com.mitc.redes.editorfip.servicios.gestionfip.importarfip.ImportadorFIP;
import com.mitc.redes.editorfip.utilidades.Constantes;
import com.mitc.redes.editorfip.utilidades.Textos;

@Stateful
@Name ("servicioListadoFips")
public class ListadoFipsBean extends ListaGenericaBean implements ListadoFips{

	
	
	@PersistenceContext
	EntityManager entityManager;
	
	@In(create=true) FacesMessages facesMessages;
	@In (create = true) ServicioBasicoGeneral servicioBasicoGeneral;
	@In Credentials credentials;
	@Logger private Log log;
	@RequestParameter private Long identificador;
	
	@In(create = true, required = false)
	ImportadorFIP importadorFIP;
	
	// Creacion del contexto
    @Resource
    private SessionContext contextoTransaccion;
	
	
	
	
	private FipsCargados fipCargado;
	private HashMap<Long,Boolean> seleccion;
	private boolean estaBloqueado;
	private boolean puedeBloquear;
	private boolean puedeDesbloquear;
	private boolean puedeConsolidar;
	private String idSeleccionado;
	private boolean mostrarPanelFip;
	private boolean mostrarPanelBloquear;
	private boolean mostrarPanelInformacion;
	private boolean mostrarBotonesImportar;
	
	
	
	
	/**
	 * 	OBTENER EL LISTADO DE TODOS LOS FIPS
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<FipsCargados> getListado() {

		log.info("Obteniendo lista de FIPs encargados ...");
		
		/** Codigo para el seleccionado de usuarios por CheckBox **/
		try {
			super.setOrderColumn("fechaIntroduccion desc");
			super.inicializarLista("com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<FipsCargados> lista = (List<FipsCargados>) super.listaInicial;
		
		seleccion = new HashMap<Long,Boolean>();
		if(lista!=null) {
			for(FipsCargados temp : lista) {
				seleccion.put(temp.getId(), Boolean.FALSE);
			}
		}
		
		return (List<FipsCargados>) super.listaInicial;
	}

	/**
	 *	MOSTRAR POPUP INFO
	 * 
	 */
	public void mostrarInfo() {
		
		log.info("Obteniendo fip seleccionado, iden: " + identificador);
		this.fipCargado = entityManager.find(FipsCargados.class, identificador);
		
		if (fipCargado == null) {
			facesMessages.addFromResourceBundle("fip1_no_encontrado", null);
			
		} else {
			mostrarPanelInformacion = true;
		}
	}
	

	public void borrarRegistroFIPCargado(int idFipCargadoBorrar)
	{
		log.info("borrarRegistroFIPCargado, idFipCargadoBorrar: " + idFipCargadoBorrar);
		
		this.fipCargado = entityManager.find(FipsCargados.class, new Long(idFipCargadoBorrar));
		
		try
		{
			entityManager.remove(fipCargado);	
			entityManager.flush();
			facesMessages.addFromResourceBundle(Severity.INFO,"El Registro de FIP Cargado ha sido borrado correctamente", null);
		} 
		catch (Exception e) {
			facesMessages.addFromResourceBundle(Severity.ERROR,"Se ha producido un error al borrar el Registro de FIP Cargado: "+fipCargado.getIdentificador(), null);
			e.printStackTrace();
		} 
		
	}


	
	/**
	 * 	DAR DE BAJA A UN FIP
	 * 
	 */
	@Override
	public void darDebaja() {
		
		
		List<FipsCargados> lista = (List<FipsCargados>)super.listaInicial;
		// List<Usuario> listaUsr = (List<Usuario>) super.getObjetosSeleccionados();
		Integer numSeleccionados = 0;
		for(Iterator<Boolean> it = seleccion.values().iterator(); it.hasNext();) {
			if(it.next()) {
				numSeleccionados++;
			}
		}  
		
		if(numSeleccionados == 0) {
			facesMessages.addFromResourceBundle("elemento_almenosuno", null);
		} else {
			for(FipsCargados temp : lista) {			
				try {
					Boolean seleccionado = seleccion.get(temp.getId());
					if(seleccionado) {
						FipsCargados planTemp = entityManager.find(FipsCargados.class,temp.getId());
						//planTemp.setDadoBaja(true);
						
					    File file = new File(planTemp.getRuta() + File.separator + planTemp.getIdentificador());  
					    
						entityManager.remove(planTemp);	
						entityManager.flush();
						
						file.delete();  
						
						facesMessages.addFromResourceBundle("elemento_dadodebajaok", null);
					}				
					
				} catch (Exception e) {
					facesMessages.addFromResourceBundle("planencargado_errordardebaja", null);
					e.printStackTrace();
				} 
			}
			getListado();
		}
		
	}
	

	
	
	/**
	 * 	DESBLOQUEAR
	 * 
	 */
	public void desbloquearConsolidado() {
			TokenUtils token = servicioBasicoGeneral.obtenerTokenUtilPorNombre(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
			token.setBloqueado(false);
			token.setUsuario("");
			entityManager.merge(token);
			entityManager.flush();
			
			facesMessages.addFromResourceBundle(Severity.INFO, "servicio_desbloqueado", null);
	}
	
	
	/**
	 * 	BLOQUEAR
	 * 
	 */
	public void bloquearConsolidado() {
			
			TokenUtils token = servicioBasicoGeneral.obtenerTokenUtilPorNombre(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
			token.setBloqueado(true);
			token.setUsuario(credentials.getUsername());
			entityManager.merge(token);
			entityManager.flush();
			
			facesMessages.addFromResourceBundle(Severity.INFO, "servicio_bloqueado", null);
			
			mostrarPanelFip = true;
			mostrarPanelBloquear = false;
	}
	
	public boolean isPuedeBloquear()
	{
		TokenUtils token = servicioBasicoGeneral.obtenerTokenUtilPorNombre(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
		
		if (token==null) {
			// Creamos el token vacio
			token = new TokenUtils();
			token.setBloqueado(false);
			token.setIdentificador(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
			entityManager.persist(token);
			token.setUsuario("");
			entityManager.flush();
			
			return true;
		}
		
		return (token!=null && !token.isBloqueado());
	}
	
	public void setPuedeBloquear(boolean puedeBloquear) {
		this.puedeBloquear = puedeBloquear;
	}

	public boolean isPuedeDesbloquear()
	{
		TokenUtils token = servicioBasicoGeneral.obtenerTokenUtilPorNombre(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
		
		if (token==null)
		{
			// Creamos el token vacio
			token = new TokenUtils();
			token.setBloqueado(false);
			token.setIdentificador(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
			token.setUsuario("");
			entityManager.persist(token);
			entityManager.flush();
			
			return false;
		}
		
		if (token!=null && token.isBloqueado() && token.getUsuario().equals(credentials.getUsername()))
		{
			return true;
		}
		else
			return false;
	}

	public boolean isPuedeConsolidar()
	{
		TokenUtils token = servicioBasicoGeneral.obtenerTokenUtilPorNombre(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
		
		if (token==null)
		{
			// Creamos el token vacio
			token = new TokenUtils();
			token.setBloqueado(false);
			token.setIdentificador(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
			token.setUsuario("");
			entityManager.persist(token);
		}
		
		if (token.isBloqueado() && token.getUsuario().equals(credentials.getUsername()))
		{
			return true;
		}
		else
			return false;
	}
	
	public boolean isEstaBloqueado()
	{
		TokenUtils token = servicioBasicoGeneral.obtenerTokenUtilPorNombre(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
		
		if (token!=null && token.isBloqueado())
		{
			this.estaBloqueado=true;
			return true;
		}
		else{
			this.estaBloqueado=false;
			return false;
		}
	}
	
	public void setEstaBloqueado(boolean estaBloqueado) {
		this.estaBloqueado = estaBloqueado;
	}

	public String getUsuarioBloqueante()
	{
		TokenUtils token = servicioBasicoGeneral.obtenerTokenUtilPorNombre(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
		
		if (token!=null && token.isBloqueado())
		{
			return Textos.getCadena("servicio_bloqueado_por") + " " + token.getUsuario();
		}
		else
			return "";
	}
	
	public void desbloquearServicio()
	{
		TokenUtils token = servicioBasicoGeneral.obtenerTokenUtilPorNombre(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
		
		if (token==null)
		{
			// Creamos el token vacio
			token = new TokenUtils();
			token.setBloqueado(false);
			token.setIdentificador(Constantes.NOMBRE_TOKEN_CONSOLIDAR);
			token.setUsuario("");
			entityManager.persist(token);
			entityManager.flush();
		}
		else
		{
			token.setBloqueado(false);
			token.setUsuario("");
			entityManager.merge(token);
			entityManager.flush();
		}
	}
	
	/*
	public void importacionSincrona()
	{		
		mostrarPanelFip = false;
		
		ListadoFipsBean source = (ListadoFipsBean)Component.getInstance("listadoFipsBean");
		source.importacionAsincrona(fipCargado.getId());
		
		importacionAsincrona(fipCargado.getId());
		log.debug("ACABAMOS EL MÉTODO");
	}
	*/
	
	
	

	/**
	 * 	COMENZAR PROCESO DE IMPORTACIîN
	 * 
	 * 	Se lanza cuando se pulsa el bot—n de importar de algœn FIP
	 * 
	 */
	public void importarFipSeleccionado() {
		
		log.info("Abrir popup de importar FIP1, iden: " + identificador);
		
		if (isPuedeConsolidar() || true) {

			log.info("Mostrar popup para consolidar");
			this.fipCargado = entityManager.find(FipsCargados.class, identificador);
			mostrarPanelFip = true;
			mostrarPanelBloquear = false;
			mostrarPanelInformacion = false;
			mostrarBotonesImportar = true;
		
		} else if (isPuedeBloquear()) {
			
			log.info("Mostrar popup para bloquear");
			this.fipCargado = entityManager.find(FipsCargados.class, identificador);
			mostrarPanelBloquear = true;
		
		}
	}
	
	


	    
    public void cargarFipSeleccionado()
    {
		this.fipCargado = entityManager.find(FipsCargados.class, new Long(2887783));
		mostrarPanelFip = true;
    }
	public FipsCargados getFipCargado() {
		return fipCargado;
	}
	public void setFipCargado(FipsCargados fipCargado) {
		this.fipCargado = fipCargado;
	}
	public boolean isMostrarPanelFip() {
		return mostrarPanelFip;
	}
	public void setMostrarPanelFip(boolean mostrarPanelFip) {
		this.mostrarPanelFip = mostrarPanelFip;
		this.mostrarBotonesImportar = !mostrarPanelFip;
	}
	public boolean isMostrarPanelBloquear() {
		return mostrarPanelBloquear;
	}
	public void setMostrarPanelBloquear(boolean mostrarPanelBloquear) {
		this.mostrarPanelBloquear = mostrarPanelBloquear;
	}
	public boolean isMostrarPanelInformacion() {
		return mostrarPanelInformacion;
	}
	public void setMostrarPanelInformacion(boolean mostrarPanelInformacion) {
		this.mostrarPanelInformacion = mostrarPanelInformacion;
	}
	public Long getIdentificador() {
		return identificador;
	}
	public void setIdentificador(Long identificador) {
		this.identificador = identificador;
	}
	public boolean isMostrarBotonesImportar() {
		return !mostrarPanelFip;
	}
	public void setMostrarBotonesImportar(boolean mostrarBotonesImportar) {
		this.mostrarBotonesImportar = mostrarBotonesImportar;
	}
	public HashMap getSeleccion() {
		return seleccion;
	}
	public void setSeleccion(HashMap seleccion) {
		this.seleccion = seleccion;
	}
	public String getIdSeleccionado() {
		return idSeleccionado;
	}
	public void setIdSeleccionado(String idSeleccionado) {
		this.idSeleccionado = idSeleccionado;
	}
	public void setPuedeDesbloquear(boolean puedeDesbloquear) {
		this.puedeDesbloquear = puedeDesbloquear;
	}
	public void setPuedeConsolidar(boolean puedeConsolidar) {
		this.puedeConsolidar = puedeConsolidar;
	}
	@Remove @Destroy
	public void remove() {		
	}
}