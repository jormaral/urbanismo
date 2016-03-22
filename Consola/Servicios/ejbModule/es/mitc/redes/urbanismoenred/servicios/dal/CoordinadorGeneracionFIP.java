package es.mitc.redes.urbanismoenred.servicios.dal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Singleton;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Singleton
public class CoordinadorGeneracionFIP implements CoordinadorGeneracionFIPLocal {
	
	private ConcurrentHashMap<Integer, Map<String, Object>> generacionesActivas = new ConcurrentHashMap<>();

    /**
     * Default constructor. 
     */
    public CoordinadorGeneracionFIP() {
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.dal.CoordinadorGeneracionFIPLocal#iniciarProceso(int)
     */
	@Override
	public Map<String, Object> iniciarProceso(int idtramite)
			throws ProcesoYaIniciado {
		if (!generacionesActivas.contains(idtramite)) {
			generacionesActivas.put(idtramite, new ConcurrentHashMap<String, Object>());
		} else {
			throw new ProcesoYaIniciado("Trámite " + idtramite);
		}
		return getProceso(idtramite);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.CoordinadorGeneracionFIPLocal#getProceso(int)
	 */
	@Override
	public Map<String, Object> getProceso(int idtramite) {
		return generacionesActivas.get(idtramite);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.CoordinadorGeneracionFIPLocal#finalizarProceso(int)
	 */
	@Override
	public void finalizarProceso(int idtramite) {
		if (generacionesActivas.contains(idtramite)) {
			generacionesActivas.remove(idtramite);
		}
	}

}
