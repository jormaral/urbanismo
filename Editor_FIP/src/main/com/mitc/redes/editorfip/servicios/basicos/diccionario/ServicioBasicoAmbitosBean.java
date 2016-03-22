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

package com.mitc.redes.editorfip.servicios.basicos.diccionario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambitoshp;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.mapas.ServicioCoordenadasAmbito;

@Stateless

@Name("servicioBasicoAmbitos")
public class ServicioBasicoAmbitosBean implements ServicioBasicoAmbitos
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @In(create=true) FacesMessages facesMessages;
    
    @PersistenceContext
	EntityManager entityManager;
    
		
	@In(create = true, required = false)
	ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;
	
	@In(create = true, required = false)
	ServicioCoordenadasAmbito servicioCoordenadasAmbito;

    public void servicioBasicoAmbitos()
    {
        // implement your business logic here
        log.info("servicioBasicoAmbitos.servicioBasicoAmbitos() action called");
        statusMessages.add("servicioBasicoAmbitos");
    }

    public String ambitoString(int idAmbito) {

        String nombreAmbito ="";
        String queryNombreAmbito =  "SELECT trans.texto " +
                " FROM com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito ambito " +
                " JOIN ambito.literal.traduccions trans" +
                " WHERE ambito.iden=" +idAmbito +
                " AND trans.literal = ambito.literal" ;

        

         try {
            nombreAmbito = (String) entityManager.createQuery(queryNombreAmbito).getSingleResult();
        } catch (NoResultException e) {
            log.error("[ambitoString] No se ha encontrado el ambito." + e.getMessage());

        } catch (Exception ex) {
            log.error("[ambitoString] Error en la busqueda del ambito: " + ex.getMessage());

        }
        
        return nombreAmbito;
    }
    
    
    public List<ParIdentificadorTexto> findAmbitos() {

    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
    	
        String queryNombreAmbito =  "SELECT ambito.id, trans.texto " +
                " FROM com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito ambito " +
                " JOIN ambito.literal.traduccions trans" +
                " WHERE trans.literal = ambito.literal ORDER BY trans.texto ASC" ;

        
        
        try {
        	List<Object[]> ambitoList = (List<Object[]>) entityManager.createQuery(queryNombreAmbito).getResultList();
        	
        	for (Object[] amb : ambitoList) {
        		ParIdentificadorTexto item = new ParIdentificadorTexto((Integer)amb[0],(String)amb[1], "ambito");
        		result.add(item);
            }
        	
        } catch (NoResultException e) {
            log.error("[findAmbitos] No se ha encontrado el listado de ambitos." + e.getMessage());

        } catch (Exception ex) {
            log.error("[findAmbitos] Error en la busqueda del listado de ambitos: " + ex.getMessage());

        }
        
        return result;
    }
    
  
    
    public List<ParIdentificadorTexto> obtenerAmbitosRaices() {

    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
    	
        String queryNombreAmbito =  "select amb.iden,trans.texto "+
        							"from Ambito amb "+ 
									"join amb.literal.traduccions trans "+
									"where trans.literal = amb.literal and amb.iden not in "+
									"( "+
									"select oa.ambitoByIdambitohijo from Organigramaambito oa, Ambito a "+
									" where oa.ambitoByIdambitohijo = a.iden "+
									")"+
									"order by trans.texto ASC" ;

        
        
        try {
        	List<Object[]> ambitoList = (List<Object[]>) entityManager.createQuery(queryNombreAmbito).getResultList();
        	
        	for (Object[] amb : ambitoList) {
        		ParIdentificadorTexto item = new ParIdentificadorTexto((Integer)amb[0],(String)amb[1], "ambito");
        		
        		// En la raiz, cualquier nodo siempre va a tener hoja
    			item.setHoja(false);
        		result.add(item);
            }
        	
        } catch (NoResultException e) {
            log.error("[findAmbitos] No se ha encontrado el listado de ambitos raices." + e.getMessage());

        } catch (Exception ex) {
            log.error("[findAmbitos] Error en la busqueda del listado de ambitos raices: " + ex.getMessage());

        }
        
        return result;
    }
    
    public List<ParIdentificadorTexto> obtenerAmbitosHijos(int idAmbitoPadre) {
    	
    	log.info("[obtenerAmbitosHijos] Entra en metodo con idAmbitoPadre="+idAmbitoPadre);

    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
    	
        String queryNombreAmbito =  "select oa.ambitoByIdambitohijo.iden,trans.texto "+
							        "from Organigramaambito oa "+
							        "join oa.ambitoByIdambitohijo.literal.traduccions trans "+
							        "where trans.literal = oa.ambitoByIdambitohijo.literal and oa.ambitoByIdambitopadre =" +idAmbitoPadre + " " +
							        "order by trans.texto ASC" ;

        
        
        try {
        	List<Object[]> ambitoList = (List<Object[]>) entityManager.createQuery(queryNombreAmbito).getResultList();
        	
        	for (Object[] amb : ambitoList) {
        		ParIdentificadorTexto item = new ParIdentificadorTexto((Integer)amb[0],(String)amb[1], "ambito");
        		
        		// Compruebo si es una hoja (no tiene hijos)
    			item.setHoja(!tieneHija((Integer)amb[0]));
        		
    			result.add(item);
            }
        	
        } catch (NoResultException e) {
            log.error("[findAmbitos] No se ha encontrado el listado de ambitos raices." + e.getMessage());

        } catch (Exception ex) {
            log.error("[findAmbitos] Error en la busqueda del listado de ambitos raices: " + ex.getMessage());

        }
        
        log.info("[obtenerAmbitosHijos] Numero de ambitos hijo que encuentra="+result.size());
        return result;
    }
    
    private boolean tieneHija(int idAmbito)
    {
    	boolean resul = false;
    	
    	// Obtengo todas los ambitos hijos
    	String queryAmbitoHijo =  "select oa.iden "+
        "from Organigramaambito oa "+
        "where oa.ambitoByIdambitopadre =" +idAmbito;
		
		List<Entidad> listaTodosAmbitos = entityManager.createQuery(queryAmbitoHijo).getResultList();
    	
		if (listaTodosAmbitos.size()>0)
			resul = true;
    	
    	
    	return resul;
    }
    
    public List<ParIdentificadorTexto> listadoAmbitosShp() {

    	log.debug("[listadoAmbitosShp] Inicio.");
    	
        String nombreAmbito ="";
        String queryNombreAmbito =  "SELECT ambshp.idambito from diccionario.ambitoshp ambshp, diccionario.ambito amb, diccionario.literal l, diccionario.traduccion tra "+
					" where ambshp.idambito = amb.iden and amb.idliteral=l.iden and l.iden=tra.idliteral "+
					" order by tra.texto asc";

        List<Integer> lista = new ArrayList<Integer>();
        List<ParIdentificadorTexto> listaRes = new ArrayList<ParIdentificadorTexto>();
        

         try {
        	 lista = entityManager.createNativeQuery(queryNombreAmbito).getResultList();
        	 
        	 log.debug("[listadoAmbitosShp] lista="+lista.size());
        	 
        	 if (lista!=null && lista.size()>0)
        	 {
        		 Iterator it = lista.iterator();
        		 
        		 while(it.hasNext()){
        			 
        			 ParIdentificadorTexto par = new ParIdentificadorTexto();
        			 Integer idAmbito = (Integer)it.next();
        			 par.setTexto((ambitoString(idAmbito)));
        			 par.setIdBaseDatos(idAmbito);
        			 
        			 listaRes.add(par);
        		 }
        	 }
        	 
        } catch (NoResultException e) {
            log.error("[listadoAmbitosShp] No se ha encontrado el ambito." + e.getMessage());

        } catch (Exception ex) {
            log.error("[listadoAmbitosShp] Error en la busqueda del ambito: " + ex.getMessage());

        }
        
        return listaRes;
    }
    
    public void borrarGeomtriaAmbitosShp(int idAmbito) {

    	log.debug("[borrarGeomtriaAmbitosShp] Inicio.");
    	
        String nombreAmbito ="";
        String queryNombreAmbito =  "SELECT amb.iden " +
                " FROM Ambitoshp amb " +
                " WHERE amb.idambito = "+idAmbito;

        List<Integer> listaAmbitoShp = new ArrayList<Integer>();
       
        

         try {
        	 listaAmbitoShp = entityManager.createQuery(queryNombreAmbito).getResultList();
        	 
        	 log.debug("[borrarGeomtriaAmbitosShp] lista="+listaAmbitoShp.size());
        	 
        	 for (Integer ambshp : listaAmbitoShp)
        	 {
        		 entityManager.remove(entityManager.find(Ambitoshp.class, ambshp));
        		 log.debug("[borrarGeomtriaAmbitosShp] borrado ambito");
        	 }
        	 
        } catch (NoResultException e) {
            log.error("[borrarGeomtriaAmbitosShp] No se ha encontrado el ambito." + e.getMessage());

        } catch (Exception ex) {
            log.error("[borrarGeomtriaAmbitosShp] Error en la busqueda del ambito: " + ex.getMessage());

        }
        
      
    }
    
    
    public void guardarGeomtriaAmbitosShp(int idAmbito, String wKTOrigen) {

    	log.debug("[guardarGeomtriaAmbitosShp] Inicio. idAmbito="+idAmbito);
    	
        Ambitoshp ambshp = new Ambitoshp();
        ambshp.setIdambito(idAmbito);
        //ambshp.setGeom(wKTOrigen);
		
		//guardo el ambitoshp
		entityManager.persist(ambshp);
		entityManager.flush();
		
		String sql = "update diccionario.ambitoshp set geom=geometryfromtext('" + wKTOrigen  + "') where iden=" + String.valueOf(ambshp.getIden());
		entityManager.createNativeQuery(sql).executeUpdate(); 
		entityManager.flush(); 
		
		log.debug("[guardarGeomtriaAmbitosShp] Guardado. iden="+ambshp.getIden() +" idAmbito="+ambshp.getIden() +"wkb="+ambshp.getGeom());

       
        
        
      
    }
    
    public boolean tieneGeometriaAmbitoShp (int idAmbito)
    {
    	boolean resultado = false;
    	
    	String consulta = "select ambshp from Ambitoshp ambshp where ambshp.idambito="+idAmbito;
    	
    	try
    	{
    		List<Ambitoshp> lista = entityManager.createQuery(consulta).getResultList();
    		
    		if (lista.size()>0)
    		{
    			resultado = true;
    		}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	log.debug("[tieneGeometriaAmbitoShp] resultado="+resultado);
    	return resultado;
    }
    
    public int establecerGeometriaEntidadComoAmbitoShp (int idEntidad)
    {
    	int resultado = -1;
    	log.debug("[establecerGeometriaEntidadComoAmbitoShp] idEntidad="+idEntidad);
    	
    	// Cojo la geometria de la entidad (que sera poligonal)
    	String consulta = "select epol from Entidadpol epol where epol.entidad.iden="+idEntidad;
    	
    	try
    	{
    		Entidadpol ePol = (Entidadpol)entityManager.createQuery(consulta).getSingleResult();
    		
    		// Obtengo el ambito de la entidad
    		int idAmbito = ePol.getEntidad().getTramite().getPlan().getIdambito();
    		
    		Ambitoshp ambshp = new Ambitoshp();
    		
    		ambshp.setIdambito(idAmbito);
    		
    		//guardo el ambitoshp
    		entityManager.persist(ambshp);
    		entityManager.flush();
    		
    		String wktEntidad = ePol.getGeom().toText();
    		
    		String sql = "update diccionario.ambitoshp set geom=geometryfromtext('" + wktEntidad  + "') where iden=" + String.valueOf(ambshp.getIden());
    		entityManager.createNativeQuery(sql).executeUpdate(); 
    		entityManager.flush(); 
    		
    		resultado = ambshp.getIden();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		
    	}
    	
    	
    	
    	
    	log.debug("[establecerGeometriaEntidadComoAmbitoShp] resultado="+resultado);
    	return resultado;
    	
    }
    
    public int cargarAmbitoShpConGeometriaAmbitoTramite (int idTramite)
    {
    	int resultado = -1;

    	log.debug("[cargarAmbitoShpConGeometriaAmbitoTramite] idTramite="+idTramite);
    	
    	
    	  // -----
	    // FGA: Busco el Ambito de Aplicacion del Plan Vigente para ponerlo como ambitoshp en diccionario si no existiera previamente
	    // -----
	    
    	Tramite tram = entityManager.find(Tramite.class, idTramite);
    	
	    // Compruebo si existe el registro en ambitoshp
	    int idAmbitoAMeter=tram.getPlan().getIdambito();
	    if(tieneGeometriaAmbitoShp(idAmbitoAMeter))
	    {
	    	log.info(" El ambito del FIP1 ya tiene geoemtria en diccionario->ambitoshp. No hago nada");
	    }
	    else
	    {
	    	// No tiene geoemtria
	    	//Buscamos si el plan vigente tiene Ambito de Aplicacion
	    	log.info(" El ambito del FIP1 no tiene geoemtria en diccionario->ambitoshp. Buscamos si el plan vigente tiene Ambito de Aplicacion");
	    
	    	int entidadAmbitoAplicacion = 0;
	    	
	    	// Obtengo todas las entidades aplicadas al Ambito de Aplicacion 7000000002
	    	String codigoGrupo = "7000000002";
	    	List<Integer> listaEntidadesAA = servicioBasicoCondicionesUrbanisticas.entidadesDeTramiteAplicadasAUnGrupo(idTramite, codigoGrupo);
	    	
	    	for (Integer entidadAA:listaEntidadesAA)
	    	{
	    		// Cojo la geometria de la entidad (que sera poligonal)
	        	String consulta = "select epol from Entidadpol epol where epol.entidad.iden="+entidadAA;
	        	
	        	try
	        	{
	        		Entidadpol ePol = (Entidadpol)entityManager.createQuery(consulta).getSingleResult();
	        		
	        		entidadAmbitoAplicacion = entidadAA;
	        		break;
	        	}
	        	catch (Exception e)
	        	{
	        		log.info("ambito de aplicacion no tiene geometria. entidadAA="+entidadAA);
	        		
	        	}
	    	}
	    	
	    	
	    	
	    	if (entidadAmbitoAplicacion!=0)
	    	{
	    		// Si tiene ambito de aplicacion lo ponemos como ambitoshp
	    		int resultadoAmbitoshp = establecerGeometriaEntidadComoAmbitoShp(entidadAmbitoAplicacion);
	    		
	    		if (resultadoAmbitoshp>0)
	    		{
	    			// Todo OK
	    			// Lo meto en la tabla
	    			
	    			servicioCoordenadasAmbito.meterAmbitoEnTabla(idAmbitoAMeter);
	    			
	    			resultado = resultadoAmbitoshp;
	    			log.info("Se ha podido establecer el Ambitoshp a partir de la entidad Ambito Aplicacion del Plan Vigente");
	    		}
	    		else
	    		{
	    			log.error("No se ha podido establecer el Ambitoshp a partir de la entidad Ambito Aplicacion del Plan Vigente");
	    		}
	    		
	    		
	    	}
	    
	    
	    }
    	
    	
    	log.debug("[cargarAmbitoShpConGeometriaAmbitoTramite] resultado="+resultado);
    	return resultado;
    	
    }
    
    public int cargarAmbitoShpConGeometriaAmbitoPV (int idAmbito)
    {
    	int resultado = -1;

    	log.debug("[cargarAmbitoShpConGeometriaAmbitoPV] idAmbito="+idAmbito);
    	
    	
    	  // -----
	    // FGA: Busco el Ambito de Aplicacion del Plan Vigente para ponerlo como ambitoshp en diccionario si no existiera previamente
	    // -----
	    
    	
    	String consultaPE=" select pe from PlanesEncargados pe where pe.ambito.iden ="+idAmbito;
    	try
    	{
    		List<PlanesEncargados> peLista = (List<PlanesEncargados>) entityManager.createQuery(consultaPE).getResultList();
    		
    		// Cojo el primero:
    		int idTramite = peLista.get(0).getTramiteVigente().getIden();
        	
    	    // Compruebo si existe el registro en ambitoshp
    	    int idAmbitoAMeter=idAmbito;
    	    if(tieneGeometriaAmbitoShp(idAmbitoAMeter))
    	    {
    	    	log.info(" El ambito del FIP1 ya tiene geoemtria en diccionario->ambitoshp. No hago nada");
    	    }
    	    else
    	    {
    	    	// No tiene geoemtria
    	    	//Buscamos si el plan vigente tiene Ambito de Aplicacion
    	    	log.info(" El ambito del FIP1 no tiene geoemtria en diccionario->ambitoshp. Buscamos si el plan vigente tiene Ambito de Aplicacion");
    	    
    	    	int entidadAmbitoAplicacion = 0;
    	    	
    	    	// Obtengo todas las entidades aplicadas al Ambito de Aplicacion 7000000002
    	    	String codigoGrupo = "7000000002";
    	    	List<Integer> listaEntidadesAA = servicioBasicoCondicionesUrbanisticas.entidadesDeTramiteAplicadasAUnGrupo(idTramite, codigoGrupo);
    	    	
    	    	for (Integer entidadAA:listaEntidadesAA)
    	    	{
    	    		// Cojo la geometria de la entidad (que sera poligonal)
    	        	String consulta = "select epol from Entidadpol epol where epol.entidad.iden="+entidadAA;
    	        	
    	        	try
    	        	{
    	        		Entidadpol ePol = (Entidadpol)entityManager.createQuery(consulta).getSingleResult();
    	        		
    	        		entidadAmbitoAplicacion = entidadAA;
    	        		break;
    	        	}
    	        	catch (Exception e)
    	        	{
    	        		log.info("ambito de aplicacion no tiene geometria. entidadAA="+entidadAA);
    	        		
    	        	}
    	    	}
    	    	
    	    	
    	    	
    	    	if (entidadAmbitoAplicacion!=0)
    	    	{
    	    		// Si tiene ambito de aplicacion lo ponemos como ambitoshp
    	    		int resultadoAmbitoshp = establecerGeometriaEntidadComoAmbitoShp(entidadAmbitoAplicacion);
    	    		
    	    		if (resultadoAmbitoshp>0)
    	    		{
    	    			// Todo OK
    	    			// Lo meto en la tabla
    	    			
    	    			servicioCoordenadasAmbito.meterAmbitoEnTabla(idAmbitoAMeter);
    	    			
    	    			resultado = resultadoAmbitoshp;
    	    			log.info("Se ha podido establecer el Ambitoshp a partir de la entidad Ambito Aplicacion del Plan Vigente");
    	    			facesMessages.addFromResourceBundle(Severity.INFO, "Se ha cargado correctamente la geometria del Ambito", null); 

    	    		}
    	    		else
    	    		{
    	    			log.error("No se ha podido establecer el Ambitoshp a partir de la entidad Ambito Aplicacion del Plan Vigente");
    	    			facesMessages.addFromResourceBundle(Severity.ERROR, "No se ha podido guardar la geometria. Prueba manualmente en Administracion->Configuracion Visor", null); 

    	    		}
    	    		
    	    		
    	    	}
    	    	else
    	    	{
    	    		facesMessages.addFromResourceBundle(Severity.ERROR, "No se ha podido guardar la geometria. Prueba manualmente en Administracion->Configuracion Visor", null); 

    	    	}
    	    
    	    
    	    }
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		
    		facesMessages.addFromResourceBundle(Severity.ERROR, "No se ha podido guardar la geometria. Prueba manualmente en Administracion->Configuracion Visor", null); 
    	}
    	
    	
    	
    	
    	log.debug("[cargarAmbitoShpConGeometriaAmbitoTramite] resultado="+resultado);
    	return resultado;
    	
    }


}
