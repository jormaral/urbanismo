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

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoWebServiceInterface;
import es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoWebServiceService;

@Stateless
@Name("gestionPrerefundidoNuevo")
public class GestionPrerefundidoNuevoBean implements GestionPrerefundidoNuevo
{
   
	@Logger private Log log;

	
    @In StatusMessages statusMessages;

    @In (create = true, required = false)
    GestionPrerefundidoAsincrono gestionPrerefundidoAsincrono;

	@Override
	public void esRefundible(int idtramite) {
		
		
		log.info("[esRefundible] idtramite="+idtramite);
		
		RefundidoWebServiceService service = new RefundidoWebServiceService();
		
		RefundidoWebServiceInterface ref = service.getRefundidoWebServicePort();
		
		log.info("Es Refundible=" +ref.esRefundible(idtramite));
		
		
		
	}



	@Override
	public void refundirTramite(String listatramites) {
		
		log.info("[refundirTramite]Sincrono listatramites="+listatramites);
		
		
		gestionPrerefundidoAsincrono.refundirTramiteAsincrono(listatramites,-1);
		
		log.info("Refundido Ejecutandose");
		
	}
    
  
  
	

}
