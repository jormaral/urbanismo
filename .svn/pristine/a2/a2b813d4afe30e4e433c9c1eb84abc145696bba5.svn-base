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

package com.mitc.redes.editorfip.servicios.basicos.fip.entidades;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.EntidadBusquedaDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.ColoresEntidades;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Ambitoaplicacionambito;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Conjuntoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoshp;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Planentidadordenacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;

@Stateful
@Name("servicioCRUDEntidades")
public class ServicioCRUDEntidadesBean implements ServicioCRUDEntidades {
	@Logger
	private Log log;

	@PersistenceContext
	EntityManager em;

	// -----------------------
	// ACCIONES
	// -----------------------

	public void edit(Entidad entidad) {
		em.merge(entidad);
		log.info(entidad, "Se ha guardado la entidad en la tabla planeamiento.entidades");
	}

	public int create(Entidad entidad) {

		em.persist(entidad);

		return entidad.getIden();

	}
	
	public void editColor(ColoresEntidades entidadColor) {
		em.merge(entidadColor);
		
	}

	public int createColor(ColoresEntidades entidadColor) {

		em.persist(entidadColor);
		log.info(entidadColor, "Se ha guardado la entidad en la tabla gestionfip.coloresentidades");

		return entidadColor.getIdentidad();

	}
	
	public void borrarColorDeEntidad(int idEntidad) {
		
		
		String queryPOL = " select colorent.id from gestionfip.coloresentidades colorent where colorent.identidad = "+ idEntidad;
	
		List<Integer> lista = new ArrayList<Integer>();
	
		try {
			lista = (List<Integer>)em.createNativeQuery(queryPOL).getResultList();
	
			for (Integer idColor : lista)
			{
				ColoresEntidades ce = em.find(ColoresEntidades.class, idColor);
				em.remove(ce);
			}
		} catch (Exception ex) {
			log.warn("[borrarColorDeEntidad] Error al borrar el color asociado a la entidad"
							+ ex.getMessage());
			ex.printStackTrace();
			
	
		}

		

	}

	// ----------------------
	// GEOMETRIAS
	// -----------------------
	
	
	
	
	
	
	
	
	
	public void borrarGeometriaDeEntidad(int idEntidad) {

		Entidad p = em.find(Entidad.class, idEntidad);

		// Debo borrar los EntidadPOL relacionados
		Set<Entidadpol> entpols = p.getEntidadpols();
		if (entpols != null && !entpols.isEmpty())
			for (Entidadpol entidadpol : entpols) {
				em.remove(entidadpol);
			}

		// Debo borrar los EntidadPNT relacionados
		Set<Entidadpnt> entpnts = p.getEntidadpnts();
		if (entpnts != null && !entpnts.isEmpty())
			for (Entidadpnt entidadpnt : entpnts) {
				em.remove(entidadpnt);
			}

		// Debo borrar los EntidadLIN relacionados
		Set<Entidadlin> entlins = p.getEntidadlins();
		if (entlins != null && !entlins.isEmpty())
			for (Entidadlin entidadlin : entlins) {
				em.remove(entidadlin);
			}

	}

	public boolean tieneGeometriaEntidad(int idEntidad) {
		boolean result = false;
		
		if (idEntidad!=0)
		{
			// Poligonos

			String queryPOL = " select g.iden from planeamiento.entidad ent, planeamiento.entidadpol g where g.identidad = ent.iden and ent.iden="
					+ idEntidad;
			
			List<Integer> lista = new ArrayList<Integer>();

			try {
				lista = (List<Integer>)em.createNativeQuery(queryPOL).getResultList();

				if (lista.size() != 0) {
					return true;
				}
			} catch (Exception ex) {
				log
						.warn("[tieneGeometriaEntidad] la busqueda de las geometrias pol de entidades no encuentra resultado "
								+ ex.getMessage());
				ex.printStackTrace();
				

			}

			// Lineas

			String queryLIN = " select g.iden from planeamiento.entidad ent, planeamiento.entidadlin g where g.identidad = ent.iden and ent.iden="
					+ idEntidad;

			try {
				lista = (List<Integer>)em.createNativeQuery(queryLIN).getResultList();

				if (lista.size() != 0) {
					return true;
				}
			} catch (Exception ex) {
				log
						.warn("[tieneGeometriaEntidad]la busqueda de las geometrias lin de entidades pol de entidades no encuentra resultado "
								+ ex.getMessage());
				ex.printStackTrace();
				

			}

			// Puntos

			String queryPNT = " select g.iden from planeamiento.entidad ent, planeamiento.entidadpnt g where g.identidad = ent.iden and ent.iden="
					+ idEntidad;

			try {
				lista = (List<Integer>)em.createNativeQuery(queryPNT).getResultList();

				if (lista.size() != 0) {
					return true;
				}
			} catch (Exception ex) {
				log
						.warn("[tieneGeometriaEntidad] la busqueda de las geometrias PNT de entidades pol de entidades no encuentra resultado  "
								+ ex.getMessage());
				ex.printStackTrace();
				

			}
		}

		

		return result;
	}

