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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundidoconsola;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.Prerefundido;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

import es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoWebServiceInterface;
import es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoWebServiceService;

@Stateless
@Name("gestionPrerefundidoAsincrono")
public class GestionPrerefundidoAsincronoBean implements GestionPrerefundidoAsincrono
{
   
	@Logger private Log log;

	
	
    @In StatusMessages statusMessages;


    @PersistenceContext
	EntityManager em;
	



    @Asynchronous
	public void refundirTramiteAsincrono(String listatramites, int idRegistroPreref) {
		
		log.info("[refundirTramiteAsincrono] listatramites="+listatramites+" idRegistroPreref="+idRegistroPreref);
		
		
		RefundidoWebServiceService service = new RefundidoWebServiceService();
		
		RefundidoWebServiceInterface ref = service.getRefundidoWebServicePort();
		
		try
		{
			log.info("Llamar a WS del Refundido JBoss7");
			ref.refundirTramites(listatramites);
			
			
			/*
			String queryPreref = "from Prerefundido pre where pre.id = "+idRegistroPreref;
			Prerefundido pref = (Prerefundido)em.createQuery(queryPreref).getSingleResult();
			
			// Obtengo el idtramiteencargado suponiendo que es el ultimo 
			String queryUltimoTram = "from Tramite tr where tr.idtipotramite = 11 order by iden desc";
			Tramite tram = (Tramite) em.createQuery(queryUltimoTram).getResultList().get(0);
			
			pref.setIdTramiteEncargado(tram.getIden());
			pref.setConsolidado(true);
			
			em.merge(pref);
			*/
			
			log.info("Refundido Terminado");
		}
		catch (Exception e)
		{
			log.warn("No se devolvio la respues del WS de refundir Tramites en el tiempo esperado, pero se ha lanzado.");
		}
		
		
		
		
	}
    
  
  
	

}
