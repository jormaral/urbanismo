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
** Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
** Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;



import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.fipxml.PropiedadesAdscripcion;
import com.mitc.redes.editorfip.entidades.fipxml.PropiedadesUnidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentocaso;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoshp;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;
import com.mitc.redes.editorfip.utilidades.Textos;

@Stateless
@Name("fipFacade")
public class FipFacadeBean implements FipFacade{

    private static final String PROPERTIES = "diccionario";

    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    
  
    public FipFacadeBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tramite findComplete(Object id) {
        
    	log.debug("[findComplete] Inicio = "+(Integer)id);
    	
        int iden = Integer.parseInt(id.toString());
        Tramite tram = em.find(Tramite.class, iden);
        PropiedadesAdscripcion propsAds;

        em.refresh(tram);
        //CARGAMOS LAS ADSCRIPCIONES
        log.debug("Cargando adscripciones");
        Set<PropiedadesAdscripcion> aa = getAdscripciones(tram);
        tram.setPropiedadesAdscripcion(aa);

        log.debug("Cargando documentos");
        if (tram.getDocumentos() == null) {
            em.persist(tram);
            List<Documento> docTrm = (List<Documento>) tram.getDocumentos();
        }
        tram.getDocumentos().size();
        //log.debug("Numero Documentos del Tramite=" + tram.getDocumentos().size());

        for (Documento doc : tram.getDocumentos()) {
            if (doc.getDocumentoshps() == null) {
                em.persist(doc);
                List<Documentoshp> docShp = (List<Documentoshp>) doc.getDocumentoshps();
            }
            doc.getDocumentoshps().size();
        //log.debug("Numero Hojas del Documento=" + doc.getDocumentoshps().size());
        }

        log.debug("Cargando entidades");
        if (tram.getEntidads() == null) {
            em.persist(tram);
            List<Entidad> entTrm = (List<Entidad>) tram.getEntidads();
        }
        tram.getEntidads().size();
        //log.debug("Entidades del Tramite=" + tram.getEntidads().size());

        for (Entidad ent : tram.getEntidads()) {
            if (ent.getEntidadByIdpadre() == null) {
                getEntidad(ent);
            }
        }

        log.debug("Cargando determinaciones");
        if (tram.getDeterminacions() == null) {
            em.persist(tram);
            List<Determinacion> detTrm = (List<Determinacion>) tram.getDeterminacions();
        }
        tram.getDeterminacions().size();
        //log.debug("Determinaciones del Tramite=" + tram.getDeterminacions().size());
        for (Determinacion det : tram.getDeterminacions()) {
            if (det.getDeterminacionByIdpadre() == null) {
                getDeterminacion(det);
            }
        }

        log.debug("Cargando operaciones");
        if (tram.getOperacions() == null) {
            em.persist(tram);
            List<Operacion> oper = (List<Operacion>) tram.getOperacions();
        }
        tram.getOperacions().size();
        //log.debug("Operaciones del Tramite=" + tram.getOperacions().size());
        for (Operacion oper : tram.getOperacions()) {
            for (Operacionentidad opEnt : oper.getOperacionentidads()) {
                opEnt.getEntidadByIdentidad().getNombre();
                opEnt.getEntidadByIdentidad().getTramite().getCodigofip();
                opEnt.getEntidadByIdentidadoperadora().getNombre();
                opEnt.getEntidadByIdentidadoperadora().getTramite().getCodigofip();
            }

            for (Operaciondeterminacion opDet : oper.getOperaciondeterminacions()) {
                opDet.getDeterminacionByIddeterminacion().getNombre();
                opDet.getDeterminacionByIddeterminacion().getTramite().getCodigofip();
                opDet.getDeterminacionByIddeterminacionoperadora().getNombre();
                opDet.getDeterminacionByIddeterminacionoperadora().getTramite().getCodigofip();
            }
            for (Operacionrelacion opRel : oper.getOperacionrelacions()) {
                //CARGAMOS LAS OPERACIONES DE ADSCRIPCIÓN
                propsAds = getPropiedadesAdscripcion(opRel.getRelacion().getIden());
                opRel.setPropiedadesAdscripcion(propsAds);
            }
        }



        return tram;

    }