	public void borrarEntidadYCURecursivo(int idEntidad) {

		Entidad p = em.find(Entidad.class, idEntidad);

		List<Entidad> lista = obtenerEntidadesPorIdPadre(p.getIden());
		if (lista != null && lista.size() > 0) {
			Iterator<Entidad> it = lista.iterator();
			while (it.hasNext()) {
				borrarEntidadYCURecursivo((it.next()).getIden());
			}
		}

		// Debo borrar los documentos entidad de cada entidad borrada
		Set<Planentidadordenacion> planEntOrd = p.getPlanentidadordenacions();
		if (planEntOrd != null && !planEntOrd.isEmpty()) {
			for (Planentidadordenacion planentidadordenacion : planEntOrd) {
				em.remove(planentidadordenacion);
			}
		}

		Set<Documentoentidad> documentosABorrar = p.getDocumentoentidads();
		if (documentosABorrar != null && !documentosABorrar.isEmpty()) {
			for (Documentoentidad documentoentidad : documentosABorrar) {
				em.remove(documentoentidad);
			}
		}

		// Debo borrar las OperacionEntidad relacionadas
		Set<Operacionentidad> operadas = p.getOperacionentidadsForIdentidad();
		if (operadas != null && !operadas.isEmpty()) {
			for (Operacionentidad operacionentidad : operadas) {
				em.remove(operacionentidad);
			}
		}

		// Debo borrar las OperacionEntidad relacionadas
		Set<Operacionentidad> operadoras = p
				.getOperacionentidadsForIdentidadoperadora();
		if (operadoras != null && !operadoras.isEmpty()) {
			for (Operacionentidad operacionentidad : operadoras) {
				em.remove(operacionentidad);
			}
		}

		// Debo borrar los EntidadPOL relacionados
		Set<Entidadpol> entpols = p.getEntidadpols();
		if (entpols != null && !entpols.isEmpty())
			for (Entidadpol entidadpol : entpols) {
				em.remove(entidadpol);
			}

		// Debo borrar los EntidadPNT relacionados
		Set<Entidadpnt> entpnts = p.getEntidadpnts();
		if (entpnts != null && !entpnts.isEmpty())
			for (Entidadpnt entidadpnt : entpnts) {
				em.remove(entidadpnt);
			}

		// Debo borrar los EntidadLIN relacionados
		Set<Entidadlin> entlins = p.getEntidadlins();
		if (entlins != null && !entlins.isEmpty())
			for (Entidadlin entidadlin : entlins) {
				em.remove(entidadlin);
			}

		// Debo borrar los ConjuntoEntidad relacionados
		Set<Conjuntoentidad> conjconjs = p
				.getConjuntoentidadsForIdentidadconjunto();
		if (conjconjs != null && !conjconjs.isEmpty())
			for (Conjuntoentidad conjuntoentidad : conjconjs) {
				em.remove(conjuntoentidad);
			}

		Set<Conjuntoentidad> conjmies = p
				.getConjuntoentidadsForIdentidadmiembro();
		if (conjmies != null && !conjmies.isEmpty())
			for (Conjuntoentidad conjuntoentidad : conjmies) {
				em.remove(conjuntoentidad);
			}

		// Debo borrar los AmbitoAplicacionAmbito relacionados
		Set<Ambitoaplicacionambito> ambitos = p.getAmbitoaplicacionambitos();
		if (ambitos != null && !ambitos.isEmpty())
			for (Ambitoaplicacionambito ambitoaplicacionambito : ambitos) {
				em.remove(ambitoaplicacionambito);
			}

		// Debo borrar los EntidadDeterminacion (CondicionesUrbanisticas)
		// relacionados
		// El borrado es en cascada porque cada CondicionUrbanistica tiene
		// varias entidades dependientes de ella
		Set<Entidaddeterminacion> condUrbanisticas = p
				.getEntidaddeterminacions();
		if (condUrbanisticas != null && !condUrbanisticas.isEmpty()) {
			for (Entidaddeterminacion entidaddeterminacion2 : condUrbanisticas) {
				// No le paso directamente el objeto, mejor lo recupero en
				// Remote para evitar Lazys
				borraCURecursivo(entidaddeterminacion2.getIden());
			}
		}

		// Ahora si: borro la entidad
		em.remove(p);

	}

