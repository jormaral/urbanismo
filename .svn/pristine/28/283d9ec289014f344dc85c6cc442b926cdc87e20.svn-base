/*
 * Copyright 2011 red.es
 * Autores: Arnaiz Consultores.
 *
 * Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
 ** sean aprobadas por la Comision Europea- versiones
 ** posteriores de la EUPL (la <<Licencia>>);
 ** Solo podra usarse esta obra si se respeta la Licencia.
 * Puede obtenerse una copia de la Licencia en:
 *
 * http://ec.europa.eu/idabc/eupl5
 *
 ** Salvo cuando lo exija la legislacion aplicable o se acuerde.
 * por escrito, el programa distribuido con arreglo a la
 ** Licencia se distribuye <<TAL CUAL>>,
 ** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
 ** ni implicitas.
 ** Vease la Licencia en el idioma concreto que rige
 * los permisos y limitaciones que establece la Licencia.
 */
package es.mitc.redes.urbanismoenred.servicios.dal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.fip.Adscripcion;
import es.mitc.redes.urbanismoenred.data.fip.AplicacionAmbito;
import es.mitc.redes.urbanismoenred.data.fip.Caso;
import es.mitc.redes.urbanismoenred.data.fip.CodigoCaso;
import es.mitc.redes.urbanismoenred.data.fip.CodigoDeterminacion;
import es.mitc.redes.urbanismoenred.data.fip.CondicionUrbanistica;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion;
import es.mitc.redes.urbanismoenred.data.fip.Documento;
import es.mitc.redes.urbanismoenred.data.fip.Entidad;
import es.mitc.redes.urbanismoenred.data.fip.Entidad.DOCUMENTOS.DOCUMENTO;
import es.mitc.redes.urbanismoenred.data.fip.FIP;
import es.mitc.redes.urbanismoenred.data.fip.Hoja;
import es.mitc.redes.urbanismoenred.data.fip.OperacionDeterminacion;
import es.mitc.redes.urbanismoenred.data.fip.OperacionEntidad;
import es.mitc.redes.urbanismoenred.data.fip.PropiedadesAdscripcion;
import es.mitc.redes.urbanismoenred.data.fip.Regimen;
import es.mitc.redes.urbanismoenred.data.fip.RegimenEspecifico;
import es.mitc.redes.urbanismoenred.data.fip.RegulacionEspecifica;
import es.mitc.redes.urbanismoenred.data.fip.Tramite;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.ADSCRIPCIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.APLICACIONAMBITOS;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.CONDICIONESURBANISTICAS;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.DETERMINACIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.DOCUMENTOS;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.DOCUMENTOS.DOCUMENTOREFUNDIDO;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.ENTIDADES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.OPERACIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.OPERACIONES.OPERACIONESDETERMINACIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.OPERACIONES.OPERACIONESENTIDADES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.UNIDADES;
import es.mitc.redes.urbanismoenred.data.fip.Unidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Ambitoaplicacionambito;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadlin;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpnt;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpol;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Propiedadrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso;

/**
 *
 * Clase encargada de guardar un FIP en la base de datos RPM.
 *
 * @author Arnaiz Consultores
 *
 */
@Stateless
public class GestorFipBean implements GestorFipLocal, EliminadorListener {

    private static final Logger log = Logger.getLogger(GestorFipBean.class);
    // Valores obtenidos de la tabla defpropiedad del esquema diccionario
    private static final int ID_DEF_PROPIEDAD_ABREVIATURA = 1;
    private static final int ID_DEF_PROPIEDAD_DEFINICION = 2;
    private static final int ID_DEF_PROPIEDAD_CUANTIA_ADSCRIPCION = 3;
    private static final int ID_DEF_PROPIEDAD_TEXTO_ADSCRIPCION = 4;
    private static final int ID_DEF_PROPIEDAD_ORDEN_PRESENTACION = 5;
    private static final int ID_DEF_PROPIEDAD_NOMBRE = 6;
    private static final int ID_DEF_PROPIEDAD_TEXTO = 7;
    // Valores obtenidos de la tabla defrelacion del esquema diccionario
    private static final int ID_DEF_RELACION_DET_CARACTER_UNIDAD = 1;
    private static final int ID_DEF_RELACION_VINCULO_UNIDAD = 2;
    private static final int ID_DEF_RELACION_ADSCRIPCION_ENTIDADES = 3;
    private static final int ID_DEF_RELACION_TEXTO_REGULACION_DETERMINACION = 20;
    // Valores obtenidos de la tabla defvector del esquema diccionario
    private static final int ID_DEF_VECTOR_DET_UNIDAD_APLICA_PROPIEDADES = 1;
    private static final int ID_DEF_VECTOR_DET_ASIGNA_UNIDAD = 2;
    private static final int ID_DEF_VECTOR_DET_CARACTER_UNIDAD_ASIGNA = 3;
    private static final int ID_DEF_VECTOR_ENTIDAD_ORIGEN_ADSCRIPCION = 4;
    private static final int ID_DEF_VECTOR_ENTIDAD_DESTINO_ADSCRIPCION = 5;
    private static final int ID_DEF_VECTOR_DET_UNIDAD_ADSCRIPCION = 6;
    private static final int ID_DEF_VECTOR_DET_TIPO_ADSCRIPCION = 7;
    private static final int ID_DEF_VECTOR_TEXTO_REGULACION_DETERMINACION = 40;
    private static final int ID_DEF_VECTOR_HERENCIA_CLAVE = 41;
    private static final int TIPO_OPERACION_ADICION_VALORREFERENCIA = 8;
    private static final int TIPO_OPERACION_CREACION_ADSCRIPCION = 23;
    private static final int TIPO_OPERACION_ELIMINACION_ADSCRIPCION = 24;
    private static Properties caracterAvector = new Properties();
    private static Properties caracterAdefRelacion = new Properties();

