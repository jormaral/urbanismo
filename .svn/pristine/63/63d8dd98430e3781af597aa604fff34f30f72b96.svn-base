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

package com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaDeterminacionDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionReguladoraTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.GrupoAplicacionTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegulacionEspecificaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.ValorReferenciaTramiteDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Propiedadrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuarioBean;




@Stateless
@Name("servicioBasicoDeterminaciones")
public class ServicioBasicoDeterminacionesBean implements ServicioBasicoDeterminaciones
{
    @Logger private Log log;

 
    @PersistenceContext
	EntityManager em;

    // -----------------------
    // Arbol de determinaciones
    // -----------------------
    
   
	public List<ParIdentificadorTexto> getDeterminacionesRaices(int idTramite) {
		List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();

		String hql = "SELECT d " + "FROM Determinacion d "
				+ "WHERE d.determinacionByIdpadre IS NULL AND d.tramite.iden ="
				+ idTramite + " " + "ORDER BY d.orden, d.nombre   ASC";
		List<Determinacion> ldet = em.createQuery(hql).getResultList();

		for (Determinacion det : ldet) {

			String textoArbol = "";

			if (det.getApartado() != "" & det.getApartado() != null & det.getApartado() != "null") {
				textoArbol = det.getApartado() + " - " + det.getNombre();
			} else {
				textoArbol = det.getNombre();
			}

			ParIdentificadorTexto item = new ParIdentificadorTexto(det
					.getIden(), textoArbol, "determinacion");

			// Compruebo si es una hoja (no tiene hijos)
			item.setHoja(!tieneHija(det.getIden()));
			result.add(item);

		}

		return result;
	}

    
	public List<ParIdentificadorTexto> getDeterminacionesHijasDeDet(
			int idDetPadre) {
		List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();

		String hql = "SELECT d " + "FROM Determinacion d "
				+ "WHERE d.determinacionByIdpadre.iden = " + idDetPadre + " "
				+ "ORDER BY d.orden,d.nombre  ASC";
		List<Determinacion> ldet = em.createQuery(hql).getResultList();

		for (Determinacion det : ldet) {

			String textoArbol = "";

			if (det.getApartado() != "" & det.getApartado() != null & det.getApartado() != "null") {
				textoArbol = det.getApartado() + " - " + det.getNombre();
			} else {
				textoArbol = det.getNombre();
			}

			ParIdentificadorTexto item = new ParIdentificadorTexto(det
					.getIden(), textoArbol,
					"determinacion");
			// Compruebo si es una hoja (no tiene hijos)
			item.setHoja(!tieneHija(det.getIden()));

			result.add(item);

		}

		return result;
	}
    
    
    private boolean tieneHija(int idEntidad)
    {
    	boolean resul = false;
    	
    	// Obtengo todas las entidades hijas
		String queryTodasEntidades = "SELECT d " +
        "FROM Determinacion d " +
        "WHERE d.determinacionByIdpadre.iden = " + idEntidad;
		
		List<Determinacion> listaTodasEntidades = em.createQuery(queryTodasEntidades).getResultList();
    	
		if (listaTodasEntidades.size()>0)
			resul = true;
    	
    	
    	return resul;
    }
    
    
    // -----------------------
    // GENERICAS
    // -----------------------
    
  
    public String caracterString(int idCaracter) {

        String nombreCaracter ="";
        String queryNombreCaracter =  "SELECT trans.texto " +
                " FROM Caracterdeterminacion ambito " +
                " JOIN ambito.literal.traduccions trans" +
                " WHERE ambito.iden=" +idCaracter +
                " AND trans.literal = ambito.literal" ;

        

         try {
            nombreCaracter = (String) em.createQuery(queryNombreCaracter).getSingleResult();
        } catch (NoResultException e) {
            log.error("[caracterString] No se ha encontrado el Caracter." + e.getMessage());

        } catch (Exception ex) {
            log.error("[caracterString] Error en la busqueda del Caracter: " + ex.getMessage());

        }
        
        return nombreCaracter;
    }
    
    
    public List<Object[]> findCaracterTexto() {

		List<Object[]> lista = new ArrayList<Object[]>();
		
		String queryCaracterDet = "SELECT carac.iden,trans.texto " +
        " FROM Caracterdeterminacion carac " + 
        " JOIN carac.literal.traduccions trans" +
        " WHERE trans.literal = carac.literal ORDER BY carac.iden ASC";
		
		lista = em.createQuery(queryCaracterDet).getResultList();
        
        return lista;
	}
    
 
    public List<SelectItem> findCaracterTextoSelectItem() {

    	List<Object[]> elementos = findCaracterTexto();
		
    	List<SelectItem> res = new ArrayList<SelectItem>();
		for (Object[] c : elementos) {
			SelectItem i = new SelectItem();
			i.setLabel((String) c[1]);
			i.setValue(c[0]);
			res.add(i);
		}
		
		return res;
	}
    
   
    public Determinacion buscarDeterminacion(Object id) {
    	
    	Determinacion det = new Determinacion();
    	
    	det = em.find(Determinacion.class, id);
    	
    	// Cargo los valores de determinacion padre y determinacion base
    	if (det!=null && det.getDeterminacionByIddeterminacionbase()!=null)
    	{
    		det.getDeterminacionByIddeterminacionbase().getNombre();
    	}
    	
    	if (det!=null && det.getDeterminacionByIdpadre()!=null)
    	{
    		det.getDeterminacionByIdpadre().getNombre();
    	}
    	
    	return det;
    }
    
    public int buscarDeterminacionPorTramiteYOrden (int idTramite, String apartado)
    {
    	int idDet=0;
    	
    	String[] numApartados = apartado.split("\\.");
		String queryNueva="";
		
		// Construyo la query para que rescate la determinacion a partir del apartado
		for (int i=(numApartados.length-1); i>-1; i--)
		{
			if (i==0)
			{
				queryNueva+= " SELECT d.iden " +
		         " FROM Determinacion d " +
		         " WHERE d.determinacionByIdpadre IS NULL AND d.tramite.iden = "+idTramite+
		         " AND d.orden = '"+numApartados[0]+"'";
				
				for (int j=0; j<numApartados.length-1; j++)
				{
					queryNueva+= ")";
				}
			}
			else
			{
				queryNueva+=" SELECT d"+i+".iden " +
		         " FROM Determinacion d"+i+" " +
		         " WHERE  d"+i+".tramite.iden = "+idTramite+" AND d"+i+".orden = '"+numApartados[i]+"' " +
		         " AND d"+i+".determinacionByIdpadre = (";
			}
		}
    	
		// Lanzo la query para que encuentre la determinacion
    	try
    	{
    		log.warn("CONSULTA DETERMINACION A BUSCAR: " + apartado + "--" + queryNueva);
    		idDet = (Integer)em.createQuery(queryNueva).getSingleResult();
    		if (idDet==0)
    			log.warn("CONSULTA DETERMINACION NO ENCONTRADA: " + queryNueva);
    	}
    	catch (Exception ex)
    	{
    		log.warn("[buscarDeterminacionPorTramiteYOrden] Fallo al buscar  la determinacion con idTramite="+idTramite+" y orden="+apartado +" ERROR:"+ex.getMessage());
    		ex.printStackTrace();
    	}
    	
    	
    	
    	return idDet;
    }
    
    
    public String obtenerOrdenCompletoDeterminacion (int idDeterminacion)
    {
    	String resultado ="";
    	
    	// Obtengo la determinacion
    	Determinacion detOriginal = buscarDeterminacion(idDeterminacion);
    	
    	String resultadoAux="";
    	
    	while (detOriginal!=null)
    	{
    		// Si tiene solo una cifra, añado un cero para que posteriormente se pueda ordenar bien
    		if (detOriginal.getOrden()<10)
    		{
    			resultadoAux+="0"+detOriginal.getOrden()+".";
    		}
    		else
    		{
    			resultadoAux+=detOriginal.getOrden()+".";	
    		}
    		
    		
    		if (detOriginal.getDeterminacionByIdpadre()!=null)
    		{
    			detOriginal = detOriginal.getDeterminacionByIdpadre();
    		}
    		else
    		{
    			detOriginal = null;
    		}
    		
    	}
    	
    	// Ahora tengo que poner el orden en el otro sentido
    	String[] numApartados = resultadoAux.split("\\.");
    	
    	for (int i=(numApartados.length-1); i>-1; i--)
		{
    		resultado+=numApartados[i]+".";
		}
    	
    	return resultado;
    }
    
    // ------------------------------
    // DETERMINACIONES BASE
    // ------------------------------
    
    public List<DeterminacionDTO> obtenerDeterminacionesBase (int idDeterminacion, int idTramiteBase)
    {
    	log.debug("[obtenerDeterminacionesBase] Se van a obtener las determinaciones base para el Tramite Base="+idTramiteBase + " y determinacion="+idDeterminacion);

    	List<DeterminacionDTO> listaDeterminacionesBase=null;
    	
    	// Averiguo el tipo de la determinacion ya que las determinaciones base solo van a poder ser de ese tipo
    	
    	Determinacion detSel = buscarDeterminacion(idDeterminacion);
    	
    	
    	int idTipoDeterminacion = detSel.getIdcaracter();
    	
    	String query =  "SELECT det " +
		" FROM Determinacion det " +
		" WHERE det.idcaracter = "+idTipoDeterminacion+
		" AND det.tramite.iden = "+idTramiteBase+
		" ORDER BY det.nombre ASC";
		
    	 try {
         	
             List<Determinacion> detBaseList = (List<Determinacion>) em.createQuery(query).getResultList();
             
             listaDeterminacionesBase = new ArrayList<DeterminacionDTO>(detBaseList.size());
             
             for (Determinacion det : detBaseList)
             {
            	 DeterminacionDTO unTram = new DeterminacionDTO();
            	 
            	 unTram.setSeleccionada(false);
            	 unTram.setIdDeterminacion(det.getIden());
            	 unTram.setNombreDeterminacion(det.getNombre());
            	 unTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
            	 
            	 listaDeterminacionesBase.add(unTram);
             }
            
             
            
         } catch (NoResultException e) {
             log.error("[obtenerDeterminacionesBase] No se han encontrado Determinaciones para el tramite base:"+idTramiteBase+" y tipo="+idTipoDeterminacion+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerDeterminacionesBase] Error en la busqueda de Determinaciones para el tramite base:"+idTramiteBase+" y tipo="+idTipoDeterminacion+"\n" + ex.getMessage());

         }
    	
    	
         log.debug("[obtenerDeterminacionesBase] Se devuelven "+listaDeterminacionesBase.size()+" Determinaciones para el Tramite Base: idTramite="+idTramiteBase+" y tipo="+idTipoDeterminacion);
    	return listaDeterminacionesBase;
    }
    
    //----------------
    // UNIDADES
    //----------------
    
    
    public List<UnidadTramiteDTO> obtenerUnidadTramite (int idTramite)
    {
    	
    	log.debug("[obtenerUnidadTramite] Se van a obtener los Unidad para el idTramite="+idTramite);

    	List<UnidadTramiteDTO> listaUNTramite=null;
    	
    	// Cojo por defecto del caracter de valores de referencia
    	int idCaracterUnidadDiccionario = 18;
    	
    	String query =  "SELECT det " +
		" FROM Determinacion det " +
		" WHERE det.idcaracter = "+idCaracterUnidadDiccionario+
		" AND det.tramite.iden = "+idTramite;
		
    	 try {
         	
             List<Determinacion> detUnidadList = (List<Determinacion>) em.createQuery(query).getResultList();
             
             listaUNTramite = new ArrayList<UnidadTramiteDTO>(detUnidadList.size());
             
             for (Determinacion det : detUnidadList)
             {
            	 UnidadTramiteDTO unTram = new UnidadTramiteDTO();
            	 
            	 unTram.setSeleccionada(false);
            	 unTram.setIdDeterminacion(det.getIden());
            	 unTram.setNombreDeterminacion(det.getNombre());
            	 unTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
            	 
            	 listaUNTramite.add(unTram);
             }
            
             
            
         } catch (NoResultException e) {
             log.error("[obtenerUnidadTramite] No se han encontrado Unidad para el tramite:"+idTramite+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerUnidadTramite] Error en la busqueda de Unidad para el tramite:"+idTramite+"\n" + ex.getMessage());

         }
    	
    	
         log.debug("[obtenerUnidadTramite] Se devuelven "+listaUNTramite.size()+" Unidad para el idTramite="+idTramite);
    	return listaUNTramite;
    }
    
    public UnidadTramiteDTO obtenerUnidadNoProcedeTramite (int idTramite)
    {
    	
    	log.debug("[obtenerUnidadNoProcedeTramite] Se van a obtener la Unidad 'No Procede' para el idTramite="+idTramite);

    	// Cojo por defecto del caracter de valores de referencia
    	int idCaracterUnidadDiccionario = 18;
    	
    	String query =  "SELECT det " +
		" FROM Determinacion det " +
		" WHERE det.idcaracter = "+idCaracterUnidadDiccionario+
		" AND det.tramite.iden = "+idTramite + 
    	" AND LOWER(det.nombre) like 'no procede'";
		
    	 try {
         	
             List<Determinacion> detUnidadList = (List<Determinacion>) em.createQuery(query).getResultList();
             
             UnidadTramiteDTO unTram = new UnidadTramiteDTO();
             
             if (detUnidadList!=null && detUnidadList.size()>0)
             {	
            	 Determinacion det = new Determinacion();
            	 det = detUnidadList.get(0);
            	 unTram.setSeleccionada(false);
            	 unTram.setIdDeterminacion(det.getIden());
            	 unTram.setNombreDeterminacion(det.getNombre());
            	 
            	 log.debug("[obtenerUnidadNoProcedeTramite] Encontrada la Unidad 'No Procede' con id="+det.getIden());

            	
            	 return unTram;
             }
             else
            	 return null;
            
             
            
         } catch (NoResultException e) {
             log.error("[obtenerUnidadTramite] No se han encontrado Unidad para el tramite:"+idTramite+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerUnidadTramite] Error en la busqueda de Unidad para el tramite:"+idTramite+"\n" + ex.getMessage());

         }
         return null;
    }
    
