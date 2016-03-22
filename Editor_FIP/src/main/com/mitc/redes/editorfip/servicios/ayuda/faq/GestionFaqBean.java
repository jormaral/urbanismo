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

package com.mitc.redes.editorfip.servicios.ayuda.faq;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.ayuda.Faq;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionFaq")
public class GestionFaqBean implements GestionFaq {
	@Logger
	private Log log;

	@In
	StatusMessages statusMessages;
	
	@In (create = true, required = false)
	FacesMessages facesMessages;

	@PersistenceContext
	EntityManager em;
	
	private boolean mostrarPanelNuevaFaq = false;
	
	private Faq nuevaFaq = new Faq();
	
	private ArrayList<Faq> resultado;

	public ArrayList<Faq> getResultado() {
		log.debug("[obtenerListadoFaq] Se van a obtener las faqs");

		resultado = new ArrayList<Faq>();

		String queryFaqOrden = "SELECT f " 
				+ " FROM Faq f "
				+ " ORDER BY f.orden ASC";

		try {
			resultado = (ArrayList<Faq>) em.createQuery(queryFaqOrden).getResultList();

		} catch (NoResultException e) {
			log.error("[obtenerListadoFaq] No se han encontrado faqs:"+ e.getMessage());
			e.printStackTrace();

		} catch (Exception ex) {
			log.error("[obtenerListadoFaq] Error en la busqueda de faqs:"+ ex.getMessage());
			ex.printStackTrace();

		}

		log.debug("[obtenerListadoFaq] Numero de Faqs obtenidas: "+ resultado.size());
		return resultado;
	}

	public void setResultado(ArrayList<Faq> resultado) {
		this.resultado = resultado;
	}
	
	public void guardarFaq() {
		
		int orden = 0;
		
		try {			

			Long numReg = (Long) em.createQuery("SELECT COUNT(*) FROM Faq").getSingleResult();
			
			if(numReg > 0){
				String queryFaqOrden = "SELECT MAX(f.orden) " 
					+ " FROM Faq f ";
				
				orden = (Integer)em.createQuery(queryFaqOrden).getSingleResult();
				
			}		
			
			orden = orden + 1;
			
			nuevaFaq.setOrden(orden);
			em.persist(nuevaFaq);
			mostrarPanelNuevaFaq = false;
			facesMessages.addFromResourceBundle(Severity.INFO, "faq_crear_ok", null);
			nuevaFaq = new Faq();

		} catch (NoResultException e) {
			log.error("[guardarFaq] Error al obtener el orden:"+ e.getMessage());
			facesMessages.addFromResourceBundle(Severity.ERROR, "Error al guardar la FAQ", null);
			e.printStackTrace();
		}		
		
	}
	
	public void eliminarFaq(Long idFaq) {
		Faq faq = em.find(Faq.class, idFaq);
		em.remove(faq);
		facesMessages.addFromResourceBundle(Severity.INFO, "faq_del_ok", null);
	}

	

	@Override
	public List<Faq> obtenerListadoFaq() {
		log.debug("[obtenerListadoFaq] Se van a obtener las faqs");

		resultado = new ArrayList<Faq>();

		String queryFaqOrden = "SELECT f " 
				+ " FROM Faq f "
				+ " ORDER BY f.orden ASC";

		try {
			resultado = (ArrayList<Faq>) em.createQuery(queryFaqOrden).getResultList();

		} catch (NoResultException e) {
			log.error("[obtenerListadoFaq] No se han encontrado faqs:"+ e.getMessage());
			e.printStackTrace();

		} catch (Exception ex) {
			log.error("[obtenerListadoFaq] Error en la busqueda de faqs:"+ ex.getMessage());
			ex.printStackTrace();

		}

		log.debug("[obtenerListadoFaq] Numero de Faqs obtenidas: "+ resultado.size());
		return resultado;
	}

	
	

	public void setMostrarPanelNuevaFaq(boolean mostrarPanelNuevaFaq) {
		this.mostrarPanelNuevaFaq = mostrarPanelNuevaFaq;
	}

	public boolean isMostrarPanelNuevaFaq() {
		return mostrarPanelNuevaFaq;
	}

	public Faq getNuevaFaq() {
		return nuevaFaq;
	}

	public void setNuevaFaq(Faq nuevaFaq) {
		this.nuevaFaq = nuevaFaq;
	}

	@Remove
	public void destroy() {
	}

}