	public void borraCURecursivo(int idEntDet) {

		// Hago el find desde aqui y me ahorro todos los problemas de Lazy al
		// procesar los datos sin salir de Remote
		Entidaddeterminacion entidaddeterminacion = em.find(
				Entidaddeterminacion.class, idEntDet);

		// Debo borrar los CasoEntidadDeterminacion de cada entidad borrada
		Set<Casoentidaddeterminacion> casos = entidaddeterminacion
				.getCasoentidaddeterminacions();
		if (casos != null && !casos.isEmpty()) {
			for (Casoentidaddeterminacion caso : casos) {

				// Debo borrar los VinculoCaso de cada CasoEntidadDeterminacion
				// borrada
				Set<Vinculocaso> vinculadores = caso.getVinculocasosForIdcaso();
				if (vinculadores != null && !vinculadores.isEmpty()) {
					for (Vinculocaso vinculocaso : vinculadores) {
						em.remove(vinculocaso);
					}
				}

				// Debo borrar los VinculoCaso de cada CasoEntidadDeterminacion
				// borrada
				Set<Vinculocaso> vinculados = caso
						.getVinculocasosForIdcasovinculado();
				if (vinculados != null && !vinculados.isEmpty()) {
					for (Vinculocaso vinculocaso : vinculados) {
						em.remove(vinculocaso);
					}
				}

				// Debo borrar los EntidadDeterminacionRegimen de cada
				// CasoEntidadDeterminacion borrada
				Set<Entidaddeterminacionregimen> regimenes = caso
						.getEntidaddeterminacionregimensForIdcaso();
				if (regimenes != null && !regimenes.isEmpty()) {
					for (Entidaddeterminacionregimen entidaddeterminacionregimen : regimenes) {

						// Debo borrar RECURSIVAMENTE los RegimenesEspecifico de
						// cada EntidadDeterminacionRegimen borrada
						Set<Regimenespecifico> especificos = entidaddeterminacionregimen
								.getRegimenespecificos();
						if (especificos != null && !especificos.isEmpty()) {
							for (Regimenespecifico regimenespecifico : especificos) {

								borraRegimenEspecificoRecursivo(regimenespecifico);

							}
						}

						em.remove(entidaddeterminacionregimen);
					}
				}

				// Debo borrar los EntidadDeterminacionRegimen de cada
				// CasoEntidadDeterminacion borrada
				Set<Entidaddeterminacionregimen> regimenesAplicacion = caso
						.getEntidaddeterminacionregimensForIdcasoaplicacion();
				if (regimenesAplicacion != null
						&& !regimenesAplicacion.isEmpty()) {
					for (Entidaddeterminacionregimen entidaddeterminacionregimen : regimenesAplicacion) {

						// Debo borrar RECURSIVAMENTE los RegimenesEspecifico de
						// cada EntidadDeterminacionRegimen borrada
						Set<Regimenespecifico> especificos = entidaddeterminacionregimen
								.getRegimenespecificos();
						if (especificos != null && !especificos.isEmpty()) {
							for (Regimenespecifico regimenespecifico : especificos) {

								borraRegimenEspecificoRecursivo(regimenespecifico);

							}
						}

						em.remove(entidaddeterminacionregimen);
					}
				}

				em.remove(caso);
			}
		}

		// ahora si, ha llegado la hora de borrar entidadDeterminacion
		// (condicion urbanistica)
		em.remove(entidaddeterminacion);

	}

	public void borraRegimenEspecificoRecursivo(Regimenespecifico re) {

		List<Regimenespecifico> lista = obtenerRegimenesEspecificosPorIdPadre(re
				.getIden());
		if (lista != null && lista.size() > 0) {
			Iterator<Regimenespecifico> it = lista.iterator();
			while (it.hasNext()) {
				borraRegimenEspecificoRecursivo(it.next());
			}
		}

		em.remove(em.find(Regimenespecifico.class, re.getIden()));
	}

	public List<Regimenespecifico> obtenerRegimenesEspecificosPorIdPadre(
			int idPadre) {

		List<Regimenespecifico> lista = new ArrayList<Regimenespecifico>();
		String queryPlanBase = "SELECT re " + " FROM Regimenespecifico re "
				+ " WHERE re.regimenespecifico.iden=" + idPadre;

		lista = em.createQuery(queryPlanBase).getResultList();

		for (Regimenespecifico re : lista) {

			// Para evitar los Lazy al coger el padre...
			if (re.getRegimenespecifico() != null) {
				re.getRegimenespecifico().getIden();
			}

		}

		return lista;
	}