    public ParIdentificadorTexto obtenerUnidadDeDeterminacion (int idDetContenedora)
    {
    	// Instancio la variable a devolver, por si no encuentra ningun resultado que devuelva la variable inicializada.
    	ParIdentificadorTexto parIdTexto = new ParIdentificadorTexto();
    	
    	Vectorrelacion vectorRelUnidad = null;
    	
    	
		String cadena = "SELECT vectorrelac2 " +
        " FROM Vectorrelacion vectorrelac, " +
        "		Vectorrelacion vectorrelac2" +           
        " WHERE vectorrelac.iddefvector=2 " +
        " AND vectorrelac.valor = "+idDetContenedora+
        " AND vectorrelac.relacion =vectorrelac2.relacion " +
        " AND vectorrelac2.iddefvector=3" ;
		
		
		
		try {
			
			vectorRelUnidad = (Vectorrelacion)em.createQuery(cadena).getSingleResult();
			
			// Busco la determinacion unidad asignada para sacar el nombre
			Determinacion det = buscarDeterminacion(vectorRelUnidad.getValor());
			
			if (det!=null)
			{
				// Guardo los valores en la variable a devolver
				parIdTexto.setIdBaseDatos(det.getIden());
				parIdTexto.setTexto(det.getNombre());
			}
			
					
			
		} catch (NoResultException e) {
            log.debug("[obtenerUnidadDeDeterminacion] No se han encontrado unidades para la determinacion:"+ idDetContenedora+"." + e.getMessage());

        } catch (Exception ex) {
            log.error("[obtenerUnidadDeDeterminacion] Error en la busqueda de unidades para la determinacion: "+ idDetContenedora+"." +ex.getMessage());

        }
		
		return parIdTexto;
    }
    
    public boolean tieneUnidadDeDeterminacion (int idDetContenedora)
    {
    	// Instancio la variable a devolver, por si no encuentra ningun resultado que devuelva la variable inicializada.
    	ParIdentificadorTexto parIdTexto = new ParIdentificadorTexto();
    	
    	Vectorrelacion vectorRelUnidad = null;
    	
    	
		String cadena = "SELECT vectorrelac2 " +
        " FROM Vectorrelacion vectorrelac, " +
        "		Vectorrelacion vectorrelac2" +           
        " WHERE vectorrelac.iddefvector=2 " +
        " AND vectorrelac.valor = "+idDetContenedora+
        " AND vectorrelac.relacion =vectorrelac2.relacion " +
        " AND vectorrelac2.iddefvector=3" ;
		
		
		
		try {
			
			vectorRelUnidad = (Vectorrelacion)em.createQuery(cadena).getSingleResult();
			
			// Busco la determinacion unidad asignada para sacar el nombre
			Determinacion det = buscarDeterminacion(vectorRelUnidad.getValor());
			
			if (det!=null)
			{
				return true;
			}
			
					
			
		} catch (NoResultException e) {
            log.debug("[obtenerUnidadDeDeterminacion] No se han encontrado unidades para la determinacion:"+ idDetContenedora+"." + e.getMessage());

        } catch (Exception ex) {
            log.error("[obtenerUnidadDeDeterminacion] Error en la busqueda de unidades para la determinacion: "+ idDetContenedora+"." +ex.getMessage());

        }
		
		return false;
    }
    
    public ParIdentificadorTexto obtenerPlandeUnidadDeDeterminacion (int idDetContenedora)
    {
    	// Instancio la variable a devolver, por si no encuentra ningun resultado que devuelva la variable inicializada.
    	ParIdentificadorTexto parIdTexto = new ParIdentificadorTexto();
    	
    	Vectorrelacion vectorRelUnidad = null;
    	
    	
		String cadena = "SELECT vectorrelac2 " +
        " FROM Vectorrelacion vectorrelac, " +
        "		Vectorrelacion vectorrelac2" +           
        " WHERE vectorrelac.iddefvector=2 " +
        " AND vectorrelac.valor = "+idDetContenedora+
        " AND vectorrelac.relacion =vectorrelac2.relacion " +
        " AND vectorrelac2.iddefvector=3" ;
		
		
		
		try {
			
			vectorRelUnidad = (Vectorrelacion)em.createQuery(cadena).getSingleResult();
			
			// Busco la determinacion unidad asignada para sacar el nombre
			
			String cadenaDet = "SELECT determinacion " +
	        " FROM Determinacion determinacion " +
	        " WHERE determinacion.iden=" + vectorRelUnidad.getValor();
			
			
			Determinacion det = (Determinacion)em.createQuery(cadenaDet).getSingleResult();
			
			if (det!=null)
			{
				// Guardo los valores en la variable a devolver
				parIdTexto.setIdBaseDatos(det.getIden());
				parIdTexto.setTexto(det.getTramite().getPlan().getNombre());
			}
			
					
			
		} catch (NoResultException e) {
            log.debug("[obtenerPlandeUnidadDeDeterminacion] No se han encontrado unidades para la determinacion:"+ idDetContenedora+"." + e.getMessage());

        } catch (Exception ex) {
            log.error("[obtenerPlandeUnidadDeDeterminacion] Error en la busqueda de unidades para la determinacion: "+ idDetContenedora+"." +ex.getMessage());

        }
		
		return parIdTexto;
    }
    
 
    public boolean guardarUnidadDeDeterminacion (int idDetContenedora, int idDetUnidadContenida)
    {
    	// Instancio la variable a devolver, por si no se borra de forma correcta que devuelva la variable inicializada.
    	boolean guardadocorrecto = false;
    	
    	Relacion relacionRPManadir = new Relacion();
    	
        Vectorrelacion VectorrelacionRPManadir = new Vectorrelacion();
        Vectorrelacion VectorrelacionRPManadir2 = new Vectorrelacion();

    	
    	
		// Obtengo las determinaciones tanto contenedoras como contenidas
		try {
			
			// Busco la determinacion contenedora
			Determinacion detContenedora = buscarDeterminacion(idDetContenedora);
			
			// Busco la determinacion unidad asignada 
			Determinacion detUnidadContenida = buscarDeterminacion(idDetUnidadContenida);
			
			// relacion
            relacionRPManadir.setIddefrelacion(2);
            relacionRPManadir.setTramiteByIdtramitecreador(detContenedora.getTramite());
            em.persist(relacionRPManadir);
            log.debug("--    CREADA Relacion " + relacionRPManadir.getIden());

            //vectorRelacion
            VectorrelacionRPManadir.setRelacion(relacionRPManadir);
            VectorrelacionRPManadir.setIddefvector(2);
            VectorrelacionRPManadir.setValor(detContenedora.getIden());
            em.persist(VectorrelacionRPManadir);
            log.debug("--    CREADA vector1 " + VectorrelacionRPManadir.getIden());

            VectorrelacionRPManadir2.setRelacion(relacionRPManadir);
            VectorrelacionRPManadir2.setIddefvector(3);
            VectorrelacionRPManadir2.setValor(detUnidadContenida.getIden());
            em.persist(VectorrelacionRPManadir2);
            log.debug("--    CREADA vector2 " + VectorrelacionRPManadir2.getIden());
            
            guardadocorrecto = true;
					
			
		} catch (NoResultException e) {
            log.debug("[obtenerUnidadDeDeterminacion] No se han encontrado unidades para la determinacion:"+ idDetContenedora+"." + e.getMessage());

        } catch (Exception ex) {
            log.error("[obtenerUnidadDeDeterminacion] Error en la busqueda de unidades para la determinacion: "+ idDetContenedora+"." +ex.getMessage());

        }
		
		return guardadocorrecto;
    }
    
  
    public boolean borrarUnidadDeDeterminacion (int idDetContenedora)
    {
    	// Instancio la variable a devolver, por si no se borra de forma correcta que devuelva la variable inicializada.
    	boolean borradocorrecto = false;
    	
    	Vectorrelacion vectorRelUnidad = null;
    	
    	
		String cadena = "SELECT vectorrelac2 " +
        " FROM Vectorrelacion vectorrelac, " +
        "		Vectorrelacion vectorrelac2" +           
        " WHERE vectorrelac.iddefvector=2 " +
        " AND vectorrelac.valor = "+idDetContenedora+
        " AND vectorrelac.relacion =vectorrelac2.relacion " +
        " AND vectorrelac2.iddefvector=3" ;
		
		
		
		try {
			
			vectorRelUnidad = (Vectorrelacion)em.createQuery(cadena).getSingleResult();
			
			// Borro el vector relacion que contiene a la determinacion unidad contenida
			em.remove(vectorRelUnidad);
			
			// Busco ahora la relacion que contiene a la determinacion unidad contenedora
			String cadena2 = "SELECT vectorrelac " +
	        " FROM Vectorrelacion vectorrelac " +       
	        " WHERE vectorrelac.iddefvector=2 " +
	        " AND vectorrelac.valor = "+idDetContenedora;
			
			
			vectorRelUnidad = (Vectorrelacion)em.createQuery(cadena2).getSingleResult();
			
			// Obtengo el objeto relacion de ese vector
			Relacion rel = vectorRelUnidad.getRelacion();
			
						
			// Borro el vector relacion que contiene a la determinacion unidad contenida
			em.remove(vectorRelUnidad);
			
			// Borro la relacion que contiene a la determinacion unidad contenida y contenedora
			em.remove(rel);
			
			
			borradocorrecto = true;
					
			
		} catch (NoResultException e) {
            log.debug("[obtenerUnidadDeDeterminacion] No se han encontrado unidades para la determinacion:"+ idDetContenedora+"." + e.getMessage());

        } catch (Exception ex) {
            log.error("[obtenerUnidadDeDeterminacion] Error en la busqueda de unidades para la determinacion: "+ idDetContenedora+"." +ex.getMessage());

        }
		
		return borradocorrecto;
    }
   
    
    // ------------------------------
    // DOCUMENTOS
    // ------------------------------
    
    
    public List<Documento> obtenerListaDocumentosDeterminacion(String idDeterminacion) {

        log.debug("[obtenerListaDocumentosDeterminacion] Obtener Lista de Documentos de la Determinacion [" + idDeterminacion + "]");

        // Variable a devolver
        List<Documento> listaValores = null;

        

        // Obtengo el listado
        String query= " SELECT docsdet.documento "+
     	" FROM Determinacion det " +
     	" JOIN det.documentodeterminacions docsdet "+
     	" WHERE det.iden = "+idDeterminacion;

        
        try {
            listaValores = (List<Documento>) em.createQuery(query).getResultList();
            
            /**
             * Tengo que recuperar manualmente los campos que vaya a necesitar de
             * objetos relacionados, vara evitar excepcion Lazy
             */
            for (Documento doc : listaValores)
            {
            	doc.getTramite().getIden();
            	if (doc.getDocumento()!=null)
            		doc.getDocumento().getIden();
            }
        } catch (NoResultException e) {
            log.error("[obtenerListaDocumentosDeterminacion] No se ha encontrado el listado de Documentos de la Determinacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[obtenerListaDocumentosDeterminacion] Error en la busqueda del listado de Documentos de la Determinacion: " + ex.getMessage());

        }

        log.debug("[obtenerListaDocumentosDeterminacion] Obtenida Lista Documentos  de la Determinacion [" + idDeterminacion + "]. Obtenidas " + listaValores.size() + " documentos");
        return listaValores;

    }
    
   
    public DocumentoDTO[] obtenerArrayDocumentoDeterminacionDTO(String idDeterminacion) {

        log.debug("[obtenerArrayDocumentoDeterminacionDTO] Obtener Lista de DocumentoDTO de la Determinacion [" + idDeterminacion + "]");

        // Variable a recorrer
        List<Documento> listaValores = obtenerListaDocumentosDeterminacion(idDeterminacion);
        // Variable a devolver
        DocumentoDTO[] arrayReturn = new DocumentoDTO[listaValores.size()];

        int i = 0;
        for (Iterator<Documento> iterator = listaValores.iterator(); iterator.hasNext();) {
			
        	Documento doc = (Documento) iterator.next();
			
        	int iden = doc.getIden();;
        	String nombre = doc.getNombre();
        	String grupoDocumento = "";
        	String tipoDocumento = "";
        	int escala = 0;
        	if (doc.getEscala()!=null)
        		escala = doc.getEscala();
        	String comentario = doc.getComentario();
        	
        	String archivo ="";
        	if (doc.getArchivo()!=null)
        	{
        		archivo = doc.getArchivo().substring(doc.getArchivo().lastIndexOf('/') + 1);
        		
        	}
        	
        	
        	if (doc.getIdtipodocumento()!=null)
        		tipoDocumento = tipoDocumentoNombre(doc.getIdtipodocumento());
        	if (doc.getIdgrupodocumento()!=null)
        		grupoDocumento = grupoDocumentoNombre(doc.getIdgrupodocumento());
        				
			DocumentoDTO odDTO = new DocumentoDTO(iden, nombre, grupoDocumento,
					tipoDocumento, escala, comentario, archivo, "");
			arrayReturn[i] = odDTO;		
			i++;
		}
        
        log.debug("[obtenerArrayDocumentoDeterminacionDTO] Obtenida Lista de Documentos DTO de la Determinacion [" + idDeterminacion + "]. Obtenidas " + arrayReturn.length + " Documentos");
        return arrayReturn;

    }
    
   
    public String tipoDocumentoNombre(int id) {

        String nombre ="";
        String queryNombreAmbito =  "SELECT trans.texto " +
                " FROM Tipodocumento tod " +
                " JOIN tod.literal.traduccions trans" +
                " WHERE tod=" +id +
                " AND trans.literal = tod.literal" ;

       

         try {
            nombre = (String) em.createQuery(queryNombreAmbito).getSingleResult();
        } catch (NoResultException e) {
            log.error("[ambitoString] No se ha encontrado el Tipodocumento." + e.getMessage());

        } catch (Exception ex) {
            log.error("[ambitoString] Error en la busqueda del Tipodocumento: " + ex.getMessage());

        }
        
        return nombre;
    }
    
   
    public String grupoDocumentoNombre(int id) {

        String nombre ="";
        String queryNombreAmbito =  "SELECT trans.texto " +
                " FROM Grupodocumento tod " +
                " JOIN tod.literal.traduccions trans" +
                " WHERE tod=" +id +
                " AND trans.literal = tod.literal" ;

      

         try {
            nombre = (String) em.createQuery(queryNombreAmbito).getSingleResult();
        } catch (NoResultException e) {
            log.error("[ambitoString] No se ha encontrado el Grupodocumento." + e.getMessage());

        } catch (Exception ex) {
            log.error("[ambitoString] Error en la busqueda del Grupodocumento: " + ex.getMessage());

        }
        
        return nombre;
    }
    
    
    // ------------------------------
    // GRUPOS APLICACION
    // ------------------------------

