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
package es.mitc.redes.urbanismoenred.servicios.consolidacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.validacion.Proceso;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.Estado;
import es.mitc.redes.urbanismoenred.servicios.validacion.GestorContextosValidacionLocal;

/**
 * Implementación del servicio de consolidación.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless (name = "ServicioConsolidacion")
public class ServicioConsolidacionBean implements ServicioConsolidacionLocal {
	
	private static final Logger log = Logger.getLogger(ServicioConsolidacionBean.class);
	
	@PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
	
	@EJB
	private ServicioDiarioLocal diario;
	
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;
	
	@EJB
	private GestorContextosValidacionLocal gestorContextosValidacion;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.consolidacion.ServicioConsolidacionLocal#consolidar(es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario, int)
	 */
	@Override
	public void consolidar(Usuario usuario, int proceso)
			throws ExcepcionConsolidacion {
		
		try {
			diario.nuevoRegistroDiario(usuario.getNombre(), 
					TipoSubsistema.CONSOLIDACION, 
					"Inicio del la consolidacion del trámite " + proceso);
			
			Proceso proc = em.find(Proceso.class, proceso);
			if (proc != null) {
				Tramite tramite =  (Tramite) em.createNamedQuery("Tramite.findTramiteFromCodFip")
					.setParameter("codigoFip", proc.getIdfip())
					.getSingleResult();
				
				definirGeometria(tramite);
				
				Date ahora = GregorianCalendar.getInstance().getTime();
				tramite.setFechaconsolidacion(ahora);
				proc.setConsolidado(ahora);
				
				ContextoValidacion contexto = gestorContextosValidacion.getContexto(proc.getIdfip());
				
				if (contexto != null) {
					contexto.setEstado(Estado.CONSOLIDADO);
				}
			} else {
				throw new ExcepcionConsolidacion("No se ha encontrado proceso con identificador " + proceso);
			}
		} catch (ExcepcionPersistencia e) {
			throw new ExcepcionConsolidacion("Error al ejecutar la consolidación: " + e.getMessage(),e);
		} catch (NoResultException nre) {
            throw new ExcepcionConsolidacion("No se ha localizado el trámite asociado al proceso. " + proceso,nre);
        } catch (NonUniqueResultException nure) {
        	throw new ExcepcionConsolidacion("Error interno: Existe más de un trámite con el mismo código. ",nure);
        }
	}

	/**
	 * La tabla planshp contiene la geometría que define el contorno geomgráfico
	 * del plan.
	 * Se actualiza con los datos de las entidades definidas en el último
	 * trámite.
	 * 
	 * @param tramite
	 */
	private void definirGeometria(Tramite tramite) {
		int resultado = 0;
		if (tramite.getPlan().getPlanshps().size() > 0) {
			resultado = em.createNamedQuery("Planshp.actualizarContorno")
				.setParameter("idPlan", tramite.getPlan().getIden())
				.setParameter("idTramite", tramite.getIden())
				.executeUpdate();
		} else {
			resultado = em.createNamedQuery("Planshp.crearContorno")
				.setParameter("idPlan", tramite.getPlan().getIden())
				.setParameter("idTramite", tramite.getIden())
				.executeUpdate();
		}
		
		if (resultado <= 0) {
			log.warn("No se ha podido definir la geometría del plan");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.consolidacion.ServicioConsolidacionLocal#getProcesosConsolidables(es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario)
	 */
	@Override
	public ProcesoConsolidable[] getProcesosConsolidables(Usuario usuario) {
		@SuppressWarnings("unchecked")
		List<Proceso> procesosConsolidables = (List<Proceso>)em.createNamedQuery("Proceso.obtenerNoConsolidados")
				.getResultList();
		Tramite tramite;
		
		List<ProcesoConsolidable> resultado = new ArrayList<ProcesoConsolidable>();
		ProcesoConsolidable pc;
		Instrumentoplan instrumento;
		for (Proceso proceso : procesosConsolidables) {
			try {
				tramite = (Tramite) em.createNamedQuery("Tramite.findTramiteFromCodFip")
					.setParameter("codigoFip", proceso.getIdfip())
					.getSingleResult();
				if (usuario.puedeConsolidar(tramite.getPlan().getIdambito())) {
					instrumento = servicioDiccionario.getInstrumento(tramite.getPlan().getIden());
					pc = new ProcesoConsolidable(proceso.getIden(), 
							tramite.getCodigofip(), 
							tramite.getIdtipotramite(),
							tramite.getFecha(), 
							tramite.getIteracion(),
							tramite.getPlan().getIdambito(),
							instrumento != null? instrumento.getIden():null,
							tramite.getPlan().getNombre(),
							tramite.getNombre());
					resultado.add(pc);
				}
			} catch (NoResultException nre) {
				log.warn("No se ha encontrado trámite " + proceso.getIdfip());
			} catch (NonUniqueResultException nure) {
				log.warn("Se ha encontrado más de un proceso con código " + proceso.getIdfip());
			}	
		}
		return resultado.toArray(new ProcesoConsolidable[0]);
	}

}