	public List<Entidad> obtenerEntidadesPorIdPadre(int idPadre) {

		List<Entidad> lista = new ArrayList<Entidad>();
		String cadena = "SELECT entidad " + " FROM Entidad entidad "
				+ " WHERE entidad.entidadByIdpadre.iden=" + idPadre;

		try {
			lista = em.createQuery(cadena).getResultList();
		} catch (NoResultException e) {
			log
					.error("[obtenerEntidadesPorIdPadre] No se han encontrado entidades para ese padre."
							+ e.getMessage());

		} catch (Exception ex) {
			log
					.error("[obtenerEntidadesPorIdPadre] Error en la busqueda de las entidades hijas de un padre: "
							+ ex.getMessage());

		}

		if (lista != null && !lista.isEmpty()) {
			for (Entidad e : lista) {

				// Para evitar los Lazy...
				// Necesito traerme todos los listados para poder borrar en
				// cascada al eliminar una Entidad
				Set<Documentoentidad> doceEnts = e.getDocumentoentidads();
				for (Documentoentidad documentoentidad : doceEnts) {
					documentoentidad.getIden();
				}

				Set<Ambitoaplicacionambito> ambitos = e
						.getAmbitoaplicacionambitos();
				for (Ambitoaplicacionambito ambitoaplicacionambito : ambitos) {
					ambitoaplicacionambito.getIden();
				}

				Set<Entidadlin> lins = e.getEntidadlins();
				for (Entidadlin entidadlin : lins) {
					entidadlin.getIden();
				}

				Set<Entidadpnt> pnts = e.getEntidadpnts();
				for (Entidadpnt entidadpnt : pnts) {
					entidadpnt.getIden();
				}

				Set<Entidadpol> pols = e.getEntidadpols();
				for (Entidadpol entidadpol : pols) {
					entidadpol.getIden();
				}

				Set<Conjuntoentidad> conjConj = e
						.getConjuntoentidadsForIdentidadconjunto();
				for (Conjuntoentidad conjuntoentidad : conjConj) {
					conjuntoentidad.getIden();
				}

				Set<Conjuntoentidad> conjMie = e
						.getConjuntoentidadsForIdentidadmiembro();
				for (Conjuntoentidad conjuntoentidad : conjMie) {
					conjuntoentidad.getIden();
				}

				Set<Entidaddeterminacion> condiciones = e
						.getEntidaddeterminacions();
				for (Entidaddeterminacion entidaddeterminacion : condiciones) {
					entidaddeterminacion.getIden();
					Set<Casoentidaddeterminacion> casos = entidaddeterminacion
							.getCasoentidaddeterminacions();
					for (Casoentidaddeterminacion casoentidaddeterminacion : casos) {
						casoentidaddeterminacion.getIden();
					}
				}

				if (e.getEntidadByIdentidadbase() != null) {
					e.getEntidadByIdentidadbase().getIden();
					e.getEntidadByIdentidadbase().getNombre();
				}

				if (e.getEntidadByIdpadre() != null) {
					e.getEntidadByIdpadre().getIden();
					e.getEntidadByIdpadre().getNombre();
				}

			}
		}

		return lista;
	}

	private String nextCodigo(int idBDTramite) {

		String result = "";

		String query = "SELECT max(codigo) " + " FROM Entidad det"
				+ " WHERE det.tramite.iden=" + idBDTramite;

		try {
			result = (String) em.createQuery(query).getSingleResult();
		} catch (NoResultException e) {
			log
					.error("[nextCodigo] No se ha encontrado el siguiente codigo de Entidad."
							+ e.getMessage());

		} catch (Exception ex) {
			log
					.error("[nextCodigo] Error en la busqueda del siguiente codigo de Entidad: "
							+ ex.getMessage());

		}

		String s;
		if (result != null) {
			// Hay que tratar el valor para añadir los ceros que falten si fuera
			// necesario
			Long l = Long.valueOf(result);
			l++;
			s = l.toString();
			int cerosRestantes = 10 - s.length();

			for (int j = cerosRestantes; j > 0; j--) {
				s = "0" + s;
			}
		} else {
			s = "0000000001";
		}

		return s;

	}