    public Determinacion[] getArrayGruposAplicacion(int idDetTrabajo) {
		
		List<Determinaciongrupoentidad> resultList = new ArrayList<Determinaciongrupoentidad>();
		 Determinacion[] result = null;
		
		String query =  "SELECT opDet " +
    	" FROM Determinaciongrupoentidad opDet"+
    	" WHERE opDet.determinacionByIddeterminacion="+idDetTrabajo +
    	" ORDER BY opDet.determinacionByIddeterminaciongrupo.nombre ASC";
		
		try {
        	
            resultList = (List<Determinaciongrupoentidad>) em.createQuery(query).getResultList();
            
            result = new Determinacion[resultList.size()];
            
            // De cada opcion extraigo la determinacion valor de referencia
            int i = 0;
            
            for (Determinaciongrupoentidad grupoEntidad : resultList) {
            	
            	Determinacion detGE = grupoEntidad.getDeterminacionByIddeterminaciongrupo();
            	
            	// Pillo valores necesarios para evitar Lazy
            	detGE.getTramite().getIden();
            	detGE.getTramite().getNombre();
            	detGE.getTramite().getPlan().getNombre();
             	
              	result[i] = detGE;
              	i++;
    		}
            
        } catch (NoResultException e) {
            log.error("[getArrayGruposAplicacion] No se ha encontrado el Grupo Aplicacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[getArrayGruposAplicacion] Error en la busqueda del Grupo Aplicacion: " + ex.getMessage());

        }
        
        
        return result;
	}
    
 // ------------------------------
    // GRUPOS APLICACION
    // ------------------------------

    public boolean determinacionAplicadaPorGrupo(int idDetTrabajo, int idDetGrupo) {
		
		List<Determinaciongrupoentidad> resultList = new ArrayList<Determinaciongrupoentidad>();
		 Determinacion[] result = null;
		
		String query =  "SELECT opDet " +
    	" FROM Determinaciongrupoentidad opDet"+
    	" WHERE opDet.determinacionByIddeterminacion.iden="+idDetTrabajo +
    	" AND opDet.determinacionByIddeterminaciongrupo.iden=" + idDetGrupo;
		
		try {
        	
            resultList = (List<Determinaciongrupoentidad>) em.createQuery(query).getResultList();
            if (resultList!=null && resultList.size()>0)
            	return true;
            else
            	return false;
            
        } catch (NoResultException e) {
            log.error("[determinacionAplicadaPorGrupo] No se ha encontrado el Grupo Aplicacion." + e.getMessage());
        } catch (Exception ex) {
            log.error("[determinacionAplicadaPorGrupo] Error en la busqueda del Grupo Aplicacion: " + ex.getMessage());
        }
        
        return false;
	}
    
    public void borrarRelacionDeterminacionConGrupoAplicacion(int idDeterminacion, int idGrupoAplicacion) {
		
		Determinaciongrupoentidad result = null;
     	
    	
     	String query =  "SELECT opDet " +
    	" FROM Determinaciongrupoentidad opDet"+
    	" WHERE opDet.determinacionByIddeterminacion="+idDeterminacion+
    	" AND opDet.determinacionByIddeterminaciongrupo="+idGrupoAplicacion;

        
        
        try {
        	// No deberia haber mas de uno pero si se han equivocado al insertar puede fallar con singleResult
        	// Borro uno cualquiera (el primero), da igual pues es simplemente una relacion
            result = (Determinaciongrupoentidad) em.createQuery(query).getResultList().get(0);
            em.remove(em.merge(result));
        } catch (NoResultException e) {
            log.error("[removeByDeterminacionAndValorReferencia] No se ha encontrado el Grupo Aplicacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[removeByDeterminacionAndValorReferencia] Error en la busqueda del Grupo Aplicacion: " + ex.getMessage());

        }
		
	}
    
    public Determinacion buscarDeterminacionBase(int idDeterminacion) {
		
		List<Determinacion> result = null;
     	
    	
     	String query =  "SELECT det " +
    	" FROM Determinacion det"+
    	" WHERE det.determinacionByIddeterminacionbase.iden="+idDeterminacion;

        result = (List<Determinacion>) em.createQuery(query).getResultList();
        
        if (result!=null && result.size()>0)
        	return result.get(0);
        else
        	return null;
            		
	}
    
    public void guardarGrupoAplicacion(int idDeterminacion, int idGrupoAplicacion)
	{
		
		
		Determinacion determinacion = buscarDeterminacion(new Integer(idDeterminacion));
		Determinacion grupoAplicacion = buscarDeterminacion(new Integer(idGrupoAplicacion));
		
		Determinaciongrupoentidad grupoaplicacion = new Determinaciongrupoentidad();
		

		
		grupoaplicacion.setDeterminacionByIddeterminacion(determinacion);
		grupoaplicacion.setDeterminacionByIddeterminaciongrupo(grupoAplicacion);
		
		em.persist(grupoaplicacion);
		
	}
    
    public List<GrupoAplicacionTramiteDTO> obtenerGruposAplicacionTramite (int idTramite)
    {
    	log.debug("[obtenerGruposAplicacionTramite] Se van a obtener los GruposAplicacion para el idTramite="+idTramite);

    	List<GrupoAplicacionTramiteDTO> listaGATramite=null;
    	
    	// Cojo por defecto del caracter de valores de referencia
    	int idCaracterGrupoAplicacionDiccionario = 20;
    	
    	String query =  "SELECT det " +
		" FROM Determinacion det " +
		" WHERE det.idcaracter = "+idCaracterGrupoAplicacionDiccionario+
		" AND det.tramite.iden = "+idTramite;
		
    	 try {
         	
             List<Determinacion> detGrupoAplicList = (List<Determinacion>) em.createQuery(query).getResultList();
             
             listaGATramite = new ArrayList<GrupoAplicacionTramiteDTO>(detGrupoAplicList.size());
             
             for (Determinacion detGA : detGrupoAplicList)
             {
            	 
            	 // Por cada grupo de entidades, llamo a los valores de referencia de esto ya que seran los Grupos de Aplicaion
            	 Determinacion[] detVRArray = getArrayValoresReferencia(detGA.getIden());
            	 List<Determinacion> ldet = Arrays.asList(detVRArray);
            	 
            	 for (Determinacion det : ldet)
                 {
            		 GrupoAplicacionTramiteDTO gaTram = new GrupoAplicacionTramiteDTO();
                	 
                	 gaTram.setSeleccionada(false);
                	 gaTram.setIdDeterminacion(det.getIden());
                	 gaTram.setNombreDeterminacion(det.getNombre());
                	 gaTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
                	 
                	 listaGATramite.add(gaTram); 
                 }
            	 
             }
            
             
            
         } catch (NoResultException e) {
             log.error("[obtenerGruposAplicacionTramite] No se han encontrado GruposAplicacion para el tramite:"+idTramite+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerGruposAplicacionTramite] Error en la busqueda de GruposAplicacion para el tramite:"+idTramite+"\n" + ex.getMessage());

         }
    	
    	
         log.debug("[obtenerGruposAplicacionTramite] Se devuelven "+listaGATramite.size()+" GruposAplicacion para el idTramite="+idTramite);
    	return listaGATramite;
    }
    
    // ------------------------------
    // VALORES REFERENCIA
    // ------------------------------
    
    public boolean guardarHijosComoValorReferencia (int idDeterminacion)
    {
        boolean resultado = false;
        
      
        
         String query =  "SELECT det " +
        " FROM Determinacion det"+
        " WHERE det.determinacionByIdpadre="+idDeterminacion+
        " AND det.idcaracter = 12";

        Query qNombreAmbito = em.createQuery(query);
        
        Determinacion detPadre = em.find(Determinacion.class, idDeterminacion);
        
        try {
            
            List<Determinacion> result = (List<Determinacion>) qNombreAmbito.getResultList();
            
            for (Determinacion valorReferencia : result)
            {
                Opciondeterminacion opciondeterminacion = new Opciondeterminacion();
                
                opciondeterminacion.setDeterminacionByIddeterminacion(detPadre);
                opciondeterminacion.setDeterminacionByIddeterminacionvalorref(valorReferencia);
                
                em.persist(opciondeterminacion);
               
                
            }
            
            resultado = true;
            
        } catch (NoResultException e) {
            log.error("[guardarHijosComoValorReferencia] No se ha encontrado la Determinacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[guardarHijosComoValorReferencia] Error en la busqueda de la  Determinacion: " + ex.getMessage());

        }
        
       return resultado;
    }
    
    public void guardarValoresReferencia(int idDeterminacion, List<Integer> idValoresReferencia)
	{
		
		
		Determinacion determinacion = em.find(Determinacion.class, new Integer(idDeterminacion));
		
		Determinacion[] detVRList = getArrayValoresReferencia(idDeterminacion);
		
		for (Integer idVR : idValoresReferencia)
		{
			Determinacion valorReferencia = em.find(Determinacion.class,idVR);
			
			Opciondeterminacion opciondeterminacion = new Opciondeterminacion();
			
			opciondeterminacion.setDeterminacionByIddeterminacion(determinacion);
			opciondeterminacion.setDeterminacionByIddeterminacionvalorref(valorReferencia);
			
			em.persist(opciondeterminacion);
			
			
		}
		
		
	}
    
    public List<ValorReferenciaTramiteDTO> obtenerValoresReferenciaTramite (int idTramite)
    {
    	log.debug("[obtenerValoresReferenciaTramite] Se van a obtener los Valores de Referencia para el idTramite="+idTramite);

    	List<ValorReferenciaTramiteDTO> listaVRTramite=null;
    	
    	// Cojo por defecto del caracter de valores de referencia
    	int idCaracterValorReferenciaDiccionario = 12;
    	
    	String query =  "SELECT det " +
		" FROM Determinacion det " +
		" WHERE det.idcaracter = "+idCaracterValorReferenciaDiccionario+
		" AND det.tramite.iden = "+idTramite;
		
    	 try {
         	
             List<Determinacion> detValRefList = (List<Determinacion>) em.createQuery(query).getResultList();
             
             listaVRTramite = new ArrayList<ValorReferenciaTramiteDTO>(detValRefList.size());
             
             for (Determinacion det : detValRefList)
             {
            	 ValorReferenciaTramiteDTO vrTram = new ValorReferenciaTramiteDTO();
            	 
            	 vrTram.setSeleccionada(false);
            	 vrTram.setIdDeterminacion(det.getIden());
            	 vrTram.setNombreDeterminacion(det.getNombre());
            	 vrTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
            	 
            	 listaVRTramite.add(vrTram);
             }
            
             
            
         } catch (NoResultException e) {
             log.error("[obtenerValoresReferenciaTramite] No se han encontrado Valores de Referencia para el tramite:"+idTramite+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerValoresReferenciaTramite] Error en la busqueda de Valores de Referencia para el tramite:"+idTramite+"\n" + ex.getMessage());

         }
    	
    	
         log.debug("[obtenerValoresReferenciaTramite] Se devuelven "+listaVRTramite.size()+" Valores de Referencia para el idTramite="+idTramite);
    	return listaVRTramite;
    }
    
    public List<ValorReferenciaTramiteDTO> obtenerValoresReferenciaTramiteYDeterminacion (int idTramite, int idDeterminacion)
    {
    	log.debug("[obtenerValoresReferenciaTramite] Se van a obtener los Valores de Referencia para el idTramite="+idTramite +" excepto los ya aplicados para la determinacion="+idDeterminacion);

    	List<ValorReferenciaTramiteDTO> listaVRTramite=null;
    	
    	// Cojo por defecto del caracter de valores de referencia
    	int idCaracterValorReferenciaDiccionario = 12;
    	
    	String query =  "SELECT det " +
		" FROM Determinacion det " +
		" WHERE det.idcaracter = "+idCaracterValorReferenciaDiccionario+
		" AND det.tramite.iden = "+idTramite+
		" AND det.iden NOT IN" +
		" (SELECT opDet.determinacionByIddeterminacionvalorref.iden " +
		" FROM Opciondeterminacion opDet " +
		" WHERE opDet.determinacionByIddeterminacion="+idDeterminacion+")" +
				" ORDER BY det.nombre ASC";
		
    	 try {
         	
             List<Determinacion> detValRefList = (List<Determinacion>) em.createQuery(query).getResultList();
             
             listaVRTramite = new ArrayList<ValorReferenciaTramiteDTO>(detValRefList.size());
             
             for (Determinacion det : detValRefList)
             {
            	 ValorReferenciaTramiteDTO vrTram = new ValorReferenciaTramiteDTO();
            	 
            	 vrTram.setSeleccionada(false);
            	 vrTram.setIdDeterminacion(det.getIden());
            	 vrTram.setNombreDeterminacion(det.getNombre());
            	 vrTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
            	 
            	 listaVRTramite.add(vrTram);
             }
            
             
            
         } catch (NoResultException e) {
             log.error("[obtenerValoresReferenciaTramite] No se han encontrado Valores de Referencia para el tramite:"+idTramite+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerValoresReferenciaTramite] Error en la busqueda de Valores de Referencia para el tramite:"+idTramite+"\n" + ex.getMessage());

         }
    	
    	
         log.debug("[obtenerValoresReferenciaTramite] Se devuelven "+listaVRTramite.size()+" Valores de Referencia para el idTramite="+idTramite);
    	return listaVRTramite;
    }
    
