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

package com.mitc.redes.editorfip.servicios.informacionfip.documentonormas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.DocumentosNormativaGenerados;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("servicioListadoDocumentoNormas")
public class ServicioListadoDocumentoNormasBean implements ServicioListadoDocumentoNormas
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    @In
    VariablesSesionUsuario variablesSesionUsuario;
    
    List<DocumentosNormativaGenerados> listadoDocumentoNormativa = null;
    
    public void refrescarListado()
    {
    	int idTramiteVigente = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
    	int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
    	
    	String queryListadoNormas = " select normas from DocumentosNormativaGenerados normas where normas.idTramiteDocumentoNormativa="
    		+idTramiteVigente+" or normas.idTramiteDocumentoNormativa="+idTramiteEncargado+" order by normas.fechaGeneracion desc";
    	listadoDocumentoNormativa = em.createQuery(queryListadoNormas).getResultList();
    	
    	log.info("[getListadoDocumentoNormativa] Elementos listadoDocumentoNormativa="+listadoDocumentoNormativa.size());
    	
    }

   
	public List<DocumentosNormativaGenerados> getListadoDocumentoNormativa() {
		
		if (listadoDocumentoNormativa == null)
		{
			log.info("[getListadoDocumentoNormativa] Refresco Listado");
			refrescarListado();
		}
		return listadoDocumentoNormativa;
	}
	
	public void setListadoDocumentoNormativa(List<DocumentosNormativaGenerados> listadoDocumentoNormativa) {
		this.listadoDocumentoNormativa = listadoDocumentoNormativa;
	}
	   
   
	public Resource obtenerFichero(String rutanombre) {
		
		Resource fichero = new FicheroURL(rutanombre);
		return fichero;
	}
    
	private class FicheroURL implements Resource, Serializable {

		private String nombre;
		private final Date lastModified;
	    private InputStream inputStream;
		
		public FicheroURL(String nombre) {
			this.nombre = nombre;
			this.lastModified = new Date();
		}

		@Override
		public String calculateDigest() {
			return nombre;
		}

		@Override
		public Date lastModified() {
			return lastModified;
		}

		@Override
		public InputStream open() throws IOException {
			
			File fichero = new File(nombre);
			
			if(fichero.exists()) {
				inputStream = new FileInputStream(fichero);
			} else {
				inputStream = new FileInputStream(new File("Fichero_Incorrecto.info"));
			}		
			
			return inputStream;
		}

		@Override
		public void withOptions(Options arg0) throws IOException {			
		}
		
	}
	  
}


