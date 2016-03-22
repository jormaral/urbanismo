package es.mitc.redes.urbanismoenred.servicios.dal;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.fip.CaracterDeterminacion;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario;
import es.mitc.redes.urbanismoenred.data.fip.GrupoDocumento;
import es.mitc.redes.urbanismoenred.data.fip.Instrumento;
import es.mitc.redes.urbanismoenred.data.fip.OperacionCaracter;
import es.mitc.redes.urbanismoenred.data.fip.TipoAmbito;
import es.mitc.redes.urbanismoenred.data.fip.TipoDocumento;
import es.mitc.redes.urbanismoenred.data.fip.TipoEntidad;
import es.mitc.redes.urbanismoenred.data.fip.TipoOperacionDeterminacion;
import es.mitc.redes.urbanismoenred.data.fip.TipoTramite;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.AMBITOS;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.CARACTERESDETERMINACION;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.GRUPOSDOCUMENTO;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.INSTRUMENTOS;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.OPERACIONESCARACTERES;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.ORGANIGRAMAAMBITOS;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.TIPOSAMBITO;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.TIPOSDOCUMENTO;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.TIPOSENTIDAD;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.TIPOSOPERACIONDETERMINACION;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.TIPOSOPERACIONENTIDAD;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.TIPOSTRAMITE;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.AMBITOS.AMBITO;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.ORGANIGRAMAAMBITOS.RELACION;
import es.mitc.redes.urbanismoenred.data.fip.Diccionario.TIPOSOPERACIONENTIDAD.TIPOOPERACIONENTIDAD;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambitoshp;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Caracterdeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Grupodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Operacioncaracter;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Organigramaambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipoambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless
public class GeneradorDiccionario implements GeneradorDiccionarioLocal {
	
    private DecimalFormat format6 = new DecimalFormat("000000");
    
    @PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
    
    @EJB
    private ServicioDiccionariosLocal servicioDiccionario;

    /**
     * Default constructor. 
     */
    public GeneradorDiccionario() {
    }
    
    /**
	 * 
	 * @return
	 */
    private AMBITOS generarAmbitos() {
    	AMBITOS ambitos = new AMBITOS();
    	@SuppressWarnings("unchecked")
		List<Ambito> listaAmbitos = em.createNamedQuery("Ambito.obtenerTodos")
    			.getResultList();
    	
    	AMBITO ambitoXML;
    	
    	for (Ambito ambito : listaAmbitos) {
    		ambitoXML = new AMBITO();
    		ambitoXML.setCodigo(format6.format(ambito.getIden()));
    		if (ambito.getCodigoine() != null && !ambito.getCodigoine().isEmpty()) {
    			ambitoXML.setIne(ambito.getCodigoine());
    		}
    		ambitoXML.setNombre(servicioDiccionario.getTraduccion(Ambito.class, ambito.getIden(), "es"));
    		ambitoXML.setTipoAmbito(format6.format(ambito.getTipoambito().getIden()));
    		if (ambito.getAmbitoshp().size() > 0) {
    			ambitoXML.setGEOMETRIA(ambito.getAmbitoshp().toArray(new Ambitoshp[0])[0].getGeom());
    		}
			ambitos.getAMBITO().add(ambitoXML);
    	}
    	
		return ambitos;
	}

