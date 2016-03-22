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
package es.mitc.redes.urbanismoenred.servicios.seguridad;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Fip1;

/**
 * Session Bean implementation class ServicioFipBean
 */
@Stateless(name = "ServicioFip")
public class ServicioFipBean implements ServicioFipLocal {

	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private GestorFip1Local gestorFip;
	
    /**
     * Default constructor. 
     */
    public ServicioFipBean() {
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioFipLocal#esObsoleto(java.lang.String)
     */
	@Override
	public boolean esObsoleto(String codigoFip) throws ExcepcionSeguridad {
		@SuppressWarnings("unchecked")
		List<Fip1> fips = (List<Fip1>)em.createNamedQuery("Fip1.findLastFip1ByCodigoFip").setParameter("codigoFip", codigoFip).setMaxResults(1).getResultList();
		
		// A veces devuelve una lista que al recuperar el primer elemento éste 
		// es nulo. Se añade esa comprobación aunque es un caso muy extraño.
		if (fips.size() > 0 && fips.get(0) != null) {
			return fips.get(0).getObsoleto() != null? fips.get(0).getObsoleto(): false;
		} else {
			throw new ExcepcionSeguridad("No existe FIP1 dado de alta con código: " + codigoFip);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioFipLocal#getTemplateFromAmbito(java.lang.Integer)
	 */
	@Override
    public File getTemplateFromAmbito(Integer idAmbito, boolean soloXml) throws ExcepcionSeguridad {
        return gestorFip.getPlantilla(idAmbito, "es", soloXml);
    }

}