    public Determinacion getValorReferenciaDeDeterminacion(int idDeterminacion, String nombre) {

    	log.debug("[getListValoresReferenciaDeDeterminacion] idDeterminacion:"+idDeterminacion);
    	
    	List<Determinacion> result = new ArrayList<Determinacion>();
        

    	String query= "SELECT opcdet.determinacionByIddeterminacionvalorref " +
        " FROM Opciondeterminacion opcdet " +
        " WHERE opcdet.determinacionByIddeterminacion.iden = " +idDeterminacion + 
        " AND LOWER(opcdet.determinacionByIddeterminacionvalorref.nombre) like '" + nombre.toLowerCase() + "'";
        
    	result = (List<Determinacion>) em.createQuery(query).getResultList();
    	
    	if(result!=null && result.size()>0)
    		return result.get(0);
    	else
    		return null;
       
    }
    
    public boolean esValorReferenciaDeDeterminacion(int idDeterminacion, int idDetValorRef) {

    	log.debug("[getListValoresReferenciaDeDeterminacion] idDeterminacion:"+idDeterminacion);
    	
    	List<Determinacion> result = new ArrayList<Determinacion>();
   
    	String query= "SELECT opcdet.determinacionByIddeterminacionvalorref " +
        " FROM Opciondeterminacion opcdet " +
        " WHERE opcdet.determinacionByIddeterminacion.iden = " +idDeterminacion + 
        " AND opcdet.determinacionByIddeterminacionvalorref.iden = " +idDetValorRef;
        
    	result = (List<Determinacion>) em.createQuery(query).getResultList();
    	
    	if(result!=null && result.size()>0)
    		return true;
    	else
    		return false;
       
    }
    
    public List<ValorReferenciaTramiteDTO> obtenerValoresReferenciaDeterminacion (int idDeterminacion)
    {
    	log.debug("[obtenerValoresReferenciaDeterminacion] Se van a obtener los Valores de Referencia para la determinacion="+idDeterminacion);

    	List<ValorReferenciaTramiteDTO> listaVRTramite=null;
    	
    	String query =  "SELECT opDet.determinacionByIddeterminacionvalorref " +
		" FROM Opciondeterminacion opDet " +
		" WHERE opDet.determinacionByIddeterminacion="+idDeterminacion+
		" ORDER BY opDet.determinacionByIddeterminacionvalorref.nombre ASC";



		try {
			
			List<Determinacion> detValRefList = (List<Determinacion>) em.createQuery(query).getResultList();
             
		    listaVRTramite = new ArrayList<ValorReferenciaTramiteDTO>(detValRefList.size());
            
            for (Determinacion det : detValRefList)
            {
           	 ValorReferenciaTramiteDTO vrTram = new ValorReferenciaTramiteDTO();
           	 
           	 vrTram.setSeleccionada(false);
           	 vrTram.setIdDeterminacion(det.getIden());
           	 vrTram.setNombreDeterminacion(det.getNombre());
           	 vrTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
           	 
           	 listaVRTramite.add(vrTram);
            }
		   
		    
		   
		} catch (NoResultException e) {
		    log.error("[getArrayValoresReferencia] No se han encontrado Valores de Referencia para la determinacion:"+idDeterminacion+"\n" + e.getMessage());
		
		} catch (Exception ex) {
		    log.error("[getArrayValoresReferencia] Error en la busqueda de Valores de Referencia para la determinacion:"+idDeterminacion+"\n" + ex.getMessage());
		
		}
    	
    	 log.debug("[obtenerValoresReferenciaDeterminacion] Se devuelven "+listaVRTramite.size()+" Valores de Referencia para la determinacion="+idDeterminacion);
     	return listaVRTramite;
    }
    
    public Determinacion[] getArrayValoresReferencia(int idDeterminacion) {
    	
    	log.debug("[getArrayValoresReferencia] Obtener los valores de referencia de la determinacion: " + idDeterminacion);

    	Determinacion[] result =  null;
    		
    	String query =  "SELECT opDet.determinacionByIddeterminacionvalorref " +
    			" FROM Opciondeterminacion opDet " +
    			" WHERE opDet.determinacionByIddeterminacion="+idDeterminacion+
    			" ORDER BY opDet.determinacionByIddeterminacionvalorref.nombre ASC";

        
        
        try {
        	
            List<Determinacion> resultList = (List<Determinacion>) em.createQuery(query).getResultList();
            
            result = new Determinacion[resultList.size()];
            
            int indice = 0;
            
            for (Determinacion det : resultList)
            {
            	result[indice]=det;
            	indice++;
            }
           
            
           
        } catch (NoResultException e) {
            log.error("[getArrayValoresReferencia] No se han encontrado Valores de Referencia para la determinacion:"+idDeterminacion+"\n" + e.getMessage());

        } catch (Exception ex) {
            log.error("[getArrayValoresReferencia] Error en la busqueda de Valores de Referencia para la determinacion:"+idDeterminacion+"\n" + ex.getMessage());

        }
        
       

        return result;
    }
    
    public boolean borrarRelacionDeterminacionConValorReferencia(int idDeterminacion, int idValorReferencia){
    	
    	log.error("[borrarRelacionDeterminacionConValorReferencia] idDeterminacion=" + idDeterminacion +" / idValorReferencia="+idValorReferencia);
    	boolean resultado=true;
    	
    	
     	String query =  "SELECT opDet " +
    	" FROM Opciondeterminacion opDet"+
    	" WHERE opDet.determinacionByIddeterminacion="+idDeterminacion+
    	" AND opDet.determinacionByIddeterminacionvalorref="+idValorReferencia;

        
        
        try {
        	// No deberia haber mas de uno pero si se han equivocado al insertar puede fallar con singleResult
        	// Borro todas las relaciones que puedan haber, da igual pues es simplemente una relacion 
        	
            List<Opciondeterminacion> resultList = (List<Opciondeterminacion>) em.createQuery(query).getResultList();
            
            for (Opciondeterminacion result:resultList)
            {
            	em.remove(em.merge(result));
            	em.flush();
            }
            
        } catch (NoResultException e) {
            log.error("[borrarRelacionDeterminacionConValorReferencia] No se ha encontrado la Opcion Determinacion." + e.getMessage());
            resultado=false;

        } catch (Exception ex) {
            log.error("[borrarRelacionDeterminacionConValorReferencia] Error al borrar de la Opcion Determinacion: " + ex.getMessage());
            resultado=false;

        }
        
        return resultado;
    	
    }
    
    public void borrarTodasRelacionesDeterminacionConValorReferencia(int idDeterminacion){
    	
     	
    	
     	String query =  "SELECT opDet " +
    	" FROM Opciondeterminacion opDet"+
    	" WHERE opDet.determinacionByIddeterminacion="+idDeterminacion+
    	" OR opDet.determinacionByIddeterminacionvalorref="+idDeterminacion;

       
        
        try {
        	// Se borran todos los resultantes
        	List<Opciondeterminacion> result = (List<Opciondeterminacion>) em.createQuery(query).getResultList().get(0);
        	for (Opciondeterminacion valorReferencia : result)
            {
        		em.remove(em.merge(result));
        		
            }
        } catch (NoResultException e) {
            log.error("[removeByDeterminacionAndValorReferencia] No se ha encontrado la Opcion Determinacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[removeByDeterminacionAndValorReferencia] Error en la busqueda de la Opcion Determinacion: " + ex.getMessage());

        }
    	
    }
    
    
    // -----------------------------------
    // Determinacion Regimen
    // -----------------------------------
    
    public List<DeterminacionDTO> getListValoresReferenciaDeDeterminacion(int idDeterminacion) {

    	log.debug("[getListValoresReferenciaDeDeterminacion] idDeterminacion:"+idDeterminacion);
    	
    	List<DeterminacionDTO> result = new ArrayList<DeterminacionDTO>();
        

    	String query= "SELECT opcdet.determinacionByIddeterminacionvalorref " +
        " FROM Opciondeterminacion opcdet " +
        " WHERE opcdet.determinacionByIddeterminacion.iden = " +idDeterminacion;
        
        try {
        	
        	List<Determinacion> ldet = em.createQuery(query).getResultList();
            
            
            for (Determinacion det : ldet) {
            	
            	 DeterminacionDTO unTram = new DeterminacionDTO();
            	 
            	 unTram.setSeleccionada(false);
            	 unTram.setIdDeterminacion(det.getIden());
            	 unTram.setNombreDeterminacion(det.getNombre());
            	 unTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
            	 
            	 result.add(unTram);
            	                	
               
            }
        	
        }
        catch (NoResultException ex)
		{
        	log.error("[getListValoresReferenciaDeDeterminacion] ERROR:"+ex.getCause());
        	ex.printStackTrace();
		}
		catch (NullPointerException ex1)
		{
			log.error("[getListValoresReferenciaDeDeterminacion] ERROR:"+ex1.getCause());
			ex1.printStackTrace();
		}
		catch (Exception e) {
			log.error("[getListValoresReferenciaDeDeterminacion] ERROR:"+e.getCause());
			e.printStackTrace();
		}
        
		log.debug("[getListValoresReferenciaDeDeterminacion] result:"+result.size());
        return result;
    }
    
    // ------------------------------
    // DETERMINACION REGULADORA
    // ------------------------------
    
    public void guardarDeterminacionReguladoraDeterminacion(int idDetContenedora, int idDetReguladora)
	{
    	
    	
    	try{
    		Determinacion determinacion = buscarDeterminacion(idDetContenedora);
    		Determinacion reguladora = buscarDeterminacion(idDetReguladora);
    		
    		Relacion relacion = new Relacion();
    		
    		// idDefRelacion se corresponde con los caracteres entre 1 y 17 +2
    		// EXCEPTO el caracter 16
    		int caracterDefRel = reguladora.getIdcaracter();
    		if ((caracterDefRel<=15) || (caracterDefRel==17))
    			caracterDefRel++;
    		else
    			log.error("ERROR: El caracter ("+caracterDefRel+") de la determinación reguladora no es correcto para una relación de regulación");
    		
    		relacion.setIddefrelacion(caracterDefRel); 
    		relacion.setTramiteByIdtramitecreador(determinacion.getTramite());
    		
    		em.persist(relacion);
    		
    		Vectorrelacion vrDet = new Vectorrelacion();
    		Vectorrelacion vrReg = new Vectorrelacion();
    		
    		vrDet.setValor(idDetContenedora);
    		vrReg.setValor(idDetReguladora);
    		
    		vrDet.setRelacion(relacion);
    		vrReg.setRelacion(relacion);
    		
    		vrDet.setIddefvector((determinacion.getIdcaracter()+3)*2);
    		vrReg.setIddefvector(((determinacion.getIdcaracter()+3)*2)+1);
    		
    		em.persist(vrDet);
    		em.persist(vrReg);	
    		
    		// Correcto
    		
    		log.info("[guardarDeterminacionReguladoraDeterminacion] Se ha guardado de forma correcta la asociacion de determinacion reguladora. Determinacion Contenedora:"+idDetContenedora+" / Determinacion Reguladora:"+idDetReguladora);
    	}
    	catch (Exception ex)
    	{
    		log.error("[guardarDeterminacionReguladoraDeterminacion] Se ha producido un error al guardar la relacion de determinacion" +
    				" reguladora. Determinacion Contenedora:"+idDetContenedora+" / Determinacion Reguladora:"+idDetReguladora);
    		ex.printStackTrace();
    	}
		
	}
    
    public void borrarDeterminacionReguladoraDeterminacion(int idDetContenedora, int idDetReguladora)
	{
    	  	
    	Vectorrelacion vectorRelDetReg = null;
    	Vectorrelacion vectorRelDetCont = null;
    	
    	String cadena="SELECT vectorrelac2 " +
        " FROM Vectorrelacion vectorrelac, " +
        "		Vectorrelacion vectorrelac2" +           
        " WHERE (vectorrelac.iddefvector=8 " +
        " OR vectorrelac.iddefvector=10 " +
        " OR vectorrelac.iddefvector=12 " +
        " OR vectorrelac.iddefvector=14 " +
        " OR vectorrelac.iddefvector=16 " +
        " OR vectorrelac.iddefvector=18 " +
        " OR vectorrelac.iddefvector=20 " +
        " OR vectorrelac.iddefvector=22 " +
        " OR vectorrelac.iddefvector=24 " +
        " OR vectorrelac.iddefvector=26 " +
        " OR vectorrelac.iddefvector=28 " +
        " OR vectorrelac.iddefvector=30 " +
        " OR vectorrelac.iddefvector=32 " +
        " OR vectorrelac.iddefvector=34 " +
        " OR vectorrelac.iddefvector=36 " +
        " OR vectorrelac.iddefvector=38 " +
        " OR vectorrelac.iddefvector=40) " +
        " AND vectorrelac.valor = " + idDetContenedora +
        " AND vectorrelac.relacion =vectorrelac2.relacion "+
        " AND vectorrelac.relacion.iddefrelacion>3 AND vectorrelac2.relacion.iddefrelacion<20 "+
        " AND (vectorrelac2.iddefvector=9 "+ 
        " OR vectorrelac2.iddefvector=11 "+
        " OR vectorrelac2.iddefvector=13 "+
        " OR vectorrelac2.iddefvector=15 "+
        " OR vectorrelac2.iddefvector=17 "+
        " OR vectorrelac2.iddefvector=19 "+
        " OR vectorrelac2.iddefvector=21 "+
        " OR vectorrelac2.iddefvector=23 "+
        " OR vectorrelac2.iddefvector=25 "+
        " OR vectorrelac2.iddefvector=27 "+
        " OR vectorrelac2.iddefvector=29 "+
        " OR vectorrelac2.iddefvector=31 "+
        " OR vectorrelac2.iddefvector=33 "+
        " OR vectorrelac2.iddefvector=35 "+
        " OR vectorrelac2.iddefvector=37 "+
        " OR vectorrelac2.iddefvector=39) " +
        " AND vectorrelac2.valor = " + idDetReguladora;
    	
    	
    	
    	try{
    		vectorRelDetReg = (Vectorrelacion)em.createQuery(cadena).getSingleResult();
    		
    		// Obtengo el objeto relacion de ese vector
			Relacion rel = vectorRelDetReg.getRelacion();
			
			// Borro el vector relacion que contiene a la determinacion reguladora
			em.remove(vectorRelDetReg);
			
			// Busco ahora la relacion que contiene a la determinacion  contenedora
			String cadena2 = "SELECT vectorrelac2 " +
            " FROM Vectorrelacion vectorrelac2" +           
            " WHERE vectorrelac2.relacion=" +rel.getIden()+
            " AND vectorrelac2.valor="+idDetContenedora ;
			
			
			vectorRelDetCont = (Vectorrelacion)em.createQuery(cadena2).getSingleResult();
			
									
			// Borro el vector relacion que contiene a la determinacion contenedora
			em.remove(vectorRelDetCont);
			
			// Borro la relacion que contiene a la determinacion unidad contenida y contenedora
			em.remove(rel);
			
			
			
    		log.info("[borrarDeterminacionReguladoraDeterminacion] Se ha guardado de forma correcta la asociacion de determinacion reguladora. Determinacion Contenedora:"+idDetContenedora+" / Determinacion Reguladora:"+idDetReguladora);
    	}
    	catch (Exception ex)
    	{
    		log.error("[borrarDeterminacionReguladoraDeterminacion] Se ha producido un error al guardar la relacion de determinacion" +
    				" reguladora. Determinacion Contenedora:"+idDetContenedora+" / Determinacion Reguladora:"+idDetReguladora);
    		ex.printStackTrace();
    	}
		
	}
    