    private Set<PropiedadesAdscripcion> getAdscripciones(Tramite tram) {
        String s;
        Query aQuery;
        PropiedadesAdscripcion aProp;
        Set<PropiedadesAdscripcion> aProps = null;

        try {
            aProps = new HashSet<PropiedadesAdscripcion>();

            Integer IdDefPropAdscripcion = Integer.parseInt(Textos.getCadena(PROPERTIES, "adscripcion.id_definicion_adscripcion"));
            Integer IdDefEntidadOrigen = Integer.parseInt(Textos.getCadena(PROPERTIES,"adscripcion.id_def_origen"));
            Integer IdDefEntidadDestino = Integer.parseInt(Textos.getCadena(PROPERTIES,"adscripcion.id_def_destino"));
            s = "select iden " +
                    "from planeamiento.relacion rel " +
                    "where iddefrelacion= " + IdDefPropAdscripcion + " and " +
                    "(select idtramite from planeamiento.entidad where iden in " +
                    "(select valor from planeamiento.vectorrelacion where idrelacion=rel.iden and iddefvector=" + IdDefEntidadOrigen + ")) = " + tram.getIden() + " and " +
                    "(select idtramite from planeamiento.entidad where iden in " +
                    "(select valor from planeamiento.vectorrelacion where idrelacion=rel.iden and iddefvector=" + IdDefEntidadDestino + ")) = " + tram.getIden() +
                    " and iden not in (select idrelacion from planeamiento.operacionrelacion)";
            aQuery = em.createNativeQuery(s);

            int contador = 0;
            for (Object Resultado : aQuery.getResultList()) {
                aProp = getPropiedadesAdscripcion(Integer.parseInt(Resultado.toString()));
                aProps.add(aProp);
                aProp = null;
                contador++;
            }
        
        } catch (Exception ex) {
            log.error("[getAdscripciones] ERROR="+ex.getMessage());
            ex.printStackTrace();
            return null;
        }
        return aProps;
    }

