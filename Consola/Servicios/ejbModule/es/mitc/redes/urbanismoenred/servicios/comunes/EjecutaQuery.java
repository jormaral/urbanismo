package es.mitc.redes.urbanismoenred.servicios.comunes;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
/**
 *
 * @author Arnaiz Consultores
 */
@Stateless
public class EjecutaQuery implements EjecutaQueryLocal {

    private Logger log = Logger.getLogger(EjecutaQuery.class);

    @PersistenceContext(unitName = "rpmv2")
    private EntityManager em;

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.comunes.EjecutaQueryLocal#select(java.lang.String, java.util.Map)
     */
    @Override
	public List<?> select(String jpql, Map<String, Object> parametros) {
		Query query = em.createQuery(jpql);
		for (String clave : parametros.keySet()) {
			query.setParameter(clave, parametros.get(clave));
		}
		return query.getResultList();
	}

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.comunes.EjecutaQueryLocal#selectSQL(java.lang.String)
     */
    @Override
	public List<?> selectSQL(String SQL) {
		return em.createNativeQuery(SQL).getResultList();
	}

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.comunes.EjecutaQueryRemote#updateHQL(java.lang.String)
     */
	public boolean updateHQL (String HQL)
    {
        try{
            if (em.createQuery(HQL).executeUpdate()==0){
                log.warn(HQL + " no actualizó nada");
            }

            return true;
        }catch (Exception ex)
        {
            log.error("Error lanzando HQL " + HQL,ex);
            return false;
        }
    }

	public boolean updateSQL (String SQL){
        try{
            if (em.createNativeQuery(SQL).executeUpdate()==0){
                log.warn(SQL + " no actualizó nada");
            }

            return true;
        }catch (Exception ex)
        {
            log.error("Error lanzando SQL " + SQL,ex);
            return false;
        }

    }
}
