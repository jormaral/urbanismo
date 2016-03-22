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

package com.mitc.redes.editorfip.servicios.gestionfip.importarfip;

import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.w3c.dom.Document;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;



public class Utiles extends ImportadorBase  {
	
	Log  log = Logging.getLog(Utiles.class);

	public static EntityManager em;
	static public Resultado resultado;
	

	// Datos globales de la importación
	public static Map<String,Determinacion> mapDet = new HashMap<String,Determinacion>();
	public static Map<String,Entidad> mapEntidad = new HashMap<String,Entidad>();
	public static Map<String,Documento> mapDocumento = new HashMap<String,Documento>();
	public static Map<String,Casoentidaddeterminacion> mapCasos = new HashMap<String, Casoentidaddeterminacion>();
	public static Map<String,Opciondeterminacion> mapOpDet = new HashMap<String, Opciondeterminacion>();
	
	
	protected Utiles(Document xml, EntityManager emParam) {
		super(xml, emParam);
	}

	public static void limpiar() {
		
		mapDet = new HashMap<String,Determinacion>();
		mapEntidad = new HashMap<String,Entidad>();
		mapDocumento = new HashMap<String,Documento>();
		mapCasos = new HashMap<String, Casoentidaddeterminacion>();
		mapOpDet = new HashMap<String, Opciondeterminacion>();
	}

	
	/*
	 * 	AÑADIR UN ERROR AL RESULTADO
	 */
	public  void error(String err) {
		log.error(err);
		resultado.error(err);
	}
	
	/*
	 * 	AÑADIR INFO AL RESULTADO
	 */
	public  void info(String s) {
		log.info(s);
		resultado.info(s);
	}
	

	
	/*
	 * 	COGER DETERMINACIÓN
	 * 
	 * 	Busca en la BD la determinación dada. Devuelve null si no 
	 * 	se ha encontrado
	 * 
	 */
	public static Determinacion getDeterminacion(String codTramite, String codDeterminacion) {
		return getDeterminacion(codTramite, codDeterminacion, Utiles.mapDet);
	}
	public static Determinacion getDeterminacion(String codTramite, String codDeterminacion, Map<String,Determinacion> mapAux) {
		
		String codComp = codTramite+"/"+codDeterminacion;
		
		Determinacion d = mapAux.get(codComp);
		if (d==null) d = Utiles.mapDet.get(codComp);
		
		if (d==null) {
	    	List<?> result = em.createQuery(
					"select d from Determinacion d, Tramite t "
					+ "where d.tramite=t.iden "
					+ "and d.codigo = '" + codDeterminacion + "' "
					+ "and t.codigofip = '" + codTramite + "'")
					.setMaxResults(1).getResultList();
	    	//resultado.countSQL++;
    	
			if (!result.isEmpty()) {	
				d =(Determinacion) result.get(0);
				mapAux.put(codComp, d);
			}			
		}
		return d;
	}
	
	
	/*
	 * 	COGER ENTIDAD
	 * 
	 * 	Busca en la BD la entidad dada. Devuelve null si no 
	 * 	se ha encontrado
	 * 
	 */
	public static Entidad getEntidad(String codTramite, String codEntidad) {
		return getEntidad(codTramite, codEntidad, Utiles.mapEntidad);
	}
	public static Entidad getEntidad(String codTramite, String codEntidad, Map<String,Entidad> mapAux) {
		
		String codComp = codTramite+"/"+codEntidad;
		
		Entidad e = mapAux.get(codComp);
		if (e==null) e = Utiles.mapEntidad.get(codComp);
		
		if (e==null) {
	    	List<?> result = em.createQuery(
					"select e from Entidad e, Tramite t "
					+ "where e.tramite=t.iden "
					+ "and e.codigo = '" + codEntidad + "' "
					+ "and t.codigofip = '" + codTramite + "'")
					.setMaxResults(1).getResultList();
	    	//resultado.countSQL++;
    	
			if (!result.isEmpty()) {	
				e =(Entidad) result.get(0);
				mapAux.put(codComp, e);
			}			
		}
		return e;
	}
	
	
	/*
	 * 	COGER DOCUMENTO
	 * 
	 * 	Busca en la BD un documento dado. Devuelve null si no 
	 * 	se ha encontrado
	 * 
	 */
	public static Documento getDocumento(String codTramite, String codDocumento) {
		return getDocumento(codTramite, codDocumento, Utiles.mapDocumento);
	}
	public static Documento getDocumento(String codTramite, String codDocumento, Map<String,Documento> mapAux) {
		
		String codComp = codTramite+"/"+codDocumento;
		
		Documento e = mapAux.get(codComp);
		if (e==null) e = Utiles.mapDocumento.get(codComp);
		
		if (e==null) {
	    	List<?> result = em.createQuery(
					"select d from Documento d, Tramite t "
					+ "where d.tramite=t.iden "
					+ "and d.archivo = '" + codDocumento + "' "
					+ "and t.codigofip = '" + codTramite + "'")
					.setMaxResults(1).getResultList();
	    	//resultado.countSQL++;
    	
			if (!result.isEmpty()) {	
				e =(Documento) result.get(0);
				mapAux.put(codComp, e);
			}			
		}
		return e;
	}
	
	
	
	/*
	 * 	COGER OPCION DETERMINACION
	 * 
	 * 	Busca en la BD la opcion determinacion dada. Devuelve null si no 
	 * 	se ha encontrado
	 * 
	 */
	public static Opciondeterminacion getOpcionDeterminacion(
			String codTramite1, 
			String codDeterminacion1, 
			String codTramite2, 
			String codDeterminacion2
	) {
		return getOpcionDeterminacion(
				codTramite1, 
				codDeterminacion1, 
				codTramite2, 
				codDeterminacion2, 
				Utiles.mapOpDet);
	}
	// ...................
	public static Opciondeterminacion getOpcionDeterminacion(
			String codTramite1, 
			String codDeterminacion1, 
			String codTramite2, 
			String codDeterminacion2,
			Map<String,Opciondeterminacion> mapAux
	) {
		
		String codComp = codTramite1+"/"+codDeterminacion1+"/"+codTramite2+"/"+codDeterminacion2;
		
		Opciondeterminacion e = mapAux.get(codComp);
		if (e==null) e = Utiles.mapOpDet.get(codComp);
		
		if (e==null) {
	    	List<?> result = em.createQuery(
					"select od from Opciondeterminacion od, Determinacion d1, Determinacion d2 "
					+ "where od.determinacionByIddeterminacion = d1.iden "
					+ "and od.determinacionByIddeterminacionvalorref = d2.iden "		
					+ "and d1.codigo = '" + codDeterminacion1 + "' "
					+ "and d1.tramite.codigofip = '" + codTramite1 + "'" 
					+ "and d2.codigo = '" + codDeterminacion2 + "' "
					+ "and d2.tramite.codigofip = '" + codTramite2 + "'"
			).setMaxResults(1).getResultList();
	    	//resultado.countSQL++;
    	
			if (!result.isEmpty()) {	
				e =(Opciondeterminacion) result.get(0);
				mapAux.put(codComp, e);
			}			
		}
		return e;
	}
	
	public static String calcularCodigoPlan(int idAmbito) {
		
		// Necesitamos el código
		List<?> result = em.createQuery("select max(codigo) from Plan where idambito = :idambito")
		    .setParameter("idambito", idAmbito)
		    .setMaxResults(1)
		    .getResultList();
		int k = 0;
		if (result.get(0) != null) k = Integer.parseInt(result.get(0).toString().trim());
		k++;

		return String.valueOf( (new Formatter()).format("%05d", k) );
	}
	
}