  private  PropiedadesAdscripcion getPropiedadesAdscripcion(int iden) {
        PropiedadesAdscripcion aProps;
        String s;
        Query aQuery;
        List<Object[]>  Resultado;

        try {
                Integer IdDefPropAdscripcion = Integer.parseInt(Textos.getCadena("diccionario","adscripcion.id_definicion_adscripcion"));
                Integer IdDefEntidadOrigen = Integer.parseInt(Textos.getCadena("diccionario","adscripcion.id_def_origen"));
                Integer IdDefEntidadDestino = Integer.parseInt(Textos.getCadena("diccionario","adscripcion.id_def_destino"));
                Integer IdDefUnidad = Integer.parseInt(Textos.getCadena("diccionario","adscripcion.id_def_unidad"));
                Integer IdDefTipo = Integer.parseInt(Textos.getCadena("diccionario","adscripcion.id_def_tipo"));
                Integer IdDefCuantia = Integer.parseInt(Textos.getCadena("diccionario","adscripcion.id_def_cuantia"));
                Integer IdDefTexto = Integer.parseInt(Textos.getCadena("diccionario","adscripcion.id_def_texto"));

                s= "Select r.iden," +
                "(select valor from planeamiento.vectorrelacion where iddefvector =" + IdDefEntidadOrigen + " and idrelacion=r.iden ) as identidadorigen," +
                "(select valor from planeamiento.vectorrelacion where iddefvector =" + IdDefEntidadDestino + " and idrelacion=r.iden ) as identidaddestino," +
                "(select valor from planeamiento.vectorrelacion where iddefvector =" + IdDefUnidad + " and idrelacion=r.iden ) as idunidad," +
                "(select valor from planeamiento.vectorrelacion where iddefvector =" + IdDefTipo + " and idrelacion=r.iden ) as idtipo," +
                "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad =" + IdDefCuantia + " and idrelacion=r.iden ) as cuantia," +
                "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad =" + IdDefTexto + " and idrelacion=r.iden ) as texto " +
                " From planeamiento.Relacion r, planeamiento.Vectorrelacion vr " +
                " Where r.iddefrelacion=" + IdDefPropAdscripcion +
                " And r.iden=" + iden +
                " And vr.iddefvector=" + IdDefEntidadOrigen +
                " And vr.idrelacion=r.iden ";

                aQuery=em.createNativeQuery(s);

                Resultado=aQuery.getResultList();

                aProps=null;
                String Texto="";
                if (Resultado.size()>0){
                    for (Object[] aPropsAds: Resultado){

                        if (aPropsAds[6] != null){
                            Texto = aPropsAds[6].toString();
                        }else{
                            Texto ="";
                        }
                        aProps=new PropiedadesAdscripcion(Integer.parseInt(aPropsAds[0].toString()),
                                                    em.find(Entidad.class,aPropsAds[1]),
                                                    em.find(Entidad.class,aPropsAds[2]),
                                                    Double.parseDouble(aPropsAds[5].toString()),
                                                    em.find(Determinacion.class,aPropsAds[3]),
                                                    em.find(Determinacion.class,aPropsAds[4]),
                                                    Texto);
                    }
                }



            return aProps;

        } catch (Exception ex) {
        	log.error("[getPropiedadesAdscripcion] ERROR="+ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }


    private void getDeterminacion(Determinacion det) {
        PropiedadesUnidad propsUni;

        Determinacion aDeterminacion;
        //log.debug("----------------------------------------------------------");
        if (det.getDocumentodeterminacions() == null) {
            em.persist(det);
            List<Documentodeterminacion> docDet = (List<Documentodeterminacion>) det.getDocumentodeterminacions();
        }
        det.getDocumentodeterminacions().size();
        //log.debug("Documentos de la Determinacion=" + det.getDocumentodeterminacions().size());

        if (det.getOpciondeterminacionsForIddeterminacion() == null) {
            em.persist(det);
            List<Opciondeterminacion> optDet = (List<Opciondeterminacion>) det.getOpciondeterminacionsForIddeterminacion();
        }
        det.getOpciondeterminacionsForIddeterminacion().size();
        //log.debug("Opciones de la Determinacion=" + det.getOpciondeterminacionsForIddeterminacion().size());

        if (det.getDeterminaciongrupoentidadsForIddeterminacion() == null) {
            em.persist(det);
            List<Determinaciongrupoentidad> optGrp = (List<Determinaciongrupoentidad>) det.getDeterminaciongrupoentidadsForIddeterminacion();
        }
        det.getDeterminaciongrupoentidadsForIddeterminacion().size();
        //log.debug("Grupos de aplicación de la Determinacion=" + det.getDeterminaciongrupoentidadsForIddeterminacion().size());
        for (Determinaciongrupoentidad grp : det.getDeterminaciongrupoentidadsForIddeterminacion()) {
            grp.getDeterminacionByIddeterminaciongrupo().getTramite().getCodigofip();
            grp.getDeterminacionByIddeterminaciongrupo().getNombre();
        }

        if (det.getDeterminacionsForIdpadre() == null) {
            em.persist(det);
            List<Determinacion> detHijs = (List<Determinacion>) det.getDeterminacionsForIdpadre();
        }
        det.getDeterminacionsForIdpadre().size();
        //log.debug("Hijas de la Determinación=" + det.getDeterminacionsForIdpadre().size());

        //if (det.getDeterminacionByIddeterminacionbase() == null) {
        //    em.persist(det);
        //    Determinacion detBas = (Determinacion) det.getDeterminacionByIddeterminacionbase();
        //}

        if (det.getDeterminacionByIddeterminacionbase() != null) {
            det.getDeterminacionByIddeterminacionbase().getNombre();
            det.getDeterminacionByIddeterminacionbase().getTramite().getCodigofip();
        }

        if (det.getTramite() == null) {
            em.persist(det);
            Tramite trmDet = (Tramite) det.getTramite();
        }

        det.getTramite().getCodigofip();
        //log.debug("Código del Trámite de la Determinacion=" + det.getTramite().getCodigofip());

// TODO FGA - Comentado
        /*
        aDeterminacion = det.getUnidad(em);
        if (aDeterminacion != null) {
            aDeterminacion.getNombre();
            aDeterminacion.getTramite().getCodigofip();
        }

        if (det.getDeterminacionesReguladoras(em) != null) {
            for (Determinacion detReg : det.getDeterminacionesReguladoras(em)) {
                detReg.getNombre();
                detReg.getTramite().getCodigofip();
            }
        }

        if (det.getRegulacionEspecifica(em) == null) {
            em.persist(det);
            List<RegulacionEspecifica> regEsp = (List<RegulacionEspecifica>) det.getRegulacionEspecifica(em);
        }
        det.getRegulacionEspecifica(em).size();

        //CARGAMOS LAS PROPIEDADES DE UNIDAD
        propsUni = PropiedadesUnidad.getPropiedadesUnidad(em, det);
        det.setPropiedadesUnidad(propsUni);

        for (Determinacion detHija : det.getDeterminacionsForIdpadre()) {
            getDeterminacion(detHija);
        }
        */

    }

    private void getEntidad(Entidad ent) {
        //log.debug("----------------------------------------------------------");
        if (ent.getDocumentoentidads() == null) {
            em.persist(ent);
            List<Documentoentidad> docEnt = (List<Documentoentidad>) ent.getDocumentoentidads();
        }
        ent.getDocumentoentidads().size();
        //log.debug("Documentos de la Entidad=" + ent.getDocumentoentidads().size());

        if (ent.getEntidadpols() == null) {
            em.persist(ent);
            List<Entidadpol> geo = (List<Entidadpol>) ent.getEntidadpols();
        }
        ent.getEntidadpols().size();

        if (ent.getEntidadlins() == null) {
            em.persist(ent);
            List<Entidadlin> geo = (List<Entidadlin>) ent.getEntidadlins();
        }
        ent.getEntidadlins().size();

        if (ent.getEntidadpnts() == null) {
            em.persist(ent);
            List<Entidadpnt> geo = (List<Entidadpnt>) ent.getEntidadpnts();
        }
        ent.getEntidadpnts().size();
     
        if (ent.getEntidadsForIdpadre() == null) {
            em.persist(ent);
            List<Entidad> entHijs = (List<Entidad>) ent.getEntidadsForIdpadre();
        }
        ent.getEntidadsForIdpadre().size();
     
        if (ent.getEntidadByIdentidadbase() == null) {
            em.persist(ent);
            Entidad entBas = (Entidad) ent.getEntidadByIdentidadbase();
        } else if (ent.getEntidadByIdentidadbase() != null) {
            ent.getEntidadByIdentidadbase().getNombre();
            ent.getEntidadByIdentidadbase().getTramite().getCodigofip();
        }

        if (ent.getTramite() == null) {
            em.persist(ent);
            Tramite trmEnt = (Tramite) ent.getTramite();
        }
        ent.getTramite().getCodigofip();

        if (ent.getEntidaddeterminacions() == null) {
            em.persist(ent);
            List<Entidaddeterminacion> entDet = (List<Entidaddeterminacion>) ent.getEntidaddeterminacions();
        }
        ent.getEntidaddeterminacions().size();

        for (Entidaddeterminacion entDet : ent.getEntidaddeterminacions()) {
            getEntidadDeterminacion(entDet);
        }

        for (Entidad entHija : ent.getEntidadsForIdpadre()) {
            getEntidad(entHija);
        }
    }

    private void getEntidadDeterminacion(Entidaddeterminacion entDet) {

        if (entDet.getCasoentidaddeterminacions() == null) {
            em.persist(entDet);
            List<Casoentidaddeterminacion> aCaso = (List<Casoentidaddeterminacion>) entDet.getCasoentidaddeterminacions();
        }

        entDet.getCasoentidaddeterminacions().size();
        entDet.getDeterminacion().getNombre();
        entDet.getDeterminacion().getTramite().getCodigofip();

        for (Casoentidaddeterminacion aCaso : entDet.getCasoentidaddeterminacions()) {
            getCaso(aCaso);
        }
    }

    private void getCaso(Casoentidaddeterminacion aCaso) {
        try {

            if (aCaso.getDocumentocasos() == null) {
                em.persist(aCaso);
                List<Documentocaso> docCas = (List<Documentocaso>) aCaso.getDocumentocasos();
            }
            aCaso.getDocumentocasos().size();

            if (aCaso.getVinculocasosForIdcaso() == null) {
                em.persist(aCaso);
                List<Vinculocaso> vinc = (List<Vinculocaso>) aCaso.getVinculocasosForIdcaso();
            }
            aCaso.getVinculocasosForIdcaso().size();

            for (Vinculocaso vinc : aCaso.getVinculocasosForIdcaso()) {
                getCaso(vinc.getCasoentidaddeterminacionByIdcasovinculado());
            }

            if (aCaso.getEntidaddeterminacionregimensForIdcaso() == null) {
                em.persist(aCaso);
                List<Entidaddeterminacionregimen> vinc = (List<Entidaddeterminacionregimen>) aCaso.getEntidaddeterminacionregimensForIdcaso();
            }
            aCaso.getEntidaddeterminacionregimensForIdcaso().size();

            for (Entidaddeterminacionregimen reg : aCaso.getEntidaddeterminacionregimensForIdcaso()) {
                getRegimen(reg);
            }

        } catch (Exception ex) {
            log.debug("ERROR: " + ex.toString());
        }
    }

    private void getRegimen(Entidaddeterminacionregimen aReg) {
        if (aReg.getRegimenespecificos() == null) {
            em.persist(aReg);
            List<Regimenespecifico> reg = (List<Regimenespecifico>) aReg.getRegimenespecificos();
        }
        aReg.getRegimenespecificos().size();

        for (Regimenespecifico reg : aReg.getRegimenespecificos()) {
            if (reg.getRegimenespecifico() == null) {
                getRegimenEspecifico(reg);
            }
        }

        if (aReg.getOpciondeterminacion() == null) {
            em.persist(aReg);
            Opciondeterminacion opc = (Opciondeterminacion) aReg.getOpciondeterminacion();
        } else {
            aReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getNombre();
            aReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getTramite().getCodigofip();
        }

        if (aReg.getDeterminacion() == null) {
            em.persist(aReg);
            Determinacion det = (Determinacion) aReg.getDeterminacion();
        } else {
            aReg.getDeterminacion().getNombre();
            aReg.getDeterminacion().getTramite().getCodigofip();
        }

        if (aReg.getCasoentidaddeterminacionByIdcasoaplicacion() == null) {
            em.persist(aReg);
            Casoentidaddeterminacion cas = (Casoentidaddeterminacion) aReg.getCasoentidaddeterminacionByIdcasoaplicacion();
        } else {
            aReg.getCasoentidaddeterminacionByIdcasoaplicacion().getNombre();
        }

    }

    private void getRegimenEspecifico(Regimenespecifico aReg) {
        if (aReg.getRegimenespecificos() == null) {
            em.persist(aReg);
            List<Regimenespecifico> reg = (List<Regimenespecifico>) aReg.getRegimenespecificos();
        }
        aReg.getRegimenespecificos().size();
        for (Regimenespecifico reg : aReg.getRegimenespecificos()) {
            getRegimenEspecifico(reg);
        }
    }

}

