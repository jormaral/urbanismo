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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.Prerefundido;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoAmbitos;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;
import com.mitc.redes.editorfip.utilidades.Constantes;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Archivo;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Proceso;

@Stateless
@Name("listaPrerefundido")
public class ListaPrerefundidoBean extends SortableList implements ListaPrerefundido
{
   
	@Logger private Log log;
	
	@PersistenceContext
	EntityManager em;

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
    

    
    public ListaPrerefundidoBean() {
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
	
/*
	public void comprobarFinalizado()
	{
		log.info("comprobarFinalizado");
		try
		{
			String queryPreref = "from Prerefundido pre where pre.consolidado is false ";
			List<Prerefundido> prefList = (List<Prerefundido>) em.createQuery(queryPreref).getResultList();
			
			if (prefList.size()!=0)
			{
				for (Prerefundido pref : prefList)
				{
					String querySiguiente = "from Tramite tram where tram.idtipotramite=11 " +
							"and tram.iden > "+pref.getIdultimotramprerefundido() +" order by tram.iden asc";
					
					List<Tramite> tramList = (List<Tramite>) em.createQuery(querySiguiente).getResultList();
					
					if (tramList.size()!=0)
					{
						// Si hay tramites, es que ha terminado y el id seria el primero
						int idTramPrerefTerminado = tramList.get(0).getIden();
						
						// Y ahora actualizo Prerefundido
						pref.setIdTramitePrerefundido(idTramPrerefTerminado);
						pref.setConsolidado(true);
						
						em.merge(pref);
						
						
					}
					
				}
			}
		}
		catch (Exception e)
		{
			log.error("Error al comprobarFinalizado");
			e.printStackTrace();
		}
		
	}
	*/
	
	public String rutaLog (int idproceso)
	{
		String resultado = "";
		
		log.info("[rutaLog] idproceso="+idproceso);
		
		String query = "from es.mitc.redes.urbanismoenred.data.rpm.refundido.Archivo arc where arc.tipo = 'LOG' and arc.proceso = "+idproceso;
		
		try
		{
			Archivo arch = (Archivo)em.createQuery(query).getSingleResult();
			resultado = arch.getContenido();
			
		}
		catch (Exception e)
		{
			log.error("Error rutaLog");
			e.printStackTrace();
		}
		
		
		return resultado;
		
	}
	
	public String rutaFip (int idproceso)
	{
		String resultado = "";
		
		log.info("[rutaFip] idproceso="+idproceso);
		
		String query = "from es.mitc.redes.urbanismoenred.data.rpm.refundido.Archivo arc where arc.tipo = 'FIP' and arc.proceso = "+idproceso;
		
		try
		{
			Archivo arch = (Archivo)em.createQuery(query).getSingleResult();
			resultado = arch.getContenido();
			
		}
		catch (Exception e)
		{
			log.error("Error rutaLog");
			e.printStackTrace();
		}
		
		
		return resultado;
		
	}
	
	public boolean refundidoCorrecto (int idproceso)
	{
		boolean resultado = false;
		
		log.info("[refundidoCorrecto] idproceso="+idproceso);
		
		
		
		try
		{
			Proceso proc = (Proceso) em.find(Proceso.class, idproceso);
			resultado = proc.getCorrecto();
			
		}
		catch (Exception e)
		{
			log.error("Error refundidoCorrecto");
			e.printStackTrace();
		}
		
		
		return resultado;
		
	}
	
	
	public void comprobarFinalizado()
	{
		log.info("comprobarFinalizado");
		try
		{
			// Tengo que coger lo que tengan idproceso a cero
			
			String queryPreref = "from Prerefundido pre where pre.idprocesorefundido = 0 ";
			
			// Obtengo el Listado
			List<Prerefundido> prefList = (List<Prerefundido>) em.createQuery(queryPreref).getResultList();
			
			// Por cada uno de los que pueda obtener, tengo que comprobar las fechas para intentar relacionarlos
			for (Prerefundido pre : prefList)
			{
				
				//Obtengo la fecha
				pre.getFechaInicio();
				
				// La comparo con la tabla Proceso 
				// Tengo que coger las que son mayores de esa fecha y coger el primero de la lista
				
				String queryProc = "from es.mitc.redes.urbanismoenred.data.rpm.refundido.Proceso pro" +
						" where pro.inicio > '"+pre.getFechaInicio()+"' order by pro.inicio ASC";
				
				
				Proceso proc = ((List<Proceso>)em.createQuery(queryProc).getResultList()).get(0);
				
				pre.setIdprocesorefundido(proc.getIden());
				
				// Tengo que poner tambien el idtramiteprerefundido
				if (pre.getIdTramitePrerefundido()==-1)
				{
					String querySiguiente = "from Tramite tram where tram.idtipotramite=11 " +
					"and tram.iden > "+pre.getIdultimotramprerefundido() +" order by tram.iden asc";
			
					List<Tramite> tramList = (List<Tramite>) em.createQuery(querySiguiente).getResultList();
					
					if (tramList.size()!=0)
					{
						// Si hay tramites, es que ha terminado y el id seria el primero
						int idTramPrerefTerminado = tramList.get(0).getIden();
						
						// Y ahora actualizo Prerefundido
						pre.setIdTramitePrerefundido(idTramPrerefTerminado);
						
						
						
						
						
					}
				}
				
		
				
				em.merge(pre);
				em.flush();
				
			}
			
			// Tengo que comprobar tb por si se han dejado con getIdTramitePrerefundido = -1
			String queryPreref2 = "from Prerefundido pre where pre.idTramitePrerefundido = -1 order by pre.id ASC";
			
			// Obtengo el Listado
			List<Prerefundido> prefList2 = (List<Prerefundido>) em.createQuery(queryPreref2).getResultList();
			
			for (Prerefundido pre : prefList2)
			{

				String querySiguiente = "from Tramite tram where tram.idtipotramite=11 " +
				"and tram.iden > "+pre.getIdultimotramprerefundido() +" order by tram.iden asc";
		
				List<Tramite> tramList = (List<Tramite>) em.createQuery(querySiguiente).getResultList();
				
				if (tramList.size()!=0)
				{
					// Si hay tramites, es que ha terminado y el id seria el primero
					int idTramPrerefTerminado = tramList.get(0).getIden();
					
					// Y ahora actualizo Prerefundido
					pre.setIdTramitePrerefundido(idTramPrerefTerminado);
									
					
				}
				
				em.merge(pre);
				em.flush();
			}
			
			
		}
		catch (Exception e)
		{
			log.error("Error al comprobarFinalizado");
			e.printStackTrace();
		}
		
	}
	
	/*
	public void comprobarFinalizado()
	{
		log.info("comprobarFinalizado");
		try
		{
			// Tengo que coger lo que tengan idproceso a cero
			
			String queryPreref = "from Prerefundido pre where pre.consolidado is false ";
			List<Prerefundido> prefList = (List<Prerefundido>) em.createQuery(queryPreref).getResultList();
			
			if (prefList.size()!=0)
			{
				for (Prerefundido pref : prefList)
				{
					String querySiguiente = "from Tramite tram where tram.idtipotramite=11 " +
							"and tram.iden > "+pref.getIdultimotramprerefundido() +" order by tram.iden asc";
					
					List<Tramite> tramList = (List<Tramite>) em.createQuery(querySiguiente).getResultList();
					
					if (tramList.size()!=0)
					{
						// Si hay tramites, es que ha terminado y el id seria el primero
						int idTramPrerefTerminado = tramList.get(0).getIden();
						
						// Y ahora actualizo Prerefundido
						pref.setIdTramitePrerefundido(idTramPrerefTerminado);
						pref.setConsolidado(true);
						
						em.merge(pref);
						
						
					}
					
				}
			}
		}
		catch (Exception e)
		{
			log.error("Error al comprobarFinalizado");
			e.printStackTrace();
		}
		
	}
	*/
    
	public List<Prerefundido> obtenerListaPrerefundido() {

		
		if(listaPrerefundido == null)
		{
			
			refrescarLista();
		}
		
		
		
		/*
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
	          sort();
	          oldSort = sortColumnName;
	          oldAscending = ascending;
	    }
	    */
	
		return listaPrerefundido;
	}
	
	
	public void refrescarLista()
	{

		comprobarFinalizado();
		
		log.debug("[refrescarLista] refresco la lista obtenerListaPrerefundido");
		
		listaPrerefundido = servicioBasicoPrerefundido.obtenerListaPrerefundido();

	}
	
	public String getNombreColumnAmbito() {
		return nombreColumnAmbito;
	}
	
	public Resource obtenerFichero(String nombre) {
		
		Resource fichero = new FicheroPreRef(nombre);
		return fichero;
	}
    
	private class FicheroPreRef implements Resource, Serializable {

		private String nombre;
		private final Date lastModified;
	    private InputStream inputStream;
		
		public FicheroPreRef(String nombre) {
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
