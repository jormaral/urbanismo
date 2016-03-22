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
package es.mitc.redes.urbanismoenred.servicios.ficha;

import es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntodeterminaciongrupo;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntogrupo;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Determinacionclasifacto;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Determinacionclasifuso;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.ficha.Ficha;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Fichaconjuntogrupo;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Fichagrupodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Grupodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Grupodeterminaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Regimenesacto;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Regimenesuso;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Valoresclasifacto;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Valoresclasifuso;
import javax.persistence.Query;

/**
 * Session Bean implementation class ServicioPlaneamientoBean
 */
@Stateless(name = "ServicioFicha")
public class ServicioFichaBean implements ServicioFichaLocal {

    @PersistenceContext(unitName = "rpmv2")
    private EntityManager em;

    @Override
    public <T> T get(Class<T> clase, Object id) {
        return em.find(clase, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Ficha[] getFichaPorTramite(int idTramite) {
        String consulta = "Ficha.buscaPorTramite";
        return ((List<Ficha>) em.createNamedQuery(consulta).setParameter("idTramite", (long) idTramite).getResultList()).toArray(new Ficha[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Conjuntogrupo[] getGruposFromFicha(int idFicha) {
        String consulta = "Conjuntogrupo.buscaPorFicha";
        return ((List<Conjuntogrupo>) em.createNamedQuery(consulta).setParameter("idFicha", (long) idFicha).getResultList()).toArray(new Conjuntogrupo[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Conjuntodeterminaciongrupo[] getGruposFromConjunto(int idConjunto) {
        String consulta = "Conjuntodeterminaciongrupo.buscaPorConjunto";
        return ((List<Conjuntodeterminaciongrupo>) em.createNamedQuery(consulta).setParameter("idConjunto", (long) idConjunto).getResultList()).toArray(new Conjuntodeterminaciongrupo[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Grupodeterminacion[] getGruposDeterminacionFromFichaConjunto(int idFicha, int idConjunto) {
        String consulta = "Grupodeterminacion.buscaPorFichaConjunto";
        Query query = em.createNamedQuery(consulta);
        query.setParameter("idFicha", (long) idFicha);
        query.setParameter("idConjunto", (long) idConjunto);
        return ((List<Grupodeterminacion>) query.getResultList()).toArray(new Grupodeterminacion[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Grupodeterminaciondeterminacion[] getDeterminacionFromGrupoDeterminacion(int idGrupoDet) {
        String consulta = "Grupodeterminaciondeterminacion.buscaPorGrupoDeterminacion";
        Query query = em.createNamedQuery(consulta);
        query.setParameter("idGrupoDet", (long) idGrupoDet);
        return ((List<Grupodeterminaciondeterminacion>) query.getResultList()).toArray(new Grupodeterminaciondeterminacion[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Determinacionclasifuso[] getDeterminacionesClasificatoriasUso(int idFicha) {
        String consulta = "Determinacionclasifuso.buscaDeterminacionClasifPorFicha";
        Query query = em.createNamedQuery(consulta);
        query.setParameter("idFicha", (long) idFicha);
        return ((List<Determinacionclasifuso>) query.getResultList()).toArray(new Determinacionclasifuso[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Determinacionclasifacto[] getDeterminacionesClasificatoriasActo(int idFicha) {
        String consulta = "Determinacionclasifacto.buscaDeterminacionClasifPorFicha";
        Query query = em.createNamedQuery(consulta);
        query.setParameter("idFicha", (long) idFicha);
        return ((List<Determinacionclasifacto>) query.getResultList()).toArray(new Determinacionclasifacto[0]);
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#down(java.lang.Class, java.lang.Object)
     */
    @Override
    public Boolean down(Class<?> clase, Object identificador) throws ExcepcionFicha {
        try {
            if (clase == Conjuntogrupo.class) {
                Conjuntogrupo con = this.get(Conjuntogrupo.class, identificador);
                Fichaconjuntogrupo fcg = con.getFichaconjuntogrupos().iterator().next();
                Conjuntogrupo[] conjuntos = this.getGruposFromFicha((int) fcg.getFicha().getIden());
                int idxSel = java.util.Arrays.asList(conjuntos).indexOf(con);
                if (idxSel < conjuntos.length - 1) {
                    Conjuntogrupo conjuntoInf = conjuntos[idxSel + 1];
                    Fichaconjuntogrupo fcgInf = conjuntoInf.getFichaconjuntogrupos().iterator().next();
                    fcg.setOrden(fcgInf.getOrden());
                    fcgInf.setOrden(fcgInf.getOrden() - 1);
                    em.persist(fcg);
                    em.persist(fcgInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Conjuntodeterminaciongrupo.class) {
                Conjuntodeterminaciongrupo con = this.get(Conjuntodeterminaciongrupo.class, identificador);
                Conjuntodeterminaciongrupo[] conjuntos = this.getGruposFromConjunto((int) con.getConjuntogrupo().getIden());
                int idxSel = java.util.Arrays.asList(conjuntos).indexOf(con);
                if (idxSel < conjuntos.length - 1) {
                    Conjuntodeterminaciongrupo conjuntoInf = conjuntos[idxSel + 1];
                    con.setOrden(conjuntoInf.getOrden());
                    conjuntoInf.setOrden(conjuntoInf.getOrden() - 1);
                    em.persist(con);
                    em.persist(conjuntoInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Grupodeterminacion.class) {
                Grupodeterminacion grp = this.get(Grupodeterminacion.class, identificador);
                Fichagrupodeterminacion fgd = grp.getFichagrupodeterminacions().iterator().next();
                Grupodeterminacion[] grupos = this.getGruposDeterminacionFromFichaConjunto((int) fgd.getFicha().getIden(), (int) grp.getConjuntogrupo().getIden());
                int idxSel = java.util.Arrays.asList(grupos).indexOf(grp);
                if (idxSel < grupos.length - 1) {
                    Grupodeterminacion grpInf = grupos[idxSel + 1];
                    Fichagrupodeterminacion fgdInf = grpInf.getFichagrupodeterminacions().iterator().next();
                    fgd.setOrden(fgdInf.getOrden());
                    fgdInf.setOrden(fgdInf.getOrden() - 1);
                    em.persist(fgd);
                    em.persist(fgdInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Grupodeterminaciondeterminacion.class) {
                Grupodeterminaciondeterminacion grp = this.get(Grupodeterminaciondeterminacion.class, identificador);
                Grupodeterminaciondeterminacion[] grupos = this.getDeterminacionFromGrupoDeterminacion((int) grp.getGrupodeterminacion().getIden());
                int idxSel = java.util.Arrays.asList(grupos).indexOf(grp);
                if (idxSel < grupos.length - 1) {
                    Grupodeterminaciondeterminacion grupoInf = grupos[idxSel + 1];
                    grp.setOrden(grupoInf.getOrden());
                    grupoInf.setOrden(grupoInf.getOrden() - 1);
                    em.persist(grp);
                    em.persist(grupoInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Valoresclasifuso.class) {
                Valoresclasifuso val = this.get(Valoresclasifuso.class, identificador);
                Valoresclasifuso[] valores = this.getValoresFromDetClasifUso((int) val.getDeterminacionclasifuso().getFicha().getIden(), (int) val.getDeterminacionclasifuso().getIddeterminacion());
                int idxSel = java.util.Arrays.asList(valores).indexOf(val);
                if (idxSel < valores.length - 1) {
                    Valoresclasifuso valInf = valores[idxSel + 1];
                    val.setOrden(valInf.getOrden());
                    valInf.setOrden(valInf.getOrden() - 1);
                    em.persist(val);
                    em.persist(valInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Valoresclasifacto.class) {
                Valoresclasifacto val = this.get(Valoresclasifacto.class, identificador);
                Valoresclasifacto[] valores = this.getValoresFromDetClasifActo((int) val.getDeterminacionclasifacto().getFicha().getIden(), (int) val.getDeterminacionclasifacto().getIddeterminacion());
                int idxSel = java.util.Arrays.asList(valores).indexOf(val);
                if (idxSel < valores.length - 1) {
                    Valoresclasifacto valInf = valores[idxSel + 1];
                    val.setOrden(valInf.getOrden());
                    valInf.setOrden(valInf.getOrden() - 1);
                    em.persist(val);
                    em.persist(valInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Regimenesacto.class) {
                Regimenesacto reg = this.get(Regimenesacto.class, identificador);
                Regimenesacto[] regimenes = this.getDeterminacionesRegimenActo((int) reg.getFicha().getIden());
                int idxSel = java.util.Arrays.asList(regimenes).indexOf(reg);
                if (idxSel < regimenes.length - 1) {
                    Regimenesacto regInf = regimenes[idxSel + 1];
                    reg.setOrden(regInf.getOrden());
                    regInf.setOrden(regInf.getOrden() - 1);
                    em.persist(reg);
                    em.persist(regInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Regimenesuso.class) {
                Regimenesuso reg = this.get(Regimenesuso.class, identificador);
                Regimenesuso[] regimenes = this.getDeterminacionesRegimenUso((int) reg.getFicha().getIden());
                int idxSel = java.util.Arrays.asList(regimenes).indexOf(reg);
                if (idxSel < regimenes.length - 1) {
                    Regimenesuso regInf = regimenes[idxSel + 1];
                    reg.setOrden(regInf.getOrden());
                    regInf.setOrden(regInf.getOrden() - 1);
                    em.persist(reg);
                    em.persist(regInf);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#up(java.lang.Class, java.lang.Object)
     */
    @Override
    public Boolean up(Class<?> clase, Object identificador) throws ExcepcionFicha {
        try {
            if (clase == Conjuntogrupo.class) {
                Conjuntogrupo con = this.get(Conjuntogrupo.class, identificador);
                Fichaconjuntogrupo fcg = con.getFichaconjuntogrupos().iterator().next();
                Conjuntogrupo[] conjuntos = this.getGruposFromFicha((int) fcg.getFicha().getIden());
                int idxSel = java.util.Arrays.asList(conjuntos).indexOf(con);
                if (idxSel > 0) {
                    Conjuntogrupo conjuntoInf = conjuntos[idxSel - 1];
                    Fichaconjuntogrupo fcgInf = conjuntoInf.getFichaconjuntogrupos().iterator().next();
                    fcg.setOrden(fcgInf.getOrden());
                    fcgInf.setOrden(fcgInf.getOrden() + 1);
                    em.persist(fcg);
                    em.persist(fcgInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Conjuntodeterminaciongrupo.class) {
                Conjuntodeterminaciongrupo con = this.get(Conjuntodeterminaciongrupo.class, identificador);
                Conjuntodeterminaciongrupo[] conjuntos = this.getGruposFromConjunto((int) con.getConjuntogrupo().getIden());
                int idxSel = java.util.Arrays.asList(conjuntos).indexOf(con);
                if (idxSel > 0) {
                    Conjuntodeterminaciongrupo conjuntoInf = conjuntos[idxSel - 1];
                    con.setOrden(conjuntoInf.getOrden());
                    conjuntoInf.setOrden(conjuntoInf.getOrden() + 1);
                    em.persist(con);
                    em.persist(conjuntoInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Grupodeterminacion.class) {
                Grupodeterminacion grp = this.get(Grupodeterminacion.class, identificador);
                Fichagrupodeterminacion fgd = grp.getFichagrupodeterminacions().iterator().next();
                Grupodeterminacion[] grupos = this.getGruposDeterminacionFromFichaConjunto((int) fgd.getFicha().getIden(), (int) grp.getConjuntogrupo().getIden());
                int idxSel = java.util.Arrays.asList(grupos).indexOf(grp);
                if (idxSel > 0) {
                    Grupodeterminacion grpInf = grupos[idxSel - 1];
                    Fichagrupodeterminacion fgdInf = grpInf.getFichagrupodeterminacions().iterator().next();
                    fgd.setOrden(fgdInf.getOrden());
                    fgdInf.setOrden(fgdInf.getOrden() + 1);
                    em.persist(fgd);
                    em.persist(fgdInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Grupodeterminaciondeterminacion.class) {
                Grupodeterminaciondeterminacion grp = this.get(Grupodeterminaciondeterminacion.class, identificador);
                Grupodeterminaciondeterminacion[] grupos = this.getDeterminacionFromGrupoDeterminacion((int) grp.getGrupodeterminacion().getIden());
                int idxSel = java.util.Arrays.asList(grupos).indexOf(grp);
                if (idxSel > 0) {
                    Grupodeterminaciondeterminacion grupoInf = grupos[idxSel - 1];
                    grp.setOrden(grupoInf.getOrden());
                    grupoInf.setOrden(grupoInf.getOrden() + 1);
                    em.persist(grp);
                    em.persist(grupoInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Valoresclasifuso.class) {
                Valoresclasifuso val = this.get(Valoresclasifuso.class, identificador);
                Valoresclasifuso[] valores = this.getValoresFromDetClasifUso((int) val.getDeterminacionclasifuso().getFicha().getIden(), (int) val.getDeterminacionclasifuso().getIddeterminacion());
                int idxSel = java.util.Arrays.asList(valores).indexOf(val);
                if (idxSel > 0) {
                    Valoresclasifuso valInf = valores[idxSel - 1];
                    val.setOrden(valInf.getOrden());
                    valInf.setOrden(valInf.getOrden() + 1);
                    em.persist(val);
                    em.persist(valInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Valoresclasifacto.class) {
                Valoresclasifacto val = this.get(Valoresclasifacto.class, identificador);
                Valoresclasifacto[] valores = this.getValoresFromDetClasifActo((int) val.getDeterminacionclasifacto().getFicha().getIden(), (int) val.getDeterminacionclasifacto().getIddeterminacion());
                int idxSel = java.util.Arrays.asList(valores).indexOf(val);
                if (idxSel > 0) {
                    Valoresclasifacto valInf = valores[idxSel - 1];
                    val.setOrden(valInf.getOrden());
                    valInf.setOrden(valInf.getOrden() + 1);
                    em.persist(val);
                    em.persist(valInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Regimenesacto.class) {
                Regimenesacto reg = this.get(Regimenesacto.class, identificador);
                Regimenesacto[] regimenes = this.getDeterminacionesRegimenActo((int) reg.getFicha().getIden());
                int idxSel = java.util.Arrays.asList(regimenes).indexOf(reg);
                if (idxSel  > 0) {
                    Regimenesacto regInf = regimenes[idxSel - 1];
                    reg.setOrden(regInf.getOrden());
                    regInf.setOrden(regInf.getOrden() + 1);
                    em.persist(reg);
                    em.persist(regInf);
                    return true;
                } else {
                    return false;
                }
            } else if (clase == Regimenesuso.class) {
                Regimenesuso reg = this.get(Regimenesuso.class, identificador);
                Regimenesuso[] regimenes = this.getDeterminacionesRegimenUso((int) reg.getFicha().getIden());
                int idxSel = java.util.Arrays.asList(regimenes).indexOf(reg);
                if (idxSel  > 0) {
                    Regimenesuso regInf = regimenes[idxSel - 1];
                    reg.setOrden(regInf.getOrden());
                    regInf.setOrden(regInf.getOrden() + 1);
                    em.persist(reg);
                    em.persist(regInf);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Valoresclasifacto[] getValoresFromDetClasifActo(int idFicha, int idDeterminacion) {
        String consulta = "Valoresclasifacto.buscaDeterminacionPorFichaDetClasif";
        Query query = em.createNamedQuery(consulta);
        query.setParameter("idFicha", (long) idFicha);
        query.setParameter("idDeterminacion", (long) idDeterminacion);
        return ((List<Valoresclasifacto>) query.getResultList()).toArray(new Valoresclasifacto[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Valoresclasifuso[] getValoresFromDetClasifUso(int idFicha, int idDeterminacion) {
        String consulta = "Valoresclasifuso.buscaDeterminacionPorFichaDetClasif";
        Query query = em.createNamedQuery(consulta);
        query.setParameter("idFicha", (long) idFicha);
        query.setParameter("idDeterminacion", (long) idDeterminacion);
        return ((List<Valoresclasifuso>) query.getResultList()).toArray(new Valoresclasifuso[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Regimenesuso[] getDeterminacionesRegimenUso(int idFicha) {
        String consulta = "Regimenesuso.buscaDeterminacionPorFicha";
        Query query = em.createNamedQuery(consulta);
        query.setParameter("idFicha", (long) idFicha);
        return ((List<Regimenesuso>) query.getResultList()).toArray(new Regimenesuso[0]);
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#getDeterminacionesRegimenActo(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Regimenesacto[] getDeterminacionesRegimenActo(int idFicha) {
        String consulta = "Regimenesacto.buscaDeterminacionPorFicha";
        Query query = em.createNamedQuery(consulta);
        query.setParameter("idFicha", (long) idFicha);
        return ((List<Regimenesacto>) query.getResultList()).toArray(new Regimenesacto[0]);
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delGrupo(long)
     */
    @Override
    public boolean delGrupo(long idConjuntoDeterminacionGrupo) throws ExcepcionFicha {

        if (idConjuntoDeterminacionGrupo == 0) {
            throw new ExcepcionFicha("Debe especificarse un grupo");
        }
        try {
            Conjuntodeterminaciongrupo grupoEdit = null;
            if (idConjuntoDeterminacionGrupo != 0) {
                grupoEdit = this.get(Conjuntodeterminaciongrupo.class, (long) idConjuntoDeterminacionGrupo);
            }
            if (grupoEdit != null) {
                em.remove(grupoEdit);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar el grupo. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delConjunto(long)
     */
    @Override
    public boolean delConjunto(long idConjunto) throws ExcepcionFicha {

        if (idConjunto == 0) {
            throw new ExcepcionFicha("Debe especificarse un conjunto");
        }
        try {
            Conjuntogrupo conjuntoEdit = null;
            if (idConjunto != 0) {
                conjuntoEdit = this.get(Conjuntogrupo.class, (long) idConjunto);
            }
            if (conjuntoEdit != null) {
                em.remove(conjuntoEdit);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar la capa. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delValorDetClasifUso(long)
     */
    @Override
    public boolean delValorDetClasifUso(long idValorDetClasif) throws ExcepcionFicha {

        if (idValorDetClasif == 0) {
            throw new ExcepcionFicha("Debe especificarse un valor");
        }
        try {
            Valoresclasifuso valorEdit = null;

            valorEdit = this.get(Valoresclasifuso.class, (long) idValorDetClasif);

            if (valorEdit != null) {
                em.remove(valorEdit);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar el valor. Consulte con su administrador.", e);
        }
    }

   /*
    * (non-Javadoc)
    * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delValorDetClasifActo(long)
    */
    @Override
    public boolean delValorDetClasifActo(long idValorDetClasif) throws ExcepcionFicha {

        if (idValorDetClasif == 0) {
            throw new ExcepcionFicha("Debe especificarse un valor");
        }
        try {
            Valoresclasifacto valorEdit = null;

            valorEdit = this.get(Valoresclasifacto.class, (long) idValorDetClasif);

            if (valorEdit != null) {
                em.remove(valorEdit);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar el valor. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delDeterminacionGrupo(long)
     */
    @Override
    public boolean delDeterminacionGrupo(long idGrupodeterminaciondeterminacion) throws ExcepcionFicha {

        if (idGrupodeterminaciondeterminacion == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinacion");
        }
        try {
            Grupodeterminaciondeterminacion detEdit = null;
            if (idGrupodeterminaciondeterminacion != 0) {
                detEdit = this.get(Grupodeterminaciondeterminacion.class, (long) idGrupodeterminaciondeterminacion);
            }
            if (detEdit != null) {
                em.remove(detEdit);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar la determinacion. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delDeterminacionClasificatoriaUso(long)
     */
    public boolean delDeterminacionClasificatoriaUso(long idDetClasif) throws ExcepcionFicha {

        if (idDetClasif == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinacion clasificatoria");
        }
        try {
            Determinacionclasifuso detClasif = null;
            if (idDetClasif != 0) {
                detClasif = this.get(Determinacionclasifuso.class, (long) idDetClasif);
            }
            if (detClasif != null) {
                em.remove(detClasif);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar la determinacion clasificatoria. Consulte con su administrador.", e);
        }
    }

   /*
    * (non-Javadoc)
    * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delDeterminacionClasificatoriaActo(long)
    */
    @Override
    public boolean delDeterminacionClasificatoriaActo(long idDetClasif) throws ExcepcionFicha {

        if (idDetClasif == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinacion clasificatoria");
        }
        try {
            Determinacionclasifacto detClasif = null;
            if (idDetClasif != 0) {
                detClasif = this.get(Determinacionclasifacto.class, (long) idDetClasif);
            }
            if (detClasif != null) {
                em.remove(detClasif);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar la determinacion clasificatoria. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delDeterminacionRegimenUso(long)
     */
    @Override
    public boolean delDeterminacionRegimenUso(long idRegimenUso) throws ExcepcionFicha {

        if (idRegimenUso == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinacion de regimen");
        }
        try {
            Regimenesuso regUso = null;
            if (idRegimenUso != 0) {
                regUso = this.get(Regimenesuso.class, (long) idRegimenUso);
            }
            if (regUso != null) {
                em.remove(regUso);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar determinacion de regimen de uso. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delDeterminacionRegimenActo(long)
     */
    @Override
    public boolean delDeterminacionRegimenActo(long idRegimenActo) throws ExcepcionFicha {

        if (idRegimenActo == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinacion de regimen");
        }
        try {
            Regimenesacto regActo = null;
            if (idRegimenActo != 0) {
                regActo = this.get(Regimenesacto.class, (long) idRegimenActo);
            }
            if (regActo != null) {
                em.remove(regActo);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar determinacion de regimen de acto. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delGrupoDeterminacion(long)
     */
    @Override
    public boolean delGrupoDeterminacion(long idGrupoDeterminacion) throws ExcepcionFicha {

        if (idGrupoDeterminacion == 0) {
            throw new ExcepcionFicha("Debe especificarse un grupo");
        }
        try {
            Grupodeterminacion grupoEdit = null;
            if (idGrupoDeterminacion != 0) {
                grupoEdit = this.get(Grupodeterminacion.class, (long) idGrupoDeterminacion);
            }
            if (grupoEdit != null) {
                em.remove(grupoEdit);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar el grupo de determinaciones. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#delFicha(long)
     */
    @Override
    public boolean delFicha(long idFicha) throws ExcepcionFicha {

        if (idFicha == 0) {
            throw new ExcepcionFicha("Debe especificarse una ficha");
        }
        try {
            Ficha fichaEdit = null;
            if (idFicha != 0) {
                fichaEdit = this.get(Ficha.class, (long) idFicha);
            }
            if (fichaEdit != null) {
                em.remove(fichaEdit);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al eliminar la ficha. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarFicha(int, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Ficha guardarFicha(int idTramite, int idFicha, String nombreFicha, String xPath, String ruta) throws ExcepcionFicha {

        if (idTramite == 0) {
            throw new ExcepcionFicha("Debe especificarse un trámite");
        }
        if (nombreFicha == null || nombreFicha.isEmpty()) {
            throw new ExcepcionFicha("Debe especificarse un nombre de ficha");
        }
        if (xPath == null || xPath.isEmpty()) {
            throw new ExcepcionFicha("Debe especificarse una expresión xPath");
        }
        if (ruta == null || ruta.isEmpty()) {
            throw new ExcepcionFicha("Debe especificarse una ruta de plantilla");
        }
        try {
            Ficha fichaEdit = null;
            if (idFicha != 0) {
                fichaEdit = this.get(Ficha.class, (long) idFicha);
            }
            if (fichaEdit == null) {
                fichaEdit = new Ficha();
            }
            fichaEdit.setIdtramite(idTramite);
            fichaEdit.setNombre(nombreFicha);
            fichaEdit.setPath(ruta);
            fichaEdit.setXpath(xPath);
            em.persist(fichaEdit);
            return fichaEdit;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar la ficha. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarGrupoDeterminacionDeterminacion(long, long, long, boolean)
     */
    @Override
    public Grupodeterminaciondeterminacion guardarGrupoDeterminacionDeterminacion(long idGrupoDeterminacion, long idDeterminacion, long orden, boolean regEsp) throws ExcepcionFicha {

        if (idGrupoDeterminacion == 0 && idGrupoDeterminacion == 0) {
            throw new ExcepcionFicha("Debe especificarse un grupo de determinaciones");
        }
        if (idDeterminacion == 0 && idDeterminacion == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinacion");
        }
        if (orden == 0) {
            throw new ExcepcionFicha("Debe especificarse un orden");
        }

        try {
            Grupodeterminaciondeterminacion grupo = new Grupodeterminaciondeterminacion();
            grupo.setGrupodeterminacion(this.get(Grupodeterminacion.class, idGrupoDeterminacion));
            grupo.setIddeterminacion(idDeterminacion);
            grupo.setOrden(orden);
            grupo.setRegimenesp(regEsp);
            em.persist(grupo);
            return grupo;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar la determinación. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarValorDetClasifUso(long, long, long)
     */
    @Override
    public Valoresclasifuso guardarValorDetClasifUso(long idDeterminacionClasif, long idDeterminacion, long orden) throws ExcepcionFicha {

        if (idDeterminacionClasif == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinacionclasificatoria");
        }
        if (idDeterminacion == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinacion");
        }
        if (orden == 0) {
            throw new ExcepcionFicha("Debe especificarse un orden");
        }

        try {
            Valoresclasifuso valor = new Valoresclasifuso();
            valor.setDeterminacionclasifuso(this.get(Determinacionclasifuso.class, idDeterminacionClasif));
            valor.setIddeterminacionvalorregimen(idDeterminacion);
            valor.setOrden(orden);

            em.persist(valor);
            return valor;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar el valor de determinacion clasificatoria de usos. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarValorDetClasifActo(long, long, long)
     */
    @Override
    public Valoresclasifacto guardarValorDetClasifActo(long idDeterminacionClasif, long idDeterminacion, long orden) throws ExcepcionFicha {

        if (idDeterminacionClasif == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinacionclasificatoria");
        }
        if (idDeterminacion == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinacion");
        }
        if (orden == 0) {
            throw new ExcepcionFicha("Debe especificarse un orden");
        }

        try {
            Valoresclasifacto valor = new Valoresclasifacto();
            valor.setDeterminacionclasifacto(this.get(Determinacionclasifacto.class, idDeterminacionClasif));
            valor.setIddeterminacionvalorregimen(idDeterminacion);
            valor.setOrden(orden);

            em.persist(valor);
            return valor;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar el valor de determinacion clasificatoria de actos. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarGrupo(long, long, long)
     */
    @Override
    public Conjuntodeterminaciongrupo guardarGrupo(long idConjunto, long idDeterminacionGrupo, long ordenGrupo) throws ExcepcionFicha {

        if (idConjunto == 0 && idConjunto == 0) {
            throw new ExcepcionFicha("Debe especificarse un conjunto de grupos");
        }
        if (idDeterminacionGrupo == 0 && idDeterminacionGrupo == 0) {
            throw new ExcepcionFicha("Debe especificarse un grupo de entidad");
        }
        if (ordenGrupo == 0) {
            throw new ExcepcionFicha("Debe especificarse un orden");
        }

        try {
            Conjuntodeterminaciongrupo grupo = new Conjuntodeterminaciongrupo();
            grupo.setConjuntogrupo(this.get(Conjuntogrupo.class, idConjunto));
            grupo.setIddeterminacion(idDeterminacionGrupo);
            grupo.setOrden(ordenGrupo);

            em.persist(grupo);
            return grupo;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar el conjunto. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarGrupoDeterminacion(long, long, long, java.lang.String, long)
     */
    @Override
    public Grupodeterminacion guardarGrupoDeterminacion(long idGrupoDeterminacion, long idFicha, long idConjunto, String nombreGrupo, long ordenGrupo) throws ExcepcionFicha {

        if (idGrupoDeterminacion == 0 && (idConjunto == 0 || idFicha == 0)) {
            throw new ExcepcionFicha("Debe especificarse un grupo de determinación o un conjunto de grupos y ficha");
        }
        if (nombreGrupo == null || nombreGrupo.isEmpty()) {
            throw new ExcepcionFicha("Debe especificarse un nombre de grupo de entidad");
        }
        if (ordenGrupo == 0) {
            throw new ExcepcionFicha("Debe especificarse un orden");
        }

        try {
            Grupodeterminacion grupo = null;
            Fichagrupodeterminacion fichaGrupo = null;
            Conjuntogrupo conjunto = null;
            if (idGrupoDeterminacion != 0) {
                grupo = this.get(Grupodeterminacion.class, idGrupoDeterminacion);
                fichaGrupo = grupo.getFichagrupodeterminacions().iterator().next();
            }
            if (grupo == null) {
                grupo = new Grupodeterminacion();
                fichaGrupo = new Fichagrupodeterminacion();
                fichaGrupo.setGrupodeterminacion(grupo);
                conjunto = this.get(Conjuntogrupo.class, idConjunto);
                fichaGrupo.setFicha(this.get(Ficha.class, idFicha));
                grupo.setConjuntogrupo(conjunto);
                grupo.getFichagrupodeterminacions().add(fichaGrupo);
            }

            fichaGrupo.setOrden(ordenGrupo);
            grupo.setNombre(nombreGrupo);
            em.persist(grupo);
            em.persist(fichaGrupo);
            return grupo;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar el grupo de determinaciones. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarRegimenUso(long, long, long)
     */
    @Override
    public Regimenesuso guardarRegimenUso(long idFicha, long idDeterminacion, long orden) throws ExcepcionFicha {

        if (idFicha == 0) {
            throw new ExcepcionFicha("Debe especificarse una ficha");
        }
        if (idDeterminacion == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinación");
        }
        if (orden == 0) {
            throw new ExcepcionFicha("Debe especificarse un orden");
        }
        try {

            Regimenesuso detReg = new Regimenesuso();
            detReg.setIddeterminacion(idDeterminacion);
            detReg.setFicha(this.get(Ficha.class, idFicha));
            detReg.setOrden(orden);
            em.persist(detReg);

            return detReg;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar la determinacion de régimen de uso. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarRegimenActo(long, long, long)
     */
    @Override
    public Regimenesacto guardarRegimenActo(long idFicha, long idDeterminacion, long orden) throws ExcepcionFicha {

        if (idFicha == 0) {
            throw new ExcepcionFicha("Debe especificarse una ficha");
        }
        if (idDeterminacion == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinación");
        }
        if (orden == 0) {
            throw new ExcepcionFicha("Debe especificarse un orden");
        }
        try {

            Regimenesacto detReg = new Regimenesacto();
            detReg.setIddeterminacion(idDeterminacion);
            detReg.setFicha(this.get(Ficha.class, idFicha));
            detReg.setOrden(orden);
            em.persist(detReg);

            return detReg;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar la determinacion de régimen de acto. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarDetClasifActo(long, long)
     */
    @Override
    public Determinacionclasifacto guardarDetClasifActo(long idFicha, long idDeterminacion) throws ExcepcionFicha {

        if (idFicha == 0) {
            throw new ExcepcionFicha("Debe especificarse una ficha");
        }
        if (idDeterminacion == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinación");
        }

        try {

            Determinacionclasifacto detClasif = new Determinacionclasifacto();
            detClasif.setIddeterminacion(idDeterminacion);
            detClasif.setFicha(this.get(Ficha.class, idFicha));

            em.persist(detClasif);

            return detClasif;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar la determinacion clasificatoria de actos. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarDetClasifUso(long, long)
     */
    @Override
    public Determinacionclasifuso guardarDetClasifUso(long idFicha, long idDeterminacion) throws ExcepcionFicha {

        if (idFicha == 0) {
            throw new ExcepcionFicha("Debe especificarse una ficha");
        }
        if (idDeterminacion == 0) {
            throw new ExcepcionFicha("Debe especificarse una determinación");
        }

        try {

            Determinacionclasifuso detClasif = new Determinacionclasifuso();
            detClasif.setIddeterminacion(idDeterminacion);
            detClasif.setFicha(this.get(Ficha.class, idFicha));

            em.persist(detClasif);

            return detClasif;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar la determinacion clasificatoria de usos. Consulte con su administrador.", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal#guardarConjuntoGrupo(long, long, java.lang.String, long, boolean, boolean, boolean)
     */
    @Override
    public Conjuntogrupo guardarConjuntoGrupo(long idFicha, long idConjunto, String nombreGrupo, long ordenGrupo, boolean regimen_directo, boolean usos, boolean actos) throws ExcepcionFicha {

        if (idFicha == 0 && idConjunto == 0) {
            throw new ExcepcionFicha("Debe especificarse una ficha o un conjunto de grupos");
        }
        if (nombreGrupo == null || nombreGrupo.isEmpty()) {
            throw new ExcepcionFicha("Debe especificarse un nombre de conjunto de grupo");
        }
        if (ordenGrupo == 0) {
            throw new ExcepcionFicha("Debe especificarse un orden");
        }

        try {
            Conjuntogrupo conjunto = null;
            Fichaconjuntogrupo fichaGrupo = null;
            if (idConjunto != 0) {
                conjunto = this.get(Conjuntogrupo.class, idConjunto);
                fichaGrupo = conjunto.getFichaconjuntogrupos().iterator().next();
            }
            if (conjunto == null) {
                conjunto = new Conjuntogrupo();
                fichaGrupo = new Fichaconjuntogrupo();
                fichaGrupo.setConjuntogrupo(conjunto);
                fichaGrupo.setFicha(this.get(Ficha.class, idFicha));
                conjunto.getFichaconjuntogrupos().add(fichaGrupo);
            }

            fichaGrupo.setOrden(ordenGrupo);
            fichaGrupo.setRegimen_directo(regimen_directo);
            fichaGrupo.setUsos(usos);
            fichaGrupo.setActos(actos);
            conjunto.setNombre(nombreGrupo);
            em.persist(conjunto);
            em.persist(fichaGrupo);
            return conjunto;
        } catch (Exception e) {
            throw new ExcepcionFicha("Error al guardar el conjunto. Consulte con su administrador.", e);
        }
    }
}