    public Determinacion[] getArrayReguladoras(int idDeterminacion) {

        Determinacion detTrabajo = buscarDeterminacion(new Integer(idDeterminacion));
        
        String query = "SELECT vectorrelac2 " +
        " FROM Vectorrelacion vectorrelac, " +
        "		Vectorrelacion vectorrelac2" +           
        " WHERE (vectorrelac.iddefvector=8 " +
        " OR vectorrelac.iddefvector=10 " +
        " OR vectorrelac.iddefvector=12 " +
        " OR vectorrelac.iddefvector=14 " +
        " OR vectorrelac.iddefvector=16 " +
        " OR vectorrelac.iddefvector=18 " +
        " OR vectorrelac.iddefvector=20 " +
        " OR vectorrelac.iddefvector=22 " +
        " OR vectorrelac.iddefvector=24 " +
        " OR vectorrelac.iddefvector=26 " +
        " OR vectorrelac.iddefvector=28 " +
        " OR vectorrelac.iddefvector=30 " +
        " OR vectorrelac.iddefvector=32 " +
        " OR vectorrelac.iddefvector=34 " +
        " OR vectorrelac.iddefvector=36 " +
        " OR vectorrelac.iddefvector=38 " +
        " OR vectorrelac.iddefvector=40) " +
        " AND vectorrelac.valor = " + idDeterminacion +
        " AND vectorrelac.relacion =vectorrelac2.relacion "+
        " AND vectorrelac.relacion.iddefrelacion>3 AND vectorrelac2.relacion.iddefrelacion<20 "+
        " AND (vectorrelac2.iddefvector=9 "+ 
        " OR vectorrelac2.iddefvector=11 "+
        " OR vectorrelac2.iddefvector=13 "+
        " OR vectorrelac2.iddefvector=15 "+
        " OR vectorrelac2.iddefvector=17 "+
        " OR vectorrelac2.iddefvector=19 "+
        " OR vectorrelac2.iddefvector=21 "+
        " OR vectorrelac2.iddefvector=23 "+
        " OR vectorrelac2.iddefvector=25 "+
        " OR vectorrelac2.iddefvector=27 "+
        " OR vectorrelac2.iddefvector=29 "+
        " OR vectorrelac2.iddefvector=31 "+
        " OR vectorrelac2.iddefvector=33 "+
        " OR vectorrelac2.iddefvector=35 "+
        " OR vectorrelac2.iddefvector=37 "+
        " OR vectorrelac2.iddefvector=39) ";
        
        List<Vectorrelacion> lvr = em.createQuery(query).getResultList();
        Determinacion[] adet = new Determinacion[lvr.size()];
        int i=0;
        for (Vectorrelacion vectorrelacion : lvr) {
        	Determinacion det = buscarDeterminacion(new Integer(vectorrelacion.getValor()));
        	det.getTramite().getNombre();
        	det.getTramite().getPlan().getNombre();
        	adet[i]=det;
        	i++;
		}
        
        return adet;
    }
    
    public List<DeterminacionReguladoraTramiteDTO> obtenerDeterminacionesReguladorasTramite (int idTramite)
    {
    	log.debug("[obtenerDeterminacionesReguladorasTramite] Se van a obtener los DeterminacionReguladora para el idTramite="+idTramite);

    	List<DeterminacionReguladoraTramiteDTO> listaDRTramite=null;
    	
    	// Cojo por defecto del caracter de valores de referencia
    	int idCaracterDeterminacionReguladoraDiccionario = 13;
    	
    	String query =  "SELECT det " +
		" FROM Determinacion det " +
		" WHERE det.idcaracter = "+idCaracterDeterminacionReguladoraDiccionario+
		" AND det.tramite.iden = "+idTramite;
		
    	 try {
         	
             List<Determinacion> detDetRegList = (List<Determinacion>) em.createQuery(query).getResultList();
             
             listaDRTramite = new ArrayList<DeterminacionReguladoraTramiteDTO>(detDetRegList.size());
             
             for (Determinacion det : detDetRegList)
             {
            	 DeterminacionReguladoraTramiteDTO drTram = new DeterminacionReguladoraTramiteDTO();
            	 
            	 drTram.setSeleccionada(false);
            	 drTram.setIdDeterminacion(det.getIden());
            	 drTram.setNombreDeterminacion(det.getNombre());
            	 drTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
            	 
            	 listaDRTramite.add(drTram);
             }
            
             
            
         } catch (NoResultException e) {
             log.error("[obtenerDeterminacionesReguladorasTramite] No se han encontrado DeterminacionReguladora para el tramite:"+idTramite+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerDeterminacionesReguladorasTramite] Error en la busqueda de DeterminacionReguladora para el tramite:"+idTramite+"\n" + ex.getMessage());

         }
    	
    	
         log.debug("[obtenerDeterminacionesReguladorasTramite] Se devuelven "+listaDRTramite.size()+" DeterminacionReguladora para el idTramite="+idTramite);
    	return listaDRTramite;
    }
    
    
    // ------------------------------------
    // ADSCRIPCIONES
    // ------------------------------------
    public List<AdscripcionesTramiteDTO> obtenerDeterminacionesAdscripcionesTramite (int idTramite)
    {
    	log.debug("[obtenerDeterminacionesAdscripcionesTramite] Se van a obtener los Determinacion Adscripciones para el idTramite="+idTramite);

    	List<AdscripcionesTramiteDTO> listaDRTramite=null;
    	
    	// Cojo por defecto del caracter de Adscripciones
    	int idCaracterDeterminacionAdscripcionesDiccionario = 19;
    	
    	String query =  "SELECT det " +
		" FROM Determinacion det " +
		" WHERE det.idcaracter = "+idCaracterDeterminacionAdscripcionesDiccionario+
		" AND det.tramite.iden = "+idTramite;
		
    	 try {
         	
             List<Determinacion> detDetRegList = (List<Determinacion>) em.createQuery(query).getResultList();
             
             listaDRTramite = new ArrayList<AdscripcionesTramiteDTO>(detDetRegList.size());
             
             for (Determinacion det : detDetRegList)
             {
            	 AdscripcionesTramiteDTO drTram = new AdscripcionesTramiteDTO();
            	 
            	 drTram.setSeleccionada(false);
            	 drTram.setIdDeterminacion(det.getIden());
            	 drTram.setNombreDeterminacion(det.getNombre());
            	 drTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
            	 
            	 listaDRTramite.add(drTram);
             }
            
             
            
         } catch (NoResultException e) {
             log.error("[obtenerDeterminacionesAdscripcionesTramite] No se han encontrado Determinacion Adscripciones para el tramite:"+idTramite+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerDeterminacionesAdscripcionesTramite] Error en la busqueda de Determinacion Adscripciones para el tramite:"+idTramite+"\n" + ex.getMessage());

         }
    	
    	
         log.debug("[obtenerDeterminacionesReguladorasTramite] Se devuelven "+listaDRTramite.size()+" Determinacion Adscripciones para el idTramite="+idTramite);
    	return listaDRTramite;
    }
    
/*    
    public List<DeterminacionReguladoraTramiteDTO> obtenerDeterminacionesReguladorasTramiteYDeterminacion (int idTramite, int idDeterminacion)
    {
    	log.debug("[obtenerDeterminacionesReguladorasTramiteYDeterminacion] Se van a obtener los DeterminacionesReguladoras para el idTramite="+idTramite +" excepto los ya aplicados para la determinacion="+idDeterminacion);

    	List<DeterminacionReguladoraTramiteDTO> listaDRTramite=null;
    	
    	// Cojo por defecto del caracter de valores de referencia
    	int idCaracterDeterminacionesReguladorasDiccionario = 13;
    	
    	String query =  "SELECT det " +
		" FROM Determinacion det " +
		" WHERE det.idcaracter = "+idCaracterDeterminacionesReguladorasDiccionario+
		" AND det.tramite.iden = "+idTramite+
		" AND det.iden NOT IN" +
		" (SELECT opDet.determinacionByIddeterminacionvalorref.iden " +
		" FROM Opciondeterminacion opDet " +
		" WHERE opDet.determinacionByIddeterminacion="+idDeterminacion+")";
		
    	 try {
         	
             List<Determinacion> detDetRegList = (List<Determinacion>) em.createQuery(query).getResultList();
             
             listaDRTramite = new ArrayList<DeterminacionReguladoraTramiteDTO>(detDetRegList.size());
             
             for (Determinacion det : detDetRegList)
             {
            	 DeterminacionReguladoraTramiteDTO drTram = new DeterminacionReguladoraTramiteDTO();
            	 
            	 drTram.setSeleccionada(false);
            	 drTram.setIdDeterminacion(det.getIden());
            	 drTram.setNombreDeterminacion(det.getNombre());
            	 drTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
            	 
            	 listaDRTramite.add(drTram);
             }
            
             
            
         } catch (NoResultException e) {
             log.error("[obtenerDeterminacionesReguladorasTramiteYDeterminacion] No se han encontrado DeterminacionesReguladoras para el tramite:"+idTramite+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerDeterminacionesReguladorasTramiteYDeterminacion] Error en la busqueda de DeterminacionesReguladoras para el tramite:"+idTramite+"\n" + ex.getMessage());

         }
    	
    	
         log.debug("[obtenerDeterminacionesReguladorasTramiteYDeterminacion] Se devuelven "+listaDRTramite.size()+" DeterminacionesReguladoras para el idTramite="+idTramite);
    	return listaDRTramite;
    }
    
   */
    
     

    // ------------------------------
    // REGULACIONES ESPECIFICAS
    // ------------------------------
   
    public int crearRegulacionespecifica (RegulacionEspecificaDTO regEspec, int idDeterminacion)
    {
    	int resultado = 0;
    	
    	 
    	try {
            Propiedadrelacion propiedadRelacionRPManadir = new Propiedadrelacion();
            Propiedadrelacion propiedadRelacionRPManadir2 = new Propiedadrelacion();
            Propiedadrelacion propiedadRelacionRPManadir3 = new Propiedadrelacion();
            Relacion relacionRPManadir = new Relacion();
            Vectorrelacion VectorrelacionRPManadir = new Vectorrelacion();
            Vectorrelacion VectorrelacionRPManadir2 = new Vectorrelacion();
            
            // Obtengo la determinacion contenedora
            Determinacion detContenedora = buscarDeterminacion(idDeterminacion);
            
            // Obtengo el tramite al que pertenece
            Tramite tramPertenece = detContenedora.getTramite();

            // relacion
            relacionRPManadir.setIddefrelacion(20);
            relacionRPManadir.setTramiteByIdtramitecreador(tramPertenece);
            em.persist(relacionRPManadir);
            
            log.debug("--    CREADA Relacion  " + relacionRPManadir.getIden());

           
            //PROPIEDAD RELACION orden
            propiedadRelacionRPManadir.setRelacion(relacionRPManadir);
            propiedadRelacionRPManadir.setIddefpropiedad(5);
            propiedadRelacionRPManadir.setValor(regEspec.getOrden()+"");
            em.persist(propiedadRelacionRPManadir);
            log.debug("--    CREADA Propiedad orden " + propiedadRelacionRPManadir.getIden());

            //PROPIEDAD RELACION nombre
            propiedadRelacionRPManadir2.setRelacion(relacionRPManadir);
            propiedadRelacionRPManadir2.setIddefpropiedad(6);
            propiedadRelacionRPManadir2.setValor(regEspec.getNombre());
            em.persist(propiedadRelacionRPManadir2);
            log.debug("--    CREADA Propiedad nombre " + propiedadRelacionRPManadir2.getIden());

            //PROPIEDAD RELACION texto
            propiedadRelacionRPManadir3.setRelacion(relacionRPManadir);
            propiedadRelacionRPManadir3.setIddefpropiedad(7);
            propiedadRelacionRPManadir3.setValor(regEspec.getTexto());
            em.persist(propiedadRelacionRPManadir3);
            log.debug("--    CREADA Propiedad texto " + propiedadRelacionRPManadir3.getIden());

            //vectorRelacion
            VectorrelacionRPManadir.setRelacion(relacionRPManadir);
            VectorrelacionRPManadir.setIddefvector(40);
            VectorrelacionRPManadir.setValor(detContenedora.getIden());
            em.persist(VectorrelacionRPManadir);
            log.debug("--    CREADO Vector " + VectorrelacionRPManadir.getIden());

            //vectorRelacion
            
            VectorrelacionRPManadir2.setRelacion(relacionRPManadir);
            VectorrelacionRPManadir2.setIddefvector(41);
            VectorrelacionRPManadir2.setValor(regEspec.getIdRegulacionEspPadre());
            em.persist(VectorrelacionRPManadir2);
            log.debug("--    CREADO Vector " + VectorrelacionRPManadir2.getIden());
            
            resultado=relacionRPManadir.getIden();
            log.info("[crearRegulacionespecifica] Creacion de Regulacion Especifica Correcta");
            
            
        } catch (Exception ex) {
            log.error("Ser ha producido un error al crear las regulaciones específicas. " + ex);
            resultado = 0;
            ex.printStackTrace();
        }
    	    	
    	
    	return resultado;
    }
    