	@Override
	public Diccionario crearDiccionario(String idioma) {
		Diccionario diccionario = new Diccionario();
		
		diccionario.setAMBITOS(generarAmbitos());
		diccionario.setCARACTERESDETERMINACION(generarCaracterDeterminacion(idioma));
		diccionario.setGRUPOSDOCUMENTO(generarGruposDocumento(idioma));
		diccionario.setINSTRUMENTOS(generarInstrumentos(idioma));
		diccionario.setOPERACIONESCARACTERES(generarOperacionesCaractgeres(idioma));
		diccionario.setORGANIGRAMAAMBITOS(generarOrganigramaAmbitos(idioma));
		diccionario.setTIPOSAMBITO(generarTiposAmbito(idioma));
		diccionario.setTIPOSDOCUMENTO(generarTiposDocumento(idioma));
		diccionario.setTIPOSENTIDAD(generarTiposEntidad(idioma));
		diccionario.setTIPOSOPERACIONDETERMINACION(generarTiposOperacionDeterminacion(idioma));
		diccionario.setTIPOSOPERACIONENTIDAD(generarTiposOperacionEntidad(idioma));
		diccionario.setTIPOSTRAMITE(generarTiposTramite(idioma));
		
		return diccionario;
	}
	
	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private CARACTERESDETERMINACION generarCaracterDeterminacion(String idioma) {
		CARACTERESDETERMINACION xml = new CARACTERESDETERMINACION();
		List<Caracterdeterminacion> cds = em.createNamedQuery("Caracterdeterminacion.obtenerTodos")
				.getResultList();
		
		CaracterDeterminacion cd;
		for (Caracterdeterminacion caracter : cds) {
			cd = new CaracterDeterminacion();
			cd.setAplicacionesMax(caracter.getNmaxaplicaciones() != null ? BigInteger.valueOf(caracter.getNmaxaplicaciones()): BigInteger.ZERO);
			cd.setAplicacionesMin(caracter.getNminaplicaciones() != null ? BigInteger.valueOf(caracter.getNminaplicaciones()): BigInteger.ZERO);
			cd.setCodigo(format6.format(caracter.getIden()));
			cd.setNombre(servicioDiccionario.getTraduccion(Caracterdeterminacion.class, caracter.getIden(), idioma));
			
			xml.getCARACTERDETERMINACION().add(cd);
		}
		
		return xml;
	}
	
	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private GRUPOSDOCUMENTO generarGruposDocumento(String idioma) {
		GRUPOSDOCUMENTO xml = new GRUPOSDOCUMENTO();
		
		List<Grupodocumento> gds = em.createNamedQuery("Grupodocumento.obtenerTodos")
				.getResultList();
		
		GrupoDocumento gd;
		for (Grupodocumento grupo : gds) {
			gd = new GrupoDocumento();
			
			gd.setCodigo(format6.format(grupo.getIden()));
			gd.setNombre(servicioDiccionario.getTraduccion(Grupodocumento.class, grupo.getIden(), idioma));
			
			xml.getGRUPODOCUMENTO().add(gd);
		}
		return xml;
	}
	
	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private INSTRUMENTOS generarInstrumentos(String idioma) {
		INSTRUMENTOS xml = new INSTRUMENTOS();
		
		List<Instrumentoplan> ips = em.createNamedQuery("Instrumentoplan.obtenerTodos")
				.getResultList();
		
		Instrumento instrumento;
		
		for (Instrumentoplan instrumentoPlan : ips) {

			instrumento = new Instrumento();
			
			instrumento.setCodigo(format6.format(instrumentoPlan.getIden()));
			instrumento.setNombre(servicioDiccionario.getTraduccion(Instrumentoplan.class, instrumentoPlan.getIden(), idioma));
			
			xml.getINSTRUMENTO().add(instrumento);
		}
		
		return xml;
	}
	
	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private OPERACIONESCARACTERES generarOperacionesCaractgeres(String idioma) {
		OPERACIONESCARACTERES xml = new OPERACIONESCARACTERES();
		List<Operacioncaracter> ocs = em.createNamedQuery("Operacioncaracter.obtenerTodos")
				.getResultList();
		
		OperacionCaracter oc;
		
		for (Operacioncaracter operacion : ocs) {
			oc = new OperacionCaracter();
			oc.setCaracterOperada(format6.format(operacion.getCaracterdeterminacionByIdcaracteroperado().getIden()));
			oc.setCaracterOperadora(format6.format(operacion.getCaracterdeterminacionByIdcaracteroperador().getIden()));
			oc.setTipoOperacionDeterminacion(format6.format(operacion.getTipooperaciondeterminacion().getIden()));
			
			xml.getOPERACIONCARACTER().add(oc);
		}
		
		return xml;
	}
	
	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ORGANIGRAMAAMBITOS generarOrganigramaAmbitos(String idioma) {
		ORGANIGRAMAAMBITOS xml = new ORGANIGRAMAAMBITOS();
		
		List<Organigramaambito> oas = em.createNamedQuery("Organigramaambito.obtenerTodos")
				.getResultList();
		
		RELACION relacion;
		for (Organigramaambito organigrama : oas) {
			relacion = new RELACION();
			
			relacion.setHIJO(format6.format(organigrama.getAmbitoByIdambitohijo().getIden()));
			relacion.setPADRE(format6.format(organigrama.getAmbitoByIdambitopadre().getIden()));
			
			xml.getRELACION().add(relacion);
		}
		
		return xml;
	}
	
	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TIPOSAMBITO generarTiposAmbito(String idioma) {
		TIPOSAMBITO xml = new TIPOSAMBITO();
		
		List<Tipoambito> tas = em.createNamedQuery("Tipoambito.obtenerTodos")
				.getResultList();
		
		TipoAmbito ta;
		
		for (Tipoambito tipo : tas) {
			ta = new TipoAmbito();
			
			ta.setCodigo(format6.format(tipo.getIden()));
			ta.setNombre(servicioDiccionario.getTraduccion(Tipoambito.class, tipo.getIden(), idioma));
			
			xml.getTIPOAMBITO().add(ta);
		}
		
		return xml;
	}

	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TIPOSDOCUMENTO generarTiposDocumento(String idioma) {
		TIPOSDOCUMENTO xml = new TIPOSDOCUMENTO();
		
		List<Tipodocumento> tds = em.createNamedQuery("Tipodocumento.obtenerTodos")
				.getResultList();
		
		TipoDocumento td;
		
		for (Tipodocumento tipo : tds) {
			td = new TipoDocumento();
			
			td.setCodigo(format6.format(tipo.getIden()));
			td.setNombre(servicioDiccionario.getTraduccion(Tipodocumento.class, tipo.getIden(), idioma));
			
			xml.getTIPODOCUMENTO().add(td);
		}
		
		return xml;
	}

	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TIPOSENTIDAD generarTiposEntidad(String idioma) {
		TIPOSENTIDAD xml = new TIPOSENTIDAD();
		
		List<Tipoentidad> tes = em.createNamedQuery("Tipoentidad.obtenerTodos")
				.getResultList();
		
		TipoEntidad te;
		
		for (Tipoentidad tipo : tes) {
			te = new TipoEntidad();
			
			te.setCodigo(format6.format(tipo.getIden()));
			te.setNombre(servicioDiccionario.getTraduccion(Tipoentidad.class, tipo.getIden(), idioma));
			
			xml.getTIPOENTIDAD().add(te);
		}
		
		return xml;
	}

	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TIPOSOPERACIONDETERMINACION generarTiposOperacionDeterminacion(
			String idioma) {
		TIPOSOPERACIONDETERMINACION xml = new TIPOSOPERACIONDETERMINACION();
		
		List<Tipooperaciondeterminacion> tods = em.createNamedQuery("Tipooperaciondeterminacion.obtenerTodos")
				.getResultList();
		
		TipoOperacionDeterminacion tod;
		
		for (Tipooperaciondeterminacion tipo : tods) {
			tod = new TipoOperacionDeterminacion();
			
			tod.setCodigo(format6.format(tipo.getIden()));
			tod.setNombre(servicioDiccionario.getTraduccion(Tipooperaciondeterminacion.class, tipo.getIden(), idioma));
			
			xml.getTIPOOPERACIONDETERMINACION().add(tod);
		}
		
		return xml;
	}

	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TIPOSOPERACIONENTIDAD generarTiposOperacionEntidad(String idioma) {
		TIPOSOPERACIONENTIDAD xml = new TIPOSOPERACIONENTIDAD();
		
		List<Tipooperacionentidad> toes = em.createNamedQuery("Tipooperacionentidad.obtenerTodos")
				.getResultList();
		
		TIPOOPERACIONENTIDAD toe;
		
		for (Tipooperacionentidad tipo : toes) {
			toe = new TIPOOPERACIONENTIDAD();
			
			toe.setCodigo(format6.format(tipo.getIden()));
			toe.setNombre(servicioDiccionario.getTraduccion(Tipooperacionentidad.class, tipo.getIden(), idioma));
			toe.setTipoEntidad(format6.format(tipo.getTipoentidad().getIden()));
			
			xml.getTIPOOPERACIONENTIDAD().add(toe);
		}
		
		return xml;
	}

	/**
	 * 
	 * @param idioma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TIPOSTRAMITE generarTiposTramite(String idioma) {
		TIPOSTRAMITE xml = new TIPOSTRAMITE();
		
		List<Tipotramite> tts = em.createNamedQuery("Tipotramite.obtenerTodos")
				.getResultList();
		
		TipoTramite tt;
		
		for (Tipotramite tipo : tts) {
			tt = new TipoTramite();
			
			tt.setCodigo(format6.format(tipo.getIden()));
			tt.setNombre(servicioDiccionario.getTraduccion(Tipotramite.class, tipo.getIden(), idioma));
			
			xml.getTIPOTRAMITE().add(tt);
		}
		
		return xml;
	}

}