    static {
        try {
            caracterAvector.load(GestorFipBean.class.getResourceAsStream("/es/mitc/redes/urbanismoenred/utils/recursos/textos/vectores.properties"));
            caracterAdefRelacion.load(GestorFipBean.class.getResourceAsStream("/es/mitc/redes/urbanismoenred/utils/recursos/textos/caracter_defrelacion.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
    @EJB
    private EliminadorEntidadesLocal eliminadorEntidades;
    // Variable empleada para guardar asociaciones entre los códigos de documento en FIP
    // y los documentos de base de datos.
    private Map<String, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento> docs = new HashMap<String, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento>();
    private Map<String, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion> detsRpm = new HashMap<String, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion>();
    private Map<String, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad> entsRpm = new HashMap<String, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad>();
    private List<GestorFipListener> listeners = new ArrayList<GestorFipListener>();

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.servicios.dal.EliminadorListener#actualizacionProgreso(int,
     * int, int, int)
     */
    @Override
    public void actualizacionProgreso(int paso, int total,
            int elementosBorrados, int totalElementos) {
        notificar(paso, total, elementosBorrados, totalElementos);
    }

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.servicios.dal.GestorFipLocal#addListener(es.mitc.redes.urbanismoenred.servicios.dal.GestorFipListener)
     */
    @Override
    public void addListener(GestorFipListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.servicios.dal.GestorFipLocal#borrarFip(int)
     */
    @Override
    public void borrarFip(int idTramite) throws ExcepcionPersistencia {
        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite.class, idTramite);
        if (tramite != null) {
            try {
                eliminadorEntidades.eliminar(tramite, false, this);
            } catch (Exception e) {
                throw new ExcepcionPersistencia("Error al borrar los datos originales. Causa: " + e.getMessage(), e);
            }
        } else {
            throw new ExcepcionPersistencia("No se ha encontrado trámite con id " + idTramite);
        }
    }

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.servicios.dal.GestorFipLocal#borrarFip(java.lang.String)
     */
    @Override
    public void borrarFip(String codigoTramite) throws ExcepcionPersistencia {
        try {
            log.info("Borrando datos del tramite " + codigoTramite);

            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite = (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite) em.createNamedQuery("Tramite.findTramiteFromCodFip").setParameter("codigoFip", codigoTramite).getSingleResult();

            eliminadorEntidades.eliminar(tramite, false, this);
        } catch (NonUniqueResultException nure) {
            throw new ExcepcionPersistencia("Se ha encontrado más de un trámite con código " + codigoTramite);
        } catch (NoResultException nre) {
            throw new ExcepcionPersistencia("No se ha encontrado trámite con código " + codigoTramite);
        }
    }

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.servicios.dal.GestorFipLocal#getTramitesPendientes()
     */
    @SuppressWarnings("unchecked")
    @Override
    public es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite[] getTramitesPendientes()
            throws ExcepcionPersistencia {
        List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite> tramites = (List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite>) em.createNamedQuery("Tramite.findPendientes").getResultList();

        return tramites.toArray(new es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite[0]);
    }

    /**
     *
     * @param adscripciones
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private void guardarAdscripciones(ADSCRIPCIONES adscripciones, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite) throws ExcepcionPersistencia {
        int a = 0;
        int size = adscripciones.getADSCRIPCION().size();
        for (Adscripcion adscripcion : adscripciones.getADSCRIPCION()) {
            a++;
            log.info("Guardando adscripción " + a + " de " + size);
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidadOrigen = obtenerEntidad(tramite.getCodigofip(), adscripcion.getEntidadOrigen());
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidadDestino = obtenerEntidad(tramite.getCodigofip(), adscripcion.getEntidadDestino());

            guardarPropiedadesAdscripcion(adscripcion.getPROPIEDADES(), entidadOrigen, entidadDestino, tramite);

            notificar(9, GestorFipListener.PASOS_GUARDADO, a, size);
        }

    }

    /**
     *
     * @param aplicacionambitos
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private void guardarAmbitosAplicacion(APLICACIONAMBITOS aplicacionambitos, Tramite tramite) throws ExcepcionPersistencia {
        int a = 0;
        int size = aplicacionambitos.getAPLICACIONAMBITO().size();
        for (AplicacionAmbito ambito : aplicacionambitos.getAPLICACIONAMBITO()) {
            a++;
            log.info("Guardando ámbito aplicación " + a + " de " + size);
            Ambitoaplicacionambito ambitoAplicacion = new Ambitoaplicacionambito();
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidad = obtenerEntidad(tramite.getCodigo(), ambito.getEntidad());
            ambitoAplicacion.setEntidad(entidad);
            ambitoAplicacion.setIdambito(Integer.valueOf(ambito.getAmbito()));

            em.persist(ambitoAplicacion);
            notificar(8, GestorFipListener.PASOS_GUARDADO, a, size);
        }
    }

    /**
     *
     * @param casos
     * @param ed
     * @param tramite
     * @param detCU
     * @param entidades
     * @throws ExcepcionPersistencia
     */
    private void guardarCasos(List<Caso> casos,
            Entidaddeterminacion ed,
            Tramite tramite,
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detCU,
            Map<String, Casoentidaddeterminacion> casosEntidadDeterminacion,
            Map<Entidaddeterminacionregimen, String> casosAplicacion,
            List<Object> entidades) throws ExcepcionPersistencia {

        int c = 0;
        int size = casos.size();

        for (Caso caso : casos) {
            c++;
            log.debug("Procesando caso " + c + " de " + size);
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detRegimen = null;
            Casoentidaddeterminacion ced = new Casoentidaddeterminacion();
            ced.setNombre(caso.getNombre());
            ced.setEntidaddeterminacion(ed);

            entidades.add(ced);

            casosEntidadDeterminacion.put(caso.getCodigo(), ced);

            if (caso.getREGIMENES() != null) {
                for (Regimen regimen : caso.getREGIMENES().getREGIMEN()) {
                    Entidaddeterminacionregimen edr = new Entidaddeterminacionregimen();

                    if (regimen.getSuperposicion() != null) {
                        edr.setSuperposicion(regimen.getSuperposicion().intValue());
                    }

                    edr.setValor(regimen.getVALOR());

                    if (regimen.getDETERMINACIONREGIMEN() != null) {
                        detRegimen = obtenerDeterminacion(regimen.getDETERMINACIONREGIMEN().getTramite(), regimen.getDETERMINACIONREGIMEN().getDeterminacion());

                        edr.setDeterminacion(detRegimen);
                    } else {
                        detRegimen = detCU;
                    }

                    // Si no define DETERMINACION-REGIMEN las referencias se asocian
                    // a la determinación de la condición urbanísitica
                    if (regimen.getVALORREFERENCIA() != null) {
                        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detReferencia = obtenerDeterminacion(regimen.getVALORREFERENCIA().getTramite(),
                                regimen.getVALORREFERENCIA().getDeterminacion());
                        try {
                            Opciondeterminacion od = (Opciondeterminacion) em.createNamedQuery("Opciondeterminacion.buscarPorDetYValorRef").setParameter("idDeterminacion", detRegimen.getIden()).setParameter("idValorRef", detReferencia.getIden()).getSingleResult();

                            edr.setOpciondeterminacion(od);
                        } catch (NoResultException nre) {
                            liberarCache();
                            throw new ExcepcionPersistencia("No se ha encontrado opcionDeterminacion para determinacion "
                                    + detRegimen.getIden() + " y valor referencia " + detReferencia.getIden());
                        } catch (NonUniqueResultException nure) {
                            liberarCache();
                            throw new ExcepcionPersistencia("Se han encontrado múltiples opcionDeterminacion para determinacion "
                                    + detRegimen.getIden() + " y valor referencia " + detReferencia.getIden());
                        }
                    }

                    if (regimen.getCASOAPLICACION() != null) {
                        casosAplicacion.put(edr, regimen.getCASOAPLICACION().getCaso());
                    }

                    edr.setCasoentidaddeterminacionByIdcaso(ced);

                    entidades.add(edr);

                    if (regimen.getREGIMENESESPECIFICOS() != null) {
                        for (RegimenEspecifico regimenEspecifico : regimen.getREGIMENESESPECIFICOS().getREGIMENESPECIFICO()) {
                            guardarRegimenEspecifico(regimenEspecifico, edr, null, entidades);
                        }
                    }
                }
            }

            if (caso.getDOCUMENTOS() != null) {
                for (es.mitc.redes.urbanismoenred.data.fip.Caso.DOCUMENTOS.DOCUMENTO documento : caso.getDOCUMENTOS().getDOCUMENTO()) {
                    Documentocaso dc = new Documentocaso();
                    dc.setDocumento(docs.get(documento.getCodigo()));
                    dc.setCasoentidaddeterminacion(ced);

                    entidades.add(dc);
                }
            }
        }
    }

    /**
     *
     * @param condicionesurbanisticas
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private void guardarCondicionesUrbanisticas(
            CONDICIONESURBANISTICAS condicionesurbanisticas,
            Tramite tramite) throws ExcepcionPersistencia {
        log.debug("Guardando condiciones urbanísticas.");
        int c = 0;
        int size = condicionesurbanisticas.getCONDICIONURBANISTICA().size();

        List<Object> entidades = new ArrayList<Object>();

        // primero proceso los elementos y los convierto en objetos de entidad.
        // Una vez hecho esto los persisto en base de datos.
        // Esta estrategia resulta mucho mejor que ir guardando los objetos a
        // medida que se van creando.

        Map<String, Casoentidaddeterminacion> casosEntidadDeterminacion = new HashMap<String, Casoentidaddeterminacion>();
        Map<Entidaddeterminacionregimen, String> casosAplicacion = new HashMap<Entidaddeterminacionregimen, String>();

        for (CondicionUrbanistica cu : condicionesurbanisticas.getCONDICIONURBANISTICA()) {
            c++;
            log.info("Procesando condición urbanística " + c + " de " + size);
            // Regla de guardado: El FIP no contiene ninguna condición urbanística que apunte a una entidad que no
            // sea del trámite. De hecho, lo que indica a qué tramite pertenece una
            // condición urbanística, es el trámite de su entidad.
            if (!tramite.getCodigo().equals(cu.getCODIGO().getENTIDAD().getTramite())) {
                liberarCache();
                throw new ExcepcionPersistencia("Existe una condición urbanística "
                        + "que contiene una entidad que no es del trámite. Trámite: "
                        + cu.getCODIGO().getENTIDAD().getTramite()
                        + " entidad: " + cu.getCODIGO().getENTIDAD().getEntidad());

            }

            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidad = obtenerEntidad(cu.getCODIGO().getENTIDAD().getTramite(),
                    cu.getCODIGO().getENTIDAD().getEntidad());

            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detRpm = obtenerDeterminacion(cu.getCODIGO().getDETERMINACION().getTramite(),
                    cu.getCODIGO().getDETERMINACION().getDeterminacion());

            Entidaddeterminacion ed = new Entidaddeterminacion();
            ed.setEntidad(entidad);
            ed.setDeterminacion(detRpm);

            entidades.add(ed);

            guardarCasos(cu.getCASOS().getCASO(), ed, tramite, detRpm, casosEntidadDeterminacion, casosAplicacion, entidades);
        }

        // Una vez procesados todos los casos proceso las dependencias entre ellos.
        for (Entidaddeterminacionregimen edr : casosAplicacion.keySet()) {
            edr.setCasoentidaddeterminacionByIdcasoaplicacion(casosEntidadDeterminacion.get(casosAplicacion.get(edr)));
        }
        // Y los vínculos
        for (CondicionUrbanistica cu : condicionesurbanisticas.getCONDICIONURBANISTICA()) {
            for (Caso caso : cu.getCASOS().getCASO()) {
                if (caso.getVINCULOS() != null) {
                    for (CodigoCaso vinculo : caso.getVINCULOS().getVINCULO()) {
                        Vinculocaso vinculoCaso = new Vinculocaso();
                        vinculoCaso.setCasoentidaddeterminacionByIdcaso(casosEntidadDeterminacion.get(caso.getCodigo()));
                        vinculoCaso.setCasoentidaddeterminacionByIdcasovinculado(casosEntidadDeterminacion.get(vinculo.getCaso()));
                        entidades.add(vinculoCaso);
                    }
                }
            }
        }

        c = 0;
        size = entidades.size();
        for (Object entidad : entidades) {
            c++;
            log.info("Guardando registro condiciones urbanísticas " + c + " de " + size);
            notificar(6, GestorFipListener.PASOS_GUARDADO, c, size);
            em.persist(entidad);
        }
    }

    /**
     *
     * @param determinacion
     * @param tramite
     * @param padre
     * @return
     * @throws ExcepcionPersistencia
     */
    private es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion guardarDeterminacion(Determinacion determinacion, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion padre, int orden) throws ExcepcionPersistencia {
        log.info("Guardando datos determinacion " + determinacion.getCodigo());

        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detRpm = new es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion();
        detRpm.setApartado(determinacion.getApartado());

        detRpm.setIdcaracter(Integer.parseInt(determinacion.getCaracter()));
        detRpm.setBsuspendida(determinacion.isSuspendida());
        detRpm.setCodigo(determinacion.getCodigo());
        detRpm.setEtiqueta(determinacion.getEtiqueta());
        detRpm.setNombre(determinacion.getNombre());
        detRpm.setTexto(determinacion.getTEXTO());
        detRpm.setDeterminacionByIdpadre(padre);
        detRpm.setTramite(tramite);
        detRpm.setOrden(orden);

        if (determinacion.getBASE() != null) {
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion base = obtenerDeterminacion(determinacion.getBASE().getTramite(),
                    determinacion.getBASE().getDeterminacion());

            detRpm.setDeterminacionByIddeterminacionbase(base);

        }

        em.persist(detRpm);

        if (determinacion.getHIJAS() != null) {
            int h = 0;
            int size = determinacion.getHIJAS().getDETERMINACION().size();
            for (Determinacion hija : determinacion.getHIJAS().getDETERMINACION()) {
                h++;
                log.info("Guardando determinacion hija " + h + " de " + size);
                detsRpm.put(tramite.getCodigofip() + hija.getCodigo(), guardarDeterminacion(hija, tramite, detRpm, h));
            }
        }

        // Almacenamos los documentos de una determinación. En el XML los 
        // documentos están asociados a través de su código. Ese código
        // no se almacena en base de datos, por lo que es necesario mantener
        // la correspondencia entre códigos de documentos y documentos de base de datos.
        if (determinacion.getDOCUMENTOS() != null) {
            int d = 0;
            int size = determinacion.getDOCUMENTOS().getDOCUMENTO().size();
            for (es.mitc.redes.urbanismoenred.data.fip.Determinacion.DOCUMENTOS.DOCUMENTO doc : determinacion.getDOCUMENTOS().getDOCUMENTO()) {
                d++;
                log.info("Guardando referencia a documento " + d + " de " + size);
                if (docs.containsKey(doc.getCodigo())) {
                    Documentodeterminacion docdet = new Documentodeterminacion();
                    docdet.setDeterminacion(detRpm);
                    docdet.setDocumento(docs.get(doc.getCodigo()));
                    em.persist(docdet);
                } else {
                    liberarCache();
                    throw new ExcepcionPersistencia("No se ha encontrado documento con código " + doc.getCodigo());
                }
            }
        }

        // Guardamos sólo las específicas, las reguladoras, al ser relaciones, 
        // se guardan en el método guardarReferenciasDeterminacion
        if (determinacion.getREGULACION() != null) {
            if (determinacion.getREGULACION().getREGULACIONESESPECIFICAS() != null) {
                int de = 0;
                int size = determinacion.getREGULACION().getREGULACIONESESPECIFICAS().getREGULACIONESPECIFICA().size();
                for (RegulacionEspecifica regEspecifica : determinacion.getREGULACION().getREGULACIONESESPECIFICAS().getREGULACIONESPECIFICA()) {
                    de++;
                    log.info("Guardando determinación específica " + de + " de " + size);
                    guardarRegulacionEspecifica(tramite, regEspecifica, detRpm, 0);
                }
            }
        }

        log.debug("Determinación " + determinacion.getCodigo() + " guardada con id " + detRpm.getIden());
        return detRpm;
    }

    /**
     *
     * @param determinaciones
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private void guardarDeterminaciones(DETERMINACIONES determinaciones, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite) throws ExcepcionPersistencia {
        log.debug("Guardando Determinaciones.");
        int d = 0;
        int size = determinaciones.getDETERMINACION().size();
        detsRpm.clear();

        for (Determinacion determinacion : determinaciones.getDETERMINACION()) {
            d++;
            log.info("Guardando determinación " + d + " de " + size);
            detsRpm.put(tramite.getCodigofip() + determinacion.getCodigo(), guardarDeterminacion(determinacion, tramite, null, d));
            notificar(3, GestorFipListener.PASOS_GUARDADO, d, size * 2);
        }

        em.flush();

        for (Determinacion determinacion : determinaciones.getDETERMINACION()) {
            d++;
            log.info("Guardando referencias determinación " + d + " de " + size);
            guardarReferenciasDeterminacion(determinacion, tramite);
            notificar(3, GestorFipListener.PASOS_GUARDADO, d + size, size * 2);
        }
    }

    /**
     *
     * @param documento
     * @param entidadRpm
     * @return
     * @throws ExcepcionPersistencia
     */
    private Documentoentidad guardarDocumentoEntidad(
            DOCUMENTO documento,
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidadRpm) throws ExcepcionPersistencia {
        if (docs.containsKey(documento.getCodigo())) {
            Documentoentidad de = new Documentoentidad();
            de.setDocumento(docs.get(documento.getCodigo()));
            de.setEntidad(entidadRpm);
            em.persist(de);
            return de;
        } else {
            liberarCache();
            throw new ExcepcionPersistencia("No se ha encontrado documento con código " + documento.getCodigo());
        }
    }

    /**
     *
     * @param documentos
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private void guardarDocumentos(DOCUMENTOS documentos, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite) throws ExcepcionPersistencia {
        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento documentoRpm;
        log.debug("Guardando documentos del trámite.");
        // Los documentos no almacenan en BD el código FIP, por lo que no es posible
        // recuperarlos utilizando ese código, como pasa con otros elementos.
        // Para poder obtenre los documentos a partir de su código referenciados en el 
        // FIP los almaceno en un Map asociados a su código.
        docs.clear();
        int d = 0;
        int h = 0;
        int sizeDocs = documentos.getDOCUMENTO().size();
        int sizeHojas;
        for (Documento documento : documentos.getDOCUMENTO()) {
            documentoRpm = new es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento();
            d++;
            log.info("Guardando documento " + d + " de " + sizeDocs + " codigo: " + documento.getCodigo());
            documentoRpm.setArchivo(normalizar(documento.getArchivo()));
            documentoRpm.setComentario(documento.getCOMENTARIO());
            if (documento.getGrupo() != null) {
                documentoRpm.setIdgrupodocumento(Integer.parseInt(documento.getGrupo()));
            }
            if (documento.getEscala() != null) {
                documentoRpm.setEscala(Integer.parseInt(documento.getEscala().toString()));
            }
            documentoRpm.setNombre(documento.getNombre());
            documentoRpm.setIdtipodocumento(Integer.parseInt(documento.getTipo()));
            documentoRpm.setTramite(tramite);
            em.persist(documentoRpm);

            log.debug("Documento guardado con id: " + documentoRpm.getIden());

            if (documento.getHOJAS() != null) {
                h = 0;
                sizeHojas = documento.getHOJAS().getHOJA().size();
                for (Hoja hoja : documento.getHOJAS().getHOJA()) {
                    h++;
                    log.info("Guardando hoja " + h + " de " + sizeHojas);
                    guardarHoja(documentoRpm, hoja);
                }
            }

            docs.put(documento.getCodigo(), documentoRpm);
            notificar(1, GestorFipListener.PASOS_GUARDADO, d, sizeDocs);
        }
        d = 0;
        sizeDocs = documentos.getDOCUMENTOREFUNDIDO().size();
        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento original;
        for (DOCUMENTOREFUNDIDO docref : documentos.getDOCUMENTOREFUNDIDO()) {

            d++;
            log.info("Guardando documento refundido " + d + " de " + sizeDocs + " codigo: " + docref.getCodigo());

            original = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento.class, Integer.parseInt(docref.getCodigo()));
            if (original != null) {
                documentoRpm = new es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento();
                documentoRpm.setArchivo(original.getArchivo());
                documentoRpm.setComentario(original.getComentario());
                documentoRpm.setIdgrupodocumento(original.getIdgrupodocumento());

                documentoRpm.setEscala(original.getEscala());
                documentoRpm.setNombre(original.getNombre());
                documentoRpm.setIdtipodocumento(original.getIdtipodocumento());
                documentoRpm.setTramite(tramite);
                documentoRpm.setDocumento(original);
                em.persist(documentoRpm);
            } else {
                throw new ExcepcionPersistencia("No se ha encontrado documento original con código " + docref.getCodigo());
            }

        }

        log.debug("Documentos guardados.");
    }

    /**
     * Cambia la ruta relativa del archivo sustituyendo los separadores por los
     * propios del SO en el que se ejecute.
     *
     * @param archivo
     * @return
     */
    private String normalizar(String archivo) {
        if (File.separator.equals("/")) {
            return archivo.replace('\\', File.separatorChar);
        } else {
            return archivo.replace('/', File.separatorChar);
        }
    }

    /**
     * Guarda las entidades de un FIP.
     *
     * @param entidad
     * @param padre
     * @param tramite
     * @param orden
     * @return
     * @throws ExcepcionPersistencia
     */
    private es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad guardarEntidad(Entidad entidad, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite, int orden, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad padre) throws ExcepcionPersistencia {
        log.info("Guardando datos entidad " + entidad.getCodigo());

        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidadRpm = new es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad();
        entidadRpm.setClave(entidad.getClave());
        entidadRpm.setCodigo(entidad.getCodigo());
        entidadRpm.setEtiqueta(entidad.getEtiqueta());
        entidadRpm.setNombre(entidad.getNombre());
        entidadRpm.setBsuspendida(entidad.isSuspendida());
        entidadRpm.setTramite(tramite);
        entidadRpm.setOrden(orden);

        if (padre != null) {
            entidadRpm.setEntidadByIdpadre(padre);
        }

        em.persist(entidadRpm);

        if (entidad.getBASE() != null) {
            // Obtengo la entidad base, que debe encontrarse ya en la BD
            try {
                es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad base = (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad) em.createNamedQuery("Entidad.obtenerEntidadPorTramiteYCodigoEntidad").setParameter("codigoTramite", entidad.getBASE().getTramite()).setParameter("codigoEntidad", entidad.getBASE().getEntidad()).getSingleResult();

                entidadRpm.setEntidadByIdentidadbase(base);
            } catch (NoResultException nre) {
                liberarCache();
                throw new ExcepcionPersistencia("No se ha encontrado entidad base en el trámite "
                        + entidad.getBASE().getTramite()
                        + " y con código " + entidad.getBASE().getEntidad(), nre);
            } catch (NonUniqueResultException nure) {
                liberarCache();
                throw new ExcepcionPersistencia("No se ha encontrado entidad base en el trámite "
                        + entidad.getBASE().getTramite()
                        + " y con código " + entidad.getBASE().getEntidad(), nure);
            }
        }

        entsRpm.put(tramite.getCodigofip() + entidad.getCodigo(), entidadRpm);

        if (entidad.getGEOMETRIA() != null) {
            guardarGeometriaEntidad(entidadRpm, entidad.getGEOMETRIA());
        }

        if (entidad.getHIJAS() != null) {
            int e = 0;
            int size = entidad.getHIJAS().getENTIDAD().size();
            for (Entidad hija : entidad.getHIJAS().getENTIDAD()) {
                e++;
                log.info("Guardando entidad hija " + e + " de " + size);
                guardarEntidad(hija, tramite, e, entidadRpm);
            }
        }

        if (entidad.getDOCUMENTOS() != null) {
            int d = 0;
            int size = entidad.getDOCUMENTOS().getDOCUMENTO().size();
            for (DOCUMENTO documento : entidad.getDOCUMENTOS().getDOCUMENTO()) {
                d++;
                log.info("Guardando referencia a documento " + d + " de " + size);
                guardarDocumentoEntidad(documento, entidadRpm);
            }
        }

        log.debug("Entidad " + entidad.getCodigo() + " guardada en base de datos con id " + entidadRpm.getIden());
        return entidadRpm;
    }

    /**
     * Guarda las entidades de un FIP
     *
     * @param entidades
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private void guardarEntidades(ENTIDADES entidades, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite) throws ExcepcionPersistencia {

        log.debug("Guardando Entidades.");

        entsRpm.clear();

        int e = 0;
        int size = entidades.getENTIDAD().size();
        for (Entidad entidad : entidades.getENTIDAD()) {
            e++;
            log.info("Guardando entidad " + e + " de " + size);
            guardarEntidad(entidad, tramite, e, null);
            notificar(2, GestorFipListener.PASOS_GUARDADO, e, size);
        }

    }

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.servicios.dal.GestorFipLocal#guardarFip(es.mitc.redes.urbanismoenred.data.validacion.FIP)
     */
    @Override
    public int guardarFip(FIP fip) throws ExcepcionPersistencia {
        log.debug("inicio guardado FIP.");
        // Entiendo que antes de guardar el FIP se ha validado su integridad

        int resultado = guardarTramite(fip.getTRAMITE());

        liberarCache();

        return resultado;
    }

    /**
     * Guarda el tipo de geometría en función del contenido del campo geometria
     * de una Entidad del FIP.
     *
     * El guardado de las geometrías se tiene que hacer con SQL y antes se debe
     * actualizar el estado del EnityManager, o de lo contrario los cambios
     * especificados no tendrán efecto.
     *
     * @param entidad Entidad de base de datos.
     * @param geometria
     * @return
     * @throws ExcepcionPersistencia
     */
    private Object guardarGeometriaEntidad(
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidad,
            String geometria) throws ExcepcionPersistencia {
        String geom = geometria.toUpperCase();
        if (geom.contains("POLYGON")) {
            Entidadpol poligono = new Entidadpol();
            poligono.setEntidad(entidad);
            poligono.setGeom(geometria);
            em.persist(poligono);
            em.flush();
            log.debug("Guardado poligono con id: " + poligono.getIden() + " y geometria " + geometria);
            if (em.createNativeQuery("update planeamiento.entidadpol set geom=multi(geometryfromtext('" + geometria + "')) where iden=" + poligono.getIden()).executeUpdate() != 1) {
                throw new ExcepcionPersistencia("No se ha podido guardar correctamente la geometría de la entidad " + entidad.getCodigo());
            }
            em.flush();
            return poligono;
        }

        if (geom.contains("LINESTRING")) {
            Entidadlin linea = new Entidadlin();
            linea.setEntidad(entidad);
            linea.setGeom(geometria);
            em.persist(linea);
            em.flush();
            log.debug("Guardado línea con id: " + linea.getIden() + " y geometria " + geometria);
            if (em.createNativeQuery("update planeamiento.entidadlin set geom=multi(geometryfromtext('" + geometria + "')) where iden=" + linea.getIden()).executeUpdate() != 1) {
                throw new ExcepcionPersistencia("No se ha podido guardar correctamente la geometría de la entidad " + entidad.getCodigo());
            }
            em.flush();
            return linea;
        }

        if (geom.contains("POINT")) {
            Entidadpnt punto = new Entidadpnt();
            punto.setEntidad(entidad);
            punto.setGeom(geometria);
            em.persist(punto);
            em.flush();
            log.debug("Guardado punto con id: " + punto.getIden() + " y geometria " + geometria);
            if (em.createNativeQuery("update planeamiento.entidadpnt set geom=multi(geometryfromtext('" + geometria + "')) where iden=" + punto.getIden()).executeUpdate() != 1) {
                throw new ExcepcionPersistencia("No se ha podido guardar correctamente la geometría de la entidad " + entidad.getCodigo());
            }
            em.flush();
            return punto;
        }
        liberarCache();
        throw new ExcepcionPersistencia("Geometría desconocida: " + geom);
    }

    /**
     * Guarda una hoja asociada a un documento.
     *
     * El guardado de las geometrías se tiene que hacer con SQL y antes se debe
     * actualizar el estado del EnityManager, o de lo contrario los cambios
     * especificados no tendrán efecto.
     *
     * @param documentoRpm
     * @param hoja
     * @return
     * @throws ExcepcionPersistencia
     */
    private Documentoshp guardarHoja(
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento documentoRpm,
            Hoja hoja) throws ExcepcionPersistencia {
        Documentoshp docshp = new Documentoshp();
        docshp.setGeom(hoja.getGEOMETRIA());
        docshp.setHoja(hoja.getNombre());
        docshp.setDocumento(documentoRpm);
        em.persist(docshp);
        em.flush();
        if (em.createNativeQuery("update planeamiento.documentoshp set geom=geometryfromtext('" + hoja.getGEOMETRIA() + "') where iden=" + docshp.getIden()).executeUpdate() != 1) {
            throw new ExcepcionPersistencia("No se ha podido guardar correctamente la geometría del documento " + hoja.getNombre());
        }
        em.flush();
        return docshp;
    }

    /**
     * Guarda las operaciones de un trámite. Las operaciones están definidas en
     * el FIP como: <xs:complexType> <xs:sequence> <xs:element minOccurs="0"
     * maxOccurs="unbounded" name="OPERACIONES-DETERMINACIONES">
     * <xs:complexType> <xs:sequence> <xs:element maxOccurs="unbounded"
     * name="OPERACION-DETERMINACION" type="OperacionDeterminacion"/>
     * </xs:sequence> </xs:complexType> </xs:element> <xs:element minOccurs="0"
     * maxOccurs="unbounded" name="OPERACIONES-ENTIDADES"> <xs:complexType>
     * <xs:sequence> <xs:element maxOccurs="unbounded" name="OPERACION-ENTIDAD"
     * type="OperacionEntidad"/> </xs:sequence> </xs:complexType> </xs:element>
     * </xs:sequence> </xs:complexType>
     *
     * @param operaciones
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private void guardarOperaciones(OPERACIONES operaciones, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite) throws ExcepcionPersistencia {
        log.debug("Guardando operaciones.");
        if (operaciones.getOPERACIONESDETERMINACIONES() != null) {
            guardarOperacionesDeterminaciones(operaciones.getOPERACIONESDETERMINACIONES(), tramite);
        }
        if (operaciones.getOPERACIONESENTIDADES() != null) {
            guardarOperacionesEntidades(operaciones.getOPERACIONESENTIDADES(), tramite);
        }
    }

    /**
     *
     * @param operacionesdeterminaciones
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private void guardarOperacionesDeterminaciones(
            List<OPERACIONESDETERMINACIONES> operacionesdeterminaciones, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite) throws ExcepcionPersistencia {
        int o = 0;
        int od = 0;
        int size = operacionesdeterminaciones.size();
        int sizeod = 0;
        for (OPERACIONESDETERMINACIONES operaciones : operacionesdeterminaciones) {
            o++;
            log.info("Guardando grupo operaciones determinacion " + o + " de " + size);
            sizeod = operaciones.getOPERACIONDETERMINACION().size();
            od = 0;
            for (OperacionDeterminacion operacion : operaciones.getOPERACIONDETERMINACION()) {
                od++;
                log.info("Guardando operación determinación " + od + " de " + sizeod);
                es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detOperada = obtenerDeterminacion(operacion.getOPERADA().getTramite(), operacion.getOPERADA().getDeterminacion());

                es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detOperadora = obtenerDeterminacion(operacion.getOPERADORA().getTramite(),
                        operacion.getOPERADORA().getDeterminacion());

                Operacion operacionRpm = new Operacion();
                operacionRpm.setTramite(tramite);
                operacionRpm.setTexto(operacion.getTEXTO());
                operacionRpm.setOrden((new Long(operacion.getOrden())).intValue());

                em.persist(operacionRpm);

                Operaciondeterminacion operacionDeterminacion = new Operaciondeterminacion();
                operacionDeterminacion.setDeterminacionByIddeterminacion(detOperada);
                operacionDeterminacion.setDeterminacionByIddeterminacionoperadora(detOperadora);
                operacionDeterminacion.setOperacion(operacionRpm);
                operacionDeterminacion.setIdtipooperaciondet(Integer.parseInt(operacion.getTipo()));

                em.persist(operacionDeterminacion);

                notificar(4, GestorFipListener.PASOS_GUARDADO, od, sizeod);

                // Las operaciones de adición de valor de referencia deben crear la opción determinación,
                // ya que pueden ser utilizados dentro del FIP
                if (TIPO_OPERACION_ADICION_VALORREFERENCIA == operacionDeterminacion.getIdtipooperaciondet()) {
                    for (Opciondeterminacion opciondet : detOperadora.getOpciondeterminacionsForIddeterminacion()) {
                        // Nos aseguramos que no existe la opción
                        if (em.createNamedQuery("Opciondeterminacion.buscarPorDetYValorRef").setParameter("idDeterminacion", detOperada.getIden()).setParameter("idValorRef", opciondet.getDeterminacionByIddeterminacionvalorref().getIden()).getResultList().size() == 0) {
                            Opciondeterminacion nuevaOpcionDeterminacion = new Opciondeterminacion();

                            nuevaOpcionDeterminacion.setDeterminacionByIddeterminacion(detOperada);
                            nuevaOpcionDeterminacion.setDeterminacionByIddeterminacionvalorref(opciondet.getDeterminacionByIddeterminacionvalorref());
                            em.persist(nuevaOpcionDeterminacion);

                            log.debug("opciondeterminacion creada por operacion de adicion de referencia "
                                    + operacion.getOPERADA().getDeterminacion()
                                    + " " + operacion.getOPERADORA().getDeterminacion());
                        } else {
                            log.debug("opciondeterminacion ya existente para operacion de adicion de referencia "
                                    + operacion.getOPERADA().getDeterminacion()
                                    + " " + operacion.getOPERADORA().getDeterminacion());
                        }

                    }
                }
            }
        }
    }

    /**
     *
     * @param operacionesentidades
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    @SuppressWarnings("unchecked")
    private void guardarOperacionesEntidades(
            List<OPERACIONESENTIDADES> operacionesentidades,
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite) throws ExcepcionPersistencia {
        int o = 0;
        int oe = 0;
        int size = operacionesentidades.size();
        int sizeoe = 0;
        int tipoOperacion;
        for (OPERACIONESENTIDADES operacionEntidades : operacionesentidades) {
            o++;
            log.info("Guardando grupo operaciones entidades " + o + " de " + size);
            sizeoe = operacionEntidades.getOPERACIONENTIDAD().size();
            oe = 0;
            for (OperacionEntidad operacionEntidad : operacionEntidades.getOPERACIONENTIDAD()) {
                oe++;
                log.info("Guardando operación entidad " + oe + " de " + sizeoe);
                es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidadOperada = obtenerEntidad(operacionEntidad.getOPERADA().getTramite(),
                        operacionEntidad.getOPERADA().getEntidad());
                es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidadOperadora = obtenerEntidad(operacionEntidad.getOPERADORA().getTramite(),
                        operacionEntidad.getOPERADORA().getEntidad());

                Operacion operacion = new Operacion();

                operacion.setTramite(tramite);
                operacion.setTexto(operacionEntidad.getTEXTO());
                operacion.setOrden((new Long(operacionEntidad.getOrden())).intValue());

                em.persist(operacion);

                tipoOperacion = Integer.parseInt(operacionEntidad.getTipo());

                if (tipoOperacion != TIPO_OPERACION_CREACION_ADSCRIPCION && tipoOperacion != TIPO_OPERACION_ELIMINACION_ADSCRIPCION) {
                    Operacionentidad opEnt = new Operacionentidad();
                    opEnt.setEntidadByIdentidad(entidadOperada);
                    opEnt.setEntidadByIdentidadoperadora(entidadOperadora);
                    opEnt.setIdtipooperacionent(tipoOperacion);
                    opEnt.setOperacion(operacion);
                    // No se procesan las propiedades de adscripción
                    em.persist(opEnt);
                } else {
                    Operacionrelacion or;

                    //ACR: DISTINGUIMOS SI ES DE ELIMINACIÓN O DE CREACIÓN POR EL TIPO,
                    //NO POR SI TIENE O NO PROPIEDADES 
                    if (tipoOperacion == TIPO_OPERACION_CREACION_ADSCRIPCION) {
                        if (operacionEntidad.getPROPIEDADESADSCRIPCION() != null) {
                            or = new Operacionrelacion();
                            or.setIdtipooperacionrel(2);
                            or.setOperacion(operacion);
                            or.setRelacion(guardarPropiedadesAdscripcion(operacionEntidad.getPROPIEDADESADSCRIPCION(), entidadOperada, entidadOperadora, tramite));
                            em.persist(or);
                        }
                    } else {
                        List<Relacion> paraBorrar = em.createNamedQuery("Relacion.obtenerAdscripcionPorEntidadOrigenDestino").setParameter("idDefEntidadOrigen", ID_DEF_VECTOR_ENTIDAD_ORIGEN_ADSCRIPCION).setParameter("idDefEntidadDestino", ID_DEF_VECTOR_ENTIDAD_DESTINO_ADSCRIPCION).setParameter("idEntidadOrigen", entidadOperada.getIden()).setParameter("idEntidadDestino", entidadOperadora.getIden()).setParameter("idDefPropAdscripcion", ID_DEF_RELACION_ADSCRIPCION_ENTIDADES).getResultList();
                        if (paraBorrar.size() > 0) {
                            for (Relacion rop : paraBorrar) {
                                or = new Operacionrelacion();
                                or.setIdtipooperacionrel(1);
                                or.setOperacion(operacion);
                                or.setRelacion(rop);
                                em.persist(or);
                            }
                        } else {
                            throw new ExcepcionPersistencia("No se ha encontrado adscripción para entidad operadora "
                                    + operacionEntidad.getOPERADORA().getEntidad()
                                    + " del trámite " + operacionEntidad.getOPERADORA().getTramite()
                                    + " y entidad operada " + operacionEntidad.getOPERADA().getEntidad()
                                    + " del trámite " + operacionEntidad.getOPERADA().getTramite());
                        }
                    }
                }

                notificar(5, GestorFipListener.PASOS_GUARDADO, oe, sizeoe);
            }
        }

    }

    /**
     *
     * @param propiedadesadscripcion
     * @param entidadOperadora
     * @param entidadOperada
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private Relacion guardarPropiedadesAdscripcion(
            PropiedadesAdscripcion propiedadesAdscripcion,
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidadOrigen,
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidadDestino,
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite) throws ExcepcionPersistencia {

        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detUnidad = obtenerDeterminacion(propiedadesAdscripcion.getUNIDAD().getTramite(), propiedadesAdscripcion.getUNIDAD().getDeterminacion());
        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detTipo = obtenerDeterminacion(propiedadesAdscripcion.getTIPO().getTramite(), propiedadesAdscripcion.getTIPO().getDeterminacion());

        Map<Integer, Integer> vectores = new HashMap<Integer, Integer>();
        vectores.put(ID_DEF_VECTOR_ENTIDAD_ORIGEN_ADSCRIPCION, entidadOrigen.getIden());
        vectores.put(ID_DEF_VECTOR_ENTIDAD_DESTINO_ADSCRIPCION, entidadDestino.getIden());
        vectores.put(ID_DEF_VECTOR_DET_UNIDAD_ADSCRIPCION, detUnidad.getIden());
        vectores.put(ID_DEF_VECTOR_DET_TIPO_ADSCRIPCION, detTipo.getIden());

        Map<Integer, String> propiedades = new HashMap<Integer, String>();
        propiedades.put(ID_DEF_PROPIEDAD_CUANTIA_ADSCRIPCION,
                String.valueOf(propiedadesAdscripcion.getCuantia()));
        propiedades.put(ID_DEF_PROPIEDAD_TEXTO_ADSCRIPCION,
                (propiedadesAdscripcion.getTEXTO() != null) ? propiedadesAdscripcion.getTEXTO() : "");

        return guardarRelacion(tramite,
                ID_DEF_RELACION_ADSCRIPCION_ENTIDADES,
                vectores,
                propiedades);
    }

    /**
     *
     * @param determinacion
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private void guardarReferenciasDeterminacion(Determinacion determinacion, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite) throws ExcepcionPersistencia {
        log.info("Guardando referencias determinacion " + determinacion.getCodigo());
        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detRpm = obtenerDeterminacion(tramite.getCodigofip(), determinacion.getCodigo());

        if (determinacion.getVALORESREFERENCIA() != null) {
            int vr = 0;
            int size = determinacion.getVALORESREFERENCIA().getVALORREFERENCIA().size();
            for (CodigoDeterminacion valorref : determinacion.getVALORESREFERENCIA().getVALORREFERENCIA()) {
                vr++;
                log.info("Guardando valor de referencia " + vr + " de " + size);
                es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detref;

                detref = obtenerDeterminacion(valorref.getTramite(),
                        valorref.getDeterminacion());

                Opciondeterminacion opcionDet = new Opciondeterminacion();

                opcionDet.setDeterminacionByIddeterminacion(detRpm);
                opcionDet.setDeterminacionByIddeterminacionvalorref(detref);

                em.persist(opcionDet);
            }
        }

        if (determinacion.getUNIDAD() != null) {
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion unidad;

            unidad = obtenerDeterminacion(determinacion.getUNIDAD().getTramite(), determinacion.getUNIDAD().getDeterminacion());

            Map<Integer, Integer> vectores = new HashMap<Integer, Integer>();
            vectores.put(ID_DEF_VECTOR_DET_ASIGNA_UNIDAD, detRpm.getIden());
            vectores.put(ID_DEF_VECTOR_DET_CARACTER_UNIDAD_ASIGNA, unidad.getIden());

            guardarRelacion(tramite, ID_DEF_RELACION_VINCULO_UNIDAD, vectores, null);

        }

        if (determinacion.getGRUPOSAPLICACION() != null) {
            int ga = 0;
            int size = determinacion.getGRUPOSAPLICACION().getGRUPOAPLICACION().size();

            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion grupodeterminacion;
            for (CodigoDeterminacion grupo : determinacion.getGRUPOSAPLICACION().getGRUPOAPLICACION()) {
                ga++;
                log.info("Guardando grupo de aplicacion " + ga + " de " + size);

                grupodeterminacion = obtenerDeterminacion(grupo.getTramite(),
                        grupo.getDeterminacion());

                Determinaciongrupoentidad detgrupoentidad = new Determinaciongrupoentidad();
                detgrupoentidad.setDeterminacionByIddeterminacion(detRpm);
                detgrupoentidad.setDeterminacionByIddeterminaciongrupo(grupodeterminacion);

                em.persist(detgrupoentidad);
            }

        }

        // Persistencias de regulaciones de una determinación
        if (determinacion.getREGULACION() != null) {
            if (determinacion.getREGULACION().getDETERMINACIONESREGULADORAS() != null) {
                int dr = 0;
                int size = determinacion.getREGULACION().getDETERMINACIONESREGULADORAS().getDETERMINACIONREGULADORA().size();
                es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detRpmReguladora;
                for (CodigoDeterminacion detReguladora : determinacion.getREGULACION().getDETERMINACIONESREGULADORAS().getDETERMINACIONREGULADORA()) {
                    dr++;
                    log.info("Guardando determinación reguladora " + dr + " de " + size);

                    detRpmReguladora = obtenerDeterminacion(detReguladora.getTramite(),
                            detReguladora.getDeterminacion());

                    Map<Integer, Integer> vectores = new HashMap<Integer, Integer>();
                    vectores.put(Integer.parseInt(caracterAvector.getProperty(determinacion.getCaracter())), detRpm.getIden());
                    vectores.put(Integer.parseInt(caracterAvector.getProperty(determinacion.getCaracter())) + 1, detRpmReguladora.getIden());

                    guardarRelacion(tramite,
                            Integer.parseInt(caracterAdefRelacion.getProperty(determinacion.getCaracter())),
                            vectores,
                            null);
                }
            }
        }

        if (determinacion.getHIJAS() != null) {
            int h = 0;
            int size = determinacion.getHIJAS().getDETERMINACION().size();
            for (Determinacion hija : determinacion.getHIJAS().getDETERMINACION()) {
                h++;
                log.info("Guardando referencias determinacion hija " + h + " de " + size);
                guardarReferenciasDeterminacion(hija, tramite);
            }
        }
    }

    /**
     *
     * @param regimenEspecifico
     * @param edr
     * @param regimenEspecificoPadre
     * @param entidades
     */
    private void guardarRegimenEspecifico(RegimenEspecifico regimenEspecifico, Entidaddeterminacionregimen edr, Regimenespecifico regimenEspecificoPadre, List<Object> entidades) {
        Regimenespecifico re = new Regimenespecifico();
        re.setNombre(regimenEspecifico.getNombre());
        re.setOrden(regimenEspecifico.getOrden().intValue());
        re.setTexto(regimenEspecifico.getTEXTO());
        re.setEntidaddeterminacionregimen(edr);
        re.setRegimenespecifico(regimenEspecificoPadre);

//		em.persist(re);

        entidades.add(re);

        if (regimenEspecifico.getHIJOS() != null) {
            for (RegimenEspecifico hijo : regimenEspecifico.getHIJOS().getREGIMENESPECIFICO()) {
                guardarRegimenEspecifico(hijo, edr, re, entidades);
            }
        }
    }

    /**
     *
     * @param tramite
     * @param regEspecifica
     * @param detRpm
     * @param idRelacionPadre
     */
    private void guardarRegulacionEspecifica(
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite,
            RegulacionEspecifica regEspecifica,
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion detRpm,
            int idRelacionPadre) {

        // Se almacena la asociación de regulaciones específicas a una determinación
        // como vectores de una relación
        Map<Integer, Integer> vectores = new HashMap<Integer, Integer>();
        vectores.put(ID_DEF_VECTOR_TEXTO_REGULACION_DETERMINACION, detRpm.getIden());

        vectores.put(ID_DEF_VECTOR_HERENCIA_CLAVE, idRelacionPadre);

        // Se almacenan los datos de regulaciones específicas como propiedades de una relación
        Map<Integer, String> propiedades = new HashMap<Integer, String>();
        propiedades.put(ID_DEF_PROPIEDAD_ORDEN_PRESENTACION, regEspecifica.getOrden().toString());
        propiedades.put(ID_DEF_PROPIEDAD_NOMBRE, regEspecifica.getNombre());
        propiedades.put(ID_DEF_PROPIEDAD_TEXTO, (regEspecifica.getTEXTO() != null) ? regEspecifica.getTEXTO() : "");


        Relacion relacion = guardarRelacion(tramite,
                ID_DEF_RELACION_TEXTO_REGULACION_DETERMINACION,
                vectores,
                propiedades);

        // Por último las regulaciones hijas.
        if (regEspecifica.getHIJAS() != null) {
            for (RegulacionEspecifica hija : regEspecifica.getHIJAS().getREGULACIONESPECIFICA()) {
                guardarRegulacionEspecifica(tramite, hija, detRpm, relacion.getIden());
            }
        }
    }

    /**
     * Guarda en base de datos aquella información que se almacena en las tablas
     * relacion, propiedadrelacion y vectorrelacion del esquema de planeamiento.
     *
     * @param tramite Trámite en el que se crean estos datos
     * @param defrelacion identificador de la definición de la relacion en el
     * esquema diccionario
     * @param vectores parejas identificador de la definición del vector -
     * identificador determinación asociada
     * @param propiedades parejas identificador de la definición de la propiedad
     * - valor almacenado.
     * @return
     */
    private Relacion guardarRelacion(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite,
            int defrelacion,
            Map<Integer, Integer> vectores,
            Map<Integer, String> propiedades) {
        Relacion relacion = new Relacion();

        relacion.setIddefrelacion(defrelacion);
        relacion.setTramiteByIdtramitecreador(tramite);

        em.persist(relacion);

        if (!vectores.isEmpty()) {
            for (Integer key : vectores.keySet()) {
                Vectorrelacion vector = new Vectorrelacion();
                vector.setIddefvector(key);
                vector.setValor(vectores.get(key));
                vector.setRelacion(relacion);

                em.persist(vector);
            }
        }

        if (propiedades != null && !propiedades.isEmpty()) {
            for (Integer key : propiedades.keySet()) {
                Propiedadrelacion propiedad = new Propiedadrelacion();
                propiedad.setIddefpropiedad(key);
                propiedad.setRelacion(relacion);
                propiedad.setValor(propiedades.get(key));

                em.persist(propiedad);
            }
        }

        return relacion;
    }

    /**
     *
     * @param tramite
     * @return
     * @throws ExcepcionPersistencia
     */
    private int guardarTramite(Tramite tramite) throws ExcepcionPersistencia {
        log.info("Guardando trámite " + tramite.getCodigo());
        try {
            // El trámite especificado en el FIP debe existir ya en base de datos
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite trpm = (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite) em.createNamedQuery("Tramite.findTramiteFromCodFip").setParameter("codigoFip", tramite.getCodigo()).getSingleResult();

            // Primero guardamos los documentos. Es esencial que se haga así, ya que 
            // las entidades referencian a esos documentos.
            if (tramite.getDOCUMENTOS() != null) {
                guardarDocumentos(tramite.getDOCUMENTOS(), trpm);
            }

            guardarEntidades(tramite.getENTIDADES(), trpm);
            guardarDeterminaciones(tramite.getDETERMINACIONES(), trpm);
            em.flush();
            // Procesamos antes las operaciones porque las que tienen que ver
            // con operaciones de adición de valores de referencia pueden ser 
            // referenciadas en las condiciones urbanísticas
            if (tramite.getOPERACIONES() != null) {
                guardarOperaciones(tramite.getOPERACIONES(), trpm);
            }
            em.flush();
            if (tramite.getCONDICIONESURBANISTICAS() != null) {
                guardarCondicionesUrbanisticas(tramite.getCONDICIONESURBANISTICAS(), tramite);
            }

            if (tramite.getUNIDADES() != null) {
                guardarUnidades(tramite.getUNIDADES(), trpm);
            }
            if (tramite.getAPLICACIONAMBITOS() != null) {
                guardarAmbitosAplicacion(tramite.getAPLICACIONAMBITOS(), tramite);
            }
            if (tramite.getADSCRIPCIONES() != null) {
                guardarAdscripciones(tramite.getADSCRIPCIONES(), trpm);
            }

            em.flush();
            log.info("Tramite " + tramite.getCodigo() + " guardado correctamente con id: " + trpm.getIden());

            return trpm.getIden();
        } catch (NoResultException nre) {
            liberarCache();
            throw new ExcepcionPersistencia("No se ha encontrado ningún trámite con código " + tramite.getCodigo());
        } catch (NonUniqueResultException nure) {
            liberarCache();
            throw new ExcepcionPersistencia("Se ha encontrado más de un trámite con código " + tramite.getCodigo());
        }
    }

    /**
     *
     * @param unidades
     * @param tramite
     * @throws ExcepcionPersistencia
     */
    private void guardarUnidades(UNIDADES unidades, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite) throws ExcepcionPersistencia {
        int u = 0;
        int size = unidades.getUNIDAD().size();
        for (Unidad unidad : unidades.getUNIDAD()) {
            u++;
            log.info("Guardando unidad " + u + " de " + size);
            es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion determinacion = obtenerDeterminacion(tramite.getCodigofip(), unidad.getDeterminacion());
            Map<Integer, Integer> vectores = new HashMap<Integer, Integer>();
            vectores.put(ID_DEF_VECTOR_DET_UNIDAD_APLICA_PROPIEDADES, determinacion.getIden());

            Map<Integer, String> propiedades = new HashMap<Integer, String>();
            propiedades.put(ID_DEF_PROPIEDAD_ABREVIATURA, unidad.getAbreviatura());
            propiedades.put(ID_DEF_PROPIEDAD_DEFINICION, unidad.getDEFINICION());

            guardarRelacion(tramite,
                    ID_DEF_RELACION_DET_CARACTER_UNIDAD,
                    vectores,
                    propiedades);
            notificar(7, GestorFipListener.PASOS_GUARDADO, u, size);
        }

    }

    /**
     * Se libera memoria.
     */
    private void liberarCache() {
        docs.clear();
        detsRpm.clear();
        entsRpm.clear();
    }

    /**
     *
     * @param paso
     * @param totalPasos
     * @param elemento
     * @param totalElementos
     */
    private void notificar(int paso, int totalPasos, int elemento, int totalElementos) {
        for (GestorFipListener listener : listeners) {
            listener.estadoActualizado(paso, totalPasos, elemento, totalElementos);
        }
    }

    /**
     *
     * @param codigoTramite
     * @param codigoDeterminacion
     * @return
     * @throws ExcepcionPersistencia
     */
    private es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion obtenerDeterminacion(String codigoTramite, String codigoDeterminacion) throws ExcepcionPersistencia {
        String id = codigoTramite + codigoDeterminacion;
        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion det;
        det = detsRpm.get(id);
        if (det == null) {
            log.debug("No se ha encontrado Determinacion "
                    + codigoDeterminacion + " del tramite "
                    + codigoTramite + " en memoria, buscando en base de datos.");

            try {
                det = (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion) em.createNamedQuery("Determinacion.obtenerPorCodTramiteCodDeterminacion").setParameter("codigoTramite", codigoTramite).setParameter("codigoDeterminacion", codigoDeterminacion).getSingleResult();
                detsRpm.put(id, det);
            } catch (NoResultException nre) {
                liberarCache();
                throw new ExcepcionPersistencia("No se ha encontrado determinacion con código "
                        + codigoDeterminacion + " en tramite " + codigoTramite);
            } catch (NonUniqueResultException nure) {
                liberarCache();
                throw new ExcepcionPersistencia("Se han encontrado varias determinaciones con código "
                        + codigoDeterminacion + " en tramite " + codigoTramite);
            }
        } else {
            // la determinación puede estar desincronizada, así que hacemos un refresh
            if (em.contains(det)) {
                em.refresh(det);
            }
        }

        return det;
    }

    /**
     *
     * @param tramite
     * @param entidad
     * @return
     * @throws ExcepcionPersistencia
     */
    private es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad obtenerEntidad(
            String tramite, String entidad) throws ExcepcionPersistencia {

        String id = tramite + entidad;
        es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad ent;
        ent = entsRpm.get(id);
        if (ent == null) {
            log.debug("No se ha encontrado Entidad "
                    + entidad + " del tramite "
                    + tramite + " en memoria, buscando en base de datos.");

            try {
                ent = (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad) em.createNamedQuery("Entidad.obtenerEntidadPorTramiteYCodigoEntidad").setParameter("codigoTramite", tramite).setParameter("codigoEntidad", entidad).getSingleResult();

                entsRpm.put(id, ent);
            } catch (NoResultException nre) {
                liberarCache();
                throw new ExcepcionPersistencia("No se ha encontrado entidad con código "
                        + entidad + " en tramite " + tramite);
            } catch (NonUniqueResultException nure) {
                liberarCache();
                throw new ExcepcionPersistencia("Se han encontrado varias entidades con código "
                        + entidad + " en tramite " + tramite);
            }
        }

        return ent;
    }

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.servicios.dal.GestorFipLocal#removeListener(es.mitc.redes.urbanismoenred.servicios.dal.GestorFipListener)
     */
    @Override
    public void removeListener(GestorFipListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
}