    public boolean editarRegulacionespecifica (RegulacionEspecificaDTO regEspec)
    {
    	boolean resultado = false;
    	
    	// Compruebo que exite la relacion de lo contrario daria fallo
    	
    	if (regEspec.getIdRelacion()!=0)
    	{
    		int idRelacion = regEspec.getIdRelacion();
    		
    		// ----------
    		// Modifico el orden de la regulacion especifica
    		// -----------
        	
        	String queryOrden= "SELECT proprel " +
            " FROM Propiedadrelacion proprel " +          
            " WHERE proprel.relacion="+idRelacion +
            " AND proprel.iddefpropiedad=5 ";
        	
        	Propiedadrelacion ordenPdad = (Propiedadrelacion)em.createQuery(queryOrden).getSingleResult();
        	
        	// Modifico el valor del orden
        	ordenPdad.setValor(regEspec.getOrden()+"");
        	// Lo persisto en BD
        	em.merge(ordenPdad);
        	
        	
        	// -------------------
        	// Modifico el nombre de la regulacion especifica
        	// -------------------
        	
        	String queryNombre= "SELECT proprel " +
            " FROM Propiedadrelacion proprel " +          
            " WHERE proprel.relacion="+idRelacion +
            " AND proprel.iddefpropiedad=6 ";
        	
        	Propiedadrelacion nombrePdad = (Propiedadrelacion)em.createQuery(queryNombre).getSingleResult();
        	
        	// Modifico el valor del nombre
        	nombrePdad.setValor(regEspec.getNombre());
        	// Lo persisto en BD
        	em.merge(nombrePdad);
        	
        	// -----------------------
        	// Modifico el texto de la regulacion especifica
        	// ----------------------
        	
        	String queryTexto= "SELECT proprel " +
            " FROM Propiedadrelacion proprel " +          
            " WHERE proprel.relacion="+idRelacion +
            " AND proprel.iddefpropiedad=7 ";
        	
        	Propiedadrelacion textoPdad = (Propiedadrelacion)em.createQuery(queryTexto).getSingleResult();
        	
        	// Modifico el valor del nombre
        	textoPdad.setValor(regEspec.getTexto());
        	// Lo persisto en BD
        	em.merge(textoPdad);
        	
        	// -------------------------------
        	// Modifico el padre de la regulacion especifica
        	// ------------------------------
        	
        	String queryPadre= "SELECT vectorrelac2 " +
            " FROM Vectorrelacion vectorrelac2" +           
            " WHERE vectorrelac2.iddefvector=41 " +
            " AND vectorrelac2.relacion="+idRelacion ;
        	
        	Vectorrelacion idPadreValor = (Vectorrelacion)em.createQuery(queryPadre).getSingleResult();
        	// Modifico el valor del idPadre
        	idPadreValor.setValor(regEspec.getIdRegulacionEspPadre());
        	// Lo persisto en BD
        	em.merge(idPadreValor);
        	
        	log.info("[editarRegulacionespecifica] La Regulacion Especifica se ha modificado correctamente");
        	resultado = true;
        	
        	
    	}
    	else
    	{
    		log.info("[editarRegulacionespecifica] No se ha podido guardar la Regulacion Especifica ya que IdRelacion=0");
    	}
    	    	
    	
    	return resultado;
    }
    

    public RegulacionEspecificaDTO buscarRegulacionEspecifica(int idRelacion) {
    	
    	RegulacionEspecificaDTO result = new RegulacionEspecificaDTO();
    	
    	// Guardo el identificador de relacion
    	result.setIdRelacion(idRelacion);
    	
    	// Obtengo el orden de la regulacion especifica
    	
    	String queryOrden= "SELECT proprel.valor " +
        " FROM Propiedadrelacion proprel " +          
        " WHERE proprel.relacion="+idRelacion +
        " AND proprel.iddefpropiedad=5 ";
    	
    	String valorOrden = (String)em.createQuery(queryOrden).getSingleResult();
    	
    	if (valorOrden!=null || valorOrden!="")
    	{
    		result.setOrden(Integer.parseInt(valorOrden));
    	}
    	
    	
    	// Obtengo el nombre de la regulacion especifica
    	String queryNombre= "SELECT proprel.valor " +
        " FROM Propiedadrelacion proprel " +          
        " WHERE proprel.relacion="+idRelacion +
        " AND proprel.iddefpropiedad=6 ";
    	
    	String valorNombre = (String)em.createQuery(queryNombre).getSingleResult();
    	
    	if (valorNombre!=null)
    	{
    		result.setNombre(valorNombre);
    	}
    	
    	// Obtengo el texto de la regulacion especifica
    	String queryTexto= "SELECT proprel.valor " +
        " FROM Propiedadrelacion proprel " +          
        " WHERE proprel.relacion="+idRelacion +
        " AND proprel.iddefpropiedad=7 ";
    	
    	String valorTexto = (String)em.createQuery(queryTexto).getSingleResult();
    	
    	if (valorTexto!=null)
    	{
    		result.setTexto(valorTexto);
    	}
    	
    	// Obtengo el padre de la regulacion especifica
    	String queryPadre= "SELECT vectorrelac2.valor " +
        " FROM Vectorrelacion vectorrelac2" +           
        " WHERE vectorrelac2.iddefvector=41 " +
        " AND vectorrelac2.relacion="+idRelacion ;
    	
    	int idPadreRegEsp = (Integer)em.createQuery(queryPadre).getSingleResult();
    	
    	if (idPadreRegEsp>0)
    	{
    		// Tiene Padre
    		result.setIdRegulacionEspPadre(idPadreRegEsp);
    		
    		// Busco el nombre del padre 		
        	String queryNombreREPadre= "SELECT proprel.valor " +
            " FROM Propiedadrelacion proprel " +          
            " WHERE proprel.relacion="+idPadreRegEsp +
            " AND proprel.iddefpropiedad=6 ";
        	
        	String valorNombreREPadre = (String)em.createQuery(queryNombreREPadre).getSingleResult();
        	
        	if (valorNombreREPadre!=null)
        	{
        		result.setNombreRegulacionEspPadre(valorNombreREPadre);
        	}
    		
    		
    	}
    	else
    	{
    		// No tiene Padre
    		result.setIdRegulacionEspPadre(idPadreRegEsp);
    		result.setNombreRegulacionEspPadre("");
    		
    	}
    	
        return result;
    }

    public boolean borrarRegulacionespecifica (int idRelacion)
    {
    	boolean resultado = false;
    	
    	// Obtengo los posibles hijos de la regulacion
    	String query= "SELECT vectorrelac2 " +
        " FROM Vectorrelacion vectorrelac2" +           
        " WHERE vectorrelac2.iddefvector=41 " +
        " AND vectorrelac2.valor="+idRelacion ;
        
        List<Vectorrelacion> regEspDetList = em.createQuery(query).getResultList();
        
        // Recorro los posibles hijos de forma recursiva para ir borrandolo todo los hijos
        if (regEspDetList!=null && regEspDetList.size()>0){
   			Iterator<Vectorrelacion> it = regEspDetList.iterator();
   			while (it.hasNext()){
   				borrarRegulacionespecifica(it.next().getRelacion().getIden());
   			}
   		}
        
        // Para esa relacion tengo que borrar tanto los registros de vectorrelacion, como los de propiedadesrelacion como el propio de relacion
        log.info("[borrarRegulacionespecifica] Se va a proceder a borrar la relacion:"+idRelacion);
        //em.remove(em.find(Vectorrelacion.class,new Integer(idRelacion)));
        borrarRelacionRegulacionEspecifica(idRelacion);
        resultado = true;
    	
    	
    	
    	return resultado;
    }

    private boolean borrarRelacionRegulacionEspecifica(int idRelacion) {

		boolean result = false;

		try {

			Relacion relacion = em.find(Relacion.class,idRelacion);

			// obtengo las propiedades de la relacion para borrarlos
			Set<Propiedadrelacion> pdadRelSet = relacion
					.getPropiedadrelacions();

			Iterator<Propiedadrelacion> itPdadRel = pdadRelSet.iterator();

			while (itPdadRel.hasNext()) {
				Propiedadrelacion pdadRel = itPdadRel.next();
				em.remove(pdadRel);
			}

			// obtengo los vectores de la relacion para borrarlos
			Set<Vectorrelacion> vectorRelSet = relacion.getVectorrelacions();
			Iterator<Vectorrelacion> itVectorRel = vectorRelSet.iterator();

			while (itVectorRel.hasNext()) {
				Vectorrelacion vecRel = itVectorRel.next();
				em.remove(vecRel);
			}

			// Ahora borro la relacion
			em.remove(relacion);
			result=true;
			

		} catch (Exception ex) {
			log.error("[borrarRelacionRegulacionEspecifica] Error al borrar la relacion:"
							+ ex.getMessage());
			ex.printStackTrace();

		}

		return result;
	}
    
    public List<DeterminacionDTO> obtenerDeterminacionesDocumento(int idDoc) {

    	List<DeterminacionDTO> result = new ArrayList<DeterminacionDTO>();
        

        String query= "SELECT det.determinacion " +
        " FROM Documentodeterminacion det " +
        " WHERE det.documento.iden = " +idDoc;
        
        try {
        	
        	List<Determinacion> ldet = em.createQuery(query).getResultList();
            
            
            for (Determinacion det : ldet) {
            	
            	 DeterminacionDTO unTram = new DeterminacionDTO();
            	 
            	 unTram.setSeleccionada(false);
            	 unTram.setIdDeterminacion(det.getIden());
            	 unTram.setNombreDeterminacion(det.getNombre());
            	 unTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
            	 
            	 result.add(unTram);
            	                	
               
            }
        	
        }
        catch (NoResultException ex)
		{
        	log.error("[getListDeterminacionRegimenDeTramite] ERROR:"+ex.getCause());
        	ex.printStackTrace();
		}
		catch (NullPointerException ex1)
		{
			log.error("[getListDeterminacionRegimenDeTramite] ERROR:"+ex1.getCause());
			ex1.printStackTrace();
		}
		catch (Exception e) {
			log.error("[getListDeterminacionRegimenDeTramite] ERROR:"+e.getCause());
			e.printStackTrace();
		}
        

        return result;
    }
    
    public void borrarRelacionDeterminacionConDocumento(int idDeterminacion, int idDoc)
    {
    	String query= "SELECT det" +
        " FROM Documentodeterminacion det " +
        " WHERE det.documento.iden = " +idDoc + 
        " AND det.determinacion.iden=" + idDeterminacion;
        
        try {
        	
        	List<Documentodeterminacion> ldet = em.createQuery(query).getResultList();
            
            if (ldet!=null && ldet.size()>0){
            	Documentodeterminacion det = ldet.get(0);
            	
            	em.remove(det);
            	em.flush();
            }
        	
        }
        catch (NoResultException ex)
		{
        	log.error("[getListDeterminacionRegimenDeTramite] ERROR:"+ex.getCause());
        	ex.printStackTrace();
		}
		catch (NullPointerException ex1)
		{
			log.error("[getListDeterminacionRegimenDeTramite] ERROR:"+ex1.getCause());
			ex1.printStackTrace();
		}
		catch (Exception e) {
			log.error("[getListDeterminacionRegimenDeTramite] ERROR:"+e.getCause());
			e.printStackTrace();
		}
        
    }
    
    public List<ParIdentificadorTexto> getRegulacionespecificaRaices(int idDeterminacion) {
        List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
       
        String query= "SELECT vectorrelac2 " +
        " FROM Vectorrelacion vectorrelac, " +
        "		Vectorrelacion vectorrelac2," +  
        "		Propiedadrelacion proprel " +  
        " WHERE vectorrelac.iddefvector=40 " +
        " AND vectorrelac.valor ="+idDeterminacion +
        " AND vectorrelac.relacion =vectorrelac2.relacion " +
        " AND vectorrelac2.iddefvector=41 " +
        " AND vectorrelac2.valor=0"+
        " AND proprel.relacion = vectorrelac.relacion"+
        " AND proprel.iddefpropiedad=5"+
        " ORDER BY proprel.valor ASC";
        
        List<Vectorrelacion> regEspDetList = em.createQuery(query).getResultList();
        for (Vectorrelacion regEspDet : regEspDetList) 
        {
        	int idRelacion = regEspDet.getRelacion().getIden();
        	
        	// Obtengo el orden de la regulacion especifica
        	
        	String queryOrden= "SELECT proprel.valor " +
            " FROM Propiedadrelacion proprel " +          
            " WHERE proprel.relacion="+idRelacion +
            " AND proprel.iddefpropiedad=5 ";
        	
        	String valorOrden = (String)em.createQuery(queryOrden).getSingleResult();
        	
        	// Obtengo el nombre de la regulacion especifica
        	String queryNombre= "SELECT proprel.valor " +
            " FROM Propiedadrelacion proprel " +          
            " WHERE proprel.relacion="+idRelacion +
            " AND proprel.iddefpropiedad=6 ";
        	
        	String valorNombre = (String)em.createQuery(queryNombre).getSingleResult();
        	
        	// Obtengo el texto de la regulacion especifica
        	String queryTexto= "SELECT proprel.valor " +
            " FROM Propiedadrelacion proprel " +          
            " WHERE proprel.relacion="+idRelacion +
            " AND proprel.iddefpropiedad=7 ";
        	
        	String valorTexto = (String)em.createQuery(queryTexto).getSingleResult();
        	
        	String valorTextoPublicar = "";
        	
        	// Publico los 30 primeros caracteres del texto
        	if (valorTexto.length()>30)
        	{
        		valorTextoPublicar = valorTexto.substring(0, 29)+ "...";
        	}
        	else
        	{
        		valorTextoPublicar = valorTexto;
        	}
        	
           ParIdentificadorTexto item = new ParIdentificadorTexto(idRelacion, valorTextoPublicar, "regulacionesespecificas");
           
           // Compruebo si es una hoja (no tiene hijos)
		item.setHoja(!tieneHijaRegulacionEspecifica(idRelacion));
          
           result.add(item);
        }

        return result;
    }
    
