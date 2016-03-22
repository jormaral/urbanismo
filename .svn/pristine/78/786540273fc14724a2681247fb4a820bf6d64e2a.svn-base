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

package com.mitc.redes.editorfip.servicios.basicos.fip.adscripciones;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Propiedadrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;

@Stateless
@Name("servicioBasicoAdscripciones")
public class ServicioBasicoAdscripcionesBean implements
		ServicioBasicoAdscripciones {
	@Logger
	private Log log;

	@PersistenceContext
	EntityManager em;

	@Override
	public boolean borrarAdscripcion(int idRelacion) {

		boolean resultado = false;

		// Obtengo el orden de la regulacion especifica

		String queryRelacionAdscrp = "SELECT rel " + " FROM Relacion rel "
				+ " WHERE rel.iden=" + idRelacion + " AND rel.iddefrelacion=3 ";

		try {
			Relacion relacionAdscr = (Relacion) em.createQuery(
					queryRelacionAdscrp).getSingleResult();

			// -------------------
			// Borro las propiedades de cada relacion que son la cuantia y el
			// texto de la adscripcion
			// -------------------
			Set<Propiedadrelacion> pdadRelLista = relacionAdscr
					.getPropiedadrelacions();

			for (Iterator it = pdadRelLista.iterator(); it.hasNext();) {
				Propiedadrelacion pdadRel = (Propiedadrelacion) it.next();

				em.remove(pdadRel);

			}

			// -------------------
			// Recupero los vectores de cada relacion que son la entidad origen
			// y destino y las determinaciones unidad y tipo
			// -------------------

			Set<Vectorrelacion> vectorRelLista = relacionAdscr
					.getVectorrelacions();

			for (Iterator it = vectorRelLista.iterator(); it.hasNext();) {
				Vectorrelacion vectorRel = (Vectorrelacion) it.next();

				em.remove(vectorRel);

			}

			em.remove(relacionAdscr);
			
			//Hago un flush para que termine aqui
			em.flush();

			resultado = true;

			log
					.info("[obtenerListaAdscripcionesDeTramite] Adscripcion borrada correctamente");

		} catch (Exception ex) {
			log
					.info("[borrarAdscripcion] Se ha producido un error al borrar la adscripcion de relacion:"
							+ idRelacion + " ERROR:" + ex.getLocalizedMessage());
			ex.printStackTrace();
		}

		return resultado;
	}

	@Override
	public AdscripcionesDTO buscarAdscripcion(int idRelacion) {
		
		log.debug("[buscarAdscripcion] idRelacion="+idRelacion);
		AdscripcionesDTO adscripcionResult = null;

		// Obtengo el orden de la regulacion especifica

		String queryRelacionAdscrp = "SELECT rel " + " FROM Relacion rel "
				+ " WHERE rel.iden=" + idRelacion + " AND rel.iddefrelacion=3 ";

		try {
			Relacion relacionAdscr = (Relacion) em.createQuery(
					queryRelacionAdscrp).getSingleResult();

			adscripcionResult = new AdscripcionesDTO();

			adscripcionResult.setIdRelacion(relacionAdscr.getIden());

			// -------------------
			// Recupero las propiedades de cada relacion que son la cuantia y el
			// texto de la adscripcion
			// -------------------
			Set<Propiedadrelacion> pdadRelLista = relacionAdscr
					.getPropiedadrelacions();

			for (Iterator it = pdadRelLista.iterator(); it.hasNext();) {
				Propiedadrelacion pdadRel = (Propiedadrelacion) it.next();

				// Si tiene cuantia (Iddefpropiedad = 3)
				if (pdadRel.getIddefpropiedad() == 3) {
					adscripcionResult
							.setCuantia(new Double(pdadRel.getValor()));
				}

				// Si tiene texto (Iddefpropiedad = 4)
				if (pdadRel.getIddefpropiedad() == 4) {
					adscripcionResult.setTexto(pdadRel.getValor());
				}

			}

			// -------------------
			// Recupero los vectores de cada relacion que son la entidad origen
			// y destino y las determinaciones unidad y tipo
			// -------------------

			Set<Vectorrelacion> vectorRelLista = relacionAdscr
					.getVectorrelacions();
			
			Iterator it = vectorRelLista.iterator();

			while (it.hasNext()) {
				
				Vectorrelacion vectorRel = (Vectorrelacion) it.next();

				// Si tiene entidad origen (Iddefvector = 4)
				if (vectorRel.getIddefvector() == 4) {
					adscripcionResult.setIdEntidadOrigen(vectorRel.getValor());

					// Tengo que coger tambien el nombre de la entidad, para
					// ello recupero la entidad a partir del id
					Entidad ent = em.find(Entidad.class, vectorRel.getValor());
					adscripcionResult.setNombreEntidadOrigen(ent.getNombre()
							+ " (" + ent.getClave() + ")");
					log.debug("[buscarAdscripcion] entidad origen="+ent.getNombre());

				}

				// Si tiene entidad destino (Iddefvector = 5)
				if (vectorRel.getIddefvector() == 5) {
					adscripcionResult.setIdEntidadDestino(vectorRel.getValor());

					// Tengo que coger tambien el nombre de la entidad, para
					// ello recupero la entidad a partir del id
					Entidad ent = em.find(Entidad.class, vectorRel.getValor());
					adscripcionResult.setNombreEntidadDestino(ent.getNombre()
							+ " (" + ent.getClave() + ")");
					log.debug("[buscarAdscripcion] entidad destino="+ent.getNombre());

				}

				// Si tiene determinacion unidad (Iddefvector = 6)
				if (vectorRel.getIddefvector() == 6) {
					
					log.debug("[buscarAdscripcion] Si tiene determinacion unidad (Iddefvector = 6) vectorRel.getValor()="+vectorRel.getValor());
					if (vectorRel.getValor()!=0)
					{
					adscripcionResult.setIdDeterminacionUnidad(vectorRel
							.getValor());

					// Tengo que coger tambien el nombre de la determinacion,
					// para ello recupero la determinacion a partir del id
					Determinacion det = em.find(Determinacion.class, vectorRel
							.getValor());
					adscripcionResult.setNombreDeterminacionUnidad(det
							.getNombre());

					// Y el nombre del tramite de la determinacion
					adscripcionResult
							.setNombreTramiteDeterminacionUnidad(obtenerNombreTramitePlanDeDeterminacion(vectorRel
									.getValor()));
					}

				}

				// Si tiene determinacion tipo (Iddefvector = 7)
				if (vectorRel.getIddefvector() == 7) {
					
					if (vectorRel.getValor()!=0)
					{
					adscripcionResult.setIdDeterminacionTipo(vectorRel
							.getValor());

					// Tengo que coger tambien el nombre de la determinacion,
					// para ello recupero la determinacion a partir del id
					Determinacion det = em.find(Determinacion.class, vectorRel
							.getValor());
					adscripcionResult.setNombreDeterminacionTipo(det
							.getNombre());

					// Y el nombre del tramite de la determinacion
					adscripcionResult
							.setNombreTramiteDeterminacionTipo(obtenerNombreTramitePlanDeDeterminacion(vectorRel
									.getValor()));
					}

				}

			}

		} catch (Exception ex) {
			log
					.info("[obtenerListaAdscripcionesDeTramite] Se ha producido un error al buscar la adscripcion de relacion:"
							+ idRelacion + " ERROR:" + ex.getLocalizedMessage());
			
			ex.printStackTrace();
		}

		return adscripcionResult;
	}

	@Override
	public int crearAdscripcion(AdscripcionesDTO adscrp, int idTramite) {
		int resultado = 0;

		try {
			Propiedadrelacion propiedadRelacionRPManadir = new Propiedadrelacion();
			Propiedadrelacion propiedadRelacionRPManadir2 = new Propiedadrelacion();

			Relacion relacionRPManadir = new Relacion();
			Vectorrelacion VectorrelacionRPManadir = new Vectorrelacion();

			// Cojo el Tramite
			Tramite tram = em.find(Tramite.class, idTramite);
			// relacion
			relacionRPManadir.setIddefrelacion(3);
			relacionRPManadir.setTramiteByIdtramitecreador(tram);
			em.persist(relacionRPManadir);
			log.debug("--    CREADA Relacion de Adscripcion "
					+ relacionRPManadir.getIden());

			// propiedadrelacion
			propiedadRelacionRPManadir = new Propiedadrelacion();
			propiedadRelacionRPManadir.setRelacion(relacionRPManadir);
			propiedadRelacionRPManadir.setIddefpropiedad(3);
			propiedadRelacionRPManadir.setValor(String.valueOf(adscrp
					.getCuantia()));
			em.persist(propiedadRelacionRPManadir);
			log.debug("--    CREADA Propiedad 1 de Adscripcion "
					+ propiedadRelacionRPManadir.getIden());
			propiedadRelacionRPManadir = null;

			if (adscrp.getTexto() != null) {
				propiedadRelacionRPManadir2 = new Propiedadrelacion();
				propiedadRelacionRPManadir2.setRelacion(relacionRPManadir);
				propiedadRelacionRPManadir2.setIddefpropiedad(4);
				propiedadRelacionRPManadir2.setValor(adscrp.getTexto());
				em.persist(propiedadRelacionRPManadir2);
				log.debug("--    CREADA Propiedad 2 de Adscripcion "
						+ propiedadRelacionRPManadir2.getIden());
				propiedadRelacionRPManadir2 = null;
			}
			// vectorRelacion
			VectorrelacionRPManadir = new Vectorrelacion();
			VectorrelacionRPManadir.setRelacion(relacionRPManadir);
			VectorrelacionRPManadir.setIddefvector(4);
			VectorrelacionRPManadir.setValor(adscrp.getIdEntidadOrigen());
			em.persist(VectorrelacionRPManadir);
			log.debug("--    CREADO Vector 1 de Adscripcion "
					+ VectorrelacionRPManadir.getIden());
			VectorrelacionRPManadir = null;

			VectorrelacionRPManadir = new Vectorrelacion();
			VectorrelacionRPManadir.setRelacion(relacionRPManadir);
			VectorrelacionRPManadir.setIddefvector(5);
			VectorrelacionRPManadir.setValor(adscrp.getIdEntidadDestino());
			em.persist(VectorrelacionRPManadir);
			log.debug("--    CREADO Vector 2 de Adscripcion "
					+ VectorrelacionRPManadir.getIden());
			VectorrelacionRPManadir = null;

			VectorrelacionRPManadir = new Vectorrelacion();
			VectorrelacionRPManadir.setRelacion(relacionRPManadir);
			VectorrelacionRPManadir.setIddefvector(6);
			VectorrelacionRPManadir.setValor(adscrp.getIdDeterminacionUnidad());
			em.persist(VectorrelacionRPManadir);
			log.debug("--    CREADO Vector 3 de Adscripcion "
					+ VectorrelacionRPManadir.getIden());
			VectorrelacionRPManadir = null;

			VectorrelacionRPManadir = new Vectorrelacion();
			VectorrelacionRPManadir.setRelacion(relacionRPManadir);
			VectorrelacionRPManadir.setIddefvector(7);
			VectorrelacionRPManadir.setValor(adscrp.getIdDeterminacionTipo());
			em.persist(VectorrelacionRPManadir);
			log.debug("--    CREADO Vector 4 de Adscripcion "
					+ VectorrelacionRPManadir.getIden());
			VectorrelacionRPManadir = null;
			
			//Hago un flush para que termine aqui
			em.flush();

			resultado = relacionRPManadir.getIden();
			log.info("[crearAdscripcion] Creacion de Adscripcion Correcta");

		} catch (Exception ex) {
			log.error("Ser ha producido un error al crear las adscripciones. "
					+ ex);
			resultado = 0;
			ex.printStackTrace();
		}

		return resultado;
	}

	@Override
	public List<AdscripcionesDTO> obtenerListaAdscripcionesDeTramite(
			int idTramite) {

		log
				.info("[obtenerListaAdscripcionesDeTramite] lista de Adscripciones del tramite:"
						+ idTramite);
		List<AdscripcionesDTO> listaAdscripciones = new ArrayList<AdscripcionesDTO>();

		// Obtengo el orden de la regulacion especifica

		String queryListaRelacionAdscrp = "SELECT rel " + " FROM Relacion rel "
				+ " WHERE rel.tramiteByIdtramitecreador.iden = " + idTramite
				+ " AND rel.iddefrelacion=3 ";

		try {
			List<Relacion> listaAdscrRelacion = (List<Relacion>) em
					.createQuery(queryListaRelacionAdscrp).getResultList();

			// Cada relacion es una adscripcion del tramite. Tengo que ir
			// recuperandola
			for (Relacion rel : listaAdscrRelacion) {
				AdscripcionesDTO adscr = new AdscripcionesDTO();

				adscr.setIdRelacion(rel.getIden());

				log
						.debug("[obtenerListaAdscripcionesDeTramite] Nueva relacion");
				// -------------------
				// Recupero las propiedades de cada relacion que son la cuantia
				// y el texto de la adscripcion
				// -------------------
				Set<Propiedadrelacion> pdadRelLista = rel
						.getPropiedadrelacions();

				for (Iterator it = pdadRelLista.iterator(); it.hasNext();) {
					Propiedadrelacion pdadRel = (Propiedadrelacion) it.next();

					// Si tiene cuantia (Iddefpropiedad = 3)
					if (pdadRel.getIddefpropiedad() == 3) {
						adscr.setCuantia(new Double(pdadRel.getValor()));
					}

					// Si tiene texto (Iddefpropiedad = 4)
					if (pdadRel.getIddefpropiedad() == 4) {
						adscr.setTexto(pdadRel.getValor());
					}

				}
				log
						.debug("[obtenerListaAdscripcionesDeTramite] 		Obtenido Propiedadrelacion:"
								+ pdadRelLista.size());

				// -------------------
				// Recupero los vectores de cada relacion que son la entidad
				// origen y destino y las determinaciones unidad y tipo
				// -------------------

				Set<Vectorrelacion> vectorRelLista = rel.getVectorrelacions();

				for (Iterator it = vectorRelLista.iterator(); it.hasNext();) {
					Vectorrelacion vectorRel = (Vectorrelacion) it.next();

					switch (vectorRel.getIddefvector()) {
					case 4:

						adscr.setIdEntidadOrigen(vectorRel.getValor());

						// Tengo que coger tambien el nombre de la entidad, para
						// ello recupero la entidad a partir del id
						Entidad entO = em.find(Entidad.class, vectorRel
								.getValor());
						adscr.setNombreEntidadOrigen(entO.getNombre() + " ("
								+ entO.getClave() + ")");

						log
								.debug("[obtenerListaAdscripcionesDeTramite] 			(Iddefvector = 4)");
						break;

					case 5:
						adscr.setIdEntidadDestino(vectorRel.getValor());

						// Tengo que coger tambien el nombre de la entidad, para
						// ello recupero la entidad a partir del id
						Entidad entD = em.find(Entidad.class, vectorRel
								.getValor());
						adscr.setNombreEntidadDestino(entD.getNombre() + " ("
								+ entD.getClave() + ")");

						log
								.debug("[obtenerListaAdscripcionesDeTramite] 			(Iddefvector = 5)");
						break;

					case 6:

						adscr.setIdDeterminacionUnidad(vectorRel.getValor());

						// Tengo que coger tambien el nombre de la
						// determinacion, para ello recupero la determinacion a
						// partir del id
						Determinacion detU = em.find(Determinacion.class,
								vectorRel.getValor());
						adscr.setNombreDeterminacionUnidad(detU.getNombre());

						// Y el nombre del tramite de la determinacion
						adscr
								.setNombreTramiteDeterminacionUnidad(obtenerNombreTramitePlanDeDeterminacion(vectorRel
										.getValor()));

						log
								.debug("[obtenerListaAdscripcionesDeTramite] 			(Iddefvector = 6)");
						break;

					case 7:

						// Tengo que coger tambien el nombre de la
						// determinacion, para ello recupero la determinacion a
						// partir del id
						Determinacion detT = em.find(Determinacion.class,
								vectorRel.getValor());
						adscr.setNombreDeterminacionTipo(detT.getNombre());

						// Y el nombre del tramite de la determinacion
						adscr
								.setNombreTramiteDeterminacionTipo(obtenerNombreTramitePlanDeDeterminacion(vectorRel
										.getValor()));

						log
								.debug("[obtenerListaAdscripcionesDeTramite] 			(Iddefvector = 7)");
						break;

					}

				}

				// ---------------------------
				// Hemos completado la adscripcion y la guardo en la lista
				// ---------------------------
				listaAdscripciones.add(adscr);

			}
		} catch (Exception ex) {
			log
					.info("[obtenerListaAdscripcionesDeTramite] Se ha producido un error al buscar las adscripciones para el tramite:"
							+ idTramite + " ERROR:" + ex.getMessage());
			ex.printStackTrace();
		}
		log
				.info("[obtenerListaAdscripcionesDeTramite] Obtenida lista de Adscripciones");
		return listaAdscripciones;
	}

	@Override
	public List<AdscripcionesDTO> obtenerListaAdscripcionesReducidaDeTramite(
			int idTramite) {

		log
				.info("[obtenerListaAdscripcionesDeTramite] lista de Adscripciones del tramite:"
						+ idTramite);
		List<AdscripcionesDTO> listaAdscripciones = new ArrayList<AdscripcionesDTO>();

		// Obtengo el orden de la regulacion especifica

		String queryListaRelacionAdscrp = "SELECT rel " + " FROM Relacion rel "
				+ " WHERE rel.tramiteByIdtramitecreador.iden = " + idTramite
				+ " AND rel.iddefrelacion=3 ";

		try {
			List<Relacion> listaAdscrRelacion = (List<Relacion>) em
					.createQuery(queryListaRelacionAdscrp).getResultList();

			// Cada relacion es una adscripcion del tramite. Tengo que ir
			// recuperandola
			for (Relacion rel : listaAdscrRelacion) {
				AdscripcionesDTO adscr = new AdscripcionesDTO();

				adscr.setIdRelacion(rel.getIden());

				// -------------------
				// Recupero los vectores de cada relacion que son la entidad
				// origen y destino
				// -------------------

				Set<Vectorrelacion> vectorRelLista = rel.getVectorrelacions();

				for (Iterator it = vectorRelLista.iterator(); it.hasNext();) {
					Vectorrelacion vectorRel = (Vectorrelacion) it.next();

					switch (vectorRel.getIddefvector()) {
					case 4:

						adscr.setIdEntidadOrigen(vectorRel.getValor());

						// Tengo que coger tambien el nombre de la entidad, para
						// ello recupero la entidad a partir del id
						Entidad entO = em.find(Entidad.class, vectorRel
								.getValor());
						
						if (entO!=null)
						{
							adscr.setNombreEntidadOrigen(entO.getNombre() + " ("
									+ entO.getClave() + ")");
						}
						

						break;

					case 5:
						adscr.setIdEntidadDestino(vectorRel.getValor());

						// Tengo que coger tambien el nombre de la entidad, para
						// ello recupero la entidad a partir del id
						Entidad entD = em.find(Entidad.class, vectorRel
								.getValor());
						
						if (entD!=null)
						{
							adscr.setNombreEntidadDestino(entD.getNombre() + " ("
									+ entD.getClave() + ")");
						}
						

						break;

					}

				}

				// ---------------------------
				// Hemos completado la adscripcion y la guardo en la lista
				// ---------------------------
				listaAdscripciones.add(adscr);

			}
		} catch (Exception ex) {
			log
					.info("[obtenerListaAdscripcionesDeTramite] Se ha producido un error al buscar las adscripciones para el tramite:"
							+ idTramite + " ERROR:" + ex.getMessage());
			ex.printStackTrace();
		}
		log
				.info("[obtenerListaAdscripcionesDeTramite] Obtenida lista de Adscripciones");
		return listaAdscripciones;

	}

	private String obtenerNombreTramitePlanDeDeterminacion(int idDeterminacion) {
		String nombrePlan = "";

		String queryPlanDet = "SELECT det.tramite "
				+ " FROM Determinacion det " + " WHERE det.iden="
				+ idDeterminacion;

		try {

			Tramite tram = (Tramite) em.createQuery(queryPlanDet)
					.getSingleResult();

			nombrePlan = tram.getPlan().getNombre();

		} catch (Exception ex) {
			log
					.warn("[obtenerNombreTramitePlanDeDeterminacion] No hay tramite para la determinacion:"
							+ idDeterminacion);
			ex.printStackTrace();
		}

		return nombrePlan;
	}

}
