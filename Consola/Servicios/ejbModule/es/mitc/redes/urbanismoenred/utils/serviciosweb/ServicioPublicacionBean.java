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
package es.mitc.redes.urbanismoenred.utils.serviciosweb;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Organigramaambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.explotacion.Capa;
import es.mitc.redes.urbanismoenred.data.rpm.explotacion.Peticion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Session Bean implementation class ServicioPlaneamientoBean
 */
@Stateless(name = "ServicioPublicacion")
public class ServicioPublicacionBean implements ServicioPublicacionLocal {

    @PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
    @EJB
    private ServicioPlaneamientoLocal servicioPlaneamiento;
    @EJB
    private ServicioDiccionariosLocal servicioDiccionario;

    public Entidad[] findEntidadesFromGeo(String geom, Integer idTramite) throws ExcepcionPublicacion {
        List<Entidad> aEntidades = new ArrayList<Entidad>();
        Query aQuery;
        List aResultado;
        Entidad aEntidad;
        try {

            aQuery = em.createNamedQuery("Entidad.buscarFromGeom").setParameter("idTramite", idTramite).setParameter("WKT", geom);
            aResultado = aQuery.getResultList();

            for (Object Iden : aResultado) {
                aEntidad = servicioPlaneamiento.get(Entidad.class, Iden);
                aEntidades.add(aEntidad);
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a entidades del refundido", ex);
        }

        return aEntidades.toArray(new Entidad[0]);
    }

    public Entidad[] findEntidadesRefundidoFromGeo(String geom, Integer idAmbito) throws ExcepcionPublicacion {
        List<Entidad> aEntidades = new ArrayList<Entidad>();
        Query aQuery;
        List aResultado;
        Entidad aEntidad;
        try {

            aQuery = em.createNamedQuery("Entidad.buscarRefundidoFromGeom").setParameter("idAmbito", idAmbito).setParameter("WKT", geom).setParameter("IdTipoTramiteRefundido", Integer.valueOf(Textos.getTexto("diccionario", "tipotramite.refundidoautomatico")));
            aResultado = aQuery.getResultList();

            for (Object Iden : aResultado) {
                aEntidad = servicioPlaneamiento.get(Entidad.class, Iden);
                aEntidades.add(aEntidad);
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a entidades del refundido", ex);
        }

        return aEntidades.toArray(new Entidad[0]);
    }

    public Plan[] findPlanesGeom(String geom, Integer idAmbito, boolean soloRaiz) throws ExcepcionPublicacion {
        List<Plan> aPlanes = new ArrayList<Plan>();
        Query aQuery;
        List aResultado;
        Plan aPlan;
        try {
            aQuery = em.createNamedQuery("Plan.buscarFromGeom").setParameter("idAmbito", idAmbito).setParameter("WKT", geom).setParameter("IdTipoTramiteRefundido", Integer.valueOf(Textos.getTexto("diccionario", "tipotramite.refundidoautomatico")));
            aResultado = aQuery.getResultList();

            for (Object Iden : aResultado) {
                aPlan = servicioPlaneamiento.get(Plan.class, Iden);
                if (soloRaiz) {
                    if (aPlan.getPlanByIdpadre() == null) {
                        aPlanes.add(aPlan);
                    }
                } else {
                    aPlanes.add(aPlan);
                }
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a planes desde geometria", ex);
        }
        return aPlanes.toArray(new Plan[0]);
    }

    @Override
    public Tramite[] findTramitesGeom(String geom, List<String> codigosFiltro) throws ExcepcionPublicacion {
        List<Tramite> aTramites = new ArrayList<Tramite>();
        Query aQuery;
        List aResultado;
        Tramite aTramite;
        try {
            aQuery = em.createNamedQuery("Tramite.buscarFromGeomCodigos").setParameter("WKT", geom).setParameter("codigos", codigosFiltro);
            aResultado = aQuery.getResultList();

            for (Object Iden : aResultado) {
                aTramite = servicioPlaneamiento.get(Tramite.class, Iden);
                aTramites.add(aTramite);
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a tramites desde geometria", ex);
        }
        return aTramites.toArray(new Tramite[0]);
    }

    @Override
    public Tramite[] findTramitesGeom(String geom) throws ExcepcionPublicacion {
        List<Tramite> aTramites = new ArrayList<Tramite>();
        Query aQuery;
        List aResultado;
        Tramite aTramite;
        try {
            aQuery = em.createNamedQuery("Tramite.buscarFromGeom").setParameter("WKT", geom);
            aResultado = aQuery.getResultList();

            for (Object Iden : aResultado) {
                aTramite = servicioPlaneamiento.get(Tramite.class, Iden);
                aTramites.add(aTramite);
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a tramites desde geometria", ex);
        }
        return aTramites.toArray(new Tramite[0]);
    }

    public Tramite findUltimoTramiteConsolidado(Integer idPlan) throws ExcepcionPublicacion {
        Query aQuery;
        Tramite aResultado = null;
        try {
            aQuery = em.createNamedQuery("Tramite.buscarUltimoConsolidadoFromPlan").setParameter("idPlan", idPlan);
            aResultado = (Tramite) aQuery.getSingleResult();
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo al ultimo tramite consosolidado", ex);
        }
        return aResultado;
    }

    public void findSuperficiesRefundido(String geom, Integer idAmbito, List<Entidad> entidades, List<Double> superficies, List<Double> superficiesIntersectadas) throws ExcepcionPublicacion {
        Query aQuery;
        List aResultado;
        Entidad aEntidad;
        try {
            aQuery = em.createNamedQuery("Entidad.buscarSuperficiesRefundidoFromGeom").setParameter("idAmbito", idAmbito).setParameter("WKT", geom).setParameter("IdTipoTramiteRefundido", Integer.valueOf(Textos.getTexto("diccionario", "tipotramite.refundidoautomatico")));
            aResultado = aQuery.getResultList();

            for (Object obj : aResultado) {
                Object[] objs = (Object[]) obj;
                aEntidad = servicioPlaneamiento.get(Entidad.class, Integer.valueOf(objs[0].toString()));
                entidades.add(aEntidad);
                superficies.add(Double.valueOf(objs[1].toString()));
                superficiesIntersectadas.add(Double.valueOf(objs[2].toString()));
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a planes desde geometria", ex);
        }

    }

    public void findSuperficies(String geom, String codigoFIP, List<Entidad> entidades, List<Double> superficies, List<Double> superficiesIntersectadas) throws ExcepcionPublicacion {
        Query aQuery;
        List aResultado;
        Entidad aEntidad;
        try {
            aQuery = em.createNamedQuery("Entidad.buscarSuperficiesFromGeom").setParameter("codigoTramite", codigoFIP).setParameter("WKT", geom);
            aResultado = aQuery.getResultList();
            for (Object obj : aResultado) {
                Object[] objs = (Object[]) obj;
                aEntidad = servicioPlaneamiento.get(Entidad.class, Integer.valueOf(objs[0].toString()));
                entidades.add(aEntidad);
                superficies.add(Double.valueOf(objs[1].toString()));
                superficiesIntersectadas.add(Double.valueOf(objs[2].toString()));
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a planes desde geometria", ex);
        }

    }

    public boolean saveGeomPeticion(Peticion peticion) throws ExcepcionPublicacion {
        try {
            em.persist(peticion);
            return true;
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error guardando peticion", ex);
        }
    }

    public Ambito[] findAmbitosFromGeo(String geom) throws ExcepcionPublicacion {
        List<Ambito> aAmbitos = new ArrayList<Ambito>();
        Query aQuery;
        List aResultado;
        Ambito aAmbito;
        try {
            aQuery = em.createNamedQuery("Ambito.buscarFromGeom").setParameter("WKT", geom);
            aResultado = aQuery.getResultList();

            for (Object Iden : aResultado) {
                aAmbito = servicioPlaneamiento.get(Ambito.class, Iden);
                aAmbitos.add(aAmbito);
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a ambitos desde geometria", ex);
        }
        return aAmbitos.toArray(new Ambito[0]);
    }

    public Capa[] findCapaFromCodigoGrupo(String codigo) throws ExcepcionPublicacion {
        List<Capa> aCapas = new ArrayList<Capa>();
        Query aQuery;

        try {
            aQuery = em.createNamedQuery("Capa.findFromCodigoGrupo").setParameter("codigoDeterminacionGrupo", codigo);
            aCapas = (List<Capa>) aQuery.getResultList();

        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a capas desde codigo de grupo", ex);
        }
        return aCapas.toArray(new Capa[0]);
    }

    public Ambito[] findAmbitosPadres() throws ExcepcionPublicacion {
        Query aQuery;
        List<Ambito> aResultado = null;
        try {
            aQuery = em.createNamedQuery("Ambito.buscarPadres");
            aResultado = (List<Ambito>) aQuery.getResultList();
            return aResultado.toArray(new Ambito[0]);
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo al ultimo tramite consooidado", ex);
        }
    }

    public Ambito[] findAmbitosWithPlan() throws ExcepcionPublicacion {
        Query aQuery;
        List<Ambito> aResultado = null;
        try {
            aQuery = em.createNamedQuery("Ambito.buscarWithPlan");
            aResultado = (List<Ambito>) aQuery.getResultList();
            return aResultado.toArray(new Ambito[0]);
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a los ambitos con plan", ex);
        }
    }

    public Ambito[] findAmbitosHijosWithPlan(Integer idPadre) throws ExcepcionPublicacion {
        Query aQuery;
        List<Ambito> aResultado = null;
        try {
            aQuery = em.createNamedQuery("Ambito.buscarHijosWithPlan").setParameter("idPadre", idPadre);
            aResultado = (List<Ambito>) aQuery.getResultList();
            return aResultado.toArray(new Ambito[0]);
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a los ambitos hijos con plan", ex);
        }
    }

    public Ambito[] findAmbitosHijos(Integer idPadre) throws ExcepcionPublicacion {
        Ambito amb = servicioDiccionario.get(Ambito.class, idPadre);
        List<Ambito> ret = new ArrayList<Ambito>();
        Set<Organigramaambito> orgs = amb.getOrganigramaambitosForIdambitopadre();
        for (Organigramaambito org : orgs) {
            ret.add(org.getAmbitoByIdambitohijo());
        }
        return ret.toArray(new Ambito[0]);
    }

    public Entidad[] findEntidadesLikeName_Plan(String nombreEntidad, String nombrePlan, Integer idAmbito) throws ExcepcionPublicacion {
        Query aQuery;
        List aResultado = null;
        Entidad aEntidad;
        List<Entidad> res = new ArrayList<Entidad>();
        try {
            aQuery = em.createNamedQuery("Entidad.buscarLikeNameAndPlan").setParameter("nombrePlan", "%" + nombrePlan + "%").setParameter("nombreEntidad", "%" + nombreEntidad + "%").setParameter("idAmbito", idAmbito).setParameter("IdTipoTramiteRefundido", Integer.valueOf(Textos.getTexto("diccionario", "tipotramite.refundidoautomatico")));
            aResultado = aQuery.getResultList();
            for (Object Iden : aResultado) {
                aEntidad = servicioPlaneamiento.get(Entidad.class, Iden);
                res.add(aEntidad);
            }
            return res.toArray(new Entidad[0]);
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a las entidades from nombre y plan", ex);
        }
    }

    public Entidad[] findEntidadesLikeName(String nombreEntidad, Integer idAmbito) throws ExcepcionPublicacion {
        Query aQuery;
        List aResultado = null;
        Entidad aEntidad;
        List<Entidad> res = new ArrayList<Entidad>();
        try {
            aQuery = em.createNamedQuery("Entidad.buscarLikeName").setParameter("nombreEntidad", "%" + nombreEntidad + "%").setParameter("idAmbito", idAmbito).setParameter("IdTipoTramiteRefundido", Integer.valueOf(Textos.getTexto("diccionario", "tipotramite.refundidoautomatico")));
            aResultado = aQuery.getResultList();
            for (Object Iden : aResultado) {
                aEntidad = servicioPlaneamiento.get(Entidad.class, Iden);
                res.add(aEntidad);
            }
            return res.toArray(new Entidad[0]);
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a las entidades from nombre", ex);
        }
    }

    public Plan[] findPlanesLikeName(String nombrePlan, Integer idAmbito) throws ExcepcionPublicacion {
        Query aQuery;
        List<Plan> res = new ArrayList<Plan>();
        List aResultado = null;
        Plan aPlan;
        try {
            aQuery = em.createNamedQuery("Plan.buscarLikeName").setParameter("nombrePlan", "%" + nombrePlan + "%").setParameter("idAmbito", idAmbito).setParameter("IdTipoTramiteRefundido", Integer.valueOf(Textos.getTexto("diccionario", "tipotramite.refundidoautomatico")));
            aResultado = aQuery.getResultList();
            for (Object Iden : aResultado) {
                aPlan = servicioPlaneamiento.get(Plan.class, Iden);
                res.add(aPlan);
            }
            return res.toArray(new Plan[0]);
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a planes from nombre", ex);
        }
    }

    public boolean isPlanRefundido(Integer idPlan) throws ExcepcionPublicacion {

        Query query = em.createNamedQuery("Plan.isRefundido").setParameter("idPlan", idPlan).setParameter("idIntrumentoPlanRefundido", Integer.valueOf(Textos.getTexto("consola", "idIntrumentoPlanRefundido")));


        List ret = query.getResultList();
        if (ret == null || ret.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public Tramite findUltimoTramiteRefundido(Integer idAmbito) throws ExcepcionPublicacion {
        Query query = em.createNamedQuery("Tramite.findUltimoTramiteRefundido").setParameter("idAmbito", idAmbito).setParameter("IdTipoTramiteRefundido", Integer.valueOf(Textos.getTexto("diccionario", "tipotramite.refundidoautomatico")));
        List aTramites = query.getResultList();
        if (aTramites.size() > 0) {
            Tramite tramite = (Tramite) aTramites.get(0);
            return tramite;
        } else {
            return null;
        }
    }

    public Entidad[] findEntidadesLikeClave(String claveEntidad, Integer idAmbito) throws ExcepcionPublicacion {
        Query aQuery;
        List aResultado = null;
        Entidad aEntidad;
        List<Entidad> res = new ArrayList<Entidad>();
        try {
            aQuery = em.createNamedQuery("Entidad.buscarLikeClave").setParameter("claveEntidad", "%" + claveEntidad + "%").setParameter("idAmbito", idAmbito).setParameter("IdTipoTramiteRefundido", Integer.valueOf(Textos.getTexto("diccionario", "tipotramite.refundidoautomatico")));
            aResultado = aQuery.getResultList();
            for (Object Iden : aResultado) {
                aEntidad = servicioPlaneamiento.get(Entidad.class, Iden);
                res.add(aEntidad);
            }
            return res.toArray(new Entidad[0]);
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a las entidades from clave", ex);
        }
    }

    public Documentoshp[] findPlanosRefundidoFromGeo(String geom, Integer idAmbito) throws ExcepcionPublicacion {
        List<Documentoshp> hojas = new ArrayList<Documentoshp>();
        Query aQuery;
        List aResultado;
        Documentoshp hoja;
        try {
            aQuery = em.createNamedQuery("Documentoshp.buscarFromGeom").setParameter("WKT", geom).setParameter("idAmbito", idAmbito).setParameter("IdTipoTramiteRefundido", Integer.valueOf(Textos.getTexto("diccionario", "tipotramite.refundidoautomatico")));
            aResultado = aQuery.getResultList();

            for (Object Iden : aResultado) {
                hoja = servicioPlaneamiento.get(Documentoshp.class, Iden);
                hojas.add(hoja);
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a hojas", ex);
        }
        return hojas.toArray(new Documentoshp[0]);
    }

    public Documentoshp[] findPlanosFromGeoTramite(String geom, Integer idTramite) throws ExcepcionPublicacion {
        List<Documentoshp> hojas = new ArrayList<Documentoshp>();
        Query aQuery;
        List aResultado;
        Documentoshp hoja;
        try {
            aQuery = em.createNamedQuery("Documentoshp.buscarFromGeomTramite").setParameter("WKT", geom).setParameter("idTramite", idTramite);
            aResultado = aQuery.getResultList();

            for (Object Iden : aResultado) {
                hoja = servicioPlaneamiento.get(Documentoshp.class, Iden);
                hojas.add(hoja);
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a hojas", ex);
        }
        return hojas.toArray(new Documentoshp[0]);
    }

    public Plan[] findPlanesHijosGeom(String geom, Integer idPadre) throws ExcepcionPublicacion {
        List<Plan> aPlanes = new ArrayList<Plan>();
        Query aQuery;
        List aResultado;
        Plan aPlan;
        try {
            aQuery = em.createNamedQuery("Plan.buscarHijosFromGeom").setParameter("idPadre", idPadre).setParameter("WKT", geom).setParameter("IdTipoTramiteRefundido", Integer.valueOf(Textos.getTexto("diccionario", "tipotramite.refundidoautomatico")));
            aResultado = aQuery.getResultList();

            for (Object Iden : aResultado) {
                aPlan = servicioPlaneamiento.get(Plan.class, Iden);
                aPlanes.add(aPlan);
            }
        } catch (Exception ex) {
            throw new ExcepcionPublicacion("Error accediendo a planes hijos desde geometria", ex);
        }
        return aPlanes.toArray(new Plan[0]);
    }

    public Plan[] findPlanesRaiz(Integer idAmbito) throws ExcepcionPublicacion {
        Plan[] planes = servicioPlaneamiento.getPlanesRaiz(idAmbito, ModalidadPlanes.RPM);
        List<Plan> ret = new ArrayList();
        for (Plan plan : planes) {
            if (!this.isPlanRefundido(plan.getIden())) {
                ret.add(plan);
            }
        }
        return ret.toArray(new Plan[0]);
    }

    public Plan[] findPlanesHijos(Integer idPadre) throws ExcepcionPublicacion {
        Plan[] planes = servicioPlaneamiento.getPlanesHijos(idPadre, null, ModalidadPlanes.RPM);
        List<Plan> ret = new ArrayList();
        for (Plan plan : planes) {
            if (!this.isPlanRefundido(plan.getIden())) {
                ret.add(plan);
            }
        }
        return ret.toArray(new Plan[0]);
    }

    public String getNombreTramiteCompleto(String codigoTramite, String idioma) throws ExcepcionPublicacion {
        Tramite tramite = servicioPlaneamiento.getTramitePorCodigo(codigoTramite);
        if (tramite != null) {
            Plan plan = servicioPlaneamiento.get(Plan.class, tramite.getPlan().getIden());
            return plan.getNombre() + " - "
                    + servicioDiccionario.getTraduccion(Tipotramite.class, tramite.getIdtipotramite(), idioma) + " " + String.valueOf(tramite.getIteracion())
                    + " (" + servicioDiccionario.getTraduccion(Ambito.class, plan.getIdambito(), idioma) + ")";

        } else {
            return null;
        }
    }
}