    private boolean tieneHijaRegulacionEspecifica(int idRegulacionEspec)
    {
    	boolean resul = false;
    	
    	String query= "SELECT vectorrelac2 " +
        " FROM Vectorrelacion vectorrelac2" +           
        " WHERE vectorrelac2.iddefvector=41 " +
        " AND vectorrelac2.valor="+idRegulacionEspec ;
        
        List<Vectorrelacion> regEspDetList = em.createQuery(query).getResultList();
    	
		if (regEspDetList.size()>0)
			resul = true;
    	
    	
    	return resul;
    }

    public List<ParIdentificadorTexto> getRegulacionespecificaHijas(int idREPadre) {
        
    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
        
    	String query= "SELECT vectorrelac2 " +
        " FROM Vectorrelacion vectorrelac2" +           
        " WHERE vectorrelac2.iddefvector=41 " +
        " AND vectorrelac2.valor="+idREPadre ;
        
        List<Vectorrelacion> regEspDetList = em.createQuery(query).getResultList();
        for (Vectorrelacion regEspDet : regEspDetList) 
        {
        	int idRelacion = regEspDet.getRelacion().getIden();
        	
        	// Obtengo el orden de la regulacion especifica
        	
        	String queryOrden= "SELECT proprel.valor " +
            " FROM Propiedadrelacion proprel " +          
            " WHERE proprel.relacion="+idRelacion +
            " AND proprel.iddefpropiedad=5 ";
        	
        	String valorOrden = (String)em.createQuery(queryOrden).getSingleResult();
        	
        	// Obtengo el nombre de la regulacion especifica
        	String queryNombre= "SELECT proprel.valor " +
            " FROM Propiedadrelacion proprel " +          
            " WHERE proprel.relacion="+idRelacion +
            " AND proprel.iddefpropiedad=6 ";
        	
        	String valorNombre = (String)em.createQuery(queryNombre).getSingleResult();
        	
        	// Obtengo el texto de la regulacion especifica
        	String queryTexto= "SELECT proprel.valor " +
            " FROM Propiedadrelacion proprel " +          
            " WHERE proprel.relacion="+idRelacion +
            " AND proprel.iddefpropiedad=7 ";
        	
        	String valorTexto = (String)em.createQuery(queryTexto).getSingleResult();
        	
        	String valorTextoPublicar = "";
        	
        	// Publico los 30 primeros caracteres del texto
        	if (valorTexto.length()>30)
        	{
        		valorTextoPublicar = valorTexto.substring(0, 29)+ "...";
        	}
        	else
        	{
        		valorTextoPublicar = valorTexto;
        	}
        	
           ParIdentificadorTexto item = new ParIdentificadorTexto(idRelacion,valorTextoPublicar, "regulacionesespecificas");
           
           // Compruebo si es una hoja (no tiene hijos)
           item.setHoja(!tieneHijaRegulacionEspecifica(idRelacion));
           result.add(item);
        }

        return result;
    	
    }
    
    public int obtenerOrderNuevaDeterminacion(int idTramite, int idDetPadre) {
    	int orden = 0;
    	if(idTramite != 0) {
    		String consulta = "SELECT MAX(det.orden) FROM Determinacion det WHERE det.tramite.iden = " + idTramite;
    		if(idDetPadre != 0) 
    			consulta += " AND det.determinacionByIdpadre.iden = " + idDetPadre;
    		else
    			consulta += " AND det.determinacionByIdpadre IS NULL";
    		
    		try {
				orden = (Integer) em.createQuery(consulta).getSingleResult();
				
				// A menos de que el orden venga a 0 (No existan determinaciones), incrementamos 1.
				if(orden != 0) 
					orden++;
				
				
			} 
    		catch (NoResultException ex)
    		{
    			// Si no obtiene resultado, iniciamos el orden a 1
    			orden = 1;
    		}
    		catch (NullPointerException ex1)
    		{
    			// Si no obtiene resultado, iniciamos el orden a 1
    			orden = 1;
    		}
    		catch (Exception e) {
				log.error(e.getCause(), null);
				e.printStackTrace();
			}
    	}
    	
    	return orden;
    }
    
    
    
    // -----------------------------------
    // Determinacion Regimen
    // -----------------------------------
    
    public List<DeterminacionDTO> getListDeterminacionRegimenDeTramite(int idTramite) {

    	List<DeterminacionDTO> result = new ArrayList<DeterminacionDTO>();
        

        String query= "SELECT det " +
        " FROM Determinacion det " +
        " WHERE det.tramite.iden = " +idTramite+
        " AND (det.idcaracter = 7 OR det.idcaracter = 8) ORDER BY det.nombre ASC";
        
        try {
        	
        	List<Determinacion> ldet = em.createQuery(query).getResultList();
            
            
            for (Determinacion det : ldet) {
            	
            	 DeterminacionDTO unTram = new DeterminacionDTO();
            	 
            	 unTram.setSeleccionada(false);
            	 unTram.setIdDeterminacion(det.getIden());
            	 unTram.setNombreDeterminacion(det.getNombre());
            	 unTram.setOrdenArbol(obtenerOrdenCompletoDeterminacion(det.getIden()));
            	 
            	 result.add(unTram);
            	                	
               
            }
        	
        }
        catch (NoResultException ex)
		{
        	log.error("[getListDeterminacionRegimenDeTramite] ERROR:"+ex.getCause());
        	ex.printStackTrace();
		}
		catch (NullPointerException ex1)
		{
			log.error("[getListDeterminacionRegimenDeTramite] ERROR:"+ex1.getCause());
			ex1.printStackTrace();
		}
		catch (Exception e) {
			log.error("[getListDeterminacionRegimenDeTramite] ERROR:"+e.getCause());
			e.printStackTrace();
		}
        

        return result;
    }
    
 // -----------------------------------
    // Determinacion de Documento
    // -----------------------------------
    
    
    
    // -----------------------------------
    // Entidades a la que esta aplicada una determinacion
    // -----------------------------------
    
    public List<ParIdentificadorTexto> obtenerEntidadesDondeSeAplicaDeterminacion(int idDeterminacion)
    {
    
    	log.debug("[obtenerEntidadesDondeSeAplicaDeterminacion] Buscamos las entidades donde se aplica la determinacion="+idDeterminacion);

    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
    	
    	
    	 // Obtengo el listado
        String query= " select cu.entidad from Entidaddeterminacion cu where cu.determinacion.iden="+idDeterminacion;

        
        try {
            
            
            List<Entidad> lent = em.createQuery(query).getResultList();

    		for (Entidad ent : lent) {

    			String texto = "";

    			texto = ent.getNombre()+" ("+ent.getTramite().getIden()+")";

    			ParIdentificadorTexto item = new ParIdentificadorTexto(ent.getIden(), texto, "entidad");

    			
    			result.add(item);

    		}
        } catch (NoResultException e) {
            log.error("[obtenerEntidadesDondeSeAplicaDeterminacion] No se ha encontrado el listado de obtenerEntidadesDondeSeAplicaDeterminacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[obtenerEntidadesDondeSeAplicaDeterminacion] Error en la busqueda del listado de obtenerEntidadesDondeSeAplicaDeterminacion: " + ex.getMessage());

        }
    	
        log.debug("[obtenerEntidadesDondeSeAplicaDeterminacion] Numero de entidades que se aplican="+result.size());

    	return result;
    }
    
    public Determinacion findByBaseGrupo(int idTramiteBase, String grupo)
	{	
		String hql = "SELECT d" +
        " FROM Determinacion d" +
        " WHERE d.tramite.iden=" + idTramiteBase +
        " AND LOWER(d.codigo) = '" + grupo.toLowerCase() + "'" + 
        " ORDER BY  d.nombre ASC";
		
		log.debug("BaseGrupo---" + grupo + "--" + idTramiteBase + "--" + hql);
		
		List<Determinacion> ldet = null;
		
		try {
			ldet = em.createQuery(hql).getResultList();
		}
		catch (Exception e) {
			// TODO: handle exception
			log.error("[findByBaseGrupo] Error en la busqueda del listado de " + hql);
		}
		if (ldet!=null && ldet.size()>0){
			return ldet.get(0);
		}
		else
			return null;
	}
    
    public boolean tieneDeterminacionTramite(int idTramite)
    {
		
	   	boolean resultado = false;
	   	
	   	List<Entidad> lista = new ArrayList<Entidad>();
	   	
	   	String cadena = "SELECT det " +
	       " FROM Determinacion det " +
	       " WHERE det.tramite ="+idTramite;
	
	   	try
	   	{
	   		Query query = em.createQuery(cadena);
	   		
	   		lista = query.getResultList();
	   		
	   		if (lista.size()>2)
	   		{
	   			resultado = true;
	   		}
	   	}
	   	catch (Exception e)
	   	{
	   		log.error("[tieneDeterminacionTramite] error="+e.getLocalizedMessage());
	   		e.printStackTrace();
	   	}
			
			
			return resultado;
	}
    
    public int getIdentificadorCaracter(String literal)
	{
		int id = -1;
		
		// Obtenemos el caracterç
		String query="SELECT carac.iden " +
         " FROM Caracterdeterminacion carac " + 
         " JOIN carac.literal.traduccions trans" +
         " WHERE TRIM(LOWER(trans.texto)) = '" + literal.toLowerCase().trim() + "'";
		
		List<Integer> lista = (List<Integer>) em.createQuery(query).getResultList();
		
		if (lista!=null && lista.size()>0)
			return lista.get(0);
		else
			return id;
	}
    
    public Determinacion obtenerEquivalente(String nombre, int idTramiteBase)
	{	
		String hql = "SELECT d" +
        " FROM Determinacion d" +
        " WHERE d.tramite.iden=" + idTramiteBase +
        " AND LOWER(d.nombre) = '" + nombre.toLowerCase() + "'" + 
        " ORDER BY  d.nombre ASC";
		
		List<Determinacion> ldet = em.createQuery(hql).getResultList();
		
		if (ldet!=null && ldet.size()>0){
			return ldet.get(0);
		}
		else
			return null;
	}
    
    public Determinacion obtenerEquivalenteOrden(String orden, String nombre, int idTramiteBase)
	{	
    	int idDet = buscarDeterminacionPorTramiteYOrden(idTramiteBase, orden);
    	
    	Determinacion det = new Determinacion();
    	
    	det = em.find(Determinacion.class, idDet);
    	
    	if (det!=null && det.getNombre()!=null && det.getNombre().equalsIgnoreCase(nombre))
    		return det;
    	else
    		return null;
    			
	}
    
    public void borrarDeterminacionesTramite(int idTramite)
	{
		List<Object[]> lista = findAllByIdTramiteSelect(idTramite);
		for (Object[] obj:lista)
		{	
			removeByDeterminacion((Integer)obj[0]);
			removeRecursivoEnCascada((Integer)obj[0]);
			
		}
	}
    
    public List<Object[]> findAllByIdTramiteSelect(int idTramite)
    {
    	List<Object[]> lista = new ArrayList<Object[]>();
    	
    	String cadena = "SELECT det.iden, det.nombre " +
        " FROM Determinacion det " +
        " WHERE det.tramite ="+idTramite;

		Query query = em.createQuery(cadena);
		
		lista = query.getResultList();
		
		return lista;
    }
    
    public void removeRecursivoEnCascada(int idPadre) {
        
   		Determinacion p = em.find(Determinacion.class, idPadre);
    	
   		if (p!=null)
   		{
	    	List<Determinacion> lista = findDeterminacionesByPadre(p.getIden());
	   		if (lista!=null && lista.size()>0){
	   			Iterator<Determinacion> it = lista.iterator();
	   			while (it.hasNext()){
	   				removeRecursivoEnCascada((it.next()).getIden());
	   			}
	   		}
	   		
	   		// Debo borrar los OpcionDeterminacion de cada determinacion borrada
	    	Set<Opciondeterminacion> opciones = p.getOpciondeterminacionsForIddeterminacion();
	    	if (opciones!=null && !opciones.isEmpty()){
		    	for (Opciondeterminacion opcion : opciones) {
		    		em.remove(opcion);
				}
	    	}
	    	
	    	Set<Opciondeterminacion> opciones2 = p.getOpciondeterminacionsForIddeterminacionvalorref();
	    	if (opciones2!=null && !opciones2.isEmpty()){
		    	for (Opciondeterminacion opcion : opciones2) {
		    		em.remove(opcion);
				}
	    	}
	    	
	    	// Debo borrar los documentos determinacion de cada determinacion borrada
	    	Set<Documentodeterminacion> documentosABorrar = p.getDocumentodeterminacions();
	   		if (documentosABorrar!=null && !documentosABorrar.isEmpty()){
	   			for (Documentodeterminacion documentodeterminacion : documentosABorrar) {
	   				em.remove(documentodeterminacion);
	   			}
	   		}
	   		
	   		// Debo borrar las OperacionDeterminacion relacionadas
	   		Set<Operaciondeterminacion> operadas = p.getOperaciondeterminacionsForIddeterminacion();
	   		if (operadas!=null && !operadas.isEmpty()){
	   			for (Operaciondeterminacion operaciondeterminacion : operadas) {
	   				em.remove(operaciondeterminacion);
				}
	   		}
	   		
	   		Set<Operaciondeterminacion> operadoras = p.getOperaciondeterminacionsForIddeterminacionoperadora();
	   		if (operadoras!=null && !operadoras.isEmpty()){
	   			for (Operaciondeterminacion operaciondeterminacion : operadoras) {
	   				em.remove(operaciondeterminacion);
				}
	   		}
	   		  		
	   		// Debo borrar los DeterminacionGrupoEntidad relacionados
	   		Set<Determinaciongrupoentidad> dges = p.getDeterminaciongrupoentidadsForIddeterminacion();
	   		if (dges!=null && !dges.isEmpty())
	   			for (Determinaciongrupoentidad dge : dges) {
	   				em.remove(dge);
	   			}
	   		
	   		Set<Determinaciongrupoentidad> dges2 = p.getDeterminaciongrupoentidadsForIddeterminaciongrupo();
	   		if (dges2!=null && !dges2.isEmpty())
	   			for (Determinaciongrupoentidad dge : dges2) {
	   				em.remove(dge);
	   			}
	   		
	   		// Ahora si: borro la determinacion
	   		em.remove(p);
	   		em.flush();
   		}

   }
    
