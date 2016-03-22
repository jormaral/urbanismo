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

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Diario;
import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Operaciones;
import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Subsistema;
import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless (name ="ServicioDiario")
public class ServicioDiarioBean implements ServicioDiarioLocal {
	
	 @PersistenceContext(unitName = "rpmv2")
	 private EntityManager em;

	 /*
	  * (non-Javadoc)
	  * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal#getRegistro(int)
	  */
	 @Override
	public Diario getRegistro(int identificador) {
		return em.find(Diario.class, identificador);
	}

	 /*
	  * (non-Javadoc)
	  * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal#getRegistros(java.util.Date, java.util.Date)
	  */
	 @SuppressWarnings("unchecked")
	 @Override
	 public Diario[] getRegistros(Date inicio, Date fin) {
		 return ((List<Diario>)em.createNamedQuery("Diario.buscarPorFecha")
					.setParameter("inicio", inicio,TemporalType.TIMESTAMP)
					.setParameter("fin", fin, TemporalType.TIMESTAMP)
					.getResultList())
						.toArray(new Diario[0]);
	 }

	 /*
	  * (non-Javadoc)
	  * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal#getRegistrosSubsistema(int)
	  */
	 @SuppressWarnings("unchecked")
	 @Override
	 public Diario[] getRegistrosSubsistema(int idUsuario) {
		 return ((List<Diario>)em.createNamedQuery("Diario.buscarPorSubsistema")
					.setParameter("idSubsistema", idUsuario)
					.getResultList())
						.toArray(new Diario[0]);
	 }

	/*
	  * (non-Javadoc)
	  * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal#getRegistrosUsuario(int)
	  */
	 @SuppressWarnings("unchecked")
	 @Override
	 public Diario[] getRegistrosUsuario(int idUsuario) {
		return ((List<Diario>)em.createNamedQuery("Diario.buscarPorUsuario")
				.setParameter("idUsuario", idUsuario)
				.getResultList())
					.toArray(new Diario[0]);
	 }

	/*
	  * (non-Javadoc)
	  * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal#getRegistros(int, int, int)
	  */
	 @SuppressWarnings("unchecked")
	 @Override
	 public Diario[] getRegistrosUsuario(int idUsuario, int elementos, int pagina) {
		return ((List<Diario>)em.createNamedQuery("Diario.buscarPorUsuario")
				.setParameter("idUsuario", idUsuario)
				.setFirstResult(elementos*(pagina-1))
				.setMaxResults(elementos).getResultList())
					.toArray(new Diario[0]);
	 }

	 /*
	  * (non-Javadoc)
	  * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal#getSubsistemas()
	  */
	@SuppressWarnings("unchecked")
	@Override
	public Subsistema[] getSubsistemas() {
		return ((List<Subsistema>)em.createNamedQuery("Subsistema.obtenerTodos").getResultList())
					.toArray(new Subsistema[0]);
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal#nuevoRegistroDiario(java.lang.String, es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema, java.lang.String)
	 */
	@Override
	public void nuevoRegistroDiario(String usuario, TipoSubsistema ss,
			String textoAccion) throws ExcepcionPersistencia {
		nuevoRegistroDiario(usuario, ss, textoAccion, null);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal#nuevoRegistroDiario(java.lang.String, es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema, java.lang.String, java.lang.String)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void nuevoRegistroDiario(String usuario, TipoSubsistema ss,
			String textoAccion, String log)
			throws ExcepcionPersistencia {
		Diario diario = new Diario();
        Operaciones operacion = new Operaciones();
        Date login = new Date();

        operacion.setNombre(textoAccion);
        operacion.setHorainicio(login);

        Usuario usr = null;
        try {
            usr = (Usuario) em.createNamedQuery("Usuario.findByNombre")
            	.setParameter("nombre", usuario)
            		.getSingleResult();
            Subsistema subs = em.find(Subsistema.class, ss.getCodigo());
            if (subs != null) {
            	diario.setUsuario(usr);
                diario.setSubsistema(subs);
                diario.setOperaciones(operacion);
                diario.setFechalogin(login);
                diario.setLog(log);
                em.persist(operacion);
                em.persist(diario);
                
            } else {
            	throw new ExcepcionPersistencia("No se ha encontrado el subsistema especificado.");
            }
        } catch (NoResultException nre) {
            throw new ExcepcionPersistencia("No se ha localizado el usuario indicado. " + usuario,nre);
        } catch (NonUniqueResultException nure) {
        	throw new ExcepcionPersistencia("Error interno: Existe más de un usuario con el mismo nombre. " + usuario,nure);
        } catch (Exception e) {
        	throw new ExcepcionPersistencia("Error al guardar registro. " + e.getMessage(),e);
        }
	}

}
