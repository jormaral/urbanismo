/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
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
package es.mitc.redes.urbanismoenred.servicios.dal;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;

/**
 * Session Bean implementation class GestorTramitesBean
 */
@Stateless(mappedName = "GestorTramites")
public class GestorTramitesBean implements GestorTramitesLocal {
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public GestorTramitesBean() {
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.dal.GestorTramitesLocal#getTramite(java.lang.String)
     */
	@Override
	public Tramite getTramite(String codigoFip) {
		try {
    		return (Tramite) em.createNamedQuery("Tramite.findTramiteFromCodFip")
	        	.setParameter("codigoFip", codigoFip)
	        	.getSingleResult();
    	} catch(NoResultException nre) {
    		return null;
    	} catch (NonUniqueResultException nure) {
    		return null;
    	} catch (IllegalStateException ise) {
    		return null;
    	}
	}

}
