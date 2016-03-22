/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.informacionfip.consultafichaurbanisticas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.fipxml.PropiedadesAdscripcion;
import com.mitc.redes.editorfip.entidades.fipxml.RegulacionEspecifica;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegulacionEspecificaDTO;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito;
import com.mitc.redes.editorfip.entidades.rpm.explotacion.Capa;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.procesamientofip.generacionfip2.ConversionFIPXML;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;
import com.mitc.redes.editorfip.utilidades.Textos;
import com.vividsolutions.jts.geom.Geometry;

@Stateless
@Name("servicioBasicoFichaUrbanistica")
public class ServicioBasicoFichaUrbanisticaBean implements ServicioBasicoFichaUrbanistica
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    @In Map<String,String> messages;
    
    @In(create = true, required= false)
    ConversionFIPXML conversionFIPXML;
    
    @In(create = true, required= false)
    ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    @In(create = true, required= false)
    ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;
    
    @In(create = true, required= false)
    VariablesSesionUsuario variablesSesionUsuario;
    
    
    
    // ------------------------------
    // plan
    // ------------------------------	
    
    public List<Plan> findPlanesGeom(String WKT){
        List<Plan> Planes = new ArrayList();
        
        List aResultado;
        Plan aPlan;
        String aConsulta;

        try{
        
            aConsulta = "select iden from planeamiento.plan where iden in " +
                    " (select idplan from planeamiento.tramite where iden in" +
                    " (select idtramite from planeamiento.entidad where iden in " +
                    " (select identidad from planeamiento.entidadpol as o " +
                    " INNER JOIN planeamiento.entidad ent ON o.identidad = ent.iden " +
                    " where Intersects (geom,GeomFromText('" + WKT + "'))))) order by orden";
                    
            
            aResultado = em.createNativeQuery(aConsulta).getResultList();

            for (Object Iden : aResultado) {
                aPlan = em.find(Plan.class, Iden);
                Planes.add(aPlan);
            }
        } catch (Exception ex) {
            log.error( ex);
            ex.printStackTrace();
        }

        return Planes;
    }
    
    
    // ------------------------------
    // determinacion
    // ------------------------------	
    public Set<RegulacionEspecifica> getNodosRegulacionEspecificaDeDet(Determinacion det) {
        //Determinacion det = this.find(idDet);
        Set<RegulacionEspecifica> regesps = null;

        if (det != null) {
            regesps = conversionFIPXML.getRegulacionEspecifica(det);
        }

        return regesps;
    }
    
    
	public DocumentFragment getDeterminacionesTodas(int idTramite, Document aXML) {

		log.info("[getDeterminacionesTodas]idTramite= " + idTramite);
		List<Determinacion> aDeterminaciones;

		Element aNodeDeterminaciones=null;
		Element aNodeDet=null;
		Element aNodeRegEspecifica=null;
		DocumentFragment aXMLFrag;

		try {

			aNodeDeterminaciones = aXML.createElement("DETERMINACIONES");

			// FGA
			aDeterminaciones = (List<Determinacion>) em.createQuery("select det from Determinacion det where det.tramite.iden="+ idTramite).getResultList();
			//log.info("[getDeterminacionesTodas] lista Determinaciones= "+ aDeterminaciones.size());

			/*
        	// FGA
        	List<Determinacion> aDeterminaciones2 = (List<Determinacion>) em.createQuery("select det from Determinacion det where det.tramite.iden="+ idTramite).getResultList();
            // -----
            // Para probar voy a hacerlo solo con 10 entidades
        	aDeterminaciones = new ArrayList<Determinacion>();
            
            for (int i =0; i<10; i++)
            {
            	aDeterminaciones(aDeterminaciones2.get(i));
            }
            */
			
			for (Determinacion aDeterminacion : aDeterminaciones) {
				
				aNodeDet = aXML.createElement("DETERMINACION");

				aNodeDet.setAttribute("nombre", aDeterminacion.getNombre());
				aNodeDet.setAttribute("caracter", servicioBasicoDeterminaciones.caracterString(aDeterminacion.getIdcaracter()));
				aNodeDet.setAttribute("ordencompleto", servicioBasicoDeterminaciones.obtenerOrdenCompletoDeterminacion(aDeterminacion.getIden()));
				aNodeDet.setAttribute("articulado", aDeterminacion.getApartado());
				
				// ----
				
				String regulacionesEspecif = "";
				
				List<ParIdentificadorTexto> regulacionesList = servicioBasicoDeterminaciones.getRegulacionespecificaRaices(aDeterminacion.getIden());
				
				for (ParIdentificadorTexto regEspec : regulacionesList)
				{
					RegulacionEspecificaDTO regEspecificaDTO = servicioBasicoDeterminaciones.buscarRegulacionEspecifica(regEspec.getIdBaseDatos());
					
					regulacionesEspecif += regEspecificaDTO.getNombre()+"\n\n";
					regulacionesEspecif += regEspecificaDTO.getTexto()+"\n\n";
				}
				
				//log.info("[getDeterminacionesTodas]regulacionesEspecif= " + regulacionesEspecif);
				if (!regulacionesEspecif.isEmpty())
				{
					aNodeDet.setAttribute("regulaciones", regulacionesEspecif);
				}
				
				
				

				aNodeDeterminaciones.appendChild(aNodeDet);
				
				/*
				 *  Con nodo. FGA: No he conseguido mostrarlo por pantalla
				List<ParIdentificadorTexto> regulacionesList = servicioBasicoDeterminaciones.getRegulacionespecificaRaices(aDeterminaciones.get(i).getIden());
				
				for (ParIdentificadorTexto regEspec : regulacionesList)
				{
					aNodeRegEspecifica = aXML.createElement("REGULACIONESPECIFICA");
					
					RegulacionEspecificaDTO regEspecificaDTO = servicioBasicoDeterminaciones.buscarRegulacionEspecifica(regEspec.getIdBaseDatos());
					
					aNodeRegEspecifica.setAttribute("nombre", regEspecificaDTO.getNombre());
					aNodeRegEspecifica.setAttribute("texto", regEspecificaDTO.getTexto());
					aNodeRegEspecifica.setAttribute("orden", regEspecificaDTO.getOrden()+"");
					
					aNodeDet.appendChild(aNodeRegEspecifica);
					
				}
				*/
				
				
				
				
				
				
			}

			//log.info("[getDeterminacionesTodas]aNodeDet getChildNodes= "+ aNodeDet.getChildNodes().getLength());
			//log.info("[getDeterminacionesTodas]aNodeDeterminaciones getChildNodes= "+ aNodeDeterminaciones.getChildNodes().getLength());
			
			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aNodeDeterminaciones);
			return aXMLFrag;
		
		} catch (Exception ex) {
			log.error("[getEntidades]ERROR= " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
       
   
    
    // ------------------------------
    // Entidad
    // ------------------------------	
    
 public DocumentFragment getEntidadesTodas(int idTramite, Document aXML){
        
        List<Entidad> aEntidades;
        List<Ambito> aAmbitos;
        NodeList aLista=null;
        NodeList aListaGrupos=null;
        Element aNode;
        Element aNodeAdscripcion;
        Element aNodeGrupo;
        Element aNodeCapa;
        Element aNodeEntidades;
        Element aNodeAmbito;
        DocumentFragment aXMLFrag;

        try {
            
          
            aNodeEntidades=aXML.createElement("ENTIDADES");
           
            	
            	
            	aEntidades=(List<Entidad>)em.createQuery("select ent from Entidad ent where ent.tramite.iden="+idTramite+" order by ent.clave ASC").getResultList();

            	/*
            	// FGA
            	List<Entidad> aEntidad2=(List<Entidad>)em.createQuery("select ent from Entidad ent where ent.tramite.iden="+idTramite+" order by ent.clave ASC").getResultList();
            	
                // -----
                // Para probar voy a hacerlo solo con 10 entidades
            	aEntidades = new ArrayList<Entidad>();
                
                for (int i =0; i<10; i++)
                {
                	aEntidades.add(aEntidad2.get(i));
                }
                */
                
                
                // ------
                
                aNodeAmbito=aXML.createElement("AMBITO");
                aNodeAmbito.setAttribute("id","1");
                aNodeAmbito.setAttribute("nombre", "Pruebaa");

                //for (Entidad aEntidad:aEntidades){
                for (Entidad aEntidad:aEntidades){
                    
                    if (!aEntidad.isBsuspendida()){
                        
                    	// TODO FGA Comentado    Determinacion grupo = aEntidad.getGrupo();
                        
                        int idDetGrupo = servicioBasicoCondicionesUrbanisticas.obtenerVRdeCUdeEntidadAplicadaAGA(aEntidad.getIden());
                        Determinacion grupo = em.find(Determinacion.class, idDetGrupo);
                        
                        
                        if (grupo!=null){
                            Capa capa=findFromCodigoGrupo(grupo.getCodigo());
                            if (capa!=null){
                                aLista = XMLws.findNode("CAPA[@id=\"" + capa.getIden() + "\"]", aNodeAmbito);
                                if (aLista == null || aLista.getLength()==0) {
                                    aNodeCapa= aXML.createElement("CAPA");
                                    aNodeCapa.setAttribute("id",String.valueOf(capa.getIden()));
                                    aNodeCapa.setAttribute("nombre", capa.getNombre());
                                    aNodeCapa.setAttribute("orden", String.valueOf(capa.getOrden()));
                                    for (int iNodo=0;iNodo< aNodeAmbito.getChildNodes().getLength();iNodo++)
                                    {
                                        Element aNodoAfter=(Element) aNodeAmbito.getChildNodes().item(iNodo);
                                        if (capa.getOrden()<Integer.parseInt( aNodoAfter.getAttribute("orden"))){
                                            aNodeAmbito.insertBefore(aNodeCapa,aNodoAfter);
                                            break;
                                        }
                                    }
                                    if (aNodeCapa.getParentNode()==null){
                                        aNodeAmbito.appendChild(aNodeCapa);
                                    }
                                }
                                else
                                {
                                    aNodeCapa = (Element) aLista.item(0);
                                }

                                aListaGrupos = XMLws.findNode("GRUPO[@id=\"" + grupo.getIden() + "\"]", aNodeCapa);
                                if (aListaGrupos == null || aListaGrupos.getLength()==0) {
                                    aNodeGrupo = aXML.createElement("GRUPO");
                                    aNodeGrupo.setAttribute("id",String.valueOf(grupo.getIden()));
                                    aNodeGrupo.setAttribute("nombre", grupo.getNombre());
                                    aNodeCapa.appendChild(aNodeGrupo);
                                } else {
                                    aNodeGrupo = (Element) aListaGrupos.item(0);
                                }
                                aNode = aXML.createElement("ENTIDAD");
                                aNodeGrupo.appendChild(aNode);
                                aNode.setAttribute("id", String.valueOf(aEntidad.getIden()));
                                aNode.setAttribute("clave", aEntidad.getClave());
                                aNode.setAttribute("nombre", aEntidad.getNombre());
                                
                                List<PropiedadesAdscripcion> ads = getAdscripciones(aEntidad.getIden());
                                for (PropiedadesAdscripcion ad : ads)
                                {
                                    aNodeAdscripcion= aXML.createElement("ADSCRIPCION");
                                    if (ad.getTipo()!=null){
                                        aNodeAdscripcion.setAttribute("tipo", ad.getTipo().getNombre());
                                    }
                                    if (ad.getDestino().getIden()!=aEntidad.getIden())
                                    {
                                        aNodeAdscripcion.setAttribute("destino", ad.getDestino().getNombre());
                                        aNodeAdscripcion.setAttribute("id_destino", String.valueOf(ad.getDestino().getIden()));
                                    }
                                    if (ad.getOrigen().getIden()!=aEntidad.getIden())
                                    {
                                        aNodeAdscripcion.setAttribute("origen", ad.getOrigen().getNombre());
                                        aNodeAdscripcion.setAttribute("id_origen", String.valueOf(ad.getOrigen().getIden()));
                                    }
                                    aNodeAdscripcion.setAttribute("cuantia", String.valueOf(ad.getCuantia()));
                                    if (ad.getUnidad()!=null){
                                        aNodeAdscripcion.setAttribute("unidad", ad.getUnidad().getNombre());
                                    }
                                    if (ad.getTexto()!=null && !ad.getTexto().isEmpty()){
                                        aNodeAdscripcion.setAttribute("texto", ad.getTexto());
                                    }
                                    aNode.appendChild(aNodeAdscripcion);
                                }
                            }
                        }


                    }
                    
                    aNodeEntidades.appendChild(aNodeAmbito);
                }
            
            aXMLFrag=aXML.createDocumentFragment();
            aXMLFrag.appendChild(aNodeEntidades);
            return aXMLFrag;
        } catch (Exception ex) {
        	log.error("[getEntidades]ERROR= "+ex.getMessage());
        	ex.printStackTrace();
            return null;
        }
    }
    
    
    public DocumentFragment getEntidades(Geometry aGeo,Document aXML){
        
        List<Entidad> aEntidades;
        List<Ambito> aAmbitos;
        NodeList aLista=null;
        NodeList aListaGrupos=null;
        Element aNode;
        Element aNodeAdscripcion;
        Element aNodeGrupo;
        Element aNodeCapa;
        Element aNodeEntidades;
        Element aNodeAmbito;
        DocumentFragment aXMLFrag;

        try {
            
            aAmbitos=IdAmbitos(aGeo);
            aNodeEntidades=aXML.createElement("ENTIDADES");
            for (Ambito Amb : aAmbitos){
                aEntidades=findRefundido(geoUtils.GeoToWKT(aGeo),Amb);

                aNodeAmbito=aXML.createElement("AMBITO");
                aNodeAmbito.setAttribute("id",String.valueOf(Amb.getIden()));
                aNodeAmbito.setAttribute("nombre", Amb.getIden()+"");

                for (Entidad aEntidad:aEntidades){
                    //ACR: Cambio motivado por las incidencias INC000000264195 y INC000000264191
                	
                	
                	
                    if (!aEntidad.isBsuspendida()){
                        
                    	// TODO FGA Comentado    Determinacion grupo = aEntidad.getGrupo();
                        
                        int idDetGrupo = servicioBasicoCondicionesUrbanisticas.obtenerVRdeCUdeEntidadAplicadaAGA(aEntidad.getIden());
                        Determinacion grupo = em.find(Determinacion.class, idDetGrupo);
                        
                        
                        if (grupo!=null){
                            Capa capa=findFromCodigoGrupo(grupo.getCodigo());
                            if (capa!=null){
                                aLista = XMLws.findNode("CAPA[@id=\"" + capa.getIden() + "\"]", aNodeAmbito);
                                if (aLista == null || aLista.getLength()==0) {
                                    aNodeCapa= aXML.createElement("CAPA");
                                    aNodeCapa.setAttribute("id",String.valueOf(capa.getIden()));
                                    aNodeCapa.setAttribute("nombre", capa.getNombre());
                                    aNodeCapa.setAttribute("orden", String.valueOf(capa.getOrden()));
                                    for (int iNodo=0;iNodo< aNodeAmbito.getChildNodes().getLength();iNodo++)
                                    {
                                        Element aNodoAfter=(Element) aNodeAmbito.getChildNodes().item(iNodo);
                                        if (capa.getOrden()<Integer.parseInt( aNodoAfter.getAttribute("orden"))){
                                            aNodeAmbito.insertBefore(aNodeCapa,aNodoAfter);
                                            break;
                                        }
                                    }
                                    if (aNodeCapa.getParentNode()==null){
                                        aNodeAmbito.appendChild(aNodeCapa);
                                    }
                                }
                                else
                                {
                                    aNodeCapa = (Element) aLista.item(0);
                                }

                                aListaGrupos = XMLws.findNode("GRUPO[@id=\"" + grupo.getIden() + "\"]", aNodeCapa);
                                if (aListaGrupos == null || aListaGrupos.getLength()==0) {
                                    aNodeGrupo = aXML.createElement("GRUPO");
                                    aNodeGrupo.setAttribute("id",String.valueOf(grupo.getIden()));
                                    aNodeGrupo.setAttribute("nombre", grupo.getNombre());
                                    aNodeCapa.appendChild(aNodeGrupo);
                                } else {
                                    aNodeGrupo = (Element) aListaGrupos.item(0);
                                }
                                aNode = aXML.createElement("ENTIDAD");
                                aNodeGrupo.appendChild(aNode);
                                aNode.setAttribute("id", String.valueOf(aEntidad.getIden()));
                                aNode.setAttribute("clave", aEntidad.getClave());
                                aNode.setAttribute("nombre", aEntidad.getNombre());
                                
                                List<PropiedadesAdscripcion> ads = getAdscripciones(aEntidad.getIden());
                                for (PropiedadesAdscripcion ad : ads)
                                {
                                    aNodeAdscripcion= aXML.createElement("ADSCRIPCION");
                                    if (ad.getTipo()!=null){
                                        aNodeAdscripcion.setAttribute("tipo", ad.getTipo().getNombre());
                                    }
                                    if (ad.getDestino().getIden()!=aEntidad.getIden())
                                    {
                                        aNodeAdscripcion.setAttribute("destino", ad.getDestino().getNombre());
                                        aNodeAdscripcion.setAttribute("id_destino", String.valueOf(ad.getDestino().getIden()));
                                    }
                                    if (ad.getOrigen().getIden()!=aEntidad.getIden())
                                    {
                                        aNodeAdscripcion.setAttribute("origen", ad.getOrigen().getNombre());
                                        aNodeAdscripcion.setAttribute("id_origen", String.valueOf(ad.getOrigen().getIden()));
                                    }
                                    aNodeAdscripcion.setAttribute("cuantia", String.valueOf(ad.getCuantia()));
                                    if (ad.getUnidad()!=null){
                                        aNodeAdscripcion.setAttribute("unidad", ad.getUnidad().getNombre());
                                    }
                                    if (ad.getTexto()!=null && !ad.getTexto().isEmpty()){
                                        aNodeAdscripcion.setAttribute("texto", ad.getTexto());
                                    }
                                    aNode.appendChild(aNodeAdscripcion);
                                }
                            }
                        }


                    }
                    
                    aNodeEntidades.appendChild(aNodeAmbito);
                }
            }
            aXMLFrag=aXML.createDocumentFragment();
            aXMLFrag.appendChild(aNodeEntidades);
            return aXMLFrag;
        } catch (Exception ex) {
        	log.error("[getEntidades]ERROR= "+ex.getMessage());
        	ex.printStackTrace();
            return null;
        }
    }
    
    private List<PropiedadesAdscripcion> getAdscripciones(int idEnt) {
        try {
            Integer IdDefPropAdscripcion = Integer.parseInt(Textos.getCadena("diccionario", "adscripcion.id_definicion_adscripcion"));
            Integer IdDefEntidadOrigen = Integer.parseInt(Textos.getCadena("diccionario", "adscripcion.id_def_origen"));
            Integer IdDefEntidadDestino = Integer.parseInt(Textos.getCadena("diccionario", "adscripcion.id_def_destino"));

            String hql = "SELECT DISTINCT rel.iden " +
                    "FROM Relacion as rel, " +
                    "    Vectorrelacion as vrel " +
                    "WHERE (vrel.iddefvector = " + IdDefEntidadOrigen.toString() + " OR vrel.iddefvector = " + IdDefEntidadDestino.toString() + ") AND " +
                    "      rel.iddefrelacion = " + IdDefPropAdscripcion.toString() + " AND " +
                    "	  vrel.valor = " + idEnt + " AND " +
                    "      vrel.relacion = rel";
            List<Integer> lrels = this.em.createQuery(hql).getResultList();

            List<PropiedadesAdscripcion> res = new ArrayList<PropiedadesAdscripcion>();
            for (Integer idRel : lrels) {
                

                PropiedadesAdscripcion padsc = getPropiedadesAdscripcion(idRel);
                if (padsc != null) {
                    res.add(padsc);
                }
            }

            return res;

        } catch (Exception e) {
            log.fatal (e);
            e.printStackTrace();
            return null;
        }

    }
    
    private PropiedadesAdscripcion getPropiedadesAdscripcion(int iden) {
		PropiedadesAdscripcion aProps;
		String s;
		Query aQuery;
		List<Object[]> Resultado;

		try {
			Integer IdDefPropAdscripcion = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_definicion_adscripcion"));
			Integer IdDefEntidadOrigen = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_origen"));
			Integer IdDefEntidadDestino = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_destino"));
			Integer IdDefUnidad = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_unidad"));
			Integer IdDefTipo = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_tipo"));
			Integer IdDefCuantia = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_cuantia"));
			Integer IdDefTexto = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_texto"));

			s = "Select r.iden,"
					+ "(select valor from planeamiento.vectorrelacion where iddefvector ="
					+ IdDefEntidadOrigen
					+ " and idrelacion=r.iden ) as identidadorigen,"
					+ "(select valor from planeamiento.vectorrelacion where iddefvector ="
					+ IdDefEntidadDestino
					+ " and idrelacion=r.iden ) as identidaddestino,"
					+ "(select valor from planeamiento.vectorrelacion where iddefvector ="
					+ IdDefUnidad
					+ " and idrelacion=r.iden ) as idunidad,"
					+ "(select valor from planeamiento.vectorrelacion where iddefvector ="
					+ IdDefTipo
					+ " and idrelacion=r.iden ) as idtipo,"
					+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
					+ IdDefCuantia
					+ " and idrelacion=r.iden ) as cuantia,"
					+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
					+ IdDefTexto
					+ " and idrelacion=r.iden ) as texto "
					+ " From planeamiento.Relacion r, planeamiento.Vectorrelacion vr "
					+ " Where r.iddefrelacion="
					+ IdDefPropAdscripcion
					+ " And r.iden="
					+ iden
					+ " And vr.iddefvector="
					+ IdDefEntidadOrigen + " And vr.idrelacion=r.iden ";

			aQuery = em.createNativeQuery(s);

			Resultado = aQuery.getResultList();

			aProps = null;
			String Texto = "";
			if (Resultado.size() > 0) {
				for (Object[] aPropsAds : Resultado) {

					if (aPropsAds[6] != null) {
						Texto = aPropsAds[6].toString();
					} else {
						Texto = "";
					}
					aProps = new PropiedadesAdscripcion(
							Integer.parseInt(aPropsAds[0].toString()), em.find(
									Entidad.class, aPropsAds[1]), em.find(
									Entidad.class, aPropsAds[2]),
							Double.parseDouble(aPropsAds[5].toString()),
							em.find(Determinacion.class, aPropsAds[3]),
							em.find(Determinacion.class, aPropsAds[4]), Texto);
				}
			}

			return aProps;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
    
    private List<Ambito> IdAmbitos(Geometry aGeo) {
    	
        List aResultado;
        List<Ambito> aAmbitos=new ArrayList<Ambito>();
        Ambito aAmbito;

        aResultado = em.createNativeQuery("select idambito from diccionario.ambitoshp as o where Intersects (geom,GeomFromText('" + aGeo.toString() + "'))").getResultList();

        for (Object Iden : aResultado) {
            aAmbito = em.find(Ambito.class, Iden);
            if (aAmbito != null) {
                aAmbito.getLiteral().getTraduccions().size();
                aAmbitos.add(aAmbito);
            }
        }

        return aAmbitos;


    }
    
    private Capa findFromCodigoGrupo(String codigoDeterminacionGrupo) {
        try
        {
        	
        	String query = "SELECT capa "+ 
                     " FROM Capa capa "+
                     " JOIN capa.capagrupos cg "+ 
                     " WHERE cg.codigodeterminaciongrupo='"+codigoDeterminacionGrupo+"'";
                
                List<Capa> capas=em.createQuery(query).getResultList();
                if (capas.size()>0){
                    return capas.get(0);
                }
                else
                {
                    return null;
                }
        }catch (Exception ex){
            log.debug("Error accediendo a capa del grupo " + codigoDeterminacionGrupo + " : "+ ex);
            return null;
        }
    }
    
    public List<Entidad> findRefundido(String WKT,Ambito amb) {
        List<Entidad> aEntidades = new ArrayList();
        
        List aResultado;
        Entidad aEntidad;
        String aConsulta;

        try{
        	
        	// TODO FGA: Cambio la select por una que saque ficha de los datos agregados de tramite encargado mas tramite vigente
        	/* Original
            aConsulta = "select identidad from planeamiento.entidadpol as o "
                    + "   INNER JOIN planeamiento.entidad ent ON o.identidad = ent.iden "
                    + " where Intersects (geom,GeomFromText('" + WKT + "')) AND "
                    + " o.identidad in ("
                    + "       select iden "
                    + "       from planeamiento.entidad "
                    + "       where idtramite in ("
                    + " select (select iden from planeamiento.tramite where idplan=plan.iden and fechaconsolidacion is not null and idtipotramite=" + Textos.getCadena("diccionario", "tipotramite.refundidoautomatico") + " order by fechaconsolidacion DESC limit 1) as idultimotramite  "
                    + " from planeamiento.plan "
                    + " where plan.idambito=" + amb.getIden() + " and((select iden from planeamiento.tramite where idplan=plan.iden and fechaconsolidacion is not null and idtipotramite=" + Textos.getCadena("diccionario", "tipotramite.refundidoautomatico") + " order by fechaconsolidacion DESC limit 1) is not null) "
                    + " ORDER BY ent.clave))" 
                    + " UNION "
                    + " select identidad from planeamiento.entidadlin as l "
                    + "   INNER JOIN planeamiento.entidad ent ON l.identidad = ent.iden "
                    + " where Intersects (geom,ST_Buffer(GeomFromText('" + WKT + "'),5)) AND "
                    + " l.identidad in ("
                    + "       select iden "
                    + "       from planeamiento.entidad "
                    + "       where idtramite in ("
                    + " select (select iden from planeamiento.tramite where idplan=plan.iden and fechaconsolidacion is not null and idtipotramite=" + Textos.getCadena("diccionario", "tipotramite.refundidoautomatico") + " order by fechaconsolidacion DESC limit 1) as idultimotramite  "
                    + " from planeamiento.plan "
                    + " where plan.idambito=" + amb.getIden() + " and((select iden from planeamiento.tramite where idplan=plan.iden and fechaconsolidacion is not null and idtipotramite=" + Textos.getCadena("diccionario", "tipotramite.refundidoautomatico") + " order by fechaconsolidacion DESC limit 1) is not null) "
                    + " ORDER BY ent.clave))"
                    + " UNION "
                    + " select identidad from planeamiento.entidadpnt as p "
                    + "   INNER JOIN planeamiento.entidad ent ON p.identidad = ent.iden "
                    + " where Intersects (geom,ST_Buffer(GeomFromText('" + WKT + "'),5)) AND "
                    + " p.identidad in ("
                    + "       select iden "
                    + "       from planeamiento.entidad "
                    + "       where idtramite in ("
                    + " select (select iden from planeamiento.tramite where idplan=plan.iden and fechaconsolidacion is not null and idtipotramite=" + Textos.getCadena("diccionario", "tipotramite.refundidoautomatico") + " order by fechaconsolidacion DESC limit 1) as idultimotramite  "
                    + " from planeamiento.plan "
                    + " where plan.idambito=" + amb.getIden() + " and((select iden from planeamiento.tramite where idplan=plan.iden and fechaconsolidacion is not null and idtipotramite=" + Textos.getCadena("diccionario", "tipotramite.refundidoautomatico") + " order by fechaconsolidacion DESC limit 1) is not null) "
                    + " ORDER BY ent.clave))";
               */ 
        	
        	/* FGA Vuelvo a cambiar y solo dejo entidades para el plan seleccionado (tramite de trabajo)
        	 * 
        	 
        	int idTramiteVigente = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
        	int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
        	
        	 aConsulta = "select identidad from planeamiento.entidadpol as o "
                 + "   INNER JOIN planeamiento.entidad ent ON o.identidad = ent.iden "
                 + " where Intersects (geom,GeomFromText('" + WKT + "')) AND "
                 + " o.identidad in ("
                 + "       select iden "
                 + "       from planeamiento.entidad "
                 + "       where idtramite in ("
                 + " select (select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteVigente+" or idtramite="+idTramiteEncargado+")) as idultimotramite  "
                 + " from planeamiento.plan "
                 + " where plan.idambito=" + amb.getIden() + " and((select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteVigente+" or idtramite="+idTramiteEncargado+")) is not null) "
                 + " ORDER BY ent.clave))" 
                 + " UNION "
                 + " select identidad from planeamiento.entidadlin as l "
                 + "   INNER JOIN planeamiento.entidad ent ON l.identidad = ent.iden "
                 + " where Intersects (geom,ST_Buffer(GeomFromText('" + WKT + "'),5)) AND "
                 + " l.identidad in ("
                 + "       select iden "
                 + "       from planeamiento.entidad "
                 + "       where idtramite in ("
                 + " select (select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteVigente+" or idtramite="+idTramiteEncargado+")) as idultimotramite  "
                 + " from planeamiento.plan "
                 + " where plan.idambito=" + amb.getIden() + " and((select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteVigente+" or idtramite="+idTramiteEncargado+")) is not null) "
                 + " ORDER BY ent.clave))"
                 + " UNION "
                 + " select identidad from planeamiento.entidadpnt as p "
                 + "   INNER JOIN planeamiento.entidad ent ON p.identidad = ent.iden "
                 + " where Intersects (geom,ST_Buffer(GeomFromText('" + WKT + "'),5)) AND "
                 + " p.identidad in ("
                 + "       select iden "
                 + "       from planeamiento.entidad "
                 + "       where idtramite in ("
                 + " select (select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteVigente+" or idtramite="+idTramiteEncargado+")) as idultimotramite  "
                 + " from planeamiento.plan "
                 + " where plan.idambito=" + amb.getIden() + " and((select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteVigente+" or idtramite="+idTramiteEncargado+")) is not null) "
                 + " ORDER BY ent.clave))";
            
            */
        	
        	int idTramiteTrabajo = variablesSesionUsuario.getIdTramiteTrabajoActual();
        	
        	log.debug("[findRefundido] idTramiteTrabajo="+idTramiteTrabajo);
       	 aConsulta = "select identidad from planeamiento.entidadpol as o "
                + "   INNER JOIN planeamiento.entidad ent ON o.identidad = ent.iden "
                + " where Intersects (geom,GeomFromText('" + WKT + "')) AND "
                + " o.identidad in ("
                + "       select iden "
                + "       from planeamiento.entidad "
                + "       where idtramite in ("
                + " select (select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteTrabajo+")) as idultimotramite  "
                + " from planeamiento.plan "
                + " where plan.idambito=" + amb.getIden() + " and((select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteTrabajo+")) is not null) "
                + " ORDER BY ent.clave))" 
                + " UNION "
                + " select identidad from planeamiento.entidadlin as l "
                + "   INNER JOIN planeamiento.entidad ent ON l.identidad = ent.iden "
                + " where Intersects (geom,ST_Buffer(GeomFromText('" + WKT + "'),5)) AND "
                + " l.identidad in ("
                + "       select iden "
                + "       from planeamiento.entidad "
                + "       where idtramite in ("
                + " select (select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteTrabajo+")) as idultimotramite  "
                + " from planeamiento.plan "
                + " where plan.idambito=" + amb.getIden() + " and((select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteTrabajo+")) is not null) "
                + " ORDER BY ent.clave))"
                + " UNION "
                + " select identidad from planeamiento.entidadpnt as p "
                + "   INNER JOIN planeamiento.entidad ent ON p.identidad = ent.iden "
                + " where Intersects (geom,ST_Buffer(GeomFromText('" + WKT + "'),5)) AND "
                + " p.identidad in ("
                + "       select iden "
                + "       from planeamiento.entidad "
                + "       where idtramite in ("
                + " select (select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteTrabajo+")) as idultimotramite  "
                + " from planeamiento.plan "
                + " where plan.idambito=" + amb.getIden() + " and((select iden from planeamiento.tramite where idplan=plan.iden and (idtramite="+idTramiteTrabajo+")) is not null) "
                + " ORDER BY ent.clave))";
            
            aResultado = em.createNativeQuery(aConsulta).getResultList();

            for (Object Iden : aResultado) {
                aEntidad = em.find(Entidad.class, Iden);
                
                //TODO
                /* FGA Comentado
                if (aEntidad.getGrupo() != null) {
                    aEntidad.getGrupo().getNombre();
                }
                */
                aEntidades.add(aEntidad);
            }
            if (aEntidades.size()==0){
                log.info( "Sin entidades");
                log.info( aConsulta);
            }
        } catch (Exception ex) {
            log.error( ex);
            ex.printStackTrace();
        }

        return aEntidades;
    }
    
    public Entidad findForFicha(Object id) {
        Entidad aEntidad;
        Determinacion padre=null;
        
        aEntidad = em.find(Entidad.class, id);
        for (Entidaddeterminacion aCond : aEntidad.getEntidaddeterminacions()) {
            aCond.getDeterminacion().getNombre();
            aCond.getDeterminacion().setApartadoCompleto(aCond.getDeterminacion().getApartado());
            padre=aCond.getDeterminacion().getDeterminacionByIdpadre();
            while (padre!=null){
                aCond.getDeterminacion().setApartadoCompleto(padre.getApartado() + "." + aCond.getDeterminacion().getApartadoCompleto());
                padre=padre.getDeterminacionByIdpadre();
            }
            for (Casoentidaddeterminacion aCaso : aCond.getCasoentidaddeterminacions()) {
                for (Vinculocaso aVin : aCaso.getVinculocasosForIdcaso()) {
                    Casoentidaddeterminacion casoVin =aVin.getCasoentidaddeterminacionByIdcasovinculado();
                    casoVin.getEntidaddeterminacion().getEntidad().getNombre();
                }
                for (Vinculocaso aVin : aCaso.getVinculocasosForIdcasovinculado()) {
                    Casoentidaddeterminacion casoVin =aVin.getCasoentidaddeterminacionByIdcaso();
                    casoVin.getEntidaddeterminacion().getEntidad().getNombre();
                }
                for (Entidaddeterminacionregimen aReg : aCaso.getEntidaddeterminacionregimensForIdcaso()) {
                    aReg.getValor();
                    if (aReg.getOpciondeterminacion() != null) {
                        aReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getNombre();
                    }
                    for (Regimenespecifico aRegEsp : aReg.getRegimenespecificos()) {
                        if (aRegEsp.getRegimenespecifico() == null) {
                            aRegEsp.getTexto();
                            CargaRegEspHijos(aRegEsp);
                        }
                    }
                    if (aReg.getDeterminacion() != null) 
                    {
                        aReg.getDeterminacion().getNombre();
                        aReg.getDeterminacion().setApartadoCompleto(aReg.getDeterminacion().getApartado());
                        padre=aReg.getDeterminacion().getDeterminacionByIdpadre();
                        while (padre!=null){
                            aReg.getDeterminacion().setApartadoCompleto(padre.getApartado() + "." + aReg.getDeterminacion().getApartadoCompleto());
                            padre=padre.getDeterminacionByIdpadre();
                        }
                    }
                    if (aReg.getCasoentidaddeterminacionByIdcasoaplicacion() != null) 
                    {
                        aReg.getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getNombre();
                    }
                }
            }
        }
        return em.find(Entidad.class, id);
    }

    private void CargaRegEspHijos(Regimenespecifico aRegEsp) {
        for (Regimenespecifico Hijo : aRegEsp.getRegimenespecificos()) {
            Hijo.getTexto();
            CargaRegEspHijos(Hijo);
        }
    }


	
}
