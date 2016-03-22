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
package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.text.DecimalFormat;
import java.util.List;

import javax.ejb.Stateless;

import es.mitc.redes.urbanismoenred.data.fip.Adscripcion;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.ADSCRIPCIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.CONDICIONESURBANISTICAS;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.DETERMINACIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.DOCUMENTOS;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.ENTIDADES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.OPERACIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.UNIDADES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.OPERACIONES.OPERACIONESDETERMINACIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.OPERACIONES.OPERACIONESENTIDADES;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.PropiedadesUnidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.dal.GeneradorFipBean;
import es.mitc.redes.urbanismoenred.servicios.dal.GeneradorFipLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.PropiedadesAdscripcion;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GeneradorFipRefundido")
public class GeneradorFipRefundido extends GeneradorFipBean implements GeneradorFipLocal {

	/**
	 * 
	 */
	public GeneradorFipRefundido() {
	}
	
	/**
     * 
     * @param tramite
     * @return
     * @throws RedesException
     */
    @SuppressWarnings("unchecked")
	protected es.mitc.redes.urbanismoenred.data.fip.Tramite generar(Tramite tramite) throws RedesException {
        try {
        	getEm().flush();
        	getEm().refresh(tramite);
        	log.debug("Generando FIP para trámite " + tramite.getCodigofip());
        	es.mitc.redes.urbanismoenred.data.fip.Tramite resultado = new es.mitc.redes.urbanismoenred.data.fip.Tramite();

            DecimalFormat format4 = new DecimalFormat("000000");

            resultado.setCodigo(tramite.getCodigofip());
            resultado.setTipoTramite(format4.format(tramite.getIdtipotramite()));
            
            if (tramite.getTexto() != null && !tramite.getTexto().isEmpty()) {
            	resultado.setTEXTO(tramite.getTexto());
            }
            
            if (tramite.getDocumentos()!=null && tramite.getDocumentos().size() > 0) {
            	DOCUMENTOS documentos = new DOCUMENTOS();
            	// Se ha optado por no incluir los documentos del trámite.
                for (Documento documento : tramite.getDocumentos()) {
                	if (documento.getDocumentocasos().size() > 0 || 
                			documento.getDocumentodeterminacions().size() > 0 || 
                			documento.getDocumentoentidads().size() > 0) {
                		documentos.getDOCUMENTO().add(generar(documento));
                	}
                }
                
                if (documentos.getDOCUMENTO().size() > 0)
                	resultado.setDOCUMENTOS(documentos);
            }

            List<Entidad> entidades = getEm().createNamedQuery("Entidad.buscarRaiz")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            if (entidades.size() > 0) {
                ENTIDADES entidadesXml = new ENTIDADES();
				resultado.setENTIDADES(entidadesXml);
                for (Entidad entidad : entidades) {
                    entidadesXml.getENTIDAD().add(generar(entidad));
                }
            }

            List<Determinacion> determinaciones = getEm().createNamedQuery("Determinacion.buscarRaiz")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            if (determinaciones.size() > 0) {
                DETERMINACIONES determinacionesXml = new DETERMINACIONES();
                resultado.setDETERMINACIONES(determinacionesXml);
                
                for (Determinacion determinacion : determinaciones) {
                   determinacionesXml.getDETERMINACION().add(generar(determinacion));
                }
            }

            entidades = getEm().createNamedQuery("Entidad.buscarPorTramite")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            
            //Condiciones Urbanísticas
            if (entidades.size() > 0) {
                CONDICIONESURBANISTICAS cus = new CONDICIONESURBANISTICAS();
                for (Entidad entidad : entidades) {
                	getEm().refresh(entidad);
                	for (Entidaddeterminacion ed : entidad.getEntidaddeterminacions()) {
                		getEm().refresh(ed);
                        cus.getCONDICIONURBANISTICA().add(generar(ed));
                    }
                }
                
                if (cus.getCONDICIONURBANISTICA().size() > 0) {
                	resultado.setCONDICIONESURBANISTICAS(cus);
                }
            }

            // No se está sincronizando adecuadamente el trámite, por lo que 
            // tengo que hacer esta consulta en vez de navegar por el trámite
            List<Operacion> operaciones = getEm().createNamedQuery("Operacion.buscarPorTramite")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            if (operaciones.size() > 0) {
            	OPERACIONES operacionesXML = new OPERACIONES();
            	
                for (Operacion operacion : operaciones) {
                    if (operacion.getOperaciondeterminacions().size() > 0) {
                        OPERACIONESDETERMINACIONES od = new OPERACIONESDETERMINACIONES();
                        for (Operaciondeterminacion opDet : operacion.getOperaciondeterminacions()) {
                        	if (getEm().contains(opDet))
                        		od.getOPERACIONDETERMINACION().add(generar(opDet));
                        }
                        if (od.getOPERACIONDETERMINACION().size() > 0) {
                        	operacionesXML.getOPERACIONESDETERMINACIONES().add(od);
                        }
                    }
                    
                    if (operacion.getOperacionentidads().size() > 0) {
                        OPERACIONESENTIDADES oe = new OPERACIONESENTIDADES();
                        for (Operacionentidad opEnt : operacion.getOperacionentidads()) {
                        	if (getEm().contains(opEnt))
                        		oe.getOPERACIONENTIDAD().add(generar(opEnt));
                        }
                        if (oe.getOPERACIONENTIDAD().size() > 0) {
                        	operacionesXML.getOPERACIONESENTIDADES().add(oe);
                        }
                    }
                    if (operacion.getOperacionrelacions().size() > 0) {
                    	OPERACIONESENTIDADES oe = new OPERACIONESENTIDADES();
                        for (Operacionrelacion opRel : operacion.getOperacionrelacions()) {
                        	if (getEm().contains(opRel))
                        		oe.getOPERACIONENTIDAD().add(generar(opRel));
                        }
                        if (oe.getOPERACIONENTIDAD().size() > 0) {
                        	operacionesXML.getOPERACIONESENTIDADES().add(oe);
                        }
                    }
                }
            }
        
            //Unidades
            determinaciones = getEm().createNamedQuery("Determinacion.buscarPorTramite")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            if (determinaciones.size() > 0) {
                UNIDADES unidades = new UNIDADES();
                for (Determinacion determinacion : determinaciones) {
                	PropiedadesUnidad propiedades = getServicioPlaneamiento().getPropiedadesUnidad(determinacion.getIden());
                	if (propiedades != null) {
                		unidades.getUNIDAD().add(generar(propiedades));
                	}
                }
                if (unidades.getUNIDAD().size() > 0) {
                    resultado.setUNIDADES(unidades);
                }
            }
            String origen = "";
            String destino = "";
            PropiedadesAdscripcion[] adscripciones = getServicioPlaneamiento().getPropiedadesAdscripcionTramite(tramite.getIden());
            if (adscripciones.length > 0) {
            	ADSCRIPCIONES adscripcionesXml = new ADSCRIPCIONES();
                
                for (PropiedadesAdscripcion aAdscr : adscripciones) {
                    Adscripcion adscripcion = new Adscripcion();
                    
                    origen = aAdscr.getOrigen().getCodigo();
                    destino = aAdscr.getDestino().getCodigo();
                    if (origen != null) {
                        adscripcion.setEntidadOrigen(origen);
                    }
                    if (destino != null) {
                    	adscripcion.setEntidadDestino(destino);
                    }
                    adscripcion.setPROPIEDADES(generar(aAdscr));
                    
                    adscripcionesXml.getADSCRIPCION().add(adscripcion);
                }
            }

            return resultado;
        } catch (Exception ex) {
        	ex.printStackTrace();
        	throw new RedesException("Error al generar FIP: " + ex.getMessage());
        }
    }
}
