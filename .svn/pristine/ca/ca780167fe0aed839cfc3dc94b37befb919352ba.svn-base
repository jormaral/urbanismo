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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.Prerefundido;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoAmbitos;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;
import com.mitc.redes.editorfip.utilidades.Constantes;

@Stateless
@Name("listaInfoPrerefundido")
public class ListaInfoPrerefundidoBean extends SortableList implements ListaPrerefundido
{
   
	@Logger private Log log;

	@In(create = true, required = false)
    ServicioBasicoAmbitos servicioBasicoAmbitos;
	
	@In(create = true, required = false)
	ServicioBasicoPrerefundido servicioBasicoPrerefundido;
    
    @In
	VariablesSesionUsuario variablesSesionUsuario;
    
    private static final String RUTA_XML = System.getProperty("jboss.home.dir") + 
	File.separator +  "var" 
	+ File.separator + "FIPs.war"
	+ File.separator + "prerefundido";

    
   
    private static final String nombreColumnAmbito = Constantes.NOMBRE_COLUMNA_AMBITO;
	private List<Prerefundido> listaPrerefundido = null;
    

    
    public ListaInfoPrerefundidoBean() {
		super(nombreColumnAmbito);
		
	}

	@Override
	public boolean isDefaultAscending(String sortColumn) {
		return true;
	}

	
	@Override
	public void sort() {
		
		log.debug("[sort] Inicio");
		
		final Collator comparador = Collator.getInstance();
		comparador.setStrength(Collator.PRIMARY);
		
		
		Comparator<Object> comparator = new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
            	Prerefundido d1 = (Prerefundido) o1;
            	Prerefundido d2 = (Prerefundido) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(nombreColumnAmbito)) {
                	return ascending ? comparador.compare(servicioBasicoAmbitos.ambitoString(d1.getAmbito().getIden()), servicioBasicoAmbitos.ambitoString(d2.getAmbito().getIden())) : comparador.compare(servicioBasicoAmbitos.ambitoString(d2.getAmbito().getIden()), servicioBasicoAmbitos.ambitoString(d1.getAmbito().getIden()));
                } else {
               	
                        return 0;
                    
                }
            }
        };
        Collections.sort(listaPrerefundido, comparator);
        log.debug("[sort] Fin");

	}
	

    
    
	public List<Prerefundido> obtenerListaPrerefundido() {

		
		if(listaPrerefundido == null)
		{
			refrescarLista();
		}
		
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
	          sort();
	          oldSort = sortColumnName;
	          oldAscending = ascending;
	    }
	
		return listaPrerefundido;
	}
	
	
	public void refrescarLista()
	{

		log.debug("[refrescarLista] refresco la lista obtenerListaPrerefundido");
		
		listaPrerefundido = servicioBasicoPrerefundido.obtenerListaInfoPrerefundido(variablesSesionUsuario.getIdTramiteTrabajoActual());

	}
	
	public String getNombreColumnAmbito() {
		return nombreColumnAmbito;
	}
	
	public Resource obtenerFichero(String nombre) {
		
		Resource fichero = new Fichero(nombre);
		return fichero;
	}
    
	private class Fichero implements Resource, Serializable {

		private String nombre;
		private final Date lastModified;
	    private InputStream inputStream;
		
		public Fichero(String nombre) {
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
			
			File fichero = new File(RUTA_XML, nombre);
			
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

	@Override
	public boolean refundidoCorrecto(int idproceso) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String rutaFip(int idproceso) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String rutaLog(int idproceso) {
		// TODO Auto-generated method stub
		return null;
	}

}