	public List<Entidad> autocomplete(String word, int idTramite) {

		String query = "FROM Entidad d " + "WHERE d.tramite.iden =" + idTramite
				+ " AND lower(d.nombre) like '%" + word.toLowerCase() + "%' "
				+ "ORDER BY d.nombre  ASC";
		try {

			return (List<Entidad>) em.createQuery(query).setMaxResults(1000)
					.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Entidad>();
		}
	}

	public List<EntidadBusquedaDTO> autocompleteFiltros(String word,
			int idTramite, boolean nombre, boolean determinacion, boolean clave) {

		List<Object> res = new ArrayList<Object>();
		List<EntidadBusquedaDTO> resDto = new ArrayList<EntidadBusquedaDTO>();

		if (!determinacion) {
			String query = "Select d.iden, d.nombre, d.clave "
					+ "FROM Entidad d " + "WHERE d.tramite.iden =" + idTramite;

			boolean colocado = false;
			if (nombre) {
				query = query + " AND (lower(d.nombre) like '%"
						+ word.toLowerCase() + "%' ";
				colocado = true;
			}

			if (clave && colocado)
				query = query + " OR lower(d.clave) like '%"
						+ word.toLowerCase() + "%' ";
			else if (clave && !colocado) {
				query = query + " AND (lower(d.clave) like '%"
						+ word.toLowerCase() + "%' ";
				colocado = true;
			}

			if (colocado)
				query = query + ")";

			if (!nombre && !clave) {
				query = query + "AND lower(d.nombre) like '%"
						+ word.toLowerCase() + "%' ";
			}
			query = query + " ORDER BY d.nombre  ASC";

			try {

				res = (List<Object>) em.createQuery(query).setMaxResults(20)
						.getResultList();

				for (Object resObj : res) {
					Object[] regArrayObj = (Object[]) resObj;
					EntidadBusquedaDTO obj = new EntidadBusquedaDTO();

					obj.setIden((Integer) regArrayObj[0]);
					if (((String) regArrayObj[1]).length() > 50)
						obj.setNombre(((String) regArrayObj[1])
								.substring(0, 49)
								+ "...");
					else
						obj.setNombre((String) regArrayObj[1]);
					obj.setNombreDeterminacion("Sin determinación");
					obj.setClave((String) regArrayObj[2]);

					obj.setNombreCompleto((String) regArrayObj[1]);

					resDto.add(obj);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<EntidadBusquedaDTO>();
			}
		}

		// Se busca si viene seleccionada la determinacion determinacion
		if (determinacion) {

			String query = "Select d.entidad.iden, d.entidad.nombre, d.determinacion.nombre, d.entidad.clave "
					+ "FROM Entidaddeterminacion d "
					+ "WHERE d.entidad.tramite.iden =" + idTramite;

			boolean colocado = false;
			if (nombre) {
				query = query + " AND (lower(d.entidad.nombre) like '%"
						+ word.toLowerCase() + "%' ";
				colocado = true;
			}
			if (determinacion && colocado)
				query = query + " OR lower(d.determinacion.nombre) like '%"
						+ word.toLowerCase() + "%' ";
			else if (determinacion && !colocado) {
				query = query + " AND (lower(d.determinacion.nombre) like '%"
						+ word.toLowerCase() + "%' ";
				colocado = true;
			}
			if (clave && colocado)
				query = query + " OR lower(d.entidad.clave) like '%"
						+ word.toLowerCase() + "%' ";
			else if (clave && !colocado) {
				query = query + " AND (lower(d.entidad.clave) like '%"
						+ word.toLowerCase() + "%' ";
				colocado = true;
			}

			if (colocado)
				query = query + ")";

			query = query + " ORDER BY d.entidad.nombre  ASC";

			log.debug(query);
			try {

				res = (List<Object>) em.createQuery(query).setMaxResults(20)
						.getResultList();

				for (Object resObj : res) {
					Object[] regArrayObj = (Object[]) resObj;
					EntidadBusquedaDTO obj = new EntidadBusquedaDTO();

					obj.setIden((Integer) regArrayObj[0]);
					if (((String) regArrayObj[1]).length() > 50)
						obj.setNombre(((String) regArrayObj[1])
								.substring(0, 49)
								+ "...");
					else
						obj.setNombre((String) regArrayObj[1]);

					obj.setNombreDeterminacion((String) regArrayObj[2]);
					obj.setClave((String) regArrayObj[3]);
					obj.setNombreCompleto((String) regArrayObj[1]);

					resDto.add(obj);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<EntidadBusquedaDTO>();
			}
		}

		return resDto;
	}

	@Remove
	public void destroy() {
	}

}