    private List<Determinacion> findDeterminacionesByPadre(int idPadre) {
		
		List<Determinacion> lista = new ArrayList<Determinacion>();
		String cadena = "SELECT determinacion " +
        " FROM Determinacion determinacion " +
        " WHERE determinacion.determinacionByIdpadre.iden=" + idPadre;
		
		Query query = em.createQuery(cadena);
		
		try {
			lista = query.getResultList();
		} catch (NoResultException e) {
            log.error("[findDeterminacionesByPadre] No se han encontrado determinaciones para ese padre." + e.getMessage());

        } catch (Exception ex) {
            log.error("[findDeterminacionesByPadre] Error en la busqueda de las determinaciones hijas de un padre: " + ex.getMessage());

        }
		
		return lista;
	}
    
    public String nextCodigo(String idBDTramite){
    	
     	String result = "";
     	
     	String query =  "SELECT max(codigo) " +
    	" FROM Determinacion det"+
    	" WHERE det.tramite.iden="+idBDTramite;

        Query q = em.createQuery(query);
        

        try {
            result = (String) q.getSingleResult();
        } catch (NoResultException e) {
            log.error("[nextCodigo] No se ha encontrado el siguiente codigo de Determinacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[nextCodigo] Error en la busqueda del siguiente codigo de Determinacion: " + ex.getMessage());

        }
        
        String s;
        if (result!=null && !result.equals(""))
        {
        	// Hay que tratar el valor para añadir los ceros que falten si fuera necesario
            Long l = Long.valueOf(result);
            l++;
            s = l.toString();
            int cerosRestantes = 10-s.length();
            
            for (int j = cerosRestantes; j > 0; j--) {
    			s = "0"+s;
    		}
        }
        else
        {
        	
        	s="0000000001";
        }
        
        
        return s;
    	
    }
    
    public Determinacion findByNombreTramite(int idTramiteBase, String nombre)
	{	
		String hql = "SELECT d" +
        " FROM Determinacion d" +
        " WHERE d.tramite.iden=" + idTramiteBase +
        " AND LOWER(d.nombre) = '" + nombre.toLowerCase() + "'" + 
        " ORDER BY  d.nombre ASC";
		
		log.debug("BaseGrupo---" + nombre + "--" + idTramiteBase + "--" + hql);
		
		List<Determinacion> ldet = em.createQuery(hql).getResultList();
		
		if (ldet!=null && ldet.size()>0){
			return ldet.get(0);
		}
		else
			return null;
	}
    
    public void removeByDeterminacion(int idDeterminacion){
    	
     	String query =  "SELECT opDet " +
    	" FROM Opciondeterminacion opDet"+
    	" WHERE opDet.determinacionByIddeterminacion="+idDeterminacion+
    	" OR opDet.determinacionByIddeterminacionvalorref="+idDeterminacion;

        Query qNombreAmbito = em.createQuery(query);
        
        try {
        	// Se borran todos los resultantes
        	List<Opciondeterminacion> result = (List<Opciondeterminacion>) qNombreAmbito.getResultList().get(0);
        	for (Opciondeterminacion valorReferencia : result)
            {
            	em.remove(valorReferencia);
        		
            }
        } catch (NoResultException e) {
            log.error("[removeByDeterminacionAndValorReferencia] No se ha encontrado la Opcion Determinacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[removeByDeterminacionAndValorReferencia] Error en la busqueda de la Opcion Determinacion: " + ex.getMessage());

        }
    	
    }

    public boolean crearValorRefenciaDeterminacionPadre (Determinacion hijo, int idPadre)
    {
    	boolean result = false;
    	try {
	    	
	    	Determinacion detPadre = em.find(Determinacion.class, idPadre);
	    	
	    	//Determinacion detHijo = em.find(Determinacion.class, idHijo);
	    	
	    	Opciondeterminacion opciondeterminacion = new Opciondeterminacion();
			
			opciondeterminacion.setDeterminacionByIddeterminacion(detPadre);
			opciondeterminacion.setDeterminacionByIddeterminacionvalorref(hijo);
			
			em.persist(opciondeterminacion);
			
			log.debug("OPCIÓN DETERMINACION CREADA");
	    	result = true;
		
	    } catch (NoResultException e) {
	        log.error("[guardarHijosComoValorReferencia] No se ha encontrado la Determinacion." + e.getMessage());
	
	    } catch (Exception ex) {
	        log.error("[guardarHijosComoValorReferencia] Error en la busqueda de la  Determinacion: " + ex.getMessage());
	
	    }
    	return result;
    }
    
    public List<BusquedaDeterminacionDTO> resultadosBusquedaAvanzadaDeterminacion(FiltrosDTO filtros, int idTramite) {
        
    	
    	List<BusquedaDeterminacionDTO> result = new ArrayList<BusquedaDeterminacionDTO>();
    	
        // Obtengo el listado
        String query = "SELECT ent " +
        " FROM Determinacion ent where ent.tramite.iden = " +idTramite;
        
        String clausulas="";
        boolean puesto = false;
        
        if (filtros.getNombre()!=null && !filtros.getNombre().equals("")){
        	puesto = true;
        	clausulas = clausulas + " lower(ent.nombre) like '%" + filtros.getNombre().toLowerCase() + "%' ";
        }
        
        if (filtros.getApartado()!=null && !filtros.getApartado().equals("")){
        	if (puesto)
        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(ent.apartadoCompleto) like '%" + filtros.getApartado().toLowerCase() + "%' ";
        	else{
        		puesto=true;
        		clausulas = clausulas + " lower(ent.apartadoCompleto) like '%" + filtros.getApartado().toLowerCase() + "%' ";
        	}
        }
        if (filtros.getArticulado()!=null && !filtros.getArticulado().equals("")){
        	if (puesto)
        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(ent.apartado) like '%" + filtros.getArticulado().toLowerCase() + "%' ";
        	else{
        		puesto=true;
        		clausulas = clausulas + " lower(ent.apartado) like '%" + filtros.getArticulado().toLowerCase() + "%' ";
        	}
        }
        
        if (puesto)
        	query = query + " and (" + clausulas + ")"; 
        
        Tramite tram = em.find(Tramite.class, idTramite);
        
        try {
        	
        	// Obtenemos los datos para la paginacion
        	int maxResults = filtros.getMaxResultados();
        	String consulta = "SELECT COUNT(*) FROM Determinacion ent where ent.tramite.iden = " +idTramite;
        	
        	if (puesto){
        		consulta += " and (" + clausulas + ")";
        	}
        	
        	Long totalRegistros = (Long) em.createQuery(consulta).getSingleResult();     
        	filtros.setTotalRegistros(totalRegistros);
        	int firstResult = (filtros.getPagina() -1) * filtros.getMaxResultados();
        	if(firstResult > totalRegistros)
        		firstResult = 0;
        	
        	List<Determinacion> resultList = (List<Determinacion>) em.createQuery(query)
        	.setMaxResults(maxResults)
        	.setFirstResult(firstResult)
        	.getResultList();
        	
        	filtros.actualizarPaginas();
        	
        	for (Determinacion det : resultList) {
        		 
        		BusquedaDeterminacionDTO dto = new BusquedaDeterminacionDTO();
        		
        		dto.setNombre(det.getNombre());
        		dto.setId(det.getIden());
        		dto.setApartado(det.getApartadoCompleto());
        		
        		dto.setArticulado(det.getApartado());
        		dto.setPlan(tram.getPlan().getNombre());
        		dto.setTipo(caracterString(det.getIdcaracter()));
        		dto.setTipoPlan(obtenerTipoPlan(idTramite));
        		
        		result.add(dto);
            }
        	
        } catch (NoResultException e) {
            log.error("[getTramitePorPlan] No se ha encontrado el listado de tramites para un plan." + e.getMessage());

        } catch (Exception ex) {
            log.error("[getTramitePorPlan] Error en la busqueda del listado de tramites para un plan " + ex.getMessage());

        }
		
        return result;
    }
    
    public String obtenerTipoPlan (int idTramite)
    {
    	String tipo = "Encargado";
    	
    	String query = "SELECT count(*)" +
        " FROM PlanesEncargados ent where ent.tramiteEncargado.iden=" + idTramite;
    	
    	Long total = (Long) em.createQuery(query).getSingleResult();
    	if (total>0)
    		return "Encargado";
    	
    	query = "SELECT count(*)" +
        " FROM PlanesEncargados ent where ent.tramiteVigente.iden=" + idTramite;
    	total = (Long) em.createQuery(query).getSingleResult();
    	if (total>0)
    		return "Vigente";
    	
    	query = "SELECT count(*)" +
        " FROM PlanesBase ent where ent.tramite.iden=" + idTramite;
    	total = (Long) em.createQuery(query).getSingleResult();
    	if (total>0)
    		return "Base";
    	
    	return tipo;
    	
    }
    
    
    public void crearDeterminacionesUnidades (PlanesEncargados planEncargado)
    {
    	Determinacion detUnidad = new Determinacion();
    	
    	detUnidad.setTramite(planEncargado.getTramiteEncargado());
    	detUnidad.setNombre("Unidades");
    	detUnidad.setOrden(98);
    	
    	detUnidad.setApartado("98");
    	detUnidad.setCodigo(nextCodigo(String.valueOf(planEncargado.getTramiteEncargado().getIden())));
    	detUnidad.setDeterminacionByIdpadre(null);
    	detUnidad.setIdcaracter(1);
    	
    	em.persist(detUnidad);
    	em.flush();
    	
    	Determinacion detNp = new Determinacion();
    	detNp.setTramite(planEncargado.getTramiteEncargado());
    	detNp.setNombre("No Procede");
    	detNp.setOrden(1);
    	
    	detNp.setApartado("1");
    	detNp.setCodigo(nextCodigo(String.valueOf(planEncargado.getTramiteEncargado().getIden())));
    	detNp.setDeterminacionByIdpadre(detUnidad);
    	detNp.setIdcaracter(18);
    	
    	em.persist(detNp);
    	em.flush();
    	
    	int res = crearUnidad(detNp.getIden(), "NP", "No Procede");
    	
    }
    
    public int crearUnidad(int idDeterminacion, String abreviatura, String definicion) {
		
		int unidadCreadaCorrectamente = 0;
		int caracterUnidad = 18;
		int idTramite = 0;
    	
		 log.debug("[guardarUnidad] Se va a crear los detalles de la unidad. Determinacion Caracter unidad= " + idDeterminacion+ " // Detalle Unidad: abreviatura="+abreviatura+" / definicion="+definicion);
    	
		 
		 // Compruebo si la determinacion es efectivamente de caracter unidad
		 
		try
	    {
			Determinacion detUnidad = (Determinacion) em.find(Determinacion.class, idDeterminacion);
			
			if (detUnidad.getIdcaracter()!=caracterUnidad)
			{
				log.error("[guardarUnidad] Error la determinacion no es de caracter unidad con id= "+idDeterminacion);
			}
			else
			{
				log.debug("[guardarUnidad] La determinacion es de caracter unidad con id= "+idDeterminacion);
				unidadCreadaCorrectamente = 1;
				idTramite = detUnidad.getTramite().getIden();
			}
			
	    }
    	catch (Exception e) {
    		unidadCreadaCorrectamente = 0;
			log.error("[guardarUnidad] Error la determinacion no existe id= "+idDeterminacion);
			e.printStackTrace();
		}
		 
		
    	if (unidadCreadaCorrectamente!=0)
    	{
    		try
        	{
        		// Creo una nueva relacion
            	Relacion relacionRPManadir = new Relacion();
            	
            	// Creo dos nuevas propiedadesrelacion
            	Propiedadrelacion propiedadRelacionRPManadir1 = new Propiedadrelacion ();
            	Propiedadrelacion propiedadRelacionRPManadir2 = new Propiedadrelacion ();
            	
            	// Creo un nuevo vector relacion
                Vectorrelacion vectorRelacionRPManadir = new Vectorrelacion();
                
                // Opero con la relacion
                relacionRPManadir.setIddefrelacion(1);
                relacionRPManadir.setTramiteByIdtramitecreador(em.find(Tramite.class, idTramite));
                em.persist(relacionRPManadir);
                log.debug("[guardarUnidad] --    CREADA Relacion " + relacionRPManadir.getIden());
                
                // Opero con la propiedadRelacionRPManadir1
                propiedadRelacionRPManadir1.setRelacion(relacionRPManadir);
                propiedadRelacionRPManadir1.setIddefpropiedad(1);
                propiedadRelacionRPManadir1.setValor(abreviatura);
                em.persist(propiedadRelacionRPManadir1);
                log.debug("[guardarUnidad] --    CREADA propiedadRelacion1 " + propiedadRelacionRPManadir1.getIden());
                
                
                // Opero con la propiedadRelacionRPManadir2
                propiedadRelacionRPManadir2.setRelacion(relacionRPManadir);
                propiedadRelacionRPManadir2.setIddefpropiedad(2);
                propiedadRelacionRPManadir2.setValor(definicion);
                em.persist(propiedadRelacionRPManadir2);
                log.debug("[guardarUnidad] --    CREADA propiedadRelacion2 " + propiedadRelacionRPManadir2.getIden());
            	
                // Opero con vectorRelacion
                vectorRelacionRPManadir.setRelacion(relacionRPManadir);
                vectorRelacionRPManadir.setIddefvector(1);
                vectorRelacionRPManadir.setValor(idDeterminacion);
                em.persist(vectorRelacionRPManadir);
                log.debug("[guardarUnidad] --    CREADA vectorRelacionRPManadir " + vectorRelacionRPManadir.getIden());
                
                unidadCreadaCorrectamente = 1;
                log.info("[guardarUnidad] Creada correctamente la unidad de la determinacionunidad con id= "+idDeterminacion);
                
                //Persisto
                em.flush();

        	}
        	catch (Exception e) {
        		unidadCreadaCorrectamente = 0;
    			log.error("[guardarUnidad] Error al crear la unidad de la determinacionunidad con id= "+idDeterminacion);
    			e.printStackTrace();
    		}
    	}
		
    	log.debug("[guardarUnidad] Resultado= " + unidadCreadaCorrectamente);
    	return unidadCreadaCorrectamente;
		
	}
    
}
