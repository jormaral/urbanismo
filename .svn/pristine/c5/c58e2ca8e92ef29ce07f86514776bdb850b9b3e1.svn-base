/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.dal;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless
public class GestorDocumentosBean implements GestorDocumentosLocal {
	@PersistenceContext(unitName="rpmv2")
	private EntityManager em;
	
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.GestorDocumentosLocal#getDocumentos(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Documento[] getDocumentos(String codigoTramite) {
		return (Documento[]) em.createNamedQuery("Documento.buscarPorCodigoTramite")
			.setParameter("codigoTramite", codigoTramite)
			.getResultList().toArray(new Documento[0]);
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.GestorDocumentosLocal#getHojas(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Documentoshp[] getHojas(String codigoTramite) {
		return (Documentoshp[]) em.createNamedQuery("Documentoshp.buscarPorTramite")
			.setParameter("codigoTramite", codigoTramite)
			.getResultList().toArray(new Documentoshp[0]);
	}

}
